package jade;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import util.Time;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {

    private static Window _window;

    private int _width;
    private int _height;
    private String _title;

    public float r;
    public float g;
    public float b;
    public float a;

    private long _glfwWindow;

    private static Scene _currentScene = null;

    private Window() {
        _width = 1200;
        _height = 800;
        _title = "Marjo";

        r = 0.2f;
        g = 0.2f;
        b = 0.2f;
        a = 1f;

    }

    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0:
                _currentScene = new LevelEditorScene();
                _currentScene.init();
                break;
            case 1:
                _currentScene = new LevelScene();
                _currentScene.init();
                break;
            default:
                assert false : "unknown scene " + newScene + "!";
                break;
        }
    }

    public static Window get() {
        if (_window == null) {
            _window = new Window();
        }

        return _window;
    }

    public void run() {

        System.out.println("Hello LWJGL " + Version.getVersion());

        init();
        loop();

        // free the memory
        glfwFreeCallbacks(_glfwWindow);
        glfwDestroyWindow(_glfwWindow);

        // terminate glfw
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Setup the error callback.
        GLFWErrorCallback.createPrint(System.err).set();

        // initialize glfw
        if (!glfwInit()) {
            throw new IllegalStateException("unable to initalize glfw");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

        // create the window
        _glfwWindow = glfwCreateWindow(_width, _height, _title, NULL, NULL);
        if (_glfwWindow == NULL) {
            throw new IllegalStateException("failed to create the glfw window");
        }

        glfwSetCursorPosCallback(_glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(_glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(_glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(_glfwWindow, KeyListener::keyCallback);

        // make the opengl context current.
        glfwMakeContextCurrent(_glfwWindow);
        // enable vsync
        glfwSwapInterval(1);

        // make the window visible
        glfwShowWindow(_glfwWindow);

        // * makes the opengl capabilities available.
        GL.createCapabilities();

        changeScene(0);
    }

    private void loop() {
        float beginTime = Time.getTime();
        float endTime = Time.getTime();
        float dt = -1.0f;

        while (!glfwWindowShouldClose(_glfwWindow)) {
            // poll events
            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if (dt >= 0) {
                _currentScene.update(dt);
            }

            if (KeyListener.isKeyPressed(GLFW_KEY_SPACE)){
                System.out.println("Space key has pressed.");
            }

            glfwSwapBuffers(_glfwWindow);

            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }

    }
    
}
