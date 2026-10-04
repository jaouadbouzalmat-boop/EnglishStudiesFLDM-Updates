@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package ma.fldm.englishstudies

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Speaker
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object AppSettingsPrefs {
    const val NAME = "english_studies_settings"

    const val THEME = "theme"
    const val TEXT_SIZE = "text_size"
    const val INTERFACE = "interface_mode"
    const val AUTO_READING = "auto_reading"
    const val READING_SPEED = "reading_speed"
    const val SOUND = "sound"
    const val VOICE_RECOGNITION = "voice_recognition"
    const val ACCENT = "accent"
    const val SHOW_PRONUNCIATION_SCORE = "show_pronunciation_score"
    const val LANGUAGE = "language"
    const val NOTIFICATIONS = "notifications"

    fun prefs(context: Context) =
        context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    fun string(context: Context, key: String, default: String): String =
        prefs(context).getString(key, default) ?: default

    fun boolean(context: Context, key: String, default: Boolean): Boolean =
        prefs(context).getBoolean(key, default)

    fun putString(context: Context, key: String, value: String) {
        prefs(context).edit().putString(key, value).apply()
    }

    fun putBoolean(context: Context, key: String, value: Boolean) {
        prefs(context).edit().putBoolean(key, value).apply()
    }
}

private data class SettingColors(
    val iconBackground: Color,
    val iconColor: Color,
    val titleColor: Color,
    val subtitleColor: Color
)

private val settingsColors = SettingColors(
    iconBackground = Color(0xFFEEF4FF),
    iconColor = Color(0xFF2563EB),
    titleColor = Color(0xFF111827),
    subtitleColor = Color(0xFF6B7280)
)

@Composable
fun SettingsScreen(
    hasUpdate: Boolean,
    onBack: () -> Unit,
    onAboutClick: () -> Unit
) {
    val context = LocalContext.current

    var theme by rememberSaveable {
        mutableStateOf(
            AppSettingsPrefs.string(context, AppSettingsPrefs.THEME, "Système")
        )
    }
    var textSize by rememberSaveable {
        mutableStateOf(
            AppSettingsPrefs.string(context, AppSettingsPrefs.TEXT_SIZE, "Moyenne")
        )
    }
    var interfaceMode by rememberSaveable {
        mutableStateOf(
            AppSettingsPrefs.string(context, AppSettingsPrefs.INTERFACE, "Téléphone")
        )
    }
    var autoReading by rememberSaveable {
        mutableStateOf(
            AppSettingsPrefs.boolean(context, AppSettingsPrefs.AUTO_READING, true)
        )
    }
    var readingSpeed by rememberSaveable {
        mutableStateOf(
            AppSettingsPrefs.string(context, AppSettingsPrefs.READING_SPEED, "Normale")
        )
    }
    var soundEnabled by rememberSaveable {
        mutableStateOf(
            AppSettingsPrefs.boolean(context, AppSettingsPrefs.SOUND, true)
        )
    }
    var voiceRecognition by rememberSaveable {
        mutableStateOf(
            AppSettingsPrefs.boolean(context, AppSettingsPrefs.VOICE_RECOGNITION, false)
        )
    }
    var accent by rememberSaveable {
        mutableStateOf(
            AppSettingsPrefs.string(context, AppSettingsPrefs.ACCENT, "American")
        )
    }
    var pronunciationScore by rememberSaveable {
        mutableStateOf(
            AppSettingsPrefs.boolean(
                context,
                AppSettingsPrefs.SHOW_PRONUNCIATION_SCORE,
                true
            )
        )
    }
    var language by rememberSaveable {
        mutableStateOf(
            AppSettingsPrefs.string(context, AppSettingsPrefs.LANGUAGE, "Français")
        )
    }
    var notifications by rememberSaveable {
        mutableStateOf(
            AppSettingsPrefs.boolean(context, AppSettingsPrefs.NOTIFICATIONS, true)
        )
    }

    var dialog by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    val microphonePermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->
            voiceRecognition = granted
            AppSettingsPrefs.putBoolean(
                context,
                AppSettingsPrefs.VOICE_RECOGNITION,
                granted
            )
        }

    LaunchedEffect(Unit) {
        val alreadyGranted =
            context.checkSelfPermission(Manifest.permission.RECORD_AUDIO) ==
                    PackageManager.PERMISSION_GRANTED

        if (alreadyGranted && voiceRecognition) {
            AppSettingsPrefs.putBoolean(
                context,
                AppSettingsPrefs.VOICE_RECOGNITION,
                true
            )
        }
    }

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = AppLanguage.tr("Paramètres", "Settings", "الإعدادات"),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 25.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = AppLanguage.tr("Retour", "Back", "رجوع")
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2563EB),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 16.dp)
        ) {

            SettingsSectionTitle(AppLanguage.tr("Apparence", "Appearance", "المظهر"))

            SettingsCard {
                SettingRow(
                    icon = Icons.Default.LightMode,
                    title = AppLanguage.tr("Thème", "Theme", "السمة"),
                    subtitle = theme,
                    onClick = { dialog = "theme" }
                )
                SettingsDivider()
                SettingRow(
                    icon = Icons.Default.FormatSize,
                    title = AppLanguage.tr("Taille du texte", "Text size", "حجم النص"),
                    subtitle = textSize,
                    onClick = { dialog = "text" }
                )
                SettingsDivider()
                SettingRow(
                    icon = Icons.Default.PhoneAndroid,
                    title = AppLanguage.tr("Adaptation de l'interface", "Interface mode", "وضع الواجهة"),
                    subtitle = interfaceMode,
                    onClick = { dialog = "interface" }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            SettingsSectionTitle(AppLanguage.tr("Lecture", "Reading", "القراءة"))

            SettingsCard {
                SettingSwitchRow(
                    icon = Icons.Default.PlayArrow,
                    title = AppLanguage.tr("Lecture automatique", "Auto reading", "القراءة التلقائية"),
                    subtitle = if (autoReading) AppLanguage.tr("Activée", "Enabled", "مفعّلة") else AppLanguage.tr("Désactivée", "Disabled", "معطلة"),
                    checked = autoReading,
                    onCheckedChange = {
                        autoReading = it
                        AppSettingsPrefs.putBoolean(
                            context,
                            AppSettingsPrefs.AUTO_READING,
                            it
                        )
                    }
                )
                SettingsDivider()
                SettingRow(
                    icon = Icons.Default.Speed,
                    title = AppLanguage.tr("Vitesse de lecture", "Reading speed", "سرعة القراءة"),
                    subtitle = readingSpeed,
                    onClick = { dialog = "speed" }
                )
                SettingsDivider()
                SettingSwitchRow(
                    icon = Icons.Default.Speaker,
                    title = AppLanguage.tr("Son", "Sound", "الصوت"),
                    subtitle = if (soundEnabled) AppLanguage.tr("Sons et lecture audio activés", "Sound and audio enabled", "الصوت والتشغيل الصوتي مفعّلان")
                    else AppLanguage.tr("Sons désactivés", "Sound disabled", "الصوت معطّل"),
                    checked = soundEnabled,
                    onCheckedChange = {
                        soundEnabled = it
                        AppSettingsPrefs.putBoolean(
                            context,
                            AppSettingsPrefs.SOUND,
                            it
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            SettingsSectionTitle(AppLanguage.tr("Prononciation", "Pronunciation", "النطق"))

            SettingsCard {
                SettingSwitchRow(
                    icon = Icons.Default.Mic,
                    title = AppLanguage.tr("Reconnaissance vocale", "Voice recognition", "التعرّف على الصوت"),
                    subtitle = if (voiceRecognition)
                        AppLanguage.tr("Microphone activé", "Microphone enabled", "الميكروفون مفعّل")
                    else
                        AppLanguage.tr("Activer le microphone", "Enable microphone", "تفعيل الميكروفون"),
                    checked = voiceRecognition,
                    onCheckedChange = { enabled ->
                        if (enabled) {
                            val granted =
                                context.checkSelfPermission(
                                    Manifest.permission.RECORD_AUDIO
                                ) == PackageManager.PERMISSION_GRANTED

                            if (granted) {
                                voiceRecognition = true
                                AppSettingsPrefs.putBoolean(
                                    context,
                                    AppSettingsPrefs.VOICE_RECOGNITION,
                                    true
                                )
                            } else {
                                microphonePermissionLauncher.launch(
                                    Manifest.permission.RECORD_AUDIO
                                )
                            }
                        } else {
                            voiceRecognition = false
                            AppSettingsPrefs.putBoolean(
                                context,
                                AppSettingsPrefs.VOICE_RECOGNITION,
                                false
                            )
                        }
                    }
                )
                SettingsDivider()
                SettingRow(
                    icon = Icons.Default.Translate,
                    title = AppLanguage.tr("Accent anglais", "English accent", "اللهجة الإنجليزية"),
                    subtitle = accent,
                    onClick = { dialog = "accent" }
                )
                SettingsDivider()
                SettingSwitchRow(
                    icon = Icons.Default.Assessment,
                    title = AppLanguage.tr("Score de prononciation", "Pronunciation score", "درجة النطق"),
                    subtitle = if (pronunciationScore)
                        AppLanguage.tr("Afficher les résultats", "Show results", "إظهار النتائج")
                    else
                        AppLanguage.tr("Masquer les résultats", "Hide results", "إخفاء النتائج"),
                    checked = pronunciationScore,
                    onCheckedChange = {
                        pronunciationScore = it
                        AppSettingsPrefs.putBoolean(
                            context,
                            AppSettingsPrefs.SHOW_PRONUNCIATION_SCORE,
                            it
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            SettingsSectionTitle(AppLanguage.tr("Langue", "Language", "اللغة"))

            SettingsCard {
                SettingRow(
                    icon = Icons.Default.Language,
                    title = AppLanguage.tr("Langue de l'interface", "Interface language", "لغة الواجهة"),
                    subtitle = language,
                    onClick = { dialog = "language" }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            SettingsSectionTitle(AppLanguage.tr("Application", "Application", "التطبيق"))

            SettingsCard {
                SettingSwitchRow(
                    icon = Icons.Default.Notifications,
                    title = AppLanguage.tr("Notifications", "Notifications", "الإشعارات"),
                    subtitle = if (notifications)
                        AppLanguage.tr("Cours, rappels et actualités", "Courses, reminders and news", "الدروس والتذكيرات والأخبار")
                    else
                        AppLanguage.tr("Notifications désactivées", "Notifications disabled", "الإشعارات معطّلة"),
                    checked = notifications,
                    onCheckedChange = {
                        notifications = it
                        AppSettingsPrefs.putBoolean(
                            context,
                            AppSettingsPrefs.NOTIFICATIONS,
                            it
                        )
                    }
                )
                SettingsDivider()
                SettingRow(
                    icon = Icons.Default.Info,
                    title = AppLanguage.tr("À propos de l'application", "About the app", "حول التطبيق"),
                    subtitle = AppLanguage.tr("Version, licence, mise à jour", "Version, license, update", "الإصدار والترخيص والتحديث"),
                    onClick = onAboutClick,
                    trailingDot = hasUpdate
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = AppLanguage.tr("Réglages enregistrés", "Settings saved", "تم حفظ الإعدادات"),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = AppLanguage.tr("Les préférences sont conservées sur cet appareil.", "Preferences are saved on this device.", "يتم حفظ التفضيلات على هذا الجهاز."),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = AppLanguage.tr("Microphone : ", "Microphone: ", "الميكروفون: "),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = if (voiceRecognition) "Activé" else "Désactivé",
                            fontSize = 13.sp
                        )

                        Spacer(modifier = Modifier.width(18.dp))

                        Text(
                            text = AppLanguage.tr("Son : ", "Sound: ", "الصوت: "),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = if (soundEnabled) "Activé" else "Désactivé",
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    when (dialog) {
        "theme" -> {
            ChoiceDialog(
                title = AppLanguage.tr("Thème", "Theme", "السمة"),
                options = listOf("Clair", "Sombre", "Système"),
                selected = theme,
                onSelect = {
                    theme = it
                    AppSettingsPrefs.putString(
                        context,
                        AppSettingsPrefs.THEME,
                        it
                    )
                    dialog = null
                },
                onDismiss = { dialog = null }
            )
        }

        "text" -> {
            ChoiceDialog(
                title = AppLanguage.tr("Taille du texte", "Text size", "حجم النص"),
                options = listOf("Petite", "Moyenne", "Grande"),
                selected = textSize,
                onSelect = {
                    textSize = it
                    AppSettingsPrefs.putString(
                        context,
                        AppSettingsPrefs.TEXT_SIZE,
                        it
                    )
                    dialog = null
                },
                onDismiss = { dialog = null }
            )
        }

        "interface" -> {
            ChoiceDialog(
                title = AppLanguage.tr("Adaptation de l'interface", "Interface mode", "وضع الواجهة"),
                options = listOf("Téléphone", "Tablette"),
                selected = interfaceMode,
                onSelect = {
                    interfaceMode = it
                    AppSettingsPrefs.putString(
                        context,
                        AppSettingsPrefs.INTERFACE,
                        it
                    )
                    dialog = null
                },
                onDismiss = { dialog = null }
            )
        }

        "speed" -> {
            ChoiceDialog(
                title = AppLanguage.tr("Vitesse de lecture", "Reading speed", "سرعة القراءة"),
                options = listOf("Lente", "Normale", "Rapide"),
                selected = readingSpeed,
                onSelect = {
                    readingSpeed = it
                    AppSettingsPrefs.putString(
                        context,
                        AppSettingsPrefs.READING_SPEED,
                        it
                    )
                    dialog = null
                },
                onDismiss = { dialog = null }
            )
        }

        "accent" -> {
            ChoiceDialog(
                title = AppLanguage.tr("Accent anglais", "English accent", "اللهجة الإنجليزية"),
                options = listOf("British", "American"),
                selected = accent,
                onSelect = {
                    accent = it
                    AppSettingsPrefs.putString(
                        context,
                        AppSettingsPrefs.ACCENT,
                        it
                    )
                    dialog = null
                },
                onDismiss = { dialog = null }
            )
        }

        "language" -> {
            ChoiceDialog(
                title = AppLanguage.tr("Langue de l'interface", "Interface language", "لغة الواجهة"),
                options = listOf("Français", "English", "العربية"),
                selected = language,
                onSelect = {
                    language = it
                    AppLanguage.set(context, it)
                    dialog = null
                },
                onDismiss = { dialog = null }
            )
        }
    }
}

@Composable
private fun SettingsSectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 21.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color(0xFF123A78),
        modifier = Modifier.padding(
            start = 10.dp,
            bottom = 10.dp
        )
    )
}

@Composable
private fun SettingsCard(
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            content()
        }
    }
}

@Composable
private fun SettingRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    trailingDot: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                horizontal = 18.dp,
                vertical = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconCircle(icon)

        Spacer(modifier = Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = settingsColors.titleColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = settingsColors.subtitleColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (trailingDot) {
            Text(
                text = "●",
                color = Color(0xFFEF4444),
                fontSize = 12.sp,
                modifier = Modifier.padding(end = 5.dp)
            )
        }

        Icon(
            imageVector = Icons.Default.ArrowForwardIos,
            contentDescription = null,
            modifier = Modifier.size(17.dp),
            tint = Color(0xFF94A3B8)
        )
    }
}

@Composable
private fun SettingSwitchRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 18.dp,
                vertical = 14.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconCircle(icon)

        Spacer(modifier = Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = settingsColors.titleColor
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = settingsColors.subtitleColor
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun IconCircle(
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(
        modifier = Modifier.size(56.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = settingsColors.iconBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = settingsColors.iconColor,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
private fun SettingsDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(
            start = 88.dp,
            end = 18.dp
        ),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f)
    )
}

@Composable
private fun ChoiceDialog(
    title: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                options.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelect(option)
                            }
                            .padding(vertical = 13.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (option == selected) "●" else "○",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = option,
                            fontSize = 16.sp,
                            fontWeight = if (option == selected)
                                FontWeight.Bold
                            else
                                FontWeight.Normal
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Fermer")
            }
        }
    )
}
