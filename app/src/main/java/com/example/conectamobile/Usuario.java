package com.example.conectamobile;

public class Usuario {
    private String id;
    private String nombre;
    private String apellido;
    private String correo;
    private String contrasenia;
    private String nombreUsuario;

    // Constructor vacío requerido para Firebase
    public Usuario() {}

    // Constructor con ID
    public Usuario(String id, String nombre, String apellido, String correo, String contrasenia, String nombreUsuario) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.nombreUsuario = nombreUsuario;
    }

    // Getters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    @Override
    public String toString() {
        return "ID: "+ id + "\nNombre: " + nombre + "\nApellido: " + apellido + "\nCorreo: " + correo
                + "\nContraseña: " + contrasenia + "\nNombre de Usuario: " + nombreUsuario+"\n";
    }
}