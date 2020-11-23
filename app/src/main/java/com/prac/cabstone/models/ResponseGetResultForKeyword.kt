package com.prac.cabstone.models

import com.google.gson.annotations.SerializedName

class ResponseGetResultForKeyword(
    @SerializedName("data")
    private var data: ArrayList<ResponseGetResultForKeywordData>
) {
    fun getData() : ArrayList<ResponseGetResultForKeywordData> {
        return this.data;
    }
}