package ir.amirroid.jalalidate.date

import kotlinx.datetime.DateTimeUnit

public data class DateTimeInterval(val amount: Int, val unit: DateTimeUnit)

public val Int.seconds: DateTimeInterval
    get() = DateTimeInterval(amount = this, unit = DateTimeUnit.SECOND)

public val Int.minutes: DateTimeInterval
    get() = DateTimeInterval(amount = this, unit = DateTimeUnit.MINUTE)

public val Int.hours: DateTimeInterval
    get() = DateTimeInterval(amount = this, unit = DateTimeUnit.HOUR)

public val Int.days: DateTimeInterval
    get() = DateTimeInterval(amount = this, unit = DateTimeUnit.DAY)

public val Int.weeks: DateTimeInterval
    get() = DateTimeInterval(amount = this, unit = DateTimeUnit.WEEK)

public val Int.months: DateTimeInterval
    get() = DateTimeInterval(amount = this, unit = DateTimeUnit.MONTH)

public val Int.years: DateTimeInterval
    get() = DateTimeInterval(amount = this, unit = DateTimeUnit.YEAR)