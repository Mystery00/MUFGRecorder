package com.janyo.mufgrecorder.adapter

import android.content.Context
import android.support.v7.widget.AppCompatSpinner
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.TextView
import com.janyo.mufgrecorder.R
import com.janyo.mufgrecorder.util.IngressUtil
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MUFGItemsAdapter(context: Context,
					   map: Map<String, Int>) : RecyclerView.Adapter<MUFGItemsAdapter.ViewHolder>()
{
	private var list = ArrayList<HashMap<String, Any>>()
	private var ingressUtil: IngressUtil? = null

	init
	{
		ingressUtil = IngressUtil(context)
		val tempList = ingressUtil!!.ConvertItemsFormat(map)
		list.clear()
		list.addAll(tempList)
	}

	override fun getItemCount(): Int
	{
		return list.size
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
	{
		val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mufg_things, LinearLayout(parent.context), false)
		val holder = ViewHolder(view)
		return holder
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int)
	{
		val map = list[position]
		holder.itemName.text = map["name"] as String
		holder.itemNumber.minValue = 0
		holder.itemNumber.maxValue = 100
		holder.itemNumber.value = map["number"] as Int
		if (map.containsKey("level"))
		{
			when (map["type"])
			{
				1 ->
				{
					holder.level.visibility = View.GONE
					holder.itemLevel.visibility = View.VISIBLE
					holder.itemLevel.setSelection(map["level"] as Int)
					holder.itemLevel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
					{
						override fun onItemSelected(parent: AdapterView<*>, view: View,
													i: Int, id: Long)
						{
							map.put("level", i)
						}

						override fun onNothingSelected(parent: AdapterView<*>)
						{
						}
					}
				}
				2 ->
				{
					holder.itemLevel.visibility = View.GONE
					holder.level.visibility = View.VISIBLE
					holder.level.setSelection(map["level"] as Int)
					holder.level.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
					{
						override fun onItemSelected(parent: AdapterView<*>, view: View,
													i: Int, id: Long)
						{
							map.put("level", i)
						}

						override fun onNothingSelected(parent: AdapterView<*>)
						{
						}
					}
				}
			}
		}
		holder.itemNumber.setOnValueChangedListener { _, _, newValue ->
			map.put("number", newValue)
		}
	}

	fun getList(): ArrayList<HashMap<String, Any>>
	{
		return list
	}

	class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
	{
		var itemName: TextView = itemView.findViewById(R.id.itemName)
		var level: AppCompatSpinner = itemView.findViewById(R.id.levelSpinner)
		var itemLevel: AppCompatSpinner = itemView.findViewById(R.id.itemLevelSpinner)
		var itemNumber: NumberPicker = itemView.findViewById(R.id.itemNumber)
	}
}
