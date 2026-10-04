package ma.fldm.englishstudies.updates

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import androidx.core.content.FileProvider
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

object ApkUpdateInstaller {

    private val executor = Executors.newSingleThreadExecutor()
    private val mainHandler = Handler(Looper.getMainLooper())

    fun downloadAndInstall(
        context: Context,
        downloadUrl: String,
        onProgress: (Int) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {

        executor.execute {

            var connection: HttpURLConnection? = null

            try {

                // =========================================================
                // 1. TELECHARGEMENT
                // =========================================================

                val url = URL(downloadUrl)

                connection =
                    url.openConnection() as HttpURLConnection

                connection.connectTimeout = 15_000
                connection.readTimeout = 30_000
                connection.requestMethod = "GET"
                connection.instanceFollowRedirects = true

                connection.connect()

                val responseCode =
                    connection.responseCode

                if (responseCode !in 200..299) {
                    throw Exception(
                        "Téléchargement impossible. Code HTTP : $responseCode"
                    )
                }

                // Taille totale de l'APK
                val totalBytes =
                    connection.contentLengthLong

                // =========================================================
                // 2. DOSSIER DE TELECHARGEMENT
                // =========================================================

                val downloadsDirectory =
                    context.getExternalFilesDir(
                        Environment.DIRECTORY_DOWNLOADS
                    )
                        ?: throw Exception(
                            "Dossier de téléchargement introuvable."
                        )

                if (!downloadsDirectory.exists()) {
                    downloadsDirectory.mkdirs()
                }

                // =========================================================
                // 3. FICHIER APK
                // =========================================================

                val apkFile =
                    File(
                        downloadsDirectory,
                        "EnglishStudies_update.apk"
                    )

                if (apkFile.exists()) {
                    apkFile.delete()
                }

                // =========================================================
                // 4. TELECHARGEMENT AVEC PROGRESSION
                // =========================================================

                connection.inputStream.use { input ->

                    apkFile.outputStream().use { output ->

                        val buffer =
                            ByteArray(16 * 1024)

                        var downloadedBytes = 0L
                        var lastProgress = -1

                        // Affichage immédiat
                        mainHandler.post {
                            onProgress(0)
                        }

                        while (true) {

                            val count =
                                input.read(buffer)

                            if (count == -1) {
                                break
                            }

                            output.write(
                                buffer,
                                0,
                                count
                            )

                            downloadedBytes += count

                            // Calcul du pourcentage
                            if (totalBytes > 0L) {

                                val progress =
                                    (
                                            downloadedBytes * 100L /
                                                    totalBytes
                                            )
                                        .toInt()
                                        .coerceIn(0, 100)

                                // Évite d'envoyer inutilement
                                // plusieurs fois le même pourcentage
                                if (progress != lastProgress) {

                                    lastProgress = progress

                                    mainHandler.post {
                                        onProgress(progress)
                                    }
                                }
                            }
                        }

                        output.flush()
                    }
                }

                connection.disconnect()
                connection = null

                // Assurer 100 %
                mainHandler.post {
                    onProgress(100)
                }

                // =========================================================
                // 5. VERIFICATION
                // =========================================================

                if (!apkFile.exists()) {
                    throw Exception(
                        "Le fichier APK n'existe pas."
                    )
                }

                if (apkFile.length() == 0L) {
                    throw Exception(
                        "Le fichier APK téléchargé est vide."
                    )
                }

                // =========================================================
                // 6. URI FILEPROVIDER
                // =========================================================

                val apkUri: Uri =
                    FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        apkFile
                    )

                // =========================================================
                // 7. INSTALLATEUR ANDROID
                // =========================================================

                val installIntent =
                    Intent(
                        Intent.ACTION_INSTALL_PACKAGE
                    ).apply {

                        setDataAndType(
                            apkUri,
                            "application/vnd.android.package-archive"
                        )

                        addFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK
                        )

                        addFlags(
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )

                        addFlags(
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        )

                        clipData =
                            ClipData.newRawUri(
                                "English Studies APK",
                                apkUri
                            )

                        putExtra(
                            Intent.EXTRA_INSTALLER_PACKAGE_NAME,
                            context.packageName
                        )

                        putExtra(
                            Intent.EXTRA_NOT_UNKNOWN_SOURCE,
                            true
                        )

                        putExtra(
                            Intent.EXTRA_ALLOW_REPLACE,
                            true
                        )

                        putExtra(
                            Intent.EXTRA_RETURN_RESULT,
                            true
                        )
                    }

                // =========================================================
                // 8. LANCEMENT DE L'INSTALLATEUR
                // =========================================================

                mainHandler.post {

                    try {

                        context.startActivity(
                            installIntent
                        )

                    } catch (exception: Exception) {

                        onError(
                            exception.message
                                ?: "Impossible de lancer l'installation."
                        )
                    }
                }

            } catch (exception: Exception) {

                connection?.disconnect()

                mainHandler.post {

                    onError(
                        exception.message
                            ?: "Erreur pendant le téléchargement."
                    )
                }
            }
        }
    }
}