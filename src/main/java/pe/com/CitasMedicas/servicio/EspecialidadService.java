package pe.com.CitasMedicas.servicio;


import java.util.List;
import java.util.Optional;

import pe.com.CitasMedicas.model.Especialidad;

public interface EspecialidadService {

    public void guardar(Especialidad especialidad);

    public void eliminar(Especialidad especialidad);

    public List<Especialidad> listarConsultorios();

    public Especialidad encontrarConsultorio(Especialidad especialidad);

    public Optional<Especialidad> obtenerConsultorioPorId(Long idConsultorio);
}
