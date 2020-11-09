package com.prac.cabstone.models

import com.google.gson.annotations.SerializedName

class ResponseGetSearchDetailData(
    @SerializedName("contentid")
    private var contentid: Int,

    @SerializedName("homepage")
    private var homepage: String,

    @SerializedName("tel")
    private var tel: String,

    @SerializedName("telname")
    private var telname: String,

    @SerializedName("title")
    private var title: String,

    @SerializedName("firstimage")
    private var firstimage: String,

    @SerializedName("firstimage2")
    private var firstimage2: String,

    @SerializedName("areacode")
    private var areacode: String,

    @SerializedName("addr1")
    private var addr1: String,

    @SerializedName("addr2")
    private var addr2: String,

    @SerializedName("zipcode")
    private var zipcode: String,

    @SerializedName("mapx")
    private var mapx: Double,

    @SerializedName("mapy")
    private var mapy: Double,

    @SerializedName("overview")
    private var overview: String
) {
    fun getContentid() : Int {
        return this.contentid
    }
    fun getHomepage() : String {
        return this.homepage
    }
    fun getTel() : String {
        return this.tel
    }
    fun getTelname() : String {
        return this.telname
    }
    fun getTitle() : String {
        return this.title
    }
    fun getFirstimage() : String {
        return this.firstimage
    }
    fun getFirstimage2() : String {
        return this.firstimage2
    }
    fun getAreacode() : String {
        return this.areacode
    }
    fun getAddr1() : String {
        return this.addr1
    }
    fun getAddr2() : String {
        return this.addr2
    }
    fun getAipcode() : String {
        return this.zipcode
    }
    fun getMapx() : Double {
        return this.mapx
    }
    fun getMapy() : Double {
        return this.mapy
    }
    fun getOverview() : String {
        return this.overview
    }
}