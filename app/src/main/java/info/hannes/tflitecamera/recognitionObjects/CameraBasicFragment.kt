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
package info.hannes.tflitecamera.recognitionObjects

import com.example.android.tflitecamerademo.R
import info.hannes.tflitecamera.BaseCameraGestureFragment

class CameraBasicFragment : BaseCameraGestureFragment() {

    override val maxPreviewWidth = 1920
    override val maxPreviewHeigth = 1080

    override fun fragmentAutoFitTextureView() = R.id.textureBasic
    override fun fragmentTextView() = R.id.textBasicView

    override fun classifierOfFragment() = ImageBasicClassifier(activity)

    override fun text2Show(text: String): String {
        return text
    }

    override val layout = R.layout.fragment_camera_basic

    companion object {
        fun newInstance(): CameraBasicFragment {
            return CameraBasicFragment()
        }
    }
}
