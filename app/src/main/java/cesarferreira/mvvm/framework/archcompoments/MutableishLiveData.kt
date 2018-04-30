package cesarferreira.mvvm.framework.archcompoments

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.support.annotation.MainThread
import android.util.Log
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A lifecycle-aware observable that sends only new updates after subscription, used for events like
 * navigation and Snackbar messages.
 *
 *
 * This avoids a common problem with events: on configuration change (like rotation) an update
 * can be emitted if the observer is active. This LiveData only calls the observable if there's an
 * explicit call to setValue() or call().
 *
 *
 * Note that only one observer is going to be notified of changes.
 */
class MutableishLiveData<T> : MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)
    private val sticky = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<T>) {

        // Observe the internal MutableLiveData
        super.observe(owner, Observer { t ->
            if (sticky.get() || pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    override fun setValue(t: T?) {
        pending.set(true)
        super.setValue(t)
    }

    override fun postValue(value: T) {
        postValue(value, false)
    }

    fun postValue(t: T?, sticky: Boolean = false) {
        this.sticky.set(sticky)
        super.postValue(t)
    }
}
