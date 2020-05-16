package cz.cvut.fel.pjv.modes.draw;

import cz.cvut.fel.pjv.Const;

import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;

/**
 * Implementation of DrawGameCombatRunnable: thread that draws combat.
 *
 * @author profojak
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
    // Draw monster bigger
    this.monster.setFitWidth(Const.WINDOW_HEIGHT + 125);
    for (int i = 0; i <= 20; i++) {
      if (!Thread.currentThread().isInterrupted()) {
        try {
          // Effect fade away
          this.effect.setOpacity(1 - i * 0.05);
          if (i == 4) {
            // Draw monster in normal size
            this.monster.setFitWidth(Const.WINDOW_HEIGHT);
          }
          Thread.sleep(60);
        } catch (Exception e) {
          return;
        }
      }
    }
  }
}

