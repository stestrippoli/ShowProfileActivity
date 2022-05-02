package com.example.showprofileactivity

import android.content.Context
import android.view.MenuInflater
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageButton
import android.widget.TextView
import org.json.JSONObject

import android.annotation.SuppressLint
import android.app.Activity
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val sharedViewModel : SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val btn = view?.findViewById<ImageButton>(R.id.propic_e)

        btn!!.setOnClickListener { btn!!.performLongClick() }

        sharedViewModel.fullname.observe(viewLifecycleOwner) { fullname ->
            requireView().findViewById<TextView>(R.id.name_e).text = fullname
        }
        sharedViewModel.nickname.observe(viewLifecycleOwner) { nickname ->
            requireView().findViewById<TextView>(R.id.nickname_e).text = nickname
        }
        sharedViewModel.email.observe(viewLifecycleOwner) { email ->
            requireView().findViewById<TextView>(R.id.email_e).text = email
        }
        sharedViewModel.location.observe(viewLifecycleOwner) { location ->
            requireView().findViewById<TextView>(R.id.location_e).text = location
        }
        sharedViewModel.skills.observe(viewLifecycleOwner) { skills ->
            requireView().findViewById<TextView>(R.id.skills_e).text = skills.replace(" | ", ", ")
        }
        sharedViewModel.description.observe(viewLifecycleOwner) { description ->
            requireView().findViewById<TextView>(R.id.name_e).text = description
        }
        sharedViewModel.picture.observe(viewLifecycleOwner) { picture ->
            requireView().findViewById<ImageView>(R.id.propic_e2).setImageURI(picture.toUri())
        }

        registerForContextMenu(btn)
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                sharedViewModel.saveFullname(requireView().findViewById<TextView>(R.id.name_e).text as String)
                sharedViewModel.saveEmail(requireView().findViewById<TextView>(R.id.email_e).text as String)
                sharedViewModel.saveLocation(requireView().findViewById<TextView>(R.id.location_e).text as String)
                val skillslist = requireView().findViewById<TextView>(R.id.skills_e).text as String;
                sharedViewModel.saveSkills(skillslist.replace(" | ", ", "))
                sharedViewModel.saveDescription(requireView().findViewById<TextView>(R.id.name_e).text as String)
                sharedViewModel.savePicture(currentPhotoPath)
            }
        })
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

        }
    }

    private val photolauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){ result ->

        if (result.resultCode == Activity.RESULT_OK && result.data!=null) {
            val imgbox = view?.findViewById<ImageButton>(R.id.propic_e)
            imgbox!!.setImageURI(currentPhotoPath.toUri())
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}