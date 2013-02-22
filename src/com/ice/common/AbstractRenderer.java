package com.ice.common;

import android.opengl.GLSurfaceView;
import android.util.Log;
import com.ice.graphics.geometry.CoordinateSystem;
import com.ice.graphics.shader.Shader;
import com.ice.util.BufferUtil;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static android.opengl.GLES20.*;
import static android.opengl.GLU.gluErrorString;

/**
 * User: jason
 * Date: 13-2-5
 */
public abstract class AbstractRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "AbstractRenderer";

    protected AbstractRenderer() {
        CoordinateSystem.buildGlobal(new CoordinateSystem.SimpleGlobal());
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.i(TAG, "onSurfaceCreated");

        printInfo();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        onCreated(config);
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        Log.i(TAG, "onSurfaceChanged width =" + width + " height =" + height);

        onChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        onFrame();
        checkError();
    }

    protected abstract void onCreated(EGLConfig config);

    protected abstract void onChanged(int width, int height);

    protected abstract void onFrame();

    private void printInfo() {
        Log.i(TAG, "GL_RENDERER = " + glGetString(GL_RENDERER));
        Log.i(TAG, "GL_VENDOR = " + glGetString(GL_VENDOR));
        Log.i(TAG, "GL_VERSION = " + glGetString(GL_VERSION));
        Log.i(TAG, "GL_EXTENSIONS = " + glGetString(GL_EXTENSIONS));

        IntBuffer intBuffer = BufferUtil.intBuffer(1);
        glGetIntegerv(GL_MAX_TEXTURE_SIZE, intBuffer);
        Log.i(TAG, "GL_MAX_TEXTURE_SIZE = " + intBuffer.get());

        FloatBuffer floatBuffer = BufferUtil.floatBuffer(1);
        glGetFloatv(GL_MAX_VERTEX_ATTRIBS, floatBuffer);
        int attributeCapacity = (int) floatBuffer.get(0);
        glGetFloatv(GL_MAX_VERTEX_UNIFORM_VECTORS, floatBuffer);
        int vertexUniformCapacity = (int) floatBuffer.get(0);

        Log.i(TAG, "GL_MAX_VERTEX_ATTRIBS = " + attributeCapacity);
        Log.i(TAG, "GL_MAX_VERTEX_UNIFORM_VECTORS = " + vertexUniformCapacity);

        Shader.setAttributeCapacity(attributeCapacity);
    }

    private void checkError() {
        int errorCode = glGetError();

        if (errorCode != GL_NO_ERROR) {
            throw new IllegalStateException(gluErrorString(errorCode));
        }
    }

}