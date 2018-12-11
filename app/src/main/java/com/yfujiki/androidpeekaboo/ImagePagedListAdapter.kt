package com.yfujiki.androidpeekaboo

import androidx.paging.PagedListAdapter
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_holder_image.view.*

class ImagePagedListAdapter: PagedListAdapter<Drawable, ViewHolder>(DrawableDiffItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val drawable = getItem(position)
        holder.setImage(drawable)
    }
}

class DrawableDiffItemCallback: DiffUtil.ItemCallback<Drawable>() {
    override fun areItemsTheSame(oldItem: Drawable, newItem: Drawable): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Drawable, newItem: Drawable): Boolean {
        return oldItem == newItem
    }

}

class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun setImage(drawable: Drawable?) {
        itemView?.imageView?.setImageDrawable(drawable)
    }
}