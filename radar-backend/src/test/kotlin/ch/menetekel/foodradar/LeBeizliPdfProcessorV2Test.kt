package ch.menetekel.foodradar

import ch.menetekel.foodradar.LeBeizliPdfProcessorV2.Companion.cleanMenuText
import ch.menetekel.foodradar.LeBeizliPdfProcessorV2.Companion.cleanMenuTextVegi
import ch.menetekel.foodradar.LeBeizliPdfProcessorV2.Companion.dopaminRegex
import ch.menetekel.foodradar.LeBeizliPdfProcessorV2.Companion.pastaRegex
import ch.menetekel.foodradar.LeBeizliPdfProcessorV2.Companion.quicheRegex
import ch.menetekel.foodradar.LeBeizliPdfProcessorV2.Companion.vegiRegex
import org.assertj.core.api.SoftAssertions
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(SoftAssertionsExtension::class)
class LeBeizliPdfProcessorV2Test {

    @Test
    fun `The lebeizli menu regex return the correct text blocks for the respective menu item`(softly: SoftAssertions) {

        val quiche = quicheRegex.find(leBeizliMenu)?.value?.cleanMenuText()
        val pasta = pastaRegex.find(leBeizliMenu)?.value?.cleanMenuText()
        val dopamin = dopaminRegex.find(leBeizliMenu)?.value?.cleanMenuText()
        val vegi = vegiRegex.find(leBeizliMenu)?.value?.cleanMenuTextVegi()

        softly.assertThat(quiche).isEqualTo("Quiche Champ d'amour Gemüsequiche mit Salat")
        softly.assertThat(pasta).isEqualTo("Pasta e basta Fusilli Cinque Pi")
        softly.assertThat(dopamin).isEqualTo("Dopaminration Burrito mit Käse überbacken, gefüllt mit Chili con carne, Bohnenpüree und Tomatensauce")
        softly.assertThat(vegi).isEqualTo("Garten Eden [v] Luyaragout mit Silberzwiebeln, Randen und Süsskartoffeln + (optional as addition) Pouletgeschnetzeltes")
    }

    val leBeizliMenu = """
        MITTAGSTISCH
        17. bis 20. September 2024
        LE PLUS
        Suppe [v] 5
        Gelberbse
        Salat [v] 5
        Bunter Blattsalat an Hausdressing
        mit Gemüse und karamellisierten Kernen
        HAUPTSPEISEN
        Quiche Champ d'amour 13
        Gemüsequiche mit Salat
        Pasta e basta 14 | 12 
        Fusilli Cinque Pi
        Garten Eden [v] 17 | 15
        Luyaragout mit Silberzwiebeln, Randen 
        und Süsskartoffeln 
        + Pouletgeschnetzeltes +4
        Dopaminration 19
        Burrito mit Käse überbacken, 
        gefüllt mit Chili con carne, 
        Bohnenpüree und Tomatensauce
        SÜSSES
        im Glas 5
        Zwetschgen-Mousse
        kleine Sünde 4
        Portugiesisches Rahmküchlein
        Muck's Gelati 6
        Holunderblüten, Chocolate,
        Caramel fleur de sel, Stracciatella
        Schokoladiges 7
        Pralinéeschokoladenkuchen
        (vegan möglich +15min)
        Gerichte mit [v] sind vegan. 
        Das Fleisch kommt wenn möglich aus der Region und ausschliesslich aus der Schweiz. 
        Bei Allergien und Unverträglichkeiten stehen wir gerne zur Verfügung. 
        Die Preise sind in Schweizerfranken und die gesetzliche Mehrwertsteuer ist inbegriffen.
    """.trimIndent()
}