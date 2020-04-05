package cz.cvut.fel.pjv.modes;

abstract public class Mode {
  /**
   * Destructor.
   *
   * <p>This method handles proper unloading of class instance. It must be called before deleting.
   */
  abstract public void close();

  // Key methods
  
  /**
   * Called when up key is pressed.
   */
  abstract public void keyUp();

  /**
   * Called when down key is pressed.
   */
  abstract public void keyDown();

  /**
   * Called when left key is pressed.
   */
  abstract public void keyLeft();

  /**
   * Called when right key is pressed.
   */
  abstract public void keyRight();

  /**
   * Called when escape key is pressed.
   */
  abstract public void keyEscape();

  /**
   * Called when enter key is pressed.
   */
  abstract public void keyEnter();

  /**
   * Called when delete key is pressed.
   */
  abstract public void keyDelete();
}

