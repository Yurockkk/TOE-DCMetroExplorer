package com.yubo.han.toe.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.squareup.picasso.Picasso
import com.yubo.han.toe.model.Landmarks
import kotlinx.android.synthetic.main.activity_landmark_detail.*
import android.content.Intent
import android.net.Uri
import com.yubo.han.toe.R


class LandmarkDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landmark_detail)

        //Obtain landmarkData from intent
        val landmarkData = intent.getParcelableExtra<Landmarks>("landmarkDetail")

        landmarkDetailToolbar.title = landmarkData.name
        // Set up tool bar
        setSupportActionBar(landmarkDetailToolbar)

        // Set member variable
        val name = landmarkData.name
        val url = landmarkData.imageUrl
        landmarkDetailNameText.setText(name)
        Picasso.with(this).load(url).into(landmarkImageView)

        // get lat & lon
        var lat = landmarkData.latitude
        var lon = landmarkData.longitude
        googleBtn.setOnClickListener(View.OnClickListener {

            val gmmIntentUri = Uri.parse("google.navigation:q=$lat, $lon&mode=w")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.`package` = "com.google.android.apps.maps"
            startActivity(mapIntent)
        })

    }
}
