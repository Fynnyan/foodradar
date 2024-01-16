package ch.menetekel.foodradar

import ch.menetekel.foodradar.LeBeizliPdfProcessor.Companion.cleanMenuText
import ch.menetekel.foodradar.LeBeizliPdfProcessor.Companion.trimAndCleanSpaces
import org.assertj.core.api.SoftAssertions
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(SoftAssertionsExtension::class)
class LeBeizliPdfProcessorTest {

    @Test
    fun `The lebeizli menu regex return the correct text blocks for the respective menu item`(softly: SoftAssertions) {

        val date = LeBeizliPdfProcessor.dateRegex.find(leBeizliMenu)?.value?.trimAndCleanSpaces()
        val pasta = LeBeizliPdfProcessor.pastaRegex.find(leBeizliMenu)?.value?.cleanMenuText()
        val meat = LeBeizliPdfProcessor.meatRegex.find(leBeizliMenu)?.value?.cleanMenuText()
        val vegi = LeBeizliPdfProcessor.vegiRegex.find(leBeizliMenu)?.value?.cleanMenuText()

        softly.assertThat(pasta).isEqualTo("Pasta [v] Casarecce | Tomatensauce | Oliven | Kapern | Chili")
        softly.assertThat(meat).isEqualTo("Fleischers Lust Bauernbratwurst | Zwiebelsauce | Ofenkartoffeln | Gemüse")
        softly.assertThat(vegi).isEqualTo("Garten Eden Linseneintopf überbacken mit Ziegenkäse")

        // revisit the formatting another day, currently there is a fallback and "DIENSTAG, 16.01. 23" is just a typo in the text
        // softly.assertThat(
        //     LocalDate.parse(
        //         date,
        //         DateTimeFormatters.LE_BEIZLI_DATE
        //     )
        // ).isEqualTo(LocalDate.of(2023, 1, 16))

    }

    val leBeizliMenu = """
        MITTAGSKARTE
        DIENSTAG, 16.01. 23
        LA FORMULE DU MIDI
        Lust auf ein gemütliches Mittagessen in drei Gängen? 
        Wir servieren eine Vorspeise, einen Hauptgang und 
        ein kleines Dessert nach Wahl. Bon app!
        27.00
        CARTE BLANCHE
        Keine Lust auf lange Entscheidungen am Mittag? 
        Als Überraschung servieren wir subito ein Hauptgericht. 
        Wahlweise vegetarisch. Mit diesem Teller wirken wir 
        der Verschwendung von Lebensmittel entgegen. 
        14.50
        *TÄGLICH AUCH  ZUM MITNEHMEN*
        VORSPEISEN
        Suppe in der Tasse [v] 3.50
        Rüebli | Ingwer
        Kleiner Salat [v] 4.50
        Bunter Blattsalat | Gemüse |
        karamellisierte Kerne | Hausdressing
        HAUPTSPEISEN
        Pasta [v] *12.00  /  18.00
        Casarecce | Tomatensauce | Oliven | Kapern | Chili 
        Garten Eden *14.00  /  18.50
        Linseneintopf überbacken mit Ziegenkäse
        Fleischers Lust *16.00  /  20.50 
        Bauernbratwurst | Zwiebelsauce | 
        Ofenkartoffeln | Gemüse
        Roh macht froh 18.50
        Blattsalat | Gemüse |  Hausdressing | 
        karamellisierte Kerne | Preiselbeeren
        panierter Camembert aus Gerzensee | 
        NACHSPEISEN
        Süss 5.00
        Schokoladenmousse | Rahmhaube
        Pastel de Nata 4.00
        Portugiesisches Rahmküchlein
        Mucks Gelati im Becher 6.50
        Diverse Sorten
        Schokolade 7.50
        Pralinéekuchen
        Das Fleisch kommt wenn möglich aus der Region und ausschliesslich aus 
        der Schweiz. Bei Allergien und Unverträglichkeiten stehen wir Ihnen gerne 
        zur Verfügung. Gerichte mit [v] sind vegan. Die Preise sind in 
        Schweizerfranken, die gesetzliche Mehrwertsteuer ist inbegriffen.
    """.trimIndent()
}