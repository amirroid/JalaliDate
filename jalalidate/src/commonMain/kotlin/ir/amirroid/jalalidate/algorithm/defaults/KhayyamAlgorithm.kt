package ir.amirroid.jalalidate.algorithm.defaults

import ir.amirroid.jalalidate.algorithm.JalaliAlgorithm
import ir.amirroid.jalalidate.date.JalaliDateTime
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.number

public object KhayyamAlgorithm : JalaliAlgorithm {
    private val gMonthLengthsNormal = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    private val gMonthLengthsLeap = intArrayOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    private val jalaliMonthLengths = intArrayOf(31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29)

    private val jalaliBreaks = intArrayOf(
        -61, 9, 38, 199, 426, 686, 756, 818,
        1111, 1181, 1210, 1635, 2060, 2097,
        2192, 2262, 2324, 2394, 2456, 3178
    )

    override fun toGregorian(jalali: JalaliDateTime): LocalDateTime {
        val jy = jalali.jalaliYear - 979
        val jm = jalali.jalaliMonth - 1
        val jd = jalali.jalaliDay - 1

        var jDayNo = 365 * jy + jy / 33 * 8 + (jy % 33 + 3) / 4
        for (i in 0 until jm) {
            jDayNo += jalaliMonthLengths[i]
        }
        jDayNo += jd

        var gDayNo = jDayNo + 79

        var gy = 1600 + 400 * (gDayNo / 146097)
        gDayNo %= 146097

        var leap = true
        if (gDayNo >= 36525) {
            gDayNo--
            gy += 100 * (gDayNo / 36524)
            gDayNo %= 36524
            if (gDayNo >= 365) gDayNo++ else leap = false
        }

        gy += 4 * (gDayNo / 1461)
        gDayNo %= 1461

        if (gDayNo >= 366) {
            leap = false
            gDayNo -= 1
            gy += gDayNo / 365
            gDayNo %= 365
        }

        val gMonthLengths = if (leap) gMonthLengthsLeap else gMonthLengthsNormal
        var gm = 0
        while (gm < 12 && gDayNo >= gMonthLengths[gm]) {
            gDayNo -= gMonthLengths[gm]
            gm++
        }

        val gd = gDayNo + 1

        return LocalDateTime(
            gy,
            gm + 1,
            gd,
            jalali.hour,
            jalali.minute,
            jalali.second
        )
    }

    override fun fromGregorian(date: LocalDateTime): JalaliDateTime {
        val gy = date.year
        val gm = date.month.number
        val gd = date.day

        var gDayNo =
            365 * (gy - 1600) + (gy - 1600 + 3) / 4 - (gy - 1600 + 99) / 100 + (gy - 1600 + 399) / 400

        for (i in 0 until gm - 1) {
            gDayNo += gMonthLengthsNormal[i]
        }
        if (gm > 2 && isGregorianLeap(gy)) gDayNo += 1
        gDayNo += gd - 1

        var jDayNo = gDayNo - 79

        val jNp = jDayNo / 12053
        jDayNo %= 12053

        var jy = 979 + 33 * jNp + 4 * (jDayNo / 1461)
        jDayNo %= 1461

        if (jDayNo >= 366) {
            jy += (jDayNo - 1) / 365
            jDayNo = (jDayNo - 1) % 365
        }

        var jm = 0
        while (jm < 11 && jDayNo >= jalaliMonthLengths[jm]) {
            jDayNo -= jalaliMonthLengths[jm]
            jm++
        }

        val jd = jDayNo + 1

        return JalaliDateTime(
            jy,
            jm + 1,
            jd,
            gy,
            gm,
            gd,
            date.hour,
            date.minute,
            date.second,
            this
        )
    }

    override fun isGregorianLeap(year: Int): Boolean =
        (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)

    override fun isJalaliLeapYear(year: Int): Boolean {
        var jp = jalaliBreaks[0]
        var jump: Int
        var leap: Int
        var n: Int
        var i = 1

        while (i < jalaliBreaks.size && year >= jalaliBreaks[i]) {
            jp = jalaliBreaks[i]
            i++
        }
        jump = if (i < jalaliBreaks.size) jalaliBreaks[i] - jp else 0

        n = year - jp

        if (jump - n < 6) {
            n = n - jump + ((jump + 4) / 33) * 33
        }

        leap = (((n + 1) % 33) - 1) % 4

        return leap == 0
    }
}