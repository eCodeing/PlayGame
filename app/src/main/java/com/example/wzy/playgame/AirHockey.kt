package com.example.wzy.playgame

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.util.Log
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 作者：author
 * 创建时间：2018/8/12:21:24
 * 描述：
 * 修改人：
 * 修改时间：2018/8/12:21:24
 * 修改备注：
 */
class AirHockey:GLSurfaceView.Renderer{

    private val TAG = "AirHockey"

    private val UCOLOR = "uColor"

    private val VPOSITION = "vPosition"

    private val VALUE_POS = 0.3f

    // Member variables
    private var mProgramObject: Int = 0
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var mVertices: FloatBuffer
    private var uColorLocation:Int = 0
    private var vPositionLocation:Int = 0

    private val mVerticesData = floatArrayOf(
            //Trangle1
            -0.5f, -0.5f,
            0.5f, 0.5f,
            -0.5f, 0.5f,
            //Trangle2
            -0.5f, -0.5f,
            0.5f,-0.5f,
            0.5f,0.5f,
            //MiddleLine
            -0.5f, 0.0f,
            0.5f, 0.0f,
            //Mallets
            0.0f, -0.25f,
            0.0f, 0.25f,
            //Trangle3
            -0.51f, -0.51f,
            0.51f, 0.51f,
            -0.51f, 0.51f,
            //Trangle4
            -0.51f, -0.51f,
            0.51f,-0.51f,
            0.51f,0.51f,
            //ballInMiddle
            0.0f, 0.0f
            )

    constructor(context: Context){
        mVertices = ByteBuffer.allocateDirect(mVerticesData.size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer()
        mVertices.put(mVerticesData).position(0)
    }

    ///
    // Create a shader object, load the shader source, and
    // compile the shader.
    //
    private fun LoadShader(type: Int, shaderSrc: String): Int {
        val shader: Int
        val compiled = IntArray(1)

        // Create the shader object
        shader = GLES30.glCreateShader(type)

        if (shader == 0) {
            return 0
        }

        // Load the shader source
        GLES30.glShaderSource(shader, shaderSrc)

        // Compile the shader
        GLES30.glCompileShader(shader)

        // Check the compile status
        GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compiled, 0)

        if (compiled[0] == 0) {
            Log.e(TAG, GLES30.glGetShaderInfoLog(shader))
            GLES30.glDeleteShader(shader)
            return 0
        }

        return shader
    }

    override fun onDrawFrame(gl: GL10?) {
        // Set the viewport
        GLES30.glViewport(0, 0, mWidth, mHeight)

        // Clear the color buffer
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)

        // Use the program object
        GLES30.glUseProgram(mProgramObject)

        // Load the vertex data
//        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 0, mVertices)
//        GLES30.glEnableVertexAttribArray(0)

        //绘制外边框
        GLES30.glUniform4f(uColorLocation, 1.0f,0.0f,0.0f,1.0f)

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 10,6)

        GLES30.glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 0.0f)
        //GLES30.GL_TRIANGLES:想要绘制三角形
        //从顶点数组开头读数据，读6个顶点
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 6)

        GLES30.glUniform4f(uColorLocation, 1.0f,0.0f,0.0f,1.0f)

        GLES30.glDrawArrays(GLES30.GL_LINES,6, 2)

        GLES30.glUniform4f(uColorLocation,0.0f,0.0f,1.0f,1.0f)

        GLES30.glDrawArrays(GLES30.GL_POINTS, 8, 1)

        GLES30.glUniform4f(uColorLocation,1.0f,0.0f,0.0f,1.0f)

        GLES30.glDrawArrays(GLES30.GL_POINTS, 9, 1)

        //绘制球
        GLES30.glUniform4f(uColorLocation, 0.6f,0.2f,0.1f,1.0f)

        GLES30.glDrawArrays(GLES30.GL_POINTS, 16,1)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        mWidth = width
        mHeight = height

    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        val vShaderStr = ("#version 300 es 	    \n"
                + "in vec4 vPosition;           \n"
                + "void main()                  \n"
                + "{                            \n"
                + "   gl_Position = vPosition;  \n"
                + "   gl_PointSize = 10.0;      \n"
                + "}                            \n")

        val fShaderStr = ("#version 300 es		 			    \n"
                + "precision mediump float;					  	\n"
                + "uniform vec4 uColor;	 			 	 	    \n"
                + "out vec4 fragColor;                          \n"
                + "void main()                                  \n"
                + "{                                            \n"
                + "  fragColor = uColor;                	    \n"
                + "}                                            \n")

        val vertexShader: Int
        val fragmentShader: Int
        val programObject: Int
        val linked = IntArray(1)


        // Load the vertex/fragment shaders
        vertexShader = LoadShader(GLES30.GL_VERTEX_SHADER, vShaderStr)
        fragmentShader = LoadShader(GLES30.GL_FRAGMENT_SHADER, fShaderStr)

        // Create the program object
        programObject = GLES30.glCreateProgram()

        if (programObject == 0) {
            return
        }

        GLES30.glAttachShader(programObject, vertexShader)
        GLES30.glAttachShader(programObject, fragmentShader)

        // Bind vPosition to attribute 0
    //    GLES30.glBindAttribLocation(programObject, 0, VPOSITION)

        // Link the program
        GLES30.glLinkProgram(programObject)

        // Check the link status
        GLES30.glGetProgramiv(programObject, GLES30.GL_LINK_STATUS, linked, 0)

        if (linked[0] == 0) {
            Log.e(TAG, "Error linking program:")
            Log.e(TAG, GLES30.glGetProgramInfoLog(programObject))
            GLES30.glDeleteProgram(programObject)
            return
        }

        // Store the program object
        mProgramObject = programObject

        uColorLocation = GLES30.glGetUniformLocation(mProgramObject, UCOLOR)

        vPositionLocation = GLES30.glGetAttribLocation(mProgramObject,VPOSITION)

        GLES30.glVertexAttribPointer(vPositionLocation,
                2,GLES30.GL_FLOAT,
                false,0, mVertices)

        GLES30.glEnableVertexAttribArray(vPositionLocation)

        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
    }
}