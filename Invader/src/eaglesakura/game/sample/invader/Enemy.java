package eaglesakura.game.sample.invader;

import android.graphics.Bitmap;

import com.eaglesakura.lib.android.gles11.GLManager;
import com.eaglesakura.lib.math.Vector3;

/**
 * 敵を管理する。
 * @author SAKURA
 *
 */
public class Enemy extends GameObject {
    boolean dead = false;
    SpaceField field = null;

    final int eMoveInterval = 15;
    final float eLineHeight = 20;

    public Enemy(SpaceField field, Vector3 startPos, Bitmap bitmap, GLManager glManager) {
        super(bitmap, glManager);

        this.field = field;
        position.set(startPos);
    }

    @Override
    public boolean isEnable() {
        return !dead;
    }

    public void onShotIntersect(Shot shot) {
        dead = true;
    }

    @Override
    public void update() {
        if (frame % eMoveInterval == 0) {
            position.y += eLineHeight;
        }
        ++frame;

        /*
         */
        if (position.y > field.player.position.y) {
            field.player.onDead();
        }
    }
}
