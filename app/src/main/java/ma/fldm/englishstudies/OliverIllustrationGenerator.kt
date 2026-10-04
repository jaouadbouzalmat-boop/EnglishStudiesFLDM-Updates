package ma.fldm.englishstudies

import android.content.Context
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * Générateur réel d'illustrations pour Oliver Twist.
 *
 * IMPORTANT :
 * - Ce fichier ne contient aucune clé API.
 * - L'application Android appelle un petit serveur Python sécurisé.
 * - OliverPageComposer reste responsable de l'analyse intelligente.
 * - Cette classe est responsable du transport vers le générateur d'images
 *   et de la sauvegarde locale de l'image reçue.
 *
 * Flux :
 *
 * OliverPageComposer
 *        ↓
 * OliverPagePlan
 *        ↓
 * OliverIllustrationGenerator
 *        ↓
 * serveur Python / IA image
 *        ↓
 * PNG Base64
 *        ↓
 * fichier local Android
 */
class OliverIllustrationGenerator(
    private val context: Context,
    private val serverUrl: String
) {

    enum class GenerationMode {
        NEW_SCENE,
        UPDATE_PREVIOUS,
        REUSE_PREVIOUS
    }

    data class IllustrationRequest(
        val chapterNumber: Int,
        val pageNumber: Int,
        val sceneIndex: Int,
        val mode: GenerationMode,
        val prompt: String,
        val previousImagePath: String? = null
    )

    data class IllustrationResult(
        val success: Boolean,
        val mode: GenerationMode,
        val localImagePath: String? = null,
        val errorMessage: String? = null
    )

    /**
     * Génère une illustration à partir d'une scène produite par
     * OliverPageComposer.
     *
     * Pour REUSE_PREVIOUS, aucune requête réseau n'est nécessaire :
     * on retourne directement l'image précédente.
     */
    suspend fun generate(
        request: IllustrationRequest
    ): IllustrationResult = withContext(Dispatchers.IO) {

        try {
            if (request.mode == GenerationMode.REUSE_PREVIOUS) {
                val previous = request.previousImagePath

                if (previous.isNullOrBlank()) {
                    return@withContext IllustrationResult(
                        success = false,
                        mode = request.mode,
                        errorMessage =
                            "REUSE_PREVIOUS demandé mais aucune image précédente n'est disponible."
                    )
                }

                val previousFile = File(previous)

                if (!previousFile.exists()) {
                    return@withContext IllustrationResult(
                        success = false,
                        mode = request.mode,
                        errorMessage =
                            "Image précédente introuvable : $previous"
                    )
                }

                return@withContext IllustrationResult(
                    success = true,
                    mode = request.mode,
                    localImagePath = previousFile.absolutePath
                )
            }

            val previousImageBase64 =
                request.previousImagePath
                    ?.let { path ->
                        val file = File(path)
                        if (file.exists()) {
                            Base64.encodeToString(
                                file.readBytes(),
                                Base64.NO_WRAP
                            )
                        } else {
                            null
                        }
                    }

            val payload = JSONObject().apply {
                put("chapter_number", request.chapterNumber)
                put("page_number", request.pageNumber)
                put("scene_index", request.sceneIndex)
                put("mode", request.mode.name)
                put("prompt", request.prompt)

                if (!previousImageBase64.isNullOrBlank()) {
                    put("previous_image_base64", previousImageBase64)
                } else {
                    put("previous_image_base64", JSONObject.NULL)
                }
            }

            val response = postJson(
                urlString = buildEndpointUrl(),
                payload = payload
            )

            val success = response.optBoolean("success", false)

            if (!success) {
                return@withContext IllustrationResult(
                    success = false,
                    mode = request.mode,
                    errorMessage =
                        response.optString(
                            "error",
                            "Erreur inconnue du serveur de génération."
                        )
                )
            }

            val imageBase64 =
                response.optString("image_base64", "")

            if (imageBase64.isBlank()) {
                return@withContext IllustrationResult(
                    success = false,
                    mode = request.mode,
                    errorMessage =
                        "Le serveur a répondu sans image."
                )
            }

            val localFile = saveImage(
                chapterNumber = request.chapterNumber,
                pageNumber = request.pageNumber,
                sceneIndex = request.sceneIndex,
                imageBase64 = imageBase64
            )

            IllustrationResult(
                success = true,
                mode = request.mode,
                localImagePath = localFile.absolutePath
            )

        } catch (e: Exception) {

            IllustrationResult(
                success = false,
                mode = request.mode,
                errorMessage =
                    e.message ?: e.javaClass.simpleName
            )
        }
    }

    /**
     * Génère toutes les illustrations prévues pour une page.
     *
     * previousPageImagePath = image de référence de la page précédente.
     *
     * Pour la première scène d'une page UPDATE_PREVIOUS, on utilise
     * l'image de la page précédente comme référence.
     *
     * Pour les scènes suivantes de la même page, la dernière image
     * générée devient la référence.
     */
    suspend fun generatePage(
        pagePlan: OliverPagePlan,
        previousPageImagePath: String? = null
    ): List<IllustrationResult> = withContext(Dispatchers.IO) {

        val results = mutableListOf<IllustrationResult>()

        var referenceImage = previousPageImagePath

        for (scene in pagePlan.scenes) {

            val mode = when (scene.continuityMode) {
                "REUSE_PREVIOUS" ->
                    GenerationMode.REUSE_PREVIOUS

                "UPDATE_PREVIOUS" ->
                    GenerationMode.UPDATE_PREVIOUS

                else ->
                    GenerationMode.NEW_SCENE
            }

            val result = generate(
                IllustrationRequest(
                    chapterNumber = pagePlan.chapterNumber,
                    pageNumber = pagePlan.pageNumber,
                    sceneIndex = scene.index,
                    mode = mode,
                    prompt = scene.imagePrompt,
                    previousImagePath = referenceImage
                )
            )

            results.add(result)

            if (result.success && !result.localImagePath.isNullOrBlank()) {
                referenceImage = result.localImagePath
            }

            /*
             * Si une scène échoue, on garde la dernière référence disponible.
             * Cela évite de casser la continuité visuelle de la page suivante.
             */
        }

        results
    }

    /**
     * URL finale :
     * <serverUrl>/oliver/generate
     */
    private fun buildEndpointUrl(): String {
        return serverUrl.trimEnd('/') + "/oliver/generate"
    }

    private fun postJson(
        urlString: String,
        payload: JSONObject
    ): JSONObject {

        val connection =
            URL(urlString).openConnection() as HttpURLConnection

        try {
            connection.requestMethod = "POST"
            connection.connectTimeout = 15_000
            connection.readTimeout = 180_000
            connection.doOutput = true

            connection.setRequestProperty(
                "Content-Type",
                "application/json; charset=UTF-8"
            )

            connection.setRequestProperty(
                "Accept",
                "application/json"
            )

            connection.outputStream.use { output ->
                output.write(
                    payload.toString().toByteArray(Charsets.UTF_8)
                )
                output.flush()
            }

            val responseCode = connection.responseCode

            val responseText =
                if (responseCode in 200..299) {
                    connection.inputStream.bufferedReader(
                        Charsets.UTF_8
                    ).use { it.readText() }
                } else {
                    connection.errorStream
                        ?.bufferedReader(Charsets.UTF_8)
                        ?.use { it.readText() }
                        ?: """{"success":false,"error":"HTTP $responseCode"}"""
                }

            if (responseText.isBlank()) {
                return JSONObject(
                    """{"success":false,"error":"Réponse vide du serveur"}"""
                )
            }

            return JSONObject(responseText)

        } finally {
            connection.disconnect()
        }
    }

    private fun saveImage(
        chapterNumber: Int,
        pageNumber: Int,
        sceneIndex: Int,
        imageBase64: String
    ): File {

        val cleanBase64 =
            imageBase64
                .removePrefix("data:image/png;base64,")
                .removePrefix("data:image/jpeg;base64,")
                .removePrefix("data:image/webp;base64,")
                .trim()

        val bytes =
            Base64.decode(
                cleanBase64,
                Base64.DEFAULT
            )

        val directory =
            File(
                context.filesDir,
                "oliver_illustrations/chapter_$chapterNumber"
            )

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file =
            File(
                directory,
                "page_${pageNumber}_scene_${sceneIndex}.png"
            )

        FileOutputStream(file).use { output ->
            output.write(bytes)
            output.flush()
        }

        return file
    }
}


