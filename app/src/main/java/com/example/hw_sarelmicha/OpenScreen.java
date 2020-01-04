package com.example.hw_sarelmicha;

//import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationResult;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class OpenScreen extends Activity implements HighScoreVariables {

    private Button newGameBtn;
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
    private final String SETTINGS_FILE = "SettingsFile";
    private SharedPreferences sharedPreferences;
    private static final int REQUEST_CODE = 101;
    private boolean locationHasBeenSetUp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_screen);
        getSettingsStateFromFile();
        setIds();
        createSoundtrack();
        addListenersButtons();
        highScore = new HighScore(getApplicationContext().getSharedPreferences(SCORE_FILE, MODE_PRIVATE));
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(OpenScreen.this);
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

    private void getSettingsStateFromFile(){

        sharedPreferences = (getApplicationContext().getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE));
        musicOn = sharedPreferences.getBoolean("music",true);
        regularMode = sharedPreferences.getBoolean("mode", true);
        vibrationOn = sharedPreferences.getBoolean("vibration", true);
    }
    private void setIds(){
        //Initialize Buttons
        newGameBtn = (Button)findViewById(R.id.newGameBtn);
        settings = (Button)findViewById(R.id.settings);
        highScoreBtn = (Button)findViewById(R.id.highScoreBtn);
    }

    private void createSoundtrack(){
        mediaPlayer = MediaPlayer.create(this, R.raw.gamemusic);
        mediaPlayer.setLooping(true);
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

    }

    private void newGame() {
        if(locationHasBeenSetUp) {
            Intent intent = new Intent(this, Difficulty.class);
            intent.putExtra("lat", lat);
            intent.putExtra("lon", lon);
            intent.putExtra("music", musicOn);
            intent.putExtra("mode", regularMode);
            intent.putExtra("vibration", vibrationOn);

            startActivity(intent);
        }
        else {
            Toast.makeText(this,"Fetching your location for the first time... please wait.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showSettings(){
            Intent intent = new Intent(this, Settings.class);
            startActivityForResult(intent, 1);
    }

    private void showHighScores(){

            Intent intent = new Intent(this, HighScoreScreen.class);
            intent.putExtra("music", musicOn);
            startActivity(intent);
    }

    private void fetchLocation() {

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    lat = currentLocation.getLatitude();
                    lon  = currentLocation.getLongitude();
                    locationHasBeenSetUp = true;
                    return;
                }
            }
        });
        //if you are here it means get location failed.
        setLocationManually();
        locationHasBeenSetUp = true;

//        createLocationRequest();
//        createLocationCallback();
//        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper());
//        Log.d("TUGA", "fetchLocation: hod hod hod");
    }

    private void setLocationManually(){

        lat = 32.113490;
        lon = 34.817853;
    }
//    private void createLocationRequest(){
//
//        Log.d("TUGA", "inside createLocationReq");
//
//        locationRequest = LocationRequest.create();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(100);
//        locationRequest.setFastestInterval(100);
//    }

//    private void createLocationCallback(){
//
//        Log.d("TUGA", "inside cretaeCallBack");
//
//        locationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                if (locationResult == null) {
//                    Log.d("TUGA", "maybe im here?");
//                    return;
//                }
//                for (Location location : locationResult.getLocations()) {
//                    Log.d("TUGA", "OR maybe im here?");
//                    currentLocation = location;
//                }
//
//                lat = currentLocation.getLatitude();
//                lon  = currentLocation.getLongitude();
//                locationHasBeenSetUp = true;
//
//                if (fusedLocationProviderClient != null) {
//                    fusedLocationProviderClient.removeLocationUpdates(locationCallback);
//                }
//            }
//        };
//    }
}


