package com.jacob.blur.demo;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jacob-wj on 2015/5/15.
 */
public class RenderScriptBlurActivity extends FragmentActivity {

    private ImageView imageView;
    private TextView textViewTime;
    private TextView textViewBlur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blur);

        imageView = (ImageView) findViewById(R.id.picture);
        textViewBlur = (TextView) findViewById(R.id.text_view_blur);
        textViewTime = (TextView) findViewById(R.id.text_view_time);

        applyBlur();

    }

    public void applyBlur(){
        imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                imageView.buildDrawingCache();

                Bitmap bmp = imageView.getDrawingCache();
                blur(bmp,textViewBlur);
                return true;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void blur(Bitmap bmp, TextView textViewBlur) {
        long statTime = System.currentTimeMillis();
        // Radius out of range (0 < r <= 25).
        float radio = 20;

        Bitmap mask = Bitmap.createBitmap(textViewBlur.getMeasuredWidth(),
                textViewBlur.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(mask);
        canvas.translate(-textViewBlur.getLeft(),-textViewBlur.getTop());
        canvas.drawBitmap(bmp,0,0,null);

        //核心代码--------------------------------------
        RenderScript rs = RenderScript.create(this);
        Allocation allocation  = Allocation.createFromBitmap(rs,mask);
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs,allocation.getElement());
        blur.setInput(allocation);
        blur.setRadius(radio);
        blur.forEach(allocation);
        allocation.copyTo(mask);

        textViewBlur.setBackground(new BitmapDrawable(getResources(),mask));
        rs.destroy();

        long end = System.currentTimeMillis();
        Log.e("TAG",""+(end-statTime));
        textViewTime.setText((end-statTime)+" ms");
    }
}
