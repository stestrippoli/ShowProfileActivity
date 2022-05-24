package com.example.showprofileactivity.profile

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageButton

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
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
import com.example.showprofileactivity.R
import com.example.showprofileactivity.User
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    private val profileViewModel : ProfileViewModel by activityViewModels()

    private val db: FirebaseFirestore
    init {
        db = FirebaseFirestore.getInstance()
    }

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

        currentPhotoPath = profileViewModel.picture.value.toString()
        profileViewModel.fullname.observe(viewLifecycleOwner) { fullname ->
            requireView().findViewById<EditText>(R.id.name_e).setText(fullname)
        }
        profileViewModel.nickname.observe(viewLifecycleOwner) { nickname ->
            requireView().findViewById<EditText>(R.id.nickname_e).setText(nickname)
        }
        profileViewModel.email.observe(viewLifecycleOwner) { email ->
            requireView().findViewById<EditText>(R.id.email_e).setText(email)
        }
        profileViewModel.location.observe(viewLifecycleOwner) { location ->
            requireView().findViewById<EditText>(R.id.location_e).setText(location)
        }
        profileViewModel.skills.observe(viewLifecycleOwner) { skills ->
            requireView().findViewById<EditText>(R.id.skills_e).setText(skills.toString().replace(" | ", ", "))
        }
        profileViewModel.description.observe(viewLifecycleOwner) { description ->
            requireView().findViewById<EditText>(R.id.description_e).setText(description)
        }

        profileViewModel.picture.observe(viewLifecycleOwner) { picture ->
            requireView().findViewById<ImageButton>(R.id.propic_e).setImageURI(picture.toString().toUri())
        }



        registerForContextMenu(btn)


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
                with (sharedPref.edit()) {
                    val profile = JSONObject()
                    profile.put("nickname", requireView().findViewById<EditText>(R.id.nickname_e).text)
                    profile.put("location", requireView().findViewById<EditText>(R.id.location_e).text)
                    profile.put("skills", requireView().findViewById<EditText>(R.id.skills_e).text)
                    profile.put("description", requireView().findViewById<EditText>(R.id.description_e).text)
                    //profile.put("img", sharedViewModel.picture.value)
                    putString("profile", profile.toString())
                    apply()
                }

                val namebox = view.findViewById<EditText>(R.id.name_e).text.toString()
                val nicknamebox = view.findViewById<EditText>(R.id.nickname_e).text.toString()
                val locationbox = view.findViewById<EditText>(R.id.location_e).text.toString()
                val skillsbox = view.findViewById<EditText>(R.id.skills_e).text.toString()
                val descbox = view.findViewById<EditText>(R.id.description_e).text.toString()
                db.collection("users")
                    .document(profileViewModel.email.value.toString())
                    .set(User(namebox, nicknamebox, locationbox, skillsbox, descbox))
                    .addOnSuccessListener { Log.d("Firebase", "User profile successfully modified.") }
                    .addOnFailureListener{ Log.d("Firebase", "Failed to modify user profile.") }
                findNavController().navigateUp()
            }
        })
    }


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
            profileViewModel.savePicture(currentPhotoPath)

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
        profileViewModel.saveFullname(requireView().findViewById<EditText>(R.id.name_e).text)
        profileViewModel.saveEmail(requireView().findViewById<EditText>(R.id.email_e).text)
        profileViewModel.saveLocation(requireView().findViewById<EditText>(R.id.location_e).text)
        val skillslist = requireView().findViewById<EditText>(R.id.skills_e).text.toString()
        profileViewModel.saveSkills(skillslist.replace(" | ", ", "))
        profileViewModel.saveDescription(requireView().findViewById<EditText>(R.id.description_e).text)
        profileViewModel.savePicture(currentPhotoPath)
    }
}
