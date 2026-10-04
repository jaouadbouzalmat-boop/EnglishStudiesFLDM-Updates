package ma.fldm.englishstudies

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/** Global interface language. Values are stored in the same preferences as SettingsScreen. */
object AppLanguage {
    private const val PREFS_NAME = "english_studies_settings"
    private const val KEY_LANGUAGE = "language"

    var current by mutableStateOf("Français")
        private set

    fun initialize(context: Context) {
        current = context
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_LANGUAGE, "Français")
            ?: "Français"
    }

    fun set(context: Context, language: String) {
        current = language
        context
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LANGUAGE, language)
            .apply()
    }

    fun tr(french: String, english: String, arabic: String): String = when (current) {
        "English" -> english
        "العربية" -> arabic
        else -> french
    }

    fun value(french: String, english: String, arabic: String): String =
        tr(french, english, arabic)
}


