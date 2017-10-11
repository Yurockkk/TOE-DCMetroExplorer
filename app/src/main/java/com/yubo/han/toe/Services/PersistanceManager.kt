package com.yubo.han.toe.Services

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yubo.han.toe.Constants
import com.yubo.han.toe.model.Landmarks

/**
 * Created by han on 9/25/17.
 */
class PersistanceManager(context: Context) {

    val LOG_TAG = "PersistanceManager"
    val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun saveLandmark(landmark: Landmarks){

        val landmarks = fetchLandmarks().toMutableList()
        landmarks.add(landmark)

        val editor = sharedPreferences.edit()
        editor.putString(Constants.LANDMARKS_PREF_KEY,Gson().toJson(landmarks))

        editor.apply()
        Log.i(LOG_TAG,"landmarks size: ${landmarks.size}, content: ${landmarks.toString()}")
    }

    fun fetchLandmarks(): List<Landmarks>{
        var landmarksJson = sharedPreferences.getString(Constants.LANDMARKS_PREF_KEY,null)
        //Log.i(LOG_TAG,"fetchLandmarks: ${landmarksJson.toString()}")
        if(landmarksJson == null){
            return arrayListOf<Landmarks>()
        }
        else{
//            val landmarksType = object : TypeToken<MutableList<Landmarks>>() {}.type
            val landmarksType = object : TypeToken<MutableList<Landmarks>>() {}.type
//            return Gson().fromJson(landmarksJson, landmarksType)
            return Gson().fromJson(landmarksJson,landmarksType)
        }
    }

}