package ma.fldm.englishstudies.data

import android.content.Context
import android.content.SharedPreferences

class OliverProgressStore(
    context: Context
) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(
            "oliver_twist_progress",
            Context.MODE_PRIVATE
        )

    // ---------------------------------------------------------
    // SCÈNES TERMINÉES
    // ---------------------------------------------------------
    fun isSceneCompleted(
        chapterIndex: Int,
        sceneIndex: Int
    ): Boolean {

        return prefs.getBoolean(
            sceneKey(chapterIndex, sceneIndex),
            false
        )
    }

    fun markSceneCompleted(
        chapterIndex: Int,
        sceneIndex: Int
    ) {

        prefs.edit()
            .putBoolean(
                sceneKey(chapterIndex, sceneIndex),
                true
            )
            .apply()
    }

    // ---------------------------------------------------------
    // SCORE D'UN EXERCICE
    // ---------------------------------------------------------
    fun saveScore(
        chapterIndex: Int,
        sceneIndex: Int,
        score: Int
    ) {

        val key = scoreKey(
            chapterIndex,
            sceneIndex
        )

        val previousScore =
            prefs.getInt(key, -1)

        // On conserve toujours le meilleur score.
        if (score > previousScore) {

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
            scoreKey(
                chapterIndex,
                sceneIndex
            ),
            -1
        )
    }

    // ---------------------------------------------------------
    // NOMBRE DE SCÈNES TERMINÉES
    // ---------------------------------------------------------
    fun getCompletedScenes(
        totalScenes: Int
    ): Int {

        var completed = 0

        for (chapterIndex in 0 until totalScenes) {

            val key = "completed_scene_$chapterIndex"

            if (prefs.getBoolean(key, false)) {
                completed++
            }
        }

        return completed
    }

    // ---------------------------------------------------------
    // POURCENTAGE GLOBAL
    // ---------------------------------------------------------
    fun getProgressPercent(
        totalScenes: Int
    ): Int {

        if (totalScenes <= 0) {
            return 0
        }

        var completed = 0

        // Cette méthode sera utilisée avec les clés
        // réellement enregistrées par l'écran Oliver Twist.
        prefs.all.keys.forEach { key ->

            if (
                key.startsWith("scene_completed_") &&
                prefs.getBoolean(key, false)
            ) {
                completed++
            }
        }

        return (
                completed * 100 / totalScenes
                ).coerceIn(0, 100)
    }

    // ---------------------------------------------------------
    // RESET
    // ---------------------------------------------------------
    fun resetAll() {

        prefs.edit()
            .clear()
            .apply()
    }

    // ---------------------------------------------------------
    // CLÉS
    // ---------------------------------------------------------
    private fun sceneKey(
        chapterIndex: Int,
        sceneIndex: Int
    ): String {

        return "scene_completed_${chapterIndex}_${sceneIndex}"
    }

    private fun scoreKey(
        chapterIndex: Int,
        sceneIndex: Int
    ): String {

        return "best_score_${chapterIndex}_${sceneIndex}"
    }
}
