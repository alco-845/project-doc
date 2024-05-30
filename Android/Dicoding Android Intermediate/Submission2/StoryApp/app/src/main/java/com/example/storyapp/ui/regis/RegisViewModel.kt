package com.example.storyapp.ui.regis

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.AppRepository
import com.example.storyapp.data.remote.response.DataResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisViewModel(private val repository: AppRepository) : ViewModel() {
    private val _regisUser = MutableLiveData<String>()
    val regisUser: LiveData<String> = _regisUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    val message = MutableLiveData<String>()

    fun signUpUser(name: String, email: String, password: String) {
        _isLoading.value = true
        val response: Call<DataResponse> = repository.regisUser(name, email, password)
        response.enqueue(object : Callback<DataResponse> {
            override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _regisUser.value = response.body()!!.message
                    message.value = response.body()!!.message
                } else {
                    _isLoading.value = false
                    message.value = response.message()
                }
            }

            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                _isLoading.value = false
                message.value = t.message
            }
        })
    }
}