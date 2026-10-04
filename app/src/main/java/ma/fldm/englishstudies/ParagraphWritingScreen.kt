@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package ma.fldm.englishstudies

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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

private val PWPrimary = Color(0xFF2563EB)
private val PWBackground = Color(0xFFF8FAFC)
private val PWText = Color(0xFF0F172A)
private val PWMuted = Color(0xFF64748B)
private val PWBorder = Color(0xFFE2E8F0)
private val PWSuccess = Color(0xFF15803D)
private val PWSoftBlue = Color(0xFFEFF6FF)

private data class PWLesson(
    val title: String,
    val objective: String,
    val keyIdea: String,
    val content: String,
    val terms: List<String>,
    val exampleTitle: String,
    val example: String,
    val practice: String,
    val question: String,
    val options: List<String>,
    val answer: Int,
    val explanation: String
)


private data class PWExercise(
    val type: String,
    val question: String,
    val options: List<String>,
    val answer: Int,
    val explanation: String
)

private val PWExercises: List<List<PWExercise>> = listOf(
    listOf(
        PWExercise("Multiple Choice", "What is the main function of a paragraph?", listOf("To develop one focused idea", "To introduce an entire textbook", "To list unrelated facts", "To avoid supporting details"), 0, "A paragraph is a focused unit of writing that develops one controlling idea."),
        PWExercise("True / False", "A strong academic paragraph can contain several unrelated main ideas.", listOf("True", "False"), 1, "False. Unity requires the sentences to contribute to one controlling idea."),
        PWExercise("Identify", "Which sentence is the best topic sentence?", listOf("University students.", "Time management can help university students balance academic responsibilities.", "There are many things to say about university.", "Time is interesting."), 1, "The second option gives a clear topic and controlling idea."),
        PWExercise("Matching", "Which sentence best supports a paragraph about independent study?", listOf("Students can organise their own reading schedule.", "The campus has a large entrance.", "My favourite sport is tennis.", "Some buildings are old."), 0, "The first sentence directly develops the idea of independent study."),
        PWExercise("Ordering", "Choose the most logical order for an academic paragraph.", listOf("Topic sentence → support → example → conclusion", "Example → unrelated fact → topic sentence → title", "Conclusion → topic sentence → unrelated detail → support", "Title → conclusion → example → new topic"), 0, "A focused paragraph normally begins with the main idea and then develops it before concluding."),
        PWExercise("Correction", "Which sentence contains an irrelevant detail in a paragraph about study habits?", listOf("Students should plan weekly study sessions.", "Regular review reduces last-minute pressure.", "Many students enjoy football matches.", "A study timetable can distribute tasks."), 2, "The football sentence does not support the study-habits topic."),
        PWExercise("Choose", "Which ending best closes a paragraph about time management?", listOf("In conclusion, effective time management helps students study more consistently.", "My friend has a red bag.", "Another topic is tourism.", "There are also many holidays."), 0, "The first sentence returns to the paragraph's controlling idea."),
        PWExercise("Application", "A paragraph contains a topic sentence and two explanations but no concrete example. What would improve development?", listOf("Add a relevant example", "Remove the topic sentence", "Add an unrelated story", "Delete the explanations"), 0, "A relevant example makes the abstract explanation more concrete."),
        PWExercise("Academic Style", "Which expression is most appropriate for formal academic writing?", listOf("kids", "a lot of stuff", "students", "things", "pretty good"), 2, "Students is precise and appropriate for an academic register."),
        PWExercise("Review", "Which combination describes an effective paragraph?", listOf("Unity, development, coherence and accuracy", "Length, repetition and unrelated examples", "Many ideas and no transitions", "Only complex vocabulary"), 0, "Effective paragraphs require focused content, development, logical flow and accurate language.")
    ),
    listOf(
        PWExercise("Multiple Choice", "A strong topic sentence normally contains:", listOf("A topic and a controlling idea", "Only one word", "A quotation only", "A conclusion only"), 0, "The topic identifies the subject; the controlling idea limits what the paragraph will discuss."),
        PWExercise("Choose", "Which topic sentence is too broad?", listOf("University education has many aspects.", "Regular reading can improve academic vocabulary.", "Group discussion can strengthen oral communication.", "A weekly timetable can reduce academic stress."), 0, "The first statement is so broad that it does not clearly control paragraph development."),
        PWExercise("Identify", "What is the controlling idea in: 'Regular reading can improve university students' academic vocabulary.'", listOf("Regular reading", "university students", "can improve academic vocabulary", "students'"), 2, "The phrase 'can improve academic vocabulary' tells the reader what aspect of the topic will be developed."),
        PWExercise("True / False", "A topic sentence should be specific enough to guide the paragraph.", listOf("True", "False"), 0, "True. Specificity helps the writer select relevant supporting details."),
        PWExercise("Revision", "Which revision is more focused?", listOf("Technology affects students.", "Educational technology can help university students organise online learning materials.", "Technology is everywhere.", "Students and technology have many connections."), 1, "The revised sentence clearly identifies what aspect of technology will be discussed."),
        PWExercise("Matching", "Which controlling idea best completes: 'Group discussion can improve learning by ...'", listOf("giving students opportunities to explain ideas aloud", "being common on campus", "using chairs", "lasting many minutes"), 0, "The first option gives a meaningful direction for supporting sentences."),
        PWExercise("Choose", "Which sentence is most suitable as the first sentence of a paragraph on academic vocabulary?", listOf("Academic vocabulary is important.", "Regular reading can expand students' academic vocabulary.", "Words are everywhere.", "Vocabulary, vocabulary, vocabulary."), 1, "The second option is focused and provides a clear direction for development."),
        PWExercise("Error Check", "What is wrong with the topic sentence 'There are many things to say about students.'", listOf("It lacks a clear controlling idea", "It is too formal", "It has too much evidence", "It is already a conclusion"), 0, "The sentence does not tell the reader what specific aspect of students will be discussed."),
        PWExercise("Application", "Choose the topic sentence that best controls a paragraph on note-taking.", listOf("Note-taking can help university students organise important information during lectures.", "University life.", "Lectures are long.", "Notes are words."), 0, "It identifies both the topic and the specific controlling idea."),
        PWExercise("Production", "What should you do after drafting a topic sentence?", listOf("Check whether the supporting ideas can logically develop it", "Add unrelated examples", "Change the topic in every sentence", "Write the conclusion first and ignore the topic"), 0, "A topic sentence should genuinely control the development of the paragraph.")
    ),
    listOf(
        PWExercise("Multiple Choice", "Which type of support explains why a claim is true?", listOf("Reason", "Title", "Heading", "Punctuation"), 0, "A reason provides an explanation for a claim."),
        PWExercise("Choose", "Which is the strongest support for 'Reading develops vocabulary'?", listOf("Repeated exposure introduces learners to words in context.", "Libraries have doors.", "Students have notebooks.", "Books can be heavy."), 0, "The first option directly explains the mechanism by which reading can develop vocabulary."),
        PWExercise("Example", "Which sentence is a concrete example?", listOf("For example, a student can record five new terms after each reading session.", "Vocabulary is important.", "Reading helps.", "Learning is useful."), 0, "It shows a specific, observable example."),
        PWExercise("True / False", "Every supporting sentence must relate directly to the controlling idea.", listOf("True", "False"), 0, "True. Relevance is essential for unity."),
        PWExercise("Evidence", "Which support would be most appropriate for a paragraph about revision?", listOf("Students who review material regularly are more likely to identify gaps in understanding.", "The classroom has large windows.", "Some students walk to campus.", "Libraries can be quiet."), 0, "The first sentence directly explains a benefit of revision."),
        PWExercise("Ordering", "Which sequence gives the clearest development?", listOf("Claim → reason → example", "Example → unrelated topic → reason", "Conclusion → claim → new topic", "Definition → unrelated fact → title"), 0, "A reason explains the claim and the example concretises it."),
        PWExercise("Application", "A writer says 'Independent study develops autonomy.' Which sentence best explains the claim?", listOf("Students make decisions about what, when and how to study.", "Autonomy is a long word.", "University buildings are large.", "Students sometimes travel."), 0, "The first sentence explains how independent study develops autonomy."),
        PWExercise("Weak Support", "Which sentence provides weak or unrelated support for a paragraph on study planning?", listOf("A weekly plan helps distribute tasks.", "Planning can reduce last-minute work.", "My cousin likes music.", "A timetable can include reading sessions."), 2, "The music sentence does not develop study planning."),
        PWExercise("Development", "What can a writer add when an idea is too general?", listOf("A relevant explanation or example", "An unrelated quotation", "A second topic", "A decorative heading only"), 0, "Relevant detail turns a general claim into a developed point."),
        PWExercise("Review", "Which support is least appropriate for the topic 'Benefits of group discussion'?", listOf("Students can clarify difficult concepts.", "Participants hear different viewpoints.", "The university has three gates.", "Students practise explaining ideas aloud."), 2, "The number of university gates is unrelated to the benefits of discussion.")
    ),
    listOf(
        PWExercise("Multiple Choice", "Unity means that:", listOf("All sentences contribute to the main idea", "Every sentence has the same length", "The paragraph contains no transitions", "The paragraph has exactly five sentences"), 0, "Unity is achieved when all sentences are relevant to the controlling idea."),
        PWExercise("Multiple Choice", "Coherence mainly concerns:", listOf("Logical flow and clear relationships between ideas", "Word length", "Number of commas", "Font size"), 0, "Coherence makes the paragraph easy to follow logically."),
        PWExercise("Transition", "Which transition signals sequence?", listOf("First", "However", "In contrast", "Nevertheless"), 0, "First commonly introduces the initial step in a sequence."),
        PWExercise("Transition", "Which transition signals contrast?", listOf("Therefore", "However", "For example", "Similarly"), 1, "However introduces a contrast."),
        PWExercise("Ordering", "Which order is most coherent?", listOf("Identify the problem → collect information → evaluate evidence", "Evaluate evidence → introduce problem → ignore information", "Give conclusion → change topic → identify problem", "Example → title → unrelated idea"), 0, "The stages form a logical progression."),
        PWExercise("True / False", "A paragraph can be unified even when one sentence is completely unrelated to its topic.", listOf("True", "False"), 1, "False. An unrelated sentence breaks unity."),
        PWExercise("Application", "Which transition best links a result to its cause?", listOf("Therefore", "For example", "Similarly", "Meanwhile"), 0, "Therefore signals a conclusion or result based on previous information."),
        PWExercise("Correction", "Which sentence disrupts coherence in a paragraph about reading strategies?", listOf("Previewing headings helps readers predict content.", "Skimming provides a quick overview.", "My favourite food is couscous.", "Scanning helps readers locate specific information."), 2, "The food sentence is unrelated to reading strategies."),
        PWExercise("Lexical Cohesion", "Repeating a key term or using a suitable synonym can help:", listOf("connect ideas across sentences", "change the topic", "remove all support", "avoid meaning"), 0, "Lexical connections help readers recognise that sentences belong to the same topic."),
        PWExercise("Review", "What is the best combination for coherence?", listOf("Logical order + appropriate transitions", "Long words + repeated punctuation", "Random order + unrelated examples", "Only a conclusion"), 0, "Coherence is built through logical sequencing and clear connections.")
    ),
    listOf(
        PWExercise("Multiple Choice", "Which word is a cohesive device for contrast?", listOf("However", "Therefore", "For example", "First"), 0, "However links ideas by showing contrast."),
        PWExercise("Reference", "In 'Students should revise regularly. This habit improves retention,' the word 'This' refers to:", listOf("revising regularly", "students", "retention", "the classroom"), 0, "This refers back to the preceding idea of revising regularly."),
        PWExercise("Conjunction", "Which word best shows cause and effect?", listOf("Therefore", "However", "Similarly", "Meanwhile"), 0, "Therefore introduces a result or conclusion."),
        PWExercise("Academic Register", "Which sentence is more academic?", listOf("Students gotta study a lot.", "Students need to study consistently.", "Kids have to hit the books.", "Students do loads of stuff."), 1, "The second option is formal, precise and appropriate for academic writing."),
        PWExercise("True / False", "Cohesion and coherence mean exactly the same thing.", listOf("True", "False"), 1, "False. Cohesion concerns textual connections; coherence concerns overall logical meaning and flow."),
        PWExercise("Synonym", "Which pair can create lexical cohesion?", listOf("students / learners", "student / bicycle", "reading / rainfall", "essay / window"), 0, "Students and learners are related terms and can maintain the topic."),
        PWExercise("Choice", "Which sentence pair is best connected?", listOf("Online resources are useful for research. However, students must evaluate their reliability.", "Online resources are useful for research. Football is popular.", "Online resources are useful for research. Chairs are brown.", "Online resources are useful for research. Tuesday is a day."), 0, "However creates a meaningful contrast between usefulness and the need for evaluation."),
        PWExercise("Correction", "Replace the informal phrase 'a lot of stuff' with:", listOf("many academic resources", "lots of things", "cool stuff", "whatever"), 0, "Many academic resources is precise and appropriate in an academic context."),
        PWExercise("Application", "Which sentence uses a clear formal register?", listOf("The results demonstrate a significant pattern.", "The results are kinda interesting.", "The results are super cool.", "The results are a bit weird."), 0, "The first sentence uses precise, formal academic language."),
        PWExercise("Review", "Which device can improve cohesion?", listOf("Pronouns, synonyms, conjunctions and transitions", "Random topic changes", "Unrelated examples", "Repeated conclusions"), 0, "These devices create grammatical and lexical links between sentences.")
    ),
    listOf(
        PWExercise("Classification", "A paragraph explaining similarities and differences is a:", listOf("Comparison and contrast paragraph", "Narrative paragraph", "Purely descriptive list", "Greeting"), 0, "Comparison and contrast focuses on similarities, differences or both."),
        PWExercise("Cause / Effect", "Which topic best fits a cause-and-effect paragraph?", listOf("Effects of excessive academic procrastination", "The appearance of a classroom", "My daily greeting", "A list of colours"), 0, "The topic invites explanation of causes and/or effects."),
        PWExercise("Description", "Which purpose is most characteristic of a descriptive paragraph?", listOf("Presenting characteristics of a person, place or object", "Comparing two research methods only", "Listing references", "Giving a bibliography"), 0, "Description focuses on characteristics and details."),
        PWExercise("Narration", "A narrative paragraph mainly:", listOf("Presents events in sequence", "Defines a single term", "Lists sources", "Compares two theories only"), 0, "Narration organises events according to sequence or time."),
        PWExercise("Argument", "An argumentative paragraph generally aims to:", listOf("Present and support a claim", "Describe a colour", "List random events", "Avoid reasons"), 0, "Argument requires a claim supported by reasons or evidence."),
        PWExercise("Choose", "Which topic sentence best introduces a comparison paragraph?", listOf("Online and library research offer different advantages to university students.", "Students are important.", "Libraries.", "Research is interesting."), 0, "The first sentence clearly introduces two subjects and signals comparison."),
        PWExercise("True / False", "The purpose of a paragraph should influence its organisation.", listOf("True", "False"), 0, "True. Different purposes require different supporting patterns."),
        PWExercise("Classification", "Which paragraph type is most suitable for explaining why students procrastinate and what happens as a result?", listOf("Cause and effect", "Description only", "Narration only", "Definition only"), 0, "The task asks for causes and consequences."),
        PWExercise("Application", "A paragraph tells a short story about a student's first day at university. It is mainly:", listOf("Narrative", "Comparison", "Classification", "Argument"), 0, "A sequence of events makes it narrative."),
        PWExercise("Review", "Which pairing is correct?", listOf("Comparison → similarities/differences", "Narration → only definitions", "Description → causes only", "Argument → no support"), 0, "Comparison paragraphs organise similarities and differences.")
    ),
    listOf(
        PWExercise("Sequence", "Which stage normally comes first?", listOf("Planning", "Proofreading", "Submission", "Final formatting only"), 0, "Planning precedes drafting and later revision/editing stages."),
        PWExercise("Revision", "Revision mainly focuses on:", listOf("Ideas and organisation", "Only spelling", "Only font choice", "Page numbers only"), 0, "Revision evaluates whether the content and organisation work effectively."),
        PWExercise("Editing", "Editing mainly focuses on:", listOf("Grammar, vocabulary, spelling and punctuation", "Choosing the topic", "Generating ideas only", "Changing the assignment question"), 0, "Editing improves language accuracy and mechanics."),
        PWExercise("Proofreading", "Proofreading is best described as:", listOf("A final careful check before submission", "The first brainstorming stage", "Writing without revising", "Changing every sentence"), 0, "Proofreading is the final accuracy check."),
        PWExercise("True / False", "Good writing is usually produced in a single first draft.", listOf("True", "False"), 1, "False. Effective writing normally involves planning, drafting, revising and editing."),
        PWExercise("Planning", "Which activity belongs to planning?", listOf("Brainstorming possible supporting points", "Correcting commas in the final draft", "Submitting the paper", "Checking spelling after printing"), 0, "Brainstorming helps the writer generate and organise ideas before drafting."),
        PWExercise("Ordering", "Choose the correct sequence.", listOf("Plan → Draft → Revise → Edit → Proofread", "Proofread → Plan → Submit → Draft", "Edit → Plan → Revise → Draft", "Submit → Draft → Plan → Proofread"), 0, "This sequence reflects a practical academic writing process."),
        PWExercise("Application", "During revision you notice that one supporting sentence does not relate to the topic. What should you do?", listOf("Remove or replace it", "Keep it because it is interesting", "Add three more unrelated sentences", "Change the font"), 0, "Revision is the stage for improving focus and organisation."),
        PWExercise("Checklist", "Which is an editing question?", listOf("Is the verb tense correct?", "Do all ideas support the topic?", "What should my paragraph be about?", "Which ideas should I brainstorm?"), 0, "Verb tense is a language-accuracy issue handled during editing."),
        PWExercise("Review", "Why is proofreading important?", listOf("It helps catch remaining surface errors", "It creates new arguments", "It replaces the topic sentence automatically", "It removes all examples"), 0, "Proofreading catches remaining spelling, punctuation and other surface errors.")
    ),
    listOf(
        PWExercise("Development", "Which technique adds concrete detail to a general claim?", listOf("Example", "Unrelated quotation", "New topic", "Blank space"), 0, "An example makes an abstract claim more concrete."),
        PWExercise("Elaboration", "Which sentence best elaborates 'Independent study builds autonomy'?", listOf("Students decide what to study and monitor their own progress.", "Autonomy is an interesting word.", "University buildings are large.", "Students eat lunch."), 0, "The first sentence explains the mechanism behind the claim."),
        PWExercise("Example", "Which is the best concrete illustration of vocabulary practice?", listOf("A student records five new academic terms after each reading session.", "Vocabulary exists.", "Words are useful.", "Learning is good."), 0, "The first sentence provides an observable and specific example."),
        PWExercise("Definition", "When a difficult concept is unfamiliar, which technique can help develop it?", listOf("Define the concept and then illustrate it", "Change the subject", "Remove all explanation", "Repeat the title"), 0, "Definition plus illustration helps readers understand unfamiliar ideas."),
        PWExercise("Comparison", "Which method can help explain a new concept by relating it to a familiar one?", listOf("Comparison or analogy", "Random listing", "Deletion", "Punctuation"), 0, "Comparison can clarify unfamiliar concepts through a familiar reference point."),
        PWExercise("Cause / Effect", "Which development method explains why something happens?", listOf("Cause-and-effect reasoning", "Decoration", "Title repetition", "Alphabetical order only"), 0, "Cause-and-effect reasoning explains relationships between events or conditions."),
        PWExercise("True / False", "Development means simply adding more sentences, even when they are irrelevant.", listOf("True", "False"), 1, "False. Development requires relevant elaboration, not unnecessary length."),
        PWExercise("Application", "A writer has a claim but no explanation. What should be added first?", listOf("A sentence explaining how or why the claim is true", "An unrelated example", "A new topic", "A bibliography entry"), 0, "Explanation establishes the link between the claim and its support."),
        PWExercise("Review", "Which pattern is especially effective for developing an idea?", listOf("Assertion → explanation → example", "Title → unrelated fact → conclusion", "Example → new topic → greeting", "Conclusion → random detail → claim"), 0, "The first pattern moves from a claim to its meaning and then to a concrete illustration."),
        PWExercise("Quality", "What makes an example useful in an academic paragraph?", listOf("It is relevant and clearly connected to the point", "It is as long as possible", "It introduces a completely new idea", "It avoids the topic"), 0, "Relevant examples strengthen development by directly supporting the controlling idea.")
    ),
    listOf(
        PWExercise("Revision", "Which question best checks paragraph unity?", listOf("Does every sentence support the controlling idea?", "Is the font large?", "Does the paragraph have emojis?", "Is every sentence exactly ten words?"), 0, "Unity is about relevance to the controlling idea."),
        PWExercise("Editing", "Which sentence contains a subject–verb agreement error?", listOf("The student writes carefully.", "The students writes carefully.", "The student revises carefully.", "The students revise carefully."), 1, "Plural 'students' requires the plural verb 'write'."),
        PWExercise("Punctuation", "Which sentence is correctly punctuated?", listOf("First, students plan their ideas.", "First students, plan their ideas.", "First students plan, their ideas.", "First students plan their ideas"), 0, "A comma after an introductory transition such as First is appropriate."),
        PWExercise("Clarity", "Which sentence is clearer?", listOf("Students should review their work before submission.", "Students should do things with their work before it goes.", "Work should maybe be checked somehow.", "Students do stuff before."), 0, "The first sentence is direct and precise."),
        PWExercise("True / False", "Revision and editing are identical processes.", listOf("True", "False"), 1, "False. Revision primarily improves content and organisation; editing improves language accuracy."),
        PWExercise("Error Correction", "Which version is most accurate?", listOf("Academic writing require clear organisation.", "Academic writing requires clear organisation.", "Academic writing requiring clear organisation.", "Academic writing require a clear organisation."), 1, "The singular subject 'writing' requires 'requires'."),
        PWExercise("Coherence", "Which change would most improve coherence?", listOf("Add an appropriate transition between two related ideas", "Add an unrelated example", "Remove the topic sentence", "Change the paragraph colour"), 0, "Transitions can make logical relationships explicit."),
        PWExercise("Proofreading", "Which item belongs in a final proofreading check?", listOf("Spelling of key terms", "Choosing a new topic from scratch", "Brainstorming three new arguments", "Changing the whole structure without reading"), 0, "Proofreading checks surface accuracy such as spelling."),
        PWExercise("Academic Vocabulary", "Which word is most precise in an academic sentence?", listOf("demonstrates", "shows stuff", "does things", "is kinda about"), 0, "Demonstrates is precise and formal."),
        PWExercise("Review", "A paragraph has excellent grammar but one unrelated sentence. What is the main problem?", listOf("Unity", "Spelling", "Punctuation", "Vocabulary"), 0, "The unrelated sentence breaks unity even if the grammar is correct.")
    ),
    listOf(
        PWExercise("Assessment", "What should a writer do first when reading a paragraph-writing prompt?", listOf("Interpret the task and identify the required focus", "Start writing without reading", "Choose unrelated vocabulary", "Write the conclusion immediately"), 0, "Understanding the prompt is the first step toward relevant content."),
        PWExercise("Criteria", "Which set contains key assessment criteria?", listOf("Focus, development, coherence and accuracy", "Font, colour and decoration", "Length only", "Complex words only"), 0, "These criteria address central qualities of academic paragraph writing."),
        PWExercise("Planning", "What should a paragraph plan normally include?", listOf("A controlling idea and supporting points", "Random sentences", "Only a conclusion", "A list of colours"), 0, "Planning clarifies the main idea and the points that will develop it."),
        PWExercise("Drafting", "Why is a first draft useful?", listOf("It gives the writer material to revise and improve", "It must never be changed", "It replaces proofreading", "It removes the need for planning"), 0, "A draft is a working version that can be improved."),
        PWExercise("True / False", "A good assessment paragraph should include unrelated ideas to show vocabulary range.", listOf("True", "False"), 1, "False. Vocabulary range must remain relevant to the paragraph's focus."),
        PWExercise("Application", "Which topic sentence best answers the prompt about independent study?", listOf("Independent study helps university students develop autonomy and responsibility for learning.", "University life is interesting.", "Students have many things.", "Study."), 0, "It directly addresses the topic and provides a controlling idea."),
        PWExercise("Development", "Which support would best develop the independent-study claim?", listOf("Students can plan reading, monitor progress and adjust study strategies.", "The campus has a cafeteria.", "Some students wear blue.", "Libraries have windows."), 0, "The first sentence directly explains how independent study develops responsibility."),
        PWExercise("Editing", "Which sentence is grammatically correct?", listOf("Students needs to revise regularly.", "Students need to revise regularly.", "Students needing revise regularly.", "Students need revises regularly."), 1, "Plural 'students' takes 'need', followed by the infinitive 'to revise'."),
        PWExercise("Conclusion", "Which final sentence best concludes the paragraph?", listOf("Therefore, independent study is an essential part of responsible university learning.", "Another topic is transportation.", "My friend likes music.", "There are many holidays."), 0, "The first option returns to and reinforces the paragraph's controlling idea."),
        PWExercise("Final Review", "Which final check is most appropriate before submission?", listOf("Review focus, support, transitions, grammar, spelling and punctuation", "Only check the title", "Only count words", "Change the font and submit immediately"), 0, "A final review should cover both content and language accuracy.")
    )
)

private val PWLessons = listOf(
    PWLesson(
        title = "Foundations",
        objective = "Understand what a paragraph is and how academic paragraphs are organized.",
        keyIdea = "A paragraph is a focused unit of writing that develops one controlling idea.",
        content = "An effective academic paragraph normally contains a topic sentence, supporting sentences, and a concluding sentence or concluding transition. The paragraph should remain focused on one main idea and develop it logically.",
        terms = listOf("paragraph", "main idea", "topic sentence", "support", "conclusion"),
        exampleTitle = "Model paragraph",
        example = "University students need effective time management. They have to balance lectures, reading, assignments and independent study. A weekly plan helps them distribute these tasks and avoid last-minute work. For this reason, time management is an essential academic skill.",
        practice = "Underline the main idea of the model paragraph and identify its topic sentence.",
        question = "What is the main purpose of an academic paragraph?",
        options = listOf("To present several unrelated ideas", "To develop one focused idea", "To replace an entire essay", "To contain only definitions"),
        answer = 1,
        explanation = "An academic paragraph develops a focused idea through a logical sequence of sentences."
    ),
    PWLesson(
        title = "Topic Sentences",
        objective = "Write precise topic sentences that control the content of a paragraph.",
        keyIdea = "The topic sentence states the paragraph's controlling idea and limits its scope.",
        content = "A strong topic sentence contains a topic and a controlling idea. It should be specific enough to guide the paragraph while remaining broad enough to allow development with supporting details.",
        terms = listOf("topic", "controlling idea", "scope", "focus", "specificity"),
        exampleTitle = "From broad to focused",
        example = "Broad: Social media is important.\nFocused: Social media can influence university students' study habits by changing how they manage attention and time.",
        practice = "Rewrite this topic sentence so that it has a clear controlling idea: 'Technology affects students.'",
        question = "Which topic sentence is most suitable for an academic paragraph?",
        options = listOf("Students.", "Education is interesting.", "Regular reading can improve university students' academic vocabulary.", "There are many things to say about life."),
        answer = 2,
        explanation = "The sentence has a clear topic (regular reading) and a specific controlling idea (improving academic vocabulary)."
    ),
    PWLesson(
        title = "Supporting Sentences",
        objective = "Develop a topic sentence with relevant evidence, explanation and examples.",
        keyIdea = "Support answers the reader's question: why or how is the topic sentence true?",
        content = "Supporting sentences may provide reasons, facts, examples, explanations, comparisons or brief evidence. Each sentence should contribute directly to the paragraph's main idea.",
        terms = listOf("reason", "example", "evidence", "explanation", "relevance"),
        exampleTitle = "Support pattern",
        example = "Topic: Group discussion can improve learning.\nReason: It gives students opportunities to explain ideas aloud.\nExample: A student may understand a concept more clearly after explaining it to a classmate.",
        practice = "Add two supporting sentences to this topic sentence: 'Regular revision improves academic performance.'",
        question = "Which sentence is the best support for 'Reading develops vocabulary'?",
        options = listOf("My friend likes football.", "Repeated exposure introduces learners to words in context.", "Vocabulary is a word.", "Universities have libraries."),
        answer = 1,
        explanation = "The sentence explains how reading can develop vocabulary by showing the mechanism of repeated contextual exposure."
    ),
    PWLesson(
        title = "Unity & Coherence",
        objective = "Create paragraphs in which all sentences support one idea and follow a clear logical order.",
        keyIdea = "Unity concerns relevance; coherence concerns logical flow.",
        content = "A unified paragraph avoids unrelated information. A coherent paragraph makes relationships between ideas clear through logical order, repeated key terms and appropriate transitions.",
        terms = listOf("unity", "coherence", "logical order", "transition", "relevance"),
        exampleTitle = "Useful transitions",
        example = "First, students identify the problem. Then, they collect relevant information. Finally, they evaluate the evidence before reaching a conclusion.",
        practice = "Choose three sentences about one topic and arrange them from general point to explanation to example.",
        question = "Which feature mainly helps a reader follow the logical relationship between ideas?",
        options = listOf("Random details", "Transitions and logical order", "Longer words", "More punctuation only"),
        answer = 1,
        explanation = "Logical order and transitions make connections between sentences explicit and easier to follow."
    ),
    PWLesson(
        title = "Cohesion & Academic Style",
        objective = "Use cohesive devices and formal language appropriately in academic paragraphs.",
        keyIdea = "Cohesion connects sentences grammatically and lexically so that the paragraph reads as a unified whole.",
        content = "Cohesion can be created through pronouns, repetition of key terms, synonyms, conjunctions and transition signals. Academic style generally favors precise vocabulary, controlled tone and clear sentence structures.",
        terms = listOf("cohesion", "reference", "conjunction", "transition", "formal register"),
        exampleTitle = "Cohesive link",
        example = "Online resources are useful for research. However, students must evaluate their reliability before using them in academic work.",
        practice = "Combine two short sentences using an appropriate cohesive device.",
        question = "Which word signals contrast?",
        options = listOf("Therefore", "For example", "However", "Similarly"),
        answer = 2,
        explanation = "'However' introduces a contrast between two ideas."
    ),
    PWLesson(
        title = "Types of Paragraphs",
        objective = "Distinguish common paragraph purposes and select appropriate organizational patterns.",
        keyIdea = "Paragraph structure should match communicative purpose.",
        content = "Common academic paragraph purposes include description, narration, explanation, comparison and contrast, classification, cause and effect, and argument. The writer chooses supporting details and organization according to the purpose.",
        terms = listOf("description", "narration", "comparison", "cause and effect", "argument"),
        exampleTitle = "Purpose and structure",
        example = "Comparison paragraphs commonly organize similarities and differences between two subjects. Cause-and-effect paragraphs explain relationships between events, actions or conditions.",
        practice = "Write one topic sentence for a comparison paragraph and one for a cause-and-effect paragraph.",
        question = "Which paragraph type focuses on similarities and differences?",
        options = listOf("Comparison and contrast", "Narration only", "Definition only", "Instruction only"),
        answer = 0,
        explanation = "Comparison and contrast paragraphs organize information around similarities, differences or both."
    ),
    PWLesson(
        title = "Writing Process",
        objective = "Use planning, drafting, revising and editing as stages of paragraph writing.",
        keyIdea = "Good writing is a process rather than a single act of writing the final version.",
        content = "A practical process begins with planning and brainstorming, followed by drafting. Revision improves ideas and organization; editing improves grammar, vocabulary, spelling and punctuation. Proofreading is the final check before submission.",
        terms = listOf("planning", "draft", "revision", "editing", "proofreading"),
        exampleTitle = "Process sequence",
        example = "Plan → Draft → Revise → Edit → Proofread → Submit",
        practice = "Make a five-minute plan before writing a paragraph on 'The value of independent study'.",
        question = "Which stage mainly focuses on improving ideas and organization?",
        options = listOf("Revision", "Proofreading", "Typing", "Formatting only"),
        answer = 0,
        explanation = "Revision examines the quality and organization of ideas, while editing focuses more on language accuracy."
    ),
    PWLesson(
        title = "Paragraph Development",
        objective = "Develop ideas with concrete details, examples, explanation and controlled expansion.",
        keyIdea = "Development means giving the reader enough information to understand and accept the main point.",
        content = "A paragraph becomes developed when the writer moves beyond assertion. Useful techniques include illustration, explanation, example, comparison, definition, cause-and-effect reasoning and brief evidence.",
        terms = listOf("development", "illustration", "definition", "example", "elaboration"),
        exampleTitle = "Assertion → explanation → example",
        example = "Independent study builds learner autonomy. When students plan their own reading and practice, they learn to monitor their progress. For example, a student can keep a weekly vocabulary record and review it systematically.",
        practice = "Expand one sentence into a three-sentence mini-paragraph using explanation and an example.",
        question = "What best develops an academic claim?",
        options = listOf("Repeating the claim several times", "Adding relevant explanation and evidence", "Changing the font", "Adding unrelated facts"),
        answer = 1,
        explanation = "Development comes from relevant explanation, examples and evidence that clarify the controlling idea."
    ),
    PWLesson(
        title = "Editing & Revision",
        objective = "Use a systematic checklist to improve paragraph accuracy, clarity and coherence.",
        keyIdea = "Revision asks whether the paragraph works; editing asks whether the language is correct.",
        content = "Before submission, check focus, organization, transitions, sentence boundaries, subject–verb agreement, verb tense, articles, word choice, spelling and punctuation. Read the paragraph slowly and compare it with the assignment question.",
        terms = listOf("clarity", "accuracy", "revision", "editing", "proofreading"),
        exampleTitle = "Revision checklist",
        example = "1. Is the topic sentence clear? 2. Does every sentence support it? 3. Is the order logical? 4. Are transitions accurate? 5. Are grammar and spelling correct?",
        practice = "Take a paragraph you wrote and mark one issue in content, one in organization and one in language accuracy.",
        question = "Which question is most useful for checking unity?",
        options = listOf("Are all sentences related to the controlling idea?", "Is the title in capital letters?", "Is the paragraph exactly five sentences?", "Did I use difficult words?"),
        answer = 0,
        explanation = "Unity is checked by asking whether all sentences contribute to the paragraph's controlling idea."
    ),
    PWLesson(
        title = "Assessment",
        objective = "Apply the full paragraph-writing process in a controlled academic task.",
        keyIdea = "A strong paragraph combines focus, development, coherence, cohesion, accuracy and appropriate academic style.",
        content = "For assessment, begin by interpreting the prompt. Plan a controlling idea and supporting points, write a first draft, then revise and edit. Keep the paragraph focused and use concrete, relevant support.",
        terms = listOf("prompt", "criteria", "draft", "coherence", "accuracy"),
        exampleTitle = "Assessment prompt",
        example = "Write 120–150 words on: 'Why should university students develop independent study habits?' Include a clear topic sentence, at least three supporting sentences and a concluding sentence.",
        practice = "Write the paragraph in your notebook or editor, then use the revision checklist from Lesson 9.",
        question = "Which combination best represents effective paragraph writing?",
        options = listOf("Focus + development + coherence + accuracy", "Length + difficult vocabulary only", "Many ideas + no transitions", "Short sentences only"),
        answer = 0,
        explanation = "Effective paragraphs combine a focused idea with relevant development, logical flow and accurate academic language."
    )
)



private data class PWStage(
    val title: String,
    val subtitle: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

private val PWStages = listOf(
    PWStage("Learn", "Build the concept", Icons.Default.Lightbulb),
    PWStage("Model", "Study a model", Icons.Default.Visibility),
    PWStage("Analyse", "Identify the technique", Icons.Default.Psychology),
    PWStage("Practise", "Train with exercises", Icons.Default.EditNote),
    PWStage("Write", "Produce your paragraph", Icons.Default.Create),
    PWStage("Revise", "Improve the draft", Icons.Default.Refresh),
    PWStage("Checklist", "Check quality", Icons.Default.CheckCircle),
    PWStage("Assess", "Demonstrate mastery", Icons.Default.Assignment)
)

@Composable
fun ParagraphWritingScreen(onBack: () -> Unit = {}) {
    var selectedLesson by remember { mutableIntStateOf(0) }
    var selectedStage by remember { mutableIntStateOf(0) }
    var selectedExercise by remember { mutableIntStateOf(0) }
    var selectedAnswer by remember { mutableIntStateOf(-1) }
    var showResult by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }

    val context = LocalContext.current
    val lesson = PWLessons[selectedLesson]
    val exercises = PWExercises[selectedLesson]
    val exercise = exercises[selectedExercise]

    val tts = remember {
        TextToSpeech(context) { }
    }

    DisposableEffect(Unit) {
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    LaunchedEffect(Unit) {
        EnglishStudiesSpeech.configure(tts, context)
    }

    fun resetExercise() {
        selectedAnswer = -1
        showResult = false
    }

    fun selectLesson(index: Int) {
        selectedLesson = index
        selectedStage = 0
        selectedExercise = 0
        selectedAnswer = -1
        showResult = false
        score = 0
    }

    Scaffold(
        containerColor = PWBackground,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Paragraph Writing 1",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            "English Studies • Semester 1",
                            color = Color.White.copy(alpha = 0.88f),
                            fontSize = 11.sp
                        )
                    }
                },
                navigationIcon = {
                    androidx.compose.material3.IconButton(onClick = onBack) {
                        androidx.compose.material3.Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PWPrimary
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PWBackground)
                .padding(padding)
        ) {

            PWProfessionalHeader(
                lessonNumber = selectedLesson + 1,
                lessonCount = PWLessons.count(),
                exerciseCount = PWExercises.flatten().count()
            )

            // Lesson navigation: compact, vertical and readable.
            PWLessonNavigator(
                lessons = PWLessons,
                selectedLesson = selectedLesson,
                onLessonSelected = ::selectLesson
            )

            // Lesson identity.
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Lesson ${selectedLesson + 1}",
                    color = PWPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = lesson.title,
                    color = PWText,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = lesson.objective,
                    color = PWMuted,
                    fontSize = 14.sp,
                    lineHeight = 21.sp
                )
            }

            // Pedagogical roadmap.
            PWStageGrid(
                selectedStage = selectedStage,
                onStageSelected = { selectedStage = it }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                when (selectedStage) {
                    0 -> PWRedesignedLearn(lesson)
                    1 -> PWRedesignedModel(
                        lesson = lesson,
                        onSpeak = {
                            EnglishStudiesSpeech.speak(
                                tts = tts,
                                context = context,
                                text = lesson.example,
                                utteranceId = "pw-model-$selectedLesson"
                            )
                        }
                    )
                    2 -> PWRedesignedAnalysis(lesson)
                    3 -> PWRedesignedPractice(
                        lesson = lesson,
                        exercises = exercises,
                        selectedExercise = selectedExercise,
                        exercise = exercise,
                        selectedAnswer = selectedAnswer,
                        showResult = showResult,
                        score = score,
                        onAnswer = { index ->
                            if (!showResult) {
                                selectedAnswer = index
                                showResult = true
                                if (index == exercise.answer) score += 1
                            }
                        },
                        onRetry = ::resetExercise,
                        onNext = {
                            if (selectedExercise < exercises.lastIndex) {
                                selectedExercise += 1
                            } else {
                                selectedExercise = 0
                            }
                            resetExercise()
                        }
                    )
                    4 -> PWRedesignedWrite(
                        lesson = lesson,
                        onSpeak = {
                            EnglishStudiesSpeech.speak(
                                tts = tts,
                                context = context,
                                text = lesson.example,
                                utteranceId = "pw-write-$selectedLesson"
                            )
                        }
                    )
                    5 -> PWRedesignedRevise(lesson)
                    6 -> PWRedesignedChecklist(lesson)
                    else -> PWRedesignedAssess(
                        lesson = lesson,
                        score = score,
                        exerciseCount = exercises.count()
                    )
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun PWProfessionalHeader(
    lessonNumber: Int,
    lessonCount: Int,
    exerciseCount: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = PWPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(
                "PARAGRAPH WRITING",
                color = Color.White.copy(alpha = 0.72f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(5.dp))
            Text(
                "Write clearly, develop ideas and revise effectively.",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 28.sp
            )
            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PWHeaderMetric("Lesson", "$lessonNumber/$lessonCount")
                PWHeaderMetric("Exercises", "$exerciseCount")
                PWHeaderMetric("Progress", "${(lessonNumber * 100) / lessonCount}%")
            }
        }
    }
}

@Composable
private fun PWHeaderMetric(label: String, value: String) {
    Column {
        Text(
            label,
            color = Color.White.copy(alpha = 0.72f),
            fontSize = 10.sp
        )
        Text(
            value,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun PWLessonNavigator(
    lessons: List<PWLesson>,
    selectedLesson: Int,
    onLessonSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            "Lessons",
            color = PWText,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            lessons.forEachIndexed { index, item ->
                Card(
                    modifier = Modifier.clickable {
                        onLessonSelected(index)
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor =
                            if (selectedLesson == index) PWPrimary else Color.White
                    ),
                    border = if (selectedLesson == index) {
                        null
                    } else {
                        androidx.compose.foundation.BorderStroke(1.dp, PWBorder)
                    }
                ) {
                    Column(
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 10.dp
                        )
                    ) {
                        Text(
                            "${index + 1}",
                            color = if (selectedLesson == index) {
                                Color.White.copy(alpha = 0.78f)
                            } else {
                                PWPrimary
                            },
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            item.title,
                            color = if (selectedLesson == index) {
                                Color.White
                            } else {
                                PWText
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PWStageGrid(
    selectedStage: Int,
    onStageSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            "Learning pathway",
            color = PWText,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        PWStages.chunked(2).forEach { rowStages ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowStages.forEach { stage ->
                    val stageIndex = PWStages.indexOf(stage)

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                onStageSelected(stageIndex)
                            },
                        shape = RoundedCornerShape(15.dp),
                        colors = CardDefaults.cardColors(
                            containerColor =
                                if (selectedStage == stageIndex) {
                                    PWSoftBlue
                                } else {
                                    Color.White
                                }
                        ),
                        border = if (selectedStage == stageIndex) {
                            androidx.compose.foundation.BorderStroke(
                                1.dp,
                                PWPrimary
                            )
                        } else {
                            androidx.compose.foundation.BorderStroke(
                                1.dp,
                                PWBorder
                            )
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(11.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            androidx.compose.material3.Icon(
                                stage.icon,
                                contentDescription = null,
                                tint = PWPrimary,
                                modifier = Modifier.size(22.dp)
                            )

                            Spacer(Modifier.width(8.dp))

                            Column {
                                Text(
                                    stage.title,
                                    color = PWText,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    stage.subtitle,
                                    color = PWMuted,
                                    fontSize = 10.sp,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }

                if (rowStages.count() == 1) {
                    Spacer(Modifier.weight(1f))
                }
            }

            Spacer(Modifier.height(7.dp))
        }
    }
}

@Composable
private fun PWSectionTitle(
    step: String,
    title: String,
    subtitle: String
) {
    Column {
        Text(
            step.uppercase(),
            color = PWPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(3.dp))
        Text(
            title,
            color = PWText,
            fontSize = 21.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(Modifier.height(3.dp))
        Text(
            subtitle,
            color = PWMuted,
            fontSize = 13.sp,
            lineHeight = 19.sp
        )
    }
}

@Composable
private fun PWAcademicCard(
    title: String,
    text: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, PWBorder)
    ) {
        Column(Modifier.padding(17.dp)) {
            Text(
                title,
                color = PWText,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text,
                color = PWText,
                fontSize = 14.sp,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
private fun PWTermsStrip(terms: List<String>) {
    Column {
        Text(
            "Key terms",
            color = PWText,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(7.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            terms.forEach {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = PWSoftBlue
                    )
                ) {
                    Text(
                        it,
                        color = PWPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 7.dp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun PWRedesignedLearn(lesson: PWLesson) {
    PWSectionTitle(
        "Step 1 • Learn",
        lesson.title,
        "Build a clear understanding before moving to production."
    )
    PWAcademicCard("Learning objective", lesson.objective)
    PWAcademicCard("Core concept", lesson.keyIdea)
    PWAcademicCard("Lesson content", lesson.content)
    PWTermsStrip(lesson.terms)
}

@Composable
private fun PWRedesignedModel(
    lesson: PWLesson,
    onSpeak: () -> Unit
) {
    PWSectionTitle(
        "Step 2 • Model",
        "Study the model",
        "Observe how the lesson concept is realised in actual writing."
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, PWBorder)
    ) {
        Column(Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(
                        lesson.exampleTitle,
                        color = PWText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                    Text(
                        "Read for structure, language and technique.",
                        color = PWMuted,
                        fontSize = 12.sp
                    )
                }

                androidx.compose.material3.IconButton(onClick = onSpeak) {
                    androidx.compose.material3.Icon(
                        Icons.Default.VolumeUp,
                        contentDescription = "Read aloud",
                        tint = PWPrimary
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                lesson.example,
                color = PWText,
                fontSize = 15.sp,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
private fun PWRedesignedAnalysis(lesson: PWLesson) {
    PWSectionTitle(
        "Step 3 • Analyse",
        "Analyse the model",
        "Move from recognition to conscious control of the writing technique."
    )

    PWAcademicCard(
        "What should you notice?",
        "Identify the controlling idea, the relationship between sentences, the supporting information and the language choices that make the paragraph effective."
    )

    PWAcademicCard(
        "Analyse this model",
        lesson.example
    )

    PWAcademicCard(
        "Guiding question",
        lesson.question
    )

    PWAcademicCard(
        "Expected reasoning",
        lesson.explanation
    )
}

@Composable
private fun PWRedesignedPractice(
    lesson: PWLesson,
    exercises: List<PWExercise>,
    selectedExercise: Int,
    exercise: PWExercise,
    selectedAnswer: Int,
    showResult: Boolean,
    score: Int,
    onAnswer: (Int) -> Unit,
    onRetry: () -> Unit,
    onNext: () -> Unit
) {
    PWSectionTitle(
        "Step 4 • Practise",
        "Guided practice",
        "Use controlled exercises before attempting independent writing."
    )

    PWAcademicCard("Task", lesson.practice)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = PWSoftBlue)
    ) {
        Column(Modifier.padding(17.dp)) {
            Text(
                "Exercise ${selectedExercise + 1} of ${exercises.count()}",
                color = PWPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Score: $score/${exercises.count()}",
                color = PWText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, PWBorder)
    ) {
        Column(Modifier.padding(17.dp)) {
            Text(
                exercise.type,
                color = PWPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                exercise.question,
                color = PWText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 23.sp
            )

            Spacer(Modifier.height(12.dp))

            exercise.options.forEachIndexed { index, option ->
                val selected = selectedAnswer == index
                val correct = showResult && index == exercise.answer

                Button(
                    onClick = { onAnswer(index) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    enabled = !showResult,
                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                            when {
                                correct -> Color(0xFFDCFCE7)
                                selected -> Color(0xFFFEE2E2)
                                else -> Color(0xFFF1F5F9)
                            },
                        contentColor =
                            when {
                                correct -> PWSuccess
                                selected -> Color(0xFFB91C1C)
                                else -> PWText
                            }
                    ),
                    shape = RoundedCornerShape(13.dp)
                ) {
                    Text(
                        option,
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    )
                }
            }

            if (showResult) {
                Spacer(Modifier.height(10.dp))
                Text(
                    if (selectedAnswer == exercise.answer) {
                        "Correct."
                    } else {
                        "Review the explanation below."
                    },
                    color = if (selectedAnswer == exercise.answer) {
                        PWSuccess
                    } else {
                        Color(0xFFB91C1C)
                    },
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    exercise.explanation,
                    color = PWMuted,
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onRetry,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Retry")
                    }

                    Button(
                        onClick = onNext,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Next")
                    }
                }
            }
        }
    }
}

@Composable
private fun PWRedesignedWrite(
    lesson: PWLesson,
    onSpeak: () -> Unit
) {
    PWSectionTitle(
        "Step 5 • Write",
        "Produce your paragraph",
        "Apply the lesson through your own controlled piece of academic writing."
    )

    PWAcademicCard(
        "Writing task",
        lesson.practice
    )

    PWAcademicCard(
        "Recommended process",
        "Plan the controlling idea → choose relevant support → draft → reread for unity and coherence."
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(17.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Model reminder",
                    color = PWText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                androidx.compose.material3.IconButton(onClick = onSpeak) {
                    androidx.compose.material3.Icon(
                        Icons.Default.VolumeUp,
                        contentDescription = "Read model",
                        tint = PWPrimary
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                lesson.example,
                color = PWText,
                fontSize = 14.sp,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
private fun PWRedesignedRevise(lesson: PWLesson) {
    PWSectionTitle(
        "Step 6 • Revise",
        "Improve the draft",
        "Revision focuses on ideas, organisation and clarity before final editing."
    )

    PWAcademicCard(
        "Revision priorities",
        "Check whether the paragraph stays focused, whether supporting information is relevant, whether the order is logical and whether the explanation is sufficient."
    )

    PWAcademicCard(
        "Apply to your paragraph",
        "Read your draft once for meaning only. Mark one place where an idea needs stronger support and one place where the logical connection between sentences could be clearer."
    )

    PWAcademicCard(
        "Lesson reminder",
        lesson.keyIdea
    )
}

@Composable
private fun PWRedesignedChecklist(lesson: PWLesson) {
    PWSectionTitle(
        "Step 7 • Checklist",
        "Quality check",
        "Before submission, evaluate content and language systematically."
    )

    val checks = listOf(
        "The topic sentence has a clear controlling idea.",
        "Every sentence contributes to the paragraph's main idea.",
        "Supporting information is relevant and sufficiently developed.",
        "The order of ideas is logical.",
        "Transitions and cohesive devices are appropriate.",
        "Grammar, spelling and punctuation have been checked."
    )

    checks.forEachIndexed { index, check ->
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier.padding(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "${index + 1}",
                    color = PWPrimary,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    check,
                    color = PWText,
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }

    Spacer(Modifier.height(3.dp))

    Text(
        "Current lesson focus: ${lesson.title}",
        color = PWMuted,
        fontSize = 12.sp
    )
}

@Composable
private fun PWRedesignedAssess(
    lesson: PWLesson,
    score: Int,
    exerciseCount: Int
) {
    PWSectionTitle(
        "Step 8 • Assess",
        "Demonstrate mastery",
        "Bring the complete paragraph-writing process together."
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(19.dp),
        colors = CardDefaults.cardColors(containerColor = PWSoftBlue)
    ) {
        Column(Modifier.padding(18.dp)) {
            Text(
                "Your practice score",
                color = PWPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "$score / $exerciseCount",
                color = PWText,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                "Use the score as feedback, then complete the writing task independently.",
                color = PWMuted,
                fontSize = 13.sp
            )
        }
    }

    PWAcademicCard(
        "Final task",
        lesson.example
    )

    PWAcademicCard(
        "Assessment criteria",
        "Focus • Development • Unity • Coherence • Cohesion • Academic style • Language accuracy"
    )

    PWAcademicCard(
        "Self-assessment",
        "Can you explain the lesson concept, apply it in your own paragraph and identify one improvement you would make after revision?"
    )
}
