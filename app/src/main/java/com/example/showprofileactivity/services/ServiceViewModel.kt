package com.example.showprofileactivity.services

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.showprofileactivity.User
import com.example.showprofileactivity.offers.placeholder.Offer
import com.example.showprofileactivity.services.placeholder.Service
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.util.*
import kotlin.collections.ArrayList


class ServiceViewModel: ViewModel() {
    private val _services = MutableLiveData<List<Service>>()
    val services: LiveData<List<Service>> = _services
    private val lS: ListenerRegistration
    private val _offers = MutableLiveData<List<Offer>>()
    val offers: LiveData<List<Offer>> = _offers
    private val _bookmarks = MutableLiveData<ArrayList<String>>()
    val bookmarks: LiveData<ArrayList<String>> = _bookmarks
    private val lO: ListenerRegistration
    private val lB: ListenerRegistration

    init {
        lS = FirebaseFirestore.getInstance().collection("services")
        .addSnapshotListener{ r,e ->
            _services.value = if (e!=null)
                emptyList()
            else r?.mapNotNull { d -> d.toService()  }
        }
        lO = FirebaseFirestore.getInstance().collection("offers")
            .addSnapshotListener{ r, e ->
                _offers.value = if (e!=null)
                    emptyList()
                else r?.mapNotNull { d -> d.toOffer() }
            }
        lB = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().currentUser?.email!!)
            .addSnapshotListener{ r, e ->
                _bookmarks.value = if (e != null) arrayListOf()
                else r?.data?.get("bookmarks") as ArrayList<String>?
            }
    }
    override fun onCleared() { super.onCleared(); lS.remove(); lO.remove(); lB.remove(); }


    fun DocumentSnapshot.toService(): Service? {
        return try {
            val name = get("name") as String
            val users = get("users") as List<String>
            Service(id, name, users)
        }
        catch (e: Exception){
            e.printStackTrace()
            null
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
            val ratedByAccepted = get("ratedByAccepted") as Boolean
            val ratedByCreator = get("ratedByCreator") as Boolean
            val creatorComment = get("creatorComment") as String
            val userComment = get("userComment") as String
            Offer(id, title, description, location, hours, creator, skill, email, date, time, accepted, acceptedUser, acceptedUserMail, completed, ratedByCreator, ratedByAccepted, creatorComment, userComment)
        }
        catch (e: Exception){
            e.printStackTrace()
            null
        }
    }
}