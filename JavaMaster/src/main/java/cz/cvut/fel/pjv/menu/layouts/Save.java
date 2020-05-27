package cz.cvut.fel.pjv.menu.layouts;

import cz.cvut.fel.pjv.Const;
import cz.cvut.fel.pjv.menu.Button;

/**
 * Implementation of Menu: menu layout used in game menu.
 *
 * @see Layout
 */
public class Save extends Layout {
  public Save() {
    this.buttons = new Button[3];
    this.buttons[0] = new Button(Const.MENU_CANCEL);
    this.buttons[1] = new Button(Const.MENU_SAVE);
    this.buttons[2] = new Button(Const.MENU_EXIT);
  }
}

