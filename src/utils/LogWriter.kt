package utils

import models.*

object LogWriter {
    fun log(translation: ImportedTranslation) {
        print("[${translation.platform::class.java.simpleName}]")
        translation.section?.let { section ->
            print("[${section.name}]")
        }
        log(translation.resource)
        println()
    }

    fun log(resource: Resource) {
        print(" ${resource.identifier}")
        when (resource) {
            is StringResource -> {
                println(" (translatable=${resource.translatable}, formatted=${resource.formatted})")
                log("String", resource.localizedValues)
            }
            is PluralResource -> {
                println()
                resource.localizedValues.forEach { (quantity, localizedValue) ->
                    log("Plural/$quantity", localizedValue)
                }
            }
        }
    }

    private fun log(type: String, values: Map<Language, LocalizedValue>) {
        values.forEach { (language, localizedValue) ->
            println("\t[${language::class.java.simpleName}][$type] ${localizedValue.value}")
        }
    }
}