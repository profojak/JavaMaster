package cz.cvut.fel.pjv.modes.draw;

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
 */
//TODO
public class MainMenuDraw extends Draw {
  private final Integer WIDTH = 1000, HEIGHT = 525, MENU_X = 60, MENU_Y = 125;
  private final String BG_COLOR = "#928374", FILL_COLOR = "#FBF1C7",
    OVERLAY = "/sprites/overlay/mainmenu.png";

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
    Canvas canvas = new Canvas(WIDTH, HEIGHT);
    this.gc = canvas.getGraphicsContext2D();
    this.stack.getChildren().add(canvas);
    setGC();

    // GUI background
    this.gc.setFill(Color.web(BG_COLOR));
    this.gc.fillRect(0, 0, WIDTH, HEIGHT);
    Image overlay = new Image(OVERLAY);
    this.gc.drawImage(overlay, 0, 0);

    // Logo thread
    this.logo = new Thread(new MainMenuRunnable(stack));
    this.logo.start();
    
    redraw(Draw.State.MENU);
  }

  /**
   * @see Draw
   * @author profojak
   */
  public void redraw(State state) {
    this.gc.setFill(Color.web(FILL_COLOR));
    Integer active = this.parent.getMenuActive();
    for (int i = 0; i < this.parent.getMenuCount(); i++) {
      this.gc.drawImage(BUTTON, MENU_X, MENU_Y + i * BUTTON_HEIGHT);
      this.gc.strokeText(this.parent.getMenuAction(i), MENU_X + TEXT_X_OFFSET,
        MENU_Y + i * BUTTON_HEIGHT + TEXT_Y_OFFSET);
      this.gc.fillText(this.parent.getMenuAction(i), MENU_X + TEXT_X_OFFSET,
        MENU_Y + i * BUTTON_HEIGHT + TEXT_Y_OFFSET);
      if (i == active) {
        this.gc.drawImage(BUTTON_ACTIVE, MENU_X, MENU_Y + i * BUTTON_HEIGHT);
      }
    }
  }

  /** @see Draw */
  public void close() {
    this.logo.stop();
  }
}

