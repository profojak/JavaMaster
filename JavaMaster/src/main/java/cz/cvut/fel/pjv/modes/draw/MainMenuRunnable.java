package cz.cvut.fel.pjv.modes.draw;

import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Implementation of MainMenuRunnable: thread that draws pulsing logo.
 *
 * @author profojak
 */
public class MainMenuRunnable implements Runnable {
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
    this.stack.setMargin(logo, new Insets(0, 0, 0, 340));
  }

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        this.logo.setRotate(8 * Math.sin(System.currentTimeMillis() * 0.0004));
        this.logo.setFitWidth(500 + 30 * Math.sin(System.currentTimeMillis() * 0.0012));
        Thread.sleep(50);
      } catch (Exception e) {
        return;
      }
    }
  }
}

