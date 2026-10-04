package ma.fldm.englishstudies

import android.content.Context
import android.provider.Settings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.security.MessageDigest

data class TrialInfo(
    val available: Boolean,
    val startedAtMillis: Long? = null
)

object TrialManager {

    private const val TRIAL_DAYS = 7L
    private const val COLLECTION = "trials"

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    /**
     * Identifiant stable de l'appareil pour Android 8+ :
     * ANDROID_ID, haché en SHA-256 avant stockage.
     */
    private fun getDeviceId(context: Context): String {

        val androidId = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ) ?: "unknown-device"

        val bytes = MessageDigest
            .getInstance("SHA-256")
            .digest(androidId.toByteArray(Charsets.UTF_8))

        return bytes.joinToString("") {
            "%02x".format(it)
        }
    }

    /**
     * Crée le premier essai de 7 jours pour cet appareil,
     * ou récupère l'essai déjà existant.
     */
    fun checkTrial(
        context: Context,
        onResult: (Result<TrialInfo>) -> Unit
    ) {

        val currentUser = auth.currentUser

        if (currentUser == null) {
            onResult(
                Result.failure(
                    Exception("Utilisateur Firebase non authentifié.")
                )
            )
            return
        }

        val deviceId = getDeviceId(context)

        val ref = db.collection(COLLECTION)
            .document(deviceId)

        ref.get()
            .addOnSuccessListener { document ->

                if (document.exists()) {

                    val startedAt =
                        document.getTimestamp("startedAt")

                    onResult(
                        Result.success(
                            TrialInfo(
                                available = true,
                                startedAtMillis =
                                    startedAt?.toDate()?.time
                            )
                        )
                    )

                } else {

                    ref.set(
                        mapOf(
                            "startedAt" to FieldValue.serverTimestamp()
                        )
                    )
                        .addOnSuccessListener {

                            onResult(
                                Result.success(
                                    TrialInfo(
                                        available = true
                                    )
                                )
                            )
                        }
                        .addOnFailureListener { error ->

                            onResult(
                                Result.failure(error)
                            )
                        }
                }
            }
            .addOnFailureListener { error ->

                onResult(
                    Result.failure(error)
                )
            }
    }

    /**
     * Nombre de jours restant selon l'heure locale
     * par rapport au timestamp de démarrage retourné par Firestore.
     *
     * La vraie protection finale sera assurée par les règles Firestore.
     */
    fun remainingDays(startedAtMillis: Long): Long {

        val duration =
            TRIAL_DAYS * 24L * 60L * 60L * 1000L

        val remaining =
            startedAtMillis + duration -
                    System.currentTimeMillis()

        if (remaining <= 0L) {
            return 0L
        }

        return (remaining +
                24L * 60L * 60L * 1000L - 1L) /
                (24L * 60L * 60L * 1000L)
    }
}

