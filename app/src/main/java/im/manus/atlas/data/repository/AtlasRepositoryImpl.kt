package im.manus.atlas.data.repository

import android.content.Context
import com.squareup.moshi.Moshi
import im.manus.atlas.data.local.dao.PartnerDao
import im.manus.atlas.data.local.dao.DeliveryStationDao
import im.manus.atlas.data.mapper.toEntity
import im.manus.atlas.data.mapper.toDomain
import im.manus.atlas.data.remote.PartnerDataResponse
import im.manus.atlas.domain.model.Partner
import im.manus.atlas.domain.model.DeliveryStation
import im.manus.atlas.domain.repository.AtlasRepository
import im.manus.atlas.domain.repository.GeoJsonFeature
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import timber.log.Timber
import javax.inject.Inject

class AtlasRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val moshi: Moshi,
    private val partnerDao: PartnerDao,
    private val deliveryStationDao: DeliveryStationDao
) : AtlasRepository {

    override suspend fun getPartners(): Result<List<Partner>> = try {
        val jsonString = context.assets.open("data/dados_mapa.json").bufferedReader().use { it.readText() }
        val adapter = moshi.adapter(PartnerDataResponse::class.java)
        val response = adapter.fromJson(jsonString)
        
        if (response != null) {
            val partners = response.allMarkerData.map { it.toDomain() }
            partnerDao.deleteAllPartners()
            partnerDao.insertPartners(response.allMarkerData.map { it.toEntity() })
            Result.success(partners)
        } else {
            Result.failure(Exception("Failed to parse JSON"))
        }
    } catch (e: Exception) {
        Timber.e(e, "Error loading partners from assets")
        val cachedPartners = partnerDao.getAllPartners().map { it.toDomain() }
        if (cachedPartners.isNotEmpty()) {
            Result.success(cachedPartners)
        } else {
            Result.failure(e)
        }
    }

    override suspend fun getDeliveryStations(): Result<List<DeliveryStation>> = try {
        val jsonString = context.assets.open("data/dados_mapa.json").bufferedReader().use { it.readText() }
        val adapter = moshi.adapter(PartnerDataResponse::class.java)
        val response = adapter.fromJson(jsonString)
        
        if (response != null) {
            val stations = response.deliveryStations.map { it.toDomain() }
            deliveryStationDao.deleteAllStations()
            deliveryStationDao.insertStations(response.deliveryStations.map { it.toEntity() })
            Result.success(stations)
        } else {
            Result.failure(Exception("Failed to parse JSON"))
        }
    } catch (e: Exception) {
        Timber.e(e, "Error loading stations from assets")
        val cachedStations = deliveryStationDao.getAllStations().map { it.toDomain() }
        if (cachedStations.isNotEmpty()) {
            Result.success(cachedStations)
        } else {
            Result.failure(e)
        }
    }

    override suspend fun getGeoJsonFeatures(): Flow<List<GeoJsonFeature>> {
        // Implementation for GeoJson if needed
        return emptyFlow()
    }

    override suspend fun filterPartnersByStatus(status: String): Result<List<Partner>> = try {
        val partners = partnerDao.getPartnersByStatus(status).map { it.toDomain() }
        Result.success(partners)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
