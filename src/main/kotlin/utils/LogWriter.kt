package utils

import models.ImportedTranslation
import models.PluralResource
import models.Resource
import models.StringResource

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
                resource.localizedValues.forEach { (language, localizedValue) ->
                    println("\t[${language::class.java.simpleName}][String] ${localizedValue.value}")
                }
            }
            is PluralResource -> {
                println()
                resource.localizedValues.forEach { (language, plurals) ->
                    plurals.forEach { (quantity, localizedValue) ->
                        println("\t[${language::class.java.simpleName}][Plural/$quantity] ${localizedValue.value}")
                    }
                }
            }
        }
    }
}