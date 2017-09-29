package com.yubo.han.toe

import com.yubo.han.toe.Services.FetchMetroStationsManager
import com.yubo.han.toe.model.MetroStations

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.Toolbar
import com.yubo.han.toe.Services.MetroStationsAdapter

import kotlinx.android.synthetic.main.activity_metro_stations.*

import org.jetbrains.anko.toast

class MetroStationsActivity : AppCompatActivity(), FetchMetroStationsManager.MetroStationsSearchCompletedListener {


    val LOG_TAG = "MetroStationsActivity"

    lateinit var mFetchMetroStationsManager: FetchMetroStationsManager
    lateinit var mStaggeredLayoutManager: StaggeredGridLayoutManager
    lateinit var mMetroStationsAdapter: MetroStationsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metro_stations)

        // Set up too bar
        val toolbar = findViewById<Toolbar>(R.id.MetroStationToolbar)
        setSupportActionBar(toolbar)

        loadMetroStations()
    }

    private fun loadMetroStations() {
        mFetchMetroStationsManager = FetchMetroStationsManager(this)
        //regist itself to metroStationsCompletedListener
        mFetchMetroStationsManager.metroStationsSearchCompletedListener = this

        //query WMATA for all metro stations
        mFetchMetroStationsManager.queryWMATAForAllStations()

    }

    override fun stationsLoaded(stationList: ArrayList<MetroStations>) {
        //toast("${stationList}")

        displayStationList(stationList)

    }

    override fun stationsNotLoaded() {
        toast("stations not loaded")

    }

    fun displayStationList(stationList: ArrayList<MetroStations>) {

        mStaggeredLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        metroStationViewList.layoutManager = mStaggeredLayoutManager

        mMetroStationsAdapter = MetroStationsAdapter(this, stationList)
        metroStationViewList.adapter = mMetroStationsAdapter
    }
}
