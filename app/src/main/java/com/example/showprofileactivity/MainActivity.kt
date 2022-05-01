package com.example.showprofileactivity

import android.content.Context
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.showprofileactivity.databinding.ActivityMainBinding
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)



        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.action_profile
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)


        NavigationUI.setupWithNavController(binding.navView, navController)
        NavigationUI.setupActionBarWithNavController(
            this, navController, binding.drawerLayout)
    }



    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment_content_main)
        return NavigationUI.navigateUp(navController, binding.drawerLayout)
        return true
    }

/*
     fun getAdvertList(): List<TimeSlot> {
         val list = ArrayList<TimeSlot>()
         val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
         val jsonArray = JSONArray(
             //sharedPref.getString("list", "")

             """[
   {
      "id":"1",
      "data":{
         "title":"Adv 1",
         "description":"Default description",
         "location":"Default location",
         "duration":"Default duration",
         "date":"12-12-2001",
         "time":"12:00"
      }
   },
   {
      "id":"2",
      "data":{
         "title":"Adv 2",
         "description":"Default description",
         "location":"Default location",
         "duration":"Default duration",
         "date":"12-12-2001",
         "time":"12:00"
      }
   }
]"""
         )
         for (i in 0 until jsonArray.length()) {
             // ID
             val id = jsonArray.getJSONObject(i).getString("id")
             val title = jsonArray.getJSONObject(i).getString("title")
             val description = jsonArray.getJSONObject(i).getString("description")
             val location = jsonArray.getJSONObject(i).getString("location")
             val duration = jsonArray.getJSONObject(i).getString("duration")
             val date = jsonArray.getJSONObject(i).getString("date")
             val time = jsonArray.getJSONObject(i).getString("time")
             // Save data using your Model
             val advert = TimeSlot(title, description, location, duration, date, time)
             list.add(advert)

         }



     }
*/
}

