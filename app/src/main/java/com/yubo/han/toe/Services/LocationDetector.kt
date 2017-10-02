package com.yubo.han.toe.Services

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.util.Log
import com.google.android.gms.location.*


class LocationDetector(val context: Context) {

    private val LOG_TAG = "LocationDetector"

    var locationDetectCompletedListener: LocationDetectCompletedListener? = null

    lateinit var mLocationCallback :LocationCallback
    lateinit var mFusedLocationClient : FusedLocationProviderClient

    interface LocationDetectCompletedListener: LocationListener{

        fun locationDetected(location: Location)
        fun locationNotDetected()
    }


    fun getDeviceLastLocation(){

        // create an instance of the Fused Location Provider Client
        var mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
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


    @SuppressLint("MissingPermission")
    fun getDeviceLocationUpdate(){

        // create an instance of the Fused Location Provider Client
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                for (location in locationResult!!.locations) {
                    print("lon: ${location.longitude}, lat: ${location.latitude}")
                    locationDetectCompletedListener?.onLocationChanged(location)
                }
            }


        }


        var mLocationRequest = LocationRequest()
        mLocationRequest
                .setInterval(10000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(1)
                .setExpirationDuration(10000)
        //TODO: need to handle expiration callback?

        //create a LocationSettingsRequest.Builder and add mLocationRequest to it
        val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest)

        // check whether the current location settings are satisfied
        val client = LocationServices.getSettingsClient(context)
        val task = client.checkLocationSettings(builder.build())


        mFusedLocationClient.requestLocationUpdates(mLocationRequest,mLocationCallback,null)

    }

    fun stopLocationUpdates(){

        try{
            mFusedLocationClient?.removeLocationUpdates(mLocationCallback)

        }catch (e:UninitializedPropertyAccessException){
            // haven't been init
        }

    }
    
}