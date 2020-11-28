package com.prac.cabstone.search

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.prac.cabstone.ApplicationClass
import com.prac.cabstone.BaseActivity
import com.prac.cabstone.GpsTracker
import com.prac.cabstone.R
import com.prac.cabstone.current_location_result.CurrentLocationActivity
import com.prac.cabstone.keyword_search_result.KeywordSearchResultActivity
import com.prac.cabstone.models.ResponseGetAreaCode
import com.prac.cabstone.search_result.SearchResultActivity
import kotlinx.android.synthetic.main.activity_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : BaseActivity() {

    private var REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private val GPS_ENABLE_REQUEST_CODE = 2001
    private val PERMISSIONS_REQUEST_CODE = 100
    private lateinit var mGeocoder : Geocoder
    private var mLanguage: String? = "ko"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        mLanguage = ApplicationClass.prefs.myLANGUAGE

        when (mLanguage) {
            "ko" -> {
                search_et_keyword.hint = resources.getString(R.string.search_edit_text_hint)
                search_btn_location.text = resources.getString(R.string.location_search_btn)
                search_btn_current_location.text = resources.getString(R.string.current_location_search_btn)
                search_btn_keyword_search.text = resources.getString(R.string.keyword_search_btn)
                search_btn_cancel.text = resources.getString(R.string.cancel)
            }
            "en" -> {
                search_et_keyword.hint = resources.getString(R.string.en_search_edit_text_hint)
                search_btn_location.text = resources.getString(R.string.en_location_search_btn)
                search_btn_current_location.text = resources.getString(R.string.en_current_location_search_btn)
                search_btn_keyword_search.text = resources.getString(R.string.en_keyword_search_btn)
                search_btn_cancel.text = resources.getString(R.string.en_cancel)
            }
            "jp" -> {
                search_et_keyword.hint = resources.getString(R.string.jp_search_edit_text_hint)
                search_btn_location.text = resources.getString(R.string.jp_location_search_btn)
                search_btn_current_location.text = resources.getString(R.string.jp_current_location_search_btn)
                search_btn_keyword_search.text = resources.getString(R.string.jp_keyword_search_btn)
                search_btn_cancel.text = resources.getString(R.string.jp_cancel)
            }
            "ch" -> {
                search_et_keyword.hint = resources.getString(R.string.ch_search_edit_text_hint)
                search_btn_location.text = resources.getString(R.string.ch_location_search_btn)
                search_btn_current_location.text = resources.getString(R.string.ch_current_location_search_btn)
                search_btn_keyword_search.text = resources.getString(R.string.ch_keyword_search_btn)
                search_btn_cancel.text = resources.getString(R.string.ch_cancel)
            }
            "ge" -> {
                search_et_keyword.hint = resources.getString(R.string.ge_search_edit_text_hint)
                search_btn_location.text = resources.getString(R.string.ge_location_search_btn)
                search_btn_current_location.text = resources.getString(R.string.ge_current_location_search_btn)
                search_btn_keyword_search.text = resources.getString(R.string.ge_keyword_search_btn)
                search_btn_cancel.text = resources.getString(R.string.ge_cancel)
            }
            "sp" -> {
                search_et_keyword.hint = resources.getString(R.string.sp_search_edit_text_hint)
                search_btn_location.text = resources.getString(R.string.sp_location_search_btn)
                search_btn_current_location.text = resources.getString(R.string.sp_current_location_search_btn)
                search_btn_keyword_search.text = resources.getString(R.string.sp_keyword_search_btn)
                search_btn_cancel.text = resources.getString(R.string.sp_cancel)
            }
        }

        search_btn_keyword_search.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                if (!checkLocationServicesStatus()) {
                    showDialogForLocationServiceSetting()
                } else {
                    checkRunTimePermission()
                }
                mGeocoder = Geocoder(this@SearchActivity)

                val gpsTracker = GpsTracker(this@SearchActivity)
                var lat = gpsTracker.getLatitude()
                var lon = gpsTracker.getLongitude()
                if (lat == 0.0 && lon == 0.0) {
                    lat = 37.552302
                    lon = 126.992189
                }

                var intent = Intent(this@SearchActivity, KeywordSearchResultActivity::class.java)
                intent.putExtra("keyword", search_et_keyword.text.toString())
                intent.putExtra("lat", lat)
                intent.putExtra("lon", lon)
                startActivity(intent)
            }
        })

        search_btn_location.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                getAreaCode()
            }
        })

        search_btn_current_location.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                if (!checkLocationServicesStatus()) {
                    showDialogForLocationServiceSetting()
                } else {
                    checkRunTimePermission()
                }
                mGeocoder = Geocoder(this@SearchActivity)

                val gpsTracker = GpsTracker(this@SearchActivity)
                var lat = gpsTracker.getLatitude()
                var lon = gpsTracker.getLongitude()
                if (lat == 0.0 && lon == 0.0) {
                    lat = 37.552302
                    lon = 126.992189
                }

                var intent = Intent(this@SearchActivity, CurrentLocationActivity::class.java)
                intent.putExtra("lat", lat)
                intent.putExtra("lon", lon)
                startActivity(intent)
            }
        })
    }

    private fun getAreaCode() {
        showProgressDialog()
        val api = GetAreaCodeAPI.create()

        when (mLanguage) {
            "ko" -> {
                api.getAreaCode().enqueue(object : Callback<ResponseGetAreaCode> {
                    override fun onResponse(call: Call<ResponseGetAreaCode>, response: Response<ResponseGetAreaCode>) {
                        hideProgressDialog()
                        var responseGetAreaCode = response.body()
                        if (responseGetAreaCode != null) {
                            showLocationDialog(responseGetAreaCode)
                        }
                    }

                    override fun onFailure(call: Call<ResponseGetAreaCode>, t: Throwable) {
                        hideProgressDialog()
                        showCustomToast(resources.getString(R.string.network_error))
                        t.printStackTrace()
                    }
                })
            }
            "en" -> {
                api.getAreaCode_en().enqueue(object : Callback<ResponseGetAreaCode> {
                    override fun onResponse(call: Call<ResponseGetAreaCode>, response: Response<ResponseGetAreaCode>) {
                        hideProgressDialog()
                        var responseGetAreaCode = response.body()
                        if (responseGetAreaCode != null) {
                            showLocationDialog(responseGetAreaCode)
                        }
                    }

                    override fun onFailure(call: Call<ResponseGetAreaCode>, t: Throwable) {
                        hideProgressDialog()
                        showCustomToast(resources.getString(R.string.network_error))
                        t.printStackTrace()
                    }
                })
            }
            "jp" -> {
                api.getAreaCode_jp().enqueue(object : Callback<ResponseGetAreaCode> {
                    override fun onResponse(call: Call<ResponseGetAreaCode>, response: Response<ResponseGetAreaCode>) {
                        hideProgressDialog()
                        var responseGetAreaCode = response.body()
                        if (responseGetAreaCode != null) {
                            showLocationDialog(responseGetAreaCode)
                        }
                    }

                    override fun onFailure(call: Call<ResponseGetAreaCode>, t: Throwable) {
                        hideProgressDialog()
                        showCustomToast(resources.getString(R.string.network_error))
                        t.printStackTrace()
                    }
                })
            }
            "ch" -> {
                api.getAreaCode_ch().enqueue(object : Callback<ResponseGetAreaCode> {
                    override fun onResponse(call: Call<ResponseGetAreaCode>, response: Response<ResponseGetAreaCode>) {
                        hideProgressDialog()
                        var responseGetAreaCode = response.body()
                        if (responseGetAreaCode != null) {
                            showLocationDialog(responseGetAreaCode)
                        }
                    }

                    override fun onFailure(call: Call<ResponseGetAreaCode>, t: Throwable) {
                        hideProgressDialog()
                        showCustomToast(resources.getString(R.string.network_error))
                        t.printStackTrace()
                    }
                })
            }
            "ge" -> {
                api.getAreaCode_ge().enqueue(object : Callback<ResponseGetAreaCode> {
                    override fun onResponse(call: Call<ResponseGetAreaCode>, response: Response<ResponseGetAreaCode>) {
                        hideProgressDialog()
                        var responseGetAreaCode = response.body()
                        if (responseGetAreaCode != null) {
                            showLocationDialog(responseGetAreaCode)
                        }
                    }

                    override fun onFailure(call: Call<ResponseGetAreaCode>, t: Throwable) {
                        hideProgressDialog()
                        showCustomToast(resources.getString(R.string.network_error))
                        t.printStackTrace()
                    }
                })
            }
            "sp" -> {
                api.getAreaCode_sp().enqueue(object : Callback<ResponseGetAreaCode> {
                    override fun onResponse(call: Call<ResponseGetAreaCode>, response: Response<ResponseGetAreaCode>) {
                        hideProgressDialog()
                        var responseGetAreaCode = response.body()
                        if (responseGetAreaCode != null) {
                            showLocationDialog(responseGetAreaCode)
                        }
                    }

                    override fun onFailure(call: Call<ResponseGetAreaCode>, t: Throwable) {
                        hideProgressDialog()
                        showCustomToast(resources.getString(R.string.network_error))
                        t.printStackTrace()
                    }
                })
            }
        }
    }

    private fun showLocationDialog(responseGetAreaCode: ResponseGetAreaCode){
        var list : ArrayList<String> = ArrayList()
        for (i in responseGetAreaCode.getData()) {
            list.add(i.getName())
        }

        // Initialize a new instance of alert dialog builder object
        val builder = AlertDialog.Builder(this)

        // Set a title for alert dialog
        builder.setTitle("지역을 선택해주세요")

        // Set items form alert dialog
        builder.setItems(list.toTypedArray()) { _, which ->
            // Get the dialog selected item
            val selected = list[which]
            val selectedCode = responseGetAreaCode.getData()[which].getCode()

            var intent = Intent(this@SearchActivity, SearchResultActivity::class.java)
            intent.putExtra("area_code", selectedCode)
            startActivity(intent)

            showCustomToast(selected + "선택, " + selectedCode)
        }

        // Create a new AlertDialog using builder object
        val dialog = builder.create()

        // Finally, display the alert dialog
        dialog.show()
    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private fun showDialogForLocationServiceSetting() {
        val builder = android.app.AlertDialog.Builder(this@SearchActivity)
        builder.setTitle("위치 서비스 비활성화")
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n" + "위치 설정을 수정하실래요?")
        builder.setCancelable(true)
        builder.setPositiveButton("설정") { dialog, id ->
            val callGPSSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE)
        }
        builder.setNegativeButton(
            "취소"
        ) { dialog, id -> dialog.cancel() }
        builder.create().show()
    }

    private fun checkLocationServicesStatus(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onRequestPermissionsResult(
        permsRequestCode: Int,
        permissions: Array<String>,
        grandResults: IntArray
    ) {
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.size == REQUIRED_PERMISSIONS.size) {
            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            var checkResult = true
            // 모든 퍼미션을 허용했는지 체크합니다.
            for (result in grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    checkResult = false
                    break
                }
            }
            if (checkResult) {
            }//위치 값을 가져올 수 있음
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        REQUIRED_PERMISSIONS[0]
                    ) || ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        REQUIRED_PERMISSIONS[1]
                    )
                ) {
                    Toast.makeText(
                        this@SearchActivity,
                        "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@SearchActivity,
                        "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun checkRunTimePermission() {
        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this@SearchActivity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            this@SearchActivity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            // 3.  위치 값을 가져올 수 있음
        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@SearchActivity,
                    REQUIRED_PERMISSIONS[0]
                )
            ) {
                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(
                    this@SearchActivity,
                    "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                    Toast.LENGTH_LONG
                ).show()
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(
                    this@SearchActivity, REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(
                    this@SearchActivity, REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            }
        }
    }
}