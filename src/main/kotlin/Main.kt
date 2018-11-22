
import Infrastructure.Inject
import Interfaces.IOrchestrator
import java.io.File


fun main() {

    val orchestrator: IOrchestrator by Inject.get()


    val fileToRead = File("ExampleDog.dog")

    val dataParsed: ArrayList<String> = orchestrator.parse(fileToRead)
    println("--- Example data parsed ---")
    var cont = 1
    for (js in dataParsed) {
        println("------ JS $cont ------")
        println(js)
        cont++
    }

}