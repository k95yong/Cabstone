package com.prac.cabstone.schedule

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.prac.cabstone.MainViewModel
import com.prac.cabstone.R
import com.softsquared.myapplication.db.Groups
import kotlinx.android.synthetic.main.fragment_schedule_choice.*


class ScheduleChoiceDialogActivity : AppCompatActivity {
    var viewModel: MainViewModel?
    lateinit var dialogView: View
    private lateinit var gridAdapter: GridAdapter
    constructor(){
        viewModel = null
    }
    constructor(viewModel: MainViewModel){
        this.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_schedule_choice)
        if(viewModel == null){
            viewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        }

        gridAdapter = GridAdapter(this)
        gridAdapter.list = viewModel!!.getAllGList() as ArrayList<Groups>
        btn_add_schedule.visibility = View.INVISIBLE
        val manager = GridLayoutManager(this, 4)
        schedule_list.layoutManager = manager
        schedule_list.adapter = gridAdapter
    }
}