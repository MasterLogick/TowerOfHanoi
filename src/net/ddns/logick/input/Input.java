package net.ddns.logick.input;

import net.ddns.logick.render.Camera;
import net.ddns.logick.windows.GLFWwindow;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Input implements GLFWKeyCallbackI {
    private boolean[] keys = new boolean[512];
    private Camera camera;
    public GLFWCursorPosCallback cursorPosCallback = new GLFWCursorPosCallback() {
        @Override
        public void invoke(long window, double xpos, double ypos) {
            camera.mousePos(xpos, ypos);
        }
    };
    private boolean flag = false;

    public Input(Camera camera) {
        this.camera = camera;
        for (int i = 0; i < 512; i++) {
            keys[i] = false;
        }
    }

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        keys[key] = action != GLFW_RELEASE;
    }

    public void processInput(GLFWwindow window) {
        if (keys[GLFW_KEY_ESCAPE]) {
            window.shouldClose();
        }
        if (keys[GLFW_KEY_C]) {
            camera.restoreDefaultPos();
        }
        if (keys[GLFW_KEY_W]) {
            camera.moveForward();
        }
        if (keys[GLFW_KEY_S]) {
            camera.moveBack();
        }
        if (keys[GLFW_KEY_A]) {
            camera.moveLeft();
        }
        if (keys[GLFW_KEY_D]) {
            camera.moveRight();
        }
        if (keys[GLFW_KEY_SPACE]) {
            camera.moveUp();
        }
        if (keys[GLFW_KEY_LEFT_SHIFT]) {
            camera.moveDown();
        }
        if (keys[GLFW_KEY_I]) {
            flag = !flag;
            if (flag) {
                glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
            } else {
                glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            }
        }
        if (keys[GLFW_KEY_M]) {
            System.out.println("X:" + camera.getX() + " Y:" + camera.getY() + " Z:" + camera.getZ() + " Yaw:" + camera.getYaw() + " Pitch:" + camera.getPitch());
            System.out.println(camera.projectionMatrix.toString());
            System.out.println(camera.viewMatrix.toString());
        }
        camera.update();
    }
}
