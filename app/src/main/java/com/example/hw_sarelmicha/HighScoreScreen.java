package com.example.hw_sarelmicha;

import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.HashMap;

public class HighScoreScreen extends FragmentActivity implements  HighScoreVariables, OnMapReadyCallback {

    private ArrayList<PlayerInfo> allPlayersInfoList;
    private TextView[] allHighScores;
    private HighScore highScore;
    private LinearLayout highScoreContainer;
    private LinearLayout mapContainer;
    private GoogleMap map;
    private int scorePlayerInfoIndex;
    private SupportMapFragment mapFragment;
    private HashMap<TextView, Integer> hashMap;
    private Location playerLocation;
    private final int MAX_SIZE = 10;
    private boolean musicOn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score_screen);
        highScore = new HighScore(getApplicationContext().getSharedPreferences(SCORE_FILE, MODE_PRIVATE));
        highScoreContainer = (LinearLayout) findViewById(R.id.scores_container);
        mapContainer = (LinearLayout)findViewById(R.id.map_container);
        mapContainer.setClipToOutline(true);
        mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        hashMap = new HashMap<>();
        musicOn = getIntent().getExtras().getBoolean("music");
        playerLocation = new Location("");
    }

    @Override
    protected void onResume() {

        if(musicOn)
            OpenScreen.mediaPlayer.start();
        //highScore.deleteAllScores(); //Clear the DB for some testing
        highScore.readScores();
        allPlayersInfoList = highScore.getAllPlayers();
        allHighScores = new TextView[MAX_SIZE];
        showTop10();

        super.onResume();
    }

    @Override
    protected void onPause() {
        if(musicOn)
            OpenScreen.mediaPlayer.pause();
        super.onPause();
    }

    public void showTop10(){

        createAllTextViewsScore();
        showAllTextViewsScoreOnScreen();
    }

    public void createAllTextViewsScore(){

        for (scorePlayerInfoIndex = 0; scorePlayerInfoIndex < MAX_SIZE ; scorePlayerInfoIndex++) {
            allHighScores[scorePlayerInfoIndex] = new TextView(this);
            if(scorePlayerInfoIndex < allPlayersInfoList.size()){
                hashMap.put(allHighScores[scorePlayerInfoIndex], scorePlayerInfoIndex);
                allHighScores[scorePlayerInfoIndex].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPlayerLocationOnMap(v);
                    }
                });
            }
        }
    }
    public void showPlayerLocationOnMap(View v){
        playerLocation.setLatitude(allPlayersInfoList.get(hashMap.get(v)).getLat());
        playerLocation.setLongitude(allPlayersInfoList.get(hashMap.get(v)).getLon());
        mapFragment.getMapAsync(this);
    }

    public void showAllTextViewsScoreOnScreen(){

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity  = Gravity.CENTER;
        highScoreContainer.removeAllViews();
        for (int i = 0; i < allHighScores.length ; i++) {

            //%1$-10s|%2$-10s|%3$-20s|
            allHighScores[i].setId(i);
            allHighScores[i].setPadding(0,10,0,10);
            if(i < allPlayersInfoList.size()){
                allHighScores[i].setText(String.format(" %1$-29s %2$-5d", allPlayersInfoList.get(i).getName(), allPlayersInfoList.get(i).getScore()));
            }
            allHighScores[i].setTextColor(Color.WHITE);
            allHighScores[i].setTextSize(17);
            allHighScores[i].setTypeface(Typeface.create("monospace", Typeface.BOLD));
            allHighScores[i].setLayoutParams(params);
            if(i % 2 == 0) {
                allHighScores[i].setBackgroundColor(Color.HSVToColor(new float[]{228, 94, 41}));
                allHighScores[i].getBackground().setAlpha(120);
            }
            highScoreContainer.addView(allHighScores[i]);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng latLng = new LatLng(playerLocation.getLatitude(), playerLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here!");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
        googleMap.addMarker(markerOptions);

    }
}
