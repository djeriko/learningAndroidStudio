package com.google.firebase.quickstart.auth.kotlin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ScoreFunLoot {

    @SerializedName("_id")
    @Expose
    var id: String? = null
    @SerializedName("team_1_name")
    @Expose
    var team_1_name: String? = null
    @SerializedName("team_2_name")
    @Expose
    var team_2_name: String? = null
    @SerializedName("team_1_score")
    @Expose
    var team_1_score: String? = null
    @SerializedName("team_2_score")
    @Expose
    var team_2_score:String? = null
}