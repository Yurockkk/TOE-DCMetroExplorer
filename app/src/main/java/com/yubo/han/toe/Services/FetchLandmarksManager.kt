package com.yubo.han.toe.Services

import com.yubo.han.toe.Constants
import com.yubo.han.toe.Utilities
import com.yubo.han.toe.model.Landmarks

import android.content.Context
import android.util.Log

import com.koushikdutta.ion.Ion
import com.yubo.han.toe.R


class FetchLandmarksManager(val context: Context) {
    private val LOG_TAG = "FetchLandmarksManager"


    var landmarkSearchCompletionListener: LandmarkSearchCompletionListener? = null

    interface LandmarkSearchCompletionListener {
        fun landmarkLoaded(landmarkList:ArrayList<Landmarks>)
        fun landmarkNotLoaded()
    }


    //Query landmarks list from Yelp Api
    fun queryYelpForLandMarks(latitude: Float, longitude: Float) {


        Ion.with(context).load(Constants.YELP_SEARCH_URL)
                .addHeader("Authorization", "Bearer "+ context.getString(R.string.YELP_API_KEY))
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