package com.yubo.han.toe.Services

import android.content.Context
import android.util.Log

import com.koushikdutta.ion.Ion
import com.yubo.han.toe.Constants
import com.yubo.han.toe.R
import com.yubo.han.toe.Utilities
import com.yubo.han.toe.model.MetroStations
import com.yubo.han.toe.model.NearMetroStations

/**
 * Created by yubo on 9/25/17.
 */
class FetchMetroStationsManager(val context: Context) {

    private val LOG_TAG = "FetchMetroStationsManager"

    var metroStationsSearchCompletedListener: MetroStationsSearchCompletedListener? = null

    var nearMetroSearchCompletionListener: NearMetroSearchCompletionListener? = null


    interface MetroStationsSearchCompletedListener{

        fun stationsLoaded(stationList: ArrayList<MetroStations>)
        fun stationsNotLoaded()

    }

    interface NearMetroSearchCompletionListener {
        fun nearMetroLoaded(nearMetro: NearMetroStations)
        fun nearMetroNotLoaded()
    }

    // Load all metro stations from MATA API
    fun queryWMATAForAllStations(){

        Ion.with(context)
                .load("${Constants.METRO_ALLSTATION_SEARCH_URL}${context.getString(R.string.METRO_API_KEY)}")
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
                            //Log.e(LOG_TAG, "${stationList}")
                            metroStationsSearchCompletedListener?.stationsLoaded(stationList)
                        }else {
                            Log.e(LOG_TAG, "cannot get parsed json")
                            metroStationsSearchCompletedListener?.stationsNotLoaded()
                        }

                    }
                }
    }



    // Query near metro station from Yelp API
    fun queryYelpForNearMetro(latitude: Float, longitude: Float) {


        Log.d(LOG_TAG, "APIKEY:" + context.getString(R.string.YELP_API_KEY))
        Ion.with(context).load(Constants.YELP_SEARCH_URL)
                .addHeader("Authorization", "Bearer "+ context.getString(R.string.YELP_API_KEY))
                .addQuery("term", Constants.YELP_SEARCH_NEAR_METRO_TERM)
                .addQuery("radius", Constants.YELP_SEARCH_NEAR_METRO_RADIUS.toString())
                .addQuery("latitude", latitude.toString())
                .addQuery("longitude", longitude.toString())
                .addQuery(Constants.YELP_SEARCH_NEAR_METRO_SORT, Constants.YELP_SEARCH_NEAR_METRO_SORT_BY)
                .asJsonObject()
                .setCallback { error, result ->
                    error?.let {
                        Log.e(LOG_TAG, it.message)
                        nearMetroSearchCompletionListener?.nearMetroNotLoaded()
                    }

                    result?.let {
                        val nearMetro= Utilities.parseNearMetroFromJSON(it)

                        if (nearMetro != null) {
                            //Log.e(LOG_TAG, "${landmarkList}")
                            nearMetroSearchCompletionListener?.nearMetroLoaded(nearMetro)
                        }else {
                            Log.e(LOG_TAG, "cannot get parsed json")
                            nearMetroSearchCompletionListener?.nearMetroNotLoaded()
                        }
                    }
                }
    }
}