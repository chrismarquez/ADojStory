package Infrastructure

import Interfaces.ICodeGenerator
import Interfaces.IOrchestrator
import Interfaces.IParagraphParser
import Models.Expression
import Services.BaseOrchestrator
import Services.CodeGenerator
import Services.ParagraphParser
import org.kodein.di.Kodein
import org.kodein.di.KodeinProperty
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import java.io.File

object Inject {

    val injector = Kodein {
        bind<ICodeGenerator>() with provider { CodeGenerator() }
        bind<IParagraphParser>() with provider { ParagraphParser() }
        bind<IOrchestrator>() with singleton { BaseOrchestrator() }
    }

    inline fun <reified T: Any> get(): KodeinProperty<T> = injector.instance()
}