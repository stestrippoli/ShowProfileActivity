package com.example.showprofileactivity.profile

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.showprofileactivity.Message
import com.example.showprofileactivity.MessageAdapter
import com.example.showprofileactivity.R
import com.example.showprofileactivity.User
import com.example.showprofileactivity.chat.ChatViewModel
import com.example.showprofileactivity.offers.placeholder.Offer
import com.example.showprofileactivity.services.ServiceViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*


class ChatFragment : Fragment(R.layout.fragment_chat) {
    private val db: FirebaseFirestore
    //private val vm by activityViewModels<ChatViewModel>()
    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> = _messages
    lateinit var oid:String
    lateinit var user:String

    init {
        db =  FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = requireArguments().getString("uName").toString()
        oid = requireArguments().getString("oid").toString()
        user =(requireActivity()!!.intent.extras!!.get("user") as Bundle).getString("email") as String
        db.collection("chats").document("$oid"+"_"+"$user")
            .addSnapshotListener{ r, e ->
                if(e!=null)
                    _messages.value= emptyList()
                else if(r?.data?.get("messages")!=null)
                    _messages.value = r?.data?.get("messages") as List<Message>
                else
                    _messages.value = emptyList()
                requireView().findViewById<ProgressBar>(R.id.chatProgressBar).visibility = View.GONE
                requireView().findViewById<RecyclerView>(R.id.rv_chat).visibility = View.VISIBLE
            }

        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rv = requireView().findViewById<RecyclerView>(R.id.rv_chat)
        rv.layoutManager = LinearLayoutManager(context)
        println(messages.value)
        val adapter = MessageAdapter(messages.value?: emptyList())
        rv.adapter = adapter
        //rv.scrollToPosition(messages.value!!.size)
        val send = requireView().findViewById<Button>(R.id.button_chat_send)
        send.setOnClickListener{
            val text = requireView().findViewById<EditText>(R.id.edit_chat_message).text.toString()
            requireView().findViewById<EditText>(R.id.edit_chat_message).text.clear()
            val currentDate = SimpleDateFormat("dd/MM/yyyy").format(Date())
            val currentTime = SimpleDateFormat("hh:mm").format(Date())
            var m = Message(text, user, false, currentTime, currentDate)
            var t1 = messages.value?.toMutableList()
            t1?.add(m)
            println(t1)

            db.collection("chats")
                .document("$oid"+"_"+"$user")
                .set(mapOf("messages" to t1))
                .addOnSuccessListener { Log.d("Firebase", "Chat successfully updated."); rv.adapter?.notifyDataSetChanged()}
                .addOnFailureListener{ Log.d("Firebase", "Failed to update chat.") }
        }
    }

    /*private fun DocumentSnapshot.toMessage(): Message? {
        return try {
            val text = get("text") as String
            val user = get("user") as String
            val creator = get("creator") as Boolean
            val time = get("time") as String
            val day = get("day") as String
            Message(text, user, creator, time, day)
        }
        catch (e: Exception){
            e.printStackTrace()
            null
        }
    }*/

}