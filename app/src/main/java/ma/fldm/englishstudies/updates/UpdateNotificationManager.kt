package ma.fldm.englishstudies.updates

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember

import androidx.core.content.ContextCompat

import com.google.firebase.messaging.FirebaseMessaging

private const val UPDATE_TOPIC = "english_studies_updates"

private const val FCM_TAG = "EnglishStudiesFCM"

@Composable
fun SetupUpdateNotifications(
    context: Context
) {

    val applicationContext = remember(context) {
        context.applicationContext
    }

    /*
     * Demande l'autorisation d'afficher
     * les notifications sur Android 13+
     */
    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->

            if (granted) {

                Log.d(
                    FCM_TAG,
                    "Permission de notification accordée"
                )

                subscribeToUpdateTopic()

            } else {

                Log.w(
                    FCM_TAG,
                    "Permission de notification refusée"
                )
            }
        }

    /*
     * Exécution une seule fois au lancement
     */
    LaunchedEffect(Unit) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            val permissionGranted =
                ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED

            if (permissionGranted) {

                Log.d(
                    FCM_TAG,
                    "Permission déjà accordée"
                )

                subscribeToUpdateTopic()

            } else {

                Log.d(
                    FCM_TAG,
                    "Demande de permission de notification"
                )

                permissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }

        } else {

            /*
             * Android 12 et inférieur :
             * pas de permission runtime POST_NOTIFICATIONS
             */
            subscribeToUpdateTopic()
        }
    }
}

/*
 * ============================================================
 * ABONNEMENT AU TOPIC
 * ============================================================
 */

private fun subscribeToUpdateTopic() {

    FirebaseMessaging
        .getInstance()
        .token
        .addOnCompleteListener { tokenTask ->

            if (!tokenTask.isSuccessful) {

                Log.e(
                    FCM_TAG,
                    "Impossible de récupérer le token FCM",
                    tokenTask.exception
                )

                return@addOnCompleteListener
            }

            val token = tokenTask.result

            /*
             * Affiche le token uniquement dans Logcat.
             * Ne jamais le publier publiquement.
             */
            Log.d(
                FCM_TAG,
                "TOKEN FCM = $token"
            )

            /*
             * Abonnement au topic de mise à jour
             */
            FirebaseMessaging
                .getInstance()
                .subscribeToTopic(
                    UPDATE_TOPIC
                )
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        Log.d(
                            FCM_TAG,
                            "Abonnement réussi : $UPDATE_TOPIC"
                        )

                    } else {

                        Log.e(
                            FCM_TAG,
                            "Échec de l'abonnement au topic",
                            task.exception
                        )
                    }
                }
        }
}
