package com.example.com.carritocomprasdavid;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private StorageReference storage = FirebaseStorage.getInstance().getReference();
    private DatabaseReference ref = database.getReference();
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    private EditText etCodigo;
    private EditText etDescripcion;
    private EditText etPrecio;
    private EditText etCategoria;
    private CheckBox chbEstado;
    private Button btnGuardar;
    private Button btnEliminar;
    private ImageView ivProducto;
    private FloatingActionButton fabTomar;
    private Producto producto = new Producto();
    int contador = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onStart(){
        super.onStart();


        solicitarPermiso();

        etCodigo = findViewById(R.id.etCodigo);
        etDescripcion = findViewById(R.id.etDescripcion);
        etPrecio = findViewById(R.id.etPrecio);
        etCategoria = findViewById(R.id.etTipoProducto);
        chbEstado = findViewById(R.id.chbEstado);
        ivProducto = findViewById(R.id.ivProduto);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnEliminar = findViewById(R.id.btnEliminar);
        fabTomar = findViewById(R.id.fabTomar);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardar();
            }
        });


        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(producto != null){
                    eliminar();
                }
            }
        });

        fabTomar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraIntent();
            }
        });

        getIntentData();


    }


    private static final int PERMISSION_REQUEST_CODE = 1;

    private void solicitarPermiso() {


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {android.Manifest.permission.CAMERA};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);

            }
        }
    }

    private void ir(){
        Intent intent = new Intent(this, ConsultarActivity.class);
        startActivity(intent);
    }



    private void eliminar() {
        storage.child("imagenes").child(producto.getCodigo()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ref.child("productos").child(producto.getCodigo()).removeValue(new CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        limpiar();
                        ir();
                    }
                });
            }
        });
    }



    private void getIntentData(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            producto = (Producto) extras.getSerializable("PRODUCTO");
            etCodigo.setText(producto.getCodigo());
            etDescripcion.setText(producto.getDescripcion());
            etPrecio.setText(producto.getPrecio() + "");
            etCategoria.setText(producto.getTipoproducto());
            chbEstado.setChecked(producto.isEstado());

            if(contador == 0){
                StorageReference stor = FirebaseStorage.getInstance().getReference();
                stor.child("imagenes/" + producto.getCodigo()).getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        producto.setImagen(bitmap);
                        ivProducto.setImageBitmap(producto.getImagen());
                    }
                });
                contador++;
            }
        }

    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE
        );
        if(pictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pictureIntent,
                    REQUEST_CAPTURE_IMAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == REQUEST_CAPTURE_IMAGE &&
                resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                ivProducto.setImageBitmap(imageBitmap);
            }
        }
    }



    public void guardar(){

        producto.setCodigo(etCodigo.getText().toString());
        producto.setDescripcion(etDescripcion.getText().toString());
        producto.setPrecio(Double.parseDouble(etPrecio.getText().toString()));
        producto.setTipoproducto(etCategoria.getText().toString());
        producto.setEstado(chbEstado.isChecked());
        ivProducto.buildDrawingCache();
        producto.setImagen(ivProducto.getDrawingCache());
        guardarFirebase(producto);

    }

    public void guardarFirebase(final Producto producto){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        producto.getImagen().compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = storage.child("imagenes").child(producto.getCodigo()).putBytes(data);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                guardarProducto(producto);
            }
        });
    }

    public void guardarProducto(Producto producto){
        ref.child("productos").child(producto.getCodigo()).setValue(producto, new CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                limpiar();
                ir();
            }
        });
    }

    public void limpiar(){
        etCodigo.setText("");
        etDescripcion.setText("");
        etPrecio.setText("");
        etCategoria.setText("");
        chbEstado.setChecked(false);
        ivProducto.setImageResource(R.drawable.common_full_open_on_phone);
    }




}
