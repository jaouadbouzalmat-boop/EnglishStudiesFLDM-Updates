package ma.fldm.englishstudies

/**
 * Fiche pédagogique complète pour un mot du vocabulaire d'Oliver Twist.
 *
 * Chaque mot peut avoir :
 * - la prononciation affichée
 * - un texte à prononcer avec Android Text-to-Speech
 * - une définition anglaise simple
 * - la traduction française
 * - la traduction arabe
 * - un exemple en anglais
 * - la traduction de l'exemple
 * - le nom de l'illustration locale dans res/drawable
 *
 * Exemple de nom d'illustration :
 * "vocab_workhouse"
 *
 * Le fichier image correspondant devra ensuite être placé dans :
 * app/src/main/res/drawable/
 */
data class OliverVocabularyItem(
    val word: String,
    val pronunciation: String,
    val audioText: String = word,
    val definitionEn: String,
    val translationFr: String,
    val translationAr: String,
    val exampleEn: String,
    val exampleFr: String,
    val exampleAr: String,
    val illustrationName: String? = null
) {

    /**
     * Texte pratique pour Android Text-to-Speech.
     */
    fun getWordAudioText(): String = audioText.ifBlank { word }

    /**
     * Texte pratique pour prononcer l'exemple.
     */
    fun getExampleAudioText(): String = exampleEn

    /**
     * Vérifie si une illustration est associée au mot.
     */
    fun hasIllustration(): Boolean = !illustrationName.isNullOrBlank()

    /**
     * Nom propre de la ressource drawable.
     */
    fun getDrawableName(): String? = illustrationName?.takeIf { it.isNotBlank() }
}

/**
 * Petit modèle pour afficher les informations complémentaires
 * d'un mot dans l'interface.
 */
data class OliverVocabularySection(
    val title: String,
    val value: String
)

/**
 * Conversion pratique vers une liste de sections d'affichage.
 */
fun OliverVocabularyItem.toSections(): List<OliverVocabularySection> {
    return listOf(
        OliverVocabularySection("Prononciation", pronunciation),
        OliverVocabularySection("Définition", definitionEn),
        OliverVocabularySection("Français", translationFr),
        OliverVocabularySection("العربية", translationAr),
        OliverVocabularySection("Exemple", exampleEn),
        OliverVocabularySection("Traduction", exampleFr),
        OliverVocabularySection("الترجمة", exampleAr)
    )
}


