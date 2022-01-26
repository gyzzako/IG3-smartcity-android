package com.example.ig3_smartcity_android.dataAccess.dto

data class UserDTO(val id: Int?, val firstname:String?,
                   val lastname:String?,
                   val phone_number:String?,
                   val username:String?,
                   val password: String?,
                   val province:String?,
                   val city:String?,
                   val street_and_number:String?){}
