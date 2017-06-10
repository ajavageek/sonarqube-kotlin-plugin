package ch.frankel.sonarqube.kotlin.plugin

import ch.frankel.sonarqube.kotlin.api.KotlinLexer
import ch.frankel.sonarqube.kotlin.api.KotlinParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.sonar.api.rules.RuleType
import org.sonar.api.rules.RuleType.CODE_SMELL
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class KotlinChecks {

    companion object {
        const val REPOSITORY_KEY = "kotlin"
        const val PROFILE_NAME = "Default"
        val checks = arrayOf(NoExplicitReturnUnitCheck(), NoExplicitReturnTypeExpressionBodyCheck())
    }
}

interface KotlinCheck {
    val key: String
    val name: String
    val htmlDescription: String
    val type: RuleType
    val message: String
}

abstract class AbstractKotlinCheck<L : AbstractKotlinParserListener>(protected val parserListenerClass: KClass<L>) : KotlinCheck {

    internal fun violations(file: File): List<Violation> {
        val stream = CharStreams.fromFileName(file.absolutePath)
        val lexer = KotlinLexer(stream)
        val tokens = CommonTokenStream(lexer)
        val parser = KotlinParser(tokens)
        val context = parser.kotlinFile()
        val parserListener = parserListenerClass.primaryConstructor!!.call()
        ParseTreeWalker().walk(parserListener, context)
        return parserListener.violations
    }
}

class NoExplicitReturnUnitCheck : AbstractKotlinCheck<NoExplicitReturnUnitListener>(NoExplicitReturnUnitListener::class) {

    override val key = "NoExplicitReturnUnit"
    override val name = "Unit return type shouldn't be explicitly declared"
    override val htmlDescription = "Functions that return Unit shouldn't explicitly declare the return type"
    override val type = CODE_SMELL
    override val message = "Remove this explicit function body return type"
}

class NoExplicitReturnTypeExpressionBodyCheck : AbstractKotlinCheck<NoExplicitReturnTypeExpressionBodyListener>(NoExplicitReturnTypeExpressionBodyListener::class) {

    override val key = "NoExplicitReturnTypeExpressionBody"
    override val name = "Return type shouldn't be explicitly declared for function body"
    override val htmlDescription = "Functions with an expression body shouldn't explicitly declare the return type"
    override val type = CODE_SMELL
    override val message = "Remove this explicit Unit return type"
}