package com.janyo.mufgrecorder.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.janyo.mufgrecorder.R
import com.janyo.mufgrecorder.`class`.MUFG
import com.janyo.mufgrecorder.adapter.MUFGAdapter
import com.janyo.mufgrecorder.util.FileUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity()
{
	private var fileUtil: FileUtil? = null

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setSupportActionBar(toolbar)

		fileUtil = FileUtil(this)

		fab.setOnClickListener {
			startActivity(Intent(this, EditMUFGActivity::class.java))
		}

		val list: ArrayList<MUFG> = ArrayList()
		list.clear()
		list.addAll(fileUtil!!.getObjects("MUFG"))

		recyclerView.layoutManager = LinearLayoutManager(this)
		recyclerView.adapter = MUFGAdapter(list, this)
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean
	{
		menuInflater.inflate(R.menu.menu_main, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean
	{
		when (item.itemId)
		{
			R.id.action_settings ->
			{
				startActivity(Intent(this, SettingsActivity::class.java))
				return true
			}
			else -> return super.onOptionsItemSelected(item)
		}
	}
}
