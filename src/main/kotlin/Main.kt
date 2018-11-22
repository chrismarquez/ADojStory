
import Infrastructure.Inject
import Interfaces.IOrchestrator
import com.gui.view.OrchestatorView
import tornadofx.App
import tornadofx.launch
import java.io.File

class MyApp: App(OrchestatorView::class){}

fun main(args: Array<String>) {
    launch<MyApp>(args)
    /*val orchestrator: IOrchestrator by Inject.get()
    val fileToRead = File("ExampleDog.dog")
    val dataParsed: ArrayList<String> = orchestrator.parse(fileToRead)
    for (js in dataParsed) {
        println(js)
    }*/

}