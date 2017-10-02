package com.yubo.han.toe

import android.content.Intent
import com.yubo.han.toe.Services.FetchLandmarksManager
import com.yubo.han.toe.Services.LandmarksAdapter
import com.yubo.han.toe.model.Landmarks

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.util.Log
import android.widget.Toast

import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.Toolbar
import com.squareup.picasso.Picasso
import com.yubo.han.toe.model.MetroStations
import kotlinx.android.synthetic.main.activity_landmark_detail.*

import kotlinx.android.synthetic.main.activity_landmarks.*
import kotlinx.android.synthetic.main.row_landmarks.view.*
import kotlinx.android.synthetic.main.row_metro_station.*
import org.jetbrains.anko.toast

class LandmarksActivity : AppCompatActivity(), FetchLandmarksManager.LandmarkSearchCompletionListener {
    private val LOG_TAG = "LandmarksActivity"

    private var latitude = 38.9.toFloat()
    private var longitude = (-77.051825).toFloat()
    private var stationName = "foggy bottom"

    lateinit var fetchLandmarksManager: FetchLandmarksManager
    lateinit private var landmarkAdapter: LandmarksAdapter
    lateinit private var staggeredLayoutManager: StaggeredGridLayoutManager

    // Click landmark item listener
    var onItemClickListener = object : LandmarksAdapter.OnItemClickListener {
        override fun onItemClick(view: View, landmarkData: Landmarks) {
            //Toast.makeText(this@LandmarksActivity, "Clicked " + landmarkData.latitude + ": " + view.landmarkName.text, Toast.LENGTH_SHORT).show()

            // Direct to LandmarkDetail Activity, pass landmark data to the activity
            val landmarkDetailIntent = Intent(this@LandmarksActivity,LandmarkDetailActivity::class.java)
            landmarkDetailIntent.putExtra("landmarkDetail", landmarkData)
            startActivity(landmarkDetailIntent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landmarks)

        //Obtain stationData from intent
        val stationData = intent.getParcelableExtra<MetroStations>("stationData")
        // Set member variable
        latitude = stationData.latitude
        longitude = stationData.longitude
        stationName = stationData.name


        // Setup tool bar
        landmark_toolbar_text.text = stationName
        setSupportActionBar(landmarkToolbar)


        // Query and load landmarks data from yelp api
        loadYelp(latitude, longitude)

    }

    fun loadYelp(lat: Float, lon: Float) {
        fetchLandmarksManager = FetchLandmarksManager(this)
        fetchLandmarksManager.landmarkSearchCompletionListener= this

        // Get the location from the station coordinates
//        fetchLandmarksManager.queryYelpForLandMarks(lat, lon)
        fetchLandmarksManager.queryYelpForLandMarks(lat, lon)

    }


    // If successfully get the landmarks
    override fun landmarkLoaded(landmarkList:ArrayList<Landmarks>) {
        //toast(landmarkList.toString())

        displayLandmarkList(landmarkList)
    }

    // if failed to get the landmarks
    override fun landmarkNotLoaded() {
        // Will add a feature
        toast("No landmarks")
    }


    // Display the queried landmarks in RecyclerView
    fun displayLandmarkList(landmarkList:ArrayList<Landmarks>) {

        staggeredLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        landmarkViewList.layoutManager = staggeredLayoutManager

        landmarkAdapter = LandmarksAdapter(this, landmarkList)
        landmarkViewList.adapter = landmarkAdapter

        landmarkAdapter.setOnItemClickListener(onItemClickListener)
    }

}
