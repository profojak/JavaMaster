package cz.cvut.fel.pjv.menu.layouts;

import cz.cvut.fel.pjv.Const;
import cz.cvut.fel.pjv.menu.Button;

/**
 * Class implementing Exit menu that pops up when player wishes to exit the Game.
 *
 * @see Layout
 * @author profojak
 */
public class Exit extends Layout {
  public Exit() {
    this.buttons = new Button[2];
    this.buttons[0] = new Button(Const.MENU_CANCEL);
    this.buttons[1] = new Button(Const.MENU_EXIT);
  }
}

