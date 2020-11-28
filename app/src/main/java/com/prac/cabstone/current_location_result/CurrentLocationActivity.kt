package com.prac.cabstone.current_location_result

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.prac.cabstone.ApplicationClass
import com.prac.cabstone.BaseActivity
import com.prac.cabstone.R
import com.prac.cabstone.models.*
import com.prac.cabstone.search_result.GetInfoForAreaCodeAPI
import com.prac.cabstone.search_result.SearchResultBottomSheet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

class CurrentLocationActivity : BaseActivity(), OnMapReadyCallback {

    var mMarkerList: ArrayList<ResponseGetResultForCurrentData> = ArrayList()
    private var activeMarkers: Vector<Marker>? = null
    lateinit var mNaverMap : NaverMap
    private var mLanguage: String? = "ko"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_location)

        mLanguage = ApplicationClass.prefs.myLANGUAGE

        getResult()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.current_location_fg_mapView) as MapFragment?
            ?: MapFragment.newInstance().also {
                supportFragmentManager.beginTransaction().add(R.id.current_location_fg_mapView, it).commit()
            }
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(naverMap: NaverMap) {
        mNaverMap = naverMap
        // 카메라 초기 위치 설정
        // 정의된 마커위치들중 가시거리 내에있는것들만 마커 생성
        var initialPosition = LatLng(intent.getDoubleExtra("lat", 0.0), intent.getDoubleExtra("lon", 0.0))
        val cameraUpdate = CameraUpdate.scrollTo(initialPosition)
        naverMap.moveCamera(cameraUpdate)

        // 카메라 이동 되면 호출 되는 이벤트
        naverMap.addOnCameraChangeListener { reason, animated ->
            freeActiveMarkers()
            // 정의된 마커위치들중 가시거리 내에있는것들만 마커 생성
            val currentPosition: LatLng = getCurrentPosition(naverMap)
            for (markerPosition in mMarkerList) {
                val latLng = LatLng(markerPosition.getMapY(), markerPosition.getMapX())
                if (!withinSightMarker(currentPosition, latLng)) continue
                val marker = Marker().apply {
                    setOnClickListener {
                        var resultForCurrentBottomSheet = ResultForCurrentBottomSheet(this@CurrentLocationActivity, markerPosition)
                        resultForCurrentBottomSheet.show(supportFragmentManager,resultForCurrentBottomSheet.tag)
                        true
                    }
                }
                marker.position = latLng!!
                marker.map = naverMap
                marker.icon = OverlayImage.fromResource(R.drawable.ic_location_pin)
                activeMarkers?.add(marker)
            }
        }
    }

    private fun getResult() {
        showProgressDialog()

        val api = CurrentLocationAPI.create()
        // 정의된 마커위치들중 가시거리 내에있는것들만 마커 생성
        when (mLanguage) {
            "ko" -> {
                api.getResultForCurrent(intent.getDoubleExtra("lon", 0.0), intent.getDoubleExtra("lat", 0.0), 10000).enqueue(object :
                    Callback<ResponseGetResultForCurrent> {
                    override fun onResponse(call: Call<ResponseGetResultForCurrent>, response: Response<ResponseGetResultForCurrent>) {
                        hideProgressDialog()
                        var responseGetInfoForArea = response.body()
                        if (responseGetInfoForArea != null) {
                            for (i in responseGetInfoForArea.getData()) {
                                mMarkerList.add(i)

                            }
                            showCustomToast("검색완료")

                            freeActiveMarkers()
                            // 정의된 마커위치들중 가시거리 내에있는것들만 마커 생성
                            val currentPosition: LatLng = getCurrentPosition(mNaverMap)
                            for (markerPosition in mMarkerList) {
                                val latLng = LatLng(markerPosition.getMapY(), markerPosition.getMapX())
                                if (!withinSightMarker(currentPosition, latLng)) continue
                                val marker = Marker().apply {
                                    setOnClickListener {
                                        var searchResultBottomSheet = ResultForCurrentBottomSheet(this@CurrentLocationActivity, markerPosition)
                                        searchResultBottomSheet.show(supportFragmentManager,searchResultBottomSheet.tag)
                                        true
                                    }
                                }
                                marker.position = latLng!!
                                marker.map = mNaverMap
                                activeMarkers?.add(marker)
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseGetResultForCurrent>, t: Throwable) {
                        hideProgressDialog()
                        showCustomToast(resources.getString(R.string.network_error))
                        t.printStackTrace()
                    }
                })
            }
            "en" -> {
                api.getResultForCurrent_en(intent.getDoubleExtra("lon", 0.0), intent.getDoubleExtra("lat", 0.0), 10000).enqueue(object :
                    Callback<ResponseGetResultForCurrent> {
                    override fun onResponse(call: Call<ResponseGetResultForCurrent>, response: Response<ResponseGetResultForCurrent>) {
                        hideProgressDialog()
                        var responseGetInfoForArea = response.body()
                        if (responseGetInfoForArea != null) {
                            for (i in responseGetInfoForArea.getData()) {
                                mMarkerList.add(i)

                            }
                            showCustomToast("검색완료")

                            freeActiveMarkers()
                            // 정의된 마커위치들중 가시거리 내에있는것들만 마커 생성
                            val currentPosition: LatLng = getCurrentPosition(mNaverMap)
                            for (markerPosition in mMarkerList) {
                                val latLng = LatLng(markerPosition.getMapY(), markerPosition.getMapX())
                                if (!withinSightMarker(currentPosition, latLng)) continue
                                val marker = Marker().apply {
                                    setOnClickListener {
                                        var searchResultBottomSheet = ResultForCurrentBottomSheet(this@CurrentLocationActivity, markerPosition)
                                        searchResultBottomSheet.show(supportFragmentManager,searchResultBottomSheet.tag)
                                        true
                                    }
                                }
                                marker.position = latLng!!
                                marker.map = mNaverMap
                                activeMarkers?.add(marker)
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseGetResultForCurrent>, t: Throwable) {
                        hideProgressDialog()
                        showCustomToast(resources.getString(R.string.network_error))
                        t.printStackTrace()
                    }
                })
            }
            "jp" -> {
                api.getResultForCurrent_jp(intent.getDoubleExtra("lon", 0.0), intent.getDoubleExtra("lat", 0.0), 10000).enqueue(object :
                    Callback<ResponseGetResultForCurrent> {
                    override fun onResponse(call: Call<ResponseGetResultForCurrent>, response: Response<ResponseGetResultForCurrent>) {
                        hideProgressDialog()
                        var responseGetInfoForArea = response.body()
                        if (responseGetInfoForArea != null) {
                            for (i in responseGetInfoForArea.getData()) {
                                mMarkerList.add(i)

                            }
                            showCustomToast("검색완료")

                            freeActiveMarkers()
                            // 정의된 마커위치들중 가시거리 내에있는것들만 마커 생성
                            val currentPosition: LatLng = getCurrentPosition(mNaverMap)
                            for (markerPosition in mMarkerList) {
                                val latLng = LatLng(markerPosition.getMapY(), markerPosition.getMapX())
                                if (!withinSightMarker(currentPosition, latLng)) continue
                                val marker = Marker().apply {
                                    setOnClickListener {
                                        var searchResultBottomSheet = ResultForCurrentBottomSheet(this@CurrentLocationActivity, markerPosition)
                                        searchResultBottomSheet.show(supportFragmentManager,searchResultBottomSheet.tag)
                                        true
                                    }
                                }
                                marker.position = latLng!!
                                marker.map = mNaverMap
                                activeMarkers?.add(marker)
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseGetResultForCurrent>, t: Throwable) {
                        hideProgressDialog()
                        showCustomToast(resources.getString(R.string.network_error))
                        t.printStackTrace()
                    }
                })
            }
            "ch" -> {
                api.getResultForCurrent_ch(intent.getDoubleExtra("lon", 0.0), intent.getDoubleExtra("lat", 0.0), 10000).enqueue(object :
                    Callback<ResponseGetResultForCurrent> {
                    override fun onResponse(call: Call<ResponseGetResultForCurrent>, response: Response<ResponseGetResultForCurrent>) {
                        hideProgressDialog()
                        var responseGetInfoForArea = response.body()
                        if (responseGetInfoForArea != null) {
                            for (i in responseGetInfoForArea.getData()) {
                                mMarkerList.add(i)

                            }
                            showCustomToast("검색완료")

                            freeActiveMarkers()
                            // 정의된 마커위치들중 가시거리 내에있는것들만 마커 생성
                            val currentPosition: LatLng = getCurrentPosition(mNaverMap)
                            for (markerPosition in mMarkerList) {
                                val latLng = LatLng(markerPosition.getMapY(), markerPosition.getMapX())
                                if (!withinSightMarker(currentPosition, latLng)) continue
                                val marker = Marker().apply {
                                    setOnClickListener {
                                        var searchResultBottomSheet = ResultForCurrentBottomSheet(this@CurrentLocationActivity, markerPosition)
                                        searchResultBottomSheet.show(supportFragmentManager,searchResultBottomSheet.tag)
                                        true
                                    }
                                }
                                marker.position = latLng!!
                                marker.map = mNaverMap
                                activeMarkers?.add(marker)
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseGetResultForCurrent>, t: Throwable) {
                        hideProgressDialog()
                        showCustomToast(resources.getString(R.string.network_error))
                        t.printStackTrace()
                    }
                })
            }
            "ge" -> {
                api.getResultForCurrent_ge(intent.getDoubleExtra("lon", 0.0), intent.getDoubleExtra("lat", 0.0), 10000).enqueue(object :
                    Callback<ResponseGetResultForCurrent> {
                    override fun onResponse(call: Call<ResponseGetResultForCurrent>, response: Response<ResponseGetResultForCurrent>) {
                        hideProgressDialog()
                        var responseGetInfoForArea = response.body()
                        if (responseGetInfoForArea != null) {
                            for (i in responseGetInfoForArea.getData()) {
                                mMarkerList.add(i)

                            }
                            showCustomToast("검색완료")

                            freeActiveMarkers()
                            // 정의된 마커위치들중 가시거리 내에있는것들만 마커 생성
                            val currentPosition: LatLng = getCurrentPosition(mNaverMap)
                            for (markerPosition in mMarkerList) {
                                val latLng = LatLng(markerPosition.getMapY(), markerPosition.getMapX())
                                if (!withinSightMarker(currentPosition, latLng)) continue
                                val marker = Marker().apply {
                                    setOnClickListener {
                                        var searchResultBottomSheet = ResultForCurrentBottomSheet(this@CurrentLocationActivity, markerPosition)
                                        searchResultBottomSheet.show(supportFragmentManager,searchResultBottomSheet.tag)
                                        true
                                    }
                                }
                                marker.position = latLng!!
                                marker.map = mNaverMap
                                activeMarkers?.add(marker)
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseGetResultForCurrent>, t: Throwable) {
                        hideProgressDialog()
                        showCustomToast(resources.getString(R.string.network_error))
                        t.printStackTrace()
                    }
                })
            }
            "sp" -> {
                api.getResultForCurrent_sp(intent.getDoubleExtra("lon", 0.0), intent.getDoubleExtra("lat", 0.0), 10000).enqueue(object :
                    Callback<ResponseGetResultForCurrent> {
                    override fun onResponse(call: Call<ResponseGetResultForCurrent>, response: Response<ResponseGetResultForCurrent>) {
                        hideProgressDialog()
                        var responseGetInfoForArea = response.body()
                        if (responseGetInfoForArea != null) {
                            for (i in responseGetInfoForArea.getData()) {
                                mMarkerList.add(i)

                            }
                            showCustomToast("검색완료")

                            freeActiveMarkers()
                            // 정의된 마커위치들중 가시거리 내에있는것들만 마커 생성
                            val currentPosition: LatLng = getCurrentPosition(mNaverMap)
                            for (markerPosition in mMarkerList) {
                                val latLng = LatLng(markerPosition.getMapY(), markerPosition.getMapX())
                                if (!withinSightMarker(currentPosition, latLng)) continue
                                val marker = Marker().apply {
                                    setOnClickListener {
                                        var searchResultBottomSheet = ResultForCurrentBottomSheet(this@CurrentLocationActivity, markerPosition)
                                        searchResultBottomSheet.show(supportFragmentManager,searchResultBottomSheet.tag)
                                        true
                                    }
                                }
                                marker.position = latLng!!
                                marker.map = mNaverMap
                                activeMarkers?.add(marker)
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseGetResultForCurrent>, t: Throwable) {
                        hideProgressDialog()
                        showCustomToast(resources.getString(R.string.network_error))
                        t.printStackTrace()
                    }
                })
            }
        }
    }

    // 현재 카메라가 보고있는 위치
    private fun getCurrentPosition(naverMap: NaverMap): LatLng {
        val cameraPosition = naverMap.cameraPosition
        return LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude)
    }

    // 선택한 마커의 위치가 가시거리(카메라가 보고있는 위치 반경 3km 내)에 있는지 확인
    private val REFERANCE_LAT_X3 = 3 / 109.958489129649955
    private val REFERANCE_LNG_X3 = 3 / 88.74
    private fun withinSightMarker(currentPosition: LatLng, markerPosition: LatLng): Boolean {
        val withinSightMarkerLat =
            abs(currentPosition.latitude - markerPosition.latitude) <= REFERANCE_LAT_X3
        val withinSightMarkerLng =
            abs(currentPosition.longitude - markerPosition.longitude) <= REFERANCE_LNG_X3
        return withinSightMarkerLat && withinSightMarkerLng
    }

    // 지도상에 표시되고있는 마커들 지도에서 삭제
    private fun freeActiveMarkers() {
        if (activeMarkers == null) {
            activeMarkers = Vector<Marker>()
            return
        }
        for (activeMarker in activeMarkers!!) {
            activeMarker.map = null
        }
        activeMarkers = Vector<Marker>()
    }
}