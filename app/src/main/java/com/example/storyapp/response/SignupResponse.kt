package com.example.storyapp.response

import android.provider.ContactsContract.CommonDataKinds.Email
import com.google.gson.annotations.SerializedName


data class SignupResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)
