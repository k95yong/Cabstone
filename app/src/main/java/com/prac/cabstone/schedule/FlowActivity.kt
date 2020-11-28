package com.prac.cabstone.schedule

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import com.prac.cabstone.BaseActivity
import com.prac.cabstone.MainViewModel
import com.prac.cabstone.R
import com.prac.cabstone.models.ResponseGetInfoForAreaData
import com.prac.cabstone.search_result.SearchResultBottomSheet
import com.softsquared.myapplication.db.Todo
import kotlinx.android.synthetic.main.activity_flow.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

class FlowActivity : BaseActivity(), OnMapReadyCallback {
    lateinit var date: String
    lateinit var schedule_name: String
    lateinit var viewModel: MainViewModel
    var list = ArrayList<Todo>()
    private var activeMarkers: Vector<Marker>? = null
    var mMarkerList: ArrayList<Todo> = ArrayList()
    lateinit var mNaverMap : NaverMap
    var pathList = mutableListOf<LatLng>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow)
        date = intent.getStringExtra("date")
        schedule_name = intent.getStringExtra("schedule_name")
        viewModel = ViewModelProviders.of(this)[MainViewModel::class.java]

        val mapFragment = supportFragmentManager.findFragmentById(R.id.schedule_fg_mapView) as MapFragment?
            ?: MapFragment.newInstance().also {
                supportFragmentManager.beginTransaction().add(R.id.schedule_fg_mapView, it).commit()
            }
        mapFragment.getMapAsync(this)
    }

    private fun todoToResponse(todo: Todo): ResponseGetInfoForAreaData{
        return ResponseGetInfoForAreaData(todo.addr1, todo.addr1, 0, todo.contentId,
        todo.firstImage, todo.firstImage, todo.mapX, todo.mapY, 0, 0, todo.contents)
    }

    override fun onMapReady(naverMap: NaverMap) {
        mNaverMap = naverMap
        list = ArrayList(viewModel.getDayList(date, schedule_name))

        //val currentPosition: LatLng = getCurrentPosition(naverMap)


        for(i in list){
            if(i.mapX < 1.0 || i.mapY < 1.0)
                continue
            val latLng = LatLng(i.mapY, i.mapX)
            pathList.add(latLng)

            //Log.e("pathList", i.toString())
            val marker = Marker().apply {

                setOnClickListener {

                    var searchResultBottomSheet = SearchResultBottomSheet(this@FlowActivity, todoToResponse(i))
                    searchResultBottomSheet.show(supportFragmentManager,searchResultBottomSheet.tag)
                    true
                }

            }

            marker.position = latLng!!
            marker.map = mNaverMap
            activeMarkers?.add(marker)
        }
        pathList.reverse()
        val path = PathOverlay()
        path.coords = pathList
        path.map = mNaverMap
        path.color = Color.GREEN
        path.passedColor = Color.GRAY
        path.progress = 0.5
    }

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
    private fun getCurrentPosition(naverMap: NaverMap): LatLng {
        val cameraPosition = naverMap.cameraPosition
        return LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude)
    }

    private val REFERANCE_LAT_X3 = 3 / 109.958489129649955
    private val REFERANCE_LNG_X3 = 3 / 88.74
    private fun withinSightMarker(currentPosition: LatLng, markerPosition: LatLng): Boolean {
        val withinSightMarkerLat =
            abs(currentPosition.latitude - markerPosition.latitude) <= REFERANCE_LAT_X3
        val withinSightMarkerLng =
            abs(currentPosition.longitude - markerPosition.longitude) <= REFERANCE_LNG_X3
        return withinSightMarkerLat && withinSightMarkerLng
    }
}