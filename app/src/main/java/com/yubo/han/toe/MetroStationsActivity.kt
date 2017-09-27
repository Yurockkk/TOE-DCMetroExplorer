package com.yubo.han.toe

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.yubo.han.toe.Services.FetchMetroStationsManager
import org.jetbrains.anko.toast

class MetroStationsActivity : AppCompatActivity(), FetchMetroStationsManager.MetroStationsCompletedListener {


    val LOG_TAG = "MetroStationsActivity"
    lateinit var mFetchMetroStationsManager: FetchMetroStationsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metro_stations)

        loapMetroStations()
    }

    private fun loapMetroStations() {
        mFetchMetroStationsManager = FetchMetroStationsManager(this)

        //regist itself to metroStationsCompletedListener
        mFetchMetroStationsManager.metroStationsCompletedListener = this

        //query WMATA for all metro stations
        mFetchMetroStationsManager.queryWMATAForAllStations()

    }

    override fun stationsLoaded() {
        toast("stations loaded")
    }

    override fun stationsNotLoaded() {
        toast("stations not loaded")

    }
}
