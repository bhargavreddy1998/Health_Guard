package com.example.myloginapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;
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

public class MeasureHeartAndRespRateActivity extends AppCompatActivity {

    private HealthDatabaseHelper dbHelper;
    private TextView heartRateTextView;
    private TextView respRateTextView;
    private TextView timerTextView;
    private static final long RESULT_DELAY = 45000;
    private VideoView videoView;
    private static final String CHANNEL_ID = "heartRateChannel";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        dbHelper = new HealthDatabaseHelper(this);
        heartRateTextView = findViewById(R.id.heartTextView);
        respRateTextView = findViewById(R.id.repTextView);
        timerTextView = findViewById(R.id.timerTextView);
        videoView = findViewById(R.id.videoView);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.vid;
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse(videoPath));
        Map<String, String> symptomData = new HashMap<>();
        createNotificationChannel();
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
            handler.postDelayed(() -> {
                double heartRateValue = Double.parseDouble(res);
                if (heartRateValue > 150) {
                    showFeedbackDialog();
                }
                heartRateTextView.setText(heartRateTextView.getText() + res);
            }, RESULT_DELAY);

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

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Heart Rate Channel";
            String description = "Channel for heart rate notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void showFeedbackDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("High Heart Rate");
        builder.setMessage("Are you okay?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Handle the user's response (e.g., log or take action)
            dialog.dismiss();
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            // Handle the user's response (e.g., log or take action)
            dialog.dismiss();
        });
        builder.create().show();
    }

}
