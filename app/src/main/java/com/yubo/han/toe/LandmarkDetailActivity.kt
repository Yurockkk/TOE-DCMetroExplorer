package com.yubo.han.toe

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.yubo.han.toe.model.Landmarks
import kotlinx.android.synthetic.main.activity_landmark_detail.*
import kotlinx.android.synthetic.main.row_landmarks.view.*

class LandmarkDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landmark_detail)

        //Obtain landmarkData from intent
        val landmarkData = intent.getParcelableExtra<Landmarks>("landmarkDetail")

        // Set member variable
        val name = landmarkData.name
        val url = landmarkData.imageUrl
        landmarkDetailNameText.setText(name)
        Picasso.with(this).load(url).into(landmarkImageView)

    }
}
