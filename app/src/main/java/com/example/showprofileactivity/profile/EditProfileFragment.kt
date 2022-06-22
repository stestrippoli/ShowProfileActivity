package com.example.showprofileactivity.profile

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageButton

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Build
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
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.showprofileactivity.R
import com.example.showprofileactivity.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {
    var picTaken:Boolean = false

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

        currentPhotoPath = profileViewModel.picturepath.value.toString()
        currentPhotoName = profileViewModel.picture.value.toString()
        oldPhoto = profileViewModel.picture.value.toString()

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
        profileViewModel.picturepath.observe(viewLifecycleOwner) { picture ->
            if(picture!="null") {
                val bitmap = getCroppedBitmap(
                    Bitmap.createScaledBitmap(
                        BitmapFactory.decodeFile(picture.toString()),
                        400,
                        400,
                        false
                    )
                )
                requireView().findViewById<ImageButton>(R.id.propic_e).setImageBitmap(bitmap)
            }
        }

        registerForContextMenu(btn)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val namebox = view.findViewById<EditText>(R.id.name_e).text.toString()
                val nicknamebox = view.findViewById<EditText>(R.id.nickname_e).text.toString()
                val locationbox = view.findViewById<EditText>(R.id.location_e).text.toString()
                val skillsbox = view.findViewById<EditText>(R.id.skills_e).text.toString()
                val descbox = view.findViewById<EditText>(R.id.description_e).text.toString()
                if(picTaken){
                    db.collection("users")
                        .document(profileViewModel.email.value.toString())
                        .update(
                            mapOf(
                                "fullname" to namebox,
                                "username" to nicknamebox,
                                "location" to locationbox,
                                "services" to skillsbox,
                                "description" to descbox,
                                "img" to currentPhotoName
                            )
                        )
                        .addOnSuccessListener { Log.d("Firebase", "User profile successfully modified.") }
                        .addOnFailureListener{ Log.d("Firebase", "Failed to modify user profile.") }
                }
                else{
                    db.collection("users")
                        .document(profileViewModel.email.value.toString())
                        .update(
                            mapOf(
                                "fullname" to namebox,
                                "username" to nicknamebox,
                                "location" to locationbox,
                                "services" to skillsbox,
                                "description" to descbox
                            )
                        )
                        .addOnSuccessListener { Log.d("Firebase", "User profile successfully modified.") }
                        .addOnFailureListener{ Log.d("Firebase", "Failed to modify user profile.") }
                }

                if(picTaken)
                    savePhotoOnDB()
                findNavController().navigateUp()
            }
        })
    }

    fun getCroppedBitmap(bitmap: Bitmap): Bitmap? {
        val output = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        paint.setAntiAlias(true)
        canvas.drawARGB(0, 0, 0, 0)
        paint.setColor(color)
        canvas.drawCircle(
            (bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat(),
            (bitmap.width / 2).toFloat(), paint
        )
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        canvas.drawBitmap(bitmap, rect, rect, paint)
        canvas.rotate(90F)
        return output
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

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            profileViewModel.savePicturePath(currentPhotoPath)
            val bitmap = getCroppedBitmap(
                Bitmap.createScaledBitmap(BitmapFactory.decodeFile(currentPhotoPath),400,400,false))
            requireView().findViewById<ImageButton>(R.id.propic_e).setImageBitmap(bitmap)
        }

        if (requestCode == 1)
            profileViewModel.savePicturePath(currentPhotoPath)
    }

    private fun savePhotoOnDB(){
        deletePreviousImage()
        val storageRef = FirebaseStorage.getInstance().reference

        val ImagesRef = storageRef.child("images/$currentPhotoName")

        val stream = FileInputStream(File(currentPhotoPath))

        val uploadTask = ImagesRef.putStream(stream)
        uploadTask.addOnFailureListener {
            println(it)
            }.addOnSuccessListener { taskSnapshot ->
                print("File uploaded correctly!"+taskSnapshot.metadata.toString())
            }
            .addOnFailureListener{
                println("Error: file couldn't be uploaded")
            }
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
                    takePictureIntent.putExtra("path", it.absoluteFile)
                    photolauncher.launch(takePictureIntent)
                }
            }
        }
    }

    lateinit var currentPhotoPath: String
    lateinit var currentPhotoName: String
    lateinit var oldPhoto: String
    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "PNG_${timeStamp}_", /* prefix */
            ".png", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
            currentPhotoName = "PNG_${timeStamp}_.png"
            //profileViewModel.savePicture(currentPhotoName)

        }
    }


    private fun deletePreviousImage() {
        val storageRef = FirebaseStorage.getInstance().reference

        val desertRef = storageRef.child("/images/$oldPhoto")

        desertRef.delete().addOnSuccessListener {
            println("Previous image deleted correctly.")
        }.addOnFailureListener {
            println("Previous image NOT DELETED.")
        }
    }

    private val photolauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){ result ->

        if (result.resultCode == Activity.RESULT_OK && result.data!=null) {
            profileViewModel.savePicturePath(currentPhotoPath)
            picTaken=true
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
        profileViewModel.savePicture(currentPhotoName)
        if(picTaken)
            profileViewModel.savePicturePath(currentPhotoPath)

    }

}
