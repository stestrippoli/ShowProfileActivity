package com.example.showprofileactivity.services

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.showprofileactivity.services.placeholder.Service
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.util.*


class ServiceViewModel: ViewModel() {
        private val _services = MutableLiveData<List<Service>>()
        val services: LiveData<List<Service>> = _services
        private val l: ListenerRegistration

    init {
        l = FirebaseFirestore.getInstance().collection("services")
        .addSnapshotListener{ r,e ->
            _services.value = if (e!=null)
                emptyList()
            else r?.mapNotNull { d -> d.toService()  }
            println("caricati")
        }
    }
    override fun onCleared() { super.onCleared(); l.remove(); }


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




}