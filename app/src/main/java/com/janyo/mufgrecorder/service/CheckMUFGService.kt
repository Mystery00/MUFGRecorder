package com.janyo.mufgrecorder.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.janyo.mufgrecorder.util.CheckNotification

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
		stopSelf()
	}
}
