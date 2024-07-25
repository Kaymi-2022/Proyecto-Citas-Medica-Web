package pe.com.CitasMedicas.servicio;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.CitasMedicas.model.Especialidad;
import pe.com.CitasMedicas.respository.EspecialidadDao;

import java.util.List;
import java.util.Optional;

@Service
public class EspecialidadServiceInpl implements EspecialidadService {
    
    @Autowired
    EspecialidadDao especialidadDao;

    @Override
    public void guardar(Especialidad especialidad) {
        especialidadDao.save(especialidad);
    }

    @Override
    public void eliminar(Especialidad especialidad) {
        especialidadDao.delete(especialidad);
    }

    @Override
    public List<Especialidad> listarConsultorios() {
        return especialidadDao.findAll();
    }

    @Override
    public Especialidad encontrarConsultorio(Especialidad especialidad) {
        return especialidadDao.findById(especialidad.getIdConsultorio()).orElse(null) ;
    }

    @Override
    public Optional<Especialidad> obtenerConsultorioPorId(Long idConsultorio) {
        return especialidadDao.findById(idConsultorio);
    }

    public Especialidad existeConsultorio(String nombre){
        return especialidadDao.findEspecialidadByNombre(nombre);
    }
}
