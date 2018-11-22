package com.gui.view

import Infrastructure.Inject
import Interfaces.IOrchestrator
import javafx.collections.FXCollections
import javafx.stage.FileChooser
import tornadofx.*
import javafx.collections.ObservableList
import javafx.event.EventHandler
import javafx.scene.control.SelectionMode
import javafx.scene.input.TransferMode
import java.awt.Event
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
                    var files = chooseFile("Select dog file",
                        arrayOf(FileChooser.ExtensionFilter("Dog files (*.dog)", "*.dog")))
                    if (files.isNotEmpty()) {
                        for(i in orchestrator.parse(files.get(0))) {
                            paragraphs.add(i)
                        }
                    }
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
            event.consume()
        }
    }
}
