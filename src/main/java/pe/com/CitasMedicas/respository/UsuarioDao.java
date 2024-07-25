package pe.com.CitasMedicas.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.com.CitasMedicas.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioDao extends JpaRepository<Usuario, Long> {
    // Obtener el nombre usuario por su name de usuario
    Optional<Usuario> findByUsername(String username);

    boolean existsByUsername(String username);

    // Obtner el todos sus datospor su nombre de usuario
    @Query("SELECT u FROM Usuario  u WHERE username = :username")
    Usuario findByUsernameNative(@Param("username") String username);

    @Query("SELECT COUNT(u) FROM Usuario u JOIN u.roles r WHERE r.nombre = :rolNombre")
    long countByRoleName(@Param("rolNombre") String rolNombre);

    @Query("SELECT u FROM Usuario u JOIN FETCH u.roles")
    List<Usuario> findAllWithRoles();

    @Query("SELECT u.id_usuario, u.username, u.nombre, u.apellido, u.celular, u.correo, u.dni, r.nombre,u.password FROM Usuario u JOIN u.roles r")
    List<Object[]> findAllUsersWithRoleNames();
}
