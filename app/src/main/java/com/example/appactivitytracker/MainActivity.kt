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

    private var usageStatsManager: UsageStatsManager? = null
    private var inflater: LayoutInflater? = null
    private var pm: PackageManager? = null
//    private lateinit var homeAdapter: HomeAdapter
    private var homeAdapter: HomeAdapter? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager?
        requestPermissions()
        inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
        pm = packageManager

        val typeSpinner = findViewById<Spinner>(R.id.typeSpinner)
        typeSpinner.onItemSelectedListener = this

        val listView = findViewById<ListView>(R.id.pkg_list)
//        homeAdapter = HomeAdapter()
        listView.adapter = homeAdapter

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        homeAdapter?.sortList(position)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //Do Nothing
    }

    private fun requestPermissions() {
        val stats = usageStatsManager!!
            .queryUsageStats(UsageStatsManager.INTERVAL_DAILY, 0, System.currentTimeMillis())
        val isEmpty = stats.isEmpty()
        if (isEmpty) {
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }
    }
}