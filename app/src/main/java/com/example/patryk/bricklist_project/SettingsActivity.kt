package com.example.patryk.bricklist_project

import android.app.Activity
import android.content.Intent
import android.database.SQLException
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    var myDbHelper = DataBaseHelper(this) // = (application as MainActivity).myDbHelper // napraw to
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val extras = intent.extras ?: return
        editText.setText(extras.getString("urlprefix"))

        try {
            myDbHelper.openDataBase()
        } catch (sqle: SQLException) {
            throw sqle
        }
    }

    override fun finish() {
        val data = Intent()
        data.putExtra("urlprefix",  editText.text.toString())
        setResult(Activity.RESULT_OK, data)
        myDbHelper.close()
        super.finish()
    }

    fun clearDB(v: View) {
        myDbHelper.clearDB()
        Toast.makeText(this, "Cleared", Toast.LENGTH_LONG).show()
    }
}
