package com.yubo.han.toe.activity

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.view.View
import android.support.v7.widget.StaggeredGridLayoutManager
import android.widget.ImageView

import com.yubo.han.toe.R
import com.yubo.han.toe.Services.*
import com.yubo.han.toe.model.Landmarks
import com.yubo.han.toe.model.MetroStations
import com.yubo.han.toe.model.NearMetroStations

import kotlinx.android.synthetic.main.activity_landmarks.*
import org.jetbrains.anko.toast
import android.support.v4.util.Pair
import com.yubo.han.toe.model.LandmarkViewModel


class LandmarksActivity : AppCompatActivity(), FetchLandmarksManager.LandmarkSearchCompletionListener,
                            FetchMetroStationsManager.NearMetroSearchCompletionListener, LocationDetector.LocationDetectCompletedListener{

    private val LOG_TAG = "LandmarksActivity"

    lateinit private var mLocationDetector: LocationDetector
    lateinit private var mFetchLandmarksManager: FetchLandmarksManager
    lateinit var mFetchMetroStationsManager: FetchMetroStationsManager
    lateinit private var landmarkAdapter: LandmarksAdapter
    lateinit private var staggeredLayoutManager: StaggeredGridLayoutManager
    lateinit var persistanceManager: PersistanceManager
    lateinit var mViewModel : LandmarkViewModel

    var from: Int = -1      //tell where is this activity get called    -1 -> undefined     1 -> from Select Station button     2 -> from Nearest Station button  3 -> from Favorite Landmark button


    // Click landmark item listener
    var onItemClickListener = object : LandmarksAdapter.OnItemClickListener {
        override fun onItemClick(view: View, landmarkData: Landmarks) {

            // Direct to LandmarkDetail Activity, pass landmark data to the activity
            val landmarkDetailIntent = Intent(this@LandmarksActivity, LandmarkDetailActivity::class.java)
            landmarkDetailIntent.putExtra("landmarkDetail", landmarkData)

            //activity transition
            val lImage = view.findViewById<ImageView>(R.id.landmarkImage)
            val imagePair = Pair.create(lImage as View, "tImage")
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@LandmarksActivity,imagePair)
            startActivity(landmarkDetailIntent,options.toBundle())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landmarks)

        // Set up action bar
        setSupportActionBar(landmarkToolbar)

        mViewModel = ViewModelProviders.of(this).get(LandmarkViewModel::class.java)
        mLocationDetector = LocationDetector(this)
        mLocationDetector.locationDetectCompletedListener = this
        persistanceManager = PersistanceManager(this)

        //Obtain intent from station intent and location intent
        val stationData = intent.getParcelableExtra<MetroStations>("stationData")
        val nearStation = intent.getStringExtra("nearStation")
        val favoriteLandmark = intent.getStringExtra("favorite")

        if (mViewModel.landmarkList.size == 0){
            if (stationData != null) {// From MetroStationActivity
                from = 1
                // Set member variable
                val metroLat = stationData.latitude
                val metroLon = stationData.longitude
                val stationName = stationData.name

                // Display the station name in the app bar
                mViewModel.toolBarText = stationName
                runOnUiThread {
                    supportActionBar?.setTitle(mViewModel.toolBarText)
                }

                // Query and load landmarks data from yelp api
                loadYelp(metroLat, metroLon)
            }

            if (nearStation == "true") {// From the nearest station intent
                from = 2
                // Get location
                mLocationDetector.getDeviceLocationUpdate()

            }

            if (favoriteLandmark == "true") { // from Favorite Landmark intent
                from = 3
                mViewModel.toolBarText = getString(R.string.fav_landmarks)
                runOnUiThread {
                    supportActionBar?.setTitle(mViewModel.toolBarText)
                }

            }


        }else{
            displayLandmarkList(mViewModel.landmarkList)
        }


    }

    override fun onResume() {
        super.onResume()
//        Log.i(LOG_TAG, "onResume")
        runOnUiThread {
            supportActionBar?.setTitle(mViewModel.toolBarText)
        }
        if(from == 3){
            val favLandmarks = persistanceManager.fetchLandmarks()
            displayLandmarkList(favLandmarks as ArrayList<Landmarks>)
        }
    }

    override fun onStop() {
        super.onStop()
        mLocationDetector.stopLocationUpdates()
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


    // Display the queried landmarks in RecyclerView
    fun displayLandmarkList(landmarkList:ArrayList<Landmarks>) {

        // Stop the progress bar once load the data
        landmark_indeterminate_bar.visibility = View.GONE

        staggeredLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        landmarkViewList.layoutManager = staggeredLayoutManager

        landmarkAdapter = LandmarksAdapter(this, landmarkList)
        landmarkViewList.adapter = landmarkAdapter

        // Action on click landmark
        landmarkAdapter.setOnItemClickListener(onItemClickListener)
    }


    /**
     * FetchLandmarksManager.LandmarkSearchCompletionListener implementation
     */
    // If successfully get the landmarks
    override fun landmarkLoaded(landmarkList:ArrayList<Landmarks>) {
        mViewModel.landmarkList = landmarkList
        displayLandmarkList(mViewModel.landmarkList)
    }

    // if failed to get the landmarks
    override fun landmarkNotLoaded() {
        runOnUiThread {
            toast(getString(R.string.no_landmarks))
        }
        finish()
    }

    /**
     * FetchMetroStationsManager.NearMetroSearchCompletionListener implementation
     */
    // If successfully get the nearest metro station
    override fun nearMetroLoaded(nearMetro: NearMetroStations) {

        mViewModel.toolBarText = nearMetro.name

        runOnUiThread {
            supportActionBar?.setTitle(mViewModel.toolBarText)
        }



        loadYelp(nearMetro.latitude, nearMetro.longitude)
    }
    // If failed to get the nearest metro station
    override fun nearMetroNotLoaded() {
        runOnUiThread {
            toast(getString(R.string.no_station_found))
        }
        finish()
    }


    /**
     * LocationDetectCompletedListener implementation
     */
    override fun locationDetected(location: Location) {
        queryNearStations(location.latitude.toFloat(), location.longitude.toFloat())
    }

    override fun locationNotDetected() {
        //toast(getString(R.string.no_location_detected))
        runOnUiThread({
            toast(getString(R.string.no_location_detected))
        })
        finish()
    }

    override fun onLocationChanged(location: Location?) {
        if(location != null) {
            queryNearStations(location.latitude.toFloat(), location.longitude.toFloat())
        } else {      //error handler: if we cannot get location update, then we call 'getDeviceLastLocation()'
            mLocationDetector.getDeviceLastLocation()
        }
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    override fun onProviderEnabled(p0: String?) {
    }

    override fun onProviderDisabled(p0: String?) {
    }

}
