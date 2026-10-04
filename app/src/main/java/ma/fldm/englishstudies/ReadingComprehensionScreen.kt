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
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

private val RCPrimary = Color(0xFF2563EB)
private val RCDark = Color(0xFF172033)
private val RCGray = Color(0xFF64748B)
private val RCBackground = Color(0xFFF8FAFC)
private val RCLightBlue = Color(0xFFEFF6FF)
private val RCGreen = Color(0xFF16A34A)
private val RCLightGreen = Color(0xFFDCFCE7)
private val RCRed = Color(0xFFDC2626)
private val RCLightRed = Color(0xFFFEE2E2)
private val RCOrange = Color(0xFFF59E0B)
private val RCLightOrange = Color(0xFFFEF3C7)
private val RCPurple = Color(0xFF7C3AED)
private val RCLightPurple = Color(0xFFF3E8FF)

private data class RCStrategy(
    val name: String,
    val definition: String,
    val technique: String,
    val french: String
)

private data class RCVocab(
    val word: String,
    val definition: String,
    val translation: String
)

private data class RCQuestion(
    val question: String,
    val options: List<String>,
    val correct: Int
)

/**
 * Reading Comprehension & Précis 1
 *
 * Common module shell + specialized reading pedagogy:
 * Introduction → Strategies → Text → Vocabulary → Comprehension → Précis → Read Aloud → Assessment
 */
@Composable
fun ReadingComprehensionScreen(onBack: () -> Unit = {}) {
    val tabs = listOf(
        "Introduction",
        "Reading Strategies",
        "Academic Text",
        "Vocabulary",
        "Comprehension",
        "Précis Writing",
        "Read Aloud",
        "Assessment"
    )

    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = RCBackground,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Reading Comprehension & Précis 1",
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
                    containerColor = RCPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(RCBackground)
                .padding(padding)
        ) {
            RCProgressHeader(
                current = selectedTab,
                total = tabs.size,
                title = tabs[selectedTab]
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tabs.forEachIndexed { index, title ->
                    RCTab(
                        title = title,
                        selected = selectedTab == index,
                        onClick = { selectedTab = index }
                    )
                }
            }

            when (selectedTab) {
                0 -> RCIntroduction()
                1 -> RCStrategies()
                2 -> RCAcademicText()
                3 -> RCVocabulary()
                4 -> RCComprehension()
                5 -> RCPrecis()
                6 -> RCReadAloud()
                7 -> RCAssessment()
            }
        }
    }
}

@Composable
private fun RCProgressHeader(current: Int, total: Int, title: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Module progress",
                color = RCGray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${current + 1} / $total",
                color = RCPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(Color(0xFFE2E8F0), RoundedCornerShape(50))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth((current + 1).toFloat() / total.toFloat())
                    .height(6.dp)
                    .background(RCPrimary, RoundedCornerShape(50))
            )
        }
        Spacer(Modifier.height(5.dp))
        Text(
            text = title,
            color = RCDark,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )
    }
}

@Composable
private fun RCTab(title: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(
                if (selected) RCPrimary else Color(0xFFF1F5F9),
                RoundedCornerShape(11.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 9.dp)
    ) {
        Text(
            text = title,
            color = if (selected) Color.White else RCGray,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun RCIntroduction() {
    RCPageColumn {
        RCHeaderCard(
            icon = "📚",
            title = "Reading Comprehension & Précis 1",
            subtitle = "An introduction to academic reading and concise writing."
        )

        RCSectionCard(
            title = "Course Description",
            icon = Icons.Default.MenuBook
        ) {
            Text(
                text = "This course develops the student's ability to read academic texts critically, identify central ideas, locate supporting information, infer meaning from context, and produce concise summaries in the form of a précis.",
                color = RCDark,
                fontSize = 15.sp,
                lineHeight = 24.sp
            )
        }

        RCSectionCard(title = "Learning Objectives", icon = Icons.Default.School) {
            RCObjective("Identify the main idea and purpose of a text.")
            RCObjective("Distinguish main ideas from supporting details.")
            RCObjective("Use skimming and scanning effectively.")
            RCObjective("Infer the meaning of unfamiliar words from context.")
            RCObjective("Recognize relationships between ideas.")
            RCObjective("Write a coherent and concise précis.")
        }

        RCAcademicNote(
            title = "Academic Reading",
            text = "Academic reading is purposeful and analytical. A university reader does not simply recognize words; the reader identifies arguments, evidence, relationships, assumptions and conclusions."
        )

        RCFrenchNote(
            "Objectif du cours : développer une lecture universitaire active et la capacité à résumer fidèlement les idées essentielles d'un texte sans en modifier le sens."
        )
    }
}

@Composable
private fun RCStrategies() {
    val strategies = listOf(
        RCStrategy(
            "Skimming",
            "Reading quickly to obtain the general meaning and organization of a text.",
            "Read the title, introduction, topic sentences and conclusion first.",
            "Lecture rapide pour comprendre l'idée générale."
        ),
        RCStrategy(
            "Scanning",
            "Reading quickly to locate a particular piece of information.",
            "Search for names, dates, numbers, keywords or specific terms.",
            "Lecture sélective pour trouver une information précise."
        ),
        RCStrategy(
            "Reading for Gist",
            "Identifying what a text is mainly about without focusing on every detail.",
            "Ask: What is the writer mainly discussing?",
            "Comprendre le sens général."
        ),
        RCStrategy(
            "Main Idea",
            "The central point that the writer wants the reader to understand.",
            "Look at the topic sentence and the ideas repeated or developed throughout the paragraph.",
            "Identifier l'idée principale."
        ),
        RCStrategy(
            "Supporting Details",
            "Facts, examples, reasons or explanations that develop the main idea.",
            "Ask which information explains or proves the main point.",
            "Repérer les détails qui développent l'idée principale."
        ),
        RCStrategy(
            "Inference",
            "A logical conclusion based on information stated or implied in the text.",
            "Combine textual evidence with reasonable interpretation.",
            "Déduire une information à partir des indices du texte."
        )
    )

    RCPageColumn {
        RCPageTitle(
            title = "Reading Strategies",
            subtitle = "Essential strategies for university-level reading."
        )

        strategies.forEachIndexed { index, strategy ->
            RCStrategyCard(index + 1, strategy)
        }

        RCAcademicNote(
            title = "Important principle",
            text = "Good readers change their reading speed according to their purpose. You do not read a timetable in the same way that you read an academic article."
        )
    }
}

@Composable
private fun RCStrategyCard(number: Int, strategy: RCStrategy) {
    RCWhiteCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(RCLightBlue, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$number",
                    color = RCPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.width(11.dp))
            Text(
                text = strategy.name,
                color = RCDark,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
        Spacer(Modifier.height(9.dp))
        Text(strategy.definition, color = RCDark, fontSize = 14.sp, lineHeight = 22.sp)
        Spacer(Modifier.height(8.dp))
        Text(
            text = "How to use it: ${strategy.technique}",
            color = RCPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 20.sp
        )
        Spacer(Modifier.height(7.dp))
        Text(strategy.french, color = RCPurple, fontSize = 12.sp)
    }
}

@Composable
private fun RCAcademicText() {
    val context = LocalContext.current
    val tts = rememberEnglishTts()
    var speaking by remember { mutableStateOf(false) }

    RCPageColumn {
        RCPageTitle(
            title = "Academic Text 1",
            subtitle = "Read actively. Identify the topic, main idea and supporting details."
        )

        RCWhiteCard {
            Text(
                text = ACADEMIC_TEXT_TITLE,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = RCPrimary
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = ACADEMIC_TEXT,
                color = RCDark,
                fontSize = 16.sp,
                lineHeight = 27.sp
            )
        }

        RCAudioCard(
            title = "Listen to the academic text",
            speaking = speaking,
            onPlay = {
                if (EnglishStudiesSettings.isSoundEnabled(context)) {
                    tts?.language = EnglishStudiesSettings.englishLocale(context)
                    tts?.setSpeechRate(EnglishStudiesSettings.speechRate(context))
                    speaking = true
                    tts?.speak(
                        ACADEMIC_TEXT,
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        "academic_text"
                    )
                }
            },
            onStop = {
                speaking = false
                tts?.stop()
            }
        )

        RCAcademicNote(
            title = "Reading Focus",
            text = "Before answering questions, determine the text's subject, central claim and the function of each paragraph."
        )

        RCFrenchNote(
            "Conseil : lisez d'abord le titre et le premier passage pour anticiper le sujet, puis vérifiez vos hypothèses à partir des détails du texte."
        )
    }
}

@Composable
private fun RCVocabulary() {
    val words = listOf(
        RCVocab("access", "the possibility or opportunity to use or obtain something", "accès"),
        RCVocab("academic", "related to education, study or scholarship", "académique"),
        RCVocab("benefit", "an advantage or positive result", "avantage / bénéfice"),
        RCVocab("critical", "involving careful judgment and analysis", "critique / analytique"),
        RCVocab("digital", "using or relating to electronic technology", "numérique"),
        RCVocab("resource", "a useful source of information or support", "ressource"),
        RCVocab("reliable", "able to be trusted as accurate", "fiable"),
        RCVocab("research", "systematic study undertaken to establish facts or knowledge", "recherche"),
        RCVocab("source", "a place, person or document from which information comes", "source"),
        RCVocab("evaluate", "to judge the value, quality or importance of something", "évaluer")
    )

    RCPageColumn {
        RCPageTitle(
            title = "Academic Vocabulary",
            subtitle = "Vocabulary in context is more useful than isolated memorization."
        )

        words.forEachIndexed { index, word ->
            RCVocabCard(index + 1, word)
        }
    }
}

@Composable
private fun RCVocabCard(number: Int, word: RCVocab) {
    RCWhiteCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("$number.", color = RCGray, fontSize = 12.sp)
                Spacer(Modifier.width(8.dp))
                Text(
                    word.word,
                    color = RCPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            Text(
                word.translation,
                color = RCGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(word.definition, color = RCDark, fontSize = 14.sp, lineHeight = 21.sp)
    }
}

@Composable
private fun RCComprehension() {
    val questions = listOf(
        RCQuestion(
            "What is the central subject of the text?",
            listOf(
                "The history of printed books",
                "The role of reading in academic learning",
                "The cost of university education",
                "The development of mobile phones"
            ),
            1
        ),
        RCQuestion(
            "Why is active reading important?",
            listOf(
                "It eliminates the need to understand the text.",
                "It helps students process, question and connect ideas.",
                "It makes every text shorter.",
                "It replaces academic writing."
            ),
            1
        ),
        RCQuestion(
            "Which activity is presented as useful for checking information?",
            listOf(
                "Ignoring the source",
                "Evaluating the reliability of sources",
                "Reading only the title",
                "Memorizing every sentence"
            ),
            1
        ),
        RCQuestion(
            "What can a student infer from the text?",
            listOf(
                "Reading strategies can be adapted to different purposes.",
                "All texts should be read at exactly the same speed.",
                "Academic reading requires no preparation.",
                "Students should avoid taking notes."
            ),
            0
        )
    )

    var index by remember { mutableIntStateOf(0) }
    var selected by remember { mutableStateOf<Int?>(null) }
    var score by remember { mutableIntStateOf(0) }

    RCPageColumn {
        RCPageTitle(
            title = "Comprehension Check",
            subtitle = "Answer the questions using evidence from Academic Text 1."
        )

        Text(
            text = "Question ${index + 1} / ${questions.size}",
            color = RCPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )

        RCQuestionCard(
            question = questions[index],
            selected = selected,
            onSelected = {
                if (selected == null) {
                    selected = it
                    if (it == questions[index].correct) score++
                }
            }
        )

        if (selected != null) {
            Text(
                text = if (selected == questions[index].correct)
                    "✓ Correct. Good textual reasoning."
                else
                    "✗ Review the text and look for evidence.",
                color = if (selected == questions[index].correct) RCGreen else RCRed,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

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
                colors = ButtonDefaults.buttonColors(containerColor = RCPrimary)
            ) {
                Text(if (index < questions.lastIndex) "Next Question" else "Restart")
            }
        }

        RCScoreCard("Comprehension score", score, questions.size)
    }
}

@Composable
private fun RCQuestionCard(
    question: RCQuestion,
    selected: Int?,
    onSelected: (Int) -> Unit
) {
    RCWhiteCard {
        Text(
            question.question,
            color = RCDark,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 25.sp
        )
        Spacer(Modifier.height(14.dp))

        question.options.forEachIndexed { i, option ->
            val correct = i == question.correct
            val chosen = i == selected
            val background = when {
                selected != null && correct -> RCLightGreen
                selected != null && chosen -> RCLightRed
                chosen -> RCLightBlue
                else -> Color(0xFFF8FAFC)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(background, RoundedCornerShape(12.dp))
                    .clickable(enabled = selected == null) { onSelected(i) }
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${('A'.code + i).toChar()}.",
                    color = RCPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.width(9.dp))
                Text(
                    option,
                    color = RCDark,
                    modifier = Modifier.weight(1f),
                    fontSize = 14.sp,
                    lineHeight = 21.sp
                )
                if (selected != null && correct) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = RCGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(Modifier.height(7.dp))
        }
    }
}

@Composable
private fun RCPrecis() {
    var draft by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }
    val words = countWords(draft)

    RCPageColumn {
        RCPageTitle(
            title = "Précis Writing",
            subtitle = "A précis is a concise, accurate and coherent representation of the essential ideas of a text."
        )

        RCSectionCard("What is a Précis?", Icons.Default.EditNote) {
            Text(
                text = "A précis is not a collection of copied sentences. It is a carefully organized restatement of the writer's essential ideas in your own words. It should preserve the meaning and logical relationships of the original text while eliminating unnecessary detail.",
                color = RCDark,
                fontSize = 14.sp,
                lineHeight = 22.sp
            )
        }

        RCMethodCard()

        RCAcademicNote(
            "A good précis should be",
            "Accurate • Concise • Coherent • Objective • Written mainly in your own words"
        )

        RCSectionCard("Model Précis", Icons.Default.Description) {
            Text(
                MODEL_PRECIS,
                color = RCDark,
                fontSize = 15.sp,
                lineHeight = 24.sp
            )
        }

        RCSectionCard("Guided Writing Task", Icons.Default.Create) {
            Text(
                "Write a 70–90 word précis of Academic Text 1. Identify the main argument and the most important supporting ideas. Avoid examples that are not essential.",
                color = RCDark,
                fontSize = 14.sp,
                lineHeight = 22.sp
            )
        }

        OutlinedTextField(
            value = draft,
            onValueChange = {
                draft = it
                submitted = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp),
            label = { Text("Write your précis here...") },
            supportingText = { Text("$words words • target: 70–90") },
            shape = RoundedCornerShape(15.dp)
        )

        Button(
            onClick = { submitted = true },
            enabled = words >= 10,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = RCPrimary)
        ) {
            Text("Submit Précis")
        }

        if (submitted) {
            RCPrecisFeedback(words, draft)
        }
    }
}

@Composable
private fun RCMethodCard() {
    val steps = listOf(
        "Read the whole text carefully.",
        "Identify the subject and central idea.",
        "Underline essential supporting ideas.",
        "Remove repetition, minor examples and unnecessary details.",
        "Paraphrase the essential information.",
        "Organize the ideas logically.",
        "Check accuracy, grammar and word count."
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = RCLightBlue)
    ) {
        Column(Modifier.padding(18.dp)) {
            Text(
                "How to write a précis",
                color = RCPrimary,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(10.dp))
            steps.forEachIndexed { i, step ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text("${i + 1}", color = RCPrimary, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(9.dp))
                    Text(step, color = RCDark, fontSize = 14.sp, lineHeight = 20.sp)
                }
            }
        }
    }
}

@Composable
private fun RCPrecisFeedback(words: Int, text: String) {
    val firstChar = text.trimStart().firstOrNull()
    val hasCapital = firstChar?.isUpperCase() == true
    val hasPunctuation = text.trim().endsWith(".")
    val goodLength = words in 70..90

    val message = when {
        words < 70 -> "Your précis is below the target length. Develop the essential ideas without adding irrelevant details."
        words > 90 -> "Your précis exceeds the target length. Remove repetition and non-essential details."
        !hasCapital -> "Begin with a capital letter."
        !hasPunctuation -> "Check the final punctuation."
        else -> "Good structural attempt. Next, revise for accuracy, coherence and faithful representation of the source."
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (goodLength && hasCapital && hasPunctuation) RCLightGreen else RCLightOrange
        )
    ) {
        Column(Modifier.padding(18.dp)) {
            Text(
                "Précis Feedback",
                color = if (goodLength && hasCapital && hasPunctuation) RCGreen else RCOrange,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(Modifier.height(7.dp))
            Text(message, color = RCDark, fontSize = 14.sp, lineHeight = 21.sp)
        }
    }
}

@Composable
private fun RCReadAloud() {
    val context = LocalContext.current
    var recognized by remember { mutableStateOf("") }
    var listening by remember { mutableStateOf(false) }
    var feedback by remember { mutableStateOf("") }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            listening = true
            startEnglishRecognition(
                context = context,
                onResult = {
                    recognized = it
                    listening = false
                    feedback = readingFeedback(READ_ALOUD_TEXT, it)
                },
                onError = {
                    listening = false
                    feedback = "Speech could not be recognized clearly. Please try again."
                }
            )
        }
    }

    RCPageColumn {
        RCPageTitle(
            title = "Academic Read Aloud",
            subtitle = "Read the selected passage aloud in English."
        )

        RCWhiteCard {
            Text(
                "Reading passage",
                color = RCPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(Modifier.height(10.dp))
            Text(
                READ_ALOUD_TEXT,
                color = RCDark,
                fontSize = 16.sp,
                lineHeight = 26.sp
            )
        }

        Button(
            onClick = {
                if (context.checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                    listening = true
                    startEnglishRecognition(
                        context = context,
                        onResult = {
                            recognized = it
                            listening = false
                            feedback = readingFeedback(READ_ALOUD_TEXT, it)
                        },
                        onError = {
                            listening = false
                            feedback = "Please try again and speak slowly and clearly."
                        }
                    )
                } else {
                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (listening) RCRed else RCPrimary
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(
                if (listening) Icons.Default.Stop else Icons.Default.Mic,
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))
            Text(if (listening) "Listening..." else "Start Reading")
        }

        if (recognized.isNotBlank()) {
            RCSectionCard("Recognized Speech", Icons.Default.RecordVoiceOver) {
                Text(recognized, color = RCDark, fontSize = 14.sp, lineHeight = 22.sp)
                Spacer(Modifier.height(10.dp))
                Text(feedback, color = RCPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }

        RCAcademicNote(
            "Reading advice",
            "Do not try to read as quickly as possible. Focus on clear pronunciation, natural pauses and sentence rhythm."
        )
    }
}

@Composable
private fun RCAssessment() {
    val questions = listOf(
        RCQuestion(
            "Skimming is mainly used to...",
            listOf("Find one exact number", "Understand the general meaning", "Translate every word", "Memorize a paragraph"),
            1
        ),
        RCQuestion(
            "A supporting detail normally...",
            listOf("Develops or explains the main idea", "Changes the subject completely", "Appears only in the title", "Has no relationship with the topic"),
            0
        ),
        RCQuestion(
            "A précis should...",
            listOf("Copy the original text", "Include every minor example", "Present essential ideas concisely and accurately", "Add the student's personal opinion"),
            2
        ),
        RCQuestion(
            "An inference is...",
            listOf("A conclusion based on textual evidence", "A direct quotation", "A dictionary definition", "A title"),
            0
        ),
        RCQuestion(
            "When evaluating an academic source, a reader should consider...",
            listOf("Only its length", "Its reliability and relevance", "Only its font", "Whether it has many pictures"),
            1
        )
    )

    var current by remember { mutableIntStateOf(0) }
    var selected by remember { mutableStateOf<Int?>(null) }
    var score by remember { mutableIntStateOf(0) }
    var finished by remember { mutableStateOf(false) }

    RCPageColumn {
        RCPageTitle(
            title = "Final Assessment",
            subtitle = "Test your understanding of the module."
        )

        if (!finished) {
            Text(
                "Question ${current + 1} / ${questions.size}",
                color = RCPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )

            RCQuestionCard(
                question = questions[current],
                selected = selected,
                onSelected = {
                    if (selected == null) {
                        selected = it
                        if (it == questions[current].correct) score++
                    }
                }
            )

            if (selected != null) {
                Button(
                    onClick = {
                        if (current < questions.lastIndex) {
                            current++
                            selected = null
                        } else {
                            finished = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = RCPrimary)
                ) {
                    Text(if (current < questions.lastIndex) "Next" else "Finish Assessment")
                }
            }
        } else {
            RCFinalResult(score, questions.size) {
                current = 0
                selected = null
                score = 0
                finished = false
            }
        }
    }
}

@Composable
private fun RCFinalResult(score: Int, total: Int, restart: () -> Unit) {
    val percent = (score * 100) / total
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (percent >= 70) RCLightGreen else RCLightOrange
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Assessment Completed", color = RCDark, fontWeight = FontWeight.Bold, fontSize = 23.sp)
            Spacer(Modifier.height(14.dp))
            Text("$score / $total", color = RCPrimary, fontSize = 40.sp, fontWeight = FontWeight.Bold)
            Text("$percent%", color = RCGray, fontSize = 18.sp)
            Spacer(Modifier.height(12.dp))
            Text(
                text = when {
                    percent >= 80 -> "Very good understanding of the course."
                    percent >= 60 -> "Good progress. Review the strategies and précis method."
                    else -> "Review the lessons and try the assessment again."
                },
                textAlign = TextAlign.Center,
                color = RCDark,
                fontSize = 15.sp,
                lineHeight = 22.sp
            )
            Spacer(Modifier.height(17.dp))
            Button(
                onClick = restart,
                colors = ButtonDefaults.buttonColors(containerColor = RCPrimary)
            ) {
                Text("Try Again")
            }
        }
    }
}

@Composable
private fun RCPageColumn(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        content = content
    )
}

@Composable
private fun RCPageTitle(title: String, subtitle: String) {
    Column {
        Text(title, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = RCDark)
        Spacer(Modifier.height(3.dp))
        Text(subtitle, color = RCGray, fontSize = 13.sp, lineHeight = 20.sp)
    }
}

@Composable
private fun RCHeaderCard(icon: String, title: String, subtitle: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = RCPrimary)
    ) {
        Row(
            modifier = Modifier.padding(21.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 42.sp)
            Spacer(Modifier.width(14.dp))
            Column {
                Text(title, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(subtitle, color = Color.White.copy(alpha = .9f), fontSize = 13.sp, lineHeight = 19.sp)
            }
        }
    }
}

@Composable
private fun RCWhiteCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        content = { Column(Modifier.padding(18.dp), content = content) }
    )
}

@Composable
private fun RCSectionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(19.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = RCPrimary)
                Spacer(Modifier.width(9.dp))
                Text(title, color = RCDark, fontSize = 19.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun RCObjective(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 5.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            tint = RCGreen,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(9.dp))
        Text(text, color = RCDark, fontSize = 14.sp, lineHeight = 21.sp)
    }
}

@Composable
private fun RCAcademicNote(title: String, text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(containerColor = RCLightBlue)
    ) {
        Column(Modifier.padding(18.dp)) {
            Text(title, color = RCPrimary, fontWeight = FontWeight.Bold, fontSize = 17.sp)
            Spacer(Modifier.height(7.dp))
            Text(text, color = RCDark, fontSize = 14.sp, lineHeight = 22.sp)
        }
    }
}

@Composable
private fun RCFrenchNote(text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(containerColor = RCLightPurple)
    ) {
        Text(
            text = "🇫🇷 $text",
            modifier = Modifier.padding(17.dp),
            color = RCPurple,
            fontSize = 13.sp,
            lineHeight = 21.sp
        )
    }
}

@Composable
private fun RCAudioCard(
    title: String,
    speaking: Boolean,
    onPlay: () -> Unit,
    onStop: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.VolumeUp,
                contentDescription = null,
                tint = RCPrimary,
                modifier = Modifier.size(30.dp)
            )
            Spacer(Modifier.width(11.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = RCDark, fontWeight = FontWeight.Bold)
                Text("Listen carefully and follow the text.", color = RCGray, fontSize = 12.sp)
            }
            IconButton(onClick = { if (speaking) onStop() else onPlay() }) {
                Icon(
                    if (speaking) Icons.Default.Stop else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = RCPrimary
                )
            }
        }
    }
}

@Composable
private fun RCScoreCard(title: String, score: Int, total: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("🏆", fontSize = 30.sp)
            Spacer(Modifier.width(12.dp))
            Column {
                Text(title, color = RCGray, fontSize = 13.sp)
                Text("$score / $total", color = RCDark, fontWeight = FontWeight.Bold, fontSize = 21.sp)
            }
        }
    }
}

@Composable
private fun rememberEnglishTts(): TextToSpeech? {
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = EnglishStudiesSettings.englishLocale(context)
                tts?.setSpeechRate(EnglishStudiesSettings.speechRate(context))
                tts?.setPitch(1.0f)
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

private fun startEnglishRecognition(
    context: Context,
    onResult: (String) -> Unit,
    onError: () -> Unit
) {
    if (!SpeechRecognizer.isRecognitionAvailable(context)) {
        onError()
        return
    }

    val recognizer = SpeechRecognizer.createSpeechRecognizer(context)
    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.toLanguageTag())
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, Locale.US.toLanguageTag())
        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
        putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
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
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            val result = matches?.firstOrNull()
            recognizer.destroy()
            if (result.isNullOrBlank()) onError() else onResult(result)
        }
    })

    recognizer.startListening(intent)
}

private fun normalizeEnglish(text: String): List<String> {
    return text
        .lowercase(Locale.US)
        .replace("[^a-z\\s]".toRegex(), " ")
        .split("\\s+".toRegex())
        .filter { it.isNotBlank() }
}

private fun readingFeedback(expected: String, recognized: String): String {
    val expectedWords = normalizeEnglish(expected)
    val spokenWords = normalizeEnglish(recognized)

    if (spokenWords.isEmpty()) return "No clear speech was detected."

    var position = 0
    var matches = 0

    spokenWords.forEach { spoken ->
        while (position < expectedWords.size) {
            if (spoken == expectedWords[position]) {
                matches++
                position++
                break
            }
            position++
        }
    }

    val percent = (matches * 100) / expectedWords.size.coerceAtLeast(1)

    return when {
        percent >= 85 -> "Excellent reading. The speech recognizer matched most of the target words."
        percent >= 65 -> "Good reading. Slow down slightly and pronounce the less clearly recognized words."
        percent >= 40 -> "Developing reading. Focus on sentence rhythm and clear pronunciation."
        else -> "Keep practicing. Read slowly, articulate each word and stay close to the microphone."
    }
}

private fun countWords(text: String): Int {
    return text.trim()
        .split("\\s+".toRegex())
        .count { it.isNotBlank() }
}

private const val ACADEMIC_TEXT_TITLE = "Reading as an Active Academic Skill"

private const val ACADEMIC_TEXT =
    "Reading is one of the central skills required in university study. " +
            "Students read textbooks, research articles, essays and other academic materials in order to acquire knowledge and evaluate ideas. " +
            "However, effective academic reading is more than recognizing individual words. It requires readers to identify the main argument, distinguish important information from minor details, and understand how ideas are connected. " +
            "Active readers also ask questions about the text and consider the reliability of the information presented. " +
            "Different purposes require different strategies. A student may skim a text to understand its general organization, scan it to locate a particular fact, or read carefully when evaluating an argument. " +
            "Taking notes, identifying unfamiliar concepts and reviewing difficult passages can further improve comprehension. " +
            "For this reason, successful university students develop flexible reading habits and select strategies according to the purpose, difficulty and type of text."

private const val MODEL_PRECIS =
    "Academic reading is an essential university skill because students must acquire knowledge and evaluate information from different sources. " +
            "Effective readers identify central ideas, distinguish important details from minor information, and recognize relationships between ideas. " +
            "They also adapt strategies such as skimming, scanning and close reading to their purposes. " +
            "Active reading therefore requires flexibility, critical attention and deliberate use of appropriate strategies."

private const val READ_ALOUD_TEXT =
    "Effective academic reading requires more than recognizing individual words. " +
            "Students must identify central ideas, understand relationships between ideas, and evaluate information carefully. " +
            "Different reading purposes require different strategies, including skimming, scanning and close reading."
