package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.modes.*;

import java.util.Scanner;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.canvas.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Root extends Application {
  private Mode mode;
  private GraphicsContext gc;

  /**
   * Switches between MainMenu, Game and Editor objects.
   *
   * <p>This method must be called after MainMenu, Game or Editor close() method execution.
   *
   * @param mode - mode to switch to, can be MainMenu, Game or Editor
   */
  public void switchMode(String mode) {
    switch (mode) {
      case "MainMenu":
        this.mode = new MainMenu();
        break;
    }
    redraw(mode);
  }

  /**
   * Redraws window contents to correspond to the current mode state.
   *
   * @param mode - mode to redraw, can be MainMenu, Game or Editor
   */
  private void redraw(String mode) {
    switch (mode) {
      case "MainMenu":
        Image image = new Image("/sprites/monster/TEMP.png");
        this.gc.drawImage(image, 40, 40);
        this.gc.fillText("Hello, Jadernak! This is Bertík, very scary monster!", 300, 100);
        break;
    }
  }

  /** @see Application */
  @Override
  public void start(Stage stage) {
    Canvas canvas = new Canvas(1000, 525); 
    this.gc = canvas.getGraphicsContext2D();
    StackPane stack = new StackPane();
    stack.getChildren().add(canvas);
    Scene scene = new Scene(stack);

    switchMode("MainMenu");

    // Window
    stage.setScene(scene);
    stage.setResizable(false);
    stage.setTitle("Java Master: the Legend of Segfault");
    stage.show();
  }

  /**
   * Entered when game launches, exiting this method quits game.
   */
  public static void main(String args[]) {
    launch();
  }
}

