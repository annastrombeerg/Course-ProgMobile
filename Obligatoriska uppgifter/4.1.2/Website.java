package com.website;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

/*
 * Website är en Android-app som tillåter användaren att öppna olika webbsidor inuti appen via WebView.
 * Den här appen har knappar som leder till specifika webbsidor: Java- och Android-dokumentation.
 * WebView används för att visa dessa webbsidor utan att öppna externa webbläsare.
 * OBS! Denna är prövad på en Pixel 8 Pro API 35.
 */
public class Website extends AppCompatActivity {
    private static final String JAVA_URL = "https://docs.oracle.com/en/java/javase/22/";
    private static final String ANDROID_URL = "https://developer.android.com/develop";

    private WebView webView;


    /**
     * onCreate-metoden körs när aktiviteten startas. Denna metod sätter layouten
     * för aktiviteten och gör initialiseringar för UI-komponenterna.
     * Den ställer också in WebView för att visa webbsidor och kopplar knappar till åtgärder
     * för att ladda de specifika URL:er när knapparna trycks.
     *
     * @param savedInstanceState En Bundle som innehåller tidigare tillstånd för aktiviteten om appen återställs efter att ha stängts.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.website);

        //Hämta referens till WebView och knappar
        webView = findViewById(R.id.webView);
        Button buttonOpenJava = findViewById(R.id.buttonOpenJava);
        Button buttonOpenAndroid = findViewById(R.id.buttonOpenAndroid);

        //Konfigurera WebView så att det laddar webbsidor inom appen
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);  //Aktivera JavaScript då Java Doc kräver detta

        //Sätt WebViewClient för att hindra att externa webbläsare öppnas
        webView.setWebViewClient(new WebViewClient());

        //Knapp som öppnar Java Doc i WebView
        buttonOpenJava.setOnClickListener(v -> {
            webView.loadUrl(JAVA_URL);  //Ladda Java-webbsidan i WebView
        });

        //Knapp som öppnar Android Doc i WebView
        buttonOpenAndroid.setOnClickListener(v -> {
            webView.loadUrl(ANDROID_URL);  //Ladda Android-webbsidan i WebView
        });
    }
}
