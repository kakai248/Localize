package diff

import models.ImportedTranslation
import models.Translation

object DiffRunner {
    fun diff(current: List<Translation>, submitted: List<ImportedTranslation>): DiffResult {
        return DiffResult(current, submitted, emptyList())
    }

    fun submit(diffResult: DiffResult, diffResolution: DiffResolution): List<Translation> {
        return emptyList()
    }
}