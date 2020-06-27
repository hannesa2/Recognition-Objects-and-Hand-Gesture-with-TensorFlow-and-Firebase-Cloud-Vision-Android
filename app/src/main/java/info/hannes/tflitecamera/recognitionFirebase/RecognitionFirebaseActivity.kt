package info.hannes.tflitecamera.recognitionFirebase

import android.app.AlertDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.android.tflitecamerademo.R
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabel
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionLabel
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetectorOptions
import com.wonderkiln.camerakit.*
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_recognition_firebase.*

class RecognitionFirebaseActivity : AppCompatActivity() {

    lateinit var waitingDialog: AlertDialog
    override fun onResume() {
        super.onResume()
        camera_view.start()
    }

    override fun onPause() {
        super.onPause()
        camera_view.stop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recognition_firebase)
        waitingDialog = SpotsDialog.Builder().setContext(this).setMessage("Please wait ..").setCancelable(false).build()
        val detector = FirebaseVision.getInstance().visionLabelDetector
        camera_view.addCameraKitListener(object : CameraKitEventListener {
            override fun onEvent(cameraKitEvent: CameraKitEvent) {}
            override fun onError(cameraKitError: CameraKitError) {}
            override fun onImage(cameraKitImage: CameraKitImage) {
                waitingDialog.show()
                var bitmap = cameraKitImage.bitmap
                bitmap = Bitmap.createScaledBitmap(bitmap, camera_view.width, camera_view.height, false)
                camera_view.stop()
                runDetector(bitmap)
            }

            override fun onVideo(cameraKitVideo: CameraKitVideo) {}
        })
        btn_detect.setOnClickListener {
            camera_view.start()
            camera_view.captureImage()
        }
    }

    private fun runDetector(bitmap: Bitmap) {
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        InternetCheck(InternetCheck.Consumer { internet: Boolean ->
            if (internet) {
                val options = FirebaseVisionCloudDetectorOptions.Builder().setMaxResults(1).build()
                val detector = FirebaseVision.getInstance().getVisionCloudLabelDetector(options)
                detector.detectInImage(image).addOnSuccessListener { firebaseVisionCloudLabels -> processDataResultCloud(firebaseVisionCloudLabels) }.addOnFailureListener { e: Exception -> Log.d("error", "onFailure: " + e.message) }
            } else {
                val options = FirebaseVisionLabelDetectorOptions.Builder().setConfidenceThreshold(0.8f).build()
                val detector = FirebaseVision.getInstance().getVisionLabelDetector(options)
                detector.detectInImage(image).addOnSuccessListener { firebaseVisionLabels -> processDataResult(firebaseVisionLabels) }.addOnFailureListener { e: Exception -> Log.d("error", "onFailure: " + e.message) }
            }
        })
    }

    private fun processDataResultCloud(firebaseVisionCloudLabels: List<FirebaseVisionCloudLabel>) {
        for (label in firebaseVisionCloudLabels) {
            Toast.makeText(this, "Cloud result: " + label.label, Toast.LENGTH_SHORT).show()
        }
        if (waitingDialog.isShowing)
            waitingDialog.dismiss()
    }

    private fun processDataResult(firebaseVisionLabels: List<FirebaseVisionLabel>) {
        for (label in firebaseVisionLabels) {
            Toast.makeText(this, "Result: " + label.label, Toast.LENGTH_SHORT).show()
        }
        if (waitingDialog.isShowing)
            waitingDialog.dismiss()
    }
}