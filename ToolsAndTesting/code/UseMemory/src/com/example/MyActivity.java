package com.example;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MyActivity extends Activity {


    private ArrayList<StringBuffer> _lists;

        private StringBuffer _stringBuffer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        _lists = new ArrayList<StringBuffer>();
        Button button = (Button) findViewById(R.id.doWorkButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                _stringBuffer = new StringBuffer();
                for (int i = 1; i < 1000000000; i++) {
                    _lists.add(new StringBuffer("askdjalksdjla;ksdnl;kasdncl;kansdcl;kansdckl;nasdl;kcnasl;dknc;alskdncasdlkncoineopiwnf0pni23f0in230ni230oin2o0insakjdnfajsdnfoasbdgoasbdgoasbdugoasudbf"));
                    Log.d("UseMemory", "Count: " + i);
                    _stringBuffer.append("Some text,");
                }
            }
        }
        );
    }
}
