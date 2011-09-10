package eaglesakura.game.sample.invader;

import android.graphics.Bitmap;

import com.eaglesakura.lib.android.gles11.GLManager;

public class Shot extends GameObject {
    GameObject parent = null;
    boolean dead = false;
    float speed = 30.0f;

    /**
     * 
     * @param bmp
     * @param glManager
     */
    public Shot(GameObject parent, Bitmap bmp, GLManager glManager) {
        super(bmp, glManager);

        this.position.set(parent.getPosition());
    }

    @Override
    public boolean isEnable() {
        return position.y > -50 && !dead;
    }

    /**
     * 弾が当たっていたらtrueを返す。
     * @param obj
     * @return
     */
    public boolean isIntersect(GameObject obj) {
        return position.length(obj.position) < 40;
    }

    /**
     * 弾が敵にあたったことを通知する
     * @param obj
     */
    public void onIntersect(GameObject obj) {
        dead = true;
    }

    @Override
    public void update() {
        position.y -= speed;
    }

    @Override
    public void draw(GLManager glManager) {
        final int eTexWidth = 30;
        final int eTexHeight = 80;

        int x = (int) position.x - (eTexWidth / 2);
        int y = (int) position.y - (eTexHeight / 2);

        glManager.fillRect(x, y, eTexWidth, eTexHeight, 0, 0xff0000ff);
    }
}
