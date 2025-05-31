package com.example.sharedclassapp.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedclassapp.Friend
import com.example.sharedclassapp.database.FriendDatabase
import com.example.sharedclassapp.database.FriendEntity
import kotlinx.coroutines.launch

class FriendViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = FriendDatabase.getDatabase(application).friendDao()
    private val _friendList = mutableStateListOf<Friend>()
    val friendList: List<Friend> get() = _friendList

    init {
        viewModelScope.launch {
            val storedFriends = dao.getAll().map {
                Friend(
                    id = it.id,
                    name = it.name,

                    friendCode = it.friendCode
                )
            }
            _friendList.addAll(storedFriends)
        }
    }

    fun addFriend(friend: Friend) {
        viewModelScope.launch {
            val id = dao.insert(
                FriendEntity(
                    name = friend.name,
                    friendCode = friend.friendCode
                )
            ).toInt()

            _friendList.add(
                Friend(
                    id = id,
                    name = friend.name,
                    friendCode = friend.friendCode
                )
            )
        }
    }

    fun deleteFriend(friend: Friend) {
        viewModelScope.launch {
            dao.delete(
                FriendEntity(
                    id = friend.id,
                    name = friend.name,
                    friendCode = friend.friendCode
                )
            )
            _friendList.remove(friend)
        }
    }
}
