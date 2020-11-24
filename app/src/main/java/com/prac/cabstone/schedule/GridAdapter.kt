package com.prac.cabstone.schedule

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.prac.cabstone.MainViewModel
import com.prac.cabstone.R
import com.softsquared.myapplication.db.Groups
import kotlinx.android.synthetic.main.item_schedule_choice.view.*

class GridAdapter : RecyclerView.Adapter<GridAdapter.Holder> {
    constructor(viewModel: MainViewModel) {
        this.viewModel = viewModel
        scheduleFragment = ScheduleFragment(viewModel, "")
    }

    constructor(context: Context) {
        this.viewModel = null
        this.context = context
    }

    private val viewModel: MainViewModel?
    var list = ArrayList<Groups>()
    var scheduleFragment: ScheduleFragment? = null
    var context: Context? = null

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

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setItem(data: Groups) {
            itemView.tv_title.text = data.gname
            Log.e("viewModel test", "gogo")
            context?.let { Glide.with(it).load(data.img_path).into(itemView.iv_pic) }
            if(viewModel == null){
                itemView.setOnClickListener {
                    val intent = Intent()
                    Log.e("gid", data.id.toString())
                    intent.putExtra("gid", data.id)
                    intent.putExtra("g_name", data.gname)
                    (context as Activity).setResult(Activity.RESULT_OK, intent)
                    (context as Activity).finish()
                }
            }else{
                Log.e("path", data.img_path)
                Glide.with(viewModel.getMainActivity()!!).load(data.img_path).into(itemView.iv_pic)
                itemView.setOnClickListener {
                    viewModel.setTransaction()
                    val mainActivity = viewModel.getMainActivity()
                    if (mainActivity?.scheduleFragment == null) {
                        mainActivity?.scheduleFragment = ScheduleFragment(viewModel, data.gname)
                        viewModel.addTransaction(mainActivity?.scheduleFragment!!)
                    } else {
                        viewModel.removeTransaction(mainActivity.scheduleFragment!!)
                        mainActivity.scheduleFragment = ScheduleFragment(viewModel, data.gname)
                        viewModel.addTransaction(mainActivity?.scheduleFragment!!)
                    }
                    viewModel.showTransaction(mainActivity?.scheduleFragment!!)
                    mainActivity?.cur_frag = 5
                }
            }
        }
    }

    fun deleteItem(idx: Int) {
        notifyDataSetChanged()
    }


    fun swapItems(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            Log.e("list before: ", list.toString())
            for (i in fromPosition until toPosition) {
                var tmpIdx = list[i + 1].idx
                list[i + 1].idx = list[i].idx
                list[i].idx = tmpIdx

                viewModel?.update(list[i + 1])
                viewModel?.update(list[i])

                var tmp = list[i + 1]

                list[i + 1] = list[i]
                list[i] = tmp

            }
            Log.e("list after: ", list.toString())
        } else {
            Log.e("list before: ", list.toString())
            for (i in toPosition + 1..fromPosition) {
                var tmpIdx = list[i - 1].idx
                list[i - 1].idx = list[i].idx
                list[i].idx = tmpIdx

                viewModel?.update(list[i - 1])
                viewModel?.update(list[i])

                var tmp = list[i - 1]
                list[i - 1] = list[i]
                list[i] = tmp
            }
            Log.e("list after: ", list.toString())
        }
        notifyItemMoved(fromPosition, toPosition)
    }
}

