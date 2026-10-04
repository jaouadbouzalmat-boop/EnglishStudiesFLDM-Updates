package ma.fldm.englishstudies
import ma.fldm.englishstudies.data.OliverTwistFullTextRepository
import android.content.Intent
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import ma.fldm.englishstudies.data.OliverTwistTranslationData
import ma.fldm.englishstudies.data.OliverTranslationService
import android.os.Build
import androidx.compose.material3.AssistChip
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.speech.tts.UtteranceProgressListener
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// =====================================================================
// STOCKAGE DE PROGRESSION OLIVER TWIST
// =====================================================================

private class OliverProgressStore(
    private val context: android.content.Context
) {
    private val prefs = context.getSharedPreferences(
        "oliver_twist_progress",
        android.content.Context.MODE_PRIVATE
    )

    fun isSceneCompleted(
        chapterIndex: Int,
        sceneIndex: Int
    ): Boolean {
        return prefs.getBoolean(
            "scene_completed_${chapterIndex}_${sceneIndex}",
            false
        )
    }

    fun markSceneCompleted(
        chapterIndex: Int,
        sceneIndex: Int
    ) {
        prefs.edit()
            .putBoolean(
                "scene_completed_${chapterIndex}_${sceneIndex}",
                true
            )
            .apply()
    }

    fun saveScore(
        chapterIndex: Int,
        sceneIndex: Int,
        score: Int
    ) {
        val key = "best_score_${chapterIndex}_${sceneIndex}"
        val previous = prefs.getInt(key, -1)
        if (score > previous) {
            prefs.edit()
                .putInt(key, score)
                .apply()
        }
    }

    fun getBestScore(
        chapterIndex: Int,
        sceneIndex: Int
    ): Int {
        return prefs.getInt(
            "best_score_${chapterIndex}_${sceneIndex}",
            -1
        )
    }
    // -------------------------------------------------------------
// SCORE DE LECTURE
// -------------------------------------------------------------

    fun saveReadingScore(
        chapterIndex: Int,
        sceneIndex: Int,
        score: Int
    ) {
        val key =
            "best_reading_score_${chapterIndex}_${sceneIndex}"

        val previous =
            prefs.getInt(
                key,
                -1
            )

        if (score > previous) {
            prefs.edit()
                .putInt(
                    key,
                    score
                )
                .apply()
        }
    }

    fun getBestReadingScore(
        chapterIndex: Int,
        sceneIndex: Int
    ): Int {
        return prefs.getInt(
            "best_reading_score_${chapterIndex}_${sceneIndex}",
            -1
        )
    }

    fun getBestRevisionScore(
        chapterIndex: Int,
        sceneIndex: Int
    ): Int {
        return prefs.getInt(
            "best_revision_score_${chapterIndex}_${sceneIndex}",
            -1
        )
    }

    fun saveBestRevisionScore(
        chapterIndex: Int,
        sceneIndex: Int,
        score: Int
    ) {
        val key =
            "best_revision_score_${chapterIndex}_${sceneIndex}"

        val previous =
            prefs.getInt(
                key,
                -1
            )

        if (score > previous) {
            prefs.edit()
                .putInt(
                    key,
                    score.coerceIn(0, 100)
                )
                .apply()
        }
    }

    fun saveReadingAttempt(
        chapterIndex: Int,
        sceneIndex: Int,
        score: Int
    ) {
        val key =
            "reading_history_${chapterIndex}_${sceneIndex}"

        val history = prefs.getString(key, "")
            ?.split(",")
            ?.mapNotNull { it.toIntOrNull() }
            ?.toMutableList()
            ?: mutableListOf()

        history.add(score.coerceIn(0, 100))

        val lastFive =
            history.takeLast(5)

        prefs.edit()
            .putString(key, lastFive.joinToString(","))
            .apply()
    }

    fun getReadingHistory(
        chapterIndex: Int,
        sceneIndex: Int
    ): List<Int> {
        val key =
            "reading_history_${chapterIndex}_${sceneIndex}"

        return prefs.getString(key, "")
            ?.split(",")
            ?.mapNotNull { it.toIntOrNull() }
            ?: emptyList()
    }

    fun saveSentenceReadingResult(
        chapterIndex: Int,
        sceneIndex: Int,
        sentenceIndex: Int,
        correctWords: List<String>,
        wrongWords: List<String>,
        totalWords: Int
    ) {
        val prefix =
            "sentence_${chapterIndex}_${sceneIndex}_${sentenceIndex}_"

        prefs.edit()
            .putInt(
                "${prefix}correct",
                correctWords.size
            )
            .putInt(
                "${prefix}total",
                totalWords
            )
            .putString(
                "${prefix}correct_words",
                correctWords.joinToString("|")
            )
            .putString(
                "${prefix}wrong_words",
                wrongWords.joinToString("|")
            )
            .apply()
    }

    fun getSentenceReadingResult(
        chapterIndex: Int,
        sceneIndex: Int,
        sentenceIndex: Int
    ): PersistedSentenceReadingResult? {
        val prefix =
            "sentence_${chapterIndex}_${sceneIndex}_${sentenceIndex}_"

        val total =
            prefs.getInt("${prefix}total", -1)

        if (total < 0) {
            return null
        }

        val correct =
            prefs.getInt("${prefix}correct", 0)

        val correctWords =
            prefs.getString("${prefix}correct_words", "")
                ?.split("|")
                ?.filter { it.isNotBlank() }
                ?: emptyList()

        val wrongWords =
            prefs.getString("${prefix}wrong_words", "")
                ?.split("|")
                ?.filter { it.isNotBlank() }
                ?: emptyList()

        return PersistedSentenceReadingResult(
            correctWords = correctWords,
            wrongWords = wrongWords,
            correct = correct,
            total = total
        )
    }

    fun markSentenceMastered(
        chapterIndex: Int,
        sceneIndex: Int,
        sentenceIndex: Int
    ) {
        prefs.edit()
            .putBoolean(
                "sentence_mastered_${chapterIndex}_${sceneIndex}_${sentenceIndex}",
                true
            )
            .apply()
    }

    fun isSentenceMastered(
        chapterIndex: Int,
        sceneIndex: Int,
        sentenceIndex: Int
    ): Boolean {
        return prefs.getBoolean(
            "sentence_mastered_${chapterIndex}_${sceneIndex}_${sentenceIndex}",
            false
        )
    }

    // -------------------------------------------------------------
    // MÉMORISATION DES ERREURS PAR MOT
    // -------------------------------------------------------------

    private fun normalizeErrorWord(word: String): String {
        return word
            .lowercase(java.util.Locale.US)
            .replace("’", "'")
            .replace(Regex("[^a-zA-Z']"), "")
            .trim()
    }

    fun incrementSceneWordErrors(
        chapterIndex: Int,
        sceneIndex: Int,
        words: List<String>
    ) {
        val editor = prefs.edit()

        words
            .map(::normalizeErrorWord)
            .filter { it.isNotBlank() }
            .distinct()
            .forEach { word ->
                val key =
                    "scene_word_error_${chapterIndex}_${sceneIndex}_${word}"
                val current = prefs.getInt(key, 0)
                editor.putInt(key, current + 1)
            }

        editor.apply()
    }

    fun getSceneWordErrorCounts(
        chapterIndex: Int,
        sceneIndex: Int
    ): Map<String, Int> {
        val prefix =
            "scene_word_error_${chapterIndex}_${sceneIndex}_"

        return prefs.all
            .filterKeys { it.startsWith(prefix) }
            .mapNotNull { (key, value) ->
                val word = key.removePrefix(prefix)
                val count = value as? Int
                if (word.isNotBlank() && count != null && count > 0) {
                    word to count
                } else {
                    null
                }
            }
            .toMap()
    }

    // -------------------------------------------------------------
    // PROGRESSION DU PLAN DE RÉVISION PERSONNALISÉ
    // -------------------------------------------------------------

    private fun normalizeRevisionPlanWord(word: String): String {
        return word
            .lowercase(java.util.Locale.US)
            .replace("’", "'")
            .replace(Regex("[^a-zA-Z']"), "")
            .trim()
    }

    fun saveRevisionPlanProgress(
        chapterIndex: Int,
        sceneIndex: Int,
        practicedWords: Set<String>
    ) {
        val key = "revision_plan_practiced_${chapterIndex}_${sceneIndex}"
        val normalized = practicedWords
            .map(::normalizeRevisionPlanWord)
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
        prefs.edit()
            .putString(key, normalized.joinToString("|"))
            .apply()
    }

    fun getRevisionPlanProgress(
        chapterIndex: Int,
        sceneIndex: Int
    ): Set<String> {
        val key = "revision_plan_practiced_${chapterIndex}_${sceneIndex}"
        return prefs.getString(key, "")
            ?.split("|")
            ?.map(::normalizeRevisionPlanWord)
            ?.filter { it.isNotBlank() }
            ?.toSet()
            ?: emptySet()
    }

    fun saveRevisionPlanCurrentWord(
        chapterIndex: Int,
        sceneIndex: Int,
        word: String?
    ) {
        val key = "revision_plan_current_${chapterIndex}_${sceneIndex}"
        prefs.edit()
            .putString(
                key,
                word?.let(::normalizeRevisionPlanWord)?.takeIf { it.isNotBlank() } ?: ""
            )
            .apply()
    }

    fun getRevisionPlanCurrentWord(
        chapterIndex: Int,
        sceneIndex: Int
    ): String {
        return prefs.getString(
            "revision_plan_current_${chapterIndex}_${sceneIndex}",
            ""
        ) ?: ""
    }
}

// =====================================================================
// MODÈLES
// =====================================================================

data class OliverVocabulary(
    val word: String,
    val pronunciation: String,
    val definition: String,
    val french: String,
    val arabic: String,
    val example: String
)

data class OliverQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class OliverScene(
    val title: String,
    val imageEmoji: String,
    val englishText: String,
    val comprehension: List<String>,
    val frenchTranslation: String,
    val arabicExplanation: String,
    val vocabulary: List<OliverVocabulary>,
    val questions: List<OliverQuestion>,
    val audioResId: Int? = null
)

data class OliverChapter(
    val number: Int,
    val title: String,
    val subtitle: String,
    val summary: String,
    val scenes: List<OliverScene>,
    val expanded: Boolean = false
)

// =====================================================================
// DONNÉES OLIVER TWIST
// =====================================================================

object OliverTwistData {

    val chapters = listOf(
        makeChapter(
            number = 1,
            title = "Oliver is born in a workhouse",
            summary = "Oliver naît dans une workhouse froide et pauvre. Sa mère meurt peu après sa naissance et il grandit sans père ni famille.",
            sceneTitle = "A cold morning",
            imageEmoji = "🖼️",
            englishText = """
Oliver Twist was born in a workhouse. His mother was very young, and she died soon after his birth. Oliver had no father and no family. The workhouse was cold, and the food was very simple.
""".trimIndent(),
            frenchTranslation = """
Oliver Twist est né dans une maison de travail pour les pauvres. Sa mère était très jeune et elle est morte peu après sa naissance. Oliver n'avait ni père ni famille. La maison de travail était froide et la nourriture était très simple.
""".trimIndent(),
            arabicExplanation = """
وُلِد أوليفر تويست في دار لإيواء الفقراء والعمل. كانت والدته شابة جداً وتوفيت بعد وقت قصير من ولادته. لم يكن لأوليفر أب ولا أسرة. كانت دار العمل باردة وكان الطعام بسيطاً جداً.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "workhouse",
                    pronunciation = "/ˈwɜːrkhaʊs/",
                    definition = "A place where poor people lived and worked.",
                    french = "maison de travail pour les pauvres",
                    arabic = "دار لإيواء وعمل الفقراء",
                    example = "Oliver was born in a workhouse."
                ),
                OliverVocabulary(
                    word = "poor",
                    pronunciation = "/pʊr/",
                    definition = "Having little money or few possessions.",
                    french = "pauvre",
                    arabic = "فقير",
                    example = "Oliver was a poor boy."
                ),
                OliverVocabulary(
                    word = "birth",
                    pronunciation = "/bɜːrθ/",
                    definition = "The moment when a baby is born.",
                    french = "naissance",
                    arabic = "ولادة",
                    example = "His mother died after his birth."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Where was Oliver born?",
                    options = listOf(
                        "In a school",
                        "In a workhouse",
                        "In a rich house"
                    ),
                    correctIndex = 1,
                    explanation = "Oliver was born in a workhouse."
                ),
                OliverQuestion(
                    question = "What happened to his mother?",
                    options = listOf(
                        "She became rich.",
                        "She left England.",
                        "She died soon after his birth."
                    ),
                    correctIndex = 2,
                    explanation = "His mother died soon after his birth."
                )
            )
        ),
        makeChapter(
            number = 2,
            title = "Oliver asks for more food",
            summary = "À neuf ans, Oliver retourne à la workhouse où les garçons souffrent de la faim. Poussé par les autres, il demande courageusement davantage de nourriture.",
            sceneTitle = "More food",
            imageEmoji = "🍲",
            englishText = """
Oliver grows up in a harsh environment. On his ninth birthday he returns to the workhouse. The boys are always hungry, and they decide that Oliver should ask for more food. Oliver is frightened, but he still speaks.
""".trimIndent(),
            frenchTranslation = """
Oliver grandit dans un environnement difficile. Pour son neuvième anniversaire, il retourne à la maison de travail. Les garçons ont toujours faim et ils décident qu'Oliver doit demander davantage de nourriture. Oliver a peur, mais il ose parler.
""".trimIndent(),
            arabicExplanation = """
نشأ أوليفر في ظروف قاسية. في عيد ميلاده التاسع عاد إلى دار العمل. كان الصبيان دائماً جائعين وقرروا أن يطلب أوليفر المزيد من الطعام. كان خائفاً، لكنه تجرأ على الكلام.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "harsh",
                    pronunciation = "/hɑːrʃ/",
                    definition = "Very unpleasant or severe.",
                    french = "dur",
                    arabic = "قاسٍ",
                    example = "The workhouse life was harsh."
                ),
                OliverVocabulary(
                    word = "hungry",
                    pronunciation = "/ˈhʌŋɡri/",
                    definition = "Needing or wanting food.",
                    french = "avoir faim",
                    arabic = "جائع",
                    example = "The boys were very hungry."
                ),
                OliverVocabulary(
                    word = "ask",
                    pronunciation = "/æsk/",
                    definition = "To request something.",
                    french = "demander",
                    arabic = "يطلب",
                    example = "Oliver asked for more food."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Why did Oliver ask for more food?",
                    options = listOf(
                        "He was very hungry.",
                        "He wanted a new coat.",
                        "He wanted to leave London."
                    ),
                    correctIndex = 0,
                    explanation = "Oliver was very hungry."
                ),
                OliverQuestion(
                    question = "How did Oliver feel?",
                    options = listOf(
                        "Excited",
                        "Afraid",
                        "Angry"
                    ),
                    correctIndex = 1,
                    explanation = "He was afraid but he still spoke."
                )
            )
        ),
        makeChapter(
            number = 3,
            title = "Oliver refuses an apprenticeship",
            summary = "Après sa demande de nourriture, Oliver est considéré comme un problème et on veut le placer chez un ramoneur. Il supplie de ne pas y être envoyé et l’apprentissage est finalement arrêté.",
            sceneTitle = "A dangerous offer",
            imageEmoji = "🧱",
            englishText = """
After asking for more food, Oliver is treated as a problem by the authorities. He is offered to a chimney sweep, a dangerous job for a child. Oliver begs not to be sent there. The apprenticeship is stopped.
""".trimIndent(),
            frenchTranslation = """
Après avoir demandé davantage de nourriture, Oliver est considéré comme un problème. On veut le placer chez un ramoneur, un métier dangereux pour un enfant. Oliver supplie qu'on ne l'envoie pas là-bas. L'apprentissage est finalement refusé.
""".trimIndent(),
            arabicExplanation = """
بعد أن طلب المزيد من الطعام عومل أوليفر كأنه مشكلة. عُرض عليه أن يعمل لدى منظف مداخن، وهي مهنة خطيرة على طفل. توسّل أوليفر ألا يُرسل إلى هناك، وتم إلغاء الصفقة.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "apprentice",
                    pronunciation = "/əˈprentɪs/",
                    definition = "A person learning a trade.",
                    french = "apprenti",
                    arabic = "متدرّب",
                    example = "Oliver was offered an apprenticeship."
                ),
                OliverVocabulary(
                    word = "chimney",
                    pronunciation = "/ˈtʃɪmni/",
                    definition = "The part of a building through which smoke escapes.",
                    french = "cheminée",
                    arabic = "مدخنة",
                    example = "The chimney was narrow."
                ),
                OliverVocabulary(
                    word = "beg",
                    pronunciation = "/beɡ/",
                    definition = "To ask strongly or desperately.",
                    french = "supplier",
                    arabic = "يتوسّل",
                    example = "Oliver begged the magistrate."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "What job was offered to Oliver?",
                    options = listOf(
                        "Teacher",
                        "Chimney sweep",
                        "Shopkeeper"
                    ),
                    correctIndex = 1,
                    explanation = "A chimney-sweep apprenticeship was offered."
                ),
                OliverQuestion(
                    question = "What did Oliver do?",
                    options = listOf(
                        "He begged for help.",
                        "He ran to France.",
                        "He accepted immediately."
                    ),
                    correctIndex = 0,
                    explanation = "Oliver begged not to be sent away."
                )
            )
        ),
        makeChapter(
            number = 4,
            title = "Oliver becomes an undertaker's apprentice",
            summary = "Oliver est envoyé chez M. Sowerberry, entrepreneur de pompes funèbres. Il reçoit peu de nourriture, est mal traité et découvre une vie difficile au service des funérailles.",
            sceneTitle = "A new master",
            imageEmoji = "⚰️",
            englishText = """
Oliver is sent to work for the undertaker Mr. Sowerberry. He receives little food and is treated as an inferior servant. He helps with funerals and sees a new side of life in the parish. His position is difficult, but he tries to work.
""".trimIndent(),
            frenchTranslation = """
Oliver est envoyé travailler chez l'entrepreneur de pompes funèbres M. Sowerberry. Il reçoit peu de nourriture et est traité comme un serviteur inférieur. Il aide aux funérailles et découvre un autre aspect de la vie dans la paroisse. Sa situation est difficile, mais il essaie de travailler.
""".trimIndent(),
            arabicExplanation = """
أُرسل أوليفر للعمل عند متعهد دفن الموتى السيد سويربيري. كان يحصل على القليل من الطعام ويُعامل كخادم أدنى. ساعد في الجنازات واكتشف جانباً آخر من حياة الرعية. كانت ظروفه صعبة لكنه حاول العمل.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "undertaker",
                    pronunciation = "/ˈʌndərteɪkər/",
                    definition = "A person who arranges funerals.",
                    french = "entrepreneur de pompes funèbres",
                    arabic = "متعهد دفن الموتى",
                    example = "The undertaker gave Oliver work."
                ),
                OliverVocabulary(
                    word = "funeral",
                    pronunciation = "/ˈfjuːnərəl/",
                    definition = "A ceremony for a dead person.",
                    french = "funérailles",
                    arabic = "جنازة",
                    example = "Oliver attended a funeral."
                ),
                OliverVocabulary(
                    word = "servant",
                    pronunciation = "/ˈsɜːrvənt/",
                    definition = "A person who works for another person.",
                    french = "serviteur",
                    arabic = "خادم",
                    example = "Oliver was treated like a servant."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Who employs Oliver?",
                    options = listOf(
                        "Mr. Brownlow",
                        "Mr. Sowerberry",
                        "Fagin"
                    ),
                    correctIndex = 1,
                    explanation = "Mr. Sowerberry employs Oliver."
                ),
                OliverQuestion(
                    question = "What does Oliver help with?",
                    options = listOf(
                        "Funerals",
                        "Ships",
                        "Schools"
                    ),
                    correctIndex = 0,
                    explanation = "He helps with funerals."
                )
            )
        ),
        makeChapter(
            number = 5,
            title = "Oliver meets new associates",
            summary = "Chez les Sowerberry, Oliver rencontre Noah Claypole et Charlotte, mais Noah le maltraite. Une cérémonie funèbre lui fait découvrir un autre aspect du métier de son maître.",
            sceneTitle = "A funeral and a new life",
            imageEmoji = "👦",
            englishText = """
Oliver works with the Sowerberry family and meets Noah Claypole and Charlotte. Noah mocks Oliver and treats him badly. Oliver begins to understand that cruel people may be close to him. A funeral gives him a troubling view of his master's business.
""".trimIndent(),
            frenchTranslation = """
Oliver travaille avec la famille Sowerberry et rencontre Noah Claypole et Charlotte. Noah se moque de lui et le traite mal. Oliver comprend peu à peu que des personnes cruelles peuvent être proches de lui. Une cérémonie funèbre lui donne une vision troublante du métier de son maître.
""".trimIndent(),
            arabicExplanation = """
يعمل أوليفر مع عائلة سويربيري ويلتقي نواه كلايبول وشارلوت. كان نواه يسخر منه ويعامله بقسوة. بدأ أوليفر يفهم أن الأشخاص القساة قد يكونون قريبين منه. وأعطته جنازة نظرة مقلقة عن مهنة سيده.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "mock",
                    pronunciation = "/mɒk/",
                    definition = "To laugh at or make fun of someone.",
                    french = "se moquer",
                    arabic = "يسخر",
                    example = "Noah mocked Oliver."
                ),
                OliverVocabulary(
                    word = "cruel",
                    pronunciation = "/ˈkruːəl/",
                    definition = "Causing pain and not caring.",
                    french = "cruel",
                    arabic = "قاسٍ",
                    example = "Noah was cruel to Oliver."
                ),
                OliverVocabulary(
                    word = "funeral",
                    pronunciation = "/ˈfjuːnərəl/",
                    definition = "A ceremony after someone's death.",
                    french = "funérailles",
                    arabic = "جنازة",
                    example = "The funeral was serious."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Who treats Oliver badly?",
                    options = listOf(
                        "Noah Claypole",
                        "Mr. Brownlow",
                        "Harry Maylie"
                    ),
                    correctIndex = 0,
                    explanation = "Noah Claypole mocks Oliver."
                ),
                OliverQuestion(
                    question = "What does Oliver see for the first time?",
                    options = listOf(
                        "A funeral",
                        "A battlefield",
                        "A theatre"
                    ),
                    correctIndex = 0,
                    explanation = "He sees a funeral through his master's work."
                )
            )
        ),
        makeChapter(
            number = 6,
            title = "Oliver fights Noah",
            summary = "Noah insulte la mère d’Oliver et sa famille. Oliver, habituellement calme, se met en colère et se défend pour la première fois avec force.",
            sceneTitle = "Oliver loses control",
            imageEmoji = "😠",
            englishText = """
Noah insults Oliver's mother and speaks cruelly about his family. Oliver becomes angry and attacks him. The household is shocked by Oliver's resistance. For once, the quiet boy refuses to accept humiliation.
""".trimIndent(),
            frenchTranslation = """
Noah insulte la mère d'Oliver et parle cruellement de sa famille. Oliver se met en colère et l'attaque. La maison est choquée par cette résistance. Pour une fois, le garçon silencieux refuse d'accepter l'humiliation.
""".trimIndent(),
            arabicExplanation = """
أهان نواه أم أوليفر وتحدث بقسوة عن عائلته. غضب أوليفر وهاجمه. صُدمت الأسرة من مقاومة أوليفر. ولأول مرة رفض الصبي الهادئ أن يقبل الإهانة.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "insult",
                    pronunciation = "/ɪnˈsʌlt/",
                    definition = "To speak rudely and disrespectfully.",
                    french = "insulter",
                    arabic = "يهين",
                    example = "Noah insulted Oliver's mother."
                ),
                OliverVocabulary(
                    word = "humiliation",
                    pronunciation = "/hjuːˌmɪliˈeɪʃən/",
                    definition = "A painful loss of dignity.",
                    french = "humiliation",
                    arabic = "إهانة",
                    example = "Oliver feels humiliation."
                ),
                OliverVocabulary(
                    word = "resist",
                    pronunciation = "/rɪˈzɪst/",
                    definition = "To refuse to accept or obey.",
                    french = "résister",
                    arabic = "يقاوم",
                    example = "Oliver resisted Noah."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Why does Oliver attack Noah?",
                    options = listOf(
                        "Noah insults his mother.",
                        "Noah gives him food.",
                        "Noah helps him escape."
                    ),
                    correctIndex = 0,
                    explanation = "Noah insults Oliver's mother."
                ),
                OliverQuestion(
                    question = "What changes in Oliver?",
                    options = listOf(
                        "He becomes violent and loud for a moment.",
                        "He becomes rich.",
                        "He leaves for school."
                    ),
                    correctIndex = 0,
                    explanation = "He refuses to accept the insult."
                )
            )
        ),
        makeChapter(
            number = 7,
            title = "Oliver continues to rebel",
            summary = "Après la bagarre, Oliver est puni et accusé. Il est renvoyé vers les autorités de la paroisse et se retrouve seul, sans endroit où se sentir en sécurité.",
            sceneTitle = "Punishment",
            imageEmoji = "⛓️",
            englishText = """
After the fight, Oliver is punished and blamed. The adults decide that he is difficult and must be controlled. Oliver is sent back to the parish authorities. He feels alone and sees no safe place for himself.
""".trimIndent(),
            frenchTranslation = """
Après la bagarre, Oliver est puni et accusé. Les adultes décident qu'il est difficile et qu'il faut le contrôler. Oliver est renvoyé aux autorités de la paroisse. Il se sent seul et ne voit aucun endroit sûr pour lui.
""".trimIndent(),
            arabicExplanation = """
بعد الشجار عوقب أوليفر وألقي عليه اللوم. قرر الكبار أنه صعب ويجب السيطرة عليه. أُعيد إلى سلطات الرعية. شعر بالوحدة ولم يجد مكاناً آمناً لنفسه.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "punish",
                    pronunciation = "/ˈpʌnɪʃ/",
                    definition = "To make someone suffer for wrongdoing.",
                    french = "punir",
                    arabic = "يعاقب",
                    example = "Oliver is punished."
                ),
                OliverVocabulary(
                    word = "blame",
                    pronunciation = "/bleɪm/",
                    definition = "To say someone is responsible for a problem.",
                    french = "accuser",
                    arabic = "يلوم",
                    example = "The adults blame Oliver."
                ),
                OliverVocabulary(
                    word = "alone",
                    pronunciation = "/əˈloʊn/",
                    definition = "Without other people.",
                    french = "seul",
                    arabic = "وحيد",
                    example = "Oliver feels alone."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "What happens after the fight?",
                    options = listOf(
                        "Oliver is punished.",
                        "Oliver is rewarded.",
                        "Oliver goes to university."
                    ),
                    correctIndex = 0,
                    explanation = "Oliver is punished."
                ),
                OliverQuestion(
                    question = "How does Oliver feel?",
                    options = listOf(
                        "Safe",
                        "Alone",
                        "Proud of the workhouse"
                    ),
                    correctIndex = 1,
                    explanation = "He feels alone and unwanted."
                )
            )
        ),
        makeChapter(
            number = 8,
            title = "Oliver walks to London",
            summary = "Oliver s’enfuit et marche vers Londres. Il rencontre l’Artful Dodger, qui lui offre de la nourriture et un endroit où rester sans révéler les liens criminels de son groupe.",
            sceneTitle = "Meeting the Artful Dodger",
            imageEmoji = "🚶",
            englishText = """
Oliver runs away and walks towards London. On the road he meets a boy called the Artful Dodger. The Dodger offers him food and a place to stay. Oliver follows him, not knowing that the boy is connected to a criminal group.
""".trimIndent(),
            frenchTranslation = """
Oliver s'enfuit et marche vers Londres. Sur la route, il rencontre un garçon appelé l'Artful Dodger. Le garçon lui propose de la nourriture et un endroit où dormir. Oliver le suit sans savoir qu'il est lié à un groupe criminel.
""".trimIndent(),
            arabicExplanation = """
هرب أوليفر ومشى نحو لندن. في الطريق التقى فتى يُدعى دودجر الماكر. عرض عليه الطعام ومكاناً للنوم. تبعه أوليفر دون أن يعرف أنه مرتبط بجماعة إجرامية.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "escape",
                    pronunciation = "/ɪˈskeɪp/",
                    definition = "To get away from a place or danger.",
                    french = "s'échapper",
                    arabic = "يهرب",
                    example = "Oliver escapes."
                ),
                OliverVocabulary(
                    word = "road",
                    pronunciation = "/roʊd/",
                    definition = "A way between places.",
                    french = "route",
                    arabic = "طريق",
                    example = "Oliver walks along the road."
                ),
                OliverVocabulary(
                    word = "criminal",
                    pronunciation = "/ˈkrɪmɪnəl/",
                    definition = "A person involved in crime.",
                    french = "criminel",
                    arabic = "مجرم",
                    example = "The Dodger belongs to a criminal group."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Where is Oliver going?",
                    options = listOf(
                        "London",
                        "Paris",
                        "Oxford"
                    ),
                    correctIndex = 0,
                    explanation = "Oliver walks to London."
                ),
                OliverQuestion(
                    question = "Who does he meet?",
                    options = listOf(
                        "The Artful Dodger",
                        "Mr. Brownlow",
                        "Mr. Grimwig"
                    ),
                    correctIndex = 0,
                    explanation = "He meets the Artful Dodger."
                )
            )
        ),
        makeChapter(
            number = 9,
            title = "Oliver meets Fagin",
            summary = "Le Dodger conduit Oliver chez Fagin, qui dirige un groupe de jeunes voleurs. Oliver croit avoir trouvé des amis sans comprendre les véritables intentions de Fagin.",
            sceneTitle = "The old gentleman",
            imageEmoji = "🕴️",
            englishText = """
The Dodger takes Oliver to Fagin, an old man who controls a group of young thieves. Fagin welcomes Oliver and hides his true intentions. Oliver sees many boys living together in secret. He believes he has found friends.
""".trimIndent(),
            frenchTranslation = """
Le Dodger emmène Oliver chez Fagin, un vieil homme qui dirige un groupe de jeunes voleurs. Fagin accueille Oliver tout en cachant ses véritables intentions. Oliver voit plusieurs garçons vivant ensemble en secret. Il croit avoir trouvé des amis.
""".trimIndent(),
            arabicExplanation = """
أخذ دودجر أوليفر إلى فاجن، وهو رجل عجوز يقود مجموعة من اللصوص الصغار. رحب فاجن بأوليفر وأخفى نواياه الحقيقية. رأى أوليفر عدة فتيان يعيشون معاً في السر. وظن أنه وجد أصدقاء.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "thief",
                    pronunciation = "/θiːf/",
                    definition = "A person who steals.",
                    french = "voleur",
                    arabic = "لص",
                    example = "The boys are thieves."
                ),
                OliverVocabulary(
                    word = "secret",
                    pronunciation = "/ˈsiːkrət/",
                    definition = "Something kept hidden.",
                    french = "secret",
                    arabic = "سر",
                    example = "Fagin keeps his plans secret."
                ),
                OliverVocabulary(
                    word = "leader",
                    pronunciation = "/ˈliːdər/",
                    definition = "A person who directs others.",
                    french = "chef",
                    arabic = "قائد",
                    example = "Fagin is the group's leader."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Who leads the boys?",
                    options = listOf(
                        "Fagin",
                        "Brownlow",
                        "Sowerberry"
                    ),
                    correctIndex = 0,
                    explanation = "Fagin controls the group."
                ),
                OliverQuestion(
                    question = "What does Oliver believe?",
                    options = listOf(
                        "He has found friends.",
                        "He is at school.",
                        "He is going home."
                    ),
                    correctIndex = 0,
                    explanation = "Oliver thinks he has found friends."
                )
            )
        ),
        makeChapter(
            number = 10,
            title = "Oliver learns the thieves' trade",
            summary = "Oliver découvre progressivement que les garçons de Fagin volent dans les rues. Lorsqu’un vol tourne mal, il s’enfuit et est arrêté.",
            sceneTitle = "The high price of experience",
            imageEmoji = "👜",
            englishText = """
Oliver watches the boys steal from people in the streets. At first he does not understand what they are doing. When he joins them, he discovers the truth. Oliver runs when a robbery goes wrong and is arrested.
""".trimIndent(),
            frenchTranslation = """
Oliver observe les garçons voler dans les rues. Au début, il ne comprend pas ce qu'ils font. Lorsqu'il les accompagne, il découvre la vérité. Oliver s'enfuit quand un vol tourne mal et il est arrêté.
""".trimIndent(),
            arabicExplanation = """
راقب أوليفر الفتيان وهم يسرقون الناس في الشوارع. في البداية لم يفهم ما يفعلونه. عندما رافقهم اكتشف الحقيقة. هرب أوليفر عندما فشلت السرقة وتم القبض عليه.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "steal",
                    pronunciation = "/stiːl/",
                    definition = "To take something without permission.",
                    french = "voler",
                    arabic = "يسرق",
                    example = "The boys steal from people."
                ),
                OliverVocabulary(
                    word = "robbery",
                    pronunciation = "/ˈrɒbəri/",
                    definition = "The act of taking property by force or crime.",
                    french = "vol",
                    arabic = "سرقة",
                    example = "The robbery goes wrong."
                ),
                OliverVocabulary(
                    word = "arrest",
                    pronunciation = "/əˈrest/",
                    definition = "To take someone into official custody.",
                    french = "arrêter",
                    arabic = "يعتقل",
                    example = "Oliver is arrested."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "What are the boys doing?",
                    options = listOf(
                        "Stealing",
                        "Studying",
                        "Working honestly"
                    ),
                    correctIndex = 0,
                    explanation = "They are stealing."
                ),
                OliverQuestion(
                    question = "What happens to Oliver?",
                    options = listOf(
                        "He is arrested.",
                        "He becomes Fagin's leader.",
                        "He wins money."
                    ),
                    correctIndex = 0,
                    explanation = "Oliver is arrested."
                )
            )
        ),
        makeChapter(
            number = 11,
            title = "Oliver meets Mr. Fang",
            summary = "Oliver comparaît devant le magistrat M. Fang, malade et effrayé. Il ne peut pas bien se défendre et sa situation change lorsque la véritable victime apparaît.",
            sceneTitle = "A harsh magistrate",
            imageEmoji = "⚖️",
            englishText = """
Oliver is brought before Mr. Fang, a police magistrate. He is sick, frightened, and unable to explain himself clearly. The court is severe and impatient. Oliver's situation changes when the real victim appears.
""".trimIndent(),
            frenchTranslation = """
Oliver est conduit devant M. Fang, un magistrat de police. Il est malade, effrayé et incapable de s'expliquer clairement. Le tribunal est sévère et impatient. La situation d'Oliver change lorsque la véritable victime apparaît.
""".trimIndent(),
            arabicExplanation = """
أُحضر أوليفر أمام القاضي فانغ. كان مريضاً وخائفاً وغير قادر على شرح موقفه بوضوح. كانت المحكمة قاسية وغير صبورة. تغير وضع أوليفر عندما ظهر الضحية الحقيقي.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "magistrate",
                    pronunciation = "/ˈmædʒɪstreɪt/",
                    definition = "A judicial officer who deals with minor cases.",
                    french = "magistrat",
                    arabic = "قاضٍ",
                    example = "Oliver appears before the magistrate."
                ),
                OliverVocabulary(
                    word = "court",
                    pronunciation = "/kɔːrt/",
                    definition = "A place where legal cases are heard.",
                    french = "tribunal",
                    arabic = "محكمة",
                    example = "The court is crowded."
                ),
                OliverVocabulary(
                    word = "victim",
                    pronunciation = "/ˈvɪktɪm/",
                    definition = "A person harmed by a crime.",
                    french = "victime",
                    arabic = "ضحية",
                    example = "The real victim speaks."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Who is Mr. Fang?",
                    options = listOf(
                        "A magistrate",
                        "A teacher",
                        "A doctor"
                    ),
                    correctIndex = 0,
                    explanation = "He is a police magistrate."
                ),
                OliverQuestion(
                    question = "How does Oliver feel?",
                    options = listOf(
                        "Confident",
                        "Frightened",
                        "Excited"
                    ),
                    correctIndex = 1,
                    explanation = "Oliver is frightened."
                )
            )
        ),
        makeChapter(
            number = 12,
            title = "Oliver is taken in",
            summary = "Oliver est accueilli chez M. Brownlow. Mme Bedwin lui donne du repos, de la nourriture et de la bienveillance, et Oliver découvre pour la première fois une vraie sécurité.",
            sceneTitle = "A better place",
            imageEmoji = "🏠",
            englishText = """
Oliver is taken to Mr. Brownlow's home. Mrs. Bedwin cares for him and gives him a safe bed and food. Oliver slowly recovers from his illness. For the first time, he experiences kindness without being asked for anything in return.
""".trimIndent(),
            frenchTranslation = """
Oliver est accueilli chez M. Brownlow. Mme Bedwin s'occupe de lui et lui donne un lit sûr et de la nourriture. Oliver se remet peu à peu de sa maladie. Pour la première fois, il découvre une bonté sans contrepartie.
""".trimIndent(),
            arabicExplanation = """
استقبل السيد براونلو أوليفر في منزله. اعتنت به السيدة بدوين وقدمت له سريراً آمناً وطعاماً. بدأ أوليفر يتعافى من مرضه. ولأول مرة عاش لطفاً لا يطلب منه شيئاً في المقابل.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "recover",
                    pronunciation = "/rɪˈkʌvər/",
                    definition = "To become well after illness.",
                    french = "se rétablir",
                    arabic = "يتعافى",
                    example = "Oliver begins to recover."
                ),
                OliverVocabulary(
                    word = "kindness",
                    pronunciation = "/ˈkaɪndnəs/",
                    definition = "Friendly and caring behavior.",
                    french = "gentillesse",
                    arabic = "لطف",
                    example = "Mrs. Bedwin shows kindness."
                ),
                OliverVocabulary(
                    word = "safe",
                    pronunciation = "/seɪf/",
                    definition = "Protected from danger.",
                    french = "en sécurité",
                    arabic = "آمن",
                    example = "Oliver feels safe."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Who cares for Oliver?",
                    options = listOf(
                        "Mrs. Bedwin",
                        "Nancy",
                        "Mrs. Mann"
                    ),
                    correctIndex = 0,
                    explanation = "Mrs. Bedwin cares for him."
                ),
                OliverQuestion(
                    question = "What does Oliver receive?",
                    options = listOf(
                        "A safe home and food",
                        "A prison sentence",
                        "A new job at the workhouse"
                    ),
                    correctIndex = 0,
                    explanation = "He receives care and food."
                )
            )
        ),
        makeChapter(
            number = 13,
            title = "New acquaintances appear",
            summary = "Oliver rencontre d’autres personnes liées à M. Brownlow tandis que Fagin et ses compagnons craignent qu’il ne les dénonce. Les deux mondes qui entourent Oliver commencent à se rapprocher.",
            sceneTitle = "Brownlow's circle",
            imageEmoji = "👥",
            englishText = """
Oliver meets more people connected with Mr. Brownlow and hears stories about his own appearance. At the same time, Fagin and his companions worry that Oliver may reveal them. The two worlds around Oliver begin to collide.
""".trimIndent(),
            frenchTranslation = """
Oliver rencontre d'autres personnes liées à M. Brownlow et entend des histoires sur son apparence. Pendant ce temps, Fagin et ses complices craignent qu'Oliver ne les révèle. Les deux mondes qui entourent Oliver commencent à se croiser.
""".trimIndent(),
            arabicExplanation = """
التقى أوليفر بأشخاص آخرين مرتبطين بالسيد براونلو وسمع قصصاً عن مظهره. وفي الوقت نفسه خشي فاجن ورفاقه أن يكشف أوليفر أمرهم. بدأ العالمان المحيطان بأوليفر يلتقيان.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "acquaintance",
                    pronunciation = "/əˈkweɪntəns/",
                    definition = "A person you know slightly.",
                    french = "connaissance",
                    arabic = "معرفة",
                    example = "Oliver meets new acquaintances."
                ),
                OliverVocabulary(
                    word = "appearance",
                    pronunciation = "/əˈpɪərəns/",
                    definition = "The way someone looks.",
                    french = "apparence",
                    arabic = "مظهر",
                    example = "His appearance attracts attention."
                ),
                OliverVocabulary(
                    word = "reveal",
                    pronunciation = "/rɪˈviːl/",
                    definition = "To make something known.",
                    french = "révéler",
                    arabic = "يكشف",
                    example = "Fagin fears Oliver may reveal the truth."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "What worries Fagin?",
                    options = listOf(
                        "Oliver may reveal the truth.",
                        "Oliver will become a doctor.",
                        "Oliver will leave England forever."
                    ),
                    correctIndex = 0,
                    explanation = "Fagin fears exposure."
                ),
                OliverQuestion(
                    question = "What changes?",
                    options = listOf(
                        "Oliver meets new people.",
                        "Oliver joins a school.",
                        "Oliver becomes a sailor."
                    ),
                    correctIndex = 0,
                    explanation = "He meets more acquaintances."
                )
            )
        ),
        makeChapter(
            number = 14,
            title = "Oliver stays with Brownlow",
            summary = "Oliver profite d’une vie paisible chez Brownlow, malgré les doutes de M. Grimwig. Alors qu’il part faire une commission, Nancy l’emmène et le ramène chez Fagin.",
            sceneTitle = "A difficult prediction",
            imageEmoji = "📦",
            englishText = """
Oliver enjoys a peaceful life at Mr. Brownlow's house. Mr. Grimwig doubts that Oliver can remain honest and predicts trouble. Oliver is sent on an errand. On the way, he is taken by Nancy and returned to Fagin.
""".trimIndent(),
            frenchTranslation = """
Oliver mène une vie paisible chez M. Brownlow. M. Grimwig doute qu'il puisse rester honnête et prédit des problèmes. Oliver est envoyé faire une course. En chemin, Nancy l'emmène et le ramène chez Fagin.
""".trimIndent(),
            arabicExplanation = """
عاش أوليفر حياة هادئة في منزل السيد براونلو. شكّ السيد غريمويغ في قدرة أوليفر على البقاء صادقاً وتوقع حدوث مشكلة. أُرسل أوليفر في مهمة، وفي الطريق أخذته نانسي وأعادته إلى فاجن.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "errand",
                    pronunciation = "/ˈerənd/",
                    definition = "A short trip to do a task.",
                    french = "course",
                    arabic = "مهمة",
                    example = "Oliver goes on an errand."
                ),
                OliverVocabulary(
                    word = "predict",
                    pronunciation = "/prɪˈdɪkt/",
                    definition = "To say what may happen in the future.",
                    french = "prédire",
                    arabic = "يتنبأ",
                    example = "Grimwig predicts trouble."
                ),
                OliverVocabulary(
                    word = "honest",
                    pronunciation = "/ˈɒnɪst/",
                    definition = "Truthful and fair.",
                    french = "honnête",
                    arabic = "صادق",
                    example = "Brownlow wants Oliver to be honest."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Who doubts Oliver?",
                    options = listOf(
                        "Mr. Grimwig",
                        "Mr. Sowerberry",
                        "Mr. Bumble"
                    ),
                    correctIndex = 0,
                    explanation = "Grimwig doubts Oliver."
                ),
                OliverQuestion(
                    question = "What happens on Oliver's errand?",
                    options = listOf(
                        "He is taken by Nancy.",
                        "He buys a book.",
                        "He goes to school."
                    ),
                    correctIndex = 0,
                    explanation = "Nancy takes Oliver away."
                )
            )
        ),
        makeChapter(
            number = 15,
            title = "Fagin and Nancy care about Oliver",
            summary = "Fagin veut récupérer Oliver parce qu’il peut être utile à la bande. Nancy éprouve une inquiétude plus sincère pour lui, mais Oliver reste surveillé pendant que les voleurs préparent leur suite.",
            sceneTitle = "A divided loyalty",
            imageEmoji = "🤝",
            englishText = """
Fagin wants Oliver back because he can be useful to the gang. Nancy shows a softer concern for the boy. She understands more than she says. Oliver is kept under watch while the thieves plan their next move.
""".trimIndent(),
            frenchTranslation = """
Fagin veut récupérer Oliver parce qu'il peut être utile au groupe. Nancy montre une inquiétude plus sincère pour l'enfant. Elle comprend plus de choses qu'elle ne le dit. Oliver est surveillé pendant que les voleurs préparent leur prochain coup.
""".trimIndent(),
            arabicExplanation = """
أراد فاجن استعادة أوليفر لأنه قد يكون مفيداً للعصابة. أظهرت نانسي اهتماماً أكثر بالطفل. كانت تفهم أكثر مما تقول. أُبقي أوليفر تحت المراقبة بينما خطط اللصوص لخطوتهم التالية.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "loyalty",
                    pronunciation = "/ˈlɔɪəlti/",
                    definition = "Faithfulness to a person or cause.",
                    french = "loyauté",
                    arabic = "ولاء",
                    example = "Nancy shows loyalty to Oliver."
                ),
                OliverVocabulary(
                    word = "concern",
                    pronunciation = "/kənˈsɜːrn/",
                    definition = "Worry about someone or something.",
                    french = "préoccupation",
                    arabic = "قلق",
                    example = "Nancy feels concern for Oliver."
                ),
                OliverVocabulary(
                    word = "watch",
                    pronunciation = "/wɒtʃ/",
                    definition = "To observe carefully.",
                    french = "surveiller",
                    arabic = "يراقب",
                    example = "Fagin keeps watch over Oliver."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Why does Fagin want Oliver?",
                    options = listOf(
                        "Because Oliver may help the gang.",
                        "Because Oliver is rich.",
                        "Because Oliver is a teacher."
                    ),
                    correctIndex = 0,
                    explanation = "Fagin thinks Oliver can be useful."
                ),
                OliverQuestion(
                    question = "Who shows concern for Oliver?",
                    options = listOf(
                        "Nancy",
                        "Noah",
                        "Mr. Fang"
                    ),
                    correctIndex = 0,
                    explanation = "Nancy shows concern."
                )
            )
        ),
        makeChapter(
            number = 16,
            title = "Oliver is taken back",
            summary = "Nancy ramène Oliver dans le groupe de Fagin. Confus et malheureux, il est contrôlé par les voleurs et regrette la sécurité de la maison de Brownlow.",
            sceneTitle = "Back with the gang",
            imageEmoji = "🔒",
            englishText = """
Nancy brings Oliver back to Fagin's group. He is confused and unhappy. The thieves keep him under control and begin preparing him for another criminal plan. Oliver longs for the safety of Brownlow's home.
""".trimIndent(),
            frenchTranslation = """
Nancy ramène Oliver dans le groupe de Fagin. Il est confus et malheureux. Les voleurs le gardent sous contrôle et le préparent à un nouveau plan criminel. Oliver regrette la sécurité de la maison de Brownlow.
""".trimIndent(),
            arabicExplanation = """
أعادت نانسي أوليفر إلى جماعة فاجن. كان مرتبكاً وحزيناً. أبقاه اللصوص تحت السيطرة وبدأوا في إعداده لخطة إجرامية جديدة. كان أوليفر يتوق إلى أمان منزل براونلو.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "control",
                    pronunciation = "/kənˈtroʊl/",
                    definition = "Power over someone or something.",
                    french = "contrôle",
                    arabic = "سيطرة",
                    example = "Fagin keeps Oliver under control."
                ),
                OliverVocabulary(
                    word = "confused",
                    pronunciation = "/kənˈfjuːzd/",
                    definition = "Unable to understand clearly.",
                    french = "confus",
                    arabic = "مرتبك",
                    example = "Oliver is confused."
                ),
                OliverVocabulary(
                    word = "long",
                    pronunciation = "/lɒŋ/",
                    definition = "To want something strongly.",
                    french = "désirer",
                    arabic = "يتوق",
                    example = "Oliver longs for safety."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "How does Oliver feel?",
                    options = listOf(
                        "Confused and unhappy",
                        "Rich and proud",
                        "Relaxed"
                    ),
                    correctIndex = 0,
                    explanation = "He is confused and unhappy."
                ),
                OliverQuestion(
                    question = "What does Oliver miss?",
                    options = listOf(
                        "Brownlow's safe home",
                        "The workhouse",
                        "Noah's insults"
                    ),
                    correctIndex = 0,
                    explanation = "He misses Brownlow's home."
                )
            )
        ),
        makeChapter(
            number = 17,
            title = "Monks comes to London",
            summary = "Un homme mystérieux appelé Monks cherche des informations sur Oliver. Il veut cacher son passé et son histoire familiale, ce qui augmente le danger autour du garçon.",
            sceneTitle = "A hidden enemy",
            imageEmoji = "🕵️",
            englishText = """
A mysterious man called Monks begins looking for information about Oliver. He wants Oliver's past to remain hidden. His actions suggest that Oliver's identity and family history are important. The danger around Oliver grows.
""".trimIndent(),
            frenchTranslation = """
Un homme mystérieux nommé Monks commence à chercher des informations sur Oliver. Il veut que le passé d'Oliver reste caché. Ses actes montrent que l'identité et l'histoire familiale d'Oliver sont importantes. Le danger qui entoure Oliver grandit.
""".trimIndent(),
            arabicExplanation = """
بدأ رجل غامض يدعى مونكس يبحث عن معلومات حول أوليفر. كان يريد أن يبقى ماضي أوليفر مخفياً. أظهرت أفعاله أن هوية أوليفر وتاريخ عائلته مهمان. ازداد الخطر المحيط بأوليفر.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "mysterious",
                    pronunciation = "/mɪˈstɪəriəs/",
                    definition = "Difficult to understand or explain.",
                    french = "mystérieux",
                    arabic = "غامض",
                    example = "Monks is a mysterious man."
                ),
                OliverVocabulary(
                    word = "identity",
                    pronunciation = "/aɪˈdentəti/",
                    definition = "Who a person is.",
                    french = "identité",
                    arabic = "هوية",
                    example = "Oliver's identity matters."
                ),
                OliverVocabulary(
                    word = "history",
                    pronunciation = "/ˈhɪstəri/",
                    definition = "Past events connected with someone or something.",
                    french = "histoire",
                    arabic = "تاريخ",
                    example = "Oliver's family history is hidden."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Who is Monks?",
                    options = listOf(
                        "A mysterious man",
                        "A doctor",
                        "A schoolboy"
                    ),
                    correctIndex = 0,
                    explanation = "Monks is a mysterious man."
                ),
                OliverQuestion(
                    question = "What is important?",
                    options = listOf(
                        "Oliver's identity",
                        "A football match",
                        "A new shop"
                    ),
                    correctIndex = 0,
                    explanation = "Oliver's identity is important."
                )
            )
        ),
        makeChapter(
            number = 18,
            title = "Oliver lives among the thieves",
            summary = "Oliver passe davantage de temps avec la bande de Fagin et découvre comment les garçons sont entraînés à voler. Malgré cette influence, il ne se sent pas à l’aise avec le crime.",
            sceneTitle = "A dangerous routine",
            imageEmoji = "🏚️",
            englishText = """
Oliver spends more time with Fagin's group. He sees how the boys are trained to steal and how Fagin controls them. Oliver does not become comfortable with crime. He remains different from the other boys.
""".trimIndent(),
            frenchTranslation = """
Oliver passe plus de temps avec le groupe de Fagin. Il voit comment les garçons apprennent à voler et comment Fagin les contrôle. Oliver ne s'habitue pas au crime. Il reste différent des autres garçons.
""".trimIndent(),
            arabicExplanation = """
قضى أوليفر وقتاً أطول مع جماعة فاجن. رأى كيف يتعلم الصبيان السرقة وكيف يسيطر فاجن عليهم. لم يعتد أوليفر على الجريمة وبقي مختلفاً عن بقية الصبيان.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "train",
                    pronunciation = "/treɪn/",
                    definition = "To teach someone how to do something.",
                    french = "former",
                    arabic = "يدرّب",
                    example = "Fagin trains the boys."
                ),
                OliverVocabulary(
                    word = "crime",
                    pronunciation = "/kraɪm/",
                    definition = "An illegal act.",
                    french = "crime",
                    arabic = "جريمة",
                    example = "Oliver does not accept crime."
                ),
                OliverVocabulary(
                    word = "different",
                    pronunciation = "/ˈdɪfərənt/",
                    definition = "Not the same as others.",
                    french = "différent",
                    arabic = "مختلف",
                    example = "Oliver is different from the others."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "What does Fagin teach the boys?",
                    options = listOf(
                        "To steal",
                        "To read",
                        "To cook"
                    ),
                    correctIndex = 0,
                    explanation = "He trains them to steal."
                ),
                OliverQuestion(
                    question = "How is Oliver different?",
                    options = listOf(
                        "He does not accept crime.",
                        "He wants to become a thief.",
                        "He enjoys stealing."
                    ),
                    correctIndex = 0,
                    explanation = "Oliver remains uncomfortable with crime."
                )
            )
        ),
        makeChapter(
            number = 19,
            title = "A burglary is planned",
            summary = "Fagin et Sikes préparent un cambriolage et choisissent Oliver pour sa petite taille. Oliver ne comprend pas toute la gravité du plan, qui met sa vie en danger.",
            sceneTitle = "A dangerous plan",
            imageEmoji = "🗺️",
            englishText = """
Fagin and Sikes discuss a serious burglary. Oliver is chosen because his small size may help the thieves. He does not understand all the danger. The plan puts his life at risk.
""".trimIndent(),
            frenchTranslation = """
Fagin et Sikes préparent un cambriolage important. Oliver est choisi parce que sa petite taille peut aider les voleurs. Il ne comprend pas tout le danger. Le plan met sa vie en danger.
""".trimIndent(),
            arabicExplanation = """
خطط فاجن وسايكس لعملية سطو خطيرة. اختير أوليفر لأن حجمه الصغير قد يساعد اللصوص. لم يفهم كل الخطر. عرضت الخطة حياته للخطر.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "burglary",
                    pronunciation = "/ˈbɜːrɡləri/",
                    definition = "The crime of entering a building to steal.",
                    french = "cambriolage",
                    arabic = "سطو",
                    example = "The men plan a burglary."
                ),
                OliverVocabulary(
                    word = "danger",
                    pronunciation = "/ˈdeɪndʒər/",
                    definition = "The possibility of harm.",
                    french = "danger",
                    arabic = "خطر",
                    example = "The plan is dangerous."
                ),
                OliverVocabulary(
                    word = "risk",
                    pronunciation = "/rɪsk/",
                    definition = "A possibility of loss or harm.",
                    french = "risque",
                    arabic = "مخاطرة",
                    example = "Oliver is placed at risk."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "What is planned?",
                    options = listOf(
                        "A burglary",
                        "A wedding",
                        "A journey to school"
                    ),
                    correctIndex = 0,
                    explanation = "A burglary is planned."
                ),
                OliverQuestion(
                    question = "Why is Oliver chosen?",
                    options = listOf(
                        "Because he is small.",
                        "Because he is rich.",
                        "Because he knows the house owner."
                    ),
                    correctIndex = 0,
                    explanation = "His small size is useful to the thieves."
                )
            )
        ),
        makeChapter(
            number = 20,
            title = "Oliver is delivered to Sikes",
            summary = "Oliver est confié à Bill Sikes, un homme brutal et intimidant. Il est emmené sans comprendre ce qui l’attend, tandis que Nancy tente discrètement de l’aider.",
            sceneTitle = "With Bill Sikes",
            imageEmoji = "🧔",
            englishText = """
Oliver is placed in the hands of Bill Sikes. Sikes is harsh and frightening. Oliver is taken away without understanding what will happen. Nancy tries to help him in her own limited way.
""".trimIndent(),
            frenchTranslation = """
Oliver est confié à Bill Sikes. Sikes est dur et effrayant. Oliver est emmené sans comprendre ce qui va lui arriver. Nancy essaie de l'aider comme elle le peut.
""".trimIndent(),
            arabicExplanation = """
أُسلّم أوليفر إلى بيل سايكس. كان سايكس قاسياً ومخيفاً. أُخذ أوليفر دون أن يفهم ما سيحدث له. حاولت نانسي مساعدته بقدر ما تستطيع.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "harsh",
                    pronunciation = "/hɑːrʃ/",
                    definition = "Severe and unfriendly.",
                    french = "dur",
                    arabic = "قاسٍ",
                    example = "Sikes is harsh."
                ),
                OliverVocabulary(
                    word = "frightening",
                    pronunciation = "/ˈfraɪtənɪŋ/",
                    definition = "Making someone feel fear.",
                    french = "effrayant",
                    arabic = "مخيف",
                    example = "Sikes is frightening."
                ),
                OliverVocabulary(
                    word = "trust",
                    pronunciation = "/trʌst/",
                    definition = "To believe someone is reliable.",
                    french = "faire confiance",
                    arabic = "يثق",
                    example = "Oliver cannot trust Sikes."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Who takes Oliver?",
                    options = listOf(
                        "Bill Sikes",
                        "Mr. Brownlow",
                        "Harry Maylie"
                    ),
                    correctIndex = 0,
                    explanation = "Bill Sikes takes Oliver."
                ),
                OliverQuestion(
                    question = "How does Oliver feel?",
                    options = listOf(
                        "Frightened",
                        "Celebratory",
                        "Relaxed"
                    ),
                    correctIndex = 0,
                    explanation = "He is frightened."
                )
            )
        ),
        makeChapter(
            number = 21,
            title = "The expedition",
            summary = "Sikes, Nancy et Oliver se rendent vers la maison qu’ils veulent cambrioler. Oliver est inquiet et espère encore que quelqu’un viendra le sauver.",
            sceneTitle = "Travelling to the house",
            imageEmoji = "🌙",
            englishText = """
Sikes, Nancy, and Oliver travel towards the house they plan to rob. Oliver is cold and anxious. The journey is tense because the thieves know that discovery could bring punishment. Oliver hopes someone will rescue him.
""".trimIndent(),
            frenchTranslation = """
Sikes, Nancy et Oliver se rendent vers la maison qu'ils veulent cambrioler. Oliver a froid et il est inquiet. Le voyage est tendu car les voleurs savent qu'une découverte pourrait entraîner une punition. Oliver espère que quelqu'un viendra le sauver.
""".trimIndent(),
            arabicExplanation = """
ذهب سايكس ونانسي وأوليفر نحو المنزل الذي ينوون سرقته. كان أوليفر بارداً وقلقاً. كانت الرحلة متوترة لأن اللصوص يعرفون أن اكتشافهم سيجلب العقاب. كان أوليفر يأمل أن ينقذه أحد.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "expedition",
                    pronunciation = "/ˌekspəˈdɪʃən/",
                    definition = "A journey made for a particular purpose.",
                    french = "expédition",
                    arabic = "رحلة مهمة",
                    example = "The expedition is dangerous."
                ),
                OliverVocabulary(
                    word = "anxious",
                    pronunciation = "/ˈæŋkʃəs/",
                    definition = "Worried or nervous.",
                    french = "anxieux",
                    arabic = "قلق",
                    example = "Oliver feels anxious."
                ),
                OliverVocabulary(
                    word = "rescue",
                    pronunciation = "/ˈreskjuː/",
                    definition = "To save someone from danger.",
                    french = "secourir",
                    arabic = "ينقذ",
                    example = "Oliver hopes for rescue."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Why is the journey tense?",
                    options = listOf(
                        "The thieves may be discovered.",
                        "Oliver is going to school.",
                        "They are travelling for a holiday."
                    ),
                    correctIndex = 0,
                    explanation = "Discovery could lead to punishment."
                ),
                OliverQuestion(
                    question = "What does Oliver hope for?",
                    options = listOf(
                        "Rescue",
                        "Money",
                        "A new job"
                    ),
                    correctIndex = 0,
                    explanation = "He hopes someone will rescue him."
                )
            )
        ),
        makeChapter(
            number = 22,
            title = "The burglary",
            summary = "Pendant le cambriolage, Oliver doit entrer par une petite fenêtre mais tente d’avertir les habitants. Il est blessé par un tir et laissé derrière par les voleurs.",
            sceneTitle = "Oliver is wounded",
            imageEmoji = "🔫",
            englishText = """
The burglars enter the house. Oliver is ordered to enter through a small window, but he tries to warn the people inside. A shot is fired and Oliver is wounded. Sikes and the others leave him behind.
""".trimIndent(),
            frenchTranslation = """
Les voleurs entrent dans la maison. Oliver reçoit l'ordre de passer par une petite fenêtre, mais il essaie d'avertir les habitants. Un coup de feu est tiré et Oliver est blessé. Sikes et les autres l'abandonnent.
""".trimIndent(),
            arabicExplanation = """
دخل اللصوص المنزل. أُمر أوليفر بالمرور من نافذة صغيرة، لكنه حاول تحذير أهل البيت. أُطلقت رصاصة وأصيب أوليفر. تركه سايكس والآخرون وراءهم.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "wound",
                    pronunciation = "/wuːnd/",
                    definition = "An injury to the body.",
                    french = "blessure",
                    arabic = "جرح",
                    example = "Oliver is wounded."
                ),
                OliverVocabulary(
                    word = "warn",
                    pronunciation = "/wɔːrn/",
                    definition = "To tell someone about danger.",
                    french = "avertir",
                    arabic = "يحذّر",
                    example = "Oliver tries to warn the family."
                ),
                OliverVocabulary(
                    word = "gunshot",
                    pronunciation = "/ˈɡʌnʃɒt/",
                    definition = "The sound or act of a gun being fired.",
                    french = "coup de feu",
                    arabic = "طلقة نارية",
                    example = "A gunshot is heard."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "What happens to Oliver?",
                    options = listOf(
                        "He is wounded.",
                        "He becomes a policeman.",
                        "He escapes to France."
                    ),
                    correctIndex = 0,
                    explanation = "Oliver is wounded."
                ),
                OliverQuestion(
                    question = "What does Oliver try to do?",
                    options = listOf(
                        "Warn the people inside",
                        "Help the thieves",
                        "Hide the money"
                    ),
                    correctIndex = 0,
                    explanation = "He tries to warn them."
                )
            )
        ),
        makeChapter(
            number = 23,
            title = "Mr. Bumble meets Mrs. Corney",
            summary = "M. Bumble rend visite à Mme Corney, responsable de la workhouse, et cherche à lui plaire. Leur relation révèle la fierté et l’égoïsme de certains responsables de la paroisse.",
            sceneTitle = "A new marriage",
            imageEmoji = "💍",
            englishText = """
Mr. Bumble visits Mrs. Corney, a woman in charge of the workhouse. He is interested in her and tries to impress her. Their conversation reveals the pride and selfishness of some workhouse officials. Their relationship will have consequences later.
""".trimIndent(),
            frenchTranslation = """
M. Bumble rend visite à Mme Corney, responsable de la maison de travail. Il s'intéresse à elle et cherche à l'impressionner. Leur conversation révèle l'orgueil et l'égoïsme de certains responsables. Leur relation aura des conséquences plus tard.
""".trimIndent(),
            arabicExplanation = """
زار السيد بامبل السيدة كورني، المسؤولة عن دار العمل. كان مهتماً بها وحاول إبهارها. كشف حديثهما عن غرور وأنانية بعض المسؤولين. وستكون لعلاقتهما عواقب لاحقاً.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "official",
                    pronunciation = "/əˈfɪʃəl/",
                    definition = "A person with a position of authority.",
                    french = "responsable",
                    arabic = "مسؤول",
                    example = "The official runs the workhouse."
                ),
                OliverVocabulary(
                    word = "pride",
                    pronunciation = "/praɪd/",
                    definition = "A strong feeling of self-importance.",
                    french = "orgueil",
                    arabic = "غرور",
                    example = "Pride affects the officials."
                ),
                OliverVocabulary(
                    word = "selfish",
                    pronunciation = "/ˈselfɪʃ/",
                    definition = "Thinking mainly about oneself.",
                    french = "égoïste",
                    arabic = "أناني",
                    example = "Some officials are selfish."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Who does Mr. Bumble visit?",
                    options = listOf(
                        "Mrs. Corney",
                        "Nancy",
                        "Mrs. Bedwin"
                    ),
                    correctIndex = 0,
                    explanation = "He visits Mrs. Corney."
                ),
                OliverQuestion(
                    question = "What is revealed?",
                    options = listOf(
                        "Pride and selfishness",
                        "A secret school",
                        "A new business"
                    ),
                    correctIndex = 0,
                    explanation = "The conversation shows their selfishness."
                )
            )
        ),
        makeChapter(
            number = 24,
            title = "A poor subject",
            summary = "La vie des pauvres reste très dure dans la paroisse. La mort d’une vieille femme et le secret lié à la naissance d’Oliver rappellent les conséquences de la pauvreté et de la privation.",
            sceneTitle = "A death in the workhouse",
            imageEmoji = "🕯️",
            englishText = """
Life in the parish remains harsh, especially for the poor. An old woman dies after years of deprivation, and a secret from Oliver's birth remains hidden. The chapter reminds the reader that poverty shapes many lives around Oliver.
""".trimIndent(),
            frenchTranslation = """
La vie dans la paroisse reste difficile, surtout pour les pauvres. Une vieille femme meurt après des années de privation et un secret lié à la naissance d'Oliver reste caché. Le chapitre rappelle que la pauvreté marque de nombreuses vies autour d'Oliver.
""".trimIndent(),
            arabicExplanation = """
ظلت الحياة في الرعية قاسية، خاصة بالنسبة للفقراء. ماتت امرأة مسنة بعد سنوات من الحرمان وبقي سر مرتبط بولادة أوليفر مخفياً. يذكّر الفصل بأن الفقر يؤثر في حياة كثيرين حول أوليفر.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "deprivation",
                    pronunciation = "/ˌdeprɪˈveɪʃən/",
                    definition = "Lack of basic needs or comforts.",
                    french = "privation",
                    arabic = "حرمان",
                    example = "Deprivation harms the poor."
                ),
                OliverVocabulary(
                    word = "parish",
                    pronunciation = "/ˈpærɪʃ/",
                    definition = "A local church or administrative area.",
                    french = "paroisse",
                    arabic = "رعية",
                    example = "The parish controls the workhouse."
                ),
                OliverVocabulary(
                    word = "secret",
                    pronunciation = "/ˈsiːkrət/",
                    definition = "Something kept hidden.",
                    french = "secret",
                    arabic = "سر",
                    example = "A secret about Oliver remains hidden."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "What does the chapter emphasize?",
                    options = listOf(
                        "The hardship of poverty",
                        "A school competition",
                        "A holiday"
                    ),
                    correctIndex = 0,
                    explanation = "It emphasizes poverty."
                ),
                OliverQuestion(
                    question = "What remains hidden?",
                    options = listOf(
                        "A secret about Oliver's birth",
                        "Oliver's school results",
                        "A treasure"
                    ),
                    correctIndex = 0,
                    explanation = "The birth secret remains hidden."
                )
            )
        ),
        makeChapter(
            number = 25,
            title = "Back to Fagin and company",
            summary = "Fagin et ses compagnons discutent de l’échec du cambriolage et de la disparition d’Oliver. Ils craignent qu’il soit tombé entre les mains de la justice et cherchent à se protéger.",
            sceneTitle = "The gang regroups",
            imageEmoji = "🕯️",
            englishText = """
The story returns to Fagin and his companions. They discuss the failed burglary and Oliver's disappearance. The gang becomes worried because Oliver may now be in the hands of the law. Fagin thinks about how to protect himself.
""".trimIndent(),
            frenchTranslation = """
L'histoire revient à Fagin et à ses complices. Ils parlent du cambriolage raté et de la disparition d'Oliver. Le groupe s'inquiète parce qu'Oliver pourrait être entre les mains de la justice. Fagin cherche à se protéger.
""".trimIndent(),
            arabicExplanation = """
يعود السرد إلى فاجن ورفاقه. تحدثوا عن عملية السطو الفاشلة واختفاء أوليفر. أصبحت العصابة قلقة لأن أوليفر قد يكون في يد القانون. فكر فاجن في كيفية حماية نفسه.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "gang",
                    pronunciation = "/ɡæŋ/",
                    definition = "A group involved in criminal activity.",
                    french = "bande",
                    arabic = "عصابة",
                    example = "Fagin's gang is worried."
                ),
                OliverVocabulary(
                    word = "failed",
                    pronunciation = "/feɪld/",
                    definition = "Not successful.",
                    french = "échoué",
                    arabic = "فاشل",
                    example = "The burglary failed."
                ),
                OliverVocabulary(
                    word = "protect",
                    pronunciation = "/prəˈtekt/",
                    definition = "To keep safe from harm.",
                    french = "protéger",
                    arabic = "يحمي",
                    example = "Fagin wants to protect himself."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Why is Fagin worried?",
                    options = listOf(
                        "The burglary failed.",
                        "Oliver found money.",
                        "He lost his home."
                    ),
                    correctIndex = 0,
                    explanation = "The burglary failed."
                ),
                OliverQuestion(
                    question = "What does Fagin think about?",
                    options = listOf(
                        "Protecting himself",
                        "Going to school",
                        "Buying a farm"
                    ),
                    correctIndex = 0,
                    explanation = "He thinks about protecting himself."
                )
            )
        ),
        makeChapter(
            number = 26,
            title = "Monks appears",
            summary = "Monks rencontre Fagin et lui demande des informations sur Oliver. Il veut empêcher Oliver de rencontrer les personnes capables de l’aider et accepte de collaborer avec Fagin.",
            sceneTitle = "A mysterious character",
            imageEmoji = "🕵️",
            englishText = """
Monks meets Fagin and asks about Oliver. He wants to keep Oliver away from people who could help him. Money and family secrets are connected to his plan. Fagin agrees to work with him.
""".trimIndent(),
            frenchTranslation = """
Monks rencontre Fagin et demande des informations sur Oliver. Il veut éloigner Oliver des personnes qui pourraient l'aider. L'argent et les secrets de famille sont liés à son plan. Fagin accepte de collaborer avec lui.
""".trimIndent(),
            arabicExplanation = """
التقى مونكس بفاجن وسأل عن أوليفر. أراد إبعاد أوليفر عن الأشخاص الذين يمكنهم مساعدته. ارتبطت الأموال وأسرار العائلة بخطته. وافق فاجن على العمل معه.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "inheritance",
                    pronunciation = "/ɪnˈherɪtəns/",
                    definition = "Money or property received after someone's death.",
                    french = "héritage",
                    arabic = "ميراث",
                    example = "The family inheritance matters."
                ),
                OliverVocabulary(
                    word = "scheme",
                    pronunciation = "/skiːm/",
                    definition = "A secret plan, often dishonest.",
                    french = "complot",
                    arabic = "خطة سرية",
                    example = "Monks has a secret scheme."
                ),
                OliverVocabulary(
                    word = "partner",
                    pronunciation = "/ˈpɑːrtnər/",
                    definition = "A person who works together with another.",
                    french = "partenaire",
                    arabic = "شريك",
                    example = "Fagin becomes Monks's partner."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Who meets Fagin?",
                    options = listOf(
                        "Monks",
                        "Brownlow",
                        "Harry"
                    ),
                    correctIndex = 0,
                    explanation = "Monks meets Fagin."
                ),
                OliverQuestion(
                    question = "What is connected to Monks's plan?",
                    options = listOf(
                        "Family secrets and money",
                        "A school exam",
                        "A farm"
                    ),
                    correctIndex = 0,
                    explanation = "Family secrets and money are involved."
                )
            )
        ),
        makeChapter(
            number = 27,
            title = "Oliver is watched over",
            summary = "Oliver continue d’être protégé par des personnes bienveillantes. Les adultes autour de lui commencent à comprendre que son passé est beaucoup plus complexe qu’il n’y paraît.",
            sceneTitle = "A lady and her household",
            imageEmoji = "🏡",
            englishText = """
Oliver is still being cared for by people who want to protect him. The story also returns to people connected with earlier events. The adults around Oliver begin to understand that his past is more complicated than it first seemed.
""".trimIndent(),
            frenchTranslation = """
Oliver est toujours aidé par des personnes qui veulent le protéger. L'histoire revient également vers des personnages liés aux événements précédents. Les adultes comprennent peu à peu que son passé est plus compliqué qu'il ne semblait.
""".trimIndent(),
            arabicExplanation = """
ظل أوليفر تحت رعاية أشخاص يريدون حمايته. وعاد السرد أيضاً إلى أشخاص مرتبطين بالأحداث السابقة. بدأ الكبار يفهمون أن ماضي أوليفر أكثر تعقيداً مما بدا أولاً.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "protective",
                    pronunciation = "/prəˈtektɪv/",
                    definition = "Wanting to keep someone safe.",
                    french = "protecteur",
                    arabic = "حامٍ",
                    example = "The adults are protective of Oliver."
                ),
                OliverVocabulary(
                    word = "complicated",
                    pronunciation = "/ˈkɒmplɪkeɪtɪd/",
                    definition = "Containing many connected parts.",
                    french = "complexe",
                    arabic = "معقد",
                    example = "Oliver's past is complicated."
                ),
                OliverVocabulary(
                    word = "household",
                    pronunciation = "/ˈhaʊshoʊld/",
                    definition = "The people living in a home.",
                    french = "maison",
                    arabic = "أسرة المنزل",
                    example = "Oliver enters a kind household."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "What do the adults learn?",
                    options = listOf(
                        "Oliver's past is complicated.",
                        "Oliver dislikes reading.",
                        "London is empty."
                    ),
                    correctIndex = 0,
                    explanation = "They see that his past is complicated."
                ),
                OliverQuestion(
                    question = "What do they want?",
                    options = listOf(
                        "To protect Oliver",
                        "To punish Oliver",
                        "To sell Oliver"
                    ),
                    correctIndex = 0,
                    explanation = "They want to protect him."
                )
            )
        ),
        makeChapter(
            number = 28,
            title = "Oliver is with new friends",
            summary = "Oliver est accueilli par les Maylie dans un environnement calme et chaleureux. Loin de Londres, il commence à retrouver la santé et découvre une courte période de bonheur.",
            sceneTitle = "Looking after Oliver",
            imageEmoji = "🌳",
            englishText = """
Oliver is taken into the care of the Maylies and their household. He is treated with warmth and begins to recover. The countryside gives him a peaceful environment far from London. For a short time, Oliver experiences happiness.
""".trimIndent(),
            frenchTranslation = """
Oliver est accueilli par les Maylie et leur entourage. Il est traité avec chaleur et commence à se rétablir. La campagne lui offre un environnement paisible loin de Londres. Pendant quelque temps, Oliver connaît le bonheur.
""".trimIndent(),
            arabicExplanation = """
استقبلت عائلة مايلي ومحيطها أوليفر. عومل بلطف وبدأ يتعافى. منحتْه الريفُ بيئة هادئة بعيداً عن لندن. ولوقت قصير عاش أوليفر السعادة.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "countryside",
                    pronunciation = "/ˈkʌntrisaɪd/",
                    definition = "Land outside towns and cities.",
                    french = "campagne",
                    arabic = "ريف",
                    example = "Oliver stays in the countryside."
                ),
                OliverVocabulary(
                    word = "recover",
                    pronunciation = "/rɪˈkʌvər/",
                    definition = "To become healthy again.",
                    french = "se rétablir",
                    arabic = "يتعافى",
                    example = "Oliver begins to recover."
                ),
                OliverVocabulary(
                    word = "warmth",
                    pronunciation = "/wɔːrmθ/",
                    definition = "Friendly kindness and affection.",
                    french = "chaleur",
                    arabic = "دفء",
                    example = "The family gives Oliver warmth."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Who cares for Oliver?",
                    options = listOf(
                        "The Maylies",
                        "Fagin",
                        "Noah"
                    ),
                    correctIndex = 0,
                    explanation = "The Maylies care for him."
                ),
                OliverQuestion(
                    question = "What changes?",
                    options = listOf(
                        "Oliver experiences peace.",
                        "Oliver returns to the workhouse.",
                        "Oliver joins the thieves."
                    ),
                    correctIndex = 0,
                    explanation = "He experiences peace in the countryside."
                )
            )
        ),
        makeChapter(
            number = 29,
            title = "The Maylie household",
            summary = "Oliver découvre la famille Maylie, notamment Rose, Mme Maylie et le Dr Losberne. Leur gentillesse lui donne envie de montrer qu’il est digne de confiance.",
            sceneTitle = "New people around Oliver",
            imageEmoji = "🌹",
            englishText = """
Oliver learns about the people living in the Maylie household. Rose Maylie and Mrs. Maylie treat him kindly, while Dr. Losberne protects him. Oliver is grateful and wants to prove that he can be trusted.
""".trimIndent(),
            frenchTranslation = """
Oliver découvre les personnes qui vivent chez les Maylie. Rose Maylie et Mme Maylie le traitent avec bonté, tandis que le docteur Losberne le protège. Oliver est reconnaissant et veut montrer qu'on peut lui faire confiance.
""".trimIndent(),
            arabicExplanation = """
تعرّف أوليفر على أفراد منزل مايلي. عاملته روز مايلي والسيدة مايلي بلطف، وحماه الدكتور لوسبرن. كان أوليفر ممتناً وأراد أن يثبت أنه جدير بالثقة.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "grateful",
                    pronunciation = "/ˈɡreɪtfəl/",
                    definition = "Feeling thankful.",
                    french = "reconnaissant",
                    arabic = "ممتن",
                    example = "Oliver is grateful."
                ),
                OliverVocabulary(
                    word = "trustworthy",
                    pronunciation = "/ˈtrʌstwɜːrði/",
                    definition = "Able to be trusted.",
                    french = "digne de confiance",
                    arabic = "جدير بالثقة",
                    example = "Oliver wants to be trustworthy."
                ),
                OliverVocabulary(
                    word = "doctor",
                    pronunciation = "/ˈdɒktər/",
                    definition = "A person trained to treat illness.",
                    french = "médecin",
                    arabic = "طبيب",
                    example = "Dr. Losberne helps Oliver."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Who is kind to Oliver?",
                    options = listOf(
                        "Rose and Mrs. Maylie",
                        "Fagin and Sikes",
                        "Bumble and Noah"
                    ),
                    correctIndex = 0,
                    explanation = "Rose and Mrs. Maylie are kind to him."
                ),
                OliverQuestion(
                    question = "What does Oliver want to prove?",
                    options = listOf(
                        "That he can be trusted",
                        "That he can steal",
                        "That he can fight"
                    ),
                    correctIndex = 0,
                    explanation = "He wants to prove he is trustworthy."
                )
            )
        ),
        makeChapter(
            number = 30,
            title = "Oliver's visitors",
            summary = "De nouveaux visiteurs interrogent Oliver sur son passé. Certains le croient tandis que d’autres restent méfiants, et son innocence continue d’être mise en doute.",
            sceneTitle = "A critical opinion",
            imageEmoji = "👀",
            englishText = """
New visitors meet Oliver and ask questions about his past. Their reactions are mixed. Some people believe him, while others remain suspicious. Oliver's innocence is still questioned because of what happened in London.
""".trimIndent(),
            frenchTranslation = """
De nouveaux visiteurs rencontrent Oliver et lui posent des questions sur son passé. Les réactions sont partagées. Certaines personnes le croient, d'autres restent méfiantes. L'innocence d'Oliver est encore mise en doute à cause de ce qui s'est passé à Londres.
""".trimIndent(),
            arabicExplanation = """
التقى زوار جدد بأوليفر وطرحوا عليه أسئلة عن ماضيه. كانت ردود أفعالهم مختلفة. صدقه بعضهم وبقي آخرون مشككين. ظلت براءة أوليفر موضع شك بسبب ما حدث في لندن.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "suspicious",
                    pronunciation = "/səˈspɪʃəs/",
                    definition = "Not trusting someone or something.",
                    french = "méfiant",
                    arabic = "مشبوه",
                    example = "Some visitors are suspicious."
                ),
                OliverVocabulary(
                    word = "innocence",
                    pronunciation = "/ˈɪnəsəns/",
                    definition = "The state of not being guilty.",
                    french = "innocence",
                    arabic = "براءة",
                    example = "Oliver's innocence is questioned."
                ),
                OliverVocabulary(
                    word = "visitor",
                    pronunciation = "/ˈvɪzɪtər/",
                    definition = "A person who comes to see a place or person.",
                    french = "visiteur",
                    arabic = "زائر",
                    example = "A visitor asks questions."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Why do some people doubt Oliver?",
                    options = listOf(
                        "Because of his past in London",
                        "Because he is rich",
                        "Because he refuses food"
                    ),
                    correctIndex = 0,
                    explanation = "His past makes them suspicious."
                ),
                OliverQuestion(
                    question = "What quality is questioned?",
                    options = listOf(
                        "His innocence",
                        "His wealth",
                        "His age"
                    ),
                    correctIndex = 0,
                    explanation = "His innocence is questioned."
                )
            )
        ),
        makeChapter(
            number = 31,
            title = "A critical position",
            summary = "Une nouvelle menace pèse sur Oliver et ses amis. Ils comprennent que des ennemis puissants cherchent encore à agir contre lui et doivent rester prudents.",
            sceneTitle = "Oliver faces danger again",
            imageEmoji = "⚠️",
            englishText = """
A new danger threatens Oliver and his friends. The people around him realize that powerful enemies are still interested in him. They must be careful. Oliver's peaceful life may not last.
""".trimIndent(),
            frenchTranslation = """
Un nouveau danger menace Oliver et ses amis. Ceux qui l'entourent comprennent que des ennemis puissants s'intéressent toujours à lui. Ils doivent être prudents. La vie paisible d'Oliver pourrait ne pas durer.
""".trimIndent(),
            arabicExplanation = """
يهدد خطر جديد أوليفر وأصدقاءه. أدرك من حوله أن أعداء أقوياء ما زالوا مهتمين به. وعليهم أن يكونوا حذرين. قد لا تستمر حياة أوليفر الهادئة.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "threat",
                    pronunciation = "/θret/",
                    definition = "A sign of danger or harm.",
                    french = "menace",
                    arabic = "تهديد",
                    example = "A threat appears."
                ),
                OliverVocabulary(
                    word = "powerful",
                    pronunciation = "/ˈpaʊərfəl/",
                    definition = "Having great influence or strength.",
                    french = "puissant",
                    arabic = "قوي",
                    example = "Oliver has powerful enemies."
                ),
                OliverVocabulary(
                    word = "careful",
                    pronunciation = "/ˈkeərfəl/",
                    definition = "Thinking about danger before acting.",
                    french = "prudent",
                    arabic = "حذر",
                    example = "Everyone must be careful."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "What threatens Oliver?",
                    options = listOf(
                        "A new danger",
                        "A school test",
                        "A new job"
                    ),
                    correctIndex = 0,
                    explanation = "A new danger appears."
                ),
                OliverQuestion(
                    question = "What must the adults do?",
                    options = listOf(
                        "Be careful",
                        "Leave Oliver alone",
                        "Join Fagin"
                    ),
                    correctIndex = 0,
                    explanation = "They must be careful."
                )
            )
        ),
        makeChapter(
            number = 32,
            title = "Oliver begins a happy life",
            summary = "Oliver mène une vie heureuse à la campagne auprès des Maylie. Il apprend, lit et imagine enfin un avenir fondé sur la bonté et l’honnêteté.",
            sceneTitle = "Peace in the countryside",
            imageEmoji = "☀️",
            englishText = """
Oliver helps the Maylies and enjoys a healthier life. He reads, learns, and spends time with people who care for him. He begins to imagine a future based on kindness and honesty. His earlier suffering seems far away.
""".trimIndent(),
            frenchTranslation = """
Oliver aide les Maylie et profite d'une vie plus saine. Il lit, apprend et passe du temps avec des personnes qui tiennent à lui. Il commence à imaginer un avenir fondé sur la bonté et l'honnêteté. Ses souffrances passées semblent lointaines.
""".trimIndent(),
            arabicExplanation = """
ساعد أوليفر عائلة مايلي واستمتع بحياة أكثر صحة. كان يقرأ ويتعلم ويقضي وقتاً مع أشخاص يهتمون به. بدأ يتخيل مستقبلاً قائماً على اللطف والصدق. بدت معاناته السابقة بعيدة.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "honesty",
                    pronunciation = "/ˈɒnəsti/",
                    definition = "The quality of being truthful.",
                    french = "honnêteté",
                    arabic = "صدق",
                    example = "Oliver values honesty."
                ),
                OliverVocabulary(
                    word = "future",
                    pronunciation = "/ˈfjuːtʃər/",
                    definition = "The time that has not yet happened.",
                    french = "avenir",
                    arabic = "مستقبل",
                    example = "Oliver imagines a better future."
                ),
                OliverVocabulary(
                    word = "suffering",
                    pronunciation = "/ˈsʌfərɪŋ/",
                    definition = "Pain or hardship.",
                    french = "souffrance",
                    arabic = "معاناة",
                    example = "His suffering seems far away."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "What does Oliver begin to imagine?",
                    options = listOf(
                        "A better future",
                        "A life of crime",
                        "A new workhouse"
                    ),
                    correctIndex = 0,
                    explanation = "He imagines a better future."
                ),
                OliverQuestion(
                    question = "What values matter to Oliver?",
                    options = listOf(
                        "Kindness and honesty",
                        "Money and power",
                        "Fear and violence"
                    ),
                    correctIndex = 0,
                    explanation = "He values kindness and honesty."
                )
            )
        ),
        makeChapter(
            number = 33,
            title = "Oliver's happiness is interrupted",
            summary = "La tranquillité d’Oliver est brusquement interrompue par de nouveaux événements. Ses amis comprennent que le danger n’a pas disparu et cherchent à le protéger.",
            sceneTitle = "A sudden change",
            imageEmoji = "💌",
            englishText = """
Oliver's peaceful life is suddenly disturbed. A message and new events remind everyone that danger has not disappeared. Oliver cannot understand why his happiness is changing. His friends try to protect him.
""".trimIndent(),
            frenchTranslation = """
La vie paisible d'Oliver est soudainement interrompue. Un message et de nouveaux événements rappellent que le danger n'a pas disparu. Oliver ne comprend pas pourquoi son bonheur change. Ses amis essaient de le protéger.
""".trimIndent(),
            arabicExplanation = """
انقطعت حياة أوليفر الهادئة فجأة. ذكّرت رسالة وأحداث جديدة الجميع بأن الخطر لم يختفِ. لم يفهم أوليفر لماذا تغيرت سعادته. حاول أصدقاؤه حمايته.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "interrupt",
                    pronunciation = "/ˌɪntəˈrʌpt/",
                    definition = "To stop something temporarily.",
                    french = "interrompre",
                    arabic = "يقاطع",
                    example = "The news interrupts Oliver's happiness."
                ),
                OliverVocabulary(
                    word = "message",
                    pronunciation = "/ˈmesɪdʒ/",
                    definition = "Information sent to someone.",
                    french = "message",
                    arabic = "رسالة",
                    example = "A message brings new worry."
                ),
                OliverVocabulary(
                    word = "protect",
                    pronunciation = "/prəˈtekt/",
                    definition = "To keep safe from harm.",
                    french = "protéger",
                    arabic = "يحمي",
                    example = "His friends protect Oliver."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "What happens to Oliver's happiness?",
                    options = listOf(
                        "It is interrupted.",
                        "It grows stronger.",
                        "It becomes a holiday."
                    ),
                    correctIndex = 0,
                    explanation = "His happiness is interrupted."
                ),
                OliverQuestion(
                    question = "What do his friends try to do?",
                    options = listOf(
                        "Protect him",
                        "Sell him",
                        "Ignore him"
                    ),
                    correctIndex = 0,
                    explanation = "They try to protect him."
                )
            )
        ),
        makeChapter(
            number = 34,
            title = "Harry Maylie arrives",
            summary = "Harry Maylie revient et devient important dans la vie d’Oliver. L’admiration d’Oliver pour lui accompagne une nouvelle aventure où plusieurs secrets restent encore cachés.",
            sceneTitle = "A new adventure",
            imageEmoji = "🎩",
            englishText = """
Harry Maylie returns and becomes an important part of the story. Oliver admires him and hopes to be useful. A new adventure also brings Oliver into situations connected with the Maylie family. Secrets remain unresolved.
""".trimIndent(),
            frenchTranslation = """
Harry Maylie revient et devient un personnage important. Oliver l'admire et espère lui être utile. Une nouvelle aventure entraîne aussi Oliver dans des situations liées à la famille Maylie. Certains secrets restent non résolus.
""".trimIndent(),
            arabicExplanation = """
عاد هاري مايلي وأصبح جزءاً مهماً من القصة. أعجب به أوليفر وأراد أن يكون مفيداً. حملت مغامرة جديدة أوليفر إلى مواقف مرتبطة بعائلة مايلي. وظلت بعض الأسرار دون حل.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "admire",
                    pronunciation = "/ədˈmaɪər/",
                    definition = "To respect or like someone greatly.",
                    french = "admirer",
                    arabic = "يعجب بـ",
                    example = "Oliver admires Harry."
                ),
                OliverVocabulary(
                    word = "adventure",
                    pronunciation = "/ədˈventʃər/",
                    definition = "An unusual or exciting experience.",
                    french = "aventure",
                    arabic = "مغامرة",
                    example = "Oliver faces a new adventure."
                ),
                OliverVocabulary(
                    word = "unresolved",
                    pronunciation = "/ˌʌnrɪˈzɒlvd/",
                    definition = "Not yet solved or settled.",
                    french = "non résolu",
                    arabic = "غير محلول",
                    example = "Some secrets remain unresolved."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Who arrives?",
                    options = listOf(
                        "Harry Maylie",
                        "Fagin",
                        "Mr. Fang"
                    ),
                    correctIndex = 0,
                    explanation = "Harry Maylie arrives."
                ),
                OliverQuestion(
                    question = "What remains?",
                    options = listOf(
                        "Unresolved secrets",
                        "A school prize",
                        "A new ship"
                    ),
                    correctIndex = 0,
                    explanation = "Some secrets remain unresolved."
                )
            )
        ),
        makeChapter(
            number = 35,
            title = "The adventure has an uncertain result",
            summary = "L’aventure ne se termine pas comme espéré. Harry et Rose réfléchissent à leurs sentiments et à leurs responsabilités, tandis que le destin d’Oliver reste lié à leur histoire.",
            sceneTitle = "Harry and Rose",
            imageEmoji = "💬",
            englishText = """
Oliver's new adventure does not end as hoped. Harry and Rose discuss their feelings and responsibilities. The adults around Oliver see that his story is connected to theirs. The chapter prepares the reader for future revelations.
""".trimIndent(),
            frenchTranslation = """
La nouvelle aventure d'Oliver n'a pas le résultat espéré. Harry et Rose parlent de leurs sentiments et de leurs responsabilités. Les adultes comprennent que l'histoire d'Oliver est liée à la leur. Le chapitre prépare de futures révélations.
""".trimIndent(),
            arabicExplanation = """
لم تنته مغامرة أوليفر بالنتيجة المتوقعة. تحدث هاري وروز عن مشاعرهما ومسؤولياتهما. أدرك الكبار أن قصة أوليفر مرتبطة بقصتهم. ويمهّد الفصل لاكتشافات لاحقة.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "responsibility",
                    pronunciation = "/rɪˌspɒnsəˈbɪləti/",
                    definition = "A duty to deal with something carefully.",
                    french = "responsabilité",
                    arabic = "مسؤولية",
                    example = "Harry thinks about responsibility."
                ),
                OliverVocabulary(
                    word = "feeling",
                    pronunciation = "/ˈfiːlɪŋ/",
                    definition = "An emotion or reaction.",
                    french = "sentiment",
                    arabic = "شعور",
                    example = "Harry and Rose discuss their feelings."
                ),
                OliverVocabulary(
                    word = "revelation",
                    pronunciation = "/ˌrevəˈleɪʃən/",
                    definition = "A surprising new fact that becomes known.",
                    french = "révélation",
                    arabic = "كشف",
                    example = "The chapter prepares revelations."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "What do Harry and Rose discuss?",
                    options = listOf(
                        "Feelings and responsibilities",
                        "A business plan",
                        "A new school"
                    ),
                    correctIndex = 0,
                    explanation = "They discuss feelings and responsibilities."
                ),
                OliverQuestion(
                    question = "What does the chapter prepare?",
                    options = listOf(
                        "Future revelations",
                        "A sports competition",
                        "A move to France"
                    ),
                    correctIndex = 0,
                    explanation = "It prepares future revelations."
                )
            )
        ),
        makeChapter(
            number = 36,
            title = "A short but important chapter",
            summary = "Ce chapitre bref fait le lien entre plusieurs événements. Des détails qui semblaient secondaires prennent de l’importance alors que l’histoire se rapproche d’une découverte majeure.",
            sceneTitle = "A link to the next events",
            imageEmoji = "🔗",
            englishText = """
This chapter is brief but important. It connects earlier events with what will happen next. Readers are asked to remember details that may seem small now. Oliver's story is moving towards a larger discovery.
""".trimIndent(),
            frenchTranslation = """
Ce chapitre est bref mais important. Il relie les événements précédents à ceux qui vont suivre. Le lecteur doit se souvenir de détails qui semblent peut-être petits pour le moment. L'histoire d'Oliver avance vers une découverte plus importante.
""".trimIndent(),
            arabicExplanation = """
هذا الفصل قصير لكنه مهم. فهو يربط الأحداث السابقة بما سيأتي. يُطلب من القارئ تذكر تفاصيل قد تبدو صغيرة الآن. وتتجه قصة أوليفر نحو اكتشاف أكبر.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "brief",
                    pronunciation = "/briːf/",
                    definition = "Short in time or length.",
                    french = "bref",
                    arabic = "قصير",
                    example = "It is a brief chapter."
                ),
                OliverVocabulary(
                    word = "detail",
                    pronunciation = "/ˈdiːteɪl/",
                    definition = "A small piece of information.",
                    french = "détail",
                    arabic = "تفصيل",
                    example = "Small details matter."
                ),
                OliverVocabulary(
                    word = "discovery",
                    pronunciation = "/dɪˈskʌvəri/",
                    definition = "The finding of something previously unknown.",
                    french = "découverte",
                    arabic = "اكتشاف",
                    example = "The story moves toward discovery."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Why is the chapter important?",
                    options = listOf(
                        "It connects events.",
                        "It starts a war.",
                        "It introduces a new country."
                    ),
                    correctIndex = 0,
                    explanation = "It connects important events."
                ),
                OliverQuestion(
                    question = "What should readers remember?",
                    options = listOf(
                        "Small details",
                        "Football results",
                        "Weather reports"
                    ),
                    correctIndex = 0,
                    explanation = "Small details will matter."
                )
            )
        ),
        makeChapter(
            number = 37,
            title = "Mr. and Mrs. Bumble",
            summary = "M. Bumble épouse Mme Corney et sa situation à la workhouse change. Leur mariage révèle davantage leur orgueil et leur égoïsme, avec des conséquences pour la suite.",
            sceneTitle = "A contrast in marriage",
            imageEmoji = "💒",
            englishText = """
Mr. Bumble marries Mrs. Corney, and their relationship changes his position in the workhouse. Their pride and selfishness become more obvious. The marriage brings both comic moments and serious consequences.
""".trimIndent(),
            frenchTranslation = """
M. Bumble épouse Mme Corney et leur relation change sa position dans la maison de travail. Leur orgueil et leur égoïsme deviennent plus visibles. Le mariage produit des moments comiques mais aussi des conséquences sérieuses.
""".trimIndent(),
            arabicExplanation = """
تزوج السيد بامبل من السيدة كورني، وغيّرت علاقتهما مكانته في دار العمل. أصبح غرورهما وأنانيتهما أكثر وضوحاً. جلب الزواج مواقف مضحكة وعواقب خطيرة.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "marriage",
                    pronunciation = "/ˈmærɪdʒ/",
                    definition = "The legal relationship between married people.",
                    french = "mariage",
                    arabic = "زواج",
                    example = "The marriage changes Bumble's life."
                ),
                OliverVocabulary(
                    word = "pride",
                    pronunciation = "/praɪd/",
                    definition = "A feeling of self-importance.",
                    french = "orgueil",
                    arabic = "غرور",
                    example = "Their pride causes trouble."
                ),
                OliverVocabulary(
                    word = "consequence",
                    pronunciation = "/ˈkɒnsɪkwens/",
                    definition = "A result of an action.",
                    french = "conséquence",
                    arabic = "عاقبة",
                    example = "The marriage has consequences."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Who marries?",
                    options = listOf(
                        "Mr. Bumble and Mrs. Corney",
                        "Oliver and Rose",
                        "Fagin and Nancy"
                    ),
                    correctIndex = 0,
                    explanation = "Bumble marries Mrs. Corney."
                ),
                OliverQuestion(
                    question = "What does the marriage show?",
                    options = listOf(
                        "Pride and selfishness",
                        "Kindness and honesty",
                        "Bravery"
                    ),
                    correctIndex = 0,
                    explanation = "Their pride and selfishness become clear."
                )
            )
        ),
        makeChapter(
            number = 38,
            title = "Monks meets the Bumbles",
            summary = "Monks rencontre secrètement les Bumble et leur demande ce qu’ils savent de la naissance d’Oliver. En échange d’argent, ils révèlent des informations qui mettent le secret familial en danger.",
            sceneTitle = "The hidden secret",
            imageEmoji = "🤫",
            englishText = """
Monks visits Mr. and Mrs. Bumble secretly. He asks about Oliver's birth and offers money for information. The Bumbles reveal what they know about the past. A family secret becomes more dangerous.
""".trimIndent(),
            frenchTranslation = """
Monks rend visite secrètement à M. et Mme Bumble. Il pose des questions sur la naissance d'Oliver et propose de l'argent pour obtenir des informations. Les Bumbles révèlent ce qu'ils savent du passé. Un secret de famille devient plus dangereux.
""".trimIndent(),
            arabicExplanation = """
زار مونكس السيد والسيدة بامبل سراً. سأل عن ولادة أوليفر وعرض المال مقابل المعلومات. كشف بامبل وزوجته ما يعرفانه عن الماضي. أصبح سر عائلي أكثر خطورة.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "secret",
                    pronunciation = "/ˈsiːkrət/",
                    definition = "Something kept hidden.",
                    french = "secret",
                    arabic = "سر",
                    example = "Monks keeps a secret."
                ),
                OliverVocabulary(
                    word = "information",
                    pronunciation = "/ˌɪnfərˈmeɪʃən/",
                    definition = "Facts or details about something.",
                    french = "information",
                    arabic = "معلومات",
                    example = "Monks pays for information."
                ),
                OliverVocabulary(
                    word = "birth",
                    pronunciation = "/bɜːrθ/",
                    definition = "The event of being born.",
                    french = "naissance",
                    arabic = "ولادة",
                    example = "They discuss Oliver's birth."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Who visits the Bumbles?",
                    options = listOf(
                        "Monks",
                        "Harry Maylie",
                        "Mr. Brownlow"
                    ),
                    correctIndex = 0,
                    explanation = "Monks visits them."
                ),
                OliverQuestion(
                    question = "What does he want?",
                    options = listOf(
                        "Information about Oliver",
                        "A new house",
                        "A job"
                    ),
                    correctIndex = 0,
                    explanation = "He wants information about Oliver."
                )
            )
        ),
        makeChapter(
            number = 39,
            title = "Monks and Fagin work together",
            summary = "Monks revient voir Fagin et lui révèle ce qu’il a appris. Leur alliance repose sur l’argent et le secret, et leurs plans renforcent la menace qui pèse sur Oliver.",
            sceneTitle = "A dangerous partnership",
            imageEmoji = "🤝",
            englishText = """
Monks returns to Fagin and explains what he has learned. The two men want Oliver's past to remain hidden. Their partnership is based on money and secrecy. Their plans put Oliver in greater danger.
""".trimIndent(),
            frenchTranslation = """
Monks retourne chez Fagin et explique ce qu'il a appris. Les deux hommes veulent que le passé d'Oliver reste caché. Leur collaboration repose sur l'argent et le secret. Leurs plans mettent Oliver en plus grand danger.
""".trimIndent(),
            arabicExplanation = """
عاد مونكس إلى فاجن وشرح ما عرفه. أراد الرجلان أن يبقى ماضي أوليفر مخفياً. قامت شراكتهما على المال والسرية. جعلت خطتهما أوليفر في خطر أكبر.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "partnership",
                    pronunciation = "/ˈpɑːrtnərʃɪp/",
                    definition = "A relationship in which people work together.",
                    french = "partenariat",
                    arabic = "شراكة",
                    example = "Their partnership is dangerous."
                ),
                OliverVocabulary(
                    word = "secrecy",
                    pronunciation = "/ˈsiːkrəsi/",
                    definition = "The act of keeping things hidden.",
                    french = "secret",
                    arabic = "سرية",
                    example = "Their plan depends on secrecy."
                ),
                OliverVocabulary(
                    word = "danger",
                    pronunciation = "/ˈdeɪndʒər/",
                    definition = "A possibility of harm.",
                    french = "danger",
                    arabic = "خطر",
                    example = "Oliver faces more danger."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Who works with Fagin?",
                    options = listOf(
                        "Monks",
                        "Brownlow",
                        "Losberne"
                    ),
                    correctIndex = 0,
                    explanation = "Monks works with Fagin."
                ),
                OliverQuestion(
                    question = "What does their plan depend on?",
                    options = listOf(
                        "Money and secrecy",
                        "Education",
                        "Travel"
                    ),
                    correctIndex = 0,
                    explanation = "Their plan depends on money and secrecy."
                )
            )
        ),
        makeChapter(
            number = 40,
            title = "A strange interview",
            summary = "Une rencontre secrète révèle de nouveaux éléments sur le conflit autour d’Oliver. Des informations importantes circulent, mais les intentions de chacun restent difficiles à comprendre.",
            sceneTitle = "Another hidden meeting",
            imageEmoji = "🗣️",
            englishText = """
A secret meeting reveals more about the conflict surrounding Oliver. Important information is exchanged behind closed doors. The people involved have different goals, and trust becomes difficult. Oliver's future remains uncertain.
""".trimIndent(),
            frenchTranslation = """
Une rencontre secrète révèle davantage sur le conflit qui entoure Oliver. Des informations importantes sont échangées à huis clos. Les personnes présentes ont des objectifs différents et la confiance devient difficile. L'avenir d'Oliver reste incertain.
""".trimIndent(),
            arabicExplanation = """
كشف اجتماع سري المزيد من الصراع حول أوليفر. تم تبادل معلومات مهمة بعيداً عن الأنظار. كانت أهداف المشاركين مختلفة وأصبح من الصعب الثقة بالآخرين. ظل مستقبل أوليفر غير مؤكد.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "interview",
                    pronunciation = "/ˈɪntərvjuː/",
                    definition = "A formal or private meeting to ask questions.",
                    french = "entretien",
                    arabic = "مقابلة",
                    example = "The interview is secret."
                ),
                OliverVocabulary(
                    word = "goal",
                    pronunciation = "/ɡoʊl/",
                    definition = "Something a person wants to achieve.",
                    french = "objectif",
                    arabic = "هدف",
                    example = "The characters have different goals."
                ),
                OliverVocabulary(
                    word = "uncertain",
                    pronunciation = "/ʌnˈsɜːrtən/",
                    definition = "Not known or decided.",
                    french = "incertain",
                    arabic = "غير مؤكد",
                    example = "Oliver's future is uncertain."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "What happens?",
                    options = listOf(
                        "A secret interview",
                        "A wedding",
                        "A school lesson"
                    ),
                    correctIndex = 0,
                    explanation = "A secret meeting takes place."
                ),
                OliverQuestion(
                    question = "How is Oliver's future?",
                    options = listOf(
                        "Uncertain",
                        "Completely safe",
                        "Already decided"
                    ),
                    correctIndex = 0,
                    explanation = "His future is uncertain."
                )
            )
        ),
        makeChapter(
            number = 41,
            title = "Fresh discoveries",
            summary = "De nouvelles découvertes donnent à Brownlow et à ses proches des indices sur le passé d’Oliver. Plusieurs éléments commencent enfin à s’assembler et la vérité se rapproche.",
            sceneTitle = "More clues",
            imageEmoji = "🔎",
            englishText = """
New discoveries give Brownlow and others clues about Oliver's past. Several pieces of information begin to fit together. Surprises arrive quickly, and the truth becomes closer. The mystery around Oliver is finally beginning to open.
""".trimIndent(),
            frenchTranslation = """
De nouvelles découvertes donnent à Brownlow et aux autres des indices sur le passé d'Oliver. Plusieurs informations commencent à s'assembler. Les surprises arrivent rapidement et la vérité se rapproche. Le mystère autour d'Oliver commence enfin à s'éclaircir.
""".trimIndent(),
            arabicExplanation = """
قدمت اكتشافات جديدة لبراونلو وآخرين أدلة حول ماضي أوليفر. بدأت عدة معلومات تتجمع معاً. جاءت المفاجآت بسرعة وأصبحت الحقيقة أقرب. بدأ لغز أوليفر ينكشف أخيراً.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "clue",
                    pronunciation = "/kluː/",
                    definition = "A fact that helps solve a mystery.",
                    french = "indice",
                    arabic = "دليل",
                    example = "The clue is important."
                ),
                OliverVocabulary(
                    word = "mystery",
                    pronunciation = "/ˈmɪstəri/",
                    definition = "Something difficult to explain or understand.",
                    french = "mystère",
                    arabic = "لغز",
                    example = "The mystery is opening."
                ),
                OliverVocabulary(
                    word = "truth",
                    pronunciation = "/truːθ/",
                    definition = "The facts about a situation.",
                    french = "vérité",
                    arabic = "حقيقة",
                    example = "The truth is coming closer."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "What do the discoveries provide?",
                    options = listOf(
                        "Clues",
                        "Money",
                        "A new job"
                    ),
                    correctIndex = 0,
                    explanation = "They provide clues."
                ),
                OliverQuestion(
                    question = "What is getting closer?",
                    options = listOf(
                        "The truth",
                        "A holiday",
                        "A war"
                    ),
                    correctIndex = 0,
                    explanation = "The truth is getting closer."
                )
            )
        ),
        makeChapter(
            number = 42,
            title = "The Artful Dodger in trouble",
            summary = "L’Artful Dodger est arrêté après avoir montré son talent de voleur. Son assurance face à la justice rappelle le monde criminel dont Oliver a tenté de s’éloigner.",
            sceneTitle = "A public character",
            imageEmoji = "🎭",
            englishText = """
The Artful Dodger is arrested after showing his skill as a thief. He faces the law with confidence and humor. His old connection with Oliver reminds readers of the criminal world Oliver escaped. The Dodger becomes a memorable public figure.
""".trimIndent(),
            frenchTranslation = """
L'Artful Dodger est arrêté après avoir montré son talent de voleur. Il affronte la justice avec assurance et humour. Son lien avec Oliver rappelle le monde criminel qu'Oliver a fui. Le Dodger devient un personnage public mémorable.
""".trimIndent(),
            arabicExplanation = """
أُلقي القبض على دودجر الماكر بعد أن أظهر مهارته في السرقة. واجه القانون بثقة وروح فكاهية. ذكّر ارتباطه بأوليفر بالعالم الإجرامي الذي هرب منه أوليفر. أصبح دودجر شخصية عامة لا تنسى.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "arrested",
                    pronunciation = "/əˈrestɪd/",
                    definition = "Taken into official custody.",
                    french = "arrêté",
                    arabic = "معتقل",
                    example = "The Dodger is arrested."
                ),
                OliverVocabulary(
                    word = "confidence",
                    pronunciation = "/ˈkɒnfɪdəns/",
                    definition = "A feeling of certainty about oneself.",
                    french = "confiance",
                    arabic = "ثقة",
                    example = "The Dodger shows confidence."
                ),
                OliverVocabulary(
                    word = "humor",
                    pronunciation = "/ˈhjuːmər/",
                    definition = "The quality of being funny.",
                    french = "humour",
                    arabic = "فكاهة",
                    example = "He uses humor in trouble."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Who is arrested?",
                    options = listOf(
                        "The Artful Dodger",
                        "Harry",
                        "Brownlow"
                    ),
                    correctIndex = 0,
                    explanation = "The Artful Dodger is arrested."
                ),
                OliverQuestion(
                    question = "How does he face the law?",
                    options = listOf(
                        "With confidence and humor",
                        "With silence",
                        "By running away"
                    ),
                    correctIndex = 0,
                    explanation = "He shows confidence and humor."
                )
            )
        ),
        makeChapter(
            number = 43,
            title = "The Artful Dodger gets into trouble",
            summary = "L’Artful Dodger doit répondre de ses actes devant la justice. Son intelligence et son humour ne suffisent pas à éviter les conséquences de la vie criminelle.",
            sceneTitle = "The trial",
            imageEmoji = "⚖️",
            englishText = """
The Artful Dodger faces the consequences of his criminal life. His clever answers cannot protect him from the law. The chapter shows how a childhood shaped by crime can lead to prison and punishment.
""".trimIndent(),
            frenchTranslation = """
L'Artful Dodger affronte les conséquences de sa vie criminelle. Ses réponses habiles ne peuvent pas le protéger contre la justice. Le chapitre montre comment une enfance marquée par le crime peut conduire à la prison et au châtiment.
""".trimIndent(),
            arabicExplanation = """
واجه دودجر الماكر نتائج حياته الإجرامية. لم تستطع إجاباته الذكية أن تحميه من القانون. يوضح الفصل كيف يمكن لطفولة مليئة بالجريمة أن تقود إلى السجن والعقاب.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "trial",
                    pronunciation = "/ˈtraɪəl/",
                    definition = "A legal process in which a case is judged.",
                    french = "procès",
                    arabic = "محاكمة",
                    example = "The Dodger faces a trial."
                ),
                OliverVocabulary(
                    word = "consequence",
                    pronunciation = "/ˈkɒnsɪkwens/",
                    definition = "A result of an action.",
                    french = "conséquence",
                    arabic = "عاقبة",
                    example = "Crime has consequences."
                ),
                OliverVocabulary(
                    word = "punishment",
                    pronunciation = "/ˈpʌnɪʃmənt/",
                    definition = "A penalty for wrongdoing.",
                    french = "punition",
                    arabic = "عقاب",
                    example = "He faces punishment."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "What does the Dodger face?",
                    options = listOf(
                        "A trial",
                        "A holiday",
                        "A promotion"
                    ),
                    correctIndex = 0,
                    explanation = "He faces a trial."
                ),
                OliverQuestion(
                    question = "What theme appears?",
                    options = listOf(
                        "Crime has consequences",
                        "Money solves everything",
                        "School life"
                    ),
                    correctIndex = 0,
                    explanation = "Crime can lead to punishment."
                )
            )
        ),
        makeChapter(
            number = 44,
            title = "Nancy fails to redeem her pledge",
            summary = "Nancy essaie de tenir la promesse faite à Rose d’aider Oliver. Elle transmet des informations malgré la peur, mais reste prisonnière de l’influence de Fagin et de Sikes.",
            sceneTitle = "A dangerous promise",
            imageEmoji = "🌹",
            englishText = """
Nancy promised Rose that she would help Oliver. She tries to keep her promise by sharing information, but fear and danger surround her. She cannot yet break free from Sikes and Fagin. Her courage puts her in great danger.
""".trimIndent(),
            frenchTranslation = """
Nancy a promis à Rose qu'elle aiderait Oliver. Elle essaie de tenir sa promesse en partageant des informations, mais la peur et le danger l'entourent. Elle ne peut pas encore échapper à Sikes et Fagin. Son courage la met en grand danger.
""".trimIndent(),
            arabicExplanation = """
وعدت نانسي روز بأنها ستساعد أوليفر. حاولت الوفاء بوعدها من خلال مشاركة المعلومات، لكن الخوف والخطر أحاطا بها. لم تستطع بعد التحرر من سايكس وفاجن. جعلتها شجاعتها في خطر كبير.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "pledge",
                    pronunciation = "/pledʒ/",
                    definition = "A serious promise.",
                    french = "promesse solennelle",
                    arabic = "عهد",
                    example = "Nancy made a pledge."
                ),
                OliverVocabulary(
                    word = "courage",
                    pronunciation = "/ˈkʌrɪdʒ/",
                    definition = "The ability to act despite fear.",
                    french = "courage",
                    arabic = "شجاعة",
                    example = "Nancy shows courage."
                ),
                OliverVocabulary(
                    word = "danger",
                    pronunciation = "/ˈdeɪndʒər/",
                    definition = "The possibility of harm.",
                    french = "danger",
                    arabic = "خطر",
                    example = "Nancy faces danger."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "To whom did Nancy make a promise?",
                    options = listOf(
                        "Rose",
                        "Fagin",
                        "Bumble"
                    ),
                    correctIndex = 0,
                    explanation = "She promised Rose."
                ),
                OliverQuestion(
                    question = "Why is Nancy in danger?",
                    options = listOf(
                        "She is helping Oliver",
                        "She steals money",
                        "She leaves London"
                    ),
                    correctIndex = 0,
                    explanation = "She is helping Oliver."
                )
            )
        ),
        makeChapter(
            number = 45,
            title = "Noah Claypole works for Fagin",
            summary = "Noah Claypole rejoint Fagin et reçoit une mission secrète : surveiller Nancy. Il accepte pour obtenir de l’argent et une protection, plaçant Nancy sous une menace croissante.",
            sceneTitle = "A secret mission",
            imageEmoji = "👁️",
            englishText = """
Noah Claypole leaves his old life and works with Fagin. Fagin gives him a secret mission: observe Nancy and learn what she is doing. Noah accepts because he wants money and protection. Nancy is now being watched closely.
""".trimIndent(),
            frenchTranslation = """
Noah Claypole quitte son ancienne vie et travaille avec Fagin. Fagin lui confie une mission secrète : surveiller Nancy et découvrir ce qu'elle fait. Noah accepte parce qu'il veut de l'argent et de la protection. Nancy est désormais étroitement surveillée.
""".trimIndent(),
            arabicExplanation = """
ترك نواه كلايبول حياته السابقة وعمل مع فاجن. كلفه فاجن بمهمة سرية: مراقبة نانسي ومعرفة ما تفعله. وافق نواه لأنه يريد المال والحماية. أصبحت نانسي تحت المراقبة الشديدة.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "mission",
                    pronunciation = "/ˈmɪʃən/",
                    definition = "A task given to someone to achieve a goal.",
                    french = "mission",
                    arabic = "مهمة",
                    example = "Noah has a secret mission."
                ),
                OliverVocabulary(
                    word = "observe",
                    pronunciation = "/əbˈzɜːrv/",
                    definition = "To watch carefully.",
                    french = "observer",
                    arabic = "يراقب",
                    example = "Noah observes Nancy."
                ),
                OliverVocabulary(
                    word = "protection",
                    pronunciation = "/prəˈtekʃən/",
                    definition = "The act of keeping someone safe.",
                    french = "protection",
                    arabic = "حماية",
                    example = "Noah wants protection."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Who employs Noah?",
                    options = listOf(
                        "Fagin",
                        "Brownlow",
                        "Harry"
                    ),
                    correctIndex = 0,
                    explanation = "Fagin employs Noah."
                ),
                OliverQuestion(
                    question = "What is Noah told to do?",
                    options = listOf(
                        "Watch Nancy",
                        "Find Oliver's school",
                        "Meet Rose"
                    ),
                    correctIndex = 0,
                    explanation = "He is told to watch Nancy."
                )
            )
        ),
        makeChapter(
            number = 46,
            title = "The appointment is kept",
            summary = "Nancy rencontre secrètement des personnes qui veulent aider Oliver. Elle leur révèle ce qu’elle sait sur Fagin, Monks et le complot contre Oliver, au péril de sa vie.",
            sceneTitle = "Nancy meets the right people",
            imageEmoji = "🤫",
            englishText = """
Nancy secretly meets people who want to help Oliver. She explains what she knows about Fagin, Monks, and the plan against Oliver. The meeting is risky, but Nancy keeps her promise. Her information gives Brownlow a way to act.
""".trimIndent(),
            frenchTranslation = """
Nancy rencontre secrètement des personnes qui veulent aider Oliver. Elle explique ce qu'elle sait sur Fagin, Monks et le plan contre Oliver. La rencontre est risquée, mais Nancy tient sa promesse. Ses informations donnent à Brownlow un moyen d'agir.
""".trimIndent(),
            arabicExplanation = """
التقت نانسي سراً بأشخاص يريدون مساعدة أوليفر. شرحت ما تعرفه عن فاجن ومونكس والخطة ضد أوليفر. كان اللقاء خطيراً، لكنها أوفت بوعدها. منحت معلوماتها براونلو وسيلة للتحرك.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "appointment",
                    pronunciation = "/əˈpɔɪntmənt/",
                    definition = "A planned meeting.",
                    french = "rendez-vous",
                    arabic = "موعد",
                    example = "Nancy keeps the appointment."
                ),
                OliverVocabulary(
                    word = "confession",
                    pronunciation = "/kənˈfeʃən/",
                    definition = "A statement admitting what one knows or has done.",
                    french = "aveu",
                    arabic = "اعتراف",
                    example = "Nancy gives information like a confession."
                ),
                OliverVocabulary(
                    word = "act",
                    pronunciation = "/ækt/",
                    definition = "To do something deliberately.",
                    french = "agir",
                    arabic = "يتصرف",
                    example = "Brownlow can act on the information."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Who meets the helpers?",
                    options = listOf(
                        "Nancy",
                        "Noah",
                        "Sikes"
                    ),
                    correctIndex = 0,
                    explanation = "Nancy keeps the secret appointment."
                ),
                OliverQuestion(
                    question = "What does she provide?",
                    options = listOf(
                        "Important information",
                        "Money",
                        "A new house"
                    ),
                    correctIndex = 0,
                    explanation = "She provides important information."
                )
            )
        ),
        makeChapter(
            number = 47,
            title = "Fatal consequences",
            summary = "Le secret de Nancy est découvert. Sikes apprend qu’elle a parlé de la bande et la tue, faisant de cet événement l’un des tournants les plus sombres de l’histoire.",
            sceneTitle = "Nancy is killed",
            imageEmoji = "🕯️",
            englishText = """
Nancy's secret is discovered. Sikes learns that she has spoken about the gang and becomes violently angry. Nancy is murdered by Sikes. Her death becomes one of the darkest turning points in the story.
""".trimIndent(),
            frenchTranslation = """
Le secret de Nancy est découvert. Sikes apprend qu'elle a parlé du groupe et devient violemment furieux. Nancy est assassinée par Sikes. Sa mort constitue l'un des moments les plus sombres du récit.
""".trimIndent(),
            arabicExplanation = """
اكتُشف سر نانسي. علم سايكس أنها تحدثت عن العصابة فغضب بعنف. قتل سايكس نانسي. أصبحت وفاتها واحدة من أكثر نقاط التحول ظلاماً في القصة.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "fatal",
                    pronunciation = "/ˈfeɪtəl/",
                    definition = "Causing death or serious harm.",
                    french = "fatal",
                    arabic = "قاتل",
                    example = "The consequences are fatal."
                ),
                OliverVocabulary(
                    word = "murder",
                    pronunciation = "/ˈmɜːrdər/",
                    definition = "The crime of deliberately killing someone.",
                    french = "meurtre",
                    arabic = "قتل",
                    example = "Nancy is murdered."
                ),
                OliverVocabulary(
                    word = "turning point",
                    pronunciation = "/ˈtɜːrnɪŋ pɔɪnt/",
                    definition = "A moment when a situation changes greatly.",
                    french = "tournant",
                    arabic = "نقطة تحول",
                    example = "Nancy's death is a turning point."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "What happens to Nancy?",
                    options = listOf(
                        "She is murdered.",
                        "She escapes to France.",
                        "She marries Harry."
                    ),
                    correctIndex = 0,
                    explanation = "Nancy is murdered."
                ),
                OliverQuestion(
                    question = "Who kills her?",
                    options = listOf(
                        "Sikes",
                        "Fagin",
                        "Brownlow"
                    ),
                    correctIndex = 0,
                    explanation = "Sikes kills Nancy."
                )
            )
        ),
        makeChapter(
            number = 48,
            title = "The flight of Sikes",
            summary = "Après le meurtre de Nancy, Sikes fuit Londres. Il est poursuivi par la justice et hanté par son crime, tandis que sa fuite devient de plus en plus désespérée.",
            sceneTitle = "A hunted man",
            imageEmoji = "🏃",
            englishText = """
After the murder, Sikes escapes from London. He is terrified by the crime and by the people searching for him. The journey becomes a desperate flight. Sikes is haunted by what he has done.
""".trimIndent(),
            frenchTranslation = """
Après le meurtre, Sikes s'enfuit de Londres. Il est terrifié par son crime et par ceux qui le recherchent. Son voyage devient une fuite désespérée. Sikes est hanté par ce qu'il a fait.
""".trimIndent(),
            arabicExplanation = """
بعد الجريمة هرب سايكس من لندن. كان مرعوباً من فعلته ومن الأشخاص الذين يبحثون عنه. أصبحت رحلته هروباً يائساً. وظل يطارده ما فعله.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "flight",
                    pronunciation = "/flaɪt/",
                    definition = "The act of escaping quickly.",
                    french = "fuite",
                    arabic = "هروب",
                    example = "Sikes begins his flight."
                ),
                OliverVocabulary(
                    word = "haunted",
                    pronunciation = "/ˈhɔːntɪd/",
                    definition = "Unable to forget something frightening.",
                    french = "hanté",
                    arabic = "مطارد",
                    example = "Sikes is haunted by the murder."
                ),
                OliverVocabulary(
                    word = "desperate",
                    pronunciation = "/ˈdespərət/",
                    definition = "Feeling that there is little hope.",
                    french = "désespéré",
                    arabic = "يائس",
                    example = "He is desperate."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Why does Sikes flee?",
                    options = listOf(
                        "He committed murder.",
                        "He wants a holiday.",
                        "He found a job."
                    ),
                    correctIndex = 0,
                    explanation = "He flees after murdering Nancy."
                ),
                OliverQuestion(
                    question = "How does he feel?",
                    options = listOf(
                        "Desperate and afraid",
                        "Happy",
                        "Safe"
                    ),
                    correctIndex = 0,
                    explanation = "He is desperate and afraid."
                )
            )
        ),
        makeChapter(
            number = 49,
            title = "Monks meets Brownlow",
            summary = "Monks rencontre enfin M. Brownlow et révèle des éléments essentiels de l’histoire familiale d’Oliver. L’identité d’Oliver et les secrets liés à son héritage deviennent plus clairs.",
            sceneTitle = "The hidden history",
            imageEmoji = "📜",
            englishText = """
Monks finally meets Mr. Brownlow. The conversation reveals the family history that connects them to Oliver. Brownlow learns the truth about the inheritance and Monks's actions. Oliver's identity becomes clearer.
""".trimIndent(),
            frenchTranslation = """
Monks rencontre enfin M. Brownlow. La conversation révèle l'histoire familiale qui les relie à Oliver. Brownlow apprend la vérité sur l'héritage et les actions de Monks. L'identité d'Oliver devient plus claire.
""".trimIndent(),
            arabicExplanation = """
التقى مونكس أخيراً بالسيد براونلو. كشف الحديث التاريخ العائلي الذي يربطهما بأوليفر. عرف براونلو الحقيقة عن الميراث وأفعال مونكس. أصبحت هوية أوليفر أوضح.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "inheritance",
                    pronunciation = "/ɪnˈherɪtəns/",
                    definition = "Property or money received from a relative.",
                    french = "héritage",
                    arabic = "ميراث",
                    example = "The inheritance matters."
                ),
                OliverVocabulary(
                    word = "confess",
                    pronunciation = "/kənˈfes/",
                    definition = "To admit something true.",
                    french = "avouer",
                    arabic = "يعترف",
                    example = "Monks confesses the truth."
                ),
                OliverVocabulary(
                    word = "identity",
                    pronunciation = "/aɪˈdentəti/",
                    definition = "Who a person truly is.",
                    french = "identité",
                    arabic = "هوية",
                    example = "Oliver's identity is revealed."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Who meets Monks?",
                    options = listOf(
                        "Mr. Brownlow",
                        "Mr. Bumble",
                        "Sikes"
                    ),
                    correctIndex = 0,
                    explanation = "Brownlow meets Monks."
                ),
                OliverQuestion(
                    question = "What becomes clearer?",
                    options = listOf(
                        "Oliver's identity",
                        "A school plan",
                        "A business deal"
                    ),
                    correctIndex = 0,
                    explanation = "Oliver's identity becomes clearer."
                )
            )
        ),
        makeChapter(
            number = 50,
            title = "The pursuit and escape",
            summary = "Une vaste recherche est organisée pour retrouver Sikes. Les poursuivants suivent les indices dans Londres tandis que Sikes tente désespérément de leur échapper.",
            sceneTitle = "Chasing Sikes",
            imageEmoji = "🔦",
            englishText = """
A large search is organized for Sikes. People follow clues and streets through London. Sikes attempts to escape, but the pressure increases. His flight comes to a tragic end.
""".trimIndent(),
            frenchTranslation = """
Une grande recherche est organisée pour retrouver Sikes. Les poursuivants suivent des indices dans les rues de Londres. Sikes tente de s'échapper, mais la pression augmente. Sa fuite se termine tragiquement.
""".trimIndent(),
            arabicExplanation = """
نُظمت مطاردة كبيرة للقبض على سايكس. اتبع المطاردون الأدلة في شوارع لندن. حاول سايكس الهرب لكن الضغط ازداد. انتهى هروبه نهاية مأساوية.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "pursuit",
                    pronunciation = "/pərˈsuːt/",
                    definition = "The act of chasing someone.",
                    french = "poursuite",
                    arabic = "مطاردة",
                    example = "The pursuit continues."
                ),
                OliverVocabulary(
                    word = "clue",
                    pronunciation = "/kluː/",
                    definition = "Information that helps find something.",
                    french = "indice",
                    arabic = "دليل",
                    example = "The police follow a clue."
                ),
                OliverVocabulary(
                    word = "tragic",
                    pronunciation = "/ˈtrædʒɪk/",
                    definition = "Very sad and involving disaster.",
                    french = "tragique",
                    arabic = "مأساوي",
                    example = "The ending is tragic."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Who is being pursued?",
                    options = listOf(
                        "Sikes",
                        "Oliver",
                        "Brownlow"
                    ),
                    correctIndex = 0,
                    explanation = "Sikes is pursued."
                ),
                OliverQuestion(
                    question = "How does the pursuit end?",
                    options = listOf(
                        "Tragically",
                        "With a wedding",
                        "With a new job"
                    ),
                    correctIndex = 0,
                    explanation = "It ends tragically."
                )
            )
        ),
        makeChapter(
            number = 51,
            title = "More mysteries are explained",
            summary = "Les derniers secrets de la famille d’Oliver sont expliqués. Brownlow lui raconte ce qui est arrivé à ses parents et pourquoi Monks voulait cacher la vérité.",
            sceneTitle = "The family truth",
            imageEmoji = "🧬",
            englishText = """
Oliver's story is explained more fully. Brownlow tells Oliver what happened to his father and mother and why Monks tried to keep the truth hidden. Oliver also hears about his own family connections. The mystery is nearly complete.
""".trimIndent(),
            frenchTranslation = """
L'histoire d'Oliver est expliquée plus complètement. Brownlow raconte ce qui est arrivé à ses parents et pourquoi Monks a voulu cacher la vérité. Oliver entend aussi parler de ses liens familiaux. Le mystère est presque résolu.
""".trimIndent(),
            arabicExplanation = """
تم شرح قصة أوليفر بشكل أكثر اكتمالاً. أخبر براونلو أوليفر بما حدث لوالديه ولماذا حاول مونكس إخفاء الحقيقة. كما عرف أوليفر روابطه العائلية. أصبح اللغز قريباً من الحل.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "explanation",
                    pronunciation = "/ˌekspləˈneɪʃən/",
                    definition = "A statement that makes something clear.",
                    french = "explication",
                    arabic = "تفسير",
                    example = "Brownlow gives an explanation."
                ),
                OliverVocabulary(
                    word = "parentage",
                    pronunciation = "/ˈpeərəntɪdʒ/",
                    definition = "A person's family origin or parents.",
                    french = "filiation",
                    arabic = "أصل العائلة",
                    example = "Oliver learns about his parentage."
                ),
                OliverVocabulary(
                    word = "mystery",
                    pronunciation = "/ˈmɪstəri/",
                    definition = "Something not yet fully understood.",
                    french = "mystère",
                    arabic = "لغز",
                    example = "The mystery is nearly solved."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Who explains the past?",
                    options = listOf(
                        "Brownlow",
                        "Sikes",
                        "Fagin"
                    ),
                    correctIndex = 0,
                    explanation = "Brownlow explains the past."
                ),
                OliverQuestion(
                    question = "What is nearly solved?",
                    options = listOf(
                        "The mystery",
                        "A school exam",
                        "A business problem"
                    ),
                    correctIndex = 0,
                    explanation = "The mystery is nearly solved."
                )
            )
        ),
        makeChapter(
            number = 52,
            title = "Fagin's last night alive",
            summary = "Fagin attend son exécution en prison. Brownlow et Oliver viennent chercher les dernières informations importantes, tandis que Fagin apparaît désespéré et terrifié.",
            sceneTitle = "The prison",
            imageEmoji = "🔐",
            englishText = """
Fagin waits in prison for his execution. Brownlow and Oliver visit him to learn what happened to important papers. Fagin is frightened and desperate. The scene contrasts with Oliver's new safety and future.
""".trimIndent(),
            frenchTranslation = """
Fagin attend en prison son exécution. Brownlow et Oliver lui rendent visite pour savoir ce qu'il est advenu de documents importants. Fagin est effrayé et désespéré. La scène contraste avec la nouvelle sécurité et l'avenir d'Oliver.
""".trimIndent(),
            arabicExplanation = """
انتظر فاجن في السجن تنفيذ حكمه. زاره براونلو وأوليفر لمعرفة ما حدث للوثائق المهمة. كان فاجن خائفاً ويائساً. يقارن المشهد بين نهايته وبين أمان أوليفر ومستقبله الجديد.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "prison",
                    pronunciation = "/ˈprɪzən/",
                    definition = "A place where people are kept as punishment.",
                    french = "prison",
                    arabic = "سجن",
                    example = "Fagin is in prison."
                ),
                OliverVocabulary(
                    word = "execution",
                    pronunciation = "/ˌeksɪˈkjuːʃən/",
                    definition = "The carrying out of a death sentence.",
                    french = "exécution",
                    arabic = "إعدام",
                    example = "Fagin awaits execution."
                ),
                OliverVocabulary(
                    word = "desperate",
                    pronunciation = "/ˈdespərət/",
                    definition = "Without hope and extremely afraid.",
                    french = "désespéré",
                    arabic = "يائس",
                    example = "Fagin is desperate."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Where is Fagin?",
                    options = listOf(
                        "In prison",
                        "At Brownlow's house",
                        "In the countryside"
                    ),
                    correctIndex = 0,
                    explanation = "Fagin is in prison."
                ),
                OliverQuestion(
                    question = "What does Brownlow want?",
                    options = listOf(
                        "Important information",
                        "Money",
                        "A new business"
                    ),
                    correctIndex = 0,
                    explanation = "He wants information about the papers."
                )
            )
        ),
        makeChapter(
            number = 53,
            title = "And last",
            summary = "Le dernier chapitre présente le destin des principaux personnages. Oliver retrouve son identité, son héritage et une famille auprès de Brownlow, ouvrant enfin une vie sûre et pleine d’espoir.",
            sceneTitle = "Oliver's new life",
            imageEmoji = "🏡",
            englishText = """
The final chapter tells the fate of the main characters. Oliver's identity and inheritance are secured, and he is adopted by Brownlow. Monks loses his share through his own actions, while other characters find different endings. Oliver finally has a safe home and a hopeful future.
""".trimIndent(),
            frenchTranslation = """
Le dernier chapitre raconte le destin des principaux personnages. L'identité et l'héritage d'Oliver sont assurés et Brownlow l'adopte. Monks perd sa part à cause de ses propres actions, tandis que les autres personnages connaissent des destins différents. Oliver a enfin un foyer sûr et un avenir plein d'espoir.
""".trimIndent(),
            arabicExplanation = """
يروي الفصل الأخير مصير الشخصيات الرئيسية. تم تثبيت هوية أوليفر وميراثه وتبنّاه براونلو. فقد مونكس نصيبه بسبب أفعاله، بينما كانت نهايات الشخصيات الأخرى مختلفة. أصبح لدى أوليفر أخيراً بيت آمن ومستقبل مليء بالأمل.
""".trimIndent(),
            vocabulary = listOf(
                OliverVocabulary(
                    word = "inheritance",
                    pronunciation = "/ɪnˈherɪtəns/",
                    definition = "Money or property received from family.",
                    french = "héritage",
                    arabic = "ميراث",
                    example = "Oliver receives his inheritance."
                ),
                OliverVocabulary(
                    word = "adopt",
                    pronunciation = "/əˈdɒpt/",
                    definition = "To legally take a child as one's own.",
                    french = "adopter",
                    arabic = "يتبنى",
                    example = "Brownlow adopts Oliver."
                ),
                OliverVocabulary(
                    word = "hopeful",
                    pronunciation = "/ˈhoʊpfəl/",
                    definition = "Feeling that good things may happen.",
                    french = "plein d'espoir",
                    arabic = "مليء بالأمل",
                    example = "Oliver has a hopeful future."
                )
            ),
            questions = listOf(
                OliverQuestion(
                    question = "Who adopts Oliver?",
                    options = listOf(
                        "Mr. Brownlow",
                        "Fagin",
                        "Sikes"
                    ),
                    correctIndex = 0,
                    explanation = "Mr. Brownlow adopts Oliver."
                ),
                OliverQuestion(
                    question = "What does Oliver finally have?",
                    options = listOf(
                        "A safe home and a hopeful future",
                        "A life of crime",
                        "A new workhouse"
                    ),
                    correctIndex = 0,
                    explanation = "He finally has safety and hope."
                )
            )
        )
    )

    private fun makeChapter(
        number: Int,
        title: String,
        summary: String,
        sceneTitle: String,
        imageEmoji: String,
        englishText: String,
        frenchTranslation: String,
        arabicExplanation: String,
        vocabulary: List<OliverVocabulary>,
        questions: List<OliverQuestion>
    ): OliverChapter {
        return OliverChapter(
            number = number,
            title = title,
            subtitle = "Chapter $number",
            summary = summary,
            scenes = listOf(
                OliverScene(
                    title = sceneTitle,
                    imageEmoji = imageEmoji,
                    englishText = englishText,
                    comprehension = englishText
                        .split(Regex("(?<=[.!?])\\s+"))
                        .map { it.trim() }
                        .filter { it.isNotEmpty() }
                        .take(5),
                    frenchTranslation = frenchTranslation,
                    arabicExplanation = arabicExplanation,
                    vocabulary = vocabulary,
                    questions = questions
                )
            )
        )
    }
}
// =====================================================================
// ÉCRAN PRINCIPAL
// =====================================================================

@Composable
fun OliverTwistScreen(
    onBack: () -> Unit
) {
    var selectedChapterIndex by remember { mutableIntStateOf(0) }
    var selectedSceneIndex by remember { mutableIntStateOf(0) }
    var currentSection by remember { mutableStateOf("lecture") }

    val context = LocalContext.current
    val fullChapters = remember(context) {
        runCatching {
            OliverTwistFullTextRepository.loadChapters(context)
        }.getOrDefault(emptyList())
    }

    val selectedFullText =
        fullChapters.getOrNull(selectedChapterIndex)?.text

    val chapter = OliverTwistData.chapters.getOrNull(selectedChapterIndex)
    val scene = chapter?.scenes?.getOrNull(selectedSceneIndex)

    if (chapter == null || scene == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Oliver Twist n'est pas disponible.",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onBack) {
                Text("← Retour")
            }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OliverHeader(
            onBack = onBack,
            currentSection = currentSection,
            onSectionChange = { currentSection = it }
        )

        when (currentSection) {
            "lecture" -> {
                OliverLectureDashboard(
                    chapter = chapter,
                    scene = scene,
                    chapterIndex = selectedChapterIndex,
                    sceneIndex = selectedSceneIndex,
                    fullText = selectedFullText,
                    onChapterSelected = { index ->
                        selectedChapterIndex = index
                        selectedSceneIndex = 0
                    },
                    onSceneSelected = { index ->
                        selectedSceneIndex = index
                    }
                )
            }

            "vocabulaire" -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(12.dp)
                ) {
                    OliverSectionTitle(
                        icon = "📚",
                        title = "Vocabulaire important",
                        subtitle = "Les mots essentiels de la scène sélectionnée"
                    )
                    OliverVocabularyBankSection(
                        sourceText = scene.englishText,
                        maxWords = 30
                    )
                }
            }

            "exercices" -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(12.dp)
                ) {
                    OliverSectionTitle(
                        icon = "📝",
                        title = "Exercices",
                        subtitle = "Vérifiez votre compréhension"
                    )
                    OliverExerciseSection(
                        scene = scene,
                        chapterIndex = selectedChapterIndex,
                        sceneIndex = selectedSceneIndex
                    )
                }
            }

            "progression" -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(12.dp)
                ) {
                    OliverSectionTitle(
                        icon = "📊",
                        title = "Progression",
                        subtitle = "Suivez votre progression dans Oliver Twist"
                    )
                    OliverProgressSection(
                        chapterIndex = selectedChapterIndex,
                        sceneIndex = selectedSceneIndex
                    )
                }
            }
        }
    }
}

// =====================================================================
// EN-TÊTE — MODÈLE VALIDÉ
// =====================================================================

@Composable
fun OliverHeader(
    onBack: () -> Unit,
    currentSection: String,
    onSectionChange: (String) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {
        val wide = maxWidth >= 850.dp

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding(),
            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            if (wide) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OliverHeaderBackButton(onBack = onBack)
                    Spacer(modifier = Modifier.width(10.dp))

                    Text(text = "📖", fontSize = 31.sp)
                    Spacer(modifier = Modifier.width(8.dp))

                    Column(
                        modifier = Modifier.width(195.dp)
                    ) {
                        Text(
                            text = "Oliver Twist",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Charles Dickens",
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.86f),
                            fontSize = 12.sp
                        )
                    }

                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        OliverTopButton("📖 Lecture", currentSection == "lecture") {
                            onSectionChange("lecture")
                        }
                        OliverTopButton("📚 Vocabulaire", currentSection == "vocabulaire") {
                            onSectionChange("vocabulaire")
                        }
                        OliverTopButton("📝 Exercices", currentSection == "exercices") {
                            onSectionChange("exercices")
                        }
                        OliverTopButton("📊 Progression", currentSection == "progression") {
                            onSectionChange("progression")
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "⚙️",
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "⋮",
                        fontSize = 28.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            } else {
                Column(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OliverHeaderBackButton(onBack = onBack)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "📖", fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(7.dp))
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Oliver Twist",
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Charles Dickens",
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.84f),
                                fontSize = 11.sp
                            )
                        }
                        Text(
                            text = "⚙️",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(7.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        OliverTopButton("📖 Lecture", currentSection == "lecture") {
                            onSectionChange("lecture")
                        }
                        OliverTopButton("📚 Vocabulaire", currentSection == "vocabulaire") {
                            onSectionChange("vocabulaire")
                        }
                        OliverTopButton("📝 Exercices", currentSection == "exercices") {
                            onSectionChange("exercices")
                        }
                        OliverTopButton("📊 Progression", currentSection == "progression") {
                            onSectionChange("progression")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OliverHeaderBackButton(
    onBack: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(width = 38.dp, height = 36.dp)
            .clickable { onBack() },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.15f)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "←",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun OliverTopButton(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.72f)
            }
        )
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 8.dp),
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onPrimary
            }
        )
    }
}

// =====================================================================
// TABLEAU DE LECTURE — 3 COLONNES
// =====================================================================

@Composable
fun OliverLectureDashboard(
    chapter: OliverChapter,
    scene: OliverScene,
    chapterIndex: Int,
    sceneIndex: Int,
    fullText: String? = null,
    onChapterSelected: (Int) -> Unit,
    onSceneSelected: (Int) -> Unit
) {
    // =========================================================
    // PROGRESSION DE LA LECTURE
    // =========================================================

    val progressContext = LocalContext.current

    val progressStore =
        remember {
            OliverProgressStore(
                progressContext
            )
        }

    var readingCompleted by remember(
        chapterIndex,
        sceneIndex
    ) {
        mutableStateOf(
            progressStore.isSceneCompleted(
                chapterIndex,
                sceneIndex
            )
        )
    }

    var readingSessionKey by remember(
        chapterIndex,
        sceneIndex
    ) {
        mutableIntStateOf(0)
    }

    val onReadingCompleted: (Int) -> Unit = { score ->
        progressStore.saveReadingScore(
            chapterIndex = chapterIndex,
            sceneIndex = sceneIndex,
            score = score
        )

        progressStore.saveReadingAttempt(
            chapterIndex = chapterIndex,
            sceneIndex = sceneIndex,
            score = score
        )

        progressStore.markSceneCompleted(
            chapterIndex = chapterIndex,
            sceneIndex = sceneIndex
        )

        readingCompleted = true
    }
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val wideLayout = maxWidth >= 900.dp

        if (wideLayout) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.Top
            ) {
                OliverSidebar(
                    chapter = chapter,
                    chapterIndex = chapterIndex,
                    sceneIndex = sceneIndex,
                    onChapterSelected = onChapterSelected,
                    onSceneSelected = onSceneSelected,
                    modifier = Modifier
                        .width(210.dp)
                        .heightIn(min = 620.dp)
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .widthIn(min = 420.dp)
                ) {
                    key(readingSessionKey) {
                        OliverCenterReading(
                            chapter = chapter,
                            scene = scene,
                            chapterIndex = chapterIndex,
                            sceneIndex = sceneIndex,
                            fullText = fullText,
                            onReadingCompleted = onReadingCompleted
                        )
                    }

                    if (readingCompleted) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(
                                containerColor =
                                    MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp)
                            ) {
                                Text(
                                    text = "🏆 Lecture terminée !",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color =
                                        MaterialTheme.colorScheme.primary
                                )

                                Spacer(
                                    modifier = Modifier.height(3.dp)
                                )

                                Text(
                                    text =
                                        "Lecture terminée et analyse de prononciation disponible.",
                                    fontSize = 12.sp
                                )

                                Spacer(
                                    modifier = Modifier.height(7.dp)
                                )

                                Button(
                                    onClick = {
                                        readingCompleted = false
                                        readingSessionKey++
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        "🔄 Refaire la lecture pour améliorer le score"
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                }

                OliverVocabularySidebar(
                    scene = scene,
                    modifier = Modifier
                        .width(250.dp)
                        .heightIn(min = 520.dp)
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            ) {
                OliverCompactNavigation(
                    chapter = chapter,
                    chapterIndex = chapterIndex,
                    sceneIndex = sceneIndex,
                    onChapterSelected = onChapterSelected,
                    onSceneSelected = onSceneSelected
                )

                key(readingSessionKey) {
                    OliverCenterReading(
                        chapter = chapter,
                        scene = scene,
                        chapterIndex = chapterIndex,
                        sceneIndex = sceneIndex,
                        fullText = fullText,
                        onReadingCompleted = onReadingCompleted
                    )
                }

                if (readingCompleted) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor =
                                MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Text(
                                text = "🏆 Lecture terminée !",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color =
                                    MaterialTheme.colorScheme.primary
                            )

                            Spacer(
                                modifier = Modifier.height(3.dp)
                            )

                            Text(
                                text =
                                    "Lecture terminée et analyse de prononciation disponible.",
                                fontSize = 12.sp
                            )

                            Spacer(
                                modifier = Modifier.height(7.dp)
                            )

                            Button(
                                onClick = {
                                    readingCompleted = false
                                    readingSessionKey++
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    "🔄 Refaire la lecture pour améliorer le score"
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                OliverVocabularySidebar(
                    scene = scene,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// =====================================================================
// SIDEBAR GAUCHE — CHAPITRES / SCÈNES
// =====================================================================

@Composable
private fun OliverSidebar(
    chapter: OliverChapter,
    chapterIndex: Int,
    sceneIndex: Int,
    onChapterSelected: (Int) -> Unit,
    onSceneSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = "📖  Chapters",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            OliverTwistData.chapters.forEachIndexed { index, item ->
                val selected = index == chapterIndex

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onChapterSelected(index) },
                    shape = RoundedCornerShape(11.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selected) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.60f)
                        }
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(
                            horizontal = 8.dp,
                            vertical = 7.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "📖",
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.width(7.dp))
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Chapter ${item.number}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = item.title,
                                    fontSize = 13.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            if (selected) {
                                Text(
                                    text = "⌃",
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            } else {
                                Text(
                                    text = "›",
                                    fontSize = 22.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                if (selected) {
                    Spacer(modifier = Modifier.height(4.dp))

                    chapter.scenes.forEachIndexed { scenePos, sceneItem ->
                        val selectedScene = scenePos == sceneIndex

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp)
                                .clickable { onSceneSelected(scenePos) },
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedScene) {
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.82f)
                                } else {
                                    MaterialTheme.colorScheme.surface
                                }
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(
                                    horizontal = 9.dp,
                                    vertical = 8.dp
                                ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Card(
                                    shape = RoundedCornerShape(50.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (selectedScene) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.surfaceVariant
                                        }
                                    )
                                ) {
                                    Box(
                                        modifier = Modifier.size(28.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${scenePos + 1}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (selectedScene) {
                                                MaterialTheme.colorScheme.onPrimary
                                            } else {
                                                MaterialTheme.colorScheme.onSurface
                                            }
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(9.dp))

                                Text(
                                    text = sceneItem.title,
                                    fontSize = 13.sp,
                                    fontWeight = if (selectedScene) {
                                        FontWeight.Bold
                                    } else {
                                        FontWeight.Medium
                                    },
                                    color = if (selectedScene) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onSurface
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(3.dp))
                    }
                }

                Spacer(modifier = Modifier.height(7.dp))
            }
        }
    }
}

// =====================================================================
// NAVIGATION COMPACTE
// =====================================================================
@Composable
fun OliverCompactNavigation(
    chapter: OliverChapter,
    chapterIndex: Int,
    sceneIndex: Int,
    onChapterSelected: (Int) -> Unit,
    onSceneSelected: (Int) -> Unit
) {
    var showChapterMenu by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        // =============================================================
        // SÉLECTEUR DE CHAPITRE COMPACT
        // =============================================================
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showChapterMenu = true
                    },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 14.dp,
                            vertical = 11.dp
                        ),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text = "📖",
                        fontSize = 21.sp
                    )

                    Spacer(
                        modifier = Modifier.width(9.dp)
                    )

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Chapter ${chapter.number}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color =
                                MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = chapter.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Text(
                        text = if (showChapterMenu) {
                            "⌃"
                        } else {
                            "⌄"
                        },
                        fontSize = 22.sp,
                        color =
                            MaterialTheme.colorScheme.primary
                    )
                }
            }

            // =========================================================
            // MENU DES 53 CHAPITRES
            // =========================================================
            DropdownMenu(
                expanded = showChapterMenu,
                onDismissRequest = {
                    showChapterMenu = false
                },
                modifier = Modifier
                    .heightIn(max = 420.dp)
            ) {

                OliverTwistData.chapters.forEachIndexed {
                        index,
                        item ->

                    DropdownMenuItem(
                        text = {
                            Column {

                                Text(
                                    text =
                                        "Chapter ${item.number}",
                                    fontSize = 11.sp,
                                    fontWeight =
                                        FontWeight.Bold,
                                    color =
                                        MaterialTheme
                                            .colorScheme
                                            .primary
                                )

                                Text(
                                    text = item.title,
                                    fontSize = 13.sp,
                                    maxLines = 2,
                                    overflow =
                                        TextOverflow.Ellipsis
                                )
                            }
                        },
                        onClick = {
                            onChapterSelected(index)
                            showChapterMenu = false
                        },
                        leadingIcon = {
                            Text(
                                text =
                                    if (index == chapterIndex) {
                                        "✓"
                                    } else {
                                        "📖"
                                    },
                                fontSize = 16.sp
                            )
                        }
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(7.dp)
        )

        // =============================================================
        // SCÈNES DU CHAPITRE ACTUEL
        // =============================================================
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(11.dp),
            colors = CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.surfaceVariant
                        .copy(alpha = 0.45f)
            )
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {

                Text(
                    text = "Scenes",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color =
                        MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(
                        start = 4.dp,
                        bottom = 5.dp
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(
                            rememberScrollState()
                        ),
                    horizontalArrangement =
                        Arrangement.spacedBy(6.dp)
                ) {

                    chapter.scenes.forEachIndexed {
                            index,
                            sceneItem ->

                        Card(
                            modifier = Modifier.clickable {
                                onSceneSelected(index)
                            },
                            shape = RoundedCornerShape(9.dp),
                            colors = CardDefaults.cardColors(
                                containerColor =
                                    if (index == sceneIndex) {
                                        MaterialTheme
                                            .colorScheme
                                            .primaryContainer
                                    } else {
                                        MaterialTheme
                                            .colorScheme
                                            .surface
                                    }
                            )
                        ) {
                            Text(
                                text =
                                    "${index + 1}. " +
                                            sceneItem.title,
                                modifier = Modifier.padding(
                                    horizontal = 10.dp,
                                    vertical = 8.dp
                                ),
                                fontSize = 12.sp,
                                fontWeight =
                                    if (index == sceneIndex) {
                                        FontWeight.Bold
                                    } else {
                                        FontWeight.Medium
                                    }
                            )
                        }
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )
    }
}

// =====================================================================
// CENTRE — LECTURE
// =====================================================================
@Composable
private fun OliverCenterReading(
    chapter: OliverChapter,
    scene: OliverScene,
    chapterIndex: Int,
    sceneIndex: Int,
    fullText: String? = null,
    onReadingCompleted: (Int) -> Unit = {}
) {
    val readingText =
        fullText?.takeIf { it.isNotBlank() }
            ?: scene.englishText

    var chapterSummaryExpanded by remember(
        chapter.number
    ) {
        mutableStateOf(true)
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {

        // =============================================================
        // PAGINATION
        // =============================================================

        val pageWordLimit =
            if (maxWidth < 600.dp) {
                120
            } else {
                210
            }

        val pages = remember(
            readingText,
            pageWordLimit
        ) {
            splitOliverTextIntoPages(
                text = readingText,
                maxWordsPerPage = pageWordLimit
            )
        }

        var currentPageIndex by remember(
            chapter.number,
            readingText
        ) {
            mutableIntStateOf(0)
        }

        LaunchedEffect(pages.size) {

            if (
                currentPageIndex >=
                pages.size
            ) {
                currentPageIndex =
                    pages.lastIndex
                        .coerceAtLeast(0)
            }
        }

        val currentPageText =
            pages.getOrNull(currentPageIndex)
                ?: pages.firstOrNull()
                ?: scene.englishText

        // =============================================================
        // TRADUCTION
        // =============================================================

        val manualTranslation =
            OliverTwistTranslationData.pages[
                "${chapter.number}_${currentPageIndex + 1}"
            ]

        var frenchTranslation by remember(
            chapter.number,
            currentPageIndex,
            currentPageText
        ) {
            mutableStateOf(
                manualTranslation?.french ?: ""
            )
        }

        var arabicTranslation by remember(
            chapter.number,
            currentPageIndex,
            currentPageText
        ) {
            mutableStateOf(
                manualTranslation?.arabic ?: ""
            )
        }

        var isTranslating by remember(
            chapter.number,
            currentPageIndex,
            currentPageText
        ) {
            mutableStateOf(
                manualTranslation == null
            )
        }

        var translationError by remember(
            chapter.number,
            currentPageIndex,
            currentPageText
        ) {
            mutableStateOf("")
        }

        val translationService =
            remember {
                OliverTranslationService()
            }

        DisposableEffect(
            translationService
        ) {
            onDispose {
                translationService.close()
            }
        }

        LaunchedEffect(
            chapter.number,
            currentPageIndex,
            currentPageText
        ) {

            if (
                manualTranslation != null
            ) {

                frenchTranslation =
                    manualTranslation.french

                arabicTranslation =
                    manualTranslation.arabic

                isTranslating = false
                translationError = ""

            } else {

                isTranslating = true
                translationError = ""

                translationService.translatePage(
                    englishText =
                        currentPageText,

                    onSuccess = {
                            french: String,
                            arabic: String ->

                        frenchTranslation =
                            french

                        arabicTranslation =
                            arabic

                        isTranslating = false
                        translationError = ""
                    },

                    onError = {
                            error: Exception ->

                        isTranslating = false

                        translationError =
                            error.message
                                ?: "Erreur de traduction"
                    }
                )
            }
        }

        // =============================================================
        // SYNCHRONISATION AUDIO / TEXTE
        // =============================================================

        var currentSentenceIndex by remember(
            currentPageText
        ) {
            mutableIntStateOf(-1)
        }

        var currentWordIndex by remember(
            currentPageText
        ) {
            mutableIntStateOf(-1)
        }

        val sentences =
            remember(currentPageText) {

                currentPageText
                    .replace(
                        "\r\n",
                        "\n"
                    )
                    .split(
                        Regex(
                            "\\n\\s*\\n|(?<=[.!?])\\s+"
                        )
                    )
                    .map {
                        it.trim()
                    }
                    .filter {
                        it.isNotEmpty()
                    }
            }

        // =============================================================
        // CONTENU
        // =============================================================

        Column(
            modifier =
                Modifier.fillMaxWidth()
        ) {

            // =========================================================
            // EN-TÊTE
            // =========================================================

            Card(
                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(14.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            MaterialTheme
                                .colorScheme
                                .surface
                    )
            ) {

                Column(
                    modifier =
                        Modifier.padding(10.dp)
                ) {

                    Row(
                        modifier =
                            Modifier.fillMaxWidth(),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Column(
                            modifier =
                                Modifier.weight(1f)
                        ) {

                            Text(
                                text =
                                    "📕 Chapter ${chapter.number}",

                                fontSize = 15.sp,

                                fontWeight =
                                    FontWeight.Bold,

                                color =
                                    MaterialTheme
                                        .colorScheme
                                        .primary
                            )

                            Text(
                                text =
                                    chapter.title,

                                fontSize = 14.sp,

                                maxLines = 1,

                                overflow =
                                    TextOverflow.Ellipsis
                            )
                        }

                        Card(
                            shape =
                                RoundedCornerShape(
                                    20.dp
                                ),

                            colors =
                                CardDefaults
                                    .cardColors(
                                        containerColor =
                                            MaterialTheme
                                                .colorScheme
                                                .primaryContainer
                                    )
                        ) {

                            Text(
                                text =
                                    "Page ${
                                        currentPageIndex + 1
                                    } / ${
                                        pages.size
                                    }",

                                modifier =
                                    Modifier.padding(
                                        horizontal = 10.dp,
                                        vertical = 6.dp
                                    ),

                                fontSize = 12.sp,

                                fontWeight =
                                    FontWeight.Bold,

                                color =
                                    MaterialTheme
                                        .colorScheme
                                        .primary
                            )
                        }
                    }

                    Spacer(
                        modifier =
                            Modifier.height(5.dp)
                    )

                    Text(
                        text =
                            scene.title,

                        fontSize = 23.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            MaterialTheme
                                .colorScheme
                                .primary
                    )

                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )

                    if (fullText != null) {

                        Text(
                            text =
                                "📚 Texte intégral • lecture paginée",

                            fontSize = 11.sp,

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .primary
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )

                    // =================================================
                    // RÉSUMÉ DU CHAPITRE
                    // =================================================

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor =
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.60f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(11.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        chapterSummaryExpanded =
                                            !chapterSummaryExpanded
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "📘 Résumé du chapitre",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.weight(1f)
                                )

                                Text(
                                    text = if (chapterSummaryExpanded) "⌃" else "⌄",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            if (chapterSummaryExpanded) {
                                Spacer(modifier = Modifier.height(7.dp))

                                Text(
                                    text = chapter.summary,
                                    fontSize = 14.sp,
                                    lineHeight = 21.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // =================================================
                    // ILLUSTRATION ACTUELLE
                    // =================================================

                    OliverSceneIllustration(
                        scene = scene
                    )

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )

                    // =================================================
                    // PROGRESSION
                    // =================================================

                    LinearProgressIndicator(
                        progress =
                            if (pages.isEmpty()) {
                                0f
                            } else {
                                (
                                        currentPageIndex + 1
                                        ).toFloat() /
                                        pages.size.toFloat()
                            },

                        modifier =
                            Modifier.fillMaxWidth()
                    )

                    Spacer(
                        modifier =
                            Modifier.height(5.dp)
                    )

                    Row(
                        modifier =
                            Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        Text(
                            text =
                                "${
                                    currentPageText
                                        .split(
                                            Regex("\\s+")
                                        )
                                        .count {
                                            it.isNotBlank()
                                        }
                                } mots",

                            fontSize = 11.sp,

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .onSurfaceVariant
                        )

                        Text(
                            text =
                                "${
                                    (
                                            (
                                                    currentPageIndex + 1
                                                    ).toFloat() /
                                                    pages.size
                                                        .coerceAtLeast(1)
                                            ) * 100
                                        .toInt()} %",

                            fontSize = 11.sp,

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .primary
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.height(5.dp)
                    )

                    // =================================================
                    // TEXTE
                    // =================================================

                    Card(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(320.dp),

                        shape =
                            RoundedCornerShape(12.dp),

                        colors =
                            CardDefaults
                                .cardColors(
                                    containerColor =
                                        MaterialTheme
                                            .colorScheme
                                            .surfaceVariant
                                            .copy(
                                                alpha = 0.55f
                                            )
                                )
                    ) {

                        Column(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .verticalScroll(
                                        rememberScrollState()
                                    )
                                    .padding(13.dp)
                        ) {

                            Row(
                                modifier =
                                    Modifier.fillMaxWidth(),

                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {

                                Text(
                                    text =
                                        "📖 Reading",

                                    fontSize = 15.sp,

                                    fontWeight =
                                        FontWeight.Bold,

                                    color =
                                        MaterialTheme
                                            .colorScheme
                                            .primary
                                )

                                Spacer(
                                    modifier =
                                        Modifier.weight(1f)
                                )

                                Text(
                                    text =
                                        "${currentPageIndex + 1}/${pages.size}",

                                    fontSize = 11.sp,

                                    fontWeight =
                                        FontWeight.Bold,

                                    color =
                                        MaterialTheme
                                            .colorScheme
                                            .onSurfaceVariant
                                )
                            }

                            Spacer(
                                modifier =
                                    Modifier.height(8.dp)
                            )

                            sentences.forEachIndexed {
                                    sentenceIndex,
                                    sentence ->

                                val activeSentence =
                                    sentenceIndex ==
                                            currentSentenceIndex

                                val annotated =
                                    buildWordHighlightedText(
                                        sentence =
                                            sentence,

                                        activeWordIndex =
                                            if (
                                                activeSentence
                                            ) {
                                                currentWordIndex
                                            } else {
                                                -1
                                            },

                                        normalColor =
                                            MaterialTheme
                                                .colorScheme
                                                .onSurface,

                                        activeColor =
                                            MaterialTheme
                                                .colorScheme
                                                .primary,

                                        activeBackground =
                                            MaterialTheme
                                                .colorScheme
                                                .primaryContainer
                                    )

                                Text(
                                    text =
                                        annotated,

                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                horizontal = 8.dp,
                                                vertical = 7.dp
                                            ),

                                    fontSize = 16.sp,

                                    lineHeight = 25.sp
                                )

                                if (
                                    sentenceIndex <
                                    sentences.lastIndex
                                ) {

                                    Spacer(
                                        modifier =
                                            Modifier.height(5.dp)
                                    )
                                }
                            }

                            // =================================================
                            // NAVIGATION
                            // =================================================

                            Spacer(
                                modifier =
                                    Modifier.height(12.dp)
                            )

                            Row(
                                modifier =
                                    Modifier.fillMaxWidth(),

                                horizontalArrangement =
                                    Arrangement.spacedBy(
                                        8.dp
                                    )
                            ) {

                                Button(
                                    onClick = {

                                        if (
                                            currentPageIndex > 0
                                        ) {

                                            currentPageIndex--

                                            currentSentenceIndex =
                                                -1

                                            currentWordIndex =
                                                -1
                                        }
                                    },

                                    enabled =
                                        currentPageIndex > 0,

                                    modifier =
                                        Modifier.weight(1f)
                                ) {

                                    Text(
                                        "‹ Précédent"
                                    )
                                }

                                Button(
                                    onClick = {

                                        if (
                                            currentPageIndex <
                                            pages.lastIndex
                                        ) {

                                            currentPageIndex++

                                            currentSentenceIndex =
                                                -1

                                            currentWordIndex =
                                                -1
                                        }
                                    },

                                    enabled =
                                        currentPageIndex <
                                                pages.lastIndex,

                                    modifier =
                                        Modifier.weight(1f)
                                ) {

                                    Text(
                                        "Suivant ›"
                                    )
                                }
                            }
                        }
                    }

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )


                    // =================================================
                    // AUDIO
                    // =================================================

                    OliverAudioPlayer(
                        scene = scene,
                        chapterIndex = chapterIndex,
                        sceneIndex = sceneIndex,

                        textOverride =
                            currentPageText,

                        onWordChanged = {
                                sentenceIndex,
                                wordIndex ->

                            currentSentenceIndex =
                                sentenceIndex

                            currentWordIndex =
                                wordIndex
                        },

                        onReadingCompleted =
                            onReadingCompleted
                    )
                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )

                    // =================================================
                    // TRADUCTION FRANÇAISE + ARABE
                    // =================================================

                    if (
                        isTranslating ||
                        frenchTranslation.isNotBlank() ||
                        arabicTranslation.isNotBlank() ||
                        translationError.isNotBlank()
                    ) {

                        Spacer(
                            modifier =
                                Modifier.height(10.dp)
                        )

                        Card(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(210.dp),

                            shape =
                                RoundedCornerShape(12.dp),

                            colors =
                                CardDefaults.cardColors(
                                    containerColor =
                                        MaterialTheme
                                            .colorScheme
                                            .surfaceVariant
                                )
                        ) {

                            Column(
                                modifier =
                                    Modifier
                                        .fillMaxSize()
                                        .verticalScroll(
                                            rememberScrollState()
                                        )
                                        .padding(14.dp)
                            ) {

                                Text(
                                    text =
                                        "🇫🇷 Français",

                                    fontSize = 17.sp,

                                    fontWeight =
                                        FontWeight.Bold,

                                    color =
                                        MaterialTheme
                                            .colorScheme
                                            .primary
                                )

                                Spacer(
                                    modifier =
                                        Modifier.height(8.dp)
                                )

                                when {

                                    isTranslating -> {

                                        Text(
                                            text =
                                                "Traduction en cours...",

                                            fontSize = 15.sp,

                                            color =
                                                MaterialTheme
                                                    .colorScheme
                                                    .onSurfaceVariant
                                        )
                                    }

                                    frenchTranslation
                                        .isNotBlank() -> {

                                        Text(
                                            text =
                                                frenchTranslation,

                                            fontSize = 15.sp,

                                            lineHeight = 23.sp
                                        )
                                    }
                                }

                                Spacer(
                                    modifier =
                                        Modifier.height(16.dp)
                                )

                                Text(
                                    text =
                                        "🇲🇦 العربية",

                                    fontSize = 17.sp,

                                    fontWeight =
                                        FontWeight.Bold,

                                    color =
                                        MaterialTheme
                                            .colorScheme
                                            .primary
                                )

                                Spacer(
                                    modifier =
                                        Modifier.height(8.dp)
                                )

                                when {

                                    isTranslating -> {

                                        Text(
                                            text =
                                                "الترجمة جارية...",

                                            fontSize = 16.sp,

                                            color =
                                                MaterialTheme
                                                    .colorScheme
                                                    .onSurfaceVariant
                                        )
                                    }

                                    arabicTranslation
                                        .isNotBlank() -> {

                                        Text(
                                            text =
                                                arabicTranslation,

                                            fontSize = 16.sp,

                                            lineHeight = 28.sp
                                        )
                                    }
                                }

                                if (
                                    translationError
                                        .isNotBlank()
                                ) {

                                    Spacer(
                                        modifier =
                                            Modifier.height(10.dp)
                                    )

                                    Text(
                                        text =
                                            translationError,

                                        fontSize = 13.sp,

                                        color =
                                            MaterialTheme
                                                .colorScheme
                                                .error
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun splitOliverTextIntoPages(
    text: String,
    maxWordsPerPage: Int
): List<String> {
    val cleaned = text
        .replace("\r\n", "\n")
        .replace("\r", "\n")
        .trim()

    if (cleaned.isBlank()) return emptyList()

    val paragraphs = cleaned
        .split(Regex("\n\\s*\n"))
        .map { it.trim().replace(Regex("\\s+"), " ") }
        .filter { it.isNotBlank() }

    val pages = mutableListOf<String>()
    var current = StringBuilder()
    var currentWords = 0

    fun addCurrentPage() {
        val page = current.toString().trim()
        if (page.isNotBlank()) {
            pages += page
        }
        current = StringBuilder()
        currentWords = 0
    }

    for (paragraph in paragraphs) {
        val paragraphWords = paragraph
            .split(Regex("\\s+"))
            .filter { it.isNotBlank() }

        // Petit paragraphe : on essaie de conserver les paragraphes entiers.
        if (paragraphWords.size <= maxWordsPerPage) {

            if (
                currentWords > 0 &&
                currentWords + paragraphWords.size > maxWordsPerPage
            ) {
                addCurrentPage()
            }

            if (current.isNotEmpty()) {
                current.append("\n\n")
            }

            current.append(paragraph)
            currentWords += paragraphWords.size
            continue
        }

        // Très long paragraphe : découpage phrase par phrase.
        val sentences = paragraph
            .split(Regex("(?<=[.!?])\\s+"))
            .map { it.trim() }
            .filter { it.isNotBlank() }

        for (sentence in sentences) {
            val sentenceWords = sentence
                .split(Regex("\\s+"))
                .filter { it.isNotBlank() }

            if (sentenceWords.size > maxWordsPerPage) {
                // Dernier recours : découpage strict par mots.
                if (currentWords > 0) {
                    addCurrentPage()
                }

                sentenceWords
                    .chunked(maxWordsPerPage)
                    .forEach { chunk ->
                        pages += chunk.joinToString(" ")
                    }

                continue
            }

            if (
                currentWords > 0 &&
                currentWords + sentenceWords.size > maxWordsPerPage
            ) {
                addCurrentPage()
            }

            if (current.isNotEmpty()) {
                current.append(" ")
            }

            current.append(sentence)
            currentWords += sentenceWords.size
        }
    }

    if (currentWords > 0) {
        addCurrentPage()
    }

    return pages
}

// =====================================================================
// ILLUSTRATION
// =====================================================================
@Composable
private fun OliverSceneIllustration(
    scene: OliverScene
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        if (scene.title == "A cold morning") {
            androidx.compose.foundation.Image(
                painter = androidx.compose.ui.res.painterResource(
                    id = R.drawable.oliver_ch1_cold_morning
                ),
                contentDescription = "Illustration de ${scene.title}",
                modifier = Modifier.fillMaxSize(),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = scene.imageEmoji,
                        fontSize = 72.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Illustration de la scène",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// =====================================================================
// PANNEAUX DU BAS — 2 × 2
// =====================================================================

@Composable
fun OliverBottomLearningPanels(
    scene: OliverScene
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {
        val wide = maxWidth >= 620.dp

        if (wide) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OliverInfoCard(
                        title = "💡 Compréhension",
                        colorType = "green",
                        modifier = Modifier.weight(1f)
                    ) {
                        scene.comprehension.take(5).forEach { item ->
                            Text(
                                text = "• $item",
                                fontSize = 12.sp,
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                        }
                    }

                    OliverInfoCard(
                        title = "🇫🇷 Traduction",
                        colorType = "red",
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = scene.frenchTranslation,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OliverInfoCard(
                        title = "🇲🇦 الشرح بالعربية",
                        colorType = "yellow",
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = scene.arabicExplanation,
                            fontSize = 13.sp,
                            lineHeight = 21.sp
                        )
                    }

                    OliverExercisePreview(
                        scene = scene,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OliverInfoCard(
                    title = "💡 Compréhension",
                    colorType = "green"
                ) {
                    scene.comprehension.forEach { item ->
                        Text(
                            text = "• $item",
                            fontSize = 14.sp,
                            lineHeight = 21.sp
                        )
                    }
                }

                OliverInfoCard(
                    title = "🇫🇷 Traduction",
                    colorType = "red"
                ) {
                    Text(
                        text = scene.frenchTranslation,
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                }

                OliverInfoCard(
                    title = "🇲🇦 الشرح بالعربية",
                    colorType = "yellow"
                ) {
                    Text(
                        text = scene.arabicExplanation,
                        fontSize = 15.sp,
                        lineHeight = 24.sp
                    )
                }

                OliverExercisePreview(scene = scene)
            }
        }
    }
}

@Composable
fun OliverExercisePreview(
    scene: OliverScene,
    modifier: Modifier = Modifier
) {
    val question = scene.questions.firstOrNull()

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = "📝 Petit exercice",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            if (question != null) {
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "1. ${question.question}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 19.sp
                )

                Spacer(modifier = Modifier.height(5.dp))

                question.options.forEachIndexed { index, option ->
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Card(
                            shape = RoundedCornerShape(50.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Box(
                                modifier = Modifier.size(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = when (index) {
                                        0 -> "A"
                                        1 -> "B"
                                        2 -> "C"
                                        3 -> "D"
                                        else -> ""
                                    },
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = option,
                            fontSize = 12.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

// =====================================================================
// VOCABULAIRE — COLONNE DROITE
// =====================================================================

@Composable
fun OliverVocabularySidebar(
    scene: OliverScene,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = "📚  Vocabulaire important",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(7.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                scene.vocabulary.forEachIndexed { index, vocabulary ->
                    OliverVocabularyCard(
                        vocabulary = vocabulary,
                        showExample = false
                    )

                    if (index < scene.vocabulary.lastIndex) {
                        Spacer(modifier = Modifier.height(7.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun OliverVocabularySection(
    scene: OliverScene,
    showExamples: Boolean = true
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        scene.vocabulary.forEachIndexed { index, vocabulary ->
            OliverVocabularyCard(
                vocabulary = vocabulary,
                showExample = showExamples
            )

            if (index < scene.vocabulary.lastIndex) {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
@Composable
fun OliverVocabularyCard(
    vocabulary: OliverVocabulary,
    showExample: Boolean
) {
    val context = LocalContext.current

    val icon = when (vocabulary.word.lowercase()) {
        "workhouse" -> "🏚️"
        "poor" -> "👦"
        "cold" -> "❄️"
        "birth" -> "👶"
        "family" -> "👨‍👩‍👧‍👦"
        "hungry" -> "🍲"
        "ask" -> "🙋"
        "afraid" -> "😨"
        else -> "📘"
    }

    var ttsReady by remember(vocabulary.word) {
        mutableStateOf(false)
    }

    var isSpeaking by remember(vocabulary.word) {
        mutableStateOf(false)
    }

    val handler = remember(vocabulary.word) {
        android.os.Handler(android.os.Looper.getMainLooper())
    }

    val textToSpeech = remember(vocabulary.word) {
        android.speech.tts.TextToSpeech(context) { status ->
            handler.post {
                ttsReady =
                    status == android.speech.tts.TextToSpeech.SUCCESS
            }
        }
    }

    DisposableEffect(textToSpeech, vocabulary.word) {
        textToSpeech.setOnUtteranceProgressListener(
            object : android.speech.tts.UtteranceProgressListener() {

                override fun onStart(utteranceId: String?) {
                    handler.post {
                        isSpeaking = true
                    }
                }

                override fun onDone(utteranceId: String?) {
                    handler.post {
                        isSpeaking = false
                    }
                }

                override fun onError(utteranceId: String?) {
                    handler.post {
                        isSpeaking = false
                    }
                }
            }
        )

        onDispose {
            try {
                textToSpeech.stop()
                textToSpeech.shutdown()
            } catch (_: Exception) {
            }
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.Top
        ) {

            // ---------------------------------------------------------
            // IMAGE / ILLUSTRATION
            // ---------------------------------------------------------
            Card(
                modifier = Modifier.size(66.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = icon,
                        fontSize = 31.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                // -----------------------------------------------------
                // MOT + BOUTON AUDIO
                // -----------------------------------------------------
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = vocabulary.word,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Card(
                        modifier = Modifier
                            .size(40.dp)
                            .clickable(enabled = ttsReady) {
                                try {
                                    if (isSpeaking) {
                                        textToSpeech.stop()
                                        isSpeaking = false
                                    } else {
                                        textToSpeech.language =
                                            java.util.Locale.US
                                        textToSpeech.setSpeechRate(0.78f)
                                        textToSpeech.setPitch(1.0f)

                                        textToSpeech.speak(
                                            vocabulary.word,
                                            android.speech.tts.TextToSpeech.QUEUE_FLUSH,
                                            null,
                                            "vocab_${vocabulary.word}"
                                        )

                                        isSpeaking = true
                                    }
                                } catch (_: Exception) {
                                    isSpeaking = false
                                }
                            },
                        shape = RoundedCornerShape(50.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSpeaking) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.primaryContainer
                            }
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isSpeaking) "⏹" else "🔊",
                                fontSize = 17.sp,
                                color = if (isSpeaking) {
                                    MaterialTheme.colorScheme.onPrimary
                                } else {
                                    MaterialTheme.colorScheme.primary
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = vocabulary.pronunciation,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(6.dp))

                // -----------------------------------------------------
                // SENS / DEFINITION
                // -----------------------------------------------------
                Text(
                    text = "Meaning / Sens",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = vocabulary.definition,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(5.dp))

                // -----------------------------------------------------
                // FRANÇAIS
                // -----------------------------------------------------
                Text(
                    text = "🇫🇷 Français : ${vocabulary.french}",
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(2.dp))

                // -----------------------------------------------------
                // ARABE
                // -----------------------------------------------------
                Text(
                    text = "🇲🇦 العربية : ${vocabulary.arabic}",
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                // -----------------------------------------------------
                // EXEMPLE
                // -----------------------------------------------------
                if (showExample) {
                    Spacer(modifier = Modifier.height(6.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor =
                                MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Text(
                            text = "Exemple : ${vocabulary.example}",
                            modifier = Modifier.padding(
                                horizontal = 8.dp,
                                vertical = 7.dp
                            ),
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    }
                }
            }
        }
    }
}

// =====================================================================
// TITRE DE SECTION
// =====================================================================

@Composable
fun OliverSectionTitle(
    icon: String,
    title: String,
    subtitle: String
) {
    Column(
        modifier = Modifier.padding(
            start = 2.dp,
            end = 2.dp,
            bottom = 9.dp
        )
    ) {
        Text(
            text = "$icon  $title",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = subtitle,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// =====================================================================
// EXERCICES
// =====================================================================

@Composable
fun OliverExerciseSection(
    scene: OliverScene,
    chapterIndex: Int,
    sceneIndex: Int
) {
    val context = LocalContext.current
    val progressStore = remember { OliverProgressStore(context) }

    // ================================================================
    // 4 ACTIVITÉS
    // ================================================================
    val exerciseModes = listOf(
        "comprehension",
        "true_false",
        "vocabulary",
        "completion"
    )

    val modeTitles = mapOf(
        "comprehension" to "Compréhension",
        "true_false" to "Vrai / Faux",
        "vocabulary" to "Vocabulaire",
        "completion" to "Phrase à compléter"
    )

    val modeIcons = mapOf(
        "comprehension" to "🧠",
        "true_false" to "✅",
        "vocabulary" to "📚",
        "completion" to "✍️"
    )

    var activeMode by remember(scene.title) {
        mutableStateOf("comprehension")
    }

    // ---------------------------------------------------------------
    // COMPRÉHENSION
    // ---------------------------------------------------------------
    var comprehensionIndex by remember(scene.title) {
        mutableIntStateOf(0)
    }
    var comprehensionSelected by remember(scene.title) {
        mutableIntStateOf(-1)
    }
    var comprehensionSubmitted by remember(scene.title) {
        mutableStateOf(false)
    }
    var comprehensionScore by remember(scene.title) {
        mutableIntStateOf(0)
    }
    var comprehensionDone by remember(scene.title) {
        mutableStateOf(scene.questions.isEmpty())
    }

    // ---------------------------------------------------------------
    // VRAI / FAUX
    // Les propositions sont construites à partir des questions de la scène.
    // Une ligne utilise la bonne réponse, la suivante un distracteur.
    // ---------------------------------------------------------------
    val trueFalseItems = remember(scene.title, scene.questions) {
        scene.questions.mapIndexed { index, question ->
            val useCorrectAnswer = index % 2 == 0
            val statement = if (useCorrectAnswer) {
                "À la question « ${question.question} », la réponse est : « ${question.options[question.correctIndex]} »."
            } else {
                val wrongIndex = question.options.indices.firstOrNull {
                    it != question.correctIndex
                } ?: question.correctIndex
                "À la question « ${question.question} », la réponse est : « ${question.options[wrongIndex]} »."
            }

            statement to useCorrectAnswer
        }
    }

    var trueFalseIndex by remember(scene.title) {
        mutableIntStateOf(0)
    }
    var trueFalseSelected by remember(scene.title) {
        mutableIntStateOf(-1)
    }
    var trueFalseSubmitted by remember(scene.title) {
        mutableStateOf(false)
    }
    var trueFalseScore by remember(scene.title) {
        mutableIntStateOf(0)
    }
    var trueFalseDone by remember(scene.title) {
        mutableStateOf(trueFalseItems.isEmpty())
    }

    // ---------------------------------------------------------------
    // VOCABULAIRE
    // ---------------------------------------------------------------
    val vocabularyItems = remember(scene.title, scene.vocabulary) {
        scene.vocabulary.take(6)
    }

    var vocabularyIndex by remember(scene.title) {
        mutableIntStateOf(0)
    }
    var vocabularySelected by remember(scene.title) {
        mutableIntStateOf(-1)
    }
    var vocabularySubmitted by remember(scene.title) {
        mutableStateOf(false)
    }
    var vocabularyScore by remember(scene.title) {
        mutableIntStateOf(0)
    }
    var vocabularyDone by remember(scene.title) {
        mutableStateOf(vocabularyItems.isEmpty())
    }

    // ---------------------------------------------------------------
    // PHRASES À COMPLÉTER
    // ---------------------------------------------------------------
    val completionItems = remember(scene.title, scene.vocabulary) {
        scene.vocabulary
            .take(6)
            .mapNotNull { vocabulary ->
                val example = vocabulary.example.trim()
                if (example.isBlank()) return@mapNotNull null

                val regex = Regex(
                    "\\b${Regex.escape(vocabulary.word)}\\b",
                    RegexOption.IGNORE_CASE
                )

                if (!regex.containsMatchIn(example)) return@mapNotNull null

                val blanked = regex.replaceFirst(example, "_____")
                Triple(vocabulary.word, blanked, vocabulary)
            }
    }

    var completionIndex by remember(scene.title) {
        mutableIntStateOf(0)
    }
    var completionSelected by remember(scene.title) {
        mutableIntStateOf(-1)
    }
    var completionSubmitted by remember(scene.title) {
        mutableStateOf(false)
    }
    var completionScore by remember(scene.title) {
        mutableIntStateOf(0)
    }
    var completionDone by remember(scene.title) {
        mutableStateOf(completionItems.isEmpty())
    }

    val completedModes = listOf(
        comprehensionDone,
        trueFalseDone,
        vocabularyDone,
        completionDone
    ).count { it }

    val totalModes = exerciseModes.size
    val totalQuestions =
        scene.questions.size +
                trueFalseItems.size +
                vocabularyItems.size +
                completionItems.size

    val totalScore =
        comprehensionScore +
                trueFalseScore +
                vocabularyScore +
                completionScore

    val allExercisesFinished =
        completedModes == totalModes

    val globalPercent =
        if (totalQuestions > 0) {
            (totalScore * 100) / totalQuestions
        } else {
            0
        }

    // ================================================================
    // OUTILS
    // ================================================================
    fun resetAllExercises() {
        activeMode = "comprehension"

        comprehensionIndex = 0
        comprehensionSelected = -1
        comprehensionSubmitted = false
        comprehensionScore = 0
        comprehensionDone = scene.questions.isEmpty()

        trueFalseIndex = 0
        trueFalseSelected = -1
        trueFalseSubmitted = false
        trueFalseScore = 0
        trueFalseDone = trueFalseItems.isEmpty()

        vocabularyIndex = 0
        vocabularySelected = -1
        vocabularySubmitted = false
        vocabularyScore = 0
        vocabularyDone = vocabularyItems.isEmpty()

        completionIndex = 0
        completionSelected = -1
        completionSubmitted = false
        completionScore = 0
        completionDone = completionItems.isEmpty()
    }

    fun goToNextMode() {
        val currentIndex =
            exerciseModes.indexOf(activeMode)

        val nextMode =
            exerciseModes.drop(currentIndex + 1)
                .firstOrNull { mode ->
                    when (mode) {
                        "comprehension" -> !comprehensionDone
                        "true_false" -> !trueFalseDone
                        "vocabulary" -> !vocabularyDone
                        "completion" -> !completionDone
                        else -> false
                    }
                }

        if (nextMode != null) {
            activeMode = nextMode
        }
    }

    fun finishAllExercises() {
        progressStore.saveScore(
            chapterIndex = chapterIndex,
            sceneIndex = sceneIndex,
            score = comprehensionScore
        )

        progressStore.markSceneCompleted(
            chapterIndex = chapterIndex,
            sceneIndex = sceneIndex
        )
    }

    // ================================================================
    // INTERFACE
    // ================================================================
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "📝 Exercices",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(3.dp)
                        )

                        Text(
                            text =
                                "Comprendre • réviser • mémoriser • pratiquer",
                            fontSize = 12.sp,
                            color =
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor =
                                MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Text(
                            text = "$completedModes/$totalModes activités",
                            modifier = Modifier.padding(
                                horizontal = 10.dp,
                                vertical = 6.dp
                            ),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                LinearProgressIndicator(
                    progress =
                        if (totalModes > 0) {
                            completedModes.toFloat() /
                                    totalModes.toFloat()
                        } else {
                            0f
                        },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                // ------------------------------------------------------
                // NAVIGATION DES 4 ACTIVITÉS
                // ------------------------------------------------------
                Column(
                    verticalArrangement =
                        Arrangement.spacedBy(6.dp)
                ) {
                    for (rowIndex in 0..1) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement =
                                Arrangement.spacedBy(6.dp)
                        ) {
                            for (columnIndex in 0..1) {
                                val modeIndex =
                                    rowIndex * 2 + columnIndex
                                val mode =
                                    exerciseModes[modeIndex]

                                val done =
                                    when (mode) {
                                        "comprehension" ->
                                            comprehensionDone
                                        "true_false" ->
                                            trueFalseDone
                                        "vocabulary" ->
                                            vocabularyDone
                                        "completion" ->
                                            completionDone
                                        else -> false
                                    }

                                val selected =
                                    activeMode == mode

                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            activeMode = mode
                                        },
                                    shape =
                                        RoundedCornerShape(10.dp),
                                    colors =
                                        CardDefaults.cardColors(
                                            containerColor =
                                                when {
                                                    selected ->
                                                        MaterialTheme
                                                            .colorScheme
                                                            .primaryContainer
                                                    done ->
                                                        MaterialTheme
                                                            .colorScheme
                                                            .secondaryContainer
                                                    else ->
                                                        MaterialTheme
                                                            .colorScheme
                                                            .surface
                                                }
                                        )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(9.dp),
                                        verticalAlignment =
                                            Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text =
                                                modeIcons[mode] ?: "📝",
                                            fontSize = 17.sp
                                        )

                                        Spacer(
                                            modifier =
                                                Modifier.width(7.dp)
                                        )

                                        Column(
                                            modifier =
                                                Modifier.weight(1f)
                                        ) {
                                            Text(
                                                text =
                                                    modeTitles[mode]
                                                        ?: mode,
                                                fontSize = 11.sp,
                                                fontWeight =
                                                    FontWeight.Bold
                                            )

                                            Text(
                                                text = when {
                                                    done -> "✓ Terminé"
                                                    else -> "À faire"
                                                },
                                                fontSize = 9.sp,
                                                color =
                                                    MaterialTheme
                                                        .colorScheme
                                                        .onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                // ------------------------------------------------------
                // ACTIVITÉ 1 — COMPRÉHENSION
                // ------------------------------------------------------
                if (activeMode == "comprehension") {
                    if (scene.questions.isEmpty()) {
                        Text(
                            text = "Aucune question de compréhension pour cette scène.",
                            fontSize = 14.sp
                        )
                        comprehensionDone = true
                    } else if (comprehensionDone) {
                        Text(
                            text = "✅ Compréhension terminée : $comprehensionScore / ${scene.questions.size}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { goToNextMode() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Continuer vers l'activité suivante ›")
                        }
                    } else {
                        val question =
                            scene.questions[comprehensionIndex]

                        Text(
                            text =
                                "Question ${comprehensionIndex + 1} / ${scene.questions.size}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color =
                                MaterialTheme.colorScheme.primary
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = question.question,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 25.sp
                        )

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        question.options.forEachIndexed { index, option ->
                            val isSelected =
                                comprehensionSelected == index
                            val isCorrect =
                                index == question.correctIndex

                            val background =
                                when {
                                    comprehensionSubmitted &&
                                            isCorrect ->
                                        MaterialTheme
                                            .colorScheme
                                            .primaryContainer
                                    comprehensionSubmitted &&
                                            isSelected &&
                                            !isCorrect ->
                                        MaterialTheme
                                            .colorScheme
                                            .errorContainer
                                    isSelected ->
                                        MaterialTheme
                                            .colorScheme
                                            .secondaryContainer
                                    else ->
                                        MaterialTheme
                                            .colorScheme
                                            .surface
                                }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        enabled =
                                            !comprehensionSubmitted
                                    ) {
                                        comprehensionSelected =
                                            index
                                    },
                                shape =
                                    RoundedCornerShape(10.dp),
                                colors =
                                    CardDefaults.cardColors(
                                        containerColor =
                                            background
                                    )
                            ) {
                                Row(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(11.dp),
                                    verticalAlignment =
                                        Alignment.CenterVertically
                                ) {
                                    Text(
                                        text =
                                            when (index) {
                                                0 -> "A"
                                                1 -> "B"
                                                2 -> "C"
                                                3 -> "D"
                                                else -> "•"
                                            },
                                        fontWeight =
                                            FontWeight.Bold,
                                        color =
                                            MaterialTheme
                                                .colorScheme
                                                .primary
                                    )

                                    Spacer(
                                        modifier =
                                            Modifier.width(10.dp)
                                    )

                                    Text(
                                        text = option,
                                        fontSize = 14.sp,
                                        lineHeight = 20.sp
                                    )
                                }
                            }

                            Spacer(
                                modifier =
                                    Modifier.height(6.dp)
                            )
                        }

                        if (comprehensionSubmitted) {
                            Text(
                                text =
                                    if (
                                        comprehensionSelected ==
                                        question.correctIndex
                                    ) {
                                        "✅ Bonne réponse"
                                    } else {
                                        "❌ Réponse incorrecte"
                                    },
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(4.dp)
                            )

                            Text(
                                text = question.explanation,
                                fontSize = 13.sp,
                                lineHeight = 20.sp
                            )
                        }

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        Button(
                            onClick = {
                                if (!comprehensionSubmitted) {
                                    if (
                                        comprehensionSelected ==
                                        question.correctIndex
                                    ) {
                                        comprehensionScore++
                                    }
                                    comprehensionSubmitted = true
                                } else if (
                                    comprehensionIndex <
                                    scene.questions.lastIndex
                                ) {
                                    comprehensionIndex++
                                    comprehensionSelected = -1
                                    comprehensionSubmitted = false
                                } else {
                                    comprehensionDone = true
                                    finishAllExercises()
                                }
                            },
                            enabled =
                                comprehensionSelected != -1,
                            modifier =
                                Modifier.fillMaxWidth()
                        ) {
                            Text(
                                when {
                                    !comprehensionSubmitted ->
                                        "Vérifier la réponse"
                                    comprehensionIndex ==
                                            scene.questions.lastIndex ->
                                        "Terminer la compréhension"
                                    else ->
                                        "Question suivante"
                                }
                            )
                        }
                    }
                }

                // ------------------------------------------------------
                // ACTIVITÉ 2 — VRAI / FAUX
                // ------------------------------------------------------
                if (activeMode == "true_false") {
                    if (trueFalseItems.isEmpty()) {
                        Text(
                            text = "Aucune activité Vrai / Faux disponible.",
                            fontSize = 14.sp
                        )
                        trueFalseDone = true
                    } else if (trueFalseDone) {
                        Text(
                            text =
                                "✅ Vrai / Faux terminé : $trueFalseScore / ${trueFalseItems.size}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { goToNextMode() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Continuer ›")
                        }
                    } else {
                        val item =
                            trueFalseItems[trueFalseIndex]

                        Text(
                            text =
                                "Proposition ${trueFalseIndex + 1} / ${trueFalseItems.size}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color =
                                MaterialTheme.colorScheme.primary
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = item.first,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 24.sp
                        )

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement =
                                Arrangement.spacedBy(8.dp)
                        ) {
                            val choices =
                                listOf("Vrai", "Faux")

                            choices.forEachIndexed { index, label ->
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable(
                                            enabled =
                                                !trueFalseSubmitted
                                        ) {
                                            trueFalseSelected =
                                                index
                                        },
                                    shape =
                                        RoundedCornerShape(10.dp),
                                    colors =
                                        CardDefaults.cardColors(
                                            containerColor =
                                                when {
                                                    trueFalseSubmitted &&
                                                            (
                                                                    index == 0 &&
                                                                            item.second ||
                                                                            index == 1 &&
                                                                            !item.second
                                                                    ) ->
                                                        MaterialTheme
                                                            .colorScheme
                                                            .primaryContainer
                                                    trueFalseSubmitted &&
                                                            trueFalseSelected ==
                                                            index ->
                                                        MaterialTheme
                                                            .colorScheme
                                                            .errorContainer
                                                    trueFalseSelected ==
                                                            index ->
                                                        MaterialTheme
                                                            .colorScheme
                                                            .secondaryContainer
                                                    else ->
                                                        MaterialTheme
                                                            .colorScheme
                                                            .surface
                                                }
                                        )
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(13.dp),
                                        contentAlignment =
                                            Alignment.Center
                                    ) {
                                        Text(
                                            text =
                                                if (index == 0) {
                                                    "✅ Vrai"
                                                } else {
                                                    "❌ Faux"
                                                },
                                            fontSize = 15.sp,
                                            fontWeight =
                                                FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        if (trueFalseSubmitted) {
                            Spacer(
                                modifier =
                                    Modifier.height(8.dp)
                            )

                            Text(
                                text =
                                    if (
                                        (
                                                trueFalseSelected == 0 &&
                                                        item.second
                                                ) ||
                                        (
                                                trueFalseSelected == 1 &&
                                                        !item.second
                                                )
                                    ) {
                                        "✅ Correct"
                                    } else {
                                        "❌ Incorrect"
                                    },
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        Button(
                            onClick = {
                                if (!trueFalseSubmitted) {
                                    val correct =
                                        (
                                                trueFalseSelected == 0 &&
                                                        item.second
                                                ) ||
                                                (
                                                        trueFalseSelected == 1 &&
                                                                !item.second
                                                        )

                                    if (correct) {
                                        trueFalseScore++
                                    }

                                    trueFalseSubmitted = true
                                } else if (
                                    trueFalseIndex <
                                    trueFalseItems.lastIndex
                                ) {
                                    trueFalseIndex++
                                    trueFalseSelected = -1
                                    trueFalseSubmitted = false
                                } else {
                                    trueFalseDone = true
                                    goToNextMode()
                                }
                            },
                            enabled =
                                trueFalseSelected != -1,
                            modifier =
                                Modifier.fillMaxWidth()
                        ) {
                            Text(
                                when {
                                    !trueFalseSubmitted ->
                                        "Vérifier"
                                    trueFalseIndex ==
                                            trueFalseItems.lastIndex ->
                                        "Terminer"
                                    else ->
                                        "Proposition suivante"
                                }
                            )
                        }
                    }
                }

                // ------------------------------------------------------
                // ACTIVITÉ 3 — VOCABULAIRE
                // ------------------------------------------------------
                if (activeMode == "vocabulary") {
                    if (vocabularyItems.isEmpty()) {
                        Text(
                            text = "Aucun exercice de vocabulaire disponible.",
                            fontSize = 14.sp
                        )
                        vocabularyDone = true
                    } else if (vocabularyDone) {
                        Text(
                            text =
                                "✅ Vocabulaire terminé : $vocabularyScore / ${vocabularyItems.size}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { goToNextMode() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Continuer ›")
                        }
                    } else {
                        val vocabulary =
                            vocabularyItems[vocabularyIndex]

                        val askedInFrench =
                            vocabularyIndex % 2 == 0

                        val optionPool =
                            vocabularyItems
                                .map {
                                    if (askedInFrench) {
                                        it.french
                                    } else {
                                        it.arabic
                                    }
                                }
                                .distinct()
                                .take(4)
                                .toMutableList()

                        val correctValue =
                            if (askedInFrench) {
                                vocabulary.french
                            } else {
                                vocabulary.arabic
                            }

                        if (!optionPool.contains(correctValue)) {
                            optionPool.add(correctValue)
                        }

                        val options =
                            optionPool
                                .distinct()
                                .take(4)

                        val correctIndex =
                            options.indexOf(correctValue)

                        Text(
                            text =
                                "Mot ${vocabularyIndex + 1} / ${vocabularyItems.size}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color =
                                MaterialTheme.colorScheme.primary
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text =
                                if (askedInFrench) {
                                    "🇫🇷 Choisissez la traduction française de :"
                                } else {
                                    "🇲🇦 اختر الترجمة العربية للكلمة :"
                                },
                            fontSize = 13.sp,
                            color =
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(
                            modifier = Modifier.height(5.dp)
                        )

                        Text(
                            text = vocabulary.word,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color =
                                MaterialTheme.colorScheme.primary
                        )

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        options.forEachIndexed { index, option ->
                            val isSelected =
                                vocabularySelected == index
                            val isCorrect =
                                index == correctIndex

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        enabled =
                                            !vocabularySubmitted
                                    ) {
                                        vocabularySelected =
                                            index
                                    },
                                shape =
                                    RoundedCornerShape(10.dp),
                                colors =
                                    CardDefaults.cardColors(
                                        containerColor =
                                            when {
                                                vocabularySubmitted &&
                                                        isCorrect ->
                                                    MaterialTheme
                                                        .colorScheme
                                                        .primaryContainer
                                                vocabularySubmitted &&
                                                        isSelected &&
                                                        !isCorrect ->
                                                    MaterialTheme
                                                        .colorScheme
                                                        .errorContainer
                                                isSelected ->
                                                    MaterialTheme
                                                        .colorScheme
                                                        .secondaryContainer
                                                else ->
                                                    MaterialTheme
                                                        .colorScheme
                                                        .surface
                                            }
                                    )
                            ) {
                                Text(
                                    text = option,
                                    modifier = Modifier.padding(11.dp),
                                    fontSize = 14.sp
                                )
                            }

                            Spacer(
                                modifier =
                                    Modifier.height(6.dp)
                            )
                        }

                        if (vocabularySubmitted) {
                            Text(
                                text =
                                    if (
                                        vocabularySelected ==
                                        correctIndex
                                    ) {
                                        "✅ Correct"
                                    } else {
                                        "❌ Incorrect"
                                    },
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(4.dp)
                            )

                            Text(
                                text =
                                    vocabulary.definition,
                                fontSize = 13.sp,
                                lineHeight = 19.sp
                            )
                        }

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        Button(
                            onClick = {
                                if (!vocabularySubmitted) {
                                    if (
                                        vocabularySelected ==
                                        correctIndex
                                    ) {
                                        vocabularyScore++
                                    }
                                    vocabularySubmitted = true
                                } else if (
                                    vocabularyIndex <
                                    vocabularyItems.lastIndex
                                ) {
                                    vocabularyIndex++
                                    vocabularySelected = -1
                                    vocabularySubmitted = false
                                } else {
                                    vocabularyDone = true
                                    goToNextMode()
                                }
                            },
                            enabled =
                                vocabularySelected != -1,
                            modifier =
                                Modifier.fillMaxWidth()
                        ) {
                            Text(
                                when {
                                    !vocabularySubmitted ->
                                        "Vérifier"
                                    vocabularyIndex ==
                                            vocabularyItems.lastIndex ->
                                        "Terminer"
                                    else ->
                                        "Mot suivant"
                                }
                            )
                        }
                    }
                }

                // ------------------------------------------------------
                // ACTIVITÉ 4 — PHRASE À COMPLÉTER
                // ------------------------------------------------------
                if (activeMode == "completion") {
                    if (completionItems.isEmpty()) {
                        Text(
                            text =
                                "Aucune phrase à compléter disponible.",
                            fontSize = 14.sp
                        )
                        completionDone = true
                    } else if (completionDone) {
                        Text(
                            text =
                                "✅ Phrases terminées : $completionScore / ${completionItems.size}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                activeMode =
                                    "comprehension"
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("↻ Revoir les activités")
                        }
                    } else {
                        val item =
                            completionItems[completionIndex]

                        val correctWord =
                            item.first

                        val otherWords =
                            completionItems
                                .map { it.first }
                                .filter {
                                    !it.equals(
                                        correctWord,
                                        ignoreCase = true
                                    )
                                }
                                .take(3)

                        val completionOptions =
                            remember(
                                scene.title,
                                completionIndex
                            ) {
                                (
                                        listOf(correctWord) +
                                                otherWords
                                        )
                                    .distinct()
                                    .shuffled()
                            }

                        val options = completionOptions

                        val correctIndex =
                            options.indexOf(correctWord)

                        Text(
                            text =
                                "Phrase ${completionIndex + 1} / ${completionItems.size}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color =
                                MaterialTheme.colorScheme.primary
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = item.second,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 27.sp
                        )

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        options.forEachIndexed { index, option ->
                            val isSelected =
                                completionSelected == index
                            val isCorrect =
                                index == correctIndex

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        enabled =
                                            !completionSubmitted
                                    ) {
                                        completionSelected =
                                            index
                                    },
                                shape =
                                    RoundedCornerShape(10.dp),
                                colors =
                                    CardDefaults.cardColors(
                                        containerColor =
                                            when {
                                                completionSubmitted &&
                                                        isCorrect ->
                                                    MaterialTheme
                                                        .colorScheme
                                                        .primaryContainer
                                                completionSubmitted &&
                                                        isSelected &&
                                                        !isCorrect ->
                                                    MaterialTheme
                                                        .colorScheme
                                                        .errorContainer
                                                isSelected ->
                                                    MaterialTheme
                                                        .colorScheme
                                                        .secondaryContainer
                                                else ->
                                                    MaterialTheme
                                                        .colorScheme
                                                        .surface
                                            }
                                    )
                            ) {
                                Text(
                                    text = option,
                                    modifier = Modifier.padding(11.dp),
                                    fontSize = 14.sp
                                )
                            }

                            Spacer(
                                modifier =
                                    Modifier.height(6.dp)
                            )
                        }

                        if (completionSubmitted) {
                            Text(
                                text =
                                    if (
                                        completionSelected ==
                                        correctIndex
                                    ) {
                                        "✅ Bonne réponse"
                                    } else {
                                        "❌ Bonne réponse : $correctWord"
                                    },
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        Button(
                            onClick = {
                                if (!completionSubmitted) {
                                    if (
                                        completionSelected ==
                                        correctIndex
                                    ) {
                                        completionScore++
                                    }
                                    completionSubmitted = true
                                } else if (
                                    completionIndex <
                                    completionItems.lastIndex
                                ) {
                                    completionIndex++
                                    completionSelected = -1
                                    completionSubmitted = false
                                } else {
                                    completionDone = true
                                    finishAllExercises()
                                }
                            },
                            enabled =
                                completionSelected != -1,
                            modifier =
                                Modifier.fillMaxWidth()
                        ) {
                            Text(
                                when {
                                    !completionSubmitted ->
                                        "Vérifier"
                                    completionIndex ==
                                            completionItems.lastIndex ->
                                        "Terminer"
                                    else ->
                                        "Phrase suivante"
                                }
                            )
                        }
                    }
                }

                // ------------------------------------------------------
                // BILAN GLOBAL
                // ------------------------------------------------------
                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor =
                            MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = "📊 Bilan des exercices",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color =
                                MaterialTheme.colorScheme.primary
                        )

                        Spacer(
                            modifier = Modifier.height(5.dp)
                        )

                        Text(
                            text =
                                if (totalQuestions > 0) {
                                    "$totalScore / $totalQuestions corrects • $globalPercent %"
                                } else {
                                    "Aucune activité disponible"
                                },
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(6.dp)
                        )

                        LinearProgressIndicator(
                            progress =
                                if (totalQuestions > 0) {
                                    totalScore.toFloat() /
                                            totalQuestions.toFloat()
                                } else {
                                    0f
                                },
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (allExercisesFinished) {
                            Spacer(
                                modifier =
                                    Modifier.height(8.dp)
                            )

                            Text(
                                text =
                                    when {
                                        globalPercent >= 90 ->
                                            "🏆 Excellent travail !"
                                        globalPercent >= 75 ->
                                            "🟢 Très bon niveau."
                                        globalPercent >= 60 ->
                                            "🟡 Bon travail, continuez."
                                        else ->
                                            "🔵 Continuez à réviser."
                                    },
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(8.dp)
                            )

                            Button(
                                onClick = {
                                    resetAllExercises()
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("↻ Refaire tous les exercices")
                            }
                        }
                    }
                }
            }
        }
    }
}

// =====================================================================
// PROGRESSION
// =====================================================================

@Composable
fun OliverProgressSection(
    chapterIndex: Int,
    sceneIndex: Int
) {
    val context = LocalContext.current
    val progressStore = remember { OliverProgressStore(context) }

    var refreshKey by remember { mutableIntStateOf(0) }

    val totalScenes = OliverTwistData.chapters.sumOf { it.scenes.size }

    val completedScenes = OliverTwistData.chapters.flatMapIndexed { chapterPos, chapter ->
        chapter.scenes.mapIndexed { scenePos, _ ->
            progressStore.isSceneCompleted(chapterPos, scenePos)
        }
    }.count { it }

    val currentSceneNumber =
        OliverTwistData.chapters
            .take(chapterIndex)
            .sumOf { it.scenes.size } + sceneIndex + 1

    val progress =
        if (totalScenes > 0) {
            completedScenes.toFloat() / totalScenes.toFloat()
        } else {
            0f
        }

    val progressPercent = (progress * 100).toInt().coerceIn(0, 100)
    val bestScore = progressStore.getBestScore(chapterIndex, sceneIndex)
    val bestReadingScore =
        progressStore.getBestReadingScore(
            chapterIndex = chapterIndex,
            sceneIndex = sceneIndex
        )

    val readingHistory =
        progressStore.getReadingHistory(
            chapterIndex = chapterIndex,
            sceneIndex = sceneIndex
        )

    val readingProgression =
        if (readingHistory.size >= 2) {
            readingHistory.last() - readingHistory.first()
        } else {
            null
        }

    val readingProgressionText =
        when {
            readingProgression == null -> null
            readingProgression > 0 ->
                "📈 +$readingProgression points depuis la première tentative"
            readingProgression < 0 ->
                "📉 $readingProgression points depuis la première tentative"
            else ->
                "➡️ Même niveau que la première tentative"
        }

    val readingProgressionColor =
        when {
            readingProgression == null ->
                MaterialTheme.colorScheme.onSurfaceVariant
            readingProgression > 0 ->
                MaterialTheme.colorScheme.tertiary
            readingProgression < 0 ->
                MaterialTheme.colorScheme.error
            else ->
                MaterialTheme.colorScheme.onSurfaceVariant
        }

    val lastReadingScore =
        readingHistory.lastOrNull()

    val attemptCount =
        readingHistory.size

    val bestReadingHistoryScore =
        readingHistory.maxOrNull() ?: bestReadingScore

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(18.dp)
            ) {
                Text(
                    text = "📖 Votre progression",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Chapter ${chapterIndex + 1} — Scene ${sceneIndex + 1}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(14.dp))

                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$completedScenes / $totalScenes scènes terminées",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "$progressPercent %",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = "📚 Lecture en cours",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = OliverTwistData.chapters[chapterIndex].title,
                            fontSize = 13.sp
                        )

                        Spacer(modifier = Modifier.height(3.dp))

                        Text(
                            text = OliverTwistData
                                .chapters[chapterIndex]
                                .scenes[sceneIndex]
                                .title,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = if (bestScore >= 0) {
                        "🏆 Meilleur score : $bestScore / ${OliverTwistData.chapters[chapterIndex].scenes[sceneIndex].questions.size}"
                    } else {
                        "🏆 Aucun score enregistré pour cette scène"
                    },
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Text(
                            text = "🎙️ Performance de lecture",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(7.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Card(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Text(
                                        text = "Dernier",
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = if (lastReadingScore != null) "$lastReadingScore %" else "—",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            Card(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Text(
                                        text = "Meilleur",
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = if (bestReadingHistoryScore >= 0) "$bestReadingHistoryScore %" else "—",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.tertiary
                                    )
                                }
                            }

                            Card(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Text(
                                        text = "Tentatives",
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = attemptCount.toString(),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        if (readingHistory.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "📈 Dernières tentatives : " +
                                        readingHistory.joinToString(" → ") {
                                            "$it %"
                                        },
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            if (readingProgressionText != null) {
                                Spacer(modifier = Modifier.height(3.dp))

                                Text(
                                    text = readingProgressionText,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = readingProgressionColor
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        progressStore.markSceneCompleted(
                            chapterIndex = chapterIndex,
                            sceneIndex = sceneIndex
                        )
                        refreshKey++
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("✓ Marquer la scène comme étudiée")
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (refreshKey >= 0 && progressPercent >= 100) {
                        "🎉 Oliver Twist terminé !"
                    } else {
                        "Votre progression est conservée même après fermeture de l'application."
                    },
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// =====================================================================
// CARTE INFORMATION
// =====================================================================

@Composable
fun OliverInfoCard(
    title: String,
    colorType: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val containerColor = when (colorType) {
        "green" -> MaterialTheme.colorScheme.secondaryContainer
        "red" -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.55f)
        "blue" -> MaterialTheme.colorScheme.primaryContainer
        "yellow" -> MaterialTheme.colorScheme.tertiaryContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        )
    ) {
        Column(
            modifier = Modifier.padding(13.dp)
        ) {
            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(7.dp))

            content()
        }
    }
}
// =====================================================================
// ÉTAT DE PRONONCIATION
// =====================================================================

private enum class WordPronunciationState {
    NONE,
    CORRECT,
    WRONG,
    UNCERTAIN
}

// =====================================================================
// COMPARAISON DES MOTS
// =====================================================================

private data class WordComparison(
    val expected: String,
    val spoken: String?,
    val correct: Boolean
)

// =====================================================================
// LECTURE LIBRE — ÉTAT VISUEL DES MOTS
// =====================================================================

private enum class FreeReadingWordState {
    PENDING,
    CORRECT,
    APPROXIMATE,
    WRONG
}

// =====================================================================
// TOLÉRANCE AUX PETITES DIFFÉRENCES DE TRANSCRIPTION
// =====================================================================

private fun normalizeWordForComparison(word: String): String {
    return word
        .lowercase(java.util.Locale.US)
        .replace("’", "'")
        .replace(Regex("[^a-zA-Z']"), "")
        .replace("'", "")
}

private fun isEnglishVowel(char: Char): Boolean {
    return char in "aeiou"
}

private fun weightedWordEditDistance(
    first: String,
    second: String
): Double {
    val n = first.length
    val m = second.length

    if (n == 0) {
        return m.toDouble()
    }

    if (m == 0) {
        return n.toDouble()
    }

    val previous = DoubleArray(m + 1) { it.toDouble() }
    val current = DoubleArray(m + 1)

    for (i in 1..n) {
        val currentChar = first[i - 1]
        current[0] = i.toDouble()

        for (j in 1..m) {
            val spokenChar = second[j - 1]

            val substitutionCost = when {
                currentChar == spokenChar -> 0.0
                isEnglishVowel(currentChar) &&
                        isEnglishVowel(spokenChar) -> 0.35
                else -> 1.0
            }

            val deletionCost =
                if (isEnglishVowel(currentChar)) 0.35 else 1.0

            val insertionCost =
                if (isEnglishVowel(spokenChar)) 0.35 else 1.0

            current[j] = minOf(
                current[j - 1] + insertionCost,
                previous[j] + deletionCost,
                previous[j - 1] + substitutionCost
            )
        }

        for (j in 0..m) {
            previous[j] = current[j]
        }
    }

    return previous[m]
}

private fun wordsMatch(
    expected: String,
    spoken: String
): Boolean {
    val expectedNormalized =
        normalizeWordForComparison(expected)
    val spokenNormalized =
        normalizeWordForComparison(spoken)

    if (expectedNormalized.isEmpty() || spokenNormalized.isEmpty()) {
        return false
    }

    if (expectedNormalized == spokenNormalized) {
        return true
    }

    // Les très petits mots restent stricts pour éviter les faux positifs.
    if (expectedNormalized.length < 4 || spokenNormalized.length < 4) {
        return false
    }

    val weightedDistance =
        weightedWordEditDistance(
            first = expectedNormalized,
            second = spokenNormalized
        )

    val minLength =
        minOf(
            expectedNormalized.length,
            spokenNormalized.length
        )

    // Les différences essentiellement vocaliques sont davantage tolérées,
    // mais une différence de consonne reste coûteuse.
    val allowedDistance = when {
        minLength >= 8 -> 1.40
        minLength >= 6 -> 1.05
        else -> 0.70
    }

    return weightedDistance <= allowedDistance
}

private fun isApproximateWordMatch(
    expected: String,
    spoken: String
): Boolean {
    val expectedNormalized =
        normalizeWordForComparison(expected)
    val spokenNormalized =
        normalizeWordForComparison(spoken)

    if (expectedNormalized.isEmpty() || spokenNormalized.isEmpty()) {
        return false
    }

    return expectedNormalized != spokenNormalized &&
            wordsMatch(expected, spoken)
}

// =====================================================================
// ALIGNEMENT INTELLIGENT DES MOTS
// =====================================================================

private fun alignWords(
    reference: List<String>,
    spoken: List<String>
): List<WordComparison> {

    val n = reference.size
    val m = spoken.size

    if (n == 0) {
        return emptyList()
    }

    // Matrice de distance de Levenshtein
    val dp = Array(n + 1) {
        DoubleArray(m + 1)
    }

    for (i in 0..n) {
        dp[i][0] = i.toDouble()
    }

    for (j in 0..m) {
        dp[0][j] = j.toDouble()
    }

    for (i in 1..n) {
        for (j in 1..m) {

            val expectedWord = reference[i - 1]
            val spokenWord = spoken[j - 1]

            val substitutionCost = when {
                normalizeWordForComparison(expectedWord) ==
                        normalizeWordForComparison(spokenWord) -> 0.0

                isApproximateWordMatch(expectedWord, spokenWord) -> 1.5

                else -> 2.0
            }

            val deletion =
                dp[i - 1][j] + 1

            val insertion =
                dp[i][j - 1] + 1

            val substitution =
                dp[i - 1][j - 1] +
                        substitutionCost

            dp[i][j] =
                minOf(
                    deletion,
                    insertion,
                    substitution
                )
        }
    }

    // Reconstruction
    val result =
        mutableListOf<WordComparison>()

    var i = n
    var j = m

    while (i > 0 || j > 0) {

        // Correspondance ou substitution
        if (
            i > 0 &&
            j > 0
        ) {

            val expectedWord = reference[i - 1]
            val spokenWord = spoken[j - 1]

            val substitutionCost = when {
                normalizeWordForComparison(expectedWord) ==
                        normalizeWordForComparison(spokenWord) -> 0.0

                isApproximateWordMatch(expectedWord, spokenWord) -> 1.5

                else -> 2.0
            }

            if (
                dp[i][j] ==
                dp[i - 1][j - 1] +
                substitutionCost
            ) {

                result.add(
                    WordComparison(
                        expected =
                            reference[i - 1],
                        spoken =
                            spoken[j - 1],
                        correct =
                            wordsMatch(
                                reference[i - 1],
                                spoken[j - 1]
                            )
                    )
                )

                i--
                j--

                continue
            }
        }

        // Mot attendu mais non reconnu
        if (
            i > 0 &&
            dp[i][j] ==
            dp[i - 1][j] + 1
        ) {

            result.add(
                WordComparison(
                    expected =
                        reference[i - 1],
                    spoken = null,
                    correct = false
                )
            )

            i--

            continue
        }

        // Mot supplémentaire reconnu :
        // on l'ignore pour la comparaison.
        if (j > 0) {
            j--
            continue
        }
    }

    result.reverse()

    return result
}

// =====================================================================
// CHOIX DE LA MEILLEURE HYPOTHÈSE DE RECONNAISSANCE
// =====================================================================

private fun recognitionCandidateScore(
    reference: List<String>,
    candidate: String
): Float {
    if (reference.isEmpty() || candidate.isBlank()) {
        return 0f
    }

    val spoken =
        candidate
            .lowercase(java.util.Locale.US)
            .replace(Regex("[^a-zA-Z'\\s]"), " ")
            .split(Regex("\\s+"))
            .filter { it.isNotBlank() }

    if (spoken.isEmpty()) {
        return 0f
    }

    val comparisons =
        alignWords(
            reference = reference,
            spoken = spoken
        )

    val correctCount =
        comparisons.count { it.correct }

    val globalCoverage =
        correctCount.toFloat() /
                reference.size.toFloat()

    // Pour les résultats partiels, privilégier une transcription qui
    // correspond bien au début réellement prononcé de la phrase.
    val prefixCorrectCount =
        comparisons
            .takeWhile {
                it.spoken != null && it.correct
            }
            .size

    val prefixCoverage =
        prefixCorrectCount.toFloat() /
                minOf(reference.size, spoken.size)
                    .coerceAtLeast(1)
                    .toFloat()

    // Évite de favoriser une hypothèse très longue contenant beaucoup
    // de mots supplémentaires mais peu fiables.
    val spokenPrecision =
        correctCount.toFloat() /
                spoken.size.toFloat()

    return (
            (globalCoverage * 0.55f) +
                    (prefixCoverage * 0.30f) +
                    (spokenPrecision * 0.15f)
            )
}

private fun chooseBestRecognitionCandidate(
    reference: List<String>,
    candidates: List<String>
): String {
    return candidates
        .filter { it.isNotBlank() }
        .maxByOrNull { candidate ->
            recognitionCandidateScore(
                reference = reference,
                candidate = candidate
            )
        }
        ?: candidates.firstOrNull().orEmpty()
}

// =====================================================================
// LECTURE LIBRE — SÉLECTION ET CONCATÉNATION DES CHUNKS RECONNUS
// =====================================================================

private fun chooseBestFreeReadingCandidate(
    referenceText: String,
    alreadyRecognizedText: String,
    candidates: List<String>
): String {
    val referenceWords =
        referenceText
            .lowercase(java.util.Locale.US)
            .replace(Regex("[^a-zA-Z'\\s]"), " ")
            .split(Regex("\\s+"))
            .filter { it.isNotBlank() }

    if (referenceWords.isEmpty()) {
        return candidates
            .filter { it.isNotBlank() }
            .firstOrNull()
            .orEmpty()
    }

    val spokenCount =
        alreadyRecognizedText
            .lowercase(java.util.Locale.US)
            .replace(Regex("[^a-zA-Z'\\s]"), " ")
            .split(Regex("\\s+"))
            .count { it.isNotBlank() }

    val start =
        (spokenCount - 3)
            .coerceAtLeast(0)

    val remainingReference =
        referenceWords
            .drop(start)
            .take(60)
            .ifEmpty {
                referenceWords.take(60)
            }

    return candidates
        .filter { it.isNotBlank() }
        .maxByOrNull { candidate ->
            recognitionCandidateScore(
                reference = remainingReference,
                candidate = candidate
            )
        }
        ?: candidates.firstOrNull().orEmpty()
}

private fun appendFreeReadingChunk(
    existing: String,
    candidate: String
): String {
    if (existing.isBlank()) {
        return candidate.trim()
    }

    if (candidate.isBlank()) {
        return existing.trim()
    }

    val existingWords =
        existing
            .split(Regex("\\s+"))
            .filter { it.isNotBlank() }

    val candidateWords =
        candidate
            .split(Regex("\\s+"))
            .filter { it.isNotBlank() }

    if (existingWords.isEmpty()) {
        return candidate.trim()
    }

    if (candidateWords.isEmpty()) {
        return existing.trim()
    }

    val normalize = { word: String ->
        word
            .lowercase(java.util.Locale.US)
            .replace(Regex("[^a-zA-Z']"), "")
    }

    var bestOverlap = 0
    val maxOverlap =
        minOf(
            6,
            existingWords.size,
            candidateWords.size
        )

    for (overlap in maxOverlap downTo 1) {

        val suffix =
            existingWords
                .takeLast(overlap)
                .map(normalize)

        val prefix =
            candidateWords
                .take(overlap)
                .map(normalize)

        if (
            suffix.isNotEmpty() &&
            suffix == prefix
        ) {
            bestOverlap = overlap
            break
        }
    }

    return (
        existingWords +
                candidateWords.drop(bestOverlap)
        ).joinToString(" ")
}

// =====================================================================
// SURLIGNAGE DU MOT EN COURS — TTS
// =====================================================================

private fun buildWordHighlightedText(
    sentence: String,
    activeWordIndex: Int,
    normalColor: androidx.compose.ui.graphics.Color,
    activeColor: androidx.compose.ui.graphics.Color,
    activeBackground: androidx.compose.ui.graphics.Color
): AnnotatedString {

    val words = Regex("\\S+")
        .findAll(sentence)
        .map {
            it.value
        }
        .toList()

    return buildAnnotatedString {

        words.forEachIndexed {
                index,
                word ->

            if (index > 0) {
                append(" ")
            }

            if (index == activeWordIndex) {

                withStyle(
                    SpanStyle(
                        color = activeColor,
                        background = activeBackground,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(word)
                }

            } else {

                withStyle(
                    SpanStyle(
                        color = normalColor
                    )
                ) {
                    append(word)
                }
            }
        }
    }
}

/// =====================================================================
// DÉTAIL D'UNE PHRASE LUE
// =====================================================================

private data class PersistedSentenceReadingResult(
    val correctWords: List<String>,
    val wrongWords: List<String>,
    val correct: Int,
    val total: Int
)

private data class SentenceReadingDetail(
    val correctWords: List<String>,
    val wrongWords: List<String>
)

// =====================================================================
// LECTEUR AUDIO — VITESSE + SYNCHRONISATION MOT PAR MOT
// =====================================================================

@Composable
fun OliverAudioPlayer(
    scene: OliverScene,
    chapterIndex: Int,
    sceneIndex: Int,
    textOverride: String? = null,
    onWordChanged: (
        sentenceIndex: Int,
        wordIndex: Int
    ) -> Unit,
    onPronunciationPractice: (
        String
    ) -> Unit = {},
    onReadingCompleted: (Int) -> Unit = {}
) {
    val context = LocalContext.current
    val progressStore = remember {
        OliverProgressStore(context)
    }

    var microphoneGranted by remember {
        mutableStateOf(
            context.checkSelfPermission(
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val microphonePermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->
            microphoneGranted = granted
        }

    // =========================================================
// RECONNAISSANCE VOCALE
// =========================================================

    var isListening by remember {
        mutableStateOf(false)
    }

    var recognizedText by remember {
        mutableStateOf("")
    }

    // Hypothèses de reconnaissance : Android peut retourner plusieurs
    // transcriptions. Nous utiliserons la meilleure par rapport à la phrase.
    var recognizedCandidates by remember {
        mutableStateOf(emptyList<String>())
    }

    // Vrai uniquement lorsqu'un résultat final de SpeechRecognizer
    // a été reçu pour la tentative actuelle. Les résultats partiels
    // ne doivent jamais finaliser un score ou déclencher une correction.
    var recognitionFinalized by remember {
        mutableStateOf(false)
    }

    // Une seule décision finale par session de reconnaissance.
    // Empêche un double onResults() de valider ou comptabiliser deux fois
    // la même tentative.
    var finalResultHandled by remember {
        mutableStateOf(false)
    }

    // Vrai lorsqu'une tentative s'est terminée sans aucun mot reconnu.
    // Dans ce cas, aucune statistique, aucun score et aucune erreur mot par mot
    // ne doivent être générés.
    var noSpeechRecognized by remember {
        mutableStateOf(false)
    }

    // =========================================================
    // MOTS DÉJÀ RÉUSSIS PENDANT L'ENTRAÎNEMENT
    // =========================================================

    var practicedWords by remember {
        mutableStateOf(emptySet<String>())
    }

    var recognitionError by remember {
        mutableStateOf("")
    }

    var voiceRms by remember {
        mutableFloatStateOf(0f)
    }

    var speechDetected by remember {
        mutableStateOf(false)
    }

    // =========================================================
    // LECTURE LIBRE — UNE SESSION CONTINUE JUSQU'AU STOP
    // =========================================================

    var freeReadingSessionActive by remember {
        mutableStateOf(false)
    }

    var freeReadingStopRequested by remember {
        mutableStateOf(false)
    }

    var freeReadingFinalized by remember {
        mutableStateOf(false)
    }

    var freeReadingReferenceText by remember {
        mutableStateOf("")
    }

    var freeReadingRecognizedText by remember {
        mutableStateOf("")
    }

    var freeReadingPartialText by remember {
        mutableStateOf("")
    }

    var freeReadingError by remember {
        mutableStateOf("")
    }

    var freeReadingSessionKey by remember {
        mutableIntStateOf(0)
    }

    // =========================================================
    // ENTRAÎNEMENT DES MOTS ROUGES APRÈS LA LECTURE LIBRE
    // =========================================================

    var freeTrainingWord by remember(
        scene.englishText,
        textOverride
    ) {
        mutableStateOf<String?>(null)
    }

    var freeTrainingMasteredWords by remember(
        scene.englishText,
        textOverride
    ) {
        mutableStateOf(emptySet<String>())
    }

    var freeTrainingMessage by remember(
        scene.englishText,
        textOverride
    ) {
        mutableStateOf("")
    }

    var freeTrainingSuccess by remember(
        scene.englishText,
        textOverride
    ) {
        mutableStateOf(false)
    }

    var freeTrainingAttempts by remember(
        scene.englishText,
        textOverride
    ) {
        mutableIntStateOf(0)
    }

    var freeInitialWrongWords by remember(
        scene.englishText,
        textOverride
    ) {
        mutableStateOf(emptySet<String>())
    }
    // =========================================================
// ENTRAÎNEMENT D'UN MOT
// =========================================================

    var practiceWord by remember {
        mutableStateOf<String?>(null)
    }

    var practiceRecognizedText by remember {
        mutableStateOf("")
    }

    var practiceListening by remember {
        mutableStateOf(false)
    }

    var practiceCorrect by remember {
        mutableStateOf(false)
    }

    // Entraînement automatique déclenché lorsqu'un mot vient d'être
    // reconnu comme incorrect pendant la lecture normale.
    var automaticWrongWordPractice by remember(
        scene.title,
        textOverride
    ) {
        mutableStateOf(false)
    }

    var automaticWrongWordIndices by remember(
        scene.title,
        textOverride
    ) {
        mutableStateOf(emptySet<Int>())
    }

    var automaticWrongWordAttempts by remember {
        mutableIntStateOf(0)
    }

    var automaticWrongWordIndex by remember {
        mutableIntStateOf(-1)
    }

    var skippedAutomaticWrongWordIndices by remember(
        scene.title,
        textOverride
    ) {
        mutableStateOf(emptySet<Int>())
    }

    // Dernier mot laissé à reprendre après 3 échecs.
    var lastSkippedAutomaticWrongWord by remember(
        scene.title,
        textOverride
    ) {
        mutableStateOf<String?>(null)
    }

    var lastSkippedAutomaticWrongWordIndex by remember(
        scene.title,
        textOverride
    ) {
        mutableIntStateOf(-1)
    }

    var automaticWrongWordSkipRequested by remember {
        mutableStateOf(false)
    }

    var recognitionBelongsToCurrentSentence by remember {
        mutableStateOf(false)
    }

    // Feedback immédiat du mot attendu lorsque la reconnaissance finale
    // a compris un autre mot. Null = aucune différence à signaler.
    var currentMismatchFeedback by remember {
        mutableStateOf<Pair<String, String>?>(null)
    }


    var autoRevisionMode by remember(scene.title, textOverride) {
        mutableStateOf(false)
    }

    var autoRevisionIndex by remember(scene.title, textOverride) {
        mutableIntStateOf(0)
    }

    // Nombre d'essais pour le mot courant en mode automatique.
    // Le compteur est recréé à chaque nouveau mot.
    var autoRevisionAttempts by remember(
        scene.title,
        textOverride,
        autoRevisionIndex
    ) {
        mutableIntStateOf(0)
    }

    val handler = remember(
        scene.title,
        textOverride
    ) {
        android.os.Handler(
            android.os.Looper.getMainLooper()
        )
    }

    val speechRecognizer = remember {
        SpeechRecognizer.createSpeechRecognizer(context)
    }

    // Intention de reconnaissance réutilisée à chaque nouveau segment.
    val freeReadingRecognizerIntent = remember {
        {
            Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH
            ).apply {

                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )

                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE,
                    "en-US"
                )

                putExtra(
                    RecognizerIntent.EXTRA_PARTIAL_RESULTS,
                    true
                )

                putExtra(
                    RecognizerIntent.EXTRA_MAX_RESULTS,
                    5
                )

                putExtra(
                    RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS,
                    1200L
                )

                putExtra(
                    RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,
                    1500L
                )

                putExtra(
                    RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,
                    1000L
                )
            }
        }
    }

    val restartFreeReadingRecognition = remember {
        {
            if (
                freeReadingSessionActive &&
                !freeReadingStopRequested
            ) {
                finalResultHandled = false
                try {
                    speechRecognizer.startListening(
                        freeReadingRecognizerIntent()
                    )
                    isListening = true
                } catch (e: Exception) {
                    isListening = false
                    freeReadingError =
                        e.message
                            ?: "Impossible de relancer le microphone."
                }
            }
        }
    }

    // =========================================================
    // DÉMARRER L'ENTRAÎNEMENT D'UN MOT
    // =========================================================

    val startPracticeRecognition: (String) -> Unit = { word ->

        finalResultHandled = false
        practiceWord = word
        practiceRecognizedText = ""
        practiceCorrect = false
        practiceListening = false
        recognitionError = ""

        try {
            speechRecognizer.stopListening()
        } catch (_: Exception) {
        }

        try {
            speechRecognizer.cancel()
        } catch (_: Exception) {
        }

        val intent = Intent(
            RecognizerIntent.ACTION_RECOGNIZE_SPEECH
        ).apply {

            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )

            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                "en-US"
            )

            putExtra(
                RecognizerIntent.EXTRA_PARTIAL_RESULTS,
                true
            )

            putExtra(
                RecognizerIntent.EXTRA_MAX_RESULTS,
                3
            )

            putExtra(
                RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS,
                700L
            )

            putExtra(
                RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,
                1200L
            )

            putExtra(
                RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,
                800L
            )
        }

        try {
            speechRecognizer.startListening(intent)
            practiceListening = true
        } catch (e: Exception) {
            practiceListening = false
            recognitionError =
                e.message
                    ?: "Impossible de démarrer l'entraînement vocal."
        }
    }

    DisposableEffect(speechRecognizer) {

        speechRecognizer.setRecognitionListener(
            object : RecognitionListener {

                override fun onReadyForSpeech(
                    params: android.os.Bundle?
                ) {
                    if (practiceWord == null) {
                        recognitionFinalized = false
                    }

                    when {
                        practiceWord != null -> {
                            practiceListening = true
                        }

                        freeReadingSessionActive -> {
                            isListening = true
                        }

                        else -> {
                            isListening = true
                        }
                    }

                    speechDetected = false
                    voiceRms = 0f
                    recognitionError = ""
                    freeReadingError = ""
                }

                override fun onBeginningOfSpeech() {
                    if (practiceWord == null) {
                        recognitionFinalized = false
                    }

                    when {
                        practiceWord != null -> {
                            practiceListening = true
                        }

                        freeReadingSessionActive -> {
                            isListening = true
                        }

                        else -> {
                            isListening = true
                        }
                    }

                    speechDetected = true
                }

                override fun onRmsChanged(
                    rmsdB: Float
                ) {
                    voiceRms = rmsdB
                }

                override fun onBufferReceived(
                    buffer: ByteArray?
                ) {
                }

                override fun onEndOfSpeech() {
                    if (practiceWord != null) {
                        practiceListening = false

                    } else if (freeReadingSessionActive) {
                        isListening = false

                    } else {
                        isListening = false
                    }
                }

                override fun onError(
                    error: Int
                ) {
                    // -------------------------------------------------
                    // LECTURE LIBRE
                    // -------------------------------------------------
                    if (
                        practiceWord == null &&
                        freeReadingSessionActive
                    ) {
                        isListening = false

                        if (freeReadingStopRequested) {
                            freeReadingError = ""
                            return
                        }

                        when (error) {
                            SpeechRecognizer.ERROR_NO_MATCH,
                            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> {
                                freeReadingError = ""
                                handler.postDelayed(
                                    {
                                        restartFreeReadingRecognition()
                                    },
                                    250L
                                )
                            }

                            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> {
                                handler.postDelayed(
                                    {
                                        restartFreeReadingRecognition()
                                    },
                                    600L
                                )
                            }

                            SpeechRecognizer.ERROR_NETWORK,
                            SpeechRecognizer.ERROR_NETWORK_TIMEOUT,
                            SpeechRecognizer.ERROR_SERVER -> {
                                freeReadingError =
                                    "⚠️ La reconnaissance vocale rencontre un problème réseau. Nouvelle tentative..."
                                handler.postDelayed(
                                    {
                                        restartFreeReadingRecognition()
                                    },
                                    900L
                                )
                            }

                            SpeechRecognizer.ERROR_AUDIO,
                            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> {
                                freeReadingError =
                                    "⚠️ Le microphone n'est pas disponible."
                                freeReadingStopRequested = true
                            }

                            else -> {
                                freeReadingError =
                                    "⚠️ Erreur de reconnaissance. Nouvelle tentative..."
                                handler.postDelayed(
                                    {
                                        restartFreeReadingRecognition()
                                    },
                                    500L
                                )
                            }
                        }

                        return
                    }

                    val inPractice = practiceWord != null

                    if (inPractice) {
                        practiceListening = false
                    } else {
                        isListening = false
                        recognitionFinalized = false
                        finalResultHandled = false
                    }

                    when (error) {
                        SpeechRecognizer.ERROR_NO_MATCH,
                        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> {
                            if (!inPractice) {
                                // Aucun mot reconnu : effacer toute transcription
                                // provisoire afin qu'aucune erreur artificielle
                                // ne soit calculée.
                                recognizedText = ""
                                recognizedCandidates = emptyList()
                                recognitionBelongsToCurrentSentence = false
                                noSpeechRecognized = true
                                recognitionError =
                                    "🎙️ Je n'ai pas compris votre lecture. Aucun mot n'a été évalué. Réessayez."
                            } else {
                                if (freeTrainingWord != null &&
                                    practiceWord != null &&
                                    normalizeWordForComparison(
                                        freeTrainingWord!!
                                    ) == normalizeWordForComparison(
                                        practiceWord!!
                                    )
                                ) {
                                    freeTrainingAttempts++
                                    practiceWord = null
                                    practiceListening = false
                                    freeTrainingSuccess = false
                                    freeTrainingMessage =
                                        "🎤 Aucun mot reconnu. Approchez le téléphone et répétez « ${freeTrainingWord!!} »."
                                    recognitionError = ""
                                } else {
                                    recognitionError =
                                        "🎙️ Je n'ai pas reconnu le mot. Réessayez de le prononcer clairement."
                                }

                                // Un résultat vide compte comme un essai de pratique,
                                // mais jamais comme une fausse transcription.
                                if (freeTrainingWord != null &&
                                    practiceWord == null
                                ) {
                                    // Entraînement manuel : aucune progression automatique.
                                } else if (automaticWrongWordPractice) {
                                    automaticWrongWordAttempts++

                                    if (automaticWrongWordAttempts >= 3) {
                                        skippedAutomaticWrongWordIndices =
                                            skippedAutomaticWrongWordIndices +
                                                    automaticWrongWordIndex
                                        lastSkippedAutomaticWrongWordIndex =
                                            automaticWrongWordIndex
                                        lastSkippedAutomaticWrongWord =
                                            practiceWord
                                        automaticWrongWordSkipRequested = true
                                        automaticWrongWordPractice = false
                                        practiceWord = null
                                        practiceListening = false
                                        recognitionError =
                                            "⚠️ 3 essais sans reconnaissance. Mot laissé à reprendre plus tard."
                                        automaticWrongWordAttempts = 0
                                        automaticWrongWordIndex = -1
                                    } else {
                                        handler.postDelayed(
                                            {
                                                if (
                                                    automaticWrongWordPractice &&
                                                    practiceWord != null
                                                ) {
                                                    startPracticeRecognition(
                                                        practiceWord!!
                                                    )
                                                }
                                            },
                                            700L
                                        )
                                    }
                                } else if (autoRevisionMode) {
                                    autoRevisionAttempts++

                                    if (autoRevisionAttempts >= 3) {
                                        recognitionError =
                                            "⚠️ 3 essais sans reconnaissance pour « ${practiceWord ?: "mot"} ». Passage au mot suivant."
                                        handler.postDelayed(
                                            {
                                                if (autoRevisionMode) {
                                                    autoRevisionAttempts = 0
                                                    autoRevisionIndex++
                                                }
                                            },
                                            250L
                                        )
                                    } else {
                                        handler.postDelayed(
                                            {
                                                if (
                                                    autoRevisionMode &&
                                                    practiceWord != null
                                                ) {
                                                    startPracticeRecognition(
                                                        practiceWord!!
                                                    )
                                                }
                                            },
                                            700L
                                        )
                                    }
                                }
                            }
                        }

                        SpeechRecognizer.ERROR_AUDIO ->
                            recognitionError =
                                "Erreur audio du microphone."

                        SpeechRecognizer.ERROR_CLIENT ->
                            recognitionError =
                                "Erreur du service de reconnaissance."

                        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS ->
                            recognitionError =
                                "Permission microphone refusée."

                        SpeechRecognizer.ERROR_NETWORK ->
                            recognitionError =
                                "Erreur réseau."

                        SpeechRecognizer.ERROR_NETWORK_TIMEOUT ->
                            recognitionError =
                                "Délai réseau dépassé."

                        SpeechRecognizer.ERROR_RECOGNIZER_BUSY ->
                            recognitionError =
                                "La reconnaissance est déjà utilisée."

                        SpeechRecognizer.ERROR_SERVER ->
                            recognitionError =
                                "Erreur du serveur vocal."

                        else ->
                            recognitionError =
                                "Erreur inconnue de reconnaissance."
                    }
                }

                override fun onResults(
                    results: android.os.Bundle?
                ) {
                    if (finalResultHandled) {
                        return
                    }

                    finalResultHandled = true

                    val matches =
                        results?.getStringArrayList(
                            SpeechRecognizer.RESULTS_RECOGNITION
                        )

                    // -------------------------------------------------
                    // LECTURE LIBRE : on accumule les segments reconnus
                    // jusqu'à ce que l'étudiant appuie sur STOP.
                    // -------------------------------------------------
                    if (
                        practiceWord == null &&
                        freeReadingSessionActive
                    ) {
                        val candidates =
                            matches
                                ?.filter { it.isNotBlank() }
                                ?: emptyList()

                        if (candidates.isNotEmpty()) {

                            val bestCandidate =
                                chooseBestFreeReadingCandidate(
                                    referenceText =
                                        freeReadingReferenceText,
                                    alreadyRecognizedText =
                                        freeReadingRecognizedText,
                                    candidates = candidates
                                )

                            if (bestCandidate.isNotBlank()) {
                                freeReadingRecognizedText =
                                    appendFreeReadingChunk(
                                        existing =
                                            freeReadingRecognizedText,
                                        candidate =
                                            bestCandidate
                                    )

                                recognizedText =
                                    freeReadingRecognizedText

                                freeReadingPartialText = ""
                                freeReadingError = ""
                                noSpeechRecognized = false

                                if (
                                    !freeReadingStopRequested
                                ) {
                                    handler.postDelayed(
                                        {
                                            restartFreeReadingRecognition()
                                        },
                                        220L
                                    )
                                }
                            }
                        }

                        isListening =
                            !freeReadingStopRequested

                        return
                    }

                    if (practiceWord != null) {
                        practiceListening = false

                        val expected =
                            practiceWord!!
                                .lowercase(java.util.Locale.US)
                                .replace(
                                    Regex("[^a-zA-Z']"),
                                    ""
                                )

                        val practiceCandidates =
                            matches
                                ?.filter { it.isNotBlank() }
                                ?: emptyList()

                        val selectedPracticeCandidate =
                            practiceCandidates.maxByOrNull { candidate ->
                                val candidateWords =
                                    candidate
                                        .lowercase(java.util.Locale.US)
                                        .replace(
                                            Regex("[^a-zA-Z'\\s]"),
                                            " "
                                        )
                                        .split(Regex("\\s+"))
                                        .filter { it.isNotBlank() }

                                val spokenWord =
                                    candidateWords.firstOrNull()
                                        ?.replace(
                                            Regex("[^a-zA-Z']"),
                                            ""
                                        )
                                        ?: ""

                                when {
                                    spokenWord == expected -> 3
                                    wordsMatch(expected, spokenWord) -> 2
                                    else -> 0
                                }
                            } ?: ""

                        val spoken = selectedPracticeCandidate

                        practiceRecognizedText = spoken

                        val spokenWordsPractice =
                            spoken
                                .lowercase(java.util.Locale.US)
                                .replace(
                                    Regex("[^a-zA-Z'\\s]"),
                                    " "
                                )
                                .split(Regex("\\s+"))
                                .filter { it.isNotBlank() }

                        val normalizedSpoken =
                            spokenWordsPractice
                                .firstOrNull()
                                ?.replace(
                                    Regex("[^a-zA-Z']"),
                                    ""
                                )
                                ?: ""

                        practiceCorrect =
                            wordsMatch(
                                expected,
                                normalizedSpoken
                            )

                        if (practiceCorrect) {
                            practicedWords =
                                practicedWords + expected

                            recognitionError = ""

                            if (freeTrainingWord != null &&
                                normalizeWordForComparison(
                                    freeTrainingWord!!
                                ) == expected
                            ) {
                                freeTrainingMasteredWords =
                                    freeTrainingMasteredWords + expected
                                freeTrainingSuccess = true
                                freeTrainingMessage =
                                    "✅ Très bien ! « ${freeTrainingWord!!} » est maintenant maîtrisé."
                                freeTrainingAttempts = 0
                                practiceWord = null
                                practiceListening = false
                                practiceRecognizedText = ""
                            }

                            if (autoRevisionMode) {
                                handler.postDelayed(
                                    {
                                        if (autoRevisionMode) {
                                            autoRevisionIndex++
                                        }
                                    },
                                    250L
                                )
                            }
                        } else {
                            if (freeTrainingWord != null &&
                                normalizeWordForComparison(
                                    freeTrainingWord!!
                                ) == expected
                            ) {
                                freeTrainingAttempts++
                                practiceWord = null
                                practiceListening = false
                                freeTrainingSuccess = false
                                freeTrainingMessage =
                                    "❌ Essai ${freeTrainingAttempts} : écoutez le modèle puis réessayez."
                                recognitionError = ""
                            } else if (automaticWrongWordPractice) {
                                automaticWrongWordAttempts++

                                if (automaticWrongWordAttempts >= 3) {
                                    skippedAutomaticWrongWordIndices =
                                        skippedAutomaticWrongWordIndices +
                                                automaticWrongWordIndex
                                    lastSkippedAutomaticWrongWordIndex =
                                        automaticWrongWordIndex
                                    lastSkippedAutomaticWrongWord =
                                        practiceWord
                                    automaticWrongWordSkipRequested = true

                                    automaticWrongWordPractice = false
                                    practiceWord = null
                                    practiceListening = false
                                    recognitionError =
                                        "⚠️ 3 essais. Mot laissé à reprendre plus tard. Continuez votre lecture."

                                    automaticWrongWordAttempts = 0
                                    automaticWrongWordIndex = -1
                                } else {
                                    recognitionError =
                                        "❌ Essai $automaticWrongWordAttempts/3 : prononcez « $expected »."

                                    handler.postDelayed(
                                        {
                                            if (
                                                automaticWrongWordPractice &&
                                                practiceWord != null
                                            ) {
                                                startPracticeRecognition(
                                                    practiceWord!!
                                                )
                                            }
                                        },
                                        700L
                                    )
                                }
                            } else if (autoRevisionMode) {
                                autoRevisionAttempts++

                                if (autoRevisionAttempts >= 3) {
                                    recognitionError =
                                        "⚠️ 3 essais pour « $expected ». Passage au mot suivant ; vous pourrez reprendre ce mot manuellement."

                                    handler.postDelayed(
                                        {
                                            if (autoRevisionMode) {
                                                autoRevisionAttempts = 0
                                                autoRevisionIndex++
                                            }
                                        },
                                        900L
                                    )
                                } else {
                                    recognitionError =
                                        "❌ Essai $autoRevisionAttempts/3 : prononcez « $expected »."

                                    handler.postDelayed(
                                        {
                                            if (
                                                autoRevisionMode &&
                                                practiceWord != null
                                            ) {
                                                startPracticeRecognition(
                                                    practiceWord!!
                                                )
                                            }
                                        },
                                        700L
                                    )
                                }
                            } else {
                                recognitionError =
                                    "❌ Essayez encore : prononcez « $expected »."
                            }
                        }

                        return
                    }

                    isListening = false

                    if (matches != null && matches.isNotEmpty()) {
                        noSpeechRecognized = false
                        recognitionFinalized = true
                        // Conserver les hypothèses reçues pendant toute la session
                        // afin qu'un résultat final moins bon ne remplace pas
                        // une transcription partielle plus précise.
                        recognizedCandidates =
                            (recognizedCandidates + matches)
                                .filter { it.isNotBlank() }
                                .distinct()
                                .takeLast(20)

                        recognizedText =
                            recognizedCandidates.lastOrNull().orEmpty()

                        recognitionBelongsToCurrentSentence = true
                        recognitionError = ""
                    }
                }

                override fun onPartialResults(
                    partialResults: android.os.Bundle?
                ) {
                    val matches =
                        partialResults?.getStringArrayList(
                            SpeechRecognizer.RESULTS_RECOGNITION
                        )

                    if (
                        practiceWord == null &&
                        freeReadingSessionActive
                    ) {
                        val spoken =
                            matches
                                ?.firstOrNull()
                                .orEmpty()

                        if (spoken.isNotBlank()) {
                            freeReadingPartialText = spoken
                            recognizedText =
                                if (freeReadingRecognizedText.isBlank()) {
                                    spoken
                                } else {
                                    "$freeReadingRecognizedText $spoken"
                                }
                        }

                        return
                    }

                    if (practiceWord != null) {
                        val spoken =
                            matches
                                ?.firstOrNull()
                                ?: ""

                        if (spoken.isNotBlank()) {
                            practiceRecognizedText = spoken
                        }

                        return
                    }

                    if (matches != null && matches.isNotEmpty()) {
                        noSpeechRecognized = false
                        recognitionFinalized = false

                        // Accumuler les hypothèses partielles au lieu de perdre
                        // une transcription correcte déjà obtenue.
                        recognizedCandidates =
                            (recognizedCandidates + matches)
                                .filter { it.isNotBlank() }
                                .distinct()
                                .takeLast(20)

                        recognizedText =
                            recognizedCandidates.lastOrNull().orEmpty()

                        recognitionBelongsToCurrentSentence = true
                    }
                }

                override fun onEvent(
                    eventType: Int,
                    params: android.os.Bundle?
                ) {
                }
            }
        )

        onDispose {

            try {
                speechRecognizer.stopListening()
            } catch (_: Exception) {
            }

            try {
                speechRecognizer.cancel()
            } catch (_: Exception) {
            }

            try {
                speechRecognizer.destroy()
            } catch (_: Exception) {
            }
        }
    }
    // -------------------------------------------------------------
    // TEXTE DE LA PAGE ACTUELLE
    // -------------------------------------------------------------
    val readingText = textOverride
        ?.takeIf { it.isNotBlank() }
        ?: scene.englishText

    // -------------------------------------------------------------
    // ÉTAT AUDIO
    // -------------------------------------------------------------
    var isSpeaking by remember(
        scene.title,
        readingText
    ) {
        mutableStateOf(false)
    }

    var ttsReady by remember(
        scene.title,
        readingText
    ) {
        mutableStateOf(false)
    }

    var progress by remember(
        scene.title,
        readingText
    ) {
        mutableFloatStateOf(0f)
    }

    var currentSentence by remember(
        scene.title,
        readingText
    ) {
        mutableIntStateOf(0)
    }

    // -------------------------------------------------------------
    // TEXT TO SPEECH
    // -------------------------------------------------------------
    val textToSpeech = remember(
        scene.title,
        readingText
    ) {
        android.speech.tts.TextToSpeech(context) { status ->

            handler.post {
                ttsReady =
                    status ==
                            android.speech.tts.TextToSpeech.SUCCESS
            }
        }
    }


    // =========================================================
// PHRASES + COMPARAISON DE LA LECTURE
// =========================================================

    val sentences = remember(
        scene.title,
        readingText
    ) {
        readingText
            .replace(
                "\r\n",
                "\n"
            )
            .split(
                Regex(
                    "\\n\\s*\\n|(?<=[.!?])\\s+"
                )
            )
            .map {
                it.trim()
            }
            .filter {
                it.isNotEmpty()
            }
    }

// Phrase actuellement travaillée
    var readingSentenceIndex by remember(
        readingText
    ) {
        mutableIntStateOf(0)
    }

// Sécurité
    if (readingSentenceIndex > sentences.lastIndex) {
        readingSentenceIndex = 0
    }

    val currentReadingSentence =
        sentences.getOrNull(
            readingSentenceIndex
        ) ?: ""

    var sceneSentenceDetails by remember(
        readingText,
        chapterIndex,
        sceneIndex,
        sentences.size
    ) {
        val restored =
            sentences.indices.mapNotNull { index ->
                progressStore
                    .getSentenceReadingResult(
                        chapterIndex = chapterIndex,
                        sceneIndex = sceneIndex,
                        sentenceIndex = index
                    )
                    ?.let { saved ->
                        index to SentenceReadingDetail(
                            correctWords = saved.correctWords,
                            wrongWords = saved.wrongWords
                        )
                    }
            }.toMap()

        mutableStateOf(restored)
    }

    var selectedSentenceIndex by remember(readingText) {
        mutableIntStateOf(-1)
    }

    // =========================================================
    // MÉMOIRE DES ERREURS PAR MOT — TOUTE LA SCÈNE
    // =========================================================

    val legacySceneWordErrorCounts =
        remember(sceneSentenceDetails) {
            sceneSentenceDetails
                .values
                .flatMap { it.wrongWords }
                .map {
                    it.lowercase(java.util.Locale.US)
                        .replace("’", "'")
                        .replace(Regex("[^a-zA-Z']"), "")
                        .trim()
                }
                .filter { it.isNotBlank() }
                .groupingBy { it }
                .eachCount()
        }

    var sceneWordErrorCounts by remember(
        chapterIndex,
        sceneIndex,
        readingText
    ) {
        mutableStateOf(
            progressStore
                .getSceneWordErrorCounts(
                    chapterIndex = chapterIndex,
                    sceneIndex = sceneIndex
                )
                .ifEmpty { legacySceneWordErrorCounts }
        )
    }

    val sceneDifficultWordStats =
        remember(sceneWordErrorCounts) {
            sceneWordErrorCounts
                .toList()
                .sortedByDescending { it.second }
        }


    // =========================================================
    // LECTURE LIBRE — PAGE COURANTE
    // =========================================================

    val freeReadingText =
        freeReadingReferenceText
            .takeIf { it.isNotBlank() }
            ?: readingText

    val freeReferenceWords =
        remember(
            freeReadingText
        ) {
            freeReadingText
                .lowercase(java.util.Locale.US)
                .replace(
                    Regex("[^a-zA-Z'\\s]"),
                    " "
                )
                .split(
                    Regex("\\s+")
                )
                .filter {
                    it.isNotBlank()
                }
        }

    val freeSpokenWords =
        remember(
            freeReadingRecognizedText
        ) {
            freeReadingRecognizedText
                .lowercase(java.util.Locale.US)
                .replace(
                    Regex("[^a-zA-Z'\\s]"),
                    " "
                )
                .split(
                    Regex("\\s+")
                )
                .filter {
                    it.isNotBlank()
                }
        }

    val freeWordComparisons: List<WordComparison> =
        remember(
            freeReadingFinalized,
            freeReadingRecognizedText,
            freeReadingText
        ) {
            if (
                freeReadingFinalized &&
                freeReadingRecognizedText.isNotBlank()
            ) {
                alignWords(
                    reference =
                        freeReferenceWords,
                    spoken =
                        freeSpokenWords
                )
            } else {
                emptyList()
            }
        }

    val freeWordStates =
        remember(
            freeWordComparisons,
            freeReadingFinalized,
            freeReferenceWords,
            freeTrainingMasteredWords
        ) {
            if (!freeReadingFinalized) {
                List(
                    freeReferenceWords.size
                ) {
                    FreeReadingWordState.PENDING
                }
            } else {
                freeReferenceWords.mapIndexed { index, referenceWord ->
                    val normalizedReferenceWord =
                        normalizeWordForComparison(referenceWord)

                    if (
                        freeTrainingMasteredWords.contains(
                            normalizedReferenceWord
                        )
                    ) {
                        FreeReadingWordState.CORRECT
                    } else {
                        val comparison =
                            freeWordComparisons.getOrNull(index)

                        when {
                            comparison == null ||
                                    comparison.spoken == null ->
                                FreeReadingWordState.WRONG

                        wordsMatch(
                            comparison.expected,
                            comparison.spoken
                        ) ->
                            FreeReadingWordState.CORRECT

                        isApproximateWordMatch(
                            comparison.expected,
                            comparison.spoken
                        ) ->
                            FreeReadingWordState.APPROXIMATE

                            else ->
                                FreeReadingWordState.WRONG
                        }
                    }
                }
            }
        }

    val freeCorrectWordCount =
        freeWordStates.count {
            it == FreeReadingWordState.CORRECT
        }

    val freeApproximateWordCount =
        freeWordStates.count {
            it == FreeReadingWordState.APPROXIMATE
        }

    val freeWrongWordCount =
        freeWordStates.count {
            it == FreeReadingWordState.WRONG
        }

    val freeDisplayWords =
        remember(freeReadingText) {
            Regex("\\S+")
                .findAll(freeReadingText)
                .map { it.value }
                .toList()
        }

    val freeWrongTrainingWords =
        remember(
            freeDisplayWords,
            freeInitialWrongWords,
            freeTrainingMasteredWords,
            freeReadingFinalized
        ) {
            if (!freeReadingFinalized) {
                emptyList()
            } else {
                freeDisplayWords
                    .map { displayWord ->
                        val cleanWord =
                            displayWord
                                .replace(
                                    Regex("^[^a-zA-Z']+|[^a-zA-Z']+$"),
                                    ""
                                )
                        cleanWord to normalizeWordForComparison(cleanWord)
                    }
                    .filter { (_, normalized) ->
                        freeInitialWrongWords.contains(normalized) &&
                                !freeTrainingMasteredWords.contains(normalized)
                    }
                    .map { (display, _) -> display }
                    .distinctBy { normalizeWordForComparison(it) }
            }
        }

    val freeTrainingCompletedCount =
        freeInitialWrongWords.count { normalized ->
            freeTrainingMasteredWords.contains(normalized)
        }

    val freeTrainingTotalCount =
        freeInitialWrongWords.size

    val freeTrainingProgress =
        if (freeTrainingTotalCount > 0) {
            freeTrainingCompletedCount.toFloat() /
                    freeTrainingTotalCount.toFloat()
        } else {
            0f
        }

    val freeScore =
        if (
            freeReferenceWords.isNotEmpty() &&
            freeReadingFinalized
        ) {
            (
                (
                    freeCorrectWordCount +
                            freeApproximateWordCount * 0.5f
                    ) /
                        freeReferenceWords.size.toFloat() *
                        100f
                )
                    .toInt()
                    .coerceIn(0, 100)
        } else {
            0
        }

    // Ne jamais changer de page pendant une lecture libre active.
    LaunchedEffect(
        readingText
    ) {
        if (!freeReadingSessionActive) {
            freeReadingReferenceText =
                readingText

            freeReadingFinalized = false
            freeReadingRecognizedText = ""
            freeReadingPartialText = ""
            freeReadingError = ""
        }
    }

    // =========================================================
    // DÉMARRER LA LECTURE LIBRE
    // =========================================================

    fun startFreeReading() {

        if (!microphoneGranted) {
            microphonePermissionLauncher.launch(
                Manifest.permission.RECORD_AUDIO
            )
            return
        }

        freeReadingReferenceText =
            readingText

        freeReadingSessionActive = true
        freeReadingStopRequested = false
        freeReadingFinalized = false
        freeReadingRecognizedText = ""
        freeReadingPartialText = ""
        freeReadingError = ""

        freeTrainingWord = null
        freeTrainingMasteredWords = emptySet()
        freeTrainingMessage = ""
        freeTrainingSuccess = false
        freeTrainingAttempts = 0
        freeInitialWrongWords = emptySet()

        recognizedText = ""
        recognizedCandidates = emptyList()
        recognitionFinalized = false
        recognitionBelongsToCurrentSentence = false
        finalResultHandled = false
        noSpeechRecognized = false
        recognitionError = ""
        speechDetected = false
        voiceRms = 0f

        freeReadingSessionKey++

        try {
            speechRecognizer.cancel()
        } catch (_: Exception) {
        }

        try {
            finalResultHandled = false
            speechRecognizer.startListening(
                freeReadingRecognizerIntent()
            )
            isListening = true
        } catch (e: Exception) {
            freeReadingSessionActive = false
            isListening = false
            freeReadingError =
                e.message
                    ?: "Impossible de démarrer le microphone."
        }
    }

    // =========================================================
    // DÉMARRER L'ENTRAÎNEMENT D'UN MOT ROUGE
    // =========================================================

    fun startFreeWordTraining(word: String) {

        val cleanWord =
            word
                .replace(
                    Regex("^[^a-zA-Z']+|[^a-zA-Z']+$"),
                    ""
                )
                .trim()

        val normalizedWord =
            normalizeWordForComparison(cleanWord)

        if (cleanWord.isBlank() || normalizedWord.isBlank()) return

        if (!freeReadingFinalized) return

        if (!freeInitialWrongWords.contains(normalizedWord)) return

        freeReadingSessionActive = false
        freeReadingStopRequested = false
        isListening = false

        try {
            speechRecognizer.stopListening()
        } catch (_: Exception) {
        }

        try {
            speechRecognizer.cancel()
        } catch (_: Exception) {
        }

        try {
            textToSpeech.stop()
        } catch (_: Exception) {
        }

        val sameTrainingWord =
            normalizeWordForComparison(
                freeTrainingWord.orEmpty()
            ) == normalizedWord

        freeTrainingWord = cleanWord
        freeTrainingSuccess = false
        if (!sameTrainingWord) {
            freeTrainingAttempts = 0
        }
        freeTrainingMessage =
            "👂 Écoutez le modèle, puis répétez le mot."
        practiceWord = null
        practiceRecognizedText = ""
        practiceListening = false
        practiceCorrect = false
        recognitionError = ""

        if (ttsReady) {
            try {
                textToSpeech.language =
                    java.util.Locale.US
                textToSpeech.setSpeechRate(0.68f)
                textToSpeech.setPitch(1.0f)
                textToSpeech.speak(
                    cleanWord,
                    android.speech.tts.TextToSpeech.QUEUE_FLUSH,
                    null,
                    "free_training_${normalizedWord.hashCode()}"
                )
            } catch (_: Exception) {
            }
        }

        handler.postDelayed(
            {
                if (freeTrainingWord == cleanWord &&
                    !freeTrainingSuccess
                ) {
                    startPracticeRecognition(cleanWord)
                    freeTrainingMessage =
                        "🎤 À vous : prononcez « $cleanWord »."
                }
            },
            950L
        )
    }

    // =========================================================
    // ARRÊTER LA LECTURE LIBRE
    // =========================================================

    fun stopFreeReading() {

        if (!freeReadingSessionActive) return

        freeReadingStopRequested = true
        isListening = false

        try {
            speechRecognizer.stopListening()
        } catch (_: Exception) {
        }
    }

    // =========================================================
    // FINALISATION APRÈS STOP
    // =========================================================

    LaunchedEffect(
        freeReadingStopRequested,
        isListening,
        freeReadingSessionActive
    ) {
        if (
            freeReadingStopRequested &&
            freeReadingSessionActive &&
            !isListening
        ) {
            kotlinx.coroutines.delay(550L)

            if (
                !freeReadingStopRequested ||
                !freeReadingSessionActive ||
                isListening
            ) {
                return@LaunchedEffect
            }

            freeReadingSessionActive = false
            freeReadingFinalized = true
            freeReadingStopRequested = false
            freeReadingPartialText = ""

            if (
                freeReadingRecognizedText.isBlank()
            ) {
                freeReadingError =
                    "🎙️ Aucun mot n'a été reconnu. Vous pouvez recommencer la lecture."
                noSpeechRecognized = true
                return@LaunchedEffect
            }

            freeReadingError = ""
            noSpeechRecognized = false

            val reference =
                freeReadingText
                    .lowercase(java.util.Locale.US)
                    .replace(
                        Regex("[^a-zA-Z'\\s]"),
                        " "
                    )
                    .split(
                        Regex("\\s+")
                    )
                    .filter {
                        it.isNotBlank()
                    }

            val spoken =
                freeReadingRecognizedText
                    .lowercase(java.util.Locale.US)
                    .replace(
                        Regex("[^a-zA-Z'\\s]"),
                        " "
                    )
                    .split(
                        Regex("\\s+")
                    )
                    .filter {
                        it.isNotBlank()
                    }

            val comparisons =
                alignWords(
                    reference =
                        reference,
                    spoken =
                        spoken
                )

            val exactCorrect =
                comparisons.count { comparison ->
                    comparison.spoken != null &&
                            wordsMatch(
                                comparison.expected,
                                comparison.spoken
                            )
                }

            val approximate =
                comparisons.count { comparison ->
                    comparison.spoken != null &&
                            !wordsMatch(
                                comparison.expected,
                                comparison.spoken
                            ) &&
                            isApproximateWordMatch(
                                comparison.expected,
                                comparison.spoken
                            )
                }

            freeInitialWrongWords =
                comparisons
                    .filter { comparison ->
                        comparison.spoken == null ||
                                (!wordsMatch(
                                    comparison.expected,
                                    comparison.spoken
                                ) && !isApproximateWordMatch(
                                    comparison.expected,
                                    comparison.spoken
                                ))
                    }
                    .map { comparison ->
                        normalizeWordForComparison(
                            comparison.expected
                        )
                    }
                    .filter { it.isNotBlank() }
                    .toSet()

            freeTrainingMasteredWords = emptySet()
            freeTrainingWord = null
            freeTrainingSuccess = false
            freeTrainingAttempts = 0
            freeTrainingMessage =
                if (freeInitialWrongWords.isNotEmpty()) {
                    "🎯 Les mots rouges sont prêts à être travaillés."
                } else {
                    "🏆 Excellent : aucun mot rouge à travailler."
                }

            val finalScore =
                if (reference.isNotEmpty()) {
                    (
                        (
                            exactCorrect +
                                    approximate * 0.5f
                            ) /
                                reference.size.toFloat() *
                                100f
                        )
                            .toInt()
                            .coerceIn(0, 100)
                } else {
                    0
                }

            progressStore.saveReadingScore(
                chapterIndex =
                    chapterIndex,
                sceneIndex =
                    sceneIndex,
                score =
                    finalScore
            )

            progressStore.saveReadingAttempt(
                chapterIndex =
                    chapterIndex,
                sceneIndex =
                    sceneIndex,
                score =
                    finalScore
            )

            progressStore.markSceneCompleted(
                chapterIndex =
                    chapterIndex,
                sceneIndex =
                    sceneIndex
            )

            val wrongWords =
                comparisons
                    .filter {
                        it.spoken == null ||
                                !wordsMatch(
                                    it.expected,
                                    it.spoken
                                )
                    }
                    .map {
                        it.expected
                    }
                    .map {
                        it.lowercase(
                            java.util.Locale.US
                        )
                            .replace(
                                "’",
                                "'"
                            )
                            .replace(
                                Regex("[^a-zA-Z']"),
                                ""
                            )
                            .trim()
                    }
                    .filter {
                        it.isNotBlank()
                    }
                    .distinct()

            if (wrongWords.isNotEmpty()) {
                progressStore.incrementSceneWordErrors(
                    chapterIndex =
                        chapterIndex,
                    sceneIndex =
                        sceneIndex,
                    words =
                        wrongWords
                )

                val updatedErrorCounts =
                    sceneWordErrorCounts.toMutableMap()

                wrongWords.forEach { word ->
                    updatedErrorCounts[word] =
                        (updatedErrorCounts[word] ?: 0) + 1
                }

                sceneWordErrorCounts =
                    updatedErrorCounts
            }

            recognitionError = ""
        }
    }

    // =========================================================
    // MOT CLIQUÉ : PRONONCIATION + TRADUCTION
    // =========================================================
    var selectedWordEnglish by remember(
        currentReadingSentence
    ) {
        mutableStateOf("")
    }

    var selectedWordFrench by remember(
        currentReadingSentence
    ) {
        mutableStateOf("")
    }

    var selectedWordArabic by remember(
        currentReadingSentence
    ) {
        mutableStateOf("")
    }

    var selectedWordTranslating by remember(
        currentReadingSentence
    ) {
        mutableStateOf(false)
    }

    var selectedWordTranslationError by remember(
        currentReadingSentence
    ) {
        mutableStateOf("")
    }

    var selectedWordClickToken by remember(
        currentReadingSentence
    ) {
        mutableIntStateOf(0)
    }

    val wordTranslationService = remember {
        OliverTranslationService()
    }

    DisposableEffect(wordTranslationService) {
        onDispose {
            wordTranslationService.close()
        }
    }

    fun speakClickedWord() {
        if (!ttsReady || selectedWordEnglish.isBlank()) return

        try {
            textToSpeech.stop()
            textToSpeech.language =
                java.util.Locale.US
            textToSpeech.setSpeechRate(0.72f)
            textToSpeech.setPitch(1.0f)
            textToSpeech.speak(
                selectedWordEnglish,
                android.speech.tts.TextToSpeech.QUEUE_FLUSH,
                null,
                "clicked_word_${selectedWordEnglish.hashCode()}"
            )
        } catch (_: Exception) {
        }
    }

    LaunchedEffect(
        selectedWordClickToken,
        ttsReady
    ) {
        if (selectedWordEnglish.isNotBlank() && ttsReady) {
            speakClickedWord()
        }
    }

    LaunchedEffect(
        selectedWordEnglish,
        currentReadingSentence
    ) {
        if (selectedWordEnglish.isBlank()) return@LaunchedEffect

        selectedWordTranslationError = ""

        val requestedWord =
            selectedWordEnglish

        val normalizedSelected =
            normalizeWordForComparison(requestedWord)

        val localVocabulary =
            scene.vocabulary.firstOrNull { vocabulary ->
                normalizeWordForComparison(vocabulary.word) ==
                        normalizedSelected
            }

        if (localVocabulary != null) {
            selectedWordFrench =
                localVocabulary.french
                    .ifBlank { localVocabulary.definition }
            selectedWordArabic =
                localVocabulary.arabic
            selectedWordTranslating = false
            return@LaunchedEffect
        }

        selectedWordFrench = ""
        selectedWordArabic = ""
        selectedWordTranslating = true

        wordTranslationService.translatePage(
            englishText = requestedWord,
            onSuccess = {
                    french: String,
                    arabic: String ->

                if (selectedWordEnglish == requestedWord) {
                    selectedWordFrench = french
                    selectedWordArabic = arabic
                    selectedWordTranslating = false
                    selectedWordTranslationError = ""
                }
            },
            onError = {
                    error: Exception ->

                if (selectedWordEnglish == requestedWord) {
                    selectedWordTranslating = false
                    selectedWordTranslationError =
                        error.message
                            ?: "Traduction indisponible"
                }
            }
        )
    }

    // =========================================================
    // SCORE RÉEL DE LA LECTURE
    // =========================================================

    var sceneReadingStats by remember(
        readingText,
        chapterIndex,
        sceneIndex,
        sentences.size
    ) {
        val restored =
            sentences.indices.mapNotNull { index ->
                progressStore
                    .getSentenceReadingResult(
                        chapterIndex = chapterIndex,
                        sceneIndex = sceneIndex,
                        sentenceIndex = index
                    )
                    ?.let { saved ->
                        index to (saved.correct to saved.total)
                    }
            }.toMap()

        mutableStateOf(restored)
    }

    var difficultWordsCardExpanded by remember(
        chapterIndex,
        sceneIndex
    ) {
        mutableStateOf(true)
    }

    var sceneRevisionPracticedWords by remember(readingText) {
        mutableStateOf(emptySet<String>())
    }

    var revisionPlanPracticedWords by remember(
        chapterIndex,
        sceneIndex,
        readingText
    ) {
        mutableStateOf(
            progressStore.getRevisionPlanProgress(
                chapterIndex = chapterIndex,
                sceneIndex = sceneIndex
            )
        )
    }

    var revisionPlanMode by remember(readingText) {
        mutableStateOf(false)
    }

    val savedRevisionPlanCurrentWord =
        progressStore.getRevisionPlanCurrentWord(
            chapterIndex = chapterIndex,
            sceneIndex = sceneIndex
        )

    // =========================================================
    // RÉVISION AUTOMATIQUE
    // =========================================================

    val sceneRevisionWords =
        sceneDifficultWordStats.map { it.first }

    // =========================================================
    // PLAN DE RÉVISION PERSONNALISÉ
    // =========================================================

    // Les 5 mots ayant le plus d'erreurs sont proposés en priorité.
    val revisionPlanWords =
        sceneDifficultWordStats
            .take(5)

    val revisionPlanDone =
        revisionPlanWords.count { (word, _) ->
            revisionPlanPracticedWords.contains(
                word.lowercase(java.util.Locale.US)
            )
        }

    val revisionPlanTotal = revisionPlanWords.size

    val revisionPlanRemaining =
        revisionPlanTotal - revisionPlanDone

    val revisionPlanPercent =
        if (revisionPlanTotal > 0) {
            (
                    revisionPlanDone.toFloat() /
                            revisionPlanTotal.toFloat() *
                            100f
                    ).toInt().coerceIn(0, 100)
        } else {
            0
        }

    val autoRevisionWord =
        sceneRevisionWords.getOrNull(autoRevisionIndex)

    val sceneRevisionTotal =
        sceneDifficultWordStats.size

    val sceneRevisionDone =
        sceneDifficultWordStats.count { (word, _) ->
            sceneRevisionPracticedWords.contains(
                word.lowercase(java.util.Locale.US)
            )
        }

    val sceneRevisionPercent =
        if (sceneRevisionTotal > 0) {
            (
                    sceneRevisionDone.toFloat() /
                            sceneRevisionTotal.toFloat() *
                            100f
                    )
                .toInt()
                .coerceIn(0, 100)
        } else {
            0
        }

    val bestRevisionScore =
        progressStore.getBestRevisionScore(
            chapterIndex = chapterIndex,
            sceneIndex = sceneIndex
        )

    val revisionMasteryText = when {
        bestRevisionScore >= 100 ->
            "🏆 Révision maîtrisée"
        bestRevisionScore >= 80 ->
            "🟢 Très bon niveau de révision"
        bestRevisionScore >= 60 ->
            "🟡 Niveau correct — continuez à pratiquer"
        bestRevisionScore >= 1 ->
            "🔵 Révision en cours"
        else ->
            "⚪ Révision non commencée"
    }

    LaunchedEffect(
        sceneRevisionPercent,
        chapterIndex,
        sceneIndex
    ) {
        if (sceneRevisionTotal > 0 && sceneRevisionPercent > 0) {
            progressStore.saveBestRevisionScore(
                chapterIndex = chapterIndex,
                sceneIndex = sceneIndex,
                score = sceneRevisionPercent
            )
        }
    }

    // Démarre automatiquement chaque mot : TTS puis microphone.
    LaunchedEffect(
        autoRevisionMode,
        autoRevisionIndex,
        sceneRevisionWords.size,
        revisionPlanMode,
        revisionPlanPracticedWords
    ) {
        if (!autoRevisionMode) return@LaunchedEffect

        val word =
            sceneRevisionWords.getOrNull(autoRevisionIndex)

        val normalizedPlanWords =
            revisionPlanWords.map { (planWord, _) ->
                planWord
                    .lowercase(java.util.Locale.US)
                    .replace(Regex("[^a-zA-Z']"), "")
            }.toSet()

        if (word == null) {
            if (revisionPlanMode) {
                progressStore.saveRevisionPlanProgress(
                    chapterIndex = chapterIndex,
                    sceneIndex = sceneIndex,
                    practicedWords = revisionPlanPracticedWords
                )
                progressStore.saveRevisionPlanCurrentWord(
                    chapterIndex = chapterIndex,
                    sceneIndex = sceneIndex,
                    word = null
                )
                revisionPlanMode = false
            }

            autoRevisionMode = false
            practiceWord = null
            practiceRecognizedText = ""
            practiceListening = false
            practiceCorrect = false
            return@LaunchedEffect
        }

        val normalizedWord =
            word.lowercase(java.util.Locale.US)
                .replace(Regex("[^a-zA-Z']"), "")

        if (revisionPlanMode && !normalizedPlanWords.contains(normalizedWord)) {
            val nextPriorityIndex =
                sceneRevisionWords.indexOfFirst { candidate ->
                    val normalizedCandidate =
                        candidate
                            .lowercase(java.util.Locale.US)
                            .replace(Regex("[^a-zA-Z']"), "")
                    normalizedPlanWords.contains(normalizedCandidate) &&
                            !revisionPlanPracticedWords.contains(normalizedCandidate)
                }

            if (nextPriorityIndex >= 0) {
                autoRevisionIndex = nextPriorityIndex
            } else {
                autoRevisionMode = false
                progressStore.saveRevisionPlanCurrentWord(
                    chapterIndex = chapterIndex,
                    sceneIndex = sceneIndex,
                    word = null
                )
                revisionPlanMode = false
            }
            return@LaunchedEffect
        }

        val alreadyMastered =
            if (revisionPlanMode) {
                revisionPlanPracticedWords.contains(normalizedWord)
            } else {
                sceneRevisionPracticedWords.contains(normalizedWord)
            }

        if (alreadyMastered) {
            autoRevisionIndex++
            return@LaunchedEffect
        }

        if (revisionPlanMode) {
            progressStore.saveRevisionPlanCurrentWord(
                chapterIndex = chapterIndex,
                sceneIndex = sceneIndex,
                word = word
            )
        }

        practiceWord = word
        practiceRecognizedText = ""
        practiceCorrect = false
        practiceListening = false
        recognitionError = ""

        try {
            speechRecognizer.stopListening()
        } catch (_: Exception) {
        }

        try {
            speechRecognizer.cancel()
        } catch (_: Exception) {
        }

        try {
            textToSpeech.stop()
            textToSpeech.language = java.util.Locale.US
            textToSpeech.setSpeechRate(0.72f)
            textToSpeech.setPitch(1.0f)
            textToSpeech.speak(
                word,
                android.speech.tts.TextToSpeech.QUEUE_FLUSH,
                null,
                "auto_revision_${autoRevisionIndex}"
            )
            isSpeaking = true
        } catch (_: Exception) {
            isSpeaking = false
        }

    }

    // Enregistrer un mot réussi dans la révision globale de la scène.
    LaunchedEffect(
        practiceCorrect,
        practiceWord,
        sceneDifficultWordStats
    ) {
        if (practiceCorrect && practiceWord != null) {
            val expected =
                practiceWord!!
                    .lowercase(java.util.Locale.US)
                    .replace(Regex("[^a-zA-Z']"), "")

            if (sceneDifficultWordStats.any { pair ->
                    pair.first
                        .lowercase(java.util.Locale.US) == expected
                }) {
                sceneRevisionPracticedWords =
                    sceneRevisionPracticedWords + expected
            }

            if (revisionPlanMode && revisionPlanWords.any { pair ->
                    pair.first
                        .lowercase(java.util.Locale.US) == expected
                }) {
                revisionPlanPracticedWords =
                    revisionPlanPracticedWords + expected

                progressStore.saveRevisionPlanProgress(
                    chapterIndex = chapterIndex,
                    sceneIndex = sceneIndex,
                    practicedWords = revisionPlanPracticedWords
                )
            }
        }
    }

    LaunchedEffect(
        revisionPlanMode,
        autoRevisionMode,
        autoRevisionIndex,
        revisionPlanPracticedWords
    ) {
        if (revisionPlanMode) {
            progressStore.saveRevisionPlanProgress(
                chapterIndex = chapterIndex,
                sceneIndex = sceneIndex,
                practicedWords = revisionPlanPracticedWords
            )

            progressStore.saveRevisionPlanCurrentWord(
                chapterIndex = chapterIndex,
                sceneIndex = sceneIndex,
                word = sceneRevisionWords.getOrNull(autoRevisionIndex)
            )
        }
    }

    var sentenceScoreRecorded by remember(
        currentReadingSentence
    ) {
        mutableStateOf(false)
    }

    // =========================================================
    // PHRASES DÉJÀ MAÎTRISÉES
    // =========================================================

    var masteredSentenceIndices by remember(
        readingText,
        chapterIndex,
        sceneIndex,
        sentences.size
    ) {
        val restored =
            sentences.indices
                .filter { index ->
                    progressStore.isSentenceMastered(
                        chapterIndex = chapterIndex,
                        sceneIndex = sceneIndex,
                        sentenceIndex = index
                    )
                }
                .toSet()

        mutableStateOf(restored)
    }

    // =========================================================
// RELECTURE OBLIGATOIRE
// =========================================================

    var rereadMode by remember(
        currentReadingSentence
    ) {
        mutableStateOf(false)
    }

    var rereadCompleted by remember(
        currentReadingSentence
    ) {
        mutableStateOf(false)
    }

    var readingCompletionSent by remember(
        currentReadingSentence
    ) {
        mutableStateOf(false)
    }

    // ---------------------------------------------------------
    // RÉINITIALISATION À CHAQUE NOUVELLE PHRASE
    // ---------------------------------------------------------

    var phraseCorrectionAttempts by remember(
        currentReadingSentence
    ) {
        mutableIntStateOf(0)
    }

    var phraseSummaryVisible by remember(
        currentReadingSentence
    ) {
        mutableStateOf(false)
    }

    LaunchedEffect(
        currentReadingSentence
    ) {
        recognizedText = ""
        recognizedCandidates = emptyList()
        recognitionFinalized = false
        noSpeechRecognized = false
        practiceRecognizedText = ""
        practiceWord = null
        practiceListening = false
        practiceCorrect = false
        automaticWrongWordPractice = false
        automaticWrongWordIndices = emptySet()
        automaticWrongWordAttempts = 0
        automaticWrongWordIndex = -1
        skippedAutomaticWrongWordIndices = emptySet()
        lastSkippedAutomaticWrongWord = null
        lastSkippedAutomaticWrongWordIndex = -1
        automaticWrongWordSkipRequested = false
        practicedWords = emptySet()
        autoRevisionMode = false
        revisionPlanMode = false
        autoRevisionIndex = 0
        sentenceScoreRecorded = false
        recognitionBelongsToCurrentSentence = false
        rereadMode = false
        rereadCompleted = false
        readingCompletionSent = false
        recognitionError = ""
        currentMismatchFeedback = null
        selectedSentenceIndex = -1
    }

    // =========================================================
// PROGRESSION DE LA LECTURE
// =========================================================

    val readingProgress =
        if (sentences.isNotEmpty()) {

            (
                    (readingSentenceIndex + 1).toFloat() /
                            sentences.size.toFloat()
                    ).coerceIn(
                    0f,
                    1f
                )

        } else {

            0f
        }

    val readingProgressPercent =
        (readingProgress * 100f)
            .toInt()
            .coerceIn(
                0,
                100
            )

// =========================================================
// MOTS DE LA PHRASE
// =========================================================

    val referenceWords =
        remember(
            currentReadingSentence
        ) {

            currentReadingSentence
                .lowercase(
                    java.util.Locale.US
                )
                .replace(
                    Regex("[^a-zA-Z'\\s]"),
                    " "
                )
                .split(
                    Regex("\\s+")
                )
                .filter {
                    it.isNotBlank()
                }
        }

// =========================================================
// MOTS RECONNUS
// =========================================================

    val bestRecognizedText =
        remember(
            referenceWords,
            recognizedCandidates,
            recognizedText
        ) {
            if (recognizedCandidates.isNotEmpty()) {
                chooseBestRecognitionCandidate(
                    reference = referenceWords,
                    candidates = recognizedCandidates
                )
            } else {
                recognizedText
            }
        }

    val spokenWords =
        remember(
            bestRecognizedText
        ) {

            bestRecognizedText
                .lowercase(
                    java.util.Locale.US
                )
                .replace(
                    Regex("[^a-zA-Z'\\s]"),
                    " "
                )
                .split(
                    Regex("\\s+")
                )
                .filter {
                    it.isNotBlank()
                }
        }

// =========================================================
// RELECTURE DE LA PHRASE
// =========================================================

    val rereadComparisons =
        remember(
            referenceWords,
            spokenWords
        ) {
            alignWords(
                reference = referenceWords,
                spoken = spokenWords
            )
        }

    val rereadIsCorrect =
        rereadMode &&
                referenceWords.isNotEmpty() &&
                bestRecognizedText.isNotBlank() &&
                !isListening &&
                rereadComparisons.size ==
                referenceWords.size &&
                rereadComparisons.all {
                    it.correct
                }

    val rereadWrongWords =
        rereadComparisons
            .filter { !it.correct }
            .map { it.expected }
            .distinct()

    LaunchedEffect(
        rereadMode,
        bestRecognizedText,
        isListening,
        recognitionFinalized,
        rereadIsCorrect
    ) {
        if (
            rereadMode &&
            !isListening &&
            recognitionFinalized &&
            bestRecognizedText.isNotBlank()
        ) {
            rereadCompleted = rereadIsCorrect
        }
    }

// =========================================================
// ALIGNEMENT DES MOTS
// =========================================================

    val wordComparisons =
        remember(
            referenceWords,
            spokenWords
        ) {

            alignWords(
                reference = referenceWords,
                spoken = spokenWords
            )
        }

    // =========================================================
    // ENREGISTREMENT DU SCORE DE LA PREMIÈRE LECTURE
    // =========================================================

    LaunchedEffect(
        isListening,
        recognizedText,
        bestRecognizedText,
        readingSentenceIndex,
        recognitionBelongsToCurrentSentence,
        recognitionFinalized,
        rereadMode
    ) {
        if (
            !isListening &&
            !rereadMode &&
            recognitionFinalized &&
            recognitionBelongsToCurrentSentence &&
            bestRecognizedText.isNotBlank() &&
            referenceWords.isNotEmpty() &&
            !sentenceScoreRecorded
        ) {
            val firstReadingComparisons =
                alignWords(
                    reference = referenceWords,
                    spoken = spokenWords
                )

            val evaluatedComparisons =
                firstReadingComparisons
                    .filter { it.spoken != null }

            val totalWords = evaluatedComparisons.size
            val correctWords =
                evaluatedComparisons.count { it.correct }

            val correctWordList =
                evaluatedComparisons
                    .filter { it.correct }
                    .map { it.expected }

            val wrongWordList =
                evaluatedComparisons
                    .filter { !it.correct }
                    .map { it.expected }

            // Une erreur est comptée une seule fois pour cette lecture
            // évaluée de la phrase, afin d'éviter les doublons provoqués
            // par les résultats partiels de SpeechRecognizer.
            val wrongWordsNormalized =
                wrongWordList
                    .map {
                        it.lowercase(java.util.Locale.US)
                            .replace("’", "'")
                            .replace(Regex("[^a-zA-Z']"), "")
                            .trim()
                    }
                    .filter { it.isNotBlank() }
                    .distinct()

            if (wrongWordsNormalized.isNotEmpty()) {
                val updatedErrorCounts =
                    sceneWordErrorCounts.toMutableMap()

                wrongWordsNormalized.forEach { word ->
                    updatedErrorCounts[word] =
                        (updatedErrorCounts[word] ?: 0) + 1
                }

                sceneWordErrorCounts = updatedErrorCounts

                progressStore.incrementSceneWordErrors(
                    chapterIndex = chapterIndex,
                    sceneIndex = sceneIndex,
                    words = wrongWordsNormalized
                )
            }

            sceneReadingStats =
                sceneReadingStats +
                        (
                                readingSentenceIndex to
                                        (correctWords to totalWords)
                                )

            sceneSentenceDetails =
                sceneSentenceDetails +
                        (
                                readingSentenceIndex to
                                        SentenceReadingDetail(
                                            correctWords = correctWordList,
                                            wrongWords = wrongWordList
                                        )
                                )

            progressStore.saveSentenceReadingResult(
                chapterIndex = chapterIndex,
                sceneIndex = sceneIndex,
                sentenceIndex = readingSentenceIndex,
                correctWords = correctWordList,
                wrongWords = wrongWordList,
                totalWords = totalWords
            )

            sentenceScoreRecorded = true
        }
    }

    // =========================================================
    // AVANCE AUTOMATIQUE MOT PAR MOT
    // =========================================================

    var confirmedWordCount by remember(
        currentReadingSentence
    ) {
        mutableIntStateOf(0)
    }

// =========================================================
// ÉTAT VISUEL DE CHAQUE MOT
// =========================================================

    val wordPronunciationStates =
        remember(
            currentReadingSentence,
            bestRecognizedText,
            spokenWords,
            isListening,
            recognitionFinalized,
            practicedWords
        ) {

            if (referenceWords.isEmpty()) {

                emptyList()

            } else {

                referenceWords.mapIndexed {
                        index,
                        expectedWord ->

                    // -------------------------------------------------
                    // Mot déjà réussi pendant l'entraînement
                    // -------------------------------------------------

                    if (
                        practicedWords.contains(
                            expectedWord
                        )
                    ) {

                        WordPronunciationState.CORRECT

                    } else if (
                        skippedAutomaticWrongWordIndices.contains(
                            index
                        )
                    ) {

                        // Mot laissé à reprendre après 3 essais : il reste rouge.
                        WordPronunciationState.WRONG

                    } else if (
                        index < confirmedWordCount
                    ) {

                        // Mot déjà validé : il reste vert pendant la tentative.
                        WordPronunciationState.CORRECT

                    } else if (
                        index > confirmedWordCount
                    ) {

                        // Un mot plus loin dans la phrase ne peut pas être validé
                        // avant le mot actuellement attendu.
                        WordPronunciationState.NONE

                    } else {

                        val comparison =
                            wordComparisons
                                .getOrNull(
                                    index
                                )

                        when {

                            comparison == null -> {

                                WordPronunciationState.NONE
                            }

                            comparison.spoken == null -> {

                                WordPronunciationState.NONE
                            }

                            !recognitionFinalized -> {

                                WordPronunciationState.UNCERTAIN
                            }

                            wordsMatch(
                                comparison.expected,
                                comparison.spoken
                            ) -> {

                                WordPronunciationState.CORRECT
                            }

                            isListening -> {

                                WordPronunciationState.UNCERTAIN
                            }

                            else -> {

                                WordPronunciationState.WRONG
                            }
                        }
                    }
                }
            }
        }
// =========================================================
// STATISTIQUES
// =========================================================

    LaunchedEffect(
        currentReadingSentence,
        bestRecognizedText,
        wordComparisons,
        recognitionFinalized,
        rereadMode
    ) {
        if (rereadMode || !recognitionFinalized || referenceWords.isEmpty()) {
            return@LaunchedEffect
        }

        if (bestRecognizedText.isBlank()) {
            confirmedWordCount = 0
            return@LaunchedEffect
        }

        var prefixCount = 0

        while (prefixCount < referenceWords.size) {
            val comparison =
                wordComparisons.getOrNull(prefixCount)

            if (comparison == null || !comparison.correct) {
                break
            }

            prefixCount++
        }

        // On ne recule jamais pendant la même tentative de lecture.
        if (prefixCount > confirmedWordCount) {
            confirmedWordCount = prefixCount
        }
    }

    val currentExpectedWordIndex =
        if (confirmedWordCount < referenceWords.size) {
            confirmedWordCount
        } else {
            -1
        }

    val currentExpectedWord =
        if (currentExpectedWordIndex >= 0) {
            referenceWords.getOrNull(
                currentExpectedWordIndex
            )
        } else {
            null
        }

    // =========================================================
    // FEEDBACK DU MOT ACTUEL
    // =========================================================

    LaunchedEffect(
        recognitionFinalized,
        bestRecognizedText,
        currentExpectedWordIndex,
        wordComparisons,
        rereadMode,
        practiceWord
    ) {
        if (
            rereadMode ||
            practiceWord != null ||
            !recognitionFinalized ||
            bestRecognizedText.isBlank() ||
            currentExpectedWordIndex < 0
        ) {
            currentMismatchFeedback = null
            return@LaunchedEffect
        }

        val comparison =
            wordComparisons.getOrNull(
                currentExpectedWordIndex
            )

        currentMismatchFeedback =
            if (
                comparison?.spoken != null &&
                !wordsMatch(
                    comparison.expected,
                    comparison.spoken!!
                )
            ) {
                comparison.spoken!! to comparison.expected
            } else {
                null
            }
    }

    // =========================================================
    // PASSAGE APRÈS 3 ESSAIS AUTOMATIQUES
    // =========================================================

    LaunchedEffect(
        automaticWrongWordSkipRequested,
        automaticWrongWordIndex
    ) {
        if (
            automaticWrongWordSkipRequested &&
            automaticWrongWordIndex >= 0
        ) {
            confirmedWordCount =
                maxOf(
                    confirmedWordCount,
                    automaticWrongWordIndex + 1
                )

            automaticWrongWordSkipRequested = false
            automaticWrongWordIndex = -1
        }
    }

    // =========================================================
    // ENTRAÎNEMENT AUTOMATIQUE DU MOT MAL PRONONCÉ + RÉÉCOUTE
    // =========================================================

    LaunchedEffect(
        bestRecognizedText,
        isListening,
        recognitionFinalized,
        rereadMode,
        currentExpectedWordIndex,
        wordComparisons,
        ttsReady
    ) {
        if (
            rereadMode ||
            isListening ||
            !recognitionFinalized ||
            bestRecognizedText.isBlank() ||
            currentExpectedWordIndex < 0 ||
            practiceWord != null ||
            automaticWrongWordPractice ||
            !ttsReady
        ) {
            return@LaunchedEffect
        }

        val comparison =
            wordComparisons.getOrNull(
                currentExpectedWordIndex
            ) ?: return@LaunchedEffect

        if (
            comparison.spoken == null ||
            wordsMatch(
                comparison.expected,
                comparison.spoken
            ) ||
            automaticWrongWordIndices.contains(
                currentExpectedWordIndex
            )
        ) {
            return@LaunchedEffect
        }

        val expectedWord =
            comparison.expected

        automaticWrongWordIndices =
            automaticWrongWordIndices +
                    currentExpectedWordIndex

        automaticWrongWordIndex =
            currentExpectedWordIndex
        automaticWrongWordAttempts = 0
        automaticWrongWordSkipRequested = false
        skippedAutomaticWrongWordIndices =
            skippedAutomaticWrongWordIndices -
                    currentExpectedWordIndex

        automaticWrongWordPractice = true
        phraseCorrectionAttempts++
        practiceWord = expectedWord
        practiceRecognizedText = ""
        practiceCorrect = false
        practiceListening = false
        recognitionError =
            "🔊 Écoutez le mot puis répétez-le."

        if (ttsReady) {
            try {
                speechRecognizer.stopListening()
            } catch (_: Exception) {
            }

            try {
                textToSpeech.stop()
                textToSpeech.language =
                    java.util.Locale.US
                textToSpeech.setSpeechRate(0.72f)
                textToSpeech.setPitch(1.0f)
                textToSpeech.speak(
                    expectedWord,
                    android.speech.tts.TextToSpeech.QUEUE_FLUSH,
                    null,
                    "auto_word_fix_${readingSentenceIndex}_${currentExpectedWordIndex}"
                )
            } catch (_: Exception) {
                // Le listener TTS gère le démarrage du microphone lorsque possible.
            }
        } else {
            handler.postDelayed(
                {
                    if (
                        automaticWrongWordPractice &&
                        practiceWord == expectedWord
                    ) {
                        startPracticeRecognition(expectedWord)
                    }
                },
                500L
            )
        }
    }

    // Une réussite dans l'entraînement automatique valide ce mot
    // pour la progression de la phrase, sans terminer la phrase.
    LaunchedEffect(
        automaticWrongWordPractice,
        practiceCorrect,
        practiceWord,
        currentExpectedWordIndex
    ) {
        if (
            automaticWrongWordPractice &&
            practiceCorrect &&
            practiceWord != null &&
            currentExpectedWordIndex >= 0
        ) {
            val practiced =
                practiceWord!!
                    .lowercase(java.util.Locale.US)
                    .replace(Regex("[^a-zA-Z']"), "")

            val targetIndex =
                if (automaticWrongWordIndex >= 0) {
                    automaticWrongWordIndex
                } else {
                    currentExpectedWordIndex
                }

            val expected =
                referenceWords.getOrNull(targetIndex)
                    ?.lowercase(java.util.Locale.US)
                    ?.replace(Regex("[^a-zA-Z']"), "")

            if (practiced == expected) {
                confirmedWordCount =
                    maxOf(
                        confirmedWordCount,
                        targetIndex + 1
                    )

                skippedAutomaticWrongWordIndices =
                    skippedAutomaticWrongWordIndices -
                            targetIndex
                automaticWrongWordAttempts = 0
                automaticWrongWordIndex = -1
                automaticWrongWordSkipRequested = false
                lastSkippedAutomaticWrongWord = null
                lastSkippedAutomaticWrongWordIndex = -1

                automaticWrongWordPractice = false
                practiceWord = null
                practiceListening = false
                recognitionError =
                    "✅ Mot corrigé. Continuez votre lecture."
            }
        }
    }

    val evaluatedWordCount =
        wordComparisons.count { it.spoken != null }

    val correctWords =
        wordComparisons
            .filter { it.spoken != null }
            .count { comparison ->
                wordsMatch(
                    comparison.expected,
                    comparison.spoken!!
                )
            }

    val wrongWords =
        wordComparisons
            .filterIndexed { index, comparison ->
                comparison.spoken != null &&
                        wordPronunciationStates.getOrNull(index) ==
                        WordPronunciationState.WRONG
            }
            .map {
                it.expected
            }

    val approximateWords =
        wordComparisons
            .filter { comparison ->
                comparison.spoken != null &&
                        isApproximateWordMatch(
                            comparison.expected,
                            comparison.spoken!!
                        )
            }

    val comparisonTotal =
        evaluatedWordCount
// =========================================================
// PHRASE MAÎTRISÉE
// =========================================================

    val allWordsGreen =
        referenceWords.isNotEmpty() &&
                wordPronunciationStates.size ==
                referenceWords.size &&
                wordPronunciationStates.all {
                    it == WordPronunciationState.CORRECT
                }

    val phraseWordsStillToRetry =
        skippedAutomaticWrongWordIndices
            .sorted()
            .mapNotNull { index ->
                referenceWords.getOrNull(index)
            }
            .distinct()

    val hasWordsStillToRetry =
        phraseWordsStillToRetry.isNotEmpty()

    val phraseMastered =
        allWordsGreen &&
                rereadCompleted

    LaunchedEffect(
        phraseMastered,
        rereadCompleted,
        readingSentenceIndex
    ) {
        if (phraseMastered || (allWordsGreen && rereadCompleted)) {
            phraseSummaryVisible = true
        }
    }

    val firstReadingDetail =
        sceneSentenceDetails[readingSentenceIndex]

    val firstReadingTotal =
        firstReadingDetail?.let { detail ->
            detail.correctWords.size + detail.wrongWords.size
        } ?: comparisonTotal

    val firstReadingCorrect =
        firstReadingDetail?.correctWords?.size ?: 0

    val firstReadingScore =
        if (firstReadingTotal > 0) {
            (
                    firstReadingCorrect.toFloat() /
                            firstReadingTotal.toFloat() *
                            100f
                    ).toInt().coerceIn(0, 100)
        } else {
            0
        }

    // =========================================================
    // SCORE DE RELECTURE — COMPARAISON AVEC LA PREMIÈRE LECTURE
    // =========================================================

    val rereadEvaluatedComparisons =
        rereadComparisons.filter {
            it.spoken != null
        }

    val rereadEvaluatedTotal =
        rereadEvaluatedComparisons.size

    val rereadCorrectCount =
        rereadEvaluatedComparisons.count {
            it.correct
        }

    val rereadScore =
        if (rereadEvaluatedTotal > 0) {
            (
                    rereadCorrectCount.toFloat() /
                            rereadEvaluatedTotal.toFloat() *
                            100f
                    ).toInt().coerceIn(0, 100)
        } else {
            0
        }

    val readingScoreImprovement =
        (rereadScore - firstReadingScore).coerceIn(-100, 100)

    val correctedWordCount =
        firstReadingDetail
            ?.wrongWords
            ?.distinct()
            ?.size
            ?: 0

    LaunchedEffect(
        phraseMastered,
        readingSentenceIndex
    ) {
        if (phraseMastered && readingSentenceIndex in sentences.indices) {
            masteredSentenceIndices =
                masteredSentenceIndices + readingSentenceIndex
        }
    }

    // =========================================================
    // VALIDATION AUTOMATIQUE DE LA SCÈNE
    // =========================================================

    LaunchedEffect(
        phraseMastered,
        readingSentenceIndex,
        sentences.size
    ) {
        if (phraseMastered) {
            masteredSentenceIndices =
                masteredSentenceIndices + readingSentenceIndex

            progressStore.markSentenceMastered(
                chapterIndex = chapterIndex,
                sceneIndex = sceneIndex,
                sentenceIndex = readingSentenceIndex
            )
        }

        if (
            phraseMastered &&
            sentences.isNotEmpty() &&
            readingSentenceIndex ==
            sentences.lastIndex &&
            !readingCompletionSent
        ) {
            val totalCorrectWords =
                sceneReadingStats.values.sumOf {
                    it.first
                }

            val totalReadingWords =
                sceneReadingStats.values.sumOf {
                    it.second
                }

            val finalReadingScore =
                if (totalReadingWords > 0) {
                    (
                            totalCorrectWords.toFloat() /
                                    totalReadingWords.toFloat() *
                                    100f
                            )
                        .toInt()
                        .coerceIn(0, 100)
                } else {
                    0
                }

            readingCompletionSent = true
            onReadingCompleted(finalReadingScore)
        }
    }

    // =========================================================
// PROGRESSION DES MOTS
// =========================================================

    val wordProgress =
        if (comparisonTotal > 0) {
            (
                    correctWords.toFloat() /
                            comparisonTotal.toFloat()
                    ).coerceIn(
                    0f,
                    1f
                )
        } else {
            0f
        }

    val wordProgressPercent =
        (
                wordProgress * 100f
                ).toInt().coerceIn(
                0,
                100
            )

// =========================================================
// PROGRESSION DES PHRASES
// =========================================================

    val totalPhraseCount =
        sentences.size

    val masteredPhraseCount =
        masteredSentenceIndices.size

    val attemptedPhraseCount =
        sceneReadingStats.size

// -------------------------------------------------------------
// POSITIONS DES MOTS
// -------------------------------------------------------------

    val wordRanges = remember(
        scene.title,
        readingText
    ) {
        sentences.map { sentence ->
            Regex("\\S+")
                .findAll(sentence)
                .map { it.range }
                .toList()
        }
    }

// -------------------------------------------------------------
// MÉMORISATION DE LA VITESSE
// -------------------------------------------------------------

    val speedPrefs = remember {
        context.getSharedPreferences(
            "oliver_audio_settings",
            android.content.Context.MODE_PRIVATE
        )
    }

    var speechRate by remember {
        mutableFloatStateOf(
            speedPrefs.getFloat(
                "speech_rate",
                0.72f
            )
        )
    }

    // -------------------------------------------------------------
    // SYNCHRONISATION TTS / MOTS
    // -------------------------------------------------------------
    DisposableEffect(
        textToSpeech,
        scene.title,
        readingText
    ) {

        textToSpeech.setOnUtteranceProgressListener(
            object :
                android.speech.tts.UtteranceProgressListener() {

                override fun onStart(
                    utteranceId: String?
                ) {
                    val sentenceIndex =
                        utteranceId
                            ?.substringAfter(
                                "oliver_",
                                ""
                            )
                            ?.toIntOrNull()
                            ?: return

                    handler.post {

                        currentSentence =
                            sentenceIndex

                        progress =
                            if (sentences.isEmpty()) {
                                0f
                            } else {
                                sentenceIndex.toFloat() /
                                        sentences.size.toFloat()
                            }

                        isSpeaking = true

                        // Premier mot
                        onWordChanged(
                            sentenceIndex,
                            0
                        )
                    }
                }

                override fun onRangeStart(
                    utteranceId: String?,
                    start: Int,
                    end: Int,
                    frame: Int
                ) {
                    if (
                        android.os.Build.VERSION.SDK_INT <
                        android.os.Build.VERSION_CODES.O
                    ) {
                        return
                    }

                    val sentenceIndex =
                        utteranceId
                            ?.substringAfter(
                                "oliver_",
                                ""
                            )
                            ?.toIntOrNull()
                            ?: return

                    val ranges =
                        wordRanges.getOrNull(
                            sentenceIndex
                        ) ?: return

                    val wordIndex =
                        ranges.indexOfFirst { range ->

                            start >= range.first &&
                                    start <= range.last + 1
                        }

                    if (wordIndex >= 0) {

                        handler.post {

                            currentSentence =
                                sentenceIndex

                            onWordChanged(
                                sentenceIndex,
                                wordIndex
                            )
                        }
                    }
                }

                override fun onDone(
                    utteranceId: String?
                ) {
                    if (
                        utteranceId?.startsWith("retry_skipped_") == true
                    ) {
                        handler.postDelayed(
                            {
                                if (
                                    automaticWrongWordPractice &&
                                    practiceWord != null
                                ) {
                                    startPracticeRecognition(
                                        practiceWord!!
                                    )
                                }
                            },
                            350L
                        )
                        return
                    }

                    if (
                        utteranceId?.startsWith("auto_word_fix_") == true
                    ) {
                        handler.postDelayed(
                            {
                                if (
                                    automaticWrongWordPractice &&
                                    practiceWord != null
                                ) {
                                    startPracticeRecognition(
                                        practiceWord!!
                                    )
                                }
                            },
                            350L
                        )
                        return
                    }

                    if (
                        utteranceId?.startsWith("auto_revision_") == true
                    ) {
                        handler.postDelayed(
                            {
                                if (autoRevisionMode && practiceWord != null) {
                                    startPracticeRecognition(practiceWord!!)
                                }
                            },
                            350L
                        )
                        return
                    }

                    val sentenceIndex =
                        utteranceId
                            ?.substringAfter(
                                "oliver_",
                                ""
                            )
                            ?.toIntOrNull()
                            ?: return

                    handler.post {

                        if (
                            sentenceIndex ==
                            sentences.lastIndex
                        ) {

                            progress = 1f

                            currentSentence =
                                sentences.size

                            isSpeaking = false

                            onWordChanged(
                                -1,
                                -1
                            )

                        } else {

                            progress =
                                if (sentences.isEmpty()) {
                                    0f
                                } else {
                                    (sentenceIndex + 1)
                                        .toFloat() /
                                            sentences.size
                                                .toFloat()
                                }
                        }
                    }
                }

                override fun onError(
                    utteranceId: String?
                ) {
                    if (
                        utteranceId?.startsWith("retry_skipped_") == true
                    ) {
                        handler.postDelayed(
                            {
                                if (
                                    automaticWrongWordPractice &&
                                    practiceWord != null
                                ) {
                                    startPracticeRecognition(
                                        practiceWord!!
                                    )
                                }
                            },
                            350L
                        )
                        return
                    }

                    if (
                        utteranceId?.startsWith("auto_word_fix_") == true
                    ) {
                        handler.postDelayed(
                            {
                                if (
                                    automaticWrongWordPractice &&
                                    practiceWord != null
                                ) {
                                    startPracticeRecognition(
                                        practiceWord!!
                                    )
                                }
                            },
                            350L
                        )
                        return
                    }

                    if (
                        utteranceId?.startsWith("auto_revision_") == true
                    ) {
                        handler.postDelayed(
                            {
                                if (autoRevisionMode && practiceWord != null) {
                                    startPracticeRecognition(practiceWord!!)
                                }
                            },
                            350L
                        )
                        return
                    }

                    handler.post {

                        isSpeaking = false

                        onWordChanged(
                            -1,
                            -1
                        )
                    }
                }
            }
        )

        onDispose {

            try {
                textToSpeech.stop()
                textToSpeech.shutdown()
            } catch (_: Exception) {
            }

            handler.post {

                isSpeaking = false

                onWordChanged(
                    -1,
                    -1
                )
            }
        }
    }

    // -------------------------------------------------------------
    // INTERFACE
    // -------------------------------------------------------------
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surface
        )
    ) {

        Column(
            modifier = Modifier.padding(11.dp)
        ) {

            // =========================================================
            // TITRE + BOUTON LIRE
            // =========================================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text = "🔊",
                    fontSize = 22.sp
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Lecture audio",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text =
                            if (isSpeaking) {
                                "Lecture mot par mot..."
                            } else {
                                "Écouter la page actuelle"
                            },
                        fontSize = 11.sp,
                        color =
                            MaterialTheme
                                .colorScheme
                                .onSurfaceVariant
                    )
                }

                Button(
                    enabled =
                        ttsReady &&
                                sentences.isNotEmpty(),

                    onClick = {

                        try {

                            if (isSpeaking) {

                                textToSpeech.stop()

                                isSpeaking = false

                                onWordChanged(
                                    -1,
                                    -1
                                )

                            } else {

                                textToSpeech.language =
                                    java.util.Locale.US

                                // -------------------------
                                // VITESSE CHOISIE
                                // -------------------------
                                textToSpeech.setSpeechRate(
                                    speechRate
                                )

                                textToSpeech.setPitch(
                                    1.0f
                                )

                                progress = 0f
                                currentSentence = 0

                                onWordChanged(
                                    0,
                                    0
                                )

                                sentences.forEachIndexed {
                                        index,
                                        sentence ->

                                    textToSpeech.speak(
                                        sentence,

                                        if (index == 0) {
                                            android.speech.tts
                                                .TextToSpeech
                                                .QUEUE_FLUSH
                                        } else {
                                            android.speech.tts
                                                .TextToSpeech
                                                .QUEUE_ADD
                                        },

                                        null,

                                        "oliver_$index"
                                    )
                                }

                                isSpeaking = true
                            }

                        } catch (_: Exception) {

                            isSpeaking = false

                            onWordChanged(
                                -1,
                                -1
                            )
                        }
                    }
                ) {
                    Text(
                        text =
                            if (isSpeaking) {
                                "⏹ Stop"
                            } else {
                                "▶ Lire"
                            }
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(9.dp)
            )

            // =========================================================
            // DÉBIT DE LECTURE
            // =========================================================
            Text(
                text = "Débit de lecture",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(6.dp)
            ) {

                // -----------------------------------------------------
                // TRÈS LENT
                // -----------------------------------------------------
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {

                            if (isSpeaking) {
                                textToSpeech.stop()
                                isSpeaking = false
                                onWordChanged(-1, -1)
                            }

                            speechRate = 0.55f

                            speedPrefs.edit()
                                .putFloat(
                                    "speech_rate",
                                    0.55f
                                )
                                .apply()
                        },
                    shape = RoundedCornerShape(9.dp),
                    colors = CardDefaults.cardColors(
                        containerColor =
                            if (speechRate == 0.55f) {
                                MaterialTheme
                                    .colorScheme
                                    .primary
                            } else {
                                MaterialTheme
                                    .colorScheme
                                    .primaryContainer
                            }
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        contentAlignment =
                            Alignment.Center
                    ) {
                        Text(
                            text = "🐢 Très lent",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color =
                                if (speechRate == 0.55f) {
                                    MaterialTheme
                                        .colorScheme
                                        .onPrimary
                                } else {
                                    MaterialTheme
                                        .colorScheme
                                        .primary
                                }
                        )
                    }
                }

                // -----------------------------------------------------
                // LENT
                // -----------------------------------------------------
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {

                            if (isSpeaking) {
                                textToSpeech.stop()
                                isSpeaking = false
                                onWordChanged(-1, -1)
                            }

                            speechRate = 0.72f

                            speedPrefs.edit()
                                .putFloat(
                                    "speech_rate",
                                    0.72f
                                )
                                .apply()
                        },
                    shape = RoundedCornerShape(9.dp),
                    colors = CardDefaults.cardColors(
                        containerColor =
                            if (speechRate == 0.72f) {
                                MaterialTheme
                                    .colorScheme
                                    .primary
                            } else {
                                MaterialTheme
                                    .colorScheme
                                    .primaryContainer
                            }
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        contentAlignment =
                            Alignment.Center
                    ) {
                        Text(
                            text = "🐢 Lent",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color =
                                if (speechRate == 0.72f) {
                                    MaterialTheme
                                        .colorScheme
                                        .onPrimary
                                } else {
                                    MaterialTheme
                                        .colorScheme
                                        .primary
                                }
                        )
                    }
                }

                // -----------------------------------------------------
                // NORMAL
                // -----------------------------------------------------
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {

                            if (isSpeaking) {
                                textToSpeech.stop()
                                isSpeaking = false
                                onWordChanged(-1, -1)
                            }

                            speechRate = 0.95f

                            speedPrefs.edit()
                                .putFloat(
                                    "speech_rate",
                                    0.95f
                                )
                                .apply()
                        },
                    shape = RoundedCornerShape(9.dp),
                    colors = CardDefaults.cardColors(
                        containerColor =
                            if (speechRate == 0.95f) {
                                MaterialTheme
                                    .colorScheme
                                    .primary
                            } else {
                                MaterialTheme
                                    .colorScheme
                                    .primaryContainer
                            }
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        contentAlignment =
                            Alignment.Center
                    ) {
                        Text(
                            text = "▶ Normal",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color =
                                if (speechRate == 0.95f) {
                                    MaterialTheme
                                        .colorScheme
                                        .onPrimary
                                } else {
                                    MaterialTheme
                                        .colorScheme
                                        .primary
                                }
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(9.dp)
            )

            // =========================================================
            // PROGRESSION
            // =========================================================
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                Text(
                    text =
                        if (sentences.isNotEmpty()) {
                            "Phrase ${
                                (currentSentence + 1)
                                    .coerceAtMost(
                                        sentences.size
                                    )
                            } / ${sentences.size}"
                        } else {
                            "0 / 0"
                        },
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color =
                        MaterialTheme
                            .colorScheme
                            .primary
                )

                Text(
                    text =
                        "${(progress * 100).toInt()} %",
                    fontSize = 11.sp,
                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant
                )
            }

            Spacer(
                modifier = Modifier.height(9.dp)
            )

            // =========================================================
            // =========================================================
// LECTURE LIBRE — L'ÉTUDIANT PARLE SANS ÊTRE BLOQUÉ
// =========================================================

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {

                    Text(
                        text = "🎤 Ma lecture — mode libre",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(
                        modifier = Modifier.height(5.dp)
                    )

                    Text(
                        text =
                            "Lisez toute la page à votre rythme. " +
                                    "Faites vos pauses naturellement : " +
                                    "l'application ne vous oblige pas à corriger un mot avant de continuer.",
                        fontSize = 11.sp,
                        color =
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(
                        modifier = Modifier.height(9.dp)
                    )

                    Button(
                        onClick = {
                            when {
                                !microphoneGranted -> {
                                    microphonePermissionLauncher.launch(
                                        Manifest.permission.RECORD_AUDIO
                                    )
                                }

                                freeReadingSessionActive -> {
                                    stopFreeReading()
                                }

                                else -> {
                                    startFreeReading()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            when {
                                !microphoneGranted ->
                                    "🎙️ Autoriser le microphone"

                                freeReadingSessionActive ->
                                    "⏹️ Terminer ma lecture"

                                freeReadingFinalized ->
                                    "🔄 Refaire la lecture"

                                else ->
                                    "🎙️ Commencer ma lecture"
                            }
                        )
                    }

                    if (freeReadingSessionActive) {

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        val signalLevel =
                            ((voiceRms + 40f) / 40f)
                                .coerceIn(0f, 1f)

                        Text(
                            text =
                                if (speechDetected) {
                                    "🗣️ Parole détectée — continuez librement..."
                                } else {
                                    "🎤 Micro ouvert — vous pouvez parler..."
                                },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color =
                                MaterialTheme.colorScheme.primary
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        LinearProgressIndicator(
                            progress = {
                                signalLevel
                            },
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(5.dp)
                        )

                        Spacer(
                            modifier = Modifier.height(3.dp)
                        )

                        Text(
                            text =
                                "Niveau du microphone : " +
                                        "${(signalLevel * 100).toInt()} %",
                            fontSize = 10.sp,
                            color =
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (freeReadingPartialText.isNotBlank()) {

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text =
                                "🗣️ Reconnaissance en cours : " +
                                        freeReadingPartialText,
                            fontSize = 11.sp,
                            color =
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    // =================================================
                    // TEXTE COLORÉ APRÈS L'ARRÊT
                    // =================================================

                    Text(
                        text = "📖 Votre lecture",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    androidx.compose.foundation.text.ClickableText(
                        text = buildAnnotatedString {

                            val displayWords =
                                Regex("\\S+")
                                    .findAll(freeReadingText)
                                    .map {
                                        it.value
                                    }
                                    .toList()

                            displayWords.forEachIndexed {
                                    index,
                                    word ->

                                if (index > 0) {
                                    append(" ")
                                }

                                val state =
                                    freeWordStates
                                        .getOrNull(index)
                                        ?: FreeReadingWordState.PENDING

                                val textColor =
                                    when (state) {
                                        FreeReadingWordState.CORRECT ->
                                            androidx.compose.ui.graphics.Color(
                                                0xFF16A34A
                                            )

                                        FreeReadingWordState.APPROXIMATE ->
                                            androidx.compose.ui.graphics.Color(
                                                0xFFF59E0B
                                            )

                                        FreeReadingWordState.WRONG ->
                                            androidx.compose.ui.graphics.Color(
                                                0xFFDC2626
                                            )

                                        FreeReadingWordState.PENDING ->
                                            MaterialTheme
                                                .colorScheme
                                                .onSurface
                                    }

                                val backgroundColor =
                                    when (state) {
                                        FreeReadingWordState.CORRECT ->
                                            androidx.compose.ui.graphics.Color(
                                                0xFFE8F5E9
                                            )

                                        FreeReadingWordState.APPROXIMATE ->
                                            androidx.compose.ui.graphics.Color(
                                                0xFFFEF3C7
                                            )

                                        FreeReadingWordState.WRONG ->
                                            androidx.compose.ui.graphics.Color(
                                                0xFFFEE2E2
                                            )

                                        FreeReadingWordState.PENDING ->
                                            androidx.compose.ui.graphics.Color.Transparent
                                    }

                                val cleanWord =
                                    word
                                        .trim()
                                        .replace(
                                            Regex(
                                                "^[^a-zA-Z']+|[^a-zA-Z']+$"
                                            ),
                                            ""
                                        )

                                withStyle(
                                    SpanStyle(
                                        color = textColor,
                                        background = backgroundColor,
                                        fontWeight =
                                            if (
                                                state ==
                                                FreeReadingWordState.PENDING
                                            ) {
                                                FontWeight.Normal
                                            } else {
                                                FontWeight.Bold
                                            }
                                    )
                                ) {

                                    val annotationStart =
                                        length

                                    append(word)

                                    addStringAnnotation(
                                        tag = "FREE_WORD",
                                        annotation = cleanWord,
                                        start = annotationStart,
                                        end = length
                                    )
                                }
                            }
                        },
                        modifier =
                            Modifier.fillMaxWidth(),
                        style =
                            androidx.compose.ui.text.TextStyle(
                                fontSize = 17.sp,
                                lineHeight = 27.sp
                            ),
                        onClick = { offset ->

                            val clicked =
                                buildAnnotatedString {

                                    val displayWords =
                                        Regex("\\S+")
                                            .findAll(
                                                freeReadingText
                                            )
                                            .map {
                                                it.value
                                            }
                                            .toList()

                                    displayWords.forEachIndexed {
                                            index,
                                            word ->

                                        if (index > 0) {
                                            append(" ")
                                        }

                                        val cleanWord =
                                            word
                                                .trim()
                                                .replace(
                                                    Regex(
                                                        "^[^a-zA-Z']+|[^a-zA-Z']+$"
                                                    ),
                                                    ""
                                                )

                                        val start =
                                            length

                                        append(word)

                                        addStringAnnotation(
                                            tag = "FREE_WORD",
                                            annotation = cleanWord,
                                            start = start,
                                            end = length
                                        )
                                    }
                                }
                                    .getStringAnnotations(
                                        tag = "FREE_WORD",
                                        start = offset,
                                        end = offset
                                    )
                                    .firstOrNull()
                                    ?.item
                                    .orEmpty()

                            if (clicked.isNotBlank()) {
                                selectedWordEnglish = clicked
                                selectedWordClickToken++
                            }
                        }
                    )

                    Spacer(
                        modifier = Modifier.height(7.dp)
                    )

                    Text(
                        text =
                            "🟢 Bien prononcé   " +
                                    "🟠 Approximatif   " +
                                    "🔴 À améliorer",
                        fontSize = 10.sp,
                        color =
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (freeReadingFinalized) {

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        if (
                            freeReadingRecognizedText.isBlank()
                        ) {

                            Text(
                                text =
                                    "⚠️ Aucun mot n'a été reconnu.",
                                fontSize = 12.sp,
                                color =
                                    MaterialTheme.colorScheme.error
                            )

                        } else {

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape =
                                    RoundedCornerShape(10.dp),
                                colors =
                                    CardDefaults.cardColors(
                                        containerColor =
                                            MaterialTheme
                                                .colorScheme
                                                .primaryContainer
                                    )
                            ) {

                                Column(
                                    modifier =
                                        Modifier.padding(10.dp)
                                ) {

                                    Row(
                                        modifier =
                                            Modifier.fillMaxWidth(),
                                        horizontalArrangement =
                                            Arrangement.SpaceBetween,
                                        verticalAlignment =
                                            Alignment.CenterVertically
                                    ) {

                                        Column(
                                            modifier =
                                                Modifier.weight(1f)
                                        ) {

                                            Text(
                                                text =
                                                    "📊 Résultat",
                                                fontSize = 13.sp,
                                                fontWeight =
                                                    FontWeight.Bold
                                            )

                                            Text(
                                                text =
                                                    "$freeScore %",
                                                fontSize = 25.sp,
                                                fontWeight =
                                                    FontWeight.Bold,
                                                color =
                                                    MaterialTheme
                                                        .colorScheme
                                                        .primary
                                            )
                                        }

                                        Column(
                                            horizontalAlignment =
                                                Alignment.End
                                        ) {

                                            Text(
                                                text =
                                                    "🟢 $freeCorrectWordCount",
                                                fontSize = 12.sp,
                                                color =
                                                    androidx.compose.ui.graphics.Color(
                                                        0xFF16A34A
                                                    )
                                            )

                                            Text(
                                                text =
                                                    "🟠 $freeApproximateWordCount",
                                                fontSize = 12.sp,
                                                color =
                                                    androidx.compose.ui.graphics.Color(
                                                        0xFFF59E0B
                                                    )
                                            )

                                            Text(
                                                text =
                                                    "🔴 $freeWrongWordCount",
                                                fontSize = 12.sp,
                                                color =
                                                    androidx.compose.ui.graphics.Color(
                                                        0xFFDC2626
                                                    )
                                            )
                                        }
                                    }

                                    Spacer(
                                        modifier =
                                            Modifier.height(7.dp)
                                    )

                                    LinearProgressIndicator(
                                        progress = {
                                            freeScore / 100f
                                        },
                                        modifier =
                                            Modifier.fillMaxWidth()
                                    )

                                    Spacer(
                                        modifier =
                                            Modifier.height(7.dp)
                                    )

                                    Text(
                                        text =
                                            "L'orange correspond à une prononciation reconnue comme proche. " +
                                                    "L'étudiant peut continuer sa lecture sans être bloqué.",
                                        fontSize = 10.sp,
                                        color =
                                            MaterialTheme
                                                .colorScheme
                                                .onSurfaceVariant
                                    )

                                    Spacer(
                                        modifier =
                                            Modifier.height(7.dp)
                                    )

                                    Text(
                                        text =
                                            "🗣️ Transcription reconnue :",
                                        fontSize = 11.sp,
                                        fontWeight =
                                            FontWeight.Bold
                                    )

                                    Text(
                                        text =
                                            freeReadingRecognizedText,
                                        fontSize = 11.sp,
                                        lineHeight = 18.sp,
                                        color =
                                            MaterialTheme
                                                .colorScheme
                                                .onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    if (freeReadingFinalized && freeInitialWrongWords.isNotEmpty()) {

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor =
                                    androidx.compose.ui.graphics.Color(0xFFFFF7ED)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(11.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = "🎯 Entraînement des mots rouges",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text =
                                                "$freeTrainingCompletedCount / $freeTrainingTotalCount maîtrisés",
                                            fontSize = 11.sp,
                                            color =
                                                MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Text(
                                        text = "${(freeTrainingProgress * 100).toInt()} %",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(
                                    modifier = Modifier.height(6.dp)
                                )

                                LinearProgressIndicator(
                                    progress = { freeTrainingProgress },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(
                                    modifier = Modifier.height(7.dp)
                                )

                                if (freeWrongTrainingWords.isNotEmpty()) {
                                    Text(
                                        text = "🔴 Cliquez sur un mot pour le travailler :",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(
                                        modifier = Modifier.height(5.dp)
                                    )

                                    freeWrongTrainingWords
                                        .chunked(3)
                                        .forEach { rowWords ->
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                                            ) {
                                                rowWords.forEach { word ->
                                                    AssistChip(
                                                        onClick = {
                                                            selectedWordEnglish = word
                                                            selectedWordClickToken++
                                                            startFreeWordTraining(word)
                                                        },
                                                        label = {
                                                            Text(
                                                                text = word,
                                                                maxLines = 1,
                                                                overflow = TextOverflow.Ellipsis,
                                                                fontSize = 11.sp
                                                            )
                                                        },
                                                        modifier = Modifier.weight(1f)
                                                    )
                                                }

                                                repeat(3 - rowWords.size) {
                                                    Spacer(
                                                        modifier = Modifier.weight(1f)
                                                    )
                                                }
                                            }

                                            Spacer(
                                                modifier = Modifier.height(3.dp)
                                            )
                                        }
                                } else {
                                    Text(
                                        text = "✅ Tous les mots rouges ont été travaillés.",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                if (freeTrainingWord != null) {
                                    Spacer(
                                        modifier = Modifier.height(7.dp)
                                    )

                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.surface
                                        )
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(9.dp)
                                        ) {
                                            Text(
                                                text = freeTrainingWord!!,
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary
                                            )

                                            Spacer(
                                                modifier = Modifier.height(4.dp)
                                            )

                                            Text(
                                                text =
                                                    if (practiceListening) {
                                                        "🎤 Prononcez le mot maintenant…"
                                                    } else {
                                                        freeTrainingMessage
                                                    },
                                                fontSize = 11.sp
                                            )

                                            if (practiceRecognizedText.isNotBlank()) {
                                                Spacer(
                                                    modifier = Modifier.height(4.dp)
                                                )
                                                Text(
                                                    text = "🗣️ Reconnu : $practiceRecognizedText",
                                                    fontSize = 10.sp,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }

                                            Spacer(
                                                modifier = Modifier.height(7.dp)
                                            )

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Button(
                                                    onClick = {
                                                        startFreeWordTraining(
                                                            freeTrainingWord!!
                                                        )
                                                    },
                                                    enabled = !practiceListening && !freeTrainingSuccess,
                                                    modifier = Modifier.weight(1f)
                                                ) {
                                                    Text("🎤 Répéter")
                                                }

                                                Button(
                                                    onClick = {
                                                        speakClickedWord()
                                                    },
                                                    enabled = ttsReady,
                                                    modifier = Modifier.weight(1f)
                                                ) {
                                                    Text("🔊 Écouter")
                                                }

                                                Button(
                                                    onClick = {
                                                        try { speechRecognizer.cancel() } catch (_: Exception) {}
                                                        try { textToSpeech.stop() } catch (_: Exception) {}
                                                        practiceWord = null
                                                        practiceListening = false
                                                        practiceRecognizedText = ""
                                                        freeTrainingWord = null
                                                        freeTrainingSuccess = false
                                                        freeTrainingMessage = ""
                                                    },
                                                    modifier = Modifier.weight(1f)
                                                ) {
                                                    Text("✕")
                                                }
                                            }

                                            if (freeTrainingSuccess) {
                                                Spacer(
                                                    modifier = Modifier.height(6.dp)
                                                )
                                                Text(
                                                    text = "✅ Mot validé. Choisissez un autre mot rouge.",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (
                        selectedWordEnglish.isNotBlank()
                    ) {

                        Spacer(
                            modifier =
                                Modifier.height(8.dp)
                        )

                        Card(
                            modifier =
                                Modifier.fillMaxWidth(),
                            shape =
                                RoundedCornerShape(12.dp),
                            colors =
                                CardDefaults.cardColors(
                                    containerColor =
                                        MaterialTheme
                                            .colorScheme
                                            .primaryContainer
                                )
                        ) {

                            Column(
                                modifier =
                                    Modifier.padding(12.dp)
                            ) {

                                Row(
                                    modifier =
                                        Modifier.fillMaxWidth(),
                                    verticalAlignment =
                                        Alignment.CenterVertically
                                ) {

                                    Column(
                                        modifier =
                                            Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text =
                                                "📖 Mot sélectionné",
                                            fontSize = 12.sp,
                                            fontWeight =
                                                FontWeight.Bold,
                                            color =
                                                MaterialTheme
                                                    .colorScheme
                                                    .primary
                                        )

                                        Text(
                                            text =
                                                selectedWordEnglish,
                                            fontSize = 21.sp,
                                            fontWeight =
                                                FontWeight.Bold
                                        )
                                    }

                                    Button(
                                        onClick = {
                                            speakClickedWord()
                                        },
                                        enabled = ttsReady
                                    ) {
                                        Text("🔊")
                                    }

                                    Spacer(
                                        modifier =
                                            Modifier.width(5.dp)
                                    )

                                    Button(
                                        onClick = {
                                            selectedWordEnglish = ""
                                        }
                                    ) {
                                        Text("✕")
                                    }
                                }

                                Spacer(
                                    modifier =
                                        Modifier.height(7.dp)
                                )

                                when {

                                    selectedWordTranslating -> {
                                        Text(
                                            text =
                                                "Traduction en cours…",
                                            fontSize = 13.sp
                                        )
                                    }

                                    selectedWordTranslationError
                                        .isNotBlank() -> {

                                        Text(
                                            text =
                                                selectedWordTranslationError,
                                            fontSize = 12.sp,
                                            color =
                                                MaterialTheme
                                                    .colorScheme
                                                    .error
                                        )
                                    }

                                    else -> {

                                        Text(
                                            text =
                                                "🇫🇷 $selectedWordFrench",
                                            fontSize = 14.sp
                                        )

                                        Spacer(
                                            modifier =
                                                Modifier.height(4.dp)
                                        )

                                        Text(
                                            text =
                                                "🇲🇦 $selectedWordArabic",
                                            fontSize = 15.sp,
                                            lineHeight = 25.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (freeReadingError.isNotBlank()) {

                Spacer(
                    modifier = Modifier.height(7.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(9.dp),
                    colors = CardDefaults.cardColors(
                        containerColor =
                            MaterialTheme.colorScheme.errorContainer
                    )
                ) {

                    Text(
                        text = freeReadingError,
                        modifier = Modifier.padding(9.dp),
                        fontSize = 11.sp,
                        color =
                            MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            if (freeReadingFinalized) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(9.dp),
                    colors = CardDefaults.cardColors(
                        containerColor =
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text =
                            "✅ Lecture analysée. Vous pouvez passer à la suite, même si certains mots sont rouges.",
                        modifier = Modifier.padding(9.dp),
                        fontSize = 11.sp,
                        color =
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

// MESSAGE D'ERREUR
// =========================================================

            if (recognitionError.isNotBlank()) {

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Card(
                    modifier =
                        Modifier.fillMaxWidth(),

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                MaterialTheme
                                    .colorScheme
                                    .errorContainer
                        ),

                    shape =
                        RoundedCornerShape(10.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Text(
                            text =
                                "⚠️ $recognitionError",
                            fontSize = 12.sp,
                            color =
                                MaterialTheme
                                    .colorScheme
                                    .onErrorContainer
                        )

                        if (noSpeechRecognized) {
                            Spacer(
                                modifier = Modifier.height(6.dp)
                            )

                            Text(
                                text =
                                    "Aucun mot n'a été ajouté au score. Appuyez sur « 🎙️ Commencer ma lecture » pour réessayer.",
                                fontSize = 11.sp,
                                color =
                                    MaterialTheme
                                        .colorScheme
                                        .onErrorContainer
                            )
                        }
                    }
                }
            }
        }
    }
