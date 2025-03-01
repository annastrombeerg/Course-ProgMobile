package com.paint;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;

/**
 * Paint är en Android-app som ritar en linjegraf med hjälp av AndroidPlot.
 * Den genererar slumpmässiga värden och visar dem i en XY-plot.
 *
 * OBS! Denna är prövad på en Pixel 8 Pro API 35.
 * OBS OBS!! Lägg till "implementation("com.androidplot:androidplot-core:1.5.10")" i build.gradle.kts (Module: app)
 */
public class Paint extends AppCompatActivity {
    private XYPlot plot;

    /**
     * onCreate-metoden körs när aktiviteten startas. Denna metod sätter layouten
     * för aktiviteten och gör initialiseringar för UI-komponenterna.
     * Den initierar grafen och genererar data.
     *
     * @param savedInstanceState En Bundle som innehåller tidigare tillstånd för aktiviteten om appen återställs efter att ha stängts.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paint);

        //Hämta referensen till XYPlot från layouten
        plot = findViewById(R.id.xyPlot);

        //Ställ in grafens titel och etiketter
        plot.setTitle("Slumpmässig Linjegraf");
        plot.setDomainLabel("Tid");
        plot.setRangeLabel("Värde");

        //Formatera värden på X- och Y-axlarna
        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new DecimalFormat("0")); // X-axel
        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).setFormat(new DecimalFormat("0"));   // Y-axel

        //Generera slumpmässiga data för grafen
        Number[] xValues = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Number[] yValues = generateRandomData(10);

        //Skapa en XYSeries för att hålla datavärdena
        XYSeries series = new SimpleXYSeries(
                Arrays.asList(xValues),
                Arrays.asList(yValues),
                "Slumpmässig Kurva"
        );

        //Skapa en formatter för att styla linjen
        LineAndPointFormatter formatter = new LineAndPointFormatter();
        formatter.setPointLabelFormatter(null);

        //Lägg till serien till grafen
        plot.addSeries(series, formatter);

        //Ställ in axelgränser
        plot.setRangeBoundaries(0, 100, BoundaryMode.FIXED);
        plot.setDomainBoundaries(1, 10, BoundaryMode.FIXED);

        //Uppdatera grafen
        plot.redraw();
    }

    /**
     * Genererar en array av slumpmässiga tal mellan 10 och 100.
     * Används för att skapa Y-värden för grafen.
     *
     * @param count Antalet värden att generera.
     * @return En array av slumpmässiga tal.
     */
    private Number[] generateRandomData(int count) {
        Number[] values = new Number[count];
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            values[i] = 10 + random.nextInt(90); //Mellan 10 och 100
        }
        return values;
    }
}
