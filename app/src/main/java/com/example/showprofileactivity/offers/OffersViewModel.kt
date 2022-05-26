package com.example.showprofileactivity.offers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class OffersViewModel: ViewModel() {
    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val location = MutableLiveData<String>()
    val hours = MutableLiveData<Long>()
    val creator = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val skill = MutableLiveData<String>()
    val id = MutableLiveData<String>()
    val accepted = MutableLiveData<Boolean>()
    val acceptedUser = MutableLiveData<String>()

    fun setEmail(email: String) {
        this.email.value = email
    }
    fun setTitle(title: String) {
        this.title.value = title
    }
    fun setDescription(description: String) {
        this.description.value = description
    }
    fun setLocation(location: String) {
        this.location.value = location
    }
    fun setHours(hours: Long) {
        this.hours.value = hours
    }
    fun setCreator(creator: String) {
        this.creator.value = creator
    }
    fun setSkill(skill: String) {
        this.skill.value = skill
    }
    fun setId(id: String) {
        this.id.value = id
    }
    fun setAccepted(acc: Boolean) {
        this.accepted.value = acc
    }
    fun setAcceptedUser(accU: String) {
        this.acceptedUser.value = accU
    }
}