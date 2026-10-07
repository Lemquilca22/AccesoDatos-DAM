package dao;

import model.Usuario;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UsuarioDAO {
    private final Map<Integer, Usuario> tabla = new LinkedHashMap<>();
    private int siguienteId = 1;

    public Usuario insertar(Usuario usuario) {
        usuario.setId_usuario(siguienteId++);
        tabla.put(usuario.getId_usuario(), usuario);
        return usuario;
    }

    public Usuario obtenerPorId(int idUsuario) {
        return tabla.get(idUsuario);
    }

    public Usuario obtenerPorEmail(String email) {
        for (Usuario u : tabla.values()) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }

    public Usuario login(String email, String password) {
        Usuario u = obtenerPorEmail(email);
        if (u != null && u.getPassword() != null && u.getPassword().equals(password)) {
            return u;
        }
        return null;
    }

    public List<Usuario> buscarPorNombre(String texto) {
        List<Usuario> resultado = new ArrayList<>();
        for (Usuario u : tabla.values()) {
            if (u.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                resultado.add(u);
            }
        }
        return resultado;
    }

    public List<Usuario> obtenerTodos() {
        return new ArrayList<>(tabla.values());
    }

    public boolean actualizar(Usuario usuario) {
        if (!tabla.containsKey(usuario.getId_usuario())) {
            return false;
        }
        tabla.put(usuario.getId_usuario(), usuario);
        return true;
    }

    public boolean eliminar(int idUsuario) {
        return tabla.remove(idUsuario) != null;
    }
}
