package ma.fldm.englishstudies.data
import ma.fldm.englishstudies.R

data class ListeningVocabulary(
    val term: String,
    val definition: String
)

data class ListeningQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String
)

data class ListeningActivity(
    val id: String,
    val title: String,
    val level: String,
    val objective: String,

    val beforeListening: List<String>,

    /*
     * Audio local Android.
     *
     * null = aucun audio installé pour le moment.
     *
     * Quand nous ajouterons le fichier MP3 dans res/raw,
     * il suffira de mettre :
     *
     * audioResId = R.raw.nom_du_fichier
     */
    val audioResId: Int? = null,

    val transcript: String,

    val vocabulary: List<ListeningVocabulary>,

    val questions: List<ListeningQuestion>
)

object ListeningData {

    val activities: List<ListeningActivity> = listOf(

        // =========================================================
        // LISTENING 1
        // =========================================================

        ListeningActivity(

            id = "s1_listening_01",

            title = "Listening 1 — University Life",

            level = "S1 — Beginner / Intermediate",

            objective = """
                Understand a short university conversation,
                identify specific information, and recognize
                useful academic vocabulary.
            """.trimIndent(),

            beforeListening = listOf(

                "Think about your first experience at university.",

                "What activities do university students usually do?",

                "What difficulties can students experience at university?"
            ),

            audioResId = R.raw.university_life,

            transcript = """
                Anna: Hi, Karim. How are you finding university life?

                Karim: It's interesting, but sometimes it is difficult.
                There is much more work than I expected.

                Anna: I agree. We have lectures, reading assignments
                and presentations.

                Karim: Yes. I especially need to improve my academic
                English.

                Anna: Same here. I think regular practice helps a lot.

                Karim: Do you usually study in the library?

                Anna: Yes. I usually go there after my classes.
                It is quiet and I can concentrate.

                Karim: That sounds useful. Maybe I should try it too.

                Anna: You should. We can also study together sometime.
            """.trimIndent(),

            vocabulary = listOf(

                ListeningVocabulary(
                    term = "lecture",
                    definition = "A university class given by a lecturer."
                ),

                ListeningVocabulary(
                    term = "reading assignment",
                    definition = "Academic reading that students are required to complete."
                ),

                ListeningVocabulary(
                    term = "presentation",
                    definition = "An oral presentation given to an audience."
                ),

                ListeningVocabulary(
                    term = "improve",
                    definition = "To make something better."
                ),

                ListeningVocabulary(
                    term = "concentrate",
                    definition = "To focus attention on something."
                ),

                ListeningVocabulary(
                    term = "regular practice",
                    definition = "Practice carried out frequently and consistently."
                )
            ),

            questions = listOf(

                ListeningQuestion(
                    id = "s1_listening_01_q1",

                    question = "How does Karim describe university life?",

                    options = listOf(
                        "Very easy",
                        "Interesting but sometimes difficult",
                        "Completely boring",
                        "Short and simple"
                    ),

                    correctAnswerIndex = 1,

                    explanation = """
                        Karim says that university life is interesting,
                        but sometimes difficult because of the amount
                        of academic work.
                    """.trimIndent()
                ),

                ListeningQuestion(
                    id = "s1_listening_01_q2",

                    question = "What types of academic work do they mention?",

                    options = listOf(
                        "Lectures, reading assignments and presentations",
                        "Sports and travel",
                        "Jobs and interviews",
                        "Music and theatre"
                    ),

                    correctAnswerIndex = 0,

                    explanation = """
                        Anna mentions lectures, reading assignments
                        and presentations.
                    """.trimIndent()
                ),

                ListeningQuestion(
                    id = "s1_listening_01_q3",

                    question = "What does Karim want to improve?",

                    options = listOf(
                        "His computer skills",
                        "His academic English",
                        "His mathematics",
                        "His writing in French"
                    ),

                    correctAnswerIndex = 1,

                    explanation = """
                        Karim explicitly says that he needs to improve
                        his academic English.
                    """.trimIndent()
                ),

                ListeningQuestion(
                    id = "s1_listening_01_q4",

                    question = "Where does Anna usually study?",

                    options = listOf(
                        "At home",
                        "In a café",
                        "In the library",
                        "Outside the university"
                    ),

                    correctAnswerIndex = 2,

                    explanation = """
                        Anna says that she usually goes to the library
                        after her classes.
                    """.trimIndent()
                ),

                ListeningQuestion(
                    id = "s1_listening_01_q5",

                    question = "Why does Anna like studying in the library?",

                    options = listOf(
                        "It is close to her home",
                        "It is quiet and she can concentrate",
                        "Her friends work there",
                        "It has free food"
                    ),

                    correctAnswerIndex = 1,

                    explanation = """
                        Anna explains that the library is quiet and
                        helps her concentrate.
                    """.trimIndent()
                )
            )
        )
    )
}