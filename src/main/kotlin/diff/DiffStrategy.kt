package diff

import models.Translation

interface DiffStrategy<D : Diff> {
    fun apply(current: MutableList<Translation>, diff: D)
}