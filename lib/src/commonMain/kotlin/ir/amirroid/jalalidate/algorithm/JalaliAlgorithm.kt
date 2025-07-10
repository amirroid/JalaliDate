package ir.amirroid.jalalidate.algorithm

import ir.amirroid.jalalidate.date.JalaliDateTime
import kotlinx.datetime.LocalDateTime

public interface JalaliAlgorithm {
    public fun toGregorian(jalali: JalaliDateTime): LocalDateTime
    public fun fromGregorian(date: LocalDateTime): JalaliDateTime
    public fun isGregorianLeap(year: Int): Boolean
    public fun isJalaliLeapYear(year: Int): Boolean
}