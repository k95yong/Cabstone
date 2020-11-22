package com.prac.cabstone.models

import com.google.gson.annotations.SerializedName

class ResponseGetResultForCurrentData(
    @SerializedName("addr1")
    private var addr1: String,

    @SerializedName("addr2")
    private var addr2: String,

    @SerializedName("areacode")
    private var areacode: Int,

    @SerializedName("contentid")
    private var contentid: Int,

    @SerializedName("firstimage")
    private var firstimage: String,

    @SerializedName("firstimage2")
    private var firstimage2: String,

    @SerializedName("mapx")
    private var mapx: Double,

    @SerializedName("mapy")
    private var mapy: Double,

    @SerializedName("mlevel")
    private var mlevel: Int,

    @SerializedName("readcount")
    private var readcount: Int,

    @SerializedName("title")
    private var title: String

    /*
    @SerializedName("zipcode")
    private var zipcode: String

     */
) {
    fun getAddr1() : String {
        return this.addr1
    }
    fun getAreaCode() : Int {
        return this.areacode
    }
    fun getContentId() : Int {
        return this.contentid
    }
    fun getFirstImage() : String {
        return this.firstimage
    }
    fun getFirstImage2() : String {
        return this.firstimage2
    }
    fun getMapX() : Double {
        return this.mapx
    }
    fun getMapY() : Double {
        return this.mapy
    }
    fun getMLevel() : Int {
        return this.mlevel
    }
    fun getReadCount() : Int {
        return this.readcount
    }
    fun getTitle() : String {
        return this.title
    }
}