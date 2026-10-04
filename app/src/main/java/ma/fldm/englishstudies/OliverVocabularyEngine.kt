package ma.fldm.englishstudies

import java.util.Locale

/**
 * Sélection intelligente du vocabulaire d'Oliver Twist.
 *
 * Principe :
 * - analyse le texte de la page ;
 * - repère les mots réellement présents dans la page ;
 * - privilégie les mots littéraires, narratifs et difficiles ;
 * - évite les mots grammaticaux ou trop fréquents ;
 * - retourne directement les objets OliverVocabulary utilisés
 *   par l'interface actuelle.
 *
 * Cette première version fonctionne entièrement en local.
 * Aucun service externe ni aucune API n'est nécessaire.
 */
object OliverVocabularyEngine {

    /**
     * Niveau pédagogique approximatif.
     * Plus le score est élevé, plus le mot est intéressant pour l'étudiant.
     */
    private data class VocabularyEntry(
        val word: String,
        val pronunciation: String,
        val definition: String,
        val french: String,
        val arabic: String,
        val example: String,
        val score: Int,
        val aliases: List<String> = emptyList()
    )

    /**
     * Dictionnaire pédagogique initial.
     *
     * Nous l'enrichirons progressivement avec le vocabulaire
     * réellement rencontré dans le roman.
     */
    private val dictionary = listOf(

        VocabularyEntry(
            word = "workhouse",
            pronunciation = "/ˈwɜːrkhaʊs/",
            definition = "A place where poor people could live and receive basic support.",
            french = "hospice pour pauvres",
            arabic = "مَأْوًى للفقراء",
            example = "Oliver Twist was born in a workhouse.",
            score = 100,
            aliases = listOf("work-house")
        ),

        VocabularyEntry(
            word = "orphan",
            pronunciation = "/ˈɔːrfən/",
            definition = "A child whose parents are dead or who has lost parental care.",
            french = "orphelin",
            arabic = "يتيم",
            example = "Oliver was an orphan.",
            score = 98
        ),

        VocabularyEntry(
            word = "parish",
            pronunciation = "/ˈpærɪʃ/",
            definition = "A local administrative or church district.",
            french = "paroisse / circonscription locale",
            arabic = "رعية / دائرة محلية",
            example = "The parish authorities managed the workhouse.",
            score = 96
        ),

        VocabularyEntry(
            word = "pauper",
            pronunciation = "/ˈpɔːpər/",
            definition = "A very poor person who depends on public support.",
            french = "indigent / pauvre assisté",
            arabic = "فقير مُعان",
            example = "The paupers lived under strict rules.",
            score = 95
        ),

        VocabularyEntry(
            word = "infant",
            pronunciation = "/ˈɪnfənt/",
            definition = "A very young baby or child.",
            french = "nourrisson",
            arabic = "رضيع",
            example = "The infant was weak and hungry.",
            score = 94
        ),

        VocabularyEntry(
            word = "newborn",
            pronunciation = "/ˈnjuːbɔːrn/",
            definition = "A baby that has just been born.",
            french = "nouveau-né",
            arabic = "حديث الولادة",
            example = "The newborn Oliver was very weak.",
            score = 93
        ),

        VocabularyEntry(
            word = "miserable",
            pronunciation = "/ˈmɪzərəbəl/",
            definition = "Very unhappy, uncomfortable, or poor.",
            french = "misérable / très malheureux",
            arabic = "بائس / شديد التعاسة",
            example = "The children lived in miserable conditions.",
            score = 92
        ),

        VocabularyEntry(
            word = "poverty",
            pronunciation = "/ˈpɒvərti/",
            definition = "The state of being very poor and lacking basic resources.",
            french = "pauvreté",
            arabic = "فقر",
            example = "The story shows the effects of poverty.",
            score = 91
        ),

        VocabularyEntry(
            word = "hunger",
            pronunciation = "/ˈhʌŋɡər/",
            definition = "The physical need or desire for food.",
            french = "faim",
            arabic = "جوع",
            example = "The children suffered from hunger.",
            score = 90
        ),

        VocabularyEntry(
            word = "starving",
            pronunciation = "/ˈstɑːrvɪŋ/",
            definition = "Extremely hungry and in need of food.",
            french = "affamé",
            arabic = "جائع جدًا",
            example = "The boys were starving.",
            score = 89
        ),

        VocabularyEntry(
            word = "feeble",
            pronunciation = "/ˈfiːbəl/",
            definition = "Physically weak or lacking strength.",
            french = "faible / frêle",
            arabic = "ضعيف / واهن",
            example = "The child had a feeble voice.",
            score = 88
        ),

        VocabularyEntry(
            word = "gasping",
            pronunciation = "/ˈɡɑːspɪŋ/",
            definition = "Breathing with difficulty or very quickly.",
            french = "haletant / à bout de souffle",
            arabic = "يلهث / يتنفس بصعوبة",
            example = "He was gasping for breath.",
            score = 87
        ),

        VocabularyEntry(
            word = "labour",
            pronunciation = "/ˈleɪbər/",
            definition = "Hard physical work; also the process of giving birth.",
            french = "travail / travail de l'accouchement",
            arabic = "عمل شاق / مخاض",
            example = "His mother was in labour.",
            score = 90,
            aliases = listOf("labor")
        ),

        VocabularyEntry(
            word = "mortality",
            pronunciation = "/mɔːrˈtælɪti/",
            definition = "The state or fact of being mortal and subject to death.",
            french = "mortalité / condition mortelle",
            arabic = "الفناء / معدل الوفيات حسب السياق",
            example = "The passage reflects on human mortality.",
            score = 93
        ),

        VocabularyEntry(
            word = "mortal",
            pronunciation = "/ˈmɔːrtəl/",
            definition = "A human being, viewed as someone who can die.",
            french = "mortel",
            arabic = "فانٍ / مائت",
            example = "Every mortal is subject to death.",
            score = 88
        ),

        VocabularyEntry(
            word = "destitute",
            pronunciation = "/ˈdestɪtuːt/",
            definition = "Having no money, resources, or means of support.",
            french = "démuni / sans ressources",
            arabic = "مُعدم / بلا موارد",
            example = "The family was destitute.",
            score = 94
        ),

        VocabularyEntry(
            word = "neglected",
            pronunciation = "/nɪˈɡlektɪd/",
            definition = "Not given proper care or attention.",
            french = "négligé / délaissé",
            arabic = "مُهمَل",
            example = "The neglected child needed care.",
            score = 90
        ),

        VocabularyEntry(
            word = "sorrow",
            pronunciation = "/ˈsɒrəʊ/",
            definition = "A strong feeling of sadness.",
            french = "chagrin / tristesse",
            arabic = "حزن",
            example = "The news brought great sorrow.",
            score = 89
        ),

        VocabularyEntry(
            word = "grief",
            pronunciation = "/ɡriːf/",
            definition = "Deep sadness caused by loss.",
            french = "deuil / profonde tristesse",
            arabic = "حزن عميق / حداد",
            example = "She was overcome with grief.",
            score = 92
        ),

        VocabularyEntry(
            word = "bewildered",
            pronunciation = "/bɪˈwɪldərd/",
            definition = "Confused because you do not understand what is happening.",
            french = "déconcerté / désorienté",
            arabic = "حائر / مرتبك",
            example = "Oliver looked bewildered.",
            score = 89
        ),

        VocabularyEntry(
            word = "frightened",
            pronunciation = "/ˈfraɪtənd/",
            definition = "Feeling afraid or scared.",
            french = "effrayé",
            arabic = "خائف / مذعور",
            example = "The boy was frightened.",
            score = 78
        ),

        VocabularyEntry(
            word = "punishment",
            pronunciation = "/ˈpʌnɪʃmənt/",
            definition = "A penalty given for doing something wrong.",
            french = "punition",
            arabic = "عقوبة",
            example = "The punishment was severe.",
            score = 86
        ),

        VocabularyEntry(
            word = "whip",
            pronunciation = "/wɪp/",
            definition = "To hit someone with a whip.",
            french = "fouetter",
            arabic = "يجلد / يضرب بالسوط",
            example = "The officer threatened to whip him.",
            score = 90
        ),

        VocabularyEntry(
            word = "beg",
            pronunciation = "/beɡ/",
            definition = "To ask strongly for something, often because you need it.",
            french = "supplier / mendier",
            arabic = "يتوسل / يستجدي",
            example = "Oliver begged for more food.",
            score = 85
        ),

        VocabularyEntry(
            word = "plead",
            pronunciation = "/pliːd/",
            definition = "To ask for something in a very serious or emotional way.",
            french = "supplier / implorer",
            arabic = "يلتمس / يتوسل",
            example = "He pleaded for mercy.",
            score = 88
        ),

        VocabularyEntry(
            word = "fled",
            pronunciation = "/fled/",
            definition = "Ran away from a place or situation.",
            french = "s'enfuit",
            arabic = "هرب",
            example = "The boy fled from the danger.",
            score = 84
        ),

        VocabularyEntry(
            word = "encounter",
            pronunciation = "/ɪnˈkaʊntər/",
            definition = "To meet someone or experience something unexpectedly.",
            french = "rencontrer / faire face à",
            arabic = "يواجه / يلتقي",
            example = "Oliver encountered a stranger.",
            score = 87
        ),

        VocabularyEntry(
            word = "robbery",
            pronunciation = "/ˈrɒbəri/",
            definition = "The crime of taking money or property by force or threat.",
            french = "vol à main armée / cambriolage",
            arabic = "سرقة / سطو",
            example = "The police investigated the robbery.",
            score = 91
        ),

        VocabularyEntry(
            word = "thief",
            pronunciation = "/θiːf/",
            definition = "A person who steals things.",
            french = "voleur",
            arabic = "لص",
            example = "The boy was accused of being a thief.",
            score = 88
        ),

        VocabularyEntry(
            word = "hideout",
            pronunciation = "/ˈhaɪdaʊt/",
            definition = "A secret place where a person hides.",
            french = "cachette / repaire",
            arabic = "مخبأ / وكر",
            example = "Fagin's hideout was crowded and dark.",
            score = 92
        ),

        VocabularyEntry(
            word = "magistrate",
            pronunciation = "/ˈmædʒɪstreɪt/",
            definition = "A public official who deals with legal cases.",
            french = "magistrat",
            arabic = "قاضٍ / مسؤول قضائي",
            example = "The magistrate listened to the case.",
            score = 94
        ),

        VocabularyEntry(
            word = "parish",
            pronunciation = "/ˈpærɪʃ/",
            definition = "A local administrative district.",
            french = "paroisse / district local",
            arabic = "رعية / دائرة محلية",
            example = "The parish officials controlled the workhouse.",
            score = 90
        ),

        VocabularyEntry(
            word = "kindness",
            pronunciation = "/ˈkaɪndnəs/",
            definition = "A friendly and caring attitude toward someone.",
            french = "gentillesse / bonté",
            arabic = "لطف / إحسان",
            example = "Oliver remembered her kindness.",
            score = 80
        ),

        VocabularyEntry(
            word = "compassion",
            pronunciation = "/kəmˈpæʃən/",
            definition = "A feeling of sympathy that makes you want to help someone.",
            french = "compassion",
            arabic = "رحمة / تعاطف",
            example = "Mrs. Bedwin showed compassion.",
            score = 91
        ),

        VocabularyEntry(
            word = "shelter",
            pronunciation = "/ˈʃeltər/",
            definition = "A place that protects someone from danger or bad weather.",
            french = "abri / refuge",
            arabic = "مأوى / ملجأ",
            example = "The room gave the child shelter.",
            score = 82
        ),

        VocabularyEntry(
            word = "cradle",
            pronunciation = "/ˈkreɪdəl/",
            definition = "A small bed for a baby.",
            french = "berceau",
            arabic = "مهد",
            example = "The baby lay in the cradle.",
            score = 83
        ),

        VocabularyEntry(
            word = "attendant",
            pronunciation = "/əˈtendənt/",
            definition = "A person whose job is to look after someone or something.",
            french = "préposé / personne chargée de surveiller",
            arabic = "موظف / شخص مكلف بالرعاية",
            example = "The attendant stayed near the bed.",
            score = 88
        ),

        VocabularyEntry(
            word = "recovery",
            pronunciation = "/rɪˈkʌvəri/",
            definition = "The process of becoming healthy or strong again.",
            french = "rétablissement",
            arabic = "تعافٍ / شفاء",
            example = "Oliver made a slow recovery.",
            score = 82
        ),

        VocabularyEntry(
            word = "murmur",
            pronunciation = "/ˈmɜːrmər/",
            definition = "To speak very quietly so that it is difficult to hear.",
            french = "murmurer",
            arabic = "يهمس",
            example = "She murmured a few words.",
            score = 85
        ),

        VocabularyEntry(
            word = "solemn",
            pronunciation = "/ˈsɒləm/",
            definition = "Serious and quiet because something is important or sad.",
            french = "grave / solennel",
            arabic = "جاد / مهيب",
            example = "The room became solemn.",
            score = 88
        )
    )

    /**
     * Extrait automatiquement les mots intéressants d'une page.
     *
     * @param pageText texte anglais de la page actuelle
     * @param maxWords nombre maximal de cartes à retourner
     */
    fun extractForPage(
        pageText: String,
        maxWords: Int = 10
    ): List<OliverVocabulary> {

        if (pageText.isBlank()) return emptyList()

        val normalizedText =
            pageText.lowercase(Locale.US)

        val candidates = dictionary
            .filter { entry ->
                entry.aliases
                    .plus(entry.word)
                    .any { key ->
                        containsWholeWord(
                            normalizedText,
                            key.lowercase(Locale.US)
                        )
                    }
            }
            .distinctBy { it.word }
            .sortedByDescending { entry ->
                /*
                 * Bonus léger lorsque le mot apparaît plusieurs fois
                 * sur la page, sans laisser la répétition dominer
                 * complètement la sélection.
                 */
                val occurrences =
                    countOccurrences(
                        normalizedText,
                        entry.aliases + entry.word
                    )

                entry.score + minOf(occurrences, 3) * 4
            }

        return candidates
            .take(maxWords.coerceIn(1, 15))
            .map { entry ->
                OliverVocabulary(
                    word = entry.word,
                    pronunciation = entry.pronunciation,
                    definition = entry.definition,
                    french = entry.french,
                    arabic = entry.arabic,
                    example = entry.example
                )
            }
    }

    /**
     * Variante pratique : retourne les mots les plus utiles,
     * tout en essayant de garder un mélange entre :
     * - vocabulaire narratif
     * - vocabulaire littéraire
     * - vocabulaire difficile.
     */
    fun extractBalanced(
        pageText: String,
        maxWords: Int = 10
    ): List<OliverVocabulary> {

        val selected =
            extractForPage(
                pageText = pageText,
                maxWords = 15
            )

        if (selected.size <= maxWords) {
            return selected
        }

        val firstHalf = selected
            .take((maxWords + 1) / 2)

        val secondHalf = selected
            .drop((maxWords + 1) / 2)
            .take(maxWords / 2)

        return (firstHalf + secondHalf)
            .distinctBy { it.word }
            .take(maxWords)
    }

    /**
     * Recherche rapide d'un mot dans notre dictionnaire local.
     * Utile plus tard pour le clic sur un mot dans le texte.
     */
    fun findWord(word: String): OliverVocabulary? {

        val normalized =
            word
                .lowercase(Locale.US)
                .trim()
                .removeSurrounding("\"")
                .removeSurrounding("'")
                .removeSuffix(".")
                .removeSuffix(",")
                .removeSuffix(";")
                .removeSuffix(":")
                .removeSuffix("!")
                .removeSuffix("?")

        val entry =
            dictionary.firstOrNull { item ->
                item.word.equals(
                    normalized,
                    ignoreCase = true
                ) ||
                        item.aliases.any {
                            it.equals(
                                normalized,
                                ignoreCase = true
                            )
                        }
            }

        return entry?.let {
            OliverVocabulary(
                word = it.word,
                pronunciation = it.pronunciation,
                definition = it.definition,
                french = it.french,
                arabic = it.arabic,
                example = it.example
            )
        }
    }

    /**
     * Vérification stricte : "poor" ne doit pas être confondu
     * avec "poorly", etc.
     */
    private fun containsWholeWord(
        text: String,
        word: String
    ): Boolean {

        if (word.isBlank()) return false

        val escaped =
            Regex.escape(word)

        return Regex(
            """(?<![A-Za-z])$escaped(?![A-Za-z])"""
        ).containsMatchIn(text)
    }

    private fun countOccurrences(
        text: String,
        aliases: List<String>
    ): Int {

        var total = 0

        aliases.forEach { alias ->
            val escaped = Regex.escape(
                alias.lowercase(Locale.US)
            )

            total += Regex(
                """(?<![A-Za-z])$escaped(?![A-Za-z])"""
            ).findAll(text).count()
        }

        return total
    }
}


