package Services

import Interfaces.ICodeGenerator
import Models.Expression
import org.junit.Assert
import org.junit.jupiter.api.Test
import kotlin.reflect.jvm.isAccessible

internal class ParagraphParserTest {

    private val parser = ParagraphParser()

    @Test
    fun parseParagraph() {
        val dogCode = javaClass.classLoader.getResource("Dog.dog").readText()
        println(dogCode)
        val jsCode = ""//parser.parseParagraph(dogCode)
        val expected = javaClass.classLoader.getResource("Dog.js").readText()
        println("Expected: $expected")
        println("Actual: $jsCode")
        //Assert.assertEquals(expected, jsCode)
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

    @Test
    fun modifyField() {
        val test1 = "its age be 8"
        val test2 = "its name be \"Bob\""
        val modifiyFieldMethod = ParagraphParser::class.members.find { it.name == "modifyField" }
        modifiyFieldMethod?.let {
            it.isAccessible = true
            val field1 = it.call(parser, test1) as String
            val field2 = it.call(parser, test2) as String
            Assert.assertEquals("age,8", field1)
            Assert.assertEquals("name,\"Bob\"", field2)
        }
    }

    @Test
    fun declareMethods() {
        val test1 = "It can run, sleep, bark"
        val declareMethodsCallable = ParagraphParser::class.members.find { it.name == "declareMethods" }
        declareMethodsCallable?.let {
            it.isAccessible = true
            val field1 = it.call(parser, test1) as List<*>
            val array1: List<String> = listOf("run","sleep","bark")
            Assert.assertEquals(array1, field1)
        }
    }

    @Test
    fun defineMethod() {
        val test1 = "To bark, its age be 8;let volume be \"Loud\""
        val test2 = "To sleep,bedtime be \"Now\""
        val defineMethodCallable = ParagraphParser::class.members.find { it.name == "defineMethod" }
        defineMethodCallable?.let {
            it.isAccessible = true
            val field1 = it.call(parser, test1) as List<*>
            val field2 = it.call(parser, test2) as List<*>
            val array1: List<String> = listOf("its age be 8","let volume be \"Loud\"")
            val array2: List<String> = listOf("bedtime be \"Now\"")
            Assert.assertEquals(array1, field1)
            Assert.assertEquals(array2, field2)
        }
    }

    @Test
    fun declareLocalVariable() {
        val test1 = "let day be \"Today\""
        val test2 = "let count be 7"
        val declareLocalVariableMethod = ParagraphParser::class.members.find { it.name == "declareLocalVariable" }
        declareLocalVariableMethod?.let {
            it.isAccessible = true
            val field1 = it.call(parser, test1) as String
            val field2 = it.call(parser, test2) as String
            Assert.assertEquals("day,\"Today\"", field1)
            Assert.assertEquals("count,7", field2)
        }
    }

    @Test
    fun modifyLocalVariable() {
        val test1 = "day be \"Yesterday\""
        val test2 = "count be 10"
        val modifyLocalVariableMethod = ParagraphParser::class.members.find { it.name == "modifyLocalVariable" }
        modifyLocalVariableMethod?.let {
            it.isAccessible = true
            val field1 = it.call(parser, test1) as String
            val field2 = it.call(parser, test2) as String
            Assert.assertEquals("day,\"Yesterday\"", field1)
            Assert.assertEquals("count,10", field2)
        }
    }

    @Test
    fun callMethod() {
        val test1 = "it uses sleep"
        val test2 = "it uses bark with 5, \"Loud\",3.12"
        val callMethodCallable = ParagraphParser::class.members.find { it.name == "callMethod" }
        callMethodCallable?.let {
            it.isAccessible = true
            val field1 = it.call(parser, test1) as String
            val field2 = it.call(parser, test2) as String
            Assert.assertEquals("sleep([])", field1)
            Assert.assertEquals("bark([5, \"Loud\", 3.12])", field2)
        }
    }
}