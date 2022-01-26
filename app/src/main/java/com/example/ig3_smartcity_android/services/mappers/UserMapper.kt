package com.example.ig3_smartcity_android.services.mappers

import com.example.ig3_smartcity_android.model.User
import com.example.ig3_smartcity_android.dataAccess.dto.UserDTO

object UserMapper {
    fun mapToUserDto(user: User): UserDTO {
        return UserDTO(null,
                user.firstname,
                user.lastname,
                user.phone_number,
                user.username,
                user.password,
                user.province,
                user.city,
                user.street_and_number)
    }
}