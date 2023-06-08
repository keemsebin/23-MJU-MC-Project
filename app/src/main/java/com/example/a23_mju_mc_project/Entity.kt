package com.example.a23_mju_mc_project

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity(tableName = "User", indices = [Index(value = ["Nickname"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true) val user_Id: Int = 0,
    @ColumnInfo(name = "Nickname") val nickname: String,
    @ColumnInfo(name = "alarm_Time") val alarm_Time: String,
    @ColumnInfo(name = "push_Mes") val push_Mes: String
)

@Entity(tableName = "Feed", foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["Nickname"], childColumns = ["Nickname"], onDelete = ForeignKey.CASCADE)])
data class Feed(
    @PrimaryKey(autoGenerate = true) val feed_Id: Int = 0,
    @ColumnInfo(name = "Nickname") val nickname: String,
    @ColumnInfo(name = "Picture") val picture: String,
    @ColumnInfo(name = "upload_Date") val upload_Date: String,
    @ColumnInfo(name = "upload_Time") val upload_Time: String,
    @ColumnInfo(name = "feed_Text") val feed_Text: String
)

@Dao
interface UserDao {
    @Query("SELECT * FROM User")
    fun getAllUsers(): List<User>

    @Query("SELECT * FROM User WHERE user_Id = :userId")
    fun getUserById(userId: Int): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Query("DELETE FROM User")
    fun deleteAllUsers()
}

@Dao
interface FeedDao {
    @Query("SELECT * FROM Feed")
    fun getAllFeeds(): List<Feed>

    @Query("SELECT * FROM Feed WHERE feed_Id = :feedId")
    fun getFeedById(feedId: Int): LiveData<Feed>

    @Query("SELECT * FROM Feed WHERE Nickname = :nickname")
    fun getFeedsByNickname(nickname: String): List<Feed>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFeed(feed: Feed)

    @Delete
    fun deleteFeed(feed: Feed)

    @Query("DELETE FROM Feed")
    fun deleteAllFeeds()
}

@Database(entities = [User::class, Feed::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun feedDao(): FeedDao
}
