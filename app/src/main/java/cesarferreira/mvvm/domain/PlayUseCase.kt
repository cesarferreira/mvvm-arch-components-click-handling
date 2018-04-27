package cesarferreira.mvvm.domain

import cesarferreira.mvvm.framework.schedulers.SchedulersProvider
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayUseCase @Inject
constructor(private val schedulersProvider: SchedulersProvider)
    : UseCase<Observable<PlayState>, PlayUseCase.Params>() {

    override fun buildUseCase(params: Params): Observable<PlayState> {

        return Observable.defer {
            // todo check if im logged in
            // todo and every other check
            return@defer Observable
                    //.just(PlayState.Error("The user is not logged in"))
                    .just(PlayState.Success("AFC81C"))
                    .delay(2, TimeUnit.SECONDS, schedulersProvider.computation())
        }
    }

    data class Params(val uUid: String)
}


sealed class PlayState {
    data class Success(val uUid: String) : PlayState()
    data class Error(val errorMessage: String) : PlayState()
    class Loading : PlayState()
}