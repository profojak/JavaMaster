package cz.cvut.fel.pjv.menu;

/**
 * Implementation of Button: selectable button used in menus.
 *
 * <p>Only purpose of Button is to hold action that is returned when Button is activated.
 */
public class Button {
  private final String action;

  public Button(String action) {
    this.action = action;
  }

  /**
   * Returns action of button.
   *
   * @return action string
   */
  public String getAction() {
    return this.action;
  }
}

