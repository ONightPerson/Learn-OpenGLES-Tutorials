package com.learnopengles.android.lesson_OpenGL_ES_2;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @Desc:
 * @Author: liubaozhu
 * @Date: 2021/11/20 8:09 PM
 */
public class MyTDView extends GLSurfaceView {
    private static final String TAG = "MyTDView";
    // 每次旋转三角形的角度
    private final float ANGLE_SPAN = 0.375f;
    private RotateThread rthThread;
    private SceneRenderer mRenderer;

    public MyTDView(Context context) {
        super(context);
        this.setEGLContextClientVersion(2);
        mRenderer = new SceneRenderer();
        this.setRenderer(mRenderer);
        this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    private class SceneRenderer implements GLSurfaceView.Renderer {
        private Triangle tle;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES20.glClearColor(0, 0, 0, 1.0f);
            tle = new Triangle(MyTDView.this);
            GLES20.glEnable(GLES20.GL_DEPTH_TEST); // 开启深度测试
            rthThread = new RotateThread();
            rthThread.start();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            // 设置视口
            GLES20.glViewport(0, 0, width, height);
            // 计算视口的宽高比
            float ratio = (float) width / height;
            Log.i(TAG, "onSurfaceChanged: ratio: " + ratio);
            // 设置透视投影
            Matrix.frustumM(Triangle.mProjMatrix, 0, -ratio, ratio, -1 , 1, 1, 10);
            // 设置摄像机
            Matrix.setLookAtM(Triangle.mVMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, -1.0f, 0f);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            tle.drawSelf();
        }
    }


    public class RotateThread extends Thread {
        public boolean flag = true;

        @Override
        public void run() {
            while (flag) {
                mRenderer.tle.xAngle += ANGLE_SPAN;
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
