package com.janyo.mufgrecorder.util

import android.content.Context
import android.net.Uri

class Settings(context: Context)
{
	private val SharedPreferenceName = "settings"
	private val sharedPreference = context.getSharedPreferences(SharedPreferenceName, Context.MODE_PRIVATE)

	fun setNotificationTime(time: String)
	{
		sharedPreference.edit().putString("notificationTime", time).apply()
	}

	fun getNotificationTime(): String
	{
		return sharedPreference.getString("notificationTime", "0:0")
	}

	fun setNotification(isNotify: Boolean)
	{
		sharedPreference.edit().putBoolean("isNotify", isNotify).apply()
	}

	fun getNotify(): Boolean
	{
		return sharedPreference.getBoolean("isNotify", false)
	}

	fun setNotificationRingtone(temp: String)
	{
		sharedPreference.edit().putString("notificationRingtone", temp).apply()
	}

	fun getNotificationRingtone(): String
	{
		return sharedPreference.getString("notificationRingtone", "")
	}

	fun setNotificationVibrate(temp: Boolean)
	{
		sharedPreference.edit().putBoolean("notificationVibrate", temp).apply()
	}

	fun getNotificationVibrate(): Boolean
	{
		return sharedPreference.getBoolean("notificationVibrate", true)
	}
}