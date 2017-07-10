package com.janyo.mufgrecorder

import android.app.Application
import com.mystery0.tools.Logs.Logs
import org.litepal.LitePal

class APP : Application()
{
	override fun onCreate()
	{
		super.onCreate()
		Logs.setLevel(Logs.LogLevel.Debug)
		LitePal.initialize(this);
	}
}