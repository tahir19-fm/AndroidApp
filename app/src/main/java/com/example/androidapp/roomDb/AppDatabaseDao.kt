package com.example.androidapp.roomDb



import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface AppDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE )
    suspend fun insert(list:List<NewsModel>)


    @Query("SELECT * from app_tbl")
    suspend fun getList():List<NewsModel>

    @Query("DELETE from app_tbl")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNews(newsModel: NewsModel)


}