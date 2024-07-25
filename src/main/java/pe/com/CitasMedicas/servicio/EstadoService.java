package pe.com.CitasMedicas.servicio;

import java.util.List;
import java.util.Optional;

import pe.com.CitasMedicas.model.EstadoCita;

public interface EstadoService {
    public void guardarEstado(EstadoCita estado);
    public void eliminarEstado(EstadoCita estado);
    public List<EstadoCita> listarEstados();
    public EstadoCita encontrarEstado(Long idestado);
    public Optional<EstadoCita> obtenerEstadoPorId(Long id);
}
