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
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityVisualizar extends AppCompatActivity {

    private static final int TOMA_VIDEOo = 1;
    private VideoView vv2;
    Button btnReproducir;
    Button btnRegresar;
    Spinner sp2;
    private String[] listaa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar);

        vv2 = findViewById(R.id.videoView4);

        sp2 = findViewById(R.id.spinner2);

        listaa = fileList();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaa);
        sp2.setAdapter(adapter);



        btnRegresar = findViewById(R.id.btnRegresar);
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intt = new Intent(ActivityVisualizar.this, MainActivity.class);
                startActivity(intt);
                finish();
            }
        });

        btnReproducir = findViewById(R.id.btnReproducir);
        btnReproducir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = sp2.getSelectedItemPosition();
                vv2.setVideoPath(getFilesDir()+ "/" + listaa[pos]);
                vv2.start();
            }
        });
    }

    /*public void tomarVideo(View v){

    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TOMA_VIDEOo && resultCode == RESULT_OK){
            Uri videoUri = data.getData();
            vv2.setVideoURI(videoUri);
            vv2.start();

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