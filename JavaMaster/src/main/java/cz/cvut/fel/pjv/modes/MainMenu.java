package cz.cvut.fel.pjv.modes;

import cz.cvut.fel.pjv.Const;
import cz.cvut.fel.pjv.Root;
import cz.cvut.fel.pjv.modes.draw.Draw;
import cz.cvut.fel.pjv.modes.draw.MainMenuDraw;
import cz.cvut.fel.pjv.menu.layouts.Menu;

import javafx.scene.layout.StackPane;

/**
 * Implementation of MainMenu mode: this class handles user input and controlls main menu behavior.
 *
 * <p>This class is loaded when the game is launched. It is used for switching between other modes.
 *
 * @see Mode
 */
public class MainMenu implements Mode {
  private final Root root;
  private final Draw draw;
  private final Menu menu;

  /**
   * @param stack - StackPane to draw images to
   * @param root - parent object
   */
  public MainMenu(StackPane stack, Root root) {
    this.root = root;
    this.menu = new Menu();
    this.draw = new MainMenuDraw(stack, this);
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
    this.draw.redraw(Const.State.MENU);
  }

  public void keyDown() {
    this.menu.buttonNext();
    this.draw.redraw(Const.State.MENU);
  }

  public void keyLeft() {
    this.menu.buttonPrevious();
    this.draw.redraw(Const.State.MENU);
  }

  public void keyRight() {
    this.menu.buttonNext();
    this.draw.redraw(Const.State.MENU);
  }

  public void keyEscape() {
  }

  public void keyEnter() {
    switch (this.menu.getAction(this.menu.getActive())) {
      case Const.MENU_GAME:
        this.draw.close();
        this.root.switchMode(Const.MENU_GAME);
        break;
      case Const.MENU_ABOUT:
        this.draw.redraw(Const.State.DEFAULT);
        break;
      case Const.MENU_EXIT:
        System.exit(0);
        break;
    }
  }

  public void keyDelete() {
  }

  // GUI

  /**
   * Following methods are connecting Menu with MainMenuDraw object.
   */

  /** @see Layout */
  public String getMenuAction(Integer index) {
    return this.menu.getAction(index);
  }

  /** @see Layout */
  public Integer getMenuActive() {
    return this.menu.getActive();
  }  

  /** @see Layout */
  public Integer getMenuCount() {
    return this.menu.getCount();
  }
}

