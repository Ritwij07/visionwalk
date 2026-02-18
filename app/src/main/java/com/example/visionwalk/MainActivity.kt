package com.example.visionwalk

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.visionwalk.ui.VisionWalkScreen
import com.example.visionwalk.utils.PermissionUtils
import com.example.visionwalk.voice.VoiceManager
import com.example.visionwalk.sos.SOSManager

class MainActivity : ComponentActivity() {

    private lateinit var tts: TextToSpeech
    private lateinit var voiceManager: VoiceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PermissionUtils.requestAll(this)

        tts = TextToSpeech(this) {
            tts.speak(
                "Vision Walk activated. Say a command.",
                TextToSpeech.QUEUE_FLUSH,
                null,
                null
            )
        }

        voiceManager = VoiceManager(this) { command ->
            handleCommand(command)
        }

        setContent {
            VisionWalkScreen(
                startListening = { voiceManager.startListening() },
                tts = tts
            )
        }
    }

    private fun handleCommand(command: String) {
        when {
            command.contains("help", true) ->
                SOSManager.sendSOS(this, tts)

            command.contains("navigate", true) ->
                tts.speak("Navigation started", TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }
}
