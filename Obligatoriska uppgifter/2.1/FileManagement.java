package com.fileManagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/*
 * FileManagement är en Android-app som hanterar primtal genom att läsa och skriva
 * det senaste primtalet till en fil. När appen startas laddas det senaste primtalet
 * och när användaren trycker på knappen söker appen efter nästa primtal och uppdaterar
 * filen samt UI.
 */

public class FileManagement extends AppCompatActivity {
    private static final String FILE = "last_prime.txt";
    private TextView primeTextView;
    private Button findPrimeButton;

    /**
     * onCreate-metoden körs när aktiviteten startas. Denna metod sätter layouten
     * för aktiviteten och gör initialiseringar för UI-komponenterna.
     *
     * @param savedInstanceState En Bundle som innehåller tidigare tillstånd för aktiviteten om appen återställs efter att ha stängts.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_management);

        //Hämta referenser
        primeTextView = findViewById(R.id.primeTextView);
        findPrimeButton = findViewById(R.id.findPrimeButton);

        //Ladda in det senaste primtalet från filen när appen startar
        try {
            loadLastPrime();
        } catch (IOException e) {
            primeTextView.setText("Senaste primtalet: Ingen hittad");
        }

        //Hitta nästa primtal när knappen klickas
        findPrimeButton.setOnClickListener(v -> {
            try {
                findNextPrime();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Metod för att hitta nästa primtal. Startar från nästa udda tal och fortsätter
     * tills ett primtal hittas. Därefter uppdateras filen och UI.
     *
     * @throws IOException Om filen inte går att skriva till.
     */
    private void findNextPrime() throws IOException {
        //Hämta senaste primtalet från filen
        long candidate = loadLastPrimeFromFile() + 2;
        while (!isPrime(candidate)) {
            candidate += 2;
        }

        //Spara det senaste primtalet
        savePrimeToFile(candidate);

        //Uppdatera UI med det nya primtalet
        primeTextView.setText("Senaste primtalet: " + candidate);
    }

    /**
     * Metod för att kontrollera om ett tal är ett primtal genom att testa delbarhet.
     *
     * @param candidate Det tal som ska kontrolleras.
     * @return true om talet är ett primtal annars false.
     */
    private boolean isPrime(long candidate) {
        long sqrt = (long)Math.sqrt(candidate);
        for(long i = 3; i <= sqrt; i += 2)
            if(candidate % i == 0) return false;
        return true;
    }

    /**
     * Metod för att läsa det senaste primtalet från filen och visa det i UI:t.
     *
     * @throws IOException Om filen inte går att läsa.
     */
    private void loadLastPrime() throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(getFilesDir() + "/" + FILE));
        String line = reader.readLine();
        if (line != null) {
            primeTextView.setText("Senaste primtalet: " + line);
        } else {
            primeTextView.setText("Senaste primtalet: Ingen hittad");
        }
        reader.close();
    }

    /**
     * Metod för att spara det senaste primtalet till filen.
     *
     * @param prime Det primtal som ska sparas.
     * @throws IOException Om filen inte går att skriva till.
     */
    private void savePrimeToFile(long prime) throws IOException {
        FileWriter writer = new FileWriter(getFilesDir() + "/" + FILE);
        writer.write(String.valueOf(prime));  //Skriv primtalet till filen
        writer.close();
    }

    /**
     * Metod för att läsa det senaste primtalet från filen.
     *
     * @return Det senaste primtalet som lästes från filen, eller 2 om filen inte finns eller är tom.
     * @throws IOException Om filen inte går att läsa.
     */
    private long loadLastPrimeFromFile() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(getFilesDir() + "/" + FILE));
        String line = reader.readLine();
        reader.close();
        return line != null ? Long.parseLong(line) : 2;
    }
}
