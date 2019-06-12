package models

class Translation(
    val module: Module,
    val section: Section,
    val key: String,
    val resources: Map<Platform, Resource>
)