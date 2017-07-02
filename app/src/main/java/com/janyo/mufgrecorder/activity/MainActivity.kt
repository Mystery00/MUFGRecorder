package com.janyo.mufgrecorder.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.janyo.mufgrecorder.R
import com.janyo.mufgrecorder.`class`.MUFG
import com.janyo.mufgrecorder.adapter.MUFGAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity()
{

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setSupportActionBar(toolbar)

		fab.setOnClickListener { view ->
			Snackbar.make(view, "添加红桶", Snackbar.LENGTH_LONG)
					.show()
		}

		val list:ArrayList<MUFG> = ArrayList()
		list.add(MUFG("1"))
		list.add(MUFG("2"))
		list.add(MUFG("3"))
		list.add(MUFG("4"))

		recyclerView.layoutManager=LinearLayoutManager(this)
		recyclerView.adapter=MUFGAdapter(list,this)
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean
	{
		menuInflater.inflate(R.menu.main, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean
	{
		when (item.itemId)
		{
			R.id.action_settings -> return true
			else -> return super.onOptionsItemSelected(item)
		}
	}
}