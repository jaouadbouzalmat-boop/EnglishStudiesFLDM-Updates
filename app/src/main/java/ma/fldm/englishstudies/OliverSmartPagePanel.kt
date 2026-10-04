package ma.fldm.englishstudies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Panneau intelligent de la page Oliver Twist.
 *
 * Important :
 * Le panneau est déjà affiché dans une zone verticale défilante
 * de l'écran principal. Nous n'ajoutons donc PAS de verticalScroll()
 * à l'intérieur du panneau afin d'éviter le crash Compose :
 * "Vertically scrollable component was measured with an infinity maximum height".
 */
@Composable
fun OliverSmartPagePanel(
    chapterNumber: Int,
    pageNumber: Int,
    pageText: String
) {
    val pagePlan = remember(
        chapterNumber,
        pageNumber,
        pageText
    ) {
        OliverPageComposer.createPagePlan(
            chapterNumber = chapterNumber,
            pageNumber = pageNumber,
            pageText = pageText
        )
    }

    val vocabulary = remember(pageText) {
        OliverVocabularyEngine.extractBalanced(
            pageText = pageText,
            maxWords = 10
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {

            // =========================================================
            // ANALYSE INTELLIGENTE
            // =========================================================

            Text(
                text = "🧠 Analyse intelligente de la page",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text =
                    "Page ${pagePlan.pageNumber} • " +
                            "${pagePlan.illustrationCount} illustration(s) proposée(s)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OliverSmartInfoChip(
                    label = "Mode",
                    value = pagePlan.continuityMode,
                    modifier = Modifier.weight(1f)
                )

                OliverSmartInfoChip(
                    label = "Continuité",
                    value = "${pagePlan.similarityScore} %",
                    modifier = Modifier.weight(1f)
                )
            }

            if (pagePlan.changedElements.isNotEmpty()) {
                Spacer(modifier = Modifier.height(7.dp))

                Text(
                    text =
                        "🔄 Changements : " +
                                pagePlan.changedElements.joinToString(", "),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            pagePlan.scenes.forEach { scene ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    shape = androidx.compose.foundation.shape
                        .RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {

                        Text(
                            text = "🎨 Illustration ${scene.index}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Mode",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = scene.continuityMode,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text =
                                "👤 Personnages : " +
                                        if (scene.characters.isEmpty()) {
                                            "Aucun détecté"
                                        } else {
                                            scene.characters.joinToString(", ")
                                        },
                            fontSize = 12.sp
                        )

                        Text(
                            text = "📍 Lieu : ${scene.location}",
                            fontSize = 12.sp
                        )

                        Text(
                            text = "🎬 Action : ${scene.action}",
                            fontSize = 12.sp
                        )

                        Text(
                            text = "💭 Ambiance : ${scene.mood}",
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Description :",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(3.dp))

                        Text(
                            text = scene.visualDescription,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // =========================================================
            // VOCABULAIRE INTELLIGENT
            // =========================================================

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "📚 Vocabulaire intelligent de la page",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text =
                    if (vocabulary.isEmpty()) {
                        "Aucun mot du dictionnaire pédagogique local n'a été détecté."
                    } else {
                        "${vocabulary.size} mot(s) important(s) détecté(s) automatiquement"
                    },
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Pas de verticalScroll ici.
            // Le parent gère déjà le défilement vertical.
            vocabulary.forEachIndexed { index, item ->

                OliverSmartVocabularyCard(
                    vocabulary = item
                )

                if (index < vocabulary.lastIndex) {
                    Spacer(modifier = Modifier.height(7.dp))
                }
            }
        }
    }
}

@Composable
private fun OliverSmartInfoChip(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(9.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 8.dp,
                vertical = 7.dp
            )
        ) {
            Text(
                text = label,
                fontSize = 9.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = value,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun OliverSmartVocabularyCard(
    vocabulary: OliverVocabulary
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = vocabulary.word,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "🔊",
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = vocabulary.pronunciation,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "🇬🇧 ${vocabulary.definition}",
                fontSize = 11.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = "🇫🇷 ${vocabulary.french}",
                fontSize = 11.sp
            )

            Text(
                text = "🇲🇦 ${vocabulary.arabic}",
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Example: ${vocabulary.example}",
                fontSize = 10.sp,
                lineHeight = 15.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
