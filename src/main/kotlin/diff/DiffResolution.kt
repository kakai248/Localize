package diff

data class DiffResolution(
    val strategies: Map<Diff, DiffStrategy<Diff>>
)