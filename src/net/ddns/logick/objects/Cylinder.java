package net.ddns.logick.objects;

import com.hackoeur.jglm.Mat3;
import com.hackoeur.jglm.Vec3;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Cylinder {
    private int vao;
    private int length;

    public Cylinder(float radius, float height, int slices) {
        vao = glGenVertexArrays();
        int vbo = glGenBuffers();
        int ibo = glGenBuffers();
        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        ArrayList<Float> array = new ArrayList<>();
        Vec3 v = new Vec3(radius, 0, 0);
        array.add(v.getX());
        array.add(v.getY());
        array.add(v.getZ());
        double angle = Math.toRadians(360 / (float) slices);
        for (int i = 1; i < slices; i++) {
            v = v.multiply(new Mat3((float) Math.cos(angle), 0, (float) Math.sin(angle), 0, 1, 0, (float) -Math.sin(angle), 0, (float) Math.cos(angle)));
            array.add(v.getX());
            array.add(v.getY());
            array.add(v.getZ());
        }
        v = new Vec3(radius, height, 0);
        array.add(v.getX());
        array.add(v.getY());
        array.add(v.getZ());
        for (int i = 1; i < slices; i++) {
            v = v.multiply(new Mat3((float) Math.cos(angle), 0, (float) Math.sin(angle), 0, 1, 0, (float) -Math.sin(angle), 0, (float) Math.cos(angle)));
            array.add(v.getX());
            array.add(v.getY());
            array.add(v.getZ());
        }
        array.add(v.getX());
        array.add(v.getY());
        array.add(v.getZ());
        float[] arr = new float[array.size()];
        for (int i = 0; i < array.size(); i++) {
            arr[i] = array.get(i);
        }
        int[] indexes = new int[(slices - 1) * 12];
        for (int i = 0; i < (slices - 2); i++) {
            indexes[3 * i] = 0;
            indexes[3 * i + 1] = i + 1;
            indexes[3 * i + 2] = i + 2;
        }
        for (int i = 0; i < slices - 2; i++) {
            indexes[(slices - 2) * 3 + 3 * i] = slices;
            indexes[(slices - 2) * 3 + 3 * i + 1] = slices + i + 1;
            indexes[(slices - 2) * 3 + 3 * i + 2] = slices + i + 2;
        }
        for (int i = 0; i < slices; i++) {
            indexes[(slices - 2) * 6 + 6 * i] = i;
            indexes[(slices - 2) * 6 + 6 * i + 1] = slices + i;
            indexes[(slices - 2) * 6 + 6 * i + 2] = i + 1;
            indexes[(slices - 2) * 6 + 6 * i + 3] = i + 1;
            indexes[(slices - 2) * 6 + 6 * i + 4] = slices + i + 1;
            indexes[(slices - 2) * 6 + 6 * i + 5] = slices + i;
        }
        indexes[(slices - 2) * 6 + 6 * (slices - 1) + 2] = 0;
        indexes[(slices - 2) * 6 + 6 * (slices - 1) + 3] = 0;
        indexes[(slices - 2) * 6 + 6 * (slices - 1) + 4] = slices;
        indexes[(slices - 2) * 6 + 6 * (slices - 1) + 5] = slices + slices - 1;
        length = indexes.length;
        glBufferData(GL_ARRAY_BUFFER, arr, GL_STATIC_DRAW);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexes, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void draw() {
        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, length, GL_UNSIGNED_INT, 0);
    }
}
