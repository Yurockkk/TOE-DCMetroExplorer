package com.yubo.han.toe.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class NearMetroStations (val name: String, val latitude: Float, val longitude: Float, val distance: Float): Parcelable