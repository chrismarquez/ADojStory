package Services

import Interfaces.ICodeGenerator
import Interfaces.IParagraphParser
import java.lang.IllegalStateException

class ParagraphParser(
    private val codeGen: ICodeGenerator
): IParagraphParser {

    private var className = ""

    private val value = "(\"\\w\"|[0-9]+(.[0-9]+)?)"
    private val fields = mutableListOf<String>()

    override fun parseParagraph(paragraph: String): String {
        val sentences = paragraph.split("\\.")
        for (sentence in sentences) matchPattern(sentence.trim())
        return codeGen.buildObject(className)
    }

    private fun matchPattern(sentence: String) = when {
        "There is a \\w".toRegex().containsMatchIn(sentence) -> createObject(sentence)
        "It has \\w of $value".toRegex().matches(sentence) -> addField(sentence)
        else -> throw IllegalArgumentException("No match found")
    }

    private fun createObject(sentence: String) {
        val splitted = sentence.split(" ")
        if (splitted.size < 4) throw IllegalArgumentException("Create expression malformed: $sentence")
        var className = ""
        for (i in 3..splitted.size) className += splitted[i]
        this.className = className
    }

    private fun addField(sentence: String): String {
        val splitted = sentence.split(" ")
        if (splitted.size != 5) throw IllegalArgumentException("Add field malformed: $sentence")
        val key = splitted[2]
        val value = splitted[4]
        if (fields.find { it == key } != null) throw IllegalStateException("Duplicate field: $key")
        fields.add(key)
        codeGen.genField(key, value)
        return "$key,$value"
    }

}