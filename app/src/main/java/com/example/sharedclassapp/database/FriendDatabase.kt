package com.example.sharedclassapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FriendEntity::class], version = 1)
abstract class FriendDatabase : RoomDatabase() {
    abstract fun friendDao(): FriendDao

    companion object {
        @Volatile private var INSTANCE: FriendDatabase? = null

        fun getDatabase(context: Context): FriendDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    FriendDatabase::class.java,
                    "friend_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}

