package com.prac.cabstone.schedule

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.prac.cabstone.R


class DragManageAdapter(adapter: GridAdapter, context: Context, dragDirs: Int, swipeDirs: Int) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs)
{
    private val dragAdapter = adapter
    private var delFlag = false

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        dragAdapter.swapItems(viewHolder.adapterPosition, target.adapterPosition)
        Log.e("onmove", "moved")
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
    }
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        dragAdapter.deleteItem(viewHolder.adapterPosition)
        Log.e("holderPos", viewHolder.adapterPosition.toString())
        Log.e("direction", direction.toString())
    }

    override fun getSwipeDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return 0
    }
}