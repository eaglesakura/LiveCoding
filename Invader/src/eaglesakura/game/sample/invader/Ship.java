package eaglesakura.game.sample.invader;

import android.graphics.Bitmap;

import com.eaglesakura.lib.android.device.SensorDevice;
import com.eaglesakura.lib.android.device.TouchDisplay;
import com.eaglesakura.lib.android.gles11.GLManager;
import com.eaglesakura.lib.math.Vector2;
import com.eaglesakura.lib.util.EagleUtil;

/**
 * 自機を管理する。
 * @author SAKURA
 *
 */
public class Ship extends GameObject {
    boolean dead = false;
    InvaderLooper looper = null;
    Vector2 moveVector = new Vector2();
    SensorDevice accSensor = null;

    public Ship(InvaderLooper looper, Bitmap bmp, GLManager glManager) {
        super(bmp, glManager);

        accSensor = new SensorDevice(looper.activity, SensorDevice.eSensorTypeAccel, SensorDevice.eSensorDelayUI);

        position.x = InvaderActivity.eGameWidth / 2;
        position.y = InvaderActivity.eGameHeight - bmp.getHeight() - 32;

        this.looper = looper;
    }

    void move() {
        accSensor.update();
        float accX = accSensor.getValue(SensorDevice.eValueTypeXAccel);
        if (Math.abs(accX) < 2.0f) {
            accX = 0;
        }
        moveVector.x -= accX;

        if (accX == 0) {
            final float eMulLevel = 0.6f;
            moveVector.x *= eMulLevel;
        }
        position.x += moveVector.x;
        position.x = EagleUtil.minmax(0, InvaderActivity.eGameWidth, position.x);
    }

    void shot() {
        Shot _shot = new Shot(this, null, null);
        SpaceField field = looper.field;

        field.addShot(_shot);
    }

    @Override
    public boolean isEnable() {
        return !dead;
    }

    /**
     * 死んだ
     */
    void onDead() {
        dead = true;
    }

    void checkShot() {
        TouchDisplay touch = looper.getTouchDisplay();

        if (touch.isTouchOnce()) {
            shot();
        }
    }

    @Override
    public void update() {
        move();
        checkShot();
    }
}
