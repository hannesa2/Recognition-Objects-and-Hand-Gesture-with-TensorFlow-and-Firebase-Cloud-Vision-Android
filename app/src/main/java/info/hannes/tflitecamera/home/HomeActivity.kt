package info.hannes.tflitecamera.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.android.tflitecamerademo.R
import info.hannes.tflitecamera.recognitionFirebase.RecognitionFirebaseActivity
import info.hannes.tflitecamera.CameraActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        cardHand.setOnClickListener {
            val intent = Intent(this@HomeActivity, CameraActivity::class.java)
            startActivity(intent)
        }
        cardObject.setOnClickListener {
            val intent = Intent(this@HomeActivity, CameraActivity::class.java)
            startActivity(intent)
        }
        cardObjectFirebase.setOnClickListener {
            val intent = Intent(this@HomeActivity, RecognitionFirebaseActivity::class.java)
            startActivity(intent)
        }
    }
}