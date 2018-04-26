package cesarferreira.mvvm.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import cesarferreira.mvvm.framework.extensions.ViewModelFactory
import cesarferreira.mvvm.framework.extensions.ViewModelKey
import cesarferreira.mvvm.presentation.MoviesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MoviesViewModel::class)
    internal abstract fun moviesViewModel(viewModel: MoviesViewModel): ViewModel

    //Add ViewModels here

}
