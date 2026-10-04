@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package ma.fldm.englishstudies

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

/*
 * LANGUAGE AND TERMINOLOGY 1
 * Semester 1 — English Studies
 *
 * Academic and pedagogical module:
 * - Introduction to terminology
 * - Academic English
 * - Word formation
 * - Terminology used in English Studies
 * - Vocabulary practice
 * - Read Aloud
 * - Quiz
 * - Terminology task
 */

private val PrimaryBlue = Color(0xFF2563EB)
private val DarkBlue = Color(0xFF1D4ED8)
private val LightBlue = Color(0xFFEFF6FF)
private val Green = Color(0xFF16A34A)
private val LightGreen = Color(0xFFDCFCE7)
private val Red = Color(0xFFDC2626)
private val LightRed = Color(0xFFFEE2E2)
private val Orange = Color(0xFFF59E0B)
private val LightOrange = Color(0xFFFEF3C7)
private val Purple = Color(0xFF7C3AED)
private val LightPurple = Color(0xFFF3E8FF)
private val TextDark = Color(0xFF172033)
private val TextGray = Color(0xFF64748B)
private val Background = Color(0xFFF8FAFC)

@Composable
fun LanguageTerminology1Screen(
    onBack: () -> Unit = {}
) {
    var openLesson by remember { mutableStateOf(false) }

    if (openLesson) {
        TerminologyLessonOneScreen(onBack = { openLesson = false })
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Language and Terminology 1",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "English Studies • Semester 1",
                            color = Color.White.copy(alpha = .85f),
                            fontSize = 12.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryBlue
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                CourseHeader()
                Spacer(Modifier.height(8.dp))
                ProgressCard()
                Spacer(Modifier.height(10.dp))
                Text(
                    "Course contents",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            }

            item {
                LessonItem(
                    1,
                    "Introduction to Terminology",
                    "Understand terms, terminology and their role in academic study.",
                    "📚",
                    true
                ) { openLesson = true }
            }

            item {
                LessonItem(
                    2,
                    "Academic English",
                    "Distinguish general English from academic language.",
                    "🎓",
                    false
                ) {}
            }

            item {
                LessonItem(
                    3,
                    "Word Formation",
                    "Study prefixes, suffixes, compounds and conversion.",
                    "🧩",
                    false
                ) {}
            }

            item {
                LessonItem(
                    4,
                    "Academic Vocabulary",
                    "Develop vocabulary commonly used in university study.",
                    "📝",
                    false
                ) {}
            }

            item {
                LessonItem(
                    5,
                    "English Studies Terminology",
                    "Become familiar with essential terms in language and literature studies.",
                    "🔤",
                    false
                ) {}
            }

            item {
                LessonItem(
                    6,
                    "Terminology Practice",
                    "Apply terminology through exercises and contextual tasks.",
                    "✍️",
                    false
                ) {}
            }
        }
    }
}

@Composable
private fun CourseHeader() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = PrimaryBlue)
    ) {
        Column(Modifier.padding(22.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("📖", fontSize = 42.sp)
                Spacer(Modifier.width(15.dp))
                Column {
                    Text(
                        "Language and Terminology 1",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Developing academic language and terminology.",
                        color = Color.White.copy(alpha = .9f),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun ProgressCard() {
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(18.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Course progress", color = TextGray, fontSize = 13.sp)
                    Text(
                        "0 / 6 lessons",
                        color = TextDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp
                    )
                }
                Text("0%", color = PrimaryBlue, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { 0f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = PrimaryBlue,
                trackColor = Color(0xFFE2E8F0)
            )
        }
    }
}

@Composable
private fun LessonItem(
    number: Int,
    title: String,
    description: String,
    icon: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val alpha = if (enabled) 1f else .55f

    Card(
        Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onClick),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(17.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(54.dp)
                    .background(
                        LightBlue.copy(alpha = alpha),
                        RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 27.sp)
            }

            Spacer(Modifier.width(14.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    "LESSON $number",
                    color = PrimaryBlue.copy(alpha = alpha),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    title,
                    color = TextDark.copy(alpha = alpha),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    description,
                    color = TextGray.copy(alpha = alpha),
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
private fun TerminologyLessonOneScreen(onBack: () -> Unit) {
    var section by remember { mutableIntStateOf(0) }

    val sections = listOf(
        "Learn",
        "Vocabulary",
        "Word Formation",
        "Read Aloud",
        "Quiz",
        "Practice"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Lesson 1",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Introduction to Terminology",
                            color = Color.White.copy(alpha = .85f),
                            fontSize = 12.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryBlue
                )
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .background(Background)
                .padding(padding)
        ) {
            SectionNavigation(
                selected = section,
                sections = sections,
                onSelected = { section = it }
            )

            when (section) {
                0 -> LearnSection()
                1 -> VocabularySection()
                2 -> WordFormationSection()
                3 -> ReadAloudSection()
                4 -> QuizSection()
                5 -> PracticeSection()
            }
        }
    }
}

@Composable
private fun SectionNavigation(
    selected: Int,
    sections: List<String>,
    onSelected: (Int) -> Unit
) {
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.White)
    ) {
        item {
            Row(
                Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                sections.forEachIndexed { index, title ->
                    Box(
                        Modifier
                            .background(
                                if (selected == index)
                                    PrimaryBlue
                                else
                                    Color(0xFFF1F5F9),
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { onSelected(index) }
                            .padding(horizontal = 12.dp, vertical = 9.dp)
                    ) {
                        Text(
                            title,
                            color = if (selected == index) Color.White else TextGray,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LearnSection() {
    val context = LocalContext.current
    val tts = rememberTextToSpeech()
    var speaking by remember { mutableStateOf(false) }

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        item {
            Text(
                "Introduction to Terminology",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Text(
                "Lesson 1 — Foundations of academic terminology",
                color = TextGray,
                fontSize = 14.sp
            )
        }

        item {
            DefinitionCard(
                title = "What is terminology?",
                icon = "📘",
                background = LightBlue,
                text = "Terminology is the systematic study and use of specialized terms within a particular field of knowledge or professional activity."
            )
        }

        item {
            DefinitionCard(
                title = "What is a term?",
                icon = "🔤",
                background = LightPurple,
                text = "A term is a word or expression that has a specific meaning within a particular subject field, discipline or professional context."
            )
        }

        item {
            AcademicNote()
        }

        item {
            AudioCard(
                "Listen to the academic explanation",
                TERMINOLOGY_INTRODUCTION,
                speaking,
                {
                    speaking = true
                    EnglishStudiesSpeech.speak(
                        tts = tts,
                        context = context,
                        text = TERMINOLOGY_INTRODUCTION,
                        utteranceId = "terminology_intro"
                    )
                },
                {
                    speaking = false
                    tts?.stop()
                }
            )
        }

        item {
            ContextCard()
        }

        item {
            KeyPrinciplesCard()
        }

        item {
            TranslationCard(
                "🇫🇷 French explanation",
                "La terminologie désigne l’étude systématique et l’emploi des termes spécialisés dans un domaine donné. Un terme possède un sens précis qui dépend du domaine dans lequel il est utilisé."
            )
        }

        item {
            Spacer(Modifier.height(30.dp))
        }
    }
}

@Composable
private fun DefinitionCard(
    title: String,
    icon: String,
    background: Color,
    text: String
) {
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = background)
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(icon, fontSize = 25.sp)
                Spacer(Modifier.width(10.dp))
                Text(
                    title,
                    color = TextDark,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text,
                color = TextDark,
                fontSize = 16.sp,
                lineHeight = 25.sp
            )
        }
    }
}

@Composable
private fun AcademicNote() {
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(
                "Why terminology matters in university study",
                color = TextDark,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(Modifier.height(10.dp))
            Text(
                "University students encounter specialized vocabulary in lectures, textbooks, articles and examinations. Understanding terminology allows students to identify concepts accurately, interpret academic texts and communicate ideas with greater precision.",
                color = TextGray,
                fontSize = 15.sp,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
private fun ContextCard() {
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(
                "A term in context",
                color = TextDark,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(Modifier.height(10.dp))

            Text(
                "In linguistics, the word “syntax” is a specialized term. It refers to the study of how words and phrases are arranged to form sentences.",
                color = TextDark,
                fontSize = 16.sp,
                lineHeight = 25.sp
            )

            Spacer(Modifier.height(12.dp))

            Text(
                "General word: arrangement\nSpecialized term: syntax\nField: linguistics",
                color = PrimaryBlue,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun KeyPrinciplesCard() {
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(
                "Key principles",
                color = TextDark,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(Modifier.height(14.dp))

            PrincipleRow("1", "Precision", "A specialized term should communicate a precise concept.")
            Divider(Modifier.padding(vertical = 11.dp))
            PrincipleRow("2", "Context", "The meaning of a term is closely connected to its field and context.")
            Divider(Modifier.padding(vertical = 11.dp))
            PrincipleRow("3", "Consistency", "Academic communication benefits from consistent use of terminology.")
            Divider(Modifier.padding(vertical = 11.dp))
            PrincipleRow("4", "Understanding", "Students should understand a term, not simply memorize its translation.")
        }
    }
}

@Composable
private fun PrincipleRow(number: String, title: String, text: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            Modifier
                .size(34.dp)
                .background(LightBlue, RoundedCornerShape(9.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(number, color = PrimaryBlue, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(title, color = TextDark, fontWeight = FontWeight.Bold)
            Text(text, color = TextGray, fontSize = 13.sp, lineHeight = 20.sp)
        }
    }
}

@Composable
private fun AudioCard(
    title: String,
    text: String,
    speaking: Boolean,
    onPlay: () -> Unit,
    onStop: () -> Unit
) {
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            Modifier.padding(17.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.VolumeUp,
                null,
                tint = PrimaryBlue,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = TextDark, fontWeight = FontWeight.Bold)
                Text("Listen and repeat.", color = TextGray, fontSize = 12.sp)
            }
            IconButton(onClick = { if (speaking) onStop() else onPlay() }) {
                Icon(
                    if (speaking) Icons.Default.Stop else Icons.Default.PlayArrow,
                    null,
                    tint = PrimaryBlue
                )
            }
        }
    }
}

@Composable
private fun TranslationCard(title: String, text: String) {
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = LightPurple)
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(
                title,
                color = Purple,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text,
                color = TextDark,
                fontSize = 15.sp,
                lineHeight = 23.sp
            )
        }
    }
}

data class TermVocabulary(
    val term: String,
    val definition: String,
    val french: String,
    val example: String
)

@Composable
private fun VocabularySection() {
    val words = listOf(
        TermVocabulary(
            "terminology",
            "The systematic study and use of specialized terms.",
            "terminologie",
            "Terminology is important in academic communication."
        ),
        TermVocabulary(
            "term",
            "A word or expression with a specialized meaning.",
            "terme",
            "Syntax is a linguistic term."
        ),
        TermVocabulary(
            "concept",
            "An abstract idea or principle.",
            "concept",
            "The term represents an important concept."
        ),
        TermVocabulary(
            "discipline",
            "A field of academic study.",
            "discipline",
            "Linguistics is an academic discipline."
        ),
        TermVocabulary(
            "context",
            "The situation or surrounding text that helps determine meaning.",
            "contexte",
            "The context can clarify the meaning of a term."
        ),
        TermVocabulary(
            "specialized",
            "Designed or used for a particular field or purpose.",
            "spécialisé",
            "Academic texts contain specialized vocabulary."
        ),
        TermVocabulary(
            "definition",
            "A statement explaining the meaning of a word or concept.",
            "définition",
            "Read the definition before using a new term."
        ),
        TermVocabulary(
            "precision",
            "Accuracy and exactness in meaning or expression.",
            "précision",
            "Terminology improves precision in academic writing."
        )
    )

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "📖 Core Vocabulary",
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Text(
                "Essential terminology for Lesson 1.",
                color = TextGray
            )
        }
        items(words) { VocabularyCard(it) }
    }
}

@Composable
private fun VocabularyCard(word: TermVocabulary) {
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(17.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    word.term,
                    color = PrimaryBlue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp
                )
                Text(
                    word.french,
                    color = Green,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            Spacer(Modifier.height(7.dp))

            Text(
                word.definition,
                color = TextGray,
                fontSize = 14.sp,
                lineHeight = 21.sp
            )

            Spacer(Modifier.height(8.dp))

            Text(
                "Example: ${word.example}",
                color = TextDark,
                fontSize = 14.sp,
                lineHeight = 21.sp
            )
        }
    }
}

@Composable
private fun WordFormationSection() {
    val items = listOf(
        FormationItem(
            "Prefix",
            "A group of letters added to the beginning of a word.",
            "un- + clear → unclear",
            "préfixe"
        ),
        FormationItem(
            "Suffix",
            "A group of letters added to the end of a word.",
            "academic + -ally → academically",
            "suffixe"
        ),
        FormationItem(
            "Compounding",
            "The combination of two or more words to form a new lexical unit.",
            "class + room → classroom",
            "composition"
        ),
        FormationItem(
            "Conversion",
            "A change of word class without adding an affix.",
            "to email → an email",
            "conversion"
        )
    )

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                "🧩 Word Formation",
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Text(
                "Understanding how academic vocabulary is formed.",
                color = TextGray
            )
        }

        item {
            Card(
                Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = LightBlue)
            ) {
                Text(
                    "Word formation helps students infer the meaning of unfamiliar vocabulary. Recognizing common prefixes, suffixes and word combinations can make academic reading more efficient.",
                    Modifier.padding(20.dp),
                    color = TextDark,
                    fontSize = 15.sp,
                    lineHeight = 24.sp
                )
            }
        }

        items(items) { formation ->
            FormationCard(formation)
        }
    }
}

data class FormationItem(
    val title: String,
    val explanation: String,
    val example: String,
    val french: String
)

@Composable
private fun FormationCard(item: FormationItem) {
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(19.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    item.title,
                    color = PrimaryBlue,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(item.french, color = Green, fontSize = 12.sp)
            }
            Spacer(Modifier.height(8.dp))
            Text(
                item.explanation,
                color = TextGray,
                fontSize = 14.sp,
                lineHeight = 21.sp
            )
            Spacer(Modifier.height(9.dp))
            Text(
                "Example: ${item.example}",
                color = TextDark,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ReadAloudSection() {
    val appContext = LocalContext.current
    var recognizedText by remember { mutableStateOf("") }
    var listening by remember { mutableStateOf(false) }
    var resultMessage by remember { mutableStateOf("") }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                listening = true
                startSpeechRecognition(
                    appContext,
                    onResult = {
                        recognizedText = it
                        listening = false
                        resultMessage =
                            compareReading(READING_TEXT, it)
                    },
                    onError = {
                        listening = false
                        resultMessage = "Please try again and speak clearly."
                    }
                )
            }
        }

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        item {
            Text(
                "🎤 Read Aloud",
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Text(
                "Read the academic passage aloud.",
                color = TextGray
            )
        }

        item {
            Card(
                Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text(
                        READING_TEXT,
                        color = TextDark,
                        fontSize = 17.sp,
                        lineHeight = 27.sp
                    )
                }
            }
        }

        item {
            Button(
                onClick = {
                    if (
                        appContext.checkSelfPermission(
                            Manifest.permission.RECORD_AUDIO
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        listening = true
                        startSpeechRecognition(
                            appContext,
                            onResult = {
                                recognizedText = it
                                listening = false
                                resultMessage =
                                    compareReading(READING_TEXT, it)
                            },
                            onError = {
                                listening = false
                                resultMessage =
                                    "The speech could not be recognized. Please try again."
                            }
                        )
                    } else {
                        permissionLauncher.launch(
                            Manifest.permission.RECORD_AUDIO
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (listening) Red else PrimaryBlue
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(
                    if (listening)
                        Icons.Default.Stop
                    else
                        Icons.Default.KeyboardVoice,
                    null
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    if (listening) "Listening..." else "Start Reading",
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (recognizedText.isNotBlank()) {
            item {
                RecognitionResultCard(recognizedText, resultMessage)
            }
        }
    }
}

@Composable
private fun RecognitionResultCard(
    recognizedText: String,
    message: String
) {
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = LightGreen)
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(
                "Your reading",
                color = Green,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                recognizedText,
                color = TextDark,
                fontSize = 15.sp,
                lineHeight = 23.sp
            )
            Spacer(Modifier.height(10.dp))
            Text(
                message,
                color = Green,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

data class TerminologyQuestion(
    val question: String,
    val options: List<String>,
    val correct: Int,
    val explanation: String
)

@Composable
private fun QuizSection() {
    val questions = remember {
        listOf(
            TerminologyQuestion(
                "What is a term?",
                listOf(
                    "A word with a specialized meaning in a field",
                    "Any random word",
                    "Only a verb",
                    "A punctuation mark"
                ),
                0,
                "A term has a specific meaning within a particular field or context."
            ),
            TerminologyQuestion(
                "Why is terminology important in academic study?",
                listOf(
                    "It makes texts longer",
                    "It improves precision and understanding",
                    "It replaces all ordinary vocabulary",
                    "It removes context"
                ),
                1,
                "Terminology helps students communicate concepts accurately."
            ),
            TerminologyQuestion(
                "Which field is associated with the term 'syntax'?",
                listOf(
                    "Linguistics",
                    "Geography",
                    "Chemistry only",
                    "Music"
                ),
                0,
                "Syntax is a fundamental term in linguistics."
            ),
            TerminologyQuestion(
                "What is a suffix?",
                listOf(
                    "A group of letters added to the beginning",
                    "A group of letters added to the end",
                    "A complete sentence",
                    "A paragraph"
                ),
                1,
                "A suffix is added to the end of a word."
            ),
            TerminologyQuestion(
                "What should a student do with a new academic term?",
                listOf(
                    "Memorize the translation only",
                    "Ignore the context",
                    "Understand its meaning and use in context",
                    "Avoid using it"
                ),
                2,
                "Academic vocabulary should be understood and used appropriately in context."
            )
        )
    }

    var index by remember { mutableIntStateOf(0) }
    var selected by remember { mutableStateOf<Int?>(null) }
    var score by remember { mutableIntStateOf(0) }

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                "🧠 Terminology Quiz",
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Text(
                "Check your understanding of Lesson 1.",
                color = TextGray
            )
        }

        item {
            Text(
                "Question ${index + 1} / ${questions.size}",
                color = PrimaryBlue,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            QuizCard(
                question = questions[index],
                selected = selected,
                onSelected = {
                    if (selected == null) {
                        selected = it
                        if (it == questions[index].correct) score++
                    }
                }
            )
        }

        if (selected != null) {
            item {
                Card(
                    Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp),
                    colors = CardDefaults.cardColors(
                        containerColor =
                            if (selected == questions[index].correct)
                                LightGreen
                            else
                                LightRed
                    )
                ) {
                    Column(Modifier.padding(17.dp)) {
                        Text(
                            if (selected == questions[index].correct)
                                "✅ Correct"
                            else
                                "❌ Incorrect",
                            color =
                                if (selected == questions[index].correct)
                                    Green
                                else
                                    Red,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            questions[index].explanation,
                            color = TextDark,
                            fontSize = 14.sp,
                            lineHeight = 21.sp
                        )
                    }
                }
            }

            item {
                Button(
                    onClick = {
                        if (index < questions.lastIndex) {
                            index++
                            selected = null
                        } else {
                            index = 0
                            selected = null
                            score = 0
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryBlue
                    )
                ) {
                    Text(
                        if (index < questions.lastIndex)
                            "Next Question"
                        else
                            "Restart Quiz"
                    )
                }
            }
        }

        item {
            ScoreCard(score, questions.size)
        }
    }
}

@Composable
private fun QuizCard(
    question: TerminologyQuestion,
    selected: Int?,
    onSelected: (Int) -> Unit
) {
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(
                question.question,
                color = TextDark,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(15.dp))

            question.options.forEachIndexed { i, option ->
                val correct = i == question.correct
                val selectedOption = selected == i

                val background = when {
                    selected != null && correct -> LightGreen
                    selected != null && selectedOption && !correct -> LightRed
                    selectedOption -> LightBlue
                    else -> Color(0xFFF8FAFC)
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(background, RoundedCornerShape(12.dp))
                        .clickable(enabled = selected == null) {
                            onSelected(i)
                        }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "${('A'.code + i).toChar()}.",
                        color = PrimaryBlue,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = option,
                        modifier = Modifier.weight(1f),
                        color = TextDark
                    )

                    if (selected != null && correct) {
                        Icon(
                            Icons.Default.CheckCircle,
                            null,
                            tint = Green
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun ScoreCard(score: Int, total: Int) {
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("🏆", fontSize = 30.sp)
            Spacer(Modifier.width(12.dp))
            Column {
                Text("Current score", color = TextGray, fontSize = 13.sp)
                Text(
                    "$score / $total",
                    color = TextDark,
                    fontWeight = FontWeight.Bold,
                    fontSize = 21.sp
                )
            }
        }
    }
}

@Composable
private fun PracticeSection() {
    var term by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }
    var feedback by remember { mutableStateOf("") }

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        item {
            Text(
                "✍️ Terminology Practice",
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Text(
                "Use terminology in an academic context.",
                color = TextGray
            )
        }

        item {
            Card(
                Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = LightOrange)
            ) {
                Column(Modifier.padding(19.dp)) {
                    Text(
                        "Task",
                        color = Orange,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Choose one term from the lesson and write an original academic sentence using it correctly.",
                        color = TextDark,
                        fontSize = 15.sp,
                        lineHeight = 23.sp
                    )
                }
            }
        }

        item {
            OutlinedTextField(
                value = term,
                onValueChange = {
                    term = it
                    feedback = ""
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Target term") },
                placeholder = { Text("e.g. terminology") },
                shape = RoundedCornerShape(14.dp)
            )
        }

        item {
            OutlinedTextField(
                value = answer,
                onValueChange = {
                    answer = it
                    feedback = ""
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp),
                label = { Text("Write your academic sentence") },
                shape = RoundedCornerShape(14.dp)
            )
        }

        item {
            Button(
                onClick = {
                    feedback = practiceFeedback(term, answer)
                },
                enabled = term.isNotBlank() && answer.trim().split("\\s+".toRegex()).count { it.isNotBlank() } >= 4,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Check My Sentence", fontWeight = FontWeight.Bold)
            }
        }

        if (feedback.isNotBlank()) {
            item {
                Card(
                    Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(17.dp),
                    colors = CardDefaults.cardColors(
                        containerColor =
                            if (feedback.startsWith("Good"))
                                LightGreen
                            else
                                LightOrange
                    )
                ) {
                    Text(
                        feedback,
                        Modifier.padding(18.dp),
                        color = TextDark,
                        fontSize = 15.sp,
                        lineHeight = 23.sp
                    )
                }
            }
        }
    }
}

private fun practiceFeedback(term: String, sentence: String): String {
    val normalizedTerm = term.trim().lowercase(Locale.US)
    val normalizedSentence = sentence.lowercase(Locale.US)

    return when {
        normalizedTerm.isBlank() ->
            "Please enter a target term."
        !normalizedSentence.contains(normalizedTerm) ->
            "Try to include the target term in your sentence."
        !sentence.trim().endsWith(".") ->
            "Your sentence uses the term, but remember to finish it with appropriate punctuation."
        else ->
            "Good work! You have used the target term in a complete sentence. Review whether the meaning fits the academic context."
    }
}

@Composable
private fun rememberTextToSpeech(): TextToSpeech? {
    val appContext = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    LaunchedEffect(Unit) {
        tts = TextToSpeech(appContext) { status ->
            if (status != TextToSpeech.SUCCESS) return@TextToSpeech

            val engine = tts ?: return@TextToSpeech

            val ok = EnglishStudiesSpeech.configure(
                tts = engine,
                context = appContext
            )

            if (!ok) {
                android.widget.Toast.makeText(
                    appContext,
                    AppLanguage.tr(
                        "Aucune voix anglaise compatible n'est installée. Installez une voix English (US/UK) dans les réglages Text-to-Speech du téléphone.",
                        "No compatible English voice is installed. Install an English (US/UK) voice in the phone's Text-to-Speech settings.",
                        "لا توجد نبرة صوت إنجليزية متوافقة. ثبّت صوتًا إنجليزيًا (أمريكيًا أو بريطانيًا) من إعدادات تحويل النص إلى كلام."
                    ),
                    android.widget.Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    return tts
}

private fun startSpeechRecognition(
    context: Context,
    onResult: (String) -> Unit,
    onError: () -> Unit
) {
    if (!SpeechRecognizer.isRecognitionAvailable(context)) {
        onError()
        return
    }

    val recognizer = SpeechRecognizer.createSpeechRecognizer(context)

    val intent = Intent(
        RecognizerIntent.ACTION_RECOGNIZE_SPEECH
    ).apply {
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            EnglishStudiesSpeech.recognitionLanguage(context)
        )
        putExtra(
            RecognizerIntent.EXTRA_PARTIAL_RESULTS,
            false
        )
    }

    recognizer.setRecognitionListener(object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {}
        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(rmsdB: Float) {}
        override fun onBufferReceived(buffer: ByteArray?) {}
        override fun onEndOfSpeech() {}
        override fun onPartialResults(partialResults: Bundle?) {}
        override fun onEvent(eventType: Int, params: Bundle?) {}

        override fun onError(error: Int) {
            recognizer.destroy()
            onError()
        }

        override fun onResults(results: Bundle?) {
            val matches = results?.getStringArrayList(
                SpeechRecognizer.RESULTS_RECOGNITION
            )
            val result = matches?.firstOrNull()
            recognizer.destroy()

            if (result.isNullOrBlank()) onError()
            else onResult(result)
        }
    })

    recognizer.startListening(intent)
}

private fun compareReading(
    expected: String,
    recognized: String
): String {
    val expectedWords = normalizeWords(expected).toSet()
    val spokenWords = normalizeWords(recognized)

    if (spokenWords.isEmpty()) return "No clear speech was detected."

    val common = spokenWords.count { it in expectedWords }
    val percentage =
        ((common.toFloat() / expectedWords.size.coerceAtLeast(1)) * 100).toInt()

    return when {
        percentage >= 85 ->
            "Excellent reading! Most key words were recognized."
        percentage >= 65 ->
            "Good reading. Try to pronounce the remaining words more clearly."
        percentage >= 40 ->
            "Keep practicing. Read slowly and pay attention to word endings."
        else ->
            "Try again. Speak slowly and clearly, preferably in a quiet environment."
    }
}

private fun normalizeWords(text: String): List<String> {
    return text
        .lowercase(Locale.US)
        .replace("[^a-zA-Z\\s]".toRegex(), "")
        .split("\\s+".toRegex())
        .filter { it.isNotBlank() }
}

private const val TERMINOLOGY_INTRODUCTION =
    "Terminology is the systematic study and use of specialized terms in a particular field. A term is a word or expression that has a specific meaning within a discipline. Understanding terminology helps university students read academic texts, identify important concepts, and communicate ideas with precision."

private const val READING_TEXT =
    "Terminology is important in academic study because specialized terms allow students to communicate concepts precisely. A term may have a different meaning depending on the discipline and the context in which it is used."

