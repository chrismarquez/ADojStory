package Services

import Interfaces.ICodeGenerator
import Models.Expression
import org.junit.Assert
import org.junit.jupiter.api.Test
import kotlin.reflect.jvm.isAccessible

internal class ParagraphParserTest {

    private val parser = ParagraphParser(object : ICodeGenerator {
        override fun genField(key: String, value: String): ICodeGenerator = this
        override fun genStatement(expression: Expression, varName: String?): ICodeGenerator = this
        override fun genMethod(name: String, args: List<String>): ICodeGenerator = this
        override fun buildObject(name: String): String = ""

    })

    @Test
    fun parseParagraph() {
        val dogCode = javaClass.getResource("Dog.dog").file
        println(dogCode)
        val jsCode = ""//parser.parseParagraph(dogCode)
        val expected = javaClass.getResource("Dog.js").file
        println("Expected: $expected")
        println("Actual: $jsCode")
        Assert.assertEquals(expected, jsCode)
    }

    @Test
    fun matchPattern() {
    }

    @Test
    fun addField() {
        val test1 = "It has age of 5"
        val test2 = "It has name of \"Miguel\""
        val addFieldMethod = ParagraphParser::class.members.find { it.name == "addField" }
        addFieldMethod?.let {
            it.isAccessible = true
            val field1 = it.call(parser, test1) as String
            val field2 = it.call(parser, test2) as String
            Assert.assertEquals("age,5", field1)
            Assert.assertEquals("name,\"Miguel\"", field2)
        }
    }
}