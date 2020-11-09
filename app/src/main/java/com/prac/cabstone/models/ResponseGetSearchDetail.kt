package com.prac.cabstone.models

import com.google.gson.annotations.SerializedName

class ResponseGetSearchDetail(
    @SerializedName("data")
    private var data : ResponseGetSearchDetailData
) {
    fun getData(): ResponseGetSearchDetailData {
        return this.data
    }
}