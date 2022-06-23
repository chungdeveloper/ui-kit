package com.chungld.uipack.rvadapter

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.ViewGroup

class RvMultiTypeAdapter<E> private constructor(
    items: MutableList<E>,
    private val viewTypeStrategies: Map<Int, ViewTypeStrategy<*, RvBaseHolder<Any>>>
) : RvSingleTypeAdapter<E, RvBaseHolder<E>>() {

    init {
        setData(items)
    }

    @Deprecated("UnUse")
    override fun getLayoutId(): Int {
        TODO("Not yet implemented")
    }

    @Deprecated("UnUse")
    override fun createViewHolder(viewDataBinding: ViewDataBinding): RvBaseHolder<E> {
        TODO("Not yet implemented")
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RvBaseHolder<E> {
        if (viewTypeStrategies[viewType] == null) {
            throw RuntimeException("Can not create viewHolder for this item with viewType = $viewType")
        }
        val viewDataBinding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(viewGroup.context),
            viewTypeStrategies.getValue(viewType).layoutId,
            viewGroup, false
        )

        return viewTypeStrategies.getValue(viewType).strategy.createHolder(viewDataBinding) as RvBaseHolder<E>
    }

    override fun getItemViewType(position: Int): Int {
        val itemObj = data[position]

        for ((viewType, strategy) in viewTypeStrategies) {
            if (strategy.clazz.isInstance(itemObj)) {
                return viewType
            }
        }
        throw RuntimeException("Can not find viewType for this item with position = $position")
    }


    class Builder<E>(private val items: MutableList<E>) {

        private val mapTypes = hashMapOf<Int, ViewTypeStrategy<*, *>>()

        fun <VH : RvBaseHolder<out Any>> register(
            viewType: Int,
            layoutId: Int,
            clazz: Class<out Any>,
            strategy: Strategy<Any, VH>
        ): Builder<E> {
            val strategyItem = ViewTypeStrategy(layoutId, clazz, strategy)
            mapTypes[viewType] = strategyItem
            return this
        }

        fun build(): RvMultiTypeAdapter<*> {
            val types = mapTypes as Map<Int, ViewTypeStrategy<*, RvBaseHolder<Any>>>
            return RvMultiTypeAdapter(items, types)
        }

    }

    class ViewTypeStrategy<out T, VH : RvBaseHolder<out Any>>(
        val layoutId: Int,
        val clazz: Class<out T>,
        val strategy: Strategy<T, VH>
    )

    fun <T> removeFirstItemType(clazz: Class<T>) {
        val obj = data.find { clazz.isInstance(it) }
        obj?.let {
            val index = data.indexOf(it)
            data.remove(it)
            notifyItemRemoved(index)
        }
    }

    fun <T> removeLastItemType(clazz: Class<T>) {
        val obj = data.findLast { clazz.isInstance(it) }
        obj?.let {
            val index = data.indexOf(it)
            data.remove(it)
            notifyItemRemoved(index)
        }
    }

    fun <T> removeAllItemType(clazz: Class<T>) {
        val listNeedRemove = data.filter { clazz.isInstance(it) }
        data.removeAll(listNeedRemove)
        notifyDataSetChanged()
    }

    fun <T> findFirstIndexOfItemType(clazz: Class<T>): Int {
        return data.indexOfFirst { clazz.isInstance(it) }
    }

    fun <T> findLastIndexOfItemType(clazz: Class<Any>): Int {
        return data.indexOfFirst { clazz.isInstance(it) }
    }

    fun <T> findAllIndexOfType(clazz: Class<T>): List<Int> {
        return data.filter { clazz.isInstance(it) }.map { data.indexOf(it) }
    }

    fun <T> replaceDataType(list: List<T>?, clazz: Class<T>) {
        removeAllItemType(clazz)
        addData(list as List<E>)
    }

    fun <T> replaceDataType(entity: E, clazz: Class<T>) {
        val index = findFirstIndexOfItemType(clazz)
        data[index] = entity
        notifyItemChanged(index)
    }

    fun <T> appendToIndex(index: Int, list: List<E>) {
        data.addAll(index, list)
        notifyItemRangeInserted(index, list.count())
    }
}