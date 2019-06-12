package models

data class Translation(
    val key: String,
    val resources: Map<Platform, Resource>,
    val module: Module? = null,
    val section: Section? = null
)