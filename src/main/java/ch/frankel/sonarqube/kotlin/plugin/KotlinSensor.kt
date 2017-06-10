package ch.frankel.sonarqube.kotlin.plugin

import ch.frankel.sonarqube.kotlin.plugin.Kotlin.Companion.KEY
import org.sonar.api.batch.fs.FileSystem
import org.sonar.api.batch.fs.InputFile
import org.sonar.api.batch.fs.InputFile.Type.MAIN
import org.sonar.api.batch.fs.InputFile.Type.TEST
import org.sonar.api.batch.sensor.Sensor
import org.sonar.api.batch.sensor.SensorContext
import org.sonar.api.batch.sensor.SensorDescriptor
import org.sonar.api.rule.RuleKey


class KotlinSensor(private val fs: FileSystem) : Sensor {

    val sources: Iterable<InputFile>
        get() = fs.inputFiles(MAIN)

    val tests: Iterable<InputFile>
        get() = fs.inputFiles(TEST)

    override fun describe(descriptor: SensorDescriptor) {
        descriptor.onlyOnLanguage(KEY).name("KotlinSensor")
    }

    override fun execute(context: SensorContext) {
        sources.forEach { inputFile: InputFile ->
            KotlinChecks.checks.forEach { check ->
                val violations = check.violations(inputFile.file())
                violations.forEach { (lineNumber) ->
                    with(context.newIssue().forRule(check.ruleKey())) {
                        val location = newLocation().apply {
                            on(inputFile)
                            message(check.message)
                            at(inputFile.selectLine(lineNumber))
                        }
                        at(location).save()
                    }
                }
            }
        }
    }

    private fun <L : AbstractKotlinParserListener> AbstractKotlinCheck<L>.ruleKey() = RuleKey.of(KotlinChecks.REPOSITORY_KEY, key)

    private fun FileSystem.inputFiles(type: InputFile.Type): MutableIterable<InputFile> = with(predicates()) {
        return inputFiles(this.and(hasLanguage(KEY), hasType(type)))
    }
}
