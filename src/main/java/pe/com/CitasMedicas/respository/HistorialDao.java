package pe.com.CitasMedicas.respository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.com.CitasMedicas.model.HistorialCitados;

public interface HistorialDao extends JpaRepository<HistorialCitados, Long> {

    @Query("SELECT h FROM HistorialCitados h WHERE idpaciente = :idpaciente")
    List<HistorialCitados> findByIdCitados(@Param("idpaciente") Long idpaciente);

    @Query("SELECT h FROM HistorialCitados h WHERE idpaciente = :idpaciente and situacion= :situacion")
    List<HistorialCitados> findByIdpacienteAndIdestado(Long idpaciente, String  situacion);

    @Query("SELECT h.consultorio, COUNT(h) FROM HistorialCitados h GROUP BY h.consultorio")
    List<Object[]> findTotalCitadosByEspecialidad();

    @Query("SELECT h.situacion, COUNT(h) FROM HistorialCitados h GROUP BY h.situacion")
    List<Object[]> findSituacionByConsultorio();

    @Query("SELECT FUNCTION('MONTH', h.fecha), FUNCTION('YEAR', h.fecha), COUNT(h) FROM HistorialCitados h GROUP BY FUNCTION('YEAR', h.fecha), FUNCTION('MONTH', h.fecha) ORDER BY FUNCTION('YEAR', h.fecha), FUNCTION('MONTH', h.fecha)")
    List<Object[]> findCountByMonth();

    @Query("SELECT FUNCTION('DAY', h.fecha), FUNCTION('MONTH', h.fecha), FUNCTION('YEAR', h.fecha), COUNT(h) FROM HistorialCitados h GROUP BY FUNCTION('YEAR', h.fecha), FUNCTION('MONTH', h.fecha), FUNCTION('DAY', h.fecha) ORDER BY FUNCTION('YEAR', h.fecha), FUNCTION('MONTH', h.fecha), FUNCTION('DAY', h.fecha)")
    List<Object[]> findCountByDay();

}
