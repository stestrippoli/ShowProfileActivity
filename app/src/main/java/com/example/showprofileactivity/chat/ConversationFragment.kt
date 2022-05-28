package com.example.showprofileactivity.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.showprofileactivity.Message
import com.example.showprofileactivity.MessageAdapter
import com.example.showprofileactivity.R
import com.example.showprofileactivity.User
import com.example.showprofileactivity.chat.placeholder.PlaceholderContent
import com.example.showprofileactivity.offers.placeholder.Chat
import com.example.showprofileactivity.offers.placeholder.Offer
import com.example.showprofileactivity.services.ServiceViewModel
import com.example.showprofileactivity.services.placeholder.Service
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.processNextEventInCurrentThread


class ConversationFragment : Fragment() {
    private val _conversations = MutableLiveData<List<Conversation>>()
    private val vm by activityViewModels<ConversationViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_conversation_list, container, false)
        val user = requireActivity().intent.getBundleExtra("user")?.getString("email")

        FirebaseFirestore.getInstance().collection("chats")
            .document(user!!)
            .collection("chats")
            .addSnapshotListener { r, e ->
                if (e != null)
                    _conversations.value = emptyList()
                else{
                    _conversations.value = r?.mapNotNull { d -> d.toConversation() }

                    vm.saveConversations(_conversations.value!!)
                }
            }
            _conversations.value= emptyList()


        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = requireActivity().intent.getBundleExtra("user")?.getString("email")

        val vieww = requireView().findViewById<RecyclerView>(R.id.list)
            with(vieww) {
                layoutManager = LinearLayoutManager(context)
                vm.conversation.observe(viewLifecycleOwner) {
                    adapter = MyConversationRecyclerViewAdapter(it,user!!)

                    adapter?.notifyDataSetChanged()
                }

            }
    }
    fun DocumentSnapshot.toConversation(): Conversation? {

        return try {
            val offer = getOffer(this.id.split("#")[0])
            val user = getUser(this.id.split("#")[1])

            Conversation(this.id.split("#")[1], this.id.split("#")[0], this.toObject(Chat::class.java)!!.messages )

        }
        catch (e: Exception){
            e.printStackTrace()
            null
        }
    }
    fun getOffer(oid: String) : Offer?{
        var offer : Offer? = null
        FirebaseFirestore.getInstance().collection("offers")
            .document(oid)
            .get()
            .addOnSuccessListener { r ->
                offer = r?.toOffer()
            }
        return offer

    }
    fun getUser(email: String): User?{
        var user: User? = null
        FirebaseFirestore.getInstance().collection("users").document(email).get()
            .addOnSuccessListener { r ->
                user = r?.toUser()
            }
        return user

    }
    private fun DocumentSnapshot.toOffer(): Offer? {
        return try {
            val title = get("title") as String
            val description = get("description") as String
            val location = get("location") as String
            val hours = get("hours") as Long
            val creator = get("creator") as String
            val skill = get("skill") as String
            val email = get("email") as String
            val date = get("date") as String
            val time = get("time") as String
            val accepted = get("accepted") as Boolean
            val acceptedUser = get("acceptedUser") as String
            Offer(id, title, description, location, hours, creator, skill, email, date, time, accepted, acceptedUser)
        }
        catch (e: Exception){
            e.printStackTrace()
            null
        }
    }
    fun DocumentSnapshot.toUser(): User? {
        return try{

            val fullname = get("fullname") as String
            val username = get("username") as String?
            val location = get("location") as String?
            val services = get("services") as String?
            val description = get("description") as String?
            val credit = get("credit") as Long
            User(fullname, username, location, services, description, credit)
        } catch(e:Exception){
            e.printStackTrace()
            null
        }
    }

}