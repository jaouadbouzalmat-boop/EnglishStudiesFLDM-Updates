package ma.fldm.englishstudies

object OliverPageComposerTest {

    fun analyserPage1(): String {

        val pageText = """
            Oliver Twist was born in a workhouse. His mother was very young,
            and she died soon after his birth. Oliver had no father and no family.
            The workhouse was cold, and the food was very simple.
        """.trimIndent()

        val plan = OliverPageComposer.createPagePlan(
            chapterNumber = 1,
            pageNumber = 1,
            pageText = pageText
        )

        return buildString {

            appendLine("==========================================")
            appendLine("🧠 ANALYSE INTELLIGENTE DE LA PAGE")
            appendLine("==========================================")
            appendLine()

            appendLine("Chapitre : ${plan.chapterNumber}")
            appendLine("Page     : ${plan.pageNumber}")
            appendLine(
                "Nombre d'illustrations : " +
                        plan.illustrationCount
            )

            appendLine()

            plan.scenes.forEach { scene ->

                appendLine("------------------------------------------")
                appendLine("🎨 SCÈNE ${scene.index}")
                appendLine("------------------------------------------")

                appendLine("Type : ${scene.type}")

                appendLine(
                    "Personnages : ${
                        if (scene.characters.isEmpty()) {
                            "Aucun détecté"
                        } else {
                            scene.characters.joinToString(", ")
                        }
                    }"
                )

                appendLine("Lieu : ${scene.location}")
                appendLine("Action : ${scene.action}")
                appendLine("Ambiance : ${scene.mood}")

                appendLine()

                appendLine("Description visuelle :")
                appendLine(scene.visualDescription)

                appendLine()

                appendLine("Prompt pour l'illustration :")
                appendLine(scene.imagePrompt)

                appendLine()
            }

            appendLine("==========================================")
            appendLine("✅ FIN DE L'ANALYSE")
            appendLine("==========================================")
        }
    }
}