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

        if(result != null){
            if(result.getContents() == null){
                Toast.makeText(this, "Lectura cancelada", Toast.LENGTH_LONG).show();
            }
            else {
                if(result.getContents().startsWith("https://www.vidonbar.com.ar/vC.aspx?")) {
                    String[] urlParts = result.getContents().split("\\?");
                    String baseQueryParam = urlParts.length > 1 ? urlParts[1] : ""; // Obtener la parte después del '?'

                    String base = null;
                    if (baseQueryParam.startsWith("base=1")) {
                        base = "1";
                    } else if (baseQueryParam.startsWith("base=2")) {
                        base = "2";
                    } else if (baseQueryParam.startsWith("base=3")) {
                        base = "3";
                    }

                    if (base != null) {
                        String id = null;

                        if (urlParts.length > 1) {
                            String query = urlParts[1]; // Obtener la parte de la cadena de consulta
                            String[] queryParams = query.split("&"); // Dividir en parámetros

                            // Iterar a través de los parámetros y encontrar el que comienza con "id="
                            for (String param : queryParams) {
                                if (param.startsWith("id=")) {
                                    id = param.split("=")[1]; // Extraer el valor del parámetro "id"
                                    break; // Salir del bucle una vez que se encuentra el valor
                                }
                            }
                        }

                        if (id != null) {
                            String destino = "https://www.vidonbar.com.ar/validacion.aspx?base=" + base + "&id=" + id + "&sucursal=" + sucuId;

                            Intent intent = new Intent(this, WebActivity.class);
                            intent.putExtra("URL", destino);

                            startActivity(intent);
                        } else {
                            Toast.makeText(this,"El QR no es valido", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(this,"El QR no es valido", Toast.LENGTH_LONG).show();
                    }
                }else
                    Toast.makeText(this,"El QR no es valido", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

}






















