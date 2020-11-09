package com.prac.cabstone.search_detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import com.prac.cabstone.BaseActivity
import com.prac.cabstone.GlideApp
import com.prac.cabstone.R
import com.prac.cabstone.models.ResponseGetInfoForArea
import com.prac.cabstone.models.ResponseGetSearchDetail
import com.prac.cabstone.search_result.GetInfoForAreaCodeAPI
import com.prac.cabstone.search_result.SearchResultBottomSheet
import kotlinx.android.synthetic.main.activity_search_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchDetailActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_detail)

        getSearchDetail()
    }

    private fun getSearchDetail() {
        showProgressDialog()
        val api = SearchDetailAPI.create()

        api.getSearchDetail(intent.getIntExtra("content_id", 0)).enqueue(object :
            Callback<ResponseGetSearchDetail> {
            override fun onResponse(call: Call<ResponseGetSearchDetail>, response: Response<ResponseGetSearchDetail>) {
                hideProgressDialog()
                var responseGetSearchDetail = response.body()
                if (responseGetSearchDetail != null) {
                    search_detail_tv_title.text = responseGetSearchDetail.getData().getTitle()
                    search_detail_tv_location.text = responseGetSearchDetail.getData().getAddr1()
                    GlideApp.with(this@SearchDetailActivity).load(responseGetSearchDetail.getData().getFirstimage())
                        .centerCrop()
                        .into(search_detail_iv_image)
                }
            }

            override fun onFailure(call: Call<ResponseGetSearchDetail>, t: Throwable) {
                hideProgressDialog()
                showCustomToast(resources.getString(R.string.network_error))
                t.printStackTrace()
            }
        })
    }
}