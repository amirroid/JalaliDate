package ir.amirroid.jalalidate.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal fun getCurrentLocalDateTime(): LocalDateTime {
    val nowInstant = Clock.System.now()
    val timeZone = TimeZone.currentSystemDefault()
    return nowInstant.toLocalDateTime(timeZone)
}