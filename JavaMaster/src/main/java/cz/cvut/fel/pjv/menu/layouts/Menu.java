package cz.cvut.fel.pjv.menu.layouts;

import cz.cvut.fel.pjv.menu.Layout;
import cz.cvut.fel.pjv.menu.Button;

public class Menu extends Layout {
  public Menu() {
    this.buttons = new Button[3];
    this.buttons[0] = new Button("Game");
    this.buttons[1] = new Button("About");
    this.buttons[2] = new Button("Exit");
  }
}

