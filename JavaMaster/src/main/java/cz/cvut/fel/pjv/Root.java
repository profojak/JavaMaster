package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.modes.*;

import javafx.application.Application;
import javafx.scene.canvas.*;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
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
    this.mode.close();
    switch (mode) {
      case "MainMenu":
        this.mode = new MainMenu(this.gc, this);
        break;
      case "Game":
        this.mode = new Game("complete_example.dung", this.gc, this);
        break;
    }
  }

  /**
   * Handles key press events and calls corresponding method in Mode object.
   *
   * @param e - key event
   * @author profojak
   */
  private void keyPressHandler(KeyEvent e) {
    System.out.println(e.getCode());
    switch (e.getCode()) {
      case W: // fall through
      case UP:
        System.out.println("'-> up");
        this.mode.keyUp();
        break;
      case S: // fall through
      case DOWN:
        System.out.println("'-> down");
        this.mode.keyDown();
        break;
      case A: // fall through
      case LEFT:
        System.out.println("'-> left");
        this.mode.keyLeft();
        break;
      case D: // fall through
      case RIGHT:
        System.out.println("'-> right");
        this.mode.keyRight();
        break;
      case ESCAPE:
        System.out.println("'-> escape");
        this.mode.keyEscape();
        break;
      case ENTER:
        System.out.println("'-> enter");
        this.mode.keyEnter();
        break;
      case DELETE:
        System.out.println("'-> delete");
        this.mode.keyDelete();
        break;
      default:
        break;
    }
  }

  /** @see Application */
  @Override
  public void start(Stage stage) {
    // Window content
    Canvas canvas = new Canvas(1000, 525); 
    this.gc = canvas.getGraphicsContext2D();
    StackPane stack = new StackPane();
    stack.getChildren().add(canvas);
    Scene scene = new Scene(stack);

    scene.setOnKeyPressed(e -> { keyPressHandler(e); });
    this.mode = new MainMenu(this.gc, this);

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

