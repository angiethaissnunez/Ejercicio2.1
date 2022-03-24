package com.aplicacion.ejercicio22;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int TOMA_VIDEO = 1;
    private VideoView vv1;
    Button btnTomarVideo;
    Button btnVerVideo;
    Button btnIrGaleria;
    Spinner sp1;
    private String[] lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vv1 = findViewById(R.id.videoView);

        sp1 = findViewById(R.id.spinner);

        lista = fileList();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lista);
        sp1.setAdapter(adapter);


        btnTomarVideo = findViewById(R.id.btnTomarVideo);

        btnTomarVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(intent, TOMA_VIDEO);
            }
        });

        btnVerVideo = findViewById(R.id.btnVerVideo);
        btnVerVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               int pos = sp1.getSelectedItemPosition();
               vv1.setVideoPath(getFilesDir()+ "/" + lista[pos]);
               vv1.start();
            }
        });

        btnIrGaleria = findViewById(R.id.btnIrGaleria);
        btnIrGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, ActivityVisualizar.class);
                startActivity(in);
            }
        });
    }

    /*public void tomarVideo(View v){

    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TOMA_VIDEO && resultCode == RESULT_OK){
            Uri videoUri = data.getData();
            vv1.setVideoURI(videoUri);
            vv1.start();

            try {
                AssetFileDescriptor videoAsset = getContentResolver().openAssetFileDescriptor(data.getData(),"r");
                FileInputStream in = videoAsset.createInputStream();
                FileOutputStream archivo = openFileOutput(crerNombreArchivoMP4(), Context.MODE_PRIVATE);
                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf))>0){
                    archivo.write(buf, 0, len);
                }
            } catch (IOException e){
                Toast.makeText(this, "Problemas en la grabación", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String crerNombreArchivoMP4(){
        String fecha = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombre = fecha + ".mp4";
        return nombre;
    }
}