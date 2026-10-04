package ma.fldm.englishstudies.updates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UpdateUiState {

    data object Idle : UpdateUiState()

    data object Checking : UpdateUiState()

    data object UpToDate : UpdateUiState()

    data class Available(
        val updateInfo: UpdateInfo
    ) : UpdateUiState()

    data class Error(
        val message: String
    ) : UpdateUiState()
}

class UpdateViewModel : ViewModel() {

    private val repository =
        UpdateRepository()

    private val _uiState =
        MutableStateFlow<UpdateUiState>(
            UpdateUiState.Idle
        )

    val uiState: StateFlow<UpdateUiState> =
        _uiState.asStateFlow()

    fun checkForUpdate() {

        _uiState.value =
            UpdateUiState.Checking

        repository.checkForUpdate { result ->

            result
                .onSuccess { updateInfo ->

                    _uiState.value =
                        if (updateInfo != null) {
                            UpdateUiState.Available(
                                updateInfo
                            )
                        } else {
                            UpdateUiState.UpToDate
                        }
                }

                .onFailure { exception ->

                    _uiState.value =
                        UpdateUiState.Error(
                            exception.message
                                ?: "Erreur inconnue"
                        )
                }
        }
    }
}

