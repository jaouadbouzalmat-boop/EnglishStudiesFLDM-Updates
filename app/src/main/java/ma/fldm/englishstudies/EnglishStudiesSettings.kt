package ma.fldm.englishstudies

import android.content.Context
import java.util.Locale

/**
 * Central access to the preferences used by SettingsScreen.
 *
 * This object intentionally does not depend on AppSettingsPrefs, so there
 * is no cross-file visibility problem. It reads the same preference store
 * and the same keys used by SettingsScreen_FINAL_PUBLIC_PREFS.kt.
 */
object EnglishStudiesSettings {

    private const val PREFS_NAME = "english_studies_settings"

    private const val KEY_AUTO_READING = "auto_reading"
    private const val KEY_READING_SPEED = "reading_speed"
    private const val KEY_SOUND = "sound"
    private const val KEY_VOICE_RECOGNITION = "voice_recognition"
    private const val KEY_ACCENT = "accent"
    private const val KEY_PRONUNCIATION_SCORE = "show_pronunciation_score"

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isSoundEnabled(context: Context): Boolean =
        prefs(context).getBoolean(KEY_SOUND, true)

    fun isAutoReadingEnabled(context: Context): Boolean =
        prefs(context).getBoolean(KEY_AUTO_READING, true)

    fun isVoiceRecognitionEnabled(context: Context): Boolean =
        prefs(context).getBoolean(KEY_VOICE_RECOGNITION, false)

    fun showPronunciationScore(context: Context): Boolean =
        prefs(context).getBoolean(KEY_PRONUNCIATION_SCORE, true)

    fun speechRate(context: Context): Float =
        when (prefs(context).getString(KEY_READING_SPEED, "Normale")) {
            "Lente" -> 0.65f
            "Rapide" -> 1.05f
            else -> 0.82f
        }

    fun englishLocale(context: Context): Locale =
        when (prefs(context).getString(KEY_ACCENT, "American")) {
            "British" -> Locale.UK
            else -> Locale.US
        }
}


