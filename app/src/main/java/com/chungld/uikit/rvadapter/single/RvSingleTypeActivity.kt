package com.chungld.uikit.rvadapter.single

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.chungld.uikit.R
import kotlinx.android.synthetic.main.activity_rv_single_type.*

class RvSingleTypeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rv_single_type)

        val data = mutableListOf<String>().apply {
            for(i in 0..1000){
                add("This is item $i")
            }
        }
        val adapter = MySingTypeRvAdapter()
        adapter.setData(data)
        rvString.adapter = adapter
        rvString.addItemDecoration(
            DividerItemDecoration(
                this,
                RecyclerView.VERTICAL
            )
        )

        val data2 = mutableListOf<String>().apply {
            add("233")
        }
        adapter.addData(data2)
    }
}