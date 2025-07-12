package ir.amirroid.jalalidate.formatter

import ir.amirroid.jalalidate.date.JalaliDateTime
import ir.amirroid.jalalidate.date.JalaliDateTime.Companion.jalaliDayNames
import ir.amirroid.jalalidate.date.JalaliDateTime.Companion.jalaliDayNamesEnglish
import ir.amirroid.jalalidate.date.JalaliDateTime.Companion.jalaliDayNamesEnglishShort
import ir.amirroid.jalalidate.date.JalaliDateTime.Companion.jalaliDayNamesShort
import ir.amirroid.jalalidate.date.JalaliDateTime.Companion.jalaliMonthNames
import ir.amirroid.jalalidate.date.JalaliDateTime.Companion.jalaliMonthNamesEnglish
import ir.amirroid.jalalidate.date.JalaliDateTime.Companion.jalaliMonthShortNames
import ir.amirroid.jalalidate.date.JalaliDateTime.Companion.jalaliMonthShortNamesEnglish
import kotlinx.datetime.DayOfWeek


internal data class ParseResult(val value: Int?, val nextPos: Int)

internal sealed interface FormatPart {
    val name: String?

    fun format(date: JalaliDateTime): String
    fun parse(input: String, pos: Int): ParseResult
}

internal class LiteralPart(val literal: String) : FormatPart {
    override val name: String = "literal$literal"
    override fun format(date: JalaliDateTime) = literal
    override fun parse(input: String, pos: Int): ParseResult {
        if (input.startsWith(literal, pos)) {
            return ParseResult(null, pos + literal.length)
        }
        throw IllegalArgumentException("Expected literal '$literal' at position $pos")
    }
}

internal class NumericPart(
    override val name: String,
    val extractor: (JalaliDateTime) -> Int,
    val length: Int,
    val padding: Padding = Padding.ZERO
) : FormatPart {
    override fun format(date: JalaliDateTime): String {
        val value = extractor(date)
        return when (padding) {
            Padding.ZERO -> value.toString().padStart(length, '0')
            Padding.SPACE -> value.toString().padStart(length, ' ')
        }
    }

    override fun parse(input: String, pos: Int): ParseResult {
        val endPos = pos + length
        if (endPos > input.length) throw IllegalArgumentException("Input too short for numeric part at $pos")
        val substring = input.substring(pos, endPos)
        val number = substring.trim().toIntOrNull()
            ?: throw IllegalArgumentException("Invalid number at $pos")
        return ParseResult(number, endPos)
    }
}

internal sealed class NamedPart(
    private val full: Boolean,
    private val locale: Locale
) : FormatPart {

    protected abstract val namesEnglishFull: List<String>
    protected abstract val namesEnglishShort: List<String>
    protected abstract val namesPersianFull: List<String>
    protected abstract val namesPersianShort: List<String>

    private val names: List<String> by lazy {
        when (locale) {
            Locale.ENGLISH -> if (full) namesEnglishFull else namesEnglishShort
            Locale.PERSIAN -> if (full) namesPersianFull else namesPersianShort
        }
    }

    protected abstract fun extractIndex(date: JalaliDateTime): Int
    protected abstract fun applyParsedIndex(index: Int): Int
    abstract override val name: String

    override fun format(date: JalaliDateTime): String =
        names.getOrNull(extractIndex(date)) ?: ""

    override fun parse(input: String, pos: Int): ParseResult {
        for ((index, partName) in names.withIndex()) {
            if (input.startsWith(partName, pos)) {
                return ParseResult(applyParsedIndex(index), pos + partName.length)
            }
        }
        throw IllegalArgumentException("Invalid $name name at position $pos")
    }
}

internal class MonthNamePart(full: Boolean, locale: Locale) : NamedPart(full, locale) {
    override val namesEnglishFull = jalaliMonthNamesEnglish
    override val namesEnglishShort = jalaliMonthShortNamesEnglish
    override val namesPersianFull = jalaliMonthNames
    override val namesPersianShort = jalaliMonthShortNames
    override val name: String = "month"

    override fun extractIndex(date: JalaliDateTime): Int = date.jalaliMonth - 1
    override fun applyParsedIndex(index: Int): Int = index + 1
}

internal class WeekDayNamePart(full: Boolean, locale: Locale) : NamedPart(full, locale) {
    override val namesEnglishFull = jalaliDayNamesEnglish
    override val namesEnglishShort = jalaliDayNamesEnglishShort
    override val namesPersianFull = jalaliDayNames
    override val namesPersianShort = jalaliDayNamesShort
    override val name: String = "weekday"

    override fun extractIndex(date: JalaliDateTime): Int = date.dayOfWeekNumber() - 1

    override fun applyParsedIndex(index: Int): Int = index
}