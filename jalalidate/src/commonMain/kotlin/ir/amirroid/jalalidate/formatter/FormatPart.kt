package ir.amirroid.jalalidate.formatter

import ir.amirroid.jalalidate.date.JalaliDateTime
import ir.amirroid.jalalidate.date.JalaliDateTime.Companion.jalaliMonthNames
import ir.amirroid.jalalidate.date.JalaliDateTime.Companion.jalaliMonthShortNames


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

internal class MonthNamePart(val full: Boolean) : FormatPart {
    private val names = if (full) jalaliMonthNames else jalaliMonthShortNames
    override val name: String get() = "month"

    override fun format(date: JalaliDateTime): String =
        names.getOrNull(date.jalaliMonth - 1) ?: ""

    override fun parse(input: String, pos: Int): ParseResult {
        for ((index, monthName) in names.withIndex()) {
            if (input.startsWith(monthName, pos)) {
                return ParseResult(index + 1, pos + monthName.length)
            }
        }
        throw IllegalArgumentException("Invalid month name at position $pos")
    }
}