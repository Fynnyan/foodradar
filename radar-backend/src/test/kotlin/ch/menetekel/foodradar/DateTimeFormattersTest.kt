package ch.menetekel.foodradar

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.SignStyle
import java.time.temporal.ChronoField

class DateTimeFormattersTest {

    @Test
    fun `The custom date format can parse the local dates string of the leBeizli`() {

        val dateString = "Montag 4.September 2023"

        val date = LocalDate.parse(
            dateString,
            DateTimeFormatters.LE_BEIZLI_DATE
        )

        assertThat(date).isEqualTo(LocalDate.of(2023, 9, 4))

    }
    @ParameterizedTest
    @CsvSource(
        "Dienstag 5.September 2023, 2023-09-05",
        "Mittwoch 6.September 2023, 2023-09-06",
        "Donnerstag 5.Oktober 2023, 2023-10-05",
        "Freitag 16.Juni 2023, 2023-06-16",
        "Montag 25.Dezember 2023, 2023-12-25",
        "'Montag, 25.Dezember 2023', 2023-12-25",
        "'Montag, 25. Dezember 2023', 2023-12-25",
        "'Montag 25. Dezember 2023', 2023-12-25",
    )
    fun `Can parse various dates in the format that the leBeisl uses`(
        dateString: String,
        expectedDate: String
    ) {
        val date = LocalDate.parse(dateString, DateTimeFormatters.LE_BEIZLI_DATE)
        assertThat(date.toString()).isEqualTo(expectedDate)
    }

    @ParameterizedTest
    @CsvSource(
        "'Montag, 04.09.2023', 2023-09-04",
        "'Dienstag, 05.09.2023', 2023-09-05",
        "'Mittwoch, 06.09.2023', 2023-09-06",
        "'Donnerstag, 07.09.2023', 2023-09-07",
        "'Freitag, 08.09.2023', 2023-09-08",
    )
    fun `Can parse various dates in the dreiganger date format`(
        dateString: String,
        expectedDate: String
    ) {
        val date = LocalDate.parse(dateString, DateTimeFormatters.DREIGAENGER_DATE)
        assertThat(date.toString()).isEqualTo(expectedDate)
    }
}