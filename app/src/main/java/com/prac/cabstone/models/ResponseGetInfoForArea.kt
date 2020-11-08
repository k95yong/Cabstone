package com.prac.cabstone.models

import com.google.gson.annotations.SerializedName

class ResponseGetInfoForArea(
    @SerializedName("data")
    private var data: ArrayList<ResponseGetInfoForAreaData>
) {
    fun getData() : ArrayList<ResponseGetInfoForAreaData> {
        return this.data;
    }
}