package ch.menetekel.foodradar

import java.time.format.DateTimeFormatterBuilder
import java.time.format.SignStyle
import java.time.temporal.ChronoField

object DateTimeFormatters {

    val dayOfWeekGerman = mapOf(
        1L to "Montag",
        2L to "Dienstag",
        3L to "Mittwoch",
        4L to "Donnerstag",
        5L to "Freitag",
        6L to "Samstag",
        7L to "Sonnatag",
    )
    val monthGerman = mapOf(
        1L to "Januar",
        2L to "Februar",
        3L to "März",
        4L to "April",
        5L to "Mai",
        6L to "Juni",
        7L to "Juli",
        8L to "August",
        9L to "September",
        10L to "Oktober",
        11L to "November",
        12L to "Dezember",
    )

    /**
     * expected datete format: Dienstag 5.September 2023
     */
    val LE_BEIZLI_DATE = DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .parseLenient()
        .optionalStart()
        .appendText(ChronoField.DAY_OF_WEEK, dayOfWeekGerman)
        .optionalEnd()
        // can be a space with or without a comma
        .optionalStart()
        .appendLiteral(" ")
        .optionalEnd()
        .optionalStart()
        .appendLiteral(", ")
        .optionalEnd()
        .appendValue(ChronoField.DAY_OF_MONTH, 1, 2, SignStyle.NOT_NEGATIVE)
        // after the day digit, can have a . with or without space before the month
        .optionalStart()
        .appendLiteral(". ")
        .optionalEnd()
        .optionalStart()
        .appendLiteral(".")
        .optionalEnd()
        .appendText(ChronoField.MONTH_OF_YEAR, monthGerman)
        .appendLiteral(' ')
        .appendValue(ChronoField.YEAR, 4)
        .toFormatter()

    /**
     * expected datete format: Montag, 04.09.2023
     */
    val DREIGAENGER_DATE = DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .parseLenient()
        .optionalStart()
        .appendText(ChronoField.DAY_OF_WEEK, dayOfWeekGerman)
        .appendLiteral(", ")
        .optionalEnd()
        .appendValue(ChronoField.DAY_OF_MONTH, 2)
        .appendLiteral('.')
        .appendValue(ChronoField.MONTH_OF_YEAR, 2)
        .appendLiteral('.')
        .appendValue(ChronoField.YEAR, 4)
        .toFormatter()
}