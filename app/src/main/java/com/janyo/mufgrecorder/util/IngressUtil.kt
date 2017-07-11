package com.janyo.mufgrecorder.util

import android.content.Context
import android.content.pm.PackageInfo
import com.janyo.mufgrecorder.R
import android.content.pm.ApplicationInfo
import com.janyo.mufgrecorder.`class`.MUFG
import com.janyo.mufgrecorder.`class`.UpdateItems
import java.util.*


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

	fun compareList(updateTime: String, mufgName: String, oldList: ArrayList<HashMap<String, Any>>,
					newList: ArrayList<HashMap<String, Any>>): UpdateItems
	{
		val updateItems = UpdateItems(updateTime, mufgName)
		for (map in oldList)
		{
			newList
					.filter { map["name"] == it["name"] }
					.map { it["number"] as Int - map["number"] as Int }
					.forEach {
						when (map["name"])
						{
							"media" ->
							{
								updateItems.Media = it
							}
							"Portal Shield" ->
							{
								compareItemLevel(map["type"] as Int, map["level"] as Int, updateItems, it, "PortalShield")
							}
							"AXA Shield" ->
							{
								updateItems.AXAShield = it
							}
							"Link Amp" ->
							{
								compareItemLevel(map["type"] as Int, map["level"] as Int, updateItems, it, "LinkAmp")
							}
							"SoftBank" ->
							{
								updateItems.SoftBank = it
							}
							"Heat Sink" ->
							{
								compareItemLevel(map["type"] as Int, map["level"] as Int, updateItems, it, "HeatSink")
							}
							"Multi-hack" ->
							{
								compareItemLevel(map["type"] as Int, map["level"] as Int, updateItems, it, "Multihack")
							}
							"Force Amp" ->
							{
								updateItems.ForceAmp = it
							}
							"Turret" ->
							{
								updateItems.Turret = it
							}
							"Portal Key" ->
							{
								updateItems.PortalKey = it
							}
							"Power Cube" ->
							{
								compareItemLevel(map["type"] as Int, map["level"] as Int, updateItems, it, "PowerCube")
							}
							"Resonator" ->
							{
								compareItemLevel(map["type"] as Int, map["level"] as Int, updateItems, it, "Resonator")
							}
							"Xmp Burster" ->
							{
								compareItemLevel(map["type"] as Int, map["level"] as Int, updateItems, it, "XmpBurster")
							}
							"Ultra Strike" ->
							{
								compareItemLevel(map["type"] as Int, map["level"] as Int, updateItems, it, "UltraStrike")
							}
							"Jarvis Virus" ->
							{
								updateItems.JarvisVirus = it
							}
							"ADA Refactor" ->
							{
								updateItems.ADARefactor = it
							}
							"Lawson Power Cube" ->
							{
								updateItems.LawsonPowerCube = it
							}
							"Circle-K Power Cube" ->
							{
								updateItems.CircleKPowerCube = it
							}
						}
					}
		}
		return updateItems
	}

	fun compareItemLevel(type: Int, level: Int, objects: Any, number: Int, itemName: String)
	{
		val c = objects.javaClass
		var itemLevel = ""
		when (type)
		{
			1 ->
			{
				val itemLevels = context.resources.getStringArray(R.array.ingress_item_level)
				itemLevel = itemLevels[level]
			}
			2 ->
			{
				itemLevel = (level + 1).toString()
			}
		}
		c.declaredMethods
				.filter { it.name.toLowerCase().contains("set") }
				.filter { it.name.toLowerCase().contains(itemLevel.toLowerCase()) }
				.filter { it.name.contains(itemName) }
				.forEach { it.invoke(objects, number) }
	}
}
