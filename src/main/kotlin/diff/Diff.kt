package diff

import models.*

sealed class Diff {
    data class Created(
        val platform: Platform,
        val resource: Resource,
        val section: Section?
    ) : Diff() {

        sealed class Resolution {
            data class Create(val key: String) : Resolution()
            object Ignore : Resolution()
        }
    }

    data class Deleted(
        val translation: Translation,
        val platform: Platform,
        val language: Language
    ) : Diff() {

        sealed class Resolution {
            object Delete : Resolution()
            object Ignore : Resolution()
        }
    }

    data class ValueChanged(
        val translation: Translation,
        val platform: Platform,
        val language: Language,
        val newValue: String
    ) : Diff() {

        sealed class Resolution {
            object KeepOld : Resolution()
            object KeepNew : Resolution()
        }
    }

    data class SectionChanged(
        val translation: Translation,
        val platform: Platform,
        val language: Language,
        val newSection: Section?
    ) : Diff() {

        sealed class Resolution {
            object KeepOld : Resolution()
            object KeepNew : Resolution()
        }
    }
}