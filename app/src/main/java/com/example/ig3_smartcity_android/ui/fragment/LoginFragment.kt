package com.example.ig3_smartcity_android.ui.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ig3_smartcity_android.R
import com.example.ig3_smartcity_android.databinding.FragmentLoginBinding
import com.example.ig3_smartcity_android.model.LoginUser
import com.example.ig3_smartcity_android.model.NetworkError
import com.example.ig3_smartcity_android.ui.actitvity.MainActivity
import com.example.ig3_smartcity_android.ui.actitvity.RegistrationActivity
import com.example.ig3_smartcity_android.ui.error.ApiError
import com.example.ig3_smartcity_android.ui.viewModel.LoginUserViewModel


class LoginFragment : Fragment() {

    private lateinit var usernameText : EditText
    private lateinit var passwordText: EditText
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: FragmentLoginBinding
    private lateinit var loginUserViewModel: LoginUserViewModel
    private var areAllFieldsChecked :Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

       loginUserViewModel = ViewModelProvider(this)[LoginUserViewModel::class.java]
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        binding.viewModel = loginUserViewModel
        binding.lifecycleOwner = this

        sharedPreferences = requireActivity().getSharedPreferences(getString(R.string.sharedPref),Context.MODE_PRIVATE)

        usernameText = binding.username
        passwordText = binding.password

        loginUserViewModel.error.observe(viewLifecycleOwner) { error: NetworkError -> ApiError.showError(error, this.context)
        }
        binding.loginButtonID.setOnClickListener{
            areAllFieldsChecked = areFiledsNotEmpty()
            if(areAllFieldsChecked){
                loginUser()
            }
        }
        binding.signupID.setOnClickListener{
            goToRegisterActivity()
        }

        loginUserViewModel.jwt.observe(viewLifecycleOwner){
            token :String ->this.preferencesSaved(token)
            goToMainActivity()
        }
        return binding.root

    }

    private fun preferencesSaved(token : String){
        val editor : SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(getString(R.string.token),token).apply()
    }

    private fun areFiledsNotEmpty(): Boolean {
        if (usernameText.length() == 0) {
            usernameText.error = resources.getText(R.string.username_empty_message)
            return false
        }
        if (passwordText.length() == 0) {
            passwordText.error = resources.getText(R.string.password_empty_message)
            return false
        }
        return true
    }

    private fun goToMainActivity(){
        val intent = Intent(requireActivity().applicationContext,MainActivity::class.java)
        startActivity(intent)
    }

    private fun goToRegisterActivity(){
        val intent = Intent(requireActivity().applicationContext,RegistrationActivity::class.java)
        startActivity(intent)
    }

    /**
     * calls the loginUserViewModel to log in the user.
     */
    private fun loginUser(){
        val login = LoginUser(
            usernameText.text.toString(),
            passwordText.text.toString()
        )
        loginUserViewModel.loginUser(login)
    }

}