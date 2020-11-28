package com.prac.cabstone

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.prac.cabstone.home.HomeFragment
import com.prac.cabstone.ocr.OcrFragment
import com.prac.cabstone.schedule.ScheduleChoiceFragment
import com.prac.cabstone.schedule.ScheduleFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private val GPS_ENABLE_REQUEST_CODE = 2001
    private val PERMISSIONS_REQUEST_CODE = 100


    var homeFragment: HomeFragment? = null
    var scheduleChoiceFragment: ScheduleChoiceFragment? = null
    var ocrFragment: OcrFragment? = null
    var scheduleFragment: ScheduleFragment? = null
    var cur_frag = 1

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        viewModel.setBottomNavigationView(bn_bottom_navigation_view)
        viewModel.getBottomNavigationView().setOnNavigationItemSelectedListener(
            mOnNavigationItemSelectedListener
        )

        viewModel.setTransaction(supportFragmentManager.beginTransaction())
        viewModel.setActivity(this)
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting()
        } else {
            checkRunTimePermission()
        }
    }


    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            viewModel.setTransaction(supportFragmentManager.beginTransaction())
            when (menuItem.itemId) {
                R.id.bni_home -> {
                    if (homeFragment != null) {
                        viewModel.setTransaction(supportFragmentManager.beginTransaction())
                        viewModel.showTransaction(homeFragment!!)
                        cur_frag = 1
                    } else {
                        homeFragment = HomeFragment.newInstance()
                        viewModel.addTransaction(homeFragment!!)
                        viewModel.showTransaction(homeFragment!!)
                        cur_frag = 1
                    }
                    scheduleChoiceFragment?.let {
                        viewModel.setTransaction(supportFragmentManager.beginTransaction())
                        viewModel.hideTransaction(scheduleChoiceFragment!!)
                    }
                    ocrFragment?.let {
                        viewModel.setTransaction(supportFragmentManager.beginTransaction())
                        viewModel.hideTransaction(ocrFragment!!)
                    }
                    scheduleFragment?.let {
                        viewModel.setTransaction(supportFragmentManager.beginTransaction())
                        viewModel.hideTransaction(scheduleFragment!!)
                    }
                    return@OnNavigationItemSelectedListener true
                }
                R.id.bni_schedule -> {
                    if (scheduleChoiceFragment != null) {
                        viewModel.setTransaction(supportFragmentManager.beginTransaction())
                        viewModel.showTransaction(scheduleChoiceFragment!!)
                        cur_frag = 2
                    } else {
                        scheduleChoiceFragment = ScheduleChoiceFragment(viewModel)
                        viewModel.addTransaction(scheduleChoiceFragment!!)
                        viewModel.showTransaction(scheduleChoiceFragment!!)
                        cur_frag = 2
                    }
                    homeFragment?.let {
                        viewModel.setTransaction(supportFragmentManager.beginTransaction())
                        viewModel.hideTransaction(homeFragment!!)
                    }
                    ocrFragment?.let {
                        viewModel.setTransaction(supportFragmentManager.beginTransaction())
                        viewModel.hideTransaction(ocrFragment!!)
                    }
                    scheduleFragment?.let {
                        viewModel.setTransaction(supportFragmentManager.beginTransaction())
                        viewModel.hideTransaction(scheduleFragment!!)
                    }
                    return@OnNavigationItemSelectedListener true
                }
                R.id.bni_ocr -> {
                    if (ocrFragment != null) {
                        viewModel.setTransaction(supportFragmentManager.beginTransaction())
                        viewModel.showTransaction(ocrFragment!!)
                        cur_frag = 4
                    } else {
                        ocrFragment = OcrFragment()
                        viewModel.addTransaction(ocrFragment!!)
                        viewModel.showTransaction(ocrFragment!!)
                        cur_frag = 4
                    }
                    scheduleChoiceFragment?.let {
                        viewModel.setTransaction(supportFragmentManager.beginTransaction())
                        viewModel.hideTransaction(scheduleChoiceFragment!!)
                    }
                    homeFragment?.let {
                        viewModel.setTransaction(supportFragmentManager.beginTransaction())
                        viewModel.hideTransaction(homeFragment!!)
                    }
                    scheduleFragment?.let {
                        viewModel.setTransaction(supportFragmentManager.beginTransaction())
                        viewModel.hideTransaction(scheduleFragment!!)
                    }
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onBackPressed() {
        Log.e("cur_frag", cur_frag.toString())
        if(cur_frag == 5){
            viewModel.getBottomNavigationView().selectedItemId = R.id.bni_schedule
            cur_frag = 2
            return
        }
        super.onBackPressed()
    }

    private fun showDialogForLocationServiceSetting() {
        val builder = android.app.AlertDialog.Builder(this@MainActivity)
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
                viewModel.getBottomNavigationView().selectedItemId = R.id.bni_home
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
                        this@MainActivity,
                        "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@MainActivity,
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
            this@MainActivity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            this@MainActivity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            // 3.  위치 값을 가져올 수 있음
            viewModel.getBottomNavigationView().selectedItemId = R.id.bni_home
        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MainActivity,
                    REQUIRED_PERMISSIONS[0]
                )
            ) {
                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(
                    this@MainActivity,
                    "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                    Toast.LENGTH_LONG
                ).show()
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(
                    this@MainActivity, REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(
                    this@MainActivity, REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            }
        }
    }
}