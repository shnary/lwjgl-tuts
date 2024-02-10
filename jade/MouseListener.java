package jade;

import static org.lwjgl.glfw.GLFW.*;

public class MouseListener {
    
    private static MouseListener _instance;

    private double _scrollX;
    private double _scrollY;

    private double _xPos;
    private double _yPos;
    private double _zPos;
    
    private double _lastX;
    private double _lastY;

    private boolean _mouseButtonPressed[] = new boolean[3];
    private boolean _isDragging;

    private MouseListener() {
        _scrollX = 0.0;
        _scrollY = 0.0;
        _xPos = 0.0;
        _yPos = 0.0;
        _lastX = 0.0;
        _lastY = 0.0;

        _isDragging = false;
    }

    public static MouseListener get() {
        if (_instance == null) {
            _instance = new MouseListener();
        }

        return _instance;
    }

    public static void mousePosCallback(long window, double xPos, double yPos) {
        var w = get();
        w._lastX = w._xPos;
        w._lastY = w._yPos;
        w._xPos = xPos;
        w._yPos = yPos;
        w._isDragging = w._mouseButtonPressed[0] || w._mouseButtonPressed[1] || w._mouseButtonPressed[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        var w = get();

        if (button < w._mouseButtonPressed.length) {
            if (action == GLFW_PRESS) {
                w._mouseButtonPressed[button] = true;
            }
            else if (action == GLFW_RELEASE) {
                w._mouseButtonPressed[button] = false;
                w._isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        var w = get();
        w._scrollX = xOffset;
        w._scrollY = yOffset;
    }

    public static void endFrame() {
        var w = get();
        w._scrollX = 0;
        w._scrollY = 0;
        w._lastX = w._xPos;
        w._lastY = w._yPos;
    }

    public static float getX() {
        return (float)get()._xPos;
    }

    public static float getY() {
        return (float)get()._yPos;
    }

    public static float getDx() {
        return (float)(get()._lastX - get()._xPos);
    }

    public static float getDy() {
        return (float)(get()._lastY - get()._yPos);
    }

    public static float getScrollX() {
        return (float)get()._scrollX;
    }

    public static float getScrollY() {
        return (float)get()._scrollY;
    }

    public static boolean isDragging() {
        return get()._isDragging;
    }

    public static boolean isMouseButtonDown(int button) {
        if (button >= get()._mouseButtonPressed.length) {
            return false;
        }

        return get()._mouseButtonPressed[button];
    }
}
