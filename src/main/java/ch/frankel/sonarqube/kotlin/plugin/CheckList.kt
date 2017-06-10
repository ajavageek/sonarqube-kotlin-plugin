package ch.frankel.sonarqube.kotlin.plugin

import ch.frankel.sonarqube.kotlin.plugin.CheckList.Companion.REPOSITORY_KEY
import org.sonar.api.rule.RuleKey
import org.sonar.api.rules.RuleType
import org.sonar.api.rules.RuleType.CODE_SMELL

class CheckList {

    companion object {

        const val REPOSITORY_KEY = "kotlin"
        const val PROFILE_NAME = "Default"
        val checks = arrayOf(NoExplicitReturnUnitCheck())
    }
}

interface KotlinCheck {
    val key: String
    val name: String
    val htmlDescription: String
    val type: RuleType
}

class NoExplicitReturnUnitCheck : KotlinCheck {
    companion object {
        const val KEY = "NoExplicitReturnUnit"
        val RULE_KEY = RuleKey.of(REPOSITORY_KEY, KEY)
    }

    override val key: String
        get() = KEY
    override val name: String
        get() = "Unit return type shouldn't be explicitly declared"
    override val htmlDescription: String
        get() = "Functions that return Unit shouldn't explicitly declare the return type"
    override val type: RuleType
        get() = CODE_SMELL
}