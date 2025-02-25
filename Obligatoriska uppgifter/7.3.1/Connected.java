package com.internet;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

/*
 * Connected är en Android-app som kontrollerar om enheten är ansluten till internet.
 * Denna app använder Androids ConnectivityManager för att kontrollera nätverksanslutningen
 * och visar statusen (ansluten eller inte) i en TextView.
 *
 * OBS! Denna app är prövad på en Pixel 8 Pro API 35.
 */

public class Connected extends AppCompatActivity {
    private TextView connectionStatus;

    /**
     * onCreate-metoden körs när aktiviteten startas. Denna metod sätter layouten
     * för aktiviteten och gör initialiseringar för UI-komponenterna.
     * Den hanterar också kontrollen av nätverksanslutning och uppdaterar TextView
     * för att visa om enheten är ansluten till internet.
     *
     * @param savedInstanceState En Bundle som innehåller tidigare tillstånd för aktiviteten om appen återställs efter att ha stängts.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connected);

        connectionStatus = findViewById(R.id.tvConnectionStatus);

        //Kontrollera nätverksanslutningen
        if (isConnectedToInternet()) {
            connectionStatus.setText("Enheten är ansluten till internet.");
        } else {
            connectionStatus.setText("Enheten är inte ansluten till internet.");
        }
    }

    /**
     * Denna metod kontrollerar om enheten är ansluten till internet genom att använda
     * ConnectivityManager och NetworkCapabilities. Den returnerar true om enheten är ansluten
     * till internet via ett aktivt nätverk, annars false.
     *
     * @return true om enheten är ansluten till internet, annars false.
     */

    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        //Kontrollera om ConnectivityManager är null
        if (connectivityManager != null) {
            //Hämta det aktiva nätverket
            Network activeNetwork = connectivityManager.getActiveNetwork();

            //Om det finns ett aktivt nätverk
            if (activeNetwork != null) {
                //Få network capabilities (ex om nätverket är anslutet och om det är internet)
                android.net.NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
                if (capabilities != null) {
                    return capabilities.hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET);
                }
            }
        }
        //Om ingen anslutning eller nätverkskapabilitet finns, returnera false
        return false;
    }
}
