package com.prac.cabstone.models

import com.google.gson.annotations.SerializedName

class ResponseGetAreaCode(
    @SerializedName("data")
    private var data: ArrayList<ResponseGetAreaCodeData>
) {
    fun getData() : ArrayList<ResponseGetAreaCodeData> {
        return this.data
    }
}