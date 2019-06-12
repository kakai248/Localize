package models

import java.util.*

sealed class Resource(
    val identifier: String,
    val translatable: Boolean,
    val formatted: Boolean
) {
    class String(
        identifier: String,
        val localizedValues: Map<Language, LocalizedValue>,
        translatable: Boolean = true,
        formatted: Boolean = false
    ) : Resource(identifier, translatable, formatted)

    class Plural(
        identifier: String,
        val localizedValues: SortedMap<Quantity, Map<Language, LocalizedValue>>,
        translatable: Boolean = true,
        formatted: Boolean = false
    ) : Resource(identifier, translatable, formatted) {

        enum class Quantity {
            Zero,
            One,
            Two,
            Few,
            Many,
            Other
        }
    }
}