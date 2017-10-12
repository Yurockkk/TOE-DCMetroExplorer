package com.yubo.han.toe.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by han on 9/27/17.
 */
@Parcelize
data class Landmarks (val name: String, val imageString: String?, val latitude: Float, val longitude: Float,
                        val yelpUrl: String?, val reviewCount: Int, val address: String, val distance: Float): Parcelable