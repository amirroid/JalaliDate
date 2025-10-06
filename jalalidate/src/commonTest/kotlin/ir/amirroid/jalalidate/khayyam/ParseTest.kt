package ir.amirroid.jalalidate.khayyam

import ir.amirroid.jalalidate.algorithm.defaults.KhayyamAlgorithm
import ir.amirroid.jalalidate.date.JalaliDateTime
import ir.amirroid.jalalidate.format
import ir.amirroid.jalalidate.formatter.JalaliDateTimeFormatter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ParseTest {
    val algorithm = KhayyamAlgorithm

    @Test
    fun testParseFullPattern() {
        val input = "1402-05-09 14:03:07"
        val formatter = JalaliDateTimeFormatter().byUnicodePattern("yyyy-MM-dd HH:mm:ss")
        val date = formatter.parse(input, algorithm)
        assertEquals(1402, date.jalaliYear)
        assertEquals(5, date.jalaliMonth)
        assertEquals(9, date.jalaliDay)
        assertEquals(14, date.hour)
        assertEquals(3, date.minute)
        assertEquals(7, date.second)
    }

    @Test
    fun testParseFullPatternWithMonthName() {
        val input = "1402 مرداد 09 14:03:07"
        val formatter = JalaliDateTimeFormatter().byUnicodePattern("yyyy MMMM dd HH:mm:ss")
        val date = formatter.parse(input, algorithm)
        assertEquals(1402, date.jalaliYear)
        assertEquals(5, date.jalaliMonth)
        assertEquals(9, date.jalaliDay)
        assertEquals(14, date.hour)
        assertEquals(3, date.minute)
        assertEquals(7, date.second)
    }

    @Test
    fun testParseShortYearAndMonth() {
        val input = "1322-1-1"
        val formatter = JalaliDateTimeFormatter().byUnicodePattern("yyyy-M-d")
        val date = formatter.parse(input, algorithm)
        assertEquals(1322, date.jalaliYear)
        assertEquals(1, date.jalaliMonth)
        assertEquals(1, date.jalaliDay)
    }

    @Test
    fun testParseWithSingleDigitTime() {
        val input = "1402-05-09 4:3:7"
        val formatter = JalaliDateTimeFormatter().byUnicodePattern("yyyy-MM-dd H:m:s")
        val date = formatter.parse(input, algorithm)
        assertEquals(1402, date.jalaliYear)
        assertEquals(5, date.jalaliMonth)
        assertEquals(9, date.jalaliDay)
        assertEquals(4, date.hour)
        assertEquals(3, date.minute)
        assertEquals(7, date.second)
    }

    @Test
    fun testParseMissingFieldsDefaultsToNow() {
        val input = "1402-05"
        val formatter = JalaliDateTimeFormatter().byUnicodePattern("yyyy-MM")
        val date = formatter.parse(input, algorithm)
        assertEquals(1402, date.jalaliYear)
        assertEquals(5, date.jalaliMonth)
        assertEquals(1, date.jalaliDay) // default day=1
    }

    @Test
    fun testParseWithShortMonthName() {
        val input = "1402 مرد 09"
        val formatter = JalaliDateTimeFormatter().byUnicodePattern("yyyy MMM dd")
        val date = formatter.parse(input, algorithm)
        assertEquals(1402, date.jalaliYear)
        assertEquals(5, date.jalaliMonth)
        assertEquals(9, date.jalaliDay)
    }

    @Test
    fun testParseInvalidLiteralThrows() {
        val input = "1402/05/09"
        val formatter = JalaliDateTimeFormatter().byUnicodePattern("yyyy-MM-dd")
        assertFailsWith<IllegalArgumentException> {
            formatter.parse(input, algorithm)
        }
    }

    @Test
    fun testParsePartialTime() {
        val input = "1402-05-09 14:03"
        val formatter = JalaliDateTimeFormatter().byUnicodePattern("yyyy-MM-dd HH:mm")
        val date = formatter.parse(input, algorithm)
        assertEquals(1402, date.jalaliYear)
        assertEquals(5, date.jalaliMonth)
        assertEquals(9, date.jalaliDay)
        assertEquals(14, date.hour)
        assertEquals(3, date.minute)
        assertEquals(0, date.second)
    }

    @Test
    fun testParseYearMonthDay() {
        val input = "1401/01/01"
        val formatter = JalaliDateTimeFormatter()
            .year()
            .chars("/")
            .monthTwoDigit()
            .chars("/")
            .dayTwoDigit()

        val date = formatter.parse(input, algorithm)
        assertEquals(1401, date.jalaliYear)
        assertEquals(1, date.jalaliMonth)
        assertEquals(1, date.jalaliDay)
    }

    @Test
    fun testParseTimeOnly() {
        val input = "23:45:09"
        val formatter = JalaliDateTimeFormatter()
            .hour()
            .chars(":")
            .minute()
            .chars(":")
            .second()

        val date = formatter.parse(input, algorithm)
        assertEquals(23, date.hour)
        assertEquals(45, date.minute)
        assertEquals(9, date.second)
    }

    @Test
    fun testParseDateTimeManualFormat() {
        val input = "1400-07-15 @ 13:30:00"
        val formatter = JalaliDateTimeFormatter()
            .year()
            .chars("-")
            .monthTwoDigit()
            .chars("-")
            .dayTwoDigit()
            .chars(" @ ")
            .hour()
            .chars(":")
            .minute()
            .chars(":")
            .second()

        val date = formatter.parse(input, algorithm)
        assertEquals(1400, date.jalaliYear)
        assertEquals(7, date.jalaliMonth)
        assertEquals(15, date.jalaliDay)
        assertEquals(13, date.hour)
        assertEquals(30, date.minute)
        assertEquals(0, date.second)
    }

    @Test
    fun testParseWithSpacesAndWords() {
        val input = "سال:1402 ماه:6 روز:9"
        val formatter = JalaliDateTimeFormatter()
            .chars("سال:")
            .year()
            .chars(" ماه:")
            .monthOneDigit()
            .chars(" روز:")
            .dayOneDigit()

        val date = formatter.parse(input, algorithm)
        assertEquals(1402, date.jalaliYear)
        assertEquals(6, date.jalaliMonth)
        assertEquals(9, date.jalaliDay)
    }

    @Test
    fun testParseWithOneDigitMonthDay() {
        val input = "1402-6-9"
        val formatter = JalaliDateTimeFormatter()
            .year()
            .chars("-")
            .monthOneDigit()
            .chars("-")
            .dayOneDigit()

        val date = formatter.parse(input, algorithm)
        assertEquals(1402, date.jalaliYear)
        assertEquals(6, date.jalaliMonth)
        assertEquals(9, date.jalaliDay)
    }

    @Test
    fun testParseWithWeekShortName() {
        val input = "ش 1402-6-9"
        val formatter = JalaliDateTimeFormatter()
            .weekShortName()
            .chars(" ")
            .year()
            .chars("-")
            .monthOneDigit()
            .chars("-")
            .dayOneDigit()

        val date = formatter.parse(input, algorithm)
        assertEquals(1402, date.jalaliYear)
        assertEquals(6, date.jalaliMonth)
        assertEquals(9, date.jalaliDay)
    }

    @Test
    fun testParseWithWeekShortNameUnicode() {
        val input = "ش 1402/6/9"
        val formatter = JalaliDateTimeFormatter()
            .byUnicodePattern("EEE yyyy/M/d")

        val date = formatter.parse(input, algorithm)
        assertEquals(1402, date.jalaliYear)
        assertEquals(6, date.jalaliMonth)
        assertEquals(9, date.jalaliDay)
    }

    @Test
    fun testParseAmPattern() {
        val input = "1402/07/15 09:30:00 AM"
        val formatter = JalaliDateTimeFormatter()
            .byUnicodePattern("yyyy/MM/dd hh:mm:ss a")

        val date = formatter.parse(input, algorithm)
        assertEquals(1402, date.jalaliYear)
        assertEquals(7, date.jalaliMonth)
        assertEquals(15, date.jalaliDay)
        assertEquals(9, date.hour)
        assertEquals(30, date.minute)
        assertEquals(0, date.second)
    }

    @Test
    fun testParsePmPattern() {
        val input = "1402/07/15 09:30:00 PM"
        val formatter = JalaliDateTimeFormatter()
            .byUnicodePattern("yyyy/MM/dd hh:mm:ss a")

        val date = formatter.parse(input, algorithm)
        assertEquals(1402, date.jalaliYear)
        assertEquals(7, date.jalaliMonth)
        assertEquals(15, date.jalaliDay)
        assertEquals(21, date.hour)
        assertEquals(30, date.minute)
        assertEquals(0, date.second)
    }

    @Test
    fun testParseNoonBoundary() {
        val input = "1402/07/15 12:00:00 PM"
        val formatter = JalaliDateTimeFormatter()
            .byUnicodePattern("yyyy/MM/dd hh:mm:ss a")

        val date = formatter.parse(input, algorithm)
        assertEquals(12, date.hour)
    }

    @Test
    fun testParseMidnightBoundary() {
        val input = "1402/07/15 12:00:00 AM"
        val formatter = JalaliDateTimeFormatter()
            .byUnicodePattern("yyyy/MM/dd hh:mm:ss a")

        val date = formatter.parse(input, algorithm)
        assertEquals(0, date.hour)
    }

    @Test
    fun testParseAmPmWithTextMixPattern() {
        val input = "شنبه 1402/01/01 11:15 PM"
        val formatter = JalaliDateTimeFormatter()
            .byUnicodePattern("EEEE yyyy/MM/dd hh:mm a")

        val date = formatter.parse(input, algorithm)
        assertEquals(1402, date.jalaliYear)
        assertEquals(1, date.jalaliMonth)
        assertEquals(1, date.jalaliDay)
        assertEquals(23, date.hour)
        assertEquals(15, date.minute)
    }

    @Test
    fun parseHourIn12HourFormatWithVariableLength() {
        val format = JalaliDateTime.Format { byUnicodePattern("yyyy/MM/dd h:mm a") }

        val date1 = format.parse("1402/01/01 1:05 PM")
        assertEquals(13, date1.hour) // 1 PM -> 13

        val date2 = format.parse("1402/01/01 11:15 PM")
        assertEquals(23, date2.hour) // 11 PM -> 23

        val date3 = format.parse("1402/01/01 9:00 AM")
        assertEquals(9, date3.hour) // 9 AM -> 9
    }
}