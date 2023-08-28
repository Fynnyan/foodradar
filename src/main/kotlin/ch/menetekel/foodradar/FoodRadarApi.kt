package ch.menetekel.foodradar

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/food-radar")
class FoodRadarApi(
    private val dataCollectors: DataCollectors
) {

    @GetMapping("/dreiganger")
    fun getDreigaengerMenu(): Mono<Place> =
        dataCollectors.fetchDreigaengerMenu()

    @GetMapping("/schichtwechsel")
    fun getSchichtwechselMenu(): Mono<Place> =
        dataCollectors.fetchSchichtWechselMenu()

    @GetMapping("/le-beizli")
    fun getLeBeizliMenu(): Flux<Menu> {
        TODO()
    }
}

