import Infrastructure.Inject
import Interfaces.ICodeGenerator
import Interfaces.IOrchestrator
import Interfaces.IParagraphParser
import Services.BaseOrchestrator
import Services.ParagraphParser
import java.io.File


fun main() {
    val codeGenerator : ICodeGenerator by Inject.get()
    val paragraphParser : IParagraphParser by Inject.get()
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