package com.example.myloginapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class HeartRateCode {


    public String loadVideo(Context context) {
        File cacheDir = context.getCacheDir();
        File f = new File(cacheDir, "vid.mp4");
        if (!f.exists()) {
            try {
                InputStream inputStream = context.getAssets().open("vid.mp4");
                int size = inputStream.available();
                byte[] buffer = new byte[size];
                inputStream.read(buffer);
                inputStream.close();

                OutputStream outputStream = new FileOutputStream(f);
                outputStream.write(buffer);
                outputStream.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return f.getAbsolutePath();
    }

    public class SlowTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            List<Bitmap> frameList = new ArrayList<>();

            try {
                retriever.setDataSource(params[0]);
                int i = 0;
                while (i < 1000) {
                    Bitmap bitmap = retriever.getFrameAtTime(i * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                    frameList.add(bitmap);
                    i = i + 75;
                }
                retriever.release();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                long redBucket = 0;
                long pixelCount = 0;
                List<Long> a = new ArrayList<>();

                for (Bitmap bitmap : frameList) {
                    redBucket = 0;
                    for (int y = 225; y < 250; y++) {
                        for (int x = 225; x < 250; x++) {
                            int c = bitmap.getPixel(x, y);
                            pixelCount++;
                            redBucket += Color.red(c) + Color.blue(c) + Color.green(c);
                        }
                    }
                    a.add(redBucket);
                }

                List<Long> b = new ArrayList<>();
                for (int j = 0; j < a.size() - 5; j++) {
                    long temp = (a.get(j) + a.get(j + 1) + a.get(j + 2) + a.get(j + 3) + a.get(j + 4)) / 4;
                    b.add(temp);
                }

                long x = b.get(0);
                int count = 0;
                for (int k = 1; k < b.size(); k++) {
                    long p = b.get(k);
                    if (p - x < 0) {
                        count++;
                    }
                    x = b.get(k);
                }

                float rate = ((count * 1.0f) / 7.5f) * 60;
                return String.valueOf(rate * 2);
            }
        }

    }
}
