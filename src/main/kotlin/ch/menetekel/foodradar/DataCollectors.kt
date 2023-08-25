package ch.menetekel.foodradar

import org.jsoup.Jsoup
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toFlux

@Service
class DataCollectors {

    fun fetchDreigaengerMenu(): Flux<Menu> {
        val page = Jsoup.connect("https://dreigaenger.ch/").get()
        /*
         * currently one carousel element marked by the class is used on the page.
         * it contains the menu for the week, so we can iterate over the children to parse the menu
         */
        return page.selectFirst(".carousel-inner")
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
            ?.toFlux()
            ?: throw Exception("no connection to dreigänger")
    }

    fun fetchSchichtWechselMenu(): Flux<Menu> {
        val page = Jsoup.connect("https://schichtwechsel.ch/").get()

        return page.selectFirst(".rmc-menu > ul")
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
            ?.toFlux()
            ?: throw Exception("could not process schichtwechsel menu")
    }
}

data class Menu(
    val date: String,
    val courses: List<Course>,
)

data class Course(
    val name: String,
    val price: String?
)