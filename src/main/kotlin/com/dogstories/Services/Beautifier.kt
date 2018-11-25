package com.dogstories.Services

import javax.script.Invocable
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

object Beautifier {

    private val engine: ScriptEngine
    private val invocableEngine: Invocable
        get() = engine as Invocable

    init {
        println("Initializing...")
        val scriptManager = ScriptEngineManager()
        engine = scriptManager.getEngineByName("nashorn")

        val beautifier = javaClass.classLoader.getResource("beautify.js").readText()
        engine.eval("var global = this;")
        engine.eval(beautifier)
        println("Done.")
    }

    fun beautify(js: String): String = invocableEngine.invokeFunction("js_beautify", js) as String

}