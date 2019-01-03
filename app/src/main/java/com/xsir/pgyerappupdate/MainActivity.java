package com.xsir.pgyerappupdate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xsir.pgyerappupdate.library.PgyerApi;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PgyerApi.update(this);
    }
}
