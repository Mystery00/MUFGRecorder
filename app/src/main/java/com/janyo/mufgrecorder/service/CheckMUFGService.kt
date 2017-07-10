package com.janyo.mufgrecorder.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.janyo.mufgrecorder.util.CheckNotification
import com.mystery0.tools.Logs.Logs

class CheckMUFGService : Service()
{
	private val TAG = "CheckMUFGService"
	override fun onBind(intent: Intent): IBinder?
	{
		return null
	}

	override fun onCreate()
	{
		super.onCreate()
		Logs.i(TAG, "onCreate: ")
		CheckNotification.notify(this, 1)
		stopSelf()
	}
}
