package com.example.sharedclassapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val teacher: String,
    val classroom: String,
    val dayOfWeek: Int,
    val startTime: String,
    val endTime: String,
    val courseCode: String? = null
)
