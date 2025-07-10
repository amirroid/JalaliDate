package ir.amirroid.jalalidate.khayyam

import ir.amirroid.jalalidate.algorithm.defaults.KhayyamAlgorithm
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
}