package popUpMenu;

import javax.swing.*;

public class MyPopUpMenu extends JPopupMenu {

    public  MyPopUpMenu(model.Renderer renderer){
        MyMenuItems myItems = new MyMenuItems(renderer);

        JMenu dispMode = new JMenu("Display Mode");
        dispMode.add(myItems.getItemBaseC());
        dispMode.add(myItems.getItemNormalC());
        dispMode.add(myItems.getItemNdoLC());
        dispMode.add(myItems.getItemPhongC());
        dispMode.add(myItems.getItemSpotPhongC());
        add(dispMode);

        add(myItems.getItemRotateL());
        add(myItems.getItemLine());

        JMenu texMenu = new JMenu("Textures");
        texMenu.add(myItems.getItemShowTexture());
        texMenu.add(myItems.getItemShowTextureSample());
        add(texMenu);

        add(myItems.getItemExit());
    }
}
