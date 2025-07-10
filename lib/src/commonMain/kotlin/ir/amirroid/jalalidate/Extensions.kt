package ir.amirroid.jalalidate

import ir.amirroid.jalalidate.algorithm.JalaliAlgorithm
import ir.amirroid.jalalidate.configuration.JalaliDateGlobalConfiguration
import ir.amirroid.jalalidate.date.JalaliDateTime
import ir.amirroid.jalalidate.formatter.JalaliDateTimeFormatter
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

public fun LocalDateTime.toJalaliDateTime(algorithm: JalaliAlgorithm = JalaliDateGlobalConfiguration.convertAlgorithm): JalaliDateTime {
    return JalaliDateTime.fromGregorian(datetime = this, algorithm = algorithm)
}


public fun LocalDate.toJalaliDateTime(algorithm: JalaliAlgorithm = JalaliDateGlobalConfiguration.convertAlgorithm): JalaliDateTime {
    return algorithm.fromGregorian(date = atTime(LocalTime(0, 0)))
}

public fun JalaliDateTime.format(block: JalaliDateTimeFormatter.() -> Unit): String {
    val formatter = JalaliDateTimeFormatter().apply(block)
    return formatter.format(this)
}

@OptIn(ExperimentalTime::class)
internal fun LocalDateTime.plus(
    amount: Int,
    unit: DateTimeUnit,
    timeZone: TimeZone = TimeZone.currentSystemDefault()
): LocalDateTime {
    val instant = toInstant(timeZone)
    val newInstant = instant.plus(amount, unit, timeZone)
    return newInstant.toLocalDateTime(timeZone)
}

@OptIn(ExperimentalTime::class)
internal fun LocalDateTime.minus(
    amount: Int,
    unit: DateTimeUnit,
    timeZone: TimeZone = TimeZone.currentSystemDefault()
): LocalDateTime {
    val instant = toInstant(timeZone)
    val newInstant = instant.minus(amount, unit, timeZone)
    return newInstant.toLocalDateTime(timeZone)
}