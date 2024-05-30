package com.example.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.AppRepository
import com.example.storyapp.data.remote.response.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val repository: AppRepository) : ViewModel() {
    private val _loginUser = MutableLiveData<User>()
    val loginUser: LiveData<User> = _loginUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    val message = MutableLiveData<String>()

    fun signInUser(email: String, password: String) {
        _isLoading.value = true
        val response: Call<User> = repository.loginUser(email, password)
        response.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                _isLoading.value = false
                _loginUser.value = response.body()
                message.value = response.body()!!.message
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                _isLoading.value = false
                message.value = t.message
            }
        })
    }
}