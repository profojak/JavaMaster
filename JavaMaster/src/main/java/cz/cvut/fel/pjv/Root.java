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

    FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("DUNG files (*.dung)", "*.dung");
    fileChooser.getExtensionFilters().add(extensionFilter);

    File saveDirectory = new File(Const.SAVE_PATH);
    logger.info(Const.LOG_WHITE + "Can read saves: " + saveDirectory.canRead() + Const.LOG_RESET);
    logger.info(Const.LOG_WHITE + "Path to saves: " + saveDirectory.getPath() + Const.LOG_RESET);
    logger.info(Const.LOG_WHITE + "Absolute path to saves: " + saveDirectory.getAbsolutePath() + Const.LOG_RESET);

    fileChooser.setInitialDirectory(saveDirectory.exists() ? saveDirectory : null);

    logger.info(Const.LOG_WHITE + "Initial directory: " + fileChooser.getInitialDirectory() + Const.LOG_RESET);

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
    if (this.mode != null) {
      this.mode.close();
    }
    this.mode = null;
    switch (mode) {
      case Const.MENU_MAINMENU:
        this.mode = new MainMenu(this.stack, this);
        break;
      case Const.MENU_GAME:
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
    logger.info(Const.LOG_WHITE + e.getCode() + Const.LOG_RESET); // DEBUG
    switch (e.getCode()) {
      case K: case W: // fall through
      case UP:
        logger.info(Const.LOG_WHITE + "'-> up" + Const.LOG_RESET); // DEBUG
        this.mode.keyUp();
        break;
      case J: case S: // fall through
      case DOWN:
        logger.info(Const.LOG_WHITE + "'-> down" + Const.LOG_RESET); // DEBUG
        this.mode.keyDown();
        break;
      case H: case A: // fall through
      case LEFT:
        logger.info(Const.LOG_WHITE + "'-> left" + Const.LOG_RESET); // DEBUG
        this.mode.keyLeft();
        break;
      case L: case D: // fall through
      case RIGHT:
        logger.info(Const.LOG_WHITE + "'-> right" + Const.LOG_RESET); // DEBUG
        this.mode.keyRight();
        break;
      case ESCAPE:
        logger.info(Const.LOG_WHITE + "'-> escape" + Const.LOG_RESET); // DEBUG
        this.mode.keyEscape();
        break;
      case ENTER:
        logger.info(Const.LOG_WHITE + "'-> enter" + Const.LOG_RESET); // DEBUG
        this.mode.keyEnter();
        break;
      case DELETE:
        logger.info(Const.LOG_WHITE + "'-> delete" + Const.LOG_RESET); // DEBUG
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
    switchMode(Const.MENU_MAINMENU);

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

