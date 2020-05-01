package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.modes.*;

import java.io.File;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

public class Root extends Application {
  private final String WHITE = "\u001B[37m", RESET = "\u001B[0m";
  private static final Logger logger = Logger.getLogger(Root.class.getName());

  private Stage stage;
  private StackPane stack;
  private Mode mode;

  /**
   * Opens file chooser to choose files.
   *
   * @return selected file
   */
  public File getFile() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Choose dungeon file");
    return fileChooser.showOpenDialog(this.stage);
  }

  /**
   * Switches between MainMenu, Game and Editor objects.
   *
   * <p>This method must be called after MainMenu, Game or Editor close() method execution.
   *
   * @param mode - mode to switch to, can be MainMenu, Game or Editor
   */
  // TODO add About
  public void switchMode(String mode) {
    this.mode = null;
    switch (mode) {
      case "MainMenu":
        this.mode = new MainMenu(this.stack, this);
        break;
      case "Game":
        this.mode = new Game(this.stack, this);
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
    logger.info(WHITE + e.getCode() + RESET); // DEBUG
    switch (e.getCode()) {
      case K: case W: // fall through
      case UP:
        logger.info(WHITE + "'-> up" + RESET); // DEBUG
        this.mode.keyUp();
        break;
      case J: case S: // fall through
      case DOWN:
        logger.info(WHITE + "'-> down" + RESET); // DEBUG
        this.mode.keyDown();
        break;
      case H: case A: // fall through
      case LEFT:
        logger.info(WHITE + "'-> left" + RESET); // DEBUG
        this.mode.keyLeft();
        break;
      case L: case D: // fall through
      case RIGHT:
        logger.info(WHITE + "'-> right" + RESET); // DEBUG
        this.mode.keyRight();
        break;
      case ESCAPE:
        logger.info(WHITE + "'-> escape" + RESET); // DEBUG
        this.mode.keyEscape();
        break;
      case ENTER:
        logger.info(WHITE + "'-> enter" + RESET); // DEBUG
        this.mode.keyEnter();
        break;
      case DELETE:
        logger.info(WHITE + "'-> delete" + RESET); // DEBUG
        this.mode.keyDelete();
        break;
      default:
        break;
    }
  }

  /**
   * @see Application
   * @author profojak
   */
  @Override
  public void start(Stage stage) {
    // Window content
    this.stage = stage;
    this.stack = new StackPane();
    Scene scene = new Scene(stack, 1000, 525);

    scene.setOnKeyPressed(e -> {
      keyPressHandler(e);
    });
    switchMode("MainMenu");

    // Window
    stage.setScene(scene);
    stage.setResizable(false);
    stage.setTitle("Java Master: the Legend of Segfault");
    stage.show();
  }

  /** @see Application */
  @Override
  public void stop() {
    System.exit(0);
  }

  /**
   * Entered when game launches, exiting this method quits game.
   */
  public static void main(String args[]) {
    launch();
  }
}

