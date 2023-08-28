package ch.menetekel.foodradar

import org.jsoup.Jsoup
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

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
                    Menu(date, courses.mapIndexed { i, e -> Course(e, prices.getOrNull(i)) })
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
                ?.map {
                    val dateAndText = it.selectFirst("p")
                        ?.text()
                        ?.split(":")
                        ?: throw Exception("could not parse list and split text")
                    Menu(
                        date = dateAndText.getOrNull(0) ?: "",
                        courses = listOf(
                            Course(
                                name = dateAndText.getOrNull(1) ?: "",
                                price = null
                            )
                        )
                    )
                }
                ?.let { Place.from(placesConfig.schichtwechsel, it, ProcessingStatus.PROCESSED).toMono() }
                ?: throw Exception("Did not find the specified elements to process")
        } catch (e: Exception) {
            logAndReturnProcessingFailure(placesConfig.schichtwechsel, e)
        }
    }

    private fun logAndReturnScrapeFailure(config: PlacesConfig.PlacesMetaData, exception: Exception?): Mono<Place> {
        log.error("Could not scrape ${config.scrapeAddress}", exception)
        return Place.from(config, emptyList(), ProcessingStatus.SITE_NOT_ACCESSIBLE).toMono()
    }

    private fun logAndReturnProcessingFailure(config: PlacesConfig.PlacesMetaData, exception: Exception?): Mono<Place> {
        log.error("Could process the page of ${config.scrapeAddress}", exception)
        return Place.from(config, emptyList(), ProcessingStatus.PROCESS_ERROR).toMono()
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(DataCollectors::class.java)
    }
}

