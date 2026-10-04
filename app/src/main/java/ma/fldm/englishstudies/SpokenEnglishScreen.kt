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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardVoice
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

private val SEPrimary = Color(0xFF2563EB)
private val SEBackground = Color(0xFFF8FAFC)
private val SEText = Color(0xFF172033)
private val SEGray = Color(0xFF64748B)
private val SELightBlue = Color(0xFFEFF6FF)
private val SEGreen = Color(0xFF16A34A)
private val SELightGreen = Color(0xFFDCFCE7)
private val SERed = Color(0xFFDC2626)
private val SELightRed = Color(0xFFFEE2E2)
private val SEOrange = Color(0xFFF59E0B)
private val SELightOrange = Color(0xFFFEF3C7)
private val SEPurple = Color(0xFF7C3AED)
private val SELightPurple = Color(0xFFF3E8FF)

private data class SpokenUnit(
    val number: Int,
    val title: String,
    val description: String,
    val icon: String,
)

private data class SpokenQuizQuestion(
    val question: String,
    val options: List<String>,
    val correct: Int,
    val explanation: String
)

private val SPOKEN_UNITS = listOf(
    SpokenUnit(
        1,
        "Introduction to Spoken English",
        "Understand spoken communication, fluency, accuracy and interaction.",
        "🎙️",
    ),
    SpokenUnit(
        2,
        "English Sounds and Pronunciation",
        "Study vowels, consonants and basic pronunciation patterns.",
        "🔤",
    ),
    SpokenUnit(
        3,
        "Word Stress",
        "Learn how stressed syllables influence English pronunciation.",
        "🔊",
    ),
    SpokenUnit(
        4,
        "Sentence Stress and Intonation",
        "Understand rhythm, prominence and rising or falling intonation.",
        "📈",
    ),
    SpokenUnit(
        5,
        "Everyday Communication",
        "Practise greetings, introductions, requests and polite responses.",
        "💬",
    ),
    SpokenUnit(
        6,
        "Fluency and Interaction",
        "Develop turn-taking, clarification and spontaneous speaking.",
        "🤝",
    )
)

private val SPEAKING_QUIZ = listOf(
    SpokenQuizQuestion(
        "Which concept refers to speaking smoothly and continuously?",
        listOf("Accuracy", "Fluency", "Context", "Clarification"),
        1,
        "Fluency concerns the continuity and smoothness of spoken language."
    ),
    SpokenQuizQuestion(
        "Which feature concerns variation in pitch during speech?",
        listOf("Intonation", "Vocabulary", "Grammar", "Turn-taking"),
        0,
        "Intonation refers to variation in pitch across spoken utterances."
    ),
    SpokenQuizQuestion(
        "What is an important part of oral interaction?",
        listOf("Ignoring the listener", "Turn-taking", "Avoiding responses", "Using only written language"),
        1,
        "Turn-taking helps participants organize who speaks and when."
    ),
    SpokenQuizQuestion(
        "What does intelligibility mean?",
        listOf("Speaking very quickly", "Being understood by the listener", "Using difficult vocabulary", "Speaking without pauses"),
        1,
        "Intelligibility concerns how successfully the listener understands the speaker."
    )
)

@Composable
fun SpokenEnglishScreen(onBack: () -> Unit = {}) {
    var selectedUnit by remember { mutableStateOf<Int?>(null) }

    selectedUnit?.let { unitIndex ->
        if (unitIndex == 0) {
            SpokenUnitOneScreen(
                onBack = { selectedUnit = null }
            )
        } else {
            SpokenActiveUnitScreen(
                unit = SPOKEN_UNITS[unitIndex],
                onBack = { selectedUnit = null }
            )
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Spoken English",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            "English Studies • Semester 1",
                            color = Color.White.copy(alpha = 0.88f),
                            fontSize = 11.sp
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
                    containerColor = SEPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(SEBackground)
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                SpokenCourseHeader()
            }

            item {
                SpokenProgressCard()
            }

            item {
                Text(
                    "Course Units",
                    color = SEText,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    "Each unit has its own pedagogical pathway.",
                    color = SEGray,
                    fontSize = 13.sp
                )
            }

            itemsIndexed(SPOKEN_UNITS) { index, unit ->
                SpokenUnitCard(
                    unit = unit,
                    onClick = {
                        selectedUnit = index
                    }
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SELightBlue)
                ) {
                    Text(
                        text = "Les 6 unités sont actives. Touchez une unité pour ouvrir son espace pédagogique.",
                        modifier = Modifier.padding(16.dp),
                        color = SEPrimary,
                        fontSize = 13.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SpokenActiveUnitScreen(
    unit: SpokenUnit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val tts = rememberEnglishTts()
    var speaking by remember { mutableStateOf(false) }
    var selectedAnswer by remember { mutableIntStateOf(-1) }
    var submitted by remember { mutableStateOf(false) }
    var selectedTask by remember { mutableIntStateOf(0) }

    val content = spokenUnitContent(unit.number)

    val speakText = {
        if (speaking) {
            tts?.stop()
            speaking = false
        } else if (EnglishStudiesSettings.isSoundEnabled(context)) {
            tts?.language = EnglishStudiesSettings.englishLocale(context)
            tts?.setSpeechRate(EnglishStudiesSettings.speechRate(context))
            tts?.speak(
                content.listenText,
                TextToSpeech.QUEUE_FLUSH,
                null,
                "spoken-unit-${unit.number}"
            )
            speaking = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Unit ${unit.number}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            unit.title,
                            color = Color.White.copy(alpha = 0.88f),
                            fontSize = 11.sp
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
                    containerColor = SEPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(SEBackground)
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SEPrimary)
                ) {
                    Column(Modifier.padding(20.dp)) {
                        Text(unit.icon, fontSize = 40.sp)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            unit.title,
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 30.sp
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            content.overview,
                            color = Color.White.copy(alpha = 0.92f),
                            fontSize = 14.sp,
                            lineHeight = 21.sp
                        )
                    }
                }
            }

            item {
                SpokenSectionTitle("Learning objectives")
                SpokenBulletCard(content.objectives)
            }

            item {
                SpokenListenCard(
                    title = "Listen to the academic model",
                    subtitle = "The selected British/American accent and reading speed are applied.",
                    speaking = speaking,
                    onClick = speakText
                )
            }

            item {
                SpokenSectionTitle("1. Learn")
                SpokenAcademicCard(
                    title = content.learnTitle,
                    points = content.learnPoints
                )
            }

            item {
                SpokenSectionTitle("2. Understand")
                SpokenDefinitionCard(
                    title = content.understandTitle,
                    text = content.understandText
                )
            }

            item {
                SpokenSectionTitle("3. Guided practice")
                SpokenPracticeCard(
                    title = content.practiceTitle,
                    instructions = content.practiceInstructions,
                    examples = content.practiceExamples
                )
            }

            item {
                SpokenSectionTitle("4. Apply")
                SpokenTaskSelector(
                    tasks = content.tasks,
                    selected = selectedTask,
                    onSelected = { selectedTask = it }
                )
                SpokenTaskCard(content.tasks[selectedTask])
            }

            item {
                SpokenSectionTitle("5. Assessment")
                val question = content.quiz.first()
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(17.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Text(
                            "Check your understanding",
                            color = SEPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            question.question,
                            color = SEText,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 23.sp
                        )
                        Spacer(Modifier.height(10.dp))

                        question.options.forEachIndexed { index, option ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        if (!submitted) selectedAnswer = index
                                    },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = when {
                                        submitted && index == question.correct -> SELightGreen
                                        submitted && index == selectedAnswer && index != question.correct -> SELightRed
                                        index == selectedAnswer -> SELightBlue
                                        else -> Color(0xFFF8FAFC)
                                    }
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(13.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "${('A'.code + index).toChar()}",
                                        color = SEPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(Modifier.width(10.dp))
                                    Text(
                                        option,
                                        color = SEText,
                                        fontSize = 14.sp,
                                        lineHeight = 20.sp
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(8.dp))

                        Button(
                            onClick = { submitted = true },
                            enabled = selectedAnswer >= 0 && !submitted,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = SEPrimary),
                            shape = RoundedCornerShape(13.dp)
                        ) {
                            Text("Check answer", fontWeight = FontWeight.Bold)
                        }

                        if (submitted) {
                            Spacer(Modifier.height(10.dp))
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(13.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (selectedAnswer == question.correct) {
                                        SELightGreen
                                    } else {
                                        SELightOrange
                                    }
                                )
                            ) {
                                Column(Modifier.padding(13.dp)) {
                                    Text(
                                        if (selectedAnswer == question.correct) "Correct" else "Review",
                                        color = if (selectedAnswer == question.correct) SEGreen else SEOrange,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        question.explanation,
                                        color = SEText,
                                        fontSize = 13.sp,
                                        lineHeight = 20.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(17.dp),
                    colors = CardDefaults.cardColors(containerColor = SELightPurple)
                ) {
                    Row(
                        modifier = Modifier.padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = SEPurple,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            Text(
                                "Independent study checkpoint",
                                color = SEPurple,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                content.checkpoint,
                                color = SEText,
                                fontSize = 13.sp,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class SpokenTask(
    val title: String,
    val instruction: String,
    val model: String
)

private data class SpokenUnitContent(
    val overview: String,
    val objectives: List<String>,
    val listenText: String,
    val learnTitle: String,
    val learnPoints: List<String>,
    val understandTitle: String,
    val understandText: String,
    val practiceTitle: String,
    val practiceInstructions: String,
    val practiceExamples: List<Pair<String, String>>,
    val tasks: List<SpokenTask>,
    val quiz: List<SpokenQuizQuestion>,
    val checkpoint: String
)

private fun spokenUnitContent(number: Int): SpokenUnitContent {
    return when (number) {
        2 -> SpokenUnitContent(
            overview = "This unit develops the learner's control of English speech sounds so that pronunciation becomes clearer, more intelligible and easier to monitor.",
            objectives = listOf(
                "Distinguish vowels, consonants, phonemes and syllables.",
                "Recognise the role of articulation and voicing in English consonants.",
                "Produce common short and long vowels with improved clarity.",
                "Use minimal pairs to notice and correct sound contrasts."
            ),
            listenText = "English pronunciation is based on contrasts between speech sounds. Clear articulation helps a speaker become more intelligible to a listener. Students should listen for differences in voicing, vowel quality and place of articulation, then practise producing the contrasts themselves.",
            learnTitle = "The sound system of spoken English",
            learnPoints = listOf(
                "A phoneme is a sound category that can distinguish meaning, as in ship and sheep.",
                "Vowels are produced with a relatively open vocal tract; consonants involve some constriction or closure.",
                "Voiced sounds use vibration of the vocal folds, while voiceless sounds do not.",
                "Minimal pairs such as fan/van and rice/raise are useful for focused pronunciation training."
            ),
            understandTitle = "Why intelligibility matters",
            understandText = "The goal of university-level pronunciation practice is not to erase a student's identity or accent. The goal is intelligibility: producing sounds clearly enough for listeners to understand the intended meaning without unnecessary effort. Listening, discrimination and controlled production should therefore be practised together.",
            practiceTitle = "Minimal pairs and articulation",
            practiceInstructions = "Read each pair slowly. First identify what changes; then pronounce the pair three times, keeping the rest of the word as stable as possible.",
            practiceExamples = listOf(
                "ship / sheep" to "short vowel /i/ contrast with long vowel /iː/",
                "fan / van" to "voiceless /f/ versus voiced /v/",
                "rice / rise" to "voiceless /s/ versus voiced /z/",
                "cat / cut" to "a change in vowel quality changes meaning"
            ),
            tasks = listOf(
                SpokenTask("Listening discrimination", "Listen to a partner pronounce two words and identify which word was spoken.", "Example: ship or sheep?"),
                SpokenTask("Controlled production", "Choose four minimal pairs and record yourself saying each pair three times.", "ship–sheep; fan–van; rice–rise; cat–cut"),
                SpokenTask("Peer feedback", "Ask a partner to identify one sound that was clear and one sound that needs another attempt.", "Use the sentence: 'Your /v/ was clear, but work again on /θ/.'")
            ),
            quiz = listOf(
                SpokenQuizQuestion("What is the main purpose of a minimal pair?", listOf("To compare meanings created by different sounds", "To memorise spelling rules", "To practise silent reading", "To increase speaking speed"), 0, "Minimal pairs contain words that differ in one sound and help learners notice sound contrasts that can change meaning.")
            ),
            checkpoint = "Can you produce and distinguish at least four pairs of contrasting English sounds without changing the surrounding sounds?"
        )

        3 -> SpokenUnitContent(
            overview = "This unit develops awareness of word stress, syllable prominence and common stress patterns so that spoken words are easier to recognise and understand.",
            objectives = listOf(
                "Identify syllables and the primary stressed syllable in words.",
                "Use stronger prominence through length, pitch, clarity and loudness.",
                "Notice stress shifts in common noun/verb pairs and word families.",
                "Use dictionaries or models to verify unfamiliar stress patterns."
            ),
            listenText = "English words contain stressed and unstressed syllables. The stressed syllable is usually more prominent through a combination of length, pitch, loudness and vowel quality. Correct word stress improves intelligibility and helps listeners recognise words more efficiently.",
            learnTitle = "How word stress works",
            learnPoints = listOf(
                "Every polysyllabic word has one primary stressed syllable, for example TAble, beGIN and reLAX.",
                "Unstressed vowels often become weaker, especially toward schwa /ə/.",
                "Some words change stress when their grammatical category changes, such as REcord (noun) and reCORD (verb).",
                "Stress can shift in word families: PHOtograph, phoTOGraphy, photoGRAphic."
            ),
            understandTitle = "Prominence is more than loudness",
            understandText = "A stressed syllable is not simply a louder syllable. It is typically more prominent because the speaker combines several cues: duration, pitch movement, clearer vowel quality and relative loudness. Effective practice should therefore focus on the whole rhythm of the word.",
            practiceTitle = "Mark, clap and produce",
            practiceInstructions = "Write the stressed syllable in CAPITAL LETTERS. Then clap once on the stressed syllable and pronounce the whole word naturally.",
            practiceExamples = listOf(
                "photograph" to "PHOtograph",
                "photography" to "phoTOGraphy",
                "record (noun)" to "REcord",
                "record (verb)" to "reCORD",
                "important" to "imPORtant"
            ),
            tasks = listOf(
                SpokenTask("Stress marking", "Mark the stressed syllable in ten course-related words, then compare with a reliable pronunciation model.", "ACAdemic; comMUniCATE; proNUNciation; uniVERsity"),
                SpokenTask("Word-family practice", "Say a noun, verb or adjective family and notice any stress movement.", "PHOtograph → phoTOGraphy → photoGRAphic"),
                SpokenTask("Contrastive speaking", "Use a noun/verb pair in two short sentences so the stress difference is meaningful.", "I need the REcord. / Please reCORD the lecture.")
            ),
            quiz = listOf(
                SpokenQuizQuestion("Which syllable receives primary stress in the word 'photography'?", listOf("PHO-", "TOG-", "RA-", "PHY"), 1, "The common pronunciation is pho-TOG-ra-phy, with primary stress on the second syllable.")
            ),
            checkpoint = "Can you mark the primary stress in unfamiliar academic words and confirm it with a dictionary or pronunciation model?"
        )

        4 -> SpokenUnitContent(
            overview = "This unit moves from individual words to connected speech. Students learn how prominence, rhythm and intonation organise spoken messages and signal meaning.",
            objectives = listOf(
                "Distinguish content words from function words in sentences.",
                "Place sentence stress on the words that carry the main information.",
                "Use thought groups and pausing to make speech easier to follow.",
                "Use falling and rising intonation appropriately in common interaction patterns."
            ),
            listenText = "In natural English, speakers do not give equal prominence to every word. Content words usually receive more stress, while grammatical words are often reduced. Intonation then helps the listener interpret whether a speaker is finishing, continuing, asking, checking or showing a particular attitude.",
            learnTitle = "Sentence stress, rhythm and intonation",
            learnPoints = listOf(
                "Content words usually include nouns, main verbs, adjectives, adverbs and important negatives.",
                "Function words such as articles, auxiliaries and prepositions are often less prominent in connected speech.",
                "Thought groups divide speech into manageable meaning units and support listener processing.",
                "Falling intonation often signals completion; rising intonation commonly signals a question, continuation or checking meaning."
            ),
            understandTitle = "Stress changes information focus",
            understandText = "Sentence stress can change what the speaker presents as new or important information. Compare: 'I wanted the BLUE book' and 'I WANTED the blue book.' The words are the same, but the prominence guides the listener toward a different interpretation of the message.",
            practiceTitle = "Mark the message",
            practiceInstructions = "Underline the words that should receive the strongest prominence. Read the sentence once neutrally, then again with the intended focus.",
            practiceExamples = listOf(
                "I NEED the BOOK today." to "Need and book carry the main information.",
                "She SENT the EMAIL yesterday." to "Sent, email and yesterday can be prominent.",
                "Are you READY?" to "A rising contour can keep the question open in interaction.",
                "Where are you GOING?" to "A falling contour commonly signals a completed WH-question."
            ),
            tasks = listOf(
                SpokenTask("Information focus", "Say one sentence three times, changing the stressed word each time. Ask a partner what meaning they infer.", "I ordered the NEW book. / I ORDERED the new book. / I ordered the new BOOK."),
                SpokenTask("Intonation practice", "Read a short dialogue and use a rising contour for checking questions and a falling contour for completed statements.", "You finished? ↗ / Yes, I finished. ↘"),
                SpokenTask("Connected speech", "Read a four-sentence paragraph using thought groups and sensible pauses rather than stopping after every word.", "Pause at natural meaning boundaries, not after each word.")
            ),
            quiz = listOf(
                SpokenQuizQuestion("Which group usually receives greater sentence stress?", listOf("Every article and preposition", "Important content words", "Only pronouns", "Only punctuation words"), 1, "Content words normally carry more lexical information and therefore receive greater prominence in neutral sentence stress.")
            ),
            checkpoint = "Can you read a short paragraph with clear thought groups, sensible prominence and appropriate final intonation?"
        )

        5 -> SpokenUnitContent(
            overview = "This unit transfers pronunciation and speaking skills into authentic everyday interactions, with special attention to register, politeness and communicative purpose.",
            objectives = listOf(
                "Choose greetings and introductions appropriate to context and relationship.",
                "Make requests, offers and suggestions politely and clearly.",
                "Respond appropriately to thanks, apologies and invitations.",
                "Use clarification and confirmation strategies when communication breaks down."
            ),
            listenText = "Effective everyday communication depends on more than grammar. Speakers choose expressions according to the relationship, level of formality, purpose and situation. They also listen actively, respond appropriately and repair misunderstandings when necessary.",
            learnTitle = "Language functions in real interaction",
            learnPoints = listOf(
                "Greetings vary by context: 'Good morning' is more formal than 'Hi' or 'Hey'.",
                "Polite requests often use forms such as 'Could you...?', 'Would you mind...?' and 'May I...?'.",
                "Clarification strategies include 'Could you repeat that?', 'Do you mean...?' and 'Could you explain what you mean by...?'.",
                "Register should match context: a classroom presentation, office reception and close friendship do not require identical language."
            ),
            understandTitle = "Communicative appropriateness",
            understandText = "A grammatically correct sentence can still be inappropriate for a particular situation. University students should learn to judge who they are speaking to, what they need to achieve, how formal the situation is and how much politeness is expected. This is communicative competence in practice.",
            practiceTitle = "Functional dialogues",
            practiceInstructions = "Read the situation, choose an appropriate expression and then continue the exchange naturally rather than memorising isolated sentences.",
            practiceExamples = listOf(
                "Request" to "Could you send me the article, please?",
                "Clarification" to "Could you explain the second point again?",
                "Apology" to "I'm sorry I'm late. The bus was delayed.",
                "Offer" to "Would you like me to help you with the presentation?",
                "Response to thanks" to "You're welcome."
            ),
            tasks = listOf(
                SpokenTask("Campus situation", "Role-play asking a teacher for clarification after a lecture. Use an appropriately respectful register.", "Excuse me, could you clarify the second example?"),
                SpokenTask("Service interaction", "Role-play asking for information at a university office and respond to follow-up questions.", "Could you tell me where the registration office is?"),
                SpokenTask("Repair strategy", "Create a misunderstanding deliberately, then repair it using a clarification or confirmation expression.", "Sorry, did you say Tuesday or Thursday?")
            ),
            quiz = listOf(
                SpokenQuizQuestion("Which expression is most suitable for a polite request to a university teacher?", listOf("Give me the file.", "Send this now.", "Could you please send me the file?", "You have to send it."), 2, "'Could you please...?' is a conventional polite request that fits many academic contexts.")
            ),
            checkpoint = "Can you adapt the same message to a friend, a classmate and a teacher without changing the intended meaning?"
        )

        else -> SpokenUnitContent(
            overview = "This unit develops independent spoken interaction: managing turns, maintaining a conversation, repairing breakdowns and speaking with increasing fluency and confidence.",
            objectives = listOf(
                "Enter and leave conversations appropriately.",
                "Use turn-taking signals, backchannels and follow-up questions.",
                "Maintain speech through planning, pausing and useful discourse markers.",
                "Repair misunderstandings and negotiate meaning without abandoning the interaction."
            ),
            listenText = "Fluent speakers do not speak perfectly or without pauses. They manage interaction effectively. They know how to enter a conversation, hold the floor, respond to another speaker, ask follow-up questions, clarify meaning and continue after a moment of hesitation.",
            learnTitle = "Fluency as interactive competence",
            learnPoints = listOf(
                "Turn-taking involves signalling when you want to speak, continue or finish.",
                "Backchannels such as 'right', 'I see', 'exactly' and 'mm-hm' show active listening.",
                "Discourse markers such as 'well', 'actually', 'so' and 'for example' help organise spoken ideas.",
                "Repair strategies keep communication moving: 'Let me rephrase that', 'What I mean is...' and 'Sorry, I didn't catch that.'"
            ),
            understandTitle = "Fluency is not the absence of pauses",
            understandText = "At university level, fluency means being able to sustain meaningful communication with reasonable continuity, appropriate pacing and effective interaction. Strategic pauses can help a speaker plan the next idea. The objective is controlled, purposeful speech rather than maximum speed.",
            practiceTitle = "Turn-taking and repair",
            practiceInstructions = "Practise the interaction in pairs. Speaker A talks for 30–45 seconds. Speaker B asks one follow-up question, then each speaker must use one clarification or repair expression.",
            practiceExamples = listOf(
                "Enter a turn" to "Can I add something here?",
                "Hold the floor" to "The main point is... and there is another issue I want to mention.",
                "Invite response" to "What do you think?",
                "Repair" to "Let me put that another way.",
                "Clarify" to "Do you mean that the course is compulsory?"
            ),
            tasks = listOf(
                SpokenTask("One-minute talk", "Speak for one minute about a familiar academic topic. Use at least two discourse markers and one follow-up question.", "Well, first of all... For example... What do you think?"),
                SpokenTask("Paired problem-solving", "Discuss a campus problem and agree on two realistic solutions. Each speaker must contribute at least three ideas.", "Let's consider the first option. What about the cost?"),
                SpokenTask("Repair challenge", "Tell a short story while deliberately correcting one idea. Use a repair strategy naturally.", "I studied in Rabat—sorry, I mean I studied in Fès during my first year.")
            ),
            quiz = listOf(
                SpokenQuizQuestion("Which strategy is most useful when you need time to organise your next idea?", listOf("Stop the conversation completely", "Use a strategic pause or discourse marker", "Change the topic immediately", "Repeat every word twice"), 1, "Strategic pauses and discourse markers can give speakers planning time while maintaining the flow of interaction.")
            ),
            checkpoint = "Can you sustain a one-minute conversation, ask a follow-up question, and repair a misunderstanding without switching to your first language?"
        )
    }
}

@Composable
private fun SpokenSectionTitle(text: String) {
    Text(
        text,
        color = SEText,
        fontSize = 21.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun SpokenBulletCard(points: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(18.dp)) {
            points.forEachIndexed { index, point ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        "${index + 1}.",
                        color = SEPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(9.dp))
                    Text(
                        point,
                        color = SEText,
                        fontSize = 14.sp,
                        lineHeight = 21.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SpokenListenCard(
    title: String,
    subtitle: String,
    speaking: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(containerColor = SELightBlue)
    ) {
        Row(
            modifier = Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.VolumeUp,
                contentDescription = null,
                tint = SEPrimary,
                modifier = Modifier.size(30.dp)
            )
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = SEText, fontWeight = FontWeight.Bold)
                Text(subtitle, color = SEGray, fontSize = 12.sp, lineHeight = 18.sp)
            }
            IconButton(onClick = onClick) {
                Icon(
                    if (speaking) Icons.Default.Stop else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = SEPrimary
                )
            }
        }
    }
}

@Composable
private fun SpokenDefinitionCard(title: String, text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(18.dp)) {
            Text(title, color = SEPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(7.dp))
            Text(text, color = SEText, fontSize = 15.sp, lineHeight = 23.sp)
        }
    }
}

@Composable
private fun SpokenPracticeCard(
    title: String,
    instructions: String,
    examples: List<Pair<String, String>>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(18.dp)) {
            Text(title, color = SEPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(7.dp))
            Text(instructions, color = SEGray, fontSize = 14.sp, lineHeight = 21.sp)
            Spacer(Modifier.height(11.dp))
            examples.forEach { example ->
                Row(
                    modifier = Modifier.padding(vertical = 5.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        example.first,
                        color = SEText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.width(120.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        example.second,
                        color = SEGray,
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun SpokenTaskSelector(
    tasks: List<SpokenTask>,
    selected: Int,
    onSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        tasks.forEachIndexed { index, task ->
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onSelected(index) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (selected == index) SEPrimary else Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "${index + 1}",
                        color = if (selected == index) Color.White else SEPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        task.title,
                        color = if (selected == index) Color.White else SEText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SpokenTaskCard(task: SpokenTask) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(18.dp)) {
            Text(task.title, color = SEPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(7.dp))
            Text(task.instruction, color = SEText, fontSize = 14.sp, lineHeight = 21.sp)
            Spacer(Modifier.height(10.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SELightBlue)
            ) {
                Column(Modifier.padding(13.dp)) {
                    Text("Model", color = SEPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(task.model, color = SEText, fontSize = 14.sp, lineHeight = 20.sp)
                }
            }
        }
    }
}

@Composable
private fun SpokenCourseHeader() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SEPrimary)
    ) {
        Row(
            modifier = Modifier.padding(21.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("🎙️", fontSize = 42.sp)
            Spacer(Modifier.width(14.dp))
            Column {
                Text(
                    "Spoken English",
                    color = Color.White,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Develop accurate, fluent and confident oral communication.",
                    color = Color.White.copy(alpha = 0.90f),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
private fun SpokenProgressCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Course progress", color = SEGray, fontSize = 13.sp)
                    Text(
                        "0 / 6 units",
                        color = SEText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp
                    )
                }
                Text(
                    "0%",
                    color = SEPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(11.dp))

            LinearProgressIndicator(
                progress = { 0f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = SEPrimary,
                trackColor = Color(0xFFE2E8F0)
            )
        }
    }
}

@Composable
private fun SpokenUnitCard(
    unit: SpokenUnit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(17.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(
                        SELightBlue,
                        RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(unit.icon, fontSize = 27.sp)
            }

            Spacer(Modifier.width(14.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    "UNIT ${unit.number}",
                    color = SEPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    unit.title,
                    color = SEText,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    unit.description,
                    color = SEGray,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
            }

            Spacer(Modifier.width(8.dp))

            Icon(
                Icons.Default.PlayArrow,
                contentDescription = "Open Unit ${unit.number}",
                tint = SEPrimary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun SpokenUnitOneScreen(onBack: () -> Unit) {
    var selectedSection by remember { mutableIntStateOf(0) }

    val sections = listOf(
        "Listen",
        "Pronunciation",
        "Repeat",
        "Dialogue",
        "Speaking",
        "Assessment"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Unit 1",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Introduction to Spoken English",
                            color = Color.White.copy(alpha = 0.88f),
                            fontSize = 11.sp
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
                    containerColor = SEPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SEBackground)
                .padding(padding)
        ) {
            SpokenSectionNavigation(
                selected = selectedSection,
                sections = sections,
                onSelected = { selectedSection = it }
            )

            when (selectedSection) {
                0 -> SpokenListenSection()
                1 -> SpokenPronunciationSection()
                2 -> SpokenRepeatSection()
                3 -> SpokenDialogueSection()
                4 -> SpokenSpeakingSection()
                5 -> SpokenAssessmentSection()
            }
        }
    }
}

@Composable
private fun SpokenSectionNavigation(
    selected: Int,
    sections: List<String>,
    onSelected: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .background(Color.White)
    ) {
        item {
            Row(
                modifier = Modifier.padding(7.dp),
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                sections.forEachIndexed { index, title ->
                    Box(
                        modifier = Modifier
                            .background(
                                if (selected == index) SEPrimary else Color(0xFFF1F5F9),
                                RoundedCornerShape(11.dp)
                            )
                            .clickable { onSelected(index) }
                            .padding(horizontal = 11.dp, vertical = 8.dp)
                    ) {
                        Text(
                            title,
                            color = if (selected == index) Color.White else SEGray,
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
private fun SpokenListenSection() {
    val context = LocalContext.current
    val tts = rememberEnglishTts()
    var speaking by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                "Introduction to Spoken English",
                color = SEText,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "An academic introduction to oral communication in English.",
                color = SEGray
            )
        }

        item {
            SpokenAcademicCard(
                title = "Learning objectives",
                points = listOf(
                    "Define spoken English as a form of language used for oral communication.",
                    "Distinguish between accuracy, fluency, pronunciation and interaction.",
                    "Recognise the importance of listening in successful communication.",
                    "Use basic communication strategies in short academic and everyday exchanges."
                )
            )
        }

        item {
            SpokenAcademicCard(
                title = "What is Spoken English?",
                points = listOf(
                    "Spoken English is the use of English in oral communication between speakers.",
                    "Speech is produced in real time and normally involves pronunciation, rhythm, stress, intonation and interaction.",
                    "Effective communication requires grammatical knowledge, listening, appropriate responses and adaptation to context."
                )
            )
        }

        item {
            SpokenAudioCard(
                title = "Listen to the academic explanation",
                speaking = speaking,
                onPlay = {
                    if (EnglishStudiesSettings.isSoundEnabled(context)) {
                        tts?.language = EnglishStudiesSettings.englishLocale(context)
                        tts?.setSpeechRate(EnglishStudiesSettings.speechRate(context))
                        speaking = true
                        tts?.speak(
                            SPOKEN_INTRO,
                            TextToSpeech.QUEUE_FLUSH,
                            null,
                            "spoken_intro"
                        )
                    }
                },
                onStop = {
                    speaking = false
                    tts?.stop()
                }
            )
        }

        item {
            SpokenAcademicCard(
                title = "Key concepts",
                points = listOf(
                    "Accuracy: using correct grammar, vocabulary and pronunciation.",
                    "Fluency: speaking at a reasonable pace with appropriate continuity.",
                    "Pronunciation: producing sounds, stress and intonation clearly enough to be understood.",
                    "Interaction: listening, taking turns, responding and maintaining communication.",
                    "Communicative competence: choosing language appropriate to context and purpose."
                )
            )
        }
    }
}

@Composable
private fun SpokenPronunciationSection() {
    val context = LocalContext.current
    val tts = rememberEnglishTts()
    var speaking by remember { mutableStateOf(false) }

    val examples = listOf(
        "ship — sheep" to "These words differ in vowel quality and length.",
        "cat — cut" to "The vowel sounds are different and should be distinguished.",
        "record (noun) — record (verb)" to "Stress can change according to grammatical function."
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                "Pronunciation Foundations",
                color = SEText,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Pronunciation is studied through sounds, stress, rhythm and intonation.",
                color = SEGray
            )
        }

        item {
            SpokenAcademicCard(
                title = "Three dimensions of pronunciation",
                points = listOf(
                    "Segmental features: individual vowel and consonant sounds.",
                    "Suprasegmental features: stress, rhythm and intonation.",
                    "Intelligibility: producing speech clearly enough for the listener to understand the intended message."
                )
            )
        }

        item {
            SpokenAudioCard(
                title = "Listen to the pronunciation examples",
                speaking = speaking,
                onPlay = {
                    if (EnglishStudiesSettings.isSoundEnabled(context)) {
                        val text = examples.joinToString(". ") {
                            "${it.first}. ${it.second}"
                        }
                        tts?.language = EnglishStudiesSettings.englishLocale(context)
                        tts?.setSpeechRate(EnglishStudiesSettings.speechRate(context))
                        speaking = true
                        tts?.speak(
                            text,
                            TextToSpeech.QUEUE_FLUSH,
                            null,
                            "pronunciation_examples"
                        )
                    }
                },
                onStop = {
                    speaking = false
                    tts?.stop()
                }
            )
        }

        itemsIndexed(examples) { index, example ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(Modifier.padding(17.dp)) {
                    Text(
                        "Example ${index + 1}",
                        color = SEPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        example.first,
                        color = SEText,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(3.dp))
                    Text(
                        example.second,
                        color = SEGray,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SpokenRepeatSection() {
    val context = LocalContext.current
    val tts = rememberEnglishTts()
    var speaking by remember { mutableStateOf(false) }
    var currentPhrase by remember {
        mutableStateOf("Good morning. My name is Sara.")
    }

    val phrases = listOf(
        "Good morning. My name is Sara.",
        "I am a first-year English Studies student.",
        "I am interested in literature and language learning.",
        "My academic goal is to improve my speaking skills."
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "Repeat Practice",
                color = SEText,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Listen to a model sentence, then repeat it aloud.",
                color = SEGray
            )
        }

        itemsIndexed(phrases) { index, phrase ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(17.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (phrase == currentPhrase) SELightBlue else Color.White
                )
            ) {
                Column(Modifier.padding(17.dp)) {
                    Text(
                        "Practice ${index + 1}",
                        color = SEPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        phrase,
                        color = SEText,
                        fontSize = 16.sp,
                        lineHeight = 23.sp
                    )
                    Spacer(Modifier.height(10.dp))
                    Button(
                        onClick = {
                            currentPhrase = phrase
                            if (EnglishStudiesSettings.isSoundEnabled(context)) {
                                tts?.language = EnglishStudiesSettings.englishLocale(context)
                                tts?.setSpeechRate(EnglishStudiesSettings.speechRate(context))
                                speaking = true
                                tts?.speak(
                                    phrase,
                                    TextToSpeech.QUEUE_FLUSH,
                                    null,
                                    "repeat_$index"
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SEPrimary
                        )
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(Modifier.width(7.dp))
                        Text("Listen & Repeat")
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SELightOrange)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "Technique",
                        color = SEOrange,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(5.dp))
                    Text(
                        "Listen once, notice the pronunciation and rhythm, then repeat the sentence at a natural pace.",
                        color = SEText,
                        fontSize = 14.sp,
                        lineHeight = 21.sp
                    )
                    if (speaking) {
                        Spacer(Modifier.height(8.dp))
                        IconButton(onClick = {
                            speaking = false
                            tts?.stop()
                        }) {
                            Icon(
                                Icons.Default.Stop,
                                contentDescription = "Stop",
                                tint = SERed
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SpokenDialogueSection() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                "Academic Communication Dialogue",
                color = SEText,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Observe how speakers request, respond and close an interaction politely.",
                color = SEGray,
                lineHeight = 21.sp
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text(
                        "Dialogue",
                        color = SEPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        "A: Good morning. Could you explain this point, please?\n\n" +
                                "B: Certainly. The main idea is that effective communication depends on both speaking and listening.\n\n" +
                                "A: Thank you. That makes the concept clearer.",
                        color = SEText,
                        fontSize = 15.sp,
                        lineHeight = 25.sp
                    )
                    Spacer(Modifier.height(11.dp))
                    Text(
                        "Notice: the speakers use a polite request, a clear response and an appropriate closing.",
                        color = SEGray,
                        fontSize = 13.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(17.dp),
                colors = CardDefaults.cardColors(containerColor = SELightPurple)
            ) {
                Column(Modifier.padding(17.dp)) {
                    Text(
                        "Interaction focus",
                        color = SEPurple,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Listen → understand → respond → maintain the interaction.",
                        color = SEText,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SpokenSpeakingSection() {
    val context = LocalContext.current
    var recognized by remember { mutableStateOf("") }
    var feedback by remember { mutableStateOf("") }
    var listening by remember { mutableStateOf(false) }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                listening = true
                startEnglishRecognition(
                    context = context,
                    onResult = {
                        recognized = it
                        listening = false
                        feedback = evaluateSpeaking(it)
                    },
                    onError = {
                        listening = false
                        feedback = "Speech could not be recognised clearly. Please try again."
                    }
                )
            }
        }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                "Guided Speaking Practice",
                color = SEText,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Speak clearly and use complete sentences.",
                color = SEGray
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = SELightOrange)
            ) {
                Column(Modifier.padding(19.dp)) {
                    Text(
                        "Speaking task",
                        color = SEOrange,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Introduce yourself in English. Mention your name, field of study, interests and one academic goal.",
                        color = SEText,
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(Modifier.padding(19.dp)) {
                    Text(
                        "Model response",
                        color = SEPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(7.dp))
                    Text(
                        "Good morning. My name is Sara. I am a first-year English Studies student. " +
                                "I am interested in literature and language learning. " +
                                "My academic goal is to improve my speaking skills and communicate confidently in English.",
                        color = SEText,
                        fontSize = 15.sp,
                        lineHeight = 24.sp
                    )
                }
            }
        }

        item {
            Button(
                onClick = {
                    if (
                        context.checkSelfPermission(Manifest.permission.RECORD_AUDIO) ==
                        PackageManager.PERMISSION_GRANTED
                    ) {
                        listening = true
                        startEnglishRecognition(
                            context = context,
                            onResult = {
                                recognized = it
                                listening = false
                                feedback = evaluateSpeaking(it)
                            },
                            onError = {
                                listening = false
                                feedback = "Please try again."
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
                    containerColor = if (listening) SERed else SEPrimary
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(
                    if (listening) Icons.Default.Stop else Icons.Default.KeyboardVoice,
                    contentDescription = null
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    if (listening) "Listening..." else "Start Speaking",
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (recognized.isNotBlank()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (feedback.startsWith("Good")) {
                            SELightGreen
                        } else {
                            SELightBlue
                        }
                    )
                ) {
                    Column(Modifier.padding(19.dp)) {
                        Text(
                            "Recognised speech",
                            color = SEGreen,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            recognized,
                            color = SEText,
                            fontSize = 15.sp,
                            lineHeight = 23.sp
                        )
                        Spacer(Modifier.height(9.dp))
                        Text(
                            feedback,
                            color = SEText,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            lineHeight = 21.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SpokenAssessmentSection() {
    var current by remember { mutableIntStateOf(0) }
    var selected by remember { mutableStateOf<Int?>(null) }
    var score by remember { mutableIntStateOf(0) }

    val question = SPEAKING_QUIZ[current]

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                "Unit Assessment",
                color = SEText,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Check your understanding of Unit 1.",
                color = SEGray
            )
        }

        item {
            Text(
                "Question ${current + 1} / ${SPEAKING_QUIZ.size}",
                color = SEPrimary,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(Modifier.padding(19.dp)) {
                    Text(
                        question.question,
                        color = SEText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 25.sp
                    )
                    Spacer(Modifier.height(14.dp))

                    question.options.forEachIndexed { index, option ->
                        val isCorrect = selected != null && index == question.correct
                        val isWrong = selected == index && index != question.correct
                        val background = when {
                            isCorrect -> SELightGreen
                            isWrong -> SELightRed
                            else -> Color(0xFFF8FAFC)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    background,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable(enabled = selected == null) {
                                    selected = index
                                    if (index == question.correct) score++
                                }
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "${('A'.code + index).toChar()}.",
                                color = SEPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.width(9.dp))
                            Text(
                                option,
                                color = SEText,
                                modifier = Modifier.weight(1f),
                                fontSize = 14.sp
                            )
                            if (isCorrect) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = SEGreen
                                )
                            }
                        }

                        Spacer(Modifier.height(7.dp))
                    }

                    if (selected != null) {
                        Spacer(Modifier.height(6.dp))
                        Text(
                            if (selected == question.correct) "Correct!" else "Review the concept.",
                            color = if (selected == question.correct) SEGreen else SERed,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(Modifier.height(5.dp))
                        Text(
                            question.explanation,
                            color = SEGray,
                            fontSize = 13.sp,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }

        if (selected != null) {
            item {
                Button(
                    onClick = {
                        if (current < SPEAKING_QUIZ.lastIndex) {
                            current += 1
                            selected = null
                        } else {
                            current = 0
                            selected = null
                            score = 0
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SEPrimary
                    )
                ) {
                    Text(
                        if (current < SPEAKING_QUIZ.lastIndex) {
                            "Next Question"
                        } else {
                            "Restart Assessment"
                        }
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🏆", fontSize = 30.sp)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            "Current score",
                            color = SEGray,
                            fontSize = 13.sp
                        )
                        Text(
                            "$score / ${SPEAKING_QUIZ.size}",
                            color = SEText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 21.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SpokenAcademicCard(
    title: String,
    points: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(19.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = SEPrimary
                )
                Spacer(Modifier.width(9.dp))
                Text(
                    title,
                    color = SEText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp
                )
            }

            Spacer(Modifier.height(11.dp))

            points.forEachIndexed { index, point ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        "${index + 1}.",
                        color = SEPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        point,
                        color = SEText,
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SpokenAudioCard(
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
                tint = SEPrimary,
                modifier = Modifier.size(30.dp)
            )

            Spacer(Modifier.width(11.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    title,
                    color = SEText,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Listen carefully and repeat.",
                    color = SEGray,
                    fontSize = 12.sp
                )
            }

            IconButton(
                onClick = {
                    if (speaking) onStop() else onPlay()
                }
            ) {
                Icon(
                    if (speaking) Icons.Default.Stop else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = SEPrimary
                )
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
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            Locale.US.toLanguageTag()
        )
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
            Locale.US.toLanguageTag()
        )
        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
        putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
    }

    recognizer.setRecognitionListener(
        object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) = Unit
            override fun onBeginningOfSpeech() = Unit
            override fun onRmsChanged(rmsdB: Float) = Unit
            override fun onBufferReceived(buffer: ByteArray?) = Unit
            override fun onEndOfSpeech() = Unit
            override fun onPartialResults(partialResults: Bundle?) = Unit
            override fun onEvent(eventType: Int, params: Bundle?) = Unit

            override fun onError(error: Int) {
                recognizer.destroy()
                onError()
            }

            override fun onResults(results: Bundle?) {
                val result = results
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.firstOrNull()

                recognizer.destroy()

                if (result.isNullOrBlank()) {
                    onError()
                } else {
                    onResult(result)
                }
            }
        }
    )

    recognizer.startListening(intent)
}

private fun evaluateSpeaking(text: String): String {
    val words = text
        .trim()
        .split("\\s+".toRegex())
        .filter { it.isNotBlank() }

    return when {
        words.size >= 35 ->
            "Good. You produced a sufficiently developed response. Continue working on pronunciation, grammar and natural rhythm."
        words.size >= 15 ->
            "Good start. Develop your response with more information and connect your ideas clearly."
        words.isNotEmpty() ->
            "Your response is short. Try to speak in complete sentences and add supporting information."
        else ->
            "No clear speech was detected."
    }
}

private const val SPOKEN_INTRO =
    "Spoken English is the use of English for oral communication. " +
            "Successful spoken communication requires accuracy, fluency, clear pronunciation and effective interaction. " +
            "A speaker must also listen carefully, respond appropriately and adapt language to the communicative context."
