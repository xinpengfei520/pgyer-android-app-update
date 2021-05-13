package com.xsir.pgyerappupdate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xsir.pgyerappupdate.library.PgyerApi;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PgyerApi.checkUpdate(this);
    }
}
