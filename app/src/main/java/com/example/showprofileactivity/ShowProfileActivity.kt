package com.example.showprofileactivity

import android.app.Activity
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts


class ShowProfileActivity : AppCompatActivity() {
    var photo = "@drawable/propic.jpg"
    var fullName: String? = "Full Nameeee"
    var nickname: String? = "Nickname"
    var email: String? = "email"
    var location: String? = "location"
    var skills: Map<String, String> = emptyMap()


    private fun populate(){
        val imgbox = findViewById<ImageView>(R.id.profilepic)
        val namebox = findViewById<TextView>(R.id.name)
        val nicknamebox = findViewById<TextView>(R.id.nickname)
        val emailbox = findViewById<TextView>(R.id.email)
        val locationbox = findViewById<TextView>(R.id.location)
        namebox.text = fullName
        locationbox.text = location
        emailbox.text = email
        nicknamebox.text = nickname
        imgbox.setImageResource(R.drawable.propic)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_profile)
        populate()

    }

    private fun editProfile(){
        val i = Intent(this, EditProfileActivity::class.java)
        val b = Bundle()
        b.putString("showprofileactivity.FULL_NAME", findViewById<TextView>(R.id.name).text.toString())
        b.putString("showprofileactivity.NICKNAME", findViewById<TextView>(R.id.nickname).text.toString())
        b.putString("showprofileactivity.EMAIL", findViewById<TextView>(R.id.email).text.toString())
        b.putString("showprofileactivity.LOCATION", findViewById<TextView>(R.id.location).text.toString())
        i.putExtras(b)

        launcher.launch(i)



    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.custom_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onContextItemSelected(item)
        editProfile()
        return true
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        println(result)
        if (result.resultCode == Activity.RESULT_OK
            && result.getData()!=null) {
            val data: Intent = result.getData()!!
            val namebox = findViewById<TextView>(R.id.name)
            val nicknamebox = findViewById<TextView>(R.id.nickname)
            val emailbox = findViewById<TextView>(R.id.email)
            val locationbox = findViewById<TextView>(R.id.location)
            namebox.text = data.getStringExtra("showprofileactivity.FULL_NAME")
            locationbox.text = data.getStringExtra("showprofileactivity.LOCATION")
            emailbox.text = data.getStringExtra("showprofileactivity.EMAIL")
            nicknamebox.text = data.getStringExtra("showprofileactivity.NICKNAME")
        }
    }

}