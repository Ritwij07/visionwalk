  package com.example.visionwalk.ui

import android.speech.tts.TextToSpeech
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.visionwalk.ml.ObjectAnalyzer
import java.util.concurrent.Executors

@Composable
fun VisionWalkScreen(
    startListening: () -> Unit,
    tts: TextToSpeech
) {
    val context = LocalContext.current

    AndroidView(
        modifier = Modifier,
        factory = { ctx ->
            val previewView = PreviewView(ctx)

            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build()
                preview.setSurfaceProvider(previewView.surfaceProvider)

                val analysis = ImageAnalysis.Builder().build()
                analysis.setAnalyzer(
                    Executors.newSingleThreadExecutor(),
                    ObjectAnalyzer(ctx, tts)
                )

                cameraProvider.bindToLifecycle(
                    ctx as LifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    analysis
                )
            }, ContextCompat.getMainExecutor(ctx))

            previewView
        }
    )

    LaunchedEffect(Unit) {
        startListening()
    }
}
