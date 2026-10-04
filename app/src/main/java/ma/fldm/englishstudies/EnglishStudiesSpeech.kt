package ma.fldm.englishstudies

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

/**
 * Configuration vocale anglaise centralisée pour toute l'application.
 *
 * L'accent sélectionné dans Paramètres > Accent anglais est relu
 * à chaque lecture afin qu'un changement de British/American soit
 * appliqué même lorsqu'un écran est déjà ouvert.
 *
 * L'accent exact dépend de la voix English (UK/US) installée
 * dans le moteur Text-to-Speech du téléphone.
 */
object EnglishStudiesSpeech {

    fun locale(context: Context): Locale =
        EnglishStudiesSettings.englishLocale(context)

    fun configure(
        tts: TextToSpeech,
        context: Context
    ): Boolean {
        val preferredLocale = locale(context)

        val result = runCatching {
            tts.setLanguage(preferredLocale)
        }.getOrElse {
            TextToSpeech.ERROR
        }

        val englishVoice = tts.voices
            .orEmpty()
            .filter { it.locale.language.equals("en", ignoreCase = true) }
            .filter { !it.isNetworkConnectionRequired }
            .sortedByDescending { voice ->
                var score = 0

                if (voice.locale.language.equals("en", true)) {
                    score += 100
                }

                if (voice.locale.country.equals(preferredLocale.country, true)) {
                    score += 1000
                }

                if (!voice.locale.variant.isNullOrBlank()) {
                    score += 5
                }

                score
            }
            .firstOrNull()

        if (englishVoice != null) {
            runCatching {
                tts.voice = englishVoice
            }
        }

        tts.setSpeechRate(
            EnglishStudiesSettings.speechRate(context)
        )
        tts.setPitch(1.0f)

        return result != TextToSpeech.LANG_MISSING_DATA &&
                result != TextToSpeech.LANG_NOT_SUPPORTED
    }

    fun speak(
        tts: TextToSpeech?,
        context: Context,
        text: String,
        utteranceId: String
    ) {
        if (tts == null || text.isBlank()) return

        if (!EnglishStudiesSettings.isSoundEnabled(context)) {
            tts.stop()
            return
        }

        runCatching {
            configure(tts, context)

            tts.speak(
                text,
                TextToSpeech.QUEUE_FLUSH,
                null,
                utteranceId
            )
        }
    }

    fun recognitionLanguage(context: Context): String =
        locale(context).toLanguageTag()
}


