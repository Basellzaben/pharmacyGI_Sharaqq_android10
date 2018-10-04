package Methdes;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by user on 10/27/2016.
 */
public class MyTextView_Digital extends TextView {
    public MyTextView_Digital(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"digital7.ttf"));

    }
}
