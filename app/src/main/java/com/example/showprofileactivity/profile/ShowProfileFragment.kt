package com.example.showprofileactivity.profile

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.showprofileactivity.R
import com.example.showprofileactivity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONObject


class ShowProfileFragment : Fragment(R.layout.fragment_show_profile) {

    private val sharedViewModel : SharedViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth
    private val db: FirebaseFirestore
    //val lateinit user:User
    init {
        db = FirebaseFirestore.getInstance()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        setViewModel()

        return inflater.inflate(R.layout.fragment_show_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val namebox = requireView().findViewById<TextView>(R.id.fullname)

        sharedViewModel.fullname.observe(viewLifecycleOwner) { fullname ->
            namebox.text = fullname
        }
        sharedViewModel.nickname.observe(viewLifecycleOwner) { nickname ->
            requireView().findViewById<TextView>(R.id.nickname).text = nickname
        }
        sharedViewModel.email.observe(viewLifecycleOwner) { email ->
            requireView().findViewById<TextView>(R.id.email).text = email
        }
        sharedViewModel.location.observe(viewLifecycleOwner) { location ->
            requireView().findViewById<TextView>(R.id.location).text = location
        }
        sharedViewModel.skills.observe(viewLifecycleOwner) { skills ->
            requireView().findViewById<TextView>(R.id.skills).text = skills
        }
        sharedViewModel.description.observe(viewLifecycleOwner) { description ->
            requireView().findViewById<TextView>(R.id.description).text = description
        }
        sharedViewModel.picture.observe(viewLifecycleOwner) { picture ->
            requireView().findViewById<ImageView>(R.id.profilepic).setImageURI(picture.toString().toUri())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.custom_menu, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.modifybtn -> {
                findNavController().navigate(R.id.action_toEditProfileFragment)
                true
            }
            else -> {super.onContextItemSelected(item)}
        }
        return true
    }

    private fun setViewModel(){
       val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val myJSON = JSONObject(
            sharedPref.getString("profile",
                """{"fullname":"Default Name","nickname":"Default nickname","email":"default@email.com","location":"Default location","skills":"Skill1 | Skill2 | Skill3","description": "Default description","img": "android.resource://com.example.showprofileactivity/${R.drawable.propic}"}""")
        )

        sharedViewModel.saveFullname(myJSON.getString("fullname").toString())
        sharedViewModel.saveNickname(myJSON.getString("nickname").toString())
        sharedViewModel.saveEmail(myJSON.getString("email").toString())
        sharedViewModel.saveLocation(myJSON.getString("location").toString())
        sharedViewModel.saveSkills(myJSON.getString("skills").toString())
        sharedViewModel.saveDescription(myJSON.getString("description").toString())
        sharedViewModel.savePicture( myJSON.getString("img").toString())

        /*val (name, username, email, location, skills, description, picture) = user

        sharedViewModel.saveFullname(name as String)
        sharedViewModel.saveNickname(username as String)
        sharedViewModel.saveEmail(email as String)
        sharedViewModel.saveLocation(location as String)
        sharedViewModel.saveSkills(skills as String)
        sharedViewModel.saveDescription(description as String)
        sharedViewModel.savePicture(picture as String)*/

    }

}