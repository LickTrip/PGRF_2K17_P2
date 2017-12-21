package model;



import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import popUpMenu.MyMenuItems;
import popUpMenu.MyPopClickListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class JOGLApp {
	private static final int FPS = 60; // animator's target frames per second

	public void start() {
		try {
			JFrame testFrame = new JFrame("ModelFrame");
			testFrame.setSize(1800, 1200);

			// setup OpenGL version
	    	GLProfile profile = GLProfile.getMaximum(true);
	    	GLCapabilities capabilities = new GLCapabilities(profile);
	    	
	    	// The canvas is the widget that's drawn in the JFrame
	    	GLCanvas canvas = new GLCanvas(capabilities);
	    	Renderer ren = new Renderer();
			canvas.addGLEventListener(ren);
			canvas.addMouseListener(ren);
			canvas.addMouseMotionListener(ren);
			canvas.addKeyListener(ren);
	    	canvas.setSize( 1800, 1200 );
			testFrame.add(canvas);

			//region MenuBar
			JMenuBar menuBar = new JMenuBar();
			MyMenuItems myItems = new MyMenuItems(ren);

			JMenu fileMenu = new JMenu("File");
			JMenu fileEdit = new JMenu("Edit");
			menuBar.add(fileMenu);
			menuBar.add(fileEdit);

			fileMenu.add(myItems.getItemExit());

			testFrame.setJMenuBar(menuBar);
			//endregion

			//region PopUpMenu
			canvas.addMouseListener(new MyPopClickListener(ren));
			//endregion
			
	        //shutdown the program on windows close event
	        			
	    	//final Animator animator = new Animator(canvas);
	    	final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);
	    	 
	    	testFrame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					new Thread() {
	                     @Override
	                     public void run() {
	                        if (animator.isStarted()) animator.stop();
	                        System.exit(0);
	                     }
	                  }.start();
				}
			});
	    	testFrame.setTitle(ren.getClass().getName());
	    	testFrame.pack();
	    	testFrame.setVisible(true);
            animator.start(); // start the animation loop
            
            
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new JOGLApp().start());
	}

}