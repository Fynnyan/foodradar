package ch.menetekel.foodradar

import ch.menetekel.foodradar.jooq.tables.pojos.FoodTruck
import ch.menetekel.foodradar.jooq.tables.records.FoodTruckRecord
import ch.menetekel.foodradar.jooq.tables.references.FOOD_TRUCK
import org.jooq.Converter
import org.jooq.DSLContext
import org.jooq.impl.DSL.*
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toFlux
import java.time.DayOfWeek
import java.util.*

@Repository
class DataRepository(
    private val jooq: DSLContext
) {
    init {
        jooq.batchInsert(
            FoodTruckRecord(
                id = UUID.randomUUID(),
                name = "FireChefs",
                web = "https://www.firechefs.ch/food-truck",
                location = "Liebefeld Park",
                days = arrayOf(DayOfWeek.TUESDAY)
            ),
            FoodTruckRecord(
                id = UUID.randomUUID(),
                name = "Gabriele",
                web = "https://www.gabriele-streetfood.ch/",
                location = "Liebefeld Bahnhof",
                days = arrayOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)
            ),
            FoodTruckRecord(
                id = UUID.randomUUID(),
                name = "Mê - vietnamese cuisine",
                web = "https://mevietnam.ch/",
                location = "Liebefeld Bahnhof",
                days = arrayOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)
            )
        ).execute()
    }

    fun getFoodTrucks(day: DayOfWeek? = null): Flux<FoodTruck> {
        return jooq.selectFrom(FOOD_TRUCK)
            .where(trueCondition())
            .let {
                if (day != null) it.and(field("ARRAY_CONTAINS(?,?)", Boolean::class.java, FOOD_TRUCK.DAYS, day.value))
                else it
            }
            .fetch { FoodTruck(it.id, it.name, it.web, it.location, it.days) }
            .toFlux()
    }
}

class DayOfWeekConverter : Converter<Array<Int>, Array<DayOfWeek>> {
    override fun from(databaseObject: Array<Int>?): Array<DayOfWeek>? =
        databaseObject?.map { DayOfWeek.of(it) }?.toTypedArray()

    override fun to(userObject: Array<DayOfWeek>?): Array<Int>? =
        userObject?.map { it.value }?.toTypedArray()

    override fun fromType(): Class<Array<Int>> =
        Array<Int>::class.java

    override fun toType(): Class<Array<DayOfWeek>> =
        Array<DayOfWeek>::class.java
}