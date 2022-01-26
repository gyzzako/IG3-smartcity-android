package com.example.ig3_smartcity_android.ui.viewModel

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ig3_smartcity_android.dataAccess.configuration.RetrofitConfigurationService
import com.example.ig3_smartcity_android.model.NetworkError
import com.example.ig3_smartcity_android.utils.errors.NoConnectivityException
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val _error :MutableLiveData<NetworkError> = MutableLiveData()
    val error :LiveData<NetworkError> = _error

    private var apiWebServices = RetrofitConfigurationService.getInstance(application).apiWebServices()

    fun createOrder(order: RequestBody, token : String){
        apiWebServices.createOrder(order, "Bearer $token").enqueue(object :Callback<String> {
            override fun onResponse(@NonNull call: Call<String>, @NonNull response: Response<String>){
                if(response.isSuccessful){
                    _error.value = NetworkError.NO_ERROR_DETECTED
                }else{
                    if(response.code()==409){
                        _error.value = NetworkError.MEAL_ALREADY_CLAIMED
                    }else{
                        _error.value = NetworkError.REQUEST_ERROR
                    }
                }
            }

            override fun onFailure(@NonNull call: Call<String>, @NonNull t: Throwable) {
                if(t is NoConnectivityException){
                    _error.value = NetworkError.NO_CONNECTION_ERROR
                }else{
                    _error.value = NetworkError.TECHNICAL_ERROR
                }
            }
        })
    }
}