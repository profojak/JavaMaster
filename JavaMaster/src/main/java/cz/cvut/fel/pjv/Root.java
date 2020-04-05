package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.modes.MainMenu;
import cz.cvut.fel.pjv.modes.Game;
import cz.cvut.fel.pjv.modes.Editor;

public class Root {
  /**
   * Listens for key press and calls corresponding methods from MainMenu, Game or Editor classes.
   *
   * <p>This method is blocking. After each keypress and corresponding action, code execution stops
   * here and waits for next key press.
   */
  public void listen() {
    // While loop should be present here.
  }

  /**
   * Switches between MainMenu, Game and Editor objects.
   *
   * <p>This method must be called after MainMenu, Game or Editor close() method execution.
   *
   * @param mode - mode to switch to, can be MainMenu, Game or Editor
   */
  public void switchMode() {
  }

  /**
   * Entered when game launches, exiting this method quits game.
   */
  public static void main(String args[]) {
    System.out.println("Half-Life 3 would make more money than Steam. Change my mind.");
  }
}

