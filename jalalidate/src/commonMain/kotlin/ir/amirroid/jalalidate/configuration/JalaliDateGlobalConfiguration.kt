package ir.amirroid.jalalidate.configuration

import ir.amirroid.jalalidate.algorithm.JalaliAlgorithm
import ir.amirroid.jalalidate.algorithm.defaults.KhayyamAlgorithm
import ir.amirroid.jalalidate.formatter.Locale

public object JalaliDateGlobalConfiguration {
    public var convertAlgorithm: JalaliAlgorithm = KhayyamAlgorithm
    public var formatterLocale: Locale = Locale.PERSIAN
}