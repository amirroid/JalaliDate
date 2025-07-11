package ir.amirroid.jalalidate.configuration

import ir.amirroid.jalalidate.algorithm.JalaliAlgorithm
import ir.amirroid.jalalidate.algorithm.defaults.KhayyamAlgorithm

public object JalaliDateGlobalConfiguration {
    public var convertAlgorithm: JalaliAlgorithm = KhayyamAlgorithm
}