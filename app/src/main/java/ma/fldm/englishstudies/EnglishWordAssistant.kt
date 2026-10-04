package ma.fldm.englishstudies

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.view.SoundEffectConstants
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.resume

/*
 * ============================================================
 * ENGLISH WORD ASSISTANT
 * ============================================================
 *
 * Clic sur un mot anglais :
 *
 *   English word 🔊
 *   -----------------
 *   Français : traduction
 *   العربية : الترجمة
 *
 * IMPORTANT :
 * - La voix prononce UNIQUEMENT le mot anglais.
 * - Aucune voix française.
 * - Aucune voix arabe.
 * - La traduction FR + AR est affichée uniquement en texte.
 *
 * Utilisation dans une page :
 *
 * EnglishWordText(
 *     text = "Oliver Twist was born in a workhouse.",
 *     fontSize = 16.sp,
 *     lineHeight = 27.sp
 * )
 *
 * Prérequis Gradle :
 *
 * implementation("com.google.mlkit:translate:17.0.3")
 *
 * ============================================================
 */

/* ------------------------------------------------------------
 * 1. TRADUCTION ENGLISH -> FRENCH / ARABIC
 * ------------------------------------------------------------ */

object EnglishWordTranslation {

    private val frenchCache = ConcurrentHashMap<String, String>()
    private val arabicCache = ConcurrentHashMap<String, String>()

    private var englishFrenchTranslator: Translator? = null
    private var englishArabicTranslator: Translator? = null

    private fun frenchTranslator(): Translator {
        return englishFrenchTranslator ?: synchronized(this) {
            englishFrenchTranslator ?: Translation.getClient(
                TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.ENGLISH)
                    .setTargetLanguage(TranslateLanguage.FRENCH)
                    .build()
            ).also {
                englishFrenchTranslator = it
            }
        }
    }

    private fun arabicTranslator(): Translator {
        return englishArabicTranslator ?: synchronized(this) {
            englishArabicTranslator ?: Translation.getClient(
                TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.ENGLISH)
                    .setTargetLanguage(TranslateLanguage.ARABIC)
                    .build()
            ).also {
                englishArabicTranslator = it
            }
        }
    }

    suspend fun translateToFrench(
        context: Context,
        word: String
    ): String? {
        val clean = word.trim()
        if (clean.isBlank()) return null

        frenchCache[clean.lowercase(Locale.ENGLISH)]?.let { return it }

        val translator = frenchTranslator()

        return try {
            val conditions = DownloadConditions.Builder().build()

            awaitRealTask(
                translator.downloadModelIfNeeded(conditions)
            )

            val result = awaitRealTask(
                translator.translate(clean)
            )

            frenchCache[clean.lowercase(Locale.ENGLISH)] = result
            result
        } catch (_: Exception) {
            null
        }
    }

    suspend fun translateToArabic(
        context: Context,
        word: String
    ): String? {
        val clean = word.trim()
        if (clean.isBlank()) return null

        arabicCache[clean.lowercase(Locale.ENGLISH)]?.let { return it }

        val translator = arabicTranslator()

        return try {
            val conditions = DownloadConditions.Builder().build()

            awaitRealTask(
                translator.downloadModelIfNeeded(conditions)
            )

            val result = awaitRealTask(
                translator.translate(clean)
            )

            arabicCache[clean.lowercase(Locale.ENGLISH)] = result
            result
        } catch (_: Exception) {
            null
        }
    }

    fun clearCache() {
        frenchCache.clear()
        arabicCache.clear()
    }

    fun close() {
        englishFrenchTranslator?.close()
        englishArabicTranslator?.close()
        englishFrenchTranslator = null
        englishArabicTranslator = null
        clearCache()
    }
}

/* ------------------------------------------------------------
 * 2. ATTENDRE UNE TASK ML KIT
 * ------------------------------------------------------------ */

private suspend fun <T> awaitRealTask(
    task: com.google.android.gms.tasks.Task<T>
): T = suspendCancellableCoroutine { continuation ->

    task.addOnSuccessListener { result ->
        if (continuation.isActive) {
            continuation.resume(result)
        }
    }

    task.addOnFailureListener { error ->
        if (continuation.isActive) {
            continuation.resumeWith(Result.failure(error))
        }
    }
}

/* ------------------------------------------------------------
 * 3. PRONONCIATION ANGLAISE UNIQUEMENT
 * ------------------------------------------------------------ */

private class EnglishOnlySpeaker(
    private val context: Context
) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var initialized = false

    init {
        tts = TextToSpeech(
            context.applicationContext,
            this
        )
    }

    override fun onInit(status: Int) {
        if (status != TextToSpeech.SUCCESS) {
            initialized = false
            return
        }

        initialized = true
        configureEnglishVoice()
    }

    private fun configureEnglishVoice() {

        val engine = tts ?: return

        val requestedLocale =
            getEnglishAccentLocale(context)

        val availableEnglishVoice = engine.voices
            ?.filter { voice ->
                voice.locale.language.equals(
                    Locale.ENGLISH.language,
                    ignoreCase = true
                )
            }
            ?.sortedWith(
                compareByDescending<android.speech.tts.Voice> { voice ->
                    voice.locale.country.equals(
                        requestedLocale.country,
                        ignoreCase = true
                    )
                }.thenBy { voice ->
                    voice.isNetworkConnectionRequired
                }
            )
            ?.firstOrNull()

        if (availableEnglishVoice != null) {
            engine.voice = availableEnglishVoice
        } else {
            engine.language = requestedLocale
        }

        engine.setPitch(1.0f)
        engine.setSpeechRate(
            getReadingSpeed(context)
        )
    }

    fun speak(word: String) {

        if (!initialized) return
        if (!isSoundEnabled(context)) return

        val engine = tts ?: return

        // On force l'anglais avant chaque lecture.
        configureEnglishVoice()

        engine.speak(
            word,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "english_word_${System.currentTimeMillis()}"
        )
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        initialized = false
    }
}

/* ------------------------------------------------------------
 * 4. TEXTE ANGLAIS AVEC MOTS CLIQUABLES
 * ------------------------------------------------------------ */

@Composable
fun EnglishWordText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    fontSize: TextUnit = 16.sp,
    lineHeight: TextUnit = 27.sp,
    style: TextStyle = MaterialTheme.typography.bodyLarge
) {

    val context = LocalContext.current
    val view = LocalView.current

    var selectedWord by remember {
        mutableStateOf<String?>(null)
    }

    val annotatedText = remember(text) {

        buildAnnotatedString {

            val matches = Regex(
                """\s+|[^\s]+"""
            ).findAll(text)

            matches.forEach { match ->

                val token = match.value

                val cleanWord = cleanEnglishWord(token)

                if (
                    cleanWord.isNotBlank() &&
                    cleanWord.any { it.isLetter() }
                ) {

                    withLink(
                        LinkAnnotation.Clickable(
                            tag = cleanWord,
                            linkInteractionListener = {

                                selectedWord = cleanWord

                                if (isSoundEnabled(context)) {
                                    view.playSoundEffect(
                                        SoundEffectConstants.CLICK
                                    )
                                }
                            }
                        )
                    ) {
                        append(token)
                    }

                } else {
                    append(token)
                }
            }
        }
    }

    Text(
        text = annotatedText,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        lineHeight = lineHeight,
        style = style
    )

    selectedWord?.let { word ->

        EnglishWordDialog(
            word = word,
            onDismiss = {
                selectedWord = null
            }
        )
    }
}

/* ------------------------------------------------------------
 * 5. FENETRE DU MOT
 *
 * VOIX = ANGLAIS SEULEMENT
 * TRADUCTIONS = TEXTE SEULEMENT
 * ------------------------------------------------------------ */

@Composable
private fun EnglishWordDialog(
    word: String,
    onDismiss: () -> Unit
) {

    val context = LocalContext.current

    var frenchTranslation by remember(word) {
        mutableStateOf<String?>(null)
    }

    var arabicTranslation by remember(word) {
        mutableStateOf<String?>(null)
    }

    var loading by remember(word) {
        mutableStateOf(true)
    }

    val speaker = remember(context) {
        EnglishOnlySpeaker(context)
    }

    DisposableEffect(speaker) {
        onDispose {
            speaker.shutdown()
        }
    }

    LaunchedEffect(word) {

        loading = true

        frenchTranslation =
            EnglishWordTranslation.translateToFrench(
                context = context,
                word = word
            )

        arabicTranslation =
            EnglishWordTranslation.translateToArabic(
                context = context,
                word = word
            )

        loading = false

        // Prononciation automatique DU MOT ANGLAIS SEULEMENT.
        speaker.speak(word)
    }

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Mot anglais",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = word,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }

                IconButton(
                    onClick = onDismiss
                ) {

                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fermer"
                    )
                }
            }
        },

        text = {

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                // --------------------------------------------
                // PRONONCIATION ANGLAISE
                // --------------------------------------------

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor =
                            MaterialTheme.colorScheme.primaryContainer
                    )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp,
                                vertical = 12.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = "Prononciation anglaise",
                                style = MaterialTheme.typography.labelLarge
                            )

                            Text(
                                text = "Écouter le mot en anglais",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        IconButton(
                            onClick = {
                                speaker.speak(word)
                            }
                        ) {

                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription =
                                    "Prononcer le mot anglais",
                                tint =
                                    MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                // --------------------------------------------
                // FRANÇAIS : TEXTE UNIQUEMENT
                // --------------------------------------------

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor =
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(15.dp)
                    ) {

                        Text(
                            text = "🇫🇷 Français",
                            style = MaterialTheme.typography.titleSmall
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text =
                                frenchTranslation
                                    ?: if (loading)
                                        "Traduction en cours..."
                                    else
                                        "Traduction indisponible",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                // --------------------------------------------
                // ARABE : TEXTE UNIQUEMENT
                // AUCUN BOUTON DE VOIX
                // --------------------------------------------

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor =
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(15.dp)
                    ) {

                        Text(
                            text = "🇲🇦 العربية",
                            style = MaterialTheme.typography.titleSmall
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text =
                                arabicTranslation
                                    ?: if (loading)
                                        "جارٍ تحميل الترجمة..."
                                    else
                                        "الترجمة غير متوفرة",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        },

        confirmButton = {

            Button(
                onClick = onDismiss
            ) {
                Text("Fermer")
            }
        }
    )
}

/* ------------------------------------------------------------
 * 6. BOUTON DE PRONONCIATION ANGLAISE
 *    POUR LES PAGES QUI ONT DEJA LEUR PROPRE UI
 * ------------------------------------------------------------ */

@Composable
fun EnglishPronunciationButton(
    word: String,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    val speaker = remember(context) {
        EnglishOnlySpeaker(context)
    }

    DisposableEffect(speaker) {

        onDispose {
            speaker.shutdown()
        }
    }

    IconButton(
        onClick = {
            speaker.speak(word)
        },
        modifier = modifier
    ) {

        Icon(
            imageVector = Icons.Default.VolumeUp,
            contentDescription =
                "Prononcer $word en anglais"
        )
    }
}

/* ------------------------------------------------------------
 * 7. UTILITAIRES
 * ------------------------------------------------------------ */

private fun cleanEnglishWord(token: String): String {

    return token
        .trim()
        .trim(
            '(',
            ')',
            '[',
            ']',
            '{',
            '}',
            '<',
            '>',
            '"',
            '\'',
            ',',
            '.',
            '!',
            '?',
            ':',
            ';',
            '…'
        )
}

/**
 * Lecture des paramètres son/accent/vitesse directement dans
 * SharedPreferences pour éviter une dépendance supplémentaire.
 *
 * Les mêmes clés correspondent à SettingsScreen :
 * english_studies_settings / sound / accent / reading_speed
 */

private fun appPrefs(context: Context) =
    context.getSharedPreferences(
        "english_studies_settings",
        Context.MODE_PRIVATE
    )

private fun isSoundEnabled(context: Context): Boolean =
    appPrefs(context)
        .getBoolean("sound", true)

private fun getReadingSpeed(context: Context): Float {

    return when (
        appPrefs(context)
            .getString("reading_speed", "Normale")
    ) {

        "Lente" -> 0.65f
        "Rapide" -> 1.05f
        else -> 0.82f
    }
}

private fun getEnglishAccentLocale(
    context: Context
): Locale {

    return when (
        appPrefs(context)
            .getString("accent", "American")
    ) {

        "British" -> Locale.UK
        else -> Locale.US
    }
}
