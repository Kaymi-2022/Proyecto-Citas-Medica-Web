package pe.com.CitasMedicas.servicio;


import java.util.List;

import pe.com.CitasMedicas.model.HistorialCitados;


public interface HistorialCitasService {

    public void guardar(HistorialCitados historialPaciente);

    public void eliminar(HistorialCitados historialPaciente);

    public List<HistorialCitados> listarHistorialPaciente();

    public HistorialCitados encontrarHistorialPaciente(HistorialCitados historialPaciente);
}
