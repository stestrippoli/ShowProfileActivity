package com.example.showprofileactivity.chat

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.showprofileactivity.R
import com.example.showprofileactivity.User
import com.example.showprofileactivity.offers.placeholder.Chat
import com.example.showprofileactivity.offers.placeholder.Offer
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File


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
                    //_conversations.value = r?.mapNotNull { d -> d.toConversation() }
                    var conversations: MutableList<Conversation> = mutableListOf()
                    var count:Int = 0
                    for(c in r!!){
                        var offer : Offer
                        var user: User
                        FirebaseFirestore.getInstance().collection("offers")
                            .document(c.id.split("#")[0])
                            .get()
                            .addOnSuccessListener { r1 ->
                                offer = r1?.toOffer() as Offer
                                FirebaseFirestore.getInstance().collection("users").document(c.id.split("#")[1]).get()
                                    .addOnSuccessListener { r2 ->
                                        user = r2?.toUser() as User
                                        var co = Conversation(user, offer, c.toObject(Chat::class.java)!!.messages)
                                        conversations.add(co)
                                        count++
                                        if(count==r!!.size()){
                                            _conversations.value = conversations
                                            vm.saveConversations(_conversations.value!!)
                                        }
                                    }
                            }
                    }
                }
            }
            _conversations.value= emptyList()


        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = requireActivity().intent.getBundleExtra("user")?.getString("email")
        val name = requireActivity().intent.getBundleExtra("user")?.getString("fullname")

        val vieww = requireView().findViewById<RecyclerView>(R.id.chatList)
            with(vieww) {
                layoutManager = LinearLayoutManager(context)
                vm.conversation.observe(viewLifecycleOwner) {
                    adapter = MyConversationRecyclerViewAdapter(it,user!!,name!!)

                    adapter?.notifyDataSetChanged()
                    requireView().findViewById<ProgressBar>(R.id.chatListProgressBar).visibility = View.GONE
                    requireView().findViewById<RecyclerView>(R.id.chatList).visibility = View.VISIBLE
                }

            }
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
            val acceptedUserMail = get("acceptedUserMail") as String
            val completed = get("completed") as Boolean
            val ratedByCreator = get("ratedByCreator") as Boolean
            val ratedByAccepted = get("ratedByAccepted") as Boolean
            Offer(id, title, description, location, hours, creator, skill, email, date, time, accepted, acceptedUser, acceptedUserMail, completed, ratedByCreator, ratedByAccepted)
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