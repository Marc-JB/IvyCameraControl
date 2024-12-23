package nl.marc_apps.ivycameracontrol

import kotlinx.cinterop.ptr
import nl.marc_apps.ivycameracontrol.di.setupModules
import nl.marc_apps.ivycameracontrol.utils.createErrorPointer
import nl.marc_apps.ivycameracontrol.utils.throwOnError
import org.koin.core.context.startKoin
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryOptionAllowBluetooth
import platform.AVFAudio.AVAudioSessionCategoryOptionDefaultToSpeaker
import platform.AVFAudio.AVAudioSessionCategoryPlayAndRecord
import platform.AVFAudio.setActive

@Suppress("unused")
fun onAppStartup() {
    startKoin {
        setupModules()
    }

    try {
        val audioSession = AVAudioSession.sharedInstance()

        createErrorPointer { errorPointer ->
            audioSession.setCategory(AVAudioSessionCategoryPlayAndRecord,
                withOptions = AVAudioSessionCategoryOptionAllowBluetooth or AVAudioSessionCategoryOptionDefaultToSpeaker,
                error = errorPointer.ptr)

            errorPointer.throwOnError()

            audioSession.setActive(true, errorPointer.ptr)

            errorPointer.throwOnError()
        }
    } catch (error: RuntimeException) {
        // ignored
    }
}
