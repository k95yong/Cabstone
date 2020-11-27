package com.prac.cabstone.flow

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.prac.cabstone.MainViewModel
import com.prac.cabstone.R
import com.prac.cabstone.schedule.DragManageAdapter
import com.prac.cabstone.schedule.GridAdapter
import com.softsquared.myapplication.db.Groups
import kotlinx.android.synthetic.main.custom_dialog.view.*
import kotlinx.android.synthetic.main.fragment_schedule_choice.*
import kotlinx.android.synthetic.main.schedule_add_dialog.view.*

class FlowFragment : Fragment {
    private lateinit var gridAdapter: GridAdapter
    private val viewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView : View = inflater.inflate(R.layout.fragment_flow, container, false);
        return rootView
    }
    constructor(viewModel: MainViewModel){
        this.viewModel = viewModel
    }
    override fun onResume() {
        super.onResume()
        gridAdapter.list = viewModel.getAllGList() as ArrayList<Groups>
        gridAdapter.notifyDataSetChanged()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gridAdapter = GridAdapter(viewModel)

        val manager = GridLayoutManager(context, 4)
        val callback = DragManageAdapter(
            gridAdapter, activity!!,
            ItemTouchHelper.UP.or(ItemTouchHelper.DOWN).or(ItemTouchHelper.LEFT)
                .or(ItemTouchHelper.RIGHT), -1
        )
        val helper = ItemTouchHelper(callback)
        schedule_list.layoutManager = manager
        schedule_list.adapter = gridAdapter
        helper.attachToRecyclerView(schedule_list)

    }
}