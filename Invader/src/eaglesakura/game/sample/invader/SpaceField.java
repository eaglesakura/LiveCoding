package eaglesakura.game.sample.invader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.graphics.Bitmap;

import com.eaglesakura.lib.android.gles11.GLManager;
import com.eaglesakura.lib.android.gles11.ITexture;
import com.eaglesakura.lib.math.Vector3;

/**
 * ゲーム内の空間を管理する。
 * @author SAKURA
 *
 */
public class SpaceField {
    Ship player = null;
    List<Shot> shots = new ArrayList<Shot>();
    List<Enemy> enemys = new ArrayList<Enemy>();
    InvaderLooper looper = null;
    GLManager glManager = null;

    GameState state = GameState.Playing;

    /**
     * 現在のゲーム進行状況を示す。
     * @author SAKURA
     *
     */
    public enum GameState {
        /**
         * プレイ中
         */
        Playing,

        /**
         * ゲームオーバー
         */
        GameOver,

        /**
         * ゲームクリア
         */
        GameClear,
    }

    /**
     * 
     * @param looper
     */
    public SpaceField(InvaderLooper looper) {
        this.looper = looper;
        this.glManager = looper.glManager;

        //! 自機の画像読み込み
        {
            Bitmap bmp = looper.createBitmap("player.png", 128, 128);
            player = new Ship(looper, bmp, glManager);

            bmp.recycle();
        }

        //! 敵の画像読み込み
        {
            Bitmap bmp = looper.createBitmap("enemy_00.png", 64, 64);

            int eLineEnemy = 5;
            int eButai = 3;
            int eEnemySpace = 10;

            int y = 60;

            final int xBase = InvaderActivity.eGameWidth / (eLineEnemy + 1);

            for (int k = 0; k < eButai; ++k) {
                for (int i = 0; i < eLineEnemy; ++i) {
                    int x = xBase * (i + 1);
                    Vector3 pos = new Vector3(x, y, 0);

                    Enemy enemy = new Enemy(this, pos, bmp, glManager);
                    enemy.frame += (k * 3);
                    enemy.frame++;
                    enemys.add(enemy);
                }

                y += (bmp.getHeight() + eEnemySpace);
            }
            bmp.recycle();
        }
    }

    public void addShot(Shot shot) {
        shots.add(shot);
    }

    void checkShotIntersect() {
        for (Shot shot : shots) {
            //! あとでプレイヤーのショット判定

            for (Enemy enemy : enemys) {
                if (shot.isIntersect(enemy)) {
                    enemy.onShotIntersect(shot);
                    shot.onIntersect(enemy);
                }
            }
        }
    }

    void play() {
        //! 弾の動きを更新する
        {
            Iterator<Shot> itr = shots.iterator();
            while (itr.hasNext()) {
                Shot shot = itr.next();
                shot.update();
                if (!shot.isEnable()) {
                    shot.destroy();
                    itr.remove();
                }
            }
        }

        {
            Iterator<Enemy> itr = enemys.iterator();
            while (itr.hasNext()) {
                Enemy enemy = itr.next();
                enemy.update();
                if (!enemy.isEnable()) {
                    enemy.destroy();
                    itr.remove();
                }
            }
        }

        //! 敵が全滅したらゲームクリア
        if (enemys.size() == 0) {
            state = GameState.GameClear;
        } else if (!player.isEnable()) {
            state = GameState.GameOver;
        }
        player.update();
    }

    public void update() {

        if (state == GameState.Playing) {
            play();
        }

        //! 弾の衝突判定をして死亡フラグをつける
        checkShotIntersect();
    }

    public void draw() {
        glManager.beginSprite();
        {
            for (Shot shot : shots) {
                shot.draw(glManager);
            }

            for (Enemy enemy : enemys) {
                enemy.draw(glManager);
            }

            player.draw(glManager);

            if (state == GameState.GameClear) {
                glManager.drawImage(0, 0, InvaderActivity.eGameWidth, InvaderActivity.eGameWidth, 0, 0xffffffff, player.texture, 0, 0, player.texture
                        .getWidth(), player.texture.getHeight());
            } else if (state == GameState.GameOver) {
                ITexture texture = (enemys.get(0).texture);
                glManager.drawImage(0, 0, InvaderActivity.eGameWidth, InvaderActivity.eGameWidth, 0, 0xffffffff, texture, 0, 0, texture.getWidth(), texture
                        .getHeight());

            }
        }
        glManager.endSprite();

    }
}
