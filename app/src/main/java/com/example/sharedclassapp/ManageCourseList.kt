package com.example.sharedclassapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sharedclassapp.viewmodel.CourseViewModel


data class Course(
    val id: Int,
    val name: String,
    val teacher: String,
    val classroom: String,
    val dayOfWeek: Int,
    val startTime: String,
    val endTime: String,
    val courseCode: String? = null
)

@Composable
fun ManageCourseListScreen(modifier: Modifier) {
    val viewModel: CourseViewModel = viewModel()
    val courseList = viewModel.courseList
    var showDialog by remember { mutableStateOf(false) }
    val reverseDayMap = mapOf(1 to "一", 2 to "二", 3 to "三", 4 to "四", 5 to "五", 6 to "六", 7 to "日")
    val sortedCourses = courseList.sortedWith(compareBy({ it.dayOfWeek }, { it.startTime }))

    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
    )
    {
        LazyColumn {
            items(sortedCourses) { course ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    elevation = 4.dp,
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = Color(0xFFE0E0E0)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (course.courseCode.isNullOrBlank()) course.name
                                else "${course.name} (${course.courseCode})",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            val dayStr = reverseDayMap[course.dayOfWeek] ?: "?"
                            Text("上課時間: 星期$dayStr ${course.startTime} - ${course.endTime}")
                            Text("教授: ${course.teacher}")
                            Text("教室: ${course.classroom}")
                        }
                        IconButton(
                            onClick = { viewModel.deleteCourse(course) },
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.Red, shape = CircleShape)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddCourseDialog(
            onAdd = {
                viewModel.addCourse(it)
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            onClick = { showDialog = true },
            backgroundColor = Color(0xFF0D47A1),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Course", tint = Color.White)
        }
    }
}

@Composable
fun AddCourseDialog(
    onAdd: (Course) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var teacher by remember { mutableStateOf("") }
    var classroom by remember { mutableStateOf("") }
    var courseCode by remember { mutableStateOf("") }
    var selectedDay by remember { mutableStateOf("星期") }
    var selectedStartTime by remember { mutableStateOf("開始") }
    var selectedEndTime by remember { mutableStateOf("結束") }
    val days = listOf("一", "二", "三", "四", "五", "六", "日")
    val start = listOf("08:20", "09:20", "10:20", "11:15", "12:10", "13:10", "14:10", "15:10", "16:05", "17:30", "18:30", "19:25", "20:20", "21:15")
    val end = listOf("09:10", "10:10", "11:10", "12:05", "13:00", "14:00", "15:00", "16:00", "16:55", "18:20", "19:20", "20:15", "21:20", "21:15", "22:05")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("新增課堂") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("課名") },
                    leadingIcon = { Icon(Icons.Default.Book, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = teacher,
                    onValueChange = { teacher = it },
                    label = { Text("教授") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = classroom,
                    onValueChange = { classroom = it },
                    label = { Text("教室") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    DropdownMenuBox(
                        label = "星期",
                        options = days,
                        selected = selectedDay,
                        onSelected = { selectedDay = it },
                        modifier = Modifier.weight(1f)
                    )
                    DropdownMenuBox(
                        label = "開始",
                        options = start,
                        selected = selectedStartTime,
                        onSelected = { selectedStartTime = it },
                        modifier = Modifier.weight(1f)
                    )
                    DropdownMenuBox(
                        label = "結束",
                        options = end,
                        selected = selectedEndTime,
                        onSelected = { selectedEndTime = it },
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = courseCode,
                    onValueChange = { courseCode = it },
                    label = { Text("課號（非必要）") },
                    leadingIcon = { Icon(Icons.Default.Info, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(onClick = onDismiss) { Text("取消") }
                Button(
                    onClick = {
                        if (name.isNotBlank() && teacher.isNotBlank() && classroom.isNotBlank() &&
                            selectedDay != "星期" && selectedStartTime != "開始" && selectedEndTime != "結束") {
                            val dayOfWeek = days.indexOf(selectedDay) + 1
                            onAdd(
                                Course(
                                    id = 0,
                                    name = name,
                                    teacher = teacher,
                                    classroom = classroom,
                                    dayOfWeek = dayOfWeek,
                                    startTime = selectedStartTime,
                                    endTime = selectedEndTime,
                                    courseCode = courseCode.ifBlank { null }
                                )
                            )
                        }
                    }
                ) { Text("儲存") }
            }
        }
    )
}

@Composable
fun DropdownMenuBox(
    label: String,
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {  // use the modifier passed in
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) },
            enabled = true
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    onSelected(option)
                    expanded = false
                }) {
                    Text(option)
                }
            }
        }
        Spacer(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Transparent)
                .clickable { expanded = true }
        )
    }
}
