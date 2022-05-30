package com.example.showprofileactivity.chat

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.showprofileactivity.Message
import com.example.showprofileactivity.User
import com.example.showprofileactivity.offers.placeholder.Chat
import com.example.showprofileactivity.offers.placeholder.Offer
import com.example.showprofileactivity.services.placeholder.Service
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.util.*

class Conversation(val user:User, val offer:Offer, val chat: List<Message>)

class ChatViewModel: ViewModel() {
    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> = _messages
    //private val lm: ListenerRegistration

    fun saveMessages(messages: List<Message>){
        _messages.value = messages
    }
}

class ConversationViewModel: ViewModel() {
    val conversation = MutableLiveData<List<Conversation>>()

    fun saveConversations(conv: List<Conversation>){
        conversation.value = conv
    }
}