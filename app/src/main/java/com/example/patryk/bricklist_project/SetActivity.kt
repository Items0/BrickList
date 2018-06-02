package com.example.patryk.bricklist_project

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_set.*
import kotlinx.android.synthetic.main.activity_settings.*

class SetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set)

        val extras = intent.extras ?: return
        nameTextView.text = extras.getString("itemDto")
        numberTextView.text = extras.getInt("position").toString()
    }
}
