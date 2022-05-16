package com.example.showprofileactivity

import android.net.Uri

data class User(
    var name:String?,
    var username:String?,
    var email:String?,
    var location:String?,
    var skills:String?,
    var descritpion:String?,
    var picture:Uri?
)