package cz.cvut.fel.pjv.modes.draw;

import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;

/**
 * Implementation of DrawGameCombatRunnable: thread that draws combat.
 */
public class GameDrawCombatRunnable implements Runnable {
  private final ImageView monster, effect;

  /**
   * @param monster - monster to draw
   * @param effect - effect to draw
   */
  public GameDrawCombatRunnable(ImageView monster, ImageView effect) {
    this.monster = monster;
    this.effect = effect;
  }

  @Override
  public void run() {
    this.monster.setFitWidth(650);
    for (int i = 0; i <= 20; i++) {
      if (!Thread.currentThread().isInterrupted()) {
        try {
          this.effect.setOpacity(1 - i * 0.05);
          if (i == 2) {
            this.monster.setFitWidth(525);
          }
          Thread.sleep(60);
        } catch (Exception e) {
          return;
        }
      }
    }
  }
}

