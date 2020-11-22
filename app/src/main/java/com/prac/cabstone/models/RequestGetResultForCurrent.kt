package com.prac.cabstone.models

import com.google.gson.annotations.SerializedName

class RequestGetResultForCurrent(
    @SerializedName("mapX")
    private var mapX: Double,

    @SerializedName("mapY")
    private var mapY: Double,

    @SerializedName("radius")
    private var radius: Int
) {

}