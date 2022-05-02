package com.example.appactivitytracker

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.util.ArrayMap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.util.*


class HomeAdapter: BaseAdapter() {
    private val appLabelMap: ArrayMap<String, String> = ArrayMap()
    private val usageStats: ArrayList<UsageStats> = ArrayList()
    private val usageStatsManager: UsageStatsManager? = null
    private val inflater: LayoutInflater? = null
    private val adapter: HomeAdapter? = null
    private val pm: PackageManager? = null
//    private val appLabelComparator: Comparator? = null

    fun HomeAdapter() {
        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -5)

        val stats = usageStatsManager!!.queryUsageStats(
            UsageStatsManager.INTERVAL_BEST,
            calendar.timeInMillis, System.currentTimeMillis()
        ) ?: return
        val map = ArrayMap<String, UsageStats>()
        val statCount = stats.size

        for (i in 0 until statCount) {
            val useStats: UsageStats = stats.get(i)
            try {
                val applicationInfo: ApplicationInfo = pm!!.getApplicationInfo(useStats.packageName, 0)
                val label = applicationInfo.loadLabel(pm).toString()
                appLabelMap.put(useStats.packageName, label)

                val existingStats: UsageStats? = map.get(useStats.packageName)
                if (existingStats == null) {
                    map.put(useStats.packageName, useStats)
                } else {
                    existingStats.add(useStats)
                }
            } catch (e: NameNotFoundException) {

            }
        }
        usageStats.addAll(map.values)
        //Sort List
    }
    override fun getCount(): Int {
        return usageStats.size
    }

    override fun getItem(position: Int): Any {
        return usageStats.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: AppViewHolder

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_home, null)
        }
    }

    internal class AppViewHolder {
        var pkgName: TextView? = null
        var lastTimeUsed: TextView? = null
        var usageTime: TextView? = null
    }


}