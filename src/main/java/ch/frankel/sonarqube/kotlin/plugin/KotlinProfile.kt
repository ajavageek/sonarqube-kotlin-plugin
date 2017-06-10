package ch.frankel.sonarqube.kotlin.plugin

import ch.frankel.sonarqube.kotlin.plugin.Kotlin.Companion.LABEL
import ch.frankel.sonarqube.kotlin.plugin.KotlinChecks.Companion.PROFILE_NAME
import org.sonar.api.profiles.ProfileDefinition
import org.sonar.api.profiles.RulesProfile
import org.sonar.api.profiles.RulesProfile.create
import org.sonar.api.utils.ValidationMessages

class KotlinProfile : ProfileDefinition() {

    override fun createProfile(validation: ValidationMessages): RulesProfile = create(PROFILE_NAME, LABEL)
}