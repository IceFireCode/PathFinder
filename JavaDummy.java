package nl.ns.pathfinder;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by paulvc on 20-12-17.
 */

public class JavaDummy {

    public void doSomething(){

    }

    public static int getScreenHeightInPixels(Context context){
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        return height;
    }

}
