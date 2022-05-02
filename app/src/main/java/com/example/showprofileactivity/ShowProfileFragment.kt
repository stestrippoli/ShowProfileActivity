package com.example.showprofileactivity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShowProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShowProfileFragment : Fragment(R.layout.fragment_show_profile2) {
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
        setHasOptionsMenu(true)
        sharedViewModel.fullname.observe(viewLifecycleOwner) { fullname ->
            requireView().findViewById<TextView>(R.id.name3).text = fullname
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
            requireView().findViewById<TextView>(R.id.name3).text = description
        }
        sharedViewModel.picture.observe(viewLifecycleOwner) { picture ->
            requireView().findViewById<ImageView>(R.id.profilepic3).setImageURI(picture.toUri())
        }
        return inflater.inflate(R.layout.fragment_show_profile2, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu!!, inflater)
        inflater.inflate(R.menu.custom_menu, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onContextItemSelected(item)
        editProfile()
        return true
    }

    private fun editProfile(){
        /*val i = Intent(activity, EditProfileActivity::class.java)
        val b = Bundle()
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val myJSON = JSONObject(
            sharedPref.getString("profile",
                """{"fullname":"Default Name","nickname":"Default nickname","email":"default@email.com","location":"Default location","skills":"Skill1 | Skill2 | Skill3","description": "Default description","img": "android.resource://com.example.showprofileactivity/${R.drawable.propic}"}""")
        )
        b.putString("showprofileactivity.FULL_NAME", view?.findViewById<TextView>(R.id.name)!!.text.toString())
        b.putString("showprofileactivity.NICKNAME", view?.findViewById<TextView>(R.id.nickname)!!.text.toString())
        b.putString("showprofileactivity.EMAIL", view?.findViewById<TextView>(R.id.email)!!.text.toString())
        b.putString("showprofileactivity.LOCATION", view?.findViewById<TextView>(R.id.locationLabel)!!.text.toString())
        b.putString("showprofileactivity.SKILLS", view?.findViewById<TextView>(R.id.skills)!!.text.toString())
        b.putString("showprofileactivity.DESCRIPTION", view?.findViewById<TextView>(R.id.descriptionLabel)!!.text.toString())
        b.putString("showprofileactivity.IMG", myJSON.getString("img").toString())
        i.putExtras(b)

        launcher.launch(i)*/
        findNavController().navigate(R.id.action_toEditProfileFragment);
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK
            && result.data!=null) {

            val data: Intent = result.data!!
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
            with (sharedPref!!.edit()) {
                val profile = JSONObject()
                profile.put("fullname", data.getStringExtra("showprofileactivity.FULL_NAME"))
                profile.put("nickname", data.getStringExtra("showprofileactivity.NICKNAME"))
                profile.put("email", data.getStringExtra("showprofileactivity.EMAIL"))
                profile.put("location", data.getStringExtra("showprofileactivity.LOCATION"))
                profile.put("skills", data.getStringExtra("showprofileactivity.SKILLS"))
                profile.put("description", data.getStringExtra("showprofileactivity.DESCRIPTION"))
                profile.put("img", data.getStringExtra("showprofileactivity.IMG"))
                putString("profile", profile.toString())
                apply()
            }
            //populateBoxes()
        }
    }


    /*private fun populateBoxes(){
        val imgbox = view?.findViewById<ImageView>(R.id.profilepic3)
        val namebox = view?.findViewById<TextView>(R.id.name3)
        val nicknamebox = view?.findViewById<TextView>(R.id.nickname)
        val emailbox = view?.findViewById<TextView>(R.id.email)
        val locationbox = view?.findViewById<TextView>(R.id.locationLabel)
        val skillsbox = view?.findViewById<TextView>(R.id.skills)
        val descbox = view?.findViewById<TextView>(R.id.descriptionLabel)
        val sharedPref =this.activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val myJSON = JSONObject(
            sharedPref.getString("profile",
                """{"fullname":"Default Name","nickname":"Default nickname","email":"default@email.com","location":"Default location","skills":"Skill1 | Skill2 | Skill3","description": "Default description","img": "android.resource://com.example.showprofileactivity/${android.R.drawable.picture_frame}"}""")
        )

        namebox!!.text = myJSON.getString("fullname")
        locationbox!!.text = myJSON.getString("location")
        emailbox!!.text = myJSON.getString("email")
        nicknamebox!!.text = myJSON.getString("nickname")
        skillsbox!!.text = myJSON.getString("skills")
        descbox!!.text = myJSON.getString("description")
        imgbox!!.setImageURI(myJSON.getString("img").toUri())
    }*/

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ShowProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShowProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    //populateBoxes()
                }
            }
    }
}