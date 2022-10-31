package com.example.smac_runapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smac_runapp.databinding.ItemReceiveBinding
import com.example.smac_runapp.models.Receive

class ReceiveAdapter(
    private val lsReceive: ArrayList<Receive>,
    private val type: Int
) : RecyclerView.Adapter<ReceiveAdapter.MyViewHolder>() {

    private lateinit var listener: OnClickItem
    private val limit = 4

    @SuppressLint("NotifyDataSetChanged")
    fun addData(list: List<Receive>) {
        lsReceive.clear()
        lsReceive.addAll(list)
        notifyDataSetChanged()
    }

    fun setOnClickListener(listener: OnClickItem) {
        this.listener = listener
    }

    class MyViewHolder(val mBinding: ItemReceiveBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemReceiveBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //var opacity = 100
        val currentMovie = lsReceive[position]
        holder.mBinding.item = currentMovie
        if (!currentMovie.check){
            holder.mBinding.image.setAlpha(20)
        }
    }

    override fun getItemCount(): Int {
        if (type == 0 && lsReceive.size > limit) return limit

        return lsReceive.size
    }

    interface OnClickItem {
        fun clickItem(results: Receive);
    }
}