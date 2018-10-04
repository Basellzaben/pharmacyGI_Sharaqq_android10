package Methdes;



import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by jobex-OS on 6/9/2015.
 */
public class MethodToUse {

    public static Typeface SetTFace(Context context) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "Hacen Tunisia.ttf");
        return font;
    }

    public static void ToastCreate(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
    public static boolean CheckConn(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
