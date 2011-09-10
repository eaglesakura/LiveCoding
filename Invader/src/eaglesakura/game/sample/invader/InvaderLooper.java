package eaglesakura.game.sample.invader;

import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.eaglesakura.lib.android.gles11.GLManager;
import com.eaglesakura.lib.android.thread.ILooper;

public class InvaderLooper extends ILooper {

    InvaderActivity activity = null;
    GLManager glManager = null;
    SpaceField field = null;

    public InvaderLooper(InvaderActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onInitialize() {
        glManager = activity.glView.getGLManager();

        //! GLの初期化
        glManager.initGL();

        //! 必要なリソースをかき集める
        {
            field = new SpaceField(this);
        }
    }

    /**
     * 画像を指定サイズで読み込む
     * @param fileName
     * @param width
     * @param height
     * @return
     */
    public Bitmap createBitmap(String fileName, int width, int height) {
        try {
            InputStream is = activity.getAssets().open(fileName);
            Bitmap temp = BitmapFactory.decodeStream(is);
            is.close();

            Bitmap scaling = Bitmap.createScaledBitmap(temp, width, height, true);
            temp.recycle();

            return scaling;
        } catch (Exception e) {

        }

        return null;
    }

    @Override
    public void onLoop() {
        glManager.setSurfaceSize(480, 800);
        glManager.clearColorRGBA(0.0f, 1.0f, 1.0f, 1.0f);
        glManager.clear(GL10.GL_COLOR_BUFFER_BIT);

        //! 毎フレームの処理
        {
            getTouchDisplay().update();
            field.update();
        }

        //! 毎フレームの描画
        {
            field.draw();
        }

        glManager.swapBuffers();
    }

    @Override
    public void onFinalize() {
    }

}
