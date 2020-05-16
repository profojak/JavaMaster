package cz.cvut.fel.pjv.modes.draw;

import cz.cvut.fel.pjv.Const;
import cz.cvut.fel.pjv.modes.MainMenu;

import javafx.scene.layout.StackPane;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

/**
 * Implementation of MainMenuDraw: Draw object that handles drawing of MainMenu mode.
 *
 * @see Draw
 * @author profojak
 */
//TODO
public class MainMenuDraw extends Draw {
  private final Integer MENU_X = 60, MENU_Y = 125;
  private final String OVERLAY = "/sprites/overlay/mainmenu.png";

  private MainMenu parent;
  private Thread logo;

  /**
   * @param stack - StackPane to draw images to
   * @param - Mode from which to get information to draw
   */
  public MainMenuDraw(StackPane stack, MainMenu parent) {
    super(stack);
    this.parent = parent;

    // GUI setup
    this.stack.getChildren().clear();
    Canvas canvas = new Canvas(Const.WINDOW_WIDTH, Const.WINDOW_HEIGHT);
    this.gc = canvas.getGraphicsContext2D();
    this.stack.getChildren().add(canvas);
    setGC();

    // GUI background
    this.gc.setFill(Color.web(Const.COLOR_BG));
    this.gc.fillRect(0, 0, Const.WINDOW_WIDTH, Const.WINDOW_HEIGHT);
    Image overlay = new Image(OVERLAY);
    this.gc.drawImage(overlay, 0, 0);

    // Logo thread
    this.logo = new Thread(new MainMenuRunnable(stack));
    this.logo.start();
    
    redraw(Const.State.MENU);
  }

  /**
   * @see Draw
   */
  public void redraw(Const.State state) {
    this.gc.setFill(Color.web(Const.COLOR_FILL));
    Integer active = this.parent.getMenuActive();
    for (int i = 0; i < this.parent.getMenuCount(); i++) {
      this.gc.drawImage(IMAGE_BUTTON, MENU_X, MENU_Y + i * Const.BUTTON_HEIGHT);
      this.gc.strokeText(this.parent.getMenuAction(i), MENU_X + Const.TEXT_X_OFFSET,
        MENU_Y + i * Const.BUTTON_HEIGHT + Const.TEXT_Y_OFFSET);
      this.gc.fillText(this.parent.getMenuAction(i), MENU_X + Const.TEXT_X_OFFSET,
        MENU_Y + i * Const.BUTTON_HEIGHT + Const.TEXT_Y_OFFSET);
      if (i == active) {
        this.gc.drawImage(IMAGE_BUTTON_ACTIVE, MENU_X, MENU_Y + i * Const.BUTTON_HEIGHT);
      }
    }
  }

  /** @see Draw */
  public void close() {
    this.logo.interrupt();
  }
}

