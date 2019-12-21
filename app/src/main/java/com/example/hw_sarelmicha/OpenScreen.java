package com.example.hw_sarelmicha;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


public class OpenScreen extends Activity implements HighScoreVariables {

    private Button newGameBtn;
    private Button exitBtn;
    private Button highScoreBtn;
    private HighScore highScore;
    public static MediaPlayer mediaPlayer;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private double lat;
    private double lon;
    private static final int REQUEST_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_screen);
        setIds();
        startSoundtrack();
        addListenersButtons();
        highScore = new HighScore(getApplicationContext().getSharedPreferences(SCORE_FILE, MODE_PRIVATE));
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();

    }

    @Override
    protected void onPause() {
        mediaPlayer.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mediaPlayer.start();
        super.onResume();

    }
    private void setIds(){
        //Initialize Buttons
        newGameBtn = (Button)findViewById(R.id.newGameBtn);
        exitBtn = (Button)findViewById(R.id.exitBtn);
        highScoreBtn = (Button)findViewById(R.id.highScoreBtn);
    }

    private void startSoundtrack(){
        mediaPlayer = MediaPlayer.create(this, R.raw.gamemusic);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    private void addListenersButtons(){

        newGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newGame();
            }
        });

        highScoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHighScores();
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitGame();
            }
        });

    }

    private void newGame() {
        Intent intent = new Intent(this, Difficulty.class);
        intent.putExtra("lat", lat);
        intent.putExtra("lon", lon);


        startActivity(intent);
    }

    private void showHighScores(){
        Intent intent = new Intent(this, HighScoreScreen.class);
        startActivity(intent);
    }

    public static void exitGame() {
        System.exit(0);
    }

    private void fetchLocation() {

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    lat = currentLocation.getLatitude();
                    lon  = currentLocation.getLongitude();
                }
            }
        });
    }
}