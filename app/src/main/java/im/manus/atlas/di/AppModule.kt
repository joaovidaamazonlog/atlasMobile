package im.manus.atlas.di

import im.manus.atlas.data.remote.AtlasApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule{
    @Provides
    @Singleton
    fun provideAtlasApi(): AtlasApi{
        return Retrofit.Builder()
            .baseUrl("https://joaovidaamazonlog.github.io/atlas/data/") //Ajustar depois para a URL do github pages
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(AtlasApi::class.java)
    }
}