package com.example.showprofileactivity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.view.drawToBitmap
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class EditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        populate(intent)
        val btn = findViewById<ImageButton>(R.id.propic_e)

        btn.setOnClickListener { btn.performLongClick() }

        registerForContextMenu(btn)
    }

    private fun populate(i: Intent){
        val namebox = findViewById<TextView>(R.id.name_e)
        val nicknamebox = findViewById<TextView>(R.id.nickname_e)
        val emailbox = findViewById<TextView>(R.id.email_e)
        val locationbox = findViewById<TextView>(R.id.location_e)
        val skillsbox = findViewById<TextView>(R.id.skills_e)
        val descbox = findViewById<TextView>(R.id.description_e)
        namebox.text = i.getStringExtra("showprofileactivity.FULL_NAME")
        locationbox.text = i.getStringExtra("showprofileactivity.LOCATION")
        emailbox.text = i.getStringExtra("showprofileactivity.EMAIL")
        nicknamebox.text = i.getStringExtra("showprofileactivity.NICKNAME")
        skillsbox.text = i.getStringExtra("showprofileactivity.SKILLS")?.replace(" | ", ", ")
        descbox.text = i.getStringExtra("showprofileactivity.DESCRIPTION")
        currentPhotoPath = i.getStringExtra("showprofileactivity.IMG").toString()
    }
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.image_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.camera -> {
                dispatchTakePictureIntent()
                true
            }
            R.id.gallery -> {
                true
            }

            else -> {super.onOptionsItemSelected(item)}
        }
    }


    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent

            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()

                } catch (ex: IOException) {
                    null
                }

                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.android.showprofileactivity",
                        it
                    )

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    photolauncher.launch(takePictureIntent)
                }
            }
        }
    }

    lateinit var currentPhotoPath: String

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath

        }
    }

    private val photolauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){ result ->

        if (result.resultCode == Activity.RESULT_OK && result.data!=null) {
            val imgbox = findViewById<ImageButton>(R.id.propic_e)
            imgbox.setImageURI(currentPhotoPath.toUri())
        }
    }

    override fun onBackPressed() {
        val i = Intent()
        val b = Bundle()

        b.putString("showprofileactivity.FULL_NAME", findViewById<EditText>(R.id.name_e).text.toString())
        b.putString("showprofileactivity.NICKNAME", findViewById<EditText>(R.id.nickname_e).text.toString())
        b.putString("showprofileactivity.EMAIL", findViewById<EditText>(R.id.email_e).text.toString())
        b.putString("showprofileactivity.LOCATION", findViewById<EditText>(R.id.location_e).text.toString())
        b.putString("showprofileactivity.SKILLS", findViewById<EditText>(R.id.skills_e).text.toString().replace(", ", " | "))
        b.putString("showprofileactivity.DESCRIPTION", findViewById<EditText>(R.id.description_e).text.toString())
        b.putString("showprofileactivity.IMG", currentPhotoPath)
        i.putExtras(b)

        setResult(Activity.RESULT_OK, i)
        super.onBackPressed()
    }



}