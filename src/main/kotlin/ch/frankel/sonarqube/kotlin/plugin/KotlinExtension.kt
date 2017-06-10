package ch.frankel.sonarqube.kotlin.plugin

import org.sonar.api.Plugin
import org.sonar.api.Plugin.Context
import org.sonar.api.profiles.ProfileDefinition
import org.sonar.api.profiles.RulesProfile
import org.sonar.api.resources.AbstractLanguage
import org.sonar.api.utils.ValidationMessages

class KotlinPlugin : Plugin {

    override fun define(context: Context) {
        context.addExtensions(
                Kotlin::class.java,
                KotlinProfile::class.java,
                KotlinSensor::class.java,
                KotlinRulesDefinition::class.java)
    }
}

class Kotlin : AbstractLanguage(KEY, LABEL) {

    override fun getFileSuffixes() = arrayOf(".kt")

    companion object {
        internal const val KEY = "kotlin"
        internal const val LABEL = "Kotlin"
    }
}

class KotlinProfile : ProfileDefinition() {

    override fun createProfile(validation: ValidationMessages): RulesProfile = RulesProfile.create(KotlinChecks.PROFILE_NAME, Kotlin.LABEL)
}