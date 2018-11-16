package Services

import Interfaces.ICodeGenerator
import Interfaces.IParagraphParser

class ParagraphParser(
    private val codeGen: ICodeGenerator
): IParagraphParser {

    private var className = ""
    private var pendingMethdos = arrayListOf<String>()

    override fun parseParagraph(paragraph: String): String {
        val sentences = paragraph.split("\\.")
        for (sentence in sentences) matchPattern(sentence)
        return codeGen.buildObject(className)
    }


    private fun matchPattern(sentence: String) = when {
        "There is a [a-zA-Z ]+".toRegex().containsMatchIn(sentence) -> createObject(sentence)
        "It can (\\w+(, )*)*".toRegex().containsMatchIn(sentence) -> declareMethods(sentence)
        else -> throw IllegalArgumentException("No match found")
    }

    private fun createObject(sentence: String) {
        val splitted = sentence.split(" ")
        var className = ""
        for (i in 3..splitted.size) className += splitted[i]
        this.className = className
    }

    private fun declareMethods(sentence: String) {
        val splitted = sentence.split(" ")
        for (i in 2..splitted.size) this.pendingMethdos.add(splitted[i].replace(",", ""))
    }
}