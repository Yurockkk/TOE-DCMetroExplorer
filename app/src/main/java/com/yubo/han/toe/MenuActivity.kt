package com.yubo.han.toe

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {

    val LOG_TAG = "MenuActivity"
    val MY_PERMISSIONS_REQUEST_FINE_LOCATION = 666
    //lateinit var mFusedLocationClient: Any

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        initUI()
    }

    fun initUI(){
        near_station_button.setOnClickListener(View.OnClickListener {

            val landmarkdsIntent = Intent(this,LandmarksActivity::class.java)
            startActivity(landmarkdsIntent)
        })

        select_station_button.setOnClickListener(View.OnClickListener {
            val metroStationIntent = Intent(this,MetroStationsActivity::class.java)
            startActivity(metroStationIntent)
        })

        favorite_landmark_button.setOnClickListener(View.OnClickListener {
            val favIntent = Intent(this,LandmarkDetailActivity::class.java)
            startActivity(favIntent)
        })
    }
}
