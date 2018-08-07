package com.example.com.carritocomprasdavid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ViewHolder> {



        List<Producto> productoList;
        Context context;

        public ProductosAdapter(Context context, List<Producto> productoList){
            this.productoList = productoList;
            this.context = context;

        }

        private Context getContext(){
            return context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);

            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            Producto producto = productoList.get(position);

            holder.tvDescripcion.setText(producto.getDescripcion());
            holder.tvPrecio.setText("$ " + producto.getPrecio());







        }

        @Override
        public int getItemCount() {
            return productoList.size();
        }

        public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            @BindView(R.id.tvDescripcion)
            TextView tvDescripcion;
            @BindView(R.id.tvPrecio)
            TextView tvPrecio;


            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {

                Producto producto = productoList.get(getAdapterPosition());

                Bundle bundle = new Bundle();
                bundle.putSerializable("PRODUCTO", producto);
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtras(bundle);
                getContext().startActivity(intent);

            }
        }


}
