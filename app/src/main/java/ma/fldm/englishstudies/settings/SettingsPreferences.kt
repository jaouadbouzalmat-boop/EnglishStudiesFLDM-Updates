package ma.fldm.englishstudies.settings

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.settingsDataStore by preferencesDataStore(
    name = "english_studies_settings"
)

data class SettingsState(

    // =====================================================
    // APPARENCE
    // =====================================================

    val deviceMode: String = "PHONE",
    val theme: String = "SYSTEM",
    val textScale: Float = 1.0f,
    val font: String = "Sans",
    val accentColor: String = "BLUE",

    // =====================================================
    // LECTURE
    // =====================================================

    val autoPlay: Boolean = true,
    val highlightText: Boolean = true,
    val autoScroll: Boolean = false,
    val resumePosition: Boolean = true,
    val readingSpeed: Float = 1.0f,
    val translationMode: String = "ON_DEMAND",
    val showIllustrations: Boolean = true,

    // =====================================================
    // PRONONCIATION
    // =====================================================

    val speechRecognition: Boolean = true,
    val accent: String = "BRITISH",
    val pronunciationEvaluation: Boolean = true,
    val repeatDifficultWords: Boolean = true,
    val recognitionTolerance: String = "NORMAL",

    // =====================================================
    // AUDIO
    // =====================================================

    val soundEnabled: Boolean = true,
    val volume: Float = 0.8f,
    val buttonSounds: Boolean = true,
    val backgroundAudio: Boolean = false,

    // =====================================================
    // LANGUE
    // =====================================================

    val appLanguage: String = "FR",
    val explanationLanguage: String = "FR",
    val translationLanguage: String = "FR",

    // =====================================================
    // PROGRESSION
    // =====================================================

    val showStatistics: Boolean = true,
    val showScores: Boolean = true,
    val dailyGoalMinutes: Int = 30,

    // =====================================================
    // NOTIFICATIONS
    // =====================================================

    val notificationsEnabled: Boolean = true,
    val dailyReminder: Boolean = true,
    val readingReminder: Boolean = true,
    val revisionReminder: Boolean = true,
    val newLessonsNotification: Boolean = true,
    val updateNotification: Boolean = true,

    // =====================================================
    // HORS CONNEXION
    // =====================================================

    val downloadCourses: Boolean = true,
    val downloadAudios: Boolean = true,
    val downloadIllustrations: Boolean = true,
    val mobileDataAllowed: Boolean = false,
    val wifiOnly: Boolean = true,
    val highAudioQuality: Boolean = true
)

object SettingsKeys {

    // Apparence
    val DEVICE_MODE = stringPreferencesKey("device_mode")
    val THEME = stringPreferencesKey("theme")
    val TEXT_SCALE = floatPreferencesKey("text_scale")
    val FONT = stringPreferencesKey("font")
    val ACCENT_COLOR = stringPreferencesKey("accent_color")

    // Lecture
    val AUTO_PLAY = booleanPreferencesKey("auto_play")
    val HIGHLIGHT_TEXT = booleanPreferencesKey("highlight_text")
    val AUTO_SCROLL = booleanPreferencesKey("auto_scroll")
    val RESUME_POSITION = booleanPreferencesKey("resume_position")
    val READING_SPEED = floatPreferencesKey("reading_speed")
    val TRANSLATION_MODE = stringPreferencesKey("translation_mode")
    val SHOW_ILLUSTRATIONS = booleanPreferencesKey("show_illustrations")

    // Prononciation
    val SPEECH_RECOGNITION = booleanPreferencesKey("speech_recognition")
    val ACCENT = stringPreferencesKey("accent")
    val PRONUNCIATION_EVALUATION =
        booleanPreferencesKey("pronunciation_evaluation")
    val REPEAT_DIFFICULT_WORDS =
        booleanPreferencesKey("repeat_difficult_words")
    val RECOGNITION_TOLERANCE =
        stringPreferencesKey("recognition_tolerance")

    // Audio
    val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
    val VOLUME = floatPreferencesKey("volume")
    val BUTTON_SOUNDS = booleanPreferencesKey("button_sounds")
    val BACKGROUND_AUDIO = booleanPreferencesKey("background_audio")

    // Langue
    val APP_LANGUAGE = stringPreferencesKey("app_language")
    val EXPLANATION_LANGUAGE =
        stringPreferencesKey("explanation_language")
    val TRANSLATION_LANGUAGE =
        stringPreferencesKey("translation_language")

    // Progression
    val SHOW_STATISTICS = booleanPreferencesKey("show_statistics")
    val SHOW_SCORES = booleanPreferencesKey("show_scores")
    val DAILY_GOAL = intPreferencesKey("daily_goal")

    // Notifications
    val NOTIFICATIONS_ENABLED =
        booleanPreferencesKey("notifications_enabled")
    val DAILY_REMINDER =
        booleanPreferencesKey("daily_reminder")
    val READING_REMINDER =
        booleanPreferencesKey("reading_reminder")
    val REVISION_REMINDER =
        booleanPreferencesKey("revision_reminder")
    val NEW_LESSONS_NOTIFICATION =
        booleanPreferencesKey("new_lessons_notification")
    val UPDATE_NOTIFICATION =
        booleanPreferencesKey("update_notification")

    // Hors connexion
    val DOWNLOAD_COURSES =
        booleanPreferencesKey("download_courses")
    val DOWNLOAD_AUDIOS =
        booleanPreferencesKey("download_audios")
    val DOWNLOAD_ILLUSTRATIONS =
        booleanPreferencesKey("download_illustrations")
    val MOBILE_DATA_ALLOWED =
        booleanPreferencesKey("mobile_data_allowed")
    val WIFI_ONLY =
        booleanPreferencesKey("wifi_only")
    val HIGH_AUDIO_QUALITY =
        booleanPreferencesKey("high_audio_quality")
}

class SettingsRepository(
    private val context: Context
) {

    val settings: Flow<SettingsState> =
        context.settingsDataStore.data
            .catch { exception ->

                if (exception is IOException) {
                    emit(
                        androidx.datastore.preferences.core
                            .emptyPreferences()
                    )
                } else {
                    throw exception
                }
            }
            .map { preferences ->

                SettingsState(
                    deviceMode =
                        preferences[SettingsKeys.DEVICE_MODE]
                            ?: "PHONE",

                    theme =
                        preferences[SettingsKeys.THEME]
                            ?: "SYSTEM",

                    textScale =
                        preferences[SettingsKeys.TEXT_SCALE]
                            ?: 1.0f,

                    font =
                        preferences[SettingsKeys.FONT]
                            ?: "Sans",

                    accentColor =
                        preferences[SettingsKeys.ACCENT_COLOR]
                            ?: "BLUE",

                    autoPlay =
                        preferences[SettingsKeys.AUTO_PLAY]
                            ?: true,

                    highlightText =
                        preferences[SettingsKeys.HIGHLIGHT_TEXT]
                            ?: true,

                    autoScroll =
                        preferences[SettingsKeys.AUTO_SCROLL]
                            ?: false,

                    resumePosition =
                        preferences[SettingsKeys.RESUME_POSITION]
                            ?: true,

                    readingSpeed =
                        preferences[SettingsKeys.READING_SPEED]
                            ?: 1.0f,

                    translationMode =
                        preferences[SettingsKeys.TRANSLATION_MODE]
                            ?: "ON_DEMAND",

                    showIllustrations =
                        preferences[SettingsKeys.SHOW_ILLUSTRATIONS]
                            ?: true,

                    speechRecognition =
                        preferences[SettingsKeys.SPEECH_RECOGNITION]
                            ?: true,

                    accent =
                        preferences[SettingsKeys.ACCENT]
                            ?: "BRITISH",

                    pronunciationEvaluation =
                        preferences[
                            SettingsKeys.PRONUNCIATION_EVALUATION
                        ] ?: true,

                    repeatDifficultWords =
                        preferences[
                            SettingsKeys.REPEAT_DIFFICULT_WORDS
                        ] ?: true,

                    recognitionTolerance =
                        preferences[
                            SettingsKeys.RECOGNITION_TOLERANCE
                        ] ?: "NORMAL",

                    soundEnabled =
                        preferences[SettingsKeys.SOUND_ENABLED]
                            ?: true,

                    volume =
                        preferences[SettingsKeys.VOLUME]
                            ?: 0.8f,

                    buttonSounds =
                        preferences[SettingsKeys.BUTTON_SOUNDS]
                            ?: true,

                    backgroundAudio =
                        preferences[SettingsKeys.BACKGROUND_AUDIO]
                            ?: false,

                    appLanguage =
                        preferences[SettingsKeys.APP_LANGUAGE]
                            ?: "FR",

                    explanationLanguage =
                        preferences[
                            SettingsKeys.EXPLANATION_LANGUAGE
                        ] ?: "FR",

                    translationLanguage =
                        preferences[
                            SettingsKeys.TRANSLATION_LANGUAGE
                        ] ?: "FR",

                    showStatistics =
                        preferences[SettingsKeys.SHOW_STATISTICS]
                            ?: true,

                    showScores =
                        preferences[SettingsKeys.SHOW_SCORES]
                            ?: true,

                    dailyGoalMinutes =
                        preferences[SettingsKeys.DAILY_GOAL]
                            ?: 30,

                    notificationsEnabled =
                        preferences[
                            SettingsKeys.NOTIFICATIONS_ENABLED
                        ] ?: true,

                    dailyReminder =
                        preferences[SettingsKeys.DAILY_REMINDER]
                            ?: true,

                    readingReminder =
                        preferences[SettingsKeys.READING_REMINDER]
                            ?: true,

                    revisionReminder =
                        preferences[SettingsKeys.REVISION_REMINDER]
                            ?: true,

                    newLessonsNotification =
                        preferences[
                            SettingsKeys.NEW_LESSONS_NOTIFICATION
                        ] ?: true,

                    updateNotification =
                        preferences[
                            SettingsKeys.UPDATE_NOTIFICATION
                        ] ?: true,

                    downloadCourses =
                        preferences[SettingsKeys.DOWNLOAD_COURSES]
                            ?: true,

                    downloadAudios =
                        preferences[SettingsKeys.DOWNLOAD_AUDIOS]
                            ?: true,

                    downloadIllustrations =
                        preferences[
                            SettingsKeys.DOWNLOAD_ILLUSTRATIONS
                        ] ?: true,

                    mobileDataAllowed =
                        preferences[
                            SettingsKeys.MOBILE_DATA_ALLOWED
                        ] ?: false,

                    wifiOnly =
                        preferences[SettingsKeys.WIFI_ONLY]
                            ?: true,

                    highAudioQuality =
                        preferences[
                            SettingsKeys.HIGH_AUDIO_QUALITY
                        ] ?: true
                )
            }

    suspend fun setBoolean(
        key: Preferences.Key<Boolean>,
        value: Boolean
    ) {
        context.settingsDataStore.edit {
            it[key] = value
        }
    }

    suspend fun setString(
        key: Preferences.Key<String>,
        value: String
    ) {
        context.settingsDataStore.edit {
            it[key] = value
        }
    }

    suspend fun setFloat(
        key: Preferences.Key<Float>,
        value: Float
    ) {
        context.settingsDataStore.edit {
            it[key] = value
        }
    }

    suspend fun setInt(
        key: Preferences.Key<Int>,
        value: Int
    ) {
        context.settingsDataStore.edit {
            it[key] = value
        }
    }
}

