package ma.fldm.englishstudies

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

data class LicenseInfo(
    val code: String,
    val status: String,
    val deviceId: String?
)

object LicenseManager {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    /**
     * Assure qu'un utilisateur Firebase anonyme existe.
     */
    fun ensureAnonymousUser(
        onResult: (Result<String>) -> Unit
    ) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            onResult(Result.success(currentUser.uid))
            return
        }

        auth.signInAnonymously()
            .addOnSuccessListener { result ->
                val uid = result.user?.uid

                if (uid != null) {
                    onResult(Result.success(uid))
                } else {
                    onResult(
                        Result.failure(
                            Exception("Utilisateur Firebase introuvable.")
                        )
                    )
                }
            }
            .addOnFailureListener { error ->
                onResult(Result.failure(error))
            }
    }

    /**
     * Vérifie l'existence et l'état d'une licence.
     */
    fun checkLicense(
        licenseCode: String,
        onResult: (Result<LicenseInfo>) -> Unit
    ) {
        ensureAnonymousUser { authResult ->

            authResult.onSuccess {

                val code = licenseCode.trim()

                db.collection("licenses")
                    .document(code)
                    .get()
                    .addOnSuccessListener { document ->

                        if (!document.exists()) {
                            onResult(
                                Result.failure(
                                    Exception("Licence introuvable.")
                                )
                            )
                            return@addOnSuccessListener
                        }

                        val status =
                            document.getString("status") ?: ""

                        val deviceId =
                            document.getString("deviceId")

                        onResult(
                            Result.success(
                                LicenseInfo(
                                    code = code,
                                    status = status,
                                    deviceId = deviceId
                                )
                            )
                        )
                    }
                    .addOnFailureListener { error ->
                        onResult(Result.failure(error))
                    }

            }.onFailure { error ->

                onResult(
                    Result.failure(
                        error
                    )
                )
            }
        }
    }

    /**
     * Active une licence disponible sur l'utilisateur Firebase courant.
     */
    fun activateLicense(
        licenseCode: String,
        onResult: (Result<Boolean>) -> Unit
    ) {
        ensureAnonymousUser { authResult ->

            authResult.onSuccess { uid ->

                val code = licenseCode.trim()

                val ref = db.collection("licenses")
                    .document(code)

                db.runTransaction { transaction ->

                    val snapshot = transaction.get(ref)

                    if (!snapshot.exists()) {
                        throw Exception("Licence introuvable.")
                    }

                    val status =
                        snapshot.getString("status") ?: ""

                    val deviceId =
                        snapshot.getString("deviceId") ?: ""

                    when {

                        status == "available" &&
                                deviceId.isEmpty() -> {

                            transaction.update(
                                ref,
                                mapOf(
                                    "status" to "active",
                                    "deviceId" to uid,
                                    "activatedAt" to FieldValue.serverTimestamp()
                                )
                            )

                            true
                        }

                        status == "active" &&
                                deviceId == uid -> {
                            true
                        }

                        else -> {
                            throw Exception(
                                "Cette licence est déjà utilisée sur un autre appareil."
                            )
                        }
                    }

                }.addOnSuccessListener {

                    onResult(Result.success(true))

                }.addOnFailureListener { error ->

                    onResult(Result.failure(error))
                }

            }.onFailure { error ->

                onResult(Result.failure(error))
            }
        }
    }
}

