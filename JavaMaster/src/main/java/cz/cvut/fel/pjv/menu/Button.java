package cz.cvut.fel.pjv.menu;

public class Button {
  private String action;

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

