package com.example.patryk.bricklist_project

import android.database.SQLException
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_set.*

class SetActivity : AppCompatActivity() {

    var myList = ArrayList<Brick>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    var myDbHelper = DataBaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set)

        try {
            myDbHelper.openDataBase()
        } catch (sqle: SQLException) {
            throw sqle
        }
        val extras = intent.extras ?: return
        var number = extras.getString("number")
        var name = extras.getString("name")
        //myList = extras.getStringArrayList("list")
        brickNumberTextView.text = number
        nameTextView.text = name

        myList = myDbHelper.loadInventoriesParts(number)

        viewManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        viewAdapter = RecyclerViewAdapterSet(myList)

        //var blas = RecyclerViewAdapterSet(myList).valuesList // naprawTO

        recyclerView = findViewById<RecyclerView>(R.id.mySetRecyclerView).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }
    }

    override fun finish() {
        myDbHelper.modifyInventoriesParts(myList)
        super.finish()
    }
}
