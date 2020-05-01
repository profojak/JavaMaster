package cz.cvut.fel.pjv.modes.draw;

import cz.cvut.fel.pjv.modes.MainMenu;

import javafx.scene.layout.StackPane;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

/**
 * Implementation of MainMenuDraw: Draw object that handles drawing of MainMenu mode.
 *
 * @see Draw
 */
//TODO
public class MainMenuDraw extends Draw {
  private MainMenu parent;
  private GraphicsContext gc;
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
    Canvas canvas = new Canvas(1000, 525);
    this.gc = canvas.getGraphicsContext2D();
    this.stack.getChildren().add(canvas);

    // Logo thread
    this.logo = new Thread(new MainMenuRunnable(stack));
    this.logo.start();
    
    redraw(Draw.State.DEFAULT);
  }

  /**
   * @see Draw
   * @author profojak
   */
  public void redraw(State state) {
    gc.setFill(Color.web("#E0E0E0"));
    gc.fillRect(0, 0, 375, 525);
    gc.setFill(Color.web("#000000"));
    Integer active = this.parent.getMenuActive();
    for (int i = 0; i < this.parent.getMenuCount(); i++) {
      if (active == i) {
        gc.fillText(">", 25, 40 + i*40);
      }
      gc.fillText(this.parent.getMenuAction(i), 40, 40 + i*40);
    }
  }

  /** @see Draw */
  public void close() {
    this.logo.stop();
  }
}

