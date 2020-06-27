/* Copyright 2017 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/
package info.hannes.tflitecamera.recognitionObjectsTensorFlow

import com.example.android.tflitecamerademo.R
import info.hannes.tflitecamera.BaseCameraGestureFragment

class CameraGestureFragment : BaseCameraGestureFragment() {

    override fun fragmentAutoFitTextureView() = R.id.textureGesture
    override fun fragmentTextView() = R.id.textGestureView

    override fun classifierOfFragment() = ImageGestureClassifier(activity)
    override val maxPreviewWidth = 4608
    override val maxPreviewHeigth = 2592

    override fun text2Show(text: String): String {
        var textResult = text
        textResult = if (textResult[14].toString().equals("(", ignoreCase = true)) {
            text[15].toString()
        } else if (text[14].toString().equals(")", ignoreCase = true)) {
            text[13].toString()
        } else {
            text[14].toString()
        }
        return textResult
    }

    override val layout = R.layout.fragment_camera_gesture


    companion object {
        fun newInstance(): CameraGestureFragment {
            return CameraGestureFragment()
        }
    }

}
