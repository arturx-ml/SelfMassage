package ai.mlxdroid.selfmassage.di

import ai.mlxdroid.selfmassage.data.MassageRepository
import ai.mlxdroid.selfmassage.data.MassageRepositoryInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideMassageRepository(): MassageRepositoryInterface = MassageRepository
}
