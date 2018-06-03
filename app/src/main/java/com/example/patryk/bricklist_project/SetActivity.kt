package com.example.patryk.bricklist_project

import android.content.Intent
import android.database.SQLException
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_set.*

class SetActivity : AppCompatActivity() {

    var myList = ArrayList<Pair<String, String>>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set)
        var myDbHelper = DataBaseHelper(this)// = (application as MainActivity).myDbHelper // napraw to
        try {
            myDbHelper.openDataBase()
        } catch (sqle: SQLException) {
            throw sqle
        }
        val extras = intent.extras ?: return
        var number = extras.getString("number")
        var name = extras.getString("name")
        //myList = extras.getStringArrayList("list")
        numberTextView.text = number
        nameTextView.text = name

        myList = myDbHelper.loadInventoriesParts(number)

        viewManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        viewAdapter = RecyclerViewAdapterSet(myList) { position: Int ->
            //val i = Intent(this, SetActivity::class.java)
            //i.putExtra("number", myList[position].first)
            //i.putExtra("name",  myList[position].second)
            //var list = myDbHelper.loadInventoriesParts(myList[position].first)
            //i.putExtra("list", list)
            //startActivityForResult(i, REQUEST_CODE_SET)
        }


        recyclerView = findViewById<RecyclerView>(R.id.mySetRecyclerView).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }
        //myList = myDbHelper.firstLoadInventories()
        /*
        Log.e("Przed", "Przed wypisaniem")
        for (el in myList) {
            Log.e("for", el.first)
           // Toast.makeText(this, el.first, Toast.LENGTH_LONG).show()
        }*/

    }
}
