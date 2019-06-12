package models

import java.util.*

sealed class Resource(
    val identifier: String
)

class StringResource(
    identifier: String,
    val localizedValues: Map<Language, LocalizedValue>,
    val translatable: Boolean = true,
    val formatted: Boolean = false
) : Resource(identifier)

class PluralResource(
    identifier: String,
    val localizedValues: SortedMap<Quantity, Map<Language, LocalizedValue>>
) : Resource(identifier) {

    enum class Quantity {
        Zero,
        One,
        Two,
        Few,
        Many,
        Other
    }
}