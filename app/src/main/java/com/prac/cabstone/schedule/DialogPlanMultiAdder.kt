package com.prac.cabstone.schedule

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.EditText
import androidx.recyclerview.widget.GridLayoutManager
import com.prac.cabstone.BaseCalendar
import com.prac.cabstone.R
import kotlinx.android.synthetic.main.dialog_calendar.*
import java.text.SimpleDateFormat
import java.util.*

class DialogPlanMultiAdder(context: Context) {
    private val dlg = Dialog(context)
    lateinit var scheduleRecyclerViewAdapter: DialogRecyclerAdapter
    val context = context
    var set_data = mutableSetOf<String>()

    fun start(arr: ArrayList<String>, et_dialog_sel_date: EditText) {
        dlg.let{
            it.requestWindowFeature(Window.FEATURE_NO_TITLE)
            it.setContentView(R.layout.dialog_calendar)
            it.setCancelable(true)
        }
        initView()
        dlg.run{
            btn_dialog_calendar_ok.setOnClickListener {
                for (value in set_data) {
                    arr.add(value)
                }
                dismiss()
                if (arr.size > 0) {
                    et_dialog_sel_date.setText("${arr[0] + " 포함 '" + arr.size + "'건"}")
                }
            }
            btn_dialog_calendar_cancle.setOnClickListener {
                dismiss()
            }
            show()
        }
    }

    fun initView() {
        scheduleRecyclerViewAdapter = DialogRecyclerAdapter(this, set_data)
        dlg.run{
            rv_dialog_calendar.layoutManager = GridLayoutManager(context, BaseCalendar.DAYS_OF_WEEK)
            rv_dialog_calendar.adapter = scheduleRecyclerViewAdapter
            ibtn_dialog_prev.setOnClickListener {
                scheduleRecyclerViewAdapter.changeToPrevMonth()
            }
            ibtn_dialog_next.setOnClickListener {
                scheduleRecyclerViewAdapter.changeToNextMonth()
            }
        }
    }

    fun refreshCurrentMonth(calendar: Calendar): String {
        val sdf = SimpleDateFormat("yyyy-MM", Locale.KOREAN)
        dlg.tv_dialog_current_month.text = sdf.format(calendar.time)
        return dlg.tv_dialog_current_month.text.toString()
    }
}