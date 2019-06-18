package com.google.firebase.quickstart.auth.kotlin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NewsFunLoot {

    @SerializedName("_id")
    @Expose
    var id: String? = null
    @SerializedName("news_title")
    @Expose
    var news_title: String? = null
    @SerializedName("news_body")
    @Expose
    var news_body: String? = null

}