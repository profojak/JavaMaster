package cz.cvut.fel.pjv.modes;

import cz.cvut.fel.pjv.Root;
import cz.cvut.fel.pjv.modes.draw.Draw;
import cz.cvut.fel.pjv.modes.draw.MainMenuDraw;
import cz.cvut.fel.pjv.menu.layouts.Menu;

import javafx.scene.canvas.GraphicsContext;

/** @see Mode */
public class MainMenu implements Mode {
  private Root root;
  private Draw draw;
  private Menu menu;

  public MainMenu(GraphicsContext gc, Root root) {
    this.root = root;
    this.menu = new Menu();
    this.draw = new MainMenuDraw(gc, this);
  }

  // Key methods

  public void keyUp() {
    this.menu.buttonPrevious();
    this.draw.redraw();
  }

  public void keyDown() {
    this.menu.buttonNext();
    this.draw.redraw();
  }

  public void keyLeft() {
  }

  public void keyRight() {
  }

  public void keyEscape() {
  }

  public void keyEnter() {
    switch (this.menu.getAction(this.menu.getActive())) {
      case "Game":
        this.root.switchMode("Game");
        break;
      default:
        break;
    }
  }

  public void keyDelete() {
  }

  // GUI

  public String getMenuAction(Integer index) {
    return this.menu.getAction(index);
  }

  public Integer getMenuActive() {
    return this.menu.getActive();
  }  

  public Integer getMenuCount() {
    return this.menu.getCount();
  }
}

