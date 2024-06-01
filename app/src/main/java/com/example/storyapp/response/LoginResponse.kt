package com.example.storyapp.response

import android.provider.ContactsContract.CommonDataKinds.Email
import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("loginResult")
    val loginResult: LoginResult,

    @field:SerializedName("error")
    val error: Boolean = true,

    @field:SerializedName("message")
    val message: String = ""
)

data class LoginResult(

    @field:SerializedName("name")
    val name: String = "",

    @field:SerializedName("userId")
    val userId: String = "",

    @field:SerializedName("token")
    val token: String = ""
)