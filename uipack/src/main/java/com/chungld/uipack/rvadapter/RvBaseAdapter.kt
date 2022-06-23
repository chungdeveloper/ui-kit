package com.chungld.uipack.rvadapter

import androidx.recyclerview.widget.RecyclerView

abstract class RvBaseAdapter<E, VH : RvBaseHolder<E>> : RecyclerView.Adapter<VH>() {

    val data = mutableListOf<E>()

    override fun getItemCount(): Int = data.count()

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(data[position]!!)
    }

    fun setData(newData: List<E>?) {
        newData?.let {
            data.clear()
            data.addAll(newData)
            notifyDataSetChanged()
        }
    }

    fun addData(newData: List<E>?) {
        newData?.let {
            val startIndex = data.count()
            data.addAll(newData)
            notifyItemRangeInserted(startIndex, newData.count())
        }
    }

    fun addItem(obj: E) {
        data.add(obj)
        notifyItemInserted(data.count())
    }

    fun addItemIndex(index: Int, obj: E) {
        data.add(index, obj)
        notifyItemInserted(index)
    }

    fun removeAll() {
        data.clear()
        notifyDataSetChanged()
    }

    fun removeItem(obj: E) {
        val index = data.indexOf(obj)
        if (index != -1) {
            data.remove(obj)
            notifyItemRemoved(index)
        }
    }

    fun removeAt(index: Int) {
        if (index != -1) {
            data.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun indexOf(obj: E): Int {
        return data.indexOf(obj)
    }

    fun replaceIndex(index: Int, obj: E) {
        if (index > -1 && index < data.count()) {
            data[index] = obj
            notifyItemChanged(index)
        }
    }

}