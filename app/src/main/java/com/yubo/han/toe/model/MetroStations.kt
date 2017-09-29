package com.yubo.han.toe.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by han on 9/28/17.
 */
@Parcelize
data class MetroStations (val code: String, val name: String, val latitude: Float, val longitude: Float,
                          val lineCode1: String, val lineCode2: String?, val lineCode3: String?,
                          val stationTogether1: String?): Parcelable