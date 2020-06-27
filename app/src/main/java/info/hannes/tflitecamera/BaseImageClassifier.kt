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
package info.hannes.tflitecamera

import android.app.Activity
import android.graphics.Bitmap
import android.os.SystemClock
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.*

abstract class BaseImageClassifier internal constructor(activity: Activity) {

    private val intValues = IntArray(DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y)

    private var tfLite: Interpreter?
    private val labelList: List<String>
    private var imgData: ByteBuffer? = null
    private var labelProbArray: Array<FloatArray>? = null
    private var filterLabelProbArray: Array<FloatArray>? = null
    private val sortedLabels = PriorityQueue<Map.Entry<String, Float>>(
            RESULTS_TO_SHOW,
            Comparator<Map.Entry<String?, Float>> { o1, o2 -> o1.value.compareTo(o2.value) })

    private val modelPath: String
        get() = modelPathClassifier()

    private val labelPath: String
        get() = labelPathClassifier()

    abstract fun modelPathClassifier(): String
    abstract fun labelPathClassifier(): String

    init {
        tfLite = Interpreter(loadModelFile(activity))
        labelList = loadLabelList(activity)
        imgData = ByteBuffer.allocateDirect(4 * DIM_BATCH_SIZE * DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y * DIM_PIXEL_SIZE)
        imgData?.order(ByteOrder.nativeOrder())
        labelProbArray = Array(1) { FloatArray(labelList.size) }
        filterLabelProbArray = Array(FILTER_STAGES) { FloatArray(labelList.size) }
    }

    fun classifyFrame(bitmap: Bitmap): String {
        if (tfLite == null) {
            Log.e(TAG, "Image classifier has not been initialized;")
            return "Uninitialized classifier."
        }
        convertBitmapToByteBuffer(bitmap)
        val startTime = SystemClock.uptimeMillis()
        tfLite!!.run(imgData, labelProbArray)
        val endTime = SystemClock.uptimeMillis()
        Log.d(TAG, "Timecost to run model inference: " + (endTime - startTime).toString())

        applyFilter()

        var textToShow = printTopKLabels()
        textToShow = (endTime - startTime).toString() + "ms" + textToShow
        return textToShow
    }

    private fun applyFilter() {
        val numLabels = labelList.size

        for (j in 0 until numLabels) {
            filterLabelProbArray!![0][j] += FILTER_FACTOR * (labelProbArray!![0][j] -
                    filterLabelProbArray!![0][j])
        }
        for (i in 1 until FILTER_STAGES) {
            for (j in 0 until numLabels) {
                filterLabelProbArray!![i][j] += FILTER_FACTOR * (filterLabelProbArray!![i - 1][j] -
                        filterLabelProbArray!![i][j])
            }
        }

        for (j in 0 until numLabels) {
            labelProbArray!![0][j] = filterLabelProbArray!![FILTER_STAGES - 1][j]
        }
    }

    fun close() {
        tfLite?.close()
        tfLite = null
    }

    @Throws(IOException::class)
    private fun loadLabelList(activity: Activity): List<String> {
        return activity.assets.open(labelPath).bufferedReader().readLines()
    }

    @Throws(IOException::class)
    private fun loadModelFile(activity: Activity): MappedByteBuffer {
        val fileDescriptor = activity.assets.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap) {
        if (imgData == null) {
            return
        }
        imgData?.rewind()
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        var pixel = 0
        val startTime = SystemClock.uptimeMillis()
        for (i in 0 until DIM_IMG_SIZE_X) {
            for (j in 0 until DIM_IMG_SIZE_Y) {
                val `val` = intValues[pixel++]
                imgData?.putFloat(((`val` shr 16 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                imgData?.putFloat(((`val` shr 8 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                imgData?.putFloat(((`val` and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
            }
        }
        val endTime = SystemClock.uptimeMillis()
        Log.d(TAG, "Timecost to put values in ByteBuffer: " + (endTime - startTime).toString())
    }

    private fun printTopKLabels(): String {
        for (i in labelList.indices) {
            sortedLabels.add(AbstractMap.SimpleEntry(labelList[i], labelProbArray!![0][i]))
            if (sortedLabels.size > RESULTS_TO_SHOW) {
                sortedLabels.poll()
            }
        }
        var textToShow = ""
        val size = sortedLabels.size
        for (i in 0 until size) {
            val label = sortedLabels.poll()
            textToShow = toText(label, textToShow)
            when (label.key) {
                "0" -> textToShow = String.format("\nNumber (%s) = %4.2f", label.key, label.value) + textToShow
                "1" -> textToShow = String.format("\nNumber (%s) = %4.2f", label.key, label.value) + textToShow
                "2" -> textToShow = String.format("\nNumber (%s) = %4.2f", label.key, label.value) + textToShow
                "3" -> textToShow = String.format("\nNumber (%s) = %4.2f", label.key, label.value) + textToShow
                "4" -> textToShow = String.format("\nNumber (%s) = %4.2f", label.key, label.value) + textToShow
                "5" -> textToShow = String.format("\nNumber (%s) = %4.2f", label.key, label.value) + textToShow
                "a" -> textToShow = String.format("\nVocal (%s) = %4.2f", label.key, label.value) + textToShow
                "e" -> textToShow = String.format("\nVocal (%s) = %4.2f", label.key, label.value) + textToShow
                "i" -> textToShow = String.format("\nVocal (%s) = %4.2f", label.key, label.value) + textToShow
                "o" -> textToShow = String.format("\nVocal (%s) = %4.2f", label.key, label.value) + textToShow
                "u" -> textToShow = String.format("\nVocal (%s) = %4.2f", label.key, label.value) + textToShow
            }
        }
        return textToShow
    }

    abstract fun toText(label: Map.Entry<String, Float>, text: String): String

    companion object {
        const val DIM_IMG_SIZE_X = 224
        const val DIM_IMG_SIZE_Y = 224
        private const val TAG = "HandgestureRecognition"
        private const val RESULTS_TO_SHOW = 3
        private const val DIM_BATCH_SIZE = 1
        private const val DIM_PIXEL_SIZE = 3
        private const val IMAGE_MEAN = 128
        private const val IMAGE_STD = 128.0f
        private const val FILTER_STAGES = 3
        private const val FILTER_FACTOR = 0.4f
    }

}