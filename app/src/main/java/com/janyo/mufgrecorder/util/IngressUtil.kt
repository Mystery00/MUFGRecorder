package com.janyo.mufgrecorder.util

import android.content.Context
import com.janyo.mufgrecorder.R
import java.util.HashMap

class IngressUtil(val context: Context)
{
	private val TAG = "IngressUtil"

	fun CheckItems(checkedMap: Map<String, Boolean>): Map<String, Int>
	{
		val map = HashMap<String, Int>()
		val item_level = context.resources.getStringArray(R.array.ingress_items_level_things)
		val level = context.resources.getStringArray(R.array.ingress_level_things)
		for (item in checkedMap.keys)
		{
			if (checkedMap[item] == false)
			{
				continue
			}
			if (item_level.contains(item))
			{
				map.put(item + "!1!0", 0)
				continue
			}
			if (level.contains(item))
			{
				map.put(item + "!2!0", 0)
				continue
			}
			map.put(item, 0)
		}
		return map
	}

	fun ConvertItemsFormat(map: Map<String, Int>): ArrayList<HashMap<String, Any>>
	{
		val list = ArrayList<HashMap<String, Any>>()
		for (name in map.keys)
		{
			val temp = HashMap<String, Any>()
			val names = name.split("!")
			temp.put("name", names[0])
			if (names.size > 1)
			{
				temp.put("type", names[1].toInt())
				temp.put("level", names[2].toInt())
			}
			temp.put("number", map[name] as Int)
			list.add(temp)
		}
		return list
	}

	fun ConvertItemsFormat(list: ArrayList<HashMap<String, Any>>): HashMap<String, Int>
	{
		val map = HashMap<String, Int>()
		for (item in list)
		{
			var name = item["name"] as String
			if (item.containsKey("level"))
			{
				name += "!" + item["type"] + "!" + item["level"]
			}
			map.put(name, item["number"] as Int)
		}
		return map
	}
}