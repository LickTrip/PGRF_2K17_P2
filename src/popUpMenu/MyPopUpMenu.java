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

        add(dispMode);
        add(myItems.getItemRotateL());
        add(myItems.getItemLine());
        add(myItems.getItemExit());
    }
}
