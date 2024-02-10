package jade;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.Scanner;

import org.lwjgl.glfw.Callbacks.*;
import org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.system.MemoryStack.*;
import org.lwjgl.system.MemoryUtil.*;

import renderer.Shader;


public class LevelEditorScene extends Scene {

    private boolean _changingScene;
    private float _timeToChangeScene = 0.2f;

    private int _vaoId;
    private int _vboId;
    private int _eboId;

    private Shader _defaultShader;

    private float[] _vertexArray = {

        0.5f, -0.5f, 0.0f,          1.0f, 0.0f, 0.0f, 1.0f,
        -0.5f, 0.5f, 0.0f,          0.0f, 1.0f, 0.0f, 1.0f,
        0.5f, 0.5f, 0.0f,           0.0f, 0.0f, 1.0f, 1.0f,
        -0.5f, -0.5f, 0.0f,         1.0f, 1.0f, 0.0f, 1.0f,

    };

    private int[] _elementArray = {

        2, 1, 0,
        0, 1, 3

    };
    
    public LevelEditorScene() {

        System.out.println("Inside LevelEditorScene");
    }

    @Override
    public void init() {
        // =========================
        // Compile and link shaders.
        // =========================

        var version = glGetString(GL_SHADING_LANGUAGE_VERSION);
        System.out.println("OpenGL version %s".formatted(version));

        _defaultShader = new Shader("shaders/default.glsl");
        _defaultShader.compile();

        // ========================
        // Generate VAO, VBO, EAO
        // ========================

        _vaoId = glGenVertexArrays();
        glBindVertexArray(_vaoId);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(_vertexArray.length);
        vertexBuffer.put(_vertexArray).flip();

        _vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, _vboId);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // create the indices.
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(_elementArray.length);
        elementBuffer.put(_elementArray).flip();

        _eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, _eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers.
        int positionsSize = 3;
        int colorSize = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * Float.BYTES;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes , 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * Float.BYTES);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float dt) {

        // Bind the shader program
        _defaultShader.use();

        // Bind the VAO
        glBindVertexArray(_vaoId);

        // Enable the attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, _elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
        _defaultShader.detach();

        System.out.println("" + (1.0f / dt) + " FPS");

        if (!_changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            _changingScene = true;

        }
        
        if (_changingScene && _timeToChangeScene > 0) {
            _timeToChangeScene -= dt;
            Window.get().r -= dt * 5.0f;
            Window.get().g -= dt * 5.0f;
            Window.get().b -= dt * 5.0f;

        }
        else if (_changingScene) {
            Window.changeScene(1);
        }
    }

}
