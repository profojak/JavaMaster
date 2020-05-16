package cz.cvut.fel.pjv.inventory.items;

/**
 * Abstract class Item implementing basic functionality of item classes.
 */
public abstract class Item {
  protected String name, sprite;

  /**
   * Gets name of item.
   *
   * @return name of item
   */
  public String getName() {
    return name;
  }

  /**
   * Gets sprite of item.
   *
   * @return sprite of item
   */
  public String getSprite() {
    return sprite;
  }
}

