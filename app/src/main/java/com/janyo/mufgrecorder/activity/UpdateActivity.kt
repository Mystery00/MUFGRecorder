package com.janyo.mufgrecorder.activity

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.janyo.mufgrecorder.R
import com.janyo.mufgrecorder.`class`.MUFG
import com.janyo.mufgrecorder.adapter.MUFGAdapter
import com.janyo.mufgrecorder.adapter.MUFGItemsUpdateAdapter
import com.janyo.mufgrecorder.util.CheckNotification
import com.janyo.mufgrecorder.util.FileUtil
import com.janyo.mufgrecorder.util.IngressUtil
import com.mystery0.tools.Logs.Logs

import kotlinx.android.synthetic.main.activity_update.*
import kotlinx.android.synthetic.main.content_main.*

class UpdateActivity : AppCompatActivity()
{
	private val TAG = "UpdateActivity"
	private var fileUtil: FileUtil? = null
	private var ingressUtil: IngressUtil? = null
	private var adapter: MUFGAdapter? = null
	private val list: ArrayList<MUFG> = ArrayList()

	private var myHandler: MyHandler? = null

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_update)
		setSupportActionBar(toolbar)

		fileUtil = FileUtil(this)
		ingressUtil = IngressUtil(this)

		adapter = MUFGAdapter(list, this)
		adapter!!.setOnClickListener(object : MUFGAdapter.OnClickListener
		{
			override fun onClick(position: Int, mufg: MUFG)
			{
				val oldList = ingressUtil!!.cloneList(mufg.content)
				val adapter = MUFGItemsUpdateAdapter(mufg.content)
				val recyclerView = RecyclerView(this@UpdateActivity)
				recyclerView.setPadding(50, 20, 50, 0)
				recyclerView.layoutManager = LinearLayoutManager(this@UpdateActivity)
				recyclerView.adapter = adapter
				AlertDialog.Builder(this@UpdateActivity)
						.setTitle(R.string.title_update_items_number)
						.setView(recyclerView)
						.setPositiveButton(android.R.string.ok, { _, _ ->
							Logs.i(TAG, "onClick: " + oldList.toString())
							Logs.i(TAG, "onClick: " + adapter.getItemsList().toString())
						})
						.show()
			}
		})
		myHandler = MyHandler(adapter!!, swipeRefreshLayout)
		recyclerView.layoutManager = LinearLayoutManager(this)
		recyclerView.adapter = adapter

		swipeRefreshLayout.setColorSchemeResources(
				android.R.color.holo_blue_light,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light)
		swipeRefreshLayout.setOnRefreshListener { refresh() }

		swipeRefreshLayout.isRefreshing = true
		refresh()
	}

	private fun refresh()
	{
		Thread(Runnable {
			list.clear()
			list.addAll(fileUtil!!.getObjects(getString(R.string.mufg_dir)))
			val message = Message()
			message.what = 1
			myHandler!!.sendMessage(message)
		}).start()
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean
	{
		menuInflater.inflate(R.menu.menu_update, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean
	{
		when (item.itemId)
		{
			R.id.action_update ->
			{
				CheckNotification.cancel(this)
				Logs.i(TAG, "onOptionsItemSelected: 更新数据")
				return true
			}
			else -> return super.onOptionsItemSelected(item)
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
}
