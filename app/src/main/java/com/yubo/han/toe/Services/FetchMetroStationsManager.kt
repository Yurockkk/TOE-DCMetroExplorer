package com.yubo.han.toe.Services

import android.content.Context
import android.util.Log

import com.google.gson.JsonObject
import com.koushikdutta.ion.Ion
import com.yubo.han.toe.Constants
import com.yubo.han.toe.Utilities
import com.yubo.han.toe.model.MetroStations

import org.jetbrains.anko.toast

/**
 * Created by yubo on 9/25/17.
 */
class FetchMetroStationsManager(val context: Context) {

    private val LOG_TAG = "FetchMetroStationsManager"

    var metroStationsSearchCompletedListener: MetroStationsSearchCompletedListener? = null


    interface MetroStationsSearchCompletedListener{

        fun stationsLoaded(stationList: ArrayList<MetroStations>)
        fun stationsNotLoaded()

    }

    fun queryWMATAForAllStations(){

        Ion.with(context)
                .load(Constants.METRO_ALLSTATION_SEARCH_URL)
                .asJsonObject()
                .setCallback { error, result ->
                    error?.let {
                        Log.e(LOG_TAG, it.message)
                        metroStationsSearchCompletedListener?.stationsNotLoaded()
                    }

                    result?.let {
                        //Log.i(LOG_TAG, result.toString())
                        val stationList = Utilities.parseStationsFromJSON(it)

                        if (stationList != null) {
                            Log.e(LOG_TAG, "${stationList}")
                            metroStationsSearchCompletedListener?.stationsLoaded(stationList)
                        }else {
                            Log.e(LOG_TAG, "cannot get parsed json")
                            metroStationsSearchCompletedListener?.stationsNotLoaded()
                        }

                    }
                }
    }
}