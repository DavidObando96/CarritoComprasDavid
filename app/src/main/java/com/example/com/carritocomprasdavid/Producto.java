package com.example.com.carritocomprasdavid;

import android.graphics.Bitmap;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Producto implements Serializable {

    @Exclude
    private String codigo;
    private String descripcion;
    private double precio;
    private String tipoproducto;
    private boolean estado;
    @Exclude
    private Bitmap imagen;

    public Producto(){}

    public Producto(String codigo, String descripcion, double precio, String tipoproducto, boolean estado) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.tipoproducto = tipoproducto;
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getTipoproducto() {
        return tipoproducto;
    }

    public void setTipoproducto(String tipoproducto) {
        this.tipoproducto = tipoproducto;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Exclude
    public Bitmap getImagen() {
        return imagen;
    }

    @Exclude
    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }
}
