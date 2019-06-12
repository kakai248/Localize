package importer

import models.ImportedTranslation
import parser.AndroidXmlParser
import java.io.File
import java.io.FileInputStream

class AndroidImporter(
    private val parser: AndroidXmlParser
) : Importer {

    override fun import(file: File): List<ImportedTranslation> {
        return parser.parse(FileInputStream(file))
    }
}