package cz.cvut.fel.pjv.menu.layouts;

import cz.cvut.fel.pjv.Const;
import cz.cvut.fel.pjv.menu.Button;

/**
 * Implementation of Menu: menu layout used when asking player whether to change weapon.
 *
 * @see Layout
 */
public class NewWeapon extends Layout {
    public NewWeapon() {
        this.buttons = new Button[2];
        this.buttons[0] = new Button(Const.MENU_TAKE);
        this.buttons[1] = new Button(Const.MENU_KEEP);
    }
}
