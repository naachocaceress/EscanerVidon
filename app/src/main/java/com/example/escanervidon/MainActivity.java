package com.example.escanervidon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.window.SplashScreen;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.ScanContract;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    List<Button> btnScanList = new ArrayList<>(); // Lista de botones

    String sucuId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScanList.add(findViewById(R.id.btn1));
        btnScanList.add(findViewById(R.id.btn2));
        btnScanList.add(findViewById(R.id.btn3));
        btnScanList.add(findViewById(R.id.btn4));
        btnScanList.add(findViewById(R.id.btn5));
        btnScanList.add(findViewById(R.id.btn6));
        btnScanList.add(findViewById(R.id.btn7));

        //txtResultado = findViewById(R.id.txtResultado);

        for (final Button btnScan : btnScanList) {
            btnScan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String sucuNombre = btnScan.getText().toString();

                    if (btnScan.getId() == R.id.btn1) {
                        sucuId = "2";
                    } else if (btnScan.getId() == R.id.btn2) {
                        sucuId = "3";
                    } else if (btnScan.getId() == R.id.btn3) {
                        sucuId = "4";
                    }else if (btnScan.getId() == R.id.btn4) {
                        sucuId = "5";
                    }else if (btnScan.getId() == R.id.btn5) {
                        sucuId = "6";
                    }else if (btnScan.getId() == R.id.btn6) {
                        sucuId = "7";
                    }else if (btnScan.getId() == R.id.btn7) {
                        sucuId = "10";
                    }

                    // Aquí puedes usar btnScan como referencia al botón que se hizo clic
                    IntentIntegrator integrador = new IntentIntegrator(MainActivity.this);
                    integrador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                    integrador.setPrompt("Sucursal: " + sucuNombre); // Título
                    integrador.setCameraId(0); // Qué cámara usar
                    integrador.setBeepEnabled(true); // Para que haga sonido
                    //integrador.setTorchEnabled(true); // Flash
                    integrador.setBarcodeImageEnabled(true);
                    integrador.initiateScan();
                }
            });
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                showToast("Lectura cancelada");
            } else {
                String qrContents = result.getContents();
                if (isValidQRCode(qrContents)) {
                    String id = extractIdFromQR(qrContents);
                    if (id != null) {
                        int base = extractBaseFromQR(qrContents);
                        String destino = "https://sistemas.vidonbar.com.ar/VVouchers/validacion.aspx?base=" + base + "&id=" + id + "&sucursal=" + sucuId;

                        Intent intent = new Intent(this, WebActivity.class);
                        intent.putExtra("URL", destino);

                        startActivity(intent);
                    } else {
                        showToast("El QR no es válido");
                    }
                } else {
                    showToast("El QR no es válido");
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private boolean isValidQRCode(String contents) {
        return contents.startsWith("https://www.vidonbar.com.ar/vC.aspx?") || contents.startsWith("https://sistemas.vidonbar.com.ar/vC.aspx?") || contents.startsWith("https://sistemas.vidonbar.com.ar/VVouchers/vC.aspx?");
    }

    private String extractIdFromQR(String contents) {
        String[] urlParts = contents.split("\\?");
        if (urlParts.length > 1) {
            String query = urlParts[1];
            String[] queryParams = query.split("&");
            for (String param : queryParams) {
                if (param.startsWith("id=")) {
                    return param.split("=")[1];
                }
            }
        }
        return null;
    }

    private int extractBaseFromQR(String contents) {
        String[] urlParts = contents.split("\\?");
        if (urlParts.length > 1) {
            String query = urlParts[1];
            String[] queryParams = query.split("&");
            for (String param : queryParams) {
                if (param.startsWith("base=")) {
                    try {
                        return Integer.parseInt(param.split("=")[1]);
                    } catch (NumberFormatException e) {
                        // Manejar el caso en el que base no es un número válido
                        return -1; // Otra señal de que el QR no es válido
                    }
                }
            }
        }
        return -1; // Otra señal de que el QR no es válido
    }


    @Override
    public void onBackPressed() {
        finishAffinity();
    }

}