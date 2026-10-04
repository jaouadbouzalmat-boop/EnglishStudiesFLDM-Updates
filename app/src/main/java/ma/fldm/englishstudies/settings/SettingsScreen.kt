package ma.fldm.englishstudies.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudDownload
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.RecordVoiceOver
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlin.math.abs

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onCheckUpdates: () -> Unit = {},
    onLicense: () -> Unit = {},
    onAbout: () -> Unit = {},
    onResetProgress: () -> Unit = {}
) {

    val settings by viewModel.settings.collectAsState()

    Scaffold { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues),

            verticalArrangement =
                Arrangement.spacedBy(14.dp)
        ) {

            // =================================================
            // EN-TÊTE
            // =================================================

            item {

                SettingsHeader()
            }

            // =================================================
            // APPARENCE
            // =================================================

            item {

                SettingsSection(
                    icon = Icons.Outlined.Palette,
                    title = "Apparence"
                ) {

                    SettingsDropdown(
                        title = "Mode appareil",
                        value =
                            if (settings.deviceMode == "TABLET")
                                "Tablette"
                            else
                                "Portable",

                        options = listOf(
                            "Portable" to "PHONE",
                            "Tablette" to "TABLET"
                        ),

                        onSelected = {
                            viewModel.setString(
                                SettingsKeys.DEVICE_MODE,
                                it
                            )
                        }
                    )

                    SettingsDropdown(
                        title = "Thème",
                        value = when (settings.theme) {
                            "LIGHT" -> "Clair"
                            "DARK" -> "Sombre"
                            else -> "Système"
                        },

                        options = listOf(
                            "Système" to "SYSTEM",
                            "Clair" to "LIGHT",
                            "Sombre" to "DARK"
                        ),

                        onSelected = {
                            viewModel.setString(
                                SettingsKeys.THEME,
                                it
                            )
                        }
                    )

                    SettingsDropdown(
                        title = "Taille du texte",
                        value = when {
                            settings.textScale < 0.95f ->
                                "Petit"

                            settings.textScale < 1.10f ->
                                "Normal"

                            settings.textScale < 1.25f ->
                                "Grand"

                            else ->
                                "Très grand"
                        },

                        options = listOf(
                            "Petit" to "0.90",
                            "Normal" to "1.00",
                            "Grand" to "1.15",
                            "Très grand" to "1.30"
                        ),

                        onSelected = {
                            viewModel.setFloat(
                                SettingsKeys.TEXT_SCALE,
                                it.toFloat()
                            )
                        }
                    )

                    SettingsDropdown(
                        title = "Police de lecture",
                        value = settings.font,

                        options = listOf(
                            "Sans" to "Sans",
                            "Serif" to "Serif",
                            "Lecture" to "Reading"
                        ),

                        onSelected = {
                            viewModel.setString(
                                SettingsKeys.FONT,
                                it
                            )
                        }
                    )

                    SettingsDropdown(
                        title = "Couleur principale",
                        value = when (settings.accentColor) {
                            "GREEN" -> "Vert"
                            "PURPLE" -> "Violet"
                            else -> "Bleu"
                        },

                        options = listOf(
                            "Bleu" to "BLUE",
                            "Vert" to "GREEN",
                            "Violet" to "PURPLE"
                        ),

                        onSelected = {
                            viewModel.setString(
                                SettingsKeys.ACCENT_COLOR,
                                it
                            )
                        }
                    )
                }
            }

            // =================================================
            // LECTURE
            // =================================================

            item {

                SettingsSection(
                    icon = Icons.Outlined.MenuBook,
                    title = "Lecture"
                ) {

                    SettingsSwitch(
                        "Lecture automatique",
                        settings.autoPlay
                    ) {
                        viewModel.setBoolean(
                            SettingsKeys.AUTO_PLAY,
                            it
                        )
                    }

                    SettingsSwitch(
                        "Surligner le texte",
                        settings.highlightText
                    ) {
                        viewModel.setBoolean(
                            SettingsKeys.HIGHLIGHT_TEXT,
                            it
                        )
                    }

                    SettingsSwitch(
                        "Défilement automatique",
                        settings.autoScroll
                    ) {
                        viewModel.setBoolean(
                            SettingsKeys.AUTO_SCROLL,
                            it
                        )
                    }

                    SettingsSwitch(
                        "Reprendre à la dernière position",
                        settings.resumePosition
                    ) {
                        viewModel.setBoolean(
                            SettingsKeys.RESUME_POSITION,
                            it
                        )
                    }

                    ReadingSpeedSlider(
                        value = settings.readingSpeed,
                        onFinished = {
                            viewModel.setFloat(
                                SettingsKeys.READING_SPEED,
                                it
                            )
                        }
                    )

                    SettingsDropdown(
                        title = "Traduction",
                        value = when (
                            settings.translationMode
                        ) {
                            "ALWAYS" -> "Toujours"
                            "OFF" -> "Désactivée"
                            else -> "Sur demande"
                        },

                        options = listOf(
                            "Sur demande" to "ON_DEMAND",
                            "Toujours" to "ALWAYS",
                            "Désactivée" to "OFF"
                        ),

                        onSelected = {
                            viewModel.setString(
                                SettingsKeys.TRANSLATION_MODE,
                                it
                            )
                        }
                    )

                    SettingsSwitch(
                        "Afficher les illustrations",
                        settings.showIllustrations
                    ) {
                        viewModel.setBoolean(
                            SettingsKeys.SHOW_ILLUSTRATIONS,
                            it
                        )
                    }
                }
            }

            // =================================================
            // PRONONCIATION
            // =================================================

            item {

                SettingsSection(
                    icon = Icons.Outlined.RecordVoiceOver,
                    title = "Prononciation"
                ) {

                    SettingsSwitch(
                        "Reconnaissance vocale",
                        settings.speechRecognition
                    ) {
                        viewModel.setBoolean(
                            SettingsKeys.SPEECH_RECOGNITION,
                            it
                        )
                    }

                    SettingsDropdown(
                        title = "Accent",
                        value =
                            if (settings.accent == "AMERICAN")
                                "American 🇺🇸"
                            else
                                "British 🇬🇧",

                        options = listOf(
                            "British 🇬🇧" to "BRITISH",
                            "American 🇺🇸" to "AMERICAN"
                        ),

                        onSelected = {
                            viewModel.setString(
                                SettingsKeys.ACCENT,
                                it
                            )
                        }
                    )

                    SettingsSwitch(
                        "Évaluation de la prononciation",
                        settings.pronunciationEvaluation
                    ) {
                        viewModel.setBoolean(
                            SettingsKeys.PRONUNCIATION_EVALUATION,
                            it
                        )
                    }

                    SettingsSwitch(
                        "Répéter les mots difficiles",
                        settings.repeatDifficultWords
                    ) {
                        viewModel.setBoolean(
                            SettingsKeys.REPEAT_DIFFICULT_WORDS,
                            it
                        )
                    }

                    SettingsDropdown(
                        title = "Tolérance",
                        value =
                            when (settings.recognitionTolerance) {
                                "LOW" -> "Faible"
                                "HIGH" -> "Élevée"
                                else -> "Normale"
                            },

                        options = listOf(
                            "Faible" to "LOW",
                            "Normale" to "NORMAL",
                            "Élevée" to "HIGH"
                        ),

                        onSelected = {
                            viewModel.setString(
                                SettingsKeys.RECOGNITION_TOLERANCE,
                                it
                            )
                        }
                    )
                }
            }

            // =================================================
            // AUDIO
            // =================================================

            item {

                SettingsSection(
                    icon = Icons.Outlined.VolumeUp,
                    title = "Audio"
                ) {

                    SettingsSwitch(
                        "Son activé",
                        settings.soundEnabled
                    ) {
                        viewModel.setBoolean(
                            SettingsKeys.SOUND_ENABLED,
                            it
                        )
                    }

                    VolumeSlider(
                        value = settings.volume,
                        onFinished = {
                            viewModel.setFloat(
                                SettingsKeys.VOLUME,
                                it
                            )
                        }
                    )

                    SettingsSwitch(
                        "Sons des boutons",
                        settings.buttonSounds
                    ) {
                        viewModel.setBoolean(
                            SettingsKeys.BUTTON_SOUNDS,
                            it
                        )
                    }

                    SettingsSwitch(
                        "Lecture en arrière-plan",
                        settings.backgroundAudio
                    ) {
                        viewModel.setBoolean(
                            SettingsKeys.BACKGROUND_AUDIO,
                            it
                        )
                    }
                }
            }

            // =================================================
            // LANGUE
            // =================================================

            item {

                SettingsSection(
                    icon = Icons.Outlined.Language,
                    title = "Langue"
                ) {

                    LanguageSetting(
                        title = "Langue de l'application",
                        value = settings.appLanguage,
                        key = SettingsKeys.APP_LANGUAGE,
                        viewModel = viewModel
                    )

                    LanguageSetting(
                        title = "Langue des explications",
                        value = settings.explanationLanguage,
                        key = SettingsKeys.EXPLANATION_LANGUAGE,
                        viewModel = viewModel
                    )

                    LanguageSetting(
                        title = "Langue de traduction",
                        value = settings.translationLanguage,
                        key = SettingsKeys.TRANSLATION_LANGUAGE,
                        viewModel = viewModel
                    )
                }
            }

            // =================================================
            // PROGRESSION
            // =================================================

            item {

                SettingsSection(
                    icon = Icons.Outlined.ShowChart,
                    title = "Étude & progression"
                ) {

                    SettingsSwitch(
                        "Afficher les statistiques",
                        settings.showStatistics
                    ) {
                        viewModel.setBoolean(
                            SettingsKeys.SHOW_STATISTICS,
                            it
                        )
                    }

                    SettingsSwitch(
                        "Afficher les scores",
                        settings.showScores
                    ) {
                        viewModel.setBoolean(
                            SettingsKeys.SHOW_SCORES,
                            it
                        )
                    }

                    SettingsDropdown(
                        title = "Objectif quotidien",
                        value =
                            "${settings.dailyGoalMinutes} minutes",

                        options = listOf(
                            "10 minutes" to "10",
                            "20 minutes" to "20",
                            "30 minutes" to "30",
                            "60 minutes" to "60"
                        ),

                        onSelected = {
                            viewModel.setInt(
                                SettingsKeys.DAILY_GOAL,
                                it.toInt()
                            )
                        }
                    )

                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onResetProgress
                    ) {
                        Text("Réinitialiser ma progression")
                    }
                }
            }

            // =================================================
            // NOTIFICATIONS
            // =================================================

            item {

                SettingsSection(
                    icon = Icons.Outlined.Notifications,
                    title = "Notifications"
                ) {

                    SettingsSwitch(
                        "Notifications activées",
                        settings.notificationsEnabled
                    ) {
                        viewModel.setBoolean(
                            SettingsKeys.NOTIFICATIONS_ENABLED,
                            it
                        )
                    }

                    SettingsSwitch(
                        "Rappel quotidien",
                        settings.dailyReminder
                    ) {
                        viewModel.setBoolean(
                            SettingsKeys.DAILY_REMINDER,
                            it
                        )
                    }

                    SettingsSwitch(
                        "Rappel de lecture",
                        settings.readingReminder
                    ) {
                        viewModel.setBoolean(
                            SettingsKeys.READING_REMINDER,
                            it
                        )
                    }

                    SettingsSwitch(
                        "Rappel de révision",
                        settings.revisionReminder
                    ) {
                        viewModel.setBoolean(
                            SettingsKeys.REVISION_REMINDER,
                            it
                        )
                    }

                    SettingsSwitch(
                        "Nouveaux cours",
                        settings.newLessonsNotification
                    ) {
                        viewModel.setBoolean(
                            SettingsKeys.NEW_LESSONS_NOTIFICATION,
                            it
                        )
                    }

                    SettingsSwitch(
                        "Mises à jour",
                        settings.updateNotification
                    ) {
                        viewModel.setBoolean(
                            SettingsKeys.UPDATE_NOTIFICATION,
                            it
                        )
                    }
                }
            }

            // =================================================
            // HORS CONNEXION
            // =================================================

            item {

                SettingsSection(
                    icon = Icons.Outlined.CloudDownload,
                    title = "Hors connexion & données"
                ) {

                    SettingsSwitch(
                        "Télécharger les cours",
                        settings.downloadCourses
                    ) {
                        viewModel.setBoolean(
                            SettingsKeys.DOWNLOAD_COURSES,
                            it
                        )
                    }

                    SettingsSwitch(
                        "Télécharger les audios",
                        settings.downloadAudios
                    ) {
                        viewModel.setBoolean(
                            SettingsKeys.DOWNLOAD_AUDIOS,
                            it
                        )
                    }

                    SettingsSwitch(
                        "Télécharger les illustrations",
                        settings.downloadIllustrations
                    ) {
                        viewModel.setBoolean(
                            SettingsKeys.DOWNLOAD_ILLUSTRATIONS,
                            it
                        )
                    }

                    SettingsSwitch(
                        "Autoriser les données mobiles",
                        settings.mobileDataAllowed
                    ) {
                        viewModel.setBoolean(
                            SettingsKeys.MOBILE_DATA_ALLOWED,
                            it
                        )
                    }

                    SettingsSwitch(
                        "Wi-Fi uniquement",
                        settings.wifiOnly
                    ) {
                        viewModel.setBoolean(
                            SettingsKeys.WIFI_ONLY,
                            it
                        )
                    }

                    SettingsSwitch(
                        "Qualité audio élevée",
                        settings.highAudioQuality
                    ) {
                        viewModel.setBoolean(
                            SettingsKeys.HIGH_AUDIO_QUALITY,
                            it
                        )
                    }
                }
            }

            // =================================================
            // LICENCE
            // =================================================

            item {

                SettingsSection(
                    icon = Icons.Outlined.Security,
                    title = "Licence & application"
                ) {

                    SettingsAction(
                        title = "Ma licence",
                        subtitle = "Consulter ma licence",
                        onClick = onLicense
                    )

                    SettingsAction(
                        title = "Vérifier les mises à jour",
                        subtitle = "Rechercher une nouvelle version",
                        onClick = onCheckUpdates
                    )

                    SettingsAction(
                        title = "À propos",
                        subtitle = "English Studies",
                        onClick = onAbout
                    )

                    Text(
                        text = "Version 1.3",
                        style = MaterialTheme.typography.bodySmall,
                        color =
                            MaterialTheme.colorScheme
                                .onSurfaceVariant,

                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

// =============================================================
// HEADER
// =============================================================

@Composable
private fun SettingsHeader() {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),

        shape = RoundedCornerShape(22.dp),

        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.primaryContainer
        )
    ) {

        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )

            Spacer(
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Column {

                Text(
                    text = "English Studies",
                    style =
                        MaterialTheme.typography.titleLarge
                )

                Text(
                    text =
                        "Personnalisez votre expérience d'apprentissage.",
                    style =
                        MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

// =============================================================
// SECTION
// =============================================================

@Composable
private fun SettingsSection(
    icon: ImageVector,
    title: String,
    content: @Composable () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),

        shape = RoundedCornerShape(20.dp),

        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surface
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier = Modifier.padding(horizontal = 6.dp)
                )

                Text(
                    text = title,
                    style =
                        MaterialTheme.typography.titleMedium
                )
            }

            Spacer(
                modifier = Modifier.padding(vertical = 3.dp)
            )

            content()
        }
    }
}

// =============================================================
// SWITCH
// =============================================================

@Composable
private fun SettingsSwitch(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),

        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

// =============================================================
// DROPDOWN
// =============================================================

@Composable
private fun SettingsDropdown(
    title: String,
    value: String,
    options: List<Pair<String, String>>,
    onSelected: (String) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),

        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )

        androidx.compose.foundation.layout.Box {

            OutlinedButton(
                onClick = {
                    expanded = true
                }
            ) {
                Text(value)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {

                options.forEach { option ->

                    DropdownMenuItem(
                        text = {
                            Text(option.first)
                        },
                        onClick = {

                            onSelected(option.second)

                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

// =============================================================
// VITESSE DE LECTURE
// =============================================================

@Composable
private fun ReadingSpeedSlider(
    value: Float,
    onFinished: (Float) -> Unit
) {

    val speeds = listOf(
        0.75f,
        1.00f,
        1.25f,
        1.50f,
        2.00f
    )

    val initialIndex =
        speeds.indices.minByOrNull { index ->
            abs(speeds[index] - value)
        } ?: 1

    var index by remember(initialIndex) {
        mutableStateOf(initialIndex)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Vitesse de lecture",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = speedLabel(speeds[index]),
                color = MaterialTheme.colorScheme.primary
            )
        }

        Slider(
            value = index.toFloat(),

            onValueChange = {
                index = it.toInt()
            },

            valueRange =
                0f..speeds.lastIndex.toFloat(),

            steps = speeds.size - 2,

            onValueChangeFinished = {
                onFinished(speeds[index])
            }
        )
    }
}

private fun speedLabel(value: Float): String {

    return when (value) {
        0.75f -> "0,75×"
        1.00f -> "1×"
        1.25f -> "1,25×"
        1.50f -> "1,5×"
        2.00f -> "2×"
        else -> "${value}×"
    }
}

// =============================================================
// VOLUME
// =============================================================

@Composable
private fun VolumeSlider(
    value: Float,
    onFinished: (Float) -> Unit
) {

    var currentValue by remember(value) {
        mutableStateOf(value)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Volume",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text =
                    "${(currentValue * 100).toInt()} %",
                color = MaterialTheme.colorScheme.primary
            )
        }

        Slider(
            value = currentValue,

            onValueChange = {
                currentValue = it
            },

            valueRange = 0f..1f,

            onValueChangeFinished = {
                onFinished(currentValue)
            }
        )
    }
}

// =============================================================
// LANGUE
// =============================================================

@Composable
private fun LanguageSetting(
    title: String,
    value: String,
    key: androidx.datastore.preferences.core.Preferences.Key<String>,
    viewModel: SettingsViewModel
) {

    val label = when (value) {
        "EN" -> "English"
        "AR" -> "العربية"
        else -> "Français"
    }

    SettingsDropdown(
        title = title,
        value = label,

        options = listOf(
            "Français" to "FR",
            "English" to "EN",
            "العربية" to "AR"
        ),

        onSelected = {
            viewModel.setString(key, it)
        }
    )
}

// =============================================================
// ACTION
// =============================================================

@Composable
private fun SettingsAction(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp),

        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color =
                    MaterialTheme.colorScheme
                        .onSurfaceVariant
            )
        }

        Button(
            onClick = onClick
        ) {
            Text("Ouvrir")
        }
    }
}