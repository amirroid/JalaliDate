package ir.amirroid.jalalidate.birashk

import ir.amirroid.jalalidate.algorithm.defaults.BirashkAlgorithm
import ir.amirroid.jalalidate.date.JalaliDateTime
import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PropertiesTest {
    val algorithm = BirashkAlgorithm

    @Test
    fun testJalaliLeapYear() {
        val leapYear = JalaliDateTime(1399, 1, 1, algorithm = algorithm)
        val normalYear = JalaliDateTime(1400, 1, 1, algorithm = algorithm)

        assertTrue(leapYear.isJalaliLeapYear)
        assertFalse(normalYear.isJalaliLeapYear)
    }


    @Test
    fun testGregorianLeapYear() {
        val leapYear =
            JalaliDateTime.fromGregorian(LocalDateTime(2020, 2, 29, 0, 0), algorithm = algorithm)
        val normalYear =
            JalaliDateTime.fromGregorian(LocalDateTime(2019, 3, 1, 0, 0), algorithm = algorithm)

        assertTrue(leapYear.isGregorianLeapYear)
        assertFalse(normalYear.isGregorianLeapYear)
    }

    @Test
    fun testMonthLength() {
        val dateFarvardin = JalaliDateTime(1400, 1, 1, algorithm = algorithm)
        val dateEsfand = JalaliDateTime(1400, 12, 1, algorithm = algorithm)
        val dateEsfandLeap = JalaliDateTime(1399, 12, 1, algorithm = algorithm)

        assertEquals(31, dateFarvardin.monthLength)
        assertEquals(29, dateEsfand.monthLength)
        assertEquals(29, dateEsfandLeap.monthLength)
    }

    @Test
    fun testDayOfWeekNumberForJalaliDate() {
        val date = JalaliDateTime(1402, 1, 1, algorithm = algorithm) // 2023-03-21 (Tuesday)
        assertEquals(4, date.dayOfWeekNumber()) // Tuesday = 4
    }

    @Test
    fun testDayOfWeekNumberForAnotherDate() {
        val date = JalaliDateTime(1402, 1, 4, algorithm = algorithm) // 2023-03-24 (Friday)
        assertEquals(7, date.dayOfWeekNumber()) // Friday = 7
    }
}