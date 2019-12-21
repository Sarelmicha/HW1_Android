package com.example.hw_sarelmicha;

import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
    private GoogleMap map;
    private int scorePlayerInfoIndex;
    private SupportMapFragment mapFragment;
    private HashMap<TextView, Integer> hashMap;
    private Location playerLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score_screen);
        highScore = new HighScore(getApplicationContext().getSharedPreferences(SCORE_FILE, MODE_PRIVATE));
        highScoreContainer = (LinearLayout) findViewById(R.id.scores_container);
        mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        hashMap = new HashMap<>();
        playerLocation = new Location("");
    }

    @Override
    protected void onResume() {

        OpenScreen.mediaPlayer.start();
        //highScore.deleteAllScores(); //Clear the DB for some QA
        highScore.readScores();
        allPlayersInfoList = highScore.getAllPlayers();
        allHighScores = new TextView[allPlayersInfoList.size()];
        showTop10();

        super.onResume();
    }

    @Override
    protected void onPause() {
        OpenScreen.mediaPlayer.pause();
        super.onPause();
    }

    public void showTop10(){

        createAllTextViewsScore();
        showAllTextViewsScoreOnScreen();
    }

    public void createAllTextViewsScore(){

        for (scorePlayerInfoIndex = 0; scorePlayerInfoIndex < allPlayersInfoList.size() ; scorePlayerInfoIndex++) {
            allHighScores[scorePlayerInfoIndex] = new TextView(this);
            allHighScores[scorePlayerInfoIndex].setText(String.format("%-1s %-5d %-1f %-1f", allPlayersInfoList.get(scorePlayerInfoIndex).getName(), allPlayersInfoList.get(scorePlayerInfoIndex).getScore(),allPlayersInfoList.get(scorePlayerInfoIndex).getLat(),
                    allPlayersInfoList.get(scorePlayerInfoIndex).getLon()));
            allHighScores[scorePlayerInfoIndex].setTextColor(Color.WHITE);
            hashMap.put(allHighScores[scorePlayerInfoIndex], scorePlayerInfoIndex);
            allHighScores[scorePlayerInfoIndex].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   showPlayerLocationOnMap(v);
                }
            });
        }
    }
    public void showPlayerLocationOnMap(View v){
        playerLocation.setLatitude(allPlayersInfoList.get(hashMap.get(v)).getLat());
        playerLocation.setLongitude(allPlayersInfoList.get(hashMap.get(v)).getLon());
        mapFragment.getMapAsync(this);
    }

    public void showAllTextViewsScoreOnScreen(){

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity  = Gravity.CENTER;
        highScoreContainer.removeAllViews();
        for (int i = 0; i < allHighScores.length ; i++) {

            allHighScores[i].setId(i);
            allHighScores[i].setPadding(0,10,0,10);
            allHighScores[i].setTextSize(18);
            allHighScores[i].setTypeface(Typeface.create("monospace", Typeface.BOLD));
            allHighScores[i].setLayoutParams(params);
            highScoreContainer.addView(allHighScores[i]); // dynamicButtonsLinearLayout is the container of the buttons
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
