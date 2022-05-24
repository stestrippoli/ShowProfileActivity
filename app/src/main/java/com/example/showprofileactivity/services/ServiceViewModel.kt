package com.example.showprofileactivity.services

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.showprofileactivity.offers.placeholder.Offer
import com.example.showprofileactivity.services.placeholder.Service
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.util.*


class ServiceViewModel: ViewModel() {
    private val _services = MutableLiveData<List<Service>>()
    val services: LiveData<List<Service>> = _services
    private val lS: ListenerRegistration
    private val _offers = MutableLiveData<List<Offer>>()
    val offers: LiveData<List<Offer>> = _offers
    private val lO: ListenerRegistration

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
    }
    override fun onCleared() { super.onCleared(); lS.remove(); }


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
            val id = get("id") as String
            Offer(title, description, location, hours, creator, skill, email, id)
        }
        catch (e: Exception){
            e.printStackTrace()
            null
        }
    }
}