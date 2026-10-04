package ma.fldm.englishstudies.updates

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import ma.fldm.englishstudies.BuildConfig

class UpdateRepository {

    private val firestore =
        FirebaseFirestore.getInstance()

    fun checkForUpdate(
        onResult: (Result<UpdateInfo?>) -> Unit
    ) {

        Log.d(
            "UpdateRepository",
            "Début de la vérification de mise à jour"
        )

        firestore
            .collection("app_config")
            .document("android")
            .get()
            .addOnSuccessListener { document ->

                Log.d(
                    "UpdateRepository",
                    "Firestore OK - exists=${document.exists()} data=${document.data}"
                )

                if (!document.exists()) {
                    Log.d(
                        "UpdateRepository",
                        "Le document app_config/android n'existe pas"
                    )

                    onResult(Result.success(null))
                    return@addOnSuccessListener
                }

                val versionCode =
                    document
                        .getLong("versionCode")
                        ?.toInt()
                        ?: 0

                val versionName =
                    document
                        .getString("versionName")
                        ?: ""

                val downloadUrl =
                    document
                        .getString("downloadUrl")
                        ?: ""

                val changelog =
                    document
                        .getString("changelog")
                        ?: ""

                val mandatory =
                    document
                        .getBoolean("mandatory")
                        ?: false

                val currentVersionCode =
                    BuildConfig.VERSION_CODE

                Log.d(
                    "UpdateRepository",
                    "Version distante = $versionCode / " +
                            "$versionName ; " +
                            "Version actuelle = $currentVersionCode"
                )

                if (versionCode > currentVersionCode) {

                    Log.d(
                        "UpdateRepository",
                        "MISE À JOUR DISPONIBLE"
                    )

                    onResult(
                        Result.success(
                            UpdateInfo(
                                versionCode = versionCode,
                                versionName = versionName,
                                downloadUrl = downloadUrl,
                                changelog = changelog,
                                mandatory = mandatory
                            )
                        )
                    )

                } else {

                    Log.d(
                        "UpdateRepository",
                        "Aucune mise à jour disponible"
                    )

                    onResult(Result.success(null))
                }
            }
            .addOnFailureListener { exception ->

                Log.e(
                    "UpdateRepository",
                    "ERREUR FIRESTORE : ${exception.message}",
                    exception
                )

                onResult(
                    Result.failure(exception)
                )
            }
    }
}