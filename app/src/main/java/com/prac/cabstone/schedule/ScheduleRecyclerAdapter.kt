package com.prac.cabstone.schedule

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.prac.cabstone.MainViewModel
import com.prac.cabstone.R
import com.softsquared.myapplication.db.Todo
import kotlinx.android.synthetic.main.item_today.view.*
import java.util.*
import kotlin.collections.ArrayList

class ScheduleRecyclerAdapter(
    val context: Context,
    val scheduleFragment: ScheduleFragment,
    val curDate: String,
    var list: ArrayList<Todo>,
    val viewBinderHelper: ViewBinderHelper = ViewBinderHelper(),
    val viewModel: MainViewModel,
    val itemClick: (Todo) -> Unit
) :
    RecyclerView.Adapter<ScheduleRecyclerAdapter.ViewHolder>() {

    override fun getItemCount() = list.size
    fun removeItem(position: Int) {
        val del = list.get(position)
        viewModel.delete(del)
        list.remove(del)

        notifyDataSetChanged()
    }

    fun modifyItem(position: Int) {
        val mod = list.get(position)
        scheduleFragment.showAlertDialog(2, mod)
        notifyDataSetChanged()
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        viewBinderHelper.setOpenOnlyOne(true)
        viewBinderHelper.bind(
            holder.swipeRevealLayout,
            list.get(position).id.toString()
        )
        viewBinderHelper.closeLayout((list.get(position).id.toString()))

        holder?.bind(list[position], context)
        if (holder.itemView.btn_del != null) {
            holder.itemView.btn_del.setOnClickListener {
                if (viewModel.getMyGroupSize(list.get(position).gid)!! > 1) {
                    val builder = AlertDialog.Builder(context)
                    val dialogView =
                        scheduleFragment.layoutInflater.inflate(R.layout.dialog_del_type, null)
                    val rbtn_type_one = dialogView.findViewById<RadioButton>(R.id.rbtn_type_one)
                    val rbtn_type_multi = dialogView.findViewById<RadioButton>(R.id.rbtn_type_multi)

                    builder.setView(dialogView)
                        .setPositiveButton("확인") { dialogInterface, i ->
                            if (rbtn_type_one.isChecked()) {
                                removeItem(position)
                            } else {
                                viewModel.removeGroup(list.get(position).gid)
                                list.remove(list.get(position))
                                notifyDataSetChanged()
                            }
                        }
                        .setNegativeButton("취소") { dialogInterface, i ->
                        }
                        .show()
                } else {
                    removeItem(position)
                }
            }
        }

        holder.itemView.btn_modify.setOnClickListener {
            modifyItem(position)
        }

        holder.itemView.cb_checker.setOnClickListener {
            list[position].clear = !list[position].clear
            viewModel.update(list[position])
            if (list[position].clear) {
                holder.itemView.tv_contents.setTextColor(
                    Color.parseColor(
                        "#bbbbbb"
                    )
                )
                holder.itemView.cb_checker.buttonTintList =
                    context.getColorStateList(R.color.cleared)
            } else {
                holder.itemView.tv_contents.setTextColor(
                    Color.parseColor(
                        "#000000"
                    )
                )
                holder.itemView.cb_checker.buttonTintList =
                    context.getColorStateList(R.color.black)
            }
        }

        if (list[position].clear) {
            holder.itemView.tv_contents.setTextColor(
                Color.parseColor(
                    "#bbbbbb"
                )
            )
            holder.itemView.cb_checker.buttonTintList = context.getColorStateList(R.color.cleared)
        } else {
            holder.itemView.cb_checker.setTextColor(
                Color.parseColor(
                    "#000000"
                )
            )
            holder.itemView.cb_checker.buttonTintList = context.getColorStateList(R.color.black)
        }
        holder.itemView.ll_checker.setOnClickListener {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_today, parent, false)
        return ViewHolder(inflatedView, itemClick)
    }

    inner class ViewHolder(itemView: View?, itemClick: (Todo) -> Unit) :

        RecyclerView.ViewHolder(itemView!!) {
        val swipeRevealLayout = itemView?.findViewById<SwipeRevealLayout>(R.id.swipelayout)


        fun bind(item: Todo, context: Context) {
            itemView.tv_contents.text = item.contents
            itemView.cb_checker.isChecked = item.clear
            itemView.setOnClickListener { itemClick(item) }
        }

    }

    fun deleteItem(idx: Int){
//        viewModel.removeGroup(list[idx])
//        list.remove(list[idx])
        notifyDataSetChanged()
    }

    fun onItemMove(fromPosition: Int?, toPosition: Int?): Boolean {
        fromPosition?.let {
            toPosition?.let {
                if (fromPosition < toPosition) {
                    for (i in fromPosition until toPosition) {
                        var tmpIdx = list[i + 1].idx
                        list[i + 1].idx = list[i].idx
                        list[i].idx = tmpIdx
                        viewModel.update(list[i + 1])
                        viewModel.update(list[i])

                        Collections.swap(list, i, i + 1)

                    }
                } else {
                    for (i in fromPosition downTo toPosition + 1) {
                        var tmpIdx = list[i-1].idx
                        list[i - 1].idx = list[i].idx
                        list[i].idx = tmpIdx
                        viewModel.update(list[i - 1])
                        viewModel.update(list[i])

                        Collections.swap(list, i, i - 1)
                    }
                }
                notifyItemMoved(fromPosition, toPosition)
                return true
            }
        }
        return false
    }
}



