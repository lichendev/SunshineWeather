package com.example.sunshineweather

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.text.FieldPosition

class CityItemTouchCallback(val itemMoveSwipeCallback: ItemMoveSwipeCallback): ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val drawFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlag = ItemTouchHelper.LEFT
        return makeMovementFlags(drawFlag,swipeFlag)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return itemMoveSwipeCallback.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
       itemMoveSwipeCallback.onItemSwipe(viewHolder.adapterPosition)
    }

}

interface ItemMoveSwipeCallback{
    fun onItemMove(sourcePosition: Int, targetPosition: Int): Boolean
    fun onItemSwipe(sourcePosition: Int)
}