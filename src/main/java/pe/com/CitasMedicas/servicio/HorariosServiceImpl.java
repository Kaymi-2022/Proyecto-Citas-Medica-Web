package pe.com.CitasMedicas.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.CitasMedicas.model.Horarios;
import pe.com.CitasMedicas.model.Medicos;
import pe.com.CitasMedicas.respository.HorariosDao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class HorariosServiceImpl implements HorariosService {
    @Autowired
    private HorariosDao horariosDao;

    @Override
    public List<Horarios> listarHorarios() {
        return horariosDao.findAll();
    }

    @Override
    public void guardarHorario(Horarios horario) {
        horariosDao.save(horario);
    }

    @Override
    public void eliminarHorario(Horarios horario) {
        horariosDao.delete(horario);
    }

    @Override
    public Optional<Horarios> obtenerHorarioPorId(Long id) {
        return horariosDao.findById(id);
    }

    public List<Horarios> obtenerHorarioPorIdDoctor(Long idMedico) {
        return horariosDao.findAllByMedicoId(idMedico);
    }

    public Horarios obtenerHorario(Long id) {
        return horariosDao.findById(id).orElse(null);
    }

    public int actualizarEstadoHorario(Long estadoDisponible,Long idHorario) {
        return horariosDao.actualizarEstadoHorario(estadoDisponible,idHorario);
    }

    public boolean existsByMedicoAndDiaAndTime(Medicos medicos, LocalDate dia, LocalTime time) {
        return horariosDao.existsByMedicosAndDiaAndTime(medicos, dia, time);
    }

}
