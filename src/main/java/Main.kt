import ch.frankel.sonarqube.kotlin.api.KotlinLexer
import ch.frankel.sonarqube.kotlin.api.KotlinParser
import ch.frankel.sonarqube.kotlin.api.KotlinParser.EQ
import ch.frankel.sonarqube.kotlin.api.KotlinParserBaseListener
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.antlr.v4.runtime.tree.TerminalNode

fun main(args: Array<String>) {
    val stream = CharStreams.fromStream(String::class.java.getResourceAsStream("/kotlin.txt"))
    val lexer = KotlinLexer(stream)
    val tokens = CommonTokenStream(lexer)
    val parser = KotlinParser(tokens)
    val context = parser.kotlinFile()
    ParseTreeWalker().apply {
        walk(object : KotlinParserBaseListener() {
            override fun enterFunctionDeclaration(ctx: KotlinParser.FunctionDeclarationContext) {
                val bodyChildren = ctx.functionBody().children
                if (bodyChildren.size > 1
                        && bodyChildren[0] is TerminalNode && bodyChildren[0].text == "="
                        && ctx.type().isNotEmpty()) {
                    val firstChild = bodyChildren[0] as TerminalNode
                    println("Found explicit return type for expression body " +
                            "in function ${ctx.SimpleName()} at line ${firstChild.symbol.line}")
                }
            }
        }, context)
    }
}

val foo = object : KotlinParserBaseListener() {
    override fun enterFunctionDeclaration(ctx: KotlinParser.FunctionDeclarationContext) {
        if (ctx.type().isNotEmpty()) {
            val typeContext = ctx.type(0)
            with(typeContext.typeDescriptor().userType().simpleUserType()) {
                val typeName = this[0].SimpleName()
                if (typeName.symbol.text == "Unit") {
                    println("Found Unit as explicit return type " +
                            "in function ${ctx.SimpleName()} at line ${typeName.symbol.line}")
                }
            }
        }
    }
}