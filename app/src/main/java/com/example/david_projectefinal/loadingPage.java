package com.example.david_projectefinal;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.david_projectefinal.MainActivity;

public class loadingPage extends AppCompatActivity {

    public void gifs()
    {
        //Agregar implementacion Glide dentro de archivo build.gradle.
        //1 gif
        ImageView imageView = (ImageView)findViewById(R.id.imageMachine);
        Glide.with(getApplicationContext()).load(R.drawable.machine3).into(imageView);
        ImageView imageView1 = (ImageView)findViewById(R.id.imgWelcome);
        Glide.with(getApplicationContext()).load(R.drawable.welcome).into(imageView1);
    }
    MediaPlayer mediaPlayer;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_load);
        setTitle("Buidem S.L.");
        mediaPlayer = MediaPlayer.create(this, R.raw.adiosmusic);
        mediaPlayer.start();


        gifs();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.stop();
                Intent intent = new Intent (com.example.david_projectefinal.loadingPage.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },4000);
    }







}
