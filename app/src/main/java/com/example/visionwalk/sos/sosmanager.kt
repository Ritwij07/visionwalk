package com.example.visionwalk.sos

import android.content.Context
import android.speech.tts.TextToSpeech
import android.telephony.SmsManager

object SOSManager {

    fun sendSOS(context: Context, tts: TextToSpeech) {
        val sms = SmsManager.getDefault()
        sms.sendTextMessage(
            "9555184533",
            null,
            "EMERGENCY! I need help immediately.",
            null,
            null
        )

        tts.speak("Emergency alert sent", TextToSpeech.QUEUE_FLUSH, null, null)
    }
}
