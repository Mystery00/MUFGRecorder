package com.janyo.mufgrecorder.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.janyo.mufgrecorder.R
import com.janyo.mufgrecorder.`class`.MUFG
import com.janyo.mufgrecorder.adapter.MUFGAdapter
import com.janyo.mufgrecorder.util.CheckNotification
import com.janyo.mufgrecorder.util.FileUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity()
{
	private var fileUtil: FileUtil? = null
	private var adapter: MUFGAdapter? = null
	private val list: ArrayList<MUFG> = ArrayList()

	private var myHandler: MyHandler? = null

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setSupportActionBar(toolbar)

		fileUtil = FileUtil(this)
		CheckNotification.cancel(this)

		fab.setOnClickListener {
			startActivity(Intent(this, EditMUFGActivity::class.java))
		}

		adapter = MUFGAdapter(list, this)
		myHandler = MyHandler(adapter!!, swipeRefreshLayout)
		recyclerView.layoutManager = LinearLayoutManager(this)
		recyclerView.adapter = adapter

		swipeRefreshLayout.setColorSchemeResources(
				android.R.color.holo_blue_light,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light)
		swipeRefreshLayout.setOnRefreshListener { refresh() }
	}

	override fun onResume()
	{
		super.onResume()
		swipeRefreshLayout.isRefreshing = true
		refresh()
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
				CheckNotification.notify(this, 1)
				return true
			}
			else -> return super.onOptionsItemSelected(item)
		}
	}

	private fun refresh()
	{
		Thread(Runnable {
			list.clear()
			list.addAll(fileUtil!!.getObjects("MUFG"))
			val message = Message()
			message.what = 1
			myHandler!!.sendMessage(message)
		}).start()
	}
}

private class MyHandler(var adapter: MUFGAdapter,
						var swipeRefreshLayout: SwipeRefreshLayout) : Handler()
{
	override fun handleMessage(message: Message)
	{
		when (message.what)
		{
			1 ->
			{
				adapter.notifyDataSetChanged()
				swipeRefreshLayout.isRefreshing = false
			}
		}
	}
}
