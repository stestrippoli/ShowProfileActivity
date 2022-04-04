package com.example.showprofileactivity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import org.json.JSONObject


class ShowProfileActivity : AppCompatActivity() {

    private fun populateBoxes(){
        val imgbox = findViewById<ImageView>(R.id.profilepic)
        val namebox = findViewById<TextView>(R.id.name)
        val nicknamebox = findViewById<TextView>(R.id.nickname)
        val emailbox = findViewById<TextView>(R.id.email)
        val locationbox = findViewById<TextView>(R.id.location)
        val skillsbox = findViewById<TextView>(R.id.skills)
        val descbox = findViewById<TextView>(R.id.description)
        val sharedPref =this.getPreferences(Context.MODE_PRIVATE) ?: return
        val myJSON = JSONObject(
            sharedPref.getString("profile",
                """{"fullname":"Default Name","nickname":"Default nickname","email":"default@email.com","location":"Default location","skills":"Skill1 | Skill2 | Skill3","description": "Default description","img": "android.resource://com.example.showprofileactivity/${R.drawable.propic}"}""")
        )

        namebox.text = myJSON.getString("fullname")
        locationbox.text = myJSON.getString("location")
        emailbox.text = myJSON.getString("email")
        nicknamebox.text = myJSON.getString("nickname")
        skillsbox.text = myJSON.getString("skills")
        descbox.text = myJSON.getString("description")
        imgbox.setImageURI(myJSON.getString("img").toUri())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_profile)
        populateBoxes()
    }

    private fun editProfile(){
        val i = Intent(this, EditProfileActivity::class.java)
        val b = Bundle()
        val sharedPref =this.getPreferences(Context.MODE_PRIVATE) ?: return
        val myJSON = JSONObject(
            sharedPref.getString("profile",
                """{"fullname":"Default Name","nickname":"Default nickname","email":"default@email.com","location":"Default location","skills":"Skill1 | Skill2 | Skill3","description": "Default description","img": "android.resource://com.example.showprofileactivity/${R.drawable.propic}"}""")
        )
        b.putString("showprofileactivity.FULL_NAME", findViewById<TextView>(R.id.name).text.toString())
        b.putString("showprofileactivity.NICKNAME", findViewById<TextView>(R.id.nickname).text.toString())
        b.putString("showprofileactivity.EMAIL", findViewById<TextView>(R.id.email).text.toString())
        b.putString("showprofileactivity.LOCATION", findViewById<TextView>(R.id.location).text.toString())
        b.putString("showprofileactivity.SKILLS", findViewById<TextView>(R.id.skills).text.toString())
        b.putString("showprofileactivity.DESCRIPTION", findViewById<TextView>(R.id.description).text.toString())
        b.putString("showprofileactivity.IMG", myJSON.getString("img").toString())
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
        if (result.resultCode == Activity.RESULT_OK
            && result.data!=null) {

            val data: Intent = result.data!!
            val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
            with (sharedPref.edit()) {
                val profile = JSONObject()
                profile.put("fullname", data.getStringExtra("showprofileactivity.FULL_NAME"))
                profile.put("nickname", data.getStringExtra("showprofileactivity.NICKNAME"))
                profile.put("email", data.getStringExtra("showprofileactivity.EMAIL"))
                profile.put("location", data.getStringExtra("showprofileactivity.LOCATION"))
                profile.put("skills", data.getStringExtra("showprofileactivity.SKILLS"))
                profile.put("description", data.getStringExtra("showprofileactivity.DESCRIPTION"))
                profile.put("img", data.getStringExtra("showprofileactivity.IMG"))
                putString("profile", profile.toString())
                apply()
            }
            populateBoxes()
        }
    }

}