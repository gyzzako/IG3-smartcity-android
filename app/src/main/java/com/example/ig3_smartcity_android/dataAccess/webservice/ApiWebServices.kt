package com.example.ig3_smartcity_android.dataAccess.webservice

import com.example.ig3_smartcity_android.dataAccess.dto.LoginUserDTO
import com.example.ig3_smartcity_android.dataAccess.dto.MealDTO
import com.example.ig3_smartcity_android.dataAccess.dto.UserDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiWebServices {

    //permet de se connecter
    @POST("V1/user/login")
    fun userLogin(@Body loginUserDTO: LoginUserDTO) : Call<String>

    //permet d'inscrire un utilisateur. -->Token String
    @POST("V1/user")
    fun registerUser(@Body userDTO: UserDTO) : Call<String>

    //récupère tous les repas pas encore réclamés
    @GET("V1/meal?mealAvailableFilter=true")
    fun getAllMeals(@Header("Authorization") authHeader: String) : Call<List<MealDTO>>

    //Ajout d'un repas
    @Multipart
    @POST("V1/meal")
    fun addMeal(@Part namePart:MultipartBody.Part, @Part descriptionPart:MultipartBody.Part,@Part portionNumberPart:MultipartBody.Part,
                @Part userFkPart:MultipartBody.Part,@Part categoryFkPart:MultipartBody.Part, @Part image: MultipartBody.Part, @Header("Authorization") authHeader: String):Call<String>

    @POST("V1/order")
    fun createOrder(@Body order: RequestBody, @Header("Authorization") authHeader: String):Call<String>
}