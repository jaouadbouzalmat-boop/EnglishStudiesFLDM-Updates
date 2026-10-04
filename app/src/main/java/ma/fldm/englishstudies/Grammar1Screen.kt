
@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package ma.fldm.englishstudies

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ma.fldm.englishstudies.data.CourseData
import ma.fldm.englishstudies.data.CourseUnit
import ma.fldm.englishstudies.data.Example
import ma.fldm.englishstudies.data.Lesson

private val G1Primary = Color(0xFF2563EB)
private val G1Dark = Color(0xFF173F8F)
private val G1Background = Color(0xFFF2F7FF)
private val G1Text = Color(0xFF172033)
private val G1Muted = Color(0xFF64748B)
private val G1SoftBlue = Color(0xFFEAF2FF)
private val G1SoftGreen = Color(0xFFDCFCE7)

@Composable
fun Grammar1Screen(
    onBack: () -> Unit = {}
) {
    var selectedUnit by remember { mutableStateOf<CourseUnit?>(null) }
    var selectedLesson by remember { mutableStateOf<Lesson?>(null) }

    when {
        selectedLesson != null -> {
            Grammar1LessonScreen(
                lesson = selectedLesson!!,
                lessonNumber = lessonGlobalNumber(selectedLesson!!),
                onBack = { selectedLesson = null }
            )
        }

        selectedUnit != null -> {
            Grammar1UnitScreen(
                unit = selectedUnit!!,
                onBack = { selectedUnit = null },
                onLessonClick = { lesson -> selectedLesson = lesson }
            )
        }

        else -> {
            Grammar1HomeScreen(
                onBack = onBack,
                onUnitClick = { selectedUnit = it }
            )
        }
    }
}

@Composable
private fun Grammar1HomeScreen(
    onBack: () -> Unit,
    onUnitClick: (CourseUnit) -> Unit
) {
    val units = CourseData.grammar1Units

    Scaffold(
        containerColor = G1Background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Grammar 1",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "English Studies • Semester 1",
                            color = Color.White.copy(alpha = 0.86f),
                            fontSize = 11.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = G1Primary
                )
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(G1Background)
                .padding(padding)
                .navigationBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Grammar1Header(
                    unitCount = units.size,
                    lessonCount = units.sumOf { it.lessons.size }
                )
            }

            item {
                SectionTitle(
                    title = "Learning path",
                    subtitle = "Progress from grammatical foundations to verb-phrase analysis."
                )
            }

            itemsIndexed(units) { index, unit ->
                GrammarUnitCard(
                    number = index + 1,
                    unit = unit,
                    onClick = { onUnitClick(unit) }
                )
            }

            item {
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}

@Composable
private fun Grammar1Header(
    unitCount: Int,
    lessonCount: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = G1Primary),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.linearGradient(
                        colors = listOf(G1Primary, G1Dark)
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(62.dp)
                            .background(
                                Color.White.copy(alpha = 0.14f),
                                RoundedCornerShape(17.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(34.dp)
                        )
                    }

                    Spacer(Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Grammar 1",
                            color = Color.White,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Foundations of English Grammar",
                            color = Color.White.copy(alpha = 0.90f),
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "A structured study path built directly from the Grammar 1 course content.",
                    color = Color.White.copy(alpha = 0.93f),
                    fontSize = 14.sp,
                    lineHeight = 21.sp
                )

                Spacer(Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricPill(
                        icon = Icons.Default.School,
                        value = "$unitCount units"
                    )
                    MetricPill(
                        icon = Icons.Default.MenuBook,
                        value = "$lessonCount lessons"
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricPill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String
) {
    Surface(
        shape = RoundedCornerShape(50.dp),
        color = Color.White.copy(alpha = 0.14f)
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 11.dp,
                vertical = 7.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(17.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = value,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun SectionTitle(
    title: String,
    subtitle: String
) {
    Column {
        Text(
            text = title,
            color = G1Text,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = subtitle,
            color = G1Muted,
            fontSize = 13.sp,
            lineHeight = 19.sp
        )
    }
}

@Composable
private fun GrammarUnitCard(
    number: Int,
    unit: CourseUnit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        G1SoftBlue,
                        RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = number.toString(),
                    color = G1Primary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(Modifier.width(13.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = unit.title,
                    color = G1Text,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = unit.description,
                    color = G1Muted,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    maxLines = 3
                )

                Spacer(Modifier.height(7.dp))

                Text(
                    text = "${unit.lessons.size} lessons",
                    color = G1Primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.width(8.dp))

            Text(
                text = "›",
                color = G1Primary,
                fontSize = 29.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun Grammar1UnitScreen(
    unit: CourseUnit,
    onBack: () -> Unit,
    onLessonClick: (Lesson) -> Unit
) {
    Scaffold(
        containerColor = G1Background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Grammar 1",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = unit.title,
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 11.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = G1Primary
                )
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(G1Background)
                .padding(padding)
                .navigationBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                UnitIntroCard(unit)
            }

            item {
                SectionTitle(
                    title = "Lessons",
                    subtitle = "Open a lesson and follow the complete learning sequence."
                )
            }

            itemsIndexed(unit.lessons) { index, lesson ->
                LessonCard(
                    number = index + 1,
                    lesson = lesson,
                    onClick = { onLessonClick(lesson) }
                )
            }
        }
    }
}

@Composable
private fun UnitIntroCard(
    unit: CourseUnit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = G1SoftBlue
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = unit.title,
                color = G1Primary,
                fontSize = 23.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(Modifier.height(7.dp))

            Text(
                text = unit.description,
                color = G1Text,
                fontSize = 14.sp,
                lineHeight = 21.sp
            )
        }
    }
}

@Composable
private fun LessonCard(
    number: Int,
    lesson: Lesson,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(43.dp)
                    .background(
                        G1SoftBlue,
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = number.toString(),
                    color = G1Primary,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = lesson.title,
                    color = G1Text,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 21.sp
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = lesson.objective,
                    color = G1Muted,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    maxLines = 2
                )
            }

            Spacer(Modifier.width(8.dp))

            Text(
                text = "›",
                color = G1Primary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun Grammar1LessonScreen(
    lesson: Lesson,
    lessonNumber: Int,
    onBack: () -> Unit
) {
    var selectedSection by remember { mutableIntStateOf(0) }

    val sections = listOf(
        "Learn",
        "Examples",
        "Practice",
        "Quiz",
        "Review"
    )

    Scaffold(
        containerColor = G1Background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Lesson $lessonNumber",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = lesson.title,
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 11.sp,
                            maxLines = 1
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = G1Primary
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(G1Background)
                .padding(padding)
                .navigationBarsPadding()
        ) {

            ScrollableTabRow(
                selectedTabIndex = selectedSection,
                containerColor = Color.White,
                edgePadding = 8.dp
            ) {
                sections.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedSection == index,
                        onClick = { selectedSection = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    )
                }
            }

            when (selectedSection) {
                0 -> LessonLearnContent(lesson)
                1 -> LessonExamplesContent(lesson)
                2 -> LessonPracticeContent(lesson)
                3 -> LessonQuizContent(lesson)
                4 -> LessonReviewContent(lesson)
            }
        }
    }
}

@Composable
private fun LessonLearnContent(
    lesson: Lesson
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        item {
            InfoCard(
                icon = Icons.Default.TaskAlt,
                title = "Learning objective",
                body = lesson.objective,
                background = G1SoftBlue,
                accent = G1Primary
            )
        }

        item {
            AcademicContentCard(
                title = "Academic explanation",
                body = lesson.content
            )
        }

        item {
            KeyTermsCard(lesson.keyTerms)
        }
    }
}

@Composable
private fun AcademicContentCard(
    title: String,
    body: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = title,
                color = G1Text,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = body,
                color = G1Text,
                fontSize = 15.sp,
                lineHeight = 25.sp
            )
        }
    }
}

@Composable
private fun InfoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    body: String,
    background: Color,
    accent: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = background
        )
    ) {
        Row(
            modifier = Modifier.padding(17.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(24.dp)
            )

            Spacer(Modifier.width(11.dp))

            Column {
                Text(
                    text = title,
                    color = accent,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = body,
                    color = G1Text,
                    fontSize = 14.sp,
                    lineHeight = 21.sp
                )
            }
        }
    }
}

@Composable
private fun KeyTermsCard(
    terms: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = "Key terms",
                color = G1Text,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(11.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                terms.forEach { term ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = G1SoftBlue
                    ) {
                        Text(
                            text = term,
                            modifier = Modifier.padding(
                                horizontal = 10.dp,
                                vertical = 7.dp
                            ),
                            color = G1Primary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LessonExamplesContent(
    lesson: Lesson
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SectionTitle(
                title = "Examples in context",
                subtitle = "Study the examples provided for this lesson."
            )
        }

        itemsIndexed(lesson.examples) { index, example ->
            ExampleCard(
                number = index + 1,
                example = example
            )
        }
    }
}

@Composable
private fun ExampleCard(
    number: Int,
    example: Example
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(17.dp)
        ) {
            Text(
                text = "Example $number",
                color = G1Primary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(7.dp))

            Text(
                text = example.english,
                color = G1Text,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 22.sp
            )

            Spacer(Modifier.height(7.dp))

            Text(
                text = example.french,
                color = G1Muted,
                fontSize = 13.sp,
                lineHeight = 19.sp
            )
        }
    }
}

@Composable
private fun LessonPracticeContent(
    lesson: Lesson
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        item {
            InfoCard(
                icon = Icons.Default.TaskAlt,
                title = "Guided practice",
                body = "Use the questions below to practise the concept before taking the quiz.",
                background = G1SoftBlue,
                accent = G1Primary
            )
        }

        itemsIndexed(lesson.questions) { index, question ->
            PracticeQuestionCard(
                number = index + 1,
                question = question.question,
                options = question.options,
                correctIndex = question.correctAnswerIndex,
                explanation = question.explanation
            )
        }
    }
}

@Composable
private fun PracticeQuestionCard(
    number: Int,
    question: String,
    options: List<String>,
    correctIndex: Int,
    explanation: String
) {
    var selected by remember { mutableStateOf<Int?>(null) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = "Practice $number",
                color = G1Primary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = question,
                color = G1Text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 23.sp
            )

            Spacer(Modifier.height(12.dp))

            options.forEachIndexed { index, option ->
                val selectedOption = selected == index
                val correctOption = index == correctIndex

                val background = when {
                    selected != null && correctOption -> G1SoftGreen
                    selectedOption -> Color(0xFFFEE2E2)
                    else -> Color(0xFFF8FAFC)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(
                            background,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable(
                            enabled = selected == null
                        ) {
                            selected = index
                        }
                        .padding(13.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${('A'.code + index).toChar()}.",
                        color = G1Primary,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.width(9.dp))

                    Text(
                        text = option,
                        color = G1Text,
                        modifier = Modifier.weight(1f),
                        fontSize = 14.sp
                    )

                    if (selected != null && correctOption) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF16A34A)
                        )
                    }
                }
            }

            if (selected != null) {
                Spacer(Modifier.height(11.dp))
                Text(
                    text = explanation,
                    color = G1Muted,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
            }
        }
    }
}

@Composable
private fun LessonQuizContent(
    lesson: Lesson
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var selected by remember { mutableStateOf<Int?>(null) }
    var score by remember { mutableIntStateOf(0) }

    val questions = lesson.questions

    if (questions.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No quiz questions available for this lesson.",
                color = G1Muted
            )
        }
        return
    }

    val question = questions[currentIndex]

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = G1SoftBlue
                )
            ) {
                Column(Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Quiz",
                            color = G1Primary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${currentIndex + 1} / ${questions.size}",
                            color = G1Muted,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(Modifier.height(7.dp))

                    Text(
                        text = "Score: $score",
                        color = G1Text,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text(
                        text = question.question,
                        color = G1Text,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 24.sp
                    )

                    Spacer(Modifier.height(14.dp))

                    question.options.forEachIndexed { index, option ->
                        val isCorrect = index == question.correctAnswerIndex
                        val isSelected = selected == index

                        val bg = when {
                            selected != null && isCorrect -> G1SoftGreen
                            selected != null && isSelected -> Color(0xFFFEE2E2)
                            else -> Color(0xFFF8FAFC)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .background(
                                    bg,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable(enabled = selected == null) {
                                    selected = index
                                    if (index == question.correctAnswerIndex) {
                                        score++
                                    }
                                }
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${('A'.code + index).toChar()}.",
                                color = G1Primary,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.width(9.dp))

                            Text(
                                text = option,
                                modifier = Modifier.weight(1f),
                                color = G1Text,
                                fontSize = 14.sp
                            )

                            if (selected != null && isCorrect) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF16A34A)
                                )
                            }
                        }
                    }

                    if (selected != null) {
                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = question.explanation,
                            color = G1Muted,
                            fontSize = 13.sp,
                            lineHeight = 19.sp
                        )

                        Spacer(Modifier.height(12.dp))

                        Button(
                            onClick = {
                                if (currentIndex < questions.lastIndex) {
                                    currentIndex++
                                    selected = null
                                } else {
                                    currentIndex = 0
                                    selected = null
                                    score = 0
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = G1Primary
                            )
                        ) {
                            Text(
                                text = if (currentIndex < questions.lastIndex) {
                                    "Next Question"
                                } else {
                                    "Restart Quiz"
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LessonReviewContent(
    lesson: Lesson
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        item {
            InfoCard(
                icon = Icons.Default.CheckCircle,
                title = "Lesson objective",
                body = lesson.objective,
                background = G1SoftGreen,
                accent = Color(0xFF16A34A)
            )
        }

        item {
            AcademicContentCard(
                title = "Key terms to review",
                body = lesson.keyTerms.joinToString(" • ")
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text(
                        text = "Before moving on",
                        color = G1Text,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Make sure you can explain the main concept of the lesson and use the examples correctly.",
                        color = G1Muted,
                        fontSize = 14.sp,
                        lineHeight = 21.sp
                    )
                }
            }
        }
    }
}

/*
 * The current CourseData is the source of truth for Grammar 1.
 * It contains 4 units and 12 lessons.
 */
private fun lessonGlobalNumber(
    lesson: Lesson
): Int {
    var number = 0

    for (unit in CourseData.grammar1Units) {
        for (item in unit.lessons) {
            number++

            if (item.id == lesson.id) {
                return number
            }
        }
    }

    return number
}
