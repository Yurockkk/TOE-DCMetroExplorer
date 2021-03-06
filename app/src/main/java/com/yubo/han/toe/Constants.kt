package com.yubo.han.toe


object Constants {

    // Yelp search
    val YELP_SEARCH_TERM = "landmarks"
    val YELP_SEARCH_URL = "https://api.yelp.com/v3/businesses/search"
    val YELP_JSON_MEMBER_NAME = "businesses"
    val YELP_SEARCH_RADIUS = 2500

    // Yelp search near metro station
    val YELP_SEARCH_NEAR_METRO_TERM = "Metro Stations"
    val YELP_SEARCH_NEAR_METRO_RADIUS = 40000
    val YELP_SEARCH_NEAR_METRO_SORT = "sort_by"
    val YELP_SEARCH_NEAR_METRO_SORT_BY = "distance"

    //WMATA search API
    val METRO_ALLSTATION_SEARCH_URL = "https://api.wmata.com/Rail.svc/json/jStations?oauth_signature_method=HMAC-SHA1&oauth_timestamp=1506294098&oauth_nonce=V41Ok3&oauth_version=1.0&oauth_signature=KCb50NS6o9prToEAYXK/l+MtMZY=&api_key="
    val METRO_JSON_MEMBER_NAME = "Stations"
    val LANDMARKS_PREF_KEY = "LANDMARKS"

    val CENTIMETER_TO_MILE = 0.000621371192
}