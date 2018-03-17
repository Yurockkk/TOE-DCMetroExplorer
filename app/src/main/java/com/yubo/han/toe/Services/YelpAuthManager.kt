package com.yubo.han.toe.Services

import android.content.Context
import android.content.res.Resources
import com.google.gson.JsonObject
import com.koushikdutta.ion.Ion
import com.yubo.han.toe.Constants
import com.yubo.han.toe.R
import com.yubo.han.toe.Utilities


class YelpAuthManager {
    val LOG_TAG = "YelpAuthManager"

    // Get access token from yelp
    fun getYelpToken(context: Context): String {
        val json: JsonObject = Ion.with(context).load(Constants.YELP_QUEST_TOKEN_URL)
                .setBodyParameter("grant_type", Constants.YELP_GRANT_TYPE)
                .setBodyParameter("client_id", Resources.getSystem().getString(R.string.YELP_API_KEY))
                .setBodyParameter("client_secret", Constants.YELP_CLIENT_SECRET)
                .asJsonObject()
                .get()

        return Utilities.parseTokenFromYelp(json)
    }

}