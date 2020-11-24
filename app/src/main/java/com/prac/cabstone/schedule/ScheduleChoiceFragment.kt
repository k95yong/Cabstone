package com.prac.cabstone.schedule

import android.app.Activity
import android.content.Intent
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
import com.softsquared.myapplication.db.Groups
import kotlinx.android.synthetic.main.custom_dialog.view.et_dialog_contents
import kotlinx.android.synthetic.main.fragment_schedule_choice.*
import kotlinx.android.synthetic.main.schedule_add_dialog.view.*


class ScheduleChoiceFragment : Fragment {
    private val viewModel: MainViewModel
    lateinit var dialogView: View
    private lateinit var gridAdapter: GridAdapter
    constructor(viewModel: MainViewModel){
        this.viewModel = viewModel
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule_choice, container, false)
    }

    override fun onResume() {
        super.onResume()
        gridAdapter.list = viewModel.getAllGList() as ArrayList<Groups>
        Log.e("list on resume: ", gridAdapter.list.toString())
        gridAdapter.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gridAdapter = GridAdapter(viewModel)

        val manager = GridLayoutManager(context, 4)
        //val manager = LinearLayoutManager(context)
        val callback = DragManageAdapter(
            gridAdapter, activity!!,
            ItemTouchHelper.UP.or(ItemTouchHelper.DOWN).or(ItemTouchHelper.LEFT)
                .or(ItemTouchHelper.RIGHT), -1
        )
        val helper = ItemTouchHelper(callback)
        schedule_list.layoutManager = manager
        schedule_list.adapter = gridAdapter
        helper.attachToRecyclerView(schedule_list)

        btn_add_schedule.setOnClickListener {
            dialogView = layoutInflater.inflate(R.layout.schedule_add_dialog, null)
            val builder = AlertDialog.Builder(activity!!)
            builder.setView(dialogView)
                .setPositiveButton("확인") { dialogInterface, i ->
                if (dialogView.et_dialog_contents.text.isEmpty()) {
                    Toast.makeText(context, "일정의 이름을 입력해 주세요.", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.insert(
                        Groups(
                            dialogView.et_dialog_contents.text.toString(),
                            viewModel.getGroupIdx() + 1,
                            dialogView.et_dialog_sel_image.text.toString()
                        )
                    )
                    gridAdapter.list = viewModel.getAllGList() as ArrayList<Groups>
                    gridAdapter.notifyDataSetChanged()
                }
            }
                .setNegativeButton("취소") { dialogInterface, i ->
                }
                .show()
            dialogView.et_dialog_sel_image.setOnClickListener {
                //val intent = Intent(activity, ChoicePicActivity::class.java)
                //startActivityForResult(intent, 100)
                openGalleryForImage()
            }
        }
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 100){
            dialogView.et_dialog_sel_image.setText(data?.data.toString())
        }
    }
}