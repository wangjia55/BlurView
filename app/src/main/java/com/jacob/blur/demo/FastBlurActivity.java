package com.jacob.blur.demo;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jacob-wj on 2015/5/15.
 */
public class FastBlurActivity extends FragmentActivity {

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


    /**
     * 它所表述的原理为先通过缩小图片，使其丢失一些像素点，接着进行模糊化处理，然后再放大到原来尺寸。
     * 由于图片缩小后再进行模糊处理，需要处理的像素点和半径都变小，从而使得模糊处理速度加快。
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void blur(Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();
        float scaleFactor = 8;
        float radius = 2;

        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth()/scaleFactor),
                (int) (view.getMeasuredHeight()/scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft()/scaleFactor, -view.getTop()/scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        overlay = FastBlur.doBlur(overlay, (int)radius, true);
        view.setBackground(new BitmapDrawable(getResources(), overlay));
        textViewTime.setText(System.currentTimeMillis() - startMs + "ms");
    }


}
