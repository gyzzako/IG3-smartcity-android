package com.example.ig3_smartcity_android.services.mappers

import com.example.ig3_smartcity_android.model.LoginUser
import com.example.ig3_smartcity_android.dataAccess.dto.LoginUserDTO

object UserLoginMapper {

    fun mapToLoginUserDTO(loginUser : LoginUser) : LoginUserDTO{
        return LoginUserDTO(loginUser.username,loginUser.password)
    }
}