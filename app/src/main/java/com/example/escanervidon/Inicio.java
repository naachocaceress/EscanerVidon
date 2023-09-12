package com.example.escanervidon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import Clases.MyReceiver;

public class Inicio extends AppCompatActivity {

    MyReceiver oMyReceiver;

    Button sig;
    Button btn_descargar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        Init();

        btn_descargar = (Button) findViewById(R.id.btn_Descargar);
        btn_descargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oMyReceiver.Descargar();
            }
        });


        sig = (Button)findViewById(R.id.button);
    sig.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Inicio.this, MainActivity.class);
            startActivity(intent);
        }
    });
    }

    private void Init(){
        oMyReceiver = new MyReceiver(Inicio.this);
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