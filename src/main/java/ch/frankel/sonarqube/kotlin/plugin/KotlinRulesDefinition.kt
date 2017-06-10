package ch.frankel.sonarqube.kotlin.plugin

import ch.frankel.sonarqube.kotlin.plugin.CheckList.Companion.REPOSITORY_KEY
import ch.frankel.sonarqube.kotlin.plugin.Kotlin.Companion.KEY
import org.sonar.api.server.rule.RulesDefinition
import org.sonar.api.server.rule.RulesDefinition.Context
import org.sonar.api.server.rule.RulesDefinition.NewExtendedRepository

class KotlinRulesDefinition : RulesDefinition {

    override fun define(context: Context) {
        context.createRepository(REPOSITORY_KEY, KEY).apply {
            setName("SonarAnalyzer")
            CheckList.checks.forEach {
                createRule(it)
            }
            done()
        }
    }
}

private fun NewExtendedRepository.createRule(check: KotlinCheck) = createRule(check.key).apply {
    with(check) {
        setName(name)
        setHtmlDescription(htmlDescription)
        setType(type)
    }
}

