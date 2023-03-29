package com.example.camera;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        foto = findViewById(R.id.foto);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
    }

    public void tirarFoto(View v) {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        abrirCamera.launch(i);
    }

    public void pegaFoto(View v) {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        abrirCamera.launch(i);
    }

    ActivityResultLauncher<Intent> abrirCamera = registerForActivityResult
            (new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    Bundle dado = data.getExtras();
                    Bitmap image = (Bitmap) dado.get("data");
                    foto.setImageBitmap(image);
                }
            });
    ActivityResultLauncher<Intent> abrirGaleria  = registerForActivityResult
            (new ActivityResultContracts.StartActivityForResult(), result ->{
                if(result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    Uri imagemSelecionada = data.getData();
                    String[] caminho = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(imagemSelecionada, caminho , null, null, null);
                    c.moveToFirst();
                    int coluna = c.getColumnIndex(caminho[0]);
                    String caminhoFisico = c.getString(coluna);
                    c.close();
                    Bitmap imagem = (BitmapFactory.decodeFile(caminhoFisico));
                    foto.setImageBitmap(imagem);
                }
            });


}
