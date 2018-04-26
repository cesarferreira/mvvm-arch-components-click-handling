package cesarferreira.mvvm.di

import cesarferreira.mvvm.MyApplication
import cesarferreira.mvvm.presentation.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ApplicationModule::class,
    ViewModelModule::class
])
interface ApplicationComponent {
    fun inject(target: MainActivity)
    fun inject(target: MyApplication)
}