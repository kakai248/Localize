import importer.AndroidImporter
import models.Language
import parser.AndroidXmlParser
import utils.LogWriter
import java.io.File

class LocalizeApp {
    fun run() {
        val file = File("/home/rcarrapico/StudioProjects/i9-android/app_base/src/main/res/values-pt-rPT/strings.xml")

        val parser = AndroidXmlParser(Language.Portuguese)
        val importer = AndroidImporter(parser)

        val translations = importer.import(file)

        translations.forEach { translation ->
            LogWriter.log(translation)
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            LocalizeApp().run()
        }
    }
}