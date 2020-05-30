package cz.cvut.fel.pjv.modes.draw;

import cz.cvut.fel.pjv.Const;
import cz.cvut.fel.pjv.modes.MainMenu;

import javafx.scene.layout.StackPane;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

/**
 * Class drawing MainMenu state to the screen.
 *
 * @see Draw
 * @author profojak
 */
//TODO
public class MainMenuDraw extends Draw {
  private final Integer MENU_X = 60, MENU_Y = 75;
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
   * @author profojak, povolji2
   */
  public void redraw(Const.State state) {
    switch (state) {
      /* Menu */
      case MENU:
        this.gc.setFill(Color.web(Const.COLOR_BG));
        this.gc.fillRect(30, 75, 345, 280);
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
        break;
      /* About */
      case DEFAULT:
        this.gc.setFill(Color.web(Const.COLOR_BG));
        this.gc.fillRect(60, 75, 285, 280);
        this.gc.drawImage(IMAGE_BUTTON, 60, 360);
        this.gc.setFill(Color.web(Const.COLOR_FILL));
        // Info
        this.gc.strokeText("Game by:", 60 + Const.TEXT_X_OFFSET, 110);
        this.gc.fillText("Game by:", 60 + Const.TEXT_X_OFFSET, 110);
        this.gc.strokeText("J. Profota", 60 + Const.TEXT_X_OFFSET, 160);
        this.gc.fillText("J. Profota", 60 + Const.TEXT_X_OFFSET, 160);
        this.gc.strokeText("J. Povolny", 60 + Const.TEXT_X_OFFSET, 210);
        this.gc.fillText("J. Povolny", 60 + Const.TEXT_X_OFFSET, 210);
        this.gc.strokeText("B0B36PJV", 60 + Const.TEXT_X_OFFSET, 295);
        this.gc.fillText("B0B36PJV", 60 + Const.TEXT_X_OFFSET, 295);
        this.gc.strokeText("at FEE CTU", 60 + Const.TEXT_X_OFFSET, 345);
        this.gc.fillText("at FEE CTU", 60 + Const.TEXT_X_OFFSET, 345);
        // Back button
        this.gc.strokeText("Back", 60 + Const.TEXT_X_OFFSET, 360 + Const.TEXT_Y_OFFSET);
        this.gc.fillText("Back", 60 + Const.TEXT_X_OFFSET, 360 + Const.TEXT_Y_OFFSET);
        this.gc.drawImage(IMAGE_BUTTON_ACTIVE, 60, 360);
        break;
    }
  }

  /**
   * @see Draw
   */
  public void close() {
    this.logo.interrupt();
  }
}

