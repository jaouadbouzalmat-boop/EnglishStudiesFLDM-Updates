package ma.fldm.englishstudies
import ma.fldm.englishstudies.data.RobinsonBook
import ma.fldm.englishstudies.data.RobinsonChapter
import ma.fldm.englishstudies.data.RobinsonQuestion
import ma.fldm.englishstudies.data.RobinsonScene
import ma.fldm.englishstudies.data.RobinsonVocabulary
import ma.fldm.englishstudies.data.RobinsonCrusoeData
import ma.fldm.englishstudies.data.OliverTranslationService
import android.content.Intent
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
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

private class RobinsonProgressStore(
    private val context: android.content.Context
) {
    private val prefs = context.getSharedPreferences(
        "robinson_crusoe_progress",
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
    ): RobinsonPersistedSentenceReadingResult? {
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

        return RobinsonPersistedSentenceReadingResult(
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
// DONNÉES DE ROBINSON CRUSOE FOURNIES PAR NovelModels.kt / RobinsonCrusoeData.kt
// =====================================================================

// =====================================================================
// VOCABULAIRE DE SECOURS — UTILISÉ QUAND UNE SCÈNE N'A PAS ENCORE
// DE LISTE "vocabulary" DANS RobinsonCrusoeData.kt.
// =====================================================================

private data class RobinsonVocabularyInfo(
    val pronunciation: String,
    val definition: String,
    val french: String,
    val arabic: String
)

private val ROBINSON_VOCABULARY_CATALOG = mapOf(
    "assistance" to RobinsonVocabularyInfo("/əˈsɪstəns/", "help or support", "aide, assistance", "مساعدة"),
    "alarmed" to RobinsonVocabularyInfo("/əˈlɑːrmd/", "made suddenly worried or frightened", "alarmé, inquiet", "مذعور، قَلِق"),
    "frightened" to RobinsonVocabularyInfo("/ˈfraɪtənd/", "afraid or scared", "effrayé", "خائف"),
    "beard" to RobinsonVocabularyInfo("/bɪərd/", "hair growing on a man's chin and cheeks", "barbe", "لحية"),
    "native" to RobinsonVocabularyInfo("/ˈneɪtɪv/", "a person born in a particular place", "natif, originaire", "أصلي، من مواليد المكان"),
    "mission" to RobinsonVocabularyInfo("/ˈmɪʃən/", "an important task or purpose", "mission", "مهمة"),
    "complexions" to RobinsonVocabularyInfo("/kəmˈplekʃənz/", "the natural color and appearance of skin", "teints", "بشرات، ألوان البشرة"),
    "frontier" to RobinsonVocabularyInfo("/frʌnˈtɪr/", "the border between two areas or countries", "frontière", "حدود"),
    "bearing" to RobinsonVocabularyInfo("/ˈbeərɪŋ/", "a person's manner or posture", "allure, maintien", "هيئة، أسلوب"),
    "insult" to RobinsonVocabularyInfo("/ˈɪnsʌlt/", "an offensive remark or action", "insulte", "إهانة"),
    "observation" to RobinsonVocabularyInfo("/ˌɑːbzərˈveɪʃən/", "something noticed or remarked", "observation", "ملاحظة"),
    "district" to RobinsonVocabularyInfo("/ˈdɪstrɪkt/", "an area of a city or country", "quartier, district", "حي، منطقة"),
    "courtesan" to RobinsonVocabularyInfo("/ˈkɔːrtɪzæn/", "a woman associated with a royal or wealthy court", "courtisane", "مومس البلاط"),
    "immured" to RobinsonVocabularyInfo("/ɪˈmjʊrd/", "confined or enclosed", "emmuré, enfermé", "محبوس، محاط بجدران"),
    "quest" to RobinsonVocabularyInfo("/kwest/", "a search for something", "quête, recherche", "بحث، سعي"),
    "unparalleled" to RobinsonVocabularyInfo("/ʌnˈpærəleld/", "having no equal", "sans égal", "لا مثيل له"),
    "upholstered" to RobinsonVocabularyInfo("/ʌpˈhoʊlstərd/", "covered with soft material", "rembourré", "مُنجّد"),
    "intermittent" to RobinsonVocabularyInfo("/ˌɪntərˈmɪtənt/", "happening at intervals, not continuously", "intermittent", "متقطع"),
    "pleasant" to RobinsonVocabularyInfo("/ˈplezənt/", "enjoyable or agreeable", "agréable", "ممتع، لطيف"),
    "fragrant" to RobinsonVocabularyInfo("/ˈfreɪɡrənt/", "having a pleasant smell", "parfumé", "عَطِر"),
    "specialty" to RobinsonVocabularyInfo("/ˈspeʃəlti/", "a particular product or activity a place is known for", "spécialité", "تخصص، شيء مميز"),
    "intimidating" to RobinsonVocabularyInfo("/ɪnˈtɪmədeɪtɪŋ/", "making someone feel afraid or less confident", "intimidant", "مُخيف، مُرهِب"),
    "irreproachably" to RobinsonVocabularyInfo("/ˌɪrɪˈproʊtʃəbli/", "in a way that cannot reasonably be criticized", "irréprochablement", "بشكل لا تشوبه شائبة"),
    "substantial" to RobinsonVocabularyInfo("/səbˈstænʃəl/", "large or considerable in amount", "important, considérable", "كبير، مهم"),
    "Gothic" to RobinsonVocabularyInfo("/ˈɡɑːθɪk/", "a style of European architecture", "gothique", "قوطي"),
    "ingenious" to RobinsonVocabularyInfo("/ɪnˈdʒiːniəs/", "clever and inventive", "ingénieux", "ذكي، مبتكر"),
    "stonemasonry" to RobinsonVocabularyInfo("/ˈstoʊnˌmeɪsənri/", "the craft of building with stone", "maçonnerie de pierre", "بناء الحجارة"),
    "campus" to RobinsonVocabularyInfo("/ˈkæmpəs/", "the grounds of a school or university", "campus", "حرم جامعي"),
    "professors" to RobinsonVocabularyInfo("/prəˈfesərz/", "teachers at a university", "professeurs", "أساتذة جامعيون"),
    "titans" to RobinsonVocabularyInfo("/ˈtaɪtənz/", "people of exceptional importance or power", "géants, grandes figures", "عمالقة، شخصيات بارزة"),
    "assumptions" to RobinsonVocabularyInfo("/əˈsʌmpʃənz/", "beliefs accepted without proof", "hypothèses, suppositions", "افتراضات"),
    "daunting" to RobinsonVocabularyInfo("/ˈdɔːntɪŋ/", "seeming difficult or intimidating", "intimidant, difficile", "مرهِب، صعب"),
    "selection" to RobinsonVocabularyInfo("/sɪˈlekʃən/", "the act of choosing", "sélection", "اختيار"),
    "enrolled" to RobinsonVocabularyInfo("/ɪnˈroʊld/", "officially registered as a student", "inscrit", "مسجّل"),
    "compatriots" to RobinsonVocabularyInfo("/kəmˈpeɪtriəts/", "people from the same country", "compatriotes", "مواطنون من نفس البلد"),
    "international" to RobinsonVocabularyInfo("/ˌɪntərˈnæʃənəl/", "involving more than one country", "international", "دولي"),
    "pragmatic" to RobinsonVocabularyInfo("/præɡˈmætɪk/", "focused on practical results", "pragmatique", "عملي"),
    "effective" to RobinsonVocabularyInfo("/ɪˈfektɪv/", "successful in producing the desired result", "efficace", "فعّال"),
    "standardized" to RobinsonVocabularyInfo("/ˈstændərdaɪzd/", "made according to a common standard", "standardisé", "موحّد المعايير"),
    "painstakingly" to RobinsonVocabularyInfo("/ˈpeɪnzteɪkɪŋli/", "with great care and effort", "avec beaucoup de soin", "بعناية كبيرة"),
    "customized" to RobinsonVocabularyInfo("/ˈkʌstəmaɪzd/", "made for a particular person or purpose", "personnalisé", "مخصّص"),
    "meritocracy" to RobinsonVocabularyInfo("/ˌmerɪˈtɑːkrəsi/", "a system rewarding ability and achievement", "méritocratie", "نظام الجدارة"),
    "talents" to RobinsonVocabularyInfo("/ˈtælənts/", "natural abilities or skills", "talents, aptitudes", "مواهب"),
    "society" to RobinsonVocabularyInfo("/səˈsaɪəti/", "a community of people living together", "société", "مجتمع"),
    "corporate" to RobinsonVocabularyInfo("/ˈkɔːrpərət/", "relating to a company or business", "d'entreprise", "متعلق بالشركات"),
    "recruiters" to RobinsonVocabularyInfo("/rɪˈkruːtərz/", "people who find and hire employees", "recruteurs", "موظفو التوظيف"),
    "eloquent" to RobinsonVocabularyInfo("/ˈeləkwənt/", "able to express ideas clearly and effectively", "éloquent", "بليغ"),
    "defiant" to RobinsonVocabularyInfo("/dɪˈfaɪənt/", "showing resistance or refusal to obey", "défiant", "متحدٍّ"),
    "gravity" to RobinsonVocabularyInfo("/ˈɡrævəti/", "the force that pulls objects toward Earth", "gravité", "جاذبية"),
    "analyst" to RobinsonVocabularyInfo("/ˈænəlɪst/", "a person who examines information to make judgments", "analyste", "محلل"),
    "valuation" to RobinsonVocabularyInfo("/ˌvæljuˈeɪʃən/", "an estimate of how much something is worth", "évaluation", "تقييم القيمة"),
    "precision" to RobinsonVocabularyInfo("/prɪˈsɪʒən/", "accuracy and exactness", "précision", "دقة"),
    "uncanny" to RobinsonVocabularyInfo("/ʌnˈkæni/", "strangely impressive or unusual", "étrange, troublant", "غريب، غير مألوف"),
    "boutique" to RobinsonVocabularyInfo("/buːˈtiːk/", "a small specialized business", "petite entreprise spécialisée", "شركة صغيرة متخصصة"),
    "robust" to RobinsonVocabularyInfo("/roʊˈbʌst/", "strong and effective", "solide, robuste", "متين، قوي"),
    "exalted" to RobinsonVocabularyInfo("/ɪɡˈzɔːltɪd/", "highly respected or prestigious", "prestigieux, élevé", "مرموق، رفيع"),
    "analyst" to RobinsonVocabularyInfo("/ˈænəlɪst/", "a person who studies information carefully", "analyste", "محلل"),
    "interview" to RobinsonVocabularyInfo("/ˈɪntərvjuː/", "a formal conversation for assessment or selection", "entretien", "مقابلة"),
    "transcript" to RobinsonVocabularyInfo("/ˈtrænskrɪpt/", "an official record of academic results", "relevé de notes", "كشف النقط"),
    "tenacious" to RobinsonVocabularyInfo("/təˈneɪʃəs/", "determined and unwilling to give up", "tenace, persévérant", "مثابر"),
    "physiotherapy" to RobinsonVocabularyInfo("/ˌfɪzioʊˈθerəpi/", "treatment using physical exercises and methods", "kinésithérapie", "العلاج الطبيعي"),
    "judgmental" to RobinsonVocabularyInfo("/dʒʌdʒˈmentəl/", "too quick to judge other people", "porté à juger", "كثير الحكم على الآخرين"),
    "appraising" to RobinsonVocabularyInfo("/əˈpreɪzɪŋ/", "carefully judging value or quality", "évaluateur", "مُقيِّم"),
    "jeweler" to RobinsonVocabularyInfo("/ˈdʒuːələr/", "a person who sells or works with jewelry", "bijoutier", "صائغ"),
    "transcript" to RobinsonVocabularyInfo("/ˈtrænskrɪpt/", "an official academic record", "relevé de notes", "سجل دراسي"),
    "regulators" to RobinsonVocabularyInfo("/ˈreɡjəleɪtərz/", "official bodies that supervise an activity or industry", "organismes de réglementation", "هيئات تنظيمية"),
    "competitors" to RobinsonVocabularyInfo("/kəmˈpetɪtərz/", "people or companies trying to win the same market", "concurrents", "منافسون"),
    "revenues" to RobinsonVocabularyInfo("/ˈrevənuːz/", "money earned by a company or organization", "revenus", "إيرادات"),
    "profits" to RobinsonVocabularyInfo("/ˈprɑːfɪts/", "money left after costs are paid", "bénéfices", "أرباح"),
    "discounted" to RobinsonVocabularyInfo("/dɪˈskaʊntɪd/", "reduced in value using a financial calculation", "actualisé", "مُخفَّض بالقيمة الحالية"),
    "assumptions" to RobinsonVocabularyInfo("/əˈsʌmpʃənz/", "ideas accepted as true for reasoning", "hypothèses", "افتراضات"),
    "brother" to RobinsonVocabularyInfo("/ˈbrʌðər/", "a male sibling", "frère", "أخ"),
    "female" to RobinsonVocabularyInfo("/ˈfiːmeɪl/", "of or relating to a woman or girl", "féminin, femelle", "أنثى، مؤنث"),
    "traditional" to RobinsonVocabularyInfo("/trəˈdɪʃənəl/", "following a long-established custom", "traditionnel", "تقليدي"),
    "attractive" to RobinsonVocabularyInfo("/əˈtræktɪv/", "pleasant or appealing in appearance", "attrayant, séduisant", "جذاب"),
    "different" to RobinsonVocabularyInfo("/ˈdɪfrənt/", "not the same", "différent", "مختلف"),
    "matter" to RobinsonVocabularyInfo("/ˈmætər/", "a subject, situation, or concern", "question, affaire", "أمر، مسألة"),
    "caught" to RobinsonVocabularyInfo("/kɔːt/", "noticed, trapped, or captured", "attrapé, remarqué", "أمسك، لاحظ"),
    "homeland" to RobinsonVocabularyInfo("/ˈhoʊmlænd/", "a person's native country", "pays natal, patrie", "الوطن"),
    "preference" to RobinsonVocabularyInfo("/ˈprefərəns/", "a greater liking for one thing than another", "préférence", "تفضيل"),
    "intensity" to RobinsonVocabularyInfo("/ɪnˈtensəti/", "the degree of strength or force", "intensité", "شدة"),
    "gaze" to RobinsonVocabularyInfo("/ɡeɪz/", "a long or steady look", "regard, regard fixe", "نظرة، تحديق"),
    "students" to RobinsonVocabularyInfo("/ˈstuːdənts/", "people who are studying", "étudiants", "طلاب"),
    "establishment" to RobinsonVocabularyInfo("/ɪˈstæblɪʃmənt/", "a business or organization", "établissement", "مؤسسة، محل"),
    "intermittent" to RobinsonVocabularyInfo("/ˌɪntərˈmɪtənt/", "not continuous; stopping and starting", "intermittent", "متقطع"),
    "corner" to RobinsonVocabularyInfo("/ˈkɔːrnər/", "the place where two streets or sides meet", "coin", "زاوية، ركن"),
    "college" to RobinsonVocabularyInfo("/ˈkɑːlɪdʒ/", "an institution of higher education", "université, collège", "كلية"),
    "scholarships" to RobinsonVocabularyInfo("/ˈskɑːlərʃɪps/", "financial support for education", "bourses d'études", "منح دراسية"),
    "financial" to RobinsonVocabularyInfo("/faɪˈnænʃəl/", "related to money or finance", "financier", "مالي"),
    "society" to RobinsonVocabularyInfo("/səˈsaɪəti/", "a community of people", "société", "مجتمع"),
    "experience" to RobinsonVocabularyInfo("/ɪkˈspɪriəns/", "knowledge gained by doing or living through something", "expérience", "تجربة، خبرة"),
    "arrived" to RobinsonVocabularyInfo("/əˈraɪvd/", "reached a place", "arrivé", "وصل"),
    "campus" to RobinsonVocabularyInfo("/ˈkæmpəs/", "the grounds of a university or school", "campus", "حرم جامعي"),
    "family" to RobinsonVocabularyInfo("/ˈfæməli/", "a group of related people", "famille", "عائلة"),
    "country" to RobinsonVocabularyInfo("/ˈkʌntri/", "a nation or its territory", "pays", "بلد، دولة"),
    "important" to RobinsonVocabularyInfo("/ɪmˈpɔːrtənt/", "having great value or significance", "important", "مهم"),
    "understand" to RobinsonVocabularyInfo("/ˌʌndərˈstænd/", "to know the meaning of something", "comprendre", "يفهم"),
    "question" to RobinsonVocabularyInfo("/ˈkwestʃən/", "a sentence asking for information", "question", "سؤال"),
    "future" to RobinsonVocabularyInfo("/ˈfjuːtʃər/", "the time that is yet to come", "avenir", "مستقبل"),
    "silence" to RobinsonVocabularyInfo("/ˈsaɪləns/", "absence of sound or speech", "silence", "صمت"),
    "attention" to RobinsonVocabularyInfo("/əˈtenʃən/", "careful notice or concentration", "attention", "انتباه"),
    "relationship" to RobinsonVocabularyInfo("/rɪˈleɪʃənʃɪp/", "a connection between people or things", "relation", "علاقة"),
    "generation" to RobinsonVocabularyInfo("/ˌdʒenəˈreɪʃən/", "people born and living at about the same time", "génération", "جيل"),
    "possibility" to RobinsonVocabularyInfo("/ˌpɑːsəˈbɪləti/", "something that may happen or be true", "possibilité", "إمكانية"),
    "institution" to RobinsonVocabularyInfo("/ˌɪnstɪˈtuːʃən/", "an established organization", "institution", "مؤسسة"),
    "manuscript" to RobinsonVocabularyInfo("/ˈmænjuskrɪpt/", "a handwritten or original written text", "manuscrit", "مخطوط"),
    "nostalgia" to RobinsonVocabularyInfo("/nɑːˈstældʒə/", "a sentimental feeling about the past", "nostalgie", "حنين إلى الماضي"),
    "foreigner" to RobinsonVocabularyInfo("/ˈfɔːrənər/", "a person from another country", "étranger", "أجنبي"),
    "reality" to RobinsonVocabularyInfo("/riˈæləti/", "the state of things as they actually exist", "réalité", "واقع"),
    "desire" to RobinsonVocabularyInfo("/dɪˈzaɪər/", "a strong wish for something", "désir", "رغبة"),
    "connection" to RobinsonVocabularyInfo("/kəˈnekʃən/", "a link or relationship", "connexion, lien", "صلة، ارتباط"),
    "observe" to RobinsonVocabularyInfo("/əbˈzɜːrv/", "to watch or notice carefully", "observer", "يلاحظ"),
    "gesture" to RobinsonVocabularyInfo("/ˈdʒestʃər/", "a movement expressing an idea or feeling", "geste", "إشارة، حركة"),
    "mistake" to RobinsonVocabularyInfo("/mɪˈsteɪk/", "an error", "erreur", "خطأ"),
    "emotional" to RobinsonVocabularyInfo("/ɪˈmoʊʃənəl/", "related to feelings", "émotionnel", "عاطفي"),
    "likelihood" to RobinsonVocabularyInfo("/ˈlaɪklihʊd/", "the chance that something will happen", "probabilité", "احتمال"),
    "constant" to RobinsonVocabularyInfo("/ˈkɑːnstənt/", "continuing without change", "constant", "ثابت"),
    "precisely" to RobinsonVocabularyInfo("/prɪˈsaɪsli/", "exactly; accurately", "précisément", "بدقة"),
    "events" to RobinsonVocabularyInfo("/ɪˈvents/", "things that happen", "événements", "أحداث"),
    "visitors" to RobinsonVocabularyInfo("/ˈvɪzɪtərz/", "people who come to a place", "visiteurs", "زوار"),
    "international" to RobinsonVocabularyInfo("/ˌɪntərˈnæʃənəl/", "involving different countries", "international", "دولي"),
    "assure" to RobinsonVocabularyInfo("/əˈʃʊr/", "to tell someone confidently that something is true", "assurer", "يؤكد، يطمئن"),
    "noticed" to RobinsonVocabularyInfo("/ˈnoʊtɪst/", "became aware of something", "remarqué", "لاحظ"),
    "possible" to RobinsonVocabularyInfo("/ˈpɑːsəbəl/", "able to happen or exist", "possible", "ممكن"),
    "relationship" to RobinsonVocabularyInfo("/rɪˈleɪʃənʃɪp/", "a connection between people", "relation", "علاقة"),
    "attention" to RobinsonVocabularyInfo("/əˈtenʃən/", "focused notice", "attention", "انتباه")
)

private fun robinsonVocabularyForScene(scene: RobinsonScene): List<RobinsonVocabulary> {
    if (scene.vocabulary.isNotEmpty()) return scene.vocabulary

    val source = scene.englishText
    val normalizedWords = Regex("[A-Za-z][A-Za-z'-]{3,}")
        .findAll(source)
        .map { it.value.lowercase() }
        .toList()

    val selectedKeys = linkedSetOf<String>()
    for (word in normalizedWords) {
        if (ROBINSON_VOCABULARY_CATALOG.containsKey(word)) {
            selectedKeys += word
        }
        if (selectedKeys.size >= 12) break
    }

    return selectedKeys.mapNotNull { key ->
        val info = ROBINSON_VOCABULARY_CATALOG[key] ?: return@mapNotNull null
        val example = Regex("(?i)([^.!?]*\\b${Regex.escape(key)}\\b[^.!?]*[.!?])")
            .find(source)
            ?.value
            ?.trim()
            ?.takeIf { it.isNotBlank() }
            ?: "This word appears in the selected scene."

        RobinsonVocabulary(
            word = key,
            pronunciation = info.pronunciation,
            definition = info.definition,
            french = info.french,
            arabic = info.arabic,
            example = example
        )
    }
}


private fun robinsonComprehensionItems(scene: RobinsonScene): List<String> {
    val fromQuestions = scene.questions.map { it.explanation }.filter { it.isNotBlank() }
    return if (fromQuestions.isNotEmpty()) fromQuestions else listOf(
        scene.englishText.trim().replace(Regex("\\s+"), " ")
    )
}

@Composable
fun RobinsonCrusoeScreen(
    onBack: () -> Unit
) {
    var selectedChapterIndex by remember { mutableIntStateOf(0) }
    var selectedSceneIndex by remember { mutableIntStateOf(0) }
    var currentSection by remember { mutableStateOf("lecture") }

    val book: RobinsonBook = RobinsonCrusoeData.book
    val chapter = book.chapters.getOrNull(selectedChapterIndex)
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
                text = "Robinson Crusoe n'est pas disponible.",
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
        RobinsonHeader(
            onBack = onBack,
            currentSection = currentSection,
            onSectionChange = { currentSection = it }
        )

        when (currentSection) {
            "lecture" -> {
                RobinsonLectureDashboard(
                    chapter = chapter,
                    scene = scene,
                    chapterIndex = selectedChapterIndex,
                    sceneIndex = selectedSceneIndex,
                    fullText = scene.englishText,
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
                    RobinsonSectionTitle(
                        icon = "📚",
                        title = "Vocabulaire important",
                        subtitle = "Les mots essentiels de la scène sélectionnée"
                    )
                    RobinsonVocabularySection(
                        scene = scene,
                        showExamples = true
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
                    RobinsonSectionTitle(
                        icon = "📝",
                        title = "Exercices",
                        subtitle = "Vérifiez votre compréhension"
                    )
                    RobinsonExerciseSection(
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
                    RobinsonSectionTitle(
                        icon = "📊",
                        title = "Progression",
                        subtitle = "Suivez votre progression dans The Robinson Crusoe"
                    )
                    RobinsonProgressSection(
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
fun RobinsonHeader(
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
                    RobinsonHeaderBackButton(onBack = onBack)
                    Spacer(modifier = Modifier.width(10.dp))

                    Text(text = "📖", fontSize = 31.sp)
                    Spacer(modifier = Modifier.width(8.dp))

                    Column(
                        modifier = Modifier.width(195.dp)
                    ) {
                        Text(
                            text = "The Robinson Crusoe",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Daniel Defoe",
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
                        RobinsonTopButton("📖 Lecture", currentSection == "lecture") {
                            onSectionChange("lecture")
                        }
                        RobinsonTopButton("📚 Vocabulaire", currentSection == "vocabulaire") {
                            onSectionChange("vocabulaire")
                        }
                        RobinsonTopButton("📝 Exercices", currentSection == "exercices") {
                            onSectionChange("exercices")
                        }
                        RobinsonTopButton("📊 Progression", currentSection == "progression") {
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
                        RobinsonHeaderBackButton(onBack = onBack)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "📖", fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(7.dp))
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "The Robinson Crusoe",
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Daniel Defoe",
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
                        RobinsonTopButton("📖 Lecture", currentSection == "lecture") {
                            onSectionChange("lecture")
                        }
                        RobinsonTopButton("📚 Vocabulaire", currentSection == "vocabulaire") {
                            onSectionChange("vocabulaire")
                        }
                        RobinsonTopButton("📝 Exercices", currentSection == "exercices") {
                            onSectionChange("exercices")
                        }
                        RobinsonTopButton("📊 Progression", currentSection == "progression") {
                            onSectionChange("progression")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RobinsonHeaderBackButton(
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
fun RobinsonTopButton(
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
fun RobinsonLectureDashboard(
    chapter: RobinsonChapter,
    scene: RobinsonScene,
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
            RobinsonProgressStore(
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
                RobinsonSidebar(
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
                        RobinsonCenterReading(
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

                RobinsonVocabularySidebar(
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
                RobinsonCompactNavigation(
                    chapter = chapter,
                    chapterIndex = chapterIndex,
                    sceneIndex = sceneIndex,
                    onChapterSelected = onChapterSelected,
                    onSceneSelected = onSceneSelected
                )

                key(readingSessionKey) {
                    RobinsonCenterReading(
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
                RobinsonVocabularySidebar(
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
private fun RobinsonSidebar(
    chapter: RobinsonChapter,
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

            RobinsonCrusoeData.book.chapters.forEachIndexed { index, item ->
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
fun RobinsonCompactNavigation(
    chapter: RobinsonChapter,
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

                RobinsonCrusoeData.book.chapters.forEachIndexed {
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
private fun RobinsonCenterReading(
    chapter: RobinsonChapter,
    scene: RobinsonScene,
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
            splitRobinsonTextIntoPages(
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
        // TRADUCTION FRANÇAISE + ARABE
        // =============================================================

        val frenchTranslation = scene.frenchTranslation
        val arabicTranslation = scene.arabicExplanation
        val isTranslating = false
        val translationError = ""

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

                    RobinsonSceneIllustration(
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
                                    robinsonBuildWordHighlightedText(
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

                    RobinsonAudioPlayer(
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

private fun splitRobinsonTextIntoPages(
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
private fun RobinsonSceneIllustration(
    scene: RobinsonScene
) {
    val context = LocalContext.current
    val imageResourceId = remember(scene.image) {
        scene.image
            ?.substringBeforeLast('.')
            ?.substringAfterLast('/')
            ?.lowercase(java.util.Locale.US)
            ?.takeIf { it.isNotBlank() }
            ?.let { name -> context.resources.getIdentifier(name, "drawable", context.packageName) }
            ?.takeIf { it != 0 }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        if (imageResourceId != null) {
            androidx.compose.foundation.Image(
                painter = androidx.compose.ui.res.painterResource(imageResourceId),
                contentDescription = "Illustration de ${scene.title}",
                modifier = Modifier.fillMaxSize(),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🖼️", fontSize = 72.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Illustration de la scène",
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
fun RobinsonBottomLearningPanels(
    scene: RobinsonScene
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
                    RobinsonInfoCard(
                        title = "💡 Compréhension",
                        colorType = "green",
                        modifier = Modifier.weight(1f)
                    ) {
                        robinsonComprehensionItems(scene).take(5).forEach { item ->
                            Text(
                                text = "• $item",
                                fontSize = 12.sp,
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                        }
                    }

                    RobinsonInfoCard(
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
                    RobinsonInfoCard(
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

                    RobinsonExercisePreview(
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
                RobinsonInfoCard(
                    title = "💡 Compréhension",
                    colorType = "green"
                ) {
                    robinsonComprehensionItems(scene).forEach { item ->
                        Text(
                            text = "• $item",
                            fontSize = 14.sp,
                            lineHeight = 21.sp
                        )
                    }
                }

                RobinsonInfoCard(
                    title = "🇫🇷 Traduction",
                    colorType = "red"
                ) {
                    Text(
                        text = scene.frenchTranslation,
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                }

                RobinsonInfoCard(
                    title = "🇲🇦 الشرح بالعربية",
                    colorType = "yellow"
                ) {
                    Text(
                        text = scene.arabicExplanation,
                        fontSize = 15.sp,
                        lineHeight = 24.sp
                    )
                }

                RobinsonExercisePreview(scene = scene)
            }
        }
    }
}

@Composable
fun RobinsonExercisePreview(
    scene: RobinsonScene,
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
fun RobinsonVocabularySidebar(
    scene: RobinsonScene,
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

            val vocabularyItems = remember(
                scene.englishText,
                scene.vocabulary
            ) {
                robinsonVocabularyForScene(scene)
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                vocabularyItems.forEachIndexed { index, vocabulary ->
                    RobinsonVocabularyCard(
                        vocabulary = vocabulary,
                        showExample = false
                    )

                    if (index < vocabularyItems.lastIndex) {
                        Spacer(modifier = Modifier.height(7.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun RobinsonVocabularySection(
    scene: RobinsonScene,
    showExamples: Boolean = true
) {
    val vocabularyItems = remember(
        scene.englishText,
        scene.vocabulary
    ) {
        robinsonVocabularyForScene(scene)
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        vocabularyItems.forEachIndexed { index, vocabulary ->
            RobinsonVocabularyCard(
                vocabulary = vocabulary,
                showExample = showExamples
            )

            if (index < vocabularyItems.lastIndex) {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
@Composable
fun RobinsonVocabularyCard(
    vocabulary: RobinsonVocabulary,
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

    var isSpeaking by remember(vocabulary.word) {
        mutableStateOf(false)
    }

    var textToSpeech by remember(vocabulary.word) {
        mutableStateOf<android.speech.tts.TextToSpeech?>(null)
    }

    DisposableEffect(vocabulary.word) {
        onDispose {
            try {
                textToSpeech?.stop()
                textToSpeech?.shutdown()
            } catch (_: Exception) {
            }
        }
    }

    fun speakVocabularyWord() {
        try {
            val existing = textToSpeech

            if (existing != null) {
                if (isSpeaking) {
                    existing.stop()
                    isSpeaking = false
                } else {
                    existing.language = java.util.Locale.US
                    existing.setSpeechRate(0.78f)
                    existing.setPitch(1.0f)
                    existing.speak(
                        vocabulary.word,
                        android.speech.tts.TextToSpeech.QUEUE_FLUSH,
                        null,
                        "robinson_vocab_${vocabulary.word}"
                    )
                    isSpeaking = true
                }
                return
            }

            val created = android.speech.tts.TextToSpeech(context) { status ->
                if (status == android.speech.tts.TextToSpeech.SUCCESS) {
                    val engine = textToSpeech ?: return@TextToSpeech
                    engine.language = java.util.Locale.US
                    engine.setSpeechRate(0.78f)
                    engine.setPitch(1.0f)
                    engine.setOnUtteranceProgressListener(
                        object : android.speech.tts.UtteranceProgressListener() {
                            override fun onStart(utteranceId: String?) {
                                isSpeaking = true
                            }

                            override fun onDone(utteranceId: String?) {
                                isSpeaking = false
                            }

                            override fun onError(utteranceId: String?) {
                                isSpeaking = false
                            }
                        }
                    )
                    engine.speak(
                        vocabulary.word,
                        android.speech.tts.TextToSpeech.QUEUE_FLUSH,
                        null,
                        "robinson_vocab_${vocabulary.word}"
                    )
                    isSpeaking = true
                }
            }

            textToSpeech = created
        } catch (_: Exception) {
            isSpeaking = false
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
                            .clickable {
                                speakVocabularyWord()
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
fun RobinsonSectionTitle(
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
fun RobinsonExerciseSection(
    scene: RobinsonScene,
    chapterIndex: Int,
    sceneIndex: Int
) {
    val context = LocalContext.current
    val progressStore = remember { RobinsonProgressStore(context) }

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
fun RobinsonProgressSection(
    chapterIndex: Int,
    sceneIndex: Int
) {
    val context = LocalContext.current
    val progressStore = remember { RobinsonProgressStore(context) }

    var refreshKey by remember { mutableIntStateOf(0) }

    val totalScenes = RobinsonCrusoeData.book.chapters.sumOf { it.scenes.size }

    val completedScenes = RobinsonCrusoeData.book.chapters.flatMapIndexed { chapterPos, chapter ->
        chapter.scenes.mapIndexed { scenePos, _ ->
            progressStore.isSceneCompleted(chapterPos, scenePos)
        }
    }.count { it }

    val currentSceneNumber =
        RobinsonCrusoeData.book.chapters
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
                            text = RobinsonCrusoeData.book.chapters[chapterIndex].title,
                            fontSize = 13.sp
                        )

                        Spacer(modifier = Modifier.height(3.dp))

                        Text(
                            text = RobinsonCrusoeData.book
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
                        "🏆 Meilleur score : $bestScore / ${RobinsonCrusoeData.book.chapters[chapterIndex].scenes[sceneIndex].questions.size}"
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
                        "🎉 The Robinson Crusoe terminé !"
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
fun RobinsonInfoCard(
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

private enum class RobinsonWordPronunciationState {
    NONE,
    CORRECT,
    WRONG,
    UNCERTAIN
}

// =====================================================================
// COMPARAISON DES MOTS
// =====================================================================

private data class RobinsonWordComparison(
    val expected: String,
    val spoken: String?,
    val correct: Boolean
)

// =====================================================================
// LECTURE LIBRE — ÉTAT VISUEL DES MOTS
// =====================================================================

private enum class RobinsonFreeReadingWordState {
    PENDING,
    CORRECT,
    APPROXIMATE,
    WRONG
}

// =====================================================================
// TOLÉRANCE AUX PETITES DIFFÉRENCES DE TRANSCRIPTION
// =====================================================================

private fun robinsonNormalizeWordForComparison(word: String): String {
    return word
        .lowercase(java.util.Locale.US)
        .replace("’", "'")
        .replace(Regex("[^a-zA-Z']"), "")
        .replace("'", "")
}

private fun robinsonIsEnglishVowel(char: Char): Boolean {
    return char in "aeiou"
}

private fun robinsonWeightedWordEditDistance(
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
                robinsonIsEnglishVowel(currentChar) &&
                        robinsonIsEnglishVowel(spokenChar) -> 0.35
                else -> 1.0
            }

            val deletionCost =
                if (robinsonIsEnglishVowel(currentChar)) 0.35 else 1.0

            val insertionCost =
                if (robinsonIsEnglishVowel(spokenChar)) 0.35 else 1.0

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

private fun robinsonWordsMatch(
    expected: String,
    spoken: String
): Boolean {
    val expectedNormalized =
        robinsonNormalizeWordForComparison(expected)
    val spokenNormalized =
        robinsonNormalizeWordForComparison(spoken)

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
        robinsonWeightedWordEditDistance(
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

private fun robinsonIsApproximateWordMatch(
    expected: String,
    spoken: String
): Boolean {
    val expectedNormalized =
        robinsonNormalizeWordForComparison(expected)
    val spokenNormalized =
        robinsonNormalizeWordForComparison(spoken)

    if (expectedNormalized.isEmpty() || spokenNormalized.isEmpty()) {
        return false
    }

    return expectedNormalized != spokenNormalized &&
            robinsonWordsMatch(expected, spoken)
}

// =====================================================================
// ALIGNEMENT INTELLIGENT DES MOTS
// =====================================================================

private fun robinsonAlignWords(
    reference: List<String>,
    spoken: List<String>
): List<RobinsonWordComparison> {

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
                robinsonNormalizeWordForComparison(expectedWord) ==
                        robinsonNormalizeWordForComparison(spokenWord) -> 0.0

                robinsonIsApproximateWordMatch(expectedWord, spokenWord) -> 1.5

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
        mutableListOf<RobinsonWordComparison>()

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
                robinsonNormalizeWordForComparison(expectedWord) ==
                        robinsonNormalizeWordForComparison(spokenWord) -> 0.0

                robinsonIsApproximateWordMatch(expectedWord, spokenWord) -> 1.5

                else -> 2.0
            }

            if (
                dp[i][j] ==
                dp[i - 1][j - 1] +
                substitutionCost
            ) {

                result.add(
                    RobinsonWordComparison(
                        expected =
                            reference[i - 1],
                        spoken =
                            spoken[j - 1],
                        correct =
                            robinsonWordsMatch(
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
                RobinsonWordComparison(
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

private fun robinsonRecognitionCandidateScore(
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
        robinsonAlignWords(
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

private fun robinsonChooseBestRecognitionCandidate(
    reference: List<String>,
    candidates: List<String>
): String {
    return candidates
        .filter { it.isNotBlank() }
        .maxByOrNull { candidate ->
            robinsonRecognitionCandidateScore(
                reference = reference,
                candidate = candidate
            )
        }
        ?: candidates.firstOrNull().orEmpty()
}

// =====================================================================
// LECTURE LIBRE — SÉLECTION ET CONCATÉNATION DES CHUNKS RECONNUS
// =====================================================================

private fun robinsonChooseBestFreeReadingCandidate(
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
            robinsonRecognitionCandidateScore(
                reference = remainingReference,
                candidate = candidate
            )
        }
        ?: candidates.firstOrNull().orEmpty()
}

private fun robinsonAppendFreeReadingChunk(
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

private fun robinsonBuildWordHighlightedText(
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

private data class RobinsonPersistedSentenceReadingResult(
    val correctWords: List<String>,
    val wrongWords: List<String>,
    val correct: Int,
    val total: Int
)

private data class RobinsonSentenceReadingDetail(
    val correctWords: List<String>,
    val wrongWords: List<String>
)

// =====================================================================
// LECTEUR AUDIO — VITESSE + SYNCHRONISATION MOT PAR MOT
// =====================================================================

@Composable
fun RobinsonAudioPlayer(
    scene: RobinsonScene,
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
        RobinsonProgressStore(context)
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
                                    robinsonNormalizeWordForComparison(
                                        freeTrainingWord!!
                                    ) == robinsonNormalizeWordForComparison(
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
                                robinsonChooseBestFreeReadingCandidate(
                                    referenceText =
                                        freeReadingReferenceText,
                                    alreadyRecognizedText =
                                        freeReadingRecognizedText,
                                    candidates = candidates
                                )

                            if (bestCandidate.isNotBlank()) {
                                freeReadingRecognizedText =
                                    robinsonAppendFreeReadingChunk(
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
                                    robinsonWordsMatch(expected, spokenWord) -> 2
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
                            robinsonWordsMatch(
                                expected,
                                normalizedSpoken
                            )

                        if (practiceCorrect) {
                            practicedWords =
                                practicedWords + expected

                            recognitionError = ""

                            if (freeTrainingWord != null &&
                                robinsonNormalizeWordForComparison(
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
                                robinsonNormalizeWordForComparison(
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
                        index to RobinsonSentenceReadingDetail(
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

    val freeWordComparisons: List<RobinsonWordComparison> =
        remember(
            freeReadingFinalized,
            freeReadingRecognizedText,
            freeReadingText
        ) {
            if (
                freeReadingFinalized &&
                freeReadingRecognizedText.isNotBlank()
            ) {
                robinsonAlignWords(
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
                    RobinsonFreeReadingWordState.PENDING
                }
            } else {
                freeReferenceWords.mapIndexed { index, referenceWord ->
                    val normalizedReferenceWord =
                        robinsonNormalizeWordForComparison(referenceWord)

                    if (
                        freeTrainingMasteredWords.contains(
                            normalizedReferenceWord
                        )
                    ) {
                        RobinsonFreeReadingWordState.CORRECT
                    } else {
                        val comparison =
                            freeWordComparisons.getOrNull(index)

                        when {
                            comparison == null ||
                                    comparison.spoken == null ->
                                RobinsonFreeReadingWordState.WRONG

                        robinsonWordsMatch(
                            comparison.expected,
                            comparison.spoken
                        ) ->
                            RobinsonFreeReadingWordState.CORRECT

                        robinsonIsApproximateWordMatch(
                            comparison.expected,
                            comparison.spoken
                        ) ->
                            RobinsonFreeReadingWordState.APPROXIMATE

                            else ->
                                RobinsonFreeReadingWordState.WRONG
                        }
                    }
                }
            }
        }

    val freeCorrectWordCount =
        freeWordStates.count {
            it == RobinsonFreeReadingWordState.CORRECT
        }

    val freeApproximateWordCount =
        freeWordStates.count {
            it == RobinsonFreeReadingWordState.APPROXIMATE
        }

    val freeWrongWordCount =
        freeWordStates.count {
            it == RobinsonFreeReadingWordState.WRONG
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
                        cleanWord to robinsonNormalizeWordForComparison(cleanWord)
                    }
                    .filter { (_, normalized) ->
                        freeInitialWrongWords.contains(normalized) &&
                                !freeTrainingMasteredWords.contains(normalized)
                    }
                    .map { (display, _) -> display }
                    .distinctBy { robinsonNormalizeWordForComparison(it) }
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
            robinsonNormalizeWordForComparison(cleanWord)

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
            robinsonNormalizeWordForComparison(
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
                robinsonAlignWords(
                    reference =
                        reference,
                    spoken =
                        spoken
                )

            val exactCorrect =
                comparisons.count { comparison ->
                    comparison.spoken != null &&
                            robinsonWordsMatch(
                                comparison.expected,
                                comparison.spoken
                            )
                }

            val approximate =
                comparisons.count { comparison ->
                    comparison.spoken != null &&
                            !robinsonWordsMatch(
                                comparison.expected,
                                comparison.spoken
                            ) &&
                            robinsonIsApproximateWordMatch(
                                comparison.expected,
                                comparison.spoken
                            )
                }

            freeInitialWrongWords =
                comparisons
                    .filter { comparison ->
                        comparison.spoken == null ||
                                (!robinsonWordsMatch(
                                    comparison.expected,
                                    comparison.spoken
                                ) && !robinsonIsApproximateWordMatch(
                                    comparison.expected,
                                    comparison.spoken
                                ))
                    }
                    .map { comparison ->
                        robinsonNormalizeWordForComparison(
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
                                !robinsonWordsMatch(
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
            robinsonNormalizeWordForComparison(requestedWord)

        val localVocabulary =
            scene.vocabulary.firstOrNull { vocabulary ->
                robinsonNormalizeWordForComparison(vocabulary.word) ==
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

        // Le mot n'est pas dans le petit vocabulaire prédéfini :
        // on utilise le même service de traduction que dans Oliver Twist.
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
                robinsonChooseBestRecognitionCandidate(
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
            robinsonAlignWords(
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

            robinsonAlignWords(
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
                robinsonAlignWords(
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
                                        RobinsonSentenceReadingDetail(
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

                        RobinsonWordPronunciationState.CORRECT

                    } else if (
                        skippedAutomaticWrongWordIndices.contains(
                            index
                        )
                    ) {

                        // Mot laissé à reprendre après 3 essais : il reste rouge.
                        RobinsonWordPronunciationState.WRONG

                    } else if (
                        index < confirmedWordCount
                    ) {

                        // Mot déjà validé : il reste vert pendant la tentative.
                        RobinsonWordPronunciationState.CORRECT

                    } else if (
                        index > confirmedWordCount
                    ) {

                        // Un mot plus loin dans la phrase ne peut pas être validé
                        // avant le mot actuellement attendu.
                        RobinsonWordPronunciationState.NONE

                    } else {

                        val comparison =
                            wordComparisons
                                .getOrNull(
                                    index
                                )

                        when {

                            comparison == null -> {

                                RobinsonWordPronunciationState.NONE
                            }

                            comparison.spoken == null -> {

                                RobinsonWordPronunciationState.NONE
                            }

                            !recognitionFinalized -> {

                                RobinsonWordPronunciationState.UNCERTAIN
                            }

                            robinsonWordsMatch(
                                comparison.expected,
                                comparison.spoken
                            ) -> {

                                RobinsonWordPronunciationState.CORRECT
                            }

                            isListening -> {

                                RobinsonWordPronunciationState.UNCERTAIN
                            }

                            else -> {

                                RobinsonWordPronunciationState.WRONG
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
                !robinsonWordsMatch(
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
            robinsonWordsMatch(
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
                robinsonWordsMatch(
                    comparison.expected,
                    comparison.spoken!!
                )
            }

    val wrongWords =
        wordComparisons
            .filterIndexed { index, comparison ->
                comparison.spoken != null &&
                        wordPronunciationStates.getOrNull(index) ==
                        RobinsonWordPronunciationState.WRONG
            }
            .map {
                it.expected
            }

    val approximateWords =
        wordComparisons
            .filter { comparison ->
                comparison.spoken != null &&
                        robinsonIsApproximateWordMatch(
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
                    it == RobinsonWordPronunciationState.CORRECT
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
                                        ?: RobinsonFreeReadingWordState.PENDING

                                val textColor =
                                    when (state) {
                                        RobinsonFreeReadingWordState.CORRECT ->
                                            androidx.compose.ui.graphics.Color(
                                                0xFF16A34A
                                            )

                                        RobinsonFreeReadingWordState.APPROXIMATE ->
                                            androidx.compose.ui.graphics.Color(
                                                0xFFF59E0B
                                            )

                                        RobinsonFreeReadingWordState.WRONG ->
                                            androidx.compose.ui.graphics.Color(
                                                0xFFDC2626
                                            )

                                        RobinsonFreeReadingWordState.PENDING ->
                                            MaterialTheme
                                                .colorScheme
                                                .onSurface
                                    }

                                val backgroundColor =
                                    when (state) {
                                        RobinsonFreeReadingWordState.CORRECT ->
                                            androidx.compose.ui.graphics.Color(
                                                0xFFE8F5E9
                                            )

                                        RobinsonFreeReadingWordState.APPROXIMATE ->
                                            androidx.compose.ui.graphics.Color(
                                                0xFFFEF3C7
                                            )

                                        RobinsonFreeReadingWordState.WRONG ->
                                            androidx.compose.ui.graphics.Color(
                                                0xFFFEE2E2
                                            )

                                        RobinsonFreeReadingWordState.PENDING ->
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
                                                RobinsonFreeReadingWordState.PENDING
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
