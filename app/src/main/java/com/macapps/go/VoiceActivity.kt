package com.macapps.go

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.IOException

const val LOG_TAG = "AudioRecordTest"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200


class VoiceActivity : AppCompatActivity() {

    private var fileName: String = ""
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    // Requesting permission to RECORD_AUDIO
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
        if (!permissionToRecordAccepted) finish()
    }

    private fun onRecord(start: Boolean) = if (start) {
        startRecording()
    } else {
        stopRecording()
    }
    private fun onPlay(start: Boolean) = if (start) {
        startPlaying()
    } else {
        stopPlaying()
    }
    private fun startPlaying() {
        player = MediaPlayer().apply {
            try {
                setDataSource(fileName)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed")
            }
        }
    }
    private fun stopPlaying() {
        player?.release()
        player = null
    }
    private fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed")
            }

            start()
        }
    }
    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }
    override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        fileName = "${externalCacheDir!!.absolutePath}/audiorecordtest.3gp"
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
        setContentView(R.layout.activity_voice)
        var recbtn= findViewById<Button>(R.id.recordButton)
        var playbtn= findViewById<Button>(R.id.playButton)
        var count1=0
        var count2=0
        var backbtn: Button=findViewById(R.id.backbutton6)
        backbtn.setOnClickListener(){
            finish()
        }
        recbtn.setOnClickListener {
            if(count2==1){
                Toast.makeText(this, "Please stop playback to record.", Toast.LENGTH_SHORT).show()
            }
            else {
                if (count1 == 0) {
                    onRecord(true)
                    recbtn.text = "STOP"
                    recbtn.setBackgroundColor(Color.parseColor("#F44336"))
            //        playbtn.isEnabled = false
                    count1++
                } else {
                    onRecord(false)
                    recbtn.text = "RECORD"
                    recbtn.setBackgroundColor(Color.parseColor("#018786"))
              //      playbtn.isEnabled = true
                    count1++
                }
                count1 %= 2
            }
        }

        playbtn.setOnClickListener {
            if (count1 == 1) {
                Toast.makeText(this, "Please stop recording to play.", Toast.LENGTH_SHORT).show()
            } else {
                if (count2 == 0) {
                    onPlay(true)
                    playbtn.text = "STOP"
                    playbtn.setBackgroundColor(Color.parseColor("#F44336"))
                //    recbtn.isEnabled = false
                    count2++
                } else {
                    onPlay(false)
                    playbtn.text = "PLAY"
                    playbtn.setBackgroundColor(Color.parseColor("#018786"))
                  //  recbtn.isEnabled = true
                    count2++
                }
                count2 %= 2
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        recorder?.release()
        recorder = null
        player?.release()
        player = null
    }
}