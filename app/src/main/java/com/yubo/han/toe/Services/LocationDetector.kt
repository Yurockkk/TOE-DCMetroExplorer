package com.yubo.han.toe.Services

import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.LocationServices

/**
 * Created by han on 9/25/17.
 */
class LocationDetector(val context: Context) {

    private val LOG_TAG = "LocationDetector"

    var locationDetectCompletedListener: LocationDetectCompletedListener? = null

    interface LocationDetectCompletedListener{

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
}