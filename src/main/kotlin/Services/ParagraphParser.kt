package Services

import Infrastructure.Inject
import Interfaces.ICodeGenerator
import Interfaces.IParagraphParser
import java.lang.IllegalStateException

class ParagraphParser : IParagraphParser {

    private val codeGen: ICodeGenerator by Inject.get()

    private var className = ""
    private var pendingMethods = arrayListOf<String>()

    private val value = "(\"\\w+\"|[0-9]+(.[0-9]+)?)"
    private val comma = "(, )*"
    private val fields = mutableListOf<String>()

    override fun parseParagraph(paragraph: String): String {
        val sentences = paragraph.split(".")
        for (sentence in sentences) matchPattern(sentence.trim())
        val uglyGen = codeGen.buildObject(className)
        return Beautifier.beautify(uglyGen)
    }

    private fun matchPattern(sentence: String): Any = when {
        sentence.isEmpty() -> ""
        "There is a \\w+".toRegex().containsMatchIn(sentence) -> createObject(sentence)
        "It has \\w+ of $value".toRegex().matches(sentence) -> addField(sentence)
        "its \\w+ be $value".toRegex().matches(sentence) -> modifyField(sentence)
        "It can (\\w+$comma)+".toRegex().containsMatchIn(sentence) -> declareMethods(sentence)
        "To (\\w+)(,+) (.(?!,\$))+".toRegex().containsMatchIn(sentence) -> defineMethod(sentence)
        "let \\w+ be $value".toRegex().matches(sentence) -> declareLocalVariable(sentence)
        "\\w+ be $value".toRegex().matches(sentence) -> modifyLocalVariable(sentence)
        "it uses \\w+( with ($value+$comma)+)?".toRegex().matches(sentence) -> callMethod(sentence)
        else -> throw IllegalArgumentException("No match found: $sentence")
    }

    private fun createObject(sentence: String) {
        val splitted = sentence.split(" ")
        var className = ""
        for (i in 3 until splitted.size) className += splitted[i]
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

        // Handler should be in the codeGen?
        // codeGen.genField(key, value)
        return "$key,$value"
    }

    private fun defineMethod(sentence: String): List<String> {
        val splitted = sentence.split(", ", ",")
        val methodName = splitted[0].split(" ")[0]
        val statements: List<String> = splitted[1].split("; ", ";")
        if (this.pendingMethods.find { it == methodName } != null) this.pendingMethods.remove(methodName)

        // TODO: Add Statements to Match Pattern function to run this loop
        // TODO: Send correct call to Code Gen
        // codeGen.genMethod()
        for (statement in statements) matchPattern(statement)
        // Returning statements for unit testing
        return statements
    }

    private fun declareLocalVariable(sentence: String): String {
        val splitted = sentence.split(" ")
        if (splitted.size != 4) throw IllegalArgumentException("Declare local variable malformed: $sentence")
        val key = splitted[1]
        val value = splitted[3]

        // TODO: Send correct call to Code Gen
        // codeGen.genStatement()
        // Returning local variable key-value pairs for unit testing
        return "$key,$value"
    }

    private fun modifyLocalVariable(sentence: String): String {
        val splitted = sentence.split(" ")
        if (splitted.size != 3) throw IllegalArgumentException("Modify local variable malformed: $sentence")
        val key = splitted[0]
        val value = splitted[2]

        // TODO: Send correct call to Code Gen
        // codeGen.genStatement()
        // Returning local variable key-value pairs for unit testing
        return "$key,$value"
    }

    private fun callMethod(sentence: String): String {
        val splitted = sentence.split(" ", ", ", ",")
        val method = splitted[2]
        var args = mutableListOf<String>()

        // println(splitted.size)
        if (splitted.size != 3) {
            for (i in 4..(splitted.size - 1)) args.add(splitted[i])
        }

        // TODO: Send correct call to Code Gen
        // codeGen.genStatement()
        // Returning local variable key-value pairs for unit testing
        return "$method($args)"
    }
}