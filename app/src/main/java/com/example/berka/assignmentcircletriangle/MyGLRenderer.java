package com.example.berka.assignmentcircletriangle;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by berka on 9/4/2016.
 */
public class MyGLRenderer implements GLSurfaceView.Renderer{

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mTranslationMatrix = new float[16];
    private float[] mTempMatrix = new float[16];
    private Circle mCircle;
    private Triangle mTriangle;
    private final float[] mModelMatrix = new float[16];


    float i = 0;
    int direction = 1;


    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

        // Set the background frame color
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
        // podesavamo view matricu, koja ce predstavljati poziciju kamere
        mCircle = new Circle();
        mTriangle = new Triangle();

    }



    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
//        GLES20.glViewport(0, 0, width, height);
//
//        float ratio = (float) width / height;
//
//        // this projection matrix is applied to object coordinates
//        // in the onDrawFrame() method
//        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

        GLES20.glViewport(0, 0, width, height);

        // kreiramo matricu projekcije, metodom frustumM()
        // visina ce biti fiksna, dok ce se sirina menjati u zavisnosti od //proporcija ekrana
        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 10.0f;
        // prosledjujemo podatke za projekcionu matricu

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);

    }


    @Override
    public void onDrawFrame(GL10 gl10) {

        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        // podesavamo view matricu, koja ce predstavljati poziciju kamere

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        mTempMatrix = mMVPMatrix.clone();
        /////////////////////////////////////////////////////////////////////////////


          if (i > 1) {

              direction = -1;

          }
          if (i < -1) {

              direction = 1;
          }

          i += 0.05f * direction;

          Matrix.setIdentityM(mTranslationMatrix, 0);

          Matrix.translateM(mTranslationMatrix, 0, 0.0f, i, 0.0f); // translation y

          Matrix.multiplyMM(mMVPMatrix, 0, mTranslationMatrix, 0, mMVPMatrix, 0);

          mCircle.draw(mMVPMatrix);


        GLES20.glDisable(GLES20.GL_CULL_FACE);

        // onemoguceno depth testiranje
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);


// omogucen blending
        GLES20.glEnable(GLES20.GL_BLEND);

// podesavanje efekta blendinga
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        //GLES20.glDepthMask(false);
          mTriangle.drawT(mTempMatrix);


    }

    public static int loadShader(int type, String shaderCode){

        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}
