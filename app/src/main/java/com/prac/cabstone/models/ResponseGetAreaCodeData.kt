package com.prac.cabstone.models

import com.google.gson.annotations.SerializedName

class ResponseGetAreaCodeData(
    @SerializedName("code")
    private var code: Int,
    @SerializedName("name")
    private var name: String
) {
    fun getCode() : Int {
        return this.code;
    }
    fun getName() : String {
        return this.name;
    }
}