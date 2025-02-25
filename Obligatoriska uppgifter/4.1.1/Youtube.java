package com.youtube;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/*
 * Youtube är en Android-app som tillåter användaren att spela upp YouTube-videor
 * genom att klicka på olika knappar. Den öppnar videon via YouTube.
 * OBS! Denna är prövad på en Pixel 8 Pro API 35.
 */
public class Youtube extends AppCompatActivity {
    private static final String VIDEO_URL_1 = "https://www.youtube.com/watch?v=MAlSjtxy5ak";
    private static final String VIDEO_URL_2 = "https://www.youtube.com/watch?v=HluANRwPyNo";

    Button videoOne, videoTwo;

    /**
     * onCreate-metoden körs när aktiviteten startas. Denna metod sätter layouten
     * för aktiviteten och gör initialiseringar för UI-komponenterna.
     * Den hanterar också knapptryckningen för att spela upp respektive YouTube-video
     * baserat på vilken knapp användaren klickar på.
     *
     * @param savedInstanceState En Bundle som innehåller tidigare tillstånd för aktiviteten om appen återställs efter att ha stängts.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube);

        //Hämta referenser
        videoOne = findViewById(R.id.videoButtonOne);
        videoTwo = findViewById(R.id.videoButtonTwo);

        //Klicklyssnare för första videon
        videoOne.setOnClickListener(v -> openYouTubeVideo(VIDEO_URL_1));

        //Klicklyssnare för andra videon
        videoTwo.setOnClickListener(v -> openYouTubeVideo(VIDEO_URL_2));
    }

    /**
     * Öppnar YouTube för att spela upp en video från den angivna URL:en.
     * Om användaren inte har YouTube-appen installerad kommer en webbläsare öppnas för att visa videon.
     *
     * @param url Webbadressen till YouTube-videon som ska spelas upp.
     */
    private void openYouTubeVideo(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url));
        intent.putExtra(Intent.EXTRA_REFERRER, android.net.Uri.parse("android-app://com.google.android.youtube"));
        startActivity(intent);
    }
}
