package ma.fldm.englishstudies

import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Section vocabulaire basée sur OliverVocabularyBank.
 *
 * Le moteur vocal est explicitement configuré en anglais (États-Unis).
 * On attend la fin de l'initialisation TextToSpeech avant d'appliquer
 * la langue anglaise. Cela évite que le téléphone utilise sa langue
 * par défaut (français/arabe).
 */
@Composable
fun OliverVocabularyBankSection(
    sourceText: String,
    maxWords: Int = 30,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val mainHandler = remember { Handler(Looper.getMainLooper()) }

    val words = remember(sourceText, maxWords) {
        OliverVocabularyBank.extractForText(
            text = sourceText,
            maxWords = maxWords
        )
    }

    var englishTtsReady by remember {
        mutableStateOf(false)
    }

    var englishTtsError by remember {
        mutableStateOf("")
    }

    val textToSpeech = remember(context) {
        TextToSpeech(
            context.applicationContext
        ) { status ->
            mainHandler.post {
                if (status == TextToSpeech.SUCCESS) {
                    val languageResult =
                        runCatching {
                            val engine = textToSpeechHolder.value
                                ?: return@runCatching TextToSpeech.ERROR

                            val configured =
                                EnglishStudiesSpeech.configure(
                                    tts = engine,
                                    context = context.applicationContext
                                )

                            if (configured) {
                                TextToSpeech.LANG_AVAILABLE
                            } else {
                                TextToSpeech.ERROR
                            }
                        }.getOrElse {
                            TextToSpeech.ERROR
                        }

                    when (languageResult) {
                        TextToSpeech.LANG_AVAILABLE,
                        TextToSpeech.LANG_COUNTRY_AVAILABLE,
                        TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE -> {
                            englishTtsReady = true
                            englishTtsError = ""
                        }

                        TextToSpeech.LANG_MISSING_DATA -> {
                            englishTtsReady = false
                            englishTtsError =
                                "Voix anglaise absente. Installez les données vocales anglaises dans les paramètres Text-to-Speech du téléphone."
                        }

                        TextToSpeech.LANG_NOT_SUPPORTED -> {
                            englishTtsReady = false
                            englishTtsError =
                                "La voix anglaise n'est pas prise en charge par le moteur vocal actuel."
                        }

                        else -> {
                            englishTtsReady = false
                            englishTtsError =
                                "Impossible d'activer la voix anglaise."
                        }
                    }
                } else {
                    englishTtsReady = false
                    englishTtsError =
                        "Le moteur Text-to-Speech n'a pas pu démarrer."
                }
            }
        }.also {
            textToSpeechHolder.value = it
        }
    }

    LaunchedEffect(textToSpeech, englishTtsReady) {
        if (englishTtsReady) {
            runCatching {
                EnglishStudiesSpeech.configure(
                    tts = textToSpeech,
                    context = context.applicationContext
                )
            }
        }
    }

    DisposableEffect(textToSpeech) {
        onDispose {
            runCatching {
                textToSpeech.stop()
                textToSpeech.shutdown()
            }
            textToSpeechHolder.value = null
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        Text(
            text = "📚 Vocabulaire de la scène",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "${words.size} mot(s) trouvé(s) dans le texte",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (englishTtsError.isNotBlank()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = "🔊 Voix anglaise",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = englishTtsError,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }

        if (words.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = "Aucun mot de la banque n'a été détecté dans ce texte.",
                    modifier = Modifier.padding(14.dp),
                    fontSize = 14.sp
                )
            }
        } else {
            words.forEach { item ->
                OliverVocabularyBankCard(
                    item = item,
                    tts = textToSpeech,
                    ttsReady = englishTtsReady
                )
            }
        }
    }
}

@Composable
private fun OliverVocabularyBankCard(
    item: OliverVocabularyItem,
    tts: TextToSpeech,
    ttsReady: Boolean
) {
    val context = LocalContext.current

    val drawableId = remember(item.illustrationName) {
        item.illustrationName
            ?.let { name ->
                context.resources.getIdentifier(
                    name,
                    "drawable",
                    context.packageName
                )
            }
            ?: 0
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Card(
                modifier = Modifier.size(78.dp),
                shape = RoundedCornerShape(11.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (drawableId != 0) {
                        Image(
                            painter = painterResource(id = drawableId),
                            contentDescription = item.word,
                            modifier = Modifier
                                .size(78.dp)
                                .clip(RoundedCornerShape(11.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(
                            text = "🖼️",
                            fontSize = 32.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.word,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = if (ttsReady) "🔊" else "⏳",
                        fontSize = 20.sp,
                        modifier = Modifier.clickable(enabled = ttsReady) {
                            speakEnglish(
                                context = context,
                                tts = tts,
                                text = item.getWordAudioText(),
                                utteranceId = "vocab_${item.word}"
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = item.pronunciation,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = item.definitionEn,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "🇫🇷 ${item.translationFr}",
                    fontSize = 13.sp
                )

                Text(
                    text = "🇲🇦 ${item.translationAr}",
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(7.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(9.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(9.dp)
                    ) {
                        Text(
                            text = "📖 ${item.exampleEn}",
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(3.dp))

                        Text(
                            text = "🇫🇷 ${item.exampleFr}",
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )

                        Text(
                            text = "🇲🇦 ${item.exampleAr}",
                            fontSize = 13.sp,
                            lineHeight = 19.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (ttsReady) {
                                "🔊 Écouter la phrase en anglais"
                            } else {
                                "⏳ Voix anglaise en préparation"
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable(enabled = ttsReady) {
                                speakEnglish(
                                    context = context,
                                    tts = tts,
                                    text = item.getExampleAudioText(),
                                    utteranceId = "example_${item.word}"
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

/*
 * Référence temporaire utilisée uniquement pendant l'initialisation
 * de TextToSpeech.
 */
private object textToSpeechHolder {
    var value: TextToSpeech? = null
}

private fun speakEnglish(
    context: android.content.Context,
    tts: TextToSpeech,
    text: String,
    utteranceId: String
) {
    EnglishStudiesSpeech.speak(
        tts = tts,
        context = context,
        text = text,
        utteranceId = utteranceId
    )
}
