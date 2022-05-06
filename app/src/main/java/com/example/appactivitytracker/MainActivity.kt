package com.example.appactivitytracker

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ListView
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity(), OnItemSelectedListener {

    private var mUsageStatsManager: UsageStatsManager? = null
    private var mInflater: LayoutInflater? = null
    private var mAdapter: HomeAdapter? = null
    private var mPm: PackageManager? = null

    override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        setContentView(R.layout.activity_main)
        mUsageStatsManager = getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
        mInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mPm = packageManager
        val typeSpinner = findViewById<View>(R.id.typeSpinner) as Spinner
        typeSpinner.onItemSelectedListener = this
        val listView = findViewById<View>(R.id.pkg_list) as ListView
        mAdapter = HomeAdapter()
        listView.adapter = mAdapter
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        mAdapter!!.sortList(position)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // do nothing
    }

    private fun requestPermissions() {
        val stats = mUsageStatsManager!!
            .queryUsageStats(UsageStatsManager.INTERVAL_DAILY, 0, System.currentTimeMillis())
        val isEmpty = stats.isEmpty()
        if (isEmpty) {
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }
    }
}