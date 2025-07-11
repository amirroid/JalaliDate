package ir.amirroid.jalalidate.date

import ir.amirroid.jalalidate.algorithm.JalaliAlgorithm
import ir.amirroid.jalalidate.configuration.JalaliDateGlobalConfiguration
import ir.amirroid.jalalidate.formatter.JalaliDateTimeFormatter
import ir.amirroid.jalalidate.minus
import ir.amirroid.jalalidate.models.MonthName
import ir.amirroid.jalalidate.plus
import ir.amirroid.jalalidate.utils.getCurrentLocalDateTime
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.number

public class JalaliDateTime {
    public val gregorianYear: Int
    public val gregorianMonth: Int
    public val gregorianDay: Int


    public val jalaliYear: Int
    public val jalaliMonth: Int
    public val jalaliDay: Int
    public val algorithm: JalaliAlgorithm

    public val hour: Int
    public val minute: Int
    public val second: Int

    public val gregorian: LocalDateTime
        get() = algorithm.toGregorian(this)

    public val weekOfYear: Int
        get() {
            val daysSinceYearStart = jalaliDay - 1 + monthDaysUntil(jalaliMonth)
            return (daysSinceYearStart / 7) + 1
        }

    public val isJalaliLeapYear: Boolean
        get() = algorithm.isJalaliLeapYear(jalaliYear)

    public val isGregorianLeapYear: Boolean
        get() = algorithm.isGregorianLeap(gregorianYear)

    public val monthLength: Int
        get() = maxDayInMonth()

    public val monthName: MonthName
        get() = MonthName(
            english = jalaliMonthNamesEnglish[jalaliMonth - 1],
            persian = jalaliMonthNames[jalaliMonth - 1],
        )

    public fun dayOfWeek(weekStartDay: DayOfWeek = DayOfWeek.SATURDAY): DayOfWeek {
        val current = gregorian.dayOfWeek.isoDayNumber
        val start = weekStartDay.isoDayNumber
        val shiftedIndex = ((current - start + 7) % 7)
        return DayOfWeek.entries[shiftedIndex]
    }

    public fun dayOfWeekNumber(weekStartDay: DayOfWeek = DayOfWeek.SATURDAY): Int {
        val currentDay = gregorian.dayOfWeek.isoDayNumber
        val startDay = weekStartDay.isoDayNumber
        return ((currentDay - startDay + 7) % 7) + 1
    }

    public fun monthDaysUntil(month: Int): Int {
        return jalaliMonthLengths.take(month - 1).sum()
    }

    override fun toString(): String = "JalaliDate($jalaliYear, $jalaliMonth, $jalaliDay)"

    public fun copyGregorian(
        year: Int = gregorianYear,
        month: Int = gregorianMonth,
        day: Int = gregorianDay,
        hour: Int = this.hour,
        minute: Int = this.minute,
        second: Int = this.second,
    ): JalaliDateTime {
        return if (year == gregorianYear && month == gregorianMonth && day == gregorianYear && this.hour == hour && this.minute == minute && this.second == second)
            this
        else LocalDateTime(
            year, month, day,
            hour, minute, second
        ).let { fromGregorian(datetime = it, algorithm = algorithm) }
    }

    public fun copyJalali(
        year: Int = jalaliYear,
        month: Int = jalaliMonth,
        day: Int = jalaliDay,
        hour: Int = this.hour,
        minute: Int = this.minute,
        second: Int = this.second,
    ): JalaliDateTime {
        return if (
            year == jalaliYear &&
            month == jalaliMonth &&
            day == jalaliDay &&
            hour == this.hour &&
            minute == this.minute &&
            second == this.second
        ) this
        else JalaliDateTime(year, month, day, hour, minute, second, algorithm)
    }


    // Operators
    public fun plus(amount: Int, unit: DateTimeUnit): JalaliDateTime = fromGregorian(
        gregorian.plus(amount, unit),
        algorithm
    )

    public fun minus(amount: Int, unit: DateTimeUnit): JalaliDateTime = fromGregorian(
        gregorian.minus(amount, unit),
        algorithm
    )


    public operator fun plus(interval: DateTimeInterval): JalaliDateTime =
        plus(amount = interval.amount, unit = interval.unit)

    public operator fun minus(interval: DateTimeInterval): JalaliDateTime =
        minus(amount = interval.amount, unit = interval.unit)


    public fun plusDays(days: Int): JalaliDateTime = plus(days, DateTimeUnit.DAY)
    public fun minusDays(days: Int): JalaliDateTime = minus(days, DateTimeUnit.DAY)


    public fun plusWeeks(weeks: Int): JalaliDateTime = plus(weeks, DateTimeUnit.WEEK)
    public fun minusWeeks(weeks: Int): JalaliDateTime = minus(weeks, DateTimeUnit.WEEK)

    public fun plusMonths(months: Int): JalaliDateTime = plus(months, DateTimeUnit.MONTH)
    public fun minusMonths(months: Int): JalaliDateTime = minus(months, DateTimeUnit.MONTH)

    public fun plusYears(years: Int): JalaliDateTime = plus(years, DateTimeUnit.YEAR)
    public fun minusYears(years: Int): JalaliDateTime = minus(years, DateTimeUnit.YEAR)

    public fun plusHours(hours: Int): JalaliDateTime = plus(hours, DateTimeUnit.HOUR)
    public fun minusHours(hours: Int): JalaliDateTime = minus(hours, DateTimeUnit.HOUR)

    public fun plusMinutes(minutes: Int): JalaliDateTime = plus(minutes, DateTimeUnit.MINUTE)
    public fun minusMinutes(minutes: Int): JalaliDateTime = minus(minutes, DateTimeUnit.MINUTE)

    public fun plusSeconds(seconds: Int): JalaliDateTime = plus(seconds, DateTimeUnit.SECOND)
    public fun minusSeconds(seconds: Int): JalaliDateTime = minus(seconds, DateTimeUnit.SECOND)

    private fun maxDayInMonth(): Int {
        return if (jalaliMonth == 12 && isJalaliLeapYear) 30 else jalaliMonthLengths[jalaliMonth - 1]
    }

    public companion object {
        public fun fromGregorian(
            datetime: LocalDateTime,
            algorithm: JalaliAlgorithm = JalaliDateGlobalConfiguration.convertAlgorithm
        ): JalaliDateTime {
            return algorithm.fromGregorian(datetime)
        }

        public fun now(algorithm: JalaliAlgorithm = JalaliDateGlobalConfiguration.convertAlgorithm): JalaliDateTime =
            fromGregorian(getCurrentLocalDateTime(), algorithm)

        @Suppress("FunctionName")
        public fun Format(block: JalaliDateTimeFormatter.() -> Unit): JalaliDateTimeFormatter {
            return JalaliDateTimeFormatter().apply(block)
        }

        public val jalaliMonthLengths: IntArray =
            intArrayOf(31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29)

        public val jalaliMonthNames: List<String> = listOf(
            "فروردین", "اردیبهشت", "خرداد",
            "تیر", "مرداد", "شهریور",
            "مهر", "آبان", "آذر",
            "دی", "بهمن", "اسفند"
        )
        public val jalaliMonthNamesEnglish: List<String> = listOf(
            "Farvardin", "Ordibehesht", "Khordad",
            "Tir", "Mordad", "Shahrivar",
            "Mehr", "Aban", "Azar",
            "Dey", "Bahman", "Esfand"
        )
        public val jalaliMonthShortNames: List<String> = listOf(
            "فرو", "ارد", "خرد",
            "تیر", "مرد", "شهر",
            "مه", "آبا", "آذر",
            "دی", "بهم", "اسف"
        )
    }

    public constructor(
        year: Int,
        month: Int,
        day: Int,
        algorithm: JalaliAlgorithm = JalaliDateGlobalConfiguration.convertAlgorithm
    ) {
        this.algorithm = algorithm
        this.jalaliYear = year
        this.jalaliMonth = month
        this.jalaliDay = day

        val gregorianDate = gregorian
        gregorianYear = gregorianDate.year
        gregorianMonth = gregorianDate.month.number
        gregorianDay = gregorianDate.day
        hour = gregorianDate.hour
        minute = gregorianDate.minute
        second = gregorianDate.second
    }

    public constructor(
        year: Int,
        month: Int,
        day: Int,
        hour: Int,
        minute: Int,
        second: Int,
        algorithm: JalaliAlgorithm = JalaliDateGlobalConfiguration.convertAlgorithm
    ) {
        this.algorithm = algorithm
        this.jalaliYear = year
        this.jalaliMonth = month
        this.jalaliDay = day
        this.hour = hour
        this.minute = minute
        this.second = second

        val gregorianDate = gregorian
        gregorianYear = gregorianDate.year
        gregorianMonth = gregorianDate.month.number
        gregorianDay = gregorianDate.day
    }

    internal constructor(
        jYear: Int,
        jMonth: Int,
        jDay: Int,
        gYear: Int,
        gMonth: Int,
        gDay: Int,
        hour: Int,
        minute: Int,
        second: Int,
        algorithm: JalaliAlgorithm = JalaliDateGlobalConfiguration.convertAlgorithm
    ) {
        this.algorithm = algorithm
        this.jalaliYear = jYear
        this.jalaliMonth = jMonth
        this.jalaliDay = jDay
        this.gregorianYear = gYear
        this.gregorianMonth = gMonth
        this.gregorianDay = gDay
        this.hour = hour
        this.minute = minute
        this.second = second
    }
}