package com.example.appactivitytracker

import android.app.usage.UsageStats
import java.util.Comparator

class AppNameComparator internal constructor(private val mAppLabelList: Map<String, String>) :
    Comparator<UsageStats> {
    override fun compare(a: UsageStats, b: UsageStats): Int {
        val alabel = mAppLabelList[a.packageName]
        val blabel = mAppLabelList[b.packageName]
        return alabel!!.compareTo(blabel!!)
    }
}

class LastTimeUsedComparator : Comparator<UsageStats> {
    override fun compare(a: UsageStats, b: UsageStats): Int {
        // return by descending order
        return (b.lastTimeUsed - a.lastTimeUsed).toInt()
    }
}

class UsageTimeComparator : Comparator<UsageStats> {
    override fun compare(a: UsageStats, b: UsageStats): Int {
        return (b.totalTimeInForeground - a.totalTimeInForeground).toInt()
    }
}