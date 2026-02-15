package im.manus.atlas.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import im.manus.atlas.domain.model.Partner
import im.manus.atlas.domain.usecase.GetPartnersUseCase
import im.manus.atlas.domain.usecase.GetDeliveryStationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getPartnersUseCase: GetPartnersUseCase,
    private val getDeliveryStationsUseCase: GetDeliveryStationsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MapUiState>(MapUiState.Loading)
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    private var allPartners: List<Partner> = emptyList()
    
    private val _filters = MutableStateFlow(MapFilters())
    val filters = _filters.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = MapUiState.Loading
            try {
                val result = getPartnersUseCase()
                result.fold(
                    onSuccess = { partners ->
                        allPartners = partners
                        applyFilters()
                    },
                    onFailure = { exception ->
                        Timber.e(exception, "Error loading partners")
                        _uiState.value = MapUiState.Error(
                            exception.message ?: "Erro desconhecido ao carregar dados"
                        )
                    }
                )
            } catch (e: Exception) {
                Timber.e(e, "Unexpected error in loadData")
                _uiState.value = MapUiState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }

    fun updateStatusFilter(status: String?) {
        _filters.update { it.copy(selectedStatus = status) }
        applyFilters()
    }

    fun updateClusterFilter(cluster: String?) {
        _filters.update { it.copy(selectedCluster = cluster) }
        applyFilters()
    }

    fun updateStationFilter(station: String?) {
        _filters.update { it.copy(selectedStation = station) }
        applyFilters()
    }

    private fun applyFilters() {
        val currentFilters = _filters.value
        val filtered = allPartners.filter { partner ->
            val statusMatch = currentFilters.selectedStatus == null || partner.status == currentFilters.selectedStatus
            val clusterMatch = currentFilters.selectedCluster == null || partner.clusterName == currentFilters.selectedCluster
            val stationMatch = currentFilters.selectedStation == null || partner.deliveryStation == currentFilters.selectedStation
            statusMatch && clusterMatch && stationMatch
        }
        _uiState.value = MapUiState.Success(filtered)
    }

    fun refreshData() {
        loadData()
    }
}

data class MapFilters(
    val selectedStatus: String? = null,
    val selectedCluster: String? = null,
    val selectedStation: String? = null
)

sealed class MapUiState {
    object Loading : MapUiState()
    data class Success(val partners: List<Partner>) : MapUiState()
    data class Error(val message: String) : MapUiState()
}
