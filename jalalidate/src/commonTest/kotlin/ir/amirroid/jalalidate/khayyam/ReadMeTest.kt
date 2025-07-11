package ir.amirroid.jalalidate.khayyam

import ir.amirroid.jalalidate.*
import ir.amirroid.jalalidate.algorithm.defaults.KhayyamAlgorithm
import ir.amirroid.jalalidate.date.JalaliDateTime
import ir.amirroid.jalalidate.date.*
import ir.amirroid.jalalidate.formatter.JalaliDateTimeFormatter
import kotlinx.datetime.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ReadMeTest {
    private val algorithm = KhayyamAlgorithm

    @Test
    fun testFormatWithMonthName() {
        val date = JalaliDateTime(1402, 5, 9, algorithm)
        val formatted = date.format {
            byUnicodePattern("yyyy dd MMMM")
        }
        assertEquals("1402 09 مرداد", formatted)
    }

    @Test
    fun testParseWithShortMonthName() {
        val input = "1402 09 مرد"
        val formatter = JalaliDateTimeFormatter().byUnicodePattern("yyyy dd MMM")
        val date = formatter.parse(input)
        assertEquals(5, date.jalaliMonth)
    }

    @Test
    fun testDateTimeIntervalOperations() {
        val date = JalaliDateTime(1402, 5, 10, 12, 30, 15, algorithm)
        val result1 = date + 5.days - 2.months
        val result2 = date - 1.years + 10.days
        assertEquals(JalaliDateTime(1402, 3, 15, 12, 30, 15, algorithm), result1)
        assertEquals(JalaliDateTime(1401, 5, 20, 12, 30, 15, algorithm), result2)
    }

    @Test
    fun testCopyGregorianAndJalali() {
        val original = JalaliDateTime(1402, 5, 10, 14, 45, 30, algorithm)
        val copyG = original.copyGregorian(year = 2023, month = 8, day = 1)
        val copyJ = original.copyJalali(year = 1403, month = 6, day = 20)

        assertEquals(LocalDateTime(2023, 8, 1, 14, 45, 30), copyG.gregorian)
        assertEquals(1403, copyJ.jalaliYear)
        assertEquals(6, copyJ.jalaliMonth)
        assertEquals(20, copyJ.jalaliDay)
    }

    @Test
    fun testMainProperties() {
        val date = JalaliDateTime(1402, 7, 15, 10, 20, 30, algorithm)

        assertEquals(2023, date.gregorianYear)
        assertEquals(10, date.gregorianMonth)
        assertEquals(7, date.gregorianDay)

        assertEquals(1402, date.jalaliYear)
        assertEquals(7, date.jalaliMonth)
        assertEquals(15, date.jalaliDay)

        assertEquals(10, date.hour)
        assertEquals(20, date.minute)
        assertEquals(30, date.second)

        assertEquals(29, date.weekOfYear)
        assertEquals(false, date.isJalaliLeapYear)
        assertEquals(false, date.isGregorianLeapYear)

        assertEquals(30, date.monthLength)
        assertEquals("Mehr", date.monthName.english)
        assertEquals("مهر", date.monthName.persian)

        assertEquals(DayOfWeek.SATURDAY, date.dayOfWeek())
        assertEquals(DayOfWeek.SATURDAY, date.dayOfWeek(weekStartDay = DayOfWeek.SUNDAY))
        assertEquals(1, date.dayOfWeekNumber())
    }
}