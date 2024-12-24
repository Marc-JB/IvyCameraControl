package nl.marc_apps.ivycameracontrol.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyiot.ipcam_sdk.IvyCameraConnection
import com.ivyiot.ipcam_sdk.IvySdk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CameraDetailViewModel(private val ivySdk: IvySdk) : ViewModel() {
    var ivyCameraConnection: IvyCameraConnection? = null
        private set

    private val mutableIsLoggedIn = MutableStateFlow(false)
    val isLoggedIn = mutableIsLoggedIn.asStateFlow()

    fun login(uid: String, username: String, password: String) {
        viewModelScope.launch {
            ivyCameraConnection = ivySdk.login(uid, username, password)
            mutableIsLoggedIn.update { true }
        }
    }

    fun logout() {
        mutableIsLoggedIn.update { false }
        ivyCameraConnection?.close()
    }
}
