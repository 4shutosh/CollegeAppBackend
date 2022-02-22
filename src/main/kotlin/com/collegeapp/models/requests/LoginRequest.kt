package com.collegeapp.models.requests

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String,
    @SerializedName("imageUrl") val imageUrl: String
)