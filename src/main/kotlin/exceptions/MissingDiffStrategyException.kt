package exceptions

import diff.Diff

class MissingDiffStrategyException(
    private val diff: Diff
) : Exception("Missing diff strategy for $diff")