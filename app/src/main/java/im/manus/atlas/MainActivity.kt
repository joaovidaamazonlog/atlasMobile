package im.manus.atlas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import im.manus.atlas.presentation.ui.MapScreen
import im.manus.atlas.presentation.viewmodel.MapViewModel
import im.manus.atlas.ui.theme.AtlasTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            // Aplica o tema padrão do projeto
            AtlasTheme {
                // Surface é o container principal que usa a cor de fundo do tema
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Instancia o ViewModel (Hilt cuidará da injeção se configurado)
                    val mapViewModel: MapViewModel = viewModel()
                    
                    // Chama a tela de mapa que criamos anteriormente
                    MapScreen(viewModel = mapViewModel)
                }
            }
        }
    }
}