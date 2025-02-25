package com.camera;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/*
 * Camera är en Android-app som tillåter användaren att ta ett foto med enhetens kamera
 * och visa det senaste tagna fotot direkt i appen. Appen använder Androids Camera API
 * och hanterar kamerafunktionalitet genom ett ActivityResultLauncher för att starta kameran
 * och visa resultatet i en ImageView.
 * OBS! Denna är prövad på en Pixel 8 Pro API 35.
 */

public class Camera extends AppCompatActivity {
    private static final int REQUEST_PERMISSION = 200;
    Button takePhoto;
    ImageView imageView;
    ActivityResultLauncher<Intent> activityResultLauncher;

    /**
     * onCreate-metoden körs när aktiviteten startas. Denna metod sätter layouten
     * för aktiviteten och gör initialiseringar för UI-komponenterna.
     * Den hanterar också knapptryckningen för att ta ett foto och resultatet från kameran och visar den tagna bilden i en ImageView.
     *
     * @param savedInstanceState En Bundle som innehåller tidigare tillstånd för aktiviteten om appen återställs efter att ha stängts.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);

        //Hämta referenserna
        takePhoto = findViewById(R.id.takePictureButton);
        imageView = findViewById(R.id.imageView);

        //Kontrollera om kameratillstånd är beviljat, om inte, begär tillstånd
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_PERMISSION);
        }

        //Klicklyssnare för knappen så kameran startar
        takePhoto.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //Skapa en Intent för att starta kameran
            activityResultLauncher.launch(cameraIntent);
        });

        //Hantera resultatet från kameran via ActivityResultLauncher
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                //Hämta den tagna bilden som en Bitmap
                Bitmap imageBitmap = (Bitmap) result.getData().getExtras().get("data");

                //Visa bilden direkt i ImageView
                imageView.setImageBitmap(imageBitmap);
            }
        });
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