package ma.fldm.englishstudies

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max

private const val LICENSE_PREFS = "english_studies_license"
private const val LICENSE_KEY = "activated_license_code"

private val StartupBlue = Color(0xFF17458A)
private val StartupBlueDark = Color(0xFF123A73)
private val StartupGold = Color(0xFFE8B12D)
private val StartupBackground = Color(0xFFFFFBF8)

private fun savedLicenseCode(context: Context): String? {
    return context
        .getSharedPreferences(LICENSE_PREFS, Context.MODE_PRIVATE)
        .getString(LICENSE_KEY, null)
        ?.trim()
        ?.takeIf { it.isNotEmpty() }
}

private fun saveLicenseCode(context: Context, code: String) {
    context
        .getSharedPreferences(LICENSE_PREFS, Context.MODE_PRIVATE)
        .edit()
        .putString(LICENSE_KEY, code.trim())
        .apply()
}

@Composable
fun LicenseGateScreen(
    onAccessGranted: () -> Unit,
    onOpenLicense: () -> Unit
) {
    val context = LocalContext.current

    var loading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }
    var trialDays by remember { mutableStateOf<Long?>(null) }
    var hasActiveLicense by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        loading = true
        errorMessage = ""

        LicenseManager.ensureAnonymousUser { authResult ->
            authResult
                .onSuccess {
                    val savedCode = savedLicenseCode(context)

                    if (!savedCode.isNullOrEmpty()) {
                        LicenseManager.checkLicense(savedCode) { result ->
                            result
                                .onSuccess { info ->
                                    if (
                                        info.status == "active" &&
                                        info.deviceId ==
                                        com.google.firebase.auth.FirebaseAuth
                                            .getInstance()
                                            .currentUser
                                            ?.uid
                                    ) {
                                        saveLicenseCode(context, savedCode)
                                        hasActiveLicense = true
                                        trialDays = null
                                        loading = false
                                        onAccessGranted()
                                        return@checkLicense
                                    }

                                    checkTrialNow(
                                        context = context,
                                        onSuccess = { days ->
                                            trialDays = days
                                            loading = false
                                        },
                                        onError = { message ->
                                            errorMessage = message
                                            loading = false
                                        }
                                    )
                                }
                                .onFailure {
                                    checkTrialNow(
                                        context = context,
                                        onSuccess = { days ->
                                            trialDays = days
                                            loading = false
                                        },
                                        onError = { message ->
                                            errorMessage = message
                                            loading = false
                                        }
                                    )
                                }
                        }
                    } else {
                        checkTrialNow(
                            context = context,
                            onSuccess = { days ->
                                trialDays = days
                                loading = false
                            },
                            onError = { message ->
                                errorMessage = message
                                loading = false
                            }
                        )
                    }
                }
                .onFailure { error ->
                    errorMessage =
                        error.message ?: "Connexion Firebase impossible."
                    loading = false
                }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = StartupBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ---------------------------------------------------------
            // BRANDING — même identité visuelle que la page d'accueil
            // ---------------------------------------------------------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "English Studies",
                        fontSize = 31.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = StartupBlue
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "FLDM – Fès",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = StartupBlue
                    )
                }

                Text(
                    text = "🎓",
                    fontSize = 42.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Learning English at University",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ---------------------------------------------------------
            // CARTE PRINCIPALE
            // ---------------------------------------------------------
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = StartupBlue
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(22.dp)
                ) {
                    Text(
                        text = "Bienvenue",
                        fontSize = 29.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(7.dp))

                    Text(
                        text = "Votre parcours universitaire commence ici.",
                        fontSize = 16.sp,
                        lineHeight = 22.sp,
                        color = Color.White.copy(alpha = 0.94f)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(50.dp),
                            color = StartupGold
                        ) {
                            Text(
                                text = "🔐",
                                modifier = Modifier.padding(12.dp),
                                fontSize = 24.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Accès sécurisé",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Text(
                                text = "Essai gratuit ou licence active",
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            if (loading) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = StartupBlue
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Vérification de votre accès...",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            if (!loading && errorMessage.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Accès temporairement indisponible",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(7.dp))

                        Text(
                            text = errorMessage,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = onOpenLicense,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = StartupBlue
                            )
                        ) {
                            Text("Entrer ma licence")
                        }
                    }
                }
            }

            if (!loading && errorMessage.isEmpty() && !hasActiveLicense) {
                val days = trialDays ?: 7L
                val progress = max(0f, minOf(1f, days / 7f))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(22.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Essai gratuit",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = StartupBlue
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = if (days > 0)
                                        "$days jour(s) d'essai restant(s)"
                                    else
                                        "Votre essai gratuit est terminé.",
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Text(
                                text = if (days > 0) "7 J" else "FIN",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = StartupGold
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxWidth(),
                            color = StartupBlue,
                            trackColor = StartupBlue.copy(alpha = 0.12f)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        if (days > 0) {
                            Button(
                                onClick = onAccessGranted,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = StartupBlue
                                )
                            ) {
                                Text(
                                    text = "Continuer vers l'application",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        OutlinedButton(
                            onClick = onOpenLicense,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "J'ai une clé de licence",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = StartupBlue
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Cours • Exercices • Quiz • Audio",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = StartupBlue.copy(alpha = 0.78f)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "English Studies – FLDM Fès",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun checkTrialNow(
    context: Context,
    onSuccess: (Long) -> Unit,
    onError: (String) -> Unit
) {
    TrialManager.checkTrial(context) { result ->
        result
            .onSuccess { info ->
                val days =
                    info.startedAtMillis?.let {
                        TrialManager.remainingDays(it)
                    } ?: 7L

                onSuccess(days)
            }
            .onFailure { error ->
                onError(
                    error.message ?: "Impossible de vérifier l'essai gratuit."
                )
            }
    }
}
