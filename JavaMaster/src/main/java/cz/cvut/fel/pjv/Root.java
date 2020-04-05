package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.modes.MainMenu;
import cz.cvut.fel.pjv.modes.Game;
import cz.cvut.fel.pjv.modes.Editor;

public class Root {
  /*
   * Listens for a keypress.
   */
  public void listen() {
    // While loop should be present here.
  }

  /*
   * Switches between MainMenu, Game and Editor.
   */
  public void switchMode() {
  }

  /*
   * Entered when the game launches. Exiting this method quits the game.
   */
  public static void main(String args[]) {
    System.out.println("Half-Life 3 would make more money than Steam. Change my mind.");
  }
}

