package ch.menetekel.foodradar

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.time.LocalDate


abstract class LeBeizliPdfProcessor(private val pdf: PDDocument) {
    private val pdfTextStripper: PDFTextStripper = PDFTextStripper().also { it.sortByPosition = true }
    protected val text = pdfTextStripper.getText(pdf).menuTextBlockAsSingleLine()

    abstract fun getMenu(): Menu

    companion object {
        val regexOption = setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE)

        fun buildMenuRegex(id: String, startToken: String, endTokens: List<String>) =
            "(?<$id>$startToken(?:(?!(${endTokens.joinToString("|")}))\\X)*)".toRegex(regexOption)

        fun String.trimAndCleanSpaces() =
            this.trim().replace("\\s{2,}".toRegex(), " ")

        fun String.menuTextBlockAsSingleLine() = this
            .lines()
            .filter { it.isNotBlank() }
            .joinToString(" ") { it.trim() }
    }
}

/**
 * Updated PDF processor for the new menu card of the Le Beizli after the 01.09.2024
 */
class LeBeizliPdfProcessorV2(pdf: PDDocument) : LeBeizliPdfProcessor(pdf) {
    override fun getMenu(): Menu {
        val quiche = quicheRegex.find(text)?.value?.cleanMenuText()
        val pasta = pastaRegex.find(text)?.value?.cleanMenuText()
        val dopamin = dopaminRegex.find(text)?.value?.cleanMenuText()
        val meat = meatRegex.find(text)?.value?.cleanMenuText()
        val vegi = vegiRegex.find(text)?.value?.cleanMenuTextVegi()

        if (quiche == null && pasta == null && dopamin == null && vegi == null) throw Exception("Could not parse the lunch menu, the regex didn't find it.")

        return Menu(
            // use current date, the new menu is weekly, if the pdfi is available it should be for the current week.
            date = LocalDate.now(),
            courses = listOfNotNull(
                Course(name = quiche ?: "There is no quiche menu or the parser did not find it.", price = null),
                Course(name = pasta ?: "There is no pasta menu or the parser did not find it.", price = null),
                Course(name = dopamin ?: "There is no dopamin menu or the parser did not find it.", price = null),
                Course(name = vegi ?: "There is no vegi menu or the parser did not find it.", price = null),
                Course(name = meat ?: "There is no meat menu or the parser did not find it.", price = null),
            )
        )
    }

    companion object {

        // Tokens, beginning of the titles of the different daily menu items
        const val quicheToken = "quiche"
        const val pastaToken = "pasta"
        const val vegiToken = "garten"
        const val meatToken = "fleisch"
        const val dopaminToken = "dopaminration"
        const val suessesSection = "SÜSSES"

        val quicheRegex =
            buildMenuRegex("quiche", quicheToken, listOf(meatToken, pastaToken, vegiToken, dopaminToken, suessesSection))
        val pastaRegex =
            buildMenuRegex("pasta", pastaToken, listOf(meatToken, quicheToken, vegiToken, dopaminToken, suessesSection))
        val dopaminRegex =
            buildMenuRegex("dopamin", dopaminToken, listOf(meatToken, pastaToken, vegiToken, quicheToken, suessesSection))
        val meatRegex =
            buildMenuRegex("fleisch", meatToken, listOf(dopaminToken, pastaToken, vegiToken, quicheToken, suessesSection))
        val vegiRegex =
            buildMenuRegex("garten", vegiToken, listOf(meatToken, quicheToken, pastaToken, dopaminToken, suessesSection))
        val priceRegex = "(\\+[\\d.]|\\d+[\\s.|]+)".toRegex()

        fun String.cleanMenuText() =
            this.replace(priceRegex, " ")
                .trimAndCleanSpaces()
        // the vegetarian menu has an optional meat addition marked by a single "+",
        // add text for clarity for the display on the radar
        fun String.cleanMenuTextVegi() =
            this.cleanMenuText().replace("+", "+ (optional as addition)")
    }
}

class LeBeizliPdfProcessorV1(pdf: PDDocument) : LeBeizliPdfProcessor(pdf) {

    val date = dateRegex.find(text)?.value?.trimAndCleanSpaces()
    val pasta = pastaRegex.find(text)?.value?.cleanMenuText()
    val meat = meatRegex.find(text)?.value?.cleanMenuText()
    val vegi = vegiRegex.find(text)?.value?.cleanMenuText()
    val fish = fishRegex.find(text)?.value?.cleanMenuText()

    override fun getMenu(): Menu {

        if (pasta == null && meat == null && vegi == null) throw Exception("Could not parse the lunch menu, the regex didn't find it.")

        return Menu(
            date = runCatching { LocalDate.parse(date, DateTimeFormatters.LE_BEIZLI_DATE) }
                .recoverCatching { LocalDate.parse(date, DateTimeFormatters.LE_BEIZLI_ALTERNATIVE_DATE) }
                .getOrThrow(),
            courses = listOfNotNull(
                Course(name = pasta ?: "There is no pasta menu or the parser did not find it.", price = null),
                Course(name = meat ?: "There is no meat menu or the parser did not find it.", price = null),
                Course(name = vegi ?: "There is no vegi menu or the parser did not find it.", price = null),
                fish?.let { Course(name = fish, price = null) }
            )
        )
    }

    companion object {

        // Tokens, beginning of the titles of the different daily menu items
        private const val rohToken = "Roh macht"
        private const val pastaToken = "Pasta"
        private const val meatToken = "fleisch"
        private const val vegiToken = "Garten"
        private const val fishToken = "Fisch"

        val dateRegex =
            "(?<day>(MONTAG|DIENSTAG|MITTWOCH|DONNERSTAG|FREITAG|SAMSTAG|SONNTAG)[,.\\s\\d]*((JAN|FEB|MÄR|APR|MAI|JUN|JUL|AUG|SEP|OKT|NOV|DEZ)\\w*[,.\\s]*\\d{2,4}|\\d{2}[.\\s]*\\d{2}[.\\s]*\\d{2,4}))".toRegex(regexOption)
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
    }
}