package models

import java.util.*

sealed class Resource(
    open val identifier: String
)

data class StringResource(
    override val identifier: String,
    val localizedValues: Map<Language, LocalizedValue>,
    val translatable: Boolean = true,
    val formatted: Boolean = false
) : Resource(identifier)

data class PluralResource(
    override val identifier: String,
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