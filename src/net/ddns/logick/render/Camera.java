package net.ddns.logick.render;

import Tests.Main;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Matrices;
import com.hackoeur.jglm.Vec3;

public class Camera {
    public static Vec3 POSITION = new Vec3();
    public static Vec3 EYE = new Vec3(0, 0, -1);
    private static Vec3 UP = new Vec3(0, 1, 0);
    public static double PITCH = 0d;
    public static double YAW = -90d;
    private boolean isVectorsChanged = false;
    private Vec3 position;
    private Vec3 eye;
    private Vec3 up = new Vec3(0, 1, 0);
    public Mat4 viewMatrix;
    public Mat4 projectionMatrix;
    private float speed = 0.15f;
    private double prevXPos = Main.SCR_WIDTH / 2f;
    private double prevYPos = Main.SCR_HEIGHT / 2f;
    private double yaw;
    private double pitch;

    public static Camera init(float fov, float aspect, float nearBorder, float farBorder) {
        Mat4 projectionMatrix = Matrices.perspective(fov, aspect, nearBorder, farBorder);
        Mat4 viewMatrix = new Mat4((float) 0.99991, (float) 0.00000, (float) -0.01309, (float) -19.21283,
                (float) -0.00154, (float) 0.99306, (float) -0.11753, (float) -0.86213,
                (float) 0.01300, (float) 0.11754, (float) 0.99298, (float) -60.77901,
                (float) 0.00000, (float) 0.00000, (float) 0.00000, (float) 1.00000);
        return new Camera(projectionMatrix, viewMatrix);
    }

    private Camera(Mat4 projectionMatrix, Mat4 viewMatrix) {
        eye = EYE;
        position = POSITION;
        yaw = YAW;
        pitch = PITCH;
        this.projectionMatrix = projectionMatrix;
        this.viewMatrix = viewMatrix;
    }

    public void update() {
        if (isVectorsChanged) {
            isVectorsChanged = false;
//            up = position.add(UP);
            viewMatrix = Matrices.lookAt(position, eye, up);
        }
    }

    public void mousePos(double xpos, double ypos) {
        isVectorsChanged = true;
        double deltaX = xpos - prevXPos;
        double deltaY = ypos - prevYPos;
        prevXPos = xpos;
        prevYPos = ypos;
        double senetivity = 0.25;
        yaw -= deltaX * senetivity;
        pitch -= deltaY * senetivity;
        if (pitch > 0f)
            pitch = -1f;
        if (pitch < -180.0f)
            pitch = -179f;
        if (yaw >= 360) yaw = 0;
        if (yaw <= -360) yaw = 0;
        double radYaw = Math.toRadians(yaw);
        double radPitch = Math.toRadians(pitch);
        eye = new Vec3((float) (Math.sin(radYaw) * Math.sin(radPitch)), (float) (Math.cos(radPitch)),
                (float) (Math.cos(radYaw) * Math.sin(radPitch))).getUnitVector();
    }

    public void moveForward() {
        isVectorsChanged = true;
        position = position.add(eye.horisontal().getUnitVector().multiply(speed));
    }

    public void moveBack() {
        isVectorsChanged = true;
        position = position.subtract(eye.horisontal().getUnitVector().multiply(speed));
    }

    public void moveLeft() {
        isVectorsChanged = true;
        position = position.subtract(eye.cross(up).getUnitVector().horisontal().multiply(speed));
    }

    public void moveRight() {
        isVectorsChanged = true;
        position = position.add(eye.cross(up).getUnitVector().horisontal().multiply(speed));
    }

    public void moveUp() {
        isVectorsChanged = true;
        position = position.add(UP.multiply(speed));
    }

    public void moveDown() {
        isVectorsChanged = true;
        position = position.subtract(UP.multiply(speed));
    }

    public void restoreDefaultPos() {
        position = POSITION;
        eye = EYE;
        up = UP;
        pitch = PITCH;
        yaw = YAW;
        Main.window.setMousePos(Main.SCR_WIDTH / 2f, Main.SCR_HEIGHT / 2f);
        prevXPos = Main.SCR_WIDTH / 2f;
        prevYPos = Main.SCR_HEIGHT / 2f;
        isVectorsChanged = true;
    }

    public float getX() {
        return position.getX();
    }

    public float getY() {
        return position.getY();
    }

    public float getZ() {
        return position.getZ();
    }

    public double getYaw() {
        return yaw;
    }

    public double getPitch() {
        return pitch;
    }
}
