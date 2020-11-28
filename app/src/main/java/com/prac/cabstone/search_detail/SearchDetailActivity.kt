package com.prac.cabstone.search_detail

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import com.prac.cabstone.*
import com.prac.cabstone.models.ResponseGetSearchDetail
import com.prac.cabstone.schedule.ScheduleChoiceDialogActivity
import com.softsquared.myapplication.db.Todo
import kotlinx.android.synthetic.main.activity_search_detail.*
import kotlinx.android.synthetic.main.custom_dialog.view.*
import kotlinx.android.synthetic.main.custom_dialog.view.et_dialog_contents
import kotlinx.android.synthetic.main.dialog_schedule_adder.view.*
import kotlinx.android.synthetic.main.dialog_schedule_adder.view.et_dialog_sel_group
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.android.synthetic.main.fragment_schedule.view.*
import kotlinx.android.synthetic.main.item_today.view.*
import kotlinx.android.synthetic.main.schedule_add_dialog.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SearchDetailActivity : BaseActivity() {
    lateinit var search_title: String
    lateinit var dialogView: View
    lateinit var viewModel: MainViewModel
    var addr1 = ""
    var firstImage = ""
    var contentId = 1
    var mapX = 0.0
    var mapY = 0.0
    var g_id: Long? = null
    var g_name: String? = null
    private var mLanguage: String? = "ko"
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 100){
            g_name = data?.getStringExtra("g_name")
            dialogView.et_dialog_sel_group.setText(g_name)
            // data를 어떻게 받는거지?
            g_id = data?.getStringExtra("gid")?.toLong()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_detail)

        mLanguage = ApplicationClass.prefs.myLANGUAGE

        getSearchDetail()

        viewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        add_to_schedule.setOnClickListener {
            lateinit var dialogText: EditText
            lateinit var et_dialog_sel_date: EditText
            lateinit var et_dialog_sel_group: EditText
            val cal = Calendar.getInstance()
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val today_date = sdf.format(cal.time)
            dialogView = layoutInflater.inflate(R.layout.dialog_schedule_adder, null)
            et_dialog_sel_group = dialogView.findViewById(R.id.et_dialog_sel_group)
            dialogText = dialogView.findViewById(R.id.et_dialog_contents)
            et_dialog_sel_date = dialogView.findViewById(R.id.et_dialog_sel_date)
            et_dialog_sel_date.setText(today_date)
            var set_arr = ArrayList<String>()
            dialogText.setText(search_title)

            et_dialog_sel_group.setOnClickListener {
                intent = Intent(this, ScheduleChoiceDialogActivity::class.java)
                startActivityForResult(intent, 100)
            }

            et_dialog_sel_date.setOnClickListener {
                var selected_date = today_date
                val year = cal.get(Calendar.YEAR)
                val month = cal.get(Calendar.MONTH)
                val day = cal.get(Calendar.DAY_OF_MONTH)
                set_arr = ArrayList()
                set_arr.add(today_date)
                val dpd = DatePickerDialog(
                    this!!,
                    DatePickerDialog.OnDateSetListener { view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                        set_arr = ArrayList()
                        cal.set(Calendar.YEAR, year)
                        cal.set(Calendar.MONTH, monthOfYear)
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        selected_date = (year.toString() + "-")
                        if (monthOfYear + 1 < 10) {
                            selected_date += "0"
                        }
                        selected_date += ((monthOfYear + 1).toString() + "-")
                        if (dayOfMonth < 10) {
                            selected_date += "0"
                        }
                        selected_date += dayOfMonth.toString()
                        et_dialog_sel_date.setText(selected_date)
                        set_arr.add(selected_date)
                        cal.time = Date()
                    },
                    year,
                    month,
                    day
                )
                dpd.show()
            }
            val builder = AlertDialog.Builder(this!!)

            builder.setView(dialogView)
                .setPositiveButton("확인") { dialogInterface, i ->
                    if (dialogView.et_dialog_contents.text.isEmpty()) {
                        Toast.makeText(this, "일정을 입력해 주세요.", Toast.LENGTH_LONG).show()
                    } else {
                        if (set_arr.size == 0) {
                            set_arr.add(today_date)
                        }
                        if(dialogView.et_dialog_contents.text.isEmpty()){
                            Toast.makeText(this, "그룹을 선택해 주세요", Toast.LENGTH_SHORT).show()
                        }else{
                            val tmpgid = viewModel.getNewGid()
                            set_arr.forEach{
                                viewModel.insert(
                                    Todo(
                                        dialogView.et_dialog_contents.text.toString(),
                                        false,
                                        it,
                                        tmpgid,
                                        g_name!!,
                                        viewModel.getTodoIdx(it),
                                        mapX,
                                        mapY,
                                        firstImage = firstImage,
                                        addr1 = addr1,
                                        contentId = contentId
                                    )
                                )
                            }
                        }
                    }
                }
                .setNegativeButton("취소") { dialogInterface, i ->
                }
                .show()
        }


    }


    private fun getSearchDetail() {
        showProgressDialog()
        val api = SearchDetailAPI.create()

        when (mLanguage) {
            "ko" -> {
                api.getSearchDetail(intent.getIntExtra("content_id", 0)).enqueue(object :
                    Callback<ResponseGetSearchDetail> {
                    override fun onResponse(call: Call<ResponseGetSearchDetail>, response: Response<ResponseGetSearchDetail>) {
                        hideProgressDialog()
                        var responseGetSearchDetail = response.body()
                        if (responseGetSearchDetail != null) {
                            search_title = responseGetSearchDetail.getData().getTitle()
                            search_detail_tv_title.text = search_title
                            addr1 = responseGetSearchDetail.getData().getAddr1()
                            search_detail_tv_location.text = "주소 : $addr1"
                            firstImage = responseGetSearchDetail.getData().getFirstimage()
                            search_detail_tv_content.text = responseGetSearchDetail.getData().getOverview()
                            GlideApp.with(this@SearchDetailActivity).load(firstImage)
                                .centerCrop()
                                .into(search_detail_iv_image)
                            mapX = responseGetSearchDetail.getData().getMapx()
                            mapY = responseGetSearchDetail.getData().getMapy()
                            contentId = responseGetSearchDetail.getData().getContentid()

                        }
                    }

                    override fun onFailure(call: Call<ResponseGetSearchDetail>, t: Throwable) {
                        hideProgressDialog()
                        showCustomToast(resources.getString(R.string.network_error))
                        t.printStackTrace()
                    }
                })
            }
            "en" -> {
                api.getSearchDetail_en(intent.getIntExtra("content_id", 0)).enqueue(object :
                    Callback<ResponseGetSearchDetail> {
                    override fun onResponse(call: Call<ResponseGetSearchDetail>, response: Response<ResponseGetSearchDetail>) {
                        hideProgressDialog()
                        var responseGetSearchDetail = response.body()
                        if (responseGetSearchDetail != null) {
                            search_title = responseGetSearchDetail.getData().getTitle()
                            search_detail_tv_title.text = search_title
                            addr1 = responseGetSearchDetail.getData().getAddr1()
                            search_detail_tv_location.text = "주소 : $addr1"
                            firstImage = responseGetSearchDetail.getData().getFirstimage()
                            search_detail_tv_content.text = responseGetSearchDetail.getData().getOverview()
                            GlideApp.with(this@SearchDetailActivity).load(firstImage)
                                .centerCrop()
                                .into(search_detail_iv_image)
                            mapX = responseGetSearchDetail.getData().getMapx()
                            mapY = responseGetSearchDetail.getData().getMapy()
                            contentId = responseGetSearchDetail.getData().getContentid()

                        }
                    }

                    override fun onFailure(call: Call<ResponseGetSearchDetail>, t: Throwable) {
                        hideProgressDialog()
                        showCustomToast(resources.getString(R.string.network_error))
                        t.printStackTrace()
                    }
                })
            }
            "jp" -> {
                api.getSearchDetail_jp(intent.getIntExtra("content_id", 0)).enqueue(object :
                    Callback<ResponseGetSearchDetail> {
                    override fun onResponse(call: Call<ResponseGetSearchDetail>, response: Response<ResponseGetSearchDetail>) {
                        hideProgressDialog()
                        var responseGetSearchDetail = response.body()
                        if (responseGetSearchDetail != null) {
                            search_title = responseGetSearchDetail.getData().getTitle()
                            search_detail_tv_title.text = search_title
                            addr1 = responseGetSearchDetail.getData().getAddr1()
                            search_detail_tv_location.text = "주소 : $addr1"
                            firstImage = responseGetSearchDetail.getData().getFirstimage()
                            search_detail_tv_content.text = responseGetSearchDetail.getData().getOverview()
                            GlideApp.with(this@SearchDetailActivity).load(firstImage)
                                .centerCrop()
                                .into(search_detail_iv_image)
                            mapX = responseGetSearchDetail.getData().getMapx()
                            mapY = responseGetSearchDetail.getData().getMapy()
                            contentId = responseGetSearchDetail.getData().getContentid()

                        }
                    }

                    override fun onFailure(call: Call<ResponseGetSearchDetail>, t: Throwable) {
                        hideProgressDialog()
                        showCustomToast(resources.getString(R.string.network_error))
                        t.printStackTrace()
                    }
                })
            }
            "ch" -> {
                api.getSearchDetail_ch(intent.getIntExtra("content_id", 0)).enqueue(object :
                    Callback<ResponseGetSearchDetail> {
                    override fun onResponse(call: Call<ResponseGetSearchDetail>, response: Response<ResponseGetSearchDetail>) {
                        hideProgressDialog()
                        var responseGetSearchDetail = response.body()
                        if (responseGetSearchDetail != null) {
                            search_title = responseGetSearchDetail.getData().getTitle()
                            search_detail_tv_title.text = search_title
                            addr1 = responseGetSearchDetail.getData().getAddr1()
                            search_detail_tv_location.text = "주소 : $addr1"
                            firstImage = responseGetSearchDetail.getData().getFirstimage()
                            search_detail_tv_content.text = responseGetSearchDetail.getData().getOverview()
                            GlideApp.with(this@SearchDetailActivity).load(firstImage)
                                .centerCrop()
                                .into(search_detail_iv_image)
                            mapX = responseGetSearchDetail.getData().getMapx()
                            mapY = responseGetSearchDetail.getData().getMapy()
                            contentId = responseGetSearchDetail.getData().getContentid()

                        }
                    }

                    override fun onFailure(call: Call<ResponseGetSearchDetail>, t: Throwable) {
                        hideProgressDialog()
                        showCustomToast(resources.getString(R.string.network_error))
                        t.printStackTrace()
                    }
                })
            }
            "ge" -> {
                api.getSearchDetail_ge(intent.getIntExtra("content_id", 0)).enqueue(object :
                    Callback<ResponseGetSearchDetail> {
                    override fun onResponse(call: Call<ResponseGetSearchDetail>, response: Response<ResponseGetSearchDetail>) {
                        hideProgressDialog()
                        var responseGetSearchDetail = response.body()
                        if (responseGetSearchDetail != null) {
                            search_title = responseGetSearchDetail.getData().getTitle()
                            search_detail_tv_title.text = search_title
                            addr1 = responseGetSearchDetail.getData().getAddr1()
                            search_detail_tv_location.text = "주소 : $addr1"
                            firstImage = responseGetSearchDetail.getData().getFirstimage()
                            search_detail_tv_content.text = responseGetSearchDetail.getData().getOverview()
                            GlideApp.with(this@SearchDetailActivity).load(firstImage)
                                .centerCrop()
                                .into(search_detail_iv_image)
                            mapX = responseGetSearchDetail.getData().getMapx()
                            mapY = responseGetSearchDetail.getData().getMapy()
                            contentId = responseGetSearchDetail.getData().getContentid()

                        }
                    }

                    override fun onFailure(call: Call<ResponseGetSearchDetail>, t: Throwable) {
                        hideProgressDialog()
                        showCustomToast(resources.getString(R.string.network_error))
                        t.printStackTrace()
                    }
                })
            }
            "sp" -> {
                api.getSearchDetail_sp(intent.getIntExtra("content_id", 0)).enqueue(object :
                    Callback<ResponseGetSearchDetail> {
                    override fun onResponse(call: Call<ResponseGetSearchDetail>, response: Response<ResponseGetSearchDetail>) {
                        hideProgressDialog()
                        var responseGetSearchDetail = response.body()
                        if (responseGetSearchDetail != null) {
                            search_title = responseGetSearchDetail.getData().getTitle()
                            search_detail_tv_title.text = search_title
                            addr1 = responseGetSearchDetail.getData().getAddr1()
                            search_detail_tv_location.text = "주소 : $addr1"
                            firstImage = responseGetSearchDetail.getData().getFirstimage()
                            search_detail_tv_content.text = responseGetSearchDetail.getData().getOverview()
                            GlideApp.with(this@SearchDetailActivity).load(firstImage)
                                .centerCrop()
                                .into(search_detail_iv_image)
                            mapX = responseGetSearchDetail.getData().getMapx()
                            mapY = responseGetSearchDetail.getData().getMapy()
                            contentId = responseGetSearchDetail.getData().getContentid()

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
    }
}