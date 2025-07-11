package ir.amirroid.jalalidate.birashk

import ir.amirroid.jalalidate.algorithm.defaults.BirashkAlgorithm
import ir.amirroid.jalalidate.date.JalaliDateTime
import ir.amirroid.jalalidate.minus
import ir.amirroid.jalalidate.plus
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

class OperatorsTest {
    private val algorithm = BirashkAlgorithm

    @Test
    fun testPlusGregorianDays() {
        val date = JalaliDateTime(1400, 1, 1, algorithm)
        val result = date.plusGregorianDays(10)
        val expected =
            JalaliDateTime.fromGregorian(date.gregorian.plus(10, DateTimeUnit.DAY), algorithm)
        assertEquals(expected.gregorianYear, result.gregorianYear)
        assertEquals(expected.gregorianMonth, result.gregorianMonth)
        assertEquals(expected.gregorianDay, result.gregorianDay)
    }

    @Test
    fun testMinusGregorianDays() {
        val date = JalaliDateTime(1400, 1, 15, algorithm)
        val result = date.minusGregorianDays(5)
        val expected =
            JalaliDateTime.fromGregorian(date.gregorian.minus(5, DateTimeUnit.DAY), algorithm)
        assertEquals(expected.gregorianYear, result.gregorianYear)
        assertEquals(expected.gregorianMonth, result.gregorianMonth)
        assertEquals(expected.gregorianDay, result.gregorianDay)
    }

    @Test
    fun testPlusGregorianMonths() {
        val date = JalaliDateTime(1400, 10, 10, algorithm)
        val result = date.plusGregorianMonths(3)
        val expected =
            JalaliDateTime.fromGregorian(date.gregorian.plus(3, DateTimeUnit.MONTH), algorithm)
        assertEquals(expected.gregorianYear, result.gregorianYear)
        assertEquals(expected.gregorianMonth, result.gregorianMonth)
        assertEquals(expected.gregorianDay, result.gregorianDay)
    }

    @Test
    fun testMinusGregorianMonths() {
        val date = JalaliDateTime(1401, 2, 28, algorithm)
        val result = date.minusGregorianMonths(4)
        val expected =
            JalaliDateTime.fromGregorian(date.gregorian.minus(4, DateTimeUnit.MONTH), algorithm)
        assertEquals(expected.gregorianYear, result.gregorianYear)
        assertEquals(expected.gregorianMonth, result.gregorianMonth)
        assertEquals(expected.gregorianDay, result.gregorianDay)
    }

    @Test
    fun testPlusGregorianYears() {
        val date = JalaliDateTime(1399, 12, 30, algorithm)
        val result = date.plusGregorianYears(1)
        val expected =
            JalaliDateTime.fromGregorian(date.gregorian.plus(1, DateTimeUnit.YEAR), algorithm)
        assertEquals(expected.gregorianYear, result.gregorianYear)
        assertEquals(expected.gregorianMonth, result.gregorianMonth)
        assertEquals(expected.gregorianDay, result.gregorianDay)
    }

    @Test
    fun testMinusGregorianYears() {
        val date = JalaliDateTime(1401, 1, 1, algorithm)
        val result = date.minusGregorianYears(1)
        val expected =
            JalaliDateTime.fromGregorian(date.gregorian.minus(1, DateTimeUnit.YEAR), algorithm)
        assertEquals(expected.gregorianYear, result.gregorianYear)
        assertEquals(expected.gregorianMonth, result.gregorianMonth)
        assertEquals(expected.gregorianDay, result.gregorianDay)
    }

    @Test
    fun testPlusHours() {
        val date = JalaliDateTime.fromGregorian(LocalDateTime(2023, 3, 21, 10, 0, 0), algorithm)
        val result = date.plusHours(5)
        val expected =
            JalaliDateTime.fromGregorian(date.gregorian.plus(5, DateTimeUnit.HOUR), algorithm)
        assertEquals(expected.hour, result.hour)
    }

    @Test
    fun testMinusHours() {
        val date = JalaliDateTime.fromGregorian(LocalDateTime(2023, 3, 21, 15, 0, 0), algorithm)
        val result = date.minusHours(3)
        val expected =
            JalaliDateTime.fromGregorian(date.gregorian.minus(3, DateTimeUnit.HOUR), algorithm)
        assertEquals(expected.hour, result.hour)
    }

    @Test
    fun testPlusMinutes() {
        val date = JalaliDateTime.fromGregorian(LocalDateTime(2023, 3, 21, 10, 30, 0), algorithm)
        val result = date.plusMinutes(15)
        val expected =
            JalaliDateTime.fromGregorian(date.gregorian.plus(15, DateTimeUnit.MINUTE), algorithm)
        assertEquals(expected.minute, result.minute)
    }

    @Test
    fun testMinusMinutes() {
        val date = JalaliDateTime.fromGregorian(LocalDateTime(2023, 3, 21, 10, 30, 0), algorithm)
        val result = date.minusMinutes(20)
        val expected =
            JalaliDateTime.fromGregorian(date.gregorian.minus(20, DateTimeUnit.MINUTE), algorithm)
        assertEquals(expected.minute, result.minute)
    }

    @Test
    fun testPlusSeconds() {
        val date = JalaliDateTime.fromGregorian(LocalDateTime(2023, 3, 21, 10, 0, 30), algorithm)
        val result = date.plusSeconds(45)
        val expected =
            JalaliDateTime.fromGregorian(date.gregorian.plus(45, DateTimeUnit.SECOND), algorithm)
        assertEquals(expected.second, result.second)
    }

    @Test
    fun testMinusSeconds() {
        val date = JalaliDateTime.fromGregorian(LocalDateTime(2023, 3, 21, 10, 0, 30), algorithm)
        val result = date.minusSeconds(15)
        val expected =
            JalaliDateTime.fromGregorian(date.gregorian.minus(15, DateTimeUnit.SECOND), algorithm)
        assertEquals(expected.second, result.second)
    }

    @Test
    fun testPlusJalaliDays() {
        val date = JalaliDateTime(1400, 1, 1, algorithm)
        val result = date.plusJalaliDays(10)
        val expected =
            JalaliDateTime.fromGregorian(date.gregorian.plus(10, DateTimeUnit.DAY), algorithm)
        assertEquals(expected.jalaliYear, result.jalaliYear)
        assertEquals(expected.jalaliMonth, result.jalaliMonth)
        assertEquals(expected.jalaliDay, result.jalaliDay)
    }

    @Test
    fun testMinusJalaliDays() {
        val date = JalaliDateTime(1400, 1, 15, algorithm)
        val result = date.minusJalaliDays(5)
        val expected =
            JalaliDateTime.fromGregorian(date.gregorian.minus(5, DateTimeUnit.DAY), algorithm)
        assertEquals(expected.jalaliYear, result.jalaliYear)
        assertEquals(expected.jalaliMonth, result.jalaliMonth)
        assertEquals(expected.jalaliDay, result.jalaliDay)
    }

    @Test
    fun testPlusJalaliMonths() {
        val date = JalaliDateTime(1400, 10, 10, algorithm)
        val result = date.plusJalaliMonths(5)
        val expectedYear = 1401
        val expectedMonth = 3
        val expectedDay = 10
        assertEquals(expectedYear, result.jalaliYear)
        assertEquals(expectedMonth, result.jalaliMonth)
        assertEquals(expectedDay, result.jalaliDay)
    }

    @Test
    fun testMinusJalaliMonths() {
        val date = JalaliDateTime(1401, 3, 30, algorithm)
        val result = date.minusJalaliMonths(5)
        val expectedYear = 1400
        val expectedMonth = 10
        val expectedDay = 30
        assertEquals(expectedYear, result.jalaliYear)
        assertEquals(expectedMonth, result.jalaliMonth)
        assertEquals(expectedDay, result.jalaliDay)
    }

    @Test
    fun testPlusJalaliYears() {
        val date = JalaliDateTime(1399, 12, 30, algorithm)
        val result = date.plusJalaliYears(2)
        val expectedYear = 1401
        val expectedMonth = 12
        val expectedDay = 29
        assertEquals(expectedYear, result.jalaliYear)
        assertEquals(expectedMonth, result.jalaliMonth)
        assertEquals(expectedDay, result.jalaliDay)
    }

    @Test
    fun testMinusJalaliYears() {
        val date = JalaliDateTime(1401, 1, 30, algorithm)
        val result = date.minusJalaliYears(1)
        val expectedYear = 1400
        val expectedMonth = 1
        val expectedDay = 30
        assertEquals(expectedYear, result.jalaliYear)
        assertEquals(expectedMonth, result.jalaliMonth)
        assertEquals(expectedDay, result.jalaliDay)
    }
}