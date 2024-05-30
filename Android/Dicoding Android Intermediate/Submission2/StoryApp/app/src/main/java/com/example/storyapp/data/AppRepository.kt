package com.example.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.storyapp.data.local.room.AppDatabase
import com.example.storyapp.data.remote.response.ListStoryResponse
import com.example.storyapp.data.remote.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AppRepository(private val appDatabase: AppDatabase, private val apiService: ApiService) {
    fun regisUser(name: String, email: String, password: String) = apiService.regisUser(name, email, password)
    fun loginUser(email: String, password: String) = apiService.loginUser(email, password)

    fun getListStory(): LiveData<PagingData<ListStoryResponse>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 6
            ),
            remoteMediator = StoryRemoteMediator(appDatabase, apiService),
            pagingSourceFactory = {
                appDatabase.appDao().getAllStory()
            }
        ).liveData
    }

    fun getStoryLocation(token: String, location: Int) = apiService.getStoryLocation(token, location)

    fun addStory(token: String, description: RequestBody, file: MultipartBody.Part) = apiService.addStory(token, description, file)
    fun getDetailStory(token: String, id: String) = apiService.getDetailStory(token, id)

    companion object {
        @Volatile
        private var instance: AppRepository? = null
        fun getInstance(
            appDatabase: AppDatabase,
            apiService: ApiService
        ): AppRepository =
            instance ?: synchronized(this) {
                instance ?: AppRepository(appDatabase, apiService)
            }.also { instance = it }
    }
}