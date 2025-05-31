package com.example.sharedclassapp.database

import androidx.room.*

@Dao
interface FriendDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(friend: FriendEntity): Long

    @Delete
    suspend fun delete(friend: FriendEntity)

    @Query("SELECT * FROM friends ORDER BY name ASC")
    suspend fun getAll(): List<FriendEntity>
}