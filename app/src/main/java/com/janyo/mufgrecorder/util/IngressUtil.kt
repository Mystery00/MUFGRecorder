package com.janyo.mufgrecorder.util

import android.content.Context
import com.janyo.mufgrecorder.R
import java.util.HashMap

class IngressUtil(val context: Context)
{
	fun ConvertArrayToList(checked: BooleanArray,
						   items: Array<String>): ArrayList<HashMap<String, Any>>
	{
		val item_level = context.resources.getStringArray(R.array.ingress_items_level_things)
		val level = context.resources.getStringArray(R.array.ingress_level_things)
		val list = ArrayList<HashMap<String, Any>>()
		for (i in checked.indices)
		{
			if (!checked[i])
			{
				continue
			}
			val map = HashMap<String, Any>()
			map.put("name", items[i])
			map.put("number", 0)
			if (item_level.contains(items[i]))
			{
				map.put("type", 1)
				map.put("level", 0)
			}
			if (level.contains(items[i]))
			{
				map.put("type", 2)
				map.put("level", 0)
			}
			list.add(map)
		}
		return list
	}
}