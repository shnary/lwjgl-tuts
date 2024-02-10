package renderer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Shader {

    private int _shaderProgramId;

    private String _vertexSource;
    private String _fragmentSource;
    private String _filePath;

    public Shader(String filePath) {

        _filePath = filePath;
        try {
            Path path = Paths.get(filePath);
            byte[] content = Files.readAllBytes(path);
            String source = new String(content);
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            // fin dhe first pattern after #type 'pattern'
            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index, eol).trim();

            // fin dhe second pattern after #type 'pattern'
            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if (firstPattern.equals("vertex")){
                _vertexSource = splitString[1];
                _fragmentSource = splitString[2];
            }
            else if (firstPattern.equals("fragment")){
                _vertexSource = splitString[2];
                _fragmentSource = splitString[1];
            }
            else {
                throw new IOException("unexpected token: " + firstPattern + " " + secondPattern);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            assert false : "couldn't open file for shader: " + filePath;
        }

    }

    public void compile() {

        int _vertexId;
        int _fragmentId;

        _vertexId = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(_vertexId, _vertexSource);
        glCompileShader(_vertexId);
        int success = glGetShaderi(_vertexId, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(_vertexId, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: " + glGetShaderInfoLog(_vertexId, len));
            assert false : "";
        }

        _fragmentId = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(_fragmentId, _fragmentSource);
        glCompileShader(_fragmentId);
        success = glGetShaderi(_fragmentId, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(_fragmentId, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: " + glGetShaderInfoLog(_fragmentId, len));
            assert false : "";
        }

        _shaderProgramId = glCreateProgram();
        glAttachShader(_shaderProgramId, _vertexId);
        glAttachShader(_shaderProgramId, _fragmentId);
        glLinkProgram(_shaderProgramId);


        success = glGetProgrami(_shaderProgramId, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(_shaderProgramId, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR " + glGetProgramInfoLog(_shaderProgramId, len));
            assert false : "";
        }
    }

    public void use() {
        glUseProgram(_shaderProgramId);
    }

    public void detach() {
        glUseProgram(0);
    }
}
