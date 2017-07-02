package com.janyo.mufgrecorder.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.janyo.mufgrecorder.R
import com.janyo.mufgrecorder.`class`.MUFG

class MUFGAdapter(var list:ArrayList<MUFG>,var context:Context): RecyclerView.Adapter<MUFGAdapter.ViewHolder>()
{
	override fun onBindViewHolder(holder: ViewHolder, position: Int)
	{
		Glide.with(context)
				.load(R.raw.mufg_capsule)
				.into(holder.mufgImage)
		holder.mufgID.text=list[position].number
	}

	override fun getItemCount(): Int
	{
		return list.size
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
	{
		val view=LayoutInflater.from(parent.context).inflate(R.layout.item_mufg_show,parent,false)
		return ViewHolder(view)
	}

	class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
	{
		var mufgImage=itemView.findViewById<ImageView>(R.id.mufgImage)
		var mufgNumber=itemView.findViewById<TextView>(R.id.mufgNumber)
		var mufgID=itemView.findViewById<TextView>(R.id.mufgID)
	}
}