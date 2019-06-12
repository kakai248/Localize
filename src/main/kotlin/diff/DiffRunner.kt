package diff

import exceptions.MissingDiffStrategyException
import models.ImportedTranslation
import models.Translation

object DiffRunner {
    fun diff(current: List<Translation>, submitted: List<ImportedTranslation>): DiffResult {
        return DiffResult(current, submitted, emptyList())
    }

    fun submit(result: DiffResult, resolution: DiffResolution): List<Translation> {
        val newTranslations = result.current.toMutableList()
        result.diffs.forEach { diff ->
            val strategy = resolution.strategies[diff] ?: throw MissingDiffStrategyException(diff)
            strategy.apply(newTranslations, diff)
        }
        return newTranslations
    }
}