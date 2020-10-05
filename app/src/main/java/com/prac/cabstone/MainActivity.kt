package com.prac.cabstone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.prac.cabstone.flow.FlowFragment
import com.prac.cabstone.home.HomeFragment
import com.prac.cabstone.ocr.OcrFragment
import com.prac.cabstone.schedule.ScheduleFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var homeFragment: HomeFragment? = null
    var scheduleFragment: ScheduleFragment? = null
    var flowFragment: FlowFragment? = null
    var ocrFragment: OcrFragment? = null
    var transaction = supportFragmentManager.beginTransaction()
    lateinit var viewModel: MainViewModel
    lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        viewModel.setBottomNavigationView(bn_bottom_navigation_view)
        viewModel.getBottomNavigationView().setOnNavigationItemSelectedListener(
            mOnNavigationItemSelectedListener
        )
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            transaction = supportFragmentManager.beginTransaction()
            when (menuItem.itemId) {
                R.id.bni_home -> {
                    if (homeFragment != null) {
                        transaction = supportFragmentManager.beginTransaction()
                        transaction.show(homeFragment!!).commit()
                    } else {
                        homeFragment = HomeFragment(viewModel)
                        transaction.add(R.id.main_frame_layout, homeFragment!!)
                        transaction.show(homeFragment!!).commit()
                    }
                    scheduleFragment?.let {
                        transaction = supportFragmentManager.beginTransaction()
                        transaction.hide(scheduleFragment!!).commit()
                    }
                    flowFragment?.let {
                        transaction = supportFragmentManager.beginTransaction()
                        transaction.hide(flowFragment!!).commit()
                    }
                    ocrFragment?.let {
                        transaction = supportFragmentManager.beginTransaction()
                        transaction.hide(ocrFragment!!).commit()
                    }
                    return@OnNavigationItemSelectedListener true
                }
                R.id.bni_schedule -> {
                    if (scheduleFragment != null) {
                        transaction = supportFragmentManager.beginTransaction()
                        transaction.show(scheduleFragment!!).commit()
                    } else {
                        scheduleFragment = ScheduleFragment(viewModel)
                        transaction.add(R.id.main_frame_layout, scheduleFragment!!)
                        transaction.show(scheduleFragment!!).commit()
                    }
                    homeFragment?.let {
                        transaction = supportFragmentManager.beginTransaction()
                        transaction.hide(homeFragment!!).commit()
                    }
                    flowFragment?.let {
                        transaction = supportFragmentManager.beginTransaction()
                        transaction.hide(flowFragment!!).commit()
                    }
                    ocrFragment?.let {
                        transaction = supportFragmentManager.beginTransaction()
                        transaction.hide(ocrFragment!!).commit()
                    }
                    return@OnNavigationItemSelectedListener true
                }
                R.id.bni_flow -> {
                    if (flowFragment != null) {
                        transaction = supportFragmentManager.beginTransaction()
                        transaction.show(flowFragment!!).commit()
                    } else {
                        flowFragment = FlowFragment()
                        transaction.add(R.id.main_frame_layout, flowFragment!!)
                        transaction.show(flowFragment!!).commit()
                    }
                    scheduleFragment?.let {
                        transaction = supportFragmentManager.beginTransaction()
                        transaction.hide(scheduleFragment!!).commit()
                    }
                    homeFragment?.let {
                        transaction = supportFragmentManager.beginTransaction()
                        transaction.hide(homeFragment!!).commit()
                    }
                    ocrFragment?.let {
                        transaction = supportFragmentManager.beginTransaction()
                        transaction.hide(ocrFragment!!).commit()
                    }
                    return@OnNavigationItemSelectedListener true
                }
                R.id.bni_ocr -> {
                    if (ocrFragment != null) {
                        transaction = supportFragmentManager.beginTransaction()
                        transaction.show(ocrFragment!!).commit()
                    } else {
                        ocrFragment = OcrFragment()
                        transaction.add(R.id.main_frame_layout, ocrFragment!!)
                        transaction.show(ocrFragment!!).commit()
                    }
                    scheduleFragment?.let {
                        transaction = supportFragmentManager.beginTransaction()
                        transaction.hide(scheduleFragment!!).commit()
                    }
                    flowFragment?.let {
                        transaction = supportFragmentManager.beginTransaction()
                        transaction.hide(flowFragment!!).commit()
                    }
                    homeFragment?.let {
                        transaction = supportFragmentManager.beginTransaction()
                        transaction.hide(homeFragment!!).commit()
                    }
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
}