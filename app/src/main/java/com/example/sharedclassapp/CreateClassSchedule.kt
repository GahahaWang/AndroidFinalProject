package com.example.sharedclassapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sharedclassapp.viewmodel.CourseViewModel
class CreateClassSchedule {
}



@Composable
fun HomeScreen() {
    val viewModel: CourseViewModel = viewModel()
    val courseList = viewModel.courseList
    val reverseDayMap = mapOf(1 to "一", 2 to "二", 3 to "三", 4 to "四", 5 to "五", 6 to "六", 7 to "日")
    val sortedCourses = courseList.sortedWith(compareBy({ it.dayOfWeek }, { it.startTime }))

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("我的課表", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(sortedCourses) { course ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = if (course.courseCode.isNullOrBlank()) course.name
                            else "${course.name} (${course.courseCode})",
                            fontWeight = FontWeight.Bold
                        )
                        val dayStr = reverseDayMap[course.dayOfWeek] ?: "?"
                        Text("時間：星期$dayStr ${course.startTime} - ${course.endTime}")
                        Text("教授：${course.teacher}")
                        Text("教室：${course.classroom}")
                    }
                }
            }
        }
    }
}