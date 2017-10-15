package com.yubo.han.toe.Services

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.util.Log
import com.google.android.gms.location.*
import com.yubo.han.toe.Utilities
import java.util.*
import kotlin.concurrent.timerTask


class LocationDetector(val context: Context) {

    private val LOG_TAG = "LocationDetector"

    var locationDetectCompletedListener: LocationDetectCompletedListener? = null

    lateinit var mLocationCallback :LocationCallback
    lateinit var mFusedLocationClient : FusedLocationProviderClient

    interface LocationDetectCompletedListener: LocationListener{

        fun locationDetected(location: Location)
        fun locationNotDetected()
    }

    init {
        // create an instance of the Fused Location Provider Client
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    }


    fun getDeviceLastLocation(){

        if(!Utilities.isLocationServiceEnable(context)){
            locationDetectCompletedListener?.locationNotDetected()
        }else{
            try{
                mFusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    //Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        Log.i(LOG_TAG,location.toString())
                        locationDetectCompletedListener?.locationDetected(location)
                    }else{
                        locationDetectCompletedListener?.locationNotDetected()
                    }
                }
            }catch (e:SecurityException){

                Log.e(LOG_TAG,e.toString())
            }
        }

    }


    @SuppressLint("MissingPermission")
    fun getDeviceLocationUpdate(){

        if(!Utilities.isLocationServiceEnable(context)){
            locationDetectCompletedListener?.locationNotDetected()
        }else{
            //create a timer
            var timer = Timer()

            mLocationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {

                    //get locationCallback, remove location updates and timer
                    mFusedLocationClient.removeLocationUpdates(this)
                    timer.cancel()

                    //fire callback with location
                    locationDetectCompletedListener?.onLocationChanged(locationResult?.locations?.first())
                }
            }

            //start a timer to ensure location detection ends after 10 secs
            timer.schedule(timerTask {
                //if time expire, remove location update and fire locationNotDetect callback
                mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                locationDetectCompletedListener?.locationNotDetected()
            },10*1000)

            //create LocationRequest
            var mLocationRequest = LocationRequest()
            mLocationRequest
                    .setInterval(0)
                    .setFastestInterval(0)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setNumUpdates(1)
                    .setExpirationDuration(10000)

            //start location update
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,mLocationCallback,null)
        }

    }

    fun stopLocationUpdates(){

        try{
            mFusedLocationClient?.removeLocationUpdates(mLocationCallback)

        }catch (e:UninitializedPropertyAccessException){
            // haven't been init
        }

    }
    
}