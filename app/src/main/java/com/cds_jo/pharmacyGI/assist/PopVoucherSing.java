
package com.cds_jo.pharmacyGI.assist;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cds_jo.pharmacyGI.R;
import com.cds_jo.pharmacyGI.SqlHandler;

import java.io.File;
import java.io.FileOutputStream;


/**
 * Created by Hp on 07/02/2016.
 */

public class PopVoucherSing extends DialogFragment implements View.OnClickListener  {
    View form ;
    ImageButton add,cancel;
    Button btn_Save,btn_Clear,btn_Cancel;
    LinearLayout mContent;
    View view;
    signature mSignature;
    Bitmap bitmap;
    SqlHandler sqlHandler;
    float min = 0 ;

    ImageButton btn_filter_search ;
    String UnitNo ="";
    String UnitName ="";
    String str= "";
    ImageView imgSig;







    String StoredPath ;
    String DIRECTORY ;

    int SaveImg = 0 ;
    @Override
    public void onStart()
    {
        super.onStart();

        // safety check
        if (getDialog() == null)
            return;
        int dialogWidth =  WindowManager.LayoutParams.WRAP_CONTENT;//340; // specify a value here
        int dialogHeight =  WindowManager.LayoutParams.WRAP_CONTENT;//400; // specify a value here
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

    }


    @Override
    public View onCreateView(LayoutInflater inflater   , ViewGroup container  , Bundle savestate){

        form =inflater.inflate(R.layout.popordersig,container,false);
           getDialog().setTitle("توقيع العميل");

        String folder_main = "/Android/Cv_Images/Vo_Sig";

        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }

       DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/Android/Cv_Images/Vo_Sig/";
       StoredPath = DIRECTORY + getArguments().getString("OrdeNo")+".png";

        sqlHandler = new SqlHandler(getActivity());
        SaveImg = 0 ;


        btn_Save=(Button) form.findViewById(R.id.btn_Save);
        btn_Save.setOnClickListener(this);

        btn_Cancel=(Button) form.findViewById(R.id.btn_Cancel);
        btn_Cancel.setOnClickListener(this);

        btn_Clear=(Button) form.findViewById(R.id.btn_Clear);
        btn_Clear.setOnClickListener(this);



        mContent = (LinearLayout) form.findViewById(R.id.mContent);
        mSignature = new signature(getActivity(), null);
        mSignature.setBackgroundColor(Color.WHITE);

        // Dynamically generating Layout through java code
        mContent.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view = mContent;

        loadsig();


        return  form;
    }

   private  void loadsig(){
       imgSig = (ImageView) form.findViewById(R.id.imageView16);
       File imgFile = new File("//sdcard//Android/Cv_Images/Vo_Sig/"+getArguments().getString("OrdeNo").toString() +".png");
       try {
           if (imgFile.exists()) {
               Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
               imgSig.setImageBitmap(myBitmap);
           }
       }
       catch (Exception ex){}

   }
    public class signature extends View {

        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private Paint paint = new Paint();
        private Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public signature(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        public void save(View v, String StoredPath) {
            Log.v("log_tag", "Width: " + v.getWidth());
            Log.v("log_tag", "Height: " + v.getHeight());
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
            }
            Canvas canvas = new Canvas(bitmap);
            try {
                // Output the file
                FileOutputStream mFileOutStream = new FileOutputStream(StoredPath);
                v.draw(canvas);

                // Convert the output file to Image such as .png
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
                mFileOutStream.flush();
                mFileOutStream.close();

            } catch (Exception e) {
                Log.v("log_tag", e.toString());
            }

        }

        public void clear() {
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            SaveImg = 1 ;
            //mGetSign.setEnabled(true);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void debug(String string) {

            Log.v("log_tag", string);

        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }







    public void onClick(View v) {


         if (v == btn_Cancel) {
            this.dismiss();
        }
        if (v == btn_Clear) {
            mSignature.clear();
            SaveImg = 0 ;
            //mGetSign.setEnabled(false);
        }

        if (v == btn_Save) {



            String q = " SELECT * FROM RecVoucher   Where DocNo ='"+getArguments().getString("OrdeNo")+ "'";
            Cursor c1 = sqlHandler.selectQuery(q);
            if (c1 == null || c1.getCount() == 0) {

                AlertDialog alertDialog = new AlertDialog.Builder(
                        getActivity()).create();
                alertDialog.setTitle("التوقيع على سند القبض");
                alertDialog.setMessage("يجب تخزين سند القبض اولاّ");
                alertDialog.setIcon(R.drawable.delete);
                alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alertDialog.show();
                c1.close();
                return;

            }


                SaveSig();






        }
    }
public  void SaveSig(){
    view.setDrawingCacheEnabled(true);
    mSignature.save(view, StoredPath);
    Toast.makeText(getActivity(), "تمت عملية تخزين التوقيع بنجاح", Toast.LENGTH_SHORT).show();
    loadsig();
}
}


