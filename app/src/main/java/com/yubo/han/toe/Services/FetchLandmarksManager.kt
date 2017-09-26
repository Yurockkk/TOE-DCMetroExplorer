package com.yubo.han.toe.Services

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.google.gson.JsonObject
import com.koushikdutta.ion.Ion
import com.yubo.han.toe.Constants
import org.jetbrains.anko.toast

/**
 * Created by han on 9/25/17.
 */
class FetchLandmarksManager(val context: Context) {
    private val LOG_TAG = "FetchLandmarksManager"
//    var imageSearchCompletionListener: ImageSearchCompletionListener? = null
//
//    interface ImageSearchCompletionListener {
//        fun imageLoaded()
//        fun imageNotLoaded()
//    }



    //Query landmarks list from Yelp Api
    fun queryYelpForLandMarks(address: String) {
        Ion.with(context).load(Constants.YELP_SEARCH_URL)
                .addHeader("Authorization", Constants.YELP_SEARCH_TOKEN)
                .addQuery("term", Constants.term)
                .addQuery("location", address)
                .asJsonObject()
                .setCallback { error, result ->
                    error?.let {
                        Log.e(LOG_TAG, it.message)
                    }

                    result?.let {
                        val json = parseLandmarksFromJSON(it)

                        if (json != null) {
                            Log.e(LOG_TAG, "${json}")
                        }else {
                            Log.e(LOG_TAG, "cannot get parsed json")
                        }

                    }
                }

    }


    // Get landmarks list from Jsonobject
    fun parseLandmarksFromJSON(jsonobject: JsonObject): String? {

        val landmarksResults = jsonobject.getAsJsonArray("businesses").get(1).toString()
        return landmarksResults



    }






//    fun queryYelpForLandMarks(address: String): JsonObject? {
//        try {
//            return Ion.with(context).load(Constants.YELP_SEARCH_URL)
//                    .addHeader("Authorization", Constants.YELP_SEARCH_TOKEN)
//                    .addQuery("term", Constants.term)
//                    .addQuery("location", address)
//                    .asJsonObject().get()
//        } catch (e: Exception) {
//            Log.e(LOG_TAG, e.message)
//            return null
//        }
//    }

}