package com.yubo.han.toe

import android.content.Context
import android.graphics.Color
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.util.Log
import com.google.gson.JsonObject
import com.yubo.han.toe.model.Landmarks
import com.yubo.han.toe.model.MetroStations
import com.yubo.han.toe.model.NearMetroStations

/**
 * Created by han on 9/28/17.
 */
object Utilities {

    val LOG_TAG = "Utilities"

    // Get Yelp access token from Json object
    fun parseTokenFromYelp(jsonobject: JsonObject): String {

        val type = jsonobject.get("token_type").asString
        val token = jsonobject.get("access_token").asString
        val accessToken = type + " " + token

        return accessToken
    }


    // Get landmarks list from Jsonobject
    fun parseLandmarksFromJSON(jsonobject: JsonObject): ArrayList<Landmarks>? {
        val landmarksList = arrayListOf<Landmarks>()

        // Get json array from the json object
        val landmarksArray = jsonobject.getAsJsonArray(Constants.YELP_JSON_MEMBER_NAME)

        if (landmarksArray != null && landmarksArray.size() > 0) {
            for (i in 0..landmarksArray.size() - 1) {
                val json = landmarksArray.get(i).asJsonObject

                // Get all json elements of yelp API
                val name = json.get("name").asString
                val imageString = json.get("image_url").asString

                val coordinates = json.get("coordinates")
                val latitude = coordinates.asJsonObject.get("latitude").asFloat
                val longitude = coordinates.asJsonObject.get("longitude").asFloat

                val yelpUrl = json.get("url").asString
                val reviewCount = json.get("review_count").asInt
                val distance = json.get("distance").asFloat

                val location = json.get("location").asJsonObject
                val address = location.getAsJsonArray("display_address")
                val displayAddress = StringBuilder()

                for (line in address) {
                    displayAddress.append(line.asString)
                }

                val landmark = Landmarks(name, imageString, latitude, longitude, yelpUrl, reviewCount,
                                        displayAddress.toString(), distance)
                landmarksList.add(landmark)
            }

            return landmarksList
        }

        return null
    }


    // Get stations list from Jsonobject
    fun parseStationsFromJSON(jsonobject: JsonObject): ArrayList<MetroStations>? {
        val stations = arrayListOf<MetroStations>()

        // Get json array from the json object
        val stationsResults = jsonobject.getAsJsonArray(Constants.METRO_JSON_MEMBER_NAME)

        if (stationsResults != null && stationsResults.size() > 0) {
            for (i in 0..stationsResults.size() - 1) {
                val json = stationsResults.get(i).asJsonObject

                // Get all json elements
                val code = json.get("Code").asString
                val name = json.get("Name").asString
                val lat = json.get("Lat").asFloat
                val lon = json.get("Lon").asFloat
                val lineCode1 = json.get("LineCode1").asString
                // Element can be null or empty
                val lineCode2 = if (json.get("LineCode2").isJsonNull) null else json.get("LineCode2").asString
                val lineCode3 = if (json.get("LineCode3").isJsonNull) null else json.get("LineCode3").asString
                val stationTogether1 = json.get("StationTogether1").asString

                val station = MetroStations(code, name, lat, lon, lineCode1, lineCode2, lineCode3,
                        stationTogether1)

                stations.add(station)
            }
            //Log.e(LOG_TAG, "${stations}")

            return stations
        }

        return null
    }


    // Get the nearest metro station from Jsonobject
    fun parseNearMetroFromJSON(jsonobject: JsonObject): NearMetroStations? {
        val metro: NearMetroStations

        val nearMetroResults = jsonobject.getAsJsonArray(Constants.YELP_JSON_MEMBER_NAME)

        if (nearMetroResults != null && nearMetroResults.size() > 0) {
            // We get the results sorted by the "distance"
            // get the first one is the nearest one
            val json = nearMetroResults.get(0).asJsonObject

            // Get metro elements
            val stationName = json.get("name").asString
            val coordinates = json.get("coordinates")
            val latitude = coordinates.asJsonObject.get("latitude").asFloat
            val longitude = coordinates.asJsonObject.get("longitude").asFloat
            val distance = json.asJsonObject.get("distance").asFloat

            metro = NearMetroStations(stationName, latitude, longitude, distance)

            return metro
        }

        return null

    }


    // Get Line Color from the linecode of metro station
    fun getLineColor(lineColor: String) = when(lineColor) {
        "RD" ->  R.drawable.button_bg_metro_line_red
        "BL" ->  R.drawable.button_bg_metro_line_blue
        "SV" ->  R.drawable.button_bg_metro_line_silver
        "OR" ->  R.drawable.button_bg_metro_line_orange
        "GR" ->  R.drawable.button_bg_metro_line_green
        "YL" ->  R.drawable.button_bg_metro_line_yellow
        else ->  R.drawable.button_bg_round
    }

    //check if we have WiFi or GPS
    fun isNetworkAvailable(context: Context): Boolean{
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }


    // Check if the LocationService is enabled
    fun isLocationServiceEnable(context: Context): Boolean{
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var networkEnable = false
        var gpsEnable = false

        try {
            networkEnable = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.message)
        }

        try {
            gpsEnable = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.message)
        }

        return networkEnable || gpsEnable
    }
}