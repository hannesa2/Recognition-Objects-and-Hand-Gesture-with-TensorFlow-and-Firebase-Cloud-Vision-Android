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

import android.app.Activity
import info.hannes.tflitecamera.BaseImageClassifier

class ImageGestureClassifier(activity: Activity) : BaseImageClassifier(activity) {

    override fun modelPathClassifier() = "hand_graph.lite"
    override fun labelPathClassifier() = "graph_label_strings.txt"

    override fun toText(label: Map.Entry<String, Float>, text: String): String {
        return when (label.key) {
            "0" -> String.format("\nNumber (%s) = %4.2f", label.key, label.value) + text
            "1" -> String.format("\nNumber (%s) = %4.2f", label.key, label.value) + text
            "2" -> String.format("\nNumber (%s) = %4.2f", label.key, label.value) + text
            "3" -> String.format("\nNumber (%s) = %4.2f", label.key, label.value) + text
            "4" -> String.format("\nNumber (%s) = %4.2f", label.key, label.value) + text
            "5" -> String.format("\nNumber (%s) = %4.2f", label.key, label.value) + text
            "a" -> String.format("\nVocal (%s) = %4.2f", label.key, label.value) + text
            "e" -> String.format("\nVocal (%s) = %4.2f", label.key, label.value) + text
            "i" -> String.format("\nVocal (%s) = %4.2f", label.key, label.value) + text
            "o" -> String.format("\nVocal (%s) = %4.2f", label.key, label.value) + text
            "u" -> String.format("\nVocal (%s) = %4.2f", label.key, label.value) + text
            else -> ""
        }
    }
}
