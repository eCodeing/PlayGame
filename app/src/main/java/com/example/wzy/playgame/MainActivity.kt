package com.example.wzy.playgame

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.pm.ConfigurationInfo
import android.opengl.GLSurfaceView
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    val TAG:String = "MainActivity"

    val CONTEXT_CLIENT_VERSION:Int = 3

    lateinit var mGLSurfaceView:GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mGLSurfaceView = GLSurfaceView(this)

        if(detectOpenGLES30()){
            mGLSurfaceView.setEGLContextClientVersion(CONTEXT_CLIENT_VERSION)
            mGLSurfaceView.setRenderer(AirHockey(this))
        }else{
            Log.e(TAG,"OpenGL ES 3.0 not supported on device. Exiting...")
            finish()
        }

        setContentView(mGLSurfaceView)

    }

    override fun onResume() {
        super.onResume()
        mGLSurfaceView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mGLSurfaceView.onPause()
    }

    fun detectOpenGLES30():Boolean{
        val am:ActivityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val info:ConfigurationInfo = am.deviceConfigurationInfo

        return (info.reqGlEsVersion >= 0x30000)
    }
}
