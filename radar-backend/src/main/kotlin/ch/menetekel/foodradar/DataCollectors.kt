package ch.menetekel.foodradar

import org.apache.pdfbox.Loader
import org.apache.pdfbox.text.PDFTextStripper
import org.jsoup.Jsoup
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.net.URL
import java.time.DayOfWeek
import java.time.LocalDate

@Service
class DataCollectors(
    val placesConfig: PlacesConfig
) {

    fun fetchDreigaengerMenu(): Mono<Place> {
        val page =
            try { Jsoup.connect(placesConfig.dreigaenger.scrapeAddress).get() }
            catch (e: Exception) { return logAndReturnScrapeFailure(placesConfig.dreigaenger, e) }
        /*
         * currently one carousel element marked by the class is used on the page.
         * it contains the menu for the week, so we can iterate over the children to parse the menu
         */
        return try {
            page.selectFirst(".carousel-inner")
                ?.children()
                ?.map { card ->
                    var date = ""
                    val courses = mutableListOf<String>()
                    val prices = mutableListOf<String>()
                    /*
                         * usual order
                         * - date
                         * - first course text
                         * - first course prise
                         * possible second item
                         * - second course text
                         * - second course prise
                         */
                    card.children().forEach {
                        when {
                            it.classNames().contains("menu_date") -> date = it.text()
                            it.classNames().contains("menu_text") -> courses.add(it.text())
                            it.classNames().contains("menu_price") -> prices.add(it.text())
                        }
                    }
                    Menu(
                        date = LocalDate.parse(date, DateTimeFormatters.DREIGAENGER_DATE),
                        courses = courses.mapIndexed { i, e -> Course(e, prices.getOrNull(i)) }
                    )
                }
                ?.let { Place.from(placesConfig.dreigaenger, it, ProcessingStatus.PROCESSED).toMono() }
                ?: throw Exception("Did not find the specified elements to process")
        } catch (e: Exception) {
            logAndReturnProcessingFailure(placesConfig.dreigaenger, e)
        }
    }

    fun fetchSchichtWechselMenu(): Mono<Place> {
        val page =
            try { Jsoup.connect(placesConfig.schichtwechsel.scrapeAddress).get() }
            catch (e: Exception) { return logAndReturnScrapeFailure(placesConfig.schichtwechsel, e) }

        return try {
            page.selectFirst(".rmc-menu > ul")
                ?.children()
                ?.map men@{
                    val dateAndText = it.selectFirst("p")
                        ?.text()
                        ?.split(":")
                        ?: throw Exception("could not parse list and split text")

                    val day = dateAndText.getOrNull(0)?.lowercase()
                    val menuText = dateAndText.getOrNull(1) ?: ""

                    // skip the daily soup and salat entry
                    if (day != "täglich") {
                        val calcDate = getDateRelativeOfGermanDayForCurrentWeek(day ?: "")
                            ?: throw Exception("Could not parse and calculate date from the $day text")

                        Menu(
                            date = calcDate,
                            // sometimes they use the / to display two menus, options
                            courses = menuText.split("/").map { name ->
                                Course(
                                    name = name.trim(),
                                    price = null
                                )
                            }
                        )
                    } else null
                }
                ?.filterNotNull()
                ?.let { Place.from(placesConfig.schichtwechsel, it, ProcessingStatus.PROCESSED).toMono() }
                ?: throw Exception("Did not find the specified elements to process")
        } catch (e: Exception) {
            logAndReturnProcessingFailure(placesConfig.schichtwechsel, e)
        }
    }

    fun getLeBeizli(): Mono<Place> {
        return try {
            val pageWihtButtonToFile = Jsoup.connect(placesConfig.leBeizli.scrapeAddress).get()

            // the link has no dedicated id, search by text content
            val pdfLink = pageWihtButtonToFile.select("a[href][data-doc-id]")
                .find { it.text() == "Mittagsmenu" }
                ?.attr("abs:href")
                ?: return logAndReturnScrapeFailure(
                    placesConfig.leBeizli,
                    Exception("Could not find the link to the pdf to get and parse the lunch menu")
                )

            val pdf = Loader.loadPDF(URL(pdfLink).readBytes())
            val text = PDFTextStripper().getText(pdf)

            // stupid way to track the cursor when iterating over lines of the text, there are no constant markers
            // for the sections and they may varies in lines
            var menuTitlePassed = false
            var pastaStarted = false
            var pastaPassed = false
            var meatStarted = false
            var meatPassed = false
            var vegiStarted = false
            var parse = true

            val date = StringBuilder()
            val pasta = StringBuilder()
            val meat = StringBuilder()
            val vegi = StringBuilder()

            text.lines()
                .drop(1)
                .filter { it.isNotBlank() }
                .map { it.trim() }
                .forEach {
                    when {
                        it.contains("F O R M U L E") -> menuTitlePassed = true
                        it.contains("Pasta") -> pastaStarted = true
                        it.contains("Fleischers") -> { meatStarted = true; pastaPassed = true }
                        it.contains("Garten") -> { vegiStarted = true; meatPassed = true }
                        it.contains("Roh macht froh") -> parse = false
                    }
                    if (parse) {
                        when {
                            menuTitlePassed.not() -> date.append(it)
                            pastaStarted && pastaPassed.not() -> pasta.appendLine(it)
                            meatPassed && vegiStarted -> vegi.appendLine(it)
                            pastaPassed && meatStarted -> meat.appendLine(it)
                        }
                    }
                }

            Place(
                name = placesConfig.leBeizli.name,
                web = placesConfig.leBeizli.web,
                menus = listOf(
                    Menu(
                        date = LocalDate.parse(
                            date,
                            DateTimeFormatters.LE_BEIZLI_DATE
                        ),
                        courses = listOf(
                            Course(name = pasta.toString(), price = null),
                            Course(name = meat.toString(), price = null),
                            Course(name = vegi.toString(), price = null)
                        )
                    )
                ),
                processingStatus = ProcessingStatus.PROCESSED
            ).toMono()
        } catch (e: Exception) {
            logAndReturnScrapeFailure(placesConfig.leBeizli, e)
        }
    }

    private fun logAndReturnScrapeFailure(config: PlacesConfig.PlacesMetaData, exception: Exception?): Mono<Place> {
        log.error("Could not scrape ${config.scrapeAddress}", exception)
        return Place.from(config, emptyList(), ProcessingStatus.SITE_NOT_ACCESSIBLE).toMono()
    }

    private fun logAndReturnProcessingFailure(config: PlacesConfig.PlacesMetaData, exception: Exception?): Mono<Place> {
        log.error("Could not process the page of ${config.scrapeAddress}", exception)
        return Place.from(config, emptyList(), ProcessingStatus.PROCESS_ERROR).toMono()
    }


    fun getDateRelativeOfGermanDayForCurrentWeek(germanDay: String): LocalDate? {
        val dayOfWeek = when (germanDay.lowercase()) {
            "montag" -> DayOfWeek.MONDAY
            "dienstag" -> DayOfWeek.TUESDAY
            "mittwoch" -> DayOfWeek.WEDNESDAY
            "donnerstag" -> DayOfWeek.THURSDAY
            "freitag" -> DayOfWeek.FRIDAY
            else -> return null
        }

        val now = LocalDate.now()
        val currentDay = now.dayOfWeek
        val op = dayOfWeek.value - currentDay.value
        return now.plusDays(op.toLong())
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(DataCollectors::class.java)
    }
}

