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
import com.yubo.han.toe.Constants
import com.yubo.han.toe.R
import com.yubo.han.toe.Services.PersistanceManager
import org.jetbrains.anko.toast


class LandmarkDetailActivity : AppCompatActivity() {

    private val LOG_TAG = "LandmarkDetailActivity"
    lateinit var persistanceManager: PersistanceManager
    lateinit var landmarkData: Landmarks


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landmark_detail)

        //Obtain landmarkData from intent
        landmarkData = intent.getParcelableExtra<Landmarks>("landmarkDetail")
//        Log.i(LOG_TAG,landmarkData.name)

        initUI(landmarkData)

        // Favorite button
        persistanceManager = PersistanceManager(this)

        // Google directin link
        var lat = landmarkData.latitude
        var lon = landmarkData.longitude
        google_link.setOnClickListener(View.OnClickListener {

            val gmmIntentUri = Uri.parse("google.navigation:q=$lat, $lon&mode=w")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.`package` = "com.google.android.apps.maps"
            startActivity(mapIntent)
        })

        // yelp link
        val yelpUrl = landmarkData.yelpUrl
        yelp_link.setOnClickListener(View.OnClickListener {

            val yelpPage = Uri.parse(yelpUrl)
            val yelpIntent = Intent(Intent.ACTION_VIEW, yelpPage)
            startActivity(yelpIntent)
        })

    }

    // Display landmark information
    fun initUI(landmarkData: Landmarks) {

        landmarkDetailToolbar.title = landmarkData.name

        // Set up tool bar
        setSupportActionBar(landmarkDetailToolbar)

        // Set member variable
        val name = landmarkData.name
        val url = landmarkData.imageString
        val address = landmarkData.address
        val distance = "%.2f".format(landmarkData.distance * Constants.CENTIMETER_TO_MILE) + resources.getString(R.string.distance_unit)

        // Set values to display
        landmarkDetail_name.setText(name)
        landmarkDetail_address.setText(address)
        landmarkDetail_distance.setText(distance)
        Picasso.with(this).load(Uri.parse(url)).into(landmarkDetail_image)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_landmarks_detail, menu)

        // Check if the current landmarks saved or not, then display different layout
        val isFavorite = persistanceManager.isFavorite(landmarkData)
        if (isFavorite) {
            menu?.getItem(0)?.setIcon(R.drawable.ic_favorite_white_24dp)
        }
        else {
            menu?.getItem(0)?.setIcon(R.drawable.ic_favorite_border_white_24dp)
        }

        return true
    }

    // Add or remove favorite landmark
    fun addLandmarkPressed(item: MenuItem){
//        toast("addLandmarkPressed")

        val isSave = persistanceManager.saveLandmark(landmarkData)
        if (isSave) {
            toast(getString(R.string.add_to_favorite))
            item.setIcon(R.drawable.ic_favorite_white_24dp)
        }

        else {
            toast(getString(R.string.remove_from_favorite))
            item.setIcon(R.drawable.ic_favorite_border_white_24dp)
        }
    }

    // Share landmark info to other apps
    fun sharePressed(item: MenuItem){
        val landmarkInfo = landmarkData.name + "\n" + landmarkData.address + "\n" + landmarkData.yelpUrl

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
        shareIntent.putExtra(Intent.EXTRA_SUBJECT,  landmarkData.name)
        shareIntent.putExtra(Intent.EXTRA_TEXT, landmarkInfo)

        val title = resources.getString(R.string.share)
        // Create intent to show chooser
        val chooser = Intent.createChooser(shareIntent, title)

        // Verify the intent will resolve to at least one activity
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
        startActivity(chooser)
        }
    }
}
