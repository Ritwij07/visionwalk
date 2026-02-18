package com.example.visionwalk.ml

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.speech.tts.TextToSpeech
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions

class ObjectAnalyzer(
    private val context: Context,
    private val tts: TextToSpeech
) : ImageAnalysis.Analyzer {

    private val detector = ObjectDetection.getClient(
        ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
            .enableMultipleObjects()
            .build()
    )

    private var lastAlertTime = 0L

    override fun analyze(imageProxy: ImageProxy) {
        val image = imageProxy.image ?: return
        val input = InputImage.fromMediaImage(
            image,
            imageProxy.imageInfo.rotationDegrees
        )

        detector.process(input)
            .addOnSuccessListener { objects ->
                for (obj in objects) {
                    val area = obj.boundingBox.width() * obj.boundingBox.height()
                    if (area > 15000 &&
                        System.currentTimeMillis() - lastAlertTime > 3000
                    ) {
                        alert()
                        lastAlertTime = System.currentTimeMillis()
                        break
                    }
                }
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }

    private fun alert() {
        // Speak alert
        tts.speak(
            "Obstacle ahead",
            TextToSpeech.QUEUE_FLUSH,
            null,
            "obstacle_alert"
        )

        // Vibrate safely
        val vibrator =
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        if (!vibrator.hasVibrator()) return

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    300,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(300)
        }
    }

}
