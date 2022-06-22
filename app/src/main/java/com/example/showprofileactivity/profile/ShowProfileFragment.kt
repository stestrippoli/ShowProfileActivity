package com.example.showprofileactivity.profile

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.example.showprofileactivity.R
import com.example.showprofileactivity.User
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.InputStream


class ShowProfileFragment : Fragment(R.layout.fragment_show_profile) {

    private val profileViewModel : ProfileViewModel by activityViewModels()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var email: String
    lateinit var rating: String
    //var rating: String = "☆ 4"
    var user: User? = null
    private var mainMenu: Menu? = null
    var picDownloaded=false
    var ready=false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var editmode: Boolean
        setHasOptionsMenu(true)
        if(arguments==null) {
            email = FirebaseAuth.getInstance().currentUser?.email!!
            editmode = true
        }
        else{
            email = requireArguments().getString("email").toString()
            editmode = false
        }

        db.collection("users").document(email).get()
            .addOnSuccessListener { res ->
                user = res.toUser()!!
                ready=true
                setViewModel()
                mainMenu?.findItem(R.id.modifybtn)?.isVisible = editmode
                requireView().findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                requireView().findViewById<ConstraintLayout>(R.id.profileLayout).visibility = View.VISIBLE
                if(editmode) {
                    requireView().findViewById<TextView>(R.id.labelCredit).visibility =
                        View.VISIBLE
                    requireView().findViewById<TextView>(R.id.credit).visibility =
                        View.VISIBLE
                }
                else{
                    requireView().findViewById<TextView>(R.id.labelCredit).visibility =
                        View.GONE
                    requireView().findViewById<TextView>(R.id.credit).visibility =
                        View.GONE
                }
            }
            .addOnFailureListener {
                Toast
                    .makeText(context, "Error", Toast.LENGTH_LONG)
                    .show()
            }

        return inflater.inflate(R.layout.fragment_show_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.fullname.observe(viewLifecycleOwner) { fullname ->
            if(ready)
                requireView().findViewById<TextView>(R.id.fullname).text = fullname
        }
        profileViewModel.rating.observe(viewLifecycleOwner) { rating ->
            if(ready)
                requireView().findViewById<RatingBar>(R.id.rating).rating = rating.toString().toFloat()
        }
        profileViewModel.nickname.observe(viewLifecycleOwner) { nickname ->
            if(ready)
                requireView().findViewById<TextView>(R.id.nickname).text = nickname
        }
        profileViewModel.email.observe(viewLifecycleOwner) { email ->
            if(ready)
                requireView().findViewById<TextView>(R.id.email).text = email
        }
        profileViewModel.location.observe(viewLifecycleOwner) { location ->
            if(ready)
                requireView().findViewById<TextView>(R.id.location).text = location
        }
        profileViewModel.skills.observe(viewLifecycleOwner) { skills ->
            if(ready)
                requireView().findViewById<TextView>(R.id.skills).text = skills
        }
        profileViewModel.description.observe(viewLifecycleOwner) { description ->
            if(ready)
                requireView().findViewById<TextView>(R.id.description).text = description
        }
        profileViewModel.credit.observe(viewLifecycleOwner) { credit ->
            if(ready)
                requireView().findViewById<TextView>(R.id.credit).text = credit.toString()
        }
        profileViewModel.picture.observe(viewLifecycleOwner) { picture ->
            if(ready) {
                downloadImage(picture.toString())
                picDownloaded = true
            }
        }
        profileViewModel.picturepath.observe(viewLifecycleOwner) { picture ->
            if(picture!="null"&&picDownloaded) {
                val imageView = requireView().findViewById<ImageView>(R.id.profilepic)
                val bitmap = getCroppedBitmap(
                    Bitmap.createScaledBitmap(
                        BitmapFactory.decodeFile(picture.toString()),
                        400,
                        400,
                        false
                    )
                )

                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap)
                }
                picDownloaded=false
            }

        }
    }


    fun downloadImage(picture: String){

        // Create a storage reference from our app
        val storageRef = FirebaseStorage.getInstance().reference
        val imgRef = storageRef.child("/images/$picture")
        val imageView = requireView().findViewById<ImageView>(R.id.profilepic)

        val localFile = File.createTempFile("images", "png", requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES))
        imgRef.getFile(localFile)
            .addOnSuccessListener {
                val bitmap = getCroppedBitmap(
                    Bitmap.createScaledBitmap(BitmapFactory.decodeFile(localFile.path),400,400,false))

                if(bitmap != null) {
                    imageView.setImageBitmap(bitmap)
                    profileViewModel.savePicturePath(localFile.path)
                    imageView.visibility=View.VISIBLE
                    view?.findViewById<ProgressBar>(R.id.picprogress)?.visibility=View.INVISIBLE
                }
                println("File downloaded successfully")
            }
            .addOnFailureListener {
                requireView().findViewById<ProgressBar>(R.id.picprogress).visibility=View.INVISIBLE
                imageView.visibility=View.VISIBLE
                println("Error: file couldn't be downloaded")
            }
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
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(
            (bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat(),
            (bitmap.width / 2).toFloat(), paint
        )
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        canvas.drawBitmap(bitmap, rect, rect, paint)
        canvas.rotate(90F)
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        mainMenu = menu
        inflater.inflate(R.menu.custom_menu, menu)
        mainMenu?.findItem(R.id.modifybtn)?.isVisible = false
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.modifybtn -> {
                findNavController().navigate(R.id.action_toEditProfileFragment)
                true
            }
            else -> {super.onContextItemSelected(item)}
        }
    }

    private fun setViewModel(){
        val (fullname, username, email, location, services, description, credit, img) = user!!

        profileViewModel.saveFullname(fullname)
        profileViewModel.saveNickname(username?:"Your Username" as String)
        profileViewModel.saveLocation(location?:"Your Location" as String)
        profileViewModel.saveSkills(services?:"" as String)
        profileViewModel.saveDescription(description?:"Your Description" as String)
        profileViewModel.saveEmail(email.toString())
        profileViewModel.savePicture(img?:"")
        profileViewModel.saveEmail(email as CharSequence)
        profileViewModel.saveCredit(credit!!)
        //profileViewModel.savePicture(img.toString())
        profileViewModel.saveRating(rating)
    }

    override fun onDestroyView() {
        val imageView = requireView().findViewById<ImageView>(R.id.profilepic)
        imageView.visibility=View.INVISIBLE
        requireView().findViewById<ProgressBar>(R.id.picprogress).visibility=View.VISIBLE
        super.onDestroyView()
    }

    fun DocumentSnapshot.toUser(): User? {
        return try{

            rating = get("rating") as String
            var timesrated = get("timesrated") as String
            if (timesrated.toInt() != 0) {
                rating = (rating.toDouble() / timesrated.toDouble()).toString()
            }

            val fullname = get("fullname") as String
            val username = get("username") as String?
            val email = get("email") as String?
            val location = get("location") as String?
            val services = get("services") as String?
            val description = get("description") as String?
            val credit = get("credit") as Long
            val img = get("img") as String?
            User(fullname, username, email, location, services, description, credit, img)
        } catch(e:Exception){
            e.printStackTrace()
            null
        }
    }

}