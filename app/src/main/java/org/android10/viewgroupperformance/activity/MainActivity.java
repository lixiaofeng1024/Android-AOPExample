package org.android10.viewgroupperformance.activity;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import org.android10.gintonic.annotation.CollectCountMsg;
import org.android10.gintonic.annotation.CollectSpentTimeAsync;
import org.android10.gintonic.annotation.CollectSpentTimeSync;
import org.android10.gintonic.annotation.CollectValueMsg;
import org.android10.gintonic.annotation.DebugTrace;
import org.android10.viewgroupperformance.R;

public class MainActivity extends Activity {
    private Switch countSwitch;
    private Button countBt;
    private Button spentBt;
    private EditText valueEt;
    private Button valueBt;
    private boolean isEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countSwitch = (Switch) findViewById(R.id.count_switch);
        countBt = (Button) findViewById(R.id.count_bt);
        spentBt = (Button) findViewById(R.id.spent_bt);
        valueEt = (EditText) findViewById(R.id.value_et);
        valueBt = (Button) findViewById(R.id.value_bt);

        countBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countSwitch.isChecked()) {
                    onSuccess();
                } else {
                    onFailure();
                }
            }
        });
        spentBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEnd) {
                    onSpentStart();
                } else {
                    onSpentEnd();
                }
                isEnd = !isEnd;
            }
        });
        valueBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = valueEt.getEditableText().toString();
                if (s.length() == 0) {
                    return;
                }
                onValue(Float.valueOf(s));
            }
        });
    }

    @CollectCountMsg(target = "CollectCountMsg", isSuccess = true)
    public void onSuccess() {

    }

    @CollectCountMsg(target = "CollectCountMsg", isSuccess = false)
    @DebugTrace
    public void onFailure() {

    }

    @CollectSpentTimeSync(target = "CollectSpentTimeSync")
    @CollectSpentTimeAsync(target = "CollectSpentTimeAsync", isEndPoint = false)
    public void onSpentStart() {

    }

    @CollectSpentTimeSync(target = "CollectSpentTimeSync")
    @CollectSpentTimeAsync(target = "CollectSpentTimeAsync", isEndPoint = true)
    public void onSpentEnd() {

    }

    @CollectValueMsg(target = "CollectValueMsg")

    public float onValue(float value) {
        return value;
    }
}
