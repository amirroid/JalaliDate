package ir.amirroid.jalalidate.formatter

import ir.amirroid.jalalidate.algorithm.JalaliAlgorithm
import ir.amirroid.jalalidate.configuration.JalaliDateGlobalConfiguration
import ir.amirroid.jalalidate.date.JalaliDateTime

public enum class Padding { ZERO, SPACE }

public class JalaliDateTimeFormatter {
    private val parts = mutableListOf<FormatPart>()

    public fun byUnicodePattern(pattern: String): JalaliDateTimeFormatter {
        val regex = "(yyyy|yy|MMMM|MMM|MM|M|dd|d|HH|H|mm|m|ss|s)".toRegex()
        var lastIndex = 0
        for (match in regex.findAll(pattern)) {
            if (lastIndex < match.range.first) {
                parts += LiteralPart(pattern.substring(lastIndex, match.range.first))
            }
            parts += mapPatternToFormatPart(match.value)
            lastIndex = match.range.last + 1
        }
        if (lastIndex < pattern.length) {
            parts += LiteralPart(pattern.substring(lastIndex))
        }
        return this
    }

    public fun year(padding: Padding = Padding.ZERO): JalaliDateTimeFormatter =
        addNumericPart("year", { it.jalaliYear }, 4, padding)

    public fun monthTwoDigit(padding: Padding = Padding.ZERO): JalaliDateTimeFormatter =
        addNumericPart("month", { it.jalaliMonth }, 2, padding)

    public fun monthOneDigit(): JalaliDateTimeFormatter =
        addNumericPart("month", { it.jalaliMonth }, 1, Padding.ZERO)

    public fun monthFullName(): JalaliDateTimeFormatter {
        parts += MonthNamePart(full = true)
        return this
    }

    public fun monthShortName(): JalaliDateTimeFormatter {
        parts += MonthNamePart(full = false)
        return this
    }

    public fun dayTwoDigit(padding: Padding = Padding.ZERO): JalaliDateTimeFormatter =
        addNumericPart("day", { it.jalaliDay }, 2, padding)

    public fun dayOneDigit(): JalaliDateTimeFormatter =
        addNumericPart("day", { it.jalaliDay }, 1, Padding.ZERO)

    public fun hour(padding: Padding = Padding.ZERO): JalaliDateTimeFormatter =
        addNumericPart("hour", { it.hour }, 2, padding)

    public fun minute(padding: Padding = Padding.ZERO): JalaliDateTimeFormatter =
        addNumericPart("minute", { it.minute }, 2, padding)

    public fun second(padding: Padding = Padding.ZERO): JalaliDateTimeFormatter =
        addNumericPart("second", { it.second }, 2, padding)

    public fun chars(literal: String): JalaliDateTimeFormatter {
        parts += LiteralPart(literal)
        return this
    }

    private fun addNumericPart(
        name: String,
        extractor: (JalaliDateTime) -> Int,
        length: Int,
        padding: Padding
    ): JalaliDateTimeFormatter {
        parts += NumericPart(name, extractor, length, padding)
        return this
    }

    private fun mapPatternToFormatPart(token: String): FormatPart = when (token) {
        "yyyy" -> NumericPart("year", { it.jalaliYear }, 4, Padding.ZERO)
        "yy" -> NumericPart("yearShort", { it.jalaliYear % 100 }, 2, Padding.ZERO)

        "MMMM" -> MonthNamePart(full = true)
        "MMM" -> MonthNamePart(full = false)

        "MM" -> NumericPart("month", { it.jalaliMonth }, 2, Padding.ZERO)
        "M" -> NumericPart("month", { it.jalaliMonth }, 1, Padding.ZERO)

        "dd" -> NumericPart("day", { it.jalaliDay }, 2, Padding.ZERO)
        "d" -> NumericPart("day", { it.jalaliDay }, 1, Padding.ZERO)

        "HH" -> NumericPart("hour", { it.hour }, 2, Padding.ZERO)
        "H" -> NumericPart("hour", { it.hour }, 1, Padding.ZERO)

        "mm" -> NumericPart("minute", { it.minute }, 2, Padding.ZERO)
        "m" -> NumericPart("minute", { it.minute }, 1, Padding.ZERO)

        "ss" -> NumericPart("second", { it.second }, 2, Padding.ZERO)
        "s" -> NumericPart("second", { it.second }, 1, Padding.ZERO)

        else -> LiteralPart(token)
    }

    internal fun format(date: JalaliDateTime): String =
        parts.joinToString(separator = "") { it.format(date) }

    internal fun parse(
        input: String,
        algorithm: JalaliAlgorithm = JalaliDateGlobalConfiguration.convertAlgorithm
    ): JalaliDateTime {
        var pos = 0
        val values = mutableMapOf<String, Int>()

        for (part in parts) {
            val result = part.parse(input, pos)
            pos = result.nextPos
            result.value?.let { value ->
                part.name?.let { name ->
                    values[name] = value
                }
            }
        }

        val year = values["year"] ?: 1300
        val month = values["month"] ?: 1
        val day = values["day"] ?: 1
        val hour = values["hour"] ?: 0
        val minute = values["minute"] ?: 0
        val second = values["second"] ?: 0

        return JalaliDateTime(year, month, day, hour, minute, second, algorithm)
    }
}