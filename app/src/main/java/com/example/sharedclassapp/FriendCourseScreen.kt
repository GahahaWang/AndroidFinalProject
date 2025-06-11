import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sharedclassapp.Course
import com.example.sharedclassapp.Friend
import com.google.gson.Gson

@Composable
fun FriendCourseScreen(friend: Friend, modifier: Modifier = Modifier) {

    // 解析朋友課表 JSON
    val courseList = remember(friend.courseListJson) {
        try {
            Gson().fromJson(
                friend.courseListJson,
                Array<Course>::class.java
            )?.toList() ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

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

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text(
            "${friend.name} 的課表  ${friend.school ?: ""} ${friend.subject ?: ""}",
            style = MaterialTheme.typography.h6
        )
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
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
                        it.dayOfWeek == dayIdx &&
                                periodMap[it.startTime]!! <= periodIdx + 1 &&
                                periodMap[it.endTime]!! >= periodIdx + 1
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