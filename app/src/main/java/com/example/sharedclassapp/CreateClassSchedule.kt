package com.example.sharedclassapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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

@Composable
fun HomeScreen(modifier: Modifier) {
    val viewModel: CourseViewModel = viewModel()
    val courseList = viewModel.courseList
    val days = listOf("一", "二", "三", "四", "五", "六", "日")
    val periods = listOf(
        "第一節", "第二節", "第三節", "第四節", "第五節",
        "第六節", "第七節", "第八節", "第九節", "第十節"
    )

    // 建立一個 map 方便查詢課程
    val courseMap = courseList.associateBy { Pair(it.dayOfWeek, it.startTime) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text("我的課表", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(8.dp))

        // 表頭
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("", modifier = Modifier.weight(0.8f)) // 左上角空白
            days.forEach { day ->
                Text(
                    text = "星期$day",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // 表格內容
        periods.forEachIndexed { periodIdx, period ->
            Row(modifier = Modifier.fillMaxWidth()) {
                // 節次欄
                Text(
                    text = period,
                    modifier = Modifier
                        .weight(0.8f)
                        .padding(2.dp),
                    fontWeight = FontWeight.Bold
                )
                // 7 天欄
                for (dayIdx in 1..7) {
                    val course = courseList.find {
                        it.dayOfWeek == dayIdx && it.startTime == period
                    }
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(2.dp),
                        elevation = 2.dp
                    ) {
                        Column(modifier = Modifier.padding(4.dp)) {
                            if (course != null) {
                                Text(
                                    text = course.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = MaterialTheme.typography.body2.fontSize
                                )
                                Text(
                                    text = course.classroom,
                                    fontSize = MaterialTheme.typography.caption.fontSize
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}