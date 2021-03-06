package com.janyo.mufgrecorder.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import com.janyo.mufgrecorder.R
import com.janyo.mufgrecorder.`class`.MUFG
import com.janyo.mufgrecorder.adapter.MUFGItemsAdapter
import com.janyo.mufgrecorder.util.FileUtil
import com.janyo.mufgrecorder.util.IngressUtil

import kotlinx.android.synthetic.main.activity_edit_mufg.*
import kotlinx.android.synthetic.main.content_edit_mufg.*


class EditMUFGActivity : AppCompatActivity()
{
	private val INTENT_TAG = "MUFG"
	private var ingressUtil: IngressUtil? = null
	private var fileUtil: FileUtil? = null
	private var items: Array<String>? = null
	private var checked: BooleanArray? = null
	private var content = ArrayList<HashMap<String, Any>>()
	private var adapter: MUFGItemsAdapter? = null
	private var mufg: MUFG? = null
	private var menu: Menu? = null
	private var isNew = true

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_edit_mufg)
		setSupportActionBar(toolbar)

		supportActionBar!!.setDisplayHomeAsUpEnabled(true)
		toolbar.setNavigationOnClickListener { finish() }

		ingressUtil = IngressUtil(this)
		fileUtil = FileUtil(this)

		items = resources.getStringArray(R.array.ingress_items)
		checked = kotlin.BooleanArray(items!!.size)
		for (i in items!!.indices)
		{
			checked!![i] = false
		}

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
				if (mufg == null || !fileUtil!!.checkMUFGName(mufg!!.MUFGID, getString(R.string.mufg_dir)))
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
			content.clear()
			content.addAll(mufg!!.content)
		}

		recyclerView.layoutManager = LinearLayoutManager(this)
		adapter = MUFGItemsAdapter(content)
		val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT)
		{
			override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
								target: RecyclerView.ViewHolder): Boolean
			{
				return false
			}

			override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int)
			{
				val position = viewHolder.adapterPosition
				content.removeAt(position)
				adapter!!.notifyItemRemoved(position)
			}
		}
		ItemTouchHelper(callback).attachToRecyclerView(recyclerView)
		recyclerView.adapter = adapter

		fab.setOnClickListener {
			for (i in items!!.indices)
			{
				checked!![i] = false
			}
			AlertDialog.Builder(this)
					.setTitle(R.string.title_check_items_of_new_mufg)
					.setMultiChoiceItems(items, checked, { _, position, checked ->
						this.checked!![position] = checked
					})
					.setPositiveButton(android.R.string.ok, { _, _ ->
						content = ingressUtil!!.ConvertArrayToList(checked!!, items!!, content)
						adapter!!.notifyDataSetChanged()
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
				if (adapter!!.list.sumBy { it["number"] as Int } <= 100)
				{
					ingressUtil!!.checkContent(content, mufg!!)
					fileUtil!!.saveObject(mufg!!, mufg!!.MUFGID, getString(R.string.mufg_dir))
					Snackbar.make(coordinatorLayout, R.string.hint_mufg_saved, Snackbar.LENGTH_SHORT)
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
				else
				{
					Snackbar.make(coordinatorLayout, R.string.hint_out_of_number, Snackbar.LENGTH_SHORT)
							.show()
				}
				return true
			}
			R.id.action_history ->
			{
				val view = LayoutInflater.from(this).inflate(R.layout.dialog_show_history, LinearLayout(this), false)
				val text_info: TextView = view.findViewById(R.id.mufg_info)
				text_info.text = ingressUtil!!.getInfoFromMUFG(mufg!!.MUFGID)
				AlertDialog.Builder(this)
						.setTitle(R.string.title_show_history)
						.setView(view)
						.setPositiveButton(android.R.string.ok, null)
						.show()
				return true
			}
			R.id.action_edit_title ->
			{
				val view = LayoutInflater.from(this).inflate(R.layout.text_input_layout, TextInputLayout(this), false)
				val text_input: TextInputLayout = view.findViewById(R.id.text_input)
				text_input.editText!!.setText(mufg!!.MUFGID)
				var newName = ""
				text_input.hint = getString(R.string.title_set_mufg_id)
				val builder: AlertDialog.Builder = AlertDialog.Builder(this)
				builder.setTitle(R.string.title_edit_mufg)
				builder.setView(view)
				builder.setPositiveButton(android.R.string.ok, { _, _ ->
					newName = text_input.editText!!.text.toString()
				})
				builder.setOnDismissListener {
					if (newName == "" || !fileUtil!!.checkMUFGName(newName, getString(R.string.mufg_dir)))
					{
						Snackbar.make(coordinatorLayout, R.string.hint_id_invalid, Snackbar.LENGTH_SHORT)
								.show()
					}
					else
					{
						if (fileUtil!!.changeMUFGName(mufg, getString(R.string.mufg_dir), newName))
						{
							Snackbar.make(coordinatorLayout, String.format(getString(R.string.hint_mufg_renamed), newName), Snackbar.LENGTH_SHORT)
									.show()
						}
						else
						{
							Snackbar.make(coordinatorLayout, R.string.hint_id_invalid, Snackbar.LENGTH_SHORT)
									.show()
						}
					}
				}
				builder.show()
				return true
			}
			else -> return super.onOptionsItemSelected(item)
		}
	}
}
