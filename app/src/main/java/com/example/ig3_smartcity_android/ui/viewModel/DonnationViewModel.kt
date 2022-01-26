package com.example.ig3_smartcity_android.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ig3_smartcity_android.dataAccess.configuration.RetrofitConfigurationService
import com.example.ig3_smartcity_android.model.NetworkError
import com.example.ig3_smartcity_android.utils.errors.NoConnectivityException
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DonnationViewModel(application: Application) :AndroidViewModel(application) {

    private val _error : MutableLiveData<NetworkError> = MutableLiveData()
    val error :LiveData<NetworkError> = _error

    private var apiWebServices = RetrofitConfigurationService.getInstance(application).apiWebServices()

    fun addNewMeal(namePart:MultipartBody.Part,descriptionPart:MultipartBody.Part,portionNumberPart:MultipartBody.Part,
                   userFkPart:MultipartBody.Part,categoryFkPart:MultipartBody.Part, image:MultipartBody.Part, token: String){
        apiWebServices.addMeal(namePart,descriptionPart,portionNumberPart,userFkPart,categoryFkPart, image, "Bearer $token").enqueue(object :Callback<String?>{
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                if(response.isSuccessful){
                    _error.value = NetworkError.NO_ERROR_DETECTED
                }else{
                    _error.value = NetworkError.REQUEST_ERROR
                }
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                if(t is NoConnectivityException){
                    _error.value = NetworkError.NO_CONNECTION_ERROR
                }else{
                    _error.value = NetworkError.TECHNICAL_ERROR
                }
            }
        })
    }
}