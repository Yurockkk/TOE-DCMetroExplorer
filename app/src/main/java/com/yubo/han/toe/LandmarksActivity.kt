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
import com.yubo.han.toe.Services.FetchMetroStationsManager
import com.yubo.han.toe.model.MetroStations
import com.yubo.han.toe.model.NearMetroStations
import kotlinx.android.synthetic.main.activity_landmark_detail.*

import kotlinx.android.synthetic.main.activity_landmarks.*
import kotlinx.android.synthetic.main.row_landmarks.view.*
import kotlinx.android.synthetic.main.row_metro_station.*
import org.jetbrains.anko.toast

class LandmarksActivity : AppCompatActivity(), FetchLandmarksManager.LandmarkSearchCompletionListener, FetchMetroStationsManager.NearMetroSearchCompletionListener {
    private val LOG_TAG = "LandmarksActivity"

    private var curLat = 38.898955.toFloat()
    private var curLon = (-77.042447).toFloat()

    lateinit var mFetchLandmarksManager: FetchLandmarksManager
    lateinit var mFetchMetroStationsManager: FetchMetroStationsManager
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

        //Obtain stationData from station intent
        val stationData = intent.getParcelableExtra<MetroStations>("stationData")
        if (stationData != null) {// From MetroStationActivity
            // Set member variable
            val metroLat = stationData.latitude
            val metroLon = stationData.longitude
            val stationName = stationData.name

            landmark_toolbar_text.text = stationName

            // Query and load landmarks data from yelp api
            loadYelp(metroLat, metroLon)
        }

        else {// From the nearest station intent
            // Get location coordinates---TODO


            // Search the nearest station
            queryNearStations(curLat, curLon)
        }

        // Set up action bar
        setSupportActionBar(landmarkToolbar)

    }


    // find the nearest metro station base on current location
    fun queryNearStations(lat: Float, lon: Float) {
        mFetchMetroStationsManager = FetchMetroStationsManager(this)
        mFetchMetroStationsManager.nearMetroSearchCompletionListener = this

        mFetchMetroStationsManager.queryYelpForNearMetro(lat, lon)
    }

    // Load landmarks data from Yelp based on a station
    fun loadYelp(lat: Float, lon: Float) {
        mFetchLandmarksManager = FetchLandmarksManager(this)
        mFetchLandmarksManager.landmarkSearchCompletionListener = this

        // Get the location from the station coordinates
        mFetchLandmarksManager.queryYelpForLandMarks(lat, lon)

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

    // If successfully get the nearest metro station
    override fun nearMetroLoaded(nearMetro: NearMetroStations) {

        landmark_toolbar_text.text = nearMetro.name

        loadYelp(nearMetro.latitude, nearMetro.longitude)


    }
    // If failed to get the nearest metro station
    override fun nearMetroNotLoaded() {
        toast("No metro found near you")
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
