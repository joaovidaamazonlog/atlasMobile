package im.manus.atlas.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import im.manus.atlas.domain.model.Partner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartnerDetailScreen(
    partner: Partner,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(partner.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            DetailItem("Status", partner.status)
            DetailItem("Cluster", partner.clusterName)
            DetailItem("Delivery Station", partner.deliveryStation)
            DetailItem("Capacidade", partner.capacity.toString())
            DetailItem("Raio", "${partner.radius}m")
            DetailItem("Dificuldade de Prospecção", partner.prospectingDifficulty)
            if (partner.telefone.isNotEmpty()) {
                DetailItem("Telefone", partner.telefone)
            }
            if (partner.city.isNotEmpty()) {
                DetailItem("Cidade", partner.city)
            }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
    }
}
