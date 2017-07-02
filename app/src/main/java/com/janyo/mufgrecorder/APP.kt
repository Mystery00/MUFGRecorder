package com.janyo.mufgrecorder

import android.app.Application
import com.mystery0.tools.Logs.Logs

class APP : Application()
{
	override fun onCreate()
	{
		super.onCreate()
		Logs.setLevel(Logs.LogLevel.Debug)
	}
}