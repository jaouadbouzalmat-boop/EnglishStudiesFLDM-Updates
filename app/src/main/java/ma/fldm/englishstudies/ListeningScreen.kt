package ma.fldm.englishstudies

import android.media.MediaPlayer
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import ma.fldm.englishstudies.data.ListeningActivity
import ma.fldm.englishstudies.data.ListeningData


// =================================================================
// ÉCRAN PRINCIPAL LISTENING
// =================================================================

// =================================================================
// CARTE D'UNE ACTIVITÉ
// =================================================================

@Composable
fun ListeningActivityCard(
    activity: ListeningActivity,
    number: Int,
    selected: Boolean,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(16.dp),

        colors = CardDefaults.cardColors(
            containerColor =
                if (selected)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.surfaceVariant
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "🎧",
                fontSize = 28.sp
            )

            Spacer(
                modifier = Modifier.width(14.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Listening $number",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = activity.title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = activity.level,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "›",
                fontSize = 30.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}


// =================================================================
// CONTENU DE L'ACTIVITÉ
// =================================================================

@Composable
fun ListeningActivityContent(
    activity: ListeningActivity
) {

    Text(
        text = activity.title,
        fontSize = 25.sp,
        fontWeight = FontWeight.Bold
    )

    Spacer(
        modifier = Modifier.height(8.dp)
    )

    Text(
        text = activity.level,
        fontSize = 15.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(
        modifier = Modifier.height(20.dp)
    )

    // =============================================================
    // OBJECTIF
    // =============================================================

    ListeningSectionCard(
        title = "🎯 Learning Objective"
    ) {

        Text(
            text = activity.objective,
            fontSize = 16.sp,
            lineHeight = 24.sp
        )
    }

    Spacer(
        modifier = Modifier.height(14.dp)
    )

    // =============================================================
    // BEFORE LISTENING
    // =============================================================

    ListeningSectionCard(
        title = "🧠 Before Listening"
    ) {

        activity.beforeListening.forEachIndexed { index, question ->

            Text(
                text = "${index + 1}. $question",
                fontSize = 15.sp,
                lineHeight = 23.sp
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }
    }

    Spacer(
        modifier = Modifier.height(14.dp)
    )

    // =============================================================
    // AUDIO
    // =============================================================

    ListeningAudioPlayer(
        activity = activity
    )

    Spacer(
        modifier = Modifier.height(14.dp)
    )

    // =============================================================
    // TRANSCRIPT
    // =============================================================

    ListeningSectionCard(
        title = "📄 Transcript"
    ) {

        Text(
            text = activity.transcript,
            fontSize = 15.sp,
            lineHeight = 24.sp
        )
    }

    Spacer(
        modifier = Modifier.height(14.dp)
    )

    // =============================================================
    // VOCABULAIRE
    // =============================================================

    ListeningSectionCard(
        title = "📚 Academic Vocabulary"
    ) {

        activity.vocabulary.forEach { vocabulary ->

            Text(
                text = vocabulary.term,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = vocabulary.definition,
                fontSize = 14.sp,
                lineHeight = 21.sp
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )
        }
    }

    Spacer(
        modifier = Modifier.height(14.dp)
    )

    // =============================================================
    // QUIZ
    // =============================================================

    ListeningQuiz(
        activity = activity
    )
}


// =================================================================
// LECTEUR AUDIO
// =================================================================

@Composable
fun ListeningAudioPlayer(
    activity: ListeningActivity
) {

    val context = LocalContext.current

    var mediaPlayer by remember(
        activity.id,
        activity.audioResId
    ) {
        mutableStateOf<MediaPlayer?>(null)
    }

    var isPlaying by remember(
        activity.id
    ) {
        mutableStateOf(false)
    }

    var progress by remember(
        activity.id
    ) {
        mutableFloatStateOf(0f)
    }

    var audioReady by remember(
        activity.id
    ) {
        mutableStateOf(false)
    }

    var audioError by remember(
        activity.id
    ) {
        mutableStateOf(false)
    }

    // =============================================================
    // INITIALISATION DU LECTEUR
    // =============================================================

    DisposableEffect(
        activity.id,
        activity.audioResId
    ) {

        val resourceId = activity.audioResId

        if (resourceId == null) {

            audioReady = false
            audioError = false
            mediaPlayer = null

            onDispose {
            }

        } else {

            var player: MediaPlayer? = null

            try {

                player = MediaPlayer.create(
                    context,
                    resourceId
                )

                if (player != null) {

                    player.setOnCompletionListener {

                        isPlaying = false
                        progress = 1f
                    }

                    player.setOnErrorListener { _, _, _ ->

                        isPlaying = false
                        audioError = true

                        true
                    }

                    mediaPlayer = player
                    audioReady = true
                    audioError = false

                } else {

                    mediaPlayer = null
                    audioReady = false
                    audioError = true
                }

            } catch (_: Exception) {

                mediaPlayer = null
                audioReady = false
                audioError = true
            }

            onDispose {

                try {
                    player?.stop()
                } catch (_: Exception) {
                }

                try {
                    player?.release()
                } catch (_: Exception) {
                }

                mediaPlayer = null
            }
        }
    }

    // =============================================================
    // SUIVI DE LA PROGRESSION
    // =============================================================

    LaunchedEffect(
        mediaPlayer,
        isPlaying
    ) {

        while (
            isPlaying &&
            mediaPlayer != null
        ) {

            try {

                val player = mediaPlayer

                if (player != null) {

                    val duration = player.duration

                    if (duration > 0) {

                        progress =
                            player.currentPosition.toFloat() /
                                    duration.toFloat()
                    }
                }

            } catch (_: Exception) {
            }

            delay(250)
        }
    }

    // =============================================================
    // INTERFACE
    // =============================================================

    ListeningSectionCard(
        title = "🎧 Audio"
    ) {

        if (audioError) {

            Text(
                text = "❌ Impossible de lire cet audio.",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Vérifiez que university_life.mp3 est présent dans res/raw.",
                fontSize = 14.sp,
                lineHeight = 21.sp
            )

        } else if (!audioReady) {

            Text(
                text = "⏳ Audio non disponible.",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Le fichier audio n'est pas encore chargé.",
                fontSize = 14.sp
            )

        } else {

            Button(
                onClick = {

                    try {

                        val player = mediaPlayer

                        if (player != null) {

                            if (player.isPlaying) {

                                player.pause()
                                isPlaying = false

                            } else {

                                if (
                                    player.currentPosition >=
                                    player.duration
                                ) {

                                    player.seekTo(0)
                                    progress = 0f
                                }

                                player.start()
                                isPlaying = true
                            }
                        }

                    } catch (_: Exception) {

                        audioError = true
                        isPlaying = false
                    }
                },

                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text =
                        if (isPlaying)
                            "⏸ Pause"
                        else
                            "▶ Play"
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Slider(
                value = progress,

                onValueChange = { newValue ->

                    progress = newValue

                    try {

                        val player = mediaPlayer

                        if (player != null) {

                            val position =
                                (
                                        player.duration *
                                                newValue
                                        ).toInt()

                            player.seekTo(position)
                        }

                    } catch (_: Exception) {
                    }
                },

                valueRange = 0f..1f
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


// =================================================================
// QUIZ LISTENING
// =================================================================

@Composable
fun ListeningQuiz(
    activity: ListeningActivity
) {

    var currentQuestionIndex by remember(
        activity.id
    ) {
        mutableIntStateOf(0)
    }

    var selectedAnswerIndex by remember(
        activity.id
    ) {
        mutableIntStateOf(-1)
    }

    var answerSubmitted by remember(
        activity.id
    ) {
        mutableStateOf(false)
    }

    var score by remember(
        activity.id
    ) {
        mutableIntStateOf(0)
    }

    var quizFinished by remember(
        activity.id
    ) {
        mutableStateOf(false)
    }

    ListeningSectionCard(
        title = "✅ Comprehension Quiz"
    ) {

        if (activity.questions.isEmpty()) {

            Text(
                text = "No quiz available yet.",
                fontSize = 15.sp
            )

        } else if (quizFinished) {

            val percentage =
                (score * 100) / activity.questions.size

            Text(
                text = "🎉 Quiz Completed",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "$score / ${activity.questions.size}",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "$percentage %",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Button(
                onClick = {

                    currentQuestionIndex = 0
                    selectedAnswerIndex = -1
                    answerSubmitted = false
                    score = 0
                    quizFinished = false
                },

                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "Restart Quiz"
                )
            }

        } else {

            val question =
                activity.questions[currentQuestionIndex]

            Text(
                text =
                    "Question ${currentQuestionIndex + 1} / ${activity.questions.size}",

                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Text(
                text = question.question,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 27.sp
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            question.options.forEachIndexed { index, option ->

                val selected =
                    selectedAnswerIndex == index

                val cardColor = when {

                    answerSubmitted &&
                            index ==
                            question.correctAnswerIndex ->
                        MaterialTheme.colorScheme.primaryContainer

                    answerSubmitted &&
                            selected &&
                            index !=
                            question.correctAnswerIndex ->
                        MaterialTheme.colorScheme.errorContainer

                    selected ->
                        MaterialTheme.colorScheme.secondaryContainer

                    else ->
                        MaterialTheme.colorScheme.surfaceVariant
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            enabled = !answerSubmitted
                        ) {
                            selectedAnswerIndex = index
                        },

                    shape = RoundedCornerShape(12.dp),

                    colors = CardDefaults.cardColors(
                        containerColor = cardColor
                    )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),

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
                                    else -> ""
                                },

                            fontWeight = FontWeight.Bold,

                            color =
                                MaterialTheme.colorScheme.primary
                        )

                        Spacer(
                            modifier = Modifier.width(12.dp)
                        )

                        Text(
                            text = option,
                            fontSize = 15.sp
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )
            }

            if (answerSubmitted) {

                val correct =
                    selectedAnswerIndex ==
                            question.correctAnswerIndex

                Card(
                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(12.dp),

                    colors = CardDefaults.cardColors(
                        containerColor =
                            if (correct)
                                MaterialTheme.colorScheme
                                    .primaryContainer
                            else
                                MaterialTheme.colorScheme
                                    .errorContainer
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(15.dp)
                    ) {

                        Text(
                            text =
                                if (correct)
                                    "✅ Correct"
                                else
                                    "❌ Incorrect",

                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text =
                                "Correct answer: " +
                                        question.options[
                                            question.correctAnswerIndex
                                        ],

                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = question.explanation,
                            fontSize = 14.sp,
                            lineHeight = 21.sp
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )
            }

            Button(
                onClick = {

                    if (!answerSubmitted) {

                        if (
                            selectedAnswerIndex ==
                            question.correctAnswerIndex
                        ) {
                            score++
                        }

                        answerSubmitted = true

                    } else {

                        if (
                            currentQuestionIndex <
                            activity.questions.lastIndex
                        ) {

                            currentQuestionIndex++
                            selectedAnswerIndex = -1
                            answerSubmitted = false

                        } else {

                            quizFinished = true
                        }
                    }
                },

                enabled = selectedAnswerIndex != -1,

                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text =
                        if (!answerSubmitted) {
                            "Check Answer"
                        } else if (
                            currentQuestionIndex ==
                            activity.questions.lastIndex
                        ) {
                            "Finish Quiz"
                        } else {
                            "Next Question"
                        }
                )
            }
        }
    }
}


// =================================================================
// CARTE DE SECTION
// =================================================================

@Composable
fun ListeningSectionCard(
    title: String,
    content: @Composable () -> Unit
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
            modifier = Modifier.padding(18.dp)
        ) {

            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            content()
        }
    }
}