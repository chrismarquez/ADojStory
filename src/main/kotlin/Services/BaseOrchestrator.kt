package Services
import Infrastructure.Inject
import Interfaces.IOrchestrator
import Interfaces.IParagraphParser
import java.io.File
import java.io.FileReader


class BaseOrchestrator : IOrchestrator {

    private val paragraphParser: IParagraphParser by Inject.get()

    override fun parse(inputFile: File): ArrayList<String> {
        val paragraphs : ArrayList<String> = parseParagraphs(inputFile.readText())
        val paragraphsResult : ArrayList<String> = ArrayList()
        for (paragraph in paragraphs) {
            paragraphsResult.add(paragraphParser.parseParagraph(paragraph))
        }
        return paragraphsResult
    }

    private fun parseParagraphs(textBody: String): ArrayList<String> {
        val result: ArrayList<String> = arrayListOf()
        if(textBody.isEmpty()) {
            println("Nothing to parse!")
            return result
        }


        var paragraph: String = ""
        for(i in textBody.indices) {
            val value = textBody[i]
            if(value == '\n' && paragraph.isNotEmpty()) {
                if(i < textBody.length - 1 && textBody[i+1] == '\n' && paragraph.isNotEmpty()) {
                    result.add(paragraph)
                    paragraph = ""
                }
            } else {
                if(value == '\n') continue
                paragraph += value
            }
        }
        result.add(paragraph)
        return result
    }

    private fun parseFile(file: File): String {
        var result: String = ""
        FileReader(file).use {
            var character = it.read()
            while(character != -1) {
                result += character.toChar()
                character = it.read()
            }
        }
        return result
    }

}
