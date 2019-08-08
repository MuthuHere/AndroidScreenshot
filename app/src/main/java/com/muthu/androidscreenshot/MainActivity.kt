package com.muthu.androidscreenshot

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.muthu.androidscreenshot.helper.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var bitmap: Bitmap? = null
    private var clickeCount = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        //enable click
        btnDelete.setOnClickListener(this)

    }


    /**
     * Navigation controls
     */
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_capture -> {
                onCapture()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_save -> {

                requestPermissionAndSaveInStorage()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_layout_capture -> {
                onCaptureLayout()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    /**
     * Capturing the whole screen
     */
    private fun onCapture() {
        clickeCount += 1

        tvTitle.text = "Screenshot Count: $clickeCount"

        bitmap = ScreenshotUtil.getInstance().takeScreenshotForScreen(this@MainActivity)
        ivScreenshot.setImageBitmap(bitmap)
    }


    /**
     * Capturing the only view screen
     */
    private fun onCaptureLayout() {

        clickeCount += 1

        tvTitle.text = "Screenshot Count: $clickeCount"


        bitmap = ScreenshotUtil.getInstance().takeScreenshotForView(containerView)
        ivScreenshot.setImageBitmap(bitmap)
    }


    /**
     * Resetting taken screenshots
     */
    private fun resetImageView() {

        clickeCount = 0
        tvTitle.text = "Click Capture/Capture Layout to take SS"


        tvTitle.text = "Screenshot Count: $clickeCount"
        bitmap = null
        ivScreenshot.setImageBitmap(bitmap)
    }



    override fun onClick(v: View?) {
        if (v == null) {
            return
        }
        when (v) {
            btnDelete -> {
                resetImageView()
            }
        }
    }


    /**
     * Request user to allow storage permission to store screenshot
     * here i'm using Dexter library to easy access!
     * to know more about Dexter {@link https://github.com/Karumi/Dexter}
     */
    private fun requestPermissionAndSaveInStorage() {

        Dexter.withActivity(this)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {

                    if (bitmap != null) {
                        val path = Environment.getExternalStorageDirectory().toString() + "/ss.png"
                        FileUtil.getInstance().storeBitmap(bitmap, path)
                        toast("Screenshot Saved at $path")
                    } else {
                        toast("Take Screenshot to save :)")
                    }

                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    // check for permanent denial of permission
                    if (response.isPermanentlyDenied) {
                        goToAppSettings(this@MainActivity, PERMISSION_SETTINGS)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                    token.continuePermissionRequest()
                }
            }).check()
    }


    /**
     * Permission result will trigger
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    PERMISSION_SETTINGS -> {
                        requestPermissionAndSaveInStorage()
                    }
                }

            }
            Activity.RESULT_CANCELED -> {
                //request again
                requestPermissionAndSaveInStorage()
            }
        }
    }


}
