package ma.fldm.englishstudies.settings

import android.app.Application
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository =
        SettingsRepository(
            application.applicationContext
        )

    val settings: StateFlow<SettingsState> =
        repository.settings.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsState()
        )

    fun setBoolean(
        key: Preferences.Key<Boolean>,
        value: Boolean
    ) {
        viewModelScope.launch {
            repository.setBoolean(key, value)
        }
    }

    fun setString(
        key: Preferences.Key<String>,
        value: String
    ) {
        viewModelScope.launch {
            repository.setString(key, value)
        }
    }

    fun setFloat(
        key: Preferences.Key<Float>,
        value: Float
    ) {
        viewModelScope.launch {
            repository.setFloat(key, value)
        }
    }

    fun setInt(
        key: Preferences.Key<Int>,
        value: Int
    ) {
        viewModelScope.launch {
            repository.setInt(key, value)
        }
    }
}