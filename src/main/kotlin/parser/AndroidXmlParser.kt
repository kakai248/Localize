package parser

import models.*
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.xml.sax.InputSource
import java.io.InputStream
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory

class AndroidXmlParser(
    private val language: Language
) {

    private val factory = DocumentBuilderFactory.newInstance()

    fun parse(inputStream: InputStream): List<ImportedTranslation> {
        val builder = factory.newDocumentBuilder()

        val inputSource = InputSource(inputStream)
        val document = builder.parse(inputSource)
        return parseDocument(document)
    }

    private fun parseDocument(document: Document): List<ImportedTranslation> {
        document.documentElement.normalize()
        val resources = document.documentElement
        val nodes = resources.childNodes

        var section: Section? = null

        val translations = mutableListOf<ImportedTranslation>()

        for (i in 0 until nodes.length) {
            val node = nodes.item(i)

            when {
                node.isComment -> section = parseComment(node)
                node.isElement -> translations.add(parseElement(node, section))
            }
        }

        return translations
    }

    private val Node.isComment: Boolean
        get() = nodeType == Node.COMMENT_NODE

    private fun parseComment(node: Node): Section? {
        return Section(node.textContent.trim())
    }

    private val Node.isElement: Boolean
        get() = nodeType == Node.ELEMENT_NODE && (nodeName == "string" || nodeName == "plurals")

    private fun parseElement(node: Node, section: Section?): ImportedTranslation {
        val resource = when (node.nodeName) {
            "string" -> parseStringElement(node)
            "plurals" -> parsePluralsElement(node)
            else -> throw IllegalArgumentException("Invalid element type")
        }

        return ImportedTranslation(
            platform = Platform.Android,
            resource = resource,
            section = section
        )
    }

    private fun parseStringElement(node: Node): StringResource {
        val attributes = node.attributes

        val name = attributes.getNamedItem("name").textContent
        val translatable = attributes.getNamedItem("translatable")?.run {
            textContent!!.toBoolean()
        } ?: true
        val formatted = attributes.getNamedItem("formatted")?.run {
            textContent!!.toBoolean()
        } ?: false
        val value = node.textContent

        return StringResource(
            identifier = name,
            localizedValues = mapOf(
                language to LocalizedValue(language, value)
            ),
            translatable = translatable,
            formatted = formatted
        )
    }

    private fun parsePluralsElement(node: Node): PluralResource {
        val attributes = node.attributes

        val name = attributes.getNamedItem("name").textContent

        val localizedValues = mutableMapOf<Language, SortedMap<PluralResource.Quantity, LocalizedValue>>()
        for (i in 0 until node.childNodes.length) {
            val itemNode = node.childNodes.item(i)

            if (itemNode.nodeType == Node.ELEMENT_NODE) {
                val quantity = itemNode.attributes.getNamedItem("quantity")?.run {
                    parsePluralQuantity(textContent)
                } ?: continue

                val value = TreeMap<PluralResource.Quantity, LocalizedValue>().apply {
                    put(quantity, LocalizedValue(language, itemNode.textContent))
                }
                localizedValues[language] = value
            }
        }

        return PluralResource(
            identifier = name,
            localizedValues = localizedValues
        )
    }

    private fun parsePluralQuantity(quantity: String): PluralResource.Quantity? {
        return when (quantity.toLowerCase()) {
            "zero" -> PluralResource.Quantity.Zero
            "one" -> PluralResource.Quantity.One
            "two" -> PluralResource.Quantity.Two
            "few" -> PluralResource.Quantity.Few
            "many" -> PluralResource.Quantity.Many
            "other" -> PluralResource.Quantity.Other
            else -> null
        }
    }
}