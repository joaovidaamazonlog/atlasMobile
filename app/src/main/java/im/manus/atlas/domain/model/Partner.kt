package im.manus.atlas.domain.model

data class Partner(
    val storeId: String?,
    val name: String,
    val status: String,
    val deliveryStation: String,
    val lat: Double,
    val lon: Double,
    val radius: Int,
    val capacity: Int,
    val prospectingDifficulty: String = "Baixa",
    val clusterName: String = "N/A",
    val decision: String = "",
    val telefone: String = "",
    val salesforceId: String = "",
    val jurisdictionType: String = "",
    val city: String = ""
)
