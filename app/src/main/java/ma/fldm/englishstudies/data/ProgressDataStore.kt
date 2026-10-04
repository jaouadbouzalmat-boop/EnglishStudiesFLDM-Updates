package ma.fldm.englishstudies.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.progressDataStore by preferencesDataStore(
    name = "student_progress"
)

class ProgressDataStore(private val context: Context) {

    private val completedLessonsKey =
        stringPreferencesKey("completed_lessons")

    private val bestScoresKey =
        stringPreferencesKey("best_scores")

    val completedLessons: Flow<Set<String>> =
        context.progressDataStore.data.map { preferences ->
            preferences[completedLessonsKey]
                ?.split("|")
                ?.filter { it.isNotBlank() }
                ?.toSet()
                ?: emptySet()
        }

    val bestScores: Flow<Map<String, Int>> =
        context.progressDataStore.data.map { preferences ->
            preferences[bestScoresKey]
                ?.split("|")
                ?.filter { it.contains(":") }
                ?.associate {
                    val parts = it.split(":", limit = 2)
                    parts[0] to parts[1].toIntOrNull().orZero()
                }
                ?: emptyMap()
        }

    suspend fun markLessonCompleted(lessonId: String) {
        context.progressDataStore.edit { preferences ->
            val current =
                preferences[completedLessonsKey]
                    ?.split("|")
                    ?.filter { it.isNotBlank() }
                    ?.toMutableSet()
                    ?: mutableSetOf()

            current.add(lessonId)

            preferences[completedLessonsKey] =
                current.joinToString("|")
        }
    }

    suspend fun saveBestScore(quizId: String, score: Int) {
        context.progressDataStore.edit { preferences ->

            val current =
                preferences[bestScoresKey]
                    ?.split("|")
                    ?.filter { it.contains(":") }
                    ?.associate {
                        val parts = it.split(":", limit = 2)
                        parts[0] to parts[1].toIntOrNull().orZero()
                    }
                    ?.toMutableMap()
                    ?: mutableMapOf()

            val oldScore = current[quizId] ?: 0

            if (score > oldScore) {
                current[quizId] = score

                preferences[bestScoresKey] =
                    current.entries.joinToString("|") {
                        "${it.key}:${it.value}"
                    }
            }
        }
    }
}

private fun Int?.orZero(): Int = this ?: 0