package pe.com.CitasMedicas.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pe.com.CitasMedicas.model.Medicos;

import java.util.List;

@Repository
public interface MedicoDao extends JpaRepository<Medicos, Long>{
    @Query("SELECT m FROM Medicos m WHERE m.especialidad.id = :idConsultorio")
    List<Medicos> findMedicosByConsultorio(@Param("idConsultorio") Long idConsultorio);

    @Query("SELECT m FROM Medicos m WHERE m.nombre = :nombre")
    Medicos findMedicoByNombre(@Param("nombre") String nombre);
}
