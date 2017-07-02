package com.janyo.mufgrecorder.activity

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.janyo.mufgrecorder.R
import com.mystery0.tools.Logs.Logs

import kotlinx.android.synthetic.main.activity_new_mufg.*

class NewMUFGActivity : AppCompatActivity()
{
	private val TAG = "NewMUFGActivity"
	private var items: Array<String>? = null
	private var checkedMap = HashMap<String, Boolean>()//选择的物品列表

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_new_mufg)
		setSupportActionBar(toolbar)

		items = resources.getStringArray(R.array.ingress_items)

		for (temp in items!!)
		{
			checkedMap.put(temp, false)
		}

		fab.setOnClickListener {
			AlertDialog.Builder(this)
					.setTitle(" ")
					.setMultiChoiceItems(items, null, { _, position, checked ->
						Logs.i(TAG, "onCreate: " + position)
						Logs.i(TAG, "onCreate: " + checked)
						checkedMap[items!![position]] = checked
					})
					.setPositiveButton(android.R.string.ok, { _, _ ->
						checkItemsAndShowLevel()
					})
					.show()
		}
	}

	fun checkItemsAndShowLevel(): Map<String, Int>
	{
		val needShowLevel = HashMap<String, Int>()
		val items_level_things = resources.getStringArray(R.array.ingress_items_level_things)
		val level_things = resources.getStringArray(R.array.ingress_level_things)
		for (temp in checkedMap.keys)
		{
			Logs.i(TAG, "checkItemsAndShowLevel: " + temp)
			if (checkedMap[temp]==false)
			{
				continue
			}
			if (items_level_things.contains(temp))
			{
				needShowLevel.put(temp, 1)
				continue
			}
			if (level_things.contains(temp))
			{
				needShowLevel.put(temp, 2)
				continue
			}
		}
		Logs.i(TAG, "onCreate: ")
		return needShowLevel
	}

}
