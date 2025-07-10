package ir.amirroid.jalalidate.khayyam

import ir.amirroid.jalalidate.algorithm.defaults.KhayyamAlgorithm
import ir.amirroid.jalalidate.date.JalaliDateTime
import ir.amirroid.jalalidate.format
import ir.amirroid.jalalidate.formatter.Padding
import kotlin.test.Test
import kotlin.test.assertEquals

class FormatTest {
    val algorithm = KhayyamAlgorithm

    @Test
    fun testYearMonthDayFormat() {
        val date = JalaliDateTime(1399, 12, 30, 23, 59, 59, algorithm = algorithm)
        val formatter = JalaliDateTime.Format {
            byUnicodePattern("yyyy")
            chars("/")
            byUnicodePattern("MM")
            chars("/")
            byUnicodePattern("dd")
        }
        assertEquals("1399/12/30", formatter.format(date))
    }

    @Test
    fun testTimeOnlyFormat() {
        val date = JalaliDateTime(1400, 1, 1, 0, 5, 7, algorithm = algorithm)
        val formatter = JalaliDateTime.Format {
            byUnicodePattern("HH")
            chars(":")
            byUnicodePattern("mm")
            chars(":")
            byUnicodePattern("ss")
        }
        assertEquals("00:05:07", formatter.format(date))
    }

    @Test
    fun testMixedOrderFormat() {
        val date = JalaliDateTime(1402, 1, 9, 15, 8, 3, algorithm = algorithm)
        val formatter = JalaliDateTime.Format {
            dayTwoDigit(Padding.ZERO)
            chars("-")
            byUnicodePattern("yyyy")
            chars("-")
            byUnicodePattern("MM")
            chars(" ")
            byUnicodePattern("ss")
            chars(":")
            byUnicodePattern("HH")
            chars(":")
            byUnicodePattern("mm")
        }
        val expected = "09-1402-01 03:15:08"
        assertEquals(expected, formatter.format(date))
    }

    @Test
    fun testSingleDigitMonthDayWithZeroPadding() {
        val date = JalaliDateTime(1402, 4, 3, algorithm = algorithm)
        val formatter = JalaliDateTime.Format {
            byUnicodePattern("MM")
            chars("/")
            byUnicodePattern("dd")
        }
        assertEquals("04/03", formatter.format(date))
    }

    @Test
    fun testSingleDigitMonthDayWithSpacePadding() {
        val date = JalaliDateTime(1402, 4, 3, algorithm = algorithm)
        val formatter = JalaliDateTime.Format {
            byUnicodePattern("M")  // Assuming "M" means no leading zero or space (implement if needed)
            chars("/")
            dayTwoDigit(Padding.SPACE)
        }
        assertEquals("4/ 3", formatter.format(date))
    }

    @Test
    fun testLeapYearDateFormat() {
        val date =
            JalaliDateTime(1399, 12, 30, algorithm = algorithm) // 1399 is leap year in Birashk
        val formatter = JalaliDateTime.Format {
            byUnicodePattern("yyyy")
            chars("-")
            byUnicodePattern("MM")
            chars("-")
            byUnicodePattern("dd")
        }
        assertEquals("1399-12-30", formatter.format(date))
    }

    @Test
    fun testFullPatternFormat() {
        val date = JalaliDateTime(1402, 5, 9, 14, 3, 7, algorithm = algorithm)
        val formatted = date.format { byUnicodePattern("yyyy-MM-dd HH:mm:ss") }
        assertEquals("1402-05-09 14:03:07", formatted)
    }

    @Test
    fun testFullPatternWithMonthName() {
        val date = JalaliDateTime(1402, 5, 9, 14, 3, 7, algorithm = algorithm)
        val formatted = date.format { byUnicodePattern("yyyy MMMM dd HH:mm:ss") }
        assertEquals("1402 مرداد 09 14:03:07", formatted)
    }

    @Test
    fun testShortYearAndMonth() {
        val date = JalaliDateTime(1402, 1, 1, algorithm = algorithm)
        val formatted = date.format { byUnicodePattern("yy-M-d") }
        assertEquals("02-1-1", formatted)
    }

    @Test
    fun testMonthShortNameAndSingleDigitDay() {
        val date = JalaliDateTime(1402, 8, 3, algorithm = algorithm)
        val formatted = date.format { byUnicodePattern("MMM d") }
        assertEquals("آبا 3", formatted)
    }

    @Test
    fun testHourMinuteSecondNoLeadingZeros() {
        val date = JalaliDateTime(1402, 3, 7, 5, 4, 9, algorithm = algorithm)
        val formatted = date.format { byUnicodePattern("H:m:s") }
        assertEquals("5:4:9", formatted)
    }

    @Test
    fun testMixedPattern() {
        val date = JalaliDateTime(1399, 12, 30, 23, 59, 59, algorithm = algorithm)
        val formatted = date.format { byUnicodePattern("yyyy/MM/dd HH:mm:ss") }
        assertEquals("1399/12/30 23:59:59", formatted)
    }
}