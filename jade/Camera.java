package jade;

import org.joml.*;

public class Camera {

    public Matrix4f viewMatrix;
    public Matrix4f projectionMatrix;

    private Vector2f _position;

    public Camera(Vector2f position) {
        _position = position;

        projectionMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();

        adjustProjection();
        adjustViewMatrix();
    }

    public void adjustProjection() {

        projectionMatrix.identity();
        projectionMatrix.ortho(0, 32.0f * 40.0f, 0f, 32.0f * 21.0f, 0, 100f);
    }

    public void adjustViewMatrix() {
        Vector3f cameraFront = new Vector3f(0, 0, -1f);
        Vector3f cameraUp = new Vector3f(0, 1f, 0);

        viewMatrix.identity();
        viewMatrix = viewMatrix.lookAt(new Vector3f(_position.x, _position.y, 20f), cameraFront.add(_position.x, _position.y, 0f), cameraUp);
    }
}
