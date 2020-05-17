package cz.cvut.fel.pjv.menu.layouts;

import cz.cvut.fel.pjv.Const;
import cz.cvut.fel.pjv.menu.Button;

/**
 * Implementation of Menu: menu layout used in main menu.
 *
 * @see Layout
 */
public class Menu extends Layout {
  public Menu() {
    this.buttons = new Button[4];
    this.buttons[0] = new Button(Const.MENU_GAME);
    this.buttons[1] = new Button(Const.MENU_EDITOR);
    this.buttons[2] = new Button(Const.MENU_ABOUT);
    this.buttons[3] = new Button(Const.MENU_EXIT);
  }
}

