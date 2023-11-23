package com.example.myloginapp;

import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SecondActivity extends AppCompatActivity {

    private DatabaseHelper2 dbHelper;
    private TextView heartRateTextView;
    private TextView respRateTextView;
    private TextView timerTextView;
    private static final long RESULT_DELAY = 45000;
    private VideoView videoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        dbHelper = new DatabaseHelper2(this);
        heartRateTextView = findViewById(R.id.heartTextView);
        respRateTextView = findViewById(R.id.repTextView);
        timerTextView = findViewById(R.id.timerTextView);
        videoView = findViewById(R.id.videoView);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.vid;
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse(videoPath));
        Map<String, String> symptomData = new HashMap<>();
//        Button nextPage = findViewById(R.id.nextPage);
//        nextPage.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
//            startActivity(intent);
//        });

        Button uploadSigns = findViewById(R.id.uploadSigns);
        uploadSigns.setOnClickListener(v -> {
            dbHelper.insertData(symptomData.get("Heart-Rate"), symptomData.get("Resp-Rate"));
            respRateTextView.setText("Respiratory Rate: ");
            heartRateTextView.setText("Heart Rate: ");
        });
        Button heartRate = findViewById(R.id.heartRate);
        heartRate.setOnClickListener(v -> {
            if (!videoView.isPlaying()) {
                videoView.start();
            } else {
                videoView.pause();
            }
            startTimer();
            HeartRateCode heartRateCode = new HeartRateCode();
            HeartRateCode.SlowTask slowTask = heartRateCode.new SlowTask();
            String vidPath;
            String res;
            try {
                vidPath = heartRateCode.loadVideo(this);
                res = slowTask.execute(vidPath).get();
                symptomData.put("Heart-Rate", res);
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            Handler handler = new Handler();
            handler.postDelayed(() -> heartRateTextView.setText(heartRateTextView.getText() + res), RESULT_DELAY);

        });

        Button respRate = findViewById(R.id.respRate);
        respRate.setOnClickListener(v -> {
            startTimer();
            RespRateCode respRateCode = new RespRateCode();
            List<Double> xcsvDataArray = new ArrayList<>();
            List<Double> ycsvDataArray = new ArrayList<>();
            List<Double> zcsvDataArray = new ArrayList<>();
            xcsvDataArray = getCoordinates(xcsvDataArray, R.raw.xcsvbreathe19);
            ycsvDataArray = getCoordinates(ycsvDataArray, R.raw.ycsvbreathe19);
            zcsvDataArray = getCoordinates(zcsvDataArray, R.raw.zcsvbreathe19);
            int respVal = respRateCode.callRespiratoryCalculator(xcsvDataArray, ycsvDataArray, zcsvDataArray);
            symptomData.put("Resp-Rate", String.valueOf(respVal));
            Handler handler = new Handler();
            handler.postDelayed(() -> respRateTextView.setText(respRateTextView.getText() + String.valueOf(respVal)), RESULT_DELAY);
        });

    }


    private void startTimer() {
        new CountDownTimer(45000, 1000) { // 45 seconds, with 1 second intervals
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsLeft = millisUntilFinished / 1000;
                timerTextView.setText("Time left: " + secondsLeft);
            }

            @Override
            public void onFinish() {
                String resetTime = "45";
                timerTextView.setText(resetTime);
                videoView.stopPlayback();
                videoView.setVideoURI(null);
            }
        }.start();
    }

    public List getCoordinates(List<Double> csvDataArray, int csvbreathe19) {
        InputStream inputStream = getResources().openRawResource(csvbreathe19);

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                double number = Double.parseDouble(line);
                csvDataArray.add(number);
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvDataArray;
    }
}
