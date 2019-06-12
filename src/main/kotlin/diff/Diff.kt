package diff

import models.*

sealed class Diff {
    data class Created(
        val platform: Platform,
        val resource: Resource,
        val section: Section?
    ) : Diff() {

        sealed class Strategy : DiffStrategy<Created> {
            object Ignore : Strategy() {
                override fun apply(current: MutableList<Translation>, diff: Created) {
                    // Do nothing.
                }
            }

            data class Create(val key: String) : Strategy() {
                override fun apply(current: MutableList<Translation>, diff: Created) {
                    val translation = Translation(
                        key = key,
                        platformResources = mapOf(
                            diff.platform to diff.resource
                        ),
                        section = diff.section
                    )
                    current.add(translation)
                }
            }
        }
    }

    data class Deleted(
        val translation: Translation,
        val platform: Platform,
        val language: Language
    ) : Diff() {

        sealed class Strategy : DiffStrategy<Deleted> {
            object Ignore : Strategy() {
                override fun apply(current: MutableList<Translation>, diff: Deleted) {
                    // Do nothing.
                }
            }

            object Delete : Strategy() {
                override fun apply(current: MutableList<Translation>, diff: Deleted) {
                    val resource = diff.translation.platformResources.getValue(diff.platform)

                    val updatedResource = when (resource) {
                        is StringResource -> {
                            resource.copy(localizedValues = resource.localizedValues.minus(diff.language))
                        }
                        is PluralResource -> {
                            resource.copy(localizedValues = resource.localizedValues.minus(diff.language))
                        }
                    }

                    // If resource no longer has values for any language, remove it.
                    val platformResources = when (updatedResource.hasValues) {
                        true -> diff.translation.platformResources.plus(diff.platform to updatedResource)
                        false -> diff.translation.platformResources.minus(diff.platform)
                    }

                    // If we no longer have any platform, remove the translation.
                    if (platformResources.isEmpty()) {
                        current.remove(diff.translation)
                    }
                    // Otherwise, update it.
                    else {
                        val translation = diff.translation.copy(
                            platformResources = platformResources
                        )

                        current[current.indexOf(diff.translation)] = translation
                    }
                }
            }
        }
    }

    data class ValueChanged(
        val translation: Translation,
        val platform: Platform,
        val language: Language,
        val newResource: Resource
    ) : Diff() {

        sealed class Strategy : DiffStrategy<ValueChanged> {
            object KeepOld : Strategy() {
                override fun apply(current: MutableList<Translation>, diff: ValueChanged) {
                    // Do nothing.
                }
            }

            object KeepNew : Strategy() {
                override fun apply(current: MutableList<Translation>, diff: ValueChanged) {
                    val resource = diff.translation.platformResources.getValue(diff.platform)

                    val updatedResource = when (resource) {
                        is StringResource -> {
                            val newResource = diff.newResource as StringResource
                            val newValue = newResource.localizedValues.getValue(diff.language)
                            resource.copy(localizedValues = resource.localizedValues.plus(diff.language to newValue))
                        }
                        is PluralResource -> {
                            val newResource = diff.newResource as PluralResource
                            val newValue = newResource.localizedValues.getValue(diff.language)
                            resource.copy(localizedValues = resource.localizedValues.plus(diff.language to newValue))
                        }
                    }

                    val platformResources = diff.translation.platformResources.plus(diff.platform to updatedResource)

                    val translation = diff.translation.copy(
                        platformResources = platformResources
                    )

                    current[current.indexOf(diff.translation)] = translation
                }
            }
        }
    }

    data class SectionChanged(
        val translation: Translation,
        val newSection: Section?
    ) : Diff() {

        sealed class Strategy : DiffStrategy<SectionChanged> {
            object KeepOld : Strategy() {
                override fun apply(current: MutableList<Translation>, diff: SectionChanged) {
                    // Do nothing.
                }
            }

            object KeepNew : Strategy() {
                override fun apply(current: MutableList<Translation>, diff: SectionChanged) {
                    val translation = diff.translation.copy(
                        section = diff.newSection
                    )
                    current[current.indexOf(diff.translation)] = translation
                }
            }
        }
    }
}