@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package ma.fldm.englishstudies

import android.speech.tts.TextToSpeech
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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

private val MAWPrimary = Color(0xFF2563EB)
private val MAWDark = Color(0xFF1D4ED8)
private val MAWLight = Color(0xFFEFF6FF)
private val MAWGreen = Color(0xFF16A34A)
private val MAWLightGreen = Color(0xFFDCFCE7)
private val MAWRed = Color(0xFFDC2626)
private val MAWLightRed = Color(0xFFFEE2E2)
private val MAWOrange = Color(0xFFF59E0B)
private val MAWLightOrange = Color(0xFFFEF3C7)
private val MAWPurple = Color(0xFF7C3AED)
private val MAWLightPurple = Color(0xFFF3E8FF)
private val MAWText = Color(0xFF172033)
private val MAWGray = Color(0xFF64748B)
private val MAWBackground = Color(0xFFF8FAFC)

data class MAWExercise(
    val question: String,
    val options: List<String>,
    val correct: Int,
    val explanation: String
)

data class AcademicLesson(
    val number: Int,
    val title: String,
    val subtitle: String,
    val objective: String,
    val concepts: List<Pair<String, String>>,
    val vocabulary: List<Pair<String, String>>,
    val task: String,
    val exercises: List<MAWExercise>
)

private val academicLessons = listOf(
    AcademicLesson(
        1,
        "Introduction to Academic Work",
        "Understanding university learning and academic expectations.",
        "Identify the characteristics of successful academic work.",
        listOf(
            "Academic work" to "Purposeful study, reading, writing, research and communication carried out according to academic conventions.",
            "Academic responsibility" to "Students organize their work, meet deadlines and take responsibility for the accuracy of their work.",
            "Academic conventions" to "Established practices concerning language, structure, evidence, citation and presentation."
        ),
        listOf(
            "academic" to "related to study and education",
            "convention" to "an accepted academic practice",
            "responsibility" to "a duty or obligation"
        ),
        "Write three academic habits that you want to develop during your first semester.",
        listOf(
            MAWExercise(
                "Which statement best describes academic work?",
                listOf("Unplanned memorization only", "Purposeful study based on academic practices", "Informal conversation", "Copying information without checking it"),
                1,
                "Academic work is purposeful and follows accepted academic practices."
            ),
            MAWExercise(
                "Which behavior shows academic responsibility?",
                listOf("Ignoring deadlines", "Checking the accuracy of your work", "Copying a classmate", "Avoiding feedback"),
                1,
                "Responsibility includes organizing work, meeting deadlines and checking accuracy."
            ),
            MAWExercise(
                "Academic conventions include:",
                listOf("Only handwriting style", "Language, structure, evidence and citation", "Only exam dates", "Only classroom attendance"),
                1,
                "Academic conventions concern language, structure, evidence, citation and presentation."
            ),
            MAWExercise(
                "A student who plans assignments before deadlines is showing:",
                listOf("Academic responsibility", "Plagiarism", "Informal register", "Passive reading"),
                0,
                "Planning and meeting deadlines are part of academic responsibility."
            ),
            MAWExercise(
                "Which activity is normally part of academic work?",
                listOf("Researching a question", "Guessing without reading", "Copying without attribution", "Ignoring sources"),
                0,
                "Research and careful reading are central academic activities."
            ),
            MAWExercise(
                "Academic communication should normally be:",
                listOf("Purposeful and evidence-aware", "Random", "Unsupported", "Unclear"),
                0,
                "Academic communication aims to communicate ideas clearly and responsibly."
            ),
            MAWExercise(
                "Why are conventions useful?",
                listOf("They create shared expectations for academic communication", "They prevent students from reading", "They replace evidence", "They eliminate critical thinking"),
                0,
                "Conventions help students and readers understand how academic work is organized and supported."
            ),
            MAWExercise(
                "Which is an academic habit?",
                listOf("Keeping organized notes", "Submitting without checking", "Avoiding all planning", "Copying a website"),
                0,
                "Organized note-taking supports effective academic study."
            ),
            MAWExercise(
                "Academic responsibility is mainly about:",
                listOf("Taking ownership of one's academic tasks", "Letting others complete your work", "Avoiding deadlines", "Using informal language"),
                0,
                "Responsibility means taking ownership of academic tasks and their quality."
            ),
            MAWExercise(
                "Before submitting work, a student should:",
                listOf("Review requirements and accuracy", "Delete evidence", "Ignore formatting", "Submit immediately"),
                0,
                "Checking requirements and accuracy is an essential academic practice."
            )
        )
    ),
    AcademicLesson(
        2,
        "Academic and Non-Academic Language",
        "Recognizing formal academic communication.",
        "Distinguish formal academic language from informal everyday language.",
        listOf(
            "Formal register" to "A style appropriate for academic and professional communication.",
            "Objectivity" to "Presenting information carefully without relying on unsupported personal claims.",
            "Precision" to "Choosing words that communicate an idea accurately."
        ),
        listOf(
            "register" to "level or style of language",
            "formal" to "appropriate for serious academic contexts",
            "precise" to "exact and clear"
        ),
        "Rewrite two informal sentences in a formal academic style.",
        listOf(
            MAWExercise(
                "Which expression is most appropriate in an academic essay?",
                listOf("Kids nowadays are super stressed.", "A lot of stuff happens.", "Many students experience significant academic pressure.", "Students are totally overwhelmed."),
                2,
                "Academic essays generally require a formal and precise register."
            ),
            MAWExercise(
                "Which word is more formal?",
                listOf("kids", "children", "stuff", "tons"),
                1,
                "Children is more neutral and formal than kids."
            ),
            MAWExercise(
                "Precision means:",
                listOf("Choosing exact and clear words", "Using the longest words possible", "Using slang", "Avoiding specific information"),
                0,
                "Precision is accuracy and clarity in word choice."
            ),
            MAWExercise(
                "Objectivity requires:",
                listOf("Unsupported personal claims", "Careful presentation of information", "Only emotions", "Informal jokes"),
                1,
                "Academic objectivity emphasizes careful, supportable presentation."
            ),
            MAWExercise(
                "Which sentence is more academic?",
                listOf("The results are kind of weird.", "The results indicate a significant difference.", "The results are super good.", "The results are pretty cool."),
                1,
                "The second sentence uses a more precise and formal academic register."
            ),
            MAWExercise(
                "A formal register is appropriate for:",
                listOf("A research report", "A casual chat message", "A joke with friends", "A slang conversation"),
                0,
                "Formal register is suited to academic and professional contexts."
            ),
            MAWExercise(
                "Which word is least academic in this context?",
                listOf("however", "significant", "stuff", "therefore"),
                2,
                "Stuff is vague and informal."
            ),
            MAWExercise(
                "Choose the most precise phrase:",
                listOf("a thing", "an important variable", "some stuff", "lots of things"),
                1,
                "An important variable communicates a more exact academic meaning."
            ),
            MAWExercise(
                "Academic language should avoid unnecessary:",
                listOf("Specific terms", "Evidence", "Slang and vague expressions", "Clear definitions"),
                2,
                "Slang and vague expressions can reduce academic precision."
            ),
            MAWExercise(
                "Which revision is most formal?",
                listOf("Students get lots of stress.", "Students experience considerable stress.", "Students get super stressed.", "Students have loads of stress."),
                1,
                "The second sentence is formal, concise and precise."
            )
        )
    ),
    AcademicLesson(
        3,
        "Time Management and Study Planning",
        "Planning reading, writing and revision.",
        "Create a realistic study plan and prioritize academic tasks.",
        listOf(
            "Priority" to "The relative importance or urgency of a task.",
            "Study schedule" to "A planned distribution of study activities over a period of time.",
            "Deadline" to "The date or time by which an academic task should be completed."
        ),
        listOf(
            "priority" to "something that receives attention first",
            "deadline" to "final date for completing a task",
            "schedule" to "planned timetable"
        ),
        "Prepare a weekly plan including reading, lectures, revision and assignment time.",
        listOf(
            MAWExercise(
                "What is the main purpose of a study schedule?",
                listOf("To eliminate all free time", "To organize academic tasks and allocate study time", "To avoid deadlines", "To study only before examinations"),
                1,
                "A study schedule distributes tasks across available time."
            ),
            MAWExercise(
                "A deadline is:",
                listOf("A final date for completing a task", "A free day", "A study break", "A list of vocabulary"),
                0,
                "A deadline is the final date or time for a task."
            ),
            MAWExercise(
                "Which task usually deserves higher priority?",
                listOf("A task due tomorrow", "A task with no due date", "An optional activity", "An unrelated task"),
                0,
                "Urgency and importance help determine priority."
            ),
            MAWExercise(
                "A realistic study plan should:",
                listOf("Allocate time for several academic activities", "Fill every minute", "Ignore personal constraints", "Include only revision"),
                0,
                "A realistic plan considers available time and different academic tasks."
            ),
            MAWExercise(
                "Which is a useful planning action?",
                listOf("Breaking a large task into smaller steps", "Waiting until the last night", "Ignoring the deadline", "Removing all breaks"),
                0,
                "Breaking tasks into smaller steps can make progress manageable."
            ),
            MAWExercise(
                "Why include revision in a weekly plan?",
                listOf("To reinforce learning over time", "To replace lectures", "To avoid reading", "To eliminate practice"),
                0,
                "Spaced revision supports continued learning."
            ),
            MAWExercise(
                "Which item is a study schedule entry?",
                listOf("Tuesday 17:00–18:00: reading", "Maybe someday", "No plan", "Whenever"),
                0,
                "A schedule entry specifies a task and a time."
            ),
            MAWExercise(
                "When several tasks are due soon, the student should:",
                listOf("Prioritize by urgency and importance", "Choose randomly", "Ignore all of them", "Only do the easiest"),
                0,
                "Prioritization helps manage competing demands."
            ),
            MAWExercise(
                "A good plan should include:",
                listOf("Study and revision time", "Only exam dates", "No deadlines", "Only free time"),
                0,
                "Study planning should allocate time to key academic activities."
            ),
            MAWExercise(
                "What can reduce the risk of missing a deadline?",
                listOf("Starting early and tracking due dates", "Ignoring calendars", "Waiting for reminders from others", "Avoiding weekly plans"),
                0,
                "Early planning and deadline tracking support timely completion."
            )
        )
    ),
    AcademicLesson(
        4,
        "Note-Taking Techniques",
        "Turning lectures and readings into useful study notes.",
        "Take concise, organized notes without copying everything.",
        listOf(
            "Selective note-taking" to "Recording key ideas, concepts, examples and relationships rather than every word.",
            "Keywords" to "Important terms that help recall a larger idea.",
            "Cornell method" to "A structured system using notes, cues and a summary for review."
        ),
        listOf(
            "keyword" to "important word representing an idea",
            "summary" to "brief statement of main points",
            "cue" to "prompt used to recall information"
        ),
        "Take notes from a short lecture or video and finish with a five-sentence summary.",
        listOf(
            MAWExercise(
                "Effective note-taking mainly involves:",
                listOf("Copying every sentence", "Recording key ideas in an organized form", "Writing without headings", "Avoiding abbreviations completely"),
                1,
                "Effective notes select and organize key information instead of copying everything."
            ),
            MAWExercise(
                "A keyword is:",
                listOf("A term that helps recall an idea", "A full paragraph", "A citation style", "A deadline"),
                0,
                "Keywords condense and trigger larger ideas."
            ),
            MAWExercise(
                "The Cornell method uses:",
                listOf("Notes, cues and a summary", "Only drawings", "Only quotations", "Only a final exam"),
                0,
                "The Cornell system separates notes, cues and a summary."
            ),
            MAWExercise(
                "Why use headings in notes?",
                listOf("To organize ideas", "To make notes longer", "To hide information", "To avoid reviewing"),
                0,
                "Headings show structure and make review easier."
            ),
            MAWExercise(
                "Selective note-taking means:",
                listOf("Recording every spoken word", "Recording important ideas and relationships", "Writing nothing", "Copying slides word for word"),
                1,
                "Selective notes focus on important content."
            ),
            MAWExercise(
                "A summary in notes should:",
                listOf("State the main points briefly", "Add unrelated details", "Replace the lecture", "Contain every sentence"),
                0,
                "A summary condenses the main ideas."
            ),
            MAWExercise(
                "A cue can help a student:",
                listOf("Recall information", "Delete notes", "Skip review", "Avoid understanding"),
                0,
                "Cues act as prompts during later review."
            ),
            MAWExercise(
                "Which note is most useful?",
                listOf("Important concept + example", "Every filler word", "Random phrases", "No organization"),
                0,
                "Useful notes capture concepts and meaningful examples."
            ),
            MAWExercise(
                "A student should review notes:",
                listOf("Regularly", "Only after graduation", "Never", "Only when notes are lost"),
                0,
                "Regular review strengthens learning."
            ),
            MAWExercise(
                "One advantage of concise notes is:",
                listOf("They are easier to review", "They always contain every detail", "They eliminate the need for reading", "They avoid structure"),
                0,
                "Concise notes make key information easier to revisit."
            )
        )
    ),
    AcademicLesson(
        5,
        "Academic Reading Strategies",
        "Reading actively rather than passively.",
        "Use skimming, scanning and close reading appropriately.",
        listOf(
            "Skimming" to "Reading quickly to identify the general topic and organization.",
            "Scanning" to "Searching a text for specific information.",
            "Close reading" to "Carefully analyzing language, evidence, structure and meaning."
        ),
        listOf(
            "skim" to "read quickly for the general idea",
            "scan" to "search for specific information",
            "infer" to "reach a conclusion from evidence"
        ),
        "Choose an English article and write its topic, three key ideas and one conclusion you can infer.",
        listOf(
            MAWExercise(
                "Which strategy is most suitable when looking for a specific date in a text?",
                listOf("Scanning", "Freewriting", "Memorization", "Brainstorming"),
                0,
                "Scanning is designed to locate specific information quickly."
            ),
            MAWExercise(
                "Skimming is useful for:",
                listOf("Identifying the general topic and organization", "Finding one exact number", "Checking punctuation", "Memorizing every sentence"),
                0,
                "Skimming provides a quick overview."
            ),
            MAWExercise(
                "Close reading involves:",
                listOf("Careful analysis of language and evidence", "Reading only the title", "Searching for one date", "Skipping difficult parts"),
                0,
                "Close reading examines meaning, structure and supporting evidence."
            ),
            MAWExercise(
                "To find a specific name quickly, use:",
                listOf("Scanning", "Skimming only", "Freewriting", "Summarizing"),
                0,
                "Scanning helps locate specific names, dates or terms."
            ),
            MAWExercise(
                "An inference is:",
                listOf("A conclusion drawn from evidence", "A copied sentence", "A title", "A random guess"),
                0,
                "An inference is supported by information in the text."
            ),
            MAWExercise(
                "Active reading includes:",
                listOf("Questioning and annotating", "Reading without attention", "Ignoring headings", "Memorizing punctuation"),
                0,
                "Active readers interact with a text and monitor meaning."
            ),
            MAWExercise(
                "Which technique is useful before detailed reading?",
                listOf("Skimming", "Proofreading", "Citation", "Quoting"),
                0,
                "Skimming can provide a framework before close reading."
            ),
            MAWExercise(
                "Which activity belongs to close reading?",
                listOf("Analyzing word choice", "Finding a phone number", "Counting pages only", "Checking the cover"),
                0,
                "Close reading examines language and meaning in detail."
            ),
            MAWExercise(
                "Why identify a text's main idea?",
                listOf("To understand its central point", "To avoid reading", "To replace evidence", "To remove context"),
                0,
                "The main idea helps establish the text's central message."
            ),
            MAWExercise(
                "A reader who writes questions in the margin is:",
                listOf("Reading actively", "Scanning only", "Avoiding comprehension", "Copying passively"),
                0,
                "Marginal questions are a practical active-reading strategy."
            )
        )
    ),
    AcademicLesson(
        6,
        "Finding and Evaluating Sources",
        "Selecting reliable information for academic work.",
        "Evaluate sources according to authority, relevance, currency and evidence.",
        listOf(
            "Authority" to "The expertise and credibility of the author or institution.",
            "Relevance" to "The degree to which a source directly supports the research question.",
            "Currency" to "How recent and appropriate the information is for the topic."
        ),
        listOf(
            "source" to "place or document from which information is obtained",
            "credible" to "worthy of trust",
            "evidence" to "information used to support a claim"
        ),
        "Compare two online sources about the same topic and explain which one appears more academically useful and why.",
        listOf(
            MAWExercise(
                "Which factor is important when evaluating an academic source?",
                listOf("Only its length", "Its color and design", "Authority, relevance and evidence", "Whether it agrees with you"),
                2,
                "Academic evaluation considers credibility, relevance and supporting evidence."
            ),
            MAWExercise(
                "Authority refers to:",
                listOf("The expertise or credibility of the author or institution", "The number of pages", "The font size", "The website color"),
                0,
                "Authority concerns who produced the information and their expertise."
            ),
            MAWExercise(
                "A relevant source:",
                listOf("Directly supports the research question", "Is always very old", "Must agree with you", "Has many pictures"),
                0,
                "Relevance is about connection to the research question."
            ),
            MAWExercise(
                "Currency concerns:",
                listOf("How recent and appropriate information is", "Only the publication language", "Only page length", "The author's age"),
                0,
                "Currency considers recency in relation to the topic."
            ),
            MAWExercise(
                "Which is stronger evidence?",
                listOf("A supported claim from a credible source", "An anonymous rumor", "A personal guess", "A slogan"),
                0,
                "Evidence should come from credible, relevant information."
            ),
            MAWExercise(
                "Why compare sources?",
                listOf("To assess quality and usefulness", "To avoid checking reliability", "To choose the longest source", "To copy both"),
                0,
                "Comparison helps identify stronger and more relevant sources."
            ),
            MAWExercise(
                "A source can be authoritative but irrelevant when:",
                listOf("The author is expert but the source does not address your question", "It has a title", "It is recent", "It contains evidence"),
                0,
                "Authority alone does not guarantee relevance."
            ),
            MAWExercise(
                "A credible source is generally:",
                listOf("Trustworthy and supported", "Always informal", "Never dated", "Based on rumor"),
                0,
                "Credibility reflects trustworthiness and evidence."
            ),
            MAWExercise(
                "Which question helps evaluate a source?",
                listOf("Who wrote it and what evidence is provided?", "Is the page colorful?", "Is the title short?", "Does it entertain me?"),
                0,
                "Authorship and evidence are central evaluation questions."
            ),
            MAWExercise(
                "A student should use a source because:",
                listOf("It is appropriate and supports the research purpose", "It is the first result only", "It agrees with a personal belief", "It is very long"),
                0,
                "Academic source selection should be based on relevance and quality."
            )
        )
    ),
    AcademicLesson(
        7,
        "Library and Online Academic Resources",
        "Using catalogs, databases and scholarly resources.",
        "Develop efficient strategies for locating academic material.",
        listOf(
            "Library catalogue" to "A searchable system used to locate books and other library holdings.",
            "Academic database" to "A structured collection of scholarly publications and research information.",
            "Search terms" to "Words or phrases selected to retrieve relevant information."
        ),
        listOf(
            "catalogue" to "organized list of available resources",
            "database" to "organized collection of information",
            "scholarly" to "produced for an academic audience"
        ),
        "Create five search terms for a research topic related to English Studies.",
        listOf(
            MAWExercise(
                "Why should a student use precise search terms?",
                listOf("To make searching slower", "To retrieve more relevant results", "To avoid reading sources", "To replace critical evaluation"),
                1,
                "Precise search terms narrow results and improve relevance."
            ),
            MAWExercise(
                "A library catalogue helps you:",
                listOf("Locate library resources", "Write your conclusion automatically", "Evaluate every source", "Replace reading"),
                0,
                "A catalogue helps identify and locate holdings."
            ),
            MAWExercise(
                "An academic database contains:",
                listOf("Scholarly publications and research information", "Only social messages", "Only advertisements", "Only novels"),
                0,
                "Academic databases organize scholarly information."
            ),
            MAWExercise(
                "Which is the best search term for a focused topic?",
                listOf("English", "academic reading strategies university students", "stuff", "things"),
                1,
                "Specific multi-word terms often retrieve more relevant material."
            ),
            MAWExercise(
                "Scholarly means:",
                listOf("Produced for an academic audience", "Written as a personal text message", "Always fictional", "Unrelated to research"),
                0,
                "Scholarly resources are designed for academic use."
            ),
            MAWExercise(
                "Search terms should be adjusted when:",
                listOf("Results are too broad or irrelevant", "The first page is colorful", "You have a notebook", "The title is short"),
                0,
                "Refining terms improves search results."
            ),
            MAWExercise(
                "Using a database can help students:",
                listOf("Find research literature efficiently", "Avoid source evaluation", "Copy abstracts", "Skip all reading"),
                0,
                "Databases support efficient literature searching."
            ),
            MAWExercise(
                "Which item is most likely in a library catalogue?",
                listOf("A book title and author", "A private chat", "A classroom joke", "A social media password"),
                0,
                "Library catalogues record bibliographic information for holdings."
            ),
            MAWExercise(
                "A useful search strategy is to:",
                listOf("Combine key concepts and synonyms", "Use only one vague word", "Avoid topic terms", "Search randomly"),
                0,
                "Combining concepts and synonyms can improve retrieval."
            ),
            MAWExercise(
                "After finding a source, the student should:",
                listOf("Evaluate its relevance and credibility", "Copy it immediately", "Ignore authorship", "Delete the record"),
                0,
                "Finding a source is only the first stage; it must be evaluated."
            )
        )
    ),
    AcademicLesson(
        8,
        "Paraphrasing",
        "Expressing a source idea accurately in your own words.",
        "Paraphrase without changing the original meaning or copying its wording.",
        listOf(
            "Paraphrase" to "A restatement of a source idea using different wording and sentence structure while preserving the meaning.",
            "Source attribution" to "Acknowledging where an idea or information originated.",
            "Meaning" to "The idea communicated by the original source."
        ),
        listOf(
            "paraphrase" to "restate an idea in different words",
            "attribute" to "identify the source of an idea",
            "original" to "coming from the initial source"
        ),
        "Paraphrase a short academic sentence and identify the source in your notes.",
        listOf(
            MAWExercise(
                "A good paraphrase should:",
                listOf("Change a few words only", "Keep the meaning but use genuinely different wording and structure", "Remove the source", "Copy the sentence and change the punctuation"),
                1,
                "A paraphrase changes wording and structure while preserving meaning."
            ),
            MAWExercise(
                "Paraphrasing requires:",
                listOf("Understanding the original idea", "Copying sentence order", "Removing attribution", "Changing meaning"),
                0,
                "Understanding is necessary before restating an idea accurately."
            ),
            MAWExercise(
                "Source attribution tells the reader:",
                listOf("Where the idea came from", "How long the text is", "Who is in the classroom", "Which font was used"),
                0,
                "Attribution identifies the source of borrowed information or ideas."
            ),
            MAWExercise(
                "Which is NOT a good paraphrase technique?",
                listOf("Changing wording and structure", "Understanding the source", "Replacing a few words only", "Checking meaning"),
                2,
                "Changing a few words while retaining the original structure can be too close to the source."
            ),
            MAWExercise(
                "A paraphrase should preserve:",
                listOf("The original meaning", "Every original word", "The same punctuation", "The same sentence length"),
                0,
                "Meaning must remain accurate even when wording changes."
            ),
            MAWExercise(
                "Why cite a paraphrase?",
                listOf("The underlying idea comes from a source", "Because all sentences need quotations", "To make the text longer", "To avoid reading"),
                0,
                "Paraphrased ideas still require acknowledgement of their source."
            ),
            MAWExercise(
                "Before paraphrasing, it is useful to:",
                listOf("Read and understand the passage carefully", "Copy it exactly", "Delete the source", "Memorize punctuation"),
                0,
                "Comprehension comes before accurate restatement."
            ),
            MAWExercise(
                "Which sentence best shows structural change?",
                listOf("Original: Research improves learning. Paraphrase: Learning can be strengthened through research.", "Original: Research improves learning. Paraphrase: Research improves learning.", "Original: Research improves learning. Paraphrase: Research improves learning!", "Original: Research improves learning. Paraphrase: Research improves learning."),
                0,
                "The first example changes wording and structure while preserving the idea."
            ),
            MAWExercise(
                "A paraphrase that changes the author's meaning is:",
                listOf("Inaccurate", "More academic", "Perfect", "A citation"),
                0,
                "Changing meaning makes the paraphrase inaccurate."
            ),
            MAWExercise(
                "A careful paraphrase combines:",
                listOf("Accuracy, new wording and source attribution", "Copying and omission", "Slang and guessing", "No source and no meaning"),
                0,
                "These three elements support responsible paraphrasing."
            )
        )
    ),
    AcademicLesson(
        9,
        "Summarizing",
        "Reducing a text to its essential ideas.",
        "Produce concise summaries that represent the main ideas accurately.",
        listOf(
            "Main idea" to "The central point developed by a text or section.",
            "Summary" to "A concise account of the main ideas without unnecessary detail.",
            "Conciseness" to "Communicating essential information using relatively few words."
        ),
        listOf(
            "concise" to "brief but complete",
            "essential" to "necessary or most important",
            "overview" to "general account of a subject"
        ),
        "Read a short academic paragraph and write a 40–50 word summary.",
        listOf(
            MAWExercise(
                "A summary should mainly contain:",
                listOf("Every example in the source", "The main ideas and essential information", "Your unrelated opinion", "Only the title"),
                1,
                "A summary selects the central ideas and essential information."
            ),
            MAWExercise(
                "The main idea is:",
                listOf("The central point", "A minor example", "A citation number", "A title only"),
                0,
                "The main idea expresses the text's central point."
            ),
            MAWExercise(
                "Conciseness means:",
                listOf("Brief but complete communication", "Very long writing", "Removing all meaning", "Using many examples"),
                0,
                "A concise summary is brief while retaining essential meaning."
            ),
            MAWExercise(
                "A summary usually excludes:",
                listOf("Unnecessary minor details", "Main ideas", "Essential information", "The central point"),
                0,
                "Summaries omit details that are not necessary for understanding the core message."
            ),
            MAWExercise(
                "An effective summary should be:",
                listOf("Accurate and concise", "Longer than the original", "Mostly personal opinion", "A collection of quotations"),
                0,
                "Accuracy and conciseness are fundamental to summarizing."
            ),
            MAWExercise(
                "Why identify the main idea first?",
                listOf("To decide what belongs in the summary", "To avoid reading", "To add opinions", "To copy the title"),
                0,
                "Finding the main idea guides selection of essential content."
            ),
            MAWExercise(
                "An overview gives:",
                listOf("A general account of a subject", "Every minor detail", "Only one example", "A list of errors"),
                0,
                "An overview presents a general picture rather than exhaustive detail."
            ),
            MAWExercise(
                "Which sentence is most suitable for a summary?",
                listOf("The article examines how reading strategies support comprehension.", "The article has three commas in paragraph two.", "I personally loved the article.", "The author used a blue cover."),
                0,
                "The first sentence states a central idea suitable for a summary."
            ),
            MAWExercise(
                "A summary should normally be:",
                listOf("Shorter than the source", "The same length as the source", "Longer than the source", "Unrelated to the source"),
                0,
                "A summary reduces the source to its essential information."
            ),
            MAWExercise(
                "Good summarizing requires:",
                listOf("Selecting and combining central ideas", "Copying every sentence", "Adding unrelated evidence", "Changing the topic"),
                0,
                "A summary compresses central information while maintaining coherence."
            )
        )
    ),
    AcademicLesson(
        10,
        "Quoting and Referencing",
        "Using sources transparently and responsibly.",
        "Understand when and how source information should be acknowledged.",
        listOf(
            "Quotation" to "The exact words taken from a source and identified as such.",
            "Citation" to "A reference in academic writing that identifies the source of information or ideas.",
            "Reference list" to "A list of sources cited in an academic assignment."
        ),
        listOf(
            "quote" to "use the exact words of a source",
            "citation" to "formal acknowledgement of a source",
            "reference" to "bibliographic information identifying a source"
        ),
        "Find one academic source and record its author, title, year and publication information.",
        listOf(
            MAWExercise(
                "Why are citations important?",
                listOf("They make every paragraph longer", "They identify sources and support academic transparency", "They replace your own analysis", "They remove the need to read sources"),
                1,
                "Citations identify sources and make the use of information transparent."
            ),
            MAWExercise(
                "A quotation contains:",
                listOf("The exact words from a source", "Your paraphrase only", "Your opinion", "A summary without attribution"),
                0,
                "A quotation reproduces exact source wording and must be identified."
            ),
            MAWExercise(
                "A reference list contains:",
                listOf("Sources cited in the assignment", "Every website on the internet", "Only lecture notes", "Only quotations"),
                0,
                "The reference list records sources cited in the work."
            ),
            MAWExercise(
                "A citation helps a reader:",
                listOf("Identify the source of an idea or information", "Avoid all reading", "Replace analysis", "Ignore evidence"),
                0,
                "Citations provide source information to the reader."
            ),
            MAWExercise(
                "When using exact words, a student should:",
                listOf("Mark them as a quotation and give a citation", "Pretend they are original", "Remove the author", "Change only punctuation"),
                0,
                "Exact source wording requires clear quotation and attribution."
            ),
            MAWExercise(
                "Bibliographic information may include:",
                listOf("Author, title and year", "Only page color", "Only word count", "Only the student's name"),
                0,
                "Bibliographic details identify the source."
            ),
            MAWExercise(
                "Referencing supports:",
                listOf("Academic transparency and traceability", "Plagiarism", "Source hiding", "Informal writing"),
                0,
                "Referencing lets readers trace borrowed information."
            ),
            MAWExercise(
                "Which action is responsible source use?",
                listOf("Recording citation details while researching", "Deleting source information", "Copying without labels", "Inventing authors"),
                0,
                "Keeping source details supports accurate referencing."
            ),
            MAWExercise(
                "A citation is different from a quotation because:",
                listOf("A citation identifies the source; a quotation reproduces exact words", "They are always identical", "A citation is informal slang", "A quotation never uses a source"),
                0,
                "Citation and quotation are related but serve different functions."
            ),
            MAWExercise(
                "Before submitting an assignment, students should check:",
                listOf("That citations and the reference list are consistent", "That all sources are hidden", "That every sentence is quoted", "That references are deleted"),
                0,
                "Consistency between citations and references supports transparent academic work."
            )
        )
    ),
    AcademicLesson(
        11,
        "Avoiding Plagiarism",
        "Academic integrity and responsible use of sources.",
        "Recognize plagiarism and apply responsible academic practices.",
        listOf(
            "Plagiarism" to "Presenting another person's words, ideas or work as one's own without appropriate acknowledgement.",
            "Academic integrity" to "Commitment to honesty, responsibility and ethical academic practice.",
            "Acknowledgement" to "Clear recognition of the source of borrowed information or ideas."
        ),
        listOf(
            "integrity" to "honesty and ethical conduct",
            "plagiarism" to "using another's work without proper acknowledgement",
            "acknowledge" to "recognize a source or contribution"
        ),
        "Write a short paragraph explaining two ways a student can avoid plagiarism.",
        listOf(
            MAWExercise(
                "Which practice helps prevent plagiarism?",
                listOf("Removing source information", "Copying without quotation marks", "Taking careful source notes and citing borrowed ideas", "Changing only two words"),
                2,
                "Careful notes, paraphrasing and citation help prevent plagiarism."
            ),
            MAWExercise(
                "Plagiarism involves:",
                listOf("Presenting another's work as your own without acknowledgement", "Reading a source", "Writing an original idea", "Using a citation"),
                0,
                "The problem is unacknowledged use of another person's work."
            ),
            MAWExercise(
                "Academic integrity emphasizes:",
                listOf("Honesty and responsible practice", "Speed only", "Avoiding all sources", "Copying accurately"),
                0,
                "Integrity is grounded in honest and ethical academic behavior."
            ),
            MAWExercise(
                "Acknowledgement means:",
                listOf("Recognizing the source of borrowed material", "Deleting authors", "Hiding sources", "Changing titles"),
                0,
                "Acknowledgement gives credit to the source."
            ),
            MAWExercise(
                "Which is safest when copying exact words?",
                listOf("Use quotation marks and a citation", "Remove the author", "Change one adjective", "Present it as your own"),
                0,
                "Exact wording should be clearly identified and attributed."
            ),
            MAWExercise(
                "Good research notes should include:",
                listOf("Source details and quotations/paraphrases clearly marked", "Only copied paragraphs", "No authors", "Random web pages"),
                0,
                "Clear source notes reduce the risk of accidental plagiarism."
            ),
            MAWExercise(
                "Changing a few words in a copied sentence:",
                listOf("May still be too close to the source", "Always becomes original", "Needs no citation", "Is automatically a summary"),
                0,
                "Minor word changes do not necessarily produce an independent paraphrase."
            ),
            MAWExercise(
                "Which behavior supports integrity?",
                listOf("Admitting uncertainty and checking sources", "Inventing evidence", "Hiding references", "Submitting copied work"),
                0,
                "Integrity requires honest and responsible academic practice."
            ),
            MAWExercise(
                "A student can avoid accidental plagiarism by:",
                listOf("Recording where each idea came from", "Ignoring source details", "Copying without labels", "Waiting until the end to remember sources"),
                0,
                "Source tracking during research reduces mistakes."
            ),
            MAWExercise(
                "Plagiarism can be reduced by:",
                listOf("Understanding paraphrasing, quotation and citation", "Avoiding all reading", "Deleting references", "Using only personal opinions"),
                0,
                "These skills support responsible source use."
            )
        )
    ),
    AcademicLesson(
        12,
        "Academic Vocabulary",
        "Building a precise vocabulary for university study.",
        "Recognize common academic verbs and use them appropriately.",
        listOf(
            "Analyze" to "Examine something carefully by considering its components and relationships.",
            "Discuss" to "Examine a subject by presenting relevant ideas, evidence and interpretation.",
            "Evaluate" to "Make a reasoned judgment based on appropriate criteria and evidence."
        ),
        listOf(
            "analyze" to "examine in detail",
            "compare" to "identify similarities and differences",
            "evaluate" to "judge using evidence and criteria"
        ),
        "Write one academic sentence using each of these verbs: analyze, compare, evaluate.",
        listOf(
            MAWExercise(
                "Which verb asks a student to examine something in detail?",
                listOf("Analyze", "Copy", "Guess", "Ignore"),
                0,
                "Analyze asks the writer to examine components and relationships in detail."
            ),
            MAWExercise(
                "Which verb focuses on similarities and differences?",
                listOf("Compare", "Ignore", "Quote", "Describe"),
                0,
                "Compare requires identifying similarities and differences."
            ),
            MAWExercise(
                "Evaluate means:",
                listOf("Make a reasoned judgment using criteria and evidence", "Copy a source", "Describe without thought", "Guess"),
                0,
                "Evaluation involves criteria, evidence and a reasoned judgment."
            ),
            MAWExercise(
                "Which task best matches analyze?",
                listOf("Examine the causes of a problem", "List the title only", "Copy a paragraph", "Give an unsupported opinion"),
                0,
                "Analysis examines components and relationships."
            ),
            MAWExercise(
                "Which task best matches compare?",
                listOf("Identify similarities and differences between two texts", "Memorize one definition", "Copy two pages", "Ignore one text"),
                0,
                "Comparison focuses on similarities and differences."
            ),
            MAWExercise(
                "Which task best matches evaluate?",
                listOf("Assess a source using clear criteria", "Rewrite a title", "Copy data", "List random facts"),
                0,
                "Evaluation makes a reasoned judgment based on criteria and evidence."
            ),
            MAWExercise(
                "Academic verbs are useful because they:",
                listOf("Signal the type of thinking required", "Replace all evidence", "Make writing informal", "Remove structure"),
                0,
                "Academic verbs such as analyze and evaluate indicate expected intellectual work."
            ),
            MAWExercise(
                "Which sentence uses analyze correctly?",
                listOf("The paper analyzes the causes of migration.", "The paper analyzes very good.", "The paper analyze yesterday.", "The paper is analyze"),
                0,
                "Analyzes is correctly used to indicate detailed examination."
            ),
            MAWExercise(
                "Which sentence uses evaluate correctly?",
                listOf("The study evaluates the reliability of the data.", "The study evaluate yesterday.", "The study evaluating is.", "The study evaluates a color only."),
                0,
                "Evaluates can describe a reasoned assessment of reliability."
            ),
            MAWExercise(
                "Using precise academic verbs helps students:",
                listOf("Match their writing to academic task requirements", "Avoid thinking", "Remove evidence", "Write only summaries"),
                0,
                "Task verbs communicate the level and type of academic work expected."
            )
        )
    ),
    AcademicLesson(
        13,
        "Organizing an Academic Assignment",
        "From introduction to conclusion.",
        "Understand how an academic assignment can be logically organized.",
        listOf(
            "Introduction" to "Establishes the topic, context, purpose and direction of the assignment.",
            "Body" to "Develops ideas through organized paragraphs, evidence and analysis.",
            "Conclusion" to "Synthesizes the main points and closes the discussion without simply repeating the introduction."
        ),
        listOf(
            "introduction" to "opening section establishing the topic",
            "body" to "main section where ideas are developed",
            "conclusion" to "final section synthesizing the discussion"
        ),
        "Create a simple outline for a 500-word assignment about the importance of reading.",
        listOf(
            MAWExercise(
                "Where are the main arguments normally developed?",
                listOf("Title only", "Body paragraphs", "Bibliography only", "Cover page"),
                1,
                "The body is where arguments, evidence and analysis are developed."
            ),
            MAWExercise(
                "The introduction should:",
                listOf("Establish topic, context and direction", "Present every detail", "Repeat the conclusion", "Contain only references"),
                0,
                "An introduction frames the assignment and signals its direction."
            ),
            MAWExercise(
                "The body of an assignment should:",
                listOf("Develop organized ideas with evidence and analysis", "Only list titles", "Contain no paragraphs", "Repeat the introduction"),
                0,
                "The body is the main development section."
            ),
            MAWExercise(
                "The conclusion should:",
                listOf("Synthesize the main points", "Introduce many unrelated new arguments", "Copy the title", "List sources only"),
                0,
                "A conclusion brings the discussion together."
            ),
            MAWExercise(
                "Which order is most typical?",
                listOf("Introduction → Body → Conclusion", "Conclusion → Title → Body", "Body → References → Introduction", "Title → Conclusion → Introduction"),
                0,
                "This sequence gives a clear academic progression."
            ),
            MAWExercise(
                "A topic sentence usually helps:",
                listOf("Introduce the focus of a body paragraph", "Replace the reference list", "End the whole assignment", "Provide every piece of evidence"),
                0,
                "A topic sentence signals the paragraph's main point."
            ),
            MAWExercise(
                "Logical organization helps a reader:",
                listOf("Follow the development of ideas", "Avoid understanding", "Ignore evidence", "Skip the topic"),
                0,
                "Clear structure supports comprehension."
            ),
            MAWExercise(
                "A strong outline should include:",
                listOf("Main sections and key points", "Only a title", "Random sentences", "No purpose"),
                0,
                "An outline maps the structure before drafting."
            ),
            MAWExercise(
                "Which is appropriate for a conclusion?",
                listOf("Synthesize what the discussion has established", "Add an unrelated topic", "Introduce a completely new research question", "Copy every body sentence"),
                0,
                "Conclusions synthesize rather than open unrelated discussions."
            ),
            MAWExercise(
                "Academic organization is useful because:",
                listOf("It creates coherence and direction", "It removes the need for evidence", "It makes sources unnecessary", "It prevents revision"),
                0,
                "Organization helps ideas connect logically for the reader."
            )
        )
    ),
    AcademicLesson(
        14,
        "Preparing an Academic Presentation",
        "Planning, presenting and supporting ideas orally.",
        "Organize a clear academic presentation and communicate confidently.",
        listOf(
            "Audience" to "The people for whom the presentation is prepared.",
            "Signposting" to "Language that guides listeners through the organization of a presentation.",
            "Visual aid" to "A chart, image, slide or other visual element used to support communication."
        ),
        listOf(
            "audience" to "people listening to a presentation",
            "signpost" to "language showing the structure of a talk",
            "visual aid" to "visual support for an oral presentation"
        ),
        "Prepare a three-minute presentation outline with an introduction, two main points and a conclusion.",
        listOf(
            MAWExercise(
                "What is the purpose of signposting language?",
                listOf("To confuse the audience", "To guide the audience through the presentation", "To replace evidence", "To make slides longer"),
                1,
                "Signposting helps listeners follow the structure and transitions of a presentation."
            ),
            MAWExercise(
                "A visual aid should:",
                listOf("Support the speaker's message", "Replace the speaker completely", "Contain every word said", "Distract the audience"),
                0,
                "Visual aids should support communication, not replace it."
            ),
            MAWExercise(
                "The audience is:",
                listOf("The people for whom the presentation is prepared", "Only the speaker", "The bibliography", "A slide template"),
                0,
                "The audience influences how content and language are presented."
            ),
            MAWExercise(
                "Which is a signpost?",
                listOf("First, I will discuss...", "This slide is blue.", "Maybe.", "I forgot the topic."),
                0,
                "First, I will discuss... explicitly signals presentation structure."
            ),
            MAWExercise(
                "A good presentation introduction should:",
                listOf("State the topic and direction", "Read the full reference list", "Give every detail", "Skip the purpose"),
                0,
                "An introduction orients the audience."
            ),
            MAWExercise(
                "A three-minute presentation benefits from:",
                listOf("A clear and focused structure", "Unlimited slides", "Many unrelated points", "No conclusion"),
                0,
                "Limited time requires focus and organization."
            ),
            MAWExercise(
                "Which visual aid is suitable for showing data?",
                listOf("A clear chart", "A long paragraph on one slide", "A random meme", "An empty slide"),
                0,
                "A chart can communicate quantitative relationships efficiently."
            ),
            MAWExercise(
                "Signposting is especially useful when:",
                listOf("Moving between main points", "Ending before speaking", "Removing transitions", "Avoiding organization"),
                0,
                "Signposts make transitions easier for listeners to follow."
            ),
            MAWExercise(
                "A presentation conclusion should:",
                listOf("Summarize key points and close the talk", "Introduce many new topics", "Repeat every slide word for word", "Ignore the audience"),
                0,
                "A conclusion reinforces the main message and closes the presentation."
            ),
            MAWExercise(
                "Academic presentations should generally be:",
                listOf("Clear, organized and audience-aware", "Unplanned and very vague", "Entirely dependent on reading slides", "Without evidence"),
                0,
                "Academic oral communication benefits from clarity, structure and audience awareness."
            )
        )
    ),
    AcademicLesson(
        15,
        "Academic Revision and Self-Assessment",
        "Reviewing work before submission.",
        "Use a systematic checklist to improve academic work.",
        listOf(
            "Revision" to "Reviewing and improving content, organization, language and accuracy.",
            "Proofreading" to "Checking a finished draft for errors in grammar, spelling, punctuation and formatting.",
            "Self-assessment" to "Evaluating your own work against clear criteria."
        ),
        listOf(
            "revise" to "improve a draft after reviewing it",
            "proofread" to "check a text for errors",
            "criterion" to "standard used for evaluation"
        ),
        "Create a personal submission checklist with at least eight items.",
        listOf(
            MAWExercise(
                "What should a student do before submitting an assignment?",
                listOf("Submit immediately", "Proofread and check the assignment against requirements", "Delete all references", "Avoid reviewing the introduction"),
                1,
                "Revision and proofreading help ensure that requirements, content and language have been checked."
            ),
            MAWExercise(
                "Revision focuses on:",
                listOf("Content, organization, language and accuracy", "Only spelling", "Only page numbers", "Only the title"),
                0,
                "Revision is broader than proofreading and can improve content and structure."
            ),
            MAWExercise(
                "Proofreading mainly checks:",
                listOf("Grammar, spelling, punctuation and formatting", "Research questions only", "Ideas only", "The course timetable"),
                0,
                "Proofreading focuses on surface-level accuracy and presentation."
            ),
            MAWExercise(
                "Self-assessment means:",
                listOf("Evaluating your own work against clear criteria", "Ignoring feedback", "Copying a model answer", "Submitting without checking"),
                0,
                "Self-assessment compares work with defined standards."
            ),
            MAWExercise(
                "A criterion is:",
                listOf("A standard used for evaluation", "A paragraph", "A citation style only", "A reading strategy"),
                0,
                "A criterion is a standard against which work can be evaluated."
            ),
            MAWExercise(
                "Which should happen first?",
                listOf("Review content and organization before final proofreading", "Delete the draft", "Submit immediately", "Remove references"),
                0,
                "It is useful to revise larger issues before final proofreading."
            ),
            MAWExercise(
                "A revision checklist may include:",
                listOf("Task requirements, structure, evidence and language", "Only the title", "Only punctuation", "No criteria"),
                0,
                "A checklist can cover the key requirements of the assignment."
            ),
            MAWExercise(
                "Why proofread after revising?",
                listOf("Changes may introduce new errors", "Proofreading replaces research", "It makes sources unnecessary", "It removes the need for a conclusion"),
                0,
                "Revisions can create new wording or formatting errors that should be checked."
            ),
            MAWExercise(
                "Self-assessment is stronger when:",
                listOf("Criteria are clear", "Standards are unknown", "The student guesses", "The work is never reviewed"),
                0,
                "Clear criteria make self-evaluation more consistent."
            ),
            MAWExercise(
                "A good final check should confirm:",
                listOf("The assignment meets requirements and is accurate", "Only that the pages are colorful", "That all evidence is removed", "That the introduction is missing"),
                0,
                "Final checks should confirm compliance with task requirements and accuracy."
            )
        )
    )
)


@Composable
fun MethodologyAcademicWorkScreen(
    onBack: () -> Unit = {}
) {
    var openLesson by remember { mutableStateOf<Int?>(null) }

    if (openLesson != null) {
        MethodologyLessonScreen(
            lesson = academicLessons[openLesson!!],
            onBack = { openLesson = null }
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Methodology of Academic Work", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("English Studies • Semester 1", color = Color.White.copy(alpha = .85f), fontSize = 12.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MAWPrimary)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MAWBackground)
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            item { MethodologyHeader() }
            item { MethodologyProgress() }
            item {
                Text("Course objectives", fontSize = 21.sp, fontWeight = FontWeight.Bold, color = MAWText)
            }
            item { ObjectivesCard() }
            item { Text("Lessons", fontSize = 21.sp, fontWeight = FontWeight.Bold, color = MAWText) }
            items(academicLessons) { lesson ->
                MethodologyLessonItem(lesson = lesson, onClick = { openLesson = lesson.number - 1 })
            }
        }
    }
}

@Composable
private fun MethodologyHeader() {
    Card(Modifier.fillMaxWidth(), RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MAWPrimary)) {
        Column(Modifier.padding(22.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(58.dp).background(Color.White.copy(alpha = .15f), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.School, null, tint = Color.White, modifier = Modifier.size(34.dp))
                }
                Spacer(Modifier.width(15.dp))
                Column {
                    Text("Methodology of Academic Work", color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Bold)
                    Text("Academic skills, study strategies and responsible university work.", color = Color.White.copy(alpha = .9f), fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
private fun MethodologyProgress() {
    val totalExercises = academicLessons.sumOf { it.exercises.size }
    Card(Modifier.fillMaxWidth(), RoundedCornerShape(17.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(18.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Course content", color = MAWGray, fontSize = 13.sp)
                    Text("15 lessons • $totalExercises exercises", color = MAWText, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                }
                Text("Academic", color = MAWPrimary, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(11.dp))
            LinearProgressIndicator(progress = { 0f }, modifier = Modifier.fillMaxWidth().height(8.dp), color = MAWPrimary, trackColor = Color(0xFFE2E8F0))
            Spacer(Modifier.height(8.dp))
            Text("Each lesson includes explanation, vocabulary, guided practice, reading aloud and corrected exercises.", color = MAWGray, fontSize = 12.sp)
        }
    }
}

@Composable
private fun ObjectivesCard() {
    val objectives = listOf(
        "Develop effective university study habits.",
        "Read and process academic information critically.",
        "Use reliable sources responsibly.",
        "Write, paraphrase and summarize accurately.",
        "Understand academic integrity and avoid plagiarism.",
        "Prepare and present academic work clearly.",
        "Revise and self-assess work before submission."
    )
    Card(Modifier.fillMaxWidth(), RoundedCornerShape(17.dp), colors = CardDefaults.cardColors(containerColor = MAWLight)) {
        Column(Modifier.padding(19.dp)) {
            objectives.forEach {
                Row(Modifier.padding(vertical = 5.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.CheckCircle, null, tint = MAWPrimary, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(9.dp))
                    Text(it, color = MAWText, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
private fun MethodologyLessonItem(lesson: AcademicLesson, onClick: () -> Unit) {
    Card(Modifier.fillMaxWidth().clickable(onClick = onClick), RoundedCornerShape(17.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(52.dp).background(MAWLight, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
                Text("%02d".format(lesson.number), color = MAWPrimary, fontWeight = FontWeight.Bold, fontSize = 17.sp)
            }
            Spacer(Modifier.width(13.dp))
            Column(Modifier.weight(1f)) {
                Text("LESSON ${lesson.number}", color = MAWPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text(lesson.title, color = MAWText, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                Text(lesson.subtitle, color = MAWGray, fontSize = 13.sp)
                Spacer(Modifier.height(4.dp))
                Text("${lesson.exercises.size} corrected exercises", color = MAWGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Icon(Icons.Default.PlayArrow, null, tint = MAWPrimary)
        }
    }
}

@Composable
private fun MethodologyLessonScreen(lesson: AcademicLesson, onBack: () -> Unit) {
    var section by remember { mutableIntStateOf(0) }
    val sections = listOf("Learn", "Vocabulary", "Exercises", "Practice", "Read Aloud")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Lesson ${lesson.number}", color = Color.White, fontWeight = FontWeight.Bold)
                        Text(lesson.title, color = Color.White.copy(alpha = .85f), fontSize = 12.sp)
                    }
                },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MAWPrimary)
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().background(MAWBackground).padding(padding)) {
            Row(
                Modifier.fillMaxWidth().background(Color.White).padding(7.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                sections.forEachIndexed { index, title ->
                    Box(
                        Modifier.weight(1f)
                            .background(if (section == index) MAWPrimary else Color(0xFFF1F5F9), RoundedCornerShape(11.dp))
                            .clickable { section = index }
                            .padding(vertical = 9.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(title, color = if (section == index) Color.White else MAWGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            when (section) {
                0 -> LessonLearn(lesson)
                1 -> LessonVocabulary(lesson)
                2 -> LessonExercises(lesson)
                3 -> LessonPractice(lesson)
                4 -> LessonReadAloud(lesson)
            }
        }
    }
}

@Composable
private fun LessonLearn(lesson: AcademicLesson) {
    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Text(lesson.title, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MAWText)
            Text(lesson.subtitle, color = MAWGray)
        }
        item {
            Card(Modifier.fillMaxWidth(), RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MAWLight)) {
                Column(Modifier.padding(19.dp)) {
                    Text("Learning objective", color = MAWPrimary, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(7.dp))
                    Text(lesson.objective, color = MAWText, fontSize = 15.sp)
                }
            }
        }
        item { Text("Key concepts", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MAWText) }
        items(lesson.concepts) { (term, explanation) -> ConceptCard(term, explanation) }
        item { Text("Academic application", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MAWText) }
        item {
            Card(Modifier.fillMaxWidth(), RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MAWLightPurple)) {
                Column(Modifier.padding(19.dp)) {
                    Text("Think and apply", color = MAWPurple, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(7.dp))
                    Text(lesson.task, color = MAWText, fontSize = 15.sp, lineHeight = 23.sp)
                }
            }
        }
    }
}

@Composable
private fun ConceptCard(term: String, explanation: String) {
    Card(Modifier.fillMaxWidth(), RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(17.dp)) {
            Text(term, color = MAWPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text(explanation, color = MAWText, fontSize = 14.sp, lineHeight = 22.sp)
        }
    }
}

@Composable
private fun LessonVocabulary(lesson: AcademicLesson) {
    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Text("Academic Vocabulary", fontSize = 27.sp, fontWeight = FontWeight.Bold, color = MAWText)
            Text("Key terms from this lesson.", color = MAWGray)
        }
        items(lesson.vocabulary) { (word, definition) ->
            Card(Modifier.fillMaxWidth(), RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(Modifier.padding(17.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.MenuBook, null, tint = MAWPrimary, modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(11.dp))
                    Column {
                        Text(word, color = MAWPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(Modifier.height(4.dp))
                        Text(definition, color = MAWText, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun LessonExercises(lesson: AcademicLesson) {
    var current by remember { mutableIntStateOf(0) }
    var selected by remember { mutableStateOf<Int?>(null) }
    var answered by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    val exercise = lesson.exercises[current]
    val total = lesson.exercises.size
    val progress = (current + if (answered) 1 else 0).toFloat() / total.toFloat()

    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Text("Corrected Exercises", fontSize = 27.sp, fontWeight = FontWeight.Bold, color = MAWText)
            Text("Immediate correction with explanation • Exercise ${current + 1} / $total", color = MAWGray)
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(progress = { progress.coerceIn(0f, 1f) }, modifier = Modifier.fillMaxWidth().height(8.dp), color = MAWPrimary, trackColor = Color(0xFFE2E8F0))
        }
        item {
            Card(Modifier.fillMaxWidth(), RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(19.dp)) {
                    Text("Question ${current + 1}", color = MAWPrimary, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(9.dp))
                    Text(exercise.question, color = MAWText, fontSize = 19.sp, fontWeight = FontWeight.Bold, lineHeight = 26.sp)
                    Spacer(Modifier.height(15.dp))
                    exercise.options.forEachIndexed { index, option ->
                        val correct = index == exercise.correct
                        val chosen = selected == index
                        val background = when {
                            answered && correct -> MAWLightGreen
                            answered && chosen -> MAWLightRed
                            chosen -> MAWLight
                            else -> Color(0xFFF8FAFC)
                        }
                        Row(
                            Modifier.fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .background(background, RoundedCornerShape(12.dp))
                                .clickable(enabled = !answered) { selected = index; answered = true; if (index == exercise.correct) score++ }
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("${('A'.code + index).toChar()}.", color = MAWPrimary, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.width(10.dp))
                            Text(option, color = MAWText, modifier = Modifier.weight(1f))
                            if (answered && correct) Icon(Icons.Default.CheckCircle, null, tint = MAWGreen)
                        }
                    }
                }
            }
        }
        if (answered) {
            item {
                Card(Modifier.fillMaxWidth(), RoundedCornerShape(17.dp), colors = CardDefaults.cardColors(containerColor = if (selected == exercise.correct) MAWLightGreen else MAWLightRed)) {
                    Column(Modifier.padding(18.dp)) {
                        Text(if (selected == exercise.correct) "Correct" else "Not correct", color = if (selected == exercise.correct) MAWGreen else MAWRed, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        Text("Correct answer: ${exercise.options[exercise.correct]}", color = MAWText, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        Text("Why? ${exercise.explanation}", color = MAWText, lineHeight = 22.sp)
                    }
                }
            }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                    Button(
                        onClick = { selected = null; answered = false },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MAWDark)
                    ) {
                        Icon(Icons.Default.Refresh, null)
                        Spacer(Modifier.width(6.dp))
                        Text("Redo")
                    }
                    Button(
                        onClick = {
                            if (current < total - 1) { current++; selected = null; answered = false }
                        },
                        enabled = current < total - 1,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MAWPrimary)
                    ) {
                        Text(if (current < total - 1) "Next" else "Finished")
                    }
                }
            }
            if (current == total - 1) {
                item {
                    Card(Modifier.fillMaxWidth(), RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MAWLight)) {
                        Text("Score: $score / $total", modifier = Modifier.padding(16.dp), color = MAWPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun LessonPractice(lesson: AcademicLesson) {
    var answer by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }
    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Text("Guided Practice", fontSize = 27.sp, fontWeight = FontWeight.Bold, color = MAWText)
            Text("Apply the skill in your own words.", color = MAWGray)
        }
        item {
            Card(Modifier.fillMaxWidth(), RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MAWLightOrange)) {
                Column(Modifier.padding(19.dp)) {
                    Text("Task", color = MAWOrange, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text(lesson.task, color = MAWText, fontSize = 15.sp, lineHeight = 23.sp)
                }
            }
        }
        item {
            OutlinedTextField(
                value = answer,
                onValueChange = { answer = it; submitted = false },
                modifier = Modifier.fillMaxWidth().height(220.dp),
                label = { Text("Write your answer here...") },
                shape = RoundedCornerShape(15.dp)
            )
        }
        item {
            Button(onClick = { submitted = true }, enabled = answer.trim().isNotEmpty(), modifier = Modifier.fillMaxWidth().height(52.dp), colors = ButtonDefaults.buttonColors(containerColor = MAWPrimary), shape = RoundedCornerShape(14.dp)) {
                Text("Save Practice Answer", fontWeight = FontWeight.Bold)
            }
        }
        if (submitted) {
            item {
                Card(Modifier.fillMaxWidth(), RoundedCornerShape(17.dp), colors = CardDefaults.cardColors(containerColor = MAWLightGreen)) {
                    Column(Modifier.padding(18.dp)) {
                        Text("Answer recorded", color = MAWGreen, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        Text("Review your response against the lesson concepts and correct any weak points before moving on.", color = MAWText)
                    }
                }
            }
        }
    }
}

@Composable
private fun LessonReadAloud(lesson: AcademicLesson) {
    val context = LocalContext.current
    val tts = remember {
        TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // English is the reading language for this module.
            }
        }
    }
    DisposableEffect(Unit) { onDispose { tts.stop(); tts.shutdown() } }

    val text = buildString {
        append(lesson.title).append(". ")
        append(lesson.objective).append(". ")
        lesson.concepts.forEach { append(it.first).append(". ").append(it.second).append(". ") }
        append("Academic task. ").append(lesson.task)
    }

    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Text("Read Aloud", fontSize = 27.sp, fontWeight = FontWeight.Bold, color = MAWText)
            Text("Listen to the lesson content in clear English.", color = MAWGray)
        }
        item {
            Card(Modifier.fillMaxWidth(), RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(19.dp)) {
                    Text(text, color = MAWText, fontSize = 15.sp, lineHeight = 24.sp)
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {
                            tts.language = Locale.US
                            tts.setSpeechRate(0.88f)
                            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "MAW_${lesson.number}")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MAWPrimary)
                    ) {
                        Icon(Icons.Default.VolumeUp, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Read this lesson aloud")
                    }
                }
            }
        }
    }
}
