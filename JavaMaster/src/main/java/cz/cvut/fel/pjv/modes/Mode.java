package cz.cvut.fel.pjv.modes;

public interface Mode {
  /**
   * Destructor.
   *
   * <p>This method handles proper unloading of class instance. It must be called before deleting.
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

