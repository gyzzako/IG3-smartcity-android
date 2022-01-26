package com.example.ig3_smartcity_android.services.mappers

import com.example.ig3_smartcity_android.model.JwtTokenPayload
import com.example.ig3_smartcity_android.dataAccess.dto.JwtTokenPayloadDTO

object TokenMapper {
    fun mapToJwtTokenPayload(JwtTokenPayloadDTO: JwtTokenPayloadDTO) :JwtTokenPayload{
        return JwtTokenPayload(JwtTokenPayloadDTO.status, JwtTokenPayloadDTO.id, JwtTokenPayloadDTO.firstname, JwtTokenPayloadDTO.lastname)
    }
}