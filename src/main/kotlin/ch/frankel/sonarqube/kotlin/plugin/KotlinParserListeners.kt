package ch.frankel.sonarqube.kotlin.plugin

import ch.frankel.sonarqube.kotlin.api.KotlinParser
import ch.frankel.sonarqube.kotlin.api.KotlinParserBaseListener
import org.antlr.v4.runtime.tree.TerminalNode

data class Violation(val lineNumber: Int)

abstract class AbstractKotlinParserListener : KotlinParserBaseListener() {

    internal val violations = mutableListOf<Violation>()
    protected fun addViolation(violation: Violation) = violations.add(violation)
}

class NoExplicitReturnUnitListener : AbstractKotlinParserListener() {

    override fun enterFunctionDeclaration(ctx: KotlinParser.FunctionDeclarationContext) {
        if (ctx.type().isNotEmpty()) {
            val typeContext = ctx.type(0)
            with(typeContext.typeDescriptor().userType().simpleUserType()) {
                val typeName = this[0].SimpleName()
                if (typeName.symbol.text == "Unit") {
                    addViolation(Violation(typeName.symbol.line))
                }
            }
        }
    }
}

class NoExplicitReturnTypeExpressionBodyListener : AbstractKotlinParserListener() {

    override fun enterFunctionDeclaration(ctx: KotlinParser.FunctionDeclarationContext) {
        val bodyChildren = ctx.functionBody().children
        if (bodyChildren.size > 1
                && bodyChildren[0] is TerminalNode && bodyChildren[0].text == "="
                && ctx.type().isNotEmpty()) {
            val firstChild = bodyChildren[0] as TerminalNode
            addViolation(Violation(firstChild.symbol.line))
        }
    }
}