package com.example.cargarimg;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 123;
    private List<String> imagePaths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void requestPermissions() {
        String[] permissions = {
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
           //     Manifest.permission.ACCESS_FINE_LOCATION,
           //     Manifest.permission.READ_EXTERNAL_STORAGE,
           //     Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_MEDIA_IMAGES
        };

        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
    }



    private void loadImagesFromGallery() {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null
        );

        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            while (cursor.moveToNext()) {
                String imagePath = cursor.getString(columnIndex);
                imagePaths.add(imagePath);
            }
            cursor.close();
        }

        // En este punto, 'imagePaths' contiene las rutas de las imágenes en la galería
        // Puedes acceder a las rutas usando 'imagePaths.get(index)'
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allPermissionsGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
            if (allPermissionsGranted) {
                Toast.makeText(this, "yeeeeee todo concedido+", Toast.LENGTH_SHORT).show();
                loadImagesFromGallery();
            }
        }
    }
}

