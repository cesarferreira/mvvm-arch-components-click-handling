package cesarferreira.mvvm.domain

import cesarferreira.mvvm.framework.schedulers.SchedulersProvider
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadUseCase @Inject
constructor(private val schedulersProvider: SchedulersProvider)
    : UseCase<Observable<DownloadState>, DownloadUseCase.Params>() {

    override fun buildUseCase(params: Params): Observable<DownloadState> {

        return Observable.defer {
            // todo and every other check
            return@defer Observable
                    .just(DownloadState.Success("AFC81C"))
                    .delay(2, TimeUnit.SECONDS, schedulersProvider.computation())
        }
    }

    data class Params(val uUid: String)

}

sealed class DownloadState {
    data class Success(val uUid: String) : DownloadState()
    data class Error(val errorMessage: String) : DownloadState()
    class Loading : DownloadState()
}