import Interfaces.ICodeGenerator
import Interfaces.IOrchestrator
import Interfaces.IParagraphParser
import Services.BaseOrchestrator
import Services.CodeGen
import Services.ParagraphParseExample
import Services.ParagraphParser
import com.apple.eio.FileManager
import java.io.File


fun main() {
    val codeGenerator : ICodeGenerator = CodeGen()
    val paragraphParser : IParagraphParser = ParagraphParseExample()
    val orchestrator : IOrchestrator = BaseOrchestrator(paragraphParser)


    val fileToRead: File = File("ExampleDog.dog")

    val dataParsed: ArrayList<String> = orchestrator.parse(fileToRead)
    println("--- Example data parsed ---")
    var cont = 1
    for (js in dataParsed) {
        println("------ JS $cont ------")
        println(js)
        cont++
    }

}