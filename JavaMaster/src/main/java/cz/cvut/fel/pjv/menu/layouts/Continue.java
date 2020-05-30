package cz.cvut.fel.pjv.menu.layouts;

import cz.cvut.fel.pjv.Const;
import cz.cvut.fel.pjv.menu.Button;

/**
 * Class implementing Continue menu that pops up when dungeon level is finished.
 *
 * @see Layout
 * @author profojak
 */
public class Continue extends Layout {
  public Continue() {
    this.buttons = new Button[2];
    this.buttons[0] = new Button(Const.MENU_DESCEND);
    this.buttons[1] = new Button(Const.MENU_NOT_YET);
  }
}

