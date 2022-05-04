package com.example.appactivitytracker

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.text.format.DateUtils
import android.util.ArrayMap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.text.DateFormat
import java.util.*

class HomeAdapter: BaseAdapter() {

    private val TAG = "HomeAdapter"

    // Constants defining order for display order
    private val _DISPLAY_ORDER_USAGE_TIME = 0
    private val _DISPLAY_ORDER_LAST_TIME_USED = 1
    private val _DISPLAY_ORDER_APP_NAME = 2

    private var mDisplayOrder: Int = _DISPLAY_ORDER_USAGE_TIME
    private val localLOGV = false
    private val usageTimeComparator: UsageTimeComparator = UsageTimeComparator()

    private val appLabelMap: ArrayMap<String, String> = ArrayMap()
    private val usageStats: ArrayList<UsageStats> = ArrayList()
    private val usageStatsManager: UsageStatsManager? = null
    private val inflater: LayoutInflater? = null
    private val adapter: HomeAdapter? = null
    private val pm: PackageManager? = null
    private var appLabelComparator: AppNameComparator? = null

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

        appLabelComparator = AppNameComparator(appLabelMap)
        sortList()
    }

    internal class AppViewHolder {
        var pkgName: TextView? = null
        var lastTimeUsed: TextView? = null
        var usageTime: TextView? = null
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
        var convertView = convertView
        var holder = AppViewHolder()

        if (convertView == null) {
            convertView = inflater!!.inflate(R.layout.usage_stats_item, null)

            holder.pkgName = convertView.findViewById(R.id.package_name) as TextView
            holder.lastTimeUsed = convertView.findViewById(R.id.last_time_used) as TextView
            holder.usageTime = convertView.findViewById(R.id.usage_time) as TextView
            convertView.setTag(holder)
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = convertView.tag as AppViewHolder
        }

        val useStats2: UsageStats = usageStats.get(position)
        if (usageStats != null) {
            val label = appLabelMap.get(useStats2.packageName)
            holder.pkgName?.setText(label)
            holder.lastTimeUsed?.setText(DateUtils.formatSameDayTime(useStats2.lastTimeUsed,
                System.currentTimeMillis(), DateFormat.MEDIUM, DateFormat.MEDIUM))
            holder.usageTime?.setText(DateUtils.formatElapsedTime(useStats2.totalTimeInForeground / 1000))
        } else {
            Log.w(TAG, "No Usage stats info for package:$position")
        }
        return convertView!!
    }

    fun sortList(sortOrder: Int) {
        if (mDisplayOrder == sortOrder) {
            //do nothing
            return
        }
        mDisplayOrder = sortOrder
        sortList()
    }

    private fun sortList() {
        if (mDisplayOrder == _DISPLAY_ORDER_USAGE_TIME) {
            if (localLOGV) Log.i(TAG, "Sorting by usage time")
            Collections.sort(usageStats, usageTimeComparator)
        }
    }
}