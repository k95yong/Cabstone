package com.prac.cabstone.schedule

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prac.cabstone.MainActivity
import com.prac.cabstone.MainViewModel
import com.prac.cabstone.R
import com.softsquared.myapplication.db.Groups
import kotlinx.android.synthetic.main.item_schedule_choice.view.*

class GridAdapter: RecyclerView.Adapter<GridAdapter.Holder> {
    constructor(viewModel: MainViewModel) {
        this.viewModel = viewModel
        scheduleFragment = ScheduleFragment(viewModel, "")
    }
    private val viewModel: MainViewModel
    var list = ArrayList<Groups>()
    var scheduleFragment:ScheduleFragment? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_schedule_choice, parent, false)
        return Holder(inflatedView)

    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = list[position]
        holder.setItem(item)

    }
    override fun getItemCount(): Int {
        return list.size
    }
    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun setItem(data: Groups){
            itemView.iv_pic.clipToOutline = true
            itemView.tv_title.text = data.gname

            itemView.setOnClickListener {
                viewModel.setTransaction()
                val mainActivity = viewModel.getMainActivity()
                if(mainActivity?.scheduleFragment == null){
                    mainActivity?.scheduleFragment = ScheduleFragment(viewModel, data.gname)
                    viewModel.addTransaction(mainActivity?.scheduleFragment!!)
                }
                viewModel.showTransaction(mainActivity?.scheduleFragment!!)
            }

        }
    }
    fun swapItems(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            Log.e("list before: ", list.toString())
            for (i in fromPosition until toPosition) {
                var tmpIdx = list[i + 1].idx
                list[i + 1].idx = list[i].idx
                list[i].idx = tmpIdx

                viewModel.update(list[i + 1])
                viewModel.update(list[i])

                var tmp = list[i + 1]

                list[i + 1] = list[i]
                list[i] = tmp

            }
            Log.e("list after: ", list.toString())
        } else {
            Log.e("list before: ", list.toString())
            for (i in toPosition + 1..fromPosition) {
                var tmpIdx = list[i-1].idx
                list[i - 1].idx = list[i].idx
                list[i].idx = tmpIdx

                viewModel.update(list[i - 1])
                viewModel.update(list[i])

                var tmp = list[i-1]
                list[i - 1] = list[i]
                list[i] = tmp
            }
            Log.e("list after: ", list.toString())
        }
        notifyItemMoved(fromPosition, toPosition)
    }
}

