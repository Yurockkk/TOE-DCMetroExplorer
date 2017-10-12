package com.yubo.han.toe.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.squareup.picasso.Picasso
import com.yubo.han.toe.model.Landmarks
import kotlinx.android.synthetic.main.activity_landmark_detail.*
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.yubo.han.toe.R
import com.yubo.han.toe.Services.PersistanceManager
import org.jetbrains.anko.toast


class LandmarkDetailActivity : AppCompatActivity() {

    private val LOG_TAG = "LandmarkDetailActivity"
    lateinit var persistanceManager: PersistanceManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landmark_detail)

        //Obtain landmarkData from intent
        val landmarkData = intent.getParcelableExtra<Landmarks>("landmarkDetail")
        Log.i(LOG_TAG,landmarkData.name)

        landmarkDetailToolbar.title = landmarkData.name
        // Set up tool bar
        setSupportActionBar(landmarkDetailToolbar)

        // Set member variable
        val name = landmarkData.name
        val url = landmarkData.imageString
        landmarkDetailNameText.setText(name)
        Picasso.with(this).load(Uri.parse(url)).into(landmarkImageView)

        // get lat & lon
        var lat = landmarkData.latitude
        var lon = landmarkData.longitude
        googleBtn.setOnClickListener(View.OnClickListener {

            val gmmIntentUri = Uri.parse("google.navigation:q=$lat, $lon&mode=w")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.`package` = "com.google.android.apps.maps"
            startActivity(mapIntent)
        })

        persistanceManager = PersistanceManager(this)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_landmarks_detail, menu)
        return true
    }

    fun addLandmarkPressed(item: MenuItem){
        toast("addLandmarkPressed")
        val landmarkData = intent.getParcelableExtra<Landmarks>("landmarkDetail")
//        Log.i(LOG_TAG,"landmark.name= ${landmarkData.name}, landmark.imageString= ${landmarkData.imageString}, landmark.lat = ${landmarkData.latitude}, landmark.lon = ${landmarkData.longitude}")
        val favLandmark = landmarkData
        //Log.i(LOG_TAG, "save landmark data: ${landmarkData.toString()}")
        persistanceManager.saveLandmark(favLandmark)

    }

    fun sharePressed(item: MenuItem){
        toast("sharePressed")
    }
}
