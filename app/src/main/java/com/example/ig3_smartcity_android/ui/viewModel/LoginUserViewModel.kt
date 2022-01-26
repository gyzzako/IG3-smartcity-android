package com.example.ig3_smartcity_android.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ig3_smartcity_android.dataAccess.configuration.RetrofitConfigurationService
import com.example.ig3_smartcity_android.model.LoginUser
import com.example.ig3_smartcity_android.model.NetworkError
import com.example.ig3_smartcity_android.services.mappers.UserLoginMapper
import com.example.ig3_smartcity_android.utils.errors.NoConnectivityException
import retrofit2.Call
import retrofit2.Response


class LoginUserViewModel(application: Application) : AndroidViewModel(application) {

    private val _error : MutableLiveData<NetworkError> = MutableLiveData()
    val error : LiveData<NetworkError> = _error

    private val _jwt : MutableLiveData<String> = MutableLiveData()
    val jwt : LiveData<String> = _jwt;

    private var apiWebServices = RetrofitConfigurationService.getInstance(application).apiWebServices()
    private var userLoginMapper = UserLoginMapper

    fun loginUser(loginUser : LoginUser) {
        apiWebServices.userLogin(userLoginMapper.mapToLoginUserDTO(loginUser)).enqueue(object :retrofit2.Callback<String>{
            override  fun onResponse(call : Call<String>,response : Response<String>){
                if(response.isSuccessful){
                    _error.value = NetworkError.NO_ERROR_DETECTED
                    _jwt.value = response.body()!!;

                }else{
                    _error.value = NetworkError.REQUEST_ERROR
                }
            }

            override  fun onFailure(call: Call<String>, t:Throwable){
                if(t is NoConnectivityException){
                    _error.value = NetworkError.NO_CONNECTION_ERROR
                }else{
                    _error.value = NetworkError.TECHNICAL_ERROR
                }

            }
        })
    }

}