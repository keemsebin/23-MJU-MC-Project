package com.example.a23_mju_mc_project

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class FeedDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val db = MyAppDatabase.getDatabase(application)

    fun getFeedById(feedId: Int): LiveData<Feed> {
        return db.feedDao().getFeedById(feedId)
    }
}

