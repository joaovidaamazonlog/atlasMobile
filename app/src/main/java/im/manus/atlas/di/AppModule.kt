package im.manus.atlas.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import im.manus.atlas.data.local.AtlasDatabase
import im.manus.atlas.data.repository.AtlasRepositoryImpl
import im.manus.atlas.domain.repository.AtlasRepository
import dagger.Module
import dagger.Provides
import dagger.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideAtlasDatabase(
        @ApplicationContext context: Context
    ): AtlasDatabase {
        return Room.databaseBuilder(
            context,
            AtlasDatabase::class.java,
            "atlas_database"
        ).build()
    }

    @Provides
    @Singleton
    fun providePartnerDao(database: AtlasDatabase) = database.partnerDao()

    @Provides
    @Singleton
    fun provideDeliveryStationDao(database: AtlasDatabase) = database.deliveryStationDao()

    @Provides
    @Singleton
    fun provideAtlasRepository(impl: AtlasRepositoryImpl): AtlasRepository = impl
}
