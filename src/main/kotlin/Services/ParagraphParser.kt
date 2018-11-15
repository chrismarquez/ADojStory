package Services

import Interfaces.ICodeGenerator
import Interfaces.IParagraphParser
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import java.lang.Exception

class ParagraphParser(
    private val codeGen: ICodeGenerator
): IParagraphParser {

    private var sentences = listOf<String>()
    private val deferred = mutableListOf<Deferred<String>>()

    override fun parseParagraph(paragraph: String): String {
        sentences = paragraph.split("\\.")
        val code = runBlocking { spawnGenerators() }
        return code
    }

    suspend fun spawnGenerators(): String = coroutineScope {
        for (sentence in sentences) {
            val result = async { matchPattern(sentence) }
            deferred.add(result)
        }
        val values = deferred.map { it.await() }
        values.joinToString("\n")
    }

    fun matchPattern(sentence: String): String = when(sentence) {
        "" -> ""
        else -> throw Exception("Pattern not found")
    }


}