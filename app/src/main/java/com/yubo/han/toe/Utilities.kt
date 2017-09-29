package com.yubo.han.toe

import android.net.Uri
import com.google.gson.JsonObject
import com.yubo.han.toe.model.Landmarks

/**
 * Created by han on 9/28/17.
 */
object Utilities {

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