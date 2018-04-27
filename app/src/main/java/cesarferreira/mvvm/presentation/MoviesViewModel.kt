package cesarferreira.mvvm.presentation

import cesarferreira.mvvm.domain.PlayState
import cesarferreira.mvvm.domain.PlayUseCase
import cesarferreira.mvvm.framework.archcompoments.BaseViewModel
import cesarferreira.mvvm.framework.archcompoments.MutableLiveEvent
import cesarferreira.mvvm.framework.schedulers.SchedulersProvider
import javax.inject.Inject

class MoviesViewModel
@Inject constructor(
        private val playUseCase: PlayUseCase,
        private val scheduler: SchedulersProvider
) : BaseViewModel() {

    internal val playState = MutableLiveEvent<PlayState>()

    fun onPlayClicked(uUid: String) {

        playState.postValue(PlayState.Loading(), true)

        val params = PlayUseCase.Params(uUid)

        val disposable = playUseCase.buildUseCase(params)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.mainThread())
                .subscribe(
                        {
                            when (it) {
                                is PlayState.Success -> playState.postValue(PlayState.Success(it.uUid))
                                is PlayState.Error -> playState.postValue(PlayState.Error(it.errorMessage))
                            }
                        },
                        { playState.postValue(PlayState.Error("Unknown error")) }
                )

        compositeDisposable.addAll(disposable)

    }
}