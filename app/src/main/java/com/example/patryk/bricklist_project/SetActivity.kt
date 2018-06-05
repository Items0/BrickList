package com.example.patryk.bricklist_project

import android.database.SQLException
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_set.*
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory



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

    fun xmlExport(v: View) {
        val docBuilder: DocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val doc: Document = docBuilder.newDocument()

        val rootElement: Element = doc.createElement("INVENTORY")

        Log.e("Size", myList.size.toString())
        for (el in myList) {
            Log.e("for", el.itemType + " " + el.id + " " + el.color + " " + el.qty)
            if (el.qtyInStore != el.qty) {
                val itemElement: Element = doc.createElement("ITEM")
                val itemTypeElement: Element = doc.createElement("ITEMTYPE")
                val itemIDElement: Element = doc.createElement("ITEMID")
                val colorElement: Element = doc.createElement("COLOR")
                val qtyfilledElement: Element = doc.createElement("QTYFILLED")

                itemTypeElement.appendChild(doc.createTextNode(el.itemType))
                itemIDElement.appendChild(doc.createTextNode(el.id))
                colorElement.appendChild(doc.createTextNode(el.color))
                qtyfilledElement.appendChild(doc.createTextNode((el.qty.toInt() - el.qtyInStore.toInt()).toString()))
                itemElement.appendChild(itemTypeElement)
                itemElement.appendChild(itemIDElement)
                itemElement.appendChild(colorElement)
                itemElement.appendChild(qtyfilledElement)
                rootElement.appendChild(itemElement)
            }
        }
        doc.appendChild(rootElement)
        val transformer: Transformer = TransformerFactory.newInstance().newTransformer()

        transformer.setOutputProperty(OutputKeys.INDENT, "yes")
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")

        transformer.transform(DOMSource(doc), StreamResult(File("$filesDir/xml-test-output.xml")))

        //Toast.makeText(this, "XML Complete  " + Environment.getExternalStorageDirectory().absolutePath, Toast.LENGTH_LONG).show()

        

    }

}
