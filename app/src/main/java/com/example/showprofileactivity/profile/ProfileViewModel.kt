package com.example.showprofileactivity.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData

class ProfileViewModel: ViewModel() {

    private var _fullname = MutableLiveData<CharSequence>()
    val fullname: LiveData<CharSequence> = _fullname
    private var _location = MutableLiveData<CharSequence>()
    val location: LiveData<CharSequence> = _location
    private var _rating = MutableLiveData<CharSequence>()
    val rating: LiveData<CharSequence> = _rating
    private var _email = MutableLiveData<CharSequence>()
    val email: LiveData<CharSequence> = _email
    private var _nickname = MutableLiveData<CharSequence>()
    val nickname: LiveData<CharSequence> = _nickname
    private var _skills = MutableLiveData<CharSequence>()
    val skills: LiveData<CharSequence> = _skills
    private var _description = MutableLiveData<CharSequence>()
    val description: LiveData<CharSequence> = _description
    private var _credit = MutableLiveData<Long>()
    val credit: LiveData<Long> = _credit
    private var _picture = MutableLiveData<CharSequence>()
    val picture: LiveData<CharSequence> = _picture
    private var _picturepath = MutableLiveData<CharSequence>()
    val picturepath: LiveData<CharSequence> = _picturepath

    fun saveFullname(newFullname: CharSequence) {
        _fullname.value = newFullname
    }
    fun saveLocation(newLocation: CharSequence) {
        _location.value = newLocation
    }
    fun saveRating(newRating: CharSequence) {
        _rating.value = newRating
    }
    fun saveEmail(newEmail: CharSequence) {
        _email.value = newEmail
    }
    fun saveNickname(newNickname: CharSequence) {
        _nickname.value = newNickname
    }
    fun saveSkills(newSkills: CharSequence) {
        _skills.value = newSkills
    }
    fun saveDescription(newDescription: CharSequence) {
        _description.value = newDescription
    }
    fun saveCredit(newCredit: Long) {
        _credit.value = newCredit
    }
    fun savePicture(newPicture: CharSequence) {
        _picture.value = newPicture
    }
    fun savePicturePath(newPicture: CharSequence) {
        _picturepath.value = newPicture
    }
}