package pe.com.CitasMedicas.servicio;


import java.util.List;
import java.util.Optional;

import pe.com.CitasMedicas.model.Horarios;

public interface HorariosService {
    public List<Horarios> listarHorarios();
    public void guardarHorario(Horarios horario);
    public void eliminarHorario(Horarios horario);
    public Optional<Horarios> obtenerHorarioPorId(Long id);
}
