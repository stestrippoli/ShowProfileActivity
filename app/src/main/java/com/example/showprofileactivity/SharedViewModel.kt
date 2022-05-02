package com.example.showprofileactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData

class SharedViewModel: ViewModel() {

    private var _fullname = MutableLiveData<String>();
    val fullname: LiveData<String> = _fullname;
    private var _location = MutableLiveData<String>();
    val location: LiveData<String> = _location;
    private var _email = MutableLiveData<String>();
    val email: LiveData<String> = _email;
    private var _nickname = MutableLiveData<String>();
    val nickname: LiveData<String> = _nickname;
    private var _skills = MutableLiveData<String>();
    val skills: LiveData<String> = _skills;
    private var _description = MutableLiveData<String>();
    val description: LiveData<String> = _description;
    private var _picture = MutableLiveData<String>();
    val picture: LiveData<String> = _picture;

    fun saveFullname(newFullname: String) {
        _fullname.value = newFullname
    }
    fun saveLocation(newLocation: String) {
        _location.value = newLocation
    }
    fun saveEmail(newEmail: String) {
        _email.value = newEmail
    }
    fun saveNickname(newNickname: String) {
        _nickname.value = newNickname
    }
    fun saveSkills(newSkills: String) {
        _skills.value = newSkills
    }
    fun saveDescription(newDescription: String) {
        _description.value = newDescription
    }
    fun savePicture(newPicture: String) {
        _picture.value = newPicture
    }
}