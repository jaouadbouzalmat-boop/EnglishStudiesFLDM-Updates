@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package ma.fldm.englishstudies

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

private val FPrimary = Color(0xFF2563EB)
private val FDark = Color(0xFF1D4ED8)
private val FLight = Color(0xFFEFF6FF)
private val FGreen = Color(0xFF16A34A)
private val FLightGreen = Color(0xFFDCFCE7)
private val FRed = Color(0xFFDC2626)
private val FLightRed = Color(0xFFFEE2E2)
private val FOrange = Color(0xFFF59E0B)
private val FLightOrange = Color(0xFFFEF3C7)
private val FText = Color(0xFF172033)
private val FGray = Color(0xFF64748B)
private val FBackground = Color(0xFFF8FAFC)

data class FrenchExercise(
    val question: String,
    val options: List<String>,
    val correct: Int,
    val explanation: String
)

data class FrenchLesson(
    val number: Int,
    val title: String,
    val subtitle: String,
    val objective: String,
    val concepts: List<Pair<String, String>>,
    val vocabulary: List<Pair<String, String>>,
    val exercises: List<FrenchExercise>
)

private val frenchLessons = listOf(
    FrenchLesson(
        1,
        "Communication universitaire",
        "Communiquer clairement dans les contextes universitaires.",
        "Identifier les principales situations de communication académique.",
        listOf(
            "Registre universitaire" to "Niveau de langue adapté aux études supérieures.",
            "Message formel" to "Communication structurée et respectueuse destinée à un enseignant ou une institution.",
            "Consigne" to "Instruction indiquant précisément ce qui doit être réalisé."
        ),
        listOf(
            "registre" to "niveau de langue",
            "consigne" to "instruction à suivre",
            "destinataire" to "personne qui reçoit le message"
        ),
        listOf(
            FrenchExercise("Quelle formulation convient le mieux à un courriel adressé à un enseignant ?", listOf("Salut prof, ça va ?", "Bonjour Madame, je vous écris au sujet du devoir demandé.", "Hey, j'ai une question.", "Tu peux regarder mon devoir ?"), 1, "La deuxième formulation respecte un registre formel et précise le motif du message."),
            FrenchExercise("Une consigne universitaire doit être : ", listOf("vague", "précise", "incomplète", "contradictoire"), 1, "Une consigne efficace indique clairement l'action attendue."),
            FrenchExercise("Le destinataire d’un courriel est : ", listOf("celui qui écrit", "celui qui reçoit", "le sujet du devoir", "le document joint"), 1, "Le destinataire est la personne à laquelle le message est adressé."),
            FrenchExercise("Quel élément est indispensable dans un courriel académique ?", listOf("Un surnom", "Un objet clair", "Des emojis uniquement", "Une phrase sans verbe"), 1, "L’objet permet d’identifier rapidement le sujet du message."),
            FrenchExercise("Quel verbe est adapté à une demande formelle ?", listOf("Donne-moi", "Je voudrais savoir", "File-moi", "Passe-moi"), 1, "« Je voudrais savoir » est une formulation polie et formelle."),
            FrenchExercise("Le registre universitaire privilégie : ", listOf("la précision", "l’argot", "les abréviations de messagerie", "les expressions familières"), 0, "Le registre universitaire recherche une expression précise et adaptée au contexte."),
            FrenchExercise("Une demande à un professeur doit généralement être : ", listOf("agressive", "courtoise", "ironique", "anonyme"), 1, "La courtoisie facilite une communication institutionnelle efficace."),
            FrenchExercise("Quel objet est le plus précis ?", listOf("Question", "Important", "Demande", "Question concernant le devoir de méthodologie – S1"), 3, "Un objet précis aide le destinataire à comprendre immédiatement le sujet."),
            FrenchExercise("Une consigne qui demande « Comparez les deux textes » exige de : ", listOf("résumer seulement le premier", "identifier ressemblances et différences", "copier les deux textes", "traduire uniquement les titres"), 1, "Comparer consiste notamment à relever les ressemblances et les différences."),
            FrenchExercise("Dans un message académique, il faut éviter surtout : ", listOf("la politesse", "la structure", "les informations utiles", "les formulations très familières"), 3, "Les formulations très familières ne correspondent généralement pas à un contexte académique formel.")
        )
    ),
    FrenchLesson(
        2,
        "Compréhension écrite en français",
        "Lire efficacement des textes universitaires en français.",
        "Repérer le thème, les idées principales et les informations importantes.",
        listOf(
            "Thème" to "Sujet général traité dans un texte.",
            "Idée principale" to "Information centrale développée par un paragraphe ou un texte.",
            "Indice contextuel" to "Élément du texte permettant d’interpréter un mot ou une idée."
        ),
        listOf(
            "thème" to "sujet général",
            "inférer" to "déduire à partir d'indices",
            "paragraphe" to "unité organisée autour d'une idée"
        ),
        listOf(
            FrenchExercise("Pour identifier rapidement le sujet d’un texte, on cherche d’abord : ", listOf("les détails secondaires", "le thème général", "les fautes", "la ponctuation uniquement"), 1, "Le thème donne une première représentation globale du contenu."),
            FrenchExercise("Une idée principale est : ", listOf("un détail", "le point central", "un exemple isolé", "un mot"), 1, "L’idée principale organise les informations essentielles."),
            FrenchExercise("Pour comprendre un mot inconnu, on peut utiliser : ", listOf("le contexte", "uniquement un dictionnaire bilingue", "le nombre de lettres", "la police"), 0, "Le contexte fournit des indices sémantiques."),
            FrenchExercise("Lire le titre sert notamment à : ", listOf("prévoir le sujet", "ignorer le texte", "remplacer la lecture", "corriger la grammaire"), 0, "Le titre aide à anticiper le contenu et à formuler des hypothèses."),
            FrenchExercise("Quel élément est souvent un indice de l’organisation du texte ?", listOf("les connecteurs", "la couleur de l’écran", "le mot de passe", "le clavier"), 0, "Les connecteurs indiquent les relations entre les idées."),
            FrenchExercise("Une conclusion de paragraphe peut souvent être repérée par : ", listOf("un retour à l’idée centrale", "une liste de mots au hasard", "un changement de police obligatoire", "l’absence de verbe"), 0, "La fin d’un paragraphe peut synthétiser ou reformuler l’idée développée."),
            FrenchExercise("Inférer signifie : ", listOf("copier", "déduire", "traduire mot à mot", "supprimer"), 1, "Inférer consiste à tirer une conclusion à partir d’indices."),
            FrenchExercise("Pour vérifier une information précise dans un texte, on peut : ", listOf("relire la zone concernée", "ignorer le texte", "inventer la réponse", "lire uniquement le titre"), 0, "Une relecture ciblée permet de vérifier précisément l'information."),
            FrenchExercise("Une lecture efficace alterne souvent : ", listOf("vue d’ensemble et lecture détaillée", "copie et oubli", "traduction automatique uniquement", "mémorisation sans compréhension"), 0, "Une lecture stratégique combine compréhension globale et analyse détaillée."),
            FrenchExercise("Un bon résumé de lecture conserve surtout : ", listOf("les idées essentielles", "tous les exemples", "chaque phrase", "les opinions hors sujet"), 0, "Le résumé sélectionne l’essentiel.")
        )
    ),
    FrenchLesson(
        3,
        "Les classes grammaticales",
        "Reconnaître les principales catégories grammaticales.",
        "Identifier les classes de mots et leur rôle de base.",
        listOf(
            "Nom" to "Mot désignant une personne, un lieu, un objet, une notion ou une idée.",
            "Verbe" to "Mot qui exprime généralement une action, un état ou un processus.",
            "Adjectif" to "Mot qui caractérise ou précise un nom."
        ),
        listOf(
            "nom" to "désigne une entité ou une notion",
            "verbe" to "exprime action ou état",
            "adjectif" to "qualifie un nom"
        ),
        listOf(
            FrenchExercise("Dans « La recherche avance rapidement », « recherche » est : ", listOf("un nom", "un verbe", "un adverbe", "une préposition"), 0, "« recherche » désigne une notion : c’est un nom."),
            FrenchExercise("Dans « une lecture attentive », « attentive » est : ", listOf("un nom", "un adjectif", "un pronom", "un verbe"), 1, "« attentive » caractérise le nom « lecture »."),
            FrenchExercise("Dans « Les étudiants analysent le texte », « analysent » est : ", listOf("un adjectif", "un nom", "un verbe", "un article"), 2, "« analysent » exprime une action."),
            FrenchExercise("Dans « très clairement », « très » est : ", listOf("un adverbe", "un nom", "un verbe", "un déterminant"), 0, "« très » modifie l’adverbe « clairement »."),
            FrenchExercise("Dans « le livre », « le » est : ", listOf("un article/déterminant", "un nom", "un verbe", "un adjectif"), 0, "« le » détermine le nom « livre »."),
            FrenchExercise("Dans « nous », la classe grammaticale est : ", listOf("pronom", "nom", "préposition", "adjectif"), 0, "« nous » est un pronom personnel."),
            FrenchExercise("Dans « avec soin », « avec » est : ", listOf("une préposition", "un adjectif", "un nom", "un pronom"), 0, "« avec » introduit un complément."),
            FrenchExercise("Dans « mais pourtant », « mais » est : ", listOf("une conjonction", "un nom", "un article", "un verbe"), 0, "« mais » relie des éléments en exprimant une opposition."),
            FrenchExercise("Dans « ce texte », « ce » est : ", listOf("un déterminant", "un verbe", "un adverbe", "un nom"), 0, "« ce » accompagne le nom « texte »."),
            FrenchExercise("Identifier correctement les classes grammaticales aide à : ", listOf("comprendre la structure des phrases", "supprimer la ponctuation", "éviter toute lecture", "remplacer le vocabulaire"), 0, "L’analyse grammaticale facilite la compréhension de la structure syntaxique.")
        )
    ),
    FrenchLesson(
        4,
        "Accord dans le groupe nominal",
        "Maîtriser les accords de genre et de nombre.",
        "Appliquer correctement les règles d’accord dans le groupe nominal.",
        listOf(
            "Genre" to "Distinction grammaticale entre masculin et féminin.",
            "Nombre" to "Distinction entre singulier et pluriel.",
            "Accord" to "Adaptation grammaticale d’un mot à un autre."
        ),
        listOf(
            "singulier" to "un seul élément",
            "pluriel" to "plusieurs éléments",
            "féminin" to "genre grammatical"
        ),
        listOf(
            FrenchExercise("Quelle forme est correcte ?", listOf("des recherche universitaire", "des recherches universitaires", "des recherches universitaire", "des recherche universitaires"), 1, "Le nom et l’adjectif prennent ici le pluriel."),
            FrenchExercise("Quelle forme est correcte ?", listOf("une analyse précise", "une analyse précis", "un analyse précise", "une analyses précise"), 0, "« analyse » est féminin singulier ; « précise » s’accorde avec lui."),
            FrenchExercise("Quelle forme est correcte ?", listOf("les méthode efficaces", "les méthodes efficace", "les méthodes efficaces", "les méthode efficace"), 2, "Le nom et l’adjectif sont au féminin pluriel."),
            FrenchExercise("Dans « des articles scientifiques », l’adjectif s’accorde avec : ", listOf("articles", "des", "scientifiques", "aucun mot"), 0, "« scientifiques » caractérise « articles » et s’accorde avec lui."),
            FrenchExercise("Quelle phrase est correcte ?", listOf("Ces idée sont intéressante.", "Ces idées sont intéressantes.", "Cette idées sont intéressante.", "Ces idées est intéressantes."), 1, "Le déterminant, le nom, le verbe et l’adjectif sont cohérents."),
            FrenchExercise("« une expérience » devient au pluriel : ", listOf("des expérience", "des expériences", "une expériences", "des expériencess"), 1, "Le nom prend la marque régulière du pluriel."),
            FrenchExercise("« un étudiant motivé » au féminin devient : ", listOf("une étudiante motivée", "une étudiant motivé", "un étudiante motivée", "une étudiante motivé"), 0, "Le nom et l’adjectif s’accordent au féminin."),
            FrenchExercise("Le mot « universitaires » est au : ", listOf("masculin singulier", "féminin singulier", "pluriel", "infinitif"), 2, "La terminaison indique ici le pluriel."),
            FrenchExercise("Quelle expression est correcte ?", listOf("les langue française", "la langues françaises", "les langues françaises", "les langues française"), 2, "Le déterminant, le nom et l’adjectif sont au pluriel."),
            FrenchExercise("L’accord permet notamment de : ", listOf("marquer les relations grammaticales", "supprimer les verbes", "éviter les noms", "remplacer la ponctuation"), 0, "Les accords rendent visibles les relations grammaticales entre les mots.")
        )
    ),
    FrenchLesson(
        5,
        "Conjugaison et temps verbaux",
        "Employer les principaux temps dans des phrases universitaires.",
        "Choisir une forme verbale adaptée au contexte.",
        listOf(
            "Présent" to "Temps souvent utilisé pour les faits, définitions et habitudes.",
            "Passé composé" to "Temps courant pour une action achevée dans le passé.",
            "Futur" to "Temps employé pour une action ou un fait à venir."
        ),
        listOf(
            "décrire" to "présenter les caractéristiques",
            "analyser" to "examiner en détail",
            "observer" to "regarder attentivement"
        ),
        listOf(
            FrenchExercise("« Les étudiants … un article chaque semaine. »", listOf("lit", "lisent", "lisez", "lire"), 1, "Le sujet pluriel « les étudiants » exige « lisent »."),
            FrenchExercise("« Hier, nous … le texte. »", listOf("analysons", "avons analysé", "analyserons", "analyse"), 1, "« Hier » situe l’action dans le passé ; le passé composé convient."),
            FrenchExercise("« Demain, elle … son exposé. »", listOf("présente", "a présenté", "présentera", "présenter"), 2, "« Demain » appelle ici le futur simple."),
            FrenchExercise("« L’article … une question importante. »", listOf("pose", "posent", "poseront", "posé"), 0, "Le sujet singulier « l’article » prend « pose »."),
            FrenchExercise("« Nous … cette méthode depuis septembre. »", listOf("utilisons", "utilisions", "utiliserons", "utilisé"), 0, "Le présent convient pour une situation commencée et toujours actuelle dans cette phrase."),
            FrenchExercise("« Ils … leurs notes avant l’examen. »", listOf("réviseront", "révisaient", "révise", "réviser"), 0, "La forme « réviseront » exprime une action future."),
            FrenchExercise("« Elle … son devoir hier soir. »", listOf("termine", "terminera", "a terminé", "terminer"), 2, "« hier soir » marque une action achevée dans le passé."),
            FrenchExercise("Une définition générale est souvent formulée au : ", listOf("présent", "conditionnel passé", "plus-que-parfait uniquement", "futur antérieur"), 0, "Le présent est courant pour les définitions et généralisations."),
            FrenchExercise("« Quand il lit, il … des notes. »", listOf("prend", "prennent", "prendra", "pris"), 0, "Le sujet « il » exige « prend » au présent."),
            FrenchExercise("Un emploi correct des temps verbaux améliore : ", listOf("la précision temporelle", "la couleur du texte", "le nombre de pages", "la taille des titres"), 0, "Les temps permettent de situer clairement les actions et les idées.")
        )
    ),
    FrenchLesson(
        6,
        "Syntaxe et construction de la phrase",
        "Construire des phrases françaises correctes et claires.",
        "Identifier les constituants essentiels d’une phrase.",
        listOf(
            "Sujet" to "Élément qui réalise l’action ou dont on parle.",
            "Verbe" to "Élément central exprimant action, état ou processus.",
            "Complément" to "Élément qui apporte une information supplémentaire au verbe, au nom ou à la phrase."
        ),
        listOf(
            "sujet" to "élément dont on parle ou qui agit",
            "verbe" to "noyau verbal",
            "complément" to "information ajoutée"
        ),
        listOf(
            FrenchExercise("Dans « Les étudiants rédigent un essai », le sujet est : ", listOf("Les étudiants", "rédigent", "un essai", "aucun"), 0, "« Les étudiants » réalisent l’action de rédiger."),
            FrenchExercise("Dans « Marie consulte la bibliothèque », le verbe est : ", listOf("Marie", "consulte", "la bibliothèque", "la"), 1, "« consulte » exprime l’action."),
            FrenchExercise("Quelle phrase est correctement construite ?", listOf("Les étudiants un texte lisent.", "Lis le étudiants texte.", "Les étudiants lisent un texte.", "Un texte les étudiants lisent correctement jamais."), 2, "La phrase suit l’ordre syntaxique attendu en français."),
            FrenchExercise("Le complément direct répond souvent à : ", listOf("qui ? / quoi ?", "quand ? uniquement", "où ? uniquement", "comment ? uniquement"), 0, "Le complément d’objet direct peut répondre à « qui ? » ou « quoi ? » après le verbe."),
            FrenchExercise("« À l’université, les étudiants travaillent. » Le groupe « À l’université » indique principalement : ", listOf("le lieu", "le sujet", "le verbe", "le genre"), 0, "Il précise le lieu de l’action."),
            FrenchExercise("Quelle phrase évite une rupture syntaxique ?", listOf("Parce que le texte, difficile.", "Les étudiants, bien que fatigués, poursuivent leur travail.", "Les étudiants poursuivre le travail.", "Texte lire université."), 1, "La deuxième phrase est complète et grammaticalement structurée."),
            FrenchExercise("Le noyau d’un groupe verbal est généralement : ", listOf("le verbe", "l’article", "le déterminant", "la ponctuation"), 0, "Le verbe constitue le noyau du groupe verbal."),
            FrenchExercise("Une phrase claire doit surtout avoir : ", listOf("une organisation logique", "des mots au hasard", "uniquement des phrases longues", "aucun connecteur"), 0, "Une organisation logique améliore la compréhension."),
            FrenchExercise("Dans « Le professeur explique la méthode aux étudiants », « la méthode » est : ", listOf("un complément d’objet direct", "le sujet", "un adverbe", "une préposition"), 0, "Le verbe « explique » porte directement sur « la méthode »."),
            FrenchExercise("Travailler la syntaxe permet de : ", listOf("réduire les ambiguïtés", "supprimer le vocabulaire", "éviter toute révision", "remplacer la lecture"), 0, "Une syntaxe maîtrisée rend les relations entre les éléments plus claires.")
        )
    ),
    FrenchLesson(
        7,
        "Connecteurs logiques",
        "Organiser les relations entre les idées.",
        "Choisir un connecteur cohérent avec le raisonnement.",
        listOf(
            "Addition" to "Ajout d’une idée : de plus, en outre, également.",
            "Opposition" to "Contraste entre des idées : cependant, pourtant, en revanche.",
            "Cause" to "Relation expliquant pourquoi : parce que, puisque, en raison de."
        ),
        listOf(
            "cependant" to "marque l'opposition",
            "donc" to "marque une conséquence",
            "en outre" to "ajoute une information"
        ),
        listOf(
            FrenchExercise("« Le texte est long. …, il reste facile à comprendre. »", listOf("Cependant", "Donc", "Parce que", "Ainsi"), 0, "« Cependant » introduit une opposition."),
            FrenchExercise("« Il a beaucoup travaillé ; …, il a réussi. »", listOf("par conséquent", "cependant", "mais", "quoique"), 0, "« par conséquent » introduit une conséquence."),
            FrenchExercise("« … ses recherches, il a trouvé une réponse. »", listOf("Grâce à", "Cependant", "En revanche", "Mais"), 0, "« Grâce à » introduit une cause favorable."),
            FrenchExercise("Quel connecteur indique l’addition ?", listOf("en outre", "cependant", "donc", "pourtant"), 0, "« en outre » ajoute une information."),
            FrenchExercise("Quel connecteur indique l’opposition ?", listOf("toutefois", "ainsi", "donc", "parce que"), 0, "« toutefois » signale un contraste."),
            FrenchExercise("Quel connecteur indique la conséquence ?", listOf("donc", "mais", "car", "quoique"), 0, "« donc » indique une conséquence logique."),
            FrenchExercise("« Le texte présente des limites ; …, il apporte des résultats utiles. »", listOf("néanmoins", "donc", "parce que", "afin de"), 0, "« néanmoins » marque une concession/opposition."),
            FrenchExercise("« Il révise régulièrement … il souhaite progresser. »", listOf("parce qu’", "cependant", "ainsi", "en revanche"), 0, "« parce que » introduit la cause."),
            FrenchExercise("Les connecteurs servent surtout à : ", listOf("rendre les relations entre idées explicites", "allonger les phrases", "remplacer tous les verbes", "supprimer les paragraphes"), 0, "Ils structurent le raisonnement et améliorent la cohésion."),
            FrenchExercise("Un texte sans connecteurs peut être plus difficile à : ", listOf("suivre logiquement", "lire uniquement à voix haute", "imprimer", "copier"), 0, "Les connecteurs aident le lecteur à suivre la logique.")
        )
    ),
    FrenchLesson(
        8,
        "Vocabulaire académique",
        "Développer un lexique français précis pour les études.",
        "Comprendre et employer les verbes et noms académiques fréquents.",
        listOf(
            "Analyser" to "Examiner un objet ou un texte de manière détaillée.",
            "Comparer" to "Mettre en relation deux éléments pour relever ressemblances et différences.",
            "Évaluer" to "Porter un jugement fondé sur des critères ou des éléments pertinents."
        ),
        listOf(
            "enjeu" to "question importante soulevée par un sujet",
            "donnée" to "information recueillie",
            "argument" to "raison servant à soutenir une idée"
        ),
        listOf(
            FrenchExercise("Quel verbe signifie « examiner en détail » ?", listOf("analyser", "raconter", "saluer", "copier"), 0, "Analyser signifie examiner un objet, un texte ou un phénomène en détail."),
            FrenchExercise("Comparer consiste à : ", listOf("relever ressemblances et différences", "résumer un seul document", "donner une définition seulement", "supprimer les exemples"), 0, "La comparaison met deux ou plusieurs éléments en relation."),
            FrenchExercise("Évaluer signifie : ", listOf("juger à partir de critères", "copier une source", "lire sans comprendre", "traduire mot à mot"), 0, "Évaluer implique un jugement raisonné fondé sur des critères."),
            FrenchExercise("Un argument est : ", listOf("une raison qui soutient une idée", "un titre", "un article grammatical", "un signe de ponctuation"), 0, "Un argument sert à justifier ou soutenir une position."),
            FrenchExercise("Une donnée est : ", listOf("une information recueillie", "une conclusion obligatoire", "un verbe", "une salutation"), 0, "Une donnée est une information utilisée dans l’analyse."),
            FrenchExercise("Un enjeu est : ", listOf("une question importante liée au sujet", "une erreur typographique", "une virgule", "une traduction automatique"), 0, "L’enjeu correspond à ce qui est important ou problématique dans une situation."),
            FrenchExercise("Quel terme convient à « éléments utilisés pour soutenir une affirmation » ?", listOf("arguments", "salutations", "articles", "pronoms"), 0, "Les arguments servent à soutenir une affirmation."),
            FrenchExercise("Le verbe « démontrer » implique généralement : ", listOf("apporter des éléments pour établir quelque chose", "saluer un lecteur", "résumer sans preuve", "inventer"), 0, "Démontrer suppose de présenter des éléments qui permettent d’établir une proposition."),
            FrenchExercise("Une formulation académique privilégie : ", listOf("la précision lexicale", "l’argot", "les mots vagues", "les répétitions inutiles"), 0, "Un lexique précis améliore la qualité de l’expression universitaire."),
            FrenchExercise("Enrichir son vocabulaire permet notamment : ", listOf("d’exprimer les idées avec plus de précision", "d’éviter toutes les lectures", "de supprimer la grammaire", "de réduire les connaissances"), 0, "Un lexique plus riche offre davantage de choix pour formuler les idées.")
        )
    ),
    FrenchLesson(
        9,
        "Rédaction universitaire en français",
        "Produire des paragraphes français structurés.",
        "Organiser une idée, l’expliquer et l’illustrer de manière cohérente.",
        listOf(
            "Introduction" to "Partie qui présente le sujet et oriente le lecteur.",
            "Développement" to "Partie où les idées sont expliquées, argumentées et illustrées.",
            "Conclusion" to "Partie finale qui synthétise l’essentiel et ouvre éventuellement sur une perspective."
        ),
        listOf(
            "cohérence" to "logique globale des idées",
            "cohésion" to "liaison entre les éléments du texte",
            "transition" to "passage organisé d’une idée à une autre"
        ),
        listOf(
            FrenchExercise("Dans un paragraphe argumentatif, la première phrase peut souvent présenter : ", listOf("l’idée directrice", "la bibliographie entière", "une conclusion sans sujet", "une liste aléatoire"), 0, "L’idée directrice annonce le point principal du paragraphe."),
            FrenchExercise("La cohérence concerne surtout : ", listOf("la logique des idées", "la taille de la police", "le nombre de pages", "la couleur"), 0, "La cohérence concerne l’enchaînement logique du contenu."),
            FrenchExercise("La cohésion est renforcée par : ", listOf("les connecteurs et reprises", "les répétitions aléatoires", "l’absence de ponctuation", "les phrases sans relation"), 0, "Les outils de cohésion relient les différentes parties du texte."),
            FrenchExercise("Une transition sert à : ", listOf("relier deux idées ou parties", "supprimer une idée", "remplacer un titre", "éviter la conclusion"), 0, "La transition accompagne le passage d’une idée à une autre."),
            FrenchExercise("Une conclusion doit principalement : ", listOf("synthétiser l’essentiel", "introduire dix nouveaux sujets", "copier l’introduction mot pour mot", "supprimer l’argumentation"), 0, "La conclusion ferme la réflexion en reprenant l’essentiel."),
            FrenchExercise("Quel élément améliore un paragraphe académique ?", listOf("un exemple pertinent", "des phrases sans lien", "des répétitions excessives", "des expressions très familières"), 0, "Un exemple pertinent aide à expliquer ou illustrer une idée."),
            FrenchExercise("Un paragraphe efficace développe généralement : ", listOf("une idée principale", "plusieurs idées sans rapport", "aucun sujet", "uniquement des citations"), 0, "L’unité thématique facilite la compréhension."),
            FrenchExercise("Réviser un texte consiste notamment à vérifier : ", listOf("le sens, la structure et la langue", "uniquement le nombre de mots", "uniquement le titre", "la couleur du papier"), 0, "La révision porte sur plusieurs dimensions du texte."),
            FrenchExercise("Une phrase de transition efficace doit : ", listOf("annoncer ou relier logiquement la suite", "changer de sujet sans avertir", "être forcément très longue", "ne contenir aucun verbe"), 0, "La transition guide le lecteur dans le raisonnement."),
            FrenchExercise("La rédaction universitaire exige généralement : ", listOf("clarté, précision et organisation", "argot et improvisation", "absence de structure", "phrases toujours très longues"), 0, "Ces qualités favorisent une communication académique claire.")
        )
    ),
    FrenchLesson(
        10,
        "Révision et correction",
        "Relire méthodiquement un texte en français.",
        "Repérer et corriger les erreurs de grammaire, orthographe et formulation.",
        listOf(
            "Révision" to "Processus d’amélioration d’un brouillon ou d’un texte.",
            "Relecture" to "Lecture attentive destinée à vérifier le contenu et la forme.",
            "Correction" to "Modification des erreurs ou formulations inadéquates."
        ),
        listOf(
            "orthographe" to "écriture correcte des mots",
            "ponctuation" to "signes organisant la phrase",
            "reformuler" to "exprimer autrement une idée"
        ),
        listOf(
            FrenchExercise("Quelle phrase est correcte ?", listOf("Les étudiants travail beaucoup.", "Les étudiants travaillent beaucoup.", "Les étudiants travaille beaucoup.", "Les étudiant travaillent beaucoup."), 1, "Le sujet pluriel « les étudiants » exige le verbe « travaillent »."),
            FrenchExercise("Quelle phrase est correcte ?", listOf("Cette analyse sont pertinente.", "Cette analyse est pertinente.", "Cette analyse est pertinents.", "Cette analyses est pertinente."), 1, "Le sujet féminin singulier commande « est pertinente »."),
            FrenchExercise("Quel mot est correctement écrit ?", listOf("universitté", "université", "univercité", "universitée"), 1, "« université » est l’orthographe correcte."),
            FrenchExercise("La ponctuation sert notamment à : ", listOf("structurer la lecture", "remplacer la grammaire", "supprimer les paragraphes", "allonger automatiquement le texte"), 0, "La ponctuation organise les unités de sens."),
            FrenchExercise("Avant de rendre un devoir, il est utile de : ", listOf("relire et corriger", "ne jamais relire", "supprimer les sources", "ignorer les consignes"), 0, "Une relecture finale permet de repérer des erreurs et incohérences."),
            FrenchExercise("Reformuler signifie : ", listOf("exprimer autrement", "copier exactement", "supprimer le sens", "traduire chaque mot"), 0, "Reformuler consiste à produire une autre formulation en conservant l’idée."),
            FrenchExercise("Quel élément faut-il vérifier dans une phrase ?", listOf("accords", "couleur de fond uniquement", "taille du fichier uniquement", "nom du clavier"), 0, "Les accords sont une composante importante de la correction grammaticale."),
            FrenchExercise("Une relecture efficace peut se faire : ", listOf("à plusieurs niveaux", "uniquement après l’impression", "sans lire le texte", "seulement sur le titre"), 0, "On peut relire séparément le contenu, la langue et la présentation."),
            FrenchExercise("Une erreur de ponctuation peut : ", listOf("affecter le sens ou la lisibilité", "améliorer automatiquement l’argument", "remplacer un paragraphe", "rendre le sujet pluriel"), 0, "La ponctuation contribue à la structure et à l’interprétation."),
            FrenchExercise("La dernière vérification d’un devoir devrait inclure : ", listOf("consignes, structure, langue et références", "uniquement la page de garde", "uniquement le nombre de mots", "aucune source"), 0, "Une vérification complète réduit les erreurs avant la remise.")
        )
    ),
    FrenchLesson(
        11,
        "Prise de parole en contexte universitaire",
        "S’exprimer clairement dans une situation académique.",
        "Préparer une courte intervention orale structurée en français.",
        listOf(
            "Exposé" to "Présentation orale organisée sur un sujet.",
            "Prise de parole" to "Action de s’exprimer devant un interlocuteur ou un groupe.",
            "Interaction" to "Échange entre plusieurs participants."
        ),
        listOf(
            "introduction" to "début qui présente le sujet",
            "transition" to "lien entre deux parties",
            "conclusion" to "fin qui synthétise"
        ),
        listOf(
            FrenchExercise("Un exposé universitaire doit généralement avoir : ", listOf("une structure claire", "aucun plan", "uniquement des anecdotes", "des phrases inachevées"), 0, "Une organisation claire aide le public à suivre l’exposé."),
            FrenchExercise("Au début d’un exposé, il est utile de : ", listOf("annoncer le sujet", "cacher le sujet", "lire sans introduction", "commencer par la conclusion"), 0, "L’annonce du sujet oriente l’auditoire."),
            FrenchExercise("Une transition orale sert à : ", listOf("passer clairement à une nouvelle idée", "arrêter l’exposé", "supprimer le sujet", "éviter les explications"), 0, "Elle signale l’évolution du raisonnement."),
            FrenchExercise("Pour être compréhensible à l’oral, on peut : ", listOf("articuler et faire des pauses", "parler sans respirer", "lire très vite", "éviter la ponctuation orale"), 0, "L’articulation et les pauses favorisent la compréhension."),
            FrenchExercise("Quel comportement est adapté pendant les questions ?", listOf("écouter puis répondre précisément", "interrompre systématiquement", "ignorer la question", "changer de sujet sans réponse"), 0, "L’écoute active et une réponse ciblée favorisent l’interaction."),
            FrenchExercise("Un support visuel doit : ", listOf("aider à comprendre", "contenir tout le discours", "remplacer le locuteur", "être illisible"), 0, "Un support efficace complète la présentation sans la remplacer."),
            FrenchExercise("Une conclusion orale peut : ", listOf("rappeler les points essentiels", "introduire un sujet totalement nouveau", "supprimer les idées principales", "ignorer la question étudiée"), 0, "La conclusion rappelle et synthétise l’essentiel."),
            FrenchExercise("Le registre d’un exposé universitaire est plutôt : ", listOf("formel et précis", "très familier", "argotique", "incomplet"), 0, "Le contexte universitaire appelle une expression adaptée et précise."),
            FrenchExercise("Une bonne prise de parole repose notamment sur : ", listOf("préparation et clarté", "improvisation totale", "absence de plan", "lecture monotone obligatoire"), 0, "La préparation facilite la maîtrise du contenu."),
            FrenchExercise("Après un exposé, il est utile de : ", listOf("évaluer ce qui a été clair et ce qui peut être amélioré", "ne jamais réfléchir à la performance", "supprimer les notes", "éviter tout retour"), 0, "L’autoévaluation permet de progresser.")
        )
    ),
    FrenchLesson(
        12,
        "Traduction et reformulation",
        "Passer d’une formulation à une autre sans perdre le sens.",
        "Développer une première compétence de traduction et de reformulation académique.",
        listOf(
            "Traduction" to "Transfert d’un message d’une langue vers une autre en préservant le sens.",
            "Reformulation" to "Nouvelle formulation d’une idée avec une structure différente.",
            "Fidélité" to "Respect du sens et des informations essentielles du message de départ."
        ),
        listOf(
            "traduire" to "transférer un message dans une autre langue",
            "reformuler" to "exprimer une même idée autrement",
            "fidèle" to "qui respecte le sens original"
        ),
        listOf(
            FrenchExercise("Une bonne traduction doit d’abord préserver : ", listOf("le sens", "la longueur exacte", "la ponctuation uniquement", "la police"), 0, "La priorité est de préserver le sens et les informations essentielles."),
            FrenchExercise("Reformuler une phrase signifie : ", listOf("la copier", "l’exprimer autrement", "la supprimer", "changer uniquement un article"), 1, "Une reformulation modifie la structure et le vocabulaire tout en conservant l’idée."),
            FrenchExercise("Face à un mot inconnu en traduction, il est utile de considérer : ", listOf("le contexte", "uniquement sa longueur", "la couleur", "le numéro de page"), 0, "Le contexte aide à choisir le sens pertinent."),
            FrenchExercise("La fidélité en traduction signifie : ", listOf("respecter le message source", "traduire mécaniquement chaque mot", "ajouter des idées", "supprimer les informations"), 0, "Une traduction fidèle respecte le contenu et le sens du message source."),
            FrenchExercise("« Les étudiants doivent lire davantage » peut être reformulé par : ", listOf("Les étudiants sont encouragés à augmenter leur lecture.", "Les étudiants ne lisent jamais.", "Les étudiants ont terminé leurs études.", "Lire est impossible."), 0, "La première phrase conserve le sens général tout en changeant la formulation."),
            FrenchExercise("La traduction mot à mot peut être problématique parce qu’elle : ", listOf("ignore parfois le contexte et les expressions", "est toujours parfaite", "supprime la grammaire", "n’utilise jamais de dictionnaire"), 0, "Les langues ne correspondent pas toujours mot pour mot ; le contexte est essentiel."),
            FrenchExercise("Pour vérifier une traduction, on peut : ", listOf("relire le texte cible et le comparer au sens source", "changer tous les mots", "supprimer le texte source", "éviter toute relecture"), 0, "La comparaison permet de vérifier la fidélité et la cohérence."),
            FrenchExercise("Une reformulation académique doit rester : ", listOf("claire et fidèle", "plus vague", "plus familière", "sans rapport avec le texte initial"), 0, "Elle doit modifier la forme tout en conservant l’idée."),
            FrenchExercise("Quel élément doit rester stable dans une reformulation ?", listOf("l’idée principale", "chaque mot", "la longueur exacte", "la ponctuation exacte"), 0, "La reformulation change la forme mais conserve l’idée."),
            FrenchExercise("Développer la traduction peut aider l’étudiant à améliorer : ", listOf("la compréhension et la précision linguistique", "uniquement l’écriture manuscrite", "la taille des fichiers", "la couleur des notes"), 0, "La traduction exige compréhension, choix lexicaux et contrôle grammatical.")
        )
    )
)


@Composable
fun FrenchScreen(
    onBack: () -> Unit = {}
) {
    var openLesson by remember { mutableStateOf<Int?>(null) }

    if (openLesson != null) {
        FrenchLessonScreen(
            lesson = frenchLessons[openLesson!!],
            onBack = { openLesson = null }
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("French", color = Color.White, fontWeight = FontWeight.Bold)
                        Text(
                            "English Studies • Semester 1",
                            color = Color.White.copy(alpha = .85f),
                            fontSize = 12.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = FPrimary)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(FBackground)
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = FPrimary)
                ) {
                    Column(Modifier.padding(22.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(
                                        Color.White.copy(alpha = .15f),
                                        RoundedCornerShape(16.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.School,
                                    null,
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Spacer(Modifier.width(14.dp))
                            Column {
                                Text(
                                    "French",
                                    color = Color.White,
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "Academic communication, grammar and writing.",
                                    color = Color.White.copy(alpha = .9f),
                                    fontSize = 14.sp
                                )
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "12 lessons • 120 corrected exercises",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(17.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Text(
                            "Learning objectives",
                            color = FPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        listOf(
                            "Communiquer dans un contexte universitaire.",
                            "Comprendre des textes et consignes en français.",
                            "Maîtriser les bases grammaticales utiles à l’écrit.",
                            "Construire des phrases et paragraphes clairs.",
                            "Développer un vocabulaire académique précis.",
                            "Réviser et corriger ses productions."
                        ).forEach {
                            Row(
                                Modifier.padding(vertical = 4.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    null,
                                    tint = FPrimary,
                                    modifier = Modifier.size(19.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(it, color = FText, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
            item {
                Text(
                    "Lessons",
                    color = FText,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            items(frenchLessons) { lesson ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { openLesson = lesson.number - 1 },
                    shape = RoundedCornerShape(17.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .background(FLight, RoundedCornerShape(14.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "%02d".format(lesson.number),
                                color = FPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(Modifier.width(13.dp))
                        Column(Modifier.weight(1f)) {
                            Text(
                                "LESSON ${lesson.number}",
                                color = FPrimary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                lesson.title,
                                color = FText,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                lesson.subtitle,
                                color = FGray,
                                fontSize = 13.sp
                            )
                        }
                        Icon(Icons.Default.PlayArrow, null, tint = FPrimary)
                    }
                }
            }
        }
    }
}

@Composable
private fun FrenchLessonScreen(
    lesson: FrenchLesson,
    onBack: () -> Unit
) {
    var section by remember { mutableIntStateOf(0) }
    val tabs = listOf("Learn", "Vocabulary", "Exercises", "Read Aloud", "Review")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Lesson ${lesson.number}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            lesson.title,
                            color = Color.White.copy(alpha = .85f),
                            fontSize = 12.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = FPrimary)
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .background(FBackground)
                .padding(padding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(7.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                tabs.forEachIndexed { index, title ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                if (section == index) FPrimary else Color(0xFFF1F5F9),
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { section = index }
                            .padding(vertical = 9.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            title,
                            color = if (section == index) Color.White else FGray,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            when (section) {
                0 -> FrenchLearn(lesson)
                1 -> FrenchVocabulary(lesson)
                2 -> FrenchExercises(lesson)
                3 -> FrenchReadAloud(lesson)
                4 -> FrenchReview(lesson)
            }
        }
    }
}

@Composable
private fun FrenchLearn(lesson: FrenchLesson) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        item {
            Text(
                lesson.title,
                color = FText,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            )
            Text(lesson.subtitle, color = FGray)
        }
        item {
            Card(
                Modifier.fillMaxWidth(),
                RoundedCornerShape(17.dp),
                colors = CardDefaults.cardColors(containerColor = FLight)
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text(
                        "Learning objective",
                        color = FPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(7.dp))
                    Text(lesson.objective, color = FText, fontSize = 15.sp)
                }
            }
        }
        item {
            Text(
                "Key concepts",
                color = FText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        items(lesson.concepts) { pair ->
            Card(
                Modifier.fillMaxWidth(),
                RoundedCornerShape(15.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(Modifier.padding(17.dp)) {
                    Text(
                        pair.first,
                        color = FPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(5.dp))
                    Text(
                        pair.second,
                        color = FText,
                        fontSize = 14.sp,
                        lineHeight = 21.sp
                    )
                }
            }
        }
        item {
            Card(
                Modifier.fillMaxWidth(),
                RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FLightOrange)
            ) {
                Column(Modifier.padding(17.dp)) {
                    Text(
                        "Academic application",
                        color = FOrange,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(7.dp))
                    Text(
                        "Après cette leçon, passez aux exercices et vérifiez chaque correction avant de continuer.",
                        color = FText,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun FrenchVocabulary(lesson: FrenchLesson) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(11.dp)
    ) {
        item {
            Text(
                "Academic Vocabulary",
                color = FText,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            )
            Text("Vocabulaire clé de la leçon.", color = FGray)
        }
        items(lesson.vocabulary) { pair ->
            Card(
                Modifier.fillMaxWidth(),
                RoundedCornerShape(15.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        Icons.Default.MenuBook,
                        null,
                        tint = FPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(
                            pair.first,
                            color = FPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            pair.second,
                            color = FText,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FrenchExercises(lesson: FrenchLesson) {
    var current by remember { mutableIntStateOf(0) }
    var selected by remember { mutableStateOf<Int?>(null) }
    var answered by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }

    val exercise = lesson.exercises[current]

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        item {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Exercise ${current + 1} / ${lesson.exercises.size}",
                    color = FText,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Score: $score",
                    color = FPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        item {
            LinearProgressIndicator(
                progress = { (current + 1).toFloat() / lesson.exercises.size.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = FPrimary,
                trackColor = Color(0xFFE2E8F0)
            )
        }
        item {
            Card(
                Modifier.fillMaxWidth(),
                RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(Modifier.padding(19.dp)) {
                    Text(
                        exercise.question,
                        color = FText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 25.sp
                    )
                    Spacer(Modifier.height(13.dp))
                    exercise.options.forEachIndexed { index, option ->
                        val isCorrect = index == exercise.correct
                        val isSelected = selected == index
                        val bg = when {
                            answered && isCorrect -> FLightGreen
                            answered && isSelected -> FLightRed
                            isSelected -> FLight
                            else -> Color(0xFFF8FAFC)
                        }

                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .background(bg, RoundedCornerShape(12.dp))
                                .clickable(enabled = !answered) {
                                    selected = index
                                    answered = true
                                    if (index == exercise.correct) score++
                                }
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "${('A'.code + index).toChar()}.",
                                color = FPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                option,
                                color = FText,
                                modifier = Modifier.weight(1f)
                            )
                            if (answered && isCorrect) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    null,
                                    tint = FGreen
                                )
                            }
                        }
                    }
                }
            }
        }

        if (answered) {
            item {
                val correct = selected == exercise.correct
                Card(
                    Modifier.fillMaxWidth(),
                    RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (correct) FLightGreen else FLightRed
                    )
                ) {
                    Column(Modifier.padding(17.dp)) {
                        Text(
                            if (correct) "Correct ✓" else "Incorrect",
                            color = if (correct) FGreen else FRed,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "Correct answer: ${exercise.options[exercise.correct]}",
                            color = FText,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(5.dp))
                        Text(
                            "Why? ${exercise.explanation}",
                            color = FText,
                            lineHeight = 21.sp
                        )
                    }
                }
            }
            item {
                Button(
                    onClick = {
                        if (current < lesson.exercises.lastIndex) {
                            current++
                            selected = null
                            answered = false
                        } else {
                            current = 0
                            selected = null
                            answered = false
                            score = 0
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FPrimary),
                    shape = RoundedCornerShape(13.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, null)
                    Spacer(Modifier.width(7.dp))
                    Text(
                        if (current < lesson.exercises.lastIndex) "Next Exercise" else "Restart Lesson"
                    )
                }
            }
        }
    }
}

@Composable
private fun FrenchReadAloud(lesson: FrenchLesson) {
    val context = LocalContext.current
    var ttsReady by remember { mutableStateOf(false) }

    val engine = remember {
        TextToSpeech(context) { status ->
            ttsReady = status == TextToSpeech.SUCCESS
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            engine.stop()
            engine.shutdown()
        }
    }

    val textToRead = buildString {
        append(lesson.title)
        append(". ")
        append(lesson.subtitle)
        append(". ")
        append(lesson.objective)
        append(". ")
        lesson.concepts.forEach {
            append(it.first)
            append(". ")
            append(it.second)
            append(". ")
        }
    }

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                "Read Aloud",
                color = FText,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Écoutez le contenu de la leçon en français.",
                color = FGray
            )
        }
        item {
            Card(
                Modifier.fillMaxWidth(),
                RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text(
                        textToRead,
                        color = FText,
                        fontSize = 15.sp,
                        lineHeight = 23.sp
                    )
                    Spacer(Modifier.height(14.dp))
                    Button(
                        onClick = {
                            if (ttsReady) {
                                engine.language = Locale.FRENCH
                                engine.speak(
                                    textToRead,
                                    TextToSpeech.QUEUE_FLUSH,
                                    null,
                                    "french_lesson_${lesson.number}"
                                )
                            }
                        },
                        enabled = ttsReady,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = FPrimary)
                    ) {
                        Icon(Icons.Default.VolumeUp, null)
                        Spacer(Modifier.width(8.dp))
                        Text(if (ttsReady) "Listen" else "Preparing voice…")
                    }
                }
            }
        }
    }
}

@Composable
private fun FrenchReview(lesson: FrenchLesson) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        item {
            Text(
                "Lesson Review",
                color = FText,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Révision rapide avant de passer à la leçon suivante.",
                color = FGray
            )
        }
        item {
            Card(
                Modifier.fillMaxWidth(),
                RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = FLight)
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text(
                        lesson.objective,
                        color = FText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        "À retenir : ${lesson.concepts.joinToString(" • ") { it.first }}",
                        color = FText,
                        lineHeight = 22.sp
                    )
                }
            }
        }
        item {
            Text(
                "Checklist",
                color = FText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        items(
            listOf(
                "J’ai lu l’explication.",
                "J’ai révisé le vocabulaire.",
                "J’ai fait les 10 exercices.",
                "J’ai lu les corrections et les explications.",
                "Je peux appliquer la notion dans une nouvelle phrase."
            )
        ) { itemText ->
            Card(
                Modifier.fillMaxWidth(),
                RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    Modifier.padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        null,
                        tint = FPrimary,
                        modifier = Modifier.size(21.dp)
                    )
                    Spacer(Modifier.width(9.dp))
                    Text(itemText, color = FText)
                }
            }
        }
    }
}
