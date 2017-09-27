package com.yubo.han.toe

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.yubo.han.toe.Services.FetchMetroStationsManager

class MetroStationsActivity : AppCompatActivity() {

    val LOG_TAG = "MetroStationsActivity"
    lateinit var mFetchMetroStationsManager: FetchMetroStationsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metro_stations)

        loapMetroStations()
    }

    private fun loapMetroStations() {
        mFetchMetroStationsManager = FetchMetroStationsManager(this)
        mFetchMetroStationsManager.queryWMATAForAllStations()
    }
}
