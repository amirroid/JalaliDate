package ir.amirroid.jalalidate.khayyam

import ir.amirroid.jalalidate.algorithm.defaults.KhayyamAlgorithm
import ir.amirroid.jalalidate.date.JalaliDateTime
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.number
import kotlin.test.Test
import kotlin.test.assertEquals

class ConvertTest {
    val algorithm = KhayyamAlgorithm

    @Test
    fun testJalaliToGregorianConversion() {
        val jalaliDate = JalaliDateTime(1400, 1, 1, algorithm = algorithm)
        val gregorian = jalaliDate.gregorian

        assertEquals(2021, gregorian.year)
        assertEquals(3, gregorian.month.number)
        assertEquals(21, gregorian.day)
    }

    @Test
    fun testGregorianToJalaliConversion() {
        val gregorianDate = LocalDateTime(2021, 3, 21, 0, 0)
        val jalaliDate = JalaliDateTime.fromGregorian(gregorianDate, algorithm = algorithm)

        assertEquals(1400, jalaliDate.jalaliYear)
        assertEquals(1, jalaliDate.jalaliMonth)
        assertEquals(1, jalaliDate.jalaliDay)
    }

    @Test
    fun testLeapYearJalali() {
        val lastDayOfEsfand = JalaliDateTime(1399, 12, 30, algorithm = algorithm)
        val gregorian = lastDayOfEsfand.gregorian

        assertEquals(2021, gregorian.year)
        assertEquals(3, gregorian.month.number)
        assertEquals(20, gregorian.day)
    }

    @Test
    fun testEndOfGregorianMonths() {
        val daysInMonth = arrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

        for (month in 1..12) {
            val lastDay = LocalDateTime(2021, month, daysInMonth[month - 1], 0, 0)
            val jalali = JalaliDateTime.fromGregorian(lastDay, algorithm = algorithm)
            val gregorianBack = jalali.gregorian

            assertEquals(lastDay.year, gregorianBack.year)
            assertEquals(lastDay.month.number, gregorianBack.month.number)
            assertEquals(lastDay.day, gregorianBack.day)
        }
    }

    @Test
    fun testEndOfJalaliMonths() {
        val jalaliMonthLengths = intArrayOf(31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29)
        for (month in 1..12) {
            val day = jalaliMonthLengths[month - 1]
            val jalaliDate = JalaliDateTime(1400, month, day, algorithm = algorithm)
            val gregorian = jalaliDate.gregorian
            val back = JalaliDateTime.fromGregorian(gregorian, algorithm = algorithm)

            assertEquals(1400, back.jalaliYear)
            assertEquals(month, back.jalaliMonth)
            assertEquals(day, back.jalaliDay)
        }
    }
}