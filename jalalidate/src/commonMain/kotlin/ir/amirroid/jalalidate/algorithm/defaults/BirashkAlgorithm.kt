package ir.amirroid.jalalidate.algorithm.defaults

import ir.amirroid.jalalidate.algorithm.JalaliAlgorithm
import ir.amirroid.jalalidate.date.JalaliDateTime
import ir.amirroid.jalalidate.utils.positiveModulo
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.number

public object BirashkAlgorithm : JalaliAlgorithm {
    private val jalaliMonthLengths = intArrayOf(31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29)
    private val gMonthLengths = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

    override fun toGregorian(jalali: JalaliDateTime): LocalDateTime {
        val jy = jalali.jalaliYear - 979
        val jm = jalali.jalaliMonth - 1
        val jd = jalali.jalaliDay - 1

        var jDayNo = 365 * jy + jy / 33 * 8 + ((jy % 33 + 3) / 4)
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
            gDayNo--
            gy += gDayNo / 365
            gDayNo %= 365
        }

        val gMonthLengthsCorrected = gMonthLengths.copyOf()
        if (leap) gMonthLengthsCorrected[1] = 29

        var gm = 0
        while (gm < 12 && gDayNo >= gMonthLengthsCorrected[gm]) {
            gDayNo -= gMonthLengthsCorrected[gm]
            gm++
        }

        val gd = gDayNo + 1

        return LocalDateTime(
            year = gy,
            month = gm + 1,
            day = gd,
            hour = jalali.hour,
            minute = jalali.minute,
            second = jalali.second
        )
    }

    override fun fromGregorian(date: LocalDateTime): JalaliDateTime {
        val gy = date.year
        val gm = date.month.number
        val gd = date.day

        var gDayNo = 365 * (gy - 1600) +
                (gy - 1600 + 3) / 4 -
                (gy - 1600 + 99) / 100 +
                (gy - 1600 + 399) / 400

        for (i in 0 until gm - 1) {
            gDayNo += gMonthLengths[i]
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
            jYear = jy,
            jMonth = jm + 1,
            jDay = jd,
            gYear = gy,
            gMonth = gm,
            gDay = gd,
            hour = date.hour,
            minute = date.minute,
            second = date.second,
            algorithm = this
        )
    }

    override fun isGregorianLeap(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }

    override fun isJalaliLeapYear(year: Int): Boolean {
        val epbase = if (year >= 0) year - 474 else year - 473
        val epyear = 474 + positiveModulo(epbase, 2820)
        return ((epyear * 682) % 2816) < 682
    }
}