package com.janyo.mufgrecorder.adapter

import android.content.Context
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AlertDialog
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
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MUFGItemsAdapter(
		var list: ArrayList<HashMap<String, Any>>) : RecyclerView.Adapter<MUFGItemsAdapter.ViewHolder>()
{
	var context: Context? = null
	override fun getItemCount(): Int
	{
		return list.size
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
	{
		context = parent.context
		val view = LayoutInflater.from(context).inflate(R.layout.item_mufg_things, LinearLayout(context), false)
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
		if (map.containsKey("keyName"))
		{
			holder.name.text = map["keyName"] as String
			holder.name.setOnClickListener {
				val view = LayoutInflater.from(context!!).inflate(R.layout.text_input_layout, TextInputLayout(context), false)
				val text_input: TextInputLayout = view.findViewById(R.id.text_input)
				text_input.editText!!.setText(map["keyName"] as String)
				AlertDialog.Builder(context!!)
						.setTitle("test")
						.setView(view)
						.setPositiveButton(android.R.string.ok, { _, _ ->
							val name = text_input.editText!!.text.toString()
							holder.name.text = name
							map.put("keyName", name)
						})
						.show()
			}
		}
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

	class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
	{
		var itemName: TextView = itemView.findViewById(R.id.itemName)
		var name: TextView = itemView.findViewById(R.id.name)
		var level: AppCompatSpinner = itemView.findViewById(R.id.levelSpinner)
		var itemLevel: AppCompatSpinner = itemView.findViewById(R.id.itemLevelSpinner)
		var itemNumber: NumberPicker = itemView.findViewById(R.id.itemNumber)
	}
}
