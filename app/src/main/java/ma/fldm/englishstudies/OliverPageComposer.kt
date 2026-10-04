package ma.fldm.englishstudies

import java.util.Locale

/**
 * Moteur local d'analyse et de continuité visuelle d'Oliver Twist.
 *
 * Principe v6 :
 * 1. La page courante est analysée pour trouver de vrais événements.
 * 2. Le plan visuel de la page précédente est mémorisé par chapitre/page.
 * 3. S'il n'y a pas de changement significatif, la scène précédente est
 *    réutilisée comme base visuelle.
 * 4. S'il y a un changement, seuls les éléments concernés sont modifiés.
 * 5. Les personnages, vêtements, visage et décor sont conservés autant que
 *    possible afin de produire une continuité de bande dessinée.
 *
 * Aucun service externe n'est utilisé.
 */

data class OliverVisualState(
    val characters: List<String> = emptyList(),
    val location: String = "Lieu non précisé",
    val actions: List<String> = emptyList(),
    val mood: String = "ambiance neutre",
    val objects: List<String> = emptyList(),
    val timeOfDay: String = "non précisé",
    val weather: String = "non précisé",
    val clothing: Map<String, String> = emptyMap(),
    val continuityNotes: List<String> = emptyList()
)

data class PageVisualDecision(
    val mode: String,
    val changedElements: List<String>,
    val similarityScore: Int,
    val reason: String
)

data class OliverIllustrationScene(
    val index: Int,
    val sourceText: String,
    val type: String,
    val characters: List<String>,
    val location: String,
    val action: String,
    val mood: String,
    val visualDescription: String,
    val imagePrompt: String,
    val visualState: OliverVisualState = OliverVisualState(),
    val continuityMode: String = "NEW_SCENE",
    val changedElements: List<String> = emptyList(),
    val similarityScore: Int = 0
)

data class OliverPagePlan(
    val chapterNumber: Int,
    val pageNumber: Int,
    val originalText: String,
    val illustrationCount: Int,
    val scenes: List<OliverIllustrationScene>,
    val visualState: OliverVisualState = OliverVisualState(),
    val continuityMode: String = "NEW_SCENE",
    val changedElements: List<String> = emptyList(),
    val similarityScore: Int = 0,
    val previousPageAvailable: Boolean = false
)

object OliverPageComposer {

    private data class SentenceInfo(
        val index: Int,
        val text: String,
        val normalized: String,
        val characters: List<String>,
        val location: String,
        val action: String,
        val mood: String,
        val objects: List<String>,
        val timeOfDay: String,
        val weather: String,
        val characterScore: Int,
        val actionScore: Int,
        val moodScore: Int,
        val locationScore: Int,
        val eventStrength: Int,
        val isNarrativelyUseful: Boolean
    )

    private data class PageKey(
        val chapterNumber: Int,
        val pageNumber: Int
    )

    private data class ComparisonResult(
        val mode: String,
        val changedElements: List<String>,
        val similarityScore: Int,
        val reason: String
    )

    // Mémoire de continuité pendant l'utilisation de l'application.
    private val pageMemory = mutableMapOf<PageKey, OliverPagePlan>()

    /* =========================================================
       OUTILS DE RECHERCHE
       ========================================================= */

    private fun containsWord(text: String, word: String): Boolean {
        val normalizedWord = word.lowercase(Locale.US).trim()
        if (normalizedWord.isBlank()) return false
        val escaped = Regex.escape(normalizedWord)
        return Regex("""(?<![A-Za-z])$escaped(?![A-Za-z])""").containsMatchIn(text)
    }

    private fun containsAny(text: String, words: List<String>): Boolean =
        words.any { containsWord(text, it) }

    private fun countOccurrences(text: String, words: List<String>): Int =
        words.count { containsWord(text, it) }

    private fun distinctPreservingOrder(values: List<String>): List<String> =
        values.filter { it.isNotBlank() }.distinct()

    /* =========================================================
       PERSONNAGES
       ========================================================= */

    private val namedCharacterKeywords = linkedMapOf(
        "Oliver" to listOf("oliver twist", "oliver"),
        "Fagin" to listOf("fagin"),
        "Nancy" to listOf("nancy"),
        "Bill Sikes" to listOf("bill sikes", "sikes"),
        "Mr. Brownlow" to listOf("mr. brownlow", "brownlow"),
        "Mrs. Bedwin" to listOf("mrs. bedwin", "bedwin"),
        "Noah Claypole" to listOf("noah claypole", "noah", "claypole"),
        "Artful Dodger" to listOf("artful dodger", "dodger")
    )

    private val contextualCharacterKeywords = linkedMapOf(
        "Oliver" to listOf(
            "the infant",
            "the baby",
            "the boy",
            "the child",
            "the orphan",
            "the newborn"
        ),
        "Oliver's mother" to listOf(
            "oliver's mother",
            "his mother",
            "the mother"
        )
    )

    /* =========================================================
       LIEUX
       ========================================================= */

    private val locationKeywords = linkedMapOf(
        "workhouse" to listOf("workhouse", "work house", "parish workhouse"),
        "London" to listOf("london"),
        "Brownlow's house" to listOf(
            "brownlow's house",
            "brownlow's home",
            "mr. brownlow's house",
            "mr. brownlow's home"
        ),
        "Fagin's hideout" to listOf(
            "fagin's house",
            "fagin's den",
            "fagin's hideout"
        ),
        "court" to listOf("court", "magistrate", "justice"),
        "road" to listOf("road", "street", "streets", "highway"),
        "bedroom" to listOf("bedroom", "bed-side", "bedside", "bed room"),
        "hospital / infirmary" to listOf("hospital", "infirmary", "ward"),
        "church" to listOf("church", "chapel"),
        "market" to listOf("market", "shop", "shops", "store")
    )

    /* =========================================================
       ACTIONS / ÉVÉNEMENTS
       ========================================================= */

    private val actionKeywords = linkedMapOf(
        "naissance" to listOf(
            "was born", "born", "birth", "gave birth", "brought into the world",
            "into the world", "first breath", "first breathed", "delivered", "labour",
            "labor"
        ),
        "mort" to listOf(
            "died", "death", "dead", "expired", "passed away", "dying", "die"
        ),
        "manger" to listOf(
            "food", "eat", "eating", "ate", "hungry", "meal", "breakfast", "dinner"
        ),
        "marcher / fuir" to listOf(
            "walked", "walking", "ran", "runs", "run away", "escape", "escapes",
            "fled", "flee", "running away"
        ),
        "parler / demander" to listOf(
            "ask", "asked", "asks", "speak", "speaks", "spoke", "said", "says",
            "replied", "answered", "questioned", "remarked", "observed", "added"
        ),
        "rencontrer" to listOf("meet", "meets", "met", "encountered"),
        "voler" to listOf(
            "steal", "stealing", "stole", "theft", "robbery", "pickpocket"
        ),
        "pleurer / supplier" to listOf(
            "beg", "begs", "begged", "cry", "cried", "wept", "pleaded"
        ),
        "être puni" to listOf(
            "punished", "punishment", "whipped", "beat", "beaten"
        ),
        "être malade" to listOf(
            "sick", "illness", "ill", "recover", "fever", "gasping", "suffering"
        ),
        "se battre" to listOf(
            "fought", "fight", "fighting", "struggle", "struggling", "attack", "attacked"
        ),
        "dormir" to listOf("sleep", "slept", "sleeping", "bed", "asleep"),
        "entrer / sortir" to listOf(
            "entered", "enter", "came in", "went out", "left", "departed", "arrived"
        )
    )

    /* =========================================================
       AMBIANCES / ÉMOTIONS
       ========================================================= */

    private val moodKeywords = linkedMapOf(
        "tristesse" to listOf(
            "died", "death", "dead", "sad", "alone", "lonely", "mourning", "grief", "sorrow"
        ),
        "peur" to listOf(
            "afraid", "frightened", "fear", "danger", "dangerous", "terrified", "terror"
        ),
        "faim" to listOf(
            "hungry", "hunger", "little food", "scarce food", "starving"
        ),
        "froid" to listOf(
            "cold", "winter", "freezing", "chilly", "ice", "icy"
        ),
        "tension" to listOf(
            "angry", "criminal", "robbery", "threat", "threatened", "fought", "struggle", "struggling"
        ),
        "fragilité / souffrance" to listOf(
            "gasping", "unequally poised", "mortal", "mortality", "respiration", "breathing",
            "suffering", "weak", "weakness", "pain", "painful", "feeble"
        ),
        "espoir" to listOf(
            "hope", "safe", "kindness", "rescued", "comfort", "glad", "relief"
        ),
        "tendresse" to listOf(
            "dear", "lamb", "tender", "gentle", "love", "loving", "kindly"
        )
    )

    /* =========================================================
       OBJETS VISUELS
       ========================================================= */

    private val objectKeywords = linkedMapOf(
        "berceau" to listOf("cradle", "crib"),
        "lit" to listOf("bed", "bed-side", "bedside"),
        "chapeau" to listOf("hat", "cap"),
        "nourriture" to listOf("food", "gruel", "bread", "meal"),
        "porte" to listOf("door", "doorway"),
        "vêtements modestes" to listOf("clothes", "dress", "gown", "garment"),
        "lettre / document" to listOf("letter", "document", "paper"),
        "argent" to listOf("money", "coin", "coins", "pound", "pounds")
    )

    /* =========================================================
       TEMPS / MÉTÉO
       ========================================================= */

    private val timeKeywords = linkedMapOf(
        "matin" to listOf("morning", "dawn", "daybreak"),
        "après-midi" to listOf("afternoon"),
        "soir" to listOf("evening", "dusk"),
        "nuit" to listOf("night", "midnight")
    )

    private val weatherKeywords = linkedMapOf(
        "froid" to listOf("cold", "freezing", "chilly", "frost", "snow", "winter"),
        "pluie" to listOf("rain", "raining", "wet", "storm"),
        "brouillard" to listOf("fog", "mist", "foggy")
    )

    /* =========================================================
       API PUBLIQUE
       ========================================================= */

    /**
     * Compatibilité avec l'appel actuel de OliverSmartPagePanel.
     *
     * La mémoire précédente est recherchée automatiquement dans pageMemory.
     */
    fun createPagePlan(
        chapterNumber: Int,
        pageNumber: Int,
        pageText: String
    ): OliverPagePlan {
        val previousPlan = pageMemory[PageKey(chapterNumber, pageNumber - 1)]
        return createPagePlan(
            chapterNumber = chapterNumber,
            pageNumber = pageNumber,
            pageText = pageText,
            previousPlan = previousPlan
        )
    }

    /**
     * Version explicite permettant de fournir directement la page précédente.
     */
    fun createPagePlan(
        chapterNumber: Int,
        pageNumber: Int,
        pageText: String,
        previousPlan: OliverPagePlan?
    ): OliverPagePlan {

        val cleanText = normalizePageText(pageText)

        if (cleanText.isBlank()) {
            val fallback = createFallbackScene(
                index = 1,
                sourceText = "",
                reason = "Texte vide",
                previousScene = previousPlan?.scenes?.firstOrNull()
            )

            val result = OliverPagePlan(
                chapterNumber = chapterNumber,
                pageNumber = pageNumber,
                originalText = pageText,
                illustrationCount = 1,
                scenes = listOf(fallback),
                visualState = fallback.visualState,
                continuityMode = fallback.continuityMode,
                changedElements = fallback.changedElements,
                similarityScore = fallback.similarityScore,
                previousPageAvailable = previousPlan != null
            )
            pageMemory[PageKey(chapterNumber, pageNumber)] = result
            return result
        }

        val sentences = splitIntoSentences(cleanText)
        val infos = sentences.mapIndexed { index, sentence ->
            analyzeSentence(
                sentence = sentence,
                sentenceIndex = index,
                chapterNumber = chapterNumber
            )
        }

        val sceneCount = chooseIllustrationCount(infos)
        val groups = splitIntoSceneGroups(infos, sceneCount)

        val scenes = groups.mapIndexed { index, group ->
            val previousScene = choosePreviousScene(
                currentIndex = index,
                previousPlan = previousPlan
            )

            buildScene(
                index = index + 1,
                group = group,
                chapterNumber = chapterNumber,
                previousScene = previousScene
            )
        }

        val pageState = mergePageState(scenes)
        val pageDecision = compareWithPrevious(
            currentState = pageState,
            previousPlan = previousPlan,
            currentText = cleanText
        )

        val finalizedScenes = scenes.map { scene ->
            // Si toute la page est sans changement significatif, la première
            // scène devient explicitement REUSE_PREVIOUS.
            if (
                previousPlan != null &&
                pageDecision.mode == "REUSE_PREVIOUS" &&
                scene.index == 1
            ) {
                val previousScene = previousPlan.scenes.firstOrNull()
                if (previousScene != null) {
                    scene.copy(
                        continuityMode = "REUSE_PREVIOUS",
                        changedElements = emptyList(),
                        similarityScore = pageDecision.similarityScore,
                        imagePrompt = buildReusePrompt(
                            sourceText = scene.sourceText,
                            previousScene = previousScene,
                            reason = pageDecision.reason
                        )
                    )
                } else {
                    scene
                }
            } else {
                scene
            }
        }

        val result = OliverPagePlan(
            chapterNumber = chapterNumber,
            pageNumber = pageNumber,
            originalText = cleanText,
            illustrationCount = finalizedScenes.size,
            scenes = finalizedScenes,
            visualState = pageState,
            continuityMode = pageDecision.mode,
            changedElements = pageDecision.changedElements,
            similarityScore = pageDecision.similarityScore,
            previousPageAvailable = previousPlan != null
        )

        pageMemory[PageKey(chapterNumber, pageNumber)] = result
        return result
    }

    fun clearMemory() {
        pageMemory.clear()
    }

    fun clearChapterMemory(chapterNumber: Int) {
        pageMemory.keys
            .filter { it.chapterNumber == chapterNumber }
            .toList()
            .forEach { pageMemory.remove(it) }
    }

    fun getStoredPagePlan(chapterNumber: Int, pageNumber: Int): OliverPagePlan? =
        pageMemory[PageKey(chapterNumber, pageNumber)]

    /* =========================================================
       NORMALISATION / PHRASES
       ========================================================= */

    private fun normalizePageText(text: String): String =
        text
            .replace("\r\n", " ")
            .replace("\r", " ")
            .replace("\n", " ")
            .replace(Regex("\\s+"), " ")
            .trim()

    private fun splitIntoSentences(text: String): List<String> =
        text
            .split(Regex("(?<=[.!?])\\s+"))
            .map { it.trim() }
            .filter { it.isNotBlank() }

    /* =========================================================
       ANALYSE D'UNE PHRASE
       ========================================================= */

    private fun analyzeSentence(
        sentence: String,
        sentenceIndex: Int,
        chapterNumber: Int
    ): SentenceInfo {

        val normalized = sentence.lowercase(Locale.US)
        val detectedCharacters = detectCharacters(normalized, chapterNumber)
        val locationResult = detectDominantCategory(normalized, locationKeywords)
        val actionResult = detectDominantCategory(normalized, actionKeywords)
        val moodResult = detectDominantCategory(normalized, moodKeywords)
        val objectResult = detectMultipleCategories(normalized, objectKeywords)
        val timeResult = detectDominantCategory(normalized, timeKeywords)
        val weatherResult = detectDominantCategory(normalized, weatherKeywords)

        val location = locationResult.first
        val locationScore = locationResult.second
        val action = actionResult.first
        val actionScore = actionResult.second
        val mood = moodResult.first
        val moodScore = moodResult.second
        val characterScore = detectedCharacters.size

        val quoteSignal = hasDialogueSignal(sentence)
        val transitionSignal = containsAny(
            normalized,
            listOf(
                "then", "afterward", "afterwards", "soon", "suddenly",
                "meanwhile", "however", "but", "when", "before", "after"
            )
        )

        val eventStrength =
            (characterScore * 3) +
                    (actionScore * 4) +
                    (locationScore * 2) +
                    (moodScore * 2) +
                    if (quoteSignal) 2 else 0

        val isNarrativelyUseful =
            actionScore > 0 ||
                    locationScore > 0 ||
                    characterScore > 0 ||
                    moodScore >= 2 ||
                    objectResult.isNotEmpty() ||
                    quoteSignal ||
                    transitionSignal

        return SentenceInfo(
            index = sentenceIndex,
            text = sentence,
            normalized = normalized,
            characters = detectedCharacters,
            location = location,
            action = action,
            mood = mood,
            objects = objectResult,
            timeOfDay = timeResult.first,
            weather = weatherResult.first,
            characterScore = characterScore,
            actionScore = actionScore,
            moodScore = moodScore,
            locationScore = locationScore,
            eventStrength = eventStrength,
            isNarrativelyUseful = isNarrativelyUseful
        )
    }

    private fun hasDialogueSignal(sentence: String): Boolean =
        sentence.contains('"') ||
                sentence.contains('“') ||
                sentence.contains('”') ||
                sentence.contains('‘') ||
                sentence.contains('’')

    private fun detectCharacters(
        normalized: String,
        chapterNumber: Int
    ): List<String> {
        val result = mutableListOf<String>()

        namedCharacterKeywords.forEach { (name, keys) ->
            if (keys.any { containsWord(normalized, it) }) {
                result.add(name)
            }
        }

        val strongBirthContext = chapterNumber == 1 && containsAny(
            normalized,
            listOf(
                "was born", "born", "birth", "gave birth", "into the world",
                "first breath", "first breathed", "newborn", "infant", "labour", "labor"
            )
        )

        if (strongBirthContext && containsAny(normalized, contextualCharacterKeywords["Oliver"].orEmpty())) {
            result.add("Oliver")
        }

        val explicitMother = containsAny(normalized, listOf("oliver's mother"))
        val motherContext = containsWord(normalized, "mother") &&
                (containsWord(normalized, "oliver") || strongBirthContext)

        if (explicitMother || motherContext) {
            result.add("Oliver's mother")
        }

        // Après une mention très explicite d'Oliver, certains pronoms peuvent
        // rester volontairement non résolus : on préfère l'incertitude à un faux positif.
        return result.distinct()
    }

    private fun detectDominantCategory(
        text: String,
        categories: Map<String, List<String>>
    ): Pair<String, Int> {
        var bestCategory = "Non précisé"
        var bestScore = 0

        categories.forEach { (category, keys) ->
            var score = 0
            keys.forEach { key ->
                if (containsWord(text, key)) score += 1
            }
            if (score > bestScore) {
                bestScore = score
                bestCategory = category
            }
        }

        return bestCategory to bestScore
    }

    private fun detectMultipleCategories(
        text: String,
        categories: Map<String, List<String>>
    ): List<String> {
        val result = mutableListOf<String>()
        categories.forEach { (category, keys) ->
            if (keys.any { containsWord(text, it) }) result.add(category)
        }
        return result
    }

    /* =========================================================
       NOMBRE D'ILLUSTRATIONS
       ========================================================= */

    private fun chooseIllustrationCount(infos: List<SentenceInfo>): Int {
        if (infos.isEmpty()) return 1

        val useful = infos.filter { it.isNarrativelyUseful }
        val strongEvents = useful.count { it.eventStrength >= 5 }
        if (strongEvents <= 1) return 1

        val boundaries = findStrongBoundaries(infos)
        val strongBoundaryCount = boundaries.count { it >= 4 }

        return when {
            strongEvents >= 4 && strongBoundaryCount >= 3 -> 3
            strongEvents >= 3 && strongBoundaryCount >= 2 -> 3
            strongEvents >= 2 && strongBoundaryCount >= 1 -> 2
            else -> 1
        }
    }

    private fun findStrongBoundaries(infos: List<SentenceInfo>): List<Int> {
        if (infos.size < 2) return emptyList()
        return infos.zipWithNext().map { (a, b) -> boundaryScore(a, b) }
    }

    private fun boundaryScore(a: SentenceInfo, b: SentenceInfo): Int {
        var score = 0

        if (a.actionScore > 0 && b.actionScore > 0 && a.action != b.action) score += 5
        if (a.locationScore > 0 && b.locationScore > 0 && a.location != b.location) score += 5
        if (a.characters.isNotEmpty() && b.characters.isNotEmpty() && a.characters.toSet() != b.characters.toSet()) score += 3
        if (a.moodScore > 0 && b.moodScore > 0 && a.mood != b.mood) score += 2
        if (a.objects.toSet() != b.objects.toSet()) score += 2
        if (a.timeOfDay != b.timeOfDay && a.timeOfDay != "Non précisé" && b.timeOfDay != "Non précisé") score += 3
        if (a.weather != b.weather && a.weather != "Non précisé" && b.weather != "Non précisé") score += 3
        if (b.isNarrativelyUseful && !a.isNarrativelyUseful) score += 3
        if (containsAny(b.normalized, listOf("then", "afterward", "afterwards", "meanwhile", "suddenly", "however"))) score += 3

        return score
    }

    private fun splitIntoSceneGroups(
        infos: List<SentenceInfo>,
        targetCount: Int
    ): List<List<SentenceInfo>> {
        if (infos.isEmpty()) return listOf(emptyList())
        if (targetCount <= 1 || infos.size == 1) return listOf(infos)

        val boundaries = findStrongBoundaries(infos)
        val selected = boundaries
            .mapIndexed { index, score -> index to score }
            .filter { it.second >= 4 }
            .sortedByDescending { it.second }
            .take(targetCount - 1)
            .sortedBy { it.first }
            .map { it.first + 1 }
            .distinct()

        if (selected.isEmpty()) return listOf(infos)

        val cuts = listOf(0) + selected + listOf(infos.size)
        val groups = mutableListOf<List<SentenceInfo>>()

        for (i in 0 until cuts.lastIndex) {
            val group = infos.subList(cuts[i], cuts[i + 1])
            if (group.isNotEmpty()) groups.add(group)
        }

        return groups.take(3)
    }

    /* =========================================================
       CONSTRUCTION D'UNE SCÈNE
       ========================================================= */

    private fun buildScene(
        index: Int,
        group: List<SentenceInfo>,
        chapterNumber: Int,
        previousScene: OliverIllustrationScene?
    ): OliverIllustrationScene {

        if (group.isEmpty()) {
            return createFallbackScene(index, "", "Groupe vide", previousScene)
        }

        val sourceText = group.joinToString(" ") { it.text }
        val lowerSource = sourceText.lowercase(Locale.US)

        val characters = group.flatMap { it.characters }.distinct().toMutableList()
        val location = dominantValue(group.map { it.location to it.locationScore }, "Lieu non précisé")
        val action = dominantValue(group.map { it.action to it.actionScore }, "situation générale")
        val mood = dominantValue(group.map { it.mood to it.moodScore }, "ambiance neutre")
        val objects = group.flatMap { it.objects }.distinct()
        val timeOfDay = dominantValue(group.map { it.timeOfDay to if (it.timeOfDay != "Non précisé") 1 else 0 }, "non précisé")
        val weather = dominantValue(group.map { it.weather to if (it.weather != "Non précisé") 1 else 0 }, "non précisé")

        val birthContext = chapterNumber == 1 && containsAny(
            lowerSource,
            listOf("born", "birth", "into the world", "first breath", "first breathed", "infant")
        )
        if (birthContext && !characters.contains("Oliver")) characters.add("Oliver")

        val finalCharacters = characters.distinct()

        val currentState = OliverVisualState(
            characters = finalCharacters,
            location = location,
            actions = if (action == "situation générale") emptyList() else listOf(action),
            mood = mood,
            objects = objects,
            timeOfDay = timeOfDay,
            weather = weather,
            clothing = inheritedClothing(finalCharacters, previousScene),
            continuityNotes = buildContinuityNotes(finalCharacters, previousScene)
        )

        val comparison = compareStates(currentState, previousScene?.visualState)

        val visualDescription = buildVisualDescription(
            sourceText = sourceText,
            state = currentState,
            decision = comparison
        )

        val imagePrompt = if (previousScene != null) {
            buildUpdatePrompt(
                sourceText = sourceText,
                state = currentState,
                previousScene = previousScene,
                decision = comparison
            )
        } else {
            buildNewPrompt(
                sourceText = sourceText,
                state = currentState
            )
        }

        val continuityMode = when {
            previousScene == null -> "NEW_SCENE"
            comparison.mode == "REUSE_PREVIOUS" -> "REUSE_PREVIOUS"
            else -> "UPDATE_PREVIOUS"
        }

        return OliverIllustrationScene(
            index = index,
            sourceText = sourceText,
            type = selectSceneType(action, mood),
            characters = finalCharacters,
            location = location,
            action = action,
            mood = mood,
            visualDescription = visualDescription,
            imagePrompt = imagePrompt,
            visualState = currentState,
            continuityMode = continuityMode,
            changedElements = comparison.changedElements,
            similarityScore = comparison.similarityScore
        )
    }

    private fun choosePreviousScene(
        currentIndex: Int,
        previousPlan: OliverPagePlan?
    ): OliverIllustrationScene? {
        if (previousPlan == null || previousPlan.scenes.isEmpty()) return null
        return previousPlan.scenes.getOrNull(currentIndex) ?: previousPlan.scenes.first()
    }

    private fun dominantValue(
        items: List<Pair<String, Int>>,
        defaultValue: String
    ): String {
        val candidates = items
            .filter { it.second > 0 }
            .filter {
                it.first != "Non précisé" &&
                        it.first != "situation générale" &&
                        it.first != "ambiance neutre"
            }

        if (candidates.isEmpty()) return defaultValue

        return candidates
            .groupBy { it.first }
            .mapValues { (_, values) -> values.sumOf { it.second } }
            .maxByOrNull { it.value }
            ?.key
            ?: defaultValue
    }

    private fun selectSceneType(action: String, mood: String): String = when {
        action == "naissance" || action == "mort" -> "moment narratif"
        mood == "peur" || mood == "tension" || mood == "fragilité / souffrance" -> "scène dramatique"
        action == "rencontrer" -> "scène de rencontre"
        action == "marcher / fuir" || action == "se battre" || action == "entrer / sortir" -> "scène d'action"
        action == "parler / demander" -> "scène de dialogue"
        else -> "scène narrative"
    }

    /* =========================================================
       COMPARAISON AVEC LA PAGE PRECEDENTE
       ========================================================= */

    private fun compareWithPrevious(
        currentState: OliverVisualState,
        previousPlan: OliverPagePlan?,
        currentText: String
    ): ComparisonResult {
        if (previousPlan == null) {
            return ComparisonResult(
                mode = "NEW_SCENE",
                changedElements = listOf("première page illustrée"),
                similarityScore = 0,
                reason = "Aucune illustration précédente disponible."
            )
        }

        val previous = previousPlan.visualState
        val result = compareStates(currentState, previous)

        val explicitChangeSignal = detectExplicitVisualChange(currentText)

        if (result.mode == "REUSE_PREVIOUS" && explicitChangeSignal.isNotEmpty()) {
            return ComparisonResult(
                mode = "UPDATE_PREVIOUS",
                changedElements = distinctPreservingOrder(result.changedElements + explicitChangeSignal),
                similarityScore = (result.similarityScore - 8).coerceAtLeast(0),
                reason = "Le texte signale un changement visuel explicite."
            )
        }

        return result
    }

    private fun compareStates(
        current: OliverVisualState,
        previous: OliverVisualState?
    ): ComparisonResult {
        if (previous == null) {
            return ComparisonResult(
                mode = "NEW_SCENE",
                changedElements = listOf("première scène"),
                similarityScore = 0,
                reason = "Aucun état visuel précédent."
            )
        }

        val changed = mutableListOf<String>()
        var total = 0
        var same = 0

        fun compareObservedSet(
            label: String,
            currentValues: Set<String>,
            previousValues: Set<String>,
            weight: Int = 1,
            unspecified: Set<String> = emptySet()
        ) {
            // Règle centrale de continuité : l'absence d'information nouvelle
            // n'est PAS un changement. On conserve alors la valeur précédente.
            val observed = currentValues
                .filter { it.isNotBlank() && it !in unspecified }
                .toSet()

            if (observed.isEmpty()) return

            total += weight
            if (observed == previousValues) {
                same += weight
            } else {
                changed.add(label)
            }
        }

        compareObservedSet(
            "personnages",
            current.characters.toSet(),
            previous.characters.toSet(),
            weight = 3
        )
        compareObservedSet(
            "lieu",
            setOf(current.location),
            setOf(previous.location),
            weight = 3,
            unspecified = setOf("Lieu non précisé")
        )
        compareObservedSet(
            "action",
            current.actions.toSet(),
            previous.actions.toSet(),
            weight = 3
        )
        compareObservedSet(
            "ambiance",
            setOf(current.mood),
            setOf(previous.mood),
            weight = 2,
            unspecified = setOf("ambiance neutre")
        )
        compareObservedSet(
            "objets",
            current.objects.toSet(),
            previous.objects.toSet(),
            weight = 2
        )
        compareObservedSet(
            "moment de la journée",
            setOf(current.timeOfDay),
            setOf(previous.timeOfDay),
            weight = 1,
            unspecified = setOf("non précisé", "Non précisé")
        )
        compareObservedSet(
            "météo",
            setOf(current.weather),
            setOf(previous.weather),
            weight = 1,
            unspecified = setOf("non précisé", "Non précisé")
        )

        // Aucun élément explicitement nouveau : on garde l'image précédente.
        if (changed.isEmpty()) {
            return ComparisonResult(
                mode = "REUSE_PREVIOUS",
                changedElements = emptyList(),
                similarityScore = 100,
                reason = "Aucun nouvel événement ou changement visuel suffisamment explicite n'a été détecté."
            )
        }

        val similarityScore = if (total == 0) {
            100
        } else {
            ((same * 100) / total).coerceIn(0, 100)
        }

        return ComparisonResult(
            mode = "UPDATE_PREVIOUS",
            changedElements = changed.distinct(),
            similarityScore = similarityScore,
            reason = "Des éléments visuels doivent évoluer avec le développement de l'histoire."
        )
    }

    private fun detectExplicitVisualChange(text: String): List<String> {
        val lower = text.lowercase(Locale.US)
        val changes = mutableListOf<String>()

        if (containsAny(lower, listOf("entered", "went out", "left", "arrived", "came to", "returned"))) {
            changes.add("position / déplacement")
        }
        if (containsAny(lower, listOf("next room", "another room", "new room", "outside", "indoors", "upstairs", "downstairs"))) {
            changes.add("décor / espace")
        }
        if (containsAny(lower, listOf("night", "morning", "evening", "dawn", "dusk"))) {
            changes.add("moment de la journée")
        }
        if (containsAny(lower, listOf("rain", "raining", "snow", "fog", "frost", "cold"))) {
            changes.add("météo / lumière")
        }
        if (containsAny(lower, listOf("hat", "coat", "dress", "clothes", "changed his clothes", "put on"))) {
            changes.add("vêtements")
        }
        if (containsAny(lower, listOf("opened the door", "closed the door", "door"))) {
            changes.add("porte / accès")
        }

        return changes.distinct()
    }

    /* =========================================================
       ÉTAT VISUEL ET CONTINUITÉ
       ========================================================= */

    private fun mergePageState(scenes: List<OliverIllustrationScene>): OliverVisualState {
        if (scenes.isEmpty()) return OliverVisualState()

        return OliverVisualState(
            characters = scenes.flatMap { it.visualState.characters }.distinct(),
            location = scenes.map { it.visualState.location }
                .firstOrNull { it != "Lieu non précisé" }
                ?: "Lieu non précisé",
            actions = scenes.flatMap { it.visualState.actions }.distinct(),
            mood = scenes.map { it.visualState.mood }
                .firstOrNull { it != "ambiance neutre" }
                ?: "ambiance neutre",
            objects = scenes.flatMap { it.visualState.objects }.distinct(),
            timeOfDay = scenes.map { it.visualState.timeOfDay }
                .firstOrNull { it != "non précisé" && it != "Non précisé" }
                ?: "non précisé",
            weather = scenes.map { it.visualState.weather }
                .firstOrNull { it != "non précisé" && it != "Non précisé" }
                ?: "non précisé",
            clothing = scenes.flatMap { it.visualState.clothing.entries }
                .associate { it.key to it.value },
            continuityNotes = scenes.flatMap { it.visualState.continuityNotes }.distinct()
        )
    }

    private fun inheritedClothing(
        characters: List<String>,
        previousScene: OliverIllustrationScene?
    ): Map<String, String> {
        val result = previousScene?.visualState?.clothing?.toMutableMap() ?: mutableMapOf()

        if ("Oliver" in characters && "Oliver" !in result) {
            result["Oliver"] = "simple worn Victorian orphan clothing"
        }
        if ("Oliver's mother" in characters && "Oliver's mother" !in result) {
            result["Oliver's mother"] = "simple modest nineteenth-century clothing"
        }

        return result
    }

    private fun buildContinuityNotes(
        characters: List<String>,
        previousScene: OliverIllustrationScene?
    ): List<String> {
        val notes = mutableListOf<String>()

        if (previousScene != null) {
            notes.add("Reprendre l'identité visuelle de la scène précédente.")
        }
        if ("Oliver" in characters) {
            notes.add("Conserver le même visage, âge apparent, coiffure et proportions d'Oliver.")
        }
        if ("Oliver's mother" in characters) {
            notes.add("Conserver la même identité visuelle de la mère d'Oliver.")
        }

        return notes.distinct()
    }

    /* =========================================================
       DESCRIPTION VISUELLE
       ========================================================= */

    private fun buildVisualDescription(
        sourceText: String,
        state: OliverVisualState,
        decision: ComparisonResult
    ): String {
        val charactersText = if (state.characters.isEmpty()) {
            "aucun personnage identifié avec certitude"
        } else {
            state.characters.joinToString(", ")
        }

        val objectsText = if (state.objects.isEmpty()) "aucun objet notable" else state.objects.joinToString(", ")

        return buildString {
            append("Décision visuelle : ")
            append(
                when (decision.mode) {
                    "REUSE_PREVIOUS" -> "réutiliser l'illustration précédente sans changement majeur"
                    "UPDATE_PREVIOUS" -> "reprendre l'illustration précédente et modifier les éléments nécessaires"
                    else -> "créer la première illustration de la séquence"
                }
            )
            append(". ")
            append("Personnages : ").append(charactersText).append(". ")
            append("Lieu : ").append(state.location).append(". ")
            append("Action(s) : ").append(if (state.actions.isEmpty()) "aucune action précise" else state.actions.joinToString(", ")).append(". ")
            append("Ambiance : ").append(state.mood).append(". ")
            append("Objets : ").append(objectsText).append(". ")
            append("Changements détectés : ")
            append(if (decision.changedElements.isEmpty()) "aucun" else decision.changedElements.joinToString(", ")).append(". ")
            append("Passage source : ").append(sourceText).append(". ")
            append("L'image doit rester fidèle au passage et à la continuité visuelle précédente.")
        }
    }

    /* =========================================================
       PROMPTS D'IMAGE
       ========================================================= */

    private fun buildNewPrompt(
        sourceText: String,
        state: OliverVisualState
    ): String = """
        Create one pedagogical literary illustration for Oliver Twist by Charles Dickens.

        SOURCE PASSAGE:
        $sourceText

        VISUAL STATE:
        Characters: ${if (state.characters.isEmpty()) "none reliably identified" else state.characters.joinToString(", ")}
        Location: ${state.location}
        Actions: ${if (state.actions.isEmpty()) "none" else state.actions.joinToString(", ")}
        Mood: ${state.mood}
        Objects: ${if (state.objects.isEmpty()) "none" else state.objects.joinToString(", ")}
        Time: ${state.timeOfDay}
        Weather: ${state.weather}

        STYLE:
        Semi-realistic historical graphic novel, Victorian England,
        educational, cinematic but clear, soft natural lighting,
        expressive faces, historically plausible clothing and architecture,
        no modern objects.

        CONTINUITY:
        ${buildCharacterContinuity(state.characters, state.clothing)}

        STRICT RULES:
        1. Represent only events supported by the source passage.
        2. Do not add unrelated characters.
        3. Do not invent a location when none is stated.
        4. Do not interpret "mortality" or "mortal" alone as a death scene.
        5. No captions, speech bubbles, subtitles, labels, logos, watermarks or written text.
    """.trimIndent()

    private fun buildUpdatePrompt(
        sourceText: String,
        state: OliverVisualState,
        previousScene: OliverIllustrationScene,
        decision: ComparisonResult
    ): String = """
        Create the next pedagogical illustration in a continuous illustrated edition of Oliver Twist by Charles Dickens.

        IMPORTANT CONTINUITY RULE:
        The previous illustration is the primary visual reference.
        Reuse it as the base composition and change ONLY the elements listed under
        CHANGES TO APPLY. Do not redesign the whole scene.

        PREVIOUS SCENE STATE:
        Characters: ${previousScene.visualState.characters.joinToString(", ").ifBlank { "none" }}
        Location: ${previousScene.visualState.location}
        Actions: ${previousScene.visualState.actions.joinToString(", ").ifBlank { "none" }}
        Mood: ${previousScene.visualState.mood}
        Objects: ${previousScene.visualState.objects.joinToString(", ").ifBlank { "none" }}
        Time: ${previousScene.visualState.timeOfDay}
        Weather: ${previousScene.visualState.weather}

        CURRENT PAGE SOURCE PASSAGE:
        $sourceText

        CURRENT SCENE STATE:
        Characters: ${state.characters.joinToString(", ").ifBlank { "none reliably identified" }}
        Location: ${state.location}
        Actions: ${state.actions.joinToString(", ").ifBlank { "none" }}
        Mood: ${state.mood}
        Objects: ${state.objects.joinToString(", ").ifBlank { "none" }}
        Time: ${state.timeOfDay}
        Weather: ${state.weather}

        CHANGES TO APPLY:
        ${if (decision.changedElements.isEmpty()) "NONE — keep the previous illustration essentially unchanged." else decision.changedElements.joinToString(", ")}

        SIMILARITY TARGET:
        ${decision.similarityScore}% visual continuity.

        CHARACTER CONTINUITY:
        ${buildCharacterContinuity(state.characters, state.clothing)}

        STYLE:
        Semi-realistic historical graphic novel, Victorian England,
        educational, consistent character design, consistent architecture,
        consistent lighting language, no modern objects.

        STRICT RULES:
        1. Never redesign a recurring character without textual justification.
        2. Never change clothing without textual justification.
        3. Never change the main location without textual justification.
        4. Preserve the previous composition whenever possible.
        5. Update only what the story development requires.
        6. Do not invent characters or events.
        7. No captions, speech bubbles, subtitles, labels, logos, watermarks or written text.
    """.trimIndent()

    private fun buildReusePrompt(
        sourceText: String,
        previousScene: OliverIllustrationScene,
        reason: String
    ): String = """
        REUSE THE PREVIOUS ILLUSTRATION AS THE VISUAL BASE.

        Oliver Twist by Charles Dickens.

        CURRENT SOURCE PASSAGE:
        $sourceText

        PREVIOUS VISUAL STATE:
        Characters: ${previousScene.visualState.characters.joinToString(", ").ifBlank { "none" }}
        Location: ${previousScene.visualState.location}
        Actions: ${previousScene.visualState.actions.joinToString(", ").ifBlank { "none" }}
        Mood: ${previousScene.visualState.mood}
        Objects: ${previousScene.visualState.objects.joinToString(", ").ifBlank { "none" }}

        DECISION:
        No significant visual change detected.
        Reason: $reason

        INSTRUCTION:
        Reuse the previous illustration almost exactly. Preserve character identity,
        face, age, hairstyle, clothing, architecture, objects, lighting and camera
        composition. Do not introduce new visual elements unless directly required
        by the source passage.

        No written text, captions, speech bubbles, labels, logos or watermarks.
    """.trimIndent()

    private fun buildCharacterContinuity(
        characters: List<String>,
        clothing: Map<String, String>
    ): String {
        if (characters.isEmpty()) {
            return "No named character has been identified reliably. Do not invent one."
        }

        val parts = mutableListOf<String>()

        if ("Oliver" in characters) {
            parts.add(
                """
                Oliver Twist: young Victorian orphan boy, slim build, pale face,
                expressive large eyes, short slightly tousled brown hair.
                Preserve the same facial identity, age, hairstyle and proportions.
                Clothing reference: ${clothing["Oliver"] ?: "simple worn Victorian orphan clothing"}.
                """.trimIndent()
            )
        }

        if ("Oliver's mother" in characters) {
            parts.add(
                """
                Oliver's mother: very young Victorian woman, pale and exhausted,
                simple modest nineteenth-century clothing. Preserve the same face,
                age appearance and clothing identity whenever she reappears.
                Clothing reference: ${clothing["Oliver's mother"] ?: "simple modest nineteenth-century clothing"}.
                """.trimIndent()
            )
        }

        return parts.joinToString("\n\n")
    }

    /* =========================================================
       FALLBACK
       ========================================================= */

    private fun createFallbackScene(
        index: Int,
        sourceText: String,
        reason: String,
        previousScene: OliverIllustrationScene?
    ): OliverIllustrationScene {
        if (previousScene != null) {
            return previousScene.copy(
                index = index,
                sourceText = sourceText,
                continuityMode = "REUSE_PREVIOUS",
                changedElements = emptyList(),
                similarityScore = 100,
                imagePrompt = buildReusePrompt(
                    sourceText = sourceText,
                    previousScene = previousScene,
                    reason = reason
                )
            )
        }

        val state = OliverVisualState()
        return OliverIllustrationScene(
            index = index,
            sourceText = sourceText,
            type = "scène narrative",
            characters = emptyList(),
            location = "Lieu non précisé",
            action = "situation générale",
            mood = "ambiance neutre",
            visualDescription = "Aucune scène narrative suffisamment précise n'a été détectée. Raison : $reason.",
            imagePrompt = "Do not generate a specific event illustration because the source is insufficiently specific.",
            visualState = state,
            continuityMode = "NEW_SCENE",
            changedElements = emptyList(),
            similarityScore = 0
        )
    }
}
