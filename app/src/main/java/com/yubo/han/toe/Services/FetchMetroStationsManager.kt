package com.yubo.han.toe.Services

import android.content.Context
import android.util.Log
import com.google.gson.JsonObject
import com.koushikdutta.ion.Ion
import com.yubo.han.toe.Constants

/**
 * Created by yubo on 9/25/17.
 */
class FetchMetroStationsManager(val context: Context) {

    private val LOG_TAG = "FetchMetroStationsManager"

    var metroStationsSearchCompletedListener: MetroStationsSearchCompletedListener? = null


    interface MetroStationsSearchCompletedListener{

        fun stationsLoaded()
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
                        val json = parseStationsFromJSON(it)

                        if (json != null) {
                            Log.e(LOG_TAG, "${json}")
                            metroStationsSearchCompletedListener?.stationsLoaded()
                        }else {
                            Log.e(LOG_TAG, "cannot get parsed json")
                            metroStationsSearchCompletedListener?.stationsNotLoaded()
                        }

                    }
                }

    }

    // Get stations list from Jsonobject
    fun parseStationsFromJSON(jsonobject: JsonObject): ArrayList<String>? {

        var stations = arrayListOf<String>()

        val stationsResults = jsonobject.getAsJsonArray("Stations")
        if (stationsResults != null && stationsResults.size() > 0) {
            for (i in 0..stationsResults.size() - 1) {
                var landmarksResult = stationsResults.get(i).toString()
                stations.add(landmarksResult)
            }

            return stations
        }

        return null

    }

}