package com.example.appactivitytracker

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.util.ArrayMap
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import java.util.*

class HomeAdapter: BaseAdapter() {
    private val mAppLabelMap: ArrayMap<String, String> = ArrayMap()
    private val usageStats: ArrayList<UsageStats> = ArrayList()
    val usageStatsManager: UsageStatsManager? = null

    fun HomeAdapter() {
        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -5)

        val stats = usageStatsManager!!.queryUsageStats(
            UsageStatsManager.INTERVAL_BEST,
            calendar.timeInMillis, System.currentTimeMillis()
        ) ?: return
        val map = ArrayMap<String, UsageStats>()
    }
    override fun getCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getItem(position: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getItemId(position: Int): Long {
        TODO("Not yet implemented")
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        TODO("Not yet implemented")
    }
}