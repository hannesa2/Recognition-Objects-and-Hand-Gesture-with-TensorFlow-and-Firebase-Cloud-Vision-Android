package info.hannes.tflitecamera.recognitionObjects

import android.app.Activity
import android.os.Bundle
import com.example.android.tflitecamerademo.R
import info.hannes.tflitecamera.recognitionObjects.CameraBasicFragment

class CameraBasicActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        if (null == savedInstanceState) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, CameraBasicFragment.newInstance())
                    .commit()
        }
    }
}