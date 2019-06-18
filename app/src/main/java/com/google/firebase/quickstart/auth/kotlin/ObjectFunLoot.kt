package com.google.firebase.quickstart.auth.kotlin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ObjectFunLoot {

    @SerializedName("_id")
    @Expose
    var id: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null

}