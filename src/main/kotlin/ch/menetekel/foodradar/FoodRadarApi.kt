package ch.menetekel.foodradar

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/api/food-radar")
class FoodRadarApi(
    private val dataCollectors: DataCollectors
) {

    @GetMapping("/dreiganger")
    fun getDreigaengerMenu(): Flux<Menu> =
        dataCollectors.fetchDreigaengerMenu()

    @GetMapping("/schichtwechsel")
    fun getSchichtwechselMenu(): Flux<Menu> =
        dataCollectors.fetchSchichtWechselMenu()

    @GetMapping("/le-beizli")
    fun getLeBeizliMenu(): Flux<Menu> {
        TODO()
    }
}

