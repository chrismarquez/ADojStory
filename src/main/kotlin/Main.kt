import Interfaces.ICodeGenerator
import Interfaces.IOrchestrator
import Interfaces.IParagraphParser
import Services.BaseOrchestrator
import Services.CodeGen
import Services.ParagraphParser
import java.io.File

fun main() {
    val codeGenerator : ICodeGenerator = CodeGen()
    val paragraphParser : IParagraphParser = ParagraphParser(codeGenerator)
    val orchestrator : IOrchestrator = BaseOrchestrator(paragraphParser)

    val fileToRead: File = File("ExampleDog.dog")

    println("--- Example ---")
    println(orchestrator.parse(fileToRead))

}