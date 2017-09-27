package com.yubo.han.toe.model

import android.content.Context
import android.net.Uri

/**
 * Created by han on 9/27/17.
 */
data class Landmarks (val name: String, val imageUrl: Uri, val latitude: Float,
                      val longitude: Float) {
}