package com.example.taller5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;

public class HomeActivity extends AppCompatActivity {
    Button btnsalir, btncamara, btngaleria, btnenviar;
    ImageView imageView;
    String pathImagen;
    Uri uriImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btncamara = (Button) findViewById(R.id.btncamara);
        btngaleria = (Button) findViewById(R.id.btngaleria);
        btnenviar = (Button) findViewById(R.id.btnenviar);
        btnsalir = (Button) findViewById(R.id.btnsalir);
        imageView = (ImageView) findViewById(R.id.imageview);

        btncamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCamara();
            }
        });
        btnsalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent para "cerrar sesion" e ir de vuelta a login
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        btngaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent para abrir la galeria
                Intent intentGaleria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentGaleria, 2);
                //request code 2 para poder diferenciarlo del request code de la camara
            }
        });

        btnenviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCompartir = new Intent(Intent.ACTION_SEND);
                intentCompartir.setType("image/*");

                if (uriImg != null){
                    intentCompartir.putExtra(Intent.EXTRA_STREAM, uriImg);
                    try {
                        startActivity(intentCompartir);
                    }catch (Exception exception){
                        Toast.makeText(getApplicationContext(), "Erroar al enviar", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "No se ha seleccionado una imagen", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //evaluo el request code, si es 1 carga la imagen elegida en la galeria
        //si es 2 usa la imagen capturada por la camara
        if (requestCode == 1 && resultCode == RESULT_OK) {
            uriImg = uriImg.parse(pathImagen);
            imageView.setImageURI(uriImg);
            }else if (requestCode == 2 && resultCode == RESULT_OK && data != null){
            uriImg = data.getData();
            imageView.setImageURI(uriImg);
        }
    }

    private void abrirCamara(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            File archivoImagen = null;
            try {
                archivoImagen = crearImagen();
            }catch (IOException exception){
                Log.e("Error", exception.toString());
            }
            if (archivoImagen != null){
                Uri imagenUri = FileProvider.getUriForFile(this, "com.example.proyectotaller", archivoImagen);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imagenUri);
                startActivityForResult(intent, 1);
            }
        }
    }

    private File crearImagen() throws IOException {
        //creo un archivo temporal donde luego se guardara la imagen obtenida con la camara
        String nombre = "IMAGEN_";
        File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile(nombre, ".jpg", directorio);
        pathImagen = imagen.getAbsolutePath();
        return imagen;
    }
}