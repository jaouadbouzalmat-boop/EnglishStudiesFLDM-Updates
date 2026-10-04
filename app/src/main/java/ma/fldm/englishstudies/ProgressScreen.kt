package ma.fldm.englishstudies

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ma.fldm.englishstudies.data.CourseData
import ma.fldm.englishstudies.data.ProgressDataStore

@Composable
fun ProgressScreen(
    onBack: () -> Unit
) {

    // =========================================================
    // DATASTORE
    // =========================================================

    val context = LocalContext.current

    val progressDataStore = remember(context) {
        ProgressDataStore(context)
    }

    // Leçons terminées
    val completedLessons by progressDataStore.completedLessons
        .collectAsState(initial = emptySet())

    // Meilleurs scores
    val bestScores by progressDataStore.bestScores
        .collectAsState(initial = emptyMap())

    // =========================================================
    // DONNÉES DU COURS
    // =========================================================

    val lessons = CourseData.grammar1Units
        .flatMap { it.lessons }

    val totalLessons = lessons.size

    // Nombre réel de leçons terminées parmi les leçons de Grammar 1
    val completedCount = lessons.count { lesson ->
        completedLessons.contains(lesson.id)
    }

    // Progression générale
    val progress = if (totalLessons > 0) {
        completedCount.toFloat() / totalLessons.toFloat()
    } else {
        0f
    }

    val progressPercentage = (progress * 100).toInt()

    // =========================================================
    // INTERFACE
    // =========================================================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        // =====================================================
        // RETOUR
        // =====================================================

        Text(
            text = "← Back",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clickable {
                    onBack()
                }
                .padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(25.dp))

        // =====================================================
        // TITRE
        // =====================================================

        Text(
            text = "My Progress",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "English Studies – FLDM Fès",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Grammar 1",
            fontSize = 21.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(25.dp))

        // =====================================================
        // CARTE DE PROGRESSION GÉNÉRALE
        // =====================================================

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Course Progress",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "$completedCount / $totalLessons lessons completed",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(15.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "$progressPercentage% completed",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(25.dp))

        // =====================================================
        // TITRE DE LA LISTE
        // =====================================================

        Text(
            text = "Grammar 1 — Course Progress",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        // =====================================================
        // LISTE DES LEÇONS
        // =====================================================

        lessons.forEachIndexed { index, lesson ->

            val completed = completedLessons.contains(lesson.id)

            val bestScore = bestScores[lesson.id]

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor =
                        if (completed) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    // Numéro + état
                    Text(
                        text = if (completed) {
                            "✅ Lesson ${index + 1}"
                        } else {
                            "○ Lesson ${index + 1}"
                        },
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Titre
                    Text(
                        text = lesson.title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // État de la leçon
                    Text(
                        text = if (completed) {
                            "Status: Completed"
                        } else {
                            "Status: Not completed"
                        },
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // =================================================
                    // MEILLEUR SCORE
                    // =================================================

                    if (lesson.questions.isNotEmpty()) {

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (bestScore != null) {
                                "Best Quiz Score: $bestScore / ${lesson.questions.size}"
                            } else {
                                "Best Quiz Score: Not attempted"
                            },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // =====================================================
        // INFORMATION ACADÉMIQUE
        // =====================================================

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {

            Column(
                modifier = Modifier.padding(18.dp)
            ) {

                Text(
                    text = "Academic Progress",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Your completed lessons and quiz results are saved automatically on this device.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}