package com.yubo.han.toe.Services

import android.content.Context
import android.util.Log
import com.google.gson.JsonObject
import com.koushikdutta.ion.Ion
import com.yubo.han.toe.Constants
import com.yubo.han.toe.Utilities

/**
 * Created by han on 9/25/17.
 */
class YelpAuthManager {
    val LOG_TAG = "YelpAuthManager"

    // Get access token from yelp
    fun getYelpToken(context: Context): String {
        val json: JsonObject = Ion.with(context).load(Constants.YELP_QUEST_TOKEN_URL)
                .setBodyParameter("grant_type", Constants.YELP_GRANT_TYPE)
                .setBodyParameter("client_id", Constants.YELP_CLIENT_ID)
                .setBodyParameter("client_secret", Constants.YELP_CLIENT_SECRET)
                .asJsonObject()
                .get()

        val token = Utilities.parseTokenFromYelp(json)

        return token
    }

}