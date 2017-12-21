package model;

import com.jogamp.opengl.GL2GL3;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

import oglutils.OGLBuffers;
import oglutils.OGLModelOBJ;
import oglutils.OGLTextRenderer;
import oglutils.OGLUtils;
import oglutils.ShaderUtils;
import oglutils.ToFloatArray;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import transforms.*;

import javax.swing.SwingUtilities;

/**
 * GLSL sample:<br/>
 * Load and draw a geometry stored in a Wavefront OBJ file<br/>
 * Requires JOGL 2.3.0 or newer
 *
 * @author PGRF FIM UHK
 * @version 2.0
 * @since 2015-09-06
 */
public class Renderer implements GLEventListener, MouseListener,
        MouseMotionListener, KeyListener {

    int width, height, axisX, axisY;
    int dMode = 3;

    OGLBuffers buffers;
    OGLTextRenderer textRenderer;
    OGLModelOBJ model;

    int shaderProgram, locMat, locProj, locMV, locTime, locCamera, locSwapXY, locMRotate;

    Camera camera = new Camera();
    Mat4 mProj, swapYZ = new Mat4(new double[]{ //for horizontal pos
            1, 0, 0, 0,
            0, 0, 1, 0,
            0, 1, 0, 0,
            0, 0, 0, 1,
    });
    Mat4 mRotate = new Mat4Identity();


    boolean line = false;
    boolean isRotete = false;
    boolean isPressedL;
    float time = 0;


    int[] subroutineDMode = new int[4];

    @Override
    public void init(GLAutoDrawable glDrawable) {
        // check whether shaders are supported
        GL2GL3 gl = glDrawable.getGL().getGL2GL3();
        OGLUtils.shaderCheck(gl);

        gl = OGLUtils.getDebugGL(gl);
        glDrawable.setGL(gl);

        OGLUtils.printOGLparameters(gl);

        textRenderer = new OGLTextRenderer(gl, glDrawable.getSurfaceWidth(), glDrawable.getSurfaceHeight());

        shaderProgram = ShaderUtils.loadProgram(gl, "/model/kukri");
        //shaderProgram = ShaderUtils.loadProgram(gl, "/model/ducky");

        //model = new OGLModelOBJ(gl, "/obj/Vase.obj");
        model = new OGLModelOBJ(gl, "/obj/kukri.obj");
        //model = new OGLModelOBJ(gl, "/obj/ducky.obj");

        buffers = model.getBuffers();

        locMat = gl.glGetUniformLocation(shaderProgram, "mat");
        locProj = gl.glGetUniformLocation(shaderProgram, "mProj");
        locMV = gl.glGetUniformLocation(shaderProgram, "mMV");
        locTime = gl.glGetUniformLocation(shaderProgram, "time");
        locCamera = gl.glGetUniformLocation(shaderProgram, "camera");
        locSwapXY = gl.glGetUniformLocation(shaderProgram, "mSwapXY");
        locMRotate = gl.glGetUniformLocation(shaderProgram, "mRotate");

        subroutineDMode[0] = gl.getGL3().glGetSubroutineIndex(shaderProgram, GL2GL3.GL_FRAGMENT_SHADER, "modeBase");
        subroutineDMode[1] = gl.getGL3().glGetSubroutineIndex(shaderProgram, GL2GL3.GL_FRAGMENT_SHADER, "modeNormal");
        subroutineDMode[2] = gl.getGL3().glGetSubroutineIndex(shaderProgram, GL2GL3.GL_FRAGMENT_SHADER, "modeNdotL");
        subroutineDMode[3] = gl.getGL3().glGetSubroutineIndex(shaderProgram, GL2GL3.GL_FRAGMENT_SHADER, "modePhong");

        int[] maxSub = new int[1];
        int[] maxSubU = new int[1];
        gl.getGL3().glGetIntegerv(GL3.GL_MAX_SUBROUTINES, maxSub, 0);
        gl.getGL3().glGetIntegerv(GL3.GL_MAX_SUBROUTINE_UNIFORM_LOCATIONS, maxSubU, 0);
        System.out.println("Max Subroutines: " + maxSub[0]);
        System.out.println("Max Subroutine Uniforms: " + maxSubU[0]);

        setMyCamera();

        gl.glEnable(GL2GL3.GL_DEPTH_TEST);
    }

    @Override
    public void display(GLAutoDrawable glDrawable) {
        GL2GL3 gl = glDrawable.getGL().getGL2GL3();

        gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        gl.glClear(GL2GL3.GL_COLOR_BUFFER_BIT | GL2GL3.GL_DEPTH_BUFFER_BIT);

        gl.glUseProgram(shaderProgram);

        gl.glUniformMatrix4fv(locProj, 1, false, ToFloatArray.convert(mProj), 0);
        gl.glUniformMatrix4fv(locMV, 1, false, ToFloatArray.convert(camera.getViewMatrix()), 0);
        gl.glUniformMatrix4fv(locSwapXY, 1, false, ToFloatArray.convert(swapYZ), 0);
//        gl.glUniformMatrix4fv(locMV, 1, false, ToFloatArray.convert(swapYZ.mul(mRotate.mul(camera.getViewMatrix()))), 0);
        gl.glUniform3f(locCamera,(float) camera.getPosition().getX(),(float) camera.getPosition().getY(),(float) camera.getPosition().getZ());

        gl.getGL3().glUniformSubroutinesuiv(GL2GL3.GL_FRAGMENT_SHADER, 1, subroutineDMode, dMode % subroutineDMode.length);

        if(isRotete){
            time += 1;
            rotateModel();
            gl.glUniformMatrix4fv(locMRotate, 1, false, ToFloatArray.convert(mRotate), 0);
        }else {
            gl.glUniformMatrix4fv(locMRotate, 1, false, ToFloatArray.convert(mRotate), 0);
        }

        if (line)
            gl.glPolygonMode(GL2GL3.GL_FRONT_AND_BACK, GL2GL3.GL_LINE);
        else
            gl.glPolygonMode(GL2GL3.GL_FRONT_AND_BACK, GL2GL3.GL_FILL);

        buffers.draw(model.getTopology(), shaderProgram);

        String text = new String(this.getClass().getName() + ": [LMB] camera, WSAD");

        textRenderer.drawStr2D(3, height - 20, text);
        textRenderer.drawStr2D(width - 90, 3, " MICHAL");
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
                        int height) {
        this.width = width;
        this.height = height;
        mProj = new Mat4PerspRH(Math.PI / 4, height / (double) width, 0.01, 1000.0);
        textRenderer.updateSize(width, height);
    }

    private void setMyCamera(){
        camera = camera.withPosition(new Vec3D(0.60, 0.30, 0.15))
                .withAzimuth(3.55)
                .withZenith(-0.2);
    }

    private void rotateModel(){
        double angle = time;
        double radian = Math.PI * angle / 180;
        double cosB = Math.cos(radian);
        double sinB = Math.sin(radian);
        mRotate = new Mat4(new double[]{ //for horizontal pos
                cosB, sinB, 0, 0,
                -sinB, cosB, 0, 0,
                0, 0, 1.0, 0,
                0, 0, 0, 1.0,
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e)){
            isPressedL = true;
            axisX = e.getX();
            axisY = e.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isPressedL = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(isPressedL){
        camera = camera.addAzimuth((double) Math.PI * (axisX - e.getX()) / width)
                .addZenith((double) Math.PI * (e.getY() - axisY) / width);
        axisX = e.getX();
        axisY = e.getY();}
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                camera = camera.forward(0.05);
                break;
            case KeyEvent.VK_D:
                camera = camera.right(0.05);
                break;
            case KeyEvent.VK_S:
                camera = camera.backward(0.05);
                break;
            case KeyEvent.VK_A:
                camera = camera.left(0.05);
                break;
            case KeyEvent.VK_CONTROL:
                camera = camera.down(0.05);
                break;
            case KeyEvent.VK_SHIFT:
                camera = camera.up(0.05);
                break;
            case KeyEvent.VK_SPACE:
                camera = camera.withFirstPerson(!camera.getFirstPerson());
                break;
            case KeyEvent.VK_U:
                camera = camera.mulRadius(0.9f);
                break;
            case KeyEvent.VK_J:
                camera = camera.mulRadius(1.1f);
                break;
            case KeyEvent.VK_L:
                line = !line;
                break;
            case KeyEvent.VK_F:
                dMode++;
                break;
            case KeyEvent.VK_R:
                isRotete = !isRotete;
                break;
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
            case KeyEvent.VK_M:
                System.out.println(camera);
                break;
            case KeyEvent.VK_ALT:
                setMyCamera();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void dispose(GLAutoDrawable glDrawable) {
        glDrawable.getGL().getGL2GL3().glDeleteProgram(shaderProgram);

    }

    public void setRotete(boolean rotete) {
        isRotete = rotete;
    }

    public boolean isRotete() {
        return isRotete;
    }

    public int getdMode() {
        return dMode;
    }

    public void setdMode(int dMode) {
        this.dMode = dMode;
    }

    public boolean isLine() {
        return line;
    }

    public void setLine(boolean line) {
        this.line = line;
    }
}