package com.example.appactivitytracker

import android.app.usage.UsageStats

class AppNameComparator internal constructor(appList: Map<String, String>) :
    Comparator<UsageStats?> {
    private val mAppLabelList: Map<String, String> = appList

    override fun compare(a: UsageStats?, b: UsageStats?): Int {
        val alabel = mAppLabelList[a?.packageName]
        val blabel = mAppLabelList[b?.packageName]
        return alabel!!.compareTo(blabel!!)
    }
}

class LastTimeUsedComparator : Comparator<UsageStats?> {

    override fun compare(a: UsageStats?, b: UsageStats?): Int {
        // return by descending order
        return (b!!.lastTimeUsed - a!!.lastTimeUsed).toInt()
    }
}

class UsageTimeComparator : Comparator<UsageStats?> {

    override fun compare(a: UsageStats?, b: UsageStats?): Int {
        return (b!!.totalTimeInForeground - a!!.totalTimeInForeground).toInt()
    }
}