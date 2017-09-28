package com.yubo.han.toe.Services

import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.gson.JsonObject
import com.koushikdutta.ion.Ion
import com.yubo.han.toe.Constants
import com.yubo.han.toe.model.Landmarks
import org.jetbrains.anko.toast

/**
 * Created by han on 9/25/17.
 */
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
                .addHeader("Authorization", Constants.YELP_SEARCH_TOKEN)
                .addQuery("term", Constants.term)
                .addQuery("radius", Constants.YELP_SEARCH_RADIUS.toString())
                .addQuery("latitude", latitude.toString())
                .addQuery("longitude", longitude.toString())
                .asJsonObject()
                .setCallback { error, result ->
                    error?.let {
                        Log.e(LOG_TAG, it.message)
                        landmarkSearchCompletionListener?.landmarkNotLoaded()
                    }

                    result?.let {
                        val landmarkList = parseLandmarksFromJSON(it)

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


    // Get landmarks list from Jsonobject
    fun parseLandmarksFromJSON(jsonobject: JsonObject): ArrayList<Landmarks>? {

        val landmarksList = arrayListOf<Landmarks>()

        val landmarksArray = jsonobject.getAsJsonArray(Constants.YELP_JSON_MEMBER_NAME)

        if (landmarksArray != null && landmarksArray.size() > 0) {
            for (i in 0..landmarksArray.size() - 1) {
                val json = landmarksArray.get(i).asJsonObject
                val name = json.get("name").asString
                val imageUrl = Uri.parse(json.get("image_url").asString)

                val coordinates = json.get("coordinates")
                val latitude = coordinates.asJsonObject.get("latitude").asFloat
                val longitude = coordinates.asJsonObject.get("longitude").asFloat

                val landmark = Landmarks(name, imageUrl, latitude, longitude)
                landmarksList.add(landmark)
            }

            return landmarksList
        }

        return null
    }
}