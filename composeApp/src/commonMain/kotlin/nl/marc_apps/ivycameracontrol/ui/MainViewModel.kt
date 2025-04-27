package nl.marc_apps.ivycameracontrol.ui

import androidx.lifecycle.ViewModel
import com.ivyiot.ipcam_sdk.IvySdk

class MainViewModel(private val ivySdk: IvySdk) : ViewModel() {
    val version by lazy {
        ivySdk.version
    }

    val localDevices by lazy {
        ivySdk.localDevices
    }
}
