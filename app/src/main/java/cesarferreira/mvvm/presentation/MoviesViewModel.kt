package cesarferreira.mvvm.presentation

import cesarferreira.mvvm.domain.DownloadState
import cesarferreira.mvvm.domain.DownloadUseCase
import cesarferreira.mvvm.domain.PlayState
import cesarferreira.mvvm.domain.PlayUseCase
import cesarferreira.mvvm.framework.archcompoments.BaseViewModel
import cesarferreira.mvvm.framework.archcompoments.HybridMutableLiveData
import cesarferreira.mvvm.framework.schedulers.SchedulersProvider
import javax.inject.Inject

class MoviesViewModel
@Inject constructor(
        private val playUseCase: PlayUseCase,
        private val downloadUseCase: DownloadUseCase,
        private val scheduler: SchedulersProvider
) : BaseViewModel() {

    internal val playState = HybridMutableLiveData<PlayState>()
    internal val downloadState = HybridMutableLiveData<DownloadState>()

    internal val selectEventState = HybridMutableLiveData<String>()

    private fun onPlayClicked(uUid: String) {

        playState.postValue(PlayState.Loading(), true)

        val params = PlayUseCase.Params(uUid)

        val disposable = playUseCase.buildUseCase(params)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.mainThread())
                .subscribe({
                    when (it) {
                        is PlayState.Success -> playState.postValue(PlayState.Success(it.uUid))
                        is PlayState.Error -> playState.postValue(PlayState.Error(it.errorMessage))
                    }
                }, {
                    playState.postValue(PlayState.Error("Unknown error"))
                })

        compositeDisposable.addAll(disposable)
    }

    private fun onDownloadClicked(uUid: String) {

        downloadState.postValue(DownloadState.Loading(), true)

        val params = DownloadUseCase.Params(uUid)

        val disposable = downloadUseCase.buildUseCase(params)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.mainThread())
                .subscribe({
                    when (it) {
                        is DownloadState.Success -> downloadState.postValue(DownloadState.Success(it.uUid))
                        is DownloadState.Error -> downloadState.postValue(DownloadState.Error(it.errorMessage))
                    }
                }, {
                    downloadState.postValue(DownloadState.Error("Unknown error"))
                })

        compositeDisposable.addAll(disposable)
    }

    fun handleItemClick(uiAction: ActionType, uUid: String) {

        when (uiAction) {
            ActionType.PLAY -> onPlayClicked(uUid)
            ActionType.DOWNLOAD -> onDownloadClicked(uUid)
            ActionType.SELECT -> selectEventState.postValue(uUid)
            else -> {
            }
        }
    }
}

enum class ActionType {
    PLAY,
    DOWNLOAD,
    SELECT,
    RECORD,
    NONE
}

