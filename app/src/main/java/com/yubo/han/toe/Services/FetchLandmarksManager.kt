package com.yubo.han.toe.Services

import com.yubo.han.toe.Constants
import com.yubo.han.toe.Utilities
import com.yubo.han.toe.model.Landmarks

import android.content.Context
import android.util.Log

import com.koushikdutta.ion.Ion
import com.yubo.han.toe.model.MetroStations
import com.yubo.han.toe.model.NearMetroStations
import org.jetbrains.anko.toast

/**
 * Created by han on 9/25/17.
 */
class FetchLandmarksManager(val context: Context) {
    private val LOG_TAG = "FetchLandmarksManager"

    lateinit var yelpAuthManager: YelpAuthManager


    var landmarkSearchCompletionListener: LandmarkSearchCompletionListener? = null

    interface LandmarkSearchCompletionListener {
        fun landmarkLoaded(landmarkList:ArrayList<Landmarks>)
        fun landmarkNotLoaded()
    }


    //Query landmarks list from Yelp Api
    fun queryYelpForLandMarks(latitude: Float, longitude: Float) {

        //Get auth token from Yelp
        yelpAuthManager = YelpAuthManager()
        val accessToken = yelpAuthManager.getYelpToken(context)

        Ion.with(context).load(Constants.YELP_SEARCH_URL)
                .addHeader("Authorization", accessToken)
                .addQuery("term", Constants.YELP_SEARCH_TERM)
                .addQuery("radius", Constants.YELP_SEARCH_RADIUS.toString())
                .addQuery("latitude", latitude.toString())
                .addQuery("longitude", longitude.toString())
                .addQuery(Constants.YELP_SEARCH_NEAR_METRO_SORT, Constants.YELP_SEARCH_NEAR_METRO_SORT_BY)
                .asJsonObject()
                .setCallback { error, result ->
                    error?.let {
                        Log.e(LOG_TAG, it.message)
                        landmarkSearchCompletionListener?.landmarkNotLoaded()
                    }

                    result?.let {
                        val landmarkList = Utilities.parseLandmarksFromJSON(it)

                        if (landmarkList != null) {
                            //Log.e(LOG_TAG, "${landmarkList}")
                            landmarkSearchCompletionListener?.landmarkLoaded(landmarkList)
                        }else {
                            Log.e(LOG_TAG, "cannot get parsed json")
                            landmarkSearchCompletionListener?.landmarkNotLoaded()
                        }
                    }
                }
    }
}