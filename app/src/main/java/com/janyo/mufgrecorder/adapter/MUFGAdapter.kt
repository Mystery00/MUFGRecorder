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
import com.mystery0.tools.Logs.Logs

class MUFGAdapter(var list: ArrayList<MUFG>,
				  var context: Context) : RecyclerView.Adapter<MUFGAdapter.ViewHolder>()
{
	private val TAG = "MUFGAdapter"
	override fun onBindViewHolder(holder: ViewHolder, position: Int)
	{
		Glide.with(context)
				.load(R.raw.mufg_capsule)
				.into(holder.mufgImage)
		holder.mufgID.text = String.format(context.getString(R.string.mufg_pre_name), list[position].id)
		holder.mufgNumber.text = (position + 1).toString()
		val space = list[position].contentMap!!.keys.sumBy { list[position].contentMap!![it] as Int }
		holder.mufgSpace.text = String.format(context.getString(R.string.mufg_space), space.toString())
		holder.fullView.setOnClickListener {
			Logs.i(TAG, "onBindViewHolder: " + position)
		}
	}

	override fun getItemCount(): Int
	{
		return list.size
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
	{
		val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mufg_show, parent, false)
		return ViewHolder(view)
	}

	class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
	{
		var fullView: View = itemView
		var mufgImage: ImageView = itemView.findViewById(R.id.mufgImage)
		var mufgNumber: TextView = itemView.findViewById(R.id.mufgNumber)
		var mufgID: TextView = itemView.findViewById(R.id.mufgID)
		var mufgSpace: TextView = itemView.findViewById(R.id.mufgSpace)
	}
}