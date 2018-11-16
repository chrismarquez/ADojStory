package Services

import Interfaces.ICodeGenerator
import Interfaces.IParagraphParser

class ParagraphParser(
    private val codeGen: ICodeGenerator
): IParagraphParser {

    private var className = ""
    private var sentences = listOf<String>()

    override fun parseParagraph(paragraph: String): String {
        sentences = paragraph.split("\\.")
        for (sentence in sentences) matchPattern(sentence)
        return codeGen.buildObject(className)
    }


    fun matchPattern(sentence: String) {
        val regex = sentence.toRegex()
        when {
            regex.containsMatchIn("There") -> createObject(sentence)
            else -> ""
        }
    }

    private fun createObject(sentence: String) {
        val splitted = sentence.split(" ")
        if (splitted.size < 4) throw IllegalArgumentException("Create expression malformed: $sentence")
        var className = ""
        for (i in 3..splitted.size) className += splitted[i]
        this.className = className
    }

}