package ch.menetekel.foodradar

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableConfigurationProperties
class FoodRadar

fun main(args: Array<String>) {
    runApplication<FoodRadar>(*args)
}
