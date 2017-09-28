package com.yubo.han.toe


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log


import com.yubo.han.toe.Services.FetchLandmarksManager
import com.yubo.han.toe.Services.LandmarksAdapter
import com.yubo.han.toe.model.Landmarks

import kotlinx.android.synthetic.main.activity_landmarks.*
import org.jetbrains.anko.toast

class LandmarksActivity : AppCompatActivity(), FetchLandmarksManager.LandmarkSearchCompletionListener {


    lateinit var fetchLandmarksManager: FetchLandmarksManager

    lateinit private var landmarkAdapter: LandmarksAdapter

    lateinit private var staggeredLayoutManager: StaggeredGridLayoutManager

    private val LOG_TAG = "LandmarksActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landmarks)

        // Set up too bar
        val toolbar = findViewById<Toolbar>(R.id.landmarkToolbar)
        setSupportActionBar(toolbar)


        // Query and load landmarks data from yelp api
        loadYelp()
    }

    fun loadYelp() {
        fetchLandmarksManager = FetchLandmarksManager(this)

        fetchLandmarksManager.landmarkSearchCompletionListener= this

        // Will get the location from the Location service
        fetchLandmarksManager.queryYelpForLandMarks(38.9.toFloat(), -77.051825.toFloat())

    }

    override fun landmarkLoaded(landmarkList:ArrayList<Landmarks>) {
        //toast(landmarkList.toString())

        displayLandmarkList(landmarkList)

    }

    override fun landmarkNotLoaded() {
        // Will add a feature
        toast("landmarks not loaded")

    }


    fun displayLandmarkList(landmarkList:ArrayList<Landmarks>) {

        staggeredLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        landmarkViewList.layoutManager = staggeredLayoutManager

        landmarkAdapter = LandmarksAdapter(this, landmarkList)
        landmarkViewList.adapter = landmarkAdapter
    }

}
