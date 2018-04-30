package cesarferreira.mvvm.presentation

import android.app.ProgressDialog
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import cesarferreira.mvvm.MyApplication
import cesarferreira.mvvm.R
import cesarferreira.mvvm.domain.DownloadState
import cesarferreira.mvvm.domain.PlayState
import cesarferreira.mvvm.framework.extensions.observe
import cesarferreira.mvvm.framework.extensions.showToast
import cesarferreira.mvvm.framework.extensions.viewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var navigator: Navigator
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MoviesViewModel

    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as MyApplication).appComponent.inject(this)

        initializeViews()

        viewModel = viewModel(viewModelFactory) {
            observe(playState, ::onPlayStateChanged)
            observe(downloadState, ::onDownloadStateChanged)
            observe(selectEventState, ::onSelectStateChanged)
        }
    }

    private fun onSelectStateChanged(uUid: String?) {
        navigator.goToDetails(this, uUid)
    }

    private fun initializeViews() {
        playButton.setOnClickListener { viewModel.handleItemClick(ActionType.PLAY, "fakeid") }
        downloadButton.setOnClickListener { viewModel.handleItemClick(ActionType.DOWNLOAD, "fakeid") }
        selectButton.setOnClickListener { viewModel.handleItemClick(ActionType.SELECT, "fakeid") }
    }

    private fun onPlayStateChanged(playResponse: PlayState?) {
        playResponse?.let {
            when (it) {
                is PlayState.Loading -> {
                    log("PlayState.Loading")
                    progressDialog = ProgressDialog.show(this, "Loading", "Preparing to play", true)
                }
                is PlayState.Success -> {
                    log("PlayState.Success")
                    progressDialog?.hide()
                    navigator.goToDetails(this, it.uUid)
                }
                is PlayState.Error -> {
                    log("PlayState.Error")
                    progressDialog?.hide()
                    showError(it.errorMessage)
                }
            }
        }
    }

    private fun onDownloadStateChanged(downloadState: DownloadState?) {
        downloadState?.let {
            when (it) {
                is DownloadState.Loading -> {
                    log("DownloadState.Loading")
                    progressDialog = ProgressDialog.show(this, "Loading", "Downloading...", true)
                }
                is DownloadState.Success -> {
                    log("DownloadState.Success")
                    progressDialog?.hide()
                    Toast.makeText(applicationContext, "DOWNLOAD SUCCESSFUL", Toast.LENGTH_LONG).show()
                }
                is DownloadState.Error -> {
                    log("DownloadState.Error")
                    progressDialog?.hide()
                    showError(it.errorMessage)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        progressDialog?.dismiss()
    }

    private fun showError(errorMessage: String) {
        Log.e("tag", "showError:  $errorMessage")
        showToast(errorMessage)
    }

    private fun log(message: String) {
        Log.i("log", message)
    }
}
