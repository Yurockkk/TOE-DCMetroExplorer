package com.yubo.han.toe

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.google.gson.JsonObject
import com.koushikdutta.ion.Ion
import com.yubo.han.toe.Services.FetchLandmarksManager
import org.jetbrains.anko.toast

class LandmarksActivity : AppCompatActivity() {

    lateinit var fetchLandmarksManager: FetchLandmarksManager

    private val LOG_TAG = "LandmarksActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landmarks)

//        val json = queryYelpForLandMarks("foggybottom")
//        toast("${json}")

        loadYelp()
    }

    fun loadYelp() {
        fetchLandmarksManager = FetchLandmarksManager(this)
        //To do
        fetchLandmarksManager.queryYelpForLandMarks("foggybottom")

    }

}
