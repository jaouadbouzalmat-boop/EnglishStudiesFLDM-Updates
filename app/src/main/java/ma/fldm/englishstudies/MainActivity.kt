@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package ma.fldm.englishstudies

import android.os.Bundle
import android.content.SharedPreferences
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import ma.fldm.englishstudies.updates.UpdateScreen
import ma.fldm.englishstudies.updates.UpdateViewModel
import ma.fldm.englishstudies.updates.UpdateUiState
import ma.fldm.englishstudies.updates.ApkUpdateInstaller
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ma.fldm.englishstudies.data.CourseData
import ma.fldm.englishstudies.ui.theme.EnglishStudiesFLDMTheme
import ma.fldm.englishstudies.updates.SetupUpdateNotifications
import androidx.compose.ui.platform.LocalContext
import ma.fldm.englishstudies.data.ProgressDataStore
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.Color


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            EnglishStudiesRuntimeTheme()
        }
    }
}

@Composable
private fun EnglishStudiesRuntimeTheme() {
    val context = LocalContext.current
    val systemDark = isSystemInDarkTheme()

    var settingsVersion by remember { mutableStateOf(0) }

    androidx.compose.runtime.DisposableEffect(context) {
        val prefs = AppSettingsPrefs.prefs(context)

        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
            settingsVersion++
        }

        prefs.registerOnSharedPreferenceChangeListener(listener)

        onDispose {
            prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    // Lecture du snapshot actuel. settingsVersion force la recomposition
    // lorsque l'utilisateur change un réglage depuis SettingsScreen.
    @Suppress("UNUSED_VARIABLE")
    val observedVersion = settingsVersion

    val themeMode =
        AppSettingsPrefs.string(
            context,
            AppSettingsPrefs.THEME,
            "Système"
        )

    val darkMode = when (themeMode) {
        "Sombre" -> true
        "Clair" -> false
        else -> systemDark
    }

    val textSize =
        AppSettingsPrefs.string(
            context,
            AppSettingsPrefs.TEXT_SIZE,
            "Moyenne"
        )

    val selectedFontScale = when (textSize) {
        "Petite" -> 0.90f
        "Grande" -> 1.15f
        else -> 1.0f
    }

    val density = LocalDensity.current

    val lightColors = lightColorScheme(
        primary = Color(0xFF2563EB),
        secondary = Color(0xFFD9A72A),
        background = Color(0xFFFFFBF8),
        surface = Color.White
    )

    val darkColors = darkColorScheme(
        primary = Color(0xFF7EA9FF),
        secondary = Color(0xFFE8B84A),
        background = Color(0xFF111827),
        surface = Color(0xFF1F2937)
    )

    EnglishStudiesFLDMTheme {
        MaterialTheme(
            colorScheme = if (darkMode) darkColors else lightColors
        ) {
            CompositionLocalProvider(
                LocalDensity provides Density(
                    density = density.density,
                    fontScale = selectedFontScale
                )
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EnglishStudiesApp()
                }
            }
        }
    }
}

@Composable
fun EnglishStudiesApp() {

    // ViewModel de mise à jour conservé pendant la durée de l’écran
    val updateViewModel = remember { UpdateViewModel() }

    // =========================================================
    // NOTIFICATIONS DE MISE À JOUR
    // =========================================================
    // Demande l'autorisation Android 13+ puis abonne
    // automatiquement l'appareil au topic FCM.
    SetupUpdateNotifications(
        context = LocalContext.current
    )

    // Écran affiché au démarrage pour tester la licence
    var currentScreen by remember {
        mutableStateOf("license_gate")
    }

    // =========================================================
    // VÉRIFICATION AUTOMATIQUE DES MISES À JOUR
    // =========================================================

    val updateState by updateViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        updateViewModel.checkForUpdate()
    }

    var previousScreen by remember {
        mutableStateOf("home")
    }

    var selectedModule by remember {
        mutableStateOf("")
    }

    var currentLesson by remember {
        mutableStateOf("")
    }

    when (currentScreen) {
        // =========================================================
        // MISE À JOUR DE L'APPLICATION
        // =========================================================

        "update" -> {

            val context = LocalContext.current

            UpdateScreen(
                viewModel = updateViewModel,

                onDownload = { url, onProgress ->

                    ApkUpdateInstaller.downloadAndInstall(
                        context = context,
                        downloadUrl = url,

                        onProgress = { progress ->
                            onProgress(progress)
                        },

                        onError = { message ->

                            android.widget.Toast
                                .makeText(
                                    context,
                                    message,
                                    android.widget.Toast.LENGTH_LONG
                                )
                                .show()
                        }
                    )
                }
            )
        }

        // =========================================================
        // PARAMÈTRES
        // =========================================================

        "settings" -> {

            SettingsScreen(
                hasUpdate = updateState is UpdateUiState.Available,

                onBack = {
                    currentScreen = "home"
                },

                onAboutClick = {
                    currentScreen = "update"
                }
            )
        }

        // =========================================================
        // ACCUEIL
        // =========================================================

        "home" -> {

            HomeScreen(

                onSettingsClick = {
                    currentScreen = "settings"
                },

                onSemesterClick = { semester ->
                    currentScreen = semester
                },

                onProgressClick = {
                    currentScreen = "progress"
                }
            )
        }
        "progress" -> {

            ProgressScreen(
                onBack = {
                    currentScreen = "home"
                }
            )
        }
        // =========================================================
// CONTRÔLE DE LICENCE / ESSAI
// =========================================================

        "license_gate" -> {

            LicenseGateScreen(
                onAccessGranted = {
                    currentScreen = "home"
                },
                onOpenLicense = {
                    currentScreen = "license"
                }
            )
        }

// =========================================================
// ÉCRAN DE LICENCE
// =========================================================

        "license" -> {

            LicenseScreen(
                onActivated = {
                    currentScreen = "home"
                },
                onBack = {
                    currentScreen = "license_gate"
                }
            )
        }

        // =========================================================
        // S1
        // =========================================================

        "S1" -> {

            SemesterScreen(
                semester = "S1",

                modules = listOf(
                    "Grammar 1",
                    "Paragraph Writing",
                    "Reading Comprehension and Précis 1",
                    "Spoken English",
                    "Guided Reading 1",
                    "Language and Terminology 1",
                    "French",
                    "Methodology of Academic Work"
                ),

                onBack = {
                    currentScreen = "home"
                },

                onModuleClick = { module ->

                    selectedModule = module
                    previousScreen = "S1"

                    when (module) {
                        "Grammar 1" -> {
                            currentScreen = "grammar1"
                        }

                        "Paragraph Writing" -> {
                            currentScreen = "paragraph_writing"
                        }

                        "Guided Reading 1" -> {
                            currentScreen = "guided_reading_1"
                        }

                        "Reading Comprehension and Précis 1" -> {
                            currentScreen = "reading_comprehension"
                        }

                        "Spoken English" -> {
                            currentScreen = "spoken_english"
                        }

                        "Language and Terminology 1" -> {
                            currentScreen = "language_terminology_1"
                        }

                        "French" -> {
                            currentScreen = "french"
                        }

                        "Methodology of Academic Work" -> {
                            currentScreen = "methodology_academic_work"
                        }

                        else -> {
                            currentScreen = "module"
                        }
                    }
                }
            )
        }

        // =========================================================
        // S2
        // =========================================================

        "S2" -> {

            SemesterScreen(
                semester = "S2",

                modules = listOf(
                    "Grammar 2",
                    "Composition 1",
                    "Reading Comprehension & Précis 2",
                    "Oral Communication",
                    "Guided Reading 2",
                    "French",
                    "Digital Skills: Digital Culture"
                ),

                onBack = {
                    currentScreen = "home"
                },

                onModuleClick = { module ->

                    selectedModule = module
                    previousScreen = "S2"
                    currentScreen = "module"
                }
            )
        }

        // =========================================================
        // S3
        // =========================================================

        "S3" -> {

            SemesterScreen(
                semester = "S3",

                modules = listOf(
                    "Advanced Grammar",
                    "Advanced Composition",
                    "Extensive Reading",
                    "Public Speaking",
                    "Readings in Global Cultures",
                    "French",
                    "Culture and Art Skills: History and Moroccan Patrimony"
                ),

                onBack = {
                    currentScreen = "home"
                },

                onModuleClick = { module ->

                    selectedModule = module
                    previousScreen = "S3"
                    currentScreen = "module"
                }
            )
        }

        // =========================================================
        // S4
        // =========================================================

        "S4" -> {

            SemesterScreen(
                semester = "S4",

                modules = listOf(
                    "Introduction to Linguistics",
                    "Introduction to Research",
                    "Introduction to Discourse Analysis",
                    "Introduction to Translation",
                    "Introduction to Cultural Studies",
                    "French",
                    "Power Skills: Personal Development"
                ),

                onBack = {
                    currentScreen = "home"
                },

                onModuleClick = { module ->

                    selectedModule = module
                    previousScreen = "S4"
                    currentScreen = "module"
                }
            )
        }

        // =========================================================
        // S5
        // =========================================================

        "S5" -> {

            StreamSelectionScreen(
                semester = "S5",

                onBack = {
                    currentScreen = "home"
                },

                onStreamSelected = { stream ->
                    currentScreen = stream
                }
            )
        }

        // =========================================================
        // S6
        // =========================================================

        "S6" -> {

            StreamSelectionScreen(
                semester = "S6",

                onBack = {
                    currentScreen = "home"
                },

                onStreamSelected = { stream ->
                    currentScreen = stream
                }
            )
        }

        // =========================================================
        // S5 - LITERARY
        // =========================================================

        "S5_LITERARY" -> {

            SemesterScreen(
                semester = "S5 – Literary & Cultural Studies",

                modules = listOf(
                    "Research Methodology",
                    "Moroccan Cultures",
                    "Novel 1",
                    "Drama 1",
                    "Muslim Heritage",
                    "Literary & Cultural Theories"
                ),

                onBack = {
                    currentScreen = "S5"
                },

                onModuleClick = { module ->

                    selectedModule = module
                    previousScreen = "S5_LITERARY"

                    if (module == "Novel 1") {
                        currentScreen = "the_reluctant_fundamentalist"
                    } else {
                        currentScreen = "module"
                    }
                }
            )
        }

        // =========================================================
        // S5 - LINGUISTICS
        // =========================================================

        "S5_LINGUISTICS" -> {

            SemesterScreen(
                semester = "S5 – Linguistics & Cultural Studies",

                modules = listOf(
                    "Research Methodology",
                    "Applied Linguistics",
                    "Sociolinguistics",
                    "Post-Colonial Literature",
                    "Media Studies",
                    "Cultural Translation"
                ),

                onBack = {
                    currentScreen = "S5"
                },

                onModuleClick = { module ->

                    selectedModule = module
                    previousScreen = "S5_LINGUISTICS"
                    currentScreen = "module"
                }
            )
        }

        // =========================================================
        // S6 - LITERARY
        // =========================================================

        "S6_LITERARY" -> {

            SemesterScreen(
                semester = "S6 – Literary & Cultural Studies",

                modules = listOf(
                    "Maghreb & Europe",
                    "Youth & Cyber Culture",
                    "Novel 2",
                    "Drama 2",
                    "PostColonial Studies",
                    "Research Project"
                ),

                onBack = {
                    currentScreen = "S6"
                },

                onModuleClick = { module ->

                    if (module == "Novel 2") {
                        previousScreen = "S6_LITERARY"
                        currentScreen = "robinson_crusoe"
                    } else {
                        selectedModule = module
                        previousScreen = "S6_LITERARY"
                        currentScreen = "module"
                    }
                }
            )
        }

        // =========================================================
        // S6 - LINGUISTICS
        // =========================================================

        "S6_LINGUISTICS" -> {

            SemesterScreen(
                semester = "S6 – Linguistics & Cultural Studies",

                modules = listOf(
                    "Semantics",
                    "Pragmatics",
                    "Morpho-syntax",
                    "Diaspora Literature",
                    "Language & Cultures",
                    "Research Project"
                ),

                onBack = {
                    currentScreen = "S6"
                },

                onModuleClick = { module ->

                    selectedModule = module
                    previousScreen = "S6_LINGUISTICS"
                    currentScreen = "module"
                }
            )
        }
        // =========================================================
// PAGE MODULE
// =========================================================
        // =========================================================
// OLIVER TWIST — GUIDED READING 1
// =========================================================

        "oliver_twist" -> {

            OliverTwistScreen(
                onBack = {
                    currentScreen = "S1"
                }
            )
        }

        // =========================================================
        // S1 - GUIDED READING 1 — NOUVELLE ARCHITECTURE
        // =========================================================

        "guided_reading_1" -> {
            GuidedReadingScreen(
                onBack = {
                    currentScreen = "S1"
                }
            )
        }

// =========================================================
// THE RELUCTANT FUNDAMENTALIST — S5 / NOVEL 1
// =========================================================

        "the_reluctant_fundamentalist" -> {

            TheReluctantFundamentalistScreen(
                onBack = {
                    currentScreen = "S5_LITERARY"
                }
            )
        }

        // =========================================================
        // ROBINSON CRUSOE — S6 / NOVEL 2
        // =========================================================

        "robinson_crusoe" -> {

            RobinsonCrusoeScreen(
                onBack = {
                    currentScreen = "S6_LITERARY"
                }
            )
        }

        // =========================================================
        // S1 - GRAMMAR 1 — NOUVELLE ARCHITECTURE PÉDAGOGIQUE
        // =========================================================

        "grammar1" -> {
            Grammar1Screen(
                onBack = {
                    currentScreen = "S1"
                }
            )
        }

        // =========================================================
        // S1 - FRENCH
        // =========================================================

        "french" -> {
            FrenchScreen(
                onBack = {
                    currentScreen = "S1"
                }
            )
        }

        // =========================================================
        // S1 - PARAGRAPH WRITING
        // =========================================================

        "paragraph_writing" -> {
            ParagraphWritingScreen(
                onBack = {
                    currentScreen = "S1"
                }
            )
        }

        // =========================================================
        // S1 - READING COMPREHENSION & PRÉCIS 1
        // =========================================================

        "reading_comprehension" -> {
            ReadingComprehensionScreen(
                onBack = {
                    currentScreen = "S1"
                }
            )
        }

        // =========================================================
        // S1 - SPOKEN ENGLISH
        // =========================================================

        "spoken_english" -> {
            SpokenEnglishScreen(
                onBack = {
                    currentScreen = "S1"
                }
            )
        }

        // =========================================================
        // S1 - LANGUAGE AND TERMINOLOGY 1
        // =========================================================

        "language_terminology_1" -> {
            LanguageTerminology1Screen(
                onBack = {
                    currentScreen = "S1"
                }
            )
        }

        // =========================================================
        // S1 - METHODOLOGY OF ACADEMIC WORK
        // =========================================================

        "methodology_academic_work" -> {
            MethodologyAcademicWorkScreen(
                onBack = {
                    currentScreen = "S1"
                }
            )
        }

        "module" -> {

            ModuleScreen(
                moduleName = selectedModule,

                onBack = {
                    currentScreen = previousScreen
                },

                onLessonsClick = {

                    if (selectedModule == "Grammar 1") {
                        currentScreen = "lessons"
                    }
                },

                onListeningClick = {
                    currentScreen = "listening"
                }
            )
        }

// =========================================================
// PAGE LISTENING
// =========================================================

        "listening" -> {

            EnglishStudiesListeningScreen(
                moduleName = selectedModule,

                onBack = {
                    currentScreen = "module"
                }
            )
        }

// =========================================================
// LISTE DES LEÇONS
// =========================================================

        "lessons" -> {

            LessonsScreen(
                moduleName = selectedModule,

                onBack = {
                    currentScreen = "module"
                },

                onLessonClick = { lessonTitle ->

                    currentLesson = lessonTitle
                    currentScreen = "lesson"
                }
            )
        }

// =========================================================
// UNE LEÇON
// =========================================================

        "lesson" -> {

            LessonScreen(
                lessonName = currentLesson,

                onBack = {
                    currentScreen = "lessons"
                },

                onQuizClick = {
                    currentScreen = "quiz"
                }
            )
        }

// =========================================================
// QUIZ
// =========================================================

        "quiz" -> {

            QuizScreen(
                lessonName = currentLesson,

                onBack = {
                    currentScreen = "lesson"
                }
            )
        }

        else -> {
            currentScreen = "home"
        }
    }
}
// =================================================================
// ÉCRAN LISTENING
// =================================================================

@Composable
fun EnglishStudiesListeningScreen(
    moduleName: String,
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Text(
            text = "← Retour",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clickable {
                    onBack()
                }
                .padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "🎧",
            fontSize = 48.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Listening",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = moduleName,
            fontSize = 17.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Compréhension orale",
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Les activités de listening seront disponibles ici."
        )
    }
}

// =================================================================
// ACCUEIL — INTERFACE PROFESSIONNELLE
// =================================================================

@Composable
fun HomeScreen(
    onSettingsClick: () -> Unit,
    onSemesterClick: (String) -> Unit,
    onProgressClick: () -> Unit
) {
    val context = LocalContext.current

    // ---------------------------------------------------------
    // PROGRESSION RÉELLEMENT ENREGISTRÉE
    // ---------------------------------------------------------
    val progressDataStore = remember(context) {
        ProgressDataStore(context)
    }

    val bestScores by progressDataStore.bestScores
        .collectAsState(initial = emptyMap())

    // Pour le moment, le suivi de progression connecté au DataStore
    // correspond aux leçons de Grammar 1 qui possèdent un quiz.
    val trackedLessonIds = CourseData.grammar1Units
        .flatMap { it.lessons }
        .map { it.id }
        .toSet()

    val totalTrackedLessons = trackedLessonIds.size
    val lessonsTracked = bestScores.keys.count { it in trackedLessonIds }
    val successfulQuizzes = bestScores.entries.count {
        it.key in trackedLessonIds && it.value > 0
    }

    val progress = if (totalTrackedLessons > 0) {
        (lessonsTracked.toFloat() / totalTrackedLessons.toFloat())
            .coerceIn(0f, 1f)
    } else {
        0f
    }

    val progressPercent = (progress * 100).toInt()

    val navy = Color(0xFF123A78)
    val blue = Color(0xFF2563EB)
    val gold = Color(0xFFD6A33A)
    val softBlue = Color(0xFFF2F7FF)
    val softGold = Color(0xFFFFF8E9)
    val softRose = Color(0xFFFFF2F4)
    val softGreen = Color(0xFFEEF9F4)
    val softPurple = Color(0xFFF4F0FF)
    val textDark = Color(0xFF172554)
    val textMuted = Color(0xFF64748B)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 14.dp)
    ) {
        // ---------------------------------------------------------
        // EN-TÊTE
        // ---------------------------------------------------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "English Studies",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = navy
                )
                Text(
                    text = "FLDM – Fès",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = blue
                )
            }

            Box {
                IconButton(onClick = onSettingsClick) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Paramètres",
                        tint = navy,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Learning English at University",
            fontSize = 15.sp,
            color = textMuted
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ---------------------------------------------------------
        // CARTE DE BIENVENUE
        // ---------------------------------------------------------
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = navy),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Column(modifier = Modifier.padding(end = 48.dp)) {
                    Text(
                        text = "Bienvenue",
                        fontSize = 27.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "Commencez votre parcours universitaire en anglais.",
                        fontSize = 15.sp,
                        lineHeight = 21.sp,
                        color = Color(0xFFE6EEFF)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(gold),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = navy,
                                modifier = Modifier.size(21.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Cours • Exercices • Quiz • Audio",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = Color(0xFFE8F0FF),
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.TopEnd)
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // ---------------------------------------------------------
        // RACCOURCIS
        // ---------------------------------------------------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            HomeInfoCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.School,
                title = "6",
                subtitle = "Semestres",
                background = softBlue,
                iconTint = blue
            )
            HomeInfoCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.MenuBook,
                title = "Modules",
                subtitle = "Académiques",
                background = softGold,
                iconTint = Color(0xFFB7791F)
            )
            HomeInfoCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.BarChart,
                title = "Suivi",
                subtitle = "Personnalisé",
                background = softPurple,
                iconTint = Color(0xFF7C3AED)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ---------------------------------------------------------
        // TITRE SEMESTRES
        // ---------------------------------------------------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .width(5.dp)
                        .height(30.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(gold)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Choisissez votre semestre",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = textDark
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "Accédez au contenu de votre programme",
            fontSize = 14.sp,
            color = textMuted,
            modifier = Modifier.padding(start = 15.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        SemesterRow(
            semester1 = "S1",
            semester2 = "S2",
            background1 = softBlue,
            background2 = softGold,
            iconTint1 = blue,
            iconTint2 = Color(0xFFB7791F),
            onSemesterClick = onSemesterClick
        )

        Spacer(modifier = Modifier.height(12.dp))

        SemesterRow(
            semester1 = "S3",
            semester2 = "S4",
            background1 = softRose,
            background2 = softGreen,
            iconTint1 = Color(0xFFD9465F),
            iconTint2 = Color(0xFF169B70),
            onSemesterClick = onSemesterClick
        )

        Spacer(modifier = Modifier.height(12.dp))

        SemesterRow(
            semester1 = "S5",
            semester2 = "S6",
            background1 = softPurple,
            background2 = Color(0xFFEEF7FF),
            iconTint1 = Color(0xFF7C3AED),
            iconTint2 = blue,
            onSemesterClick = onSemesterClick
        )

        Spacer(modifier = Modifier.height(20.dp))

        // ---------------------------------------------------------
        // MA PROGRESSION — VRAI SUIVI
        // ---------------------------------------------------------
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onProgressClick),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = navy),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = "Progression",
                            tint = blue,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Ma progression",
                            fontSize = 21.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "Suivez votre parcours et vos résultats",
                            fontSize = 13.sp,
                            color = Color(0xFFE6EEFF)
                        )
                    }

                    Text(
                        text = "$progressPercent%",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = gold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Progression des leçons évaluées",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFE6EEFF)
                )

                Spacer(modifier = Modifier.height(7.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(9.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color.White.copy(alpha = 0.18f))
                ) {
                    if (progress > 0f) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction = progress)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(50.dp))
                                .background(gold)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ProgressStat(
                        value = lessonsTracked.toString(),
                        label = "Leçons suivies"
                    )
                    ProgressStat(
                        value = successfulQuizzes.toString(),
                        label = "Quiz réussis"
                    )
                    ProgressStat(
                        value = totalTrackedLessons.toString(),
                        label = "Leçons disponibles"
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onProgressClick),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Voir les détails",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = gold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Ouvrir la progression",
                        tint = gold,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
private fun ProgressStat(
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(95.dp)
    ) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            lineHeight = 14.sp,
            color = Color(0xFFE6EEFF),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
private fun HomeInfoCard(
    modifier: Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    background: Color,
    iconTint: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = background),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 11.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(19.dp)
                )
            }
            Spacer(modifier = Modifier.height(7.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1E293B)
            )
            Text(
                text = subtitle,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF64748B)
            )
        }
    }
}

// =================================================================
// LIGNE DES SEMESTRES
// =================================================================

@Composable
fun SemesterRow(
    semester1: String,
    semester2: String,
    background1: Color,
    background2: Color,
    iconTint1: Color,
    iconTint2: Color,
    onSemesterClick: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SemesterCard(
            semester = semester1,
            modifier = Modifier.weight(1f),
            backgroundColor = background1,
            iconTint = iconTint1,
            onClick = { onSemesterClick(semester1) }
        )
        SemesterCard(
            semester = semester2,
            modifier = Modifier.weight(1f),
            backgroundColor = background2,
            iconTint = iconTint2,
            onClick = { onSemesterClick(semester2) }
        )
    }
}

// =================================================================
// CARTE SEMESTRE
// =================================================================

@Composable
fun SemesterCard(
    semester: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(116.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.78f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 2.dp)
            ) {
                Text(
                    text = semester,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF172554)
                )
                Text(
                    text = "Semestre ${semester.removePrefix("S")}",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight.Medium
                )
            }

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(iconTint),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Ouvrir $semester",
                    tint = Color.White,
                    modifier = Modifier.size(19.dp)
                )
            }
        }
    }
}


// =================================================================
// PALETTE VISUELLE DES SEMESTRES
// =================================================================

private data class SemesterPalette(
    val pageBackground: Color,
    val accent: Color,
    val accentSoft: Color
)

private fun semesterPalette(semester: String): SemesterPalette {
    return when {
        semester.startsWith("S1") -> SemesterPalette(
            pageBackground = Color(0xFFF2F7FF),
            accent = Color(0xFF2563EB),
            accentSoft = Color(0xFFE7F0FF)
        )

        semester.startsWith("S2") -> SemesterPalette(
            pageBackground = Color(0xFFFFF8E9),
            accent = Color(0xFFB7791F),
            accentSoft = Color(0xFFFFF0CE)
        )

        semester.startsWith("S3") -> SemesterPalette(
            pageBackground = Color(0xFFFFF2F4),
            accent = Color(0xFFD9465F),
            accentSoft = Color(0xFFFFE3E8)
        )

        semester.startsWith("S4") -> SemesterPalette(
            pageBackground = Color(0xFFEEF9F4),
            accent = Color(0xFF169B70),
            accentSoft = Color(0xFFDDF5EA)
        )

        semester.startsWith("S5") -> SemesterPalette(
            pageBackground = Color(0xFFF4F0FF),
            accent = Color(0xFF7C3AED),
            accentSoft = Color(0xFFEDE3FF)
        )

        semester.startsWith("S6") -> SemesterPalette(
            pageBackground = Color(0xFFEEF7FF),
            accent = Color(0xFF2563EB),
            accentSoft = Color(0xFFE2F0FF)
        )

        else -> SemesterPalette(
            pageBackground = Color(0xFFF4F7FB),
            accent = Color(0xFF2563EB),
            accentSoft = Color(0xFFEAF2FF)
        )
    }
}


// =================================================================
// PAGE SEMESTRE
// =================================================================

@Composable
fun SemesterScreen(
    semester: String,
    modules: List<String>,
    onBack: () -> Unit,
    onModuleClick: (String) -> Unit
) {
    val palette = semesterPalette(semester)
    val textDark = Color(0xFF172554)
    val textGray = Color(0xFF64748B)

    Scaffold(
        containerColor = palette.pageBackground,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "English Studies — $semester",
                            color = textDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            maxLines = 1
                        )
                        Text(
                            text = "FLDM – Fès • Modules universitaires",
                            color = textGray,
                            fontSize = 11.sp,
                            maxLines = 1
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Retour",
                            tint = textDark
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = palette.pageBackground
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(palette.pageBackground)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {

            // ---------------------------------------------------------
            // CARTE RESUME
            // ---------------------------------------------------------
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = palette.accentSoft
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 1.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(17.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.90f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = palette.accent,
                            modifier = Modifier.size(31.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Semestre $semester",
                            color = textDark,
                            fontSize = 23.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Spacer(modifier = Modifier.height(3.dp))

                        Text(
                            text = "${modules.size} modules universitaires",
                            color = textGray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // ---------------------------------------------------------
            // TITRE UNIQUE
            // ---------------------------------------------------------
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(5.dp)
                        .height(29.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(palette.accent)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Modules du $semester",
                    color = textDark,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Choisissez une matière pour continuer votre parcours.",
                color = textGray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(15.dp))

            // Les cartes des matières restent blanches.
            // Seul le fond général de la page reprend la couleur du semestre.
            modules.forEachIndexed { index, module ->
                ModuleCard(
                    number = index + 1,
                    title = module,
                    accent = palette.accent,
                    onClick = {
                        onModuleClick(module)
                    }
                )

                if (index != modules.lastIndex) {
                    Spacer(modifier = Modifier.height(9.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}


// =================================================================
// CARTE MODULE
// =================================================================

@Composable
fun ModuleCard(
    number: Int,
    title: String,
    accent: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Repère discret du semestre.
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(45.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(accent)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Numéro.
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(
                        accent.copy(alpha = 0.10f),
                        RoundedCornerShape(11.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = number.toString(),
                    color = accent,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Nom de la matière.
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                color = Color(0xFF1E293B),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 21.sp
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Flèche.
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(accent.copy(alpha = 0.10f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Ouvrir",
                    tint = accent,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}


// =================================================================
// SELECTION DU PARCOURS S5 / S6
// =================================================================

@Composable
fun StreamSelectionScreen(
    semester: String,
    onBack: () -> Unit,
    onStreamSelected: (String) -> Unit
) {
    val palette = semesterPalette(semester)
    val textDark = Color(0xFF172554)
    val textGray = Color(0xFF64748B)

    Scaffold(
        containerColor = palette.pageBackground,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "English Studies — $semester",
                            color = textDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            maxLines = 1
                        )
                        Text(
                            text = "FLDM – Fès • Parcours",
                            color = textGray,
                            fontSize = 11.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Retour",
                            tint = textDark
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = palette.pageBackground
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(palette.pageBackground)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = palette.accentSoft
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 1.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(17.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.90f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = palette.accent,
                            modifier = Modifier.size(31.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = semester,
                            color = textDark,
                            fontSize = 23.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "Choisissez votre parcours",
                            color = textGray,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(5.dp)
                        .height(29.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(palette.accent)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Parcours disponibles",
                    color = textDark,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Sélectionnez le parcours correspondant à votre formation.",
                color = textGray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            StreamCard(
                accent = palette.accent,
                title = "Literary & Cultural Studies",
                subtitle = "Literature, culture and research",
                onClick = {
                    onStreamSelected("${semester}_LITERARY")
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            StreamCard(
                accent = palette.accent,
                title = "Linguistics & Cultural Studies",
                subtitle = "Linguistics, language and culture",
                onClick = {
                    onStreamSelected("${semester}_LINGUISTICS")
                }
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}


// =================================================================
// CARTE PARCOURS
// =================================================================

@Composable
fun StreamCard(
    accent: Color,
    title: String,
    subtitle: String,
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
            defaultElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(accent.copy(alpha = 0.10f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(13.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = Color(0xFF172554),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = subtitle,
                    color = Color(0xFF64748B),
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Ouvrir",
                tint = accent,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}


// PAGE D'UN MODULE
// =================================================================

@Composable
fun ModuleScreen(
    moduleName: String,
    onBack: () -> Unit,
    onLessonsClick: () -> Unit,
    onListeningClick: () -> Unit
) {
    val primary = Color(0xFF2563EB)
    val background = Color(0xFFF8FAFC)
    val textDark = Color(0xFF172033)
    val textGray = Color(0xFF64748B)

    Scaffold(
        containerColor = background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = moduleName,
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
                            contentDescription = "Retour",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(background)
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = primary)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(
                                    Color.White.copy(alpha = 0.15f),
                                    RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "📘", fontSize = 30.sp)
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = moduleName,
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Module universitaire",
                                color = Color.White.copy(alpha = 0.90f),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            ModuleOption(
                emoji = "📖",
                title = "Lessons",
                subtitle = "Cours et explications",
                onClick = onLessonsClick
            )

            Spacer(modifier = Modifier.height(10.dp))

            ModuleOption(
                emoji = "🎧",
                title = "Listening",
                subtitle = "Compréhension orale",
                onClick = onListeningClick
            )

            Spacer(modifier = Modifier.height(10.dp))

            ModuleOption(
                emoji = "🧠",
                title = "Vocabulary",
                subtitle = "Vocabulaire du module",
                onClick = {}
            )

            Spacer(modifier = Modifier.height(10.dp))

            ModuleOption(
                emoji = "✍️",
                title = "Exercises",
                subtitle = "Exercices pratiques",
                onClick = {}
            )

            Spacer(modifier = Modifier.height(10.dp))

            ModuleOption(
                emoji = "✅",
                title = "Quiz",
                subtitle = "Testez vos connaissances",
                onClick = {}
            )

            Spacer(modifier = Modifier.height(10.dp))

            ModuleOption(
                emoji = "📊",
                title = "Final Test",
                subtitle = "Évaluation du module",
                onClick = {}
            )
        }
    }
}



// =================================================================
// OPTION D'UN MODULE
// =================================================================

@Composable
fun ModuleOption(
    emoji: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    val primary = Color(0xFF2563EB)
    val lightBlue = Color(0xFFEFF6FF)
    val textDark = Color(0xFF172033)
    val textGray = Color(0xFF64748B)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
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
                    .size(52.dp)
                    .background(lightBlue, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 27.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = textDark,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    color = textGray,
                    fontSize = 13.sp
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = primary
            )
        }
    }
}



// =================================================================
// LISTE DES LEÇONS
// =================================================================

@Composable
fun LessonsScreen(
    moduleName: String,
    onBack: () -> Unit,
    onLessonClick: (String) -> Unit
) {

    val units = CourseData.grammar1Units

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        // =========================================================
        // RETOUR
        // =========================================================

        Text(
            text = "← Retour",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clickable {
                    onBack()
                }
                .padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // =========================================================
        // TITRE
        // =========================================================

        Text(
            text = moduleName,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Course Units",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(25.dp))

        // =========================================================
        // UNITÉS
        // =========================================================

        units.forEach { unit ->

            Text(
                text = unit.title,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = unit.description,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 21.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // =====================================================
            // LEÇONS DE L'UNITÉ
            // =====================================================

            unit.lessons.forEachIndexed { index, lesson ->

                val hasQuiz = lesson.questions.isNotEmpty()

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onLessonClick(lesson.title)
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor =
                            if (hasQuiz)
                                MaterialTheme.colorScheme.surfaceVariant
                            else
                                MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "📖",
                            fontSize = 28.sp
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = "Lesson ${index + 1}",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = lesson.title,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = if (hasQuiz) {
                                    "✅ Quiz available — ${lesson.questions.size} questions"
                                } else {
                                    "⚠️ No quiz available yet"
                                },
                                fontSize = 13.sp,
                                color =
                                    if (hasQuiz)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.error
                            )
                        }

                        Text(
                            text = "›",
                            fontSize = 30.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

// =================================================================
// QUIZ
// =================================================================
// =================================================================
// PAGE D'UNE LEÇON
// =================================================================

@Composable
fun LessonScreen(
    lessonName: String,
    onBack: () -> Unit,
    onQuizClick: () -> Unit
) {

    val lesson = CourseData.grammar1Units
        .flatMap { it.lessons }
        .find { it.title == lessonName }

    if (lesson == null) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {

            Text(
                text = "← Retour",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clickable {
                        onBack()
                    }
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Leçon introuvable",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Text(
            text = "← Retour",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clickable {
                    onBack()
                }
                .padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "📖",
            fontSize = 40.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = lesson.title,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "🎯 Learning Objective",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = lesson.objective,
            fontSize = 16.sp,
            lineHeight = 25.sp
        )

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "📚 Course",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {

            Text(
                text = lesson.content,
                modifier = Modifier.padding(18.dp),
                fontSize = 16.sp,
                lineHeight = 26.sp
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "💡 Examples",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        lesson.examples.forEach { example ->

            ExampleCard(
                english = example.english,
                french = example.french
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "🧠 Key Terms",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        lesson.keyTerms.forEach { term ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {

                Text(
                    text = "• $term",
                    modifier = Modifier.padding(12.dp),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(25.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onQuizClick()
                },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "✅",
                    fontSize = 28.sp
                )

                Spacer(modifier = Modifier.width(15.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Take the Quiz",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (lesson.questions.isNotEmpty()) {
                            "Test your understanding — ${lesson.questions.size} questions"
                        } else {
                            "No quiz available yet"
                        },
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Text(
                    text = "›",
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}


// =================================================================
// QUIZ
// =================================================================

@Composable
fun QuizScreen(
    lessonName: String,
    onBack: () -> Unit
) {

    val lesson = CourseData.grammar1Units
        .flatMap { it.lessons }
        .find { it.title == lessonName }

    if (lesson == null) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {

            Text(
                text = "← Retour",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clickable {
                        onBack()
                    }
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Quiz introuvable",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        return
    }

    val questions = lesson.questions

    if (questions.isEmpty()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {

            Text(
                text = "← Retour",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clickable {
                        onBack()
                    }
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = "No quiz available",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }

        return
    }

    var currentQuestionIndex by remember {
        mutableStateOf(0)
    }

    var selectedAnswerIndex by remember {
        mutableStateOf(-1)
    }

    var answerSubmitted by remember {
        mutableStateOf(false)
    }

    var score by remember {
        mutableStateOf(0)
    }

    var quizFinished by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current


    val progressDataStore = remember(context) {
        ProgressDataStore(context)
    }
    val bestScores by progressDataStore.bestScores
        .collectAsState(initial = emptyMap())
    // =============================================================
    // RÉSULTAT FINAL
    // =============================================================

    if (quizFinished) {

        LaunchedEffect(quizFinished) {
            progressDataStore.saveBestScore(
                quizId = lesson.id,
                score = score
            )

            progressDataStore.markLessonCompleted(
                lessonId = lesson.id
            )
        }
        val bestScore = maxOf(
            score,
            bestScores[lesson.id] ?: 0
        )

        val percentage = (score * 100) / questions.size

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "🎉",
                fontSize = 60.sp
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "Quiz Completed",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = "$score / ${questions.size}",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$percentage %",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Best Score: $bestScore / ${questions.size}",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(25.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Text(
                        text = "Correct answers: $score",
                        fontSize = 17.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Incorrect answers: ${questions.size - score}",
                        fontSize = 17.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onBack()
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {

                Text(
                    text = "Back to Lesson",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        return
    }

    // =============================================================
    // QUESTION ACTUELLE
    // =============================================================

    val question = questions[currentQuestionIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Text(
            text = "← Retour",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clickable {
                    onBack()
                }
                .padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = lesson.title,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Question ${currentQuestionIndex + 1} / ${questions.size}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {

            Text(
                text = question.question,
                modifier = Modifier.padding(20.dp),
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 28.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // =========================================================
        // RÉPONSES
        // =========================================================

        question.options.forEachIndexed { index, option ->

            val isSelected = selectedAnswerIndex == index
            val isCorrect = index == question.correctAnswerIndex

            val containerColor = when {

                // Bonne réponse après validation
                answerSubmitted && isCorrect ->
                    MaterialTheme.colorScheme.primaryContainer

                // Mauvaise réponse sélectionnée
                answerSubmitted && isSelected && !isCorrect ->
                    MaterialTheme.colorScheme.errorContainer

                // Réponse simplement sélectionnée
                isSelected ->
                    MaterialTheme.colorScheme.secondaryContainer

                else ->
                    MaterialTheme.colorScheme.surfaceVariant
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = !answerSubmitted) {
                        selectedAnswerIndex = index
                    },

                shape = RoundedCornerShape(14.dp),

                colors = CardDefaults.cardColors(
                    containerColor = containerColor
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),

                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = when (index) {
                            0 -> "A"
                            1 -> "B"
                            2 -> "C"
                            3 -> "D"
                            else -> ""
                        },

                        fontSize = 17.sp,

                        fontWeight = FontWeight.Bold,

                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.width(15.dp))

                    Text(
                        text = option,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }

        // =========================================================
        // CORRECTION
        // =========================================================

        if (answerSubmitted) {

            Spacer(modifier = Modifier.height(10.dp))

            val correct =
                selectedAnswerIndex == question.correctAnswerIndex

            Card(
                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(16.dp),

                colors = CardDefaults.cardColors(
                    containerColor =
                        if (correct)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.errorContainer
                )
            ) {

                Column(
                    modifier = Modifier.padding(18.dp)
                ) {

                    Text(
                        text =
                            if (correct)
                                "✅ Correct"
                            else
                                "❌ Incorrect",

                        fontSize = 19.sp,

                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text =
                            "Correct answer: " +
                                    question.options[question.correctAnswerIndex],

                        fontSize = 16.sp,

                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "💡 Explanation",

                        fontSize = 17.sp,

                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = question.explanation,

                        fontSize = 15.sp,

                        lineHeight = 24.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // =========================================================
        // BOUTON
        // =========================================================

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    enabled = selectedAnswerIndex != -1
                ) {

                    // Première pression :
                    // vérifier la réponse
                    if (!answerSubmitted) {

                        if (
                            selectedAnswerIndex ==
                            question.correctAnswerIndex
                        ) {
                            score++
                        }

                        answerSubmitted = true

                    } else {

                        // Deuxième pression :
                        // passer à la question suivante

                        if (
                            currentQuestionIndex <
                            questions.lastIndex
                        ) {

                            currentQuestionIndex++
                            selectedAnswerIndex = -1
                            answerSubmitted = false

                        } else {

                            quizFinished = true
                        }
                    }
                },

            shape = RoundedCornerShape(16.dp),

            colors = CardDefaults.cardColors(
                containerColor =
                    if (selectedAnswerIndex != -1)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surfaceVariant
            )
        ) {

            Text(
                text =
                    if (!answerSubmitted) {
                        "Check Answer"
                    } else if (
                        currentQuestionIndex ==
                        questions.lastIndex
                    ) {
                        "Finish Quiz"
                    } else {
                        "Next Question"
                    },

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),

                fontSize = 17.sp,

                fontWeight = FontWeight.Bold,

                color =
                    if (selectedAnswerIndex != -1)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}


// =================================================================
// CARTE EXEMPLE
// =================================================================

@Composable
fun ExampleCard(
    english: String,
    french: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(14.dp),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = english,

                fontSize = 17.sp,

                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = french,

                fontSize = 14.sp
            )
        }
    }
}
