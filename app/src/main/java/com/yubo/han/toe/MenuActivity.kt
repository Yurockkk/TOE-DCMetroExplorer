package com.yubo.han.toe

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_menu.*
import com.yubo.han.toe.Services.LocationDetector
import org.jetbrains.anko.toast

class MenuActivity : AppCompatActivity(), LocationDetector.LocationDetectCompletedListener {



    val LOG_TAG = "MenuActivity"
    val MY_PERMISSIONS_REQUEST_FINE_LOCATION = 666
    //lateinit var mFusedLocationClient: Any
    lateinit var mLocationDetector: LocationDetector


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        mLocationDetector = LocationDetector(this)
        //register itself to metroStationsCompletedListener
        mLocationDetector.locationDetectCompletedListener = this

        initUI()

        //check location permission, if granded -> get location
        checkLocationPermission()
    }

    fun initUI(){
        near_station_button.setOnClickListener(View.OnClickListener {

            val permissionCheck = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
            Log.i(LOG_TAG,"permissionCheck: $permissionCheck")

            if(permissionCheck == PackageManager.PERMISSION_GRANTED){
                //get location permission, start to detect location
//                mLocationDetector.getDeviceLastLocation()
                mLocationDetector.getDeviceLocationUpdate()
            }else{
                //TODO: maybe popup a dialog to tell user we need the permission to use 'near station' function?
                Log.i(LOG_TAG, "ask user for location permission!")
                checkLocationPermission()
            }


        })

        select_station_button.setOnClickListener(View.OnClickListener {
            val metroStationIntent = Intent(this,MetroStationsActivity::class.java)
            startActivity(metroStationIntent)
        })

        favorite_landmark_button.setOnClickListener(View.OnClickListener {
            toast("No favorite landmarks")
        })
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
//        mLocationDetector.stopLocationUpdates();
    }

    fun checkLocationPermission() {

        val permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),MY_PERMISSIONS_REQUEST_FINE_LOCATION)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == MY_PERMISSIONS_REQUEST_FINE_LOCATION){
            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                toast("get location access")
            }else{

            }
        }else{
            toast("unhandled request code")
        }
    }




    //LocationDetectCompletedListener implementation

    override fun locationDetected(location: Location) {

        Log.i(LOG_TAG,"location detected")

    }

    override fun locationNotDetected() {

        Log.i(LOG_TAG, "location not detected")
    }

    override fun onLocationChanged(p0: Location?) {
        Log.i(LOG_TAG, "in onLocationChanged, ${p0.toString()}")
        toast(p0.toString())
    }
    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
        Log.i(LOG_TAG, "in onStatusChanged, ${p0.toString()}")

    }

    override fun onProviderEnabled(p0: String?) {
        Log.i(LOG_TAG, "in onProviderEnabled, ${p0.toString()}")
    }

    override fun onProviderDisabled(p0: String?) {
        Log.i(LOG_TAG, "in onProviderDisabled, ${p0.toString()}")
    }
}
