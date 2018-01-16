package com.test.audio;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.TextView;
import android.widget.EditText;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;

import android.widget.Toast;
import android.util.Log;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.lang.Thread;


/**
 * @author lcz
 */
public class AudioPlayerActivity extends Activity {
    private static final String TAG = "Audioplayer";
    private TextView textView;
    private EditText file_name_Text;
    private String filePath;
    private MediaPlayer mediaPlayer;
    private boolean pause;
    private boolean validUrl;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        textView = (TextView) this.findViewById(R.id.textView);
        file_name_Text = (EditText) this.findViewById(R.id.file_name);
        mediaPlayer = new MediaPlayer();
        DirectoryUtils.getEnvironmentDirectories();
        DirectoryUtils.getApplicationDirectories(getBaseContext());

    }

    @Override
    protected void onDestroy() {
        mediaPlayer.release();
        mediaPlayer = null;
        super.onDestroy();
    }

    public boolean checkNetWorkStatus(Context context) {
        boolean result;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if (netinfo != null && netinfo.isConnected()) {
            result = true;
            Log.i(TAG, "The net was connected");
        } else {
            result = false;
            Log.i(TAG, "The net was bad!");
        }
        return result;
    }

    public boolean checkURL(String url) {
        boolean value = false;
        URL serverAddress = null;
        HttpURLConnection connection = null;
        try {
            serverAddress = new URL(url);
            connection = (HttpURLConnection) serverAddress.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setReadTimeout(10000);
            connection.connect();
            int code = connection.getResponseCode();
            if (code != 200) {
                value = false;
            } else {
                value = true;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

//    public boolean checkURL(String url) {
//        boolean value = false;
//        try {
//            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
//            int code = conn.getResponseCode();
//            if (code != 200) {
//                value = false;
//            } else {
//                value = true;
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return value;
//    }
//
//    URL url = new URL(url);
//    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//           try
//
//    {
//        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//        readStream(in);
//		    finally{
//        urlConnection.disconnect();
//    }
//    }

    public void mediaPlay(View v) {
        switch (v.getId()) {
            case R.id.button_play:
                String fileName = file_name_Text.getText().toString();
                Log.v("AudioPlayer", fileName);
                if (fileName.startsWith("http")) {
                    filePath = fileName;
                    new Thread() {
                        public void run() {
                            validUrl = checkURL(filePath);
                        }
                    }.start();
                    if (validUrl) {
                        Log.v("AudioPlayer", "startsWith http");

                        play(0);
                        textView.setText("���ֿ�ʼ����...");
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.file_noexist, 1).show();
                    }

                } else {
                    Log.v(TAG, "not http");
                    File audio;
                    if (fileName.startsWith("/")) {
                        audio = new File(fileName);
                    } else {
                        audio = new File("/sdcard/Music/", fileName);
                    }

                    if (audio.exists()) {
                        filePath = audio.getAbsolutePath();
                        play(0);
                        textView.setText("���ֿ�ʼ����...");
                    } else {
                        filePath = null;
                        Toast.makeText(getApplicationContext(), R.string.file_noexist, 1).show();
                    }
                }

                break;
            case R.id.button_pause:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    pause = true;
                    textView.setText("������ͣ����...");
                    ((Button) v).setText(R.string.button_continue);
                } else {
                    if (pause) {
                        mediaPlayer.start();
                        pause = false;
                        textView.setText("���ּ�������...");
                        ((Button) v).setText(R.string.button_pause);
                    }
                }
                break;

            case R.id.button_stop:
                if (mediaPlayer.isPlaying()) {
                    textView.setText("����ֹͣ����...");
                    mediaPlayer.stop();
                }
                break;
            default:
                break;
        }
    }

    /**
     * @param playPosition
     */
    private void play(int playPosition) {
        try {
            mediaPlayer.reset();
            Log.v("AudioPlayer", "filePath :" + filePath);
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MyPreparedListener(playPosition));
        } catch (Exception e) {
            Log.v("AudioPlayer", "Exception");
            e.printStackTrace();
        }
    }

    private final class MyPreparedListener implements android.media.MediaPlayer.OnPreparedListener {
        private int playPosition;

        public MyPreparedListener(int playPosition) {
            this.playPosition = playPosition;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            mediaPlayer.start();
            if (playPosition > 0) {
                mediaPlayer.seekTo(playPosition);
            }
        }
    }

}