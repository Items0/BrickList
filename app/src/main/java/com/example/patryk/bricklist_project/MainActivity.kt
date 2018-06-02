package com.example.patryk.bricklist_project

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    var URL_PREFIX = "http://fcds.cs.put.poznan.pl/MyWeb/BL/"
    val REQUEST_CODE_SETTINGS = 10000
    val REQUEST_CODE_NEW_SET = 10001
    val REQUEST_CODE_SET = 10002
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    var myList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for (i in 0..20) myList.add("Set #$i")
        viewManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        viewAdapter = RecyclerViewAdapter(myList) { itemDto: String, position: Int ->
            val i = Intent(this, SetActivity::class.java)
            i.putExtra("itemDto", itemDto)
            i.putExtra("position", position)
            startActivityForResult(i, REQUEST_CODE_SET)
        }


        recyclerView = findViewById<RecyclerView>(R.id.myRecyclerView).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }

    }

    fun addNewSet(v: View) {
        val i = Intent(this, addSetActivity::class.java)
        startActivityForResult(i, REQUEST_CODE_NEW_SET)

        //myList.add("Ends")
        //recyclerView.adapter.notifyItemInserted(myList.size - 1);
        ///*recyclerView.adapter.notifyDataSetChanged();*/
    }

    fun settingsActivity(v: View) {
        val i = Intent(this, SettingsActivity::class.java)
        i.putExtra("urlprefix", URL_PREFIX)
        startActivityForResult(i, REQUEST_CODE_SETTINGS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                        if (data != null) {
                            if (data.hasExtra("urlprefix")) {
                                URL_PREFIX = data.extras.getString("urlprefix")
                            }
                        }
                    }
                }
        if (requestCode == REQUEST_CODE_NEW_SET) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    var name = data.extras.getString("name")
                    var number = data.extras.getString("number")
                }
            }
        }
    }
}
