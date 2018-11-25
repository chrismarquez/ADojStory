package com.dogstories.Services

import com.dogstories.Infrastructure.Inject
import com.dogstories.Interfaces.ICodeGenerator
import com.dogstories.Interfaces.IParagraphParser
import com.dogstories.Models.*
import java.lang.IllegalStateException

class ParagraphParser : IParagraphParser {

    private val codeGen: ICodeGenerator by Inject.get()

    private var className = ""
    private var pendingMethods = arrayListOf<String>()

    private val value = "(\"(\\w( )*)+\"|[0-9]+(.[0-9]+)?)+"
    private val comma = "(, )*"
    private val fields = mutableListOf<String>()

    override fun parseParagraph(paragraph: String): String {
        val sentences = paragraph.split(".")
        for (sentence in sentences) matchPattern(sentence.trim())
        val uglyGen = codeGen.buildObject(className)
        fields.clear()
        return Beautifier.beautify(uglyGen)
    }

    private fun matchPattern(sentence: String): Any = when {
        sentence.isEmpty() -> ""
        "its (\\w+) number ([0-9]+) be $value".toRegex().matches(sentence) -> assignIntoGlobalArray(sentence)
        "(\\w+) number ([0-9]+) be $value".toRegex().matches(sentence) -> assignIntoLocalArray(sentence)
        "[Tt]here is a \\w+".toRegex().containsMatchIn(sentence) -> createObject(sentence)
        "[Ii]t has many (\\w+)".toRegex().containsMatchIn(sentence) -> declareGlobalArray(sentence)
        "[Ii]ts (\\w+) are ($value$comma)+".toRegex().matches(sentence) -> assignGlobalArray(sentence)
        "(\\w+) are ($value$comma)+".toRegex().matches(sentence) -> assignGlobalArray(sentence)
        "[Ii]t has \\w+ of $value".toRegex().matches(sentence) -> addField(sentence)
        "[Ii]ts \\w+ be $value".toRegex().matches(sentence) -> modifyField(sentence)
        "[Ii]t can (\\w+$comma)+".toRegex().containsMatchIn(sentence) -> declareMethods(sentence)
        "[Tt]o (\\w+): (.(?!,\$))+".toRegex().containsMatchIn(sentence) -> defineMethod(sentence)
        "[Ll]et \\w+ be $value".toRegex().matches(sentence) -> declareLocalVariable(sentence)
        "\\w+ be $value".toRegex().matches(sentence) -> modifyLocalVariable(sentence)
        "[Ii]t uses \\w+( with ($value+$comma)+)?".toRegex().matches(sentence) -> callMethod(sentence)
        "[Ii]t prints ((\")?\\w+(\")?)".toRegex().matches(sentence) -> printExpression(sentence)
        "(increase|decrease) its (\\w+) by ([0-9]+)".toRegex().matches(sentence) -> mutateGlobal(sentence)
        "(increase|decrease) (\\w+) by ([0-9]+)".toRegex().matches(sentence) -> mutateLocal(sentence)
        else -> throw IllegalArgumentException("No match found: $sentence")
    }

    private fun declareGlobalArray(sentence: String) {
        val splitted = sentence.split(" ")
        val name = splitted[3]
        codeGen.genField(name, "[]")
    }

    private fun createObject(sentence: String) {
        val splitted = sentence.split(" ")
        var className = ""
        for (i in 3 until splitted.size) className += splitted[i]
        this.className = className
    }

    private fun printExpression(sentence: String) {
        val splitted = sentence.split(" ", ", ")
        val printable = splitted[2]
        codeGen.genStatement(
            Expression(
                type = Type.PRINT,
                data = Definition(printable)
            )
        )
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
        codeGen.genStatement(
            Expression(
                type = Type.ASSIGN_GLOBAL,
                data = Assignment(key, value)
            )
        )
        return "$key,$value"
    }

    private fun defineMethod(sentence: String): List<String> {
        val splitted = sentence.split(": ", ":")
        val methodName = splitted[0].split(" ")[1]
        val statements: List<String> = splitted[1].split("; ", ";")
        if (this.pendingMethods.find { it == methodName } != null) this.pendingMethods.remove(methodName)

        // TODO: Add Statements to Match Pattern function to run this loop
        for (statement in statements) matchPattern(statement)

        codeGen.genMethod(methodName, listOf())
        // Returning statements for unit testing
        return statements
    }

    private fun declareLocalVariable(sentence: String): String {
        val splitted = sentence.split(" ")
        val key = splitted[1]
        var value = ""
        for (i in 3 until splitted.size) value += splitted[i] + if (i != splitted.size - 1) " " else ""

        codeGen.genStatement(
            Expression(
                type = Type.CREATE_VAR,
                data = Assignment(key, value)
            )
        )

        // Returning local variable key-value pairs for unit testing
        return "$key,$value"
    }

    private fun modifyLocalVariable(sentence: String): String {
        val splitted = sentence.split(" ")
        if (splitted.size != 3) throw IllegalArgumentException("Modify local variable malformed: $sentence")
        val key = splitted[0]
        val value = splitted[2]

        codeGen.genStatement(
            Expression(
                type = Type.ASSIGN_LOCAL,
                data = Assignment(key, value)
            )
        )
        // Returning local variable key-value pairs for unit testing
        return "$key,$value"
    }

    private fun callMethod(sentence: String): String {
        val splitted = sentence.split(" ", ", ", ",")
        val method = splitted[2]
        var args = mutableListOf<String>()

        if (splitted.size != 3) {
            for (i in 4 until splitted.size) args.add(splitted[i])
        }

        codeGen.genStatement(
            Expression(
                type = if (args.size == 0) Type.USE_METHOD else Type.USE_METHOD_PARAMS,
                data = MethodCall(method, args)
            )
        )
        // Returning local variable key-value pairs for unit testing
        return "$method($args)"
    }

    private fun assignIntoGlobalArray(sentence: String) {
        val splitted = sentence.split(" ", ", ", ",")
        val name = splitted[1]
        val index = splitted[3].toInt() - 1
        val value = splitted[5]

        codeGen.genStatement(
            Expression(
                type = Type.ASSIGN_GLOBAL_ARR_NUM,
                data = ArrayAssignment(name, index, value)
            )
        )
    }

    private fun assignIntoLocalArray(sentence: String) {
        val splitted = sentence.split(" ", ", ", ",")
        val name = splitted[0]
        val index = splitted[2].toInt() - 1
        val value = splitted[4]

        codeGen.genStatement(
            Expression(
                type = Type.ASSIGN_ARR_NUM,
                data = ArrayAssignment(name, index, value)
            )
        )
    }

    private fun assignLocalArray(sentence: String) {
        val splitted = sentence.split(" ", ", ", ",")
        val name = splitted[0]
        val list = mutableListOf<String>()
        for (i in 2 until splitted.size) list.add(splitted[i])

        codeGen.genStatement(
            Expression(
                type = Type.ASSIGN_ARR,
                data = MethodCall(name, list)
            )
        )
    }

    private fun assignGlobalArray(sentence: String) {
        val splitted = sentence.split(" ", ", ", ",")
        val name = splitted[1]
        val list = mutableListOf<String>()
        for (i in 3 until splitted.size) list.add(splitted[i])

        codeGen.genStatement(
            Expression(
                type = Type.ASSIGN_GLOBAL_ARR,
                data = MethodCall(name, list)
            )
        )
    }

    private fun mutateLocal(sentence: String) {
        val splitted = sentence.split(" ")
        val modifier = splitted[0]
        val value = splitted[3].toInt()
        val name = splitted[1]
        codeGen.genStatement(
            Expression(
                type = if (modifier == "increase") Type.INCREASE_VAR_BY_VAL else Type.INCREASE_VAR_BY_VAL,
                data = Mutation(name, value)
            )
        )
    }

    private fun mutateGlobal(sentence: String) {
        val splitted = sentence.split(" ")
        val modifier = splitted[0]
        val name = splitted[2]
        val value = splitted[4].toInt()
        codeGen.genStatement(
            Expression(
                type = if (modifier == "increase") Type.INCREASE_GLOBAL_BY_VAL else Type.DECREASE_GLOBAL_BY_VAL,
                data = Mutation(name, value)
            )
        )
    }
}