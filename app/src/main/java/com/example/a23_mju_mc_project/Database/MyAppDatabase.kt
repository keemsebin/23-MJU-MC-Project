package com.example.a23_mju_mc_project.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.a23_mju_mc_project.Feed
import com.example.a23_mju_mc_project.FeedDao
import com.example.a23_mju_mc_project.User
import com.example.a23_mju_mc_project.UserDao

@Database(entities = [User::class, Feed::class], version = 2)
abstract class MyAppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun feedDao(): FeedDao

    companion object {
        @Volatile
        private var INSTANCE: MyAppDatabase? = null

        fun getDatabase(context: Context): MyAppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyAppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }

        }

    }
}