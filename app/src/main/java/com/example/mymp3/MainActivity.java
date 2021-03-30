package com.example.mymp3;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView txtTitle, txtTimeSong, txtTimeTotal;
    SeekBar skBar;
    ImageView imgDisc;
    ImageButton btnPrev, btnPlay, btnStop, btnForward;

    ArrayList<Song> arraySong;
    int position = 0;
    MediaPlayer mediaPlayer;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Reference();
        AddSong();

        animation = AnimationUtils.loadAnimation(this, R.anim.disc_rotate);

        initMediaPlayer();

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                btnPlay.setImageResource(R.drawable.play);
                initMediaPlayer();
            }
        });


        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    // If media is playing -> pause -> change image to play
                    mediaPlayer.pause();
                    btnPlay.setImageResource(R.drawable.play);
                } else {
                    // If media is pausing -> resume -> change image to pause
                    mediaPlayer.start();
                    btnPlay.setImageResource(R.drawable.pause);
                }
                setTimeTotal();
                UpdateTimeSong();
                imgDisc.startAnimation(animation);
            }
        });

        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position++;
                if (position > arraySong.size() - 1) {
                    position = 0;
                }
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                initMediaPlayer();
                mediaPlayer.start();
                btnPlay.setImageResource(R.drawable.pause);
                setTimeTotal();
                UpdateTimeSong();
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position--;
                if (position < 0) {
                    position = arraySong.size() - 1;
                }
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                initMediaPlayer();
                mediaPlayer.start();
                btnPlay.setImageResource(R.drawable.pause);
                setTimeTotal();
                UpdateTimeSong();
            }
        });

        skBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(skBar.getProgress());
            }
        });

    }


    private void initMediaPlayer() {
        mediaPlayer = MediaPlayer.create(MainActivity.this, arraySong.get(position).getFile());
        txtTitle.setText(arraySong.get(position).getTitle());
    }

    private void AddSong() {
        // Add song to list music automatically
        arraySong = new ArrayList<>();
        arraySong.add(new Song("Doraemon", R.raw.doraemon));
        arraySong.add(new Song("Maruko", R.raw.maruko));
        arraySong.add(new Song("My Love", R.raw.mylove));
        arraySong.add(new Song("Siêu Nhân Cuồng Phong", R.raw.ninjahuricane));
    }

    private void Reference() {
        // Create buttons
        txtTimeSong = (TextView) findViewById(R.id.textViewTimeSong);
        txtTimeTotal = (TextView) findViewById(R.id.textViewTimeTotal);
        txtTitle = (TextView) findViewById(R.id.textviewTitle);
        skBar = (SeekBar) findViewById(R.id.seekBarSong);
        btnForward = (ImageButton) findViewById(R.id.btnForward);
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnPrev = (ImageButton) findViewById(R.id.btnPrev);
        btnStop = (ImageButton) findViewById(R.id.btnStop);
        imgDisc = (ImageView) findViewById(R.id.imageViewDisc);
    }


    private void setTimeTotal() {
        // Convert to time
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        txtTimeTotal.setText(dateFormat.format(mediaPlayer.getDuration()));
        // set seekBar max duration = mediaPlayer.getDuration()
        skBar.setMax(mediaPlayer.getDuration());
    }

    private void UpdateTimeSong() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat dateFormatSong = new SimpleDateFormat("mm:ss");
                // get current time of media
                txtTimeSong.setText(dateFormatSong.format(mediaPlayer.getCurrentPosition()));
                // update progress skBar
                skBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 500);

                // Check time song if ends -> next song
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        position++;
                        if (position > arraySong.size() - 1) {
                            position = 0;
                        }
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                        }
                        initMediaPlayer();
                        mediaPlayer.start();
                        btnPlay.setImageResource(R.drawable.pause);
                        setTimeTotal();
                        UpdateTimeSong();
                    }
                });
            }
        }, 100);
    }

}