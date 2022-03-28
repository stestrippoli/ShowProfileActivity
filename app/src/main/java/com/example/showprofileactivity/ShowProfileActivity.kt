package com.example.showprofileactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView

class ShowProfileActivity : AppCompatActivity() {
    var photo = null
    var fullName: String? = "Full Name"
    var nickname: String? = "Nickname"
    var email: String? = "email"
    var location: String? = "location"
    var skills: Map<String, String> = emptyMap()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        //val tv = findViewById<TextView>(R.id.textView)
        //tv.text = ""
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}