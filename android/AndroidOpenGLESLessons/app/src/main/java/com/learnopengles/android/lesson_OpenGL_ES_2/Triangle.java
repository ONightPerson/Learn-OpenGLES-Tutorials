package com.learnopengles.android.lesson_OpenGL_ES_2;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.learnopengles.android.common.ShaderHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * @Desc:
 * @Author: liubaozhu
 * @Date: 2021/11/20 7:58 PM
 */
public class Triangle {
    private static final String TAG = "Triangle";

    public static float[] mProjMatrix = new float[16]; // 4 * 4投影矩阵
    public static float[] mVMatrix = new float[16];    // 摄像机位置朝向的参数矩阵
    public static float[] mMVPMatrix;                  // 总变换矩阵

    int mProgram;                                      // 自定义渲染管线着色器程序id
    int muMvpMatrixHandle; // 总变换矩阵引用
    int maPositionHandle;  // 顶点位置属性引用
    int maColorHandle;     // 顶点颜色属性引用
    String mVertexShader;  // 顶点着色器代码脚本
    String mFragmentShader; // 片元着色器代码脚本
    static float[] mMMatrix = new float[16]; // 具体物体的3D变换矩阵，包括旋转、平移、缩放

    FloatBuffer mVertexBuffer; // 顶点坐标数据缓冲
    FloatBuffer mColorBuffer;  // 顶点着色数据缓冲
    int vCount = 0; // 顶点数量
    float xAngle = 0; // 绕x轴旋转的角度

    public Triangle(MyTDView tdView) {
        initVertexData();
        initShader(tdView);
    }

    /**
     * 输出最终变换矩阵
     * @param spec
     * @return
     */
    public static float[] getFinalMatrix(float[] spec) {
        mMVPMatrix = new float[16];
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, spec, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }

    private void initVertexData() {
        vCount = 3;
        final float UNIT_SIZE = 0.2f;
//        float vertices[] = new float[] {
//                -1, 0, 0,
//                0, -1, 0,
//                1, 0, 0
//        };
        float ratio =  0.522f;
        float vertices[] = new float[] {
                -ratio, 0, 0,
                0, -ratio, 0,
                ratio, 0, 0
        };
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder()); // 设置字节序为本地操作系统顺序
        mVertexBuffer = vbb.asFloatBuffer(); // 转换为浮点（Float）型缓冲
        mVertexBuffer.put(vertices); // 在缓冲区内写入顶点数据
        mVertexBuffer.position(0); // 设置缓冲区起始位置

        float colors[] = new float[] {
                1, 1, 1, 0,
                0, 0, 1, 0,
                0, 1, 0, 0,
                
                
        };
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
        cbb.order(ByteOrder.nativeOrder()); // 设置字节序为本地操作系统顺序
        mColorBuffer = cbb.asFloatBuffer(); // 转换为浮点（Float）型缓冲
        mColorBuffer.put(colors); // 在缓冲区内写入颜色数据
        mColorBuffer.position(0); // 设置缓冲区起始位置
    }

    private void initShader(MyTDView view) {
        Resources res = view.getResources();
        mVertexShader = ShaderHelper.loadFromAssetsFile("vertex.sh", res);
        Log.i(TAG, "initShader: mVertexShader" + mVertexShader);
        mFragmentShader = ShaderHelper.loadFromAssetsFile("frag.sh", res);
        int vertexHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, mVertexShader);
        int fragHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, mFragmentShader);
        mProgram = ShaderHelper.createProgram(vertexHandle, fragHandle);
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        maColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
        muMvpMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    public void drawSelf() {
        GLES20.glUseProgram(mProgram);
        Matrix.setRotateM(mMMatrix, 0, 0, 1, 0, 0);
        Matrix.translateM(mMMatrix, 0, 0, 0, 2);
        Matrix.rotateM(mMMatrix, 0, xAngle, 1, 0, 0);

        GLES20.glUniformMatrix4fv(muMvpMatrixHandle, 1, false, Triangle.getFinalMatrix(mMMatrix), 0);
        GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, mVertexBuffer);
        GLES20.glVertexAttribPointer(maColorHandle, 4, GLES20.GL_FLOAT, false, 4 * 4, mColorBuffer);
        GLES20.glEnableVertexAttribArray(maPositionHandle); // 启用顶点位置数据
        GLES20.glEnableVertexAttribArray(maColorHandle); // 启用顶点着色数据
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount); // 执行绘制
    }
}
