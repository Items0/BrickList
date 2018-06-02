package com.example.patryk.bricklist_project

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_add_set.*
import kotlinx.android.synthetic.main.activity_settings.*

class addSetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_set)
    }

    fun addSet(v: View) {
        val data = Intent()
        data.putExtra("number",  numberEditText.text.toString())
        data.putExtra("name",  nameEditText.text.toString())
        setResult(Activity.RESULT_OK, data)
        super.finish()
    }
}
