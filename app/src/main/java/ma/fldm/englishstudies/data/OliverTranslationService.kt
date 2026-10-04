package ma.fldm.englishstudies.data

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions

// =====================================================================
// SERVICE DE TRADUCTION OLIVER TWIST
// Anglais → Français
// Anglais → Arabe
// =====================================================================

class OliverTranslationService {

    // ================================================================
    // ANGLAIS → FRANÇAIS
    // ================================================================

    private val englishFrenchTranslator: Translator =
        Translation.getClient(
            TranslatorOptions.Builder()
                .setSourceLanguage(
                    TranslateLanguage.ENGLISH
                )
                .setTargetLanguage(
                    TranslateLanguage.FRENCH
                )
                .build()
        )

    // ================================================================
    // ANGLAIS → ARABE
    // ================================================================

    private val englishArabicTranslator: Translator =
        Translation.getClient(
            TranslatorOptions.Builder()
                .setSourceLanguage(
                    TranslateLanguage.ENGLISH
                )
                .setTargetLanguage(
                    TranslateLanguage.ARABIC
                )
                .build()
        )

    // ================================================================
    // CONDITIONS DE TÉLÉCHARGEMENT
    // ================================================================

    private val downloadConditions =
        DownloadConditions.Builder()
            .requireWifi()
            .build()

    // ================================================================
    // CACHE
    // ================================================================

    private val cache =
        mutableMapOf<String, OliverPageTranslation>()

    // ================================================================
    // TRADUIRE UNE PAGE
    // ================================================================

    fun translatePage(
        englishText: String,
        onSuccess: (String, String) -> Unit,
        onError: (Exception) -> Unit
    ) {

        if (englishText.isBlank()) {
            onError(
                IllegalArgumentException(
                    "Le texte anglais est vide."
                )
            )
            return
        }

        // ------------------------------------------------------------
        // CACHE
        // ------------------------------------------------------------

        val cached =
            cache[englishText]

        if (cached != null) {

            onSuccess(
                cached.french,
                cached.arabic
            )

            return
        }

        // ------------------------------------------------------------
        // MODÈLE FRANÇAIS
        // ------------------------------------------------------------

        englishFrenchTranslator
            .downloadModelIfNeeded(
                downloadConditions
            )
            .addOnSuccessListener {

                englishFrenchTranslator
                    .translate(englishText)
                    .addOnSuccessListener { frenchText ->

                        // ------------------------------------------------
                        // MODÈLE ARABE
                        // ------------------------------------------------

                        englishArabicTranslator
                            .downloadModelIfNeeded(
                                downloadConditions
                            )
                            .addOnSuccessListener {

                                englishArabicTranslator
                                    .translate(englishText)
                                    .addOnSuccessListener { arabicText ->

                                        val translation =
                                            OliverPageTranslation(
                                                french = frenchText,
                                                arabic = arabicText
                                            )

                                        cache[englishText] =
                                            translation

                                        onSuccess(
                                            frenchText,
                                            arabicText
                                        )
                                    }
                                    .addOnFailureListener { error ->
                                        onError(error)
                                    }
                            }
                            .addOnFailureListener { error ->
                                onError(error)
                            }
                    }
                    .addOnFailureListener { error ->
                        onError(error)
                    }
            }
            .addOnFailureListener { error ->
                onError(error)
            }
    }

    // ================================================================
    // FERMER LES TRADUCTEURS
    // ================================================================

    fun close() {

        try {
            englishFrenchTranslator.close()
        } catch (_: Exception) {
        }

        try {
            englishArabicTranslator.close()
        } catch (_: Exception) {
        }
    }
}