package com.example.showprofileactivity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.showprofileactivity.databinding.ActivityEditProfileBinding


class EditProfileActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        populate(getIntent())
        val btn = findViewById<ImageButton>(R.id.propic_e)
        registerForContextMenu(btn)
    }
    private fun populate(i: Intent){
       // val imgbox = findViewById<ImageView>(R.id.propic)
        println(i.extras)
        val namebox = findViewById<TextView>(R.id.name_e)
        val nicknamebox = findViewById<TextView>(R.id.nickname_e)
        val emailbox = findViewById<TextView>(R.id.email_e)
        val locationbox = findViewById<TextView>(R.id.location_e)
        namebox.text = i.getStringExtra("showprofileactivity.FULL_NAME")
        locationbox.text = i.getStringExtra("showprofileactivity.LOCATION")
        emailbox.text = i.getStringExtra("showprofileactivity.EMAIL")
        nicknamebox.text = i.getStringExtra("showprofileactivity.NICKNAME")
        //imgbox.setImageResource(R.drawable.propic)

    }
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.image_menu, menu)
    }


    override fun onBackPressed() {
        val i = Intent()
        val b = Bundle()
        b.putString("showprofileactivity.FULL_NAME", findViewById<EditText>(R.id.name_e).text.toString())
        b.putString("showprofileactivity.NICKNAME", findViewById<EditText>(R.id.nickname_e).text.toString())
        b.putString("showprofileactivity.EMAIL", findViewById<EditText>(R.id.email_e).text.toString())
        b.putString("showprofileactivity.LOCATION", findViewById<EditText>(R.id.location_e).text.toString())

        i.putExtras(b)
        println(i.extras)
        setResult(Activity.RESULT_OK, i)
        super.onBackPressed()



    }

}