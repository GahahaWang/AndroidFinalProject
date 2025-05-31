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
    val periodMap = mapOf(
        "第一節" to 1, "第二節" to 2, "第三節" to 3, "第四節" to 4, "第五節" to 5,
        "第六節" to 6, "第七節" to 7, "第八節" to 8, "第九節" to 9, "第十節" to 10,
        "第十一節" to 11, "第十二節" to 12, "第十三節" to 13, "第十四節" to 14

    )
    val periods = listOf(
        "第一節", "第二節", "第三節", "第四節", "第五節",
        "第六節", "第七節", "第八節", "第九節", "第十節",
        "第十一節", "第十二節", "第十三節", "第十四節"
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
            Row(modifier = Modifier.fillMaxWidth()
                .height(50.dp)) {
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
                        it.dayOfWeek == dayIdx && periodMap[it.startTime]!! <= periodIdx +1 && periodMap[it.endTime]!! >= periodIdx +1
                    }
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(2.dp)
                            .height(50.dp),
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