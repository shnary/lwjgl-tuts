package jade;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {

    private static KeyListener _instance;

    public boolean keyPressed[] = new boolean[350];

    public static KeyListener get() {
        if (_instance == null) {
            _instance = new KeyListener();
        }

        return _instance;
    }

    public static void keyCallback(long window, int key, int scanCode, int action, int mods) {
        if (get().keyPressed.length <= key){
            return;
        }

        if (action == GLFW_PRESS){
            get().keyPressed[key] = true;
        }
        else if (action == GLFW_RELEASE) {
            get().keyPressed[key] = false;
        }

    }

    public static boolean isKeyPressed(int keyCode) {
        return get().keyPressed[keyCode];
    }
    
}
