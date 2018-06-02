package com.example.patryk.bricklist_project

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val extras = intent.extras ?: return
        editText.setText(extras.getString("urlprefix"))
    }

    override fun finish() {
        val data = Intent()
        data.putExtra("urlprefix",  editText.text.toString())
        setResult(Activity.RESULT_OK, data)
        super.finish()
    }
}
