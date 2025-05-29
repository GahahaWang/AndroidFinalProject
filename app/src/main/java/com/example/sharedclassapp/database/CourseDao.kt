package com.example.sharedclassapp.database

import androidx.room.*

@Dao
interface CourseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(course: CourseEntity): Long

    @Delete
    suspend fun delete(course: CourseEntity)

    @Query("SELECT * FROM courses ORDER BY dayOfWeek ASC, startTime ASC")
    suspend fun getAll(): List<CourseEntity>
}