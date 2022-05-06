package com.example.appactivitytracker

import android.app.Activity
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.format.DateUtils
import android.util.ArrayMap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.text.DateFormat
import java.util.*

class UsageStatsActivity1 : Activity(), AdapterView.OnItemSelectedListener {
    private var mUsageStatsManager: UsageStatsManager? = null
    private var mInflater: LayoutInflater? = null
    private var mAdapter: UsageStatsAdapter? = null
    private var mPm: PackageManager? = null

    // Constants defining order for display order
    private val _DISPLAY_ORDER_USAGE_TIME = 0
    private val _DISPLAY_ORDER_LAST_TIME_USED = 1
    private val _DISPLAY_ORDER_APP_NAME = 2

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

    // View Holder used when displaying views
    internal class AppViewHolder {
        var pkgName: TextView? = null
        var lastTimeUsed: TextView? = null
        var usageTime: TextView? = null
    }

    internal inner class UsageStatsAdapter : BaseAdapter() {
        private var mDisplayOrder = _DISPLAY_ORDER_USAGE_TIME
        private val mLastTimeUsedComparator = LastTimeUsedComparator()
        private val mUsageTimeComparator = UsageTimeComparator()
        private val mAppLabelComparator: AppNameComparator
        private val mAppLabelMap = ArrayMap<String, String>()
        private val mPackageStats = ArrayList<UsageStats>()
        override fun getCount(): Int {
            return mPackageStats.size
        }

        override fun getItem(position: Int): Any {
            return mPackageStats[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            // A ViewHolder keeps references to children views to avoid unneccessary calls
            // to findViewById() on each row.
            var convertView = convertView
            val holder: AppViewHolder

            // When convertView is not null, we can reuse it directly, there is no need
            // to reinflate it. We only inflate a new View when the convertView supplied
            // by ListView is null.
            if (convertView == null) {
                convertView = mInflater!!.inflate(R.layout.usage_stats_item, null)

                // Creates a ViewHolder and store references to the two children views
                // we want to bind data to.
                holder = AppViewHolder()
                holder.pkgName = convertView.findViewById<View>(R.id.package_name) as TextView
                holder.lastTimeUsed =
                    convertView.findViewById<View>(R.id.last_time_used) as TextView
                holder.usageTime = convertView.findViewById<View>(R.id.usage_time) as TextView
                convertView.tag = holder
            } else {
                // Get the ViewHolder back to get fast access to the TextView
                // and the ImageView.
                holder = convertView.tag as AppViewHolder
            }

            // Bind the data efficiently with the holder
            val pkgStats = mPackageStats[position]
            if (pkgStats != null) {
                val label = mAppLabelMap[pkgStats.packageName]
                holder.pkgName!!.text = label
                holder.lastTimeUsed!!.text = DateUtils.formatSameDayTime(
                    pkgStats.lastTimeUsed,
                    System.currentTimeMillis(), DateFormat.MEDIUM, DateFormat.MEDIUM
                )
                holder.usageTime!!.text =
                    DateUtils.formatElapsedTime(pkgStats.totalTimeInForeground / 1000)
            } else {
                Log.w(
                    TAG,
                    "No usage stats info for package:$position"
                )
            }
            return convertView!!
        }

        fun sortList(sortOrder: Int) {
            if (mDisplayOrder == sortOrder) {
                // do nothing
                return
            }
            mDisplayOrder = sortOrder
            sortList()
        }

        private fun sortList() {
            if (mDisplayOrder == _DISPLAY_ORDER_USAGE_TIME) {
                if (localLOGV) Log.i(TAG, "Sorting by usage time")
                Collections.sort(mPackageStats, mUsageTimeComparator)
            } else if (mDisplayOrder == _DISPLAY_ORDER_LAST_TIME_USED) {
                if (localLOGV) Log.i(TAG, "Sorting by last time used")
                Collections.sort(mPackageStats, mLastTimeUsedComparator)
            } else if (mDisplayOrder == _DISPLAY_ORDER_APP_NAME) {
                if (localLOGV) Log.i(TAG, "Sorting by application name")
                Collections.sort(mPackageStats, mAppLabelComparator)
            }
            notifyDataSetChanged()
        }

        init {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, -5)
            val stats = mUsageStatsManager!!.queryUsageStats(
                UsageStatsManager.INTERVAL_BEST,
                cal.timeInMillis, System.currentTimeMillis()
            )
            val map = ArrayMap<String, UsageStats>()
            val statCount = stats!!.size
            for (i in 0 until statCount) {
                val pkgStats = stats[i]

                // load application labels for each application
                try {
                    val appInfo = mPm!!.getApplicationInfo(pkgStats.packageName, 0)
                    val label = appInfo.loadLabel(mPm!!).toString()
                    mAppLabelMap[pkgStats.packageName] = label
                    val existingStats = map[pkgStats.packageName]
                    if (existingStats == null) {
                        map[pkgStats.packageName] = pkgStats
                    } else {
                        existingStats.add(pkgStats)
                    }
                } catch (e: PackageManager.NameNotFoundException) {
                    // This package may be gone.
                }
            }
            mPackageStats.addAll(map.values)

            // Sort list
            mAppLabelComparator = AppNameComparator(mAppLabelMap)
            sortList()
        }
    }

    /** Called when the activity is first created.  */
    override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        setContentView(R.layout.activity_main)
        mUsageStatsManager = getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
        mInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mPm = packageManager
        val typeSpinner = findViewById<View>(R.id.typeSpinner) as Spinner
        typeSpinner.onItemSelectedListener = this
        val listView = findViewById<View>(R.id.pkg_list) as ListView
        mAdapter = UsageStatsAdapter()
        listView.adapter = mAdapter
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        mAdapter!!.sortList(position)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // do nothing
    }

    companion object {
        private const val TAG = "UsageStatsActivity"
        private const val localLOGV = false
    }
}