package com.janyo.mufgrecorder.activity

import android.app.TimePickerDialog
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import com.janyo.mufgrecorder.R
import com.mystery0.tools.Logs.Logs
import java.util.*

@Suppress("DEPRECATION")
class SettingsActivity : PreferenceActivity()
{
	private val TAG = "SettingsActivity"
	private var toolbar: Toolbar? = null
	private var setNotificationTime: Preference? = null

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		addPreferencesFromResource(R.xml.settings_preference)
		toolbar!!.title = title
		initialization()
		monitor()
	}

	private fun initialization()
	{
		setNotificationTime = findPreference(getString(R.string.key_set_notification_time))
	}

	private fun monitor()
	{
		setNotificationTime!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
			val calender = Calendar.getInstance()
			TimePickerDialog(this@SettingsActivity, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
				Logs.i(TAG, "monitor: " + hour + minute)
			}, calender.get(Calendar.HOUR_OF_DAY), calender.get(Calendar.MINUTE), true)
					.show()
			false
		}
	}

	override fun setContentView(layoutResID: Int)
	{
		val contentView = LayoutInflater.from(this).inflate(R.layout.activity_settings, LinearLayout(this), false) as ViewGroup
		toolbar = contentView.findViewById(R.id.toolbar)
		toolbar!!.setNavigationOnClickListener {
			finish()
		}

		val contentWrapper: ViewGroup = contentView.findViewById(R.id.content_wrapper)
		LayoutInflater.from(this).inflate(layoutResID, contentWrapper, true)

		window.setContentView(contentView)
	}
}