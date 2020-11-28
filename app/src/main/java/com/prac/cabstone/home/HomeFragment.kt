package com.prac.cabstone.home

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.prac.cabstone.ApplicationClass
import com.prac.cabstone.GpsTracker
import com.prac.cabstone.MainViewModel
import com.prac.cabstone.R
import com.prac.cabstone.models.ResponseGetAreaCode
import com.prac.cabstone.search.SearchActivity
import com.prac.cabstone.search_result.SearchResultActivity
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment, OnMapReadyCallback {
    companion object{
        fun newInstance():HomeFragment{
            val args: Bundle = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }
    lateinit var mNaverMap : NaverMap
    lateinit var mLocationSource: FusedLocationSource
    constructor() {
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView : View = inflater.inflate(R.layout.fragment_home, container, false);
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        home_fg_cl_search.setOnClickListener {
            val searchActivity = SearchActivity()
            var intent = Intent(context, searchActivity::class.java)
            startActivity(intent)
        }
        home_fg_cl_language.setOnClickListener {
            showLanguageDialog()
        }

        val mapFragment = activity!!.supportFragmentManager.findFragmentById(R.id.home_fg_cl_search) as MapFragment?
            ?: MapFragment.newInstance().also {
                activity!!.supportFragmentManager.beginTransaction().add(R.id.schedule_fg_mapView, it).commit()
            }
        mapFragment.getMapAsync(this)
    }

    private fun showLanguageDialog(){
        var list : ArrayList<String> = ArrayList()
        var code : ArrayList<String> = ArrayList()

        list.add("한국어")
        code.add("ko")

        list.add("English")
        code.add("en")

        list.add("日本語")
        code.add("jp")

        list.add("汉语")
        code.add("ch")

        list.add("Deutsch")
        code.add("ge")

        list.add("lengua española")
        code.add("sp")

        // Initialize a new instance of alert dialog builder object
        val builder = context?.let { AlertDialog.Builder(it) }

        // Set a title for alert dialog
        builder!!.setTitle("언어를 선택해주세요")

        // Set items form alert dialog
        builder.setItems(list.toTypedArray()) { _, which ->
            // Get the dialog selected item
            val selectedCode = code[which]

            ApplicationClass.prefs.myLANGUAGE = selectedCode
        }

        // Create a new AlertDialog using builder object
        val dialog = builder.create()

        // Finally, display the alert dialog
        dialog.show()
    }

    override fun onMapReady(naverMap: NaverMap) {
        Log.e("onMapReady", "map is ready")
        mNaverMap = naverMap
        val gpsTracker = GpsTracker(activity!!)
        var lat = gpsTracker.getLatitude()
        var lon = gpsTracker.getLongitude()
        if (lat == 0.0 && lon == 0.0) {
            lat = 37.552302
            lon = 126.992189
        }
        var initialPosition = LatLng(lat, lon)
        val cameraUpdate = CameraUpdate.scrollTo(initialPosition)
        val marker = Marker()
        marker.position = initialPosition
        marker.map = mNaverMap
        marker.icon = OverlayImage.fromResource(R.drawable.ic_location_pin_cur)
        mNaverMap.moveCamera(cameraUpdate)
    }

}