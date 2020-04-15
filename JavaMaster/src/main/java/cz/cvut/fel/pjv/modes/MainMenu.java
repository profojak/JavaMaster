package cz.cvut.fel.pjv.modes;

import cz.cvut.fel.pjv.Root;
import cz.cvut.fel.pjv.modes.draw.Draw;
import cz.cvut.fel.pjv.modes.draw.MainMenuDraw;

import javafx.scene.canvas.GraphicsContext;

/** @see Mode */
public class MainMenu implements Mode {
  private Root root;
  private Draw draw;

  public MainMenu(GraphicsContext gc, Root root) {
    this.root = root;
    this.draw = new MainMenuDraw(gc, this);
  }

  public void close() {
  }

  // Key methods

  public void keyUp() {
  }

  public void keyDown() {
  }

  public void keyLeft() {
  }

  public void keyRight() {
  }

  public void keyEscape() {
    this.root.switchMode("Game");
  }

  public void keyEnter() {
  }

  public void keyDelete() {
  }

  // GUI

  /** To be implemented. */
  private void redraw() {
  }
}

