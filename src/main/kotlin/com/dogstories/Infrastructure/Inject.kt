package com.dogstories.Infrastructure

import com.dogstories.Interfaces.ICodeGenerator
import com.dogstories.Interfaces.IOrchestrator
import com.dogstories.Interfaces.IParagraphParser
import com.dogstories.Services.BaseOrchestrator
import com.dogstories.Services.CodeGenerator
import com.dogstories.Services.ParagraphParser
import org.kodein.di.Kodein
import org.kodein.di.KodeinProperty
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

object Inject {

    val injector = Kodein {
        bind<ICodeGenerator>() with provider { CodeGenerator() }
        bind<IParagraphParser>() with provider { ParagraphParser() }
        bind<IOrchestrator>() with singleton { BaseOrchestrator() }
    }

    inline fun <reified T: Any> get(): KodeinProperty<T> = injector.instance()
}