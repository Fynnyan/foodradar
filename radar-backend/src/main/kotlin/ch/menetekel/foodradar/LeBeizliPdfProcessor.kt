package ch.menetekel.foodradar

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.time.LocalDate

class LeBeizliPdfProcessor(pdf: PDDocument) {

    private val pdfTextStripper: PDFTextStripper = PDFTextStripper().also { it.sortByPosition = true }
    private val text = pdfTextStripper.getText(pdf)
        .lines()
        .drop(1)
        .filter { it.isNotBlank() }
        .map { it.trim() }
        .joinToString(" ")

    val date = dateRegex.find(text)?.value?.trimAndCleanSpaces()
    val pasta = pastaRegex.find(text)?.value?.cleanMenuText()
    val meat = meatRegex.find(text)?.value?.cleanMenuText()
    val vegi = vegiRegex.find(text)?.value?.cleanMenuText()
    val fish = fishRegex.find(text)?.value?.cleanMenuText()

    fun getMenu(): Menu {

        if (pasta == null && meat == null && vegi == null) throw Exception("Could not parse the lunch menu, the regex didn't find it.")

        return Menu(
            date = runCatching {
                LocalDate.parse(
                    date,
                    DateTimeFormatters.LE_BEIZLI_DATE
                )
            }.getOrElse { LocalDate.now() },
            courses = listOfNotNull(
                Course(name = pasta ?: "There is no pasta menu or the parser did not find it.", price = null),
                Course(name = meat ?: "There is no meat menu or the parser did not find it.", price = null),
                Course(name = vegi ?: "There is no vegi menu or the parser did not find it.", price = null),
                fish?.let { Course(name = fish, price = null) }
            )
        )
    }

    companion object {
        private val regexOption = setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE)

        // Tokens, beginning of the titles of the different daily menu items
        private const val rohToken = "Roh macht"
        private const val pastaToken = "Pasta"
        private const val meatToken = "fleisch"
        private const val vegiToken = "Garten"
        private const val fishToken = "Fisch"

        private fun buildMenuRegex(id: String, startToken: String, endTokens: List<String>) =
            "(?<$id>$startToken(?:(?!(${endTokens.joinToString("|")}))\\X)*)".toRegex(regexOption)

        val dateRegex =
            "(?<day>(MONTAG|DIENSTAG|MITTWOCH|DONNERSTAG|FREITAG|SAMSTAG|SONNTAG)[\\W\\d]*)".toRegex(regexOption)
        val pastaRegex =
            buildMenuRegex("pasta", pastaToken, listOf(meatToken, vegiToken, fishToken, rohToken))
        val meatRegex =
            buildMenuRegex("fleisch", meatToken, listOf(pastaToken, vegiToken, fishToken, rohToken))
        val vegiRegex =
            buildMenuRegex("garten", vegiToken, listOf(meatToken, pastaToken, fishToken, rohToken))
        val fishRegex =
            buildMenuRegex("fish", fishToken, listOf(meatToken, vegiToken, pastaToken, rohToken))
        val priceRegex = "\\*[*\\/\\d. ]+".toRegex()

        fun String.cleanMenuText() =
            this.replace(priceRegex, " ")
                .trimAndCleanSpaces()

        fun String.trimAndCleanSpaces() =
            this.trim().replace("\\s{2,}".toRegex(), " ")
    }
}