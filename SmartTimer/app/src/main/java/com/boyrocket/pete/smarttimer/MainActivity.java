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
    private Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerTextView = (TextView) findViewById(R.id.info_message);
        button = (Button) findViewById(R.id.stopStartButton);

        InitialisePeriodNumberPicker();

        InitialiseIncrementNumberPicker();

        InitialiseStartNumberPicker();

        InitialiseStopNumberPicker();

        toneG = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
    }

    private void InitialiseStopNumberPicker() {
        stopNumberPicker = (NumberPicker) findViewById(R.id.stop_NumberPicker);
        InitialiseBpmNumberPicker(stopNumberPicker);

        stopNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal < startNumberPicker.getValue())
                {
                    startNumberPicker.setValue(newVal);
                }
            }
        });
    }

    private void InitialiseStartNumberPicker() {
        startNumberPicker = (NumberPicker) findViewById(R.id.start_NumberPicker);
        InitialiseBpmNumberPicker(startNumberPicker);

        startNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal > stopNumberPicker.getValue())
                {
                    stopNumberPicker.setValue(newVal);
                }
            }
        });
    }

    private void InitialiseBpmNumberPicker(NumberPicker numberPicker) {
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(24);

        String[] startValues = new String[25];
        for(int i=0; i < startValues.length; i++){
            startValues[i]= Integer.toString((i + 1)*5 + 35);
        }

        numberPicker.setDisplayedValues(startValues);
        numberPicker.setValue(5);
    }

    private void InitialiseIncrementNumberPicker() {
        incrementNumberPicker = (NumberPicker) findViewById(R.id.increment_NumberPicker);
        incrementNumberPicker.setMinValue(1);
        incrementNumberPicker.setMaxValue(30);
        incrementNumberPicker.setValue(15);
    }

    private void InitialisePeriodNumberPicker() {
        periodNumberPicker = (NumberPicker) findViewById(R.id.period_NumberPicker);
        periodNumberPicker.setMinValue(0);
        periodNumberPicker.setMaxValue(24);

        String[] values = new String[25];
        for(int i=0; i < values.length; i++){
            values[i]=Integer.toString((i + 1)*5);
        }

        periodNumberPicker.setDisplayedValues(values);
        periodNumberPicker.setValue(5);
    }

    public void timerStopStart(View view) {

        if (button.getText().equals("stop")) {
            button.setText("start");

            countDownTimer.cancel();
            return;
        }

        button.setText("stop");

        period = (periodNumberPicker.getValue() + 1) * 5000;
        currentBpm = (startNumberPicker.getValue() + 1) * 5 + 35;
        currentMSecTick = 60000 / currentBpm;
        endBpm = (stopNumberPicker.getValue() + 1) * 5 + 35;
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
                    button.setText("start");
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
