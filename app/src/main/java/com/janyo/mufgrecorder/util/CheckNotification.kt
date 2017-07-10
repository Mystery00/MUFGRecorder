package com.janyo.mufgrecorder.util

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.app.NotificationCompat

import com.janyo.mufgrecorder.R
import com.janyo.mufgrecorder.activity.MainActivity
import com.janyo.mufgrecorder.activity.UpdateActivity

object CheckNotification
{
	private val NOTIFICATION_TAG = "Check"

	fun notify(context: Context, number: Int)
	{
		val res = context.resources
		val ingressUtil = IngressUtil(context)
		val settings = Settings(context)
		val vibrates = LongArray(2)
		vibrates[1] = if (settings.getNotificationVibrate()) 500 else 0
		val builder = NotificationCompat.Builder(context, context.getString(R.string.app_name))
				.setSound(Uri.parse(settings.getNotificationRingtone()))
				.setVibrate(vibrates)
				.setSmallIcon(R.drawable.ic_notifications)
				.setContentTitle(context.getString(R.string.notification_title))
				.setContentText(context.getString(R.string.notification_text))
				.setPriority(NotificationCompat.PRIORITY_DEFAULT)
				.setNumber(number)
				.setContentIntent(
						PendingIntent.getActivity(
								context,
								0,
								Intent(context, UpdateActivity::class.java),
								PendingIntent.FLAG_UPDATE_CURRENT))
				.setAutoCancel(false)
		if (ingressUtil.getIngressPackageName() != "null")
		{
			builder.addAction(
					R.drawable.ic_arrow_forward,
					res.getString(R.string.action_open_ingress),
					PendingIntent.getActivity(
							context,
							0,
							context.packageManager.getLaunchIntentForPackage(ingressUtil.getIngressPackageName()),
							PendingIntent.FLAG_UPDATE_CURRENT))
		}
		notify(context, builder.build())
	}

	private fun notify(context: Context, notification: Notification)
	{
		val nm = context
				.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
		nm.notify(NOTIFICATION_TAG, 0, notification)
	}

	fun cancel(context: Context)
	{
		val nm = context
				.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
		nm.cancel(NOTIFICATION_TAG, 0)
	}
}
