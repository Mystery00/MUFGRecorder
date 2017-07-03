package com.janyo.mufgrecorder.util

import android.content.Context

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
}