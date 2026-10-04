package ma.fldm.englishstudies.data

// =====================================================================
// TRADUCTIONS DES PAGES D'OLIVER TWIST
// =====================================================================

data class OliverPageTranslation(
    val french: String,
    val arabic: String
)

object OliverTwistTranslationData {

    /**
     * Clé :
     * numéro du chapitre + numéro de la page
     *
     * Exemple :
     * "1_1" = Chapitre 1, Page 1
     */
    val pages = mapOf(

        // =============================================================
        // CHAPITRE 1 — PAGE 1
        // =============================================================
        "1_1" to OliverPageTranslation(

            french = """
Parmi les édifices publics d'une certaine ville, dont, pour bien des raisons, il sera prudent de m'abstenir de mentionner le nom, et à laquelle je n'attribuerai aucun nom fictif, il en est un autrefois commun à la plupart des villes, grandes ou petites : à savoir, une maison de travail ; et c'est dans cette maison de travail que naquit, à un jour et à une date que je ne prends pas la peine de rappeler, puisqu'ils ne peuvent avoir aucune importance pour le lecteur, du moins à ce stade du récit, l'être humain dont le nom est placé en tête de ce chapitre.
    """.trimIndent(),

            arabic = """
من بين المباني العمومية في بلدةٍ ما، التي سيكون من الحكمة، لأسباب عديدة، أن أمتنع عن ذكر اسمها، والتي لن أطلق عليها اسمًا وهميًا، يوجد مبنى كان شائعًا قديمًا في معظم البلدات، كبيرة كانت أم صغيرة، ألا وهو دارٌ للعمل؛ وفي هذه الدار وُلد، في يومٍ وتاريخٍ لا أرى حاجة إلى تكرارهما، لأنهما لا يمكن أن تكون لهما أي أهمية للقارئ، على الأقل في هذه المرحلة من الحكاية، ذلك الكائن البشري الذي وُضع اسمه في صدر هذا الفصل.
    """.trimIndent()
        )
    )
}