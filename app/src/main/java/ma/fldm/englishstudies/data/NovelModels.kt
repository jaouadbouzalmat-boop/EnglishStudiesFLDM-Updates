package ma.fldm.englishstudies.data

data class NovelVocabulary(
    val word: String,
    val pronunciation: String,
    val definition: String,
    val french: String,
    val arabic: String,
    val example: String
)

data class NovelQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class NovelScene(
    val title: String,
    val image: String? = null,
    val englishText: String,
    val frenchTranslation: String,
    val arabicExplanation: String,
    val vocabulary: List<NovelVocabulary> = emptyList(),
    val questions: List<NovelQuestion> = emptyList(),
    val audioResId: Int? = null
)

data class NovelChapter(
    val number: Int,
    val title: String,
    val subtitle: String = "",
    val summary: String = "",
    val scenes: List<NovelScene>,
    val expanded: Boolean = false
)

data class NovelBook(
    val id: String,
    val title: String,
    val author: String,
    val semester: String,
    val module: String,
    val description: String = "",
    val chapters: List<NovelChapter>
)

