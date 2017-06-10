package ch.frankel.sonarqube.kotlin.plugin

import org.sonar.api.Plugin

class KotlinPlugin : Plugin {

    override fun define(context: Plugin.Context) {
        context.addExtensions(
                Kotlin::class.java,
                KotlinProfile::class.java,
                KotlinSensor::class.java,
                KotlinRulesDefinition::class.java)
    }
}