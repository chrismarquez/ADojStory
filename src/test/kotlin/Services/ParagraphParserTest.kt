package Services

import Interfaces.IParagraphParser
import org.junit.Assert
import org.junit.jupiter.api.Test

internal class ParagraphParserTest {

    //private val parser: IParagraphParser = ParagraphParser()

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
}