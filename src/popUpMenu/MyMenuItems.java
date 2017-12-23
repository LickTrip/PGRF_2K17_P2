package popUpMenu;

import model.Renderer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyMenuItems {
    Renderer renderer;

    JMenuItem itemExit;
    JMenuItem itemRotateL;
    JMenuItem itemLine;

    JMenuItem itemShowTexture;
    JMenuItem itemShowTextureSample;


    JMenuItem itemBaseC;
    JMenuItem itemNormalC;
    JMenuItem itemNdoLC;
    JMenuItem itemPhongC;
    JMenuItem itemSpotPhongC;


    public MyMenuItems(Renderer ren){
        renderer = ren;
        exitItem();
        rotateLItem();
        displayModes();
        lineItem();
        showTextItem();
    }

    private void exitItem(){
        itemExit = new JMenuItem("Exit");
        KeyStroke esc = KeyStroke.getKeyStroke("Esc");
        itemExit.setAccelerator(esc);
        itemExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void rotateLItem(){
        itemRotateL = new JMenuItem("Rotate");
        KeyStroke r = KeyStroke.getKeyStroke("R");
        itemRotateL.setAccelerator(r);
        itemRotateL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renderer.setRotete(!renderer.isRotete());
            }
        });
    }

    private void lineItem(){
        itemLine = new JMenuItem("Wire Frame");
        KeyStroke r = KeyStroke.getKeyStroke("L");
        itemLine.setAccelerator(r);
        itemLine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renderer.setLine(!renderer.isLine());
            }
        });
    }

    private void showTextItem(){
        itemShowTexture = new JMenuItem("Show Texture");
        KeyStroke r = KeyStroke.getKeyStroke("T");
        itemShowTexture.setAccelerator(r);
        itemShowTexture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renderer.setShowTexture(!renderer.isShowTexture());
            }
        });

        itemShowTextureSample = new JMenuItem("Show Sampler");
        KeyStroke x = KeyStroke.getKeyStroke("X");
        itemShowTextureSample.setAccelerator(x);
        itemShowTextureSample.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renderer.setTextureSample(!renderer.isTextureSample());
            }
        });

    }

    private void displayModes(){
        itemBaseC = new JMenuItem("Base Color");
        itemBaseC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renderer.setdMode(0);
            }
        });

        itemNormalC = new JMenuItem("Normal Color");
        itemNormalC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renderer.setdMode(1);
            }
        });

        itemNdoLC = new JMenuItem("NdotL Color");
        itemNdoLC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renderer.setdMode(2);
            }
        });

        itemPhongC = new JMenuItem("Phong Color");
        itemPhongC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renderer.setdMode(3);
            }
        });

        itemSpotPhongC = new JMenuItem("Phong Spot Color");
        itemSpotPhongC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renderer.setdMode(4);
            }
        });

    }

    public JMenuItem getItemExit() {
        return itemExit;
    }
    public JMenuItem getItemRotateL() {
        return itemRotateL;
    }

    public JMenuItem getItemBaseC() {
        return itemBaseC;
    }

    public JMenuItem getItemNormalC() {
        return itemNormalC;
    }

    public JMenuItem getItemNdoLC() {
        return itemNdoLC;
    }

    public JMenuItem getItemPhongC() {
        return itemPhongC;
    }

    public JMenuItem getItemSpotPhongC() {
        return itemSpotPhongC;
    }

    public JMenuItem getItemLine() {
        return itemLine;
    }

    public JMenuItem getItemShowTexture() {
        return itemShowTexture;
    }

    public JMenuItem getItemShowTextureSample() {
        return itemShowTextureSample;
    }
}
