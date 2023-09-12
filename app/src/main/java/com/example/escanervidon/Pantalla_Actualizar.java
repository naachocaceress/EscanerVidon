package com.example.escanervidon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import Clases.MyReceiver;

public class Pantalla_Actualizar extends AppCompatActivity {

    private MyReceiver downloadReceiver;
    private Button btnDescargar;

    private String url, version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_actualizar);

        version = getIntent().getStringExtra("version");
        url = getIntent().getStringExtra("url");

        downloadReceiver = new MyReceiver(Pantalla_Actualizar.this);
        downloadReceiver.registrar(downloadReceiver);

        btnDescargar = findViewById(R.id.btn_Actualizar);
        btnDescargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadReceiver.Descargar(url, "nombre_del_archivo.apk");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        downloadReceiver.borrarRegistro(downloadReceiver);
    }

    @Override
    protected void onResume(){
        super.onResume();
        downloadReceiver.registrar(downloadReceiver);
    }
}
