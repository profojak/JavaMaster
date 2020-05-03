package cz.cvut.fel.pjv.menu.layouts;

import cz.cvut.fel.pjv.menu.Button;

/**
 * Implementation of Menu: menu layout used in main menu.
 *
 * @see Layout
 */
public class Exit extends Layout {
  public Exit() {
    this.buttons = new Button[2];
    this.buttons[0] = new Button("Cancel");
    this.buttons[1] = new Button("Exit");
  }
}

