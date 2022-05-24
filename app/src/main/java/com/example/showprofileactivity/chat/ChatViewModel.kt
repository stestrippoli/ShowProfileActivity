package com.example.showprofileactivity.chat

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.showprofileactivity.Message
import com.example.showprofileactivity.offers.placeholder.Chat
import com.example.showprofileactivity.offers.placeholder.Offer
import com.example.showprofileactivity.services.placeholder.Service
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.util.*


class ChatViewModel(b:Bundle?): ViewModel() {
    private val _messages = MutableLiveData<List<Message>>()
    val offers: LiveData<List<Message>> = _messages
    private val lm: ListenerRegistration
    var oid:String = b!!.get("oid") as String
    var user:String = b!!.get("user") as String

    init {
        lm = FirebaseFirestore.getInstance().collection("chats").document("$oid"+"_"+"$user")
            .addSnapshotListener{ r, e ->
                _messages.value = if (e!=null)
                    emptyList()
                else r?.data?.get("messages") as List<Message>
            }
    }
    override fun onCleared() { super.onCleared(); lm.remove(); }


    /*fun DocumentSnapshot.toChat(): Chat? {
        return try {
            val messages = get("messages") as List<Message>
            Chat(messages)
        }
        catch (e: Exception){
            e.printStackTrace()
            null
        }
    }*/
}