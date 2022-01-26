package com.example.ig3_smartcity_android.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ig3_smartcity_android.dataAccess.configuration.RetrofitConfigurationService
import com.example.ig3_smartcity_android.model.NetworkError
import com.example.ig3_smartcity_android.model.User
import com.example.ig3_smartcity_android.services.mappers.UserMapper
import com.example.ig3_smartcity_android.utils.errors.NoConnectivityException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegistrationViewModel(application: Application) : AndroidViewModel(application)  {

    private val _error :MutableLiveData<NetworkError> = MutableLiveData()
    val error :LiveData<NetworkError> = _error


    private var apiWebServices = RetrofitConfigurationService.getInstance(application).apiWebServices()
    private var userMapper = UserMapper

    fun registerUser(user: User){
        apiWebServices.registerUser(userMapper.mapToUserDto(user)).enqueue(object :Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful){
                    _error.value = NetworkError.NO_ERROR_DETECTED
                }else{
                    if(response.code()==409){
                        _error.value = NetworkError.USER_ALREADY_EXIST
                    }else{
                        _error.value = NetworkError.REQUEST_ERROR
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                if(t is NoConnectivityException){
                    _error.value = NetworkError.NO_CONNECTION_ERROR
                }else{
                    _error.value = NetworkError.TECHNICAL_ERROR
                }
            }
        })
    }
}