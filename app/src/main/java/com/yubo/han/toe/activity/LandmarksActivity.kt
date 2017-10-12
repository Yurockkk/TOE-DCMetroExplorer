package com.yubo.han.toe.activity

import android.content.Intent
import android.location.Location
import com.yubo.han.toe.Services.FetchLandmarksManager
import com.yubo.han.toe.Services.LandmarksAdapter
import com.yubo.han.toe.model.Landmarks

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import android.support.v7.widget.StaggeredGridLayoutManager
import com.yubo.han.toe.R
import com.yubo.han.toe.Services.FetchMetroStationsManager
import com.yubo.han.toe.Services.PersistanceManager
import com.yubo.han.toe.model.MetroStations
import com.yubo.han.toe.model.NearMetroStations

import kotlinx.android.synthetic.main.activity_landmarks.*
import org.jetbrains.anko.toast

class LandmarksActivity : AppCompatActivity(), FetchLandmarksManager.LandmarkSearchCompletionListener,
                            FetchMetroStationsManager.NearMetroSearchCompletionListener{
    private val LOG_TAG = "LandmarksActivity"

    lateinit var mFetchLandmarksManager: FetchLandmarksManager
    lateinit var mFetchMetroStationsManager: FetchMetroStationsManager
    lateinit private var landmarkAdapter: LandmarksAdapter
    lateinit private var staggeredLayoutManager: StaggeredGridLayoutManager
    lateinit var persistanceManager: PersistanceManager



    // Click landmark item listener
    var onItemClickListener = object : LandmarksAdapter.OnItemClickListener {
        override fun onItemClick(view: View, landmarkData: Landmarks) {
            // Direct to LandmarkDetail Activity, pass landmark data to the activity
            val landmarkDetailIntent = Intent(this@LandmarksActivity, LandmarkDetailActivity::class.java)
            landmarkDetailIntent.putExtra("landmarkDetail", landmarkData)
            startActivity(landmarkDetailIntent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landmarks)

        persistanceManager = PersistanceManager(this)

        //Obtain intent from station intent and location intent
        val stationData = intent.getParcelableExtra<MetroStations>("stationData")
        val locationData = intent.getParcelableExtra<Location>("location")


        if (stationData != null) {// From MetroStationActivity
            // Set member variable
            val metroLat = stationData.latitude
            val metroLon = stationData.longitude
            val stationName = stationData.name

            // Display the station name in the app bar
            landmark_toolbar_text.text = stationName

            // Query and load landmarks data from yelp api
            loadYelp(metroLat, metroLon)
        }

        else if (locationData != null) {// From the nearest station intent
            // Get location coordinates
            val curLat = locationData.latitude.toFloat()
            val curLon = locationData.longitude.toFloat()

            // Search the nearest station
            queryNearStations(curLat, curLon)
        } else {
            // Favorite Landmark---TODO
            landmark_toolbar_text.text = getString(R.string.fav_landmarks)
            var favLandmarks = persistanceManager.fetchLandmarks()
            displayLandmarkList(favLandmarks as ArrayList<Landmarks>)
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
