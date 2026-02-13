package im.manus.atlas.presentation.ui

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import im.manus.atlas.presentation.viewmodel.MapUiState
import im.manus.atlas.presentation.viewmodel.MapViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapScreen(viewModel: MapViewModel) {
    val uiState = viewModel.uiState.collectAsState().value
    val context = LocalContext.current
    
    // Configuração do User Agent exigida pelo OpenStreetMap
    remember {
        Configuration.getInstance().load(
            context, 
            context.getSharedPreferences("osm_prefs", Context.MODE_PRIVATE)
        )
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            MapView(ctx).apply {
                // Configurações básicas do mapa
                setMultiTouchControls(true)
                controller.setZoom(12.0)
                
                // Centraliza inicialmente (Exemplo: São Paulo)
                val startPoint = GeoPoint(-23.5505, -46.6333)
                controller.setCenter(startPoint)
                
                // Garante que o mapa se ajuste ao layout
                this.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)
            }
        },
        update = { mapView ->
            // Sempre que o estado mudar (novos parceiros), atualizamos o mapa
            if (uiState is MapUiState.Success) {
                mapView.overlays.clear()
                
                uiState.partners.forEach { partner ->
                    val marker = Marker(mapView)
                    marker.position = GeoPoint(partner.lat, partner.lon)
                    marker.title = partner.name
                    marker.subDescription = "Status: ${partner.status} | Capacidade: ${partner.capacity}"
                    
                    // Define o ícone ou âncora se necessário
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    
                    mapView.overlays.add(marker)
                }
                
                // Repinta o mapa para mostrar os novos marcadores
                mapView.invalidate()
            }
        }
    )
}