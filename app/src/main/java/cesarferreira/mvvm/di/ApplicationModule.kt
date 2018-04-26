package cesarferreira.mvvm.di

import android.content.Context
import cesarferreira.mvvm.MyApplication
import cesarferreira.mvvm.framework.schedulers.SchedulersProvider
import cesarferreira.mvvm.framework.schedulers.SchedulersProviderImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: MyApplication) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application

    @Singleton
    @Provides
    fun providesSchedulers(): SchedulersProvider = SchedulersProviderImpl()
}