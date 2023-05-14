package com.example.androidapp.home.util

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidapp.R
import com.example.androidapp.databinding.HomeScreenItemBinding
import com.example.androidapp.home.model.RvModalClass

class HomeScreenAdapter(private var items : List<RvModalClass>) : RecyclerView.Adapter<HomeScreenAdapter.HomeViewHolder>()
{
    class HomeViewHolder(val binding : HomeScreenItemBinding)  : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
      return HomeViewHolder(HomeScreenItemBinding.inflate(LayoutInflater.from(parent.context ) , parent , false))
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item=items[position]
        holder.binding.tvHeadline.text = item.heading
        holder.binding.tvBrief.text= item.description
        holder.binding.tvTimeSource.text = item.publishedAt
        Glide.with(holder.binding.newsCover.context)
            .load(item.url)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.binding.newsCover)

    }

    override fun getItemCount(): Int {
       return items.size
    }
    fun updateData(data : MutableList<RvModalClass>){
        items = data
        notifyItemRangeChanged(0, itemCount)
    }

}
class WrapContentLinearLayoutManager(context:Context) : LinearLayoutManager(context) {
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            Log.e("TAG", "meet a IOOBE in RecyclerView")
        }
    }
}