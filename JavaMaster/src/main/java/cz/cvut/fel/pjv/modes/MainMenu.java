package cz.cvut.fel.pjv.modes;

import cz.cvut.fel.pjv.Const;
import cz.cvut.fel.pjv.Root;
import cz.cvut.fel.pjv.modes.draw.Draw;
import cz.cvut.fel.pjv.modes.draw.MainMenuDraw;
import cz.cvut.fel.pjv.menu.layouts.Menu;

import javafx.scene.layout.StackPane;

import java.io.File;

/**
 * Class implementing MainMenu.
 *
 * <p>This class is loaded when the game is launched. It is used for switching between other modes.
 *
 * @see Mode
 * @profojak
 */
public class MainMenu implements Mode {
  private final Root root;
  private final Draw draw;
  private final Menu menu;

  private Const.State state = Const.State.MENU;

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

  /**
   * @see Mode
   */
  public void keyUp() {
    switch (state) {
      /* Menu */
      case MENU:
        this.menu.buttonPrevious();
        this.draw.redraw(Const.State.MENU);
        break;
    }
    this.draw.redraw(state);
  }

  /**
   * @see Mode
   */
  public void keyDown() {
    switch (state) {
      /* Menu */
      case MENU:
        this.menu.buttonNext();
        this.draw.redraw(Const.State.MENU);
        break;
    }
    this.draw.redraw(state);
  }

  /**
   * @see Mode
   */
  public void keyLeft() {
    switch (state) {
      /* Menu */
      case MENU:
        this.menu.buttonPrevious();
        this.draw.redraw(Const.State.MENU);
        break;
    }
    this.draw.redraw(state);
  }

  /**
   * @see Mode
   */
  public void keyRight() {
    switch (state) {
      /* Menu */
      case MENU:
        this.menu.buttonNext();
        this.draw.redraw(Const.State.MENU);
        break;
    }
    this.draw.redraw(state);
  }

  /**
   * @see Mode
   */
  public void keyEscape() {
    switch (state) {
      /* About */
      case DEFAULT:
        state = Const.State.MENU;
        break;
    }
    this.draw.redraw(state);
  }

  /**
   * @see Mode
   */
  public void keyEnter() {
    switch (state) {
      /* Menu */
      case MENU:
        switch (this.menu.getAction(this.menu.getActive())) {
          case Const.MENU_GAME:
            File saveFile = this.root.getFile();
            if (saveFile != null && saveFile.canRead()) {
              this.draw.close();
              this.root.switchMode(Const.MENU_GAME);
            }
            return;
          case Const.MENU_EDITOR:
            this.draw.close();
            this.root.switchMode(Const.MENU_EDITOR);
            return;
          case Const.MENU_ABOUT:
            state = Const.State.DEFAULT;
            break;
          case Const.MENU_EXIT:
            System.exit(0);
            break;
        }
        break;
      /* About */
      case DEFAULT:
        state = Const.State.MENU;
        break;
    }
    this.draw.redraw(state);
  }

  /**
   * @see Mode
   */
  public void keyDelete() {
    switch (state) {
      /* About */
      case DEFAULT:
        state = Const.State.MENU;
        break;
    }
    this.draw.redraw(state);
  }

  // GUI

  // Following methods are connecting Menu with MainMenuDraw object.

  /**
   * @see Layout
   */
  public String getMenuAction(Integer index) {
    return this.menu.getAction(index);
  }

  /**
   * @see Layout
   */
  public Integer getMenuActive() {
    return this.menu.getActive();
  }  

  /**
   * @see Layout
   */
  public Integer getMenuCount() {
    return this.menu.getCount();
  }

  /**
   * @see Mode
   */
  public void close() {
    this.draw.close();
  }
}

