package cesarferreira.mvvm

import android.app.Application
import cesarferreira.mvvm.di.ApplicationComponent
import cesarferreira.mvvm.di.ApplicationModule
import cesarferreira.mvvm.di.DaggerApplicationComponent

class MyApplication : Application() {

    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        this.injectMembers()
    }

    private fun injectMembers() = appComponent.inject(this)
}