package models

data class Translation(
    val key: String,
    val platformResources: Map<Platform, Resource>,
    val module: Module? = null,
    val section: Section? = null
)