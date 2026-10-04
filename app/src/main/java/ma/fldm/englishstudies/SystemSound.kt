package ma.fldm.englishstudies

import android.view.SoundEffectConstants
import android.view.View

/**
 * Son de clic système centralisé.
 * Respecte le réglage « Son » de l'application.
 */
object SystemSound {
    private const val PREFS_NAME = "english_studies_settings"
    private const val KEY_SOUND = "sound"

    fun isEnabled(view: View): Boolean =
        view.context
            .getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE)
            .getBoolean(KEY_SOUND, true)

    fun click(view: View) {
        if (isEnabled(view)) {
            view.playSoundEffect(SoundEffectConstants.CLICK)
        }
    }
}


