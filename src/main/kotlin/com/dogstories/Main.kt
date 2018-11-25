package com.dogstories

import com.dogstories.View.OrchestratorView
import tornadofx.App
import tornadofx.launch

class MyApp: App(OrchestratorView::class)

fun main() {
    launch<MyApp>()
}