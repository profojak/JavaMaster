package cz.cvut.fel.pjv.inventory.items;

/**
 * Abstract class Item implementing basic functionality of item classes.
 */
public abstract class Item {
    protected String name, sprite;

    public String getName() { return name; }

    public String getSprite() { return sprite; }
}
