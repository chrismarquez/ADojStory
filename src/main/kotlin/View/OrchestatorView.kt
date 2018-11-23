package View

import Infrastructure.Inject
import Interfaces.IOrchestrator
import javafx.stage.FileChooser
import tornadofx.*
import javafx.scene.control.SelectionMode
import javafx.scene.input.TransferMode
import java.io.File

class OrchestatorView : View("Paragraph Parser") {
    private val paragraphs = arrayListOf<String>().observable()
    val orchestrator: IOrchestrator by Inject.get()

    override val root = borderpane {
        prefWidth = 800.0

        center {
            listview(paragraphs) {
                selectionModel.selectionMode = SelectionMode.SINGLE
            }
        }

        bottom {
            button("Target File") {
                action {

                    val files = chooseFile(
                        "Select dog file",
                        arrayOf(FileChooser.ExtensionFilter("Dog files (*.dog)", "*.dog"))
                    )
                    val file = File("./${files[0].nameWithoutExtension}.js")
                    if (files.isNotEmpty()) {

                        for (i in orchestrator.parse(files.get(0))) {
                            paragraphs.add(i)
                        }
                    }
                    val text = paragraphs.reduce { prev, curr -> prev + "\n" + curr  }
                    file.writeText(text)
                }
            }
        }

        setOnDragExited { event ->
            if(event.gestureSource != this && event.dragboard.hasFiles()) {
                event.acceptTransferModes(TransferMode.LINK)
                for(i in orchestrator.parse(event.dragboard.files.get(0))) {
                    paragraphs.add(i)
                }
            }
            val file = File("./${event.dragboard.files[0].nameWithoutExtension}.js")
            val text = paragraphs.reduce { prev, curr -> prev + "\n" + curr  }
            file.writeText(text)
            event.consume()
        }
    }
}
