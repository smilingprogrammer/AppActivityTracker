package com.example.appactivitytracker

import android.R
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ListView
import android.widget.Spinner
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment


class HomeFragment : Fragment(), OnItemSelectedListener {

    private val TAG = "UsageStatsActivity"

    private var usageStatsManager: UsageStatsManager? = null
    private var inflater: LayoutInflater? = null
    private var pm: PackageManager? = null
    private lateinit var adapter: HomeAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
//        inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        pm = pack
        usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        pm = get

        adapter = HomeAdapter()
    }

//    protected override fun onCreate(icicle: Bundle?) {
//        super.onCreate(icicle)
//        setContentView(R.layout.usage_stats)
//        mUsageStatsManager = (getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager?)!!
//        mInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
//        mPm = getPackageManager()
//        val typeSpinner = findViewById(R.id.typeSpinner) as Spinner
//        typeSpinner.onItemSelectedListener = this
//        val listView: ListView = findViewById(R.id.pkg_list) as ListView
//        mAdapter = UsageStatsAdapter()
//        listView.setAdapter(mAdapter)
//    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        adapter.sortList(position)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //Do Nothing
    }
}