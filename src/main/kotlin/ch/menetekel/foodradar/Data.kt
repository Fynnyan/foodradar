package ch.menetekel.foodradar

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

data class Place(
    val name: String,
    val web: String,
    val menus: List<Menu>,
    val processingStatus: ProcessingStatus
) {
    companion object {
        fun from(metaData: PlacesConfig.PlacesMetaData, menus: List<Menu>, processingStatus: ProcessingStatus) =
            Place(
                metaData.name,
                metaData.web,
                menus,
                processingStatus
            )
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

enum class ProcessingStatus {
    SITE_NOT_ACCESSIBLE,
    PROCESS_ERROR,
    PROCESSED
}

@ConfigurationProperties(prefix = "places")
data class PlacesConfig @ConstructorBinding constructor(
    val dreigaenger: PlacesMetaData,
    val schichtwechsel: PlacesMetaData
) {
    data class PlacesMetaData(
        val name: String,
        val web: String,
        val scrapeAddress: String
    )
}