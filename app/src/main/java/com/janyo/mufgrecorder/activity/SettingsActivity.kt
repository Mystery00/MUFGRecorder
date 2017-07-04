package com.janyo.mufgrecorder.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceActivity
import android.preference.RingtonePreference
import android.preference.SwitchPreference
import android.support.design.widget.Snackbar
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.janyo.mufgrecorder.R
import com.janyo.mufgrecorder.service.CheckMUFGService
import com.janyo.mufgrecorder.util.Settings
import com.janyo.mufgrecorder.util.TimeUtil
import java.util.*

@Suppress("DEPRECATION")
class SettingsActivity : PreferenceActivity()
{
	private var settings: Settings? = null
	private var toolbar: Toolbar? = null
	private var coordinatorLayout: View? = null
	private var isNotify: SwitchPreference? = null
	private var setNotificationTime: Preference? = null
	private var notificationRingtone: RingtonePreference? = null
	private var notificationVibrate: SwitchPreference? = null
	private var hour = 0
	private var minute = 0

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		settings = Settings(this)
		val time = settings!!.getNotificationTime()
		hour = time.split(":")[0].toInt()
		minute = time.split(":")[1].toInt()
		addPreferencesFromResource(R.xml.settings_preference)
		toolbar!!.title = title
		initialization()
		monitor()
	}

	private fun initialization()
	{
		isNotify = findPreference(getString(R.string.key_notification)) as SwitchPreference
		setNotificationTime = findPreference(getString(R.string.key_set_notification_time))
		notificationRingtone = findPreference(getString(R.string.key_notification_ringtone)) as RingtonePreference
		notificationVibrate = findPreference(getString(R.string.key_notification_vibrate)) as SwitchPreference

		isNotify!!.isChecked = settings!!.getNotify()
		if (settings!!.getNotify())
		{
			setNotificationTime!!.summary = String.format(getString(R.string.summary_set_notification_time), TimeUtil.timeFormat(hour.toString() + ":" + minute.toString()))
			notificationRingtone!!.summary = if (settings!!.getNotificationRingtone() == "null") null else RingtoneManager.getRingtone(this, Uri.parse(settings!!.getNotificationRingtone())).getTitle(this)
			notificationRingtone!!.setDefaultValue(Uri.parse(settings!!.getNotificationRingtone()))
			notificationVibrate!!.isChecked = settings!!.getNotificationVibrate()
		}
		else
		{
			setNotificationTime!!.isEnabled = false
			notificationRingtone!!.isEnabled = false
			notificationVibrate!!.isEnabled = false
		}
	}

	private fun monitor()
	{
		isNotify!!.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, _ ->
			val isNotify = !isNotify!!.isChecked
			if (isNotify)
			{
				setNotificationTime!!.isEnabled = true
				notificationRingtone!!.isEnabled = true
				notificationVibrate!!.isEnabled = true
				settings!!.setNotification(true)
				showTimePicker()
			}
			else
			{
				setNotificationTime!!.isEnabled = false
				notificationRingtone!!.isEnabled = false
				notificationVibrate!!.isEnabled = false
				setNotificationTime!!.summary = null
				notificationRingtone!!.summary = null
			}
			true
		}
		setNotificationTime!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
			showTimePicker()
			false
		}
		notificationRingtone!!.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
			notificationRingtone!!.summary = if (newValue == "") getString(R.string.summary_notification_mute) else RingtoneManager.getRingtone(this, Uri.parse(newValue.toString())).getTitle(this)
			settings!!.setNotificationRingtone(if (newValue.toString() == "") "null" else newValue.toString())
			false
		}
		notificationVibrate!!.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, _ ->
			val isVibrate = !notificationVibrate!!.isChecked
			settings!!.setNotificationVibrate(isVibrate)
			true
		}
	}

	override fun setContentView(layoutResID: Int)
	{
		val contentView = LayoutInflater.from(this).inflate(R.layout.activity_settings, LinearLayout(this), false) as ViewGroup
		toolbar = contentView.findViewById(R.id.toolbar)
		coordinatorLayout = contentView.findViewById(R.id.coordinatorLayout)
		toolbar!!.setNavigationOnClickListener {
			finish()
		}

		val contentWrapper: ViewGroup = contentView.findViewById(R.id.content_wrapper)
		LayoutInflater.from(this).inflate(layoutResID, contentWrapper, true)

		window.setContentView(contentView)
	}

	private fun showTimePicker()
	{
		if (hour == 0 && minute == 0)
		{
			val calender = Calendar.getInstance()
			hour = calender.get(Calendar.HOUR_OF_DAY)
			minute = calender.get(Calendar.MINUTE)
		}
		TimePickerDialog(this@SettingsActivity, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
			this.hour = hour
			this.minute = minute
			val setTime = hour.toString() + ":" + minute.toString()
			settings!!.setNotificationTime(setTime)
			setNotificationTime!!.summary = String.format(getString(R.string.summary_set_notification_time), TimeUtil.timeFormat(setTime))
			val time = TimeUtil.calculateTime(setTime)
			Snackbar.make(coordinatorLayout!!, String.format(getString(R.string.hint_set_notification, time, if (time > 1) "s" else "")), Snackbar.LENGTH_SHORT)
					.show()
			setAlarm()
		}, hour, minute, true)
				.show()
	}

	fun setAlarm()
	{
		val setTime = TimeUtil.getSetTime(TimeUtil.timeFormat(settings!!.getNotificationTime()))
		val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
		val intent = Intent()
		intent.setClass(this, CheckMUFGService::class.java)
		val pendingIntent = PendingIntent.getService(this, 0, intent, 0)
		alarmManager.set(AlarmManager.RTC_WAKEUP, setTime.time, pendingIntent)
	}
}