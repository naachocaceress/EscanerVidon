package com.example.escanervidon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import Clases.MyReceiver;

public class Pantalla_Actualizar extends AppCompatActivity {

    MyReceiver oMyReceiver;
    Button btn_descargar;

    String url, version;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_actualizar);

        version = getIntent().getStringExtra("version");
        url = getIntent().getStringExtra("url");

        Init();

        btn_descargar = (Button) findViewById(R.id.btn_Actualizar);
        btn_descargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oMyReceiver.Descargar(url);
            }
        });
    }

    private void Init(){
        oMyReceiver = new MyReceiver(Pantalla_Actualizar.this);
        oMyReceiver.registrar(oMyReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        oMyReceiver.borrarRegistro(oMyReceiver);
    }

    @Override
    protected void onResume(){
        super.onResume();
        oMyReceiver.registrar(oMyReceiver);
    }

}