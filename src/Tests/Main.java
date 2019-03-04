package Tests;

import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec3;
import com.hackoeur.jglm.Vec4;
import net.ddns.logick.input.Input;
import net.ddns.logick.objects.Cylinder;
import net.ddns.logick.render.Camera;
import net.ddns.logick.render.shaders.Shader;
import net.ddns.logick.render.shaders.ShaderLoader;
import net.ddns.logick.windows.GLFWwindow;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import res.ResourseManager;

import java.util.ArrayList;
import java.util.logging.Logger;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {
    public static int SCR_WIDTH = 1300;
    public static int SCR_HEIGHT = 800;
    public static GLFWwindow window;
    private static int count = 4;
    private static float f = 100f;

    public static void main(String[] args) {
        initGLFW();
        if (args.length != 0) {
            count = Integer.parseInt(args[0]);
        }
        if (args.length == 2) {
            f = Float.parseFloat(args[1]);
        }
        Logger.getGlobal().info("Creating simulation for " + count + " cylinders");
        Camera.POSITION = new Vec3(20f, 8f, 60f);
        Camera.EYE = new Vec3(20f, 10f, 0f);
        Camera.PITCH = -90f;
        Camera.YAW = 0f;
        Camera cam = Camera.init(45f, ((float) SCR_WIDTH) / ((float) SCR_HEIGHT), 0.1f, 100f);
        Input input = new Input(cam);
        window = new GLFWwindow(SCR_WIDTH, SCR_HEIGHT, "Tower of Hanoi");
        window.setCurrient();
        window.setVSync(1);
        window.showWindow();
//        window.grabMouse();
        window.setMousePos(SCR_WIDTH / 2f, SCR_HEIGHT / 2f);
        window.setMouseCallbacks(input.cursorPosCallback);
        window.setKeyCallbacks(input);
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        Shader shader = null;
        try {
            shader = ShaderLoader.loadShaderProgram(ResourseManager.getResourceByPath("shaders/cylinderShader/vertex.glsl"), ResourseManager.getResourceByPath("shaders/cylinderShader/fragment.glsl"));
        } catch (Exception e) {
            e.printStackTrace();
            window.shouldClose();
        }
        Cylinder cl = new Cylinder(10, 2, 200);
        Cylinder cl1 = new Cylinder(1, 2 * count + 5, 100);
        Mat4[] matrices = new Mat4[count];
        for (int i = 0; i < count; i++) {
            matrices[i] = new Mat4(new Vec4(1f - (0.5f / (float) count) * i, 1, 1f - (0.5f / (float) count) * i, 1)).translate(new Vec3(0f, 2f * i, 0f));
        }
        ArrayList<Integer> list = move(0, 2, count);
        int pos = 0;
        ArrayList<Integer>[] towers = new ArrayList[3];
        for (int i = 0; i < 3; i++) {
            towers[i] = new ArrayList<>();
        }
        for (int i = 0; i < count; i++) {
            towers[0].add(count - i - 1);
        }
        glClearColor(0.11f, 0.11f, 0.11f, 0.0f);
        Logger.getGlobal().info("initialised");
        int type = 0;
        float length = 8;
        float remains = length;
        Vec3 trans = new Vec3(0, length / f, 0);
        boolean flag = true;
        cam.restoreDefaultPos();
        window.setMousePos(Main.SCR_WIDTH / 2f, Main.SCR_HEIGHT / 2f);
        while (!window.isWindowShouldClose()) {
            input.processInput(window);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            try {
                shader.use(cam.projectionMatrix, cam.viewMatrix);
                shader.setVec3f("color1", new float[]{0.5f, 0.3f, 0.8f});
                shader.setModelMatrix(Mat4.MAT4_IDENTITY.translate(new Vec3(0, 0, 0)));
                cl1.draw();
                shader.setModelMatrix(Mat4.MAT4_IDENTITY.translate(new Vec3(20, 0, 0)));
                cl1.draw();
                shader.setModelMatrix(Mat4.MAT4_IDENTITY.translate(new Vec3(40, 0, 0)));
                cl1.draw();
                if (flag) {
                    switch (type) {
                        case 0:
                            matrices[towers[list.get(pos)].get(0)] = matrices[towers[list.get(pos)].get(0)].translate(trans);
                            remains -= trans.getY();
                            if (remains <= 0) {
                                type++;
                                length = 20 * (list.get(pos + 1) - list.get(pos)) / (1f - (0.5f / (float) count) * towers[list.get(pos)].get(0));
                                trans = new Vec3(length / f, 0, 0);
                                remains = length;
                            }
                            break;
                        case 1:
                            matrices[towers[list.get(pos)].get(0)] = matrices[towers[list.get(pos)].get(0)].translate(trans);
                            remains -= trans.getX();
                            if (remains * trans.getX() <= 0) {
                                type++;
                                length = -(2 * count + 6 - 2 * towers[list.get(pos + 1)].size());
                                trans = new Vec3(0, length / f, 0);
                                remains = length;
                            }
                            break;
                        case 2:
                            matrices[towers[list.get(pos)].get(0)] = matrices[towers[list.get(pos)].get(0)].translate(trans);
                            remains -= trans.getY();
                            if (remains >= 0) {
                                type = 0;
                                int i = towers[list.get(pos)].get(0);
                                matrices[i] = new Mat4(new Vec4(1f - (0.5f / (float) count) * i, 1, 1f - (0.5f / (float) count) * i, 1)).translate(new Vec3(list.get(pos + 1) * 20 / (1f - (0.5f / (float) count) * i), 2 * (towers[list.get(pos + 1)].size()), 0f));
                                towers[list.get(pos + 1)].add(0, towers[list.get(pos)].remove(0));
                                pos += 2;
                                if (pos == list.size()) {
                                    flag = false;
                                }
                                length = 6 + 2 * (count - towers[list.get(pos)].size() + 1);
                                trans = new Vec3(0, length / f, 0);
                                remains = length;
                            }
                            break;
                    }
                } else {
                    for (int i = 0; i < count; i++) {
                        matrices[i] = new Mat4(new Vec4(1f - (0.5f / (float) count) * i, 1, 1f - (0.5f / (float) count) * i, 1)).translate(new Vec3(40f / (1f - (0.5f / (float) count) * i), 2f * i, 0f));
                    }
                }
                for (int i = 0; i < matrices.length; i++) {
                    shader.setModelMatrix(matrices[i]);
                    shader.setVec3f("color1", new float[]{(float) Math.cos(i), (float) Math.sin(i), (float) Math.cos(i)});
                    cl.draw();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            window.swapBuffers();
            glfwPollEvents();
        }
        Logger.getGlobal().info("game loop was stopped");
        window.destroy();
        shader.destroy();
        glfwTerminate();
        glfwSetErrorCallback(null).free();
        Logger.getGlobal().info("exit");
    }

    private static void initGLFW() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");
    }

    private static ArrayList<Integer> move(int from, int to, int count) {
        ArrayList<Integer> arr = new ArrayList<>();
        if (count == 1) {
            arr.add(from);
            arr.add(to);
        } else {
            int freeTower = 3 - from - to;
            arr.addAll(move(from, freeTower, count - 1));
            arr.add(from);
            arr.add(to);
            arr.addAll(move(freeTower, to, count - 1));
        }
        return arr;
    }
}
