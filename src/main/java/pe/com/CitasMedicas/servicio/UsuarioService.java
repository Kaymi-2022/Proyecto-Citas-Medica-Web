package pe.com.CitasMedicas.servicio;

import java.util.List;

import pe.com.CitasMedicas.model.Usuario;


public interface UsuarioService {

    public List<Usuario> listarUsuarios();

    public void guardar(Usuario usuario);

    public void eliminar(Usuario usuario);

    public Usuario encontrarPersona(Usuario usuario);

}
