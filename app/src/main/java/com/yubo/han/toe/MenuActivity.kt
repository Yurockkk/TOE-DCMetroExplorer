package com.yubo.han.toe

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_menu.*
import org.jetbrains.anko.toast
import com.google.android.gms.location.LocationServices
//import com.google.android.gms.location.FusedLocationProviderClient

class MenuActivity : AppCompatActivity() {

    val LOG_TAG = "MenuActivity"
    val MY_PERMISSIONS_REQUEST_FINE_LOCATION = 666
    //lateinit var mFusedLocationClient: Any

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        initUI()
    }

    fun initUI(){
        near_station_button.setOnClickListener(View.OnClickListener {

            //check location permission, if granded -> get location
            checkLocationPermission()

        })

        select_station_button.setOnClickListener(View.OnClickListener {
            val metroStationIntent = Intent(this,MetroStationsActivity::class.java)
            startActivity(metroStationIntent)
        })

        favorite_landmark_button.setOnClickListener(View.OnClickListener {
            val favIntent = Intent(this,LandmarksActivity::class.java)
            startActivity(favIntent)
        })
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStart() {
        super.onStart()

    }

    fun checkLocationPermission() {

        val permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
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

        when(requestCode){

            MY_PERMISSIONS_REQUEST_FINE_LOCATION -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){ getDeviceLastLocation() }

        }
    }

    fun getDeviceLastLocation(){

        // create an instance of the Fused Location Provider Client
        var mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            mFusedLocationClient.lastLocation
                    .addOnSuccessListener(this) { location ->
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Log.i(LOG_TAG,location.toString())
                            toast(location.toString())
                        }
                    }
        }catch (e:SecurityException){

            Log.e(LOG_TAG,e.toString())
        }

    }
}
