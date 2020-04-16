package cz.cvut.fel.pjv.menu;

import cz.cvut.fel.pjv.menu.Button;

public abstract class Layout {
  protected Button[] buttons = new Button[1];
  protected Integer active = 0;

  public void buttonNext() {
    active += 1;
    if (active >= buttons.length) {
      active = 0;
    }
  }

  public void buttonPrevious() {
    active -= 1;
    if (active < 0) {
      active = buttons.length - 1;
    }
  }

  public String getAction(Integer index) {
    return buttons[index].getAction();
  }

  public Integer getActive() {
    return active;
  }

  public Integer getCount() {
    return buttons.length;
  }
}

