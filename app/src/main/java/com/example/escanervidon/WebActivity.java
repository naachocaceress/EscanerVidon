package com.example.escanervidon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        webView = findViewById(R.id.webView);

        // Configura el cliente de WebView para cargar las páginas dentro de la WebView
        webView.setWebViewClient(new WebViewClient());

        // Habilita JavaScript (si es necesario)
        webView.getSettings().setJavaScriptEnabled(true);

        // Obtén la URL del extra del Intent
        String url = getIntent().getStringExtra("URL");

        // Carga la URL en la WebView
        webView.loadUrl(url);
    }

    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}