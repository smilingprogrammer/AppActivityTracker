package com.example.appactivitytracker

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.provider.Settings


lateinit var mUsageStatsManager: UsageStatsManager
fun requestPermissions(context: Context?) {
    val stats: List<UsageStats> = mUsageStatsManager
        .queryUsageStats(UsageStatsManager.INTERVAL_DAILY, 0, System.currentTimeMillis())
    val isEmpty = stats.isEmpty()
    if (isEmpty) {
        val intent = Intent(context, Settings.ACTION_USAGE_ACCESS_SETTINGS::class.java)
        context?.startActivity(intent)
    }
}