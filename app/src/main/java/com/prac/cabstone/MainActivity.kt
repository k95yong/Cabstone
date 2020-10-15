package com.prac.cabstone

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.prac.cabstone.flow.FlowFragment
import com.prac.cabstone.home.HomeFragment
import com.prac.cabstone.ocr.OcrFragment
import com.prac.cabstone.schedule.ScheduleChoiceFragment
import com.prac.cabstone.schedule.ScheduleFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var homeFragment: HomeFragment? = null
    var scheduleChoiceFragment: ScheduleChoiceFragment? = null
    var flowFragment: FlowFragment? = null
    var ocrFragment: OcrFragment? = null
    var scheduleFragment: ScheduleFragment? = null


    lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        viewModel.setBottomNavigationView(bn_bottom_navigation_view)
        viewModel.getBottomNavigationView().setOnNavigationItemSelectedListener(
            mOnNavigationItemSelectedListener
        )
        viewModel.getBottomNavigationView().selectedItemId = R.id.bni_home
        viewModel.setTransaction(supportFragmentManager.beginTransaction())
        viewModel.setActivity(this)
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            viewModel.setTransaction(supportFragmentManager.beginTransaction())
            when (menuItem.itemId) {
                R.id.bni_home -> {
                    if (homeFragment != null) {
                        viewModel.setTransaction(supportFragmentManager.beginTransaction())
                        viewModel.showTransaction(homeFragment!!)
                    } else {
                        homeFragment = HomeFragment(viewModel)
                        viewModel.addTransaction(homeFragment!!)
                        viewModel.showTransaction(homeFragment!!)
                    }
                    scheduleChoiceFragment?.let {
                        viewModel.setTransaction(supportFragmentManager.beginTransaction())
                        viewModel.hideTransaction(scheduleChoiceFragment!!)
                    }
                    flowFragment?.let {
                        viewModel.setTransaction(supportFragmentManager.beginTransaction())
                        viewModel.hideTransaction(flowFragment!!)
                    }
                    ocrFragment?.let {
                        viewModel.setTransaction(supportFragmentManager.beginTransaction())
                        viewModel.hideTransaction(ocrFragment!!)
                    }
                    scheduleFragment?.let{
                        viewModel.setTransaction(supportFragmentManager.beginTransaction())
                        viewModel.hideTransaction(scheduleFragment!!)
                    }
                    return@OnNavigationItemSelectedListener true
                }
                R.id.bni_schedule -> {
                    if (scheduleChoiceFragment != null) {
                        viewModel.setTransaction(supportFragmentManager.beginTransaction())
                        viewModel.showTransaction(scheduleChoiceFragment!!)
                    } else {
                        scheduleChoiceFragment = ScheduleChoiceFragment(viewModel)
                        viewModel.addTransaction(scheduleChoiceFragment!!)
                        viewModel.showTransaction(scheduleChoiceFragment!!)
                    }
                    homeFragment?.let {
                        viewModel.setTransaction(supportFragmentManager.beginTransaction())
                        viewModel.hideTransaction(homeFragment!!)
                    }
                    flowFragment?.let {
                        viewModel.setTransaction(supportFragmentManager.beginTransaction())
                        viewModel.hideTransaction(flowFragment!!)
                    }
                    ocrFragment?.let {
                        viewModel.setTransaction(supportFragmentManager.beginTransaction())
                        viewModel.hideTransaction(ocrFragment!!)
                    }
                    scheduleFragment?.let{
                        viewModel.setTransaction(supportFragmentManager.beginTransaction())
                        viewModel.hideTransaction(scheduleFragment!!)
                    }
                    return@OnNavigationItemSelectedListener true
                }
                R.id.bni_flow -> {
                    if (flowFragment != null) {
                        viewModel.setTransaction(supportFragmentManager.beginTransaction())
                        viewModel.showTransaction(flowFragment!!)
                    } else {
                        flowFragment = FlowFragment()
                        viewModel.addTransaction(flowFragment!!)
                        viewModel.showTransaction(flowFragment!!)
                    }
                    scheduleChoiceFragment?.let {
                        viewModel.setTransaction(supportFragmentManager.beginTransaction())
                        viewModel.hideTransaction(scheduleChoiceFragment!!)
                    }
                    homeFragment?.let {
                        viewModel.setTransaction(supportFragmentManager.beginTransaction())
                        viewModel.hideTransaction(homeFragment!!)
                    }
                    ocrFragment?.let {
                        viewModel.setTransaction(supportFragmentManager.beginTransaction())
                        viewModel.hideTransaction(ocrFragment!!)
                    }
                    scheduleFragment?.let{
                        viewModel.setTransaction(supportFragmentManager.beginTransaction())
                        viewModel.hideTransaction(scheduleFragment!!)
                    }
                    return@OnNavigationItemSelectedListener true
                }
                R.id.bni_ocr -> {
                    if (ocrFragment != null) {
                        viewModel.setTransaction(supportFragmentManager.beginTransaction())
                        viewModel.showTransaction(ocrFragment!!)
                    } else {
                        ocrFragment = OcrFragment()
                        viewModel.addTransaction(ocrFragment!!)
                        viewModel.showTransaction(ocrFragment!!)
                    }
                    scheduleChoiceFragment?.let {
                        viewModel.setTransaction(supportFragmentManager.beginTransaction())
                        viewModel.hideTransaction(scheduleChoiceFragment!!)
                    }
                    flowFragment?.let {
                        viewModel.setTransaction(supportFragmentManager.beginTransaction())
                        viewModel.hideTransaction(flowFragment!!)
                    }
                    homeFragment?.let {
                        viewModel.setTransaction(supportFragmentManager.beginTransaction())
                        viewModel.hideTransaction(homeFragment!!)
                    }
                    scheduleFragment?.let{
                        viewModel.setTransaction(supportFragmentManager.beginTransaction())
                        viewModel.hideTransaction(scheduleFragment!!)
                    }
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
}