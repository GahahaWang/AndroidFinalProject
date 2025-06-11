package com.example.sharedclassapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sharedclassapp.Course

@Entity(tableName = "friends")
data class FriendEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val friendCode: String? = null,
    val subject: String? = null,
    val school: String? = null,
    val courseListJson: String? = null // 新增課表欄位
)
