package ch.frankel.sonarqube.kotlin.plugin

import org.sonar.api.resources.AbstractLanguage

class Kotlin : AbstractLanguage(KEY, LABEL) {

    override fun getFileSuffixes() = arrayOf(".kt")

    companion object {
        internal const val KEY = "kotlin"
        internal const val LABEL = "Kotlin"
    }
}