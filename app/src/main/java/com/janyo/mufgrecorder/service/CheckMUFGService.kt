package com.janyo.mufgrecorder.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.janyo.mufgrecorder.util.CheckNotification
import com.janyo.mufgrecorder.util.Settings
import com.janyo.mufgrecorder.util.TimeUtil

class CheckMUFGService : Service()
{
	override fun onBind(intent: Intent): IBinder?
	{
		return null
	}

	override fun onCreate()
	{
		super.onCreate()
		CheckNotification.notify(this, 1)
		val settings = Settings(this)
		val setTime = TimeUtil.getSetTime(TimeUtil.timeFormat(settings.getNotificationTime()))
		setTime.time += 24 * 3600 * 1000
		TimeUtil.setAlarm(this, setTime)
		stopSelf()
	}
}
