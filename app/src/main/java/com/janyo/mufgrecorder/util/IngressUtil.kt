package com.janyo.mufgrecorder.util

import android.content.Context
import android.content.pm.PackageInfo
import com.janyo.mufgrecorder.R
import java.util.HashMap
import android.content.pm.ApplicationInfo
import com.janyo.mufgrecorder.`class`.MUFG


class IngressUtil(val context: Context)
{
	fun ConvertArrayToList(checked: BooleanArray,
						   items: Array<String>, mufg: MUFG): ArrayList<HashMap<String, Any>>
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
			for (temp in mufg.content)
			{
				if (temp["name"] == items[i])
				{
					map.put("number", temp["number"] as Int)
					break
				}
			}
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

	fun getIngressPackageName(): String
	{
		val packageManager = context.packageManager
		val packageInfoList: List<PackageInfo> = packageManager.getInstalledPackages(0)
		val result = packageInfoList
				.firstOrNull { it.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM <= 0 && it.applicationInfo.packageName.contains(context.getString(R.string.ingressPackageName)) }?.applicationInfo?.packageName
				?: "null"
		return result
	}

	@Suppress("UNCHECKED_CAST")
	fun cloneList(list: ArrayList<HashMap<String, Any>>): ArrayList<HashMap<String, Any>>
	{
		val resultList = ArrayList<HashMap<String, Any>>()
		list.mapTo(resultList) { it.clone() as HashMap<String, Any> }
		return resultList
	}

	fun compareList(oldList: ArrayList<HashMap<String, Any>>,newList: ArrayList<HashMap<String, Any>>)
	{
		for (map in oldList)
		{
			for (newMap in newList)
			{
				if (map["name"]==newMap["name"])
				{

				}
			}
		}
	}
}