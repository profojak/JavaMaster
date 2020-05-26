package cz.cvut.fel.pjv.modes;

/**
 * Interface of Mode, definening key press methods that need to be implemented in mode classes.
 *
 * <p>Mode is a class that handles key presses and controlls behavior of this applicatoin. Typical
 * Mode class is Game: this class handles gameplay.
 */
public interface Mode {
  /**
   * Called when Mode is closed.
   */
  public void close();

  // Key methods
  
  /**
   * Called when up key is pressed.
   */
  public void keyUp();

  /**
   * Called when down key is pressed.
   */
  public void keyDown();

  /**
   * Called when left key is pressed.
   */
  public void keyLeft();

  /**
   * Called when right key is pressed.
   */
  public void keyRight();

  /**
   * Called when escape key is pressed.
   */
  public void keyEscape();

  /**
   * Called when enter key is pressed.
   */
  public void keyEnter();

  /**
   * Called when delete key is pressed.
   */
  public void keyDelete();
}

