package popUpMenu;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyPopClickListener extends MouseAdapter {
    model.Renderer renderer;
    public MyPopClickListener(model.Renderer renderer){
        this.renderer = renderer;
    }
    public void mousePressed(MouseEvent e){
        if(e.isPopupTrigger())
            doPop(e);
    }

    public void mouseReleased(MouseEvent e){
        if(e.isPopupTrigger())
            doPop(e);
    }

    private  void doPop(MouseEvent e){
        MyPopUpMenu myPopUpMenu = new MyPopUpMenu(renderer);
        myPopUpMenu.show(e.getComponent(), e.getX(), e.getY());
    }
}
