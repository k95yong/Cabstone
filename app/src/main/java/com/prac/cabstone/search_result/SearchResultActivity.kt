package com.prac.cabstone.search_result

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.prac.cabstone.BaseActivity
import com.prac.cabstone.MainViewModel
import com.prac.cabstone.R
import com.prac.cabstone.models.ResponseGetInfoForArea
import com.prac.cabstone.models.ResponseGetInfoForAreaData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs


class SearchResultActivity : BaseActivity(), OnMapReadyCallback {


    var mMarkerList: ArrayList<ResponseGetInfoForAreaData> = ArrayList()
    // 마커 정보 저장시킬 변수들 선언
    private var activeMarkers: Vector<Marker>? = null
    lateinit var mNaverMap : NaverMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        getResult()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.search_result_fg_mapView) as MapFragment?
            ?: MapFragment.newInstance().also {
                supportFragmentManager.beginTransaction().add(R.id.search_result_fg_mapView, it).commit()
            }
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(naverMap: NaverMap) {
        mNaverMap = naverMap
        // 카메라 초기 위치 설정
        // 정의된 마커위치들중 가시거리 내에있는것들만 마커 생성
        var initialPosition = LatLng(0.0, 0.0)
        var areaCode = intent.getIntExtra("area_code", 0)
        when {
            areaCode == 1 -> {
                //37.552819, 126.988241
                initialPosition = LatLng(37.552819, 126.988241)
            }
            areaCode == 2 -> {
                //37.455536, 126.706277
                initialPosition = LatLng(37.455536, 126.706277)
            }
            areaCode == 3 -> {
                //36.339900, 127.393261
                initialPosition = LatLng(36.339900, 127.393261)
            }
            areaCode == 4 -> {
                // 대구 35.829642, 128.562235
                initialPosition = LatLng(35.829642, 128.562235)
            }
            areaCode == 5 -> {
                // 광주 35.156192, 126.833842
                initialPosition = LatLng(35.156192, 126.833842)
            }
            areaCode == 6 -> {
                // 부산 35.160500, 129.047422
                initialPosition = LatLng(35.160500, 129.047422)
            }
            areaCode == 7 -> {
                // 울산 35.546109, 129.255024
                initialPosition = LatLng(35.546109, 129.255024)
            }
            areaCode == 8 -> {
                //세종특별자치시 36.564578, 127.263634
                initialPosition = LatLng(36.564578, 127.263634)
            }
            areaCode == 31 -> {
                //경기도 37.389971, 127.145224
                initialPosition = LatLng(37.389971, 127.145224)
            }
            areaCode == 32 -> {
                //강원도 37.832659, 128.148760
                initialPosition = LatLng(37.832659, 128.148760)
            }
            areaCode == 33 -> {
                //충청북도 36.992307, 127.716837
                initialPosition = LatLng(36.992307, 127.716837)
            }
            areaCode == 34 -> {
                //충청남도 36.721187, 126.807438
                initialPosition = LatLng(36.721187, 126.807438)
            }
            areaCode == 35 -> {
                //경상북도 36.289541, 128.897174
                initialPosition = LatLng(36.289541, 128.897174)
            }
            areaCode == 36 -> {
                //경상남도 35.442491, 128.193167
                initialPosition = LatLng( 35.442491, 128.193167)
            }
            areaCode == 37 -> {
                //전라북도 35.728514, 127.141382
                initialPosition = LatLng(35.728514, 127.141382)
            }
            areaCode == 38 -> {
                //전라남도 34.893334, 126.975797
                initialPosition = LatLng(34.893334, 126.975797)
            }
            areaCode == 39 -> {
                //제주도 33.353372, 126.566672
                initialPosition = LatLng(33.353372, 126.566672)
            }

            // 카메라 이동 되면 호출 되는 이벤트
        }
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
                        var searchResultBottomSheet = SearchResultBottomSheet(this@SearchResultActivity, markerPosition)
                        searchResultBottomSheet.show(supportFragmentManager,searchResultBottomSheet.tag)
                        true
                    }
                }
                marker.icon = OverlayImage.fromResource(R.drawable.ic_location_pin)
                marker.position = latLng!!
                marker.map = naverMap
                activeMarkers?.add(marker)
            }
        }
    }

    private fun getResult() {
        showProgressDialog()
        val api = GetInfoForAreaCodeAPI.create()

        api.getResultForAreaCode(intent.getIntExtra("area_code", 0)).enqueue(object : Callback<ResponseGetInfoForArea> {
            override fun onResponse(call: Call<ResponseGetInfoForArea>, response: Response<ResponseGetInfoForArea>) {
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
                                var searchResultBottomSheet = SearchResultBottomSheet(this@SearchResultActivity, markerPosition)
                                searchResultBottomSheet.show(supportFragmentManager,searchResultBottomSheet.tag)
                                true
                            }
                        }
                        marker.icon = OverlayImage.fromResource(R.drawable.ic_location_pin)
                        marker.position = latLng!!
                        marker.map = mNaverMap
                        activeMarkers?.add(marker)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseGetInfoForArea>, t: Throwable) {
                hideProgressDialog()
                showCustomToast(resources.getString(R.string.network_error))
                t.printStackTrace()
            }
        })
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