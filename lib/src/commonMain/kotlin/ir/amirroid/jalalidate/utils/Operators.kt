package ir.amirroid.jalalidate.utils

internal fun positiveModulo(a: Int, b: Int): Int {
    val mod = a % b
    return if (mod < 0) mod + b else mod
}