package com.example.locofoco

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class ImageAdapter(val context: Context, private val images : List<CatImage>, val ClickListener: OnClickListener) : RecyclerView.Adapter<ImageAdapter.ViewHolder>(){

    interface OnClickListener{
        fun onItemClicked(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_image,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = images.get(position)
        holder.bind(image)
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val ivImage = itemView.findViewById<ImageView>(R.id.ivImage)

        init {
            itemView.setOnClickListener{
                ClickListener.onItemClicked(adapterPosition)
                true
            }
        }
        fun bind(image : CatImage){
            Glide.with(context)
                .load(image.url)
                .into(ivImage)

        }
    }

}