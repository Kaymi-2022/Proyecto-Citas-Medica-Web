package pe.com.CitasMedicas.respository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pe.com.CitasMedicas.model.Especialidad;

@Repository
public interface EspecialidadDao extends JpaRepository<Especialidad, Long> {

    @Query("SELECT e FROM Especialidad e WHERE e.nombre = :nombre")
    Especialidad findEspecialidadByNombre(@Param("nombre") String nombre);

}
