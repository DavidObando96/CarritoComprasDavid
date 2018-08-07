package com.example.com.carritocomprasdavid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ConsultarActivity extends AppCompatActivity {

    private Button btAgregar;
    private DatabaseReference ref;
    private StorageReference stor;
    private RecyclerView rvProductos;
    private List<Producto> productoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar);
        ref = FirebaseDatabase.getInstance().getReference();
        stor = FirebaseStorage.getInstance().getReference();
    }

    public void onStart() {
        super.onStart();
        rvProductos = findViewById(R.id.rvProductos);
        btAgregar = findViewById(R.id.btAñadir);
        btAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ir();
            }
        });
        consultar();
    }

    private void ir() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void llenarRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvProductos.setHasFixedSize(true);
        rvProductos.setLayoutManager(llm);
        ProductosAdapter adapter = new ProductosAdapter(this, productoList);
        rvProductos.setAdapter(adapter);
    }

    private void consultar() {
        ref.child("productos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productoList = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    final Producto producto = postSnapshot.getValue(Producto.class);
                    producto.setCodigo(postSnapshot.getKey());
                    System.out.println(producto.toString());
                    productoList.add(producto);
                }

                llenarRecyclerView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    private void cargarImagen(Producto producto) {


    }


}
