package com.yubo.han.toe.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View

import com.yubo.han.toe.R
import kotlinx.android.synthetic.main.activity_menu.*

import com.yubo.han.toe.Services.PersistanceManager
import org.jetbrains.anko.toast
import com.yubo.han.toe.Utilities


class MenuActivity : AppCompatActivity() {

    val LOG_TAG = "MenuActivity"
    val MY_PERMISSIONS_REQUEST_FINE_LOCATION = 666
    lateinit var persistanceManager: PersistanceManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        persistanceManager = PersistanceManager(this)

        initUI()

        //check location permission, if granded -> get location
        checkLocationPermission()
    }

    fun initUI(){
        near_station_button.setOnClickListener(View.OnClickListener {

            val permissionCheck = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
//            Log.i(LOG_TAG,"permissionCheck: $permissionCheck")

            if(permissionCheck == PackageManager.PERMISSION_GRANTED) {
                if(Utilities.isNetworkAvailable(this)){
                    val nearStationIntent = Intent(this, LandmarksActivity::class.java)
                    nearStationIntent.putExtra("nearStation", "true")
                    startActivity(nearStationIntent)
                }
                else {
                    toast(getString(R.string.no_network_ability))
                }
            }

            else {
                toast(getString(R.string.ask_for_permission))
                //checkLocationPermission()
            }
        })

        // Click select station button
        select_station_button.setOnClickListener(View.OnClickListener {
            if(Utilities.isNetworkAvailable(this)) {
                val metroStationIntent = Intent(this, MetroStationsActivity::class.java)
                startActivity(metroStationIntent)
            }
            else {
                toast(getString(R.string.no_network_ability))
            }
        })


        // Click favorite button
        favorite_landmark_button.setOnClickListener(View.OnClickListener {
            val favoriteIntent = Intent(this, LandmarksActivity::class.java)
            favoriteIntent.putExtra("favorite", "true")
            startActivity(favoriteIntent)
        })
    }


    fun checkLocationPermission() {

        //check if we have ACCESS_FINE_LOCATION permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //We don't have ACCESS_FINE_LOCATION permission, request the permission.
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),MY_PERMISSIONS_REQUEST_FINE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == MY_PERMISSIONS_REQUEST_FINE_LOCATION){
            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                toast(getString(R.string.permission_granted))
            }else{
                toast(getString(R.string.permission_declined))
            }
        }else{
            //other request permission
        }
    }

}
