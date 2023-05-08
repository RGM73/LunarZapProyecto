package com.example.myapplication;

import java.util.Date;
import java.util.List;

public class ZAP {

    private String Contenido;
    private Date Fecha;
    private String Usuario;
    private int likes;
    private int dislikes;
    private List<String> comentarios;

    public ZAP(String contenido,Date fecha, String usuario, int likes, int dislikes, List<String> comentarios) {
        Contenido = contenido;
        Usuario = usuario;
        Fecha = fecha;
        this.likes = likes;
        this.dislikes = dislikes;
        this.comentarios = comentarios;
    }
    //getter and setter
    public String getContenido() {
        return Contenido;
    }
    public void setContenido(String contenido) {
        Contenido = contenido;
    }
    public Date getFecha() {
        return Fecha;
    }
    public void setFecha(Date fecha) {
        Fecha = fecha;
    }
    public String getUsuario() {
        return Usuario;
    }
    public void setUsuario(String usuario) {
        Usuario = usuario;
    }
    public int getLikes() {
        return likes;
    }
    public void setLikes(int likes) {
        this.likes = likes;
    }
    public int getDislikes() {
        return dislikes;
    }
    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }
    public List<String> getComentarios() {
        return comentarios;
    }
    public void setComentarios(List<String> comentarios) {
        this.comentarios = comentarios;
    }


}
