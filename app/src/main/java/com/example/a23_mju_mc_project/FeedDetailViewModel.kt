package com.example.a23_mju_mc_project

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.a23_mju_mc_project.Database.MyAppDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FeedDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val db = MyAppDatabase.getDatabase(application)

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://naveropenapi.apigw.ntruss.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val naverApi: NaverApi = retrofit.create(NaverApi::class.java)

    fun getFeedById(feedId: Int): LiveData<Feed> {
        return db.feedDao().getFeedById(feedId)
    }
    fun analyzeSentiment(content: String): LiveData<String> {
        val result = MutableLiveData<String>()

        naverApi.analyzeSentiment(mapOf("content" to content)).enqueue(object : Callback<SentimentResponse> {
            override fun onResponse(call: Call<SentimentResponse>, response: Response<SentimentResponse>) {
                if (response.isSuccessful) {
                    result.value = response.body()?.document?.sentiment
                }
            }

            override fun onFailure(call: Call<SentimentResponse>, t: Throwable) {
                // Log the error
            }
        })

        return result
    }

}

