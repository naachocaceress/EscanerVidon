package Clases;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;

import androidx.core.content.FileProvider;

import java.io.File;

public class MyReceiver  extends BroadcastReceiver {
    DownloadManager my_DowloadManager;
    long tamaño;
    IntentFilter my_IntentFilter;

    private Context my_context;
    private Activity my_activity;

    public MyReceiver(Activity activity_){
        this.my_context = activity_;
        this.my_activity = activity_;

        my_IntentFilter = new IntentFilter();
        my_IntentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Evento_Action", intent.getAction());

        String action = intent.getAction();

        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
            intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(tamaño);

            Cursor cursor = my_DowloadManager.query(query);

            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                if (columnIndex != -1) {
                if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex)) {
                    String uriString = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    File file = new File(Uri.parse(uriString).getPath());

                    Intent pantallaInstall = new Intent(Intent.ACTION_VIEW);
                    pantallaInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    Uri apkUri = FileProvider.getUriForFile(context, "com.example.escanervidon.provider", file);

                    pantallaInstall.setDataAndType(apkUri, "application/vnd.android.package-archive");
                    pantallaInstall.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Añadir permiso de lectura URI

                    context.startActivity(pantallaInstall);

                    Log.e("MsjDescarga", "se descargó sin problemas");
                }

            } else {
                // La columna no existe en el cursor, maneja el error o realiza alguna acción adecuada.
                Log.e("Error", "La columna COLUMN_STATUS no existe en el cursor");
            }
            }
        }
    }


    public void Descargar() {
        String url = "";
        DownloadManager.Request my_Request;

        my_DowloadManager=(DownloadManager) my_context.getSystemService(Context.DOWNLOAD_SERVICE);

        my_Request= new DownloadManager.Request(Uri.parse(url));
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(url);
        String name = URLUtil.guessFileName(url, null, fileExtension);

        //crear la carpeta
        File miFile = new File(Environment.getExternalStorageDirectory(), "apk");
        boolean isCreada = miFile.exists();

        if(isCreada == false){
            isCreada = miFile.mkdirs();
        }

        my_Request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name);
        String h = my_Request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name).toString();

        Log.e("ruta_apk", h);
        Log.e("Descargar", "OK");
        tamaño = my_DowloadManager.enqueue(my_Request);

    }

    public void  registrar(MyReceiver oMyReceiver){
        my_context.registerReceiver(oMyReceiver, my_IntentFilter);
    }

    public void borrarRegistro(MyReceiver oMyReceiver){
        my_context.unregisterReceiver(oMyReceiver);
    }
}
