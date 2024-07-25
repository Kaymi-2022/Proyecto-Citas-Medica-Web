package pe.com.CitasMedicas.respository;


import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.CitasMedicas.model.EstadoCita;

public interface EstadoDao  extends JpaRepository<EstadoCita, Long> {
}
