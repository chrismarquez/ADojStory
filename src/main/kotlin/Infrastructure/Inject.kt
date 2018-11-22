package Infrastructure

import Interfaces.ICodeGenerator
import Interfaces.IOrchestrator
import Interfaces.IParagraphParser
import Models.Expression
import Services.ParagraphParser
import org.kodein.di.Kodein
import org.kodein.di.KodeinProperty
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import java.io.File

object Inject {

    val injector = Kodein {
        bind<ICodeGenerator>() with provider { object : ICodeGenerator {
            override fun genField(key: String, value: String): ICodeGenerator = this
            override fun genStatement(expression: Expression, varName: String?): ICodeGenerator = this
            override fun genMethod(name: String, args: List<String>): ICodeGenerator = this
            override fun buildObject(name: String): String = ""
        }}
        bind<IParagraphParser>() with provider { ParagraphParser() }
        bind<IOrchestrator>() with singleton { object : IOrchestrator {
            override fun parse(inputFile: File): File {
                val dogCode = inputFile.readText()
                dogCode.split("\n").forEach {
                    val parser: IParagraphParser by get()
                    val text = parser.parseParagraph(it)
                }
                return File.createTempFile("dog", "js")
            }
        }}
    }

    inline fun <reified T: Any> get(): KodeinProperty<T> = injector.instance()
}