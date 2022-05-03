package com.example.showprofileactivity.profile

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageButton
import android.widget.TextView

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.showprofileactivity.R
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    private val sharedViewModel : SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
      return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val btn = view?.findViewById<ImageButton>(R.id.propic_e)

        btn!!.setOnClickListener {
            btn.performLongClick()
        }

        currentPhotoPath = sharedViewModel.picture.value.toString()
        sharedViewModel.fullname.observe(viewLifecycleOwner) { fullname ->
            requireView().findViewById<EditText>(R.id.name_e).setText(fullname)
        }
        sharedViewModel.nickname.observe(viewLifecycleOwner) { nickname ->
            requireView().findViewById<EditText>(R.id.nickname_e).setText(nickname)
        }
        sharedViewModel.email.observe(viewLifecycleOwner) { email ->
            requireView().findViewById<EditText>(R.id.email_e).setText(email)
        }
        sharedViewModel.location.observe(viewLifecycleOwner) { location ->
            requireView().findViewById<EditText>(R.id.location_e).setText(location)
        }
        sharedViewModel.skills.observe(viewLifecycleOwner) { skills ->
            requireView().findViewById<EditText>(R.id.skills_e).setText(skills.toString().replace(" | ", ", "))
        }
        sharedViewModel.description.observe(viewLifecycleOwner) { description ->
            requireView().findViewById<EditText>(R.id.description_e).setText(description)
        }

        sharedViewModel.picture.observe(viewLifecycleOwner) { picture ->
            requireView().findViewById<ImageButton>(R.id.propic_e).setImageURI(picture.toString().toUri())
        }



        registerForContextMenu(btn)


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
                with (sharedPref.edit()) {
                    val profile = JSONObject()
                    profile.put("fullname", sharedViewModel.fullname.value)
                    profile.put("nickname", sharedViewModel.nickname.value)
                    profile.put("email", sharedViewModel.email.value)
                    profile.put("location", sharedViewModel.location.value)
                    profile.put("skills", sharedViewModel.skills.value)
                    profile.put("description", sharedViewModel.description.value)
                    profile.put("img", sharedViewModel.picture.value)
                    putString("profile", profile.toString())
                    apply()
                }
                requireActivity().supportFragmentManager.popBackStack()
            }
        })
    }



    /*private fun populate(){
        val namebox = view?.findViewById<TextView>(R.id.name_e)
        val nicknamebox = view?.findViewById<TextView>(R.id.nickname_e)
        val emailbox = view?.findViewById<TextView>(R.id.email_e)
        val locationbox = view?.findViewById<TextView>(R.id.location_e)
        val skillsbox = view?.findViewById<TextView>(R.id.skills_e)
        val descbox = view?.findViewById<TextView>(R.id.description_e)
        val sharedPref =this.activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val myJSON = JSONObject(
            sharedPref.getString("profile",
                """{"fullname":"Default Name","nickname":"Default nickname","email":"default@email.com","location":"Default location","skills":"Skill1 | Skill2 | Skill3","description": "Default description","img": "android.resource://com.example.showprofileactivity/${android.R.drawable.picture_frame}"}""")
        )

        namebox!!.text = myJSON.getString("fullname")
        locationbox!!.text = myJSON.getString("location")
        emailbox!!.text = myJSON.getString("email")
        nicknamebox!!.text = myJSON.getString("email")
        skillsbox!!.text = myJSON.getString("nickname").replace(" | ", ", ")
        descbox!!.text = myJSON.getString("description")
        currentPhotoPath = myJSON.getString("img").toString()
    }*/

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        activity?.menuInflater?.inflate(R.menu.image_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.camera -> {
                dispatchTakePictureIntent()
                true
            }
            R.id.gallery -> {
                openGalleryForImage()
                true
            }

            else -> {super.onOptionsItemSelected(item)}
        }
    }


    private fun openGalleryForImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
        //val intent = Intent(Intent.ACTION_PICK)
        //intent.type = "image/*"
        //startActivityForResult(intent, 100)
        //val imageView = findViewById<ImageView>(R.id.propic_e)
        //val imageBitmap = intent.extras?.get("data") as Bitmap
        //imageView.setImageBitmap(imageBitmap)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val imageView = view?.findViewById<ImageView>(R.id.propic_e)
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            imageView!!.setImageURI(data?.data)
            //val imgbox = findViewById<ImageButton>(R.id.propic_e)
        }

        if (requestCode == 1)
            imageView!!.setImageURI(data?.data)
    }


    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent

            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()

                } catch (ex: IOException) {
                    null
                }

                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
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
        val storageDir: File? = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
            sharedViewModel.savePicture(currentPhotoPath)

        }
    }

    private val photolauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){ result ->

        if (result.resultCode == Activity.RESULT_OK && result.data!=null) {
            val imgbox = view?.findViewById<ImageButton>(R.id.propic_e)
            imgbox!!.setImageURI(currentPhotoPath.toUri())
        }
    }

    override fun onDestroyView() {
        updatevm()
        super.onDestroyView()

    }

    fun updatevm() {
        sharedViewModel.saveFullname(requireView().findViewById<EditText>(R.id.name_e).text)
        sharedViewModel.saveEmail(requireView().findViewById<EditText>(R.id.email_e).text)
        sharedViewModel.saveLocation(requireView().findViewById<EditText>(R.id.location_e).text)
        val skillslist = requireView().findViewById<EditText>(R.id.skills_e).text.toString()
        sharedViewModel.saveSkills(skillslist.replace(" | ", ", "))
        sharedViewModel.saveDescription(requireView().findViewById<EditText>(R.id.description_e).text)
        sharedViewModel.savePicture(currentPhotoPath)
    }
}
