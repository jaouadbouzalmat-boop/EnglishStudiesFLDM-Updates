package ma.fldm.englishstudies.data

import android.content.Context
import ma.fldm.englishstudies.R

data class OliverFullChapter(
    val number: Int,
    val title: String,
    val text: String
)

object OliverTwistFullTextRepository {

    /**
     * Charge le texte intégral depuis res/raw/pg730.txt
     * et le découpe automatiquement en 53 chapitres.
     */
    fun loadChapters(context: Context): List<OliverFullChapter> {

        val fullText = context.resources
            .openRawResource(R.raw.pg730)
            .bufferedReader(Charsets.UTF_8)
            .use { it.readText() }

        return parseChapters(fullText)
    }

    private fun parseChapters(text: String): List<OliverFullChapter> {

        val normalized = text
            .replace("\r\n", "\n")
            .replace("\r", "\n")

        // Détecte :
        // CHAPTER I.
        // CHAPTER II.
        // ...
        // CHAPTER LIII.
        val chapterRegex = Regex(
            pattern = """(?m)^\s*CHAPTER\s+([IVXLCDM]+)\.\s*$"""
        )

        val matches = chapterRegex.findAll(normalized).toList()

        if (matches.isEmpty()) {
            return emptyList()
        }

        val chapters = mutableListOf<OliverFullChapter>()

        for (index in matches.indices) {

            val match = matches[index]

            val start = match.range.last + 1

            val end =
                if (index < matches.lastIndex) {
                    matches[index + 1].range.first
                } else {
                    normalized.length
                }

            val section = normalized
                .substring(start, end)
                .trim()

            val lines = section
                .lines()
                .map { it.trimEnd() }

            // Le titre du chapitre se trouve avant le premier
            // paragraphe réellement narratif.
            val titleLines = mutableListOf<String>()
            var bodyStartIndex = 0

            for (lineIndex in lines.indices) {

                val line = lines[lineIndex].trim()

                if (line.isBlank()) {
                    if (titleLines.isNotEmpty()) {
                        bodyStartIndex = lineIndex + 1

                        while (
                            bodyStartIndex < lines.size &&
                            lines[bodyStartIndex].isBlank()
                        ) {
                            bodyStartIndex++
                        }

                        break
                    }

                    continue
                }

                // Les titres Gutenberg sont en majuscules.
                // On les récupère jusqu'au premier bloc vide.
                if (titleLines.isEmpty()) {
                    titleLines.add(line)
                } else {
                    bodyStartIndex = lineIndex
                    break
                }
            }

            // Si la détection du titre n'est pas parfaite,
            // on conserve tout le contenu.
            if (bodyStartIndex <= 0) {
                bodyStartIndex = 0
            }

            val body = lines
                .drop(bodyStartIndex)
                .joinToString("\n")
                .trim()

            val romanNumber = match.groupValues[1]

            val chapterNumber = romanToInt(romanNumber)

            val title =
                titleLines
                    .filter { it.isNotBlank() }
                    .joinToString(" ")
                    .trim()
                    .ifBlank {
                        "Chapter $chapterNumber"
                    }

            chapters += OliverFullChapter(
                number = chapterNumber,
                title = title,
                text = cleanChapterText(body)
            )
        }

        return chapters
            .filter { it.number in 1..53 }
            .sortedBy { it.number }
    }

    private fun cleanChapterText(text: String): String {

        return text
            .replace(Regex("[ \t]+"), " ")
            .replace(Regex("\n{3,}"), "\n\n")
            .trim()
    }

    private fun romanToInt(roman: String): Int {

        val values = mapOf(
            'I' to 1,
            'V' to 5,
            'X' to 10,
            'L' to 50,
            'C' to 100,
            'D' to 500,
            'M' to 1000
        )

        var total = 0
        var previous = 0

        for (char in roman.reversed()) {

            val value = values[char] ?: 0

            if (value < previous) {
                total -= value
            } else {
                total += value
            }

            previous = value
        }

        return total
    }
}
