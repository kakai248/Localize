package models

import java.util.*

sealed class Resource(
    open val identifier: String
) {
    abstract val hasValues: Boolean
}

data class StringResource(
    override val identifier: String,
    val localizedValues: Map<Language, LocalizedValue>,
    val translatable: Boolean = true,
    val formatted: Boolean = false
) : Resource(identifier) {
    override val hasValues: Boolean
        get() = localizedValues.isNotEmpty()
}

data class PluralResource(
    override val identifier: String,
    val localizedValues: Map<Language, SortedMap<Quantity, LocalizedValue>>
) : Resource(identifier) {
    override val hasValues: Boolean
        get() = localizedValues.isNotEmpty()

    enum class Quantity {
        Zero,
        One,
        Two,
        Few,
        Many,
        Other
    }
}