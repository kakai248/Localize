package importer

import models.ImportedTranslation
import java.io.File

interface Importer {
    fun import(file: File): List<ImportedTranslation>
}