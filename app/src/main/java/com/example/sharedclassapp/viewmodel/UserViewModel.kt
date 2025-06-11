package com.example.sharedclassapp.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

data class UserProfile(
    val uuid: String,
    val name: String,
    val school: String,
    val subject: String
)

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs: SharedPreferences =
        application.getSharedPreferences("user_profile", Context.MODE_PRIVATE)

    private val _profile = MutableStateFlow(
        UserProfile(
            uuid = prefs.getString("uuid", null) ?: UUID.randomUUID().toString(),
            name = prefs.getString("name", "") ?: "",
            school = prefs.getString("school", "") ?: "",
            subject = prefs.getString("subject", "") ?: ""
        )
    )
    val profile: StateFlow<UserProfile> = _profile

    init {
        // 初始化 UUID
        if (!prefs.contains("uuid")) {
            prefs.edit().putString("uuid", _profile.value.uuid).apply()
        }
    }

    fun updateProfile(name: String, school: String, subject: String) {
        _profile.value = _profile.value.copy(name = name, school = school, subject = subject)
        prefs.edit()
            .putString("name", name)
            .putString("school", school)
            .putString("subject", subject)
            .apply()
    }
}