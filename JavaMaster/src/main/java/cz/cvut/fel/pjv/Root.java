package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.modes.Mode;
import cz.cvut.fel.pjv.modes.MainMenu;
import cz.cvut.fel.pjv.modes.Game;
import cz.cvut.fel.pjv.modes.Editor;

public class Root {
  private static Boolean isListening = true;
  private static Mode mode;

  /**
   * Listens for key press and calls corresponding methods from MainMenu, Game or Editor classes.
   *
   * <p>This method is blocking. After each keypress and corresponding action, code execution stops
   * here and waits for next key press.
   */
  private static void listen() {
  } /**
   * Reads commands from console, intended for debugging purposes.
   *
   * @author profojak
   */
  private static void consoleListen() {
    while (isListening) {
      System.out.print(">>> ");
      String command = System.console().readLine();

      switch (command) {
        // Help
        case "help": // Fall through
        case "h":
          System.out.println("help, h        - shows this help");
          System.out.println("left, l        - calls keyLeft()");
          System.out.println("right, r       - calls keyRight()");
          System.out.println("up, u          - calls keyUp()");
          System.out.println("down, d        - calls keyDown()");
          System.out.println("enter          - calls keyEnter()");
          System.out.println("delete, del    - calls keyDelete()");
          System.out.println("escape, esc    - calls keyEscape()");
          System.out.println("quit, q        - quits");
          break;
        // Left
        case "left": // Fall through
        case "l":
          break;
        // Right
        case "right": // Fall through
        case "r":
          break;
        // Up
        case "up": // Fall through
        case "u":
          break;
        // Down
        case "down": // Fall through
        case "d":
          break;
        // Enter
        case "enter":
          break;
        // Delete
        case "delete": // Fall through
        case "del":
          break;
        // Escape
        case "escape": // Fall through
        case "esc": 
          break;
        // Quit
        case "quit": // Fall through
        case "q":
          isListening = false;
          break;
        default:
          System.out.println("Unknown command! Type help to see all commands.");
          break;
      }
    }
  }

  /**
   * Switches between MainMenu, Game and Editor objects.
   *
   * <p>This method must be called after MainMenu, Game or Editor close() method execution.
   *
   * @param mode - mode to switch to, can be MainMenu, Game or Editor
   */
  public static void switchMode() {
  }

  /**
   * Entered when game launches, exiting this method quits game.
   */
  public static void main(String args[]) {
    mode = new Game("complete_example.dung");
    consoleListen();
  }
}

