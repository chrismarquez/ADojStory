package Services

import Interfaces.ICodeGenerator
import Interfaces.IParagraphParser
import java.lang.IllegalStateException

class ParagraphParser(
    private val codeGen: ICodeGenerator
): IParagraphParser {

    private var className = ""
    private var pendingMethods = arrayListOf<String>()

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
        "its \\w be $value".toRegex().matches(sentence) -> modifyField(sentence)
        "It can (\\w+(, )*)*".toRegex().containsMatchIn(sentence) -> declareMethods(sentence)
        "To (\\w*)(,+) (.(?!,\$))+".toRegex().containsMatchIn(sentence) -> defineMethod(sentence)
        else -> throw IllegalArgumentException("No match found")
    }

    private fun createObject(sentence: String) {
        val splitted = sentence.split(" ")
        var className = ""
        for (i in 3..splitted.size) className += splitted[i]
        this.className = className
    }

    private fun declareMethods(sentence: String): List<String> {
        val splitted = sentence.split(" ", ", ")
        for (i in 2..(splitted.size - 1)) this.pendingMethods.add(splitted[i])
        // Returning pending methods for unit testing
        return this.pendingMethods
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

    private fun modifyField(sentence: String): String{
        val splitted = sentence.split(" ")
        if (splitted.size != 4) throw IllegalArgumentException("Modify field malformed: $sentence")
        val key = splitted[1]
        val value = splitted[3]
        if (fields.find { it == key } == null) fields.add(key)
        codeGen.genField(key, value)
        return "$key,$value"
    }

    private fun defineMethod(sentence: String): List<String> {
        val splitted = sentence.split(", ", ",")
        val statements: List<String> = splitted[1].split("; ", ";")
        // TODO: Add Statements to Match Pattern function to run this loop
        // for (statement in statements) matchPattern(statement)
        // Returning statements for unit testing
        return statements
    }

}