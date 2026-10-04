package ma.fldm.englishstudies.updates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SystemUpdate
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UpdateScreen(
    viewModel: UpdateViewModel,
    onDownload: (String, (Int) -> Unit) -> Unit = { _, _ -> }
) {

    val state by viewModel.uiState.collectAsState()

    var isDownloading by remember {
        mutableStateOf(false)
    }

    var downloadProgress by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(Unit) {
        viewModel.checkForUpdate()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = Icons.Outlined.SystemUpdate,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Mise à jour",
            style = MaterialTheme.typography.headlineMedium
        )

        when (val currentState = state) {

            UpdateUiState.Idle -> {

                Text(
                    text = "Prêt à vérifier les mises à jour."
                )
            }

            UpdateUiState.Checking -> {

                CircularProgressIndicator()

                Text(
                    text = "Recherche d'une nouvelle version..."
                )
            }

            UpdateUiState.UpToDate -> {

                Text(
                    text = "Votre application est à jour.",
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedButton(
                    onClick = {
                        viewModel.checkForUpdate()
                    }
                ) {
                    Text("Vérifier à nouveau")
                }
            }

            is UpdateUiState.Available -> {

                val update = currentState.updateInfo

                Text(
                    text = "Nouvelle version disponible",
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "Version ${update.versionName}",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = update.changelog
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                if (isDownloading) {

                    Text(
                        text = "Téléchargement de la mise à jour..."
                    )

                    LinearProgressIndicator(
                        progress = {
                            downloadProgress / 100f
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    Text(
                        text = "$downloadProgress %",
                        style = MaterialTheme.typography.titleMedium
                    )

                } else {

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isDownloading,
                        onClick = {

                            isDownloading = true
                            downloadProgress = 0

                            onDownload(
                                update.downloadUrl
                            ) { progress ->

                                downloadProgress =
                                    progress.coerceIn(0, 100)
                            }
                        }
                    ) {
                        Text(
                            "Télécharger la mise à jour"
                        )
                    }
                }
            }

            is UpdateUiState.Error -> {

                Text(
                    text = "Erreur : ${currentState.message}",
                    color = MaterialTheme.colorScheme.error
                )

                OutlinedButton(
                    onClick = {
                        viewModel.checkForUpdate()
                    }
                ) {
                    Text("Réessayer")
                }
            }
        }
    }
}