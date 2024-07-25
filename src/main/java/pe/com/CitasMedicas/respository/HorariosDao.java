package pe.com.CitasMedicas.respository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;
import pe.com.CitasMedicas.model.Horarios;
import pe.com.CitasMedicas.model.Medicos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Repository
public interface HorariosDao extends JpaRepository<Horarios, Long> {

    boolean existsByMedicosAndDiaAndTime(Medicos medicos, LocalDate dia, LocalTime time);

    @Query("SELECT h FROM Horarios h WHERE h.medicos.id = :idMedico")
    List<Horarios> findAllByMedicoId(@Param("idMedico") Long idMedico);

    @Query("SELECT h FROM Horarios h")
    List<Horarios> ListarTodo();

    @Modifying
    @Transactional
    @Query("UPDATE Horarios h SET h.idestadoCita = :estadoDisponible WHERE h.id = :idHorario")
    int actualizarEstadoHorario(@Param("estadoDisponible") Long estadoDisponible,@Param("idHorario") Long idHorario);
}
