package im.manus.atlas.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import im.manus.atlas.domain.model.Partner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<MapUiState>(MapUiState.Loading)
    val uiState: StateFlow<MapUiState> = _uiState

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = MapUiState.Loading
            try {
                // Simulação de carregamento (seria via Repository)
                // No app real, aqui chamamos o UseCase que consome o AtlasApi
                _uiState.value = MapUiState.Success(emptyList())
            } catch (e: Exception) {
                _uiState.value = MapUiState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }
}

sealed class MapUiState {
    object Loading : MapUiState()
    data class Success(val partners: List<Partner>) : MapUiState()
    data class Error(val message: String) : MapUiState()
}
