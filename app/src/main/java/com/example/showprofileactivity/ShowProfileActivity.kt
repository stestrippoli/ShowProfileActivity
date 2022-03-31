package com.example.showprofileactivity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import org.json.JSONObject
import kotlin.collections.Map as Map


class ShowProfileActivity : AppCompatActivity() {
    var fullName: String? = "Full Nameeee"
    var nickname: String? = "Nickname"
    var email: String? = "email"
    var location: String? = "location"
    var skills: Map<String, String> = emptyMap()


    private fun populateBoxes(){
        val imgbox = findViewById<ImageView>(R.id.profilepic)
        val namebox = findViewById<TextView>(R.id.name)
        val nicknamebox = findViewById<TextView>(R.id.nickname)
        val emailbox = findViewById<TextView>(R.id.email)
        val locationbox = findViewById<TextView>(R.id.location)
        val sharedPref =this.getPreferences(Context.MODE_PRIVATE) ?: return
        val myJSON = JSONObject(
            sharedPref.getString("profile", """{"fullname":"Default Name","nickname":"default nickname","email":"default@anna.com","location":"defaultlocation"}""")
        )

        println(myJSON)
        namebox.text = myJSON.getString("fullname")
        locationbox.text = myJSON.getString("location")
        emailbox.text = myJSON.getString("email")
        nicknamebox.text = myJSON.getString("nickname")
        imgbox.setImageResource(R.drawable.propic)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_profile)
        populateBoxes()
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
        if (result.resultCode == Activity.RESULT_OK
            && result.getData()!=null) {

            val data: Intent = result.getData()!!
            /*
            val namebox = findViewById<TextView>(R.id.name)
            val nicknamebox = findViewById<TextView>(R.id.nickname)
            val emailbox = findViewById<TextView>(R.id.email)
            val locationbox = findViewById<TextView>(R.id.location)
            val img = findViewById<ImageView>(R.id.profilepic)

            namebox.text = data.getStringExtra("showprofileactivity.FULL_NAME")
            locationbox.text = data.getStringExtra("showprofileactivity.LOCATION")
            emailbox.text = data.getStringExtra("showprofileactivity.EMAIL")
            nicknamebox.text = data.getStringExtra("showprofileactivity.NICKNAME")
            img.setImageBitmap(data.getParcelableExtra<Bitmap>("showprofileactivity.IMAGE"))
            */
            val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
            with (sharedPref.edit()) {
                val profile = JSONObject()
                profile.put("fullname", data.getStringExtra("showprofileactivity.FULL_NAME"))
                profile.put("nickname", data.getStringExtra("showprofileactivity.NICKNAME"))
                profile.put("email", data.getStringExtra("showprofileactivity.EMAIL"))
                profile.put("location", data.getStringExtra("showprofileactivity.LOCATION"))
                putString("profile", profile.toString())
                println(profile)
                apply()
            }
            populateBoxes()
            println(sharedPref.all)


        }
    }

}