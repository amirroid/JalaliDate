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
        val currentIso = gregorian.dayOfWeek.isoDayNumber
        val startIso = weekStartDay.isoDayNumber
        val shifted = (currentIso - startIso + 7) % 7
        val daysByIso = DayOfWeek.entries
        val startIndex = daysByIso.indexOf(weekStartDay)
        val finalIndex = (startIndex + shifted) % 7
        return daysByIso[finalIndex]
    }

    public fun dayOfWeekNumber(weekStartDay: DayOfWeek = DayOfWeek.SATURDAY): Int {
        val currentIso = gregorian.dayOfWeek.isoDayNumber
        val startIso = weekStartDay.isoDayNumber
        return ((currentIso - startIso + 7) % 7) + 1
    }


    private fun monthDaysUntil(month: Int): Int {
        return jalaliMonthLengths.take(month - 1).sum()
    }


    override fun equals(other: Any?): Boolean {
        return other is JalaliDateTime && other.toString() == toString()
    }

    override fun toString(): String =
        "JalaliDate(year=$jalaliYear, month=$jalaliMonth, day=$jalaliDay, hour=$hour, minute=$minute, second=$second)"

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
    public fun plus(amount: Int, unit: DateTimeUnit): JalaliDateTime {
        if (amount == 0) return this
        return when (unit) {
            DateTimeUnit.YEAR -> plusYears(amount)
            DateTimeUnit.MONTH -> plusMonths(amount)
            DateTimeUnit.WEEK -> plusWeeks(amount)
            DateTimeUnit.DAY -> plusDays(amount)
            else ->
                fromGregorian(
                    gregorian.plus(amount, unit),
                    algorithm
                )
        }
    }

    public fun minus(amount: Int, unit: DateTimeUnit): JalaliDateTime {
        if (amount == 0) return this
        return when (unit) {
            DateTimeUnit.YEAR -> minusYears(amount)
            DateTimeUnit.MONTH -> minusMonths(amount)
            DateTimeUnit.WEEK -> minusWeeks(amount)
            DateTimeUnit.DAY -> minusDays(amount)
            else ->
                fromGregorian(
                    gregorian.minus(amount, unit),
                    algorithm
                )
        }
    }


    public operator fun plus(interval: DateTimeInterval): JalaliDateTime =
        plus(amount = interval.amount, unit = interval.unit)

    public operator fun minus(interval: DateTimeInterval): JalaliDateTime =
        minus(amount = interval.amount, unit = interval.unit)


    public fun plusDays(days: Int): JalaliDateTime = plusJalaliCalendarDays(days)
    public fun minusDays(days: Int): JalaliDateTime = minusJalaliCalendarDays(days)


    public fun plusWeeks(weeks: Int): JalaliDateTime = plusJalaliCalendarDays(weeks * 7)
    public fun minusWeeks(weeks: Int): JalaliDateTime = minusJalaliCalendarDays(weeks * 7)

    public fun plusMonths(months: Int): JalaliDateTime = plusJalaliCalendarMonths(months)
    public fun minusMonths(months: Int): JalaliDateTime = minusJalaliCalendarMonths(months)

    public fun plusYears(years: Int): JalaliDateTime = plusJalaliCalendarYears(years)
    public fun minusYears(years: Int): JalaliDateTime = minusJalaliCalendarYears(years)

    public fun plusHours(hours: Int): JalaliDateTime = plus(hours, DateTimeUnit.HOUR)
    public fun minusHours(hours: Int): JalaliDateTime = minus(hours, DateTimeUnit.HOUR)

    public fun plusMinutes(minutes: Int): JalaliDateTime = plus(minutes, DateTimeUnit.MINUTE)
    public fun minusMinutes(minutes: Int): JalaliDateTime = minus(minutes, DateTimeUnit.MINUTE)

    public fun plusSeconds(seconds: Int): JalaliDateTime = plus(seconds, DateTimeUnit.SECOND)
    public fun minusSeconds(seconds: Int): JalaliDateTime = minus(seconds, DateTimeUnit.SECOND)

    private fun plusJalaliCalendarDays(days: Int): JalaliDateTime {
        if (days == 0) return this
        return fromGregorian(gregorian.plus(days, DateTimeUnit.DAY), algorithm)
    }

    private fun minusJalaliCalendarDays(days: Int): JalaliDateTime =
        plusJalaliCalendarDays(-days)

    private fun plusJalaliCalendarMonths(months: Int): JalaliDateTime {
        if (months == 0) return this
        val totalMonths = (jalaliYear * 12 + (jalaliMonth - 1)) + months
        val newYear = totalMonths / 12
        val newMonth = totalMonths % 12 + 1

        val maxDay = maxDayInMonth(newYear, newMonth)
        val newDay = if (jalaliDay > maxDay) maxDay else jalaliDay

        return JalaliDateTime(newYear, newMonth, newDay, hour, minute, second, algorithm)
    }

    private fun minusJalaliCalendarMonths(months: Int): JalaliDateTime =
        plusJalaliCalendarMonths(-months)

    private fun plusJalaliCalendarYears(years: Int): JalaliDateTime {
        if (years == 0) return this
        val newYear = jalaliYear + years
        val maxDay = maxDayInMonth(newYear, jalaliMonth)
        val newDay = minOf(jalaliDay, maxDay)
        return JalaliDateTime(newYear, jalaliMonth, newDay, hour, minute, second, algorithm)
    }

    private fun minusJalaliCalendarYears(years: Int): JalaliDateTime =
        plusJalaliCalendarYears(-years)

    private fun maxDayInMonth(year: Int = jalaliYear, month: Int = jalaliMonth): Int {
        return if (month == 12 && algorithm.isJalaliLeapYear(year)) 30 else jalaliMonthLengths[month - 1]
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

        public val jalaliMonthShortNamesEnglish: List<String> = listOf(
            "Far", "Ord", "Khr",
            "Tir", "Mor", "Shr",
            "Mehr", "Abn", "Azr",
            "Dey", "Bah", "Esf"
        )

        public val jalaliDayNames: List<String> = listOf(
            "شنبه", "یک‌شنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنج‌شنبه", "جمعه"
        )

        public val jalaliDayNamesEnglish: List<String> = listOf(
            "Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"
        )

        public val jalaliDayNamesShort: List<String> = listOf(
            "ش", "ی", "د", "س", "چ", "پ", "ج"
        )

        public val jalaliDayNamesEnglishShort: List<String> = listOf(
            "Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri"
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

    override fun hashCode(): Int {
        var result = gregorianYear
        result = 31 * result + gregorianMonth
        result = 31 * result + gregorianDay
        result = 31 * result + jalaliYear
        result = 31 * result + jalaliMonth
        result = 31 * result + jalaliDay
        result = 31 * result + hour
        result = 31 * result + minute
        result = 31 * result + second
        result = 31 * result + algorithm.hashCode()
        result = 31 * result + weekOfYear
        result = 31 * result + isJalaliLeapYear.hashCode()
        result = 31 * result + isGregorianLeapYear.hashCode()
        result = 31 * result + monthLength
        result = 31 * result + gregorian.hashCode()
        result = 31 * result + monthName.hashCode()
        return result
    }
}