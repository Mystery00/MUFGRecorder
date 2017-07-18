package com.janyo.mufgrecorder.util

import android.content.Context
import android.content.pm.PackageInfo
import com.janyo.mufgrecorder.R
import android.content.pm.ApplicationInfo
import android.util.Log
import com.janyo.mufgrecorder.`class`.MUFG
import com.janyo.mufgrecorder.`class`.UpdateItems
import org.litepal.crud.DataSupport
import java.util.*

class IngressUtil(val context: Context)
{
	fun ConvertArrayToList(checked: BooleanArray,
						   items: Array<String>,
						   list: ArrayList<HashMap<String, Any>>): ArrayList<HashMap<String, Any>>
	{
		val item_level = context.resources.getStringArray(R.array.ingress_items_level_things)
		val level = context.resources.getStringArray(R.array.ingress_level_things)
		for (i in checked.indices)
		{
			if (!checked[i])
			{
				continue
			}
			var isContain = false
			val map = HashMap<String, Any>()
			map.put("name", items[i])
			map.put("number", 0)
			if (items[i] == "Portal Key")
			{
				map.put("keyName", "null")
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
			for (index in list.indices)
			{
				if (list[index]["name"] == map["name"])
				{
					val oldMap = list[index]
					if (map.containsKey("type") &&
							oldMap["type"] == map["type"] &&
							oldMap["level"] == map["level"])
					{
						map.put("number", oldMap["number"] as Int + map["number"] as Int)
						list[index] = map
						list.remove(oldMap)
						isContain = true
						break
					}
					else if (map["name"] == "Portal Key")
					{
						if (oldMap["keyName"] == map["keyName"])
						{
							map.put("number", oldMap["number"] as Int + map["number"] as Int)
							list[index] = map
							list.remove(oldMap)
							isContain = true
						}
						break
					}
					else if (!map.containsKey("type"))
					{
						map.put("number", oldMap["number"] as Int + map["number"] as Int)
						list[index] = map
						list.remove(oldMap)
						isContain = true
						break
					}
				}
			}
			if (!isContain)
				list.add(map)
		}
		return list
	}

	fun checkContent(list: ArrayList<HashMap<String, Any>>, mufg: MUFG)
	{
		for (i in 0..list.size - 2)
		{
			for (j in list.size - 1 downTo i + 1)
			{
				val item1 = list[i]
				val item2 = list[j]
				if (item1["name"] == item2["name"])
				{
					Log.i("info", "名字相同" + item1["name"])
					if (item1.containsKey("type") &&
							item1["type"] == item2["type"] &&
							item1["level"] == item2["level"])
					{
						Log.i("info", "同等级")
						item1.put("number", item1["number"] as Int + item2["number"] as Int)
						list.removeAt(j)
						break
					}
					else if (!item1.containsKey("type") &&
							item1.containsKey("keyName") &&
							item1["keyName"] == item2["keyName"])
					{
						Log.i("info", "同钥匙")
						item1.put("number", item1["number"] as Int + item2["number"] as Int)
						list.removeAt(j)
						break
					}
				}
			}
		}
		mufg.content = list
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

	fun getInfoFromMUFG(mufgName: String): String
	{
		var result = ""
		val updates = DataSupport.where("mufgName=?", mufgName).find(UpdateItems::class.java)
		updates.forEach { result += convertThingsToString(it) + "\n" }
		return result
	}

	fun convertThingsToString(updateItems: UpdateItems): String
	{
		var result = "This MUFG Capsule produced:\n"
		for (field in updateItems.javaClass.declaredFields)
		{
			if (field.genericType.toString() != "int")
			{
				continue
			}
			val fieldNum = updateItems.javaClass.getMethod("get" + field.name).invoke(updateItems) as Int
			if (fieldNum == 0)
			{
				continue
			}
			val items = field.name.split("_")
			if (items.size != 2)
			{
				result += "    " + fieldNum + " " + items[0]
				if (fieldNum > 1)
					result += "s"
			}
			else
			{
				val items_level = arrayOf("Common", "Rare", "VeryRare")
				val level = arrayOf("1", "2", "3", "4", "5", "6", "7", "8")
				if (items_level.contains(items[1]))
				{
					result += "    " + fieldNum + " " + items[1] + " " + items[0]
					if (fieldNum > 1)
						result += "s"
				}
				else if (level.contains(items[1]))
				{
					result += "    " + fieldNum + " LV" + items[1] + " " + items[0]
					if (fieldNum > 1)
						result += "s"
				}
			}
			result += "\n"
		}
		result += "  at " + updateItems.updateTime
		return result
	}
}
