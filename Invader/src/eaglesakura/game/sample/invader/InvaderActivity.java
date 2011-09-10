package eaglesakura.game.sample.invader;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.eaglesakura.lib.android.thread.ILoopManager;
import com.eaglesakura.lib.android.thread.LooperHandler;
import com.eaglesakura.lib.android.util.UtilActivity;
import com.eaglesakura.lib.android.util.UtilBridgeAndroid;
import com.eaglesakura.lib.android.view.OpenGLView;
import com.eaglesakura.lib.util.EagleUtil;

public class InvaderActivity extends Activity {
    OpenGLView glView = null;

    ILoopManager loopManager = null;
    InvaderLooper looper = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UtilActivity.setOrientation(this, true);
        EagleUtil.init(new UtilBridgeAndroid("SAMPLE"));

        glView = new OpenGLView(this);
        setContentView(glView);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (loopManager != null) {
            loopManager.loopPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (loopManager != null) {
            loopManager.loopResume();
        } else {
            looper = new InvaderLooper(this);
            loopManager = new LooperHandler(new Handler(), looper);

            loopManager.addSurface(glView);
            loopManager.startLoop();
        }
    }

    public static final int eGameWidth = 480;
    public static final int eGameHeight = 800;
}