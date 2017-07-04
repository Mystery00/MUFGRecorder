package com.janyo.mufgrecorder.util

import java.text.SimpleDateFormat
import java.util.*

object TimeUtil
{
	@JvmStatic fun timeFormat(time: String): String
	{
		var result = ""
		val temp = time.split(":")
		if (temp[0].length == 1)
		{
			result += "0"
		}
		result += temp[0] + ":"
		if (temp[1].length == 1)
		{
			result += "0"
		}
		result += temp[1]
		return result
	}

	@JvmStatic fun getSetTime(time: String): Date
	{
		val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
		val calendar = Calendar.getInstance()
		val date = calendar.get(Calendar.YEAR).toString() + "-" + (calendar.get(Calendar.MONTH) + 1).toString() + "-" + calendar.get(Calendar.DATE).toString() + " " + timeFormat(time) + ":00"
		return dateFormat.parse(date)
	}

	@JvmStatic fun calculateTime(time: String): Float
	{
		val calendar = Calendar.getInstance()
		val nowTime = calendar.time
		val temp = (24 * 3600 - (nowTime.time - getSetTime(time).time) / 1000) % (24 * 3600)
		return (temp.toFloat()) / 3600
	}
}