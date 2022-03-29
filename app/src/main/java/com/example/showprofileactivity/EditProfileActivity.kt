package com.example.showprofileactivity

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.showprofileactivity.databinding.ActivityEditProfileBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class EditProfileActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        populate(getIntent())
        val btn = findViewById<ImageButton>(R.id.propic_e)
        registerForContextMenu(btn)
    }

    private fun populate(i: Intent){
        //val imgbox = findViewById<ImageView>(R.id.propic_e)
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

    override fun onContextItemSelected(item: MenuItem): Boolean {
        println(item)
        return when (item.itemId) {
            R.id.camera -> {
                    photolauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                    true
            }
            R.id.gallery -> {
                true
            }

            else -> {super.onOptionsItemSelected(item)}
        }
    }


    private val photolauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK && result.data!=null) {
            val imageBitmap = result.data!!.extras?.get("data") as Bitmap
            findViewById<ImageView>(R.id.propic_e).setImageBitmap(imageBitmap)
        }
    }
    /*
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }


     */
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