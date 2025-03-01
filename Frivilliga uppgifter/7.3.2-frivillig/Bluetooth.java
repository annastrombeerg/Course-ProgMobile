package com.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.Set;

/**
 * Bluetooth är en Android-app som hanterar Bluetooth-funktioner såsom att slå på Bluetooth och lista parade enheter.
 * Appen kräver behörigheter för att interagera med Bluetooth-enheter.
 *
 * OBS! Denna är prövad på en Pixel 8 Pro API 35.
 */
public class Bluetooth extends AppCompatActivity {
    private static final int REQUEST_BLUETOOTH_PERMISSION = 200;
    private BluetoothAdapter bluetoothAdapter;
    private ListView pairedDevicesList;
    private ArrayAdapter<String> adapter;
    private ActivityResultLauncher<Intent> enableBluetoothLauncher;
    Button toggleBluetoothButton, showPairedDevicesButton;


    /**
     * onCreate-metoden körs när aktiviteten startas. Denna metod sätter layouten
     * för aktiviteten och gör initialiseringar för UI-komponenterna.
     * Den hanterar också Bluetooth-tillstånd och initierar hantering av Bluetooth-funktioner.
     *
     * @param savedInstanceState En Bundle som innehåller tidigare tillstånd för aktiviteten om appen återställs efter att ha stängts.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth);

        toggleBluetoothButton = findViewById(R.id.toggleBluetooth);
        showPairedDevicesButton = findViewById(R.id.showPairedDevicesButton);
        pairedDevicesList = findViewById(R.id.pairedDevicesList);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        pairedDevicesList.setAdapter(adapter);

        //Hämta Bluetooth-adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Denna enhet har inte Bluetooth", Toast.LENGTH_LONG).show();
            return;
        }

        //Hantera resultatet från Bluetooth-aktivering via ActivityResultLauncher
        enableBluetoothLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Toast.makeText(this, "Bluetooth aktiverat", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Bluetooth aktivering avbruten", Toast.LENGTH_SHORT).show();
            }
        });

        //Kontrollera och begär Bluetooth-behörigheter
        requestBluetoothPermissions();

        //Hantera Bluetooth av/på
        toggleBluetoothButton.setOnClickListener(v -> enableBluetooth());

        //Visa parade enheter
        showPairedDevicesButton.setOnClickListener(v -> showPairedDevices());
    }

    /**
     * Aktiverar Bluetooth om det inte redan är påslaget.
     */
    private void enableBluetooth() {
        if (bluetoothAdapter == null) {
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            enableBluetoothLauncher.launch(enableBtIntent);
        }
    }

    /**
     * Hämtar parade Bluetooth-enheter och visar dem i en lista.
     * Om inga enheter är parade visas ett meddelande.
     */
    private void showPairedDevices() {
        if (bluetoothAdapter == null) return;
        //Kolla behörighet för Bluetooth
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_PERMISSION);
                return;
            }
        }
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        adapter.clear();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                adapter.add(device.getName() + " - " + device.getAddress());
            }
        } else {
            adapter.add("Inga parade enheter hittades.");
        }
    }

    /**
     * Begär Bluetooth-behörigheter
     */
    private void requestBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_PERMISSION);
            }
        }
    }

    /**
     * Hanterar resultatet av behörighetsförfrågningar.
     * Om behörighet beviljas, uppdateras listan med parade enheter.
     *
     * @param requestCode   Koden för behörighetsbegäran.
     * @param permissions   De begärda behörigheterna.
     * @param grantResults  Resultatet av behörighetsförfrågningen.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showPairedDevices();
            } else {
                Toast.makeText(this, "Bluetooth-behörighet nekades", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
