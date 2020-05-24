package cz.cvut.fel.pjv.menu.layouts;

import cz.cvut.fel.pjv.Const;
import cz.cvut.fel.pjv.menu.Button;

/**
 * Implementation of Menu: menu layout used in game menu.
 *
 * @see Layout
 */
public class Continue extends Layout {
  public Continue() {
    this.buttons = new Button[2];
    this.buttons[0] = new Button(Const.MENU_DESCEND);
    this.buttons[1] = new Button(Const.MENU_NOT_YET);
  }
}

