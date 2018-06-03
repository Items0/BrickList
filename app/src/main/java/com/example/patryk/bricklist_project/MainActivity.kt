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
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory


class MainActivity : AppCompatActivity() {

    var URL_PREFIX = "http://fcds.cs.put.poznan.pl/MyWeb/BL/"
    val REQUEST_CODE_SETTINGS = 10000
    val REQUEST_CODE_NEW_SET = 10001
    val REQUEST_CODE_SET = 10002
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    var myList = ArrayList<Pair<String, String>>()

    var myDbHelper = DataBaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        myList = myDbHelper.firstLoadInventories()
        viewManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        viewAdapter = RecyclerViewAdapterMain(myList) { position: Int ->
            val i = Intent(this, SetActivity::class.java)
            i.putExtra("number", myList[position].first)
            i.putExtra("name",  myList[position].second)
            //var list = myDbHelper.loadInventoriesParts(myList[position].first)
            //i.putExtra("list", list)
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
                    downloadXML(number, name)
                }
            }
        }
    }


    fun downloadXML(number : String, name: String) {
        val xd = XMLDownloader(number, name)
        xd.execute()
    }

    fun loadData(number: String, name: String) {
        Log.e("Load","start Load")

        var brickList : MutableList<Brick> = mutableListOf()

        val filename = "$number.xml"
        val path = filesDir
        val inDir = File(path, "XML")
        if (inDir.exists()) {
            val file = File(inDir, filename)
            if (file.exists()) {
                val xmlDoc : Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
                xmlDoc.documentElement.normalize()
                val items: NodeList = xmlDoc.getElementsByTagName("ITEM")
                for (i in 0..items.length - 1) {
                    val itemNode: Node = items.item(i)
                    if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                        val elem = itemNode as Element
                        val children = elem.childNodes

                        var itemtype:String? = null
                        var itemID:String? = null
                        var qty:String? = null
                        var color:String? = null
                        var extra:String? = null
                        var alternate:String? = null
                        for (j in 0..children.length - 1) {
                            val node = children.item(j)
                            if (node is Element) {
                                when (node.nodeName) {
                                    "ITEMTYPE" -> {itemtype = node.textContent}
                                    "ITEMID" -> {itemID = node.textContent}
                                    "QTY" -> {qty = node.textContent}
                                    "COLOR" -> {color = node.textContent}
                                    "EXTRA" -> {extra = node.textContent}
                                    "ALTERNATE" -> {
                                        if (node.textContent == "N") {
                                            alternate = node.textContent
                                        }
                                    }
                                    else -> { }
                                }
                            }
                        }
                        Log.e("Load","$itemtype, $itemID, $qty, $color, $extra, $alternate")
                        val b = Brick(itemtype, itemID, qty, color, extra, alternate)
                        brickList.add(b)
                        //Log.e("Dodano", "${brickList?.size}")
                    }
                }
            }
        }
        //Log.e("Wyslano", "${brickList?.size}")
        myDbHelper.insertInventory(brickList, number, name)
    }
    private inner class XMLDownloader(number: String, name: String): AsyncTask<String, Int, String>() {
        var number = number
        var name = name

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Log.e("XML", "DownloadXML Complete")
            loadData(number, name)
            //showData
        }

        override fun doInBackground(vararg params: String?): String {
            try {
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
