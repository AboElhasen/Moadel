package app.mob.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BacMarksCalculator()
        }
    }
}

@Composable
fun BacMarksCalculator() {
    val subjects = remember {
        mutableStateListOf(
            Subject("الفلسفة", 2, ""),
            Subject("الرياضيات", 6, ""),
            Subject("الكهرباء", 7, ""),
            Subject("الفيزياء", 6, ""),
            Subject("اللغة العربية", 3, ""),
            Subject("التربية البدنية", 1, ""),
            Subject("اللغة الفرنسية", 2, ""),
            Subject("اللغة الإنجليزية", 2, ""),
            Subject("التربية الإسلامية", 2, ""),
            Subject("التاريخ والجغرافيا", 2, "")
        )
    }

    var average by remember { mutableStateOf(0.0) }

    fun calculateAverage() {
        var sum = 0.0
        var coeffSum = 0
        subjects.forEach { subject ->
            val mark = subject.mark.toDoubleOrNull()
            if (mark != null && mark in 0.0..20.0) {
                sum += mark * subject.coeff
                coeffSum += subject.coeff
            }
        }
        average = if (coeffSum > 0) sum / coeffSum else 0.0
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // العنوان
        Text(
            text = "📚 حاسبة معدل البكالوريا",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // قائمة المواد
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(subjects) { subject ->
                SubjectItem(
                    subject = subject,
                    onMarkChange = { newMark ->
                        val index = subjects.indexOf(subject)
                        subjects[index] = subject.copy(mark = newMark)
                        calculateAverage()
                    }
                )
            }
        }

        // عرض النتيجة
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            elevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("🎓 المعدل العام", fontSize = 18.sp)
                Text(
                    text = String.format("%.2f", average),
                    fontSize = 48.sp,
                    color = if (average >= 10) androidx.compose.ui.graphics.Color.Green else androidx.compose.ui.graphics.Color.Red
                )
                Text(
                    text = if (average >= 10) "✅ ناجح" else "❌ غير ناجح",
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun SubjectItem(subject: Subject, onMarkChange: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${subject.name} (${subject.coeff})",
            modifier = Modifier.weight(2f),
            fontSize = 16.sp
        )
        
        TextField(
            value = subject.mark,
            onValueChange = onMarkChange,
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            placeholder = { Text("0-20") },
            singleLine = true
        )
    }
}

data class Subject(
    val name: String,
    val coeff: Int,
    val mark: String
)