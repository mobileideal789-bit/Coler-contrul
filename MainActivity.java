package com.colercontrol;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
package com.example.coolercontrol;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    // ===== متغیرهای بلوتوث =====
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;
    private boolean isConnected = false;

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String DEVICE_ADDRESS = "XX:XX:XX:XX:XX:XX"; // ← آدرس HC-06 رو بذار

    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStatus = findViewById(R.id.tvStatus);

        Button btnConnect = findViewById(R.id.btnConnect);
        Button btnPump = findViewById(R.id.btnPump);
        Button btnSlow = findViewById(R.id.btnSlow);
        Button btnFast = findViewById(R.id.btnFast);
        Button btnMode = findViewById(R.id.btnMode);
        Button btnTimer = findViewById(R.id.btnTimer);
        Button btnStatus = findViewById(R.id.btnStatus);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        btnConnect.setOnClickListener(v -> connectToBluetooth());
        btnPump.setOnClickListener(v -> sendCommand("1"));
        btnSlow.setOnClickListener(v -> sendCommand("2"));
        btnFast.setOnClickListener(v -> sendCommand("3"));
        btnMode.setOnClickListener(v -> sendCommand("4"));
        btnTimer.setOnClickListener(v -> sendCommand("T"));
        btnStatus.setOnClickListener(v -> sendCommand("s"));
    }

    private void connectToBluetooth() {
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "❌ بلوتوث پشتیبانی نمی‌شود!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "⚠️ لطفاً بلوتوث را روشن کنید!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(DEVICE_ADDRESS);
            bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            bluetoothSocket.connect();
            outputStream = bluetoothSocket.getOutputStream();
            isConnected = true;
            tvStatus.setText("✅ متصل شد!");
            Toast.makeText(this, "✅ به بلوتوث وصل شد!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            isConnected = false;
            tvStatus.setText("❌ قطع");
            Toast.makeText(this, "❌ خطا در اتصال!", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendCommand(String command) {
        if (!isConnected) {
            Toast.makeText(this, "⚠️ ابتدا به بلوتوث وصل شوید!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            outputStream.write(command.getBytes());
            Toast.makeText(this, "📤 ارسال: " + command, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "❌ خطا در ارسال!", Toast.LENGTH_SHORT).show();
        }
    }
}
