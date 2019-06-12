package diff

import models.ImportedTranslation
import models.Translation

data class DiffResult(
    val current: List<Translation>,
    val submitted: List<ImportedTranslation>,
    val diffs: List<Diff>
)