package cz.cvut.fel.pjv.menu.layouts;

import cz.cvut.fel.pjv.menu.Button;

/**
 * Abstract class implementing menu functionality.
 */
public abstract class Layout {
  protected Button[] buttons = new Button[1];
  protected Integer active = 0;

  /**
   * Selects next button.
   */
  public void buttonNext() {
    active += 1;
    if (active >= buttons.length) {
      active = 0;
    }
  }

  /**
   * Selects previous button.
   */
  public void buttonPrevious() {
    active -= 1;
    if (active < 0) {
      active = buttons.length - 1;
    }
  }

  // Getters

  /**
   * Returns action of button.
   *
   * @param index - index of button to get action of
   * @return action string
   */
  public String getAction(Integer index) {
    return buttons[index].getAction();
  }

  /**
   * Returns index of currently selected button.
   *
   * @return index of selected button.
   */
  public Integer getActive() {
    return active;
  }

  /**
   * Returns number of buttons in menu.
   *
   * @return number of buttons
   */
  public Integer getCount() {
    return buttons.length;
  }
}

