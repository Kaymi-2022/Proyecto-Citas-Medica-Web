package pe.com.CitasMedicas.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import pe.com.CitasMedicas.model.Rol;
import pe.com.CitasMedicas.model.Usuario;
import pe.com.CitasMedicas.respository.UsuarioDao;
import java.util.List;
import java.util.Optional;


@Service
public class UsuarioServiceImpl implements UserDetailsService, UsuarioService {

    @Autowired
    public UsuarioDao usuarioDao;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> user = usuarioDao.findByUsername(username);

        if (user.isPresent()) {
            var userObject = user.get();
            return User.builder()
                    .username(userObject.getUsername())
                    .password(userObject.getPassword())
                    .roles(getRoles(userObject))
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    private String[] getRoles(Usuario user) {
        if (user.getRoles() == null) {
            return new String[]{"USER"};
        }
        return user.getRoles().stream()
                .map(Rol::getNombre)
                .toArray(String[]::new);
    }
    
    @Override
    @Transactional
    public List<Usuario> listarUsuarios() {
        return usuarioDao.findAll();
    }

    @Override
    @Transactional
    public void guardar(Usuario usuario) {
        usuarioDao.save(usuario);
    }

    @Override
    @Transactional
    public void eliminar(Usuario usuario) {
        usuarioDao.delete(usuario);
    }

    @Override
    @Transactional
    public Usuario encontrarPersona(Usuario usuario) {
        return usuarioDao.findById(usuario.getId_usuario()).orElse(null);
    }

    public long countUsersWithAdminRole(String rol) {
        return usuarioDao.countByRoleName(rol);
    }

    @Transactional
    public void eliminarUsuario(Long idUsuario) {
        usuarioDao.deleteById(idUsuario);
    }

    public List<Object[]> findAllUsersWithRoleNames() {
        return usuarioDao.findAllUsersWithRoleNames();
    }

    // Método adicional para obtener el nombre del usuario
    public String getNombre(String username) {
        Optional<Usuario> name = usuarioDao.findByUsername(username);
        Usuario user = name.get();
        return user != null ? user.getNombre() : null;
    }
    // Método adicional para obtener el id del usuario
    public Usuario getUsuario(String username) {
        return usuarioDao.findByUsernameNative(username);
    }

    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioDao.existsByUsername(usuario.getUsername())) {
            throw new UsuarioExistenteException("El nombre de usuario ya existe.");
        }
        return usuarioDao.save(usuario);
    }
}
