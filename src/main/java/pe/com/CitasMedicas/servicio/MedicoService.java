package pe.com.CitasMedicas.servicio;


import java.util.List;

import pe.com.CitasMedicas.model.Medicos;

public interface MedicoService {
    public void guardar(Medicos medico);

    public void eliminar(Medicos medico);

    public List<Medicos> listarMedicos();

    public Medicos encontrarMedico(Medicos medico);
}
