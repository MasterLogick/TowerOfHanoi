package net.ddns.logick.windows;

import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GLFWwindow {
    private long id;

    public GLFWwindow(int width, int height, String name) {
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        id = glfwCreateWindow(width, height, name, NULL, NULL);
        if (id == NULL)
            throw new RuntimeException("Failed to create the GLFW window");
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);
            glfwGetWindowSize(id, pWidth, pHeight);
        }
    }

    public void setCurrient() {
        glfwMakeContextCurrent(id);
    }

    public void setVSync(int val) {
        glfwSwapInterval(val);
    }

    public void showWindow() {
        glfwShowWindow(id);
    }

    public void swapBuffers() {
        glfwSwapBuffers(id);
    }

    public boolean isWindowShouldClose() {
        return glfwWindowShouldClose(id);
    }

    public void shouldClose() {
        glfwSetWindowShouldClose(id, true);
    }

    private void freeCallbacks() {
        glfwFreeCallbacks(id);
    }

    public void destroy() {
        freeCallbacks();
        glfwDestroyWindow(id);
    }

    public void setKeyCallbacks(GLFWKeyCallbackI glfwKeyCallbackI) {
        glfwSetKeyCallback(id, glfwKeyCallbackI);
    }

    public void setMouseCallbacks(GLFWCursorPosCallbackI cdfn) {
        glfwSetCursorPosCallback(id, cdfn);
    }

    public void setMousePos(double xpos, double ypos) {
        glfwSetCursorPos(id, xpos, ypos);
    }
}
