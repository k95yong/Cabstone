package com.prac.cabstone.models

import com.google.gson.annotations.SerializedName

class ResponseGetResultForCurrent(
    @SerializedName("data")
    private var data: ArrayList<ResponseGetResultForCurrentData>
) {
    fun getData() : ArrayList<ResponseGetResultForCurrentData> {
        return this.data;
    }
}