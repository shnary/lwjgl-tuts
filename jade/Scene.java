package jade;

public abstract class Scene {

    protected Camera _camera;
    
    public Scene() {

    }

    public abstract void init();

    public abstract void update(float dt);

}
