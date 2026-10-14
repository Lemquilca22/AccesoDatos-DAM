package model;
import java.io.Serializable;

public class Usuario implements Serializable{
    private static final long serialVersionUID = 1L;//Consultar como y para que sirve esto

    private int idUsuario;
    private String nombre;
    private String email;
    private String telefono;
    private String dirección;
    private String contraseña;
    private Rol rol; // Definimos de la clase Enum (servirá para la DB a futuro)

    public Usuario(int idUsuario, String nombre, String email, String telefono, String dirección, String contraseña, Rol rol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.dirección = dirección;
        this.contraseña = contraseña;
        this.rol = rol;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDirección() {
        return dirección;
    }

    public void setDirección(String dirección) {
        this.dirección = dirección;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "Usuario [ID=" + idUsuario + ", Nombre=" + nombre +
                ", Email=" + email + ", Rol=" + rol + "]";
    }
}
