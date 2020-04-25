package cz.cvut.fel.pjv.modes;

import cz.cvut.fel.pjv.Root;
import cz.cvut.fel.pjv.modes.draw.Draw;
import cz.cvut.fel.pjv.modes.draw.MainMenuDraw;
import cz.cvut.fel.pjv.menu.layouts.Menu;

import javafx.scene.canvas.GraphicsContext;

/**
 * Implementation of MainMenu mode: this class handles user input and controlls main menu behavior.
 *
 * <p>This class is loaded when the game is launched. It is used for switching between other modes.
 *
 * @see Mode
 */
public class MainMenu implements Mode {
  private final String GAME = "Game", EXIT = "Exit";
  private final Root root;
  private final Draw draw;
  private final Menu menu;

  /**
   * @param gc - GraphicsContext to draw images to
   * @param root - parent object
   */
  public MainMenu(GraphicsContext gc, Root root) {
    this.root = root;
    this.menu = new Menu();
    this.draw = new MainMenuDraw(gc, this);
  }

  /**
   * @deprecated use MainMenu(GraphicsContext, Root) instead
   */
  @Deprecated
  public MainMenu() {
    this.root = null;
    this.menu = null;
    this.draw = null;
  }

  // Key methods

  public void keyUp() {
    this.menu.buttonPrevious();
    this.draw.redraw(null);
  }

  public void keyDown() {
    this.menu.buttonNext();
    this.draw.redraw(null);
  }

  public void keyLeft() {
  }

  public void keyRight() {
  }

  public void keyEscape() {
  }

  public void keyEnter() {
    if (this.menu.getAction(this.menu.getActive()).equals(GAME)) {
      this.root.switchMode(GAME);
    } else if (this.menu.getAction(this.menu.getActive()).equals(EXIT)) {
      System.exit(0);
    }
  }

  public void keyDelete() {
  }

  // GUI

  /**
   * Following methods are connecting Menu with MainMenuDraw object.
   */

  /** @see Menu */
  public String getMenuAction(Integer index) {
    return this.menu.getAction(index);
  }

  /** @see Menu */
  public Integer getMenuActive() {
    return this.menu.getActive();
  }  

  /** @see Menu */
  public Integer getMenuCount() {
    return this.menu.getCount();
  }
}

