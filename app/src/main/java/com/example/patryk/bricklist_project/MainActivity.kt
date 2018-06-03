package com.example.patryk.bricklist_project

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.database.SQLException
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL


class MainActivity : AppCompatActivity() {

    var URL_PREFIX = "http://fcds.cs.put.poznan.pl/MyWeb/BL/"
    val REQUEST_CODE_SETTINGS = 10000
    val REQUEST_CODE_NEW_SET = 10001
    val REQUEST_CODE_SET = 10002
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    var myList = ArrayList<String>()

    var myDbHelper = DataBaseHelper(this)

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




        try {
            myDbHelper.createDataBase()
        } catch (ioe: IOException) {
            throw Error("Unable to create database")
        }


        try {
            myDbHelper.openDataBase()
        } catch (sqle: SQLException) {
            throw sqle
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

        /*
        var myval = myDbHelper.testme();
        Toast.makeText(this, myval, Toast.LENGTH_LONG).show()
        */
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
                    downloadXML(number)

                }
            }
        }
    }


    fun downloadXML(number : String) {
        val xd = XMLDownloader()
        xd.execute(number)
    }

    /*fun loadData() {
        val filename = "615.xml"
        val path = filesDir
        val inDir = File(path, "XML")

    }*/
    private inner class XMLDownloader: AsyncTask<String, Int, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Log.e("XML", "DownloadXML Complete")
            //loadData
            //showData
        }

        override fun doInBackground(vararg params: String?): String {
            try {
                //Log.e("XML", "$filesDir/XML, ${params.toList()}")
                var number = params[0]
                val url = URL("$URL_PREFIX$number.xml")
                val connection = url.openConnection()
                connection.connect()
                //val lengthOfFile = connection.contentLength
                val isStream = url.openStream()
                val testDirectory = File("$filesDir/XML")

                if (!testDirectory.exists()) testDirectory.mkdir()
                val fos = FileOutputStream("$testDirectory/$number.xml")
                val data = ByteArray(1024)
                var count = 0
                count = isStream.read(data)
                while (count != -1) {
                   fos.write(data,0, count)
                    count = isStream.read(data)
                }
                isStream.close()
                fos.close()
            } catch (e: MalformedURLException) {
                return "Malformed URL"
            } catch (e: FileNotFoundException) {
                return "File not found"
            } catch (e: IOException) {
                return "IO Exception"
            }
            return "success"
        }
    }
}
