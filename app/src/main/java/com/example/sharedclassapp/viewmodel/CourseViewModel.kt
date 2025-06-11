package com.example.sharedclassapp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedclassapp.Course
import com.example.sharedclassapp.database.CourseDatabase
import com.example.sharedclassapp.database.CourseEntity
import kotlinx.coroutines.launch

class CourseViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = CourseDatabase.getDatabase(application).courseDao()
    private val _courseList = mutableStateListOf<Course>()
    val courseList: SnapshotStateList<Course> get() = _courseList

    init {
        viewModelScope.launch {
            val storedCourses = dao.getAll().map {
                Course(
                    id = it.id,
                    name = it.name,
                    teacher = it.teacher,
                    classroom = it.classroom,
                    dayOfWeek = it.dayOfWeek,
                    startTime = it.startTime,
                    endTime = it.endTime,
                    courseCode = it.courseCode
                )
            }
            _courseList.addAll(storedCourses)
        }
    }

    fun addCourse(course: Course) {
        viewModelScope.launch {
            val id = dao.insert(
                CourseEntity(
                    name = course.name,
                    teacher = course.teacher,
                    classroom = course.classroom,
                    dayOfWeek = course.dayOfWeek,
                    startTime = course.startTime,
                    endTime = course.endTime,
                    courseCode = course.courseCode
                )
            ).toInt()

            _courseList.add(
                Course(
                    id = id,
                    name = course.name,
                    teacher = course.teacher,
                    classroom = course.classroom,
                    dayOfWeek = course.dayOfWeek,
                    startTime = course.startTime,
                    endTime = course.endTime,
                    courseCode = course.courseCode
                )
            )
        }
    }

    fun deleteCourse(course: Course) {
        viewModelScope.launch {
            dao.delete(
                CourseEntity(
                    id = course.id,
                    name = course.name,
                    teacher = course.teacher,
                    classroom = course.classroom,
                    dayOfWeek = course.dayOfWeek,
                    startTime = course.startTime,
                    endTime = course.endTime,
                    courseCode = course.courseCode
                )
            )
            _courseList.remove(course)
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _courseList.clear()
            val storedCourses = dao.getAll().map {
                Course(
                    id = it.id,
                    name = it.name,
                    teacher = it.teacher,
                    classroom = it.classroom,
                    dayOfWeek = it.dayOfWeek,
                    startTime = it.startTime,
                    endTime = it.endTime,
                    courseCode = it.courseCode
                )
            }
            _courseList.addAll(storedCourses)
        }
    }
}
