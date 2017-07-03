package com.janyo.mufgrecorder.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import com.janyo.mufgrecorder.R
import com.janyo.mufgrecorder.`class`.MUFG
import com.janyo.mufgrecorder.adapter.MUFGItemsAdapter
import com.janyo.mufgrecorder.util.FileUtil
import com.janyo.mufgrecorder.util.IngressUtil

import kotlinx.android.synthetic.main.activity_edit_mufg.*
import kotlinx.android.synthetic.main.content_edit_mufg.*
import java.util.*


class EditMUFGActivity : AppCompatActivity()
{
	private val INTENT_TAG = "MUFG"
	private var ingressUtil: IngressUtil? = null
	private var fileUtil: FileUtil? = null
	private var items: Array<String>? = null
	private var checkedMap = HashMap<String, Boolean>()//选择的物品列表
	private var adapter: MUFGItemsAdapter? = null
	private var mufg: MUFG? = null
	private var menu: Menu? = null
	private var isNew = true

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_edit_mufg)
		setSupportActionBar(toolbar)

		ingressUtil = IngressUtil(this)
		fileUtil = FileUtil(this)

		if (intent.getBundleExtra(INTENT_TAG) == null)
		{
			val view = LayoutInflater.from(this).inflate(R.layout.text_input_layout, TextInputLayout(this), false)
			val text_input: TextInputLayout = view.findViewById(R.id.text_input)
			text_input.hint = getString(R.string.title_set_mufg_id)
			val builder: AlertDialog.Builder = AlertDialog.Builder(this)
			builder.setTitle(R.string.title_create_new_mufg)
			builder.setView(view)
			builder.setPositiveButton(android.R.string.ok, { _, _ ->
				mufg = MUFG(text_input.editText!!.text.toString())
			})
			builder.setOnDismissListener {
				if (mufg == null)
				{
					Snackbar.make(coordinatorLayout, R.string.hint_id_invalid, Snackbar.LENGTH_SHORT)
							.addCallback(object : Snackbar.Callback()
							{
								override fun onDismissed(transientBottomBar: Snackbar?, event: Int)
								{
									if (event != Snackbar.Callback.DISMISS_EVENT_ACTION)
									{
										finish()
									}
								}
							})
							.show()
				}
			}
			builder.show()
		}
		else
		{
			isNew = false
			@Suppress("CAST_NEVER_SUCCEEDS")
			mufg = intent.getBundleExtra(INTENT_TAG).getSerializable(INTENT_TAG) as MUFG
			recyclerView.layoutManager = LinearLayoutManager(this)
			adapter = MUFGItemsAdapter(this, mufg!!.contentMap)
			recyclerView.adapter = adapter
		}

		items = resources.getStringArray(R.array.ingress_items)

		for (temp in items!!)
		{
			checkedMap.put(temp, false)
		}

		fab.setOnClickListener {
			AlertDialog.Builder(this)
					.setTitle(R.string.title_check_items_of_new_mufg)
					.setMultiChoiceItems(items, null, { _, position, checked ->
						checkedMap[items!![position]] = checked
					})
					.setPositiveButton(android.R.string.ok, { _, _ ->
						recyclerView.layoutManager = LinearLayoutManager(this)
						adapter = MUFGItemsAdapter(this, ingressUtil!!.CheckItems(checkedMap))
						recyclerView.adapter = adapter
						menu!!.findItem(R.id.action_done).isVisible = true
					})
					.show()
		}
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean
	{
		menuInflater.inflate(R.menu.menu_edit, menu)
		this.menu = menu
		if (!isNew)
		{
			menu.findItem(R.id.action_done).isVisible = true
		}
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean
	{
		when (item.itemId)
		{
			R.id.action_done ->
			{
				mufg!!.contentMap = ingressUtil!!.ConvertItemsFormat(adapter!!.getList())
				fileUtil!!.saveObject(mufg!!, mufg!!.MUFGID, "MUFG")
				return true
			}
			else -> return super.onOptionsItemSelected(item)
		}
	}

}
