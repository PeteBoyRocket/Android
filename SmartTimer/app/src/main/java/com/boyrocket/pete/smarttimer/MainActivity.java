package com.boyrocket.pete.smarttimer;

import android.app.Activity;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Timer;

public class MainActivity extends Activity {

    NumberPicker periodNumberPicker;
    NumberPicker incrementNumberPicker;
    NumberPicker startNumberPicker;
    NumberPicker stopNumberPicker;
    TextView timerTextView;
    private int currentMSecTick;
    private int endBpm;
    private int increment;
    private int period;
    private int currentBpm;
    private CountDownTimer countDownTimer;
    private ToneGenerator toneG;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerTextView = (TextView) findViewById(R.id.info_message);

        periodNumberPicker = (NumberPicker) findViewById(R.id.period_NumberPicker);
        periodNumberPicker.setMinValue(0);
        periodNumberPicker.setMaxValue(24);

        String[] values = new String[25];
        for(int i=0; i < values.length; i++){
            values[i]=Integer.toString((i + 1)*5);
        }

        periodNumberPicker.setDisplayedValues(values);
        periodNumberPicker.setValue(5);

        incrementNumberPicker = (NumberPicker) findViewById(R.id.increment_NumberPicker);
        incrementNumberPicker.setMinValue(1);
        incrementNumberPicker.setMaxValue(30);
        incrementNumberPicker.setValue(15);

        startNumberPicker = (NumberPicker) findViewById(R.id.start_NumberPicker);
        startNumberPicker.setMinValue(0);
        startNumberPicker.setMaxValue(24);

        String[] startValues = new String[25];
        for(int i=0; i < startValues.length; i++){
            startValues[i]=Integer.toString((i + 1)*5);
        }

        startNumberPicker.setDisplayedValues(startValues);
        startNumberPicker.setValue(5);

        stopNumberPicker = (NumberPicker) findViewById(R.id.stop_NumberPicker);
        stopNumberPicker.setMinValue(0);
        stopNumberPicker.setMaxValue(24);

        String[] stopValues = new String[25];
        for(int i=0; i < stopValues.length; i++){
            stopValues[i]=Integer.toString((i + 1)*5);
        }

        stopNumberPicker.setDisplayedValues(stopValues);
        stopNumberPicker.setValue(5);

        toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void timerStopStart(View view) {
        Button b = (Button) view;

        if (b.getText().equals("stop")) {
            b.setText("start");

            countDownTimer.cancel();
            return;
        }

        b.setText("stop");

        period = (periodNumberPicker.getValue() + 1) * 5000;
        currentBpm = (startNumberPicker.getValue() + 1) * 5;
        currentMSecTick = 60000 / currentBpm;
        endBpm = (stopNumberPicker.getValue() + 1) * 5;
        increment = incrementNumberPicker.getValue();

        StartTimer();
    }

    private void StartTimer() {

        countDownTimer = getCountDownTimer();
        countDownTimer.start();
    }

    private CountDownTimer getCountDownTimer() {
        return new CountDownTimer(period, currentMSecTick) {

            public void onTick(long millisUntilFinished) {
                timerTextView.setText("Current BPM " + currentBpm + ", seconds remaining: " + millisUntilFinished / 1000);
                toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
            }

            public void onFinish() {
                currentBpm += increment;
                currentMSecTick = 60000 / currentBpm;

                if (currentBpm > endBpm) {
                    timerTextView.setText("done!");
                    return;
                }

                countDownTimer = getCountDownTimer();
                countDownTimer.start();
            }
        };
    }

    @Override
    public void onPause() {
        super.onPause();
      //  timerHandler.removeCallbacks(timerRunnable);
        Button b = (Button) findViewById(R.id.stopStartButton);
        b.setText("start");
    }
}
