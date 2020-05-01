package cz.cvut.fel.pjv.modes.draw;

import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of MainMenuRunnable: thread that draws pulsing logo.
 */
public class MainMenuRunnable implements Runnable {
  private final String YELLOW = "\u001B[33m", RESET = "\u001B[0m";
  private static final Logger logger = Logger.getLogger(GameDraw.class.getName());
  private final ImageView logo;

  private StackPane stack;

  /**
   * @param stack - StackPane to draw images to
   */
  public MainMenuRunnable(StackPane stack) {
    this.stack = stack;

    // GUI
    this.logo = new ImageView(new Image("/sprites/logo.png"));
    this.logo.setPreserveRatio(true);
    this.logo.setCache(true);
    this.logo.setSmooth(false);
    this.stack.getChildren().add(logo);
    this.stack.setMargin(logo, new Insets(0, 0, 0, 385));
  }

  /** @author profojak */
  @Override
  public void run() {
    while (true) {
      try {
        this.logo.setRotate(8 * Math.sin(System.currentTimeMillis() * 0.0004));
        this.logo.setFitWidth(500 + 30 * Math.sin(System.currentTimeMillis() * 0.0012));
        Thread.sleep(50);
      // Stop thread when app is closed
      } catch (Exception e) {
        Thread.currentThread().stop();
        return;
      }
    }
  }
}

