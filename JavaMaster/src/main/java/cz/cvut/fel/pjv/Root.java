package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.modes.*;

import java.io.File;
import java.util.logging.Logger;
import java.util.Optional;

import javafx.application.Application;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.control.TextInputDialog;

/**
 * Class implementing Root, the main object.
 *
 * <p>Root's role is to hold mode objects, switch them and get user input.
 *
 * @author profojak
 */
public class Root extends Application {
  private static final Logger logger = Logger.getLogger(Root.class.getName());

  private Stage stage;
  private StackPane stack;
  private Mode mode;
  private File saveFile = null;

  /**
   * Opens file chooser to choose files.
   *
   * @return selected file
   * @author povolji2
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
    saveFile = fileChooser.showOpenDialog(this.stage);
    return saveFile;
  }

  /**
   * Opens text input dialog to get text input.
   *
   * @param state - which information is requested
   * @param text - which text is displayed as default input option
   * @return text input
   */
  public String getInputDialog(Const.State state, String text) {
    TextInputDialog dialog = null;
    switch (state) {
      /* Load existing dungeon */
      case LOAD:
        dialog = new TextInputDialog(text);
        dialog.setContentText("Name of dungeon:");
        dialog.setHeaderText(".dung is not required.");
        break;
      /* Room texture */
      case ROOM:
        if (text != null) {
          dialog = new TextInputDialog(text);
        } else {
          dialog = new TextInputDialog("default.png");
        }
        dialog.setContentText("Room texture:");
        dialog.setHeaderText(null);
        break;
      /* Story before */
      case STORY_BEFORE:
        dialog = new TextInputDialog(text);
        dialog.setContentText("Story before:");
        dialog.setHeaderText("Click on cancel to delete story from the room.\nNote that deleting this story also deletes 'story after'.");
        break;
      /* Story after */
      case STORY_AFTER:
        dialog = new TextInputDialog(text);
        dialog.setContentText("Story after:");
        dialog.setHeaderText("Click on cancel to delete story from the room.");
        break;
      /* Monster texture */
      case MONSTER:
        if (text != null) {
          dialog = new TextInputDialog(text);
        } else {
          dialog = new TextInputDialog("goblin.png");
        }
        dialog.setContentText("Monster texture:");
        dialog.setHeaderText(null);
        break;
      /* Loot type */
      case LOOT:
        dialog = new TextInputDialog(text);
        dialog.setContentText("Weapon texture:");
        dialog.setHeaderText("Use 'potion' or 'bomb' for consumables.\nClick on cancel to delete loot from the room.");
        break;
      /* Weapon texture */
      case VICTORY:
        dialog = new TextInputDialog(text);
        dialog.setContentText("Weapon texture:");
        dialog.setHeaderText(null);
        break;
      /* Loot count */
      case INVENTORY:
        dialog = new TextInputDialog(text);
        dialog.setContentText("Loot count:");
        dialog.setHeaderText(null);
        break;
      /* HP */
      case HP:
        dialog = new TextInputDialog(text);
        dialog.setContentText("HP:");
        dialog.setHeaderText(null);
        break;
      /* Damage */
      case DAMAGE:
        dialog = new TextInputDialog(text);
        dialog.setContentText("Damage:");
        dialog.setHeaderText(null);
        break;
      /* Next dungeon name */
      case MENU:
        dialog = new TextInputDialog(text);
        dialog.setContentText("Next dungeon:");
        dialog.setHeaderText(".dung is not required.\nClick on cancel to mark this dungeon as the final one.");
        break;
      default:
        dialog = new TextInputDialog();
        dialog.setHeaderText(null);
        break;
    }
    dialog.setTitle("Java Master: Editor Input Dialog");

    Optional<String> result = dialog.showAndWait();
    if (result.isPresent()) {
      return result.get();
    }
    return null;
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
        this.mode = new Game(this.stack, this, saveFile);
        break;
      case Const.MENU_EDITOR:
        this.mode = new Editor(this.stack, this);
        break;
    }
  }

  /**
   * Handles key press events and calls corresponding method in Mode object.
   *
   * @param e - key event
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

