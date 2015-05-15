package com.jacob.blur.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;


public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void useRenderScript(View view){
        Intent intent = new Intent(this,RenderScriptBlurActivity.class);
        startActivity(intent);
    }

    public void fastBlur(View view){
        Intent intent = new Intent(this,FastBlurActivity.class);
        startActivity(intent);
    }
}
