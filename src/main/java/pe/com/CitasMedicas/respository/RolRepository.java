package pe.com.CitasMedicas.respository;


import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.CitasMedicas.model.Rol;


public interface RolRepository extends JpaRepository<Rol, Long> {
    Rol findByNombre(String nombre);
}