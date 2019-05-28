package com.example.personalwellness;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;

public class SpotifyPlayer extends AppCompatActivity{

    private static final String CLIENT_ID = "836a6bfce9974f90ac73a1417824a93a";
    private static final String REDIRECT_URI = "penn-wellness-resources-login://callback";

    private static final String SOOTHING_URI = "61Tcx2dNtKu5IMdSb9q6Y1";
    private static final String FITNESS_URI = "37i9dQZF1DXdURFimg6Blm";
    private static final String SLEEP_URI = "37i9dQZF1DWZd79rJ6a7lp";
    private String type;

    private TextView trackName;
    private ImageView imageView;

    private SpotifyAppRemote mSpotifyAppRemote;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spotify_player);
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.type = getIntent().getStringExtra("type");
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected! Yay!");

                        // Now you can start interacting with App Remote
                        connected();

                    }

                    public void onFailure(Throwable throwable) {
                        Log.e("MyActivity", throwable.getMessage(), throwable);
                    }
                });

        trackName = (TextView)findViewById(R.id.trackName);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    public void playOrPause(View v) {
        Button b = (Button) v;
        if (b.getText().equals("Pause")) {
            mSpotifyAppRemote.getPlayerApi().pause();
            b.setText("Play");
        } else {
            mSpotifyAppRemote.getPlayerApi().resume();
            b.setText("Pause");
        }

    }

    public void next(View v) {
        Button b = (Button) v;
        mSpotifyAppRemote.getPlayerApi().skipNext();
    }

    public void prev(View v) {
        Button b = (Button) v;
        mSpotifyAppRemote.getPlayerApi().skipPrevious();
    }

    private void connected() {

        ImageButton imageButton= (ImageButton) findViewById(R.id.buttonImage);

        if (this.type.equals("stress")) {
            mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:" + SOOTHING_URI);
            imageButton.setImageResource(R.drawable.meditation2);
        } else if (this.type.equals("fitness")) {
            mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:" + FITNESS_URI);
            imageButton.setImageResource(R.drawable.beastmode);
        } else {
            mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:" + SLEEP_URI);
            imageButton.setImageResource(R.drawable.chill);
        }

        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                        trackName.setText(track.name + " - " + track.artist.name);
                    }
                });
    }
}

