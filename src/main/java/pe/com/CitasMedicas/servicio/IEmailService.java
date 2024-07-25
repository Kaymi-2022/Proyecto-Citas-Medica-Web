package pe.com.CitasMedicas.servicio;

import pe.com.CitasMedicas.model.CorreoRequest;

public interface IEmailService {
    void enviarCorreo(CorreoRequest correoRequest);
}
