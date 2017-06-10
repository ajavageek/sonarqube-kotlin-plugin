package ch.frankel.sonarqube.kotlin.plugin

import ch.frankel.sonarqube.kotlin.plugin.Kotlin.Companion.KEY
import org.sonar.api.batch.fs.FileSystem
import org.sonar.api.batch.fs.InputFile
import org.sonar.api.batch.fs.InputFile.Type.MAIN
import org.sonar.api.batch.fs.InputFile.Type.TEST
import org.sonar.api.batch.sensor.Sensor
import org.sonar.api.batch.sensor.SensorContext
import org.sonar.api.batch.sensor.SensorDescriptor


class KotlinSensor(private val fs: FileSystem) : Sensor {

    val sources: Iterable<InputFile>
        get() = fs.inputFiles(MAIN)

    val tests: Iterable<InputFile>
        get() = fs.inputFiles(TEST)

    override fun describe(descriptor: SensorDescriptor) {
        descriptor.onlyOnLanguage(KEY).name("KotlinSensor")
    }

    override fun execute(context: SensorContext) {
        val issue = context.newIssue().forRule(NoExplicitReturnUnitCheck.RULE_KEY)
        val inputFile = sources.iterator().next()
        val location = issue.newLocation().on(inputFile).message("Chto sluchilos'?")
        location.at(inputFile.selectLine(18))
        issue.at(location).save()
    }
}

private fun FileSystem.inputFiles(type: InputFile.Type): MutableIterable<InputFile> = with(predicates()) {
    return inputFiles(this.and(hasLanguage(KEY), hasType(type)))
}