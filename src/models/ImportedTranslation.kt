package models

class ImportedTranslation(
    val platform: Platform,
    val resource: Resource,
    val section: Section? = null
)