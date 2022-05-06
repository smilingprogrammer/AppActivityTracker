package com.example.appactivitytracker

//import android.R
//import android.app.usage.UsageStats
//import android.app.usage.UsageStatsManager
//import android.content.pm.ApplicationInfo
//import android.content.pm.PackageManager
//import android.text.format.DateUtils
//import android.util.Log
//import android.view.View
//import android.view.ViewGroup
//import android.widget.BaseAdapter
//import java.util.*
//import kotlin.collections.ArrayList
//
//
//class UsageStatsAdapter : BaseAdapter() {
//    private var mDisplayOrder = _DISPLAY_ORDER_USAGE_TIME
//    private val mLastTimeUsedComparator = LastTimeUsedComparator()
//    private val mUsageTimeComparator = UsageTimeComparator()
//    private val mAppLabelComparator: AppNameComparator
//    private val mAppLabelMap: ArrayMap<String, String> = ArrayMap()
//    private val mPackageStats: ArrayList<UsageStats> = ArrayList()
//    override fun getCount(): Int {
//        return mPackageStats.size()
//    }
//
//    override fun getItem(position: Int): Any {
//        return mPackageStats[position]
//    }
//
//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
//        // A ViewHolder keeps references to children views to avoid unneccessary calls
//        // to findViewById() on each row.
//        var convertView: View? = convertView
//        val holder: HomeAdapter.AppViewHolder
//
//        // When convertView is not null, we can reuse it directly, there is no need
//        // to reinflate it. We only inflate a new View when the convertView supplied
//        // by ListView is null.
//        if (convertView == null) {
//            convertView = mInflater.inflate(R.layout.usage_stats_item, null)
//
//            // Creates a ViewHolder and store references to the two children views
//            // we want to bind data to.
//            holder = HomeAdapter.AppViewHolder()
//            holder.pkgName = convertView.findViewById(R.id.package_name)
//            holder.lastTimeUsed = convertView.findViewById(R.id.last_time_used)
//            holder.usageTime = convertView.findViewById(R.id.usage_time)
//            convertView.setTag(holder)
//        } else {
//            // Get the ViewHolder back to get fast access to the TextView
//            // and the ImageView.
//            holder = convertView.getTag() as HomeAdapter.AppViewHolder
//        }
//
//        // Bind the data efficiently with the holder
//        val pkgStats = mPackageStats[position]
//        if (pkgStats != null) {
//            val label: String = mAppLabelMap.get(pkgStats.packageName)
//            holder.pkgName.setText(label)
//            holder.lastTimeUsed.setText(
//                DateUtils.formatSameDayTime(
//                    pkgStats.lastTimeUsed,
//                    System.currentTimeMillis(), DateFormat.MEDIUM, DateFormat.MEDIUM
//                )
//            )
//            holder.usageTime.setText(
//                DateUtils.formatElapsedTime(pkgStats.totalTimeInForeground / 1000)
//            )
//        } else {
//            Log.w(TAG, "No usage stats info for package:$position")
//        }
//        return convertView
//    }
//
//    fun sortList(sortOrder: Int) {
//        if (mDisplayOrder == sortOrder) {
//            // do nothing
//            return
//        }
//        mDisplayOrder = sortOrder
//        sortList()
//    }
//
//    private fun sortList() {
//        if (mDisplayOrder == _DISPLAY_ORDER_USAGE_TIME) {
//            if (localLOGV) Log.i(TAG, "Sorting by usage time")
//            Collections.sort(mPackageStats, mUsageTimeComparator)
//        } else if (mDisplayOrder == _DISPLAY_ORDER_LAST_TIME_USED) {
//            if (localLOGV) Log.i(TAG, "Sorting by last time used")
//            Collections.sort(mPackageStats, mLastTimeUsedComparator)
//        } else if (mDisplayOrder == _DISPLAY_ORDER_APP_NAME) {
//            if (localLOGV) Log.i(TAG, "Sorting by application name")
//            Collections.sort(mPackageStats, mAppLabelComparator)
//        }
//        notifyDataSetChanged()
//    }
//
//    companion object {
//        // Constants defining order for display order
//        private const val _DISPLAY_ORDER_USAGE_TIME = 0
//        private const val _DISPLAY_ORDER_LAST_TIME_USED = 1
//        private const val _DISPLAY_ORDER_APP_NAME = 2
//    }
//
//    init {
//        val cal: Calendar = Calendar.getInstance()
//        cal.add(Calendar.DAY_OF_YEAR, -5)
//        val stats = mUsageStatsManager.queryUsageStats(
//            UsageStatsManager.INTERVAL_BEST,
//            cal.getTimeInMillis(), System.currentTimeMillis()
//        )
//        if (stats == null) {
//            return
//        }
//        val map: ArrayMap<String, UsageStats> = ArrayMap()
//        val statCount = stats!!.size
//        for (i in 0 until statCount) {
//            val pkgStats = stats[i]
//
//            // load application labels for each application
//            try {
//                val appInfo: ApplicationInfo = mPm.getApplicationInfo(pkgStats.packageName, 0)
//                val label = appInfo.loadLabel(mPm).toString()
//                mAppLabelMap.put(pkgStats.packageName, label)
//                val existingStats: UsageStats = map.get(pkgStats.packageName)
//                if (existingStats == null) {
//                    map.put(pkgStats.packageName, pkgStats)
//                } else {
//                    existingStats.add(pkgStats)
//                }
//            } catch (e: PackageManager.NameNotFoundException) {
//                // This package may be gone.
//            }
//        }
//        mPackageStats.addAll(map.values())
//
//        // Sort list
//        mAppLabelComparator = AppNameComparator(mAppLabelMap)
//        sortList()
//    }
//}