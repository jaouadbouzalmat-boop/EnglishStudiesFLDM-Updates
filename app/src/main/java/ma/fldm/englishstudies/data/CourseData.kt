package ma.fldm.englishstudies.data

data class Example(
    val english: String,
    val french: String
)

data class QuizQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String
)

data class Lesson(
    val id: String,
    val title: String,
    val objective: String,
    val content: String,
    val examples: List<Example>,
    val keyTerms: List<String>,
    val questions: List<QuizQuestion> = emptyList()
)

data class CourseUnit(
    val id: String,
    val title: String,
    val description: String,
    val lessons: List<Lesson>
)

object CourseData {

    val grammar1Units = listOf(

        // =========================================================
        // UNIT 1 — FOUNDATIONS OF GRAMMAR
        // =========================================================

        CourseUnit(
            id = "grammar1_unit1",

            title = "Unit 1 — Foundations of Grammar",

            description = """
                Introduction to the study of English grammar,
                grammatical structure, form, function and
                basic sentence organization.
            """.trimIndent(),

            lessons = listOf(

                Lesson(
                    id = "grammar1_u1_l1",

                    title = "Lesson 1 — What is Grammar?",

                    objective = """
                        Understand the concept of grammar and explain
                        its role in the organization of language.
                    """.trimIndent(),

                    content = """
                        Grammar is the system of patterns and rules
                        that allows speakers to organize words and
                        construct meaningful sentences.

                        Studying grammar is not limited to memorizing
                        rules. It also involves observing how English
                        is structured and how different forms perform
                        different functions in communication.

                        At university level, grammar should therefore
                        be studied through description, analysis,
                        examples and practice.

                        Grammar allows us to examine relationships
                        between words, phrases, clauses and sentences.
                        It helps us understand how linguistic forms are
                        organized and how meaning is constructed.
                    """.trimIndent(),

                    examples = listOf(

                        Example(
                            english = "The student reads a book.",
                            french = "L'étudiant lit un livre."
                        ),

                        Example(
                            english = "The students are preparing their assignment.",
                            french = "Les étudiants préparent leur devoir."
                        ),

                        Example(
                            english = "Academic writing requires clarity.",
                            french = "L'écriture universitaire exige de la clarté."
                        )
                    ),

                    keyTerms = listOf(
                        "grammar",
                        "sentence",
                        "structure",
                        "form",
                        "function",
                        "clause",
                        "phrase",
                        "meaning"
                    ),

                    questions = listOf(

                        QuizQuestion(
                            id = "g1_u1_l1_q1",
                            question = """
                                Which statement best describes grammar
                                in the context of linguistic study?
                            """.trimIndent(),
                            options = listOf(
                                "A collection of vocabulary words",
                                "A system organizing linguistic forms and relationships",
                                "A list of pronunciation rules only",
                                "A method for translating texts"
                            ),
                            correctAnswerIndex = 1,
                            explanation = """
                                Grammar describes the patterns and
                                relationships through which linguistic
                                units are organized into meaningful
                                expressions and sentences.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u1_l1_q2",
                            question = """
                                Which of the following is an example
                                of grammatical analysis?
                            """.trimIndent(),
                            options = listOf(
                                "Memorizing 100 English words",
                                "Identifying the function of a noun phrase",
                                "Translating a paragraph word for word",
                                "Counting the letters in a sentence"
                            ),
                            correctAnswerIndex = 1,
                            explanation = """
                                Identifying the function of a noun phrase
                                is grammatical analysis because it examines
                                the role of a linguistic unit within a sentence.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u1_l1_q3",
                            question = """
                                In the sentence "The student reads a book",
                                which element is the subject?
                            """.trimIndent(),
                            options = listOf(
                                "reads",
                                "a book",
                                "The student",
                                "book"
                            ),
                            correctAnswerIndex = 2,
                            explanation = """
                                "The student" is the subject because it
                                identifies who performs the action expressed
                                by the verb "reads".
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u1_l1_q4",
                            question = """
                                Why is grammar studied through examples
                                and analysis at university level?
                            """.trimIndent(),
                            options = listOf(
                                "Because grammar is only memorization",
                                "Because examples help reveal how language works",
                                "Because examples replace grammatical theory",
                                "Because rules are unnecessary"
                            ),
                            correctAnswerIndex = 1,
                            explanation = """
                                Examples allow students to observe how
                                grammatical structures actually function
                                in meaningful language.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u1_l1_q5",
                            question = """
                                What is the main purpose of grammatical
                                structure?
                            """.trimIndent(),
                            options = listOf(
                                "To organize linguistic elements",
                                "To make sentences longer",
                                "To eliminate vocabulary",
                                "To replace communication"
                            ),
                            correctAnswerIndex = 0,
                            explanation = """
                                Grammatical structure organizes words,
                                phrases and clauses into meaningful
                                linguistic expressions.
                            """.trimIndent()
                        )
                    )
                ),

                Lesson(
                    id = "grammar1_u1_l2",

                    title = "Lesson 2 — Form and Function",

                    objective = """
                        Distinguish grammatical form from grammatical
                        function and apply this distinction to sentence
                        analysis.
                    """.trimIndent(),

                    content = """
                        In grammatical analysis, form refers to the type
                        or shape of a linguistic unit, while function
                        refers to the role that the unit plays within
                        a sentence.

                        A noun phrase, for example, may function as
                        a subject, object or complement depending on
                        the structure of the sentence.

                        Compare:

                        The student opened the book.

                        "The student" functions as the subject,
                        while "the book" functions as the object.
                    """.trimIndent(),

                    examples = listOf(
                        Example(
                            english = "The professor explained the lesson.",
                            french = "Le professeur a expliqué la leçon."
                        ),
                        Example(
                            english = "The students completed the exercise.",
                            french = "Les étudiants ont terminé l'exercice."
                        )
                    ),

                    keyTerms = listOf(
                        "form",
                        "function",
                        "subject",
                        "object",
                        "noun phrase"
                    ),

                    questions = listOf(

                        QuizQuestion(
                            id = "g1_u1_l2_q1",
                            question = """
            In grammatical analysis, what does "form" refer to?
        """.trimIndent(),
                            options = listOf(
                                "The role a unit plays in a sentence",
                                "The type or shape of a linguistic unit",
                                "The meaning of an entire paragraph",
                                "The pronunciation of a word only"
                            ),
                            correctAnswerIndex = 1,
                            explanation = """
            Form refers to the type or shape of a linguistic
            unit, such as a noun phrase, verb phrase or clause.
        """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u1_l2_q2",
                            question = """
            What does "function" describe in grammatical analysis?
        """.trimIndent(),
                            options = listOf(
                                "The spelling of a word",
                                "The number of words in a sentence",
                                "The role a linguistic unit plays within a sentence",
                                "The pronunciation of a phrase"
                            ),
                            correctAnswerIndex = 2,
                            explanation = """
            Function refers to the grammatical role played by
            a linguistic unit within a sentence.
        """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u1_l2_q3",
                            question = """
            In the sentence "The student opened the book",
            what is the function of "The student"?
        """.trimIndent(),
                            options = listOf(
                                "Object",
                                "Subject",
                                "Complement",
                                "Adverbial"
                            ),
                            correctAnswerIndex = 1,
                            explanation = """
            "The student" functions as the subject because it
            occupies the subject position in the sentence.
        """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u1_l2_q4",
                            question = """
            In the sentence "The student opened the book",
            what is the function of "the book"?
        """.trimIndent(),
                            options = listOf(
                                "Subject",
                                "Object",
                                "Determiner",
                                "Adverb"
                            ),
                            correctAnswerIndex = 1,
                            explanation = """
            "The book" functions as the object of the verb
            "opened".
        """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u1_l2_q5",
                            question = """
            Which statement best describes the relationship
            between form and function?
        """.trimIndent(),
                            options = listOf(
                                "A form can perform different grammatical functions",
                                "Form and function always mean exactly the same thing",
                                "Function describes only pronunciation",
                                "A noun phrase can never be a subject"
                            ),
                            correctAnswerIndex = 0,
                            explanation = """
            The same grammatical form can perform different
            functions depending on how it is used in a sentence.
        """.trimIndent()
                        )
                    )
                ),

                Lesson(
                    id = "grammar1_u1_l3",

                    title = "Lesson 3 — The Basic English Sentence",

                    objective = """
                        Identify the principal components of a basic
                        English sentence and recognize the basic
                        Subject–Verb–Object pattern.
                    """.trimIndent(),

                    content = """
                        A basic English sentence commonly contains a
                        subject and a verb.

                        Depending on the verb and the meaning of the
                        sentence, other elements such as objects,
                        complements and adverbials may also appear.

                        A useful starting pattern is:

                        Subject + Verb + Object

                        Example:

                        The student reads the article.

                        The student = Subject
                        reads = Verb
                        the article = Object
                    """.trimIndent(),

                    examples = listOf(
                        Example(
                            english = "Researchers analyse data.",
                            french = "Les chercheurs analysent les données."
                        ),
                        Example(
                            english = "Students discuss the problem.",
                            french = "Les étudiants discutent du problème."
                        ),
                        Example(
                            english = "The lecturer explained the theory.",
                            french = "L'enseignant a expliqué la théorie."
                        )
                    ),

                    keyTerms = listOf(
                        "subject",
                        "verb",
                        "object",
                        "sentence",
                        "clause",
                        "predicate"
                    ),

                    questions = listOf(

                        QuizQuestion(
                            id = "g1_u1_l3_q1",
                            question = """
            Which element identifies who or what the sentence
            is about or who performs the action?
        """.trimIndent(),
                            options = listOf(
                                "The object",
                                "The subject",
                                "The predicate",
                                "The adverbial"
                            ),
                            correctAnswerIndex = 1,
                            explanation = """
            The subject is the element that identifies who or
            what the sentence is about or who performs the action.
        """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u1_l3_q2",
                            question = """
            In the sentence "The student reads the article",
            which part is the verb?
        """.trimIndent(),
                            options = listOf(
                                "The student",
                                "the article",
                                "reads",
                                "The sentence"
                            ),
                            correctAnswerIndex = 2,
                            explanation = """
            "Reads" is the verb because it expresses the action
            performed by the subject.
        """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u1_l3_q3",
                            question = """
            In the sentence "The student reads the article",
            what is the function of "the article"?
        """.trimIndent(),
                            options = listOf(
                                "Subject",
                                "Verb",
                                "Object",
                                "Predicate"
                            ),
                            correctAnswerIndex = 2,
                            explanation = """
            "The article" functions as the object of the verb
            "reads".
        """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u1_l3_q4",
                            question = """
            Which pattern is presented as a useful starting point
            for understanding a basic English sentence?
        """.trimIndent(),
                            options = listOf(
                                "Object + Subject + Verb",
                                "Verb + Object + Subject",
                                "Subject + Verb + Object",
                                "Subject + Object + Verb"
                            ),
                            correctAnswerIndex = 2,
                            explanation = """
            Subject + Verb + Object is presented as a useful
            starting pattern for analysing basic English sentences.
        """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u1_l3_q5",
                            question = """
            In the sentence "Researchers analyse data",
            what is "Researchers"?
        """.trimIndent(),
                            options = listOf(
                                "The object",
                                "The subject",
                                "The verb",
                                "The complement"
                            ),
                            correctAnswerIndex = 1,
                            explanation = """
            "Researchers" is the subject because it identifies
            who performs the action expressed by "analyse".
        """.trimIndent()
                        )
                    )
                )
            )
        ),

        // =========================================================
        // UNIT 2 — WORD CLASSES
        // =========================================================

        CourseUnit(
            id = "grammar1_unit2",

            title = "Unit 2 — Word Classes",

            description = """
                Introduction to the major grammatical word classes
                of English and their morphological, syntactic and
                semantic roles in sentence structure.
            """.trimIndent(),

            lessons = listOf(

                Lesson(
                    id = "grammar1_u2_l1",

                    title = "Lesson 1 — Nouns and Pronouns",

                    objective = """
                        Identify nouns and pronouns, distinguish their
                        major types, and explain their grammatical roles
                        in English sentences.
                    """.trimIndent(),

                    content = """
                        Nouns are words used to refer to people, places,
                        objects, concepts, events and other entities.

                        Common examples include:

                        student
                        university
                        book
                        language
                        freedom
                        research

                        Common nouns refer to general classes of entities,
                        while proper nouns identify particular entities.

                        Concrete nouns refer to entities that can be
                        experienced through the senses, while abstract
                        nouns refer to ideas, qualities or concepts.

                        Pronouns are forms that can stand in for noun
                        phrases or refer to participants already known
                        in the discourse.

                        Common personal pronouns include:

                        I, you, he, she, it, we, they

                        Pronouns contribute to cohesion because they allow
                        speakers and writers to avoid unnecessary repetition.
                    """.trimIndent(),

                    examples = listOf(
                        Example(
                            english = "The student submitted an essay.",
                            french = "L'étudiant a remis une dissertation."
                        ),
                        Example(
                            english = "Morocco has a rich linguistic heritage.",
                            french = "Le Maroc possède un riche patrimoine linguistique."
                        ),
                        Example(
                            english = "Sarah completed her research.",
                            french = "Sarah a terminé sa recherche."
                        ),
                        Example(
                            english = "The lecturer gave us the articles.",
                            french = "L'enseignant nous a donné les articles."
                        )
                    ),

                    keyTerms = listOf(
                        "noun",
                        "proper noun",
                        "common noun",
                        "concrete noun",
                        "abstract noun",
                        "pronoun",
                        "personal pronoun",
                        "reference",
                        "cohesion"
                    ),

                    questions = listOf(

                        QuizQuestion(
                            id = "g1_u2_l1_q1",
                            question = """
                                Which word is a noun in the sentence
                                "The student completed the assignment"?
                            """.trimIndent(),
                            options = listOf(
                                "completed",
                                "the",
                                "student",
                                "the assignment"
                            ),
                            correctAnswerIndex = 2,
                            explanation = """
                                "Student" is a noun because it names a person.
                                "Assignment" is also a noun, although the complete
                                phrase "the assignment" is a noun phrase.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u2_l1_q2",
                            question = "Which of the following is a proper noun?",
                            options = listOf(
                                "university",
                                "student",
                                "Morocco",
                                "language"
                            ),
                            correctAnswerIndex = 2,
                            explanation = """
                                "Morocco" is a proper noun because it identifies
                                a specific geographical and political entity.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u2_l1_q3",
                            question = "Which sentence contains a pronoun?",
                            options = listOf(
                                "The lecturer explained the theory.",
                                "Students read academic articles.",
                                "She explained the theory.",
                                "The university opened a library."
                            ),
                            correctAnswerIndex = 2,
                            explanation = """
                                "She" is a personal pronoun that refers to
                                a female person whose identity is understood
                                from the context.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u2_l1_q4",
                            question = """
                                What is the main function of pronouns in discourse?
                            """.trimIndent(),
                            options = listOf(
                                "To replace all verbs",
                                "To reduce repetition and maintain reference",
                                "To create new nouns",
                                "To indicate tense"
                            ),
                            correctAnswerIndex = 1,
                            explanation = """
                                Pronouns can refer back to previously mentioned
                                entities and therefore contribute to cohesion.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u2_l1_q5",
                            question = "Which noun is abstract?",
                            options = listOf(
                                "table",
                                "student",
                                "freedom",
                                "book"
                            ),
                            correctAnswerIndex = 2,
                            explanation = """
                                "Freedom" names an abstract concept rather
                                than a physical object.
                            """.trimIndent()
                        )
                    )
                ),

                Lesson(
                    id = "grammar1_u2_l2",

                    title = "Lesson 2 — Verbs and Auxiliary Verbs",

                    objective = """
                        Identify lexical and auxiliary verbs and explain
                        how verbs contribute to tense, aspect, voice,
                        negation and question formation.
                    """.trimIndent(),

                    content = """
                        Verbs are central elements of English clauses.
                        They commonly express actions, events, processes
                        or states.

                        Examples include:

                        study
                        write
                        analyse
                        become
                        know
                        understand

                        A distinction can be made between lexical verbs
                        and auxiliary verbs.

                        Lexical verbs carry the main lexical meaning.

                        The primary auxiliaries are:

                        be
                        have
                        do

                        Modal auxiliaries include:

                        can
                        could
                        may
                        might
                        must
                        should
                        will
                        would

                        Auxiliary verbs play important roles in tense,
                        aspect, negation, questions and passive constructions.
                    """.trimIndent(),

                    examples = listOf(
                        Example(
                            english = "Students study English.",
                            french = "Les étudiants étudient l'anglais."
                        ),
                        Example(
                            english = "Students are studying English.",
                            french = "Les étudiants étudient actuellement l'anglais."
                        ),
                        Example(
                            english = "Students have completed the task.",
                            french = "Les étudiants ont terminé la tâche."
                        ),
                        Example(
                            english = "Do students understand the lesson?",
                            french = "Les étudiants comprennent-ils la leçon ?"
                        )
                    ),

                    keyTerms = listOf(
                        "verb",
                        "lexical verb",
                        "auxiliary",
                        "primary auxiliary",
                        "modal auxiliary",
                        "tense",
                        "aspect",
                        "negation",
                        "question",
                        "passive"
                    ),

                    questions = listOf(

                        QuizQuestion(
                            id = "g1_u2_l2_q1",
                            question = """
                                Which word is the main lexical verb in
                                "Students are studying English"?
                            """.trimIndent(),
                            options = listOf(
                                "Students",
                                "are",
                                "studying",
                                "English"
                            ),
                            correctAnswerIndex = 2,
                            explanation = """
                                "Studying" is the lexical verb because it
                                carries the main lexical meaning.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u2_l2_q2",
                            question = """
                                Which of the following is a primary auxiliary?
                            """.trimIndent(),
                            options = listOf(
                                "study",
                                "have",
                                "understand",
                                "read"
                            ),
                            correctAnswerIndex = 1,
                            explanation = """
                                "Have" is one of the three primary auxiliaries
                                in English, together with "be" and "do".
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u2_l2_q3",
                            question = """
                                What is the role of "do" in
                                "Do they understand the lesson?"
                            """.trimIndent(),
                            options = listOf(
                                "It is the lexical verb",
                                "It functions as an auxiliary in question formation",
                                "It is a noun",
                                "It marks plural number"
                            ),
                            correctAnswerIndex = 1,
                            explanation = """
                                Here "do" is an auxiliary used to form
                                the interrogative construction.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u2_l2_q4",
                            question = "Which form is a modal auxiliary?",
                            options = listOf(
                                "must",
                                "study",
                                "student",
                                "quickly"
                            ),
                            correctAnswerIndex = 0,
                            explanation = """
                                "Must" is a modal auxiliary expressing
                                meanings such as obligation or necessity.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u2_l2_q5",
                            question = """
                                In "She has completed the assignment",
                                what is the function of "has"?
                            """.trimIndent(),
                            options = listOf(
                                "Lexical verb",
                                "Auxiliary verb",
                                "Noun",
                                "Adjective"
                            ),
                            correctAnswerIndex = 1,
                            explanation = """
                                "Has" is an auxiliary used to construct
                                the perfect form.
                            """.trimIndent()
                        )
                    )
                ),

                Lesson(
                    id = "grammar1_u2_l3",

                    title = "Lesson 3 — Adjectives, Adverbs, Prepositions and Conjunctions",

                    objective = """
                        Identify adjectives, adverbs, prepositions and
                        conjunctions and explain their major grammatical
                        functions in English sentences.
                    """.trimIndent(),

                    content = """
                        Adjectives typically modify nouns or noun phrases
                        and provide information about qualities, properties
                        or characteristics.

                        Adverbs form a diverse class. They can modify
                        verbs, adjectives, other adverbs or entire clauses.

                        Prepositions typically express relationships such
                        as location, direction, time or association.

                        Conjunctions connect words, phrases or clauses.

                        Coordinating conjunctions include:

                        and, but, or, so, yet

                        Subordinating conjunctions include:

                        because, although, while, if, when

                        These word classes are essential for understanding
                        how sentences are expanded and how grammatical
                        relationships are expressed.
                    """.trimIndent(),

                    examples = listOf(
                        Example(
                            english = "She wrote an excellent essay.",
                            french = "Elle a écrit une excellente dissertation."
                        ),
                        Example(
                            english = "The student answered carefully.",
                            french = "L'étudiant a répondu avec soin."
                        ),
                        Example(
                            english = "The books are on the table.",
                            french = "Les livres sont sur la table."
                        ),
                        Example(
                            english = "Students study hard because they want to succeed.",
                            french = "Les étudiants travaillent dur parce qu'ils veulent réussir."
                        )
                    ),

                    keyTerms = listOf(
                        "adjective",
                        "adverb",
                        "preposition",
                        "conjunction",
                        "modifier",
                        "coordination",
                        "subordination",
                        "clause"
                    ),

                    questions = listOf(

                        QuizQuestion(
                            id = "g1_u2_l3_q1",
                            question = """
                                Which word is an adjective in
                                "an interesting book"?
                            """.trimIndent(),
                            options = listOf(
                                "an",
                                "interesting",
                                "book",
                                "in"
                            ),
                            correctAnswerIndex = 1,
                            explanation = """
                                "Interesting" is an adjective because it
                                modifies the noun "book".
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u2_l3_q2",
                            question = """
                                Which word is an adverb in
                                "She answered carefully"?
                            """.trimIndent(),
                            options = listOf(
                                "She",
                                "answered",
                                "carefully",
                                "none"
                            ),
                            correctAnswerIndex = 2,
                            explanation = """
                                "Carefully" is an adverb because it modifies
                                the verb "answered".
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u2_l3_q3",
                            question = """
                                Which expression contains a preposition?
                            """.trimIndent(),
                            options = listOf(
                                "very quickly",
                                "on Monday",
                                "interesting book",
                                "students study"
                            ),
                            correctAnswerIndex = 1,
                            explanation = """
                                "On" is a preposition introducing the
                                time expression "on Monday".
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u2_l3_q4",
                            question = """
                                What is the role of "because" in
                                "Students study because they want to succeed"?
                            """.trimIndent(),
                            options = listOf(
                                "It introduces a reason clause",
                                "It modifies a noun",
                                "It functions as a pronoun",
                                "It marks plural number"
                            ),
                            correctAnswerIndex = 0,
                            explanation = """
                                "Because" is a subordinating conjunction
                                introducing a reason clause.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u2_l3_q5",
                            question = "Which word is a conjunction?",
                            options = listOf(
                                "although",
                                "carefully",
                                "academic",
                                "student"
                            ),
                            correctAnswerIndex = 0,
                            explanation = """
                                "Although" is a subordinating conjunction
                                used to introduce a subordinate clause.
                            """.trimIndent()
                        )
                    )
                )
            )
        ),

        // =========================================================
        // UNIT 3 — NOUN PHRASES AND DETERMINERS
        // =========================================================

        CourseUnit(
            id = "grammar1_unit3",

            title = "Unit 3 — Noun Phrases and Determiners",

            description = """
                Study of noun phrase structure, determiners,
                articles, quantifiers and reference in English.
            """.trimIndent(),

            lessons = listOf(

                // =====================================================
                // LESSON 1
                // =====================================================

                Lesson(
                    id = "grammar1_u3_l1",

                    title = "Lesson 1 — The Structure of the Noun Phrase",

                    objective = """
                        Identify the major constituents of English noun
                        phrases and analyse their internal structure.
                    """.trimIndent(),

                    content = """
                        A noun phrase is a grammatical unit organized
                        around a noun or another element functioning
                        as its head.

                        A noun phrase may consist of a single noun:

                        Students

                        It may also contain additional elements:

                        the students

                        the university students

                        the university students in Fès

                        In a basic analysis, the noun phrase can contain:

                        Determiner + Premodification + Head + Postmodification

                        Consider:

                        the two advanced English students from Morocco

                        "the" functions as a determiner.

                        "two" specifies quantity.

                        "advanced" and "English" provide premodification.

                        "students" is the head noun.

                        "from Morocco" is a postmodifier.

                        Noun phrases can perform several grammatical
                        functions in clauses, including subject, object
                        and complement.

                        Example:

                        The university students completed the assignment.

                        The entire noun phrase "The university students"
                        functions as the subject.
                    """.trimIndent(),

                    examples = listOf(

                        Example(
                            english = "Students attend lectures.",
                            french = "Les étudiants assistent aux cours."
                        ),

                        Example(
                            english = "The students attend lectures.",
                            french = "Les étudiants assistent aux cours."
                        ),

                        Example(
                            english = "The advanced English students attended the lecture.",
                            french = "Les étudiants avancés en anglais ont assisté au cours."
                        ),

                        Example(
                            english = "The students from Fès completed the task.",
                            french = "Les étudiants de Fès ont terminé la tâche."
                        )
                    ),

                    keyTerms = listOf(
                        "noun phrase",
                        "head",
                        "determiner",
                        "premodification",
                        "postmodification",
                        "modifier",
                        "subject",
                        "object",
                        "complement"
                    ),

                    questions = listOf(

                        QuizQuestion(
                            id = "g1_u3_l1_q1",

                            question = """
                                Which element is normally the head of a
                                noun phrase such as "the advanced students"?
                            """.trimIndent(),

                            options = listOf(
                                "the",
                                "advanced",
                                "students",
                                "the advanced"
                            ),

                            correctAnswerIndex = 2,

                            explanation = """
                                "Students" is the head noun because the noun
                                phrase is organized around it.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u3_l1_q2",

                            question = """
                                In "the university students", what is
                                the function of "the"?
                            """.trimIndent(),

                            options = listOf(
                                "Head noun",
                                "Determiner",
                                "Adverb",
                                "Verb"
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                "The" functions as a determiner introducing
                                and specifying the noun phrase.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u3_l1_q3",

                            question = """
                                What is the head of the noun phrase
                                "the two excellent books"?
                            """.trimIndent(),

                            options = listOf(
                                "the",
                                "two",
                                "excellent",
                                "books"
                            ),

                            correctAnswerIndex = 3,

                            explanation = """
                                "Books" is the head because the phrase is
                                centered grammatically on this noun.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u3_l1_q4",

                            question = """
                                In "the students from Morocco", what is
                                "from Morocco"?
                            """.trimIndent(),

                            options = listOf(
                                "A determiner",
                                "A head",
                                "A postmodifier",
                                "A subject"
                            ),

                            correctAnswerIndex = 2,

                            explanation = """
                                "From Morocco" follows the head noun and
                                provides additional information, so it is
                                a postmodifier.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u3_l1_q5",

                            question = """
                                In "The university students completed
                                the assignment", which phrase functions
                                as the subject?
                            """.trimIndent(),

                            options = listOf(
                                "completed",
                                "the assignment",
                                "The university students",
                                "university"
                            ),

                            correctAnswerIndex = 2,

                            explanation = """
                                The complete noun phrase "The university
                                students" functions as the subject of the clause.
                            """.trimIndent()
                        )
                    )
                ),

                // =====================================================
                // LESSON 2
                // =====================================================

                Lesson(
                    id = "grammar1_u3_l2",

                    title = "Lesson 2 — Determiners and Articles",

                    objective = """
                        Distinguish major determiners and explain the
                        use of the definite and indefinite articles
                        in English noun phrases.
                    """.trimIndent(),

                    content = """
                        Determiners occur within noun phrases and help
                        identify, specify or quantify the noun.

                        Common determiners include:

                        the
                        a
                        an
                        this
                        that
                        these
                        those
                        my
                        your
                        some
                        any
                        each
                        every

                        English has three articles:

                        the
                        a
                        an

                        "The" is the definite article. It commonly refers
                        to a specific or identifiable entity.

                        Example:

                        Close the door.

                        "A" and "an" are indefinite articles. They are
                        commonly used when the referent is not presented
                        as a specific identifiable entity.

                        Example:

                        I saw a student.

                        The choice between "a" and "an" depends on sound,
                        not simply spelling.

                        Example:

                        a university
                        an hour

                        "University" begins with a consonant sound,
                        whereas "hour" begins with a vowel sound.

                        Article choice therefore involves grammatical,
                        semantic and phonological information.
                    """.trimIndent(),

                    examples = listOf(

                        Example(
                            english = "The professor entered the classroom.",
                            french = "Le professeur est entré dans la salle."
                        ),

                        Example(
                            english = "A student asked a question.",
                            french = "Un étudiant a posé une question."
                        ),

                        Example(
                            english = "An academic article was published.",
                            french = "Un article universitaire a été publié."
                        ),

                        Example(
                            english = "She studies at a university.",
                            french = "Elle étudie dans une université."
                        )
                    ),

                    keyTerms = listOf(
                        "determiner",
                        "definite article",
                        "indefinite article",
                        "the",
                        "a",
                        "an",
                        "specific reference",
                        "identifiability"
                    ),

                    questions = listOf(

                        QuizQuestion(
                            id = "g1_u3_l2_q1",

                            question = """
                                Which word is the definite article in English?
                            """.trimIndent(),

                            options = listOf(
                                "a",
                                "an",
                                "the",
                                "some"
                            ),

                            correctAnswerIndex = 2,

                            explanation = """
                                "The" is the definite article and is used
                                when the referent is treated as identifiable
                                or specific in context.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u3_l2_q2",

                            question = """
                                Which sentence contains an indefinite article?
                            """.trimIndent(),

                            options = listOf(
                                "The student arrived.",
                                "A student arrived.",
                                "Students arrived.",
                                "Those students arrived."
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                "A" is an indefinite article introducing
                                a singular count noun.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u3_l2_q3",

                            question = """
                                Which sentence is correct?
                            """.trimIndent(),

                            options = listOf(
                                "an university",
                                "a university",
                                "an universities",
                                "the universitys"
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                "University" begins with the consonant
                                sound /j/, so the correct article is "a".
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u3_l2_q4",

                            question = """
                                Why do we use "an hour" rather than
                                "a hour"?
                            """.trimIndent(),

                            options = listOf(
                                "Because hour is plural",
                                "Because hour starts with a vowel sound",
                                "Because hour is a proper noun",
                                "Because every noun requires an"
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                The initial sound in "hour" is a vowel sound,
                                so English uses the form "an".
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u3_l2_q5",

                            question = """
                                Which word is functioning as a determiner
                                in "my book"?
                            """.trimIndent(),

                            options = listOf(
                                "my",
                                "book",
                                "in",
                                "none"
                            ),

                            correctAnswerIndex = 0,

                            explanation = """
                                "My" is a possessive determiner specifying
                                the relationship between the speaker and the book.
                            """.trimIndent()
                        )
                    )
                ),

                // =====================================================
                // LESSON 3
                // =====================================================

                Lesson(
                    id = "grammar1_u3_l3",

                    title = "Lesson 3 — Quantifiers and Reference",

                    objective = """
                        Explain how quantifiers express quantity and how
                        noun phrases establish reference in discourse.
                    """.trimIndent(),

                    content = """
                        Quantifiers are words or expressions that provide
                        information about quantity or amount.

                        Common examples include:

                        some
                        any
                        many
                        much
                        few
                        a few
                        little
                        a little
                        several
                        enough
                        all
                        each
                        every

                        Their use depends partly on whether a noun is
                        countable or uncountable and whether the meaning
                        concerns individual items or an undifferentiated amount.

                        Compare:

                        many students
                        much research

                        "Students" is countable and plural, while
                        "research" is generally treated as uncountable.

                        Reference concerns the way noun phrases identify
                        or point to entities, concepts or quantities.

                        A speaker may introduce a referent:

                        I saw a student.

                        The speaker may then refer back to the same entity:

                        The student was carrying a book.

                        Determiners and other noun phrase elements therefore
                        contribute to discourse organization and cohesion.
                    """.trimIndent(),

                    examples = listOf(

                        Example(
                            english = "Many students attend the lecture.",
                            french = "Beaucoup d'étudiants assistent au cours."
                        ),

                        Example(
                            english = "Much research has been conducted.",
                            french = "Beaucoup de recherches ont été menées."
                        ),

                        Example(
                            english = "A few students participated.",
                            french = "Quelques étudiants ont participé."
                        ),

                        Example(
                            english = "The students submitted their essays.",
                            french = "Les étudiants ont remis leurs dissertations."
                        )
                    ),

                    keyTerms = listOf(
                        "quantifier",
                        "quantity",
                        "countable noun",
                        "uncountable noun",
                        "reference",
                        "referent",
                        "cohesion",
                        "discourse"
                    ),

                    questions = listOf(

                        QuizQuestion(
                            id = "g1_u3_l3_q1",

                            question = """
                                Which quantifier is normally used with
                                plural count nouns?
                            """.trimIndent(),

                            options = listOf(
                                "much",
                                "many",
                                "little",
                                "an"
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                "Many" is commonly used with plural count
                                nouns such as "students", "books" and "ideas".
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u3_l3_q2",

                            question = """
                                Which expression is appropriate with
                                the uncountable noun "research"?
                            """.trimIndent(),

                            options = listOf(
                                "many research",
                                "a research",
                                "much research",
                                "an research"
                            ),

                            correctAnswerIndex = 2,

                            explanation = """
                                "Research" is generally treated as an
                                uncountable noun, so "much research" is appropriate.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u3_l3_q3",

                            question = """
                                What does the phrase "the student" usually
                                do in discourse after "a student" has
                                already been introduced?
                            """.trimIndent(),

                            options = listOf(
                                "Introduce an unrelated noun",
                                "Refer back to an identifiable referent",
                                "Create a new verb",
                                "Indicate past tense"
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                After "a student" introduces a referent,
                                "the student" can identify that same
                                referent as now identifiable in context.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u3_l3_q4",

                            question = """
                                Which pair correctly illustrates the
                                countable/uncountable distinction?
                            """.trimIndent(),

                            options = listOf(
                                "many students / much research",
                                "much students / many research",
                                "an students / a research",
                                "every students / each research"
                            ),

                            correctAnswerIndex = 0,

                            explanation = """
                                "Students" is a plural count noun and
                                "research" is generally uncountable, making
                                "many students" and "much research" appropriate.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u3_l3_q5",

                            question = """
                                What is the main discourse function of
                                reference?
                            """.trimIndent(),

                            options = listOf(
                                "To indicate pronunciation",
                                "To connect noun phrases with entities or concepts",
                                "To change nouns into verbs",
                                "To mark comparative forms"
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                Reference links noun phrases to entities,
                                concepts or quantities in the discourse.
                            """.trimIndent()
                        )
                    )
                )
            )

        ),

        // =========================================================
        // UNIT 4 — VERB PHRASES
        // =========================================================

        CourseUnit(
            id = "grammar1_unit4",

            title = "Unit 4 — Verb Phrases",

            description = """
                            Study of verb phrases, lexical verbs, auxiliary verbs,
                            and the distinction between finite and non-finite verbs.
                        """.trimIndent(),

            lessons = listOf(

                // =====================================================
                // LESSON 1
                // =====================================================

                Lesson(
                    id = "grammar1_u4_l1",

                    title = "Lesson 1 — Lexical Verbs and Verb Phrases",

                    objective = """
                                    Identify lexical verbs and analyse the structure
                                    of verb phrases in English sentences.
                                """.trimIndent(),

                    content = """
                                    A verb phrase is a grammatical unit organized
                                    around a verb.

                                    The main verb in a verb phrase is often called
                                    the lexical verb because it carries the main
                                    lexical meaning.

                                    Consider:

                                    The students study English.

                                    In this sentence, "study" is the lexical verb.

                                    A verb phrase may also contain auxiliary verbs:

                                    The students are studying English.

                                    "Are studying" forms the verb phrase.
                                    "Are" is an auxiliary verb and "studying" is
                                    the lexical verb.

                                    Verb phrases can therefore contain one lexical
                                    verb or a combination of auxiliaries and a
                                    lexical verb.

                                    Compare:

                                    She studies English.
                                    She is studying English.
                                    She has studied English.
                                    She has been studying English.

                                    The verb phrase becomes structurally more
                                    complex as auxiliary verbs are added.
                                """.trimIndent(),

                    examples = listOf(

                        Example(
                            english = "The students study English.",
                            french = "Les étudiants étudient l'anglais."
                        ),

                        Example(
                            english = "The students are studying English.",
                            french = "Les étudiants étudient actuellement l'anglais."
                        ),

                        Example(
                            english = "She has completed the assignment.",
                            french = "Elle a terminé le devoir."
                        ),

                        Example(
                            english = "They have been working all morning.",
                            french = "Ils travaillent depuis toute la matinée."
                        )
                    ),

                    keyTerms = listOf(
                        "verb",
                        "verb phrase",
                        "lexical verb",
                        "auxiliary verb",
                        "main verb"
                    ),

                    questions = listOf(

                        QuizQuestion(
                            id = "g1_u4_l1_q1",

                            question = """
                                            In the sentence "The students study English",
                                            which word is the lexical verb?
                                        """.trimIndent(),

                            options = listOf(
                                "The",
                                "students",
                                "study",
                                "English"
                            ),

                            correctAnswerIndex = 2,

                            explanation = """
                                            "Study" is the lexical verb because it
                                            carries the main lexical meaning of the
                                            verb phrase.
                                        """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u4_l1_q2",

                            question = """
                                            Which expression is a complete verb phrase
                                            in "She is studying English"?
                                        """.trimIndent(),

                            options = listOf(
                                "She",
                                "is",
                                "studying",
                                "is studying"
                            ),

                            correctAnswerIndex = 3,

                            explanation = """
                                            "Is studying" forms the complete verb
                                            phrase, consisting of an auxiliary and
                                            a lexical verb.
                                        """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u4_l1_q3",

                            question = """
                                            In "She has completed the assignment",
                                            what is "completed"?
                                        """.trimIndent(),

                            options = listOf(
                                "A determiner",
                                "A lexical verb",
                                "A noun",
                                "A preposition"
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                            "Completed" is the lexical verb and
                                            "has" functions as the auxiliary.
                                        """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u4_l1_q4",

                            question = """
                                            Which sentence contains a verb phrase
                                            with two auxiliary verbs?
                                        """.trimIndent(),

                            options = listOf(
                                "She studies English.",
                                "She is studying English.",
                                "She has been studying English.",
                                "She studied English."
                            ),

                            correctAnswerIndex = 2,

                            explanation = """
                                            "Has been studying" contains the two
                                            auxiliaries "has" and "been" followed
                                            by the lexical verb "studying".
                                        """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u4_l1_q5",

                            question = """
                                            What is the main lexical function of the
                                            lexical verb in a verb phrase?
                                        """.trimIndent(),

                            options = listOf(
                                "It carries the main lexical meaning",
                                "It always marks plurality",
                                "It functions as a determiner",
                                "It introduces the subject"
                            ),

                            correctAnswerIndex = 0,

                            explanation = """
                                            The lexical verb generally carries the
                                            principal lexical meaning of the verb phrase.
                                        """.trimIndent()
                        )
                    )
                ),

                // =====================================================
                // LESSON 2
                // =====================================================

                Lesson(
                    id = "grammar1_u4_l2",

                    title = "Lesson 2 — Auxiliary Verbs",

                    objective = """
                                    Identify auxiliary verbs and explain their roles
                                    in tense, aspect, negation and question formation.
                                """.trimIndent(),

                    content = """
                                    Auxiliary verbs are verbs that work with a
                                    lexical verb to form more complex verb phrases.

                                    Important auxiliaries in English include:

                                    be
                                    have
                                    do

                                    English also has modal auxiliaries such as:

                                    can
                                    could
                                    may
                                    might
                                    must
                                    shall
                                    should
                                    will
                                    would

                                    The auxiliary "be" is commonly associated with
                                    progressive constructions and the passive.

                                    Example:

                                    She is studying.
                                    The book was written by the researcher.

                                    "Have" is commonly used to form perfect constructions.

                                    Example:

                                    She has finished her work.

                                    "Do" can be used in questions and negative constructions.

                                    Example:

                                    Do you understand the lesson?

                                    They do not understand the lesson.

                                    Modal auxiliaries express meanings such as
                                    possibility, necessity, ability and prediction.
                                """.trimIndent(),

                    examples = listOf(

                        Example(
                            english = "She is reading the article.",
                            french = "Elle lit l'article."
                        ),

                        Example(
                            english = "They have completed the task.",
                            french = "Ils ont terminé la tâche."
                        ),

                        Example(
                            english = "Do you understand the lesson?",
                            french = "Comprenez-vous la leçon ?"
                        ),

                        Example(
                            english = "Students must complete the assignment.",
                            french = "Les étudiants doivent terminer le devoir."
                        )
                    ),

                    keyTerms = listOf(
                        "auxiliary",
                        "be",
                        "have",
                        "do",
                        "modal auxiliary",
                        "progressive",
                        "perfect",
                        "negation"
                    ),

                    questions = listOf(

                        QuizQuestion(
                            id = "g1_u4_l2_q1",

                            question = """
                                            Which of the following is an auxiliary
                                            verb in English?
                                        """.trimIndent(),

                            options = listOf(
                                "student",
                                "have",
                                "academic",
                                "quickly"
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                            "Have" is one of the major auxiliary
                                            verbs in English.
                                        """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u4_l2_q2",

                            question = """
                                            What is the function of "is" in
                                            "She is studying English"?
                                        """.trimIndent(),

                            options = listOf(
                                "Lexical noun",
                                "Auxiliary verb",
                                "Determiner",
                                "Object"
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                            "Is" is an auxiliary verb used with
                                            the present participle "studying".
                                        """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u4_l2_q3",

                            question = """
                                            What is the role of "do" in
                                            "Do they understand the lesson?"
                                        """.trimIndent(),

                            options = listOf(
                                "It is the lexical verb",
                                "It functions as an auxiliary in question formation",
                                "It is a noun",
                                "It marks plural number"
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                            "Do" is an auxiliary used to construct
                                            the interrogative form.
                                        """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u4_l2_q4",

                            question = """
                                            Which form is a modal auxiliary?
                                        """.trimIndent(),

                            options = listOf(
                                "must",
                                "study",
                                "student",
                                "quickly"
                            ),

                            correctAnswerIndex = 0,

                            explanation = """
                                            "Must" is a modal auxiliary expressing
                                            meanings such as obligation or necessity.
                                        """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u4_l2_q5",

                            question = """
                                            In "She has completed the assignment",
                                            what is the function of "has"?
                                        """.trimIndent(),

                            options = listOf(
                                "Lexical verb",
                                "Auxiliary verb",
                                "Noun",
                                "Adjective"
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                            "Has" is an auxiliary used to construct
                                            the perfect form.
                                        """.trimIndent()
                        )
                    )
                ),

                // =====================================================
                // LESSON 3
                // =====================================================

                Lesson(
                    id = "grammar1_u4_l3",

                    title = "Lesson 3 — Finite and Non-Finite Verbs",

                    objective = """
                                    Distinguish finite and non-finite verb forms and
                                    identify infinitives, participles and finite verbs
                                    in English sentences.
                                """.trimIndent(),

                    content = """
                                    English verbs can occur in finite and non-finite
                                    forms.

                                    A finite verb is associated with tense and can
                                    normally function as the main verb of a clause.

                                    Examples:

                                    She studies English.
                                    They studied English.

                                    "Studies" and "studied" are finite forms.

                                    Non-finite verb forms do not carry tense in the
                                    same way as finite forms.

                                    Major non-finite forms include:

                                    to-infinitive:
                                    to study

                                    -ing participle:
                                    studying

                                    past participle:
                                    studied
                                    written
                                    gone

                                    Non-finite forms can occur in larger structures.

                                    Example:

                                    She wants to study English.

                                    In this sentence, "wants" is finite, while
                                    "to study" is non-finite.

                                    Another example:

                                    Students studying English need regular practice.

                                    "Studying" is a non-finite -ing form.
                                """.trimIndent(),

                    examples = listOf(

                        Example(
                            english = "She studies English every day.",
                            french = "Elle étudie l'anglais chaque jour."
                        ),

                        Example(
                            english = "She wants to study English.",
                            french = "Elle veut étudier l'anglais."
                        ),

                        Example(
                            english = "Students studying English need practice.",
                            french = "Les étudiants étudiant l'anglais ont besoin de pratique."
                        ),

                        Example(
                            english = "The book written by the researcher was published.",
                            french = "Le livre écrit par le chercheur a été publié."
                        )
                    ),

                    keyTerms = listOf(
                        "finite",
                        "non-finite",
                        "infinitive",
                        "participle",
                        "present participle",
                        "past participle",
                        "tense"
                    ),

                    questions = listOf(

                        QuizQuestion(
                            id = "g1_u4_l3_q1",

                            question = """
                                            Which verb form is finite in
                                            "She studies English"?
                                        """.trimIndent(),

                            options = listOf(
                                "She",
                                "studies",
                                "English",
                                "none"
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                            "Studies" is finite because it is the
                                            tense-bearing verb of the clause.
                                        """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u4_l3_q2",

                            question = """
                                            Which expression is a non-finite
                                            infinitive?
                                        """.trimIndent(),

                            options = listOf(
                                "studies",
                                "studied",
                                "to study",
                                "study"
                            ),

                            correctAnswerIndex = 2,

                            explanation = """
                                            "To study" is a to-infinitive and is
                                            therefore a non-finite verb form.
                                        """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u4_l3_q3",

                            question = """
                                            In "She wants to study English",
                                            which verb is finite?
                                        """.trimIndent(),

                            options = listOf(
                                "wants",
                                "to study",
                                "English",
                                "She"
                            ),

                            correctAnswerIndex = 0,

                            explanation = """
                                            "Wants" is the finite verb of the main
                                            clause, while "to study" is non-finite.
                                        """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u4_l3_q4",

                            question = """
                                            Which form is a non-finite -ing form?
                                        """.trimIndent(),

                            options = listOf(
                                "studies",
                                "studied",
                                "studying",
                                "study"
                            ),

                            correctAnswerIndex = 2,

                            explanation = """
                                            "Studying" is an -ing form and can function
                                            as a non-finite participial form.
                                        """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u4_l3_q5",

                            question = """
                                            Why are non-finite verb forms described
                                            as "non-finite"?
                                        """.trimIndent(),

                            options = listOf(
                                "They cannot occur in English",
                                "They do not function as tense-bearing finite verbs",
                                "They are always nouns",
                                "They always occur without meaning"
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                            Non-finite forms do not carry tense in
                                            the same way as finite verbs.
                                        """.trimIndent(),


                            )
                    )
                )
            )
        ),
        // =========================================================
        // UNIT 5 — TENSE AND ASPECT
        // =========================================================

        CourseUnit(
            id = "grammar1_unit5",

            title = "Unit 5 — Tense and Aspect",

            description = """
                Study of tense, time reference and grammatical aspect
                in English, with particular attention to present, past,
                progressive and perfect constructions.
            """.trimIndent(),

            lessons = listOf(

                // =====================================================
                // LESSON 1
                // =====================================================

                Lesson(
                    id = "grammar1_u5_l1",

                    title = "Lesson 1 — The English Tense System",

                    objective = """
                        Distinguish grammatical tense from time reference
                        and identify the major tense forms used in English.
                    """.trimIndent(),

                    content = """
                        Tense is a grammatical category associated with
                        the form of the verb.

                        Time, by contrast, refers to when an event or
                        situation occurs in relation to the moment of
                        speaking or another reference point.

                        English has a morphological contrast between
                        present and past forms.

                        Compare:

                        She studies English.
                        She studied English.

                        "Studies" is a present-tense form, while
                        "studied" is a past-tense form.

                        English commonly expresses future time through
                        constructions such as "will", "be going to",
                        the present progressive and the present simple.

                        Examples:

                        She will study tomorrow.

                        She is going to study tomorrow.

                        She is studying tomorrow.

                        The train leaves at eight tomorrow morning.

                        Therefore, grammatical tense and chronological
                        time should not be treated as exactly the same
                        concept.
                    """.trimIndent(),

                    examples = listOf(

                        Example(
                            english = "She studies English every day.",
                            french = "Elle étudie l'anglais chaque jour."
                        ),

                        Example(
                            english = "She studied English yesterday.",
                            french = "Elle a étudié l'anglais hier."
                        ),

                        Example(
                            english = "She will study English tomorrow.",
                            french = "Elle étudiera l'anglais demain."
                        ),

                        Example(
                            english = "The semester starts next Monday.",
                            french = "Le semestre commence lundi prochain."
                        )
                    ),

                    keyTerms = listOf(
                        "tense",
                        "time",
                        "present tense",
                        "past tense",
                        "future time",
                        "time reference",
                        "verb form"
                    ),

                    questions = listOf(

                        QuizQuestion(
                            id = "g1_u5_l1_q1",

                            question = """
                                What does grammatical tense primarily
                                refer to?
                            """.trimIndent(),

                            options = listOf(
                                "The vocabulary of a sentence",
                                "A grammatical distinction expressed by verb forms",
                                "The length of a sentence",
                                "The pronunciation of a verb"
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                Tense is a grammatical category associated
                                with the form of the verb.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u5_l1_q2",

                            question = """
                                Which statement best distinguishes
                                tense from time?
                            """.trimIndent(),

                            options = listOf(
                                "Tense and time are always identical",
                                "Tense is grammatical, while time refers to temporal reference",
                                "Time is grammatical, while tense is lexical",
                                "Neither concept is related to verbs"
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                Tense is a grammatical category, whereas
                                time concerns when an event or situation occurs.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u5_l1_q3",

                            question = """
                                Which sentence contains a past-tense form?
                            """.trimIndent(),

                            options = listOf(
                                "She studies English.",
                                "She studied English.",
                                "She will study English.",
                                "She is studying English."
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                "Studied" is the past-tense form of the
                                verb "study".
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u5_l1_q4",

                            question = """
                                Which sentence refers to future time?
                            """.trimIndent(),

                            options = listOf(
                                "She studied yesterday.",
                                "She studies every day.",
                                "She will study tomorrow.",
                                "She studied English."
                            ),

                            correctAnswerIndex = 2,

                            explanation = """
                                "Will study tomorrow" is a construction
                                referring to future time.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u5_l1_q5",

                            question = """
                                Which statement is correct about future
                                time in English?
                            """.trimIndent(),

                            options = listOf(
                                "English expresses future time only with one tense ending",
                                "English can use several constructions to refer to future time",
                                "Future time cannot be expressed grammatically",
                                "Only the past tense can refer to the future"
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                English uses several constructions to refer
                                to future time, including will, be going to,
                                present progressive and present simple.
                            """.trimIndent()
                        )
                    )
                ),

                // =====================================================
                // LESSON 2
                // =====================================================

                Lesson(
                    id = "grammar1_u5_l2",

                    title = "Lesson 2 — Present and Past Forms",

                    objective = """
                        Distinguish the major present and past constructions
                        and explain their uses in English clauses.
                    """.trimIndent(),

                    content = """
                        The present simple is commonly used for habits,
                        general truths, routines and stable situations.

                        Example:

                        Students attend lectures every week.

                        The present progressive is commonly used for
                        situations in progress around a reference time.

                        Example:

                        The students are attending a lecture.

                        The past simple commonly refers to completed
                        events or situations located in past time.

                        Example:

                        The students attended the lecture yesterday.

                        The past progressive presents a situation as
                        ongoing at a particular time in the past.

                        Example:

                        The students were attending the lecture at ten.

                        Compare:

                        She studies English every day.

                        She is studying English now.

                        She studied English yesterday.

                        She was studying English at eight.
                    """.trimIndent(),

                    examples = listOf(

                        Example(
                            english = "Students attend lectures every week.",
                            french = "Les étudiants assistent aux cours chaque semaine."
                        ),

                        Example(
                            english = "The students are attending a lecture.",
                            french = "Les étudiants assistent à un cours actuellement."
                        ),

                        Example(
                            english = "The students attended the lecture yesterday.",
                            french = "Les étudiants ont assisté au cours hier."
                        ),

                        Example(
                            english = "The students were attending the lecture at ten.",
                            french = "Les étudiants assistaient au cours à dix heures."
                        )
                    ),

                    keyTerms = listOf(
                        "present simple",
                        "present progressive",
                        "past simple",
                        "past progressive",
                        "habit",
                        "routine",
                        "ongoing situation"
                    ),

                    questions = listOf(

                        QuizQuestion(
                            id = "g1_u5_l2_q1",

                            question = """
                                Which tense is commonly used for habits
                                and routines?
                            """.trimIndent(),

                            options = listOf(
                                "Present simple",
                                "Past progressive",
                                "Present perfect",
                                "Past perfect"
                            ),

                            correctAnswerIndex = 0,

                            explanation = """
                                The present simple is commonly used to
                                describe habits, routines and general truths.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u5_l2_q2",

                            question = """
                                Which sentence contains the present
                                progressive?
                            """.trimIndent(),

                            options = listOf(
                                "She studies English.",
                                "She studied English.",
                                "She is studying English.",
                                "She will study English."
                            ),

                            correctAnswerIndex = 2,

                            explanation = """
                                "Is studying" is the present progressive
                                construction.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u5_l2_q3",

                            question = """
                                Which sentence refers to a completed
                                event in past time?
                            """.trimIndent(),

                            options = listOf(
                                "She studies every day.",
                                "She is studying now.",
                                "She studied yesterday.",
                                "She is studying tomorrow."
                            ),

                            correctAnswerIndex = 2,

                            explanation = """
                                "Studied yesterday" presents a completed
                                event located in past time.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u5_l2_q4",

                            question = """
                                Which sentence contains the past progressive?
                            """.trimIndent(),

                            options = listOf(
                                "They study English.",
                                "They studied English.",
                                "They are studying English.",
                                "They were studying English."
                            ),

                            correctAnswerIndex = 3,

                            explanation = """
                                "Were studying" is the past progressive form.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u5_l2_q5",

                            question = """
                                What is a typical meaning of the present
                                progressive?
                            """.trimIndent(),

                            options = listOf(
                                "A situation in progress around a reference time",
                                "Only a permanent truth",
                                "Only a completed past event",
                                "A noun phrase"
                            ),

                            correctAnswerIndex = 0,

                            explanation = """
                                The present progressive commonly presents
                                a situation as ongoing around a reference time.
                            """.trimIndent()
                        )
                    )
                ),

                // =====================================================
                // LESSON 3
                // =====================================================

                Lesson(
                    id = "grammar1_u5_l3",

                    title = "Lesson 3 — Aspect: Progressive and Perfect",

                    objective = """
                        Explain progressive and perfect aspect and analyse
                        their interaction with tense in English constructions.
                    """.trimIndent(),

                    content = """
                        Aspect concerns how a situation is viewed in relation
                        to its internal temporal structure.

                        The progressive presents a situation as ongoing.

                        Example:

                        She is reading the article.

                        The perfect connects a prior situation with a later
                        reference point.

                        Example:

                        She has read the article.

                        The perfect can also appear in past forms:

                        She had read the article before the lecture.

                        Progressive and perfect constructions can combine.

                        Example:

                        She has been reading the article.

                        This is a perfect progressive construction.

                        Compare:

                        She reads the article.

                        She is reading the article.

                        She has read the article.

                        She has been reading the article.

                        These constructions differ not only in tense but
                        also in how the speaker presents the situation.
                    """.trimIndent(),

                    examples = listOf(

                        Example(
                            english = "She is reading the article.",
                            french = "Elle est en train de lire l'article."
                        ),

                        Example(
                            english = "She has read the article.",
                            french = "Elle a lu l'article."
                        ),

                        Example(
                            english = "She had read the article before the lecture.",
                            french = "Elle avait lu l'article avant le cours."
                        ),

                        Example(
                            english = "She has been reading the article.",
                            french = "Elle lit l'article depuis un certain temps."
                        )
                    ),

                    keyTerms = listOf(
                        "aspect",
                        "progressive",
                        "perfect",
                        "perfect progressive",
                        "reference point",
                        "ongoing situation",
                        "prior situation"
                    ),

                    questions = listOf(

                        QuizQuestion(
                            id = "g1_u5_l3_q1",

                            question = """
                                What does the progressive aspect commonly
                                present?
                            """.trimIndent(),

                            options = listOf(
                                "A situation as ongoing",
                                "A noun as plural",
                                "A completed noun phrase",
                                "A pronunciation rule"
                            ),

                            correctAnswerIndex = 0,

                            explanation = """
                                The progressive presents a situation as
                                ongoing in relation to a reference time.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u5_l3_q2",

                            question = """
                                Which sentence contains the perfect aspect?
                            """.trimIndent(),

                            options = listOf(
                                "She is reading the article.",
                                "She reads the article.",
                                "She has read the article.",
                                "She studied the article."
                            ),

                            correctAnswerIndex = 2,

                            explanation = """
                                "Has read" is a perfect construction.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u5_l3_q3",

                            question = """
                                Which sentence contains the past perfect?
                            """.trimIndent(),

                            options = listOf(
                                "She reads the article.",
                                "She is reading the article.",
                                "She has read the article.",
                                "She had read the article."
                            ),

                            correctAnswerIndex = 3,

                            explanation = """
                                "Had read" is the past perfect construction.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u5_l3_q4",

                            question = """
                                Which sentence contains a perfect progressive
                                construction?
                            """.trimIndent(),

                            options = listOf(
                                "She reads the article.",
                                "She is reading the article.",
                                "She has read the article.",
                                "She has been reading the article."
                            ),

                            correctAnswerIndex = 3,

                            explanation = """
                                "Has been reading" combines perfect and
                                progressive structure.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u5_l3_q5",

                            question = """
                                What is one important difference between
                                tense and aspect?
                            """.trimIndent(),

                            options = listOf(
                                "Tense concerns grammatical time distinctions, while aspect presents the internal temporal structure of a situation.",
                                "Aspect only concerns pronunciation.",
                                "Tense is only used with nouns.",
                                "There is no difference between them."
                            ),

                            correctAnswerIndex = 0,

                            explanation = """
                                Tense and aspect are different grammatical
                                concepts: tense is associated with temporal
                                distinctions in verb forms, while aspect
                                concerns how a situation is viewed temporally.
                            """.trimIndent()
                        )
                    )
                )
            )
        ),
        // =========================================================
        // UNIT 6 — SUBJECT, OBJECT AND COMPLEMENTS
        // =========================================================

        CourseUnit(
        id = "grammar1_unit6",

        title = "Unit 6 — Subject, Object and Complements",

        description = """
                Study of the major clause functions of English,
                with particular attention to subjects, objects,
                complements and their syntactic relationships.
            """.trimIndent(),

        lessons = listOf(

            // =====================================================
            // LESSON 1
            // =====================================================

            Lesson(
                id = "grammar1_u6_l1",

                title = "Lesson 1 — Subjects and Objects",

                objective = """
                        Identify subjects and objects in English clauses
                        and explain their basic syntactic functions.
                    """.trimIndent(),

                content = """
                        The subject is a major grammatical function in
                        English clauses.

                        It commonly identifies the participant that the
                        clause is primarily about and, in many active
                        clauses, the participant associated with the action.

                        Example:

                        The student reads the article.

                        "The student" functions as the subject.

                        "The article" functions as the object.

                        The object is typically an element that is affected
                        by, involved in, or otherwise selected by the verb.

                        Compare:

                        The researcher analysed the data.

                        The researcher = subject

                        analysed = verb

                        the data = object

                        Subjects and objects can be realized by different
                        grammatical forms, including noun phrases and pronouns.

                        Example:

                        She completed the assignment.

                        "She" is the subject.

                        "the assignment" is the object.
                    """.trimIndent(),

                examples = listOf(

                    Example(
                        english = "The student reads the article.",
                        french = "L'étudiant lit l'article."
                    ),

                    Example(
                        english = "The researcher analysed the data.",
                        french = "Le chercheur a analysé les données."
                    ),

                    Example(
                        english = "She completed the assignment.",
                        french = "Elle a terminé le devoir."
                    ),

                    Example(
                        english = "The lecturer explained the theory.",
                        french = "L'enseignant a expliqué la théorie."
                    )
                ),

                keyTerms = listOf(
                    "subject",
                    "object",
                    "clause",
                    "verb",
                    "active clause",
                    "syntactic function",
                    "noun phrase"
                ),

                questions = listOf(

                    QuizQuestion(
                        id = "g1_u6_l1_q1",

                        question = """
                                In the sentence "The student reads
                                the article", which phrase is the subject?
                            """.trimIndent(),

                        options = listOf(
                            "reads",
                            "the article",
                            "The student",
                            "article"
                        ),

                        correctAnswerIndex = 2,

                        explanation = """
                                "The student" functions as the subject
                                of the clause.
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u6_l1_q2",

                        question = """
                                In the sentence "The student reads
                                the article", which phrase is the object?
                            """.trimIndent(),

                        options = listOf(
                            "The student",
                            "reads",
                            "the article",
                            "student"
                        ),

                        correctAnswerIndex = 2,

                        explanation = """
                                "The article" functions as the object
                                of the verb "reads".
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u6_l1_q3",

                        question = """
                                Which grammatical function is performed
                                by "The researcher" in "The researcher
                                analysed the data"?
                            """.trimIndent(),

                        options = listOf(
                            "Object",
                            "Subject",
                            "Complement",
                            "Adverbial"
                        ),

                        correctAnswerIndex = 1,

                        explanation = """
                                "The researcher" is the subject of
                                the clause.
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u6_l1_q4",

                        question = """
                                Which sentence contains a pronoun functioning
                                as the subject?
                            """.trimIndent(),

                        options = listOf(
                            "The student completed the work.",
                            "She completed the work.",
                            "The lecturer explained the theory.",
                            "The researchers analysed the data."
                        ),

                        correctAnswerIndex = 1,

                        explanation = """
                                "She" is a pronoun functioning as the
                                subject of the clause.
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u6_l1_q5",

                        question = """
                                Which statement best describes an object
                                in an active clause?
                            """.trimIndent(),

                        options = listOf(
                            "It is always the first word in the clause.",
                            "It is a grammatical function commonly associated with a participant selected by the verb.",
                            "It is always an adjective.",
                            "It can never be a noun phrase."
                        ),

                        correctAnswerIndex = 1,

                        explanation = """
                                Objects are grammatical functions commonly
                                associated with participants selected or
                                affected by the verb.
                            """.trimIndent()
                    )
                )
            ),

            // =====================================================
            // LESSON 2
            // =====================================================

            Lesson(
                id = "grammar1_u6_l2",

                title = "Lesson 2 — Subject and Object Complements",

                objective = """
                        Distinguish complements from objects and identify
                        subject and object complements in English clauses.
                    """.trimIndent(),

                content = """
                        A complement is an element that provides information
                        needed to complete the meaning or structure of a clause.

                        Subject complements occur after certain linking verbs
                        and describe or identify the subject.

                        Example:

                        The student is intelligent.

                        "Intelligent" is a subject complement because it
                        describes the subject "the student".

                        Another example:

                        She became a researcher.

                        "A researcher" identifies the subject.

                        Object complements occur after an object and provide
                        additional information about that object.

                        Example:

                        They elected him president.

                        "Him" is the object.

                        "President" is an object complement because it
                        identifies the object.

                        Complements should therefore be distinguished from
                        ordinary objects because they perform different
                        grammatical functions.
                    """.trimIndent(),

                examples = listOf(

                    Example(
                        english = "The student is intelligent.",
                        french = "L'étudiant est intelligent."
                    ),

                    Example(
                        english = "She became a researcher.",
                        french = "Elle est devenue chercheuse."
                    ),

                    Example(
                        english = "They elected him president.",
                        french = "Ils l'ont élu président."
                    ),

                    Example(
                        english = "The committee found the proposal useful.",
                        french = "Le comité a jugé la proposition utile."
                    )
                ),

                keyTerms = listOf(
                    "complement",
                    "subject complement",
                    "object complement",
                    "linking verb",
                    "copular verb",
                    "object",
                    "identification",
                    "description"
                ),

                questions = listOf(

                    QuizQuestion(
                        id = "g1_u6_l2_q1",

                        question = """
                                In "The student is intelligent",
                                what is "intelligent"?
                            """.trimIndent(),

                        options = listOf(
                            "Object",
                            "Subject",
                            "Subject complement",
                            "Determiner"
                        ),

                        correctAnswerIndex = 2,

                        explanation = """
                                "Intelligent" is a subject complement
                                because it describes the subject.
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u6_l2_q2",

                        question = """
                                Which verb commonly introduces a subject
                                complement?
                            """.trimIndent(),

                        options = listOf(
                            "be",
                            "write",
                            "read",
                            "analyse"
                        ),

                        correctAnswerIndex = 0,

                        explanation = """
                                The verb "be" commonly functions as a
                                linking or copular verb that introduces
                                a subject complement.
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u6_l2_q3",

                        question = """
                                In "She became a researcher", what is
                                "a researcher"?
                            """.trimIndent(),

                        options = listOf(
                            "Object",
                            "Subject complement",
                            "Adverbial",
                            "Determiner"
                        ),

                        correctAnswerIndex = 1,

                        explanation = """
                                "A researcher" identifies the subject
                                "She" and functions as a subject complement.
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u6_l2_q4",

                        question = """
                                In "They elected him president",
                                what is "president"?
                            """.trimIndent(),

                        options = listOf(
                            "Subject",
                            "Object",
                            "Object complement",
                            "Preposition"
                        ),

                        correctAnswerIndex = 2,

                        explanation = """
                                "President" provides additional information
                                identifying the object "him".
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u6_l2_q5",

                        question = """
                                What is the main difference between an
                                object and a subject complement?
                            """.trimIndent(),

                        options = listOf(
                            "A subject complement describes or identifies the subject.",
                            "An object is always an adjective.",
                            "A subject complement always comes before the subject.",
                            "There is no grammatical difference."
                        ),

                        correctAnswerIndex = 0,

                        explanation = """
                                A subject complement describes or identifies
                                the subject, usually after a linking verb.
                            """.trimIndent()
                    )
                )
            ),

            // =====================================================
            // LESSON 3
            // =====================================================

            Lesson(
                id = "grammar1_u6_l3",

                title = "Lesson 3 — Direct and Indirect Objects",

                objective = """
                        Distinguish direct and indirect objects and analyse
                        their functions in ditransitive constructions.
                    """.trimIndent(),

                content = """
                        Some English verbs can occur with two objects.

                        Consider:

                        The lecturer gave the students an assignment.

                        "The students" is the indirect object.

                        "An assignment" is the direct object.

                        The direct object is commonly the entity that is
                        transferred, affected or otherwise selected directly
                        by the verb.

                        The indirect object commonly represents a recipient,
                        beneficiary or participant associated with the transfer.

                        Compare:

                        The lecturer gave an assignment to the students.

                        The same basic relationship can be expressed with
                        a prepositional construction.

                        Another example:

                        She sent her friend a message.

                        "Her friend" = indirect object

                        "A message" = direct object

                        The distinction between direct and indirect objects
                        is important for analysing English clause structure.
                    """.trimIndent(),

                examples = listOf(

                    Example(
                        english = "The lecturer gave the students an assignment.",
                        french = "L'enseignant a donné un devoir aux étudiants."
                    ),

                    Example(
                        english = "She sent her friend a message.",
                        french = "Elle a envoyé un message à son amie."
                    ),

                    Example(
                        english = "They offered the researcher a position.",
                        french = "Ils ont proposé un poste au chercheur."
                    ),

                    Example(
                        english = "The university awarded the student a scholarship.",
                        french = "L'université a accordé une bourse à l'étudiant."
                    )
                ),

                keyTerms = listOf(
                    "direct object",
                    "indirect object",
                    "recipient",
                    "beneficiary",
                    "ditransitive verb",
                    "prepositional construction",
                    "transfer"
                ),

                questions = listOf(

                    QuizQuestion(
                        id = "g1_u6_l3_q1",

                        question = """
                                In "The lecturer gave the students
                                an assignment", what is "the students"?
                            """.trimIndent(),

                        options = listOf(
                            "Direct object",
                            "Indirect object",
                            "Subject complement",
                            "Adverbial"
                        ),

                        correctAnswerIndex = 1,

                        explanation = """
                                "The students" is the indirect object,
                                representing the recipients of the assignment.
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u6_l3_q2",

                        question = """
                                In "The lecturer gave the students
                                an assignment", what is "an assignment"?
                            """.trimIndent(),

                        options = listOf(
                            "Subject",
                            "Indirect object",
                            "Direct object",
                            "Subject complement"
                        ),

                        correctAnswerIndex = 2,

                        explanation = """
                                "An assignment" is the direct object,
                                representing what was given.
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u6_l3_q3",

                        question = """
                                Which sentence contains both a direct
                                and an indirect object?
                            """.trimIndent(),

                        options = listOf(
                            "The student reads the book.",
                            "She sent her friend a message.",
                            "The students arrived early.",
                            "The researcher is intelligent."
                        ),

                        correctAnswerIndex = 1,

                        explanation = """
                                "Her friend" is the indirect object and
                                "a message" is the direct object.
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u6_l3_q4",

                        question = """
                                What role does the indirect object commonly
                                express?
                            """.trimIndent(),

                        options = listOf(
                            "The colour of an object",
                            "A recipient or beneficiary",
                            "The tense of the verb",
                            "The pronunciation of a noun"
                        ),

                        correctAnswerIndex = 1,

                        explanation = """
                                The indirect object commonly represents
                                a recipient or beneficiary.
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u6_l3_q5",

                        question = """
                                Which sentence expresses a similar relationship
                                using a prepositional construction?
                            """.trimIndent(),

                        options = listOf(
                            "The lecturer gave an assignment to the students.",
                            "The lecturer gave students yesterday.",
                            "The lecturer was students.",
                            "The lecturer studied the students."
                        ),

                        correctAnswerIndex = 0,

                        explanation = """
                        "Gave an assignment to the students" expresses
                        the same transfer relationship using a
                        prepositional construction.
                    """.trimIndent()
                    )
                )
            )
        )
        ),
        // =========================================================
        // UNIT 7 — SENTENCE STRUCTURE
        // =========================================================

        CourseUnit(
        id = "grammar1_unit7",

        title = "Unit 7 — Sentence Structure",

        description = """
                Study of clauses and sentence structures, with attention
                to simple, compound and complex sentences, coordination
                and subordination.
            """.trimIndent(),

        lessons = listOf(

            // =====================================================
            // LESSON 1
            // =====================================================

            Lesson(
                id = "grammar1_u7_l1",

                title = "Lesson 1 — Clauses and Sentence Structure",

                objective = """
                        Identify clauses and distinguish the principal
                        components involved in English sentence structure.
                    """.trimIndent(),

                content = """
                        A clause is a grammatical unit that typically
                        contains a subject and a finite verb.

                        Consider:

                        The students study English.

                        This is a clause because it contains the subject
                        "the students" and the finite verb "study".

                        A sentence may contain one clause or more than
                        one clause.

                        Compare:

                        The students study English.

                        The students study English and they read novels.

                        The second sentence contains two clauses.

                        Clauses can therefore be analysed in relation to
                        their internal structure and their relationship
                        with other clauses.

                        A useful distinction is between independent clauses
                        and dependent clauses.

                        An independent clause can normally function as a
                        complete sentence.

                        A dependent clause cannot normally stand alone and
                        is structurally dependent on another clause.
                    """.trimIndent(),

                examples = listOf(

                    Example(
                        english = "The students study English.",
                        french = "Les étudiants étudient l'anglais."
                    ),

                    Example(
                        english = "The students study English and they read novels.",
                        french = "Les étudiants étudient l'anglais et ils lisent des romans."
                    ),

                    Example(
                        english = "Because the students study regularly, they improve.",
                        french = "Parce que les étudiants étudient régulièrement, ils progressent."
                    ),

                    Example(
                        english = "Although the exam was difficult, the students succeeded.",
                        french = "Bien que l'examen fût difficile, les étudiants ont réussi."
                    )
                ),

                keyTerms = listOf(
                    "clause",
                    "sentence",
                    "independent clause",
                    "dependent clause",
                    "finite verb",
                    "subject",
                    "sentence structure"
                ),

                questions = listOf(

                    QuizQuestion(
                        id = "g1_u7_l1_q1",

                        question = """
                                Which element is typically found in a
                                basic English clause?
                            """.trimIndent(),

                        options = listOf(
                            "Only an adjective",
                            "A subject and a finite verb",
                            "Only a noun",
                            "Only a preposition"
                        ),

                        correctAnswerIndex = 1,

                        explanation = """
                                A clause typically contains a subject
                                and a finite verb.
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u7_l1_q2",

                        question = """
                                How many clauses are present in
                                "The students study English and they
                                read novels"?
                            """.trimIndent(),

                        options = listOf(
                            "One",
                            "Two",
                            "Three",
                            "Four"
                        ),

                        correctAnswerIndex = 1,

                        explanation = """
                                The sentence contains two clauses:
                                "The students study English" and
                                "they read novels".
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u7_l1_q3",

                        question = """
                                Which clause can normally function as
                                a complete sentence?
                            """.trimIndent(),

                        options = listOf(
                            "Dependent clause",
                            "Independent clause",
                            "Prepositional phrase",
                            "Noun phrase"
                        ),

                        correctAnswerIndex = 1,

                        explanation = """
                                An independent clause can normally function
                                as a complete sentence.
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u7_l1_q4",

                        question = """
                                Which sentence contains a dependent clause?
                            """.trimIndent(),

                        options = listOf(
                            "The students succeeded.",
                            "The students study English.",
                            "Because the students study regularly, they improve.",
                            "Students read novels."
                        ),

                        correctAnswerIndex = 2,

                        explanation = """
                                "Because the students study regularly" is
                                a dependent clause introduced by "because".
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u7_l1_q5",

                        question = """
                                What is an important difference between
                                an independent and a dependent clause?
                            """.trimIndent(),

                        options = listOf(
                            "A dependent clause can always stand alone.",
                            "An independent clause can normally stand alone.",
                            "An independent clause has no verb.",
                            "A dependent clause contains no subject."
                        ),

                        correctAnswerIndex = 1,

                        explanation = """
                                An independent clause can normally function
                                as a complete sentence, while a dependent
                                clause is structurally dependent on another clause.
                            """.trimIndent()
                    )
                )
            ),

            // =====================================================
            // LESSON 2
            // =====================================================

            Lesson(
                id = "grammar1_u7_l2",

                title = "Lesson 2 — Simple, Compound and Complex Sentences",

                objective = """
                        Distinguish simple, compound and complex sentences
                        according to their clause structure.
                    """.trimIndent(),

                content = """
                        Sentence classification can be based on the number
                        and relationship of clauses.

                        A simple sentence contains one independent clause.

                        Example:

                        The students study English.

                        A compound sentence contains two or more independent
                        clauses, commonly joined by a coordinating conjunction.

                        Example:

                        The students study English, and they read novels.

                        A complex sentence contains an independent clause
                        and at least one dependent clause.

                        Example:

                        Because the students study regularly, they improve.

                        The classification therefore depends primarily on
                        clause structure rather than simply on sentence length.

                        A long sentence is not necessarily complex, and a
                        short sentence can contain more than one clause.
                    """.trimIndent(),

                examples = listOf(

                    Example(
                        english = "The students study English.",
                        french = "Les étudiants étudient l'anglais."
                    ),

                    Example(
                        english = "The students study English, and they read novels.",
                        french = "Les étudiants étudient l'anglais et ils lisent des romans."
                    ),

                    Example(
                        english = "Because the students study regularly, they improve.",
                        french = "Parce que les étudiants étudient régulièrement, ils progressent."
                    ),

                    Example(
                        english = "The students study English because they enjoy it.",
                        french = "Les étudiants étudient l'anglais parce qu'ils aiment cela."
                    )
                ),

                keyTerms = listOf(
                    "simple sentence",
                    "compound sentence",
                    "complex sentence",
                    "independent clause",
                    "dependent clause",
                    "coordination",
                    "subordination"
                ),

                questions = listOf(

                    QuizQuestion(
                        id = "g1_u7_l2_q1",

                        question = """
                                How many independent clauses does a simple
                                sentence normally contain?
                            """.trimIndent(),

                        options = listOf(
                            "None",
                            "One",
                            "Two",
                            "Three"
                        ),

                        correctAnswerIndex = 1,

                        explanation = """
                                A simple sentence normally contains one
                                independent clause.
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u7_l2_q2",

                        question = """
                                Which sentence is compound?
                            """.trimIndent(),

                        options = listOf(
                            "The students study English.",
                            "Because the students study, they improve.",
                            "The students study English, and they read novels.",
                            "Studying English"
                        ),

                        correctAnswerIndex = 2,

                        explanation = """
                                The sentence contains two independent clauses
                                joined by the coordinating conjunction "and".
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u7_l2_q3",

                        question = """
                                Which sentence is complex?
                            """.trimIndent(),

                        options = listOf(
                            "The students study English.",
                            "The students study English, and they read novels.",
                            "Because the students study regularly, they improve.",
                            "The students and lecturers."
                        ),

                        correctAnswerIndex = 2,

                        explanation = """
                                The sentence contains an independent clause
                                and a dependent clause introduced by "because".
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u7_l2_q4",

                        question = """
                                What primarily determines whether a sentence
                                is simple, compound or complex?
                            """.trimIndent(),

                        options = listOf(
                            "The number of letters",
                            "The length of the sentence",
                            "The number and relationship of clauses",
                            "The number of nouns"
                        ),

                        correctAnswerIndex = 2,

                        explanation = """
                                Sentence classification is primarily based
                                on the number and relationship of clauses.
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u7_l2_q5",

                        question = """
                                Which statement is correct?
                            """.trimIndent(),

                        options = listOf(
                            "A long sentence must be complex.",
                            "A short sentence cannot contain more than one clause.",
                            "Sentence length alone does not determine sentence type.",
                            "Every sentence contains a dependent clause."
                        ),

                        correctAnswerIndex = 2,

                        explanation = """
                                Sentence type depends on clause structure,
                                not simply on sentence length.
                            """.trimIndent()
                    )
                )
            ),

            // =====================================================
            // LESSON 3
            // =====================================================

            Lesson(
                id = "grammar1_u7_l3",

                title = "Lesson 3 — Coordination and Subordination",

                objective = """
                        Distinguish coordination from subordination and
                        identify the grammatical relationships between clauses.
                    """.trimIndent(),

                content = """
                        Coordination links units of relatively equal
                        grammatical status.

                        Coordinating conjunctions include:

                        and
                        but
                        or
                        so
                        yet

                        Example:

                        The students studied hard, and they passed the exam.

                        The two clauses are coordinated.

                        Subordination creates an unequal grammatical
                        relationship in which one clause is dependent
                        on another.

                        Common subordinators include:

                        because
                        although
                        if
                        when
                        while

                        Example:

                        Although the exam was difficult, the students succeeded.

                        "Although the exam was difficult" is a dependent
                        clause, while "the students succeeded" is the
                        independent clause.

                        Coordination and subordination are central to
                        understanding complex sentence structure and
                        academic writing.
                    """.trimIndent(),

                examples = listOf(

                    Example(
                        english = "The students studied hard, and they passed the exam.",
                        french = "Les étudiants ont beaucoup étudié et ils ont réussi l'examen."
                    ),

                    Example(
                        english = "The students studied hard, but the exam was difficult.",
                        french = "Les étudiants ont beaucoup étudié, mais l'examen était difficile."
                    ),

                    Example(
                        english = "Although the exam was difficult, the students succeeded.",
                        french = "Bien que l'examen fût difficile, les étudiants ont réussi."
                    ),

                    Example(
                        english = "Students improve when they practise regularly.",
                        french = "Les étudiants progressent lorsqu'ils pratiquent régulièrement."
                    )
                ),

                keyTerms = listOf(
                    "coordination",
                    "subordination",
                    "coordinating conjunction",
                    "subordinator",
                    "independent clause",
                    "dependent clause",
                    "clause relationship"
                ),

                questions = listOf(

                    QuizQuestion(
                        id = "g1_u7_l3_q1",

                        question = """
                                Which conjunction expresses coordination?
                            """.trimIndent(),

                        options = listOf(
                            "because",
                            "although",
                            "and",
                            "when"
                        ),

                        correctAnswerIndex = 2,

                        explanation = """
                                "And" is a coordinating conjunction that
                                can connect units of equal grammatical status.
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u7_l3_q2",

                        question = """
                                Which word introduces subordination?
                            """.trimIndent(),

                        options = listOf(
                            "and",
                            "but",
                            "or",
                            "because"
                        ),

                        correctAnswerIndex = 3,

                        explanation = """
                                "Because" is a subordinator introducing
                                a dependent clause.
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u7_l3_q3",

                        question = """
                                In "The students studied hard, and they
                                passed the exam", what is the relationship
                                between the two clauses?
                            """.trimIndent(),

                        options = listOf(
                            "Subordination",
                            "Coordination",
                            "Embedding inside a noun phrase",
                            "Modification"
                        ),

                        correctAnswerIndex = 1,

                        explanation = """
                                The two independent clauses are linked
                                through coordination using "and".
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u7_l3_q4",

                        question = """
                                In "Although the exam was difficult,
                                the students succeeded", which clause
                                is dependent?
                            """.trimIndent(),

                        options = listOf(
                            "The students succeeded",
                            "Although the exam was difficult",
                            "Both clauses are independent",
                            "Neither clause is grammatical"
                        ),

                        correctAnswerIndex = 1,

                        explanation = """
                                "Although the exam was difficult" is a
                                dependent clause introduced by "although".
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u7_l3_q5",

                        question = """
                                Why are coordination and subordination
                                important in academic writing?
                            """.trimIndent(),

                        options = listOf(
                            "They eliminate all verbs.",
                            "They help organize relationships between ideas and clauses.",
                            "They replace vocabulary.",
                            "They make every sentence shorter."
                        ),

                        correctAnswerIndex = 1,

                        explanation = """
                                Coordination and subordination help writers
                                organize relationships between ideas and
                                construct more developed sentence structures.
                            """.trimIndent()
                    )
                )
            )
        )
    ),
        // =========================================================
        // UNIT 8 — QUESTIONS AND NEGATION
        // =========================================================

        CourseUnit(
            id = "grammar1_unit8",

            title = "Unit 8 — Questions and Negation",

            description = """
                Study of interrogative and negative constructions in
                English, including yes/no questions, wh-questions,
                auxiliary inversion and negation.
            """.trimIndent(),

            lessons = listOf(

                // =====================================================
                // LESSON 1
                // =====================================================

                Lesson(
                    id = "grammar1_u8_l1",

                    title = "Lesson 1 — Declarative and Interrogative Clauses",

                    objective = """
                        Distinguish declarative and interrogative clauses
                        and explain how English forms questions.
                    """.trimIndent(),

                    content = """
                        Declarative clauses are typically used to make
                        statements or present information.

                        Example:

                        The students study English.

                        Interrogative clauses are typically used to ask
                        questions.

                        Example:

                        Do the students study English?

                        English often forms questions through changes in
                        word order and the use of auxiliary verbs.

                        Compare:

                        She studies English.

                        Does she study English?

                        In the question, "does" functions as the auxiliary
                        and the lexical verb appears in its base form.

                        When an auxiliary is already present, it can move
                        before the subject.

                        Example:

                        She is studying English.

                        Is she studying English?

                        This process is commonly described as subject-
                        auxiliary inversion.
                    """.trimIndent(),

                    examples = listOf(

                        Example(
                            english = "The students study English.",
                            french = "Les étudiants étudient l'anglais."
                        ),

                        Example(
                            english = "Do the students study English?",
                            french = "Les étudiants étudient-ils l'anglais ?"
                        ),

                        Example(
                            english = "She is studying English.",
                            french = "Elle étudie l'anglais."
                        ),

                        Example(
                            english = "Is she studying English?",
                            french = "Est-elle en train d'étudier l'anglais ?"
                        )
                    ),

                    keyTerms = listOf(
                        "declarative",
                        "interrogative",
                        "question",
                        "auxiliary",
                        "inversion",
                        "subject",
                        "lexical verb"
                    ),

                    questions = listOf(

                        QuizQuestion(
                            id = "g1_u8_l1_q1",

                            question = """
                                Which sentence is declarative?
                            """.trimIndent(),

                            options = listOf(
                                "Do the students study English?",
                                "The students study English.",
                                "Are the students studying English?",
                                "Why do the students study English?"
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                "The students study English" is a
                                declarative clause presenting information.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u8_l1_q2",

                            question = """
                                Which sentence is interrogative?
                            """.trimIndent(),

                            options = listOf(
                                "The students study English.",
                                "The students studied English.",
                                "Do the students study English?",
                                "The students are students."
                            ),

                            correctAnswerIndex = 2,

                            explanation = """
                                "Do the students study English?" is an
                                interrogative clause used to ask a question.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u8_l1_q3",

                            question = """
                                What is the function of "does" in
                                "Does she study English?"
                            """.trimIndent(),

                            options = listOf(
                                "Subject",
                                "Auxiliary",
                                "Object",
                                "Determiner"
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                "Does" is the auxiliary used to form
                                the present simple question.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u8_l1_q4",

                            question = """
                                Which sentence illustrates subject-
                                auxiliary inversion?
                            """.trimIndent(),

                            options = listOf(
                                "She is studying English.",
                                "She studies English.",
                                "Is she studying English?",
                                "She studied English."
                            ),

                            correctAnswerIndex = 2,

                            explanation = """
                                In "Is she studying English?", the auxiliary
                                "is" occurs before the subject "she".
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u8_l1_q5",

                            question = """
                                What usually happens to the lexical verb
                                in "Does she study English?"
                            """.trimIndent(),

                            options = listOf(
                                "It becomes a noun.",
                                "It changes to the past tense.",
                                "It appears in its base form.",
                                "It disappears."
                            ),

                            correctAnswerIndex = 2,

                            explanation = """
                                After the auxiliary "does", the lexical
                                verb appears in its base form: "study".
                            """.trimIndent()
                        )
                    )
                ),

                // =====================================================
                // LESSON 2
                // =====================================================

                Lesson(
                    id = "grammar1_u8_l2",

                    title = "Lesson 2 — Yes/No Questions and Wh-Questions",

                    objective = """
                        Distinguish yes/no questions from wh-questions
                        and identify their structural characteristics.
                    """.trimIndent(),

                    content = """
                        Yes/no questions are questions that commonly
                        allow an answer such as "yes" or "no".

                        Example:

                        Do you study English?

                        Wh-questions contain an interrogative word such
                        as:

                        who
                        what
                        where
                        when
                        why
                        how

                        Examples:

                        Who studies English?

                        What do you study?

                        Where do you study?

                        Why do you study English?

                        How do you study?

                        When the wh-expression functions as an object
                        or another non-subject element, an auxiliary is
                        commonly used and subject-auxiliary inversion occurs.

                        Example:

                        What do you study?

                        However, when the wh-expression itself functions
                        as the subject, inversion is not normally used.

                        Example:

                        Who studies English?

                        This distinction is important in syntactic analysis.
                    """.trimIndent(),

                    examples = listOf(

                        Example(
                            english = "Do you study English?",
                            french = "Étudiez-vous l'anglais ?"
                        ),

                        Example(
                            english = "What do you study?",
                            french = "Qu'étudiez-vous ?"
                        ),

                        Example(
                            english = "Where do you study?",
                            french = "Où étudiez-vous ?"
                        ),

                        Example(
                            english = "Who studies English?",
                            french = "Qui étudie l'anglais ?"
                        )
                    ),

                    keyTerms = listOf(
                        "yes/no question",
                        "wh-question",
                        "who",
                        "what",
                        "where",
                        "why",
                        "how",
                        "subject question",
                        "object question"
                    ),

                    questions = listOf(

                        QuizQuestion(
                            id = "g1_u8_l2_q1",

                            question = """
                                Which sentence is a yes/no question?
                            """.trimIndent(),

                            options = listOf(
                                "Why do you study English?",
                                "Where do you study?",
                                "Do you study English?",
                                "What do you study?"
                            ),

                            correctAnswerIndex = 2,

                            explanation = """
                                "Do you study English?" can be answered
                                with "yes" or "no".
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u8_l2_q2",

                            question = """
                                Which word is a wh-word?
                            """.trimIndent(),

                            options = listOf(
                                "and",
                                "because",
                                "where",
                                "the"
                            ),

                            correctAnswerIndex = 2,

                            explanation = """
                                "Where" is an interrogative wh-word used
                                to ask about location.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u8_l2_q3",

                            question = """
                                In "What do you study?", what does
                                "what" represent?
                            """.trimIndent(),

                            options = listOf(
                                "The subject",
                                "The object",
                                "The auxiliary",
                                "The verb"
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                "What" represents the object of the
                                verb "study" in this question.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u8_l2_q4",

                            question = """
                                Which sentence contains a wh-expression
                                functioning as the subject?
                            """.trimIndent(),

                            options = listOf(
                                "What do you study?",
                                "Where do you study?",
                                "Who studies English?",
                                "Why do you study?"
                            ),

                            correctAnswerIndex = 2,

                            explanation = """
                                In "Who studies English?", "who" functions
                                as the subject, so ordinary auxiliary
                                inversion is not required.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u8_l2_q5",

                            question = """
                                Which word asks about reason?
                            """.trimIndent(),

                            options = listOf(
                                "who",
                                "where",
                                "why",
                                "when"
                            ),

                            correctAnswerIndex = 2,

                            explanation = """
                                "Why" is used to ask about reason.
                            """.trimIndent()
                        )
                    )
                ),

                // =====================================================
                // LESSON 3
                // =====================================================

                Lesson(
                    id = "grammar1_u8_l3",

                    title = "Lesson 3 — Negation in English",

                    objective = """
                        Explain how English forms negative clauses and
                        identify the role of negative auxiliaries and
                        negative expressions.
                    """.trimIndent(),

                    content = """
                        Negation allows speakers and writers to indicate
                        that a proposition does not hold or that an event
                        or situation is not occurring.

                        English commonly uses the negative marker "not"
                        together with an auxiliary.

                        Example:

                        She is not studying.

                        When there is no auxiliary in the affirmative
                        clause, English commonly uses "do" as a support
                        auxiliary.

                        Compare:

                        She studies English.

                        She does not study English.

                        In the negative construction, "does" carries the
                        grammatical marking and the lexical verb appears
                        in its base form.

                        Past simple negation works similarly:

                        She studied English.

                        She did not study English.

                        English also contains negative words and expressions
                        such as:

                        nobody
                        nothing
                        never
                        no
                        neither

                        Negation is therefore expressed through several
                        grammatical and lexical resources.
                    """.trimIndent(),

                    examples = listOf(

                        Example(
                            english = "She is not studying English.",
                            french = "Elle n'étudie pas l'anglais."
                        ),

                        Example(
                            english = "She does not study English.",
                            french = "Elle n'étudie pas l'anglais."
                        ),

                        Example(
                            english = "She did not study English.",
                            french = "Elle n'a pas étudié l'anglais."
                        ),

                        Example(
                            english = "Nobody understood the question.",
                            french = "Personne n'a compris la question."
                        )
                    ),

                    keyTerms = listOf(
                        "negation",
                        "negative",
                        "not",
                        "do-support",
                        "negative marker",
                        "nobody",
                        "nothing",
                        "never"
                    ),

                    questions = listOf(

                        QuizQuestion(
                            id = "g1_u8_l3_q1",

                            question = """
                                Which word is the main negative marker
                                in "She is not studying"?
                            """.trimIndent(),

                            options = listOf(
                                "she",
                                "is",
                                "not",
                                "studying"
                            ),

                            correctAnswerIndex = 2,

                            explanation = """
                                "Not" is the negative marker in the
                                construction "is not studying".
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u8_l3_q2",

                            question = """
                                Which sentence is correctly negated
                                in the present simple?
                            """.trimIndent(),

                            options = listOf(
                                "She not studies English.",
                                "She does not study English.",
                                "She does not studies English.",
                                "She not study English."
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                Present simple negation uses "does not"
                                with the lexical verb in its base form.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u8_l3_q3",

                            question = """
                                Which sentence correctly forms a past
                                simple negative?
                            """.trimIndent(),

                            options = listOf(
                                "She did not study English.",
                                "She did not studied English.",
                                "She not studied English.",
                                "She does not studied English."
                            ),

                            correctAnswerIndex = 0,

                            explanation = """
                                Past simple negation uses "did not"
                                followed by the base form "study".
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u8_l3_q4",

                            question = """
                                What happens to the lexical verb after
                                "does not"?
                            """.trimIndent(),

                            options = listOf(
                                "It takes the past tense.",
                                "It takes the third-person -s ending.",
                                "It appears in the base form.",
                                "It becomes an adjective."
                            ),

                            correctAnswerIndex = 2,

                            explanation = """
                                After "does not", the lexical verb appears
                                in its base form: "does not study".
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u8_l3_q5",

                            question = """
                                Which word expresses negative reference
                                to people?
                            """.trimIndent(),

                            options = listOf(
                                "nothing",
                                "nobody",
                                "never",
                                "where"
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                "Nobody" is a negative expression referring
                                to people.
                            """.trimIndent()
                        )
                    )
                )
            )
        ),
        // =========================================================
        // UNIT 9 — MODALITY
        // =========================================================

        CourseUnit(
        id = "grammar1_unit9",

        title = "Unit 9 — Modality",

        description = """
                Study of modal auxiliaries and modal meanings in English,
                including ability, possibility, permission, obligation,
                necessity and prediction.
            """.trimIndent(),

        lessons = listOf(

            // =====================================================
            // LESSON 1
            // =====================================================

            Lesson(
                id = "grammar1_u9_l1",

                title = "Lesson 1 — Modal Auxiliaries",

                objective = """
                        Identify the major modal auxiliaries in English
                        and explain their grammatical characteristics.
                    """.trimIndent(),

                content = """
                        Modal auxiliaries form an important part of the
                        English auxiliary system.

                        Common modal auxiliaries include:

                        can
                        could
                        may
                        might
                        must
                        shall
                        should
                        will
                        would

                        Modal auxiliaries are used to express meanings
                        such as ability, possibility, permission,
                        obligation, prediction and willingness.

                        Modal auxiliaries have several important
                        grammatical characteristics.

                        They are followed by the base form of the verb.

                        Example:

                        She can speak English.

                        Not:

                        She can speaks English.

                        Modal auxiliaries do not normally take the
                        third-person singular -s ending.

                        Example:

                        He can swim.

                        Not:

                        He cans swim.

                        They also participate in question and negative
                        constructions without requiring "do".

                        Example:

                        Can she swim?

                        She cannot swim.

                        Modal auxiliaries therefore have both grammatical
                        and semantic functions.
                    """.trimIndent(),

                examples = listOf(

                    Example(
                        english = "She can speak English.",
                        french = "Elle peut parler anglais."
                    ),

                    Example(
                        english = "They might arrive late.",
                        french = "Ils pourraient arriver en retard."
                    ),

                    Example(
                        english = "You should study regularly.",
                        french = "Tu devrais étudier régulièrement."
                    ),

                    Example(
                        english = "Students must complete the assignment.",
                        french = "Les étudiants doivent terminer le devoir."
                    )
                ),

                keyTerms = listOf(
                    "modal auxiliary",
                    "can",
                    "could",
                    "may",
                    "might",
                    "must",
                    "should",
                    "will",
                    "would"
                ),

                questions = listOf(

                    QuizQuestion(
                        id = "g1_u9_l1_q1",

                        question = """
                                Which of the following is a modal auxiliary?
                            """.trimIndent(),

                        options = listOf(
                            "study",
                            "must",
                            "student",
                            "quickly"
                        ),

                        correctAnswerIndex = 1,

                        explanation = """
                                "Must" is a modal auxiliary used to express
                                meanings such as obligation and necessity.
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u9_l1_q2",

                        question = """
                                What form follows a modal auxiliary in a
                                standard English construction?
                            """.trimIndent(),

                        options = listOf(
                            "The past participle only",
                            "The base form of the verb",
                            "The third-person -s form",
                            "A noun"
                        ),

                        correctAnswerIndex = 1,

                        explanation = """
                                Modal auxiliaries are normally followed by
                                the base form of the lexical verb.
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u9_l1_q3",

                        question = """
                                Which sentence is grammatically correct?
                            """.trimIndent(),

                        options = listOf(
                            "She can speaks English.",
                            "She can speaking English.",
                            "She can speak English.",
                            "She can spoke English."
                        ),

                        correctAnswerIndex = 2,

                        explanation = """
                                The modal "can" is followed by the base
                                form "speak".
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u9_l1_q4",

                        question = """
                                Which sentence correctly forms a question
                                with a modal auxiliary?
                            """.trimIndent(),

                        options = listOf(
                            "Does she can swim?",
                            "Can she swim?",
                            "Do can she swim?",
                            "Can does she swim?"
                        ),

                        correctAnswerIndex = 1,

                        explanation = """
                                The modal auxiliary "can" moves before
                                the subject in the interrogative construction.
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u9_l1_q5",

                        question = """
                                Which meaning can modal auxiliaries express?
                            """.trimIndent(),

                        options = listOf(
                            "Only plural number",
                            "Ability, possibility, permission and obligation",
                            "Only past tense",
                            "Only comparison"
                        ),

                        correctAnswerIndex = 1,

                        explanation = """
                                Modal auxiliaries express a range of
                                meanings including ability, possibility,
                                permission and obligation.
                            """.trimIndent()
                    )
                )
            ),

            // =====================================================
            // LESSON 2
            // =====================================================

            Lesson(
                id = "grammar1_u9_l2",

                title = "Lesson 2 — Possibility, Ability and Permission",

                objective = """
                        Analyse modal expressions of possibility, ability
                        and permission and distinguish their meanings
                        in context.
                    """.trimIndent(),

                content = """
                        Modal auxiliaries can express different semantic
                        meanings depending on context.

                        "Can" commonly expresses present ability.

                        Example:

                        She can speak three languages.

                        "Could" can refer to ability in the past.

                        Example:

                        She could read when she was four.

                        "Can" and "may" can also express permission.

                        Example:

                        You can leave now.

                        You may leave now.

                        In formal contexts, "may" is often associated
                        with permission.

                        "May" and "might" commonly express possibility.

                        Example:

                        The students may arrive late.

                        The students might arrive late.

                        "Might" often presents a weaker or less certain
                        possibility than "may", although interpretation
                        depends on context.

                        Modal meaning is therefore influenced by both
                        the modal auxiliary and the communicative context.
                    """.trimIndent(),

                examples = listOf(

                    Example(
                        english = "She can speak three languages.",
                        french = "Elle peut parler trois langues."
                    ),

                    Example(
                        english = "She could read when she was four.",
                        french = "Elle savait lire quand elle avait quatre ans."
                    ),

                    Example(
                        english = "You may leave now.",
                        french = "Vous pouvez partir maintenant."
                    ),

                    Example(
                        english = "The students might arrive late.",
                        french = "Les étudiants pourraient arriver en retard."
                    )
                ),

                keyTerms = listOf(
                    "ability",
                    "possibility",
                    "permission",
                    "can",
                    "could",
                    "may",
                    "might",
                    "certainty"
                ),

                questions = listOf(

                    QuizQuestion(
                        id = "g1_u9_l2_q1",

                        question = """
                                Which modal commonly expresses present
                                ability?
                            """.trimIndent(),

                        options = listOf(
                            "can",
                            "must",
                            "should",
                            "would"
                        ),

                        correctAnswerIndex = 0,

                        explanation = """
                                "Can" commonly expresses present ability.
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u9_l2_q2",

                        question = """
                                Which modal can express past ability?
                            """.trimIndent(),

                        options = listOf(
                            "might",
                            "could",
                            "must",
                            "shall"
                        ),

                        correctAnswerIndex = 1,

                        explanation = """
                                "Could" can express ability in the past.
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u9_l2_q3",

                        question = """
                                Which sentence expresses possibility?
                            """.trimIndent(),

                        options = listOf(
                            "She must study.",
                            "She might arrive late.",
                            "She can swim.",
                            "She should study."
                        ),

                        correctAnswerIndex = 1,

                        explanation = """
                                "Might arrive late" expresses a possible
                                future event.
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u9_l2_q4",

                        question = """
                                Which sentence most clearly expresses
                                formal permission?
                            """.trimIndent(),

                        options = listOf(
                            "You may leave now.",
                            "You must leave now.",
                            "You might leave now.",
                            "You should leave now."
                        ),

                        correctAnswerIndex = 0,

                        explanation = """
                                "May" is commonly used to express
                                permission, especially in formal contexts.
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u9_l2_q5",

                        question = """
                                Which statement best describes "might"
                                in many contexts?
                            """.trimIndent(),

                        options = listOf(
                            "It expresses certain necessity.",
                            "It can express a relatively weak possibility.",
                            "It always expresses past ability.",
                            "It always expresses permission."
                        ),

                        correctAnswerIndex = 1,

                        explanation = """
                                "Might" can express a relatively weak or
                                uncertain possibility, depending on context.
                            """.trimIndent()
                    )
                )
            ),

            // =====================================================
            // LESSON 3
            // =====================================================

            Lesson(
                id = "grammar1_u9_l3",

                title = "Lesson 3 — Obligation, Necessity and Prediction",

                objective = """
                        Distinguish modal expressions of obligation,
                        necessity, advice and prediction in English.
                    """.trimIndent(),

                content = """
                        Modality also allows speakers to express obligation,
                        necessity, advice and prediction.

                        "Must" commonly expresses strong obligation or
                        necessity.

                        Example:

                        Students must complete the assignment.

                        "Have to" can also express necessity or obligation.

                        Example:

                        Students have to complete the assignment.

                        "Should" commonly expresses advice or expectation.

                        Example:

                        Students should review their notes.

                        "Will" can express prediction.

                        Example:

                        The students will arrive tomorrow.

                        "Would" has several uses, including hypothetical
                        meaning, habitual past behaviour and polite requests.

                        Example:

                        Would you help me?

                        The interpretation of a modal expression depends
                        on grammatical form, context and communicative purpose.
                    """.trimIndent(),

                examples = listOf(

                    Example(
                        english = "Students must complete the assignment.",
                        french = "Les étudiants doivent terminer le devoir."
                    ),

                    Example(
                        english = "Students have to attend the examination.",
                        french = "Les étudiants doivent assister à l'examen."
                    ),

                    Example(
                        english = "You should review your notes.",
                        french = "Tu devrais revoir tes notes."
                    ),

                    Example(
                        english = "The students will arrive tomorrow.",
                        french = "Les étudiants arriveront demain."
                    )
                ),

                keyTerms = listOf(
                    "obligation",
                    "necessity",
                    "advice",
                    "prediction",
                    "must",
                    "have to",
                    "should",
                    "will",
                    "would"
                ),

                questions = listOf(

                    QuizQuestion(
                        id = "g1_u9_l3_q1",

                        question = """
                                Which modal commonly expresses strong
                                obligation?
                            """.trimIndent(),

                        options = listOf(
                            "might",
                            "must",
                            "could",
                            "would"
                        ),

                        correctAnswerIndex = 1,

                        explanation = """
                                "Must" commonly expresses strong obligation
                                or necessity.
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u9_l3_q2",

                        question = """
                                Which expression can also express
                                necessity or obligation?
                            """.trimIndent(),

                        options = listOf(
                            "have to",
                            "might",
                            "could",
                            "would"
                        ),

                        correctAnswerIndex = 0,

                        explanation = """
                                "Have to" commonly expresses necessity
                                or obligation.
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u9_l3_q3",

                        question = """
                                Which modal commonly expresses advice?
                            """.trimIndent(),

                        options = listOf(
                            "must",
                            "should",
                            "might",
                            "will"
                        ),

                        correctAnswerIndex = 1,

                        explanation = """
                                "Should" commonly expresses advice or
                                recommendation.
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u9_l3_q4",

                        question = """
                                Which sentence expresses a prediction?
                            """.trimIndent(),

                        options = listOf(
                            "You should study.",
                            "Students must study.",
                            "The students will arrive tomorrow.",
                            "Students have to study."
                        ),

                        correctAnswerIndex = 2,

                        explanation = """
                                "Will arrive tomorrow" expresses a
                                prediction about a future event.
                            """.trimIndent()
                    ),

                    QuizQuestion(
                        id = "g1_u9_l3_q5",

                        question = """
                                Which sentence contains "would" in a
                                polite request?
                            """.trimIndent(),

                        options = listOf(
                            "Would you help me?",
                            "You must help me.",
                            "You should help me.",
                            "You will help me."
                        ),

                        correctAnswerIndex = 0,

                        explanation = """
                                "Would you help me?" is a common polite
                                request using the modal "would".
                            """.trimIndent()
                    )
                )
            )
        )
    ),
    // =========================================================
    // UNIT 10 — AGREEMENT AND COMMON ERRORS
    // =========================================================

        CourseUnit(
            id = "grammar1_unit10",

            title = "Unit 10 — Agreement and Common Errors",

            description = """
                Study of grammatical agreement in English, including
                subject–verb agreement, pronoun agreement and common
                grammatical errors in academic writing.
            """.trimIndent(),

            lessons = listOf(

                // =====================================================
                // LESSON 1
                // =====================================================

                Lesson(
                    id = "grammar1_u10_l1",

                    title = "Lesson 1 — Subject–Verb Agreement",

                    objective = """
                        Identify the principles of subject–verb agreement
                        and apply them to common English clause structures.
                    """.trimIndent(),

                    content = """
                        Subject–verb agreement refers to the relationship
                        between the grammatical number of the subject and
                        the form of the verb.

                        In the present simple, third-person singular
                        subjects normally take a verb ending in -s.

                        Example:

                        The student studies English.

                        Plural subjects normally occur with the base form.

                        Example:

                        The students study English.

                        The verb must agree with the grammatical subject,
                        not simply with the nearest noun.

                        Example:

                        The list of students is on the table.

                        The subject is "the list", not "students".

                        Agreement also applies to forms of "be".

                        Example:

                        The student is ready.

                        The students are ready.

                        Correct agreement is especially important in
                        formal and academic writing.
                    """.trimIndent(),

                    examples = listOf(

                        Example(
                            english = "The student studies English.",
                            french = "L'étudiant étudie l'anglais."
                        ),

                        Example(
                            english = "The students study English.",
                            french = "Les étudiants étudient l'anglais."
                        ),

                        Example(
                            english = "The list of students is on the table.",
                            french = "La liste des étudiants est sur la table."
                        ),

                        Example(
                            english = "The students are ready.",
                            french = "Les étudiants sont prêts."
                        )
                    ),

                    keyTerms = listOf(
                        "agreement",
                        "subject–verb agreement",
                        "singular",
                        "plural",
                        "third-person singular",
                        "grammatical number"
                    ),

                    questions = listOf(

                        QuizQuestion(
                            id = "g1_u10_l1_q1",

                            question = """
                                Which sentence shows correct subject–verb
                                agreement?
                            """.trimIndent(),

                            options = listOf(
                                "The student study English.",
                                "The student studies English.",
                                "The student studying English.",
                                "The student are English."
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                "The student" is a third-person singular
                                subject, so the present simple verb is
                                "studies".
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u10_l1_q2",

                            question = """
                                Which sentence correctly agrees with
                                the plural subject "students"?
                            """.trimIndent(),

                            options = listOf(
                                "The students studies English.",
                                "The students study English.",
                                "The students studying English.",
                                "The students is studying English."
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                A plural subject such as "students"
                                takes the base form "study" in the
                                present simple.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u10_l1_q3",

                            question = """
                                In "The list of students is on the table",
                                what determines the form "is"?
                            """.trimIndent(),

                            options = listOf(
                                "students",
                                "of",
                                "list",
                                "table"
                            ),

                            correctAnswerIndex = 2,

                            explanation = """
                                The grammatical subject is "the list",
                                which is singular.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u10_l1_q4",

                            question = """
                                Which sentence contains incorrect
                                subject–verb agreement?
                            """.trimIndent(),

                            options = listOf(
                                "The researcher analyses the data.",
                                "The researchers analyse the data.",
                                "The student write the essay.",
                                "The students write the essay."
                            ),

                            correctAnswerIndex = 2,

                            explanation = """
                                "The student" is singular, so the correct
                                form is "writes".
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u10_l1_q5",

                            question = """
                                Why is subject–verb agreement important
                                in academic writing?
                            """.trimIndent(),

                            options = listOf(
                                "It helps maintain grammatical accuracy.",
                                "It eliminates all vocabulary.",
                                "It changes nouns into verbs.",
                                "It removes the need for punctuation."
                            ),

                            correctAnswerIndex = 0,

                            explanation = """
                                Correct agreement contributes to grammatical
                                accuracy and clarity in formal writing.
                            """.trimIndent()
                        )
                    )
                ),

                // =====================================================
                // LESSON 2
                // =====================================================

                Lesson(
                    id = "grammar1_u10_l2",

                    title = "Lesson 2 — Pronoun Agreement and Reference",

                    objective = """
                        Explain pronoun agreement and identify clear and
                        unclear patterns of pronoun reference.
                    """.trimIndent(),

                    content = """
                        Pronouns normally refer to nouns or noun phrases
                        and can contribute to cohesion in discourse.

                        A pronoun should normally agree with its antecedent
                        in relevant grammatical features such as number
                        and, where appropriate, person and gender.

                        Example:

                        The student submitted her essay.

                        "Her" refers back to "the student".

                        With plural antecedents, plural pronouns are normally
                        expected.

                        Example:

                        The students submitted their essays.

                        Pronoun reference should also be clear.

                        Example:

                        When Sarah spoke to Mary, she was nervous.

                        This sentence may be ambiguous because "she" could
                        potentially refer to Sarah or Mary.

                        Clear academic writing avoids unnecessary ambiguity
                        in pronoun reference.
                    """.trimIndent(),

                    examples = listOf(

                        Example(
                            english = "The student submitted her essay.",
                            french = "L'étudiante a remis sa dissertation."
                        ),

                        Example(
                            english = "The students submitted their essays.",
                            french = "Les étudiants ont remis leurs dissertations."
                        ),

                        Example(
                            english = "The researcher presented his findings.",
                            french = "Le chercheur a présenté ses résultats."
                        ),

                        Example(
                            english = "The committee published its report.",
                            french = "Le comité a publié son rapport."
                        )
                    ),

                    keyTerms = listOf(
                        "pronoun",
                        "antecedent",
                        "agreement",
                        "reference",
                        "cohesion",
                        "ambiguity"
                    ),

                    questions = listOf(

                        QuizQuestion(
                            id = "g1_u10_l2_q1",

                            question = """
                                Which pronoun correctly refers to the
                                plural noun "students"?
                            """.trimIndent(),

                            options = listOf(
                                "his",
                                "her",
                                "their",
                                "its"
                            ),

                            correctAnswerIndex = 2,

                            explanation = """
                                "Students" is plural, so "their" is the
                                appropriate plural possessive determiner
                                in this example.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u10_l2_q2",

                            question = """
                                What is an antecedent?
                            """.trimIndent(),

                            options = listOf(
                                "A punctuation mark",
                                "The noun or noun phrase to which a pronoun refers",
                                "A type of verb",
                                "A sentence-final adverb"
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                An antecedent is the noun or noun phrase
                                that provides the reference for a pronoun.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u10_l2_q3",

                            question = """
                                Which sentence contains a potentially
                                ambiguous pronoun reference?
                            """.trimIndent(),

                            options = listOf(
                                "Sarah submitted her essay.",
                                "The students submitted their essays.",
                                "When Sarah spoke to Mary, she was nervous.",
                                "The lecturer published his article."
                            ),

                            correctAnswerIndex = 2,

                            explanation = """
                                In the third sentence, "she" may refer to
                                either Sarah or Mary, creating ambiguity.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u10_l2_q4",

                            question = """
                                Which sentence contains clear pronoun
                                reference?
                            """.trimIndent(),

                            options = listOf(
                                "When Sara spoke to Amina, she smiled.",
                                "The students submitted their essays.",
                                "When John met Ali, he was tired.",
                                "After Mary called Sarah, she left."
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                "Their" clearly refers to the plural
                                antecedent "the students".
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u10_l2_q5",

                            question = """
                                Why is clear pronoun reference important
                                in academic writing?
                            """.trimIndent(),

                            options = listOf(
                                "It improves clarity and cohesion.",
                                "It removes all nouns.",
                                "It changes tense.",
                                "It prevents the use of verbs."
                            ),

                            correctAnswerIndex = 0,

                            explanation = """
                                Clear pronoun reference helps readers
                                understand relationships between ideas
                                and maintain textual cohesion.
                            """.trimIndent()
                        )
                    )
                ),

                // =====================================================
                // LESSON 3
                // =====================================================

                Lesson(
                    id = "grammar1_u10_l3",

                    title = "Lesson 3 — Common Grammatical Errors and Editing",

                    objective = """
                        Identify frequent grammatical errors and apply
                        systematic editing strategies to improve English
                        sentences.
                    """.trimIndent(),

                    content = """
                        Effective academic writing requires grammatical
                        accuracy as well as appropriate organization and
                        vocabulary.

                        Common grammatical problems include:

                        subject–verb agreement errors

                        incorrect verb forms

                        article errors

                        pronoun agreement problems

                        incorrect preposition choices

                        sentence fragments

                        run-on sentences

                        Editing involves identifying the grammatical
                        problem, analysing its cause and revising the
                        sentence.

                        Example:

                        Incorrect:
                        The student study English.

                        Correct:
                        The student studies English.

                        Another example:

                        Incorrect:
                        She don't understand the article.

                        Correct:
                        She doesn't understand the article.

                        Academic editing should therefore be systematic
                        rather than based only on intuition.
                    """.trimIndent(),

                    examples = listOf(

                        Example(
                            english = "Incorrect: The student study English.",
                            french = "Incorrect : L'étudiant étudie l'anglais."
                        ),

                        Example(
                            english = "Correct: The student studies English.",
                            french = "Correct : L'étudiant étudie l'anglais."
                        ),

                        Example(
                            english = "Incorrect: She don't understand the article.",
                            french = "Incorrect : Elle ne comprend pas l'article."
                        ),

                        Example(
                            english = "Correct: She doesn't understand the article.",
                            french = "Correct : Elle ne comprend pas l'article."
                        )
                    ),

                    keyTerms = listOf(
                        "error",
                        "editing",
                        "revision",
                        "agreement",
                        "verb form",
                        "article",
                        "fragment",
                        "run-on sentence"
                    ),

                    questions = listOf(

                        QuizQuestion(
                            id = "g1_u10_l3_q1",

                            question = """
                                Which sentence is grammatically correct?
                            """.trimIndent(),

                            options = listOf(
                                "The student study English.",
                                "The student studies English.",
                                "The student studying English.",
                                "The student studies English?"
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                "The student studies English" has correct
                                subject–verb agreement.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u10_l3_q2",

                            question = """
                                Which sentence correctly uses the negative
                                form of "do" with "she"?
                            """.trimIndent(),

                            options = listOf(
                                "She don't understand.",
                                "She doesn't understand.",
                                "She doesn't understands.",
                                "She not understand."
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                The correct third-person singular negative
                                is "doesn't understand".
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u10_l3_q3",

                            question = """
                                Which of the following is an editing strategy?
                            """.trimIndent(),

                            options = listOf(
                                "Ignoring grammatical patterns",
                                "Identifying the error, analysing it and revising the sentence",
                                "Removing all verbs",
                                "Replacing every noun"
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                Effective editing involves identifying,
                                analysing and correcting the grammatical problem.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u10_l3_q4",

                            question = """
                                Which problem is illustrated by
                                "The students is ready"?
                            """.trimIndent(),

                            options = listOf(
                                "Article error",
                                "Pronoun reference",
                                "Subject–verb agreement",
                                "Preposition error"
                            ),

                            correctAnswerIndex = 2,

                            explanation = """
                                "Students" is plural, so the verb should
                                be "are", not "is".
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u10_l3_q5",

                            question = """
                                Why should academic editing be systematic?
                            """.trimIndent(),

                            options = listOf(
                                "Because grammatical errors can have recurring patterns.",
                                "Because vocabulary is unnecessary.",
                                "Because every sentence must be identical.",
                                "Because grammar cannot be analysed."
                            ),

                            correctAnswerIndex = 0,

                            explanation = """
                                Many grammatical errors follow recurring
                                patterns that can be identified and corrected
                                through systematic analysis.
                            """.trimIndent()
                        )
                    )
                )
            )
        ),
        // =========================================================
        // UNIT 11 — EMBEDDED AND RELATIVE CLAUSES
        // =========================================================

        CourseUnit(
            id = "grammar1_unit11",

            title = "Unit 11 — Embedded and Relative Clauses",

            description = """
                Study of relative clauses, noun clauses and adverbial
                clauses, with attention to their grammatical functions
                and their role in complex English sentences.
            """.trimIndent(),

            lessons = listOf(

                // =====================================================
                // LESSON 1
                // =====================================================

                Lesson(
                    id = "grammar1_u11_l1",

                    title = "Lesson 1 — Relative Clauses",

                    objective = """
                        Identify relative clauses and explain how relative
                        words connect clauses to noun phrases.
                    """.trimIndent(),

                    content = """
                        A relative clause is a subordinate clause that
                        provides additional information about a noun or
                        noun phrase.

                        Consider:

                        The student who wrote the essay received a good mark.

                        The clause "who wrote the essay" modifies
                        the noun "student".

                        Common relative words include:

                        who
                        which
                        that
                        whose
                        where
                        when

                        "Who" commonly refers to people.

                        Example:

                        The researcher who conducted the study published
                        the results.

                        "Which" commonly refers to things.

                        Example:

                        The article which we discussed was published
                        recently.

                        "That" can refer to people or things in many
                        defining relative clauses.

                        Example:

                        The book that I bought is useful.

                        Relative clauses can therefore be used to integrate
                        information into a larger noun phrase.
                    """.trimIndent(),

                    examples = listOf(

                        Example(
                            english = "The student who wrote the essay received a good mark.",
                            french = "L'étudiant qui a écrit la dissertation a obtenu une bonne note."
                        ),

                        Example(
                            english = "The article which we discussed was useful.",
                            french = "L'article dont nous avons discuté était utile."
                        ),

                        Example(
                            english = "The book that I bought is useful.",
                            french = "Le livre que j'ai acheté est utile."
                        ),

                        Example(
                            english = "The researcher whose work was published received an award.",
                            french = "Le chercheur dont le travail a été publié a reçu un prix."
                        )
                    ),

                    keyTerms = listOf(
                        "relative clause",
                        "relative pronoun",
                        "antecedent",
                        "who",
                        "which",
                        "that",
                        "whose",
                        "modifier"
                    ),

                    questions = listOf(

                        QuizQuestion(
                            id = "g1_u11_l1_q1",

                            question = """
                                Which expression introduces a relative
                                clause referring to a person?
                            """.trimIndent(),

                            options = listOf(
                                "who",
                                "which",
                                "because",
                                "although"
                            ),

                            correctAnswerIndex = 0,

                            explanation = """
                                "Who" commonly introduces a relative clause
                                referring to a person.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u11_l1_q2",

                            question = """
                                In "The student who wrote the essay
                                received a good mark", what does the
                                relative clause modify?
                            """.trimIndent(),

                            options = listOf(
                                "essay",
                                "mark",
                                "student",
                                "received"
                            ),

                            correctAnswerIndex = 2,

                            explanation = """
                                "Who wrote the essay" modifies the noun
                                "student".
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u11_l1_q3",

                            question = """
                                Which relative word commonly refers
                                to things?
                            """.trimIndent(),

                            options = listOf(
                                "who",
                                "which",
                                "whom only",
                                "because"
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                "Which" commonly introduces relative
                                clauses referring to things.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u11_l1_q4",

                            question = """
                                Which sentence contains a relative clause?
                            """.trimIndent(),

                            options = listOf(
                                "The student studies English.",
                                "The student who studies English works hard.",
                                "The student studies carefully.",
                                "The student is intelligent."
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                "Who studies English" is a relative clause
                                modifying "the student".
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u11_l1_q5",

                            question = """
                                In "The researcher whose work was published
                                received an award", what is "whose"?
                            """.trimIndent(),

                            options = listOf(
                                "A determiner functioning inside a relative clause",
                                "A main verb",
                                "A conjunction of coordination",
                                "A preposition"
                            ),

                            correctAnswerIndex = 0,

                            explanation = """
                                "Whose" introduces the relative clause and
                                expresses a possessive relationship.
                            """.trimIndent()
                        )
                    )
                ),

                // =====================================================
                // LESSON 2
                // =====================================================

                Lesson(
                    id = "grammar1_u11_l2",

                    title = "Lesson 2 — Noun Clauses",

                    objective = """
                        Identify noun clauses and explain how they function
                        as subjects, objects or complements.
                    """.trimIndent(),

                    content = """
                        A noun clause is a subordinate clause that functions
                        in a sentence in a way similar to a noun phrase.

                        Noun clauses can function as subjects.

                        Example:

                        What she said was surprising.

                        The clause "What she said" functions as the subject.

                        Noun clauses can also function as objects.

                        Example:

                        I know that she studies English.

                        The clause "that she studies English" functions
                        as the object of "know".

                        Noun clauses may also be introduced by words such as:

                        that
                        whether
                        if
                        what
                        who
                        where
                        how

                        They are particularly important in academic writing
                        because they allow complex ideas and propositions
                        to be integrated into larger sentences.
                    """.trimIndent(),

                    examples = listOf(

                        Example(
                            english = "What she said was surprising.",
                            french = "Ce qu'elle a dit était surprenant."
                        ),

                        Example(
                            english = "I know that she studies English.",
                            french = "Je sais qu'elle étudie l'anglais."
                        ),

                        Example(
                            english = "We do not know whether the results are reliable.",
                            french = "Nous ne savons pas si les résultats sont fiables."
                        ),

                        Example(
                            english = "The researcher explained how the experiment worked.",
                            french = "Le chercheur a expliqué comment l'expérience fonctionnait."
                        )
                    ),

                    keyTerms = listOf(
                        "noun clause",
                        "embedded clause",
                        "subject",
                        "object",
                        "complement",
                        "that-clause",
                        "whether",
                        "embedded question"
                    ),

                    questions = listOf(

                        QuizQuestion(
                            id = "g1_u11_l2_q1",

                            question = """
                                What is a noun clause?
                            """.trimIndent(),

                            options = listOf(
                                "A clause functioning in a way similar to a noun phrase",
                                "A clause containing only nouns",
                                "A clause without a verb",
                                "A coordination marker"
                            ),

                            correctAnswerIndex = 0,

                            explanation = """
                                A noun clause is a subordinate clause that
                                performs a function similar to a noun phrase.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u11_l2_q2",

                            question = """
                                In "I know that she studies English",
                                what is the function of "that she studies English"?
                            """.trimIndent(),

                            options = listOf(
                                "Subject",
                                "Object",
                                "Adverbial",
                                "Determiner"
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                The clause functions as the object of
                                the verb "know".
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u11_l2_q3",

                            question = """
                                Which sentence contains a noun clause
                                functioning as the subject?
                            """.trimIndent(),

                            options = listOf(
                                "The student studies English.",
                                "What she said was surprising.",
                                "The student who studies English works hard.",
                                "She studies English because she enjoys it."
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                "What she said" is a noun clause functioning
                                as the subject of the sentence.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u11_l2_q4",

                            question = """
                                Which word can introduce an embedded
                                yes/no question?
                            """.trimIndent(),

                            options = listOf(
                                "whether",
                                "who only",
                                "although",
                                "and"
                            ),

                            correctAnswerIndex = 0,

                            explanation = """
                                "Whether" can introduce an embedded
                                yes/no question.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u11_l2_q5",

                            question = """
                                Why are noun clauses useful in academic writing?
                            """.trimIndent(),

                            options = listOf(
                                "They allow complex propositions to be integrated into larger sentences.",
                                "They remove all subordinate structures.",
                                "They eliminate the need for verbs.",
                                "They make every sentence shorter."
                            ),

                            correctAnswerIndex = 0,

                            explanation = """
                                Noun clauses allow writers to integrate
                                propositions and complex information into
                                larger grammatical structures.
                            """.trimIndent()
                        )
                    )
                ),

                // =====================================================
                // LESSON 3
                // =====================================================

                Lesson(
                    id = "grammar1_u11_l3",

                    title = "Lesson 3 — Adverbial Clauses",

                    objective = """
                        Identify adverbial clauses and explain how they
                        express relationships such as cause, time,
                        condition and concession.
                    """.trimIndent(),

                    content = """
                        Adverbial clauses are subordinate clauses that
                        modify a clause or proposition by expressing
                        relationships such as time, cause, condition,
                        contrast or concession.

                        Common subordinators include:

                        because
                        although
                        if
                        when
                        while
                        since
                        unless
                        whereas

                        Cause:

                        Because the students worked hard, they succeeded.

                        Time:

                        When the lecture ended, the students left.

                        Condition:

                        If you study regularly, you will improve.

                        Concession:

                        Although the exam was difficult, the students succeeded.

                        Adverbial clauses are important for academic writing
                        because they make logical relationships between ideas
                        explicit.
                    """.trimIndent(),

                    examples = listOf(

                        Example(
                            english = "Because the students worked hard, they succeeded.",
                            french = "Parce que les étudiants ont beaucoup travaillé, ils ont réussi."
                        ),

                        Example(
                            english = "When the lecture ended, the students left.",
                            french = "Lorsque le cours s'est terminé, les étudiants sont partis."
                        ),

                        Example(
                            english = "If you study regularly, you will improve.",
                            french = "Si tu étudies régulièrement, tu progresseras."
                        ),

                        Example(
                            english = "Although the exam was difficult, the students succeeded.",
                            french = "Bien que l'examen fût difficile, les étudiants ont réussi."
                        )
                    ),

                    keyTerms = listOf(
                        "adverbial clause",
                        "cause",
                        "time",
                        "condition",
                        "concession",
                        "subordinator",
                        "because",
                        "although",
                        "if"
                    ),

                    questions = listOf(

                        QuizQuestion(
                            id = "g1_u11_l3_q1",

                            question = """
                                Which word commonly introduces a clause
                                expressing cause?
                            """.trimIndent(),

                            options = listOf(
                                "because",
                                "if",
                                "although",
                                "when"
                            ),

                            correctAnswerIndex = 0,

                            explanation = """
                                "Because" commonly introduces an adverbial
                                clause expressing cause.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u11_l3_q2",

                            question = """
                                Which sentence contains an adverbial clause
                                expressing condition?
                            """.trimIndent(),

                            options = listOf(
                                "Because she studied, she passed.",
                                "If you study regularly, you will improve.",
                                "Although the exam was difficult, she passed.",
                                "The student who studied passed."
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                "If you study regularly" expresses the
                                condition for the result.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u11_l3_q3",

                            question = """
                                Which subordinator commonly expresses
                                concession?
                            """.trimIndent(),

                            options = listOf(
                                "although",
                                "because",
                                "if",
                                "when"
                            ),

                            correctAnswerIndex = 0,

                            explanation = """
                                "Although" introduces a concessive clause
                                expressing contrast.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u11_l3_q4",

                            question = """
                                In "When the lecture ended, the students left",
                                what relationship does "when" express?
                            """.trimIndent(),

                            options = listOf(
                                "Cause",
                                "Time",
                                "Possession",
                                "Comparison"
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                "When" introduces a clause expressing
                                a temporal relationship.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u11_l3_q5",

                            question = """
                                Why are adverbial clauses useful in
                                academic writing?
                            """.trimIndent(),

                            options = listOf(
                                "They make logical relationships between ideas explicit.",
                                "They eliminate subordinate clauses.",
                                "They replace all verbs.",
                                "They prevent complex reasoning."
                            ),

                            correctAnswerIndex = 0,

                            explanation = """
                                Adverbial clauses help writers make logical
                                relationships such as cause, condition,
                                time and concession explicit.
                            """.trimIndent()
                        )
                    )
                )
            )
        ),
        // =========================================================
        // UNIT 12 — COMPREHENSIVE REVIEW
        // =========================================================

        CourseUnit(
            id = "grammar1_unit12",

            title = "Unit 12 — Comprehensive Review",

            description = """
                Comprehensive review of the major grammatical concepts
                studied in Grammar 1, including word classes, phrases,
                clauses, tense, aspect, modality, questions, negation,
                agreement and sentence analysis.
            """.trimIndent(),

            lessons = listOf(

                // =====================================================
                // LESSON 1
                // =====================================================

                Lesson(
                    id = "grammar1_u12_l1",

                    title = "Lesson 1 — Integrated Grammar Review",

                    objective = """
                        Integrate the principal grammatical concepts
                        studied throughout Grammar 1 and apply them
                        to the analysis of English sentences.
                    """.trimIndent(),

                    content = """
                        Grammar 1 has introduced several interconnected
                        areas of English grammatical structure.

                        Word classes include nouns, pronouns, verbs,
                        adjectives, adverbs, prepositions and conjunctions.

                        These words combine to form larger grammatical
                        units such as noun phrases and verb phrases.

                        Clauses contain grammatical functions such as
                        subjects, objects and complements.

                        English verb phrases can express tense and aspect.

                        Modal auxiliaries express meanings such as
                        ability, possibility, permission, obligation
                        and prediction.

                        English also uses specific structures for
                        questions and negation.

                        Agreement contributes to grammatical accuracy,
                        while relative, noun and adverbial clauses allow
                        writers to construct more complex sentences.

                        Grammar therefore needs to be understood as
                        an interconnected system rather than as a list
                        of isolated rules.

                        Consider the sentence:

                        The students who studied regularly were able
                        to complete the assignment successfully.

                        "The students who studied regularly" is a noun
                        phrase functioning as the subject.

                        "who studied regularly" is a relative clause.

                        "were able to complete" forms part of the verb phrase.

                        "the assignment" functions as the object.

                        "successfully" is an adverbial element.

                        A complete grammatical analysis can therefore
                        involve several concepts at the same time.
                    """.trimIndent(),

                    examples = listOf(

                        Example(
                            english = "The student reads the article carefully.",
                            french = "L'étudiant lit attentivement l'article."
                        ),

                        Example(
                            english = "The students are preparing their assignment.",
                            french = "Les étudiants préparent leur devoir."
                        ),

                        Example(
                            english = "The researcher who conducted the study published the results.",
                            french = "Le chercheur qui a mené l'étude a publié les résultats."
                        ),

                        Example(
                            english = "Students must complete the work before the deadline.",
                            french = "Les étudiants doivent terminer le travail avant la date limite."
                        )
                    ),

                    keyTerms = listOf(
                        "grammar",
                        "word class",
                        "phrase",
                        "clause",
                        "subject",
                        "object",
                        "tense",
                        "aspect",
                        "modality",
                        "agreement"
                    ),

                    questions = listOf(

                        QuizQuestion(
                            id = "g1_u12_l1_q1",

                            question = """
                                Which grammatical unit is organized around
                                a noun or noun-like head?
                            """.trimIndent(),

                            options = listOf(
                                "Verb phrase",
                                "Noun phrase",
                                "Preposition",
                                "Conjunction"
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                A noun phrase is organized around a noun
                                or another element functioning as its head.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u12_l1_q2",

                            question = """
                                Which grammatical function identifies
                                the participant commonly associated with
                                the action of an active clause?
                            """.trimIndent(),

                            options = listOf(
                                "Subject",
                                "Determiner",
                                "Adverb",
                                "Preposition"
                            ),

                            correctAnswerIndex = 0,

                            explanation = """
                                The subject is a major grammatical function
                                in an active clause and is commonly
                                associated with the actor or topic.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u12_l1_q3",

                            question = """
                                Which grammatical category expresses
                                meanings such as possibility and obligation?
                            """.trimIndent(),

                            options = listOf(
                                "Modality",
                                "Article",
                                "Number",
                                "Reference"
                            ),

                            correctAnswerIndex = 0,

                            explanation = """
                                Modality includes meanings such as ability,
                                possibility, permission, obligation and prediction.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u12_l1_q4",

                            question = """
                                Which structure can combine several
                                grammatical concepts within one analysis?
                            """.trimIndent(),

                            options = listOf(
                                "Only a single noun",
                                "A complex sentence",
                                "Only a determiner",
                                "Only a preposition"
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                Complex sentences can contain phrases,
                                clauses, verb structures and several
                                grammatical functions simultaneously.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u12_l1_q5",

                            question = """
                                What is an important principle of
                                university-level grammatical analysis?
                            """.trimIndent(),

                            options = listOf(
                                "Grammar consists only of memorized rules.",
                                "Grammatical categories should be analysed in relation to structure and function.",
                                "Sentence meaning is irrelevant.",
                                "Only vocabulary should be analysed."
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                University-level grammar involves analysing
                                forms, functions and their relationships
                                within larger structures.
                            """.trimIndent()
                        )
                    )
                ),

                // =====================================================
                // LESSON 2
                // =====================================================

                Lesson(
                    id = "grammar1_u12_l2",

                    title = "Lesson 2 — Sentence Analysis and Editing",

                    objective = """
                        Apply grammatical analysis and editing procedures
                        to identify structural and grammatical problems
                        in English sentences.
                    """.trimIndent(),

                    content = """
                        Sentence analysis involves identifying the
                        grammatical structure of a sentence and explaining
                        the function of its constituents.

                        Editing involves detecting grammatical problems
                        and revising the sentence.

                        Consider:

                        Incorrect:
                        The student study English every day.

                        Analysis:

                        "The student" is a singular subject.

                        The present simple verb therefore requires
                        the third-person singular form.

                        Correct:

                        The student studies English every day.

                        Another example:

                        Incorrect:
                        She can studies English.

                        Correct:

                        She can study English.

                        The modal "can" is followed by the base form.

                        Effective editing therefore requires understanding
                        the underlying grammatical rule rather than simply
                        memorizing isolated corrections.
                    """.trimIndent(),

                    examples = listOf(

                        Example(
                            english = "Incorrect: The student study English.",
                            french = "Incorrect : L'étudiant étudie l'anglais."
                        ),

                        Example(
                            english = "Correct: The student studies English.",
                            french = "Correct : L'étudiant étudie l'anglais."
                        ),

                        Example(
                            english = "Incorrect: She can studies English.",
                            french = "Incorrect : Elle peut étudier l'anglais."
                        ),

                        Example(
                            english = "Correct: She can study English.",
                            french = "Correct : Elle peut étudier l'anglais."
                        )
                    ),

                    keyTerms = listOf(
                        "analysis",
                        "editing",
                        "revision",
                        "agreement",
                        "modal",
                        "base form",
                        "grammatical error"
                    ),

                    questions = listOf(

                        QuizQuestion(
                            id = "g1_u12_l2_q1",

                            question = """
                                Which sentence is correctly edited?
                            """.trimIndent(),

                            options = listOf(
                                "The student study English.",
                                "The student studies English.",
                                "The student studying English.",
                                "The student studies English?"
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                "The student studies English" correctly
                                applies subject–verb agreement.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u12_l2_q2",

                            question = """
                                Which sentence correctly uses a modal auxiliary?
                            """.trimIndent(),

                            options = listOf(
                                "She can studies English.",
                                "She can studied English.",
                                "She can study English.",
                                "She can studying English."
                            ),

                            correctAnswerIndex = 2,

                            explanation = """
                                A modal auxiliary is followed by the
                                base form of the lexical verb.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u12_l2_q3",

                            question = """
                                What should an editor identify before
                                correcting a grammatical error?
                            """.trimIndent(),

                            options = listOf(
                                "Only the punctuation",
                                "The underlying grammatical problem",
                                "Only the longest word",
                                "Only the pronunciation"
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                Effective editing requires identifying
                                the underlying grammatical problem.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u12_l2_q4",

                            question = """
                                Which sentence contains a subject–verb
                                agreement problem?
                            """.trimIndent(),

                            options = listOf(
                                "The student studies English.",
                                "The students study English.",
                                "The student study English.",
                                "The researchers analyse the data."
                            ),

                            correctAnswerIndex = 2,

                            explanation = """
                                "The student" is singular, so the verb
                                should be "studies".
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u12_l2_q5",

                            question = """
                                Why is grammatical analysis useful
                                during editing?
                            """.trimIndent(),

                            options = listOf(
                                "It helps explain why a correction is required.",
                                "It removes the need for revision.",
                                "It prevents the use of vocabulary.",
                                "It makes every sentence identical."
                            ),

                            correctAnswerIndex = 0,

                            explanation = """
                                Analysis helps the writer understand the
                                grammatical principle behind a correction.
                            """.trimIndent()
                        )
                    )
                ),

                // =====================================================
                // LESSON 3
                // =====================================================

                Lesson(
                    id = "grammar1_u12_l3",

                    title = "Lesson 3 — Comprehensive Grammar Assessment",

                    objective = """
                        Demonstrate integrated understanding of the major
                        grammatical concepts covered in Grammar 1.
                    """.trimIndent(),

                    content = """
                        A comprehensive assessment should require students
                        to recognize, analyse and apply grammatical concepts.

                        The student should be able to identify:

                        word classes

                        noun phrase structure

                        verb phrase structure

                        grammatical functions

                        tense and aspect

                        modality

                        questions and negation

                        agreement

                        clause types

                        relative and embedded clauses

                        The assessment should also move beyond recognition
                        and require application.

                        Example:

                        The students who have completed the research
                        project should submit their reports tomorrow.

                        A complete analysis may identify:

                        "The students who have completed the research project"
                        as the subject noun phrase.

                        "who have completed the research project" as a
                        relative clause.

                        "have completed" as a perfect verb construction.

                        "should submit" as a modal construction.

                        "their reports" as the object noun phrase.

                        "tomorrow" as a temporal adverbial.
                    """.trimIndent(),

                    examples = listOf(

                        Example(
                            english = "The students who have completed the research project should submit their reports tomorrow.",
                            french = "Les étudiants qui ont terminé le projet de recherche devraient remettre leurs rapports demain."
                        ),

                        Example(
                            english = "Because the examination was difficult, the students worked carefully.",
                            french = "Parce que l'examen était difficile, les étudiants ont travaillé avec soin."
                        ),

                        Example(
                            english = "The researcher explained what the results showed.",
                            french = "Le chercheur a expliqué ce que les résultats montraient."
                        ),

                        Example(
                            english = "Can the students complete the assignment before Friday?",
                            french = "Les étudiants peuvent-ils terminer le devoir avant vendredi ?"
                        )
                    ),

                    keyTerms = listOf(
                        "integrated analysis",
                        "assessment",
                        "word class",
                        "phrase structure",
                        "clause structure",
                        "tense",
                        "aspect",
                        "modality",
                        "agreement",
                        "editing"
                    ),

                    questions = listOf(

                        QuizQuestion(
                            id = "g1_u12_l3_q1",

                            question = """
                                In "The students should submit their reports",
                                what is "should"?
                            """.trimIndent(),

                            options = listOf(
                                "Noun",
                                "Modal auxiliary",
                                "Determiner",
                                "Preposition"
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                "Should" is a modal auxiliary expressing
                                meanings such as advice, expectation or obligation.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u12_l3_q2",

                            question = """
                                In "The students who have completed the
                                project", what is "who have completed
                                the project"?
                            """.trimIndent(),

                            options = listOf(
                                "A relative clause",
                                "A determiner",
                                "A preposition",
                                "An adjective only"
                            ),

                            correctAnswerIndex = 0,

                            explanation = """
                                "Who have completed the project" is a
                                relative clause modifying "the students".
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u12_l3_q3",

                            question = """
                                Which construction appears in
                                "have completed"?
                            """.trimIndent(),

                            options = listOf(
                                "Perfect aspect",
                                "Modal obligation",
                                "Simple present only",
                                "Noun phrase structure"
                            ),

                            correctAnswerIndex = 0,

                            explanation = """
                                "Have completed" forms a present perfect
                                construction.
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u12_l3_q4",

                            question = """
                                Which phrase is the object in
                                "The students should submit their reports"?
                            """.trimIndent(),

                            options = listOf(
                                "The students",
                                "should submit",
                                "their reports",
                                "should"
                            ),

                            correctAnswerIndex = 2,

                            explanation = """
                                "Their reports" functions as the object
                                of the verb "submit".
                            """.trimIndent()
                        ),

                        QuizQuestion(
                            id = "g1_u12_l3_q5",

                            question = """
                                What is the main purpose of the comprehensive
                                Grammar 1 assessment?
                            """.trimIndent(),

                            options = listOf(
                                "To test vocabulary only",
                                "To evaluate integrated understanding and application of grammatical concepts",
                                "To test pronunciation only",
                                "To memorise isolated definitions"
                            ),

                            correctAnswerIndex = 1,

                            explanation = """
                                The comprehensive assessment evaluates the
                                student's ability to integrate and apply the
                                grammatical concepts studied throughout Grammar 1.
                            """.trimIndent()
                        )
                    )
                )
            )
        )
    )
}