package com.yubo.han.toe

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

import kotlinx.android.synthetic.main.activity_landmarks.*
import kotlinx.android.synthetic.main.row_landmarks.view.*
import org.jetbrains.anko.toast

class LandmarksActivity : AppCompatActivity(), FetchLandmarksManager.LandmarkSearchCompletionListener {
    private val LOG_TAG = "LandmarksActivity"

    lateinit var fetchLandmarksManager: FetchLandmarksManager
    lateinit private var landmarkAdapter: LandmarksAdapter
    lateinit private var staggeredLayoutManager: StaggeredGridLayoutManager

    // Click landmark item listener
    var onItemClickListener = object : LandmarksAdapter.OnItemClickListener {
        override fun onItemClick(view: View, position: Int) {
            Toast.makeText(this@LandmarksActivity, "Clicked " + position + ": " +
                    view.landmarkName.text, Toast.LENGTH_SHORT).show()
        }
    }

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
        fetchLandmarksManager.queryYelpForLandMarks(38.9.toFloat(), (-77.051825).toFloat())
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
