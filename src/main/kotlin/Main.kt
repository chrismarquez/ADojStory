

import View.OrchestatorView
import tornadofx.App
import tornadofx.launch

class MyApp: App(OrchestatorView::class)

fun main() {
    launch<MyApp>()
}