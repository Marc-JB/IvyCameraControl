package nl.marc_apps.ivycameracontrol.di

import com.ivyiot.ipcam_sdk.IvySdkImpl
import com.ivyiot.ipcam_sdk.IvySdk
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
actual class IvyModule {
    @Single
    fun provideIvySdk(): IvySdk = IvySdkImpl()
}
