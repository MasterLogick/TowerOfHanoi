package net.ddns.logick.render.shaders;

import com.hackoeur.jglm.Mat4;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private int programID;

    public void use(Mat4 projectionMatrix, Mat4 viewMatrix) throws Exception {
        glUseProgram(programID);
        setProjectionMatrix(projectionMatrix);
        setViewMatrix(viewMatrix);
    }

    Shader(int programID) {
        this.programID = programID;
    }

    public void destroy() {
        glDeleteProgram(programID);
    }

    public void setModelMatrix(Mat4 modelMatrix) throws Exception {
        int loc = glGetUniformLocation(programID, "modelMatrix");
        if (loc == -1) throw new Exception("Invalid uniform variable's name: modelMatrix");
        glUniformMatrix4fv(loc, false, modelMatrix.getBuffer());
    }

    private void setViewMatrix(Mat4 viewMatrix) throws Exception {
        int loc = glGetUniformLocation(programID, "viewMatrix");
        if (loc == -1) throw new Exception("Invalid uniform variable's name: viewMatrix");
        glUniformMatrix4fv(loc, false, viewMatrix.getBuffer());
    }

    private void setProjectionMatrix(Mat4 projectionMatrix) throws Exception {
        int loc = glGetUniformLocation(programID, "projectionMatrix");
        if (loc == -1) throw new Exception("Invalid uniform variable's name: projectionMatrix");
        glUniformMatrix4fv(loc, false, projectionMatrix.getBuffer());
    }

    public void setVec3f(String name, float[] fb) throws Exception {
        int loc = glGetUniformLocation(programID, name);
        if (loc == -1) throw new Exception("Invalid uniform variable's name: " + name);
        glUniform3fv(loc, fb);
    }
}
