package com.example.hw_sarelmicha;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


public class OpenScreen extends Activity implements HighScoreVariables {

    private Button newGameBtn;
    private Button exitBtn;
    private Button settings;
    private Button highScoreBtn;
    private HighScore highScore;
    public static MediaPlayer mediaPlayer;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private double lat;
    private double lon;
    private Boolean musicOn;
    private boolean regularMode;
    private boolean vibrationOn;

    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        setContentView(R.layout.activity_open_screen);
        //first time we get in the game musicOn, regularGame and vibrationOn is true
        musicOn = true;
        regularMode = true;
        vibrationOn = true;
        setIds();
        if(musicOn)
            startSoundtrack();
        addListenersButtons();
        highScore = new HighScore(getApplicationContext().getSharedPreferences(SCORE_FILE, MODE_PRIVATE));
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();
    }

    @Override
    protected void onPause() {
        if (musicOn)
            mediaPlayer.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {

        if (musicOn)
            mediaPlayer.start();
        super.onResume();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                    musicOn = data.getExtras().getBoolean("music");
                    regularMode = data.getExtras().getBoolean("mode");
                    vibrationOn = data.getExtras().getBoolean("vibration");
            }
        }
    }
    private void setIds(){
        //Initialize Buttons
        newGameBtn = (Button)findViewById(R.id.newGameBtn);
        exitBtn = (Button)findViewById(R.id.exitBtn);
        settings = (Button)findViewById(R.id.settings);
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

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettings();
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
        intent.putExtra("music",musicOn);
        intent.putExtra("mode", regularMode);
        intent.putExtra("vibration",vibrationOn);

        startActivity(intent);
    }

    private void showSettings(){
        Intent intent = new Intent(this, Settings.class);
        intent.putExtra("music",musicOn);
        intent.putExtra("mode", regularMode);
        intent.putExtra("vibration", vibrationOn);
        startActivityForResult(intent, 1);
    }

    private void showHighScores(){
        Intent intent = new Intent(this, HighScoreScreen.class);
        intent.putExtra("music", musicOn);
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