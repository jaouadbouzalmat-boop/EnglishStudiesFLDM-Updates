@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package ma.fldm.englishstudies

import android.Manifest
import androidx.compose.material.icons.filled.ArrowForward
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

private val GRPrimary = Color(0xFF2563EB)
private val GRBackground = Color(0xFFF8FAFC)
private val GRText = Color(0xFF172033)
private val GRGray = Color(0xFF64748B)
private val GRLightBlue = Color(0xFFEFF6FF)
private val GRGreen = Color(0xFF16A34A)
private val GRLightGreen = Color(0xFFDCFCE7)
private val GRRed = Color(0xFFDC2626)
private val GRLightRed = Color(0xFFFEE2E2)
private val GROrange = Color(0xFFF59E0B)
private val GRLightOrange = Color(0xFFFEF3C7)
private val GRPurple = Color(0xFF7C3AED)
private val GRLightPurple = Color(0xFFF3E8FF)

private data class GRUnit(
    val number: Int,
    val title: String,
    val description: String,
    val icon: String,
    val enabled: Boolean
)

private data class GRQuizQuestion(
    val question: String,
    val options: List<String>,
    val correct: Int,
    val explanation: String
)

private val GUIDED_UNITS = listOf(
    GRUnit(
        1,
        "Introduction to Academic Reading",
        "Understand academic reading, purpose, prediction and active reading.",
        "📚",
        true
    ),
    GRUnit(
        2,
        "Reading Strategies",
        "Develop purposeful and efficient reading habits.",
        "🧠",
        true
    ),
    GRUnit(
        3,
        "Skimming and Scanning",
        "Locate general information and specific details.",
        "🔎",
        true
    ),
    GRUnit(
        4,
        "Main Ideas and Supporting Details",
        "Identify the organisation of academic texts.",
        "🎯",
        true
    ),
    GRUnit(
        5,
        "Context Clues and Vocabulary",
        "Infer meaning from context.",
        "📝",
        true
    ),
    GRUnit(
        6,
        "Note-Taking",
        "Record key information efficiently while studying.",
        "📒",
        true
    ),
    GRUnit(
        7,
        "Summarising and Paraphrasing",
        "Reduce and reformulate information accurately.",
        "✍️",
        true
    ),
    GRUnit(
        8,
        "Study Skills and Final Review",
        "Apply academic study strategies independently.",
        "🎓",
        true
    )
)

private val GUIDED_QUIZ = listOf(
    GRQuizQuestion(
        "What is the main purpose of previewing a text?",
        listOf(
            "To translate every word",
            "To understand its organisation and predict content",
            "To memorise the text",
            "To avoid reading"
        ),
        1,
        "Previewing helps the reader anticipate the topic, purpose and organisation before detailed reading."
    ),
    GRQuizQuestion(
        "Which strategy is useful for locating a specific date or name?",
        listOf(
            "Scanning",
            "Summarising",
            "Paraphrasing",
            "Free writing"
        ),
        0,
        "Scanning is used to locate specific information quickly."
    ),
    GRQuizQuestion(
        "What is the main idea of a text?",
        listOf(
            "Any word in the text",
            "The central point developed by the text",
            "The longest sentence",
            "The title only"
        ),
        1,
        "The main idea is the central point the writer develops."
    ),
    GRQuizQuestion(
        "Why should a reader use context clues?",
        listOf(
            "To infer meaning from surrounding information",
            "To avoid understanding the text",
            "To replace all reading",
            "To memorise a dictionary"
        ),
        0,
        "Context clues help the reader infer meaning from the surrounding text."
    ),
    GRQuizQuestion(
        "What is active reading?",
        listOf(
            "Reading without thinking",
            "Reading while predicting, questioning and checking understanding",
            "Reading only titles",
            "Reading as quickly as possible"
        ),
        1,
        "Active reading involves deliberate strategies before, during and after reading."
    )
)

@Composable
fun GuidedReadingScreen(
    onBack: () -> Unit = {},
    onOpenOliverTwist: () -> Unit = {}
) {
    var openedUnit by remember { mutableIntStateOf(0) }

    when {
        openedUnit == 1 -> {
            GuidedUnitOneScreen(
                onBack = { openedUnit = 0 }
            )
            return
        }

        openedUnit in 2..8 -> {
            GuidedUnitPedagogicalScreen(
                unitNumber = openedUnit,
                onBack = { openedUnit = 0 }
            )
            return
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Guided Reading 1",
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
                    containerColor = GRPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(GRBackground)
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { GuidedHeader() }
            item { GuidedProgressCard() }

            item {
                Text(
                    "Course Units",
                    color = GRText,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "The course moves from reading foundations to independent study skills.",
                    color = GRGray,
                    fontSize = 13.sp
                )
            }

            itemsIndexed(GUIDED_UNITS) { _, unit ->
                GuidedUnitCard(
                    unit = unit,
                    onClick = { openedUnit = unit.number }
                )

                if (unit.number == 1) {
                    OliverTwistAccessCard(
                        onClick = onOpenOliverTwist
                    )
                }
            }
        }
    }
}


@Composable
private fun OliverTwistAccessCard(
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEFF6FF)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .background(
                        Color.White,
                        RoundedCornerShape(15.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("📖", fontSize = 30.sp)
            }

            Spacer(Modifier.width(14.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    "OLIVER TWIST",
                    color = GRPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Guided Reading — Charles Dickens",
                    color = GRText,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    "Main literary text • Read, understand and practise.",
                    color = GRGray,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Open Oliver Twist",
                tint = GRPrimary,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun GuidedHeader() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = GRPrimary)
    ) {
        Row(
            modifier = Modifier.padding(21.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("📚", fontSize = 42.sp)
            Spacer(Modifier.width(14.dp))
            Column {
                Text(
                    "Guided Reading 1",
                    color = Color.White,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Academic reading and effective study strategies.",
                    color = Color.White.copy(alpha = 0.90f),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
private fun GuidedProgressCard() {
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
                    Text("Course progress", color = GRGray, fontSize = 13.sp)
                    Text(
                        "0 / ${GUIDED_UNITS.size} units",
                        color = GRText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp
                    )
                }
                Text("0%", color = GRPrimary, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(11.dp))
            LinearProgressIndicator(
                progress = { 0f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = GRPrimary,
                trackColor = Color(0xFFE2E8F0)
            )
        }
    }
}

@Composable
private fun GuidedUnitCard(
    unit: GRUnit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
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
                        GRLightBlue,
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
                    color = GRPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    unit.title,
                    color = GRText,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    unit.description,
                    color = GRGray,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
            }

            Spacer(Modifier.width(8.dp))

            Icon(
                Icons.Default.PlayArrow,
                contentDescription = "Open Unit ${unit.number}",
                tint = GRPrimary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}



private data class GuidedPedagogicalSection(
    val title: String,
    val purpose: String,
    val keyPoints: List<String>,
    val practice: String,
    val application: String,
    val check: String
)

private fun unitSections(unitNumber: Int): Pair<String, List<GuidedPedagogicalSection>> {
    return when (unitNumber) {
        2 -> "Unit 2 • Reading Strategies" to listOf(
            GuidedPedagogicalSection(
                "Previewing and setting a purpose",
                "Learn to enter a text with a deliberate reading goal.",
                listOf(
                    "Preview the title, headings, opening and closing paragraphs.",
                    "Ask what you already know and what you expect to learn.",
                    "Choose a purpose: general understanding, information search or critical evaluation."
                ),
                "Preview an academic text for one minute and write two predictions before reading.",
                "Read the first section and underline only information relevant to your chosen purpose.",
                "Can you state your reading purpose in one precise sentence?"
            ),
            GuidedPedagogicalSection(
                "Questioning while reading",
                "Turn passive reading into active academic processing.",
                listOf(
                    "Convert headings into questions.",
                    "Ask why, how and what evidence questions.",
                    "Pause after each section to test your understanding."
                ),
                "Write three questions for a short university reading and answer them from the text.",
                "Use one question to guide a second reading of the same passage.",
                "Did your questions help you identify important information?"
            ),
            GuidedPedagogicalSection(
                "Monitoring comprehension",
                "Detect and repair breakdowns in understanding.",
                listOf(
                    "Notice when a sentence, paragraph or argument is unclear.",
                    "Reread strategically instead of restarting the whole text.",
                    "Use surrounding sentences, headings and examples to repair meaning."
                ),
                "Mark one unclear sentence in a passage and identify the strategy used to clarify it.",
                "Explain aloud how you resolved one comprehension problem.",
                "What did you do when your understanding broke down?"
            ),
            GuidedPedagogicalSection(
                "Independent strategy choice",
                "Select strategies according to task demands.",
                listOf(
                    "Different purposes require different strategies.",
                    "Efficient readers combine previewing, questioning and monitoring.",
                    "Strategy choice should be justified, not automatic."
                ),
                "For three reading tasks, choose the best strategy and justify your choice.",
                "Create a personal reading routine for weekly university study.",
                "Can you explain why one strategy is more appropriate than another?"
            )
        )

        3 -> "Unit 3 • Skimming and Scanning" to listOf(
            GuidedPedagogicalSection(
                "Skimming for the global message",
                "Identify the general topic and organisation quickly.",
                listOf(
                    "Read the title, introduction, headings and topic sentences.",
                    "Notice repeated concepts and concluding signals.",
                    "Avoid stopping for every unfamiliar word."
                ),
                "Skim a two-page text in two minutes and write its topic and likely structure.",
                "Give a 30-second oral overview of the text.",
                "Can you state what the text is mainly about without rereading every line?"
            ),
            GuidedPedagogicalSection(
                "Scanning for specific information",
                "Locate names, dates, numbers and key terms efficiently.",
                listOf(
                    "Predict the visual form of the information you need.",
                    "Move your eyes quickly across the text rather than reading line by line.",
                    "Confirm the surrounding sentence before recording an answer."
                ),
                "Find five specific facts in a text and record the exact supporting location.",
                "Design five scanning questions for a classmate.",
                "Did you confirm each detail in context?"
            ),
            GuidedPedagogicalSection(
                "Choosing skimming or scanning",
                "Match strategy to purpose.",
                listOf(
                    "Use skimming for overall meaning and organisation.",
                    "Use scanning for targeted retrieval.",
                    "Combine both when a task asks for global understanding followed by evidence."
                ),
                "Classify ten reading tasks as skimming, scanning or both.",
                "Apply the correct strategy to a course handout.",
                "Can you justify your strategy choice?"
            ),
            GuidedPedagogicalSection(
                "Speed with accuracy",
                "Develop efficient reading without sacrificing understanding.",
                listOf(
                    "Speed is useful only when comprehension remains adequate.",
                    "Use short timed practice followed by an accuracy check.",
                    "Increase speed gradually as control improves."
                ),
                "Complete a timed skim and a timed scan, then verify your answers.",
                "Record your time and accuracy in a study log.",
                "Did increased speed reduce your accuracy?"
            )
        )

        4 -> "Unit 4 • Main Ideas and Supporting Details" to listOf(
            GuidedPedagogicalSection(
                "Finding the main idea",
                "Identify the central message of a paragraph or section.",
                listOf(
                    "Ask what the writer is mainly saying about the topic.",
                    "Distinguish a central claim from an isolated detail.",
                    "Use repeated concepts and topic-sentence signals as evidence."
                ),
                "Read five paragraphs and write a one-sentence main idea for each.",
                "Compare your main idea with the paragraph's supporting evidence.",
                "Does your sentence capture the whole paragraph rather than one detail?"
            ),
            GuidedPedagogicalSection(
                "Topic, main idea and controlling claim",
                "Differentiate closely related concepts.",
                listOf(
                    "The topic is the general subject.",
                    "The main idea states what the writer says about that topic.",
                    "A controlling claim gives a focused direction to the discussion."
                ),
                "Label topic, main idea and supporting claim in sample paragraphs.",
                "Write one paragraph from a given topic and controlling idea.",
                "Can you explain the difference without relying on an example?"
            ),
            GuidedPedagogicalSection(
                "Supporting details and evidence",
                "Recognise how writers develop a central idea.",
                listOf(
                    "Details may include examples, explanations, reasons, data or quotations.",
                    "Strong details clearly connect to the main idea.",
                    "Evidence should be relevant and sufficiently specific."
                ),
                "Colour-code the main idea and three supporting details in a short text.",
                "Add one relevant supporting detail to an underdeveloped paragraph.",
                "Does each detail genuinely support the main point?"
            ),
            GuidedPedagogicalSection(
                "Text organisation",
                "See how ideas are connected across paragraphs.",
                listOf(
                    "Look for cause-effect, comparison, problem-solution and chronological patterns.",
                    "Signal words help identify relationships.",
                    "Organisation supports comprehension and note-taking."
                ),
                "Identify the organisational pattern of four short texts.",
                "Create a simple outline showing the hierarchy of ideas.",
                "Can you describe the text's structure in one sentence?"
            )
        )

        5 -> "Unit 5 • Context Clues and Vocabulary" to listOf(
            GuidedPedagogicalSection(
                "Context as a source of meaning",
                "Infer unfamiliar vocabulary without immediate dictionary dependence.",
                listOf(
                    "Read the sentence containing the unknown word.",
                    "Read one or two surrounding sentences.",
                    "Ask what meaning fits the argument and grammatical role."
                ),
                "Infer the meaning of five unfamiliar words from a university passage.",
                "Check your inferences with a reliable dictionary only after reasoning from context.",
                "Which contextual clue helped you most?"
            ),
            GuidedPedagogicalSection(
                "Types of context clues",
                "Identify common clues used by academic writers.",
                listOf(
                    "Definition or restatement clues.",
                    "Example and illustration clues.",
                    "Contrast, cause-effect and logical relationship clues."
                ),
                "Label the clue type used for ten target words.",
                "Write one original sentence that gives a clear context clue.",
                "Can you identify the clue without seeing the answer first?"
            ),
            GuidedPedagogicalSection(
                "Academic vocabulary and word families",
                "Use related forms to expand lexical flexibility.",
                listOf(
                    "Recognise noun, verb, adjective and adverb relationships.",
                    "Use morphology to support meaning inference.",
                    "Check whether the word form fits the sentence grammatically."
                ),
                "Build word families for five academic terms.",
                "Rewrite sentences using an appropriate form of the same word family.",
                "Did the new form preserve the intended meaning?"
            ),
            GuidedPedagogicalSection(
                "From inference to verification",
                "Balance strategic inference with accurate confirmation.",
                listOf(
                    "Inference is a working hypothesis, not always a final answer.",
                    "Verify important terminology after contextual reasoning.",
                    "Record useful new words with meaning, example and word family."
                ),
                "Complete a vocabulary record for eight target words.",
                "Create a personal academic vocabulary bank for revision.",
                "Can you use each new word accurately in context?"
            )
        )

        6 -> "Unit 6 • Note-Taking" to listOf(
            GuidedPedagogicalSection(
                "Purposeful note-taking",
                "Record information for later retrieval rather than copying everything.",
                listOf(
                    "Select main ideas, key terms, evidence and relationships.",
                    "Use concise language and meaningful abbreviations.",
                    "Leave enough structure for later review."
                ),
                "Take notes from a short passage using only key concepts.",
                "Close the source and reconstruct the argument from your notes.",
                "Could you understand the original idea from your notes alone?"
            ),
            GuidedPedagogicalSection(
                "Cornell-style organisation",
                "Structure notes for study and retrieval.",
                listOf(
                    "Separate notes from cues and review questions.",
                    "Add a brief summary after the lesson or reading.",
                    "Use questions to make later self-testing easier."
                ),
                "Transform raw notes into a cue-and-summary format.",
                "Write three retrieval questions from your notes.",
                "Do the questions target the most important information?"
            ),
            GuidedPedagogicalSection(
                "Outlining relationships",
                "Represent hierarchy and connections among ideas.",
                listOf(
                    "Use headings and indentation to show levels.",
                    "Use arrows or labels for cause, contrast and sequence.",
                    "Keep one logical unit per note segment."
                ),
                "Turn a dense paragraph into a hierarchical outline.",
                "Build a one-page revision sheet from a course reading.",
                "Can you see the structure of the ideas at a glance?"
            ),
            GuidedPedagogicalSection(
                "Review and retrieval",
                "Convert notes into usable knowledge.",
                listOf(
                    "Review soon after learning and again later.",
                    "Cover your notes and recall the ideas.",
                    "Correct gaps instead of rereading passively."
                ),
                "Use your notes to answer five self-test questions without looking.",
                "Schedule a short retrieval review after the reading session.",
                "Which idea could you not retrieve independently?"
            )
        )

        7 -> "Unit 7 • Summarising and Paraphrasing" to listOf(
            GuidedPedagogicalSection(
                "What makes a summary?",
                "Reduce a text to its essential ideas while preserving the original meaning.",
                listOf(
                    "Select only the most important information.",
                    "Remove examples and repetition unless they are essential.",
                    "Keep the writer's central message and logical relationships."
                ),
                "Reduce a 180-word passage to approximately 60 words.",
                "Compare your summary with the original and justify what you removed.",
                "Does the summary preserve the central message?"
            ),
            GuidedPedagogicalSection(
                "Paraphrasing accurately",
                "Restate ideas in your own language without changing meaning.",
                listOf(
                    "Change sentence structure and wording meaningfully.",
                    "Preserve key concepts and relationships.",
                    "Do not replace a few words mechanically and call it paraphrasing."
                ),
                "Paraphrase three academic sentences in different structures.",
                "Produce a paraphrase and identify the changes you made.",
                "Is your version genuinely restructured and faithful?"
            ),
            GuidedPedagogicalSection(
                "Avoiding plagiarism",
                "Use source ideas responsibly in academic work.",
                listOf(
                    "Distinguish your wording from borrowed wording.",
                    "A paraphrase still requires appropriate attribution in academic writing.",
                    "Keep source notes separate from your own commentary."
                ),
                "Identify acceptable paraphrases and patchwriting examples.",
                "Create a short paraphrase with a source attribution placeholder.",
                "Can you explain why close copying is not an acceptable paraphrase?"
            ),
            GuidedPedagogicalSection(
                "Integrated synthesis",
                "Combine reading, notes, summary and paraphrase skills.",
                listOf(
                    "Read for the main idea.",
                    "Take selective notes.",
                    "Write a concise summary and one accurate paraphrase."
                ),
                "Complete the full process on a short academic passage.",
                "Use the result as preparation for a written assignment.",
                "Can you move from source text to independent reformulation confidently?"
            )
        )

        else -> "Unit 8 • Study Skills and Final Review" to listOf(
            GuidedPedagogicalSection(
                "Planning independent study",
                "Turn reading strategies into a sustainable university study routine.",
                listOf(
                    "Set a clear outcome for each study session.",
                    "Divide large readings into manageable tasks.",
                    "Combine reading, retrieval and review rather than relying on rereading."
                ),
                "Create a weekly reading plan with two short retrieval sessions.",
                "Apply the plan to one real course module.",
                "Is every study session linked to a specific learning outcome?"
            ),
            GuidedPedagogicalSection(
                "Reading for examinations",
                "Adapt reading strategies to revision demands.",
                listOf(
                    "Prioritise core concepts and relationships.",
                    "Use scanning to find evidence and skimming to recover structure.",
                    "Convert notes into questions and short explanations."
                ),
                "Turn one chapter of notes into ten exam-style questions.",
                "Complete a timed retrieval session without consulting the source.",
                "Which strategy improved your exam readiness most?"
            ),
            GuidedPedagogicalSection(
                "Metacognitive self-assessment",
                "Evaluate your own reading process and make informed adjustments.",
                listOf(
                    "Identify what you can do consistently and what remains difficult.",
                    "Use evidence from tasks and quiz performance.",
                    "Choose one concrete strategy for improvement."
                ),
                "Rate your confidence in each course skill and provide evidence.",
                "Write a personal improvement target for the next four weeks.",
                "Is your improvement target measurable and realistic?"
            ),
            GuidedPedagogicalSection(
                "Final integrated task",
                "Demonstrate independent control of Guided Reading strategies.",
                listOf(
                    "Preview and set a purpose.",
                    "Skim for organisation and scan for details.",
                    "Identify main ideas, infer vocabulary and take notes.",
                    "Summarise and paraphrase the essential content."
                ),
                "Complete an integrated reading task using one coherent strategy sequence.",
                "Prepare a one-page study guide from a new academic text.",
                "Can you explain and justify the strategy sequence you used?"
            )
        )
    }
}

@Composable
private fun GuidedUnitPedagogicalScreen(
    unitNumber: Int,
    onBack: () -> Unit
) {
    val (title, sections) = unitSections(unitNumber)
    var selectedSection by remember(unitNumber) { mutableIntStateOf(0) }
    val section = sections[selectedSection]

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Guided Reading 1",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            title,
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
                    containerColor = GRPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(GRBackground)
                .padding(padding)
        ) {
            GuidedSectionNavigation(
                selected = selectedSection,
                sections = sections.map { it.title },
                onSelected = { selectedSection = it }
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = GRPrimary
                        )
                    ) {
                        Column(Modifier.padding(20.dp)) {
                            Text(
                                "UNIT $unitNumber",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(5.dp))
                            Text(
                                title.substringAfter("• ").trim(),
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                lineHeight = 30.sp
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                sections.first().purpose,
                                color = Color.White.copy(alpha = 0.92f),
                                fontSize = 14.sp,
                                lineHeight = 21.sp
                            )
                        }
                    }
                }

                item {
                    GuidedAcademicCard(
                        section.title,
                        section.keyPoints
                    )
                }

                item {
                    GuidedNote(
                        "Guided practice",
                        section.practice
                    )
                }

                item {
                    GuidedNote(
                        "Apply independently",
                        section.application
                    )
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = GRLightGreen
                        )
                    ) {
                        Column(Modifier.padding(18.dp)) {
                            Text(
                                "Learning checkpoint",
                                color = GRGreen,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                section.check,
                                color = GRText,
                                fontSize = 14.sp,
                                lineHeight = 21.sp
                            )
                        }
                    }
                }

                item {
                    GuidedAssessmentCard(
                        unitNumber = unitNumber,
                        sectionTitle = section.title
                    )
                }
            }
        }
    }
}

@Composable
private fun GuidedAssessmentCard(
    unitNumber: Int,
    sectionTitle: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = GRPrimary,
                    modifier = Modifier.size(27.dp)
                )
                Spacer(Modifier.width(9.dp))
                Text(
                    "Assessment • Unit $unitNumber",
                    color = GRText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(10.dp))

            Text(
                "Complete a short written or oral response on \"$sectionTitle\". "
                        + "Your answer should demonstrate accurate strategy choice, "
                        + "clear reasoning and evidence from the text.",
                color = GRGray,
                fontSize = 14.sp,
                lineHeight = 21.sp
            )

            Spacer(Modifier.height(12.dp))

            Text(
                "Success criteria",
                color = GRPrimary,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(5.dp))

            listOf(
                "The strategy is appropriate to the reading purpose.",
                "The explanation is precise and supported by textual evidence.",
                "The student can apply the skill independently."
            ).forEachIndexed { index, criterion ->
                Text(
                    "• $criterion",
                    color = GRText,
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}


@Composable
private fun GuidedUnitOneScreen(onBack: () -> Unit) {
    var selectedSection by remember { mutableIntStateOf(0) }

    val sections = listOf(
        "Context",
        "Characters",
        "Setting",
        "Read",
        "Vocabulary",
        "Comprehension",
        "Analysis",
        "Discussion",
        "Reflection"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Guided Reading 1",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Unit 1 • Introduction to Academic Reading",
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
                    containerColor = GRPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(GRBackground)
                .padding(padding)
        ) {
            GuidedSectionNavigation(
                selected = selectedSection,
                sections = sections,
                onSelected = { selectedSection = it }
            )

            when (selectedSection) {
                0 -> ContextSection()
                1 -> CharactersSection()
                2 -> SettingSection()
                3 -> ReadSection()
                4 -> VocabularySection()
                5 -> ComprehensionSection()
                6 -> AnalysisSection()
                7 -> DiscussionSection()
                8 -> ReflectionSection()
            }
        }
    }
}

@Composable
private fun GuidedSectionNavigation(
    selected: Int,
    sections: List<String>,
    onSelected: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
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
                                if (selected == index) GRPrimary else Color(0xFFF1F5F9),
                                RoundedCornerShape(11.dp)
                            )
                            .clickable { onSelected(index) }
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                    ) {
                        Text(
                            title,
                            color = if (selected == index) Color.White else GRGray,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ContextSection() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                "Context",
                color = GRText,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Build a reading framework before approaching a text.",
                color = GRGray
            )
        }

        item {
            GuidedAcademicCard(
                "Why guided reading?",
                listOf(
                    "Guided reading gives the student a clear purpose before reading.",
                    "The reader identifies context, predicts content and decides what information is important.",
                    "The aim is not to translate every sentence but to construct meaning from the text."
                )
            )
        }

        item {
            GuidedAcademicCard(
                "Academic reading routine",
                listOf(
                    "Preview the title and organisation.",
                    "Predict the topic and purpose.",
                    "Read for the main idea.",
                    "Locate supporting details.",
                    "Check vocabulary from context.",
                    "Review and reflect."
                )
            )
        }

        item {
            GuidedNote(
                "Reader mindset",
                "Ask yourself: What is the text about? Why was it written? What does the writer want me to understand?"
            )
        }
    }
}

@Composable
private fun CharactersSection() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                "Characters",
                color = GRText,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "A reading lens for identifying participants and roles in a text.",
                color = GRGray
            )
        }

        item {
            GuidedAcademicCard(
                "Character awareness",
                listOf(
                    "Identify who is involved in the text or narrative.",
                    "Distinguish central participants from secondary participants.",
                    "Note what each participant does, says or represents.",
                    "Use evidence from the text rather than assumptions."
                )
            )
        }

        item {
            CharacterMiniCard("Central figure", "Who appears most important to the text or narrative?")
        }

        item {
            CharacterMiniCard("Secondary figures", "Which participants support, oppose or interact with the central figure?")
        }

        item {
            GuidedNote(
                "Evidence first",
                "When identifying a role, point to a word, sentence or event that supports your interpretation."
            )
        }
    }
}

@Composable
private fun CharacterMiniCard(title: String, question: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(17.dp)) {
            Text(title, color = GRPrimary, fontWeight = FontWeight.Bold, fontSize = 17.sp)
            Spacer(Modifier.height(5.dp))
            Text(question, color = GRGray, fontSize = 14.sp, lineHeight = 21.sp)
        }
    }
}

@Composable
private fun SettingSection() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                "Setting",
                color = GRText,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Understand where, when and under what conditions the text operates.",
                color = GRGray
            )
        }

        item {
            GuidedAcademicCard(
                "Setting questions",
                listOf(
                    "Where does the event or discussion occur?",
                    "When does it occur?",
                    "What social, academic or cultural situation surrounds it?",
                    "How does the setting influence the meaning?"
                )
            )
        }

        item {
            GuidedNote(
                "Reading clue",
                "The setting is not only a place. Time, social circumstances and institutional context can also shape interpretation."
            )
        }
    }
}

@Composable
private fun ReadSection() {
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
                "Read",
                color = GRText,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Read the model academic passage actively.",
                color = GRGray
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
                        "Reading as an Active Skill",
                        color = GRPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(11.dp))
                    Text(
                        GUIDED_READING_TEXT,
                        color = GRText,
                        fontSize = 16.sp,
                        lineHeight = 27.sp
                    )
                }
            }
        }

        item {
            GuidedAudioCard(
                title = "Listen to the passage",
                speaking = speaking,
                onPlay = {
                    if (EnglishStudiesSettings.isSoundEnabled(context)) {
                        tts?.language = EnglishStudiesSettings.englishLocale(context)
                        tts?.setSpeechRate(EnglishStudiesSettings.speechRate(context))
                        speaking = true
                        tts?.speak(
                            GUIDED_READING_TEXT,
                            TextToSpeech.QUEUE_FLUSH,
                            null,
                            "guided_reading_text"
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
            GuidedNote(
                "Active reading challenge",
                "Before moving to Comprehension, state the topic and the main idea in your own words."
            )
        }
    }
}

@Composable
private fun VocabularySection() {
    val terms = listOf(
        "purpose" to "the reason why a text is written",
        "predict" to "to anticipate likely content from clues",
        "main idea" to "the central point developed by a text",
        "supporting detail" to "information that develops or explains the main idea",
        "context" to "the surrounding situation or information",
        "infer" to "to reach a conclusion from evidence",
        "annotate" to "to add notes or marks while reading",
        "review" to "to examine information again to reinforce understanding"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(11.dp)
    ) {
        item {
            Text(
                "Vocabulary",
                color = GRText,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Key terms for guided academic reading.",
                color = GRGray
            )
        }

        itemsIndexed(terms) { index, term ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(GRLightBlue, RoundedCornerShape(9.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "${index + 1}",
                            color = GRPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.width(11.dp))
                    Column {
                        Text(
                            term.first,
                            color = GRPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Text(
                            term.second,
                            color = GRGray,
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ComprehensionSection() {
    var current by remember { mutableIntStateOf(0) }
    var selected by remember { mutableStateOf<Int?>(null) }
    var score by remember { mutableIntStateOf(0) }

    val question = GUIDED_QUIZ[current]

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                "Comprehension",
                color = GRText,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Read for evidence and select the best answer.",
                color = GRGray
            )
        }

        item {
            Text(
                "Question ${current + 1} / ${GUIDED_QUIZ.size}",
                color = GRPrimary,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            GuidedQuizCard(
                question = question,
                selected = selected,
                onSelected = {
                    if (selected == null) {
                        selected = it
                        if (it == question.correct) score++
                    }
                }
            )
        }

        if (selected != null) {
            item {
                Text(
                    if (selected == question.correct) "✅ Correct!" else "❌ Review the explanation.",
                    color = if (selected == question.correct) GRGreen else GRRed,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Text(
                    question.explanation,
                    color = GRGray,
                    fontSize = 14.sp,
                    lineHeight = 21.sp
                )
            }

            item {
                Button(
                    onClick = {
                        if (current < GUIDED_QUIZ.lastIndex) {
                            current++
                            selected = null
                        } else {
                            current = 0
                            selected = null
                            score = 0
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = GRPrimary)
                ) {
                    Text(
                        if (current < GUIDED_QUIZ.lastIndex) "Next Question"
                        else "Restart"
                    )
                }
            }
        }

        item {
            GuidedScoreCard(score, GUIDED_QUIZ.size)
        }
    }
}

@Composable
private fun GuidedQuizCard(
    question: GRQuizQuestion,
    selected: Int?,
    onSelected: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(19.dp)) {
            Text(
                question.question,
                color = GRText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 25.sp
            )
            Spacer(Modifier.height(14.dp))

            question.options.forEachIndexed { index, option ->
                val correct = selected != null && index == question.correct
                val wrong = selected == index && index != question.correct

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            when {
                                correct -> GRLightGreen
                                wrong -> GRLightRed
                                else -> GRBackground
                            },
                            RoundedCornerShape(12.dp)
                        )
                        .clickable(enabled = selected == null) {
                            onSelected(index)
                        }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "${('A'.code + index).toChar()}.",
                        color = GRPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(9.dp))
                    Text(
                        option,
                        color = GRText,
                        modifier = Modifier.weight(1f),
                        fontSize = 14.sp
                    )
                    if (correct) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = GRGreen
                        )
                    }
                }

                Spacer(Modifier.height(7.dp))
            }
        }
    }
}

@Composable
private fun AnalysisSection() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                "Literary / Text Analysis",
                color = GRText,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Move from understanding what the text says to examining how it works.",
                color = GRGray
            )
        }

        item {
            GuidedAcademicCard(
                "Analysis questions",
                listOf(
                    "What is the central idea?",
                    "How are ideas or events organised?",
                    "Which details support the central idea?",
                    "What relationships can you identify?",
                    "What can reasonably be inferred from the evidence?"
                )
            )
        }

        item {
            GuidedNote(
                "Evidence-based interpretation",
                "A strong analysis distinguishes what the text explicitly states from what the reader infers."
            )
        }
    }
}

@Composable
private fun DiscussionSection() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                "Discussion",
                color = GRText,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Turn reading into structured academic speaking.",
                color = GRGray
            )
        }

        item {
            DiscussionPrompt(
                "What makes a reader successful at university?"
            )
        }

        item {
            DiscussionPrompt(
                "Which reading strategy is most useful before an examination, and why?"
            )
        }

        item {
            DiscussionPrompt(
                "How can students avoid depending on translation for every unfamiliar word?"
            )
        }

        item {
            GuidedNote(
                "Speaking frame",
                "In my view, the main point is… The text suggests this because… A relevant example is…"
            )
        }
    }
}

@Composable
private fun DiscussionPrompt(question: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(18.dp)) {
            Text(
                "Discussion question",
                color = GRPrimary,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(7.dp))
            Text(
                question,
                color = GRText,
                fontSize = 16.sp,
                lineHeight = 23.sp
            )
        }
    }
}

@Composable
private fun ReflectionSection() {
    var recognized by remember { mutableStateOf("") }
    var feedback by remember { mutableStateOf("") }
    var listening by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                listening = true
                startEnglishRecognition(
                    context,
                    onResult = {
                        recognized = it
                        listening = false
                        feedback = evaluateReflection(it)
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
                "Reflection",
                color = GRText,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Consolidate what you learned from the reading process.",
                color = GRGray
            )
        }

        item {
            GuidedAcademicCard(
                "Self-reflection",
                listOf(
                    "What strategy did you use first?",
                    "What information was easiest to identify?",
                    "Which vocabulary item required contextual reasoning?",
                    "What would you change in your next reading session?"
                )
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = GRLightOrange)
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text(
                        "Say your reflection aloud",
                        color = GROrange,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "In 2–4 sentences, explain one reading strategy you used and why it was useful.",
                        color = GRText,
                        fontSize = 15.sp,
                        lineHeight = 22.sp
                    )
                    Spacer(Modifier.height(11.dp))
                    Button(
                        onClick = {
                            if (
                                context.checkSelfPermission(Manifest.permission.RECORD_AUDIO) ==
                                PackageManager.PERMISSION_GRANTED
                            ) {
                                listening = true
                                startEnglishRecognition(
                                    context,
                                    onResult = {
                                        recognized = it
                                        listening = false
                                        feedback = evaluateReflection(it)
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
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GRPrimary
                        )
                    ) {
                        Icon(
                            if (listening) Icons.Default.Stop else Icons.Default.KeyboardVoice,
                            contentDescription = null
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(if (listening) "Listening..." else "Record Reflection")
                    }
                }
            }
        }

        if (recognized.isNotBlank()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = GRLightGreen)
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Text(
                            "Your reflection",
                            color = GRGreen,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(7.dp))
                        Text(
                            recognized,
                            color = GRText,
                            fontSize = 15.sp,
                            lineHeight = 22.sp
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            feedback,
                            color = GRText,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GuidedAcademicCard(
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
                    tint = GRPrimary
                )
                Spacer(Modifier.width(9.dp))
                Text(
                    title,
                    color = GRText,
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
                        color = GRPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        point,
                        color = GRText,
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun GuidedNote(title: String, text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(containerColor = GRLightPurple)
    ) {
        Column(Modifier.padding(18.dp)) {
            Text(
                title,
                color = GRPurple,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text,
                color = GRText,
                fontSize = 14.sp,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
private fun GuidedAudioCard(
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
                tint = GRPrimary,
                modifier = Modifier.size(30.dp)
            )
            Spacer(Modifier.width(11.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = GRText, fontWeight = FontWeight.Bold)
                Text(
                    "Listen carefully and follow the text.",
                    color = GRGray,
                    fontSize = 12.sp
                )
            }
            IconButton(onClick = { if (speaking) onStop() else onPlay() }) {
                Icon(
                    if (speaking) Icons.Default.Stop else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = GRPrimary
                )
            }
        }
    }
}

@Composable
private fun GuidedScoreCard(score: Int, total: Int) {
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
                Text("Current score", color = GRGray, fontSize = 13.sp)
                Text(
                    "$score / $total",
                    color = GRText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 21.sp
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

private fun evaluateReflection(text: String): String {
    val words = text.trim()
        .split("\\s+".toRegex())
        .filter { it.isNotBlank() }

    return when {
        words.size >= 25 ->
            "Good reflection. You developed the idea in several sentences. Now check whether your explanation is precise and evidence-based."
        words.size >= 10 ->
            "Good start. Add one more reason or example to make the reflection more developed."
        words.isNotEmpty() ->
            "Try to explain the strategy in a complete sentence and give one reason why it was useful."
        else ->
            "No clear speech was detected."
    }
}

private const val GUIDED_READING_TEXT =
    "Academic reading is an active process that requires a clear purpose. " +
            "University students read a variety of texts in order to acquire knowledge, identify important ideas and evaluate information. " +
            "Before reading in detail, a student can preview the title and organisation of the text and make a prediction about its content. " +
            "During reading, the student can identify the main idea, distinguish supporting details and use context clues to understand unfamiliar vocabulary. " +
            "After reading, reviewing and summarising the essential ideas can strengthen comprehension and support independent learning."
