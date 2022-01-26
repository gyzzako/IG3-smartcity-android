package com.example.ig3_smartcity_android.dataAccess.dto

import com.squareup.moshi.Json
import com.fasterxml.jackson.annotation.JsonProperty

class JwtTokenPayloadDTO {
    @Json(name = "status")
    @JsonProperty("status")
    var status: String? = null

    @Json(name = "id")
    @JsonProperty("id")
    var id: Int? = null

    @Json(name = "firstname")
    @JsonProperty("firstname")
    var firstname: String? = null

    @Json(name = "lastname")
    @JsonProperty("lastname")
    var lastname: String? = null

    constructor(status: String?, id: Int?, firstname: String?, lastname: String?) : super() {
        this.status = status
        this.id = id
        this.firstname = firstname
        this.lastname = lastname
    }

    constructor() {}
}