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

public class MyReceiver extends BroadcastReceiver {
    private static final String ACTION_DOWNLOAD_COMPLETE = DownloadManager.ACTION_DOWNLOAD_COMPLETE;
    private static final String AUTHORITY = "com.example.escanervidon.provider";

    private DownloadManager my_DownloadManager;
    private long tamaño;
    private IntentFilter my_IntentFilter;

    private Context my_context;
    private Activity my_activity;

    public MyReceiver(Activity activity_) {
        this.my_context = activity_;
        this.my_activity = activity_;

        my_IntentFilter = new IntentFilter();
        my_IntentFilter.addAction(ACTION_DOWNLOAD_COMPLETE);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Evento_Action", intent.getAction());

        String action = intent.getAction();

        if (ACTION_DOWNLOAD_COMPLETE.equals(action)) {
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);

            Cursor cursor = my_DownloadManager.query(query);

            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex)) {
                    @SuppressLint("Range") String uriString = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    File file = new File(Uri.parse(uriString).getPath());

                    Intent installIntent = new Intent(Intent.ACTION_VIEW);
                    installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    Uri apkUri = FileProvider.getUriForFile(context, AUTHORITY, file);

                    installIntent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                    installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    context.startActivity(installIntent);

                    Log.e("MsjDescarga", "se descargó sin problemas");
                }
            }
        }
    }

    public void Descargar(String url, String fileName) {
        DownloadManager.Request my_Request;

        my_DownloadManager = (DownloadManager) my_context.getSystemService(Context.DOWNLOAD_SERVICE);

        my_Request = new DownloadManager.Request(Uri.parse(url));
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(url);

        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File destinationFile = new File(directory, fileName);

        my_Request.setDestinationUri(Uri.fromFile(destinationFile));
        Log.e("ruta_apk", destinationFile.getAbsolutePath());
        Log.e("Descargar", "OK");

        tamaño = my_DownloadManager.enqueue(my_Request);
    }

    public void registrar(MyReceiver oMyReceiver) {
        my_context.registerReceiver(oMyReceiver, my_IntentFilter);
    }

    public void borrarRegistro(MyReceiver oMyReceiver) {
        my_context.unregisterReceiver(oMyReceiver);
    }
}
