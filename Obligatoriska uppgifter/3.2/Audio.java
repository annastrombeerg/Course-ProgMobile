package com.audio;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.media.MediaRecorder;
import android.Manifest;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.File;
import java.io.FileOutputStream;

/*
 * Audio är en Android-app som låter användaren spela in och spela upp ljud.
 * Den använder MediaRecorder för att spela in ljud och MediaPlayer för att spela upp det inspelade ljudet.
 * Applikationen begär mikrofontillstånd från användaren och ger visuell feedback via knappar för att starta och
 * stoppa inspelning samt spela upp ljudet.
 * OBS! Denna är prövad på en Pixel 8 Pro API 35.
 */

public class Audio extends AppCompatActivity {
    private static final String RECORDING_FILE = "audio_recording.3gp";
    private static final int REQUEST_PERMISSION = 200;
    private File audioFile;
    private MediaRecorder recorder;
    private MediaPlayer player;
    Button recordButton, playButton, stopButton;


    /**
     * onCreate-metoden körs när aktiviteten startas. Denna metod sätter layouten
     * för aktiviteten och gör initialiseringar för UI-komponenterna.
     * Den hanterar också knapptryckningen för inspelning, uppspelning och stopp.
     * Den begär mikrofontillstånd om det inte redan har beviljats.
     *
     * @param savedInstanceState En Bundle som innehåller tidigare tillstånd för aktiviteten om appen återställs efter att ha stängts.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio);

        //Skapa referenser till knappar
        recordButton = findViewById(R.id.recordButton);
        playButton = findViewById(R.id.playButton);
        stopButton = findViewById(R.id.stopButton);
        //Skapa en temporär ljudfil i intern lagring
        audioFile = new File(getFilesDir(), RECORDING_FILE);

        //Begär mikrofontillstånd om inte redan beviljat
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_PERMISSION);
        }

        //Konfigurera knappen för inspelning
        recordButton.setOnClickListener(v -> startRecording());
        //Konfigurera knappen för uppspelning
        playButton.setOnClickListener(v -> startPlaying());
        //Konfigurera knappen för att stoppa inspelning
        stopButton.setOnClickListener(v -> stopRecording());

        //Konfigurera inställningar i onCreate()
        recorder = new MediaRecorder();
        player = new MediaPlayer();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

    }

    /**
     * Metoden förbereder och startar MediaRecorder för att spela in ljud från mikrofonen
     * och sparar inspelningen till en specifik fil på intern lagring.
     * Om något fel inträffar under förberedelse eller inspelning, fångas det och ett felmeddelande visas för användaren.
     */
    public void startRecording() {
        try {
            recorder.prepare();
            recorder.start();
            Toast.makeText(this, "Spelar in ljud...", Toast.LENGTH_SHORT).show();
            recorder.setOutputFile(new FileOutputStream(audioFile).getFD()); // Outputfilen
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Fel vid inspelning", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Stoppar inspelningen genom att använda MediaRecorder och frigör resurser.
     */
    public void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
        Toast.makeText(this, "Inspelningen är avslutad", Toast.LENGTH_SHORT).show();
    }

    /**
     * Startar uppspelning av den inspelade ljudfilen med hjälp av MediaPlayer.
     * Uppspelningen börjar när MediaPlayer har förberetts.
     * * Om något fel inträffar under uppspelning av ljudet, fångas det och ett felmeddelande visas för användaren.
     */
    public void startPlaying() {
        try {
            player.setDataSource(audioFile.getAbsolutePath());  //Filens väg till intern lagring
            player.prepare();
            player.start();
            Toast.makeText(this, "Spelar upp ljud", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Fel vid uppspelning", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Hanterar resultatet från permission-begäran för mikrofonen.
     * Visar en Toast beroende på om användaren gav tillåtelse.
     *
     * @param requestCode Kod för begäran (i detta fall mikrofontillstånd).
     * @param permissions De begärda rättigheterna.
     * @param grantResults Resultatet av begäran.
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
