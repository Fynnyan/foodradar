package ch.menetekel.foodradar

import ch.menetekel.foodradar.jooq.tables.pojos.FoodTruck
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate

@RestController
@RequestMapping("/api")
class FoodRadarApi(
    private val dataCollectors: DataCollectors,
    private val dataRepository: DataRepository
) {

    @GetMapping("/place/dreiganger")
    fun getDreigaengerMenu(): Mono<Place> =
        dataCollectors.fetchDreigaengerMenu()

    @GetMapping("/place/schichtwechsel")
    fun getSchichtwechselMenu(): Mono<Place> =
        dataCollectors.fetchSchichtWechselMenu()

    @GetMapping("/place/le-beizli")
    fun getLeBeizliMenu(): Mono<Place> =
        dataCollectors.getLeBeizli()

    @GetMapping("/food-trucks")
    fun getAllFoodTrucks(): Flux<FoodTruck> {
        return dataRepository.getFoodTrucks()
    }

    @GetMapping("/food-trucks/today")
    fun getTodaysFoodTrucks(): Flux<FoodTruck> {
        return dataRepository.getFoodTrucks(LocalDate.now().dayOfWeek)
    }
}

