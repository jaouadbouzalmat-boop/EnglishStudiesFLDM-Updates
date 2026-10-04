package ma.fldm.englishstudies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private const val LICENSE_PREFS = "english_studies_license"
private const val LICENSE_KEY = "activated_license_code"

private fun saveActivatedLicenseCode(
    context: android.content.Context,
    code: String
) {
    context.getSharedPreferences(
        LICENSE_PREFS,
        android.content.Context.MODE_PRIVATE
    )
        .edit()
        .putString(LICENSE_KEY, code.trim())
        .apply()
}

@Composable
fun LicenseScreen(
    onActivated: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    var licenseCode by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "English Studies FLDM",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Activation de votre licence"
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Entrez votre clé de licence",
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = licenseCode,
                    onValueChange = {
                        licenseCode = it
                        message = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Clé de licence")
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {

                        val code = licenseCode.trim()

                        if (code.isEmpty()) {
                            message = "Veuillez saisir une clé de licence."
                            return@Button
                        }

                        isLoading = true
                        message = "Vérification en cours..."

                        LicenseManager.activateLicense(
                            licenseCode = code
                        ) { result ->

                            isLoading = false

                            result
                                .onSuccess {

                                    saveActivatedLicenseCode(
                                        context = context,
                                        code = code
                                    )

                                    message =
                                        "Licence activée avec succès."

                                    onActivated()
                                }
                                .onFailure { error ->

                                    message =
                                        error.message
                                            ?: "Activation impossible."
                                }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    Text(
                        if (isLoading)
                            "Vérification..."
                        else
                            "Activer ma licence"
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (message.isNotEmpty()) {
                    Text(
                        text = message,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Retour")
                }
            }
        }
    }
}
