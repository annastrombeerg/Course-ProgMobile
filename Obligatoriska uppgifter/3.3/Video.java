package com.video;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/*
 * Video är en Android-app som tillåter användaren att spela in en video med enhetens kamera
 * och visa den senaste inspelade videon direkt i appen. Appen använder Androids Camera API
 * och hanterar videoinspelning genom ett ActivityResultLauncher för att starta kameran
 * och visa resultatet i en VideoView.
 * OBS! Denna är prövad på en Pixel 8 Pro API 35.
 */
public class Video extends AppCompatActivity {
    private static final int REQUEST_PERMISSION = 200;
    Button recordButton;
    VideoView video;
    ActivityResultLauncher<Intent> activityResultLauncher;

    /**
     * onCreate-metoden körs när aktiviteten startas. Denna metod sätter layouten
     * för aktiviteten och gör initialiseringar för UI-komponenterna.
     * Den hanterar också knapptryckningen för att spela in video och resultatet från kameran
     * och visar den inspelade videon i en VideoView.
     *
     * @param savedInstanceState En Bundle som innehåller tidigare tillstånd för aktiviteten om appen återställs efter att ha stängts.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video);

        //Hämta referenser
        recordButton = findViewById(R.id.recordVideoButton);
        video = findViewById(R.id.videoView);

        //Kontrollera om både kameratillstånd och mikrofontillstånd är beviljade, om inte begär tillstånd
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},
                    REQUEST_PERMISSION);
        }

        //Klicklyssnare för knappen så videoinspelning startar
        recordButton.setOnClickListener(v -> {
            Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE); //Skapa en Intent för att starta videoinspelning
            activityResultLauncher.launch(videoIntent);
        });

        //Hantera resultatet från videoinspelningen via ActivityResultLauncher
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                //Hämta den inspelade videon som en Uri
                Uri videoUri = result.getData().getData();

                //Visa videon direkt i VideoView
                video.setVideoURI(videoUri);
                video.start();
            }
        });
    }

    /**
     * Hanterar resultatet från permission-begäran för mikrofonen och kameran.
     * Visar en Toast beroende på om användaren gav tillåtelse för mikrofonen och kameran.
     *
     * @param requestCode Kod för begäran (i detta fall kamera- och mikrofontillstånd).
     * @param permissions De begärda rättigheterna (t.ex. CAMERA, RECORD_AUDIO).
     * @param grantResults Resultatet av begäran (beviljad eller nekad).
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Tillstånd beviljat", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Tillstånd nekades", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
