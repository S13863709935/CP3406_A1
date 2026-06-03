package com.example.weatherforecastd9k;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.weatherforecastd9k.ui.MapFragment;
import com.example.weatherforecastd9k.ui.WeatherFragment;
import com.example.weatherforecastd9k.ui.HistoryFragment;
import com.example.weatherforecastd9k.ui.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.weatherforecastd9k.util.SessionManager;
import com.example.weatherforecastd9k.network.RetrofitClient;

public class MainActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private AudioService audioService;

    //Use ServiceConnection to monitor changes in Service status
    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            audioService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            //Here we instantiate audioService and implement it through the binder
            audioService = ((AudioService.AudioBinder) binder).getService();
            audioService.play();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RetrofitClient.init(this);
        
        sessionManager = new SessionManager(this);
        // Bind Service
        Intent intentService = new Intent(MainActivity.this, AudioService.class);
        bindService(intentService, conn, BIND_AUTO_CREATE);

        // Send broadcast
        Intent intent = new Intent();
        intent.setAction(StudyEndReceiver.ACTION_STUDY_END);
        intent.setClass(MainActivity.this, StudyEndReceiver.class);
        sendBroadcast(intent);

        // Check if the session is valid
        if (!sessionManager.isSessionValid()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_map) {
                selectedFragment = new MapFragment();
            } else if (itemId == R.id.navigation_weather) {
                selectedFragment = new WeatherFragment();
            } else if (itemId == R.id.navigation_history) {
                selectedFragment = new HistoryFragment();
            } else if (itemId == R.id.navigation_settings) {
                selectedFragment = new SettingsFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, selectedFragment)
                    .commit();
            }
            return true;
        });

        // Modify the default selected Fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, new MapFragment())
                .commit();
            // Set the default selected item of the bottom navigation bar
            bottomNav.setSelectedItemId(R.id.navigation_map);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if the session has expired on each resume
        if (!sessionManager.isSessionValid()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}