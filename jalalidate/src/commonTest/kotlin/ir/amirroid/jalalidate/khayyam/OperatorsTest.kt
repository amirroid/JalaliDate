package ir.amirroid.jalalidate.khayyam

import ir.amirroid.jalalidate.algorithm.defaults.KhayyamAlgorithm
import ir.amirroid.jalalidate.date.JalaliDateTime
import ir.amirroid.jalalidate.date.days
import ir.amirroid.jalalidate.date.months
import ir.amirroid.jalalidate.date.years
import ir.amirroid.jalalidate.minus
import ir.amirroid.jalalidate.plus
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

class OperatorsTest {
    private val algorithm = KhayyamAlgorithm

    @Test
    fun testPlusDays() {
        val date = JalaliDateTime(1400, 1, 1, algorithm)
        val result = date.plusDays(10)
        val expected =
            JalaliDateTime.fromGregorian(date.gregorian.plus(10, DateTimeUnit.DAY), algorithm)
        assertEquals(expected.jalaliYear, result.jalaliYear)
        assertEquals(expected.jalaliMonth, result.jalaliMonth)
        assertEquals(expected.jalaliDay, result.jalaliDay)
    }

    @Test
    fun testMinusDays() {
        val date = JalaliDateTime(1400, 1, 15, algorithm)
        val result = date.minusDays(5)
        val expected =
            JalaliDateTime.fromGregorian(date.gregorian.minus(5, DateTimeUnit.DAY), algorithm)
        assertEquals(expected.jalaliYear, result.jalaliYear)
        assertEquals(expected.jalaliMonth, result.jalaliMonth)
        assertEquals(expected.jalaliDay, result.jalaliDay)
    }

    @Test
    fun testPlusWeeks() {
        val date = JalaliDateTime(1400, 1, 1, algorithm)
        val result = date.plusWeeks(2)
        val expected =
            JalaliDateTime.fromGregorian(date.gregorian.plus(2, DateTimeUnit.WEEK), algorithm)
        assertEquals(expected.jalaliYear, result.jalaliYear)
        assertEquals(expected.jalaliMonth, result.jalaliMonth)
        assertEquals(expected.jalaliDay, result.jalaliDay)
    }

    @Test
    fun testMinusWeeks() {
        val date = JalaliDateTime(1400, 1, 15, algorithm)
        val result = date.minusWeeks(1)
        val expected =
            JalaliDateTime.fromGregorian(date.gregorian.minus(1, DateTimeUnit.WEEK), algorithm)
        assertEquals(expected.jalaliYear, result.jalaliYear)
        assertEquals(expected.jalaliMonth, result.jalaliMonth)
        assertEquals(expected.jalaliDay, result.jalaliDay)
    }

    @Test
    fun testPlusMonths() {
        val date = JalaliDateTime(1400, 10, 10, algorithm)
        val result = date.plusMonths(3)
        val expected =
            JalaliDateTime.fromGregorian(date.gregorian.plus(3, DateTimeUnit.MONTH), algorithm)
        assertEquals(expected.jalaliYear, result.jalaliYear)
        assertEquals(expected.jalaliMonth, result.jalaliMonth)
        assertEquals(expected.jalaliDay, result.jalaliDay)
    }

    @Test
    fun testMinusMonths() {
        val date = JalaliDateTime(1401, 2, 28, algorithm)
        val result = date.minusMonths(4)
        val expected =
            JalaliDateTime.fromGregorian(date.gregorian.minus(4, DateTimeUnit.MONTH), algorithm)
        assertEquals(expected.jalaliYear, result.jalaliYear)
        assertEquals(expected.jalaliMonth, result.jalaliMonth)
        assertEquals(expected.jalaliDay, result.jalaliDay)
    }

    @Test
    fun testPlusYears() {
        val date = JalaliDateTime(1399, 12, 30, algorithm)
        val result = date.plusYears(1)
        val expected =
            JalaliDateTime.fromGregorian(date.gregorian.plus(1, DateTimeUnit.YEAR), algorithm)
        assertEquals(expected.jalaliYear, result.jalaliYear)
        assertEquals(expected.jalaliMonth, result.jalaliMonth)
        assertEquals(expected.jalaliDay, result.jalaliDay)
    }

    @Test
    fun testMinusYears() {
        val date = JalaliDateTime(1401, 1, 1, algorithm)
        val result = date.minusYears(1)
        val expected =
            JalaliDateTime.fromGregorian(date.gregorian.minus(1, DateTimeUnit.YEAR), algorithm)
        assertEquals(expected.jalaliYear, result.jalaliYear)
        assertEquals(expected.jalaliMonth, result.jalaliMonth)
        assertEquals(expected.jalaliDay, result.jalaliDay)
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
    fun testPlusDateTimeInterval() {
        val date = JalaliDateTime.fromGregorian(LocalDateTime(2023, 3, 21, 10, 0, 30), algorithm)
        val result = date + 1.days + 5.days
        val expected =
            JalaliDateTime.fromGregorian(date.gregorian.plus(6, DateTimeUnit.DAY), algorithm)
        assertEquals(expected.jalaliDay, result.jalaliDay)
    }

    @Test
    fun testMinusDateTimeInterval() {
        val date = JalaliDateTime.fromGregorian(LocalDateTime(2023, 3, 21, 10, 0, 30), algorithm)
        val result = date - 2.months - 1.months
        val expected =
            JalaliDateTime.fromGregorian(date.gregorian.minus(3, DateTimeUnit.MONTH), algorithm)
        assertEquals(expected.jalaliMonth, result.jalaliMonth)
    }

    @Test
    fun testPlusMixedIntervals() {
        val date = JalaliDateTime.fromGregorian(LocalDateTime(2023, 3, 21, 10, 0, 30), algorithm)
        val result = date + 1.years + 2.months + 10.days
        val expected = JalaliDateTime.fromGregorian(
            date.gregorian.plus(1, DateTimeUnit.YEAR)
                .plus(2, DateTimeUnit.MONTH)
                .plus(10, DateTimeUnit.DAY),
            algorithm
        )
        assertEquals(expected.jalaliYear, result.jalaliYear)
        assertEquals(expected.jalaliMonth, result.jalaliMonth)
        assertEquals(expected.jalaliDay, result.jalaliDay)
    }
}