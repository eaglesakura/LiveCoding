package eaglesakura.game.sample.invader;

import android.graphics.Bitmap;

import com.eaglesakura.lib.android.gles11.BmpTexture;
import com.eaglesakura.lib.android.gles11.GLManager;
import com.eaglesakura.lib.android.gles11.ITexture;
import com.eaglesakura.lib.math.Vector3;
import com.eaglesakura.lib.util.EagleUtil;

/**
 * ゲーム内の１キャラクターを管理する基底クラス
 * @author SAKURA
 *
 */
public abstract class GameObject {
    int frame = 0;
    Vector3 position = new Vector3();
    ITexture texture = null;

    public GameObject(Bitmap bitmap, GLManager glManager) {

        if (bitmap != null) {
            texture = new BmpTexture(bitmap, glManager);
        }
    }

    /**
     * 現在の中心位置を取得する
     * @return
     */
    public Vector3 getPosition() {
        return position;
    }

    /**
     * 自機の位置更新
     */
    public abstract void update();

    /**
     * 弾が無効になったらfalseを返す
     * @return
     */
    public boolean isEnable() {
        return true;
    }

    /**
     * 消滅させられることを通知する。
     */
    public void destroy() {
        EagleUtil.log("destroy!!! : " + this);
    }

    /**
     * 
     * @param glManager
     */
    public void draw(GLManager glManager) {
        if (texture == null) {
            return;
        }

        final int eTexWidth = texture.getWidth();
        final int eTexHeight = texture.getHeight();

        int x = (int) position.x - (eTexWidth / 2);
        int y = (int) position.y - (eTexHeight / 2);

        glManager.drawImage(x, y, eTexWidth, eTexHeight, 0, 0xffffffff, texture, 0, 0, eTexWidth, eTexHeight);
    }
}
