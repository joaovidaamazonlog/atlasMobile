package im.manus.atlas.presentation.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import im.manus.atlas.domain.model.Partner
import im.manus.atlas.presentation.viewmodel.MapUiState
import im.manus.atlas.presentation.viewmodel.MapViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapScreen(viewModel: MapViewModel) {
    val uiState = viewModel.uiState.collectAsState().value
    val filters = viewModel.filters.collectAsState().value
    val context = LocalContext.current
    var selectedPartner by remember { mutableStateOf<Partner?>(null) }

    remember {
        Configuration.getInstance().load(
            context,
            context.getSharedPreferences("osm_prefs", Context.MODE_PRIVATE)
        )
    }

    if (selectedPartner != null) {
        PartnerDetailScreen(partner = selectedPartner!!, onBack = { selectedPartner = null })
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (uiState) {
                is MapUiState.Loading -> {
                    LoadingView()
                }
                is MapUiState.Success -> {
                    Column {
                        FilterBar(
                            selectedStatus = filters.selectedStatus,
                            onStatusSelected = { viewModel.updateStatusFilter(it) },
                            selectedStation = filters.selectedStation,
                            onStationSelected = { viewModel.updateStationFilter(it) }
                        )
                        MapContent(
                            context = context,
                            partners = uiState.partners,
                            onPartnerClick = { selectedPartner = it },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                is MapUiState.Error -> {
                    ErrorView(message = uiState.message)
                }
            }
        }
    }
}

@Composable
fun FilterBar(
    selectedStatus: String?,
    onStatusSelected: (String?) -> Unit,
    selectedStation: String?,
    onStationSelected: (String?) -> Unit
) {
    val statuses = listOf("Active", "Inactive", "Onboarding", "Prospect")
    
    Column(modifier = Modifier.padding(8.dp)) {
        Text("Filtros", style = MaterialTheme.typography.titleSmall)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                FilterChip(
                    selected = selectedStatus == null,
                    onClick = { onStatusSelected(null) },
                    label = { Text("Todos") }
                )
            }
            items(statuses) { status ->
                FilterChip(
                    selected = selectedStatus == status,
                    onClick = { onStatusSelected(status) },
                    label = { Text(status) }
                )
            }
        }
    }
}

@Composable
private fun MapContent(
    context: Context,
    partners: List<Partner>,
    onPartnerClick: (Partner) -> Unit,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            MapView(ctx).apply {
                setMultiTouchControls(true)
                controller.setZoom(12.0)
                val startPoint = GeoPoint(-23.5505, -46.6333)
                controller.setCenter(startPoint)
                setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)
            }
        },
        update = { mapView ->
            mapView.overlays.clear()
            partners.forEach { partner ->
                val marker = Marker(mapView)
                marker.position = GeoPoint(partner.lat, partner.lon)
                marker.title = partner.name
                marker.subDescription = "Status: ${partner.status}"
                marker.setOnMarkerClickListener { _, _ ->
                    onPartnerClick(partner)
                    true
                }
                mapView.overlays.add(marker)
            }
            mapView.invalidate()
        }
    )
}

@Composable
fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorView(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
    }
}
