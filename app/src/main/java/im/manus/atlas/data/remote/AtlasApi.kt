package im.manus.atlas.data.remote

import retrofit2.http.GET

interface AtlasApi {
    @GET("data/dados_mapa.json")
    suspend fun getPartnersData(): PartnerDataResponse

    @GET("data/optimization_data.geojson")
    suspend fun getOptimizationGeoJson(): String // GeoJSON como String para parsing manual ou via Maps Utils
}

data class PartnerDataResponse(
    val allMarkerData: List<PartnerDto>,
    val deliveryStations: List<DeliveryStationDto>
)

data class PartnerDto(
    val store_id: String?,
    val partner_name: String,
    val status: String,
    val delivery_station: String,
    val lat: Double,
    val lon: Double,
    val radius: Int,
    val capacity: Int,
    val prospecting_difficulty: String?,
    val cluster: String?,
    val decision: String?,
    val telefone: String?,
    val salesforce_id: String?,
    val jurisdiction_type: String?,
    val city: String?
)

data class DeliveryStationDto(
    val nome: String,
    val lat: Double,
    val lon: Double
)
