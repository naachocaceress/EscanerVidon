package com.example.escanervidon;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Pantalla_Principal extends AppCompatActivity {
    String version_actual = "1.4.2";
    String version_firebase;
    String url_firebase;

    TextView txtVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_principal);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        txtVersion = (TextView) findViewById(R.id.txtVersion);

        txtVersion.setText("Version " + version_actual);

        if (isInternetConnected()) {
            Obtener_Firebase();
        } else {
            setContentView(R.layout.sin_conexion);
        }


    }

    private boolean isInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }

    private void Obtener_Firebase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference referencia_version, referencia_url;

        referencia_url=database.getReference("url");
        referencia_url.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                url_firebase = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Pantalla_Principal.this, "URL " + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        referencia_version = database.getReference("version");
        referencia_version.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                version_firebase = snapshot.getValue().toString();

                if(version_firebase.trim().equals(version_actual.trim())){
                    Intent intent = new Intent(Pantalla_Principal.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent pantallaActualizar = new Intent(getApplicationContext(), Pantalla_Actualizar.class);
                    pantallaActualizar.putExtra("version", version_firebase);
                    pantallaActualizar.putExtra("url", url_firebase);
                    finish();
                    startActivity(pantallaActualizar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Pantalla_Principal.this, "Version " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
