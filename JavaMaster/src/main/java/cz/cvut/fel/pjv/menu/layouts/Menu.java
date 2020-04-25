package cz.cvut.fel.pjv.menu.layouts;

import cz.cvut.fel.pjv.menu.Button;

/**
 * Implementation of Menu: menu layout used in main menu.
 *
 * @see Layout
 */
public class Menu extends Layout {
  public Menu() {
    this.buttons = new Button[3];
    this.buttons[0] = new Button("Game");
    this.buttons[1] = new Button("About");
    this.buttons[2] = new Button("Exit");
  }
}

